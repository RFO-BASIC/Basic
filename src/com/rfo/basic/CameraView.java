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

    Some of the code in this module is based upon code obtained from:
    http://www.brighthub.com/mobile/google-android/articles/43414.aspx

*************************************************************************************************/

package com.rfo.basic;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;


// **************************** Using the device camera UI ******************************

public class CameraView extends Activity implements SurfaceHolder.Callback,
													OnClickListener{

	public static final int PICTURE_MODE_USE_UI = 0;
	public static final int PICTURE_MODE_AUTO = 1;
	public static final int PICTURE_MODE_MANUAL = 2;
	public static final int PICTURE_MODE_BLIND = 3; // preview on dummy surface, view is black, implies PICTURE_MODE_AUTO
	public static final String EXTRA_PICTURE_MODE = "picture_mode";
	public static final String EXTRA_CAMERA_NUMBER = "camera_number";
	public static final String EXTRA_FLASH_MODE = "flash_mode";
	public static final String EXTRA_FOCUS_MODE = "focus_mode";
	private int mPictureMode;
	private int mCameraNumber;
	private int mNewFlashMode;
	private int mNewFocusMode;
	private boolean mAutoFocus;

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		Intent intent = getIntent();
		mPictureMode = intent.getIntExtra(EXTRA_PICTURE_MODE, 0);
		mCameraNumber = intent.getIntExtra(EXTRA_CAMERA_NUMBER, 0);
		mNewFlashMode = intent.getIntExtra(EXTRA_FLASH_MODE, 0);
		mNewFocusMode = intent.getIntExtra(EXTRA_FOCUS_MODE, 0);
		if (mPictureMode == PICTURE_MODE_USE_UI) {
			doCameraUI();
		} else {
			doCameraNow();
		}
	}

private void doCameraUI(){
	  final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(getTempFileName())) ); 
	  startActivityForResult(intent, 254);

}


	private String getTempFileName() { return getTempFileName("png"); }

	private String getTempFileName(String ext){		// ext is "png" or "jpg"
		return Basic.getDataPath("image." + ext);
	}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if ((resultCode == RESULT_OK) && (requestCode == 254)) {
        String fn = getTempFileName();
        try {
	        BitmapFactory.Options BFO = new BitmapFactory.Options();
	        BFO.inSampleSize = 4;
	        Run.CameraBitmap = BitmapFactory.decodeFile(fn, BFO);           // Make the bit map from the file
  		}
  		catch (Exception e) {}
  }

  Run.CameraDone = true;
  finish();
}

	protected void onDestroy() {
		Run.CameraDone = true;
		if (mPreviewRunning || mAutoFocus || (mCamera != null)) {
			surfaceDestroyed(null);
		}
		super.onDestroy();
	}

