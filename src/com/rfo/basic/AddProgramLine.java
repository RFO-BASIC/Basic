/****************************************************************************************************

 BASIC! is an implementation of the Basic programming language for
 Android devices.


 Copyright (C) 2010, 2011 Paul Laughton

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
 
 Contains contributions from Michael Camacho. 2012

 *************************************************************************************************/

package com.rfo.basic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;

public class AddProgramLine {
	
	Boolean BlockFlag = false;
	String stemp = "";
    public static ArrayList<String> lines;       //Program lines for execution
    public static ArrayList<Integer> lineCharCounts;
    public static int charCount = 0;

	
	public void AddProgramLine() {		
		charCount = 0;									// Character count = 0
		lineCharCounts = new ArrayList<Integer>();		// create a new list of line char counts
		lineCharCounts.add(0);							// First line starts with a zero char count
		Basic.lines = new ArrayList<String>();
	}
	
	public void AddLine(String line, boolean doInclude) {
		/* Adds one line to Basic.lines		 *  Each line will have all white space characters removed and all characters
		 *  converted to lower case (unless they are within quotes).    	
		 */

		// Look for block comments. All lines between block comments
		// are tossed out


    	String Temp = "";

    	// Look for block comments. All lines between block comments
    	// are tossed out

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
			if (c == '“') c = '"';                    // Change funny quote to real quote
			if (c == '"') {
				do {
					if(i+1<line.length()){
						if (line.charAt(i + 1) == '=') {
							if (c == '+') {
								Temp += "{+&=}";
								++i;c = 0;
							} else if (c == '-') {
								Temp += "{-&=}";
								++i;c = 0;
							} else if (c == '*') {
								Temp += "{*&=}";
								++i;c = 0;
							} else if (c == '/') {
								Temp += "{/&=}";
								++i;c = 0;
							}
						}
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
					if (c == '“') c = '"';					// Change funny quote to real quote
				}while (i < line.length() && c != '"');
				Temp = Temp + c;
			} else if (c == '%') {					// if the % character appears,
				break;								// drop it and the rest of the line
			} else if (c == ' ') {
				if ((i + 1) < line.length())
					if (line.charAt(i + 1) == '_') {
						Temp = Temp + "{+nl}";
						break;
					}
			} else if (c != ' ' && c != '\t') {		// toss out spaces and tabs
				c = Character.toLowerCase(c);		// convert to lower case
				Temp = Temp + c;						// and add it to the line
			}
		}

   		if (Temp.startsWith("include")) {            // If include, 
   			if (doInclude) {
   				doInclude(Temp);                        // Do the include
   				return;
   			} else {
   				Temp = Temp + " ... not allowed in an APK";
   			}
   		}

   		if (Temp.startsWith("rem")) {Temp = "";}		// toss out REM lines
   		if (Temp.startsWith("!"))Temp = "";			// and pseudo rem lines
   		if (Temp.startsWith("%"))Temp = "";			// and pseudo rem lines
   		if (!Temp.equals("")) {						// and empty lines
			if (Temp.endsWith("{+nl}")) {    //test for include next line sequence
				stemp = stemp + Temp.substring(0, Temp.length() - 5);  // remove the include next line sequence and collect it
				return;
			} else {
				Temp = stemp + Temp; // add stemp collection to line
				stemp = "";     // clear the collection
			}
		    Temp = shorthand(Temp);
			Temp = Temp.replace("{+&=}", "+=");
			Temp = Temp.replace("{-&=}", "-=");
			Temp = Temp.replace("{*&=}", "*=");
			Temp = Temp.replace("{/&=}", "/=");
   			Temp = Temp + "\n";						// end the line with New Line
   			lineCharCounts.add(charCount);			// add char count to array of char counts
   			Basic.lines.add(Temp);					// add to Basic.lines
   		}
	}
	
	public String shorthand(String line) {
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
			AddLine(t, true);
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
				AddLine(data, true);						// add the line 
			}
		} catch (IOException e) {
//					  Log.e(LoadFile.LOGTAG, e.getMessage(), e);
			data = "EOF";
		}
		while (!data.equals("EOF"));						// while not EOF
		// File all read int
	}



	
}
