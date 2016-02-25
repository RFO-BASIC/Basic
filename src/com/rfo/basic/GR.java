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

import static com.rfo.basic.Run.EventHolder.*;

import java.util.ArrayList;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Region;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;
import android.graphics.RectF;
import android.graphics.Bitmap;


public class GR extends Activity {
	private static final String LOGTAG = "GR";

	public static final String EXTRA_SHOW_STATUSBAR = "statusbar";
	public static final String EXTRA_ORIENTATION = "orientation";
	public static final String EXTRA_BACKGROUND_COLOR = "background";

	public static Object LOCK = new Object();
	public static boolean waitForLock = false;

	public static Context context;
	public static DrawView drawView;
	public static Bitmap screenBitmap = null;
//	public static boolean NewTouch[] = {false, false};
//	public static double TouchX[] = {0,0};
//	public static double TouchY[] = {0,0};
	public static float scaleX = 1f;
	public static float scaleY = 1f;
	public static boolean Running = false;				// flag set when Open object runs
	private boolean mCreated = false;					// flat set when onCreate is complete
	public static boolean NullBitMap = false;
	public static float Brightness = -1;

	public static boolean doSTT = false;
	public static boolean doEnableBT = false;
	public static boolean startConnectBT = false;

	public enum VISIBLE { SHOW, HIDE, TOGGLE; }

	// ************* enumeration of BASIC! Drawable Object types **************

	public enum Type {
		Null("null",				new String[0]),
		Point("point",				new String[]
				{ "x", "y" } ),
		Line("line",				new String[]
				{ "x1", "y1", "x2", "y2" } ),
		Rect("rect",				new String[]
				{ "left", "right", "top", "bottom" } ),
		Circle("circle",			new String[]
				{ "x", "y", "radius" } ),
		Oval("oval",				new String[]
				{ "left", "right", "top", "bottom" } ),
		Arc("arc",					new String[]
				{ "left", "right", "top", "bottom",
					"start_angle", "sweep_angle", "fill_mode" } ),
		Poly("poly",				new String[]
				{ "x", "y", "list" } ),
		Bitmap("bitmap",			new String[]
				{ "x", "y", "bitmap" } ),
		SetPixels("set.pixels",		new String[]
				{ "x", "y" } ),
		Text("text",				new String[]
				{ "x", "y", "text" } ),
		Group("group",				new String[]
				{ "list" } ),
		RotateStart("rotate.start",	new String[]
				{ "x", "y", "angle" } ),
		RotateEnd("rotate.end",		new String[0]),
		Clip("clip",				new String[]
				{ "left", "right", "top", "bottom", "RO" } ),
		Open("open",				new String[0]),
		Close("close",				new String[0]);

		private final String mType;
		private final String[] mParameters;		// all parameters except "paint" and "alpha"
		Type(String type, String[] parameters) {
			mType = type;
			mParameters = parameters;
		}

		public String type()			{ return mType; }
		public String[] parameters()	{ return mParameters; }

		public boolean hasParameter(String parameter) {
			for (String p : mParameters) {
				if (parameter.equals(p)) { return true; }
			}
			if (parameter.equals("paint")) { return true; }
			if (parameter.equals("alpha")) { return true; }
			return false;
		}
	}

	// ********************* BASIC! Drawable Object class *********************
	// Objects go on the Display List. Not related to Android's Drawable class.

	public static class BDraw {
		private final Type mType;
		private String mErrorMsg;

		private int mBitmap;							// index into the BitmapList
		private int mPaint;								// index into the PaintList
		private int mAlpha;
		private boolean mVisible;

		private String mText;							// for Type.Text
		private int mClipOpIndex;						// for getValue
		private Region.Op mClipOp;						// for Type.Clip
		private int mListIndex;
		private ArrayList<Double> mList;
		private Var.ArrayDef mArray;					// for Type.SetPixels
		private int mArrayStart;						// position in array to start pixel array
		private int mArraySublength;					// length of array segment to use as pixel array
		private int mRadius;							// for Type.Circle
		private float mAngle_1;							// for Type.Rotate, Arc
		private float mAngle_2;							// for Type.Arc
		private int mFillMode;							// for getValue
		private boolean mUseCenter;						// for Type.Arc

