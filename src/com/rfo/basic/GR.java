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

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Process;
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
import android.view.inputmethod.InputMethodManager;
import android.graphics.RectF;
import android.graphics.Bitmap;


public class GR extends Activity {
	private static final String LOGTAG = "GR";
	private static final String CLASSTAG = GR.class.getSimpleName();

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
	public static boolean Running = false;
	public static Boolean Rendering = false;	// Boolean (not boolean) so it has a lock
	public static boolean NullBitMap = false;
	public static InputMethodManager GraphicsImm ;
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
		private int mListIndex;							// for getValue
		private ArrayList<Double> mList;				// for Type.Poly
		private Run.ArrayDescriptor mArray;				// for Type.SetPixels
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
		public void array(Run.ArrayDescriptor array) { mArray = array; }
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
		public String text()				{ return mText; }
		public Region.Op clipOp()			{ return mClipOp; }
		public ArrayList<Double> list()		{
			if (mList == null) { mList = new ArrayList<Double>(); }
			return mList;
		}
		public Run.ArrayDescriptor array()	{ return mArray; }
		public int radius()					{ return mRadius; }
		public float angle()				{ return mAngle_1; }
		public float arcStart()				{ return mAngle_1; }
		public float arcSweep()				{ return mAngle_2; }
		public boolean useCenter()			{ return mUseCenter; }

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
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " On Create " + this.toString());
		super.onCreate(savedInstanceState);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		Intent intent = getIntent();
		int showStatusBar = intent.getIntExtra(EXTRA_SHOW_STATUSBAR, 0);
		int orientation = intent.getIntExtra(EXTRA_ORIENTATION, -1);
		int backgroundColor = intent.getIntExtra(EXTRA_BACKGROUND_COLOR, 0xFF000000);

		showStatusBar = (showStatusBar == 0)
				? WindowManager.LayoutParams.FLAG_FULLSCREEN			// do not show status bar
				: WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;	// show status bar
		getWindow().setFlags(showStatusBar, showStatusBar);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		scaleX = 1.0f;
		scaleY = 1.0f;
		Brightness = -1;
		Rendering = false;

		drawView = new DrawView(this);
		setContentView(drawView);
		drawView.requestFocus();
		drawView.setBackgroundColor(backgroundColor);
		drawView.setId(33);
		drawView.setOrientation(orientation);		// Set orientation, get screen height and width

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

	@Override
	protected void onPause() {
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onPause");
		Run.background = true;
		Run.bgStateChange = true;
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onRestart");
	}

	protected void onStop() {
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onStop");
		Run.background = true;
		Run.bgStateChange = true;
		super.onStop();
	}

	@Override
	protected void onResume() {
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onResume");
		Run.background = false;
		Run.bgStateChange = true;
		context = this;
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onDestroy " + this.toString());
		// if a new instance has started, don't let this one mess it up
		if (context == this) {
			Running = false;
			if (waitForLock) {
				releaseLOCK();							// don't leave GR.command hanging
			}
		}
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {

//		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " keyDown " + keyCode);

		if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
			return super.onKeyDown(keyCode, event);
		}

		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event)  {						// The user hit a key

//		Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " keyUp " + keyCode);

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " BACK KEY HIT");

			if (Run.OnBackKeyLine != 0){
				Run.BackKeyHit = true;
				return true;
			}

			Run.GRopen = false;
			Run.Stop = true;
			if (Basic.DoAutoRun) {
				Run.Exit = true;			// Signal Run to exit immediately and silently
			}
			finish();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (Run.OnMenuKeyLine != 0) {
				Run.MenuKeyHit = true;
				return true;
			}
		}

		char c;
	    String theKey = "@";
	    int n ;
	    if (keyCode >= 7 && keyCode <= 16){
	    	n = keyCode - 7;
	    	c = Run.Numbers.charAt(n);
	    	theKey = Character.toString(c);
	    	
	    }else if (keyCode >=29 && keyCode <= 54){
	    	n = keyCode -29;
	    	c = Run.Chars.charAt(n);
	    	theKey = Character.toString(c);
	    }else if (keyCode == 62){ 
	    	c = ' ';
	    	theKey = Character.toString(c);
	    }else if (keyCode >= 19 && keyCode <= 23){
	    	switch (keyCode) {
	    	case 19: theKey = "up"; break;
	    	case 20: theKey = "down"; break;
	    	case 21: theKey = "left"; break;
	    	case 22: theKey = "right"; break;
	    	case 23: theKey = "go"; break;
	    	}
	    }else {
	    	theKey = "key " + keyCode;
	    }
	    synchronized (this) {
	    	Run.InChar.add(theKey);
	    }
	    Run.KeyPressed = true;
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

	private void releaseLOCK() {
		synchronized (LOCK) {
//			Log.d(LOGTAG, "releaseLOCK");
			waitForLock = false;
			LOCK.notify();								// release GR.OPEN or .CLOSE if it is waiting
		}
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
		private static final String TAG = "DrawView";

		public DrawView(Context context) {
			super(context);
			setFocusable(true);
			setFocusableInTouchMode(true);
			GraphicsImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		}

		synchronized public void setOrientation(int orientation) {	// Convert and apply orientation setting
			Log.v(GR.LOGTAG, "Set orientation " + orientation);
			switch (orientation) {
				default:
				case 1:  orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; break;
				case 0:  orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; break;
				case -1: orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR; break;
			}
			setRequestedOrientation(orientation);
		}

		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		public int getWindowMetrics(Point outSize) {	// return size in Point, density as return value
			// This can be called when the DrawView does not yet know what size it is,
			// so get the size from the WindowManager.
			Display display = getWindowManager().getDefaultDisplay();
			int level = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
			if (level < 13) {
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
//				Log.v(GR.LOGTAG, " " + i + "," + pid + "," + action);
				Run.TouchX[pid] = (double)event.getX(i);
				Run.TouchY[pid] = (double)event.getY(i);
				if (action == MotionEvent.ACTION_DOWN ||
					action == MotionEvent.ACTION_POINTER_DOWN) {
					Run.NewTouch[pid] = true;			// which pointer (0 or 1), cleared on UP
					Run.NewTouch[2] = true;				// either pointer, not cleared on UP
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
//			Log.d(LOGTAG,"onDraw, Rendering " + Rendering);
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
				String msg = "GR.onDraw: null DisplayList";
				Log.e(LOGTAG, msg);
				throw new RuntimeException(msg);		// lost context?
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
			}

			synchronized (GR.Rendering) {
				if (Rendering) {
					Rendering = false;
					releaseLOCK();
				}
			}
		}

		public boolean doDraw(Canvas canvas, BDraw b) {
//			Log.v(GR.LOGTAG, "DrawIntoCanvas " + canvas + ", " + b);

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
				thePaint = newPaint(Run.PaintList.get(pIndex));
				int alpha = b.alpha();
				if (alpha < 256) thePaint.setAlpha(alpha);
			}

			switch (type) {
				case Group:
				case Null:
					break;
				case Open:
					synchronized (GR.Rendering) {
						Running = true;					// flag for GR.Open
						Rendering = true;				// so onDraw will release LOCK
					}
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
					Run.ArrayDescriptor array = b.array();
					int pBase = array.base();
					int pLength = array.length();
					float[] pixels = new float[pLength];
					for (int j = 0; j < pLength; ++j) {
						pixels[j] = (float)Run.Vars.get(pBase + j).nval() + fx1;
						++j;
						pixels[j] = (float)Run.Vars.get(pBase + j).nval() + fy1;
					}
					canvas.drawPoints(pixels, thePaint);
					break;
				case Poly:
					fx1 = b.x();
					fy1 = b.y();
					ArrayList<Double> thisList = b.list();
					Path p = new Path();
					float firstX = thisList.get(0).floatValue() + fx1;
					float firstY = thisList.get(1).floatValue() + fy1;
					p.moveTo(firstX, firstY);
					int size = thisList.size();
					for (int i = 2; i < size; ) {
						float nextX = thisList.get(i++).floatValue() + fx1;
						float nextY = thisList.get(i++).floatValue() + fy1;
						p.lineTo(nextX, nextY);
					}
					p.lineTo(firstX, firstY);
					p.close();
					canvas.drawPath(p, thePaint);
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
