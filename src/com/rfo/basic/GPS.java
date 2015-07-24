/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2015 Paul Laughton

This file is part of BASIC! for Android

    BASIC! is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BASIC! is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BASIC!.  If not, see <http://www.gnu.org/licenses/>.

    You may contact the author or current maintainers at http://rfobasic.freeforums.org

*************************************************************************************************/

package com.rfo.basic;

import java.util.HashMap;

import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.HandlerThread;
import android.util.Log;


// Implements the GPS Listener. Started by Run

public class GPS implements GpsStatus.Listener, LocationListener {

	private static final String LOGTAG = "GPS";

	private static final String STR_EVENT_STARTED = "Started";
	private static final String STR_EVENT_STOPPED = "Stopped";
	private static final String STR_EVENT_FIRST_FIX = "First Fix";
	private static final String STR_EVENT_STATUS_UPDATE = "Updated";

	public static final String KEY_PRN = "prn";
	private static final String KEY_AZIMUTH = "azimuth";
	private static final String KEY_ELEVATION = "elevation";
	private static final String KEY_SNR = "snr";
	private static final String KEY_INFIX = "infix";

	private double Altitude;
	private double Latitude;
	private double Longitude;
	private float Bearing;
	private float Accuracy;
	private float Speed;
	private long Time;
	private String Provider;
	private int LastStatusEvent;		// 0 means no event received yet
	private int SatelliteCount = -1;	// satellite count from LocationListener
	private Iterable<GpsSatellite> Satellites;
	private int SatellitesInView = -1;	// satellite count from GpsStatusListener
	private int SatellitesInFix = -1;	// also from GpsStatusListener
	public enum GpsData {
		ALTITUDE, LATITUDE, LONGITUDE, BEARING,
		ACCURACY, SPEED, TIME, PROVIDER, STATUS,
		SATELLITES, SATS_IN_VIEW, SATS_IN_FIX
	};

	private final Context mContext;
	private final long mMinTime;
	private final float mMinDistance;
	private LocationManager mLocator;
	private HandlerThread mThread;
	private boolean mLooperPrepared;

	public GPS(Context context, long minTime, float minDistance) {
		mContext = context;
		mMinTime = minTime;
		mMinDistance = minDistance;
		mThread = new LocationHandler("LocationListener");
		mThread.start();
		// Wait for onLooperPrepared to finish before letting the interpreter run.
		// Using getLooper() to wait for Looper does not wait long enough.
		synchronized (mThread) {
			while (!mLooperPrepared) {
				try { mThread.wait(); }
				catch (InterruptedException e) { break; }
			}
		}
	}

	private class LocationHandler extends HandlerThread {

		public LocationHandler(String name) {
			super(name);
		}

		@Override
		protected void onLooperPrepared() {
			mLocator = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
			startListener();
			mLooperPrepared = true;
			synchronized (mThread) {
				mThread.notify();
			}
		}
	}

	private void startListener() {
		// Start off by getting the last known location.

		Location location = null;
		try {
			location = mLocator.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null) {
				location = mLocator.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location == null)	Log.i(LOGTAG, "Unable to get initial location");
				else					Log.i(LOGTAG, "Initial location from Network Provider");
			}
			else						Log.i(LOGTAG, "Initial location from GPS Provider");
		} catch (IllegalArgumentException e) {
			Log.d(LOGTAG, "Exception trying to get initial location", e);
		}

		// Stuff the last known location parameters into the variables.

		onLocationChanged(location);

		// Start getting location change reports and status updates

		mLocator.requestLocationUpdates(LocationManager.GPS_PROVIDER, mMinTime, mMinDistance, this);
		mLocator.addGpsStatusListener(this);
	}

	public synchronized void onLocationChanged(Location location) {

		// Called when one the GPS parameters has changed
		// Stuff the GPS values into the parameters.

		if (location != null) {
			Altitude = location.getAltitude();
			Latitude = location.getLatitude();
			Longitude = location.getLongitude();
			Bearing = location.getBearing();
			Accuracy = location.getAccuracy();
			Speed = location.getSpeed();
			Provider = location.getProvider();
			Time = location.getTime();
			Bundle extras = location.getExtras();
			SatelliteCount = (extras != null) ? extras.getInt("satellites") : -1;
		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void onGpsStatusChanged(int event) {
		int inView = 0;
		int inFix = 0;
		if (event != GpsStatus.GPS_EVENT_STOPPED) {
			synchronized (this) {
				GpsStatus status = mLocator.getGpsStatus(null);
				Satellites = (status == null) ? null : status.getSatellites();
			}
			if (Satellites == null) {
				inFix = inView = -1;
			} else {
				for (GpsSatellite s : Satellites) {
					++inView;
					if (s.usedInFix()) { ++inFix; }
				}
			}
		}
		LastStatusEvent = event;
		SatellitesInView = inView;
		SatellitesInFix  = inFix;
	}

	public double getNumericValue(GpsData type) {
		switch (type) {
			case ALTITUDE:		return Altitude;
			case LATITUDE:		return Latitude;
			case LONGITUDE:		return Longitude;
			case BEARING:		return Bearing;
			case ACCURACY:		return Accuracy;
			case SPEED:			return Speed;
			case TIME:			return Time;
			case SATELLITES:	return SatelliteCount;
			case SATS_IN_VIEW:	return SatellitesInView;
			case SATS_IN_FIX:	return SatellitesInFix;
			case STATUS:		return LastStatusEvent;
			default:			break;
		}
		return 0.0;
	}

	public String getStringValue(GpsData type) {
		String result = null;
		switch (type) {
			case PROVIDER:		result = Provider;				break;
			case STATUS:
				switch (LastStatusEvent) {
					case GpsStatus.GPS_EVENT_STARTED:			return STR_EVENT_STARTED;
					case GpsStatus.GPS_EVENT_STOPPED:			return STR_EVENT_STOPPED;
					case GpsStatus.GPS_EVENT_FIRST_FIX:			return STR_EVENT_FIRST_FIX;
					case GpsStatus.GPS_EVENT_SATELLITE_STATUS:	return STR_EVENT_STATUS_UPDATE;
					default:	break;
				}
			break;
			default:			break;
		}
		return (result != null) ? result : "";
	}

	public HashMap<Double, Bundle> getSatellites() {
		HashMap<Double, Bundle> sats = new HashMap<Double, Bundle>();
		synchronized (this) {
			if (Satellites != null) {
				for (GpsSatellite s : Satellites) {
					Bundle b = new Bundle();
					double prn = (double)s.getPrn();
					b.putDouble(KEY_PRN, prn);
					b.putDouble(KEY_AZIMUTH, s.getAzimuth());
					b.putDouble(KEY_ELEVATION, s.getElevation());
					b.putDouble(KEY_SNR, s.getSnr());
					b.putDouble(KEY_INFIX, s.usedInFix() ? 1.0 : 0.0);
					sats.put(prn, b);						// put the satellite in the bundle by PRN
				}
			}
		}
		return sats;
	}

	public void stop() {
		Log.i(LOGTAG, "Unregistering LocationListener");
		if (mLocator != null) {
			mLocator.removeUpdates(this);
			mLocator.removeGpsStatusListener(this);
			mLocator = null;
		}
		if (mThread != null) {
			mThread.quit();
			mThread = null;
		}
	}
}
