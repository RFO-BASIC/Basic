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
    
    Some of the code in this module is based upon code obtained from:
    http://www.brighthub.com/mobile/google-android/articles/43414.aspx
    
	*************************************************************************************************/

package com.rfo.basic;


import static com.rfo.basic.Basic.AppPath;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import com.rfo.basic.Basic;

import android.app.Activity;
import android.content.Context;
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
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
	  if (Run.CameraAuto || Run.CameraManual) doCameraNow();
	  else doCameraUI();

	}

private void doCameraUI(){
	  final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	  intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(this)) ); 
	  startActivityForResult(intent, 254);

}


private File getTempFile(Context context){
	  File path = new File (Basic.filePath + "/data/image.png");
	  return path;
	}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
  if (resultCode == RESULT_OK) {
  if (requestCode == 254){
        final File file = getTempFile(this);
        String fn = new File(Basic.filePath + "/data/image.png").getAbsolutePath();
        try {
	        BitmapFactory.Options BFO = new BitmapFactory.Options();
	        BFO.inSampleSize = 4;
	        Run.CameraBitmap = BitmapFactory.decodeFile(fn, BFO);           // Make the bit map from the file
  		}
  		catch (Exception e) {}
    	}
  }
    
  Run.CameraDone = true;
  finish();
}

protected void onDestroy(){
	Run.CameraDone = true;
    super.onDestroy();

}


// **************************** Auto/Manual shutter, No UI **********************************

//	static final int FOTO_MODE = 0;
	private static final String TAG = "CameraTest";
	Camera mCamera;
	boolean mPreviewRunning = false;
	private Context mContext = this;
	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;


	private void doCameraNow() {

		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.camera);
		mSurfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		if (Run.CameraManual) mSurfaceView.setOnClickListener(this);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	Camera.ErrorCallback errorCallback = new Camera.ErrorCallback() {
		
		public void onError(int error, Camera camera) {
			int k = error;
			
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

	Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
		// The callback is not currently being called
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(Basic.filePath + "/data/image.jpg");
				outStream.write(data);
				outStream.close();
//				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
		        String fn = new File(Basic.filePath + "/data/image.jpg").getAbsolutePath();
		        System.gc();
		        BitmapFactory.Options BFO = new BitmapFactory.Options();
		        BFO.inSampleSize = 4;
		        Run.CameraBitmap = BitmapFactory.decodeFile(fn,BFO);           // Make the bit map from the file
			} catch (Exception e) {
			}
			if (Run.CameraManual) mCamera.startPreview();
			Run.CameraDone = true;
			finish();

		}
	};

	Camera.PictureCallback pngCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(Basic.filePath + "/data/image.png");
				outStream.write(data);
				outStream.close();
//				Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length);
		        String fn = new File(Basic.filePath + "/data/image.png").getAbsolutePath();
		        System.gc();
		        BitmapFactory.Options BFO = new BitmapFactory.Options();
		        BFO.inSampleSize = 4;
		        Run.CameraBitmap = BitmapFactory.decodeFile(fn,BFO);           // Make the bit map from the file
			} catch (Exception e) {
			}
			if (Run.CameraManual) mCamera.startPreview();
			Run.CameraDone = true;
			finish();

		}
	};

	protected void onResume() {
//		Log.e(TAG, "onResume");
		super.onResume();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	protected void onStop() {
//		Log.e(TAG, "onStop");
//		Run.CameraDone = true;
		super.onStop();
	}

	public void surfaceCreated(SurfaceHolder holder) {
//		Log.e(TAG, "surfaceCreated");

		try {														//Run has previously determined SDK Level
			if (Run.CameraNumber == -1 ) mCamera = Camera.open();   //If number = -1 then use level < 9 command
			else mCamera = Camera.open(Run.CameraNumber);           //else use level >= 9 command
		}
		catch (Exception e){										//If open fails, we are done.
			Log.e(TAG, "Camera open error: " + e);
			Run.CameraDone = true;
			finish();
			return;
		}

	}
	
	public void onClick(View arg0) {

		if (Run.CameraManual)
			try{
				mCamera.takePicture(null, null, null, pngCallback);
			}
			catch (Exception e){
				Log.e(TAG, "CameraManual Exception " + e);
			}

	}

	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
//		Log.e(TAG, "surfaceChanged");
		if (mCamera == null) {					// If no camera link then done
			Log.e(TAG, "Surface changed. mCamera NULL ");
			Run.CameraDone = true;
			finish();
			return;
		}

		// XXX stopPreview() will crash if preview is not running
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		
		boolean back = true;
		if (Run.CameraNumber != -1) {
			Camera.CameraInfo CI = new Camera.CameraInfo();				   
			mCamera.getCameraInfo(Run.CameraNumber, CI);
			if (CI.facing == CameraInfo.CAMERA_FACING_FRONT) 
				back = false;
			}

		if (back) {
			if (Run.CameraFlashMode == 0) p.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
			if (Run.CameraFlashMode == 1) p.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
			if (Run.CameraFlashMode == 2) p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
		}
				
		// Use the largest preview size
		
		List<Camera.Size> previewSizes = p.getSupportedPreviewSizes();
		Camera.Size cs1 = previewSizes.get(0);
		p.setPreviewSize(cs1.width, cs1.height);
			
		
		// Use the largest picture size
		
		List<Camera.Size> list = p.getSupportedPictureSizes();
		Camera.Size cs = list.get(0);
		p.setPictureSize(cs.width, cs.height);

		
		// Now set the parameters
		
		try {
			mCamera.setParameters(p);

			mCamera.setPreviewDisplay(holder);
		} catch (Exception e) {
			Log.e(TAG, "Camera parameter set error : " + e);
			Run.CameraDone = true;
			finish();
			return;

		}
		
		try{
			mCamera.startPreview();
		} catch (Exception e) {
			Log.e(TAG, "Camera start preview error : " + e);
			Run.CameraDone = true;
			finish();
			return;
		}
		
		mPreviewRunning = true;
		
		// If Auto shutter, take picture now.
		
		if (Run.CameraAuto)
			try{
				mCamera.takePicture(null, null, null, pngCallback);
			}
			catch (Exception e){
				Log.d(TAG, "CameraAuto Exception " + e);

			}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
//		Log.e(TAG, "surfaceDestroyed");
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
	}


	
	
}

