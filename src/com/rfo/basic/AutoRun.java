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

import android.content.Context;
import android.content.Intent;
import android.util.Log;


// Called from Basic.java when Basic.java
// is called from a launcher shortcut

public class AutoRun {
	private static final String LOGTAG = "AutoRun";

	private final Context mContext;
	private final String mFileName;
	private final boolean mFromRun;						// false if started by shortcut,
														// true if started by RUN command
	private final String mData;							// RUN command can add a data string

	public AutoRun(Context context, String filename, boolean fromRun, String data) {
		Log.v(AutoRun.LOGTAG, filename);
		mContext = context;
		mFileName = filename;
		mFromRun = fromRun;
		mData = data;
	}

	public Intent load() {		// load a program file and create an Intent to run it
		boolean fileFound;
		if ((mFileName != null) && !mFileName.equals("")) {	// if there is a file name
			fileFound = FileLoader(mFileName);			// then go load the file
			Log.d(LOGTAG, "Run file " + mFileName);
		} else {										// if no file name
			fileFound = FileLoader("System Error");		// then we have a problem
		}

		// If the file has been found and loaded, then set the program up for running
		// and then go run it.
		// If the file was not found then an error message program was created.
		// Go to the Editor to display it.
		Intent intent;
		if (fileFound) {								// F\file found and loaded into display text buffer

// Note:	This code could be optimized to eliminate the back and forth
//			copying from the display text buffer and Basic.lines.
// TODO:	The loading should be in a background thread.

			AddProgramLine APL = new AddProgramLine();	// creates new Basic.lines

			if (mData != null) {						// if RUN command added a data string
				String data = "##$=\"" + mData + "\"";
				AddProgramLine.charCount = data.length();
				APL.AddLine(data);
			}

			Basic.loadProgramFromString(Editor.DisplayText, APL);	// build program in Basic.lines

			if (Basic.lines.size() == 0) {							// If the program is empty
				Basic.lines.add(new Run.ProgramLine("rem\n"));		// add a single REM line
			}														// to keep Run happy
			intent = new Intent(mContext, Run.class);				// create Intent to run the program
		} else {													// File not found
			Basic.DoAutoRun = false;								// Load error message program
			intent = new Intent(mContext, Editor.class);			// into Editor so user can see it.
		}
		return intent;
	}

	private boolean FileLoader(String aFileName) {					// Loads the selected file

		// File name from old-style shortcut starts with "/source/".
		// File name from new-style shortcut is full absolute path.
		// File name from RUN command is relative to "<AppPath>/source".

		if (aFileName == null) aFileName = " ";

		boolean baseDriveChanged = false;
		if (!mFromRun) {											// file name from shortcut may need massage
			String filePath = Basic.getFilePath();
			int z = aFileName.indexOf(filePath);
			if (z == -1) {
				if (aFileName.startsWith("/source/"))				// Legacy shortcut
					aFileName = filePath + aFileName;
				else {
					baseDriveChanged = true;
					//aFileName = " fubar";
				}
			}
		}

		Basic.clearProgram(); 				// Clear the old program
		Editor.DisplayText = "";			// Clear the display text buffer

		ArrayList<String> lines = new ArrayList<String>();
		int size = Basic.loadProgramFileToList(!mFromRun, aFileName, lines);
		Boolean found = (size != 0);		// size is 0 if file not found
		if (!found) {
			// Special case of file not found and doing Launcher Shortcut.
			// Turn the program file into an error message and act as if we loaded a file.
			String msg = baseDriveChanged
				? "! Shortcut created with different base drive."
				: "! Shortcut Error: \"" + aFileName + "\" not found";
			lines.add(msg);
			size = msg.length() + 1;
		}

		// The file is now loaded into a String ArrayList. Next we need to move
		// the lines into the Editor display buffer.

		Editor.DisplayText = Basic.loadProgramListToString(lines, size);
		return found;
	}

}
