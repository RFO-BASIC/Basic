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

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;


public class TextToSpeechActivity {

	private static final String LOGTAG = "TextToSpeech";
	private static final String ID = "BASIC TTS";

	private Object LOCK = new Object();
	public int mStatus;
	public boolean mDone;
	private TextToSpeech mTTS;

	public TextToSpeechActivity(Context context) {
		// Initialize text-to-speech. This is an asynchronous operation.
		// The OnInitListener (second argument) is called after initialization completes.
		mDone = false;
		mTTS = new TextToSpeech(context, initListener);
		waitForDone();
	}

	public void shutdown() {
		// Don't forget to shutdown!
		Log.d(LOGTAG, "shutdown");
		if (mTTS != null) {
			mTTS.stop();
			mTTS.shutdown();
			mTTS = null;
			done();
		}
	}

	private TextToSpeech.OnInitListener initListener = new TextToSpeech.OnInitListener() {
		public void onInit(int status) {
			Log.d(LOGTAG, "OnInitListener.onInit status: " + status);
			if (status == TextToSpeech.SUCCESS) {
				setListener();
			}
			mStatus = status;
			done();
		}
	};

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void setListener() {
		if (android.os.Build.VERSION.SDK_INT < 15) {
			mTTS.setOnUtteranceCompletedListener(new TextToSpeech.OnUtteranceCompletedListener() {
				public void onUtteranceCompleted(String utteranceId) {
					Log.i(LOGTAG, "completeListener: " + utteranceId);
					if (utteranceId.equals(ID))		// utteranceId is value of KEY_PARAM_UTTERANCE_ID
						done();						// release LOCK
					return;
				}
			});
		} else {
			mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
				public void onStart(String utteranceId) {
					Log.i(LOGTAG, "progressListener onStart: " + utteranceId);
				}

				public void onError(String utteranceId) {
					Log.i(LOGTAG, "progressListener onError: " + utteranceId);
					if (utteranceId.equals(ID))
						done();
					return;
				}

				public void onDone(String utteranceId) {
					Log.i(LOGTAG, "progressListener onDone: " + utteranceId);
					if (utteranceId.equals(ID))
						done();
					return;
				}
			});
		}
	}

	public void speak(String text, HashMap<String, String> params, boolean block) {
		if (text != null) {
			waitForDone();									// wait for init or any previous speaking to finish
			mDone = false;
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ID);
			mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, params);
			if (block) {
				waitForDone();								// block if caller requests it
			}
		}
	}

	public void speakToFile(String text, HashMap<String, String> params, String filename) {
		if (text != null) {
			waitForDone();									// wait for init or any previous speaking to finish
			mDone = false;
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, ID);
			if (mTTS.synthesizeToFile(text, params, filename) != TextToSpeech.SUCCESS) {
				done();										// Trouble: no file written
			} else { waitForDone(); }						// always block
		}
	}

	private void done() {
		if (mDone) return;

		synchronized(LOCK) {
			mDone = true;									// semaphore used in waitForDone
			LOCK.notify();									// release the lock
		}
	}

	public void waitForDone() {
		synchronized (LOCK) {
			while (!mDone) {
				try { LOCK.wait(); }
				catch (InterruptedException e) { mDone = true; }
			}
		}
	}

 }