		private int mLeft;								// left, x, or x1
		private int mRight;								// right or x2
		private int mTop;								// top, y, or y1
		private int mBottom;							// bottom or y2

		public BDraw(Type type) {
			mType = type;
			mVisible = true;
			mErrorMsg = "";
		}

		public void common(int paintIndex, int alpha) { mPaint = paintIndex; mAlpha = alpha; }

		// setters
		// For now, range checking for bitmap, paint, and list must be done in Run.
		public void bitmap(int bitmap) { mBitmap = bitmap; }	// index into the BitmapList
		public void paint(int paint) { mPaint = paint; }		// index into the PaintList
		public void alpha(int alpha) { mAlpha = alpha & 255; }

		public void xy(int[] xy) { mLeft = mRight = xy[0]; mTop = mBottom = xy[1]; }
		public void ltrb(int[] ltrb) { mLeft = ltrb[0]; mTop = ltrb[1]; mRight = ltrb[2]; mBottom = ltrb[3]; }
		public void radius(int radius) { mRadius = radius; }
		public void text(String text) { mText = text; }
		public void array(Var.ArrayDef array, int start, int sublength) {
			mArray = array;
			mArrayStart = start;
			mArraySublength = sublength;
		}
		public void angle(float angle) { mAngle_1 = angle; }

		public void show(VISIBLE show) {
			switch (show) {
				case SHOW: mVisible = true; break;
				case HIDE: mVisible = false; break;
				case TOGGLE: mVisible = !mVisible; break;
			}
		}

		public void clipOp(int opIndex) {
			Region.Op[] ops = {
				Region.Op.INTERSECT, Region.Op.DIFFERENCE, Region.Op.REPLACE,
				Region.Op.REVERSE_DIFFERENCE, Region.Op.UNION, Region.Op.XOR
			};
			mClipOpIndex = opIndex;
			mClipOp = ops[opIndex];
		}

		public void list(int index, ArrayList<Double> list) {
			mListIndex = index;
			mList = list;
		}

		public void useCenter(int fillMode) {
			mFillMode = fillMode;
			mUseCenter = (fillMode != 0);
		}

		public void arc(int[] ltrb, float startAngle, float sweepAngle, int fillMode) {
			ltrb(ltrb);
			mAngle_1 = startAngle;
			mAngle_2 = sweepAngle;
			useCenter(fillMode);
		}

		public void circle(int[] xy, int radius) {
			xy(xy);					// (x, y) in mTop and mLeft marks the CENTER of the circle
			mRadius = radius;
		}

		public void move(int[] dxdy) {
			int dx = dxdy[0];
			int dy = dxdy[1];
			mLeft += dx; mRight += dx;
			mTop += dy; mBottom += dy;
		}

		// universal getters
		public Type type()			{ return mType; }
		public String errorMsg()	{ return mErrorMsg; }
		public int bitmap()			{ return mBitmap; }	// index into the BitmapList
		public int paint()			{ return mPaint; }	// index into the PaintList
		public int alpha()			{ return mAlpha; }
		public boolean isHidden()	{ return !mVisible; }
		public boolean isVisible()	{ return mVisible; }

		// type-specific getters
		public String text()		{ return mText; }
		public Region.Op clipOp()	{ return mClipOp; }
		public ArrayList<Double> list() {
			if (mList == null) { mList = new ArrayList<Double>(); }
			return mList;
		}
		public Var.ArrayDef array()	{ return mArray; }
		public int arrayStart()		{ return mArrayStart; }
		public int arraySublength()	{ return mArraySublength; }
		public int radius()			{ return mRadius; }
		public float angle()		{ return mAngle_1; }
		public float arcStart()		{ return mAngle_1; }
		public float arcSweep()		{ return mAngle_2; }
		public boolean useCenter()	{ return mUseCenter; }

