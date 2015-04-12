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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class TextInput extends Activity {
	private Button finishedButton;			// The buttons
	private EditText theTextView;			// The EditText TextView

	private boolean lockReleased;			// safety valve so interpreter doesn't get hung if this
											// instance is destroyed without first releasing the LOCK

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)  {
		// if BACK key leave original text unchanged
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			releaseLock();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.text_input);	// Layout xmls exist for both landscape and portrait modes

		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		if (title != null) { setTitle(title); }

		lockReleased = false;

		finishedButton = (Button) findViewById(R.id.finished_button);		// The buttons

		theTextView = (EditText) findViewById(R.id.the_text);				// The text display area
		theTextView.setText(Run.TextInputString);							// The Editor's display text
		theTextView.setTypeface(Typeface.MONOSPACE);
		theTextView.setSelection(Run.TextInputString.length());

		Basic.TextStyle style = Basic.defaultTextStyle;						// Get text color from Settings
		theTextView.setTextColor(style.mTextColor);
		theTextView.setBackgroundColor(style.mBackgroundColor);

		theTextView.setTextSize(1, Settings.getFont(this));

		finishedButton.setOnClickListener(new OnClickListener() {			// **** Done Button ****

			public void onClick(View v) {
				Run.TextInputString = theTextView.getText().toString();		// Grab the text that the user is seeing
				releaseLock();
				return;
			}
		});

	}

	public void releaseLock() {
		if (lockReleased) return;

		synchronized (Run.LOCK) {
			Run.mWaitForLock = false;
			lockReleased = true;
			Run.LOCK.notify();								// release the lock that Run is waiting for
		}
		finish();
	}

	@Override
	public void onDestroy() {
		releaseLock();										// if not already released, release the lock that Run is waiting for
		super.onDestroy();
	}

}
