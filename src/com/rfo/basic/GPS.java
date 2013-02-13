/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.


Copyright (C) 2010, 2011, 2012 Paul Laughton

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

import com.rfo.basic.Run;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

// Implements the GPS Listener. Started by Run



public class GPS extends Activity implements LocationListener{
	
    private static final String LOGTAG = "GPS";
    private static final String CLASSTAG = GPS.class.getSimpleName();
 // Log.v(GPS.LOGTAG, " " + GPS.CLASSTAG + "  " );
	
	public static boolean GPSoff = true;
	public static double Altitude;
	public static double Latitude;
	public static double Longitude;
	public static float Bearing;
	public static float Accuracy ;
	public static float Speed;
	public static long Time;
	public static String Provider = null;
	public static LocationManager locator;
	
	
	public void onCreate(Bundle savedInstanceState) {
   	 super.onCreate(savedInstanceState);

		locator = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Start off by getting the last know location.
		
		Location location = null;
		try {
			location = locator.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (location == null) {
				location = locator.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}
		}
		catch (IllegalArgumentException e){}
		
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
		
		locator.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
		
		// Signal to Run that GPS is up and running. Since Run and this class run
		// in separate threads, this signal is required. Run will not proceed
		// until it see GPSrunning true;
		
		Run.GPSrunning = true;

		super.onCreate(savedInstanceState);
	}
	
    public boolean onKeyUp(int keyCode, KeyEvent event)  {
//      if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) && event.getRepeatCount() == 0) {
        if (keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
        	if (Run.OnBackKeyLine != 0){
        		Run.BackKeyHit = true;
//            	Run.GRopen = false;
//            	finish();
        		return true;
        	}

//        	Run.GRopen = false;
        	Run.Stop = true;
        	finish();
        	if (Basic.DoAutoRun) {
//        		android.os.Process.killProcess(Basic.ProcessID) ;
                android.os.Process.killProcess(android.os.Process.myPid()) ;

        	}
        	return true;
        }
 	   return super.onKeyUp(keyCode, event);

    }
    
    public boolean onTouchEvent(MotionEvent event){
    	super.onTouchEvent(event);
    	int action = event.getAction();  // Get action type


    	for (int i = 0; i < event.getPointerCount(); i++){
    		if ( i > 1 ) break;
        	int pid = event.getPointerId(i);
        	if (pid > 1) break;
//    		Log.v(GR.LOGTAG, " " + i + ","+pid+"," + action);
    		Run.TouchX[pid] = (double)event.getX(i);
    		Run.TouchY[pid] = (double)event.getY(i);
    		if (action == MotionEvent.ACTION_DOWN ||
    			action == MotionEvent.ACTION_POINTER_DOWN ||
    			action == MotionEvent.ACTION_MOVE){
        		Run.NewTouch[pid] = true;
        	} else if (action == MotionEvent.ACTION_UP || 
        		action == MotionEvent.ACTION_POINTER_UP ||
        		action == 6 ||
        		action == 262) {
        		Run.NewTouch[pid] = false;
        	}
    	}
    	
    	return true;
    }


	
	public void onLocationChanged(Location location) {
		
//  Called when one the GPS parameters has changed
//  Stuff the GPS values into the parameters.
		
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

//  When the Run threads are quitting or the user has closed
//  GPS, it signals with GPSoff = true.
//  Stop getting updates and destroy this task with finish()
	    
	    if (Run.GPSoff) {
	    	Run.theGPS = null;
	    	locator.removeUpdates(this);
	    	finish();
	    }

	}

	public void onProviderDisabled(String provider) {
	}
	
	public void onProviderEnabled(String provider) {
	}
	
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
    protected void onPause() {
        super.onPause();
//        Log.v(GPS.LOGTAG, " " + GPS.CLASSTAG + " On Pause " );

    }
    
    protected void onDestroy() {
        super.onDestroy();
        Run.theGPS = null;
//        Log.v(GPS.LOGTAG, " " + GPS.CLASSTAG + " On Destroy " );

    }
    
    protected void onResume() {
        super.onResume();
//        Log.v(GPS.LOGTAG, " " + GPS.CLASSTAG + " On Resume " );

    }


	




	
}