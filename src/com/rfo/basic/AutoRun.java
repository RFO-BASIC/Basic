/****************************************************************************************************


 BASIC! is an implementation of the Basic programming language for
 Android devices.

 Copyright (C) 2010, 2011, 2012 Paul Laughton

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

import com.rfo.basic.Basic;
import com.rfo.basic.Run;
import com.rfo.basic.Editor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import android.content.Context;
import android.view.Gravity;
// Called from Basic.java when Basic.java
// is called from a launcher shortcut

public class AutoRun extends Activity {
	private int lc = 0;
	boolean FileNotFound = false;
    private static final String LOGTAG = "AutoRun";
    private static final String CLASSTAG = AutoRun.class.getSimpleName();
//  Log.v(AutoRun.LOGTAG, " " + AutoRun.CLASSTAG + " String Var Value =  " + d);

    private static boolean BlockFlag = false;
	private static String stemp ="";
	
	private AddProgramLine APL ;

	@Override
    protected void onCreate(Bundle savedInstanceState) {   
		super.onCreate(savedInstanceState);

//	   Log.v(AutoRun.LOGTAG, " " + AutoRun.CLASSTAG + " On Create PID  " + android.os.Process.myPid());

		// The program filename is in the bundle placed in the intent extras by Basic.java

		Intent intent = getIntent();
		Bundle b = intent.getExtras();

		APL = new AddProgramLine();
		APL.AddProgramLine();
		
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
			BlockFlag = false;

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


	/*
	private void AddLine(String line) {


		// Look for block comments. All lines between block comments
		// are tossed out


		String Temp = "";

		if (BlockFlag) {
			if (line.startsWith("!!")) {
				BlockFlag = false;
			}
			return;
		}

		if (line.startsWith("!!")) {
			BlockFlag = true;
			return;
		}

  		for (int i=0; i < line.length(); ++i) {		// do not mess with characters
			char c = line.charAt(i);				// between quote marks
			if (c == '“') c = '"';					// Change funny quote to real quote
			if (c == '"') {
				do {
					try {
						if (line.charAt(i + 1) == '=') {
							if (c == '+') {
								Temp += "{+&=}";
								i += 1;c = 0;
							} else if (c == '-') {
								Temp += "{-&=}";
								i += 1;c = 0;
							} else if (c == '*') {
								Temp += "{*&=}";
								i += 1;c = 0;
							} else if (c == '/') {
								Temp += "{/&=}";
								i += 1;c = 0;
							}
						}
					} catch (StringIndexOutOfBoundsException e) {
						Log.e("Format", e.getMessage(), e);
					}

					if (c == '\\') {					// look for \"
						if (i + 1 >= line.length()) {
							line = line + ' ';
						}
						if (line.charAt(i + 1) == '"') {    // and retain it 
							++i;						// so that user can have quotes in strings
							Temp = Temp + '\\';
							Temp = Temp + '"';
						} else if (line.charAt(i + 1) == 'n') {
							++i;
							Temp = Temp + '\r';
						}
					} else Temp = Temp + c;

					++i;
					if (i >= line.length()) { c = '"';} else {c = line.charAt(i);}				// just add it in
					if (c == '�') c = '"';					// Change funny quote to real quote
				}while (i < line.length() && c != '"');
				Temp = Temp + c;
			} else if (c == '%') {					// if the % character appears,
				break;								// drop it and the rest of the line
		    } else if (c == ' ') {
				if ((i + 1) < line.length())
					if (line.charAt(i + 1) == '_') {
						Temp = Temp + "{+nl}";
						break;
					}          					 // drop the rest of the line	
			} else if (c != ' ' && c != '\t') {		// toss out spaces and tabs
				c = Character.toLowerCase(c);		// convert to lower case
				Temp = Temp + c;						// and add it to the line
			}
		}

   		if (Temp.startsWith("include")) {            // If include, 
   			doInclude(Temp);                        // Do the include
   			return;
   		}


  		if (Temp.startsWith("rem")) {Temp = "";}		// toss out REM lines
  		if (Temp.startsWith("!"))Temp = "";			// and psuedo rem lines
  		if (!Temp.equals("")) {						// and empty lines
			if (Temp.endsWith("{+nl}")) {    			// test for include next line sequence
				stemp = stemp + Temp.substring(0, Temp.length() - 5);  // remove the include next line sequence and collect it
				return;
			} else {
				Temp = stemp + Temp; 					// add stemp collection to line
				stemp = "";							// clear the collection
			}
		    Temp = shorthand(Temp);
		    Temp = Temp.replace("{+&=}", "+=");
		    Temp = Temp.replace("{-&=}", "-=");
		    Temp = Temp.replace("{*&=}", "*=");
		    Temp = Temp.replace("{/&=}", "/=");
  			Temp = Temp + "\n";						// end the line with New Line
  			Basic.lines.add(Temp);					// add to Basic.lines
  			++lc;
			//Toaster(Temp);
  		}

	}

	private String shorthand(String line) {
		int ll = line.length();
		int then = line.indexOf("then");
		if (then == -1) {
			if (line.startsWith("++") || line.startsWith("--")) {
				line = line.substring(2, ll) + "=1" + line.substring(1, ll);
			}
			if (line.endsWith("++") || line.endsWith("--")) {
				line = line.substring(0, ll - 2) + "=" + line.substring(0, ll - 1) + "1";
			}
			int pe = line.indexOf("+=");
			int me = line.indexOf("-=");
			int te = line.indexOf("*=");
			int de = line.indexOf("/=");
			int tt=0;
			if (pe >= 0) {tt = pe;line = line.substring(0, tt) + "=" + line.substring(0, tt + 1) + line.substring(tt + 2, ll);}
			if (me >= 0) {tt = me;line = line.substring(0, tt) + "=" + line.substring(0, tt + 1) + line.substring(tt + 2, ll);}
			if (te >= 0) {tt = te;line = line.substring(0, tt) + "=" + line.substring(0, tt + 1) + line.substring(tt + 2, ll);}
			if (de >= 0) {tt = de;line = line.substring(0, tt) + "=" + line.substring(0, tt + 1) + line.substring(tt + 2, ll);}
			return line;
		}
		then += 4;
		String tline = line.substring(0, then);
		line = line.substring(then, ll);
		line = shorthand(line);
		return tline + line;
	}
	private void Toaster(CharSequence msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0 , 0);

		toast.show();


    }

	private void doInclude(String fileName) {

		boolean FileNotFound = false;
		String fn = fileName.substring(7);
		fn = fn.trim();

		File file = null;
		String FullFileName = "/sdcard/rfo-basic/source/" + 					// Base dir 
		  	fn;													// plus filename
		file = new File(FullFileName);								// is full path to the file to load
		BufferedReader buf = null;

		try { buf = new BufferedReader(new FileReader(file), 1024);} catch (FileNotFoundException e) {								// FNF should never happen
//			  Log.e(AutoEun.LOGTAG, e.getMessage(), e);
			FileNotFound = true;
		};

		if (FileNotFound) {
			String t = "Error_Include_file (" + fn + ") not_found";
			AddLine(t);
			return;
		}

		String data = null;
		do
		try {												// Read lines
			data = buf.readLine();
			if (data == null) {								// if null, say EOF
				data = "EOF";
			} else {
				data.replace('\n', ' ');            // Remove the New Line Char
				AddLine(data);						// add the line 
			}
		} catch (IOException e) {
//					  Log.e(LoadFile.LOGTAG, e.getMessage(), e);
			data = "EOF";
		}
		while (!data.equals("EOF"));						// while not EOF
		// File all read int
	}
	
	*/


	private void  FileLoader(String aFileName) {							// Loads the selected file

		BufferedReader buf = null;

		if (aFileName == null) aFileName = " ";
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
			Basic.lines.add("! Auto Run Error: \"" + aFileName + "\" not found");
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
