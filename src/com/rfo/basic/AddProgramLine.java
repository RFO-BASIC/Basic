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

	
	public AddProgramLine() {		
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

    	String Temp = "";

		int linelen = line.length();
   		for (int i=0; i < linelen; ++i) {			// do not mess with characters
			char c = line.charAt(i);				// between quote marks
			if (c == '\u201c') c = '"';                    // Change funny quote to real quote
			if (c == '"') {
				do {
					if (c == '\\') {					// look for \"
						if (i + 1 >= linelen) {
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
					} else if ( ((i + 1) < linelen) &&		// Pre-processor:
							  (line.charAt(i + 1) == '=') && // if +=, -=, *=, /= in string 
							  ("+-*/".indexOf(c) >= 0) ) {	// hide them temporarily by
						Temp += "{" + c + "&=}";			// converting to "{+&=}", etc.
						++i;
					} else Temp = Temp + c;

					++i;
					if (i >= linelen) { c = '"'; }			// no closing '"', just add it in
					else {
						c = line.charAt(i);					// next character
						if (c == '\u201c') { c = '"'; }		// Change funny quote to real quote
					}
				} while (i < linelen && c != '"');
				Temp = Temp + c;
			} else if (c == '%') {					// if the % character appears,
				break;								// drop it and the rest of the line
			} else if (c == ' ' || c == '\t') {		// toss out spaces and tabs
				// Pre-processor: space/tab + '_' at end-of-line is line continuation character
				int j = i + 1;
				if ((j < linelen) && (line.charAt(j) == '_')) {		// scan after '_', skipping spaces and tabs
					for (++j; (j < linelen) && (" \t".indexOf(line.charAt(j)) >= 0); ++j) { ; }
					if ((j >= linelen) || (line.charAt(j) == '%')) { // eol or comment
						Temp = Temp + "{+nl}";						// add line continuation marker
						break;
					}												// else catch '_' on next loop
				}													// else toss the space or tab
			} else if (c == '\\') {							// Pre-processor: check for line continuation
				int j = i + 1;										// scan after backslash, skipping spaces and tabs
				for (; (j < linelen) && (" \t".indexOf(line.charAt(j)) >= 0); ++j) { ; }
				if ((j >= linelen) || (line.charAt(j) == '%')) { 	// eol or comment
					Temp = Temp + "{+nl}";							// add line continuation marker
					break;
				}
				Temp = Temp + c;									// else add backslash to the line
			} else if ("@#^&<>*_=?.\\".indexOf(c) >= 0) {	// Pre-processor: check for line continuation
				int j = i + 1;										// scan after character pair, skipping spaces and tabs
				if ((j < linelen) && (line.charAt(j) == c)) {
					for (++j; (j < linelen) && (" \t".indexOf(line.charAt(j)) >= 0); ++j) { ; }
					if ((j >= linelen) || (line.charAt(j) == '%')) { // eol or comment
						Temp = Temp + "{+nl}";						// add line continuation marker
						break;
					}
				}
				Temp = Temp + c;									// else add character to the line
			} else {
				c = Character.toLowerCase(c);		// normal character: convert to lower case
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
   			if (stemp.length() == 0) {					// whole line, or first line of a collection
   														// connected with continuation markers
   	   			lineCharCounts.add(charCount);			// add char count to array of char counts
   			}
			if (Temp.endsWith("{+nl}")) {    		// Pre-processor: test for include next line sequence
				stemp = stemp + Temp.substring(0, Temp.length() - 5);  // remove the include next line sequence and collect it
				return;
			} else {
				Temp = stemp + Temp; // add stemp collection to line
				stemp = "";     // clear the collection
			}
			Temp = shorthand(Temp);					// Pre-processor: expand C/Java-like operators
			if (Temp.contains("{")) {				// Pre-processor: out any hidden substrings
				Temp = Temp.replace("{+&=}", "+=");
				Temp = Temp.replace("{-&=}", "-=");
				Temp = Temp.replace("{*&=}", "*=");
				Temp = Temp.replace("{/&=}", "/=");
			}
   			Temp = Temp + "\n";						// end the line with New Line
   			Basic.lines.add(Temp);					// add to Basic.lines
   		}
	}
	
	public String shorthand(String line) {			// Pre-processor: expand C/Java-like operators
		int ll = line.length();
		int then = line.indexOf("then");
		if (then == -1) {
			if (line.startsWith("++") || line.startsWith("--")) {
				line = line.substring(2, ll) + "=1" + line.substring(1, ll);
			}
			if (line.endsWith("++") || line.endsWith("--")) {
				line = line.substring(0, ll - 2) + "=" + line.substring(0, ll - 1) + "1";
			}
			int tt = line.indexOf("+=");
			if (tt < 0) {
				tt = line.indexOf("-=");
				if (tt < 0) {
					tt = line.indexOf("*=");
					if (tt < 0) {
						tt = line.indexOf("/=");
					}
				}
			}
			if (tt > 0) {
				line = line.substring(0, tt) + "=" + line.substring(0, tt + 1) + line.substring(tt + 2, ll);
			}
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