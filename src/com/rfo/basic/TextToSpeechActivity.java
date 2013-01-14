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

import java.util.HashMap;
import java.util.Locale;
import java.util.Random;

import com.rfo.basic.Run;
import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import com.rfo.basic.Run;
import android.util.Log;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.text.format.Time;
import android.util.Log;

public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener, OnUtteranceCompletedListener{

    private static final String TAG = "TextToSpeechDemo";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize text-to-speech. This is an asynchronous operation.
        // The OnInitListener (second argument) is called after initialization completes.
        Run.mTts = new TextToSpeech(this,
            this  // TextToSpeech.OnInitListener
            );

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
       	if (Basic.DoAutoRun) android.os.Process.killProcess(Basic.ProcessID) ;

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



    @Override
    public void onDestroy() {
        // Don't forget to shutdown!
        if (Run.mTts != null) {
            Run.mTts.stop();
            Run.mTts.shutdown();
        }

        super.onDestroy();
    }

    // Implements TextToSpeech.OnInitListener.
    public void onInit(int status) {
        if(status == TextToSpeech.SUCCESS) {
            Run.mTts.setOnUtteranceCompletedListener(this);
        }
    	Run.ttsInitResult = status;
        Run.ttsInit = true;

    }


    public void onUtteranceCompleted(String utteranceId) {
    	   
    	   Log.i(TAG, utteranceId); //utteranceId == "Done"
    	   if (utteranceId.equals("Done")) Run.ttsSpeakDone = true;
    	   return;
    	   }
    
    private void speak(String text) {
    	   if(text != null) {
    	      HashMap<String, String> myHashAlarm = new HashMap<String, String>();
    	      myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
    	      myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
    	      Run.mTts.speak(text, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
    	   }
    	}
    
    
    public void doSpeak(String text){

    }
    
 }
