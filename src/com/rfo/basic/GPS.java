/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.


Copyright (C) 2010 - 2013 Paul Laughton

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

    You may contact the author, Paul Laughton, at basic@laughton.com
    
	*************************************************************************************************/

package com.rfo.basic;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

// Implements the GPS Listener. Started by Run

public class GPS implements LocationListener {

	private static final String LOGTAG = "GPS";

	public static boolean GPSoff = true;
	public static double Altitude;
	public static double Latitude;
	public static double Longitude;
	public static float Bearing;
	public static float Accuracy;
	public static float Speed;
	public static long Time;
	public static String Provider = null;

	public LocationManager locator;

	public GPS(Context context) {
		locator = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		// Start off by getting the last known location.

		Location location = null;
		try {
			location = locator.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null) {
				location = locator.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location == null)	Log.i(LOGTAG, "Unable to get initial location");
				else					Log.i(LOGTAG, "Initial location from Network Provider");
			}
			else						Log.i(LOGTAG, "Initial location from GPS Provider");
		} catch (IllegalArgumentException e) {
			Log.d(LOGTAG, "Exception trying to get initial location", e);
		}

		// Stuff the last known location parameters into the variables.

		if (location != null) {
			Altitude = location.getAltitude();
			Latitude = location.getLatitude();
			Longitude = location.getLongitude();
			Bearing = location.getBearing();
			Accuracy = location.getAccuracy();
			Speed = location.getSpeed();
			Provider = location.getProvider();
			Time = location.getTime();
		}

		// This is the location listener

		LocationListener mLocationListener = this;

		// Start getting location change reports

		locator.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
	}

	public void onLocationChanged(Location location) {

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
		}
	}

	public void onProviderDisabled(String provider) {
	}

	public void onProviderEnabled(String provider) {
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	public void stop() {
		Log.i(LOGTAG, "Unregistering LocationListener");
		locator.removeUpdates(this);
	}
}