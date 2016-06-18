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

import static com.rfo.basic.Format.ASCII_SPACE;
import static com.rfo.basic.Format.ASCII_QUOTE;
import static com.rfo.basic.Format.QUOTES;
import static com.rfo.basic.Format.WHITESPACE;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;


public class AddProgramLine {
	private static final String LOGTAG = "AddProgramLine";
	private static String KW_INCLUDE = "include";
	private static String KW_IF = "if";
	private static String KW_THEN = "then";
	private static String KW_ELSE = "else";
	private static String KW_END = "end";

	// Regular expressions
	private static final String WS_REGEX			= "[" + WHITESPACE + "]*";
	private static final String IGNORED_LEAD_REGEX	= "[" + WHITESPACE + ":]*";		// ignore leading characters whitespace & colon
	// ENDIF, optional leading/trailing whitespace, optional whitespace between END and IF
	private static final String ENDIF_REGEX			= ".*" + WS_REGEX + KW_END + WS_REGEX + KW_IF + WS_REGEX;
	// Optional whitespace around the "." of certain keywords
	private static final String ARRAY_LOAD_REGEX	= "^array" + WS_REGEX + "\\." + WS_REGEX + "load(.*)";
	private static final Pattern ARRAY_LOAD_PATTERN	= Pattern.compile(ARRAY_LOAD_REGEX);
	private static final String LIST_ADD_REGEX		= "^list" + WS_REGEX + "\\." + WS_REGEX + "add(.*)";
	private static final Pattern LIST_ADD_PATTERN	= Pattern.compile(LIST_ADD_REGEX);
	private static final String SENSORS_OPEN_REGEX	= "^sensors" + WS_REGEX + "\\." + WS_REGEX + "open.*";
	private static final Pattern SENSORS_OPEN_PATTERN = Pattern.compile(SENSORS_OPEN_REGEX, Pattern.CASE_INSENSITIVE);
	private static final String SQL_UPDATE_REGEX	= "^sql" + WS_REGEX + "\\." + WS_REGEX + "update.*";
	private static final Pattern SQL_UPDATE_PATTERN = Pattern.compile(SQL_UPDATE_REGEX, Pattern.CASE_INSENSITIVE);

	private static final Pattern[] SPECIAL_COMMANDS = { ARRAY_LOAD_PATTERN, LIST_ADD_PATTERN };	// special when looking at ~ continuation

	private enum IfState { NONE, THEN, ELSE };			// states when converting single-line IF/THEN/ELSE to IF/THEN/ELSE/ENDIF.
	private IfState mIfState = IfState.NONE;

	public static ArrayList<Integer> lineCharCounts;
	public static int charCount = 0;
	private Boolean BlockFlag = false;
	private String mMerge = null;						// collects lines joined by ~ continuation character

	private final ArrayList<String> mIncludeFiles;		// prevent recursive INCLUDE

	public AddProgramLine() {
		charCount = 0;									// Character count = 0
		lineCharCounts = new ArrayList<Integer>();		// create a new list of line char counts
		lineCharCounts.add(0);							// First line starts with a zero char count
		Basic.lines = new ArrayList<Run.ProgramLine>();
		mIncludeFiles = new ArrayList<String>();
	}

	public void AddLine(String line) {
		/* Adds one line to Basic.lines
		 * Each line will have all white space characters removed and all characters
		 * converted to lower case (unless they are within quotes).
		 */
		String[] parts = new String[2];						// [0] is part before first colon, [1] is the rest
		if (getFirstStatement(line, parts)) {				// true if parts[0] contains complete statement.
			if (parts[0].length() == 0) { return; }			// blank line or comment

			convertIfThenElse(parts);						// detect single-line IF/THEN/ELSE with multi-command block(s)
			String stmt = parts[0];

			stmt = removeSpaces(stmt);						// remove all unquoted spaces
			if (stmt.startsWith(KW_INCLUDE)) {				// if include,
				doInclude(stmt);							// do the include
			} else {
				stmt += "\n";								// end the line with newline
				lineCharCounts.add(charCount);				// add char count to array of char counts
				Basic.lines.add(new Run.ProgramLine(stmt));	// add statement to Basic.lines
				// Log.d(LOGTAG, "Program line:" + stmt);
			}
		} else {											// not a complete statement, save it for later
			mMerge = parts[0]
					.substring(0, parts[0].length() - 1)	// delete the '~' continuation character
					.trim();								// and any whitespace next to it
			parts[0] = "";
		}
		if (parts[1].length() != 0) {
			AddLine(parts[1]);							// do the rest of the line
		}
		if ((mMerge == null) &&							// if no continuation, this is end of line
			(mIfState != IfState.NONE)) {				// was a single-line IF conversion left incomplete?
			String stmt = parts[0];
			stmt = "endif\n";							// complete it now
			lineCharCounts.add(charCount);
			Basic.lines.add(new Run.ProgramLine(stmt));
			// Log.d(LOGTAG, "Program line:" + stmt);
			mIfState = IfState.NONE;
		}
	}

