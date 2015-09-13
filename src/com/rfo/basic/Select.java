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

import com.rfo.basic.Basic.ColoredTextAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;


//Log.v(Select.LOGTAG, " " + Select.CLASSTAG + " String Var Value =  " + d);

/* The User Interface used to select items from a list.
 * The user is presented with a list of things from which
 * she can select a thing.
 */

public class Select extends ListActivity {
	private static final String LOGTAG = "Select";

	public static final String EXTRA_TITLE = "select_title";
	public static final String EXTRA_MSG   = "select_msg";
	public static final String EXTRA_LIST  = "select_list";

	private boolean lockReleased;			// safety valve so interpreter doesn't get hung if this
											// instance is destroyed without first releasing the LOCK

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		String title = intent.getStringExtra(EXTRA_TITLE);
		String message = intent.getStringExtra(EXTRA_MSG);
		ArrayList<String> list = intent.getStringArrayListExtra(EXTRA_LIST);

		lockReleased = false;

		ColoredTextAdapter adapter = new ColoredTextAdapter(this, list, Basic.defaultTextStyle);
		setListAdapter(adapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(false);
		if (title != null) { setTitle(title); }
		lv.setBackgroundColor(
			Settings.getEmptyConsoleColor(this).equals("line")
				? adapter.getLineColor()
				: adapter.getBackgroundColor());			// default is "background"

		// Wait for user to select something

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {	// when user long-presses a filename line
			public boolean  onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				setSelection(position + 1, true);			// convert to 1-based item index
				return true;
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {			// when user short-taps a filename line
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setSelection(position + 1, false);			// convert to 1-based item index
			}
		});

		if ((message != null) && !message.equals("")) {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();	// Display the user's toast
		}
	} // onCreate

	public void setSelection(int item, boolean isLongClick) {
		if (lockReleased) return;

		synchronized (Run.LOCK) {
			Run.SelectedItem = item;						// 1-based index of selected item
			Run.SelectLongClick = isLongClick;
			Run.mWaitForLock = false;
			lockReleased = true;
			Run.LOCK.notify();								// release the lock that Run is waiting for
		}
		finish();
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {				// If user presses back key
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { // go back to Run without a selection
			setSelection(0, false);							// zero indicates no selection made
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	public void onDestroy() {
		setSelection(0, false);						// if not already released, release the lock that Run is waiting for
		super.onDestroy();
	}

}