		// coordinate getters
		public int x()				{ return mLeft; }
		public int x1()				{ return mLeft; }
		public int left()			{ return mLeft; }
		public int y()				{ return mTop; }
		public int y1()				{ return mTop; }
		public int top()			{ return mTop; }
		public int x2() 			{ return mRight; }
		public int right()			{ return mRight; }
		public int y2()				{ return mBottom; }
		public int bottom()			{ return mBottom; }

		// For GR.Get.Value
		public double getValue(String p) {
			if (p.equals("paint"))	{ return mPaint; }
			if (p.equals("alpha"))	{ return mAlpha; }

			switch (mType) {
				case Circle:	if (p.equals("radius")) { return mRadius; }
				case Point:
				case SetPixels:
				case Text:	// For now, "text" must be handled by Run
					if (p.equals("x"))				{ return mLeft; }
					if (p.equals("y"))				{ return mTop; }
					break;
				case Line:
					if (p.equals("x1"))				{ return mLeft; }
					if (p.equals("y1"))				{ return mTop; }
					if (p.equals("x2"))				{ return mRight; }
					if (p.equals("y2"))				{ return mBottom; }
					break;
				case Clip:		if (p.equals("RO")) { return mClipOpIndex; }
				case Oval:
				case Rect:
					if (p.equals("left"))			{ return mLeft; }
					if (p.equals("top"))			{ return mTop; }
					if (p.equals("right"))			{ return mRight; }
					if (p.equals("bottom"))			{ return mBottom; }
					break;
				case Arc:
					if (p.equals("start_angle"))	{ return mAngle_1; }
					if (p.equals("sweep_angle"))	{ return mAngle_2; }
					if (p.equals("fill_mode"))		{ return mFillMode; }
					if (p.equals("left"))			{ return mLeft; }
					if (p.equals("top"))			{ return mTop; }
					if (p.equals("right"))			{ return mRight; }
					if (p.equals("bottom"))			{ return mBottom; }
					break;
				case Poly:
					if (p.equals("x"))				{ return mLeft; }
					if (p.equals("y"))				{ return mTop; }
				case Group:
					if (p.equals("list"))			{ return mListIndex; }
					break;
				case Bitmap:
					if (p.equals("bitmap"))			{ return mBitmap; }
					if (p.equals("x"))				{ return mLeft; }
					if (p.equals("y"))				{ return mTop; }
					break;
				case RotateStart:
					if (p.equals("angle"))			{ return mAngle_1; }
					if (p.equals("x"))				{ return mLeft; }
					if (p.equals("y"))				{ return mTop; }
					break;
				case Close:
				case Null:
				case RotateEnd:
				default:							break;
			}
			return 0.0;
		} // getValue(String)

		// For GR.Modify
		private boolean mod_xy(String p, int val) {
			if (p.equals("x"))		{ mLeft = mRight  = val; return true; }
			if (p.equals("y"))		{ mTop  = mBottom = val; return true; }
			return false;
		}
		private boolean mod_x2y2(String p, int val) {
			if (p.equals("x1"))		{ mLeft   = val; return true; }
			if (p.equals("y1"))		{ mTop    = val; return true; }
			if (p.equals("x2"))		{ mRight  = val; return true; }
			if (p.equals("y2"))		{ mBottom = val; return true; }
			return false;
		}
		private boolean mod_ltrb(String p, int val) {
			if (p.equals("left"))	{ mLeft   = val; return true; }
			if (p.equals("top"))	{ mTop    = val; return true; }
			if (p.equals("right"))	{ mRight  = val; return true; }
			if (p.equals("bottom"))	{ mBottom = val; return true; }
			return false;
		}
		public boolean modify(String p, int iVal, float fVal, String text) {
			// For now, "paint" is handled in Run because of range check.
			// if (p.equals("paint"))	{ mPaint = iVal; return true; }
			if (p.equals("alpha"))	{ alpha(iVal); return true; }
			switch (mType) {
				case Circle:	if (p.equals("radius"))	{ mRadius = iVal; return true; }
				case Bitmap:	// for now, BitmapList range check must be handled in Run
				case Point:
				case Poly:		// for now, list parm must be handled in Run
				case SetPixels:	if (mod_xy(p, iVal))	{ return true; } else { break; }

				case Line:		if (mod_x2y2(p, iVal))	{ return true; } else { break; }

				case Clip:		if (p.equals("RO"))		{ clipOp(iVal); return true; }
				case Oval:
				case Rect:		if (mod_ltrb(p, iVal))	{ return true; } else { break; }

				case Arc:
					if (p.equals("start_angle"))	{ mAngle_1 = fVal; return true; }
					if (p.equals("sweep_angle"))	{ mAngle_2 = fVal; return true; }
					if (p.equals("fill_mode"))		{ useCenter(iVal); return true; }
					if (mod_ltrb(p, iVal))			{ return true; }
					break;

				case RotateStart:
					if (p.equals("angle"))			{ mAngle_1 = fVal; return true; }
					if (mod_xy(p, iVal))			{ return true; }
					break;

				case Text:
					if (p.equals("text"))			{ mText = text; return true; }
					if (mod_xy(p, iVal))			{ return true; }
					break;

				case Close:
				case Group:		// for now, list parm must be handled in Run
				case Null:
				case RotateEnd:
				default:							break;
			}
			mErrorMsg = "Object does not contain: " + p;
			return false;
		} // modify(String, int, float, String)
	} // BDraw class

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.v(LOGTAG, "onCreate");
		super.onCreate(savedInstanceState);
		ContextManager cm = Basic.getContextManager();
		cm.registerContext(ContextManager.ACTIVITY_GR, this);
		cm.setCurrent(ContextManager.ACTIVITY_GR);

