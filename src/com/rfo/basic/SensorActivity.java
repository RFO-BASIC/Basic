/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2016 Paul Laughton

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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;


// Log.d(SensorActivity.LOGTAG, " " + SensorActivity.CLASSTAG + " ignored Auto Run" );
public class SensorActivity implements SensorEventListener {
	private static final String LOGTAG = "SensorActivity";
	private static final String CLASSTAG = SensorActivity.class.getSimpleName();

	public static final int MaxSensors = 20;
	public static final int UNINIT = -1;
	private SensorManager mSensorManager;
	private boolean mIsOpen = false;
	private Sensor mActiveSensors[];
	private int mActiveSensorDelays[];
	private int mPendingSensorDelays[];
	private double mSensorValues[][];

	// This constructor is invoked from Run when the user executes
	// sensors.list or sensors.open

	public SensorActivity(Context context) {
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

		mActiveSensors = new Sensor[MaxSensors + 1];			// holds references to active sensors
		mActiveSensorDelays = new int[MaxSensors + 1];			// holds delay settings of active sensors
		mPendingSensorDelays = new int[MaxSensors + 1];			// holds delay settings of sensors the user wants to activate
		mSensorValues = new double[MaxSensors + 1][4];			// holds the current sensor values
		for (int i = 0; i <= MaxSensors; ++i) {
			mActiveSensors[i] = null;							// initialize the sensor references to null
			mActiveSensorDelays[i] =							// initialize the delay arrays to "uninitialized"
			mPendingSensorDelays[i] = UNINIT;
			for (int j = 0; j < 4; ++j) {
				mSensorValues[i][j]= 0;							// initialize the values array to zero
			}
		}
	}

	public ArrayList<String> takeCensus() {
		ArrayList<String> census = new ArrayList<String>();
		List<Sensor> sensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);		// Get the list of sensors
		String name;
		for (Sensor sensor : sensorList) {						// and iterate through it
			int type = sensor.getType();						// adding each sensor to SensorCensus list
//            	 Log.d(SensorActivity.LOGTAG, "Sensor list add: " + type );
			switch (type) {
			case Sensor.TYPE_ACCELEROMETER:       name = "Accelerometer";       break;
			case Sensor.TYPE_GRAVITY:             name = "Gravity";             break;
			case Sensor.TYPE_GYROSCOPE:           name = "Gyroscope";           break;
			case Sensor.TYPE_GYROSCOPE_UNCALIBRATED: name = "Uncalibrated Gyroscope"; break;
			case Sensor.TYPE_LIGHT:               name = "Light";               break;
			case Sensor.TYPE_MAGNETIC_FIELD:      name = "Magnetic Field";      break;
			case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED: name = "Uncalibrated Magnetic Field"; break;
			case Sensor.TYPE_ORIENTATION:         name = "Orientation";         break;
			case Sensor.TYPE_PRESSURE:            name = "Pressure";            break;
			case Sensor.TYPE_PROXIMITY:           name = "Proximity";           break;
			case Sensor.TYPE_LINEAR_ACCELERATION: name = "Linear Acceleration"; break;
//			case Sensor.TYPE_STEP_DETECTOR:       name = "Step Detector";       break;					KITKAT
//			case Sensor.TYPE_STEP_COUNTER:        name = "Step Counter";        break;					KITKAT
			case Sensor.TYPE_SIGNIFICANT_MOTION:  name = "Significant Motion";  break;
			case Sensor.TYPE_ROTATION_VECTOR:     name = "Rotational Vector";   break;
			case Sensor.TYPE_GAME_ROTATION_VECTOR:name = "Game Rotation Vector";break;
//			case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:name = "Geomagnetic Rotation Vector";break;	KITKAT
			case Sensor.TYPE_TEMPERATURE:         name = "Temperature";         break;
			case Sensor.TYPE_AMBIENT_TEMPERATURE: name = "Ambient Temperature"; break;
			case Sensor.TYPE_RELATIVE_HUMIDITY:   name = "Relative Humidity";   break;
			default:                              name = "Unknown";             break;
			}
			census.add(name + ", Type = " + type);
		}

