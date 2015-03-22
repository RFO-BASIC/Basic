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
 
 Contains contributions from Michael Camacho. 2012

*************************************************************************************************/

package com.rfo.basic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import android.util.Log;


public class AddProgramLine {
	private static final String LOGTAG = "AddProgramLine";
	private static String kw_include = "include";

	Boolean BlockFlag = false;
	String stemp = "";
	public static ArrayList<Integer> lineCharCounts;
	public static int charCount = 0;

	public AddProgramLine() {
		charCount = 0;									// Character count = 0
		lineCharCounts = new ArrayList<Integer>();		// create a new list of line char counts
		lineCharCounts.add(0);							// First line starts with a zero char count
		Basic.lines = new ArrayList<Run.ProgramLine>();
	}

	public void AddLine(String line) {
		/* Adds one line to Basic.lines
		 * Each line will have all white space characters removed and all characters
		 * converted to lower case (unless they are within quotes).
		 */
		if (line == null) { return; }

		// Remove html white space when copy-paste program from forum
		String whitespace = " \t\uC2A0";			// space, tab, and UTF-8 encoding of html &nbsp

		int linelen = line.length();
		int i = 0;
		for (; i < linelen; ++i) {					// skip leading spaces and tabs
			char c = line.charAt(i);
			if (whitespace.indexOf(c) == -1) { break; }
		}

		// Look for block comments. All lines between block comments
		// are tossed out

		if (BlockFlag) {
			if (line.startsWith("!!", i)) {
				BlockFlag = false;
			}
			return;
		}
		if (line.startsWith("!!", i)) {
			BlockFlag = true;
			return;
		}

		StringBuilder sb = new StringBuilder();

		for (; i < linelen; ++i) {					// do not mess with characters
			char c = line.charAt(i);				// between quote marks
			if (c == '"' || c == '\u201c') {		// Change funny quote to real quote
				i = doQuotedString(line, i, linelen, sb);
			} else if (c == '%') {					// if the % character appears,
				break;								// drop it and the rest of the line
			} else if (whitespace.indexOf(c) == -1) { // toss out spaces, tabs, and &nbsp
				c = Character.toLowerCase(c);		// normal character: convert to lower case
				sb.append(c);						// and add it to the line
			}
		}
		String Temp = sb.toString();

		if (Temp.startsWith(kw_include)) {			// If include,
			doInclude(Temp);						// Do the include
			return;
		}

		if (Temp.equals(""))        { return; }		// toss out empty lines
		if (Temp.startsWith("!"))   { return; }		// and whole-line comments
		if (Temp.startsWith("%"))   { return; }		// and end-of-line comments
		if (Temp.startsWith("rem")) { return; }		// and REM lines

		if (stemp.length() == 0) {					// whole line, or first line of a collection
													// connected with continuation markers
			lineCharCounts.add(charCount);			// add char count to array of char counts
		}
		if (Temp.endsWith("~")) {					// Pre-processor: test for line continuation marker
			Temp = Temp.substring(0, Temp.length() - 1);	// remove the marker
			stemp = (stemp.length() == 0) ? Temp : mergeLines(stemp, Temp);	// and collect the line
			return;
		}
		if (stemp.length() > 0) {
			Temp = mergeLines(stemp, Temp);			// add stemp collection to line
			stemp = "";								// clear the collection
		}
		Temp += "\n";								// end the line with New Line
		Basic.lines.add(new Run.ProgramLine(Temp));	// add to Basic.lines
	}

	private int doQuotedString(String line, int index, int linelen, StringBuilder s) {
		char c, c2;
		s.append('"');						// Incoming index points at a quote
		while (true) {						// Loop until quote or no more characters
			++index;
			if (index >= linelen) { break; }	// No more characters, done
			else { c = line.charAt(index); }	// next character
			if (c == '"' || c == '\u201c') {	// Found quote, done
				break;
			}

			c2 = ((index + 1) < linelen) ? line.charAt(index + 1) : '\0';
			if (c == '\\') {
				if (c2 == '"' || c2 == '\\') {	// look for \" or \\ and retain it 
					s.append('\\').append(c2);	// so that user can have quotes and backslashes in strings
					++index;
				} else if (c2 == 'n') {			// change backslash-n to carriage return
					s.append('\r');
					++index;
				} else if (c2 == 't') {			// change backslash-t to tab
					s.append('\t');
					++index;
				}								// else remove the backslash
			} else { s.append(c); }				// not quote or backslash
		}
		s.append('"');							// Close string. If no closing quote in user string, add one.
												// If funny quote, convert it to ASCII quote.
		return index;							// leave index pointing at quote or EOL
	}

	private String mergeLines(String base, String addition) {
		if (base.length() == 0) { return addition; }
		if (addition.length() == 0) { return base; }

		String specialCommands[] = { "array.load", "list.add" };
		for (int i = 0; i < specialCommands.length; ++i) {
			if ( base.startsWith(specialCommands[i]) &&					// command allows continuable data
				 (base.length() > specialCommands[i].length()) ) {		// the command is not alone on the line
				char lastChar = base.charAt(base.length() - 1);
				char nextChar = addition.charAt(0);
				if (lastChar != ',' && nextChar != ',') {				// no comma between adjacent parameters
					return base + ',' + addition;						// insert comma between parameters
				}
			}
		}
		return base + addition;
	}

	private void doInclude(String fileName) {
		// If fileName is enclosed in quotes, the quotes preserved its case in AddLine().
		// Error messages go back through AddLine() again, so keep the quotes.
		String originalFileName = fileName.substring(kw_include.length()).trim();	// use this for error message
		fileName = originalFileName.replace("\"",  "");								// use this for file operations
		BufferedReader buf = null;
		try { buf = Basic.getBufferedReader(Basic.SOURCE_DIR, fileName, Basic.Encryption.ENABLE_DECRYPTION); }
		// If getBufferedReader() returned null, it could not open the file or asset,
		// or it could not decrypt an encrypted asset.
		// It may or may not throw an exception.
		// TODO: "not_found" may not be a good error message. Can we change it?
		catch (Exception e) { }
		if (buf == null) {
			String t = "Error_Include_file (" + originalFileName + ") not_found";
			AddLine(t);
			return;
		}

		String data = null;
		do {
			try { data = buf.readLine(); }
			catch (IOException e) { data = "Error reading Include file " + originalFileName; return; }
			finally { AddLine(data); }							// add the line
		} while (data != null);									// while not EOF and no error
	}

}
