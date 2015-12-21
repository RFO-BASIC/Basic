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

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import static com.rfo.basic.Editor.GO_UP;
import static com.rfo.basic.Editor.addDirMark;
import static com.rfo.basic.Editor.isMarkedDir;
import static com.rfo.basic.Editor.stripDirMark;
import static com.rfo.basic.Editor.getDisplayPath;
import static com.rfo.basic.Editor.goUp;
import static com.rfo.basic.Editor.quote;


//Log.v(Delete.LOGTAG, "String Var Value = " + d);

/* The User Interface used to delete files.
 * The user is presented with a list of files and directories from which
 * she can select files or directories to delete.
 */

public class Delete extends ListActivity {
	private static final String LOGTAG = "Delete File";

	private Basic.ColoredTextAdapter mAdapter;
	private Toast mToast = null;

	private String mDirPath;									// absolute current path
	private ArrayList<String> FL = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setRequestedOrientation(Settings.getSreenOrientation(this));

		Basic.InitDirs();										// Make sure we have base directories.

		mDirPath = Basic.getFilePath();
		updateList();											// put file list in FL for display

		mAdapter = new Basic.ColoredTextAdapter(this, FL, Basic.defaultTextStyle);
		setListAdapter(mAdapter);
		ListView lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setBackgroundColor(mAdapter.getBackgroundColor());

		// Listener waits for the user to select a file or directory. If the entry is a directory,
		// check to see if that directory is empty. If empty, it can be deleted. If not empty,
		// then display the list of files in that directory

		lv.setOnItemClickListener(new OnItemClickListener() {	// when user taps a filename line
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// If the selection is the top line, ignore it.
				if (position == 0)		return;

				String fileName = FL.get(position);
				if (fileName.equals(GO_UP)) {					// if GoUp is selected
					mDirPath = goUp(mDirPath);					// adjust path upward
					updateList();								// and display
				} else {										// up is not selected
					if (isMarkedDir(fileName)) {				// if has (d), then is directory
						fileName = stripDirMark(fileName);		// remove the (d)

						if (goDown(fileName)) {					// if non-empty directory, adjust path
							updateList();						// and display the files in it
							return;
						}
					}
					OptionalDelete(fileName);					// file or empty directory: delete it
				}
			}
		});
	}

	private boolean goDown(String dirName) {					// if dir is not empty, add it to the path
		File dir = new File(mDirPath, dirName);					// add dir name to copy of path, with slashes fixed
		String[] fileList = GetFileList(dir);					// see if the directory is empty
		if (fileList.length != 0) {
			mDirPath = dir.getPath();							// not empty: commit to new path
			return true;
		}
		return false;											// empty: do not change path
	}

	private void updateList() {
		// Note: The checking for SD card present was done in the Editor just before calling delete

		// Go through the file list and mark directory entries with (d)
		ArrayList<String> dirs = new ArrayList<String>();
		ArrayList<String> files = new ArrayList<String>();
		File dir = new File(mDirPath);
		for (String s : GetFileList(dir)) {						// for each file in the directory
			File test = new File(mDirPath, s);
			if (test.isDirectory()) {							// If file is a directory, add "(d)"
				dirs.add(addDirMark(s));						// and add to display list
			} else {
				files.add(s);									// else add name without the (d)
			}
		}
		Collections.sort(dirs);									// Sort the directory list
		Collections.sort(files);								// Sort the file list

		FL.clear();
		FL.add("Path: " + quote(getDisplayPath(mDirPath)));		// put the path at the top of the list - will not be selectable
		FL.add(GO_UP);											// put  the ".." above the directory contents
		FL.addAll(dirs);										// copy the directory list to the adapter list
		FL.addAll(files);										// copy the file list to the end of the adapter list

		if (mAdapter != null) { mAdapter.notifyDataSetChanged(); }

		if (mToast != null) { mToast.cancel(); }
		mToast = Basic.toaster(this, "Select file to Delete");	// tell the user what to do using Toast
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {						// If user presses back key
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {	// got back to the editor
			if (mToast != null) { mToast.cancel(); mToast = null; }
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);

//		return super.onKeyDown(keyCode, event);
	}

	private String[] GetFileList(File dir) {

		// Get the list of files in a directory
		String fileList[] = dir.list();
		if (fileList == null) {									// null means not a directory
			fileList = new String[] {" "};						// make a blank entry.
		}
		return fileList;
	}

	private void OptionalDelete(final String fileName) {		// we have a named file or empty directory to delete
																// ask the user if she really wants to delete it
		String thePath = mDirPath + File.separatorChar + fileName;
		final File theFile = new File(thePath);

		AlertDialog.Builder alertBuild = new AlertDialog.Builder(this);

		alertBuild
			.setMessage("Delete " + fileName)					// give the user the selected file name
			.setCancelable(false)								// do not allow user BACK key out of dialog

// The click listeners ****************************
		.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {			// Action for 'Yes' Button
				dialog.cancel();
				Log.i(LOGTAG, "Selection: " + quote(fileName));
				theFile.delete();											// Delete the file
				updateList();
			}
		})

		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {			// Action for 'NO' Button
				dialog.cancel();											// Do not delete the file
			}
		});
// End of Click Listeners ****************************************

		AlertDialog alert = alertBuild.create();							// display the dialog
		alert.setTitle("Confirm Delete");
		alert.show();
	}
}