		return census;
	}

	/* Call the markForOpen(int, int) method for each sensor the user wants to monitor.
	 * When done, call the doOpen() method to register the sensors the user requested.
	 */

	/* Record a sensor the user wants to monitor. Only record valid sensors
	 * that are not already open. If the delay value is invalid, use NORMAL.
	 * Return true if the sensor gets scheduled for open,
	 * false if the sensor type is invalid or the sensor is already registered.
	 */
	public boolean markForOpen(int type, int delay) {
		if ((type < 0) || (type >= MaxSensors)) { return false; }	// invalid
		if (mActiveSensors[type] != null) { return false; }			// already active

		switch (type) {
		case Sensor.TYPE_ACCELEROMETER:
		case Sensor.TYPE_MAGNETIC_FIELD:
		case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
		case Sensor.TYPE_ORIENTATION:
		case Sensor.TYPE_GYROSCOPE:
		case Sensor.TYPE_GYROSCOPE_UNCALIBRATED:
		case Sensor.TYPE_LIGHT:
		case Sensor.TYPE_PRESSURE:
		case Sensor.TYPE_TEMPERATURE:
		case Sensor.TYPE_PROXIMITY:
		case Sensor.TYPE_GRAVITY:
		case Sensor.TYPE_LINEAR_ACCELERATION:
		case Sensor.TYPE_SIGNIFICANT_MOTION:
		case Sensor.TYPE_ROTATION_VECTOR:
		case Sensor.TYPE_GAME_ROTATION_VECTOR:
		case Sensor.TYPE_RELATIVE_HUMIDITY:
		case Sensor.TYPE_AMBIENT_TEMPERATURE:
//		case Sensor.TYPE_STEP_DETECTOR:											KITKAT
//		case Sensor.TYPE_STEP_COUNTER:											KITKAT
//		case Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR:							KITKAT
			if ((delay < 0) || (delay > SensorManager.SENSOR_DELAY_NORMAL)) {
				delay = SensorManager.SENSOR_DELAY_NORMAL;
			}
			mPendingSensorDelays[type] = delay;					// List as "pending"
			return true;
		default:
			return false;
		}
	}

	public void doOpen() {										// register all sensors on the "pending" list
		for (int type = 0; type <= MaxSensors; ++type) {
			int delay = mPendingSensorDelays[type];
			if (delay == UNINIT) { continue; }					// Not "pending"

			Log.d(SensorActivity.LOGTAG, "Sensor register listener: " + type);
			Sensor sensor = mSensorManager.getDefaultSensor(type);
			mSensorManager.registerListener(this, sensor, delay);

			mActiveSensors[type] = sensor;						// remember the sensor
			mActiveSensorDelays[type] = delay;					// and its delay setting
			mPendingSensorDelays[type] = UNINIT;				// not "pending" any more
			mIsOpen = true;
		}
	}

	/* Register all of the sensors that have been previously opened.
	 * This method assumes they have all been unregistered.
	 * Intended to be called from the onResume() of an Activity.
	 */
	public void reOpen() {
		for (int type = 0; type <= MaxSensors; ++type) {
			Sensor sensor = mActiveSensors[type];
			if (sensor == null) { continue; }					// Not active

			int delay = mActiveSensorDelays[type];
			mSensorManager.registerListener(this, sensor, delay);
		}
	}

	public boolean isOpen() {									// Return true if any sensor is active
		return mIsOpen;
	}

	public synchronized double[] getValues(int type) {			// Return current values of one sensor
		double[] values = new double[4];
		for (int i = 1; i <= 3; ++i) {
			values[i] = mSensorValues[type][i];
		}
		return values;
	}

	public void stop() {										// Unregister all sensors
		mSensorManager.unregisterListener(this);
		mIsOpen = false;
//		Log.d(SensorActivity.LOGTAG, " " + SensorActivity.CLASSTAG + " unregister "  );
	}

	public synchronized void onSensorChanged(SensorEvent event) {

		// Called each time a sensor value has changed.

		// A sensor has changed. If its value is reliable,
		// copy the values in the parameter variables

		Sensor sensor = event.sensor;
		int type = sensor.getType();
/*		Log.d(SensorActivity.LOGTAG, "Sensor changed" + type + " Values = " +
				event.values[0] + "," +
				event.values[1] + "," +
				event.values[2] + "," );*/

/*		if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
			Log.d(SensorActivity.LOGTAG, "Sensor Unreliable, "  + type);
			return;
		}*/

		mSensorValues[type][1] = (double)event.values[0];
		mSensorValues[type][2] = (double)event.values[1];
		mSensorValues[type][3] = (double)event.values[2];
	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

}
 