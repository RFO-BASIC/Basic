/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2014 Paul Laughton

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

    public static Context context;
    public static DrawView drawView;
    public static Bitmap screenBitmap = null;
//    public static boolean NewTouch[] = {false, false};
//    public static double TouchX[] = {0,0};
//    public static double TouchY[] = {0,0};
    public static float scaleX = 1f;
    public static float scaleY = 1f;
    public static Boolean Rendering = false;	// Boolean (not boolean) so it has a lock
    public static boolean NullBitMap = false;
    public static InputMethodManager GraphicsImm ;
    public static float Brightness = -1;
    
    public static boolean doSTT = false;
    public static boolean doEnableBT = false;
    public static boolean startConnectBT = false;
    
    public static final int dNull = 0;
    public static final int dCircle = 1;
    public static final int dRect = 2;
    public static final int dLine = 3;
    public static final int dOval = 4;
    public static final int dArc = 5;
    public static final int dText = 6;
    public static final int dBitmap = 7;
    public static final int dRotate_Start = 8;
    public static final int dRotate_End = 9;
    public static final int dOrientation = 10;
    public static final int dClose = 11;
    public static final int dsetPixels = 12;
    public static final int dClip = 13;
    public static final int dPoly = 14;
    public static final int dPoint = 15;
    public static final String[] types = {			// KEEP THESE IN SYNC!!!
        "null",				// dNull = 0;
        "circle",			// dCircle = 1;
        "rect",				// dRect = 2;
        "line",				// dLine = 3;
        "oval",				// dOval = 4;
        "arc",				// dArc = 5;
        "text",				// dText = 6;
        "bitmap",			// dBitmap = 7;
        "rotate.start",		// dRotate_Start = 8;
        "rotate.end",		// dRotate_End = 9;
        "orientation",		// dOrientation = 10;
        "close",			// dClose = 11;
        "set.pixels",		// dsetPixels = 12;
        "clip",				// dClip = 13;
        "poly",				// dPoly = 14;
        "point",			// dPoint = 15;
    };

//  Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " String Var Value =  ");


    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " On Create  ");
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

		drawView = new DrawView(this);
		setContentView(drawView);
		drawView.requestFocus();
		drawView.setBackgroundColor(backgroundColor);
		drawView.setId(33);
		drawView.setOrientation(orientation);		// Set orientation, get screen height and width

		setVolumeControlStream(AudioManager.STREAM_MUSIC);

        scaleX = 1.0f;
        scaleY = 1.0f;
        Brightness = -1;
        Rendering = false;