		Intent intent = getIntent();
		int showStatusBar = intent.getIntExtra(EXTRA_SHOW_STATUSBAR, 0);
		int orientation = intent.getIntExtra(EXTRA_ORIENTATION, -1);
		int backgroundColor = intent.getIntExtra(EXTRA_BACKGROUND_COLOR, 0xFF000000);

		setOrientation(orientation);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		showStatusBar = (showStatusBar == 0)
				? WindowManager.LayoutParams.FLAG_FULLSCREEN			// do not show status bar
				: WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;	// show status bar
		getWindow().setFlags(showStatusBar, showStatusBar);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		scaleX = 1.0f;
		scaleY = 1.0f;
		Brightness = -1;

		drawView = new DrawView(this);
		setContentView(drawView);
		drawView.requestFocus();
		drawView.setBackgroundColor(backgroundColor);
		drawView.setId(33);

		synchronized (drawView) {
			mCreated = true;
			condReleaseLOCK();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(LOGTAG, "onStart");
	}

	@Override
	protected void onResume() {
		Log.v(LOGTAG, "onResume " + this.toString());
		if (context != this) {
			Log.d(LOGTAG, "Context changed from " + context + " to " + this);
			context = this;
		}
		Basic.getContextManager().onResume(ContextManager.ACTIVITY_GR);
		Run.mEventList.add(new Run.EventHolder(GR_STATE, ON_RESUME, null));
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.v(LOGTAG, "onPause " + this.toString());
		Basic.getContextManager().onPause(ContextManager.ACTIVITY_GR);
		Run.mEventList.add(new Run.EventHolder(GR_STATE, ON_PAUSE, null));
		if (drawView.mKB != null) { drawView.mKB.forceHide(); }
		super.onPause();
	}

	protected void onStop() {
		Log.v(LOGTAG, "onStop");
		super.onStop();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v(LOGTAG, "onRestart");
	}

	@Override
	public void finish() {
		// Tell the ContextManager we're done, if it doesn't already know.
		Basic.getContextManager().unregisterContext(ContextManager.ACTIVITY_GR, this);
		super.finish();
	}

