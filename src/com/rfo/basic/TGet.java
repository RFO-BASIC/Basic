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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.EditText;


public class TGet extends Activity {
	// Names for Intent extras
	public static final String TITLE = Basic.mBasicPackage + ".title";
	public static final String CONSOLE_TEXT = Basic.mBasicPackage + ".consoletext";
	public static boolean mMenuStop = false;				// set true if user selects Stop from menu

	private EditText theTextView;							// The EditText TextView
	private int PromptIndex;
	private String theText;

	private boolean lockReleased;			// safety valve so interpreter doesn't get hung if this
											// instance is destroyed without first releasing the LOCK

	@Override
	public void onBackPressed() {
		Run.mEventList.add(new Run.EventHolder(Run.EventHolder.BACK_KEY_PRESSED, 0, null));	// tell RunLoop
		returnText("");										// tell TGet command it happened
															// and end TGet Activity
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {			// Called when the menu key is pressed.
		super.onCreateOptionsMenu(menu);
		if (!Settings.getConsoleMenu(this)) { return false; }

		getMenuInflater().inflate(R.menu.run, menu);		// Same menu as Run...
		menu.removeItem(R.id.editor);						// ... except no "Editor" item
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	// A menu item is selected
		switch (item.getItemId()) {

		case R.id.stop:										// User wants to stop execution
			mMenuStop = true;								// Tell program runner (and user) it happened

			returnText("");									// Tell TGet command it happened
															// and end TGet Activity
		}
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.tget);					// Layouts xmls exist for both landscape or portrait modes

		Intent intent = getIntent();
		String title = intent.getStringExtra(TITLE);
		if (title != null) setTitle(title);
		ArrayList<String> consoleText = intent.getStringArrayListExtra(CONSOLE_TEXT);
		if (theText == null) theText = "";

		lockReleased = false;

		theTextView = (EditText) findViewById(R.id.the_text);	// The text display area

		int count = consoleText.size();
		for (int i = 0; i < count; ++ i) {
			theText = theText + consoleText.get(i) + '\n';
		}

		theText = theText + Run.TextInputString ;
		PromptIndex = theText.length();

		theTextView.setText(theText);							// The Editor's display text
		theTextView.setTypeface(Typeface.MONOSPACE);
		theTextView.setSelection(theText.length());

		Basic.TextStyle style = Basic.defaultTextStyle;			// Get text color from Settings
		theTextView.setTextColor(style.mTextColor);
		theTextView.setBackgroundColor(style.mBackgroundColor);
		theTextView.setCursorVisible(true);

		theTextView.setTextSize(1, Settings.getFont(this));

		theTextView.addTextChangedListener(inputTextWatcher);

		theTextView.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// If the event is a key-down event on the "enter" button
				if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
						(keyCode == KeyEvent.KEYCODE_ENTER)) {
					handleEOL();
					return true;
				}
				return false;
			}
		});
	}

	private synchronized void handleEOL() {
		Log.d("TGet","handleEOL");
		if (lockReleased) return;							// Don't run twice for same event

		String s = theTextView.getText().toString();
		s = (PromptIndex < s.length()) ? s.substring(PromptIndex) : "";
		returnText(s);
	}

	private TextWatcher inputTextWatcher = new TextWatcher() {
		public void afterTextChanged(Editable s) { }
		public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			if (before > 0) return;
			char c = s.charAt(start);
			if (c == '\n') {
				handleEOL();
			}
		}
	};

	public void returnText(String text) {
		if (lockReleased) return;

		synchronized (Run.LOCK) {
			Run.TextInputString = text;
			Run.mWaitForLock = false;
			lockReleased = true;
			Run.LOCK.notify();								// release the lock that Run is waiting for
		}
		finish();
		overridePendingTransition(0, 0);
	}

	@Override
	public void onDestroy() {
		returnText("");										// if not already released, release the lock that Run is waiting for
		super.onDestroy();
	}

}
