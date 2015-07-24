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

import static com.rfo.basic.Run.EventHolder.*;

import java.net.URLDecoder;
import java.util.ArrayList;

import org.apache.http.util.EncodingUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class Web extends Activity {
	//Log.v(Web.LOGTAG, " " + Web.CLASSTAG + " Line Buffer  " + ExecutingLineBuffer);

	WebView engine;
	public static TheWebView aWebView = null;
	private static final String LOGTAG = "Web";
	private static final String CLASSTAG = Web.class.getSimpleName();

	public static final String EXTRA_SHOW_STATUSBAR = "statusbar";
	public static final String EXTRA_ORIENTATION = "orientation";

	//******************************** Intercept BACK Key *************************************

	@Override
	public void onBackPressed() {
		if (engine.canGoBack()) {						// if can go back then do it
			addData("BAK", "1");						// tell user the back key was hit
		} else {
			addData("BAK", "0");						// tell user the back key was hit
			super.onBackPressed();						// done
		}
	}

	// ************************* Class startup Method ****************************

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);

		View v = findViewById(R.id.web_engine);

		Intent intent = getIntent();
		int showStatusBar = intent.getIntExtra(EXTRA_SHOW_STATUSBAR, 0);
		int orientation = intent.getIntExtra(EXTRA_ORIENTATION, -1);

		showStatusBar = (showStatusBar == 0)
						? WindowManager.LayoutParams.FLAG_FULLSCREEN			// do not show status bar
						: WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;	// show status bar
		getWindow().setFlags(showStatusBar, showStatusBar);

		setOrientation(orientation);

		engine = (WebView)  v;

		WebSettings webSettings = engine.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setGeolocationEnabled(true);
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

		engine.addJavascriptInterface(new JavaScriptInterface(), "Android");

		engine.setWebViewClient(new MyWebViewClient());

		aWebView = new TheWebView(this);

		engine.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				//Required functionality here
				return super.onJsAlert(view, url, message, result);
			}
		});

	}

	@Override
	protected void onPause() {
		Log.v(LOGTAG, " " + CLASSTAG + " onPause");
		Run.mEventList.add(new Run.EventHolder(WEB_STATE, ON_PAUSE, null));
		super.onPause();
	}

	@Override
	protected void onResume() {
		Log.v(LOGTAG, " " + CLASSTAG + " onResume");
		Run.mEventList.add(new Run.EventHolder(WEB_STATE, ON_RESUME, null));
		super.onResume();
	}

	private void setOrientation(int orientation) {	// Convert and apply orientation setting
		switch (orientation) {
			default:
			case -1: orientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR; break;
			case 0:  orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE; break;
			case 1:  orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT; break;
		}
		setRequestedOrientation(orientation);
	}

	//************************** Local method to put data into the Run read data link queue

	public void addData(String type, String data) {
		String theData = type + ":" + data;
		Run.mEventList.add(new Run.EventHolder(DATALINK_ADD, theData));
	}

	//*****************************************  WebView Client Interface *****************************

	private class MyWebViewClient extends WebViewClient implements DownloadListener{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			addData("LNK", url);
			view.setDownloadListener(this);
			return true;
		}

		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			if (failingUrl.contains("#")) { // Workaround to load a file if there is a hash appended to the file path
				Log.v("LOG", "failing url:" + failingUrl);
				String[] temp;
				temp = failingUrl.split("#");
				view.loadUrl(temp[0]); // load page without internal link

				try {
					Thread.sleep(400);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				view.loadUrl(failingUrl); // try again
			} else {
				int index = failingUrl.indexOf("FORM?");
				if (index == -1) { addData("ERR", errorCode + " " + description + failingUrl); }
			}
		}

		public void onLoadResource (WebView view, String url){
			int index = url.indexOf("FORM?");
			if (index != -1){
				String d = "&"+ URLDecoder.decode(url.substring(index+5));
				addData("FOR", d );
//				finish();
			}
		}

		public void onDownloadStart (String url, String agent, String disposition,
										String mimetype, long size) {
			addData("DNL", url);
		}
	}

	@Override
	protected void onStop() {
		//	aWebView = null; // otherwise html.load doses not work after a return to BASIC! Thanks to LUCA! !! 2013-10-11 gt
		Log.v(Web.LOGTAG, " " + Web.CLASSTAG + " onStop ");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		aWebView = null;
		Log.v(Web.LOGTAG, " " + Web.CLASSTAG + " onDestroy ");
		if (engine != null) engine.destroy();
		super.onDestroy();
	}

	// **************************  Methods called from Run.java ****************************

	public class TheWebView {

		public TheWebView(Context context) {
		}

		public void setOrientation(int orientation) {
			Web.this.setOrientation(orientation);
		}

		public void webLoadUrl(String URL) {
			if (engine != null)engine.loadUrl(URL);
		}

		public void webLoadString(String baseURL, String data) {
			if (engine == null) return;
			engine.loadDataWithBaseURL(baseURL, data, "text/html", "UTF-8", baseURL + "*");
		}

		public void webClose() {
			engine = null;
			finish();
		}

		public void goBack() {
			if ((engine != null) && engine.canGoBack()) { engine.goBack(); }
		}

		public void goForward(){
			if ((engine != null) && engine.canGoForward()) { engine.goForward(); }
		}

		public void clearCache() {
			if (engine != null) { engine.clearCache(true); }
		}

		public void clearHistory() {
			if (engine != null) { engine.clearHistory(); }
		}

		public void webPost(String URL, String htmlPostString) {
			engine.postUrl(URL, EncodingUtils.getBytes(htmlPostString, "BASE64"));
		}
	}

	//******************************** Intercept dataLink calls ***********************

	public class JavaScriptInterface {

		@JavascriptInterface
		public void dataLink(String data) {
			if (data.equals("STT")) {
				Intent intent = Run.buildVoiceRecognitionIntent();
				Run.sttListening = true;
				Run.sttDone = false;
				startActivityForResult(intent, Run.VOICE_RECOGNITION_REQUEST_CODE);
			}
			addData("DAT", data);
		}
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case Run.VOICE_RECOGNITION_REQUEST_CODE:
			if (resultCode == RESULT_OK) {
				Run.sttResults = new ArrayList<String>();
				Run.sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			}
			Run.sttDone = true;
		}
	}
}