//        Run.GRrunning = true;
//        Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " on create display list =  "+Run.DisplayList);        
    }
    
   
    @Override
    protected void onPause() {
    	Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onPause ");
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
    
    protected void onStop(){
    	Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onStop ");
    	Run.background = true;
    	Run.bgStateChange = true;
    	super.onStop();
    }
    
    @Override
    protected void onResume() {
    	Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onResume ");
//        Run.GRrunning = true;
    	Run.background = false;
    	Run.bgStateChange = true;
		context = this;
      super.onResume();
    }  
    @Override
    protected void onDestroy() {
    	Run.GRrunning = false;
    	Run.GraphicsPaused = false;
        super.onDestroy();
        Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " onDestroy");
    }
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {

    	Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " keyDown " + keyCode);

    	if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP) || (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
    	  return super.onKeyDown(keyCode, event);
    	}

        return true;
    }
    
	public boolean onKeyUp(int keyCode, KeyEvent event)  {						// The user hit a key
		
    	Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " keyUp " + keyCode);
    	
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
        	if (resultCode == RESULT_OK){
    	        Run.sttResults = new ArrayList<String>();
        		Run.sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        	}
        	Run.sttDone = true;
            Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " VR Done");
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

    
    public void startVoiceRecognitionActivity() {
        Log.v(GR.LOGTAG, " " + GR.CLASSTAG + " VR Start" + Process.myTid());

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "BASIC! Speech To Text");
        startActivityForResult(intent, Run.VOICE_RECOGNITION_REQUEST_CODE);
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

		public void setOrientation(int orientation) {	// Convert and apply orientation setting
//			Log.v(GR.LOGTAG, "Set orientation " + orientation);
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

        public boolean onTouchEvent(MotionEvent event){
        	super.onTouchEvent(event);
        	int action = event.getAction();  // Get action type


        	for (int i = 0; i < event.getPointerCount(); i++){
        		if ( i > 1 ) break;
            	int pid = event.getPointerId(i);
            	if (pid > 1) break;
//        		Log.v(GR.LOGTAG, " " + i + ","+pid+"," + action);
        		Run.TouchX[pid] = (double)event.getX(i);
        		Run.TouchY[pid] = (double)event.getY(i);
        		if (action == MotionEvent.ACTION_DOWN ||
        			action == MotionEvent.ACTION_POINTER_DOWN) {
        			Run.NewTouch[pid] = true;
        			Run.NewTouch[2] = true;
        			Run.Touched = true;
        		}
        		else if	(action == MotionEvent.ACTION_MOVE){
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
   
  	  public Paint newPaint(Paint fromPaint){
		  Typeface tf = fromPaint.getTypeface();
		  Paint rPaint = new Paint(fromPaint);
		  rPaint.setTypeface(tf);
		  return rPaint;
	  }

		@Override
		public  void onDraw(Canvas canvas) {
//			Log.d(LOGTAG,"onDraw " + getWidth() + " x " + getHeight());
            if (doEnableBT) {							// If this activity is running
            	enableBT();								// Bluetooth must be enabled here
            	doEnableBT = false;
            }
            
            if (startConnectBT) {
            	startsBTConnect();
            	startConnectBT = false;
            }
       		
       		if (doSTT) {
   					startVoiceRecognitionActivity();
   					doSTT = false;
       		}

        	synchronized (Rendering) {
        		Run.GRrunning = true;
        		if (!Rendering) return;		// If Run.java did not ask to render then don't render
        	}
        	
            float scale = getResources().getDisplayMetrics().density;
        	Paint thePaint;
        	Bitmap theBitmap;
        	drawView.setDrawingCacheEnabled(true);
        	canvas.scale(scaleX, scaleY);
        	
        	if (Run.DisplayList == null){					        // If we have lost context then
        		throw new RuntimeException("GR.onDraw: null DisplayList");
        	}
        	
        	if (Brightness != -1){
        		WindowManager.LayoutParams lp = getWindow().getAttributes();
        		lp.screenBrightness = Brightness;
        		getWindow().setAttributes(lp);
        	}

        	if (Run.RealDisplayList != null){
        		for (int di = 0 ; di< Run.RealDisplayList.size(); ++di){
        			int rdi = Run.RealDisplayList.get(di);
        			if (Run.DisplayList != null){
        				if (rdi >= Run.DisplayList.size()) return;
        				Bundle b = Run.DisplayList.get(rdi);
        				doDraw(canvas, b);
        			}
        		}
        	}

       		Rendering = false;
            
            
        }

		public void doDraw(Canvas canvas, Bundle b) {
//			Log.v(GR.LOGTAG, "DrawIntoCanvas " + canvas + ", " + b);

			int x1;
			int y1;
			int x2;
			int y2;
			int r;
			float fx1;
			float fy1;

			Paint thePaint;
			Bitmap theBitmap;
//			canvas.scale(scaleX, scaleY);

			thePaint = null;
			int type = dNull;
			if ((b != null) && (b.getInt("hide") == 0)) {
				type = b.getInt("type");
				int pIndex = b.getInt("paint");
				if (Run.PaintList.size() == 0) return;
				if (pIndex < 1 || pIndex >= Run.PaintList.size()) return;
				thePaint = newPaint(Run.PaintList.get(pIndex));
				int alpha = b.getInt("alpha");
				if (alpha < 256) thePaint.setAlpha(alpha);
			}

			switch (type) {
				case dNull:
					break;
				case dClose:
					finish();
					break;
				case dCircle:
					x1 = b.getInt("x");
					y1 = b.getInt("y");
					r = b.getInt("radius");
					canvas.drawCircle(x1,y1,r, thePaint);
					break;
				case dRect:
					x1 = b.getInt("left");
					x2 = b.getInt("top");
					y2 = b.getInt("bottom");
					y1 = b.getInt("right");
					canvas.drawRect(x1,x2,y1,y2, thePaint);
					break;
				case dClip:
					x1 = b.getInt("left");
					x2 = b.getInt("top");
					y2 = b.getInt("bottom");
					y1 = b.getInt("right");
					int RO = b.getInt("RO");
					Region.Op[] ops = {
						Region.Op.INTERSECT, Region.Op.DIFFERENCE, Region.Op.REPLACE,
						Region.Op.REVERSE_DIFFERENCE, Region.Op.UNION, Region.Op.XOR
					};
					canvas.clipRect(x1,x2,y1,y2, ops[RO]);
					break;
				case dOval:
					x1 = b.getInt("left");
					x2 = b.getInt("top");
					y1 = b.getInt("right");
					y2 = b.getInt("bottom");
					RectF rect = new RectF(x1,x2,y1,y2);
					canvas.drawOval(rect , thePaint);
					break;
				case dArc:
					x1 = b.getInt("left");
					x2 = b.getInt("top");
					y1 = b.getInt("right");
					y2 = b.getInt("bottom");
					rect = new RectF(x1,x2,y1,y2);
					int sa = b.getInt("start_angle");
					int ea = b.getInt("sweep_angle");
					boolean fill = (b.getInt("fill_mode") != 0);
					canvas.drawArc(rect, sa,ea,fill, thePaint);
					break;
				case dLine:
					x1 = b.getInt("x1");
					x2 = b.getInt("x2");
					y1 = b.getInt("y1");
					y2 = b.getInt("y2");
					canvas.drawLine(x1,y1,x2,y2, thePaint);
					break;
				case dPoint:
					x1 = b.getInt("x");
					y1 = b.getInt("y");
					canvas.drawPoint(x1,y1, thePaint);
					break;
				case dsetPixels:
					fx1 = b.getInt("x");
					fy1 = b.getInt("y");
					int pBase = b.getInt("pbase");
					int pLength = b.getInt("plength");
					float[] pixels = new float[pLength];
					for (int j = 0; j < pLength; ++j) {
						pixels[j] = Run.NumericVarValues.get(pBase + j).floatValue() + fx1;
						++j;
						pixels[j] = Run.NumericVarValues.get(pBase + j).floatValue() + fy1;
					}
					canvas.drawPoints(pixels, thePaint);
					break;
				case dPoly:
					fx1 = b.getInt("x");
					fy1 = b.getInt("y");
					int ListIndex = b.getInt("list");
					ArrayList<Double> thisList = Run.theLists.get(ListIndex);
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
				case dText:
					x1 = b.getInt("x");
					y1 = b.getInt("y");
					String s = b.getString("text");
					canvas.drawText(s, x1, y1, thePaint);
					break;
				case dBitmap:
					x1 = b.getInt("x");
					y1 = b.getInt("y");
					theBitmap = Run.BitmapList.get(b.getInt("bitmap"));
					if (theBitmap != null) {
						if (theBitmap.isRecycled()) {
							Run.BitmapList.set(b.getInt("bitmap"), null);
							theBitmap = null;
						}
					}
					if (theBitmap != null) {
						canvas.drawBitmap(theBitmap, x1, y1, thePaint);
					} else {
						NullBitMap = true;
					}
					break;
				case dRotate_Start:
					int angle = b.getInt("angle");
					x1 = b.getInt("x");
					y1 = b.getInt("y");
					canvas.save();
					canvas.rotate(angle, x1, y1);
					break;
				case dRotate_End:
					canvas.restore();
					break;
				default:
			}
		} // doDraw()

	} // class DrawView

}