	@Override
	protected void onDestroy() {
		Log.v(LOGTAG, "onDestroy " + this.toString());
		// if a new instance has started, don't let this one mess it up
		if (context == this) {
			Running = mCreated = false;
			context = null;
			releaseLOCK();								// don't leave GR.command hanging
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		Run.mEventList.add(new Run.EventHolder(GR_BACK_KEY_PRESSED, 0, null));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Log.v(LOGTAG, "keyDown " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return super.onKeyDown(keyCode, event);
		}
		if (!Run.mBlockVolKeys && (
				(keyCode == KeyEvent.KEYCODE_VOLUME_UP)   ||
				(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) ||
				(keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) ||
				(keyCode == KeyEvent.KEYCODE_MUTE)        ||
				(keyCode == KeyEvent.KEYCODE_HEADSETHOOK) ))
		{
			return super.onKeyDown(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Do not put the KeyEvent on the EventList. This keeps Run.onKeyDown() from building a menu.
			Run.mEventList.add(new Run.EventHolder(KEY_DOWN, keyCode, null));
		}
		return true;									// ignore anything else
	}

	public boolean onKeyUp(int keyCode, KeyEvent event)  {						// The user hit a key
		// Log.v(LOGTAG, "keyUp " + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return super.onKeyUp(keyCode, event);
		}
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			// Do not put the KeyEvent on the EventList. This keeps Run.onKeyDown() from building a menu.
			event = null;
		}
		Run.mEventList.add(new Run.EventHolder(KEY_UP, keyCode, event));
		return true;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
        case Run.REQUEST_CONNECT_DEVICE_SECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, true);
            }
            break;
        case Run.REQUEST_CONNECT_DEVICE_INSECURE:
            // When DeviceListActivity returns with a device to connect
            if (resultCode == Activity.RESULT_OK) {
                connectDevice(data, Run.bt_Secure);
            }
            break;
        case Run.REQUEST_ENABLE_BT:
            // When the request to enable Bluetooth returns
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth is now enabled, so set up a chat session
            	Run.bt_enabled = 1;
            } else {
                Run.bt_enabled = -1;
            }
            break;

		case Run.VOICE_RECOGNITION_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Run.sttResults = new ArrayList<String>();
				Run.sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			}
			Run.sttDone = true;
		}
	}

	private void condReleaseLOCK() {					// conditionally release LOCK
		if (mCreated & Running) { releaseLOCK(); }
	}

	private void releaseLOCK() {						// unconditionally release LOCK
		if (waitForLock) {
			synchronized (LOCK) {
				waitForLock = false;
				LOCK.notify();							// release GR.OPEN or .CLOSE if it is waiting
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public void setOrientation(int orientation) {		// Convert and apply orientation setting
		Log.v(LOGTAG, "Set orientation " + orientation);
		switch (orientation) {
			default:
			case 1:  orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; break;
			case 3:  orientation = (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
								 ? ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
								 : ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			case 0:  orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; break;
			case 2:  orientation = (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD)
								 ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
								 : ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			case -1: orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR; break;
		}
		setRequestedOrientation(orientation);
	}

    public void connectDevice(Intent data, boolean secure) {

        String address = data.getExtras()
            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        Run.btConnectDevice = null;
        try {
        	Run.btConnectDevice = Run.mBluetoothAdapter.getRemoteDevice(address);
	        if ( Run.btConnectDevice != null) Run.mChatService.connect(Run.btConnectDevice, secure);
        }
        catch (Exception e){ 
//        	RunTimeError("Connect error: " + e);
        }
        // Attempt to connect to the device
    }

    public void startsBTConnect() {
    	Intent serverIntent = null;
        serverIntent = new Intent(this, DeviceListActivity.class);
        if (serverIntent != null){
        	if (Run.bt_Secure) {
        		startActivityForResult(serverIntent, Run.REQUEST_CONNECT_DEVICE_SECURE);
        	}else {
        		startActivityForResult(serverIntent, Run.REQUEST_CONNECT_DEVICE_INSECURE);
        	}
        }

    }

    public void enableBT() {
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, Run.REQUEST_ENABLE_BT);
    }

	public class DrawView extends View {
		private static final String LOGTAG = "GR.DrawView";

		public KeyboardManager mKB;

		@SuppressLint("NewApi")
		public DrawView(Context context) {
			super(context);
			setFocusable(true);
			setFocusableInTouchMode(true);
			mKB = new KeyboardManager(context, this, new KeyboardManager.KeyboardChangeListener() {
				public void kbChanged() {
					Run.mEventList.add(new Run.EventHolder(GR_KB_CHANGED, 0, null));
				}
			});

			if (Build.VERSION.SDK_INT >= 11) {				// Hardware acceleration is supported starting API 11
				// Assume hardware acceleration is enabled for the app.
				// Choose whether to use it in DrawView based on user Preference.
				int layerType = Settings.getGraphicAcceleration(context)
								? View.LAYER_TYPE_HARDWARE	// use hardware acceleration
								: View.LAYER_TYPE_SOFTWARE;	// disable hardware acceleration
				setLayerType(layerType, null);
			}
		}

		synchronized public void setOrientation(int orientation) {	// synchronized orientation change
			Log.v(LOGTAG, "Set orientation " + orientation);
			GR.this.setOrientation(orientation);
		}

		@Override
		public boolean onKeyPreIme(int keyCode, KeyEvent event) {
			return (mKB != null) && mKB.onKeyPreIme(keyCode, event); // delegate to KeyboardManager
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		public int getWindowMetrics(Point outSize) {	// return size in Point, density as return value
			// This can be called when the DrawView does not yet know what size it is,
			// so get the size from the WindowManager.
			Display display = getWindowManager().getDefaultDisplay();
			if (Build.VERSION.SDK_INT < 13) {
				outSize.set(display.getWidth(), display.getHeight());
			} else {
				display.getSize(outSize);
			}
			DisplayMetrics dm = new DisplayMetrics();
			display.getMetrics(dm);
			return dm.densityDpi;
		}

		public boolean onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			int action = event.getAction() & MotionEvent.ACTION_MASK;	// Get action type, mask off index field
			int numPointers = event.getPointerCount();

			for (int i = 0; i < numPointers; i++) {
				int pid = event.getPointerId(i);
				if (pid > 1)  { continue; }				// currently, we allow only two pointers

				Run.TouchX[pid] = (double)event.getX(i);
				Run.TouchY[pid] = (double)event.getY(i);
				if (action == MotionEvent.ACTION_DOWN ||
					action == MotionEvent.ACTION_POINTER_DOWN) {
					Run.NewTouch[pid] = true;			// which pointer (0 or 1), cleared on UP
					Run.mEventList.add(new Run.EventHolder(GR_TOUCH, 0, null));
				}
				else if	(action == MotionEvent.ACTION_MOVE) {
					Run.NewTouch[pid] = true;
				} else if (action == MotionEvent.ACTION_UP ||
					action == MotionEvent.ACTION_POINTER_UP) {
					Run.NewTouch[pid] = false;
				}
			}
			return true;
		}

		public Paint newPaint(Paint fromPaint) {
			Typeface tf = fromPaint.getTypeface();
			Paint rPaint = new Paint(fromPaint);
			rPaint.setTypeface(tf);
			return rPaint;
		}

		@Override
		synchronized public void onDraw(Canvas canvas) {
			if (doEnableBT) {							// If this activity is running
				enableBT();								// Bluetooth must be enabled here
				doEnableBT = false;
			}

			if (startConnectBT) {
				startsBTConnect();
				startConnectBT = false;
			}

			if (doSTT) {
				Intent intent = Run.buildVoiceRecognitionIntent();
				startActivityForResult(intent, Run.VOICE_RECOGNITION_REQUEST_CODE);
				doSTT = false;
			}

			if ((Run.RealDisplayList == null) || (Run.DisplayList == null)) {
				Log.e(LOGTAG, "GR.onDraw: null DisplayList");
				finish();								// lost context, bail out
				return;
			}

			// float scale = getResources().getDisplayMetrics().density;
			drawView.setDrawingCacheEnabled(true);
			canvas.scale(scaleX, scaleY);

			if (Brightness != -1) {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.screenBrightness = Brightness;
				getWindow().setAttributes(lp);
				Brightness = -1;						// do it only once
			}

			synchronized (Run.DisplayList) {
				for (int di : Run.RealDisplayList) {
					if (di >= Run.DisplayList.size()) continue;
					BDraw b = Run.DisplayList.get(di);
					if (!doDraw(canvas, b)) {
						finish();
						break;
					}
				}
				condReleaseLOCK();
			}
		} // onDraw()

		public boolean doDraw(Canvas canvas, BDraw b) {
			float fx1;
			float fy1;
			RectF rectf;

			Paint thePaint;
			Bitmap theBitmap;
//			canvas.scale(scaleX, scaleY);

			thePaint = null;
			Type type = Type.Null;
			if ((b != null) && b.isVisible()) {
				type = b.type();
				int pIndex = b.paint();
				if (Run.PaintList.size() == 0)						return true;
				if (pIndex < 1 || pIndex >= Run.PaintList.size())	return true;

				thePaint = Run.PaintList.get(pIndex);
				int alpha = b.alpha();
				if ((alpha < 256) && (alpha != thePaint.getAlpha())) {
					thePaint = newPaint(thePaint);
					thePaint.setAlpha(alpha);
				}
			}

			switch (type) {
				case Group:
				case Null:
					break;
				case Open:
					Running = true;						// flag for GR.Open
					break;
				case Close:
					Running = false;
					return false;

				case Circle:
					canvas.drawCircle(b.x(),b.y(),b.radius(), thePaint);
					break;
				case Rect:
					canvas.drawRect(b.left(), b.top(), b.right(), b.bottom(), thePaint);
					break;
				case Clip:
					canvas.clipRect(b.left(), b.top(), b.right(), b.bottom(), b.clipOp());
					break;
				case Oval:
					rectf = new RectF(b.left(), b.top(), b.right(), b.bottom());
					canvas.drawOval(rectf, thePaint);
					break;
				case Arc:
					rectf = new RectF(b.left(), b.top(), b.right(), b.bottom());
					canvas.drawArc(rectf, b.arcStart(), b.arcSweep(), b.useCenter(), thePaint);
					break;
				case Line:
					canvas.drawLine(b.x1(),b.y1(),b.x2(),b.y2(), thePaint);
					break;
				case Point:
					canvas.drawPoint(b.x(),b.y(), thePaint);
					break;
				case SetPixels:
					fx1 = b.x();
					fy1 = b.y();
					Var.ArrayDef array = b.array();
					int pBase = b.arrayStart();
					int pLength = b.arraySublength();
					float[] pixels = new float[pLength];
					for (int j = 0; j < pLength; ++j) {
						pixels[j] = (float)array.nval(pBase + j) + fx1;
						++j;
						pixels[j] = (float)array.nval(pBase + j) + fy1;
					}
					canvas.drawPoints(pixels, thePaint);
					break;
				case Poly:
					ArrayList<Double> thisList = b.list();
					// User may have changed the list. If it has
					// an odd number of coordinates, ignore the last.
					int points = thisList.size() / 2;
					if (points >= 2) {					// do nothing if only one point
						fx1 = b.x();
						fy1 = b.y();
						Path path = new Path();
						Iterator<Double> listIt = thisList.iterator();
						float firstX = listIt.next().floatValue() + fx1;
						float firstY = listIt.next().floatValue() + fy1;
						path.moveTo(firstX, firstY);
						for (int p = 1; p < points; ++p) {
							float x = listIt.next().floatValue() + fx1;
							float y = listIt.next().floatValue() + fy1;
							path.lineTo(x, y);
						}
						path.lineTo(firstX, firstY);
						path.close();
						canvas.drawPath(path, thePaint);
					}
					break;
				case Text:
					canvas.drawText(b.text(), b.x(), b.y(), thePaint);
					break;
				case Bitmap:
					int bitmapIndex = b.bitmap();
					theBitmap = Run.BitmapList.get(bitmapIndex);
					if (theBitmap != null) {
						if (theBitmap.isRecycled()) {
							Run.BitmapList.set(bitmapIndex, null);
							theBitmap = null;
						}
					}
					if (theBitmap != null) {
						canvas.drawBitmap(theBitmap, b.x(), b.y(), thePaint);
					} else {
						NullBitMap = true;
					}
					break;
				case RotateStart:
					canvas.save();
					canvas.rotate(b.angle(), b.x1(), b.y1());
					break;
				case RotateEnd:
					canvas.restore();
					break;
				default:
					break;
			}
			return true;
		} // doDraw()

	} // class DrawView

}
