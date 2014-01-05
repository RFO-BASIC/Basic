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

    You may contact the author, Paul Laughton, at basic@laughton.com
    
	*************************************************************************************************/

package com.rfo.basic;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


//Log.v(LoadFile.LOGTAG, " " + LoadFile.CLASSTAG + " String Var Value =  " + d);

// Loads a file. Called from the Editor when user selects Menu->Load

public class LoadFile extends ListActivity {
  private static final String LOGTAG = "Load File";
  private static final String CLASSTAG = LoadFile.class.getSimpleName();
  private Basic.ColoredTextAdapter mAdapter;
  private String ProgramPath = "";								// Load file directory path
  private ArrayList<String> FL1 = new ArrayList<String>();

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setRequestedOrientation(Settings.getSreenOrientation(this));

	updateList();												// put file list in FL1

	mAdapter = new Basic.ColoredTextAdapter(this, FL1);			// Display the list
	setListAdapter(mAdapter);
	ListView lv = getListView();
	lv.setTextFilterEnabled(false);
	lv.setBackgroundColor(mAdapter.backgroundColor);

	// The click listener for the user selection *******************

	lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			// User has selected a file.
			// If the selection is a directory, change the program path
			// and then display the list of files in that directory
			// otherwise load the selected file

			if (!SelectionIsFile(position)){
				Basic.SD_ProgramPath = ProgramPath;
				updateList();
			} else {
				FileLoader(FL1.get(position));
				return;
			}
		}
	});

	// End of Click Listener **********************************************
}

private void updateList(){

	 ProgramPath = Basic.SD_ProgramPath;                 // Set Load path to current program path
  	 File lbDir = new File(Basic.getSourcePath(ProgramPath));
	 lbDir.mkdirs();

	 String[] FL = lbDir.list();									// Get the list of files in this dir
	 if (FL == null){
		Basic.toaster(this, "System Error. File not directory");
		return;
	 }
	 

	// Go through the list of files and mark directories with (d)
	// also only display files with the .bas extension
	ArrayList<String> dirs = new ArrayList<String>();
	ArrayList<String> files = new ArrayList<String>();
	String absPath = lbDir.getAbsolutePath() + '/';
	for (String s : FL) {
		File test = new File(absPath + s);
		if (test.isDirectory()) {								// If file is a directory, add "(d)"
			dirs.add(s + "(d)");								// and add to display list
		} else {
			if (s.endsWith(".bas")) {							// 	Only put files ending in
				files.add(s);									// .bas into the display list
			}
		}
	}
	Collections.sort(dirs);									// Sort the directory list
	Collections.sort(files);								// Sort the file list

	FL1.clear();
	FL1.add("..");											// put  the ".." to the top of the list
	FL1.addAll(dirs);										// copy the directory list to the adapter list
	FL1.addAll(files);										// copy the file list to the end of the adapter list

	if (mAdapter != null) { mAdapter.notifyDataSetChanged(); }

	Basic.toaster(this, "Select File To Load");					// Tell the user what to do using Toast
}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event)  {						// If back key pressed
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {      // and the key is the BACK key
       finish();																// then done with LoadFile
       return true;
    }
    return super.onKeyUp(keyCode, event);
}

	public static String goUp(String path){					// Return the parent directory
		if (path.equals("")) { return path; }					// if up at top level dir, just return
		if (!path.contains("/")) {								// if path does not contain a /
			return "";											// then path now empty
		}
		int k = path.lastIndexOf('/');							// find the right most /
		return path.substring(0, k);							// eliminate / to end to make a new path
	}

	private boolean SelectionIsFile(int Index){
		
		// Test to see if the user selection is a file or a directory
		// If is directory, then change the path so that the files
		// in that directory will be displayed.
		
		String theFileName = FL1.get(Index);
		if (Index == 0) {										// if GoUp is selected
			ProgramPath = goUp(ProgramPath);					// change FilePath to its parent directory
			return false;										// report this is not a file
		}
																	// Not UP, is selection a dir
		if (theFileName.endsWith("(d)")) {							// if has (d), then is directory
			int k = theFileName.length() - 3;
			theFileName = theFileName.substring(0, k);
			ProgramPath = ProgramPath + "/" + theFileName;			// then add this dir to the path
			return false;											// and report not a file
		}
		return true;												// if none of the above, it is a file
	}

	private void FileLoader(String aFileName) {					// The user has selected a file to load, load it.

		Basic.clearProgram(); 				// Clear the old program
		Editor.DisplayText = "";			// Clear the display text buffer

		Basic.ProgramFileName = aFileName;

		String FullFileName = Basic.getSourcePath( 							// Base Source dir 
				Basic.SD_ProgramPath + File.separatorChar +					// plus load path
				aFileName);													// plus filename

		ArrayList<String> lines = new ArrayList<String>();
		int size = Basic.loadProgramFileToList(FullFileName, lines);		// is full path to the file to load
		if (size == 0) {					// File not found - this should never happen
			// Turn the program file into an error message
			// and act as if we loaded a file.
			String msg = "! Load Error: \"" + aFileName + "\" not found";
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
		finish();													// LoadFile is done
	}

}
