/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2015 Paul Laughton

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

import android.content.Context;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


/**
 * Manage the soft keyboard for a View.
 * - Provides methods to hide and show the keyboard, for which it is its own ResultReceiver.
 * If InputMethodManager accepts the change request, these methods block until the
 * ResultReceiver callback onReceiveResult reports the response to the request.
 * - Provides a delegate method for the onKeyPreIme(in, KeyEvent) method of the View
 * that intercepts BACK key events.
 * - Provides a callback to notify the View when the keyboard visibility changes.
 * - Invoke release() before nulling the last reference to an object of this class.
 */
public class KeyboardManager extends ResultReceiver {
	private static final String LOGTAG = "KBView";

	public interface KeyboardChangeListener {
		void kbChanged();
	}

	private View mView;
	private KeyboardChangeListener mListener;
	private InputMethodManager mIMM;
	private final Object KB_LOCK = new Object();

	private boolean kbShown = false;
	private boolean mWaitForKeyboard;						// semaphore for soft keyboard changes

	public KeyboardManager(Context context, View view, KeyboardChangeListener listener) {
		super(null);
		mView = view;
		mListener = listener;
		mIMM = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	/**
	 * The view must delegate its onKeyPrime(int, KeyEvent) method to this one.
	 * It intercepts the BACK key events before the soft keyboard sees them.
	 * If the keyboard is showing, this method eats the ACTION_DOWN,
	 * then on ACTION_UP it hides the keyboard.
     * @param keyCode The value in event.getKeyCode().
     * @param event Description of the key event.
     * @return If you handled the event, return true. If you want to allow the
     *         event to be handled by the next receiver, return false.
	 */
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (showing()) {
				if (event.getAction() == KeyEvent.ACTION_UP) {
					hide();
				}
				return true;						// handled
			}
		}
		return false;								// not handled
	}

	/**
	 * Called when the InputMethodManager has serviced the keyboard change request.
	 * If the keyboard visibility changed, the KeyboardChangeListener is informed.
	 * Always updates kbShown state and, in case someone is waiting for it, releases KB_LOCK.
	 */
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		switch (resultCode) {
			case InputMethodManager.RESULT_SHOWN:
				mListener.kbChanged();
			case InputMethodManager.RESULT_UNCHANGED_SHOWN:
				kbShown = true;
				break;
			case InputMethodManager.RESULT_HIDDEN:
				mListener.kbChanged();
			case InputMethodManager.RESULT_UNCHANGED_HIDDEN:
				kbShown = false;
				break;
			default:
				break;
		}
		if (!mWaitForKeyboard)		return;

		synchronized (KB_LOCK) {
			mWaitForKeyboard = false;
			KB_LOCK.notify();
		}
	}

	/**
	 * Block on KB_LOCK, typically until onReceiveResult releases it.
	 */
	private void waitForKB() {
		synchronized (KB_LOCK) {
			while (mWaitForKeyboard) {
				try { KB_LOCK.wait(); }
				catch (InterruptedException e) { mWaitForKeyboard = false; }
			}
		}
	}

	/**
	 * Unconditionally try to hide the soft keyboard. Don't wait for response.
	 */
	public void forceHide() {
		mIMM.hideSoftInputFromWindow(mView.getWindowToken(), 0, null);
		kbShown = false;
	}

	/**
	 * Unconditionally try to hide the soft keyboard. If the InputMethodManager
	 * accepts the request, block until the response is received.
	 * @return true if the InputMethodManager accepts the request
	 */
	public boolean hide() {
		mWaitForKeyboard = true;
		boolean accepted = mIMM.hideSoftInputFromWindow(mView.getWindowToken(), 0, this);
		if (accepted)	{ waitForKB(); }
		else			{ mWaitForKeyboard = false; }
		return accepted;
	}

	/**
	 * IF the the soft keyboard is not showing, try to show it. If the InputMethodManager
	 * accepts the request, block until the response is received.
	 * @return true if the InputMethodManager accepts the request
	 */
	public boolean show() {
		if (kbShown)		return true;
		mWaitForKeyboard = true;
		boolean accepted = false;
		for (int retryCount = 0; retryCount < 10; ++retryCount) {
			accepted = mIMM.showSoftInput(mView, 0, this);
			Log.d(LOGTAG, "show: count " + retryCount);
			if (accepted) { break; }
			try { Thread.sleep(100); }
			catch (InterruptedException e) {};
		}
		if (accepted)	{ waitForKB(); }
		else			{ mWaitForKeyboard = false; }
		return accepted;
	}

	/**
	 * @return true if the soft keyboard is visible, false if it is not
	 */
	public boolean showing() {
		return (kbShown);
	}

	/**
	 * Release resources used by this KeyboardManager.
	 * Call this before nulling the last reference to the object.
	 */
	public void release() {
		synchronized (KB_LOCK) {		// wait for kbHide or kbShow to complete
			mView = null;
			mListener = null;
			mIMM = null;
		}
	}
}
