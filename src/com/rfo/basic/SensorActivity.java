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

import java.util.ArrayList;
import java.util.List;

import com.rfo.basic.Run;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;


// Log.d(SensorActivity.LOGTAG, " " + SensorActivity.CLASSTAG + " ignored Auto Run" );
public class SensorActivity extends Activity implements SensorEventListener {
    private static final String LOGTAG = "SensorActivity";
    private static final String CLASSTAG = SensorActivity.class.getSimpleName();

     private static SensorManager mSensorManager;
     private List<Sensor> sensorList;
     private static Sensor mAccel;
     private static Sensor mGravity;
     private static Sensor mGyro;
     private static Sensor mLight;
     private static Sensor mMagF;
     private static Sensor mCompass;
     private static Sensor mPressure;
     private static Sensor mProximity;
     private static Sensor mTemperature;
     private static Sensor mLinearAccl;
     private static Sensor mRotVector;
     private static Sensor mlAccel;
     private static Sensor mRelHumid;
     private static Sensor mAmbTemp;

     
//     private static Sensor mUnknown[5];
     
// This class is invoked from Run when the user executes
// sensors.open
     
     @Override
     public void onCreate(Bundle savedInstanceState) {
//    	 Log.d(SensorActivity.LOGTAG, " " + SensorActivity.CLASSTAG + " On Create" );
    	 super.onCreate(savedInstanceState);
    	 
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        
        // Set up the variables for the listeners
        // The listeners will be set in onResume
        
        mAccel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER );
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mGyro = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mMagF = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mCompass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        mPressure = mSensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mTemperature = mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        mRotVector = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        mLinearAccl = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION );
        mRelHumid= mSensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY );
        mAmbTemp= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE );
     }
     

     public boolean onKeyUp(int keyCode, KeyEvent event)  {
//       if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) && event.getRepeatCount() == 0) {
         if (keyCode == KeyEvent.KEYCODE_BACK  && event.getRepeatCount() == 0) {
         	if (Run.OnBackKeyLine != 0){
        		Run.BackKeyHit = true;
//            	Run.GRopen = false;
//            	finish();
        		return true;
        	}

//         	Run.GRopen = false;
        	Run.Stop = true;
        	
         	finish();
        	if (Basic.DoAutoRun) {
        		android.os.Process.killProcess(Basic.ProcessID) ;
        		android.os.Process.killProcess(android.os.Process.myPid()) ;
        	}

         }
         return true;
     }
     
     public boolean onTouchEvent(MotionEvent event){
     	super.onTouchEvent(event);
     	int action = event.getAction();  // Get action type


     	for (int i = 0; i < event.getPointerCount(); i++){
     		if ( i > 1 ) break;
         	int pid = event.getPointerId(i);
         	if (pid > 1) break;
//     		Log.d(GR.LOGTAG, " " + i + ","+pid+"," + action);
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
     	
     	return false;
     }


    protected void onResume() {
//         Log.d(SensorActivity.LOGTAG, " " + SensorActivity.CLASSTAG + " On Resume" );

/* This class can be called for both Sensors.list and Sensors.open
 * If the call is from sensors.list, get the list of sensors and
 * then quit.
 */
         int type;
         if (Run.GetSensorList) {
        	 Run.SensorCensus = new ArrayList<String>();
             sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);   // Get the list of sensors
             for (Sensor sensor : sensorList){							   // and iterate through it
            	 type =sensor.getType();								   // adding each sensor to SensorCensus list
//            	 Log.d(SensorActivity.LOGTAG, "Sensor list add: " + type );
            	 switch (type) {
            	 case Sensor.TYPE_ACCELEROMETER  :
            		 Run.SensorCensus.add("Acclerometer, Type = " + type);
            		 break;
            	 case Sensor.TYPE_GRAVITY:
            		 Run.SensorCensus.add("Gravity Type, =  " + type);
            		 break;
            	 case Sensor.TYPE_GYROSCOPE:
            		 Run.SensorCensus.add("Gyroscope, Type = " + type);
            		 break;
            	 case Sensor.TYPE_LIGHT:
            		 Run.SensorCensus.add("Light, Type = " + type);
            		 break;
            	 case Sensor.TYPE_MAGNETIC_FIELD:
            		 Run.SensorCensus.add("Magnetic Field, Type =  " + type);
            		 break;
            	 case Sensor.TYPE_ORIENTATION:
            		 Run.SensorCensus.add("Orientation, Type = " + type);
            		 break;
            	 case Sensor.TYPE_PRESSURE:
            		 Run.SensorCensus.add("Pressure, Type = " + type);
            		 break;
            	 case Sensor.TYPE_PROXIMITY:
            		 Run.SensorCensus.add("Proximity,  Type = " + type);
            		 break;
            	 case Sensor.TYPE_LINEAR_ACCELERATION:
            		 Run.SensorCensus.add("Linear Acceleration, Type = " + type);
            		 break;
            	 case Sensor.TYPE_ROTATION_VECTOR:
            		 Run.SensorCensus.add("Rotational Vector, Type = " + type);
            		 break;
            	 case Sensor.TYPE_AMBIENT_TEMPERATURE:
            		 Run.SensorCensus.add("Ambiant Temperature, Type = " + type);
            		 break;
            	 case Sensor.TYPE_RELATIVE_HUMIDITY:
            		 Run.SensorCensus.add("Relatve Humidity, Type = " + type);
            		 break;
            	 default:
            		 Run.SensorCensus.add("Unknown, Type = " + type);
            		 break;
            	 }
             }
             Run.GetSensorList = false;
             Run.SensorsClass = null;
             /* This is running in a separate thread from Run which called us.
              * SensorsRunning is used to signal that Sensors has done what it
              * needs to do to enable to Run to continue
              */
             Run.SensorsRunning = true; 
             // sensors.list is done now. Finished with this task. Close it.
             super.onResume();
             finish();
             return;
         }
         
/* Doing sensors.open. Register the listeners for each sensor that user wants
 * to monitor. Run.SensorOpenList is the user's list of sensors to monitor.
 */
         if (Run.SensorOpenList == null){
        	 Run.SensorsRunning = true;
             super.onResume();
        	 return;
         }
         
         for (int i = 0; i < Run.SensorOpenList.size(); ++i){
        	 type = Run.SensorOpenList.get(i);
        	 Log.d(SensorActivity.LOGTAG, "Sensor register listener: " + type);

        	 switch (type) {
        	 case Sensor.TYPE_ACCELEROMETER  :
                 mSensorManager.registerListener(this, mAccel, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_GRAVITY:
                 mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_GYROSCOPE:
                 mSensorManager.registerListener(this, mGyro, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_LIGHT:
                 mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_MAGNETIC_FIELD:
                 mSensorManager.registerListener(this, mMagF, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_ORIENTATION:
                 mSensorManager.registerListener(this, mCompass, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_ROTATION_VECTOR:
                 mSensorManager.registerListener(this, mRotVector, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_PRESSURE:
                 mSensorManager.registerListener(this, mPressure, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_PROXIMITY:
                 mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_TEMPERATURE:
                 mSensorManager.registerListener(this, mTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_LINEAR_ACCELERATION:
                 mSensorManager.registerListener(this, mLinearAccl, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_RELATIVE_HUMIDITY:
                 mSensorManager.registerListener(this, mRelHumid, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 case Sensor.TYPE_AMBIENT_TEMPERATURE:
                 mSensorManager.registerListener(this, mAmbTemp, SensorManager.SENSOR_DELAY_NORMAL);
        		 break;
        	 default:
        		 break;
        	 }

         }
         
         // Signal the Run thread that Sensors task is now operating
         
         Run.SensorsRunning = true;
         super.onResume();
        	 
     }
    
    public void onSensorChanged(SensorEvent event) {
    	
// Called each time a sensor value has changed. 
    	
    	/* If the user executed sensor.close or if the Run thread is stopping
    	 * it will signal this thread to stop by setting SensorsStop = true
    	 * In that case, this thread will shut down.
    	 */
    	
    	if (Run.SensorsStop){
    		finish();
    		return;
    	}
    	
    	// A sensor has changed. If its value is reliable,
    	// copy the values in the parameter variables
    	
    	Sensor sensor = event.sensor;
    	int type = sensor.getType();
/*      	 Log.d(SensorActivity.LOGTAG, "Sensor changed" + type + " Values = " +
      			event.values[0] + "," +
      			event.values[1] + "," +
      			event.values[2] + "," );*/

/*    	if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
    		Log.d(SensorActivity.LOGTAG, "Sensor Unreliable, "  + type);
    		return;
    	}*/

    	Run.SensorValues[type][1] = (double)event.values[0];
    	Run.SensorValues[type][2] = (double)event.values[1];
    	Run.SensorValues[type][3] = (double)event.values[2];
/*    	
   	 switch (type) {
   	 
	 case Sensor.TYPE_ACCELEROMETER :
		 Run.SensorValues[Sensor.TYPE_ACCELEROMETER][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_ACCELEROMETER][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_ACCELEROMETER][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_LINEAR_ACCELERATION :
		 Run.SensorValues[Sensor.TYPE_LINEAR_ACCELERATION][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_LINEAR_ACCELERATION][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_LINEAR_ACCELERATION][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_GRAVITY:
		 Run.SensorValues[Sensor.TYPE_GRAVITY][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_GRAVITY][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_GRAVITY][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_GYROSCOPE:
		 Run.SensorValues[Sensor.TYPE_GYROSCOPE][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_GYROSCOPE][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_GYROSCOPE][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_LIGHT:
		 Run.SensorValues[Sensor.TYPE_LIGHT][1]= (double)event.values[0];
		 break;
	 case Sensor.TYPE_MAGNETIC_FIELD:
		 Run.SensorValues[Sensor.TYPE_MAGNETIC_FIELD][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_MAGNETIC_FIELD][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_MAGNETIC_FIELD][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_ORIENTATION:
		 Run.SensorValues[Sensor.TYPE_ORIENTATION][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_ORIENTATION][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_ORIENTATION][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_ROTATION_VECTOR:
		 Run.SensorValues[Sensor.TYPE_ROTATION_VECTOR][1]= (double)event.values[0];
		 Run.SensorValues[Sensor.TYPE_ROTATION_VECTOR][2]= (double)event.values[1];
		 Run.SensorValues[Sensor.TYPE_ROTATION_VECTOR][3]= (double)event.values[2];
		 break;
	 case Sensor.TYPE_PRESSURE:
		 Run.SensorValues[Sensor.TYPE_PRESSURE][1]= (double)event.values[0];
		 break;
	 case Sensor.TYPE_PROXIMITY:
		 Run.SensorValues[Sensor.TYPE_PROXIMITY][1]= (double)event.values[0];
		 break;
	 case Sensor.TYPE_TEMPERATURE:
		 Run.SensorValues[Sensor.TYPE_TEMPERATURE][1]= (double)event.values[0];
		 break;
	 default:
		 break;
	 }
	 
*/
   	 Thread.yield();  // Gives Run a chance to do its thing
    	
    }

/*     protected void onPause() {
         super.onPause();
        if (!Run.SensorsRunning) return;
         
        int type;
         for (int i = 0; i < Run.SensorOpenList.size(); ++i){
        	 type = Run.SensorOpenList.get(i);
        	 Log.d(SensorActivity.LOGTAG, "Sensor unregister listener: " + type);

        	 switch (type) {
        	 case Sensor.TYPE_ACCELEROMETER  :
                 mSensorManager.unregisterListener(this, mAccel);
        		 break;
        	 case Sensor.TYPE_GRAVITY:
                 mSensorManager.unregisterListener(this, mGravity);
        		 break;
        	 case Sensor.TYPE_GYROSCOPE:
                 mSensorManager.unregisterListener(this, mGyro);
        		 break;
        	 case Sensor.TYPE_LIGHT:
                 mSensorManager.unregisterListener(this, mLight);
        		 break;
        	 case Sensor.TYPE_MAGNETIC_FIELD:
                 mSensorManager.unregisterListener(this, mMagF);
        		 break;
        	 case Sensor.TYPE_ORIENTATION:
                 mSensorManager.unregisterListener(this, mCompass);
        		 break;
        	 case Sensor.TYPE_ROTATION_VECTOR:
                 mSensorManager.unregisterListener(this, mRotVector);
        		 break;
        	 case Sensor.TYPE_PRESSURE:
                 mSensorManager.unregisterListener(this, mPressure);
        		 break;
        	 case Sensor.TYPE_PROXIMITY:
                 mSensorManager.unregisterListener(this, mProximity);
        		 break;
        	 case Sensor.TYPE_TEMPERATURE:
                 mSensorManager.unregisterListener(this, mTemperature);
        		 break;
        	 case Sensor.TYPE_LINEAR_ACCELERATION:
                 mSensorManager.unregisterListener(this, mLinearAccl);
        		 break;
        	 case Sensor.TYPE_RELATIVE_HUMIDITY:
                 mSensorManager.unregisterListener(this, mRelHumid);
        		 break;
        	 case Sensor.TYPE_AMBIENT_TEMPERATURE:
                 mSensorManager.unregisterListener(this, mAmbTemp);
        		 break;
        	 default:
        		 break;
        	 }

         }
     }*/
     
     protected void onDestroy() {
    	 mSensorManager.unregisterListener(this);
    	 super.onDestroy();
//         Log.d(SensorActivity.LOGTAG, " " + SensorActivity.CLASSTAG + " On destroy "  );

     }
     

     public void onAccuracyChanged(Sensor sensor, int accuracy) {
    	
     }

 }
 