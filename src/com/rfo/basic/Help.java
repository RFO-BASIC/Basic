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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.rfo.basic.Basic.ColoredTextAdapter;
import com.rfo.basic.Basic.TextStyle;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


// Called from Editor when user selected Menu->Commands
// Displays command list residing in f01_commands.bas

public class Help extends ListActivity {
	private ListView lv;
	private ArrayList <String> output = new ArrayList<String>();
	private static final String Chars = "abcdefghijklmnopqrstuvwxyz";
	private InputMethodManager IMM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TextStyle style = new TextStyle(Basic.defaultTextStyle, Typeface.MONOSPACE);
		ColoredTextAdapter AA = new ColoredTextAdapter(this, output, style);
		lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setBackgroundColor(AA.getBackgroundColor());

		setRequestedOrientation(Settings.getSreenOrientation(this));

		String commandFile = "f01_commands.bas";
		InputStream inputStream = null;
		try { inputStream = Basic.streamFromResource(Basic.SOURCE_SAMPLES_PATH, commandFile); }
		catch (Exception ex) { }

		if (inputStream != null) {
			InputStreamReader inputreader = new InputStreamReader(inputStream);
			BufferedReader buffreader = new BufferedReader(inputreader, 8192);
			String line;

			try {
				while ((line = buffreader.readLine()) != null) {		// Read one line at a time
					output.add(line);
				}
			} catch (IOException e1) {
				output.add("Error reading command list " + commandFile);
				Basic.closeStreams(buffreader, null);
			}
		} else {
			output.add("Command list " + commandFile + " not found");
		}

		setListAdapter(AA);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
																// If a line is clicked on
				String command = output.get(position);			// Get the command
				int index = command.lastIndexOf (",");			// trim off the page number
				if (index >= 0) command = command.substring(0, index);

				ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				clipboard.setText(command);						// Put the user expression into the clipboard
				finish();										// Exit back to Editor
			}
		});

		IMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		IMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			finish();
			return true;
		}

		int n;
		char theChar;
		int index = 0;

		if (keyCode >=29 && keyCode <= 54){
			n = keyCode -29;
			theChar = Chars.charAt(n);

			for (int i = 0; i < output.size(); ++i){
				String line = output.get(i);
				char c = line.charAt(0);
				c = Character.toLowerCase(c);
				if (c == theChar){
					lv.setSelection(i);
//					lv.smoothScrollToPosition(i);
					break;
				}
				index = index + line.length();
			}
		}
		return super.onKeyUp(keyCode, event);
	}

}
