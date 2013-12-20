/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.


Copyright (C) 2010 - 2013 Paul Laughton

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

    You may contact the author, Paul Laughton at basic@laughton.com
    
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


//Log.v(Delete.LOGTAG, " " + Delete.CLASSTAG + " String Var Value =  " + d);

/* The User Interface used to delete files.
 * The user is presented with a list of files and directories from which
 * she can select files or directories to delete.
 */

public class Delete extends ListActivity {
    private static final String LOGTAG = "Delete File";
    private static final String CLASSTAG = Delete.class.getSimpleName();

    private Basic.ColoredTextAdapter mAdapter;
    private String FilePath = "";
    private String FL[] = null;
    private ArrayList<String> FL1 = new ArrayList<String>();

@Override
public void onCreate(Bundle savedInstanceState) {
	
  super.onCreate(savedInstanceState);
  setRequestedOrientation(Settings.getSreenOrientation(this));

  FilePath = Basic.AppPath;
  updateList();													// put file list in FL1

  mAdapter = new Basic.ColoredTextAdapter(this, FL1);
  setListAdapter(mAdapter);
  ListView lv = getListView();
  lv.setTextFilterEnabled(false);
  lv.setBackgroundColor(mAdapter.backgroundColor);

  // Listener waits for the user to select a file or directory. If the entry is a directory,
  // check to see if that directory is empty. If empty, it can be deleted. If not empty,
  // then display the list of files in that directory
  
  lv.setOnItemClickListener(new OnItemClickListener() {			// when user taps a filename line
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (position == 0) {									// if GoUp is selected
			FilePath = LoadFile.goUp(FilePath);					// change FilePath to its parent directory
			updateList();
		} else {												// up is not selected
			String path = FilePath + '/' + FL1.get(position);
			if (path.endsWith("(d)")) {							// if has (d), then is directory
				int k = path.length() - 3;
				path = path.substring(0, k);
				GetFileList(path);								// see if the directory is empty
				if (FL.length != 0) {							// not empty, don't delete it
					FilePath = path;							// display the files in it
					updateList();
					return;
				}
			}
			OptionalDelete(path);								// file or empty directory: delete it
		}
	}
  });
}

private void updateList(){

	File lbDir = GetFileList(FilePath);						// Get the list of files in this directory
	
	// Note: The checking for SD card present was done in the Editor just before calling delete


	// Go through the file list and mark directory entries with (d)
	ArrayList<String> dirs = new ArrayList<String>();
	ArrayList<String> files = new ArrayList<String>();
	String absPath = lbDir.getAbsolutePath() + '/';
	for (String s : FL) {
		File test = new File(absPath + s);
		if (test.isDirectory()) {							// If file is a directory, add "(d)"
			dirs.add(s + "(d)");							// and add to display list
		} else {
			files.add(s);									// else add name without the (d)
		}
	}
	Collections.sort(dirs);									// Sort the directory list
	Collections.sort(files);								// Sort the file list

	FL1.clear();
	FL1.add("..");											// put  the ".." to the top of the list
	FL1.addAll(dirs);										// copy the directory list to the adapter list
	FL1.addAll(files);										// copy the file list to the end of the adapter list

	if (mAdapter != null) { mAdapter.notifyDataSetChanged(); }
	Basic.toaster(this, "Select file to Delete");
}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event)  {                     // If user presses back key
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {	 // got back to the editor
//    	startActivity( new Intent(Delete.this, Editor.class));
    	finish();
        return true;
    }
    return super.onKeyUp(keyCode, event);

//    return super.onKeyDown(keyCode, event);
}

	private File GetFileList(String path){
		
		// Get the list of files in a directory
		
		Basic.InitDirs();										// Make sure we have base directories.

		File lbDir = new File(Basic.getBasePath()				// Base dir is sdcard
						 + "/" + path);							// plus delete file path
		FL = lbDir.list();
		if (FL == null) {
			String F[] = {" "};
			FL = F;												// If the list was null, make a blank entry.
		}
		return lbDir;
	}

    private void OptionalDelete(final String theFileName){		// we have a named file or empty directory to delete
    															// ask the user if she really wants to delete it
		String thePath = Basic.getBasePath()					// Base dir is sdcard
					+ '/' + theFileName;						// plus delete file path
		final File theFile = new File(thePath);

    	AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);		// using a dialog box.

    	alt_bld.setMessage("Delete " + theFileName)							// give the user the selected file name
    	.setCancelable(false)												// Do not allow user BACK key out of dialog
    	
// The click listeners ****************************
    	.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

    	public void onClick(DialogInterface dialog, int id) {				
    																		// Action for 'Yes' Button
        	dialog.cancel();
        	theFile.delete();												// Delete the file
        	updateList();
    	}
    	})
    	.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int id) {
    																		// Action for 'NO' Button
    		dialog.cancel();												// Do not delete the file
    	}
    	});
// End of Click Listeners ****************************************
    	
    	AlertDialog alert = alt_bld.create();								// Display the dialog
    	alert.setTitle("Confirm Delete");
    	alert.show();
    
    
    }
}