	private boolean getFirstStatement(String line, String[] parts) {
		parts[0] = parts[1] = "";							// default: no statements
		if ((line == null) || (line.length() == 0)) {
										return true;		// no statements
		} else {
			line = line.replaceFirst(IGNORED_LEAD_REGEX, "");// skip leading whitespace and colons
			if (line.startsWith("!!")) {					// start or end of block comment
				BlockFlag = !BlockFlag;	return true;		// record state and toss out the marker line
			}
			if (BlockFlag)				return true;		// toss all lines between block comment markers
			if (line.startsWith("!"))	return true;		// toss whole-line comment
		}

		cleanLine(line, parts);								// strip end-of-line comments and convert to lower-case
															// parts[0] is part before first colon, [1] is the rest
		if (mMerge == null) {								// if no previous line to merge
			line = parts[0];
			if (line.startsWith("rem")) {					// toss REM lines
				parts[0] = parts[1] = "";
				return true;
			}
			if (line.length() == 0) {
				return getFirstStatement(parts[1], parts);	// empty statement, look for another one
			}
		} else {											// previous line ended in '~'
			parts[0] = mergeLines(mMerge, parts[0]);
			mMerge = null;
		}
		return (!parts[0].endsWith("~"));					// complete statement unless it ends with '~'
	}

	private void convertIfThenElse(String[] parts) {
		// Detect single-line IF/THEN/ELSE with multiple commands.
		// If found convert it to IF/THEN/ELSE/ENDIF with colons.
		String stmt = parts[0];
		int k;
		switch (mIfState) {
			case NONE:
				if (!stmt.startsWith(KW_IF)) break;		// not an IF
				if (parts[1].length() == 0) break;		// not multi-command line
				k = Format.FindKeyWord(KW_THEN, stmt, 2);
				if (k == -1) break;						// no THEN found, required for single-line IF

				changeSplit(parts, k + 4);				// move THEN to parts[1]
				mIfState = IfState.THEN;				// have THEN, looking for ELSE, EOL, or ENDIF
				break;

			case THEN:
				k = Format.FindKeyWord(KW_ELSE, stmt, 0);
				if (k == -1) break;						// ELSE not found

				if (k > 0) { 							// ELSE found, but not at beginning
					changeSplit(parts, k);				// put ELSE in parts[1] and catch it later
				} else {
					changeSplit(parts, 4);				// found at beginning, isolate it
					mIfState = IfState.ELSE;			// have ELSE, looking for ENDIF or EOL
				}
				break;

			case ELSE:
				if (stmt.matches(ENDIF_REGEX)) {		// if have ENDIF, this was not really
					mIfState = IfState.NONE;			// a single-line IF
				}
				break;
		}
	}

	private static void changeSplit(String[] parts, int where) {	// move characters from parts[0] to parts[1]
		if (where >= (parts[0].length() - 1)) return;
		StringBuilder hold = new StringBuilder(parts[0].substring(where));
		parts[0] = parts[0].substring(0, where);
		if (parts[1].length() != 0) { hold.append(':').append(parts[1]); }
		parts[1] = hold.toString();
	}

	private void cleanLine(String line, String[] parts) {	// clean up the line: remove excess whitespace,
															// fix quotes and ^nbsp, and convert to lower-case
															// return parts: [0] line up to first colon [1] the rest
		StringBuilder sb = new StringBuilder();
		int linelen = line.length();

		parts[1] = "";
		boolean doSpace = false;
		boolean firstColon = true;
		for (int i = 0; i < linelen; ++i) {
			char c = line.charAt(i);
			if (c == '%') {									// toss end-of-line comment
				break;
			} else if (c == ':') {							// complete statement in sb
				if  ( firstColon &&
					  ( (SQL_UPDATE_PATTERN.matcher(line).matches()) ||	// unless command is SQL.Update
					    ((mMerge != null) && (SQL_UPDATE_PATTERN.matcher(mMerge).matches()))
					  )
					) { // If SQL.Update, assume first : is part of the command, even if it isn't.
					sb.append(c);
					firstColon = false;
					continue;
				} else
				if  ( (SENSORS_OPEN_PATTERN.matcher(line).matches()) ||	// or command is Sensors.Open
					  ((mMerge != null) && (SENSORS_OPEN_PATTERN.matcher(mMerge).matches()))
					) {	// If Sensors.open, assume the rest of the line is part of the command, even if it isn't.
					sb.append(c);
					continue;
				} else {									// it might be a label
					int i2 = skipWhitespace(line, i + 1);	// skip following whitespace and/or comment
					if (i2 == linelen) {					// only whitespace or comment 
						sb.append(c);						// potentially a label
						break;
					}
					c = line.charAt(i2);
					if (c == ':') {
						sb.append(c);						// potentially a label
						parts[1] = line.substring(i2 + 1);  // followed by other statements
						break;
					}
				}
				parts[1] = line.substring(i + 1);
				break;
			} else if (WHITESPACE.indexOf(c) != -1) {		// collapse spaces, tabs, and &nbsp
				doSpace = true;								// to a single space
			} else {
				if (doSpace) {
					sb.append(ASCII_SPACE);
					doSpace = false;
				}
				if (QUOTES.indexOf(c) != -1) {				// clean up quoted string
					i = doQuotedString(line, i, sb);
				} else {
					c = Character.toLowerCase(c);			// convert character to lower case
					sb.append(c);							// and add it to the line
				}
			}
		}
		parts[0] = sb.toString();
	}

