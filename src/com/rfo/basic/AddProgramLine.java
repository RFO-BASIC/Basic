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

 You may contact the author, Paul Laughton at basic@laughton.com
 
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
		Basic.lines = new ArrayList<String>();
	}
	
	public void AddLine(String line) {
		/* Adds one line to Basic.lines
		 * Each line will have all white space characters removed and all characters
		 * converted to lower case (unless they are within quotes).
		 */
		if (line == null) { return; }

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
			if (c == '"' || c == '\u201c') {		// Change funny quote to real quote
				StringBuilder sb = new StringBuilder();
				i = doQuotedString(line, i, linelen, sb);
				Temp += sb.toString();
			} else if (c == '%') {					// if the % character appears,
				break;								// drop it and the rest of the line
			} else if (c == '~') {					// Pre-processor: check for line continuation '~'
				int j = i;							// scan after character, skipping spaces and tabs
				do { ++j; } while ((j < linelen) && (" \t".indexOf(line.charAt(j)) >= 0));
				if ((j >= linelen) || (line.charAt(j) == '%')) { 	// EOL or comment
					Temp += "{+nl}";				// add line continuation marker
					break;
				}
			} else if (c != ' ' && c != '\t') {		// toss out spaces and tabs
				c = Character.toLowerCase(c);		// normal character: convert to lower case
				Temp += c;							// and add it to the line
			}
		}

		if (Temp.startsWith(kw_include)) {			// If include,
			doInclude(Temp);						// Do the include
			return;
		}

   		if (Temp.startsWith("rem")) { Temp = ""; }	// toss out REM lines
   		if (Temp.startsWith("!"))   { Temp = ""; }	// and whole-line comments
   		if (Temp.startsWith("%"))   { Temp = ""; }	// and end-of-line comments
   		if (!Temp.equals("")) {						// and empty lines
   			if (stemp.length() == 0) {					// whole line, or first line of a collection
   														// connected with continuation markers
   	   			lineCharCounts.add(charCount);			// add char count to array of char counts
   			}
			if (Temp.endsWith("{+nl}")) {    		// Pre-processor: test for include next line sequence
				Temp = Temp.substring(0, Temp.length() - 5);	// remove the include next line sequence
				stemp = (stemp.length() == 0) ? Temp : mergeLines(stemp, Temp);	// and collect the line
				return;
			} else if (stemp.length() > 0) {
				Temp = mergeLines(stemp, Temp); // add stemp collection to line
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
			} else if ( (c2 == '=') &&			// Pre-processor:
						("+-*/".indexOf(c) >= 0) ) {// if +=, -=, *=, /= in string
				s.append('{').append(c)				// hide them temporarily
				 .append("&=}");					// by converting to "{+&=}", etc.
				++index;
			} else { s.append(c); }				// not quote, backslash, or pre-processor operator
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
				 (base.length() > specialCommands[i].length()) &&		// the command is not alone on the line
				 scanForComma(base) ) {									// the command's first parameter is already present
				char lastChar = base.charAt(base.length() - 1);
				char nextChar = addition.charAt(0);
				if (lastChar != ',' && nextChar != ',') {				// no comma between adjacent parameters
					base += ',';										// insert comma between parameters
				}
				break;
			}
		}
		return base + addition;
	}

	// return true iff line has a comma that is not in balanced parentheses or quotes
	private boolean scanForComma(String line) {
		boolean isQuote = false;
		int parens = 0;
		int brackets = 0;
		int len = line.length();
		char prevc = '\0';
		for (int i = 0; i < len; ++i) {
			char c = line.charAt(i);
			switch (c) {
				case '(': ++parens; break;
				case ')': --parens; break;
				case '[': ++brackets; break;
				case ']': --brackets; break;
				case '\"':
					if (prevc != '\\') { isQuote = !isQuote; }
					break;
				case ',':
					if (parens == 0 && brackets == 0 && !isQuote) { return true; }
			}
			prevc = c;
		}
		return false;
	}

	private String shorthand(String line) {			// Pre-processor: expand C/Java-like operators
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
		fileName = fileName.substring(kw_include.length()).trim();
		String path = Basic.getSourcePath(fileName);				// Base Source dir + filename
		BufferedReader buf = null;
		try { buf = Basic.getBufferedReader(path); }
		catch (Exception e) {}										// stream already closed if exception
		if (buf == null) {
			String t = "Error_Include_file (" + fileName + ") not_found";
			AddLine(t);
			return;
		}

		String data = null;
		do {
			try { data = buf.readLine(); }
			catch (IOException e) { data = null; }
			AddLine(data);		 								// add the line
		} while (data != null);									// while not EOF and no error
	}

}