// **************************** Auto/Manual shutter, No UI **********************************

	private static final String TAG = "CameraTest";
	private Camera mCamera = null;
	boolean mPreviewRunning = false;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;


	private void doCameraNow() {

		if (mPictureMode != PICTURE_MODE_BLIND) {
			getWindow().setFormat(PixelFormat.TRANSLUCENT);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			setContentView(R.layout.camera);
			mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		} else {
			mSurfaceView = new SurfaceView(getApplicationContext());
		}
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		switch (mPictureMode) {
		case PICTURE_MODE_MANUAL:
			mSurfaceView.setOnClickListener(this);
			/** FALL THROUGH to add callback **/
		case PICTURE_MODE_AUTO:
			mSurfaceHolder.addCallback(this);
			break;
		case PICTURE_MODE_BLIND:
			surfaceCreated(mSurfaceHolder);
			surfaceChanged(mSurfaceHolder, 0, 0, 0);
			break;
		default:
			Log.e(TAG, "Invalid picture mode " + mPictureMode);
			break;
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	Camera.ErrorCallback errorCallback = new Camera.ErrorCallback() {
		
		public void onError(int error, Camera camera) {
		}
	};

	Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
		// The callback is not currently being called
		public void onPictureTaken(byte[] imageData, Camera c) {
			if (imageData != null) {

				BitmapFactory.Options options=new BitmapFactory.Options();
				options.inSampleSize = 0;
				
				Run.CameraBitmap = BitmapFactory.decodeByteArray(imageData, 0,
						imageData.length,options);
				Run.CameraDone = true;
			    System.gc();

				finish();

			}

		}
	};

	Camera.AutoFocusCallback focusCallback = new Camera.AutoFocusCallback() {	
		public void onAutoFocus(boolean success, Camera camera) {
			try{
				mCamera.takePicture(null, null, null, pngCallback);
			} catch (Exception e){
				Log.e(TAG, "Camera Exception " + e);
			}
		}
	};

	Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
		// The callback is not currently being called
		public void onPictureTaken(byte[] data, Camera camera) {
			savePicture("jpg", data);
		}
	};

	Camera.PictureCallback pngCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			savePicture("png", data);
		}
	};

	private void savePicture(String ext, byte[] data) {
		FileOutputStream outStream = null;
		String fn = getTempFileName(ext);
		try {
			outStream = new FileOutputStream(fn);
			outStream.write(data);
			outStream.close();
//			Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
		} catch (Exception e) {
		}
		System.gc();
		BitmapFactory.Options BFO = new BitmapFactory.Options();
		BFO.inSampleSize = 4;
		Run.CameraBitmap = BitmapFactory.decodeFile(fn,BFO);           // Make the bit map from the file
		// if (Run.CameraManual) mCamera.startPreview();
		Run.CameraDone = true;
		finish();
	}

	public void surfaceCreated(SurfaceHolder holder) {
//		Log.d(TAG, "surfaceCreated");

		try {														// Run has previously determined SDK Level
			if (mCameraNumber == -1 ) { mCamera = Camera.open(); }	// If number = -1 then use level < 9 command
			else { mCamera = Camera.open(mCameraNumber); }			// else use level >= 9 command
		}
		catch (Exception e){										// If open fails, we are done.
			Log.e(TAG, "Camera open error: " + e);
			mCamera = null;
		}
		if (mCamera != null) {
			if (!setParameters()) {
				mCamera = null;
			} else try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
				mPreviewRunning = true;
			} catch (Exception e) {
				Log.e(TAG, "Camera start preview error : " + e);
				try {
					mCamera.stopPreview();
				} catch (Exception e1) {
					// ignore: the preview didn't get started
				} finally {
					mCamera = null;
				}
			}
		}
		if (mCamera == null) {
			Run.CameraDone = true;
			finish();
		}
	}

	public void onClick(View arg0) {

		if (mPictureMode == PICTURE_MODE_MANUAL) {
			if (!mAutoFocus) {
				try {
					mCamera.takePicture(null, null, null, pngCallback);
				} catch (Exception e) {
					Log.e(TAG, "CameraManual Exception " + e);
				}
			} else {
				mCamera.autoFocus(focusCallback);
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//		Log.d(TAG, "surfaceChanged");
		if (mCamera == null) {					// If no camera link then done
			Log.e(TAG, "Surface changed. mCamera NULL ");
			Run.CameraDone = true;
			finish();
			return;
		}

		// If Auto shutter, take picture now.
		
		if (mPictureMode == PICTURE_MODE_AUTO || mPictureMode == PICTURE_MODE_BLIND) {
			if (!mAutoFocus) {
				try{
					mCamera.takePicture(null, null, null, pngCallback);
				} catch (Exception e){
					Log.e(TAG, "CameraAuto Exception " + e);
				}
			} else {
				mCamera.autoFocus(focusCallback);
			}
		}
	}

	private boolean setParameters() {	
		Camera.Parameters p = mCamera.getParameters();

		String oldMode = p.getFlashMode();
		String newMode;
		switch (mNewFlashMode) {
		case 0: newMode = Camera.Parameters.FLASH_MODE_AUTO;    break;
		case 1: newMode = Camera.Parameters.FLASH_MODE_ON;      break;
		case 2: newMode = Camera.Parameters.FLASH_MODE_OFF;     break;
		case 3: newMode = Camera.Parameters.FLASH_MODE_TORCH;   break;	//Full Power/Flashlight/2013-07-25-16-42 gt
		case 4: newMode = Camera.Parameters.FLASH_MODE_RED_EYE; break;	//Flashlight/2013-07-27-19-12 gt
		default: newMode = ""; break;
		}
		List<String> supportedModes = p.getSupportedFlashModes();
		if ((oldMode != null) && (supportedModes != null)) {
			for (String mode : supportedModes) {
				if (mode.equals(newMode)) {
					p.setFlashMode(newMode);
					break;
				}
			}
		} else {														// "Supported Modes" not supported!
			p.setFlashMode(newMode);									// Just do it. May not work, but doesn't seem to hurt.
		}

		oldMode = p.getFocusMode();
		switch (mNewFocusMode) {											//Focus/2013-07-25-17-37 gt
		case 0: newMode = Camera.Parameters.FOCUS_MODE_AUTO;     break;
		case 1: newMode = Camera.Parameters.FOCUS_MODE_FIXED;    break;
		case 2: newMode = Camera.Parameters.FOCUS_MODE_INFINITY; break;
		case 3: newMode = Camera.Parameters.FOCUS_MODE_MACRO;    break;
		// (4 CONTINUOUS_VIDEO permissions?)
		//case 5: newMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO; break;
		default: newMode = ""; break;
		}
		supportedModes = p.getSupportedFocusModes();
		if ((oldMode != null) && (supportedModes != null)) {
			for (String mode : supportedModes) {
				if (mode.equals(newMode)) {
					p.setFocusMode(newMode);
					mAutoFocus = mode.equals(Camera.Parameters.FOCUS_MODE_AUTO);
					break;
				}
			}
		} else {														// "Supported Modes" not supported!
			p.setFlashMode(newMode);									// Just do it. May not work, but doesn't seem to hurt.
		}

		// if (Run.CameraSceneModeBarcode == 1) p.setSceneMode(Camera.Parameters.SCENE_MODE_BARCODE);

		// Use the largest preview size
		
		List<Camera.Size> sizes = p.getSupportedPreviewSizes();
		Camera.Size cs = sizes.get(0);
		for (Camera.Size ps : sizes) {	//2013-07-27-01-15 gt
			if (cs.width < ps.width) {
				cs = ps;
			}
		}
		p.setPreviewSize(cs.width, cs.height);

		// Use the largest picture size
		
		sizes = p.getSupportedPictureSizes();
		cs = sizes.get(0);
		for (Camera.Size ps : sizes) {	//2013-07-27-01-15 gt
			if (cs.width < ps.width) {
				cs = ps;
			}
		}
		p.setPictureSize(cs.width, cs.height);

		// Now set the parameters
		
		try {
			mCamera.setParameters(p);
		} catch (Exception e) {
			Log.e(TAG, "Camera parameter set error : " + e);
			Run.CameraDone = true;
			finish();
			return false;

		}
		return true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
//		Log.d(TAG, "surfaceDestroyed");
		if (mPreviewRunning) {
			if (mCamera != null) { mCamera.stopPreview(); }
			mPreviewRunning = false;
		}
		if (mAutoFocus) {
			if (mCamera != null) {mCamera.cancelAutoFocus(); }
			mAutoFocus = false;
		}
		if (mCamera != null) {
			mCamera.release();
			mCamera = null;
		}
	}

}