	private int skipWhitespace(String line, int index) {	// get index of next non-whitespace
		int linelen = line.length();
		for ( ; index < linelen; ++index) {
			char c = line.charAt(index);
			if (c == '%') { index = linelen; break; }		// treat comment '%' as eol
			if (WHITESPACE.indexOf(c) == -1) break;			// any other non-whitespace character
		}
		return index;
	}

	private String removeSpaces(String line) {				// remove spaces left by cleanLine
		StringBuilder sb = new StringBuilder();
		int linelen = line.length();
		for (int i = 0; i < linelen; ++i) {
			char c = line.charAt(i);
			if (c == ASCII_QUOTE) {							// do not mess with characters
				i = copyQuotedString(line, i, sb);			// between quote marks
			} else if (c != ASCII_SPACE) {
				sb.append(c);								// keep all non-spaces
			}
		}
		return sb.toString();
	}

	private int doQuotedString(String line, int index, StringBuilder s) {
		// Process a quoted string: fix the quotes and convert "\n" to newline and "\t" to tab.
		// line: input line; index: position in line of opening quote
		// s: output, contains quoted string with quotes

		int linelen = line.length();
		s.append(ASCII_QUOTE);							// Incoming index points at a quote
		while (true) {									// Loop until quote or no more characters
			if (++index >= linelen) { break; }			// No more characters, done
			char c = line.charAt(index);				// next character
			if (QUOTES.indexOf(c) != -1) {				// Found quote, done
				break;
			}

			if (c == '\\') {
				if (++index >= linelen) { break; }		// No more characters, drop backslash, done
				c = line.charAt(index);					// character after '\'
				boolean isQuote = (QUOTES.indexOf(c) != -1);
				if (isQuote || c == '\\') {				// look for \" (or funny quote) or \\ and retain it 
					s.append('\\');						// so that user can have quotes and \ in strings
				}
				else if (c == 'n') { c = '\r'; }		// change backslash-n to carriage return
				else if (c == 't') { c = '\t'; }		// change backslash-t to tab
														// else remove the backslash
			}
			s.append(c);
		}
		s.append(ASCII_QUOTE);							// Close string. If no closing quote in user string, add one.
														// If funny quote, convert it to ASCII quote.
		return index;									// leave index pointing at quote or EOL
	}

	private int copyQuotedString(String line, int index, StringBuilder s) {
		// Copy a quoted string without modification. Assume doQuotedString has run.
		// line: input line; index: position in line of opening quote
		// s: output, contains quoted string with quotes
		char c;
		s.append(ASCII_QUOTE);
		do {											// loop until closing quote
			c = line.charAt(++index);
			s.append(c);
			if (c == '\\') {
				s.append(line.charAt(++index));			// next character is quote or backslash
			}
		} while (c != ASCII_QUOTE);
		return index;									// leave index pointing at quote or EOL
	}

	private String mergeLines(String base, String addition) {
		if (base.length() == 0) { return addition; }
		if (addition.length() == 0) { return base; }

		StringBuilder sb = new StringBuilder(base);
		for (int i = 0; i < SPECIAL_COMMANDS.length; ++i) {
			Matcher m = SPECIAL_COMMANDS[i].matcher(base);
			if (m.matches() &&										// command allows continuable data
				(m.group(1).length() != 0))							// the command is not alone on the line
			{
				char lastChar = sb.charAt(sb.length() - 1);
				char nextChar = addition.charAt(0);
				if (lastChar != ',' && nextChar != ',') {			// no comma between adjacent parameters
					sb.append(',');									// insert comma between parameters
					break;
				}
			}
		}
		return sb.append(addition).toString();
	}

	private void doInclude(String fileName) {
		// If fileName is enclosed in quotes, the quotes preserved its case in AddLine().
		fileName = fileName.substring(KW_INCLUDE.length()).trim().replace("\"",  "");

		for (String f : mIncludeFiles) {
			if (f.equals(fileName)) return;							// don't do recursive INCLUDE
		}
		mIncludeFiles.add(fileName);

		BufferedReader buf = null;
		try { buf = Basic.getBufferedReader(Basic.SOURCE_DIR, fileName, Basic.Encryption.ENABLE_DECRYPTION); }
		// If getBufferedReader() returned null, it could not open the file or asset,
		// or it could not decrypt an encrypted asset.
		// It may or may not throw an exception.
		catch (Exception e) { }
		if (buf == null) {
			String t = "END \"Error opening INCLUDE file " + fileName + "\"";
			AddLine(t);
			return;
		}

		String data = null;
		do {
			try { data = buf.readLine(); }
			catch (IOException e) { data = "END \"Error reading INCLUDE file " + fileName + "\""; return; }
			finally { AddLine(data); }							// add the line
		} while (data != null);									// while not EOF and no error
	}

}
