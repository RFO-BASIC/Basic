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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


// Called from Basic.java when Basic.java
// is called from a launcher shortcut

public class AutoRun extends Activity {
	boolean FileNotFound = false;
    private static final String LOGTAG = "AutoRun";
    private static final String CLASSTAG = AutoRun.class.getSimpleName();
//  Log.v(AutoRun.LOGTAG, " " + AutoRun.CLASSTAG + " String Var Value =  " + d);

	private AddProgramLine APL ;

	@Override
    protected void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState);

//	   Log.v(AutoRun.LOGTAG, " " + AutoRun.CLASSTAG + " On Create PID  " + Basic.ProcessID);

		// The program filename is in the bundle placed in the intent extras by Basic.java

		Intent intent = getIntent();
		Bundle b = intent.getExtras();

		APL = new AddProgramLine();
		
		if (b != null) {											// If bundle is not empty
			String fn = b.getString("fn");							// then go load the file
			FileLoader(fn);											
		} else {													// if bundle is empty
			FileLoader("System Error");								// then we have a problem
		}

		// If the file has been found and loaded, then set the program up for running
		// and then go run it.

		// If the file was not found then create a error message program
		// and go to the Editor to display program which is an error message.

		if (!FileNotFound) {                                 // File found and loaded into Basic.lines
			// and display text buffer

// Note: This code could be optimized to eliminate the back an forth copying from the display
// text buffer and Basic.lines.

			Basic.lines = new ArrayList <String>();
			String Temp = "";

			if (!Basic.DoAutoRun) {
				String data = b.getString("data");
				Temp = "##$=\"" + data + "\"";
				APL.AddLine(Temp, true);
				Temp = "";
			}

			for (int k=0; k < Editor.DisplayText.length(); ++k) {      // Grab the display text
				if (Editor.DisplayText.charAt(k) == '\n') {			   // and add it to Basic.Lines
					APL.AddLine(Temp, true);							       // with some editing.
					Temp = "";								
				} else {
					Temp = Temp + Editor.DisplayText.charAt(k);
				}
			}
			if (Temp.length() != 0) APL.AddLine(Temp, true);				// Do not add empty lines

			if (Basic.lines.size() == 0) {						// If the program is empty
				Basic.lines.add("rem\n");					// add a single REM line
			}												// to keep Run happy
			Basic.theProgramRunner = new Intent(this, Run.class);		//now go run the program
			Basic.theRunContext = null;
			startActivity(Basic.theProgramRunner);
			finish();
		}
		finish();

	}





	private void  FileLoader(String aFileName) {							// Loads the selected file

		if (aFileName == null) aFileName = " ";

		boolean baseDriveChanged = false;
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

		BufferedReader buf = null;

		File file = new File(aFileName);
		try {  buf = new BufferedReader(new FileReader(file), 8096);} catch (FileNotFoundException e) {										// FNF should never happen
//			  Log.e(LoadFile.LOGTAG, e.getMessage(), e);
			FileNotFound = true;
		};
		// File now opened
		String data = null;
		Basic.clearProgram();
        Basic.lines.remove(0);
    	Editor.DisplayText = "";							
		int size = 0;
		if (!FileNotFound) {
			do
	        try {												// Read lines
            	data = buf.readLine();
	        	if (data == null) {								// if null, say EOF
            		data = "EOF";
        		} else {
        			Basic.lines.add(data);						// add the line 
        			size = size + data.length() + 1;
        		}
			} catch (IOException e) {
        		data = "EOF";
//				  Log.e(LoadFile.LOGTAG, e.getMessage(), e);
        	}
        	while (!data.equals("EOF"));						// while not EOF
			// File all read int
        	if (Basic.lines.isEmpty()) {
        		Basic.lines.add("!");
        		size = 2;
        	}
		} else {
			if (baseDriveChanged) 
				Basic.lines.add("! Shortcut created with different base drive.") ;
			else
				Basic.lines.add("! Shortcut Error: \"" + aFileName + "\" not found");
			
			Basic.DoAutoRun = false;
			startActivity(new Intent(this, Editor.class));
			finish();

		}
		StringBuilder sb = new StringBuilder(size);

		String s = Basic.lines.get(0);						// To the display buffer
		if (s.length() == 0) {Basic.lines.remove(0);}
		for (int i = 0; i < Basic.lines.size(); ++i) {
//            	Editor.DisplayText = Editor.DisplayText+ Basic.lines.get(i)+"\n";
			sb.append(Basic.lines.get(i) + "\n");
		}
		Editor.DisplayText = sb.toString();

		Basic.InitialProgramSize = Editor.DisplayText.length();		// Save the initial size for changed
		Basic.Saved = true;											// check
	}
	


}
