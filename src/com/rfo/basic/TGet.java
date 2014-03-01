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
	private EditText theTextView;							// The EditText TextView
	private int PromptIndex;
	private String theText;

	private boolean lockReleased;			// safety valve so interpreter doesn't get hung if this
											// instance is destroyed without first releasing the LOCK

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)  {
		// if BACK key cancel user input
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

			if (Run.OnBackKeyLine != 0) {					// Tell program runner it happened
				Run.BackKeyHit = true;
			} else {
				Run.Stop = true;
			}

			returnText("");									// Tell TGet command it happened
															// and end TGet Activity
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {			// Called when the menu key is pressed.
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.run, menu);		// Same menu as Run...
		menu.removeItem(R.id.editor);						// ... except no "Editor" item
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	// A menu item is selected
		switch (item.getItemId()) {

		case R.id.stop:										// User wants to stop execution
			Run.MenuStop();									// Tell program runner (and user) it happened

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
       String title = intent.getStringExtra("title");
       if (title != null) setTitle(title);

		lockReleased = false;

       theTextView = (EditText) findViewById(R.id.the_text);	// The text display area

       theText = "";
       for (int i = 0; i < Run.output.size(); ++ i) {
    	   theText = theText + Run.output.get(i) + '\n';
       }

       theText = theText + Run.TextInputString ;
       PromptIndex = theText.length();

       theTextView.setText(theText);							// The Editor's display text
       theTextView.setTypeface(Typeface.MONOSPACE);
       theTextView.setSelection(theText.length());

       Basic.ScreenColors colors = new Basic.ScreenColors();	// Get text color from Settings
       theTextView.setTextColor(colors.textColor);
       theTextView.setBackgroundColor(colors.backgroundColor);
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
		if (Run.HaveTextInput) return;							// Don't run twice for same event

		String s = theTextView.getText().toString();
		s = (PromptIndex < s.length()) ? s.substring(PromptIndex) : "";
		returnText(s);
	}

       private TextWatcher inputTextWatcher = new TextWatcher() {
    	    public void afterTextChanged(Editable s) { }
    	    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    	        { }
    	    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	    	Log.d("start ", " " + start);
    	    	Log.d("before ", " " + before);
    	    	Log.d("count ", " " + count);
    	    	if (before>0) return;
    	    	char c = s.charAt(start);
   	    		if (c=='\n') {
   	    			handleEOL();
    	    	}
    	    }
    	};

	public void returnText(String text) {
		if (lockReleased) return;

		synchronized (Run.LOCK) {
			Run.TextInputString = text;
			Run.HaveTextInput = true;
			lockReleased = true;
			Run.LOCK.notify();								// release the lock that Run is waiting for
		}
		finish();
	}

	@Override
	public void onDestroy() {
		returnText("");										// if not already released, release the lock that Run is waiting for
		super.onDestroy();
	}

}
