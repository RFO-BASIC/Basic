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

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

import com.rfo.basic.Basic.ColoredTextAdapter;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import static com.rfo.basic.Editor.GO_UP;
import static com.rfo.basic.Editor.addDirMark;
import static com.rfo.basic.Editor.isMarkedDir;
import static com.rfo.basic.Editor.stripDirMark;
import static com.rfo.basic.Editor.getDisplayPath;
import static com.rfo.basic.Editor.goUp;
import static com.rfo.basic.Editor.quote;


// Loads a file. Called from the Editor when user selects Menu->Load

public class LoadFile extends ListActivity {
	private static final String LOGTAG = "Load File";

	private Basic.ColoredTextAdapter mAdapter;
	private Toast mToast = null;

	private String mProgramPath;										// load file directory path
	private ArrayList<String> FL = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(Settings.getSreenOrientation(this));

		mProgramPath = Editor.ProgramPath;							// set Load path to current program path
		updateList();												// put file list in FL

		mAdapter = new ColoredTextAdapter(this, FL, Basic.defaultTextStyle);	// Display the list
		setListAdapter(mAdapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setBackgroundColor(mAdapter.getBackgroundColor());

		// The click listener for the user selection *******************

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// User has selected a line.
				// If the selection is the top line, ignore it.
				if (position == 0)		return;

				// If the selection is a directory, change the program path
				// and then display the list of files in that directory.
				// Otherwise load the selected file and tell the Editor it was loaded.

				String fileName = FL.get(position);
				Log.i(LOGTAG, "Selection: " + quote(fileName));
				if (!SelectionIsFile(fileName)) {
					updateList();
				} else {
					FileLoader(fileName);
					setResult(RESULT_OK);
					if (mToast != null) { mToast.cancel(); mToast = null; }
					finish();									// LoadFile is done
					return;
				}
			}
		});

		// End of Click Listener **********************************************
	}

	private void updateList() {

		File dir = new File(mProgramPath);
		dir.mkdirs();

		String[] fileList = dir.list();							// Get the list of files in this dir
		if (fileList == null) {
			String msg = dir.exists() ? "File not directory" : "Source directory does not exist";
			if (mToast != null) { mToast.cancel(); }
			mToast = Basic.toaster(this, "System Error: " + msg);
			return;
		}

		// Go through the list of files and mark directories with "(d)".
		// Display only files with the .bas extension.
		ArrayList<String> dirs = new ArrayList<String>();
		ArrayList<String> files = new ArrayList<String>();
		String absPath = dir.getAbsolutePath() + '/';
		for (String s : fileList) {
			File test = new File(absPath + s);
			if (test.isDirectory()) {							// If file is a directory, add "(d)"
				dirs.add(addDirMark(s));						// and add to display list
			} else {
				if (s.toLowerCase(Locale.getDefault()).endsWith(".bas")) { // Only put files ending in
					files.add(s);								// .bas into the display list
				}
			}
		}
		Collections.sort(dirs);									// sort the directory list
		Collections.sort(files);								// sort the file list

		FL.clear();
		FL.add("Path: " + quote(getDisplayPath(mProgramPath))); // put the path at the top of the list - will not be selectable
		FL.add(GO_UP);											// put  the ".." above the directory contents
		FL.addAll(dirs);										// copy the directory list to the adapter list
		FL.addAll(files);										// copy the file list to the end of the adapter list

		if (mAdapter != null) { mAdapter.notifyDataSetChanged(); }

		if (mToast != null) { mToast.cancel(); }
		mToast = Basic.toaster(this, "Select File To Load");	// tell the user what to do using Toast
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {						// If back key pressed
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {	// and the key is the BACK key
			setResult(RESULT_CANCELED);
			if (mToast != null) { mToast.cancel(); mToast = null; }
			finish();															// then done with LoadFile
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private boolean SelectionIsFile(String fileName) {
		
		// Test to see if the user selection is a file or a directory
		// If is directory, then change the path so that the files
		// in that directory will be displayed.
		
		if (fileName.equals(GO_UP)) {							// if GoUp is selected
			mProgramPath = goUp(mProgramPath);					// change path to its parent directory
			return false;										// report this is not a file
		}
																// Not UP, is selection a dir
		if (isMarkedDir(fileName)) {							// if has (d), then is directory
			fileName = stripDirMark(fileName);					// remove the (d)
			mProgramPath = new File(mProgramPath, fileName).getPath();	// add dir to path
			return false;										// and report not a file
		}
		return true;											// if none of the above, it is a file
	}

	private void FileLoader(String aFileName) {					// The user has selected a file to load, load it.

		Basic.clearProgram(); 				// Clear the old program
		Editor.DisplayText = "";			// Clear the display text buffer

		Editor.ProgramPath = mProgramPath;
		Editor.ProgramFileName = aFileName;

		String FullFileName = new File(mProgramPath, aFileName).getPath();

		ArrayList<String> lines = new ArrayList<String>();
		int size = Basic.loadProgramFileToList(true, FullFileName, lines);	// is full path to the file to load
		if (size == 0) {					// File not found - this should never happen
			// Turn the program file into an error message
			// and act as if we loaded a file.
			String msg = "! Load Error: " + quote(aFileName) + " not found";
			lines.add(msg);
			size = msg.length() + 1;
		}

		// The file is now loaded into a String ArrayList. Next we need to move
		// the lines into the Editor display buffer.

		Editor.DisplayText = Basic.loadProgramListToString(lines, size);
		Editor.InitialProgramSize = Editor.DisplayText.length();		// Save the initial size for changed check
		Editor.Saved = true;
		if (Editor.mText == null) {
			throw new RuntimeException("LoadFile: Editor.mText null");
		}
		Editor.mText.setText(Editor.DisplayText);
	}

}
