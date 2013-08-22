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

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

public class TextToSpeechActivity {

	private static final String TAG = "TextToSpeech";
	private static final String ID = "BASIC TTS";

	public int mStatus;
	public boolean mDone;
	private TextToSpeech mTTS;

	public TextToSpeechActivity(Context context) {
		Log.d(TAG, "constructor");
		mDone = true;								// not currently speaking

		// Initialize text-to-speech. This is an asynchronous operation.
		// The OnInitListener (second argument) is called after initialization completes.
		mTTS = new TextToSpeech(context, initListener);
	}

	public void shutdown() {
		// Don't forget to shutdown!
		Log.d(TAG, "shutdown");
		if (mTTS != null) {
			mTTS.stop();
			mTTS.shutdown();
			mTTS = null;
			mDone = true;
		}
	}

	private TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
		public void onInit(int status) {
			Log.d(TAG, "OnInitListener.onInit");
			if (status == TextToSpeech.SUCCESS) {
				setListener();
			}
			mStatus = status;
			Run.ttsInit = true;
		}
	};

	@SuppressWarnings("deprecation")
	private void setListener() {
		int level = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		if (level < 18) {
			mTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
				public void onUtteranceCompleted(String utteranceId) {
					Log.i(TAG, "completeListener: " + utteranceId);
					if (utteranceId.equals(ID))		// utteranceId is value of KEY_PARAM_UTTERANCE_ID
						mDone = true;
					return;
				}
			});
		} else {
			mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
				public void onStart(String utteranceId) {
					Log.i(TAG, "progressListener onStart: " + utteranceId);
				}

				public void onError(String utteranceId) {
					Log.i(TAG, "progressListener onError: " + utteranceId);
					if (utteranceId.equals(ID))
						mDone = true;
					return;
				}

				public void onDone(String utteranceId) {
					Log.i(TAG, "progressListener onDone: " + utteranceId);
					if (utteranceId.equals(ID))
						mDone = true;
					return;
				}
			});
		}
	}

	public void speak(String text, HashMap<String, String> params) {
		if (text != null) {
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ID);
			mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, params);
		}
	}

	public void speakToFile(String text, HashMap<String, String> params, String filename) {
		if (text != null) {
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ID);
			mTTS.synthesizeToFile(text, params, filename);
		}
	}

 }
