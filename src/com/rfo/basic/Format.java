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

import java.util.ArrayList;
import java.util.Locale;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Format extends ListActivity {

	private static final String LOGTAG = "Run";

	public static final char ASCII_SPACE = ' ';
	public static final char ASCII_QUOTE = '\"';
	public static final char BACKSLASH   = '\\';
	public static final char LEFT_QUOTE  = '\u201C';
	public static final char RIGHT_QUOTE = '\u201D';
	public static final char NBSP        = '\u00A0';
	public static final char COMMENT     = '%';
	public static final String QUOTES     = "" + ASCII_QUOTE + LEFT_QUOTE + RIGHT_QUOTE;
	public static final String WHITESPACE = " \t\u00A0";// space, tab, and html &nbsp

	private Background theBackground;					// Background task ID
	private ArrayAdapter<String> AA;
	private ListView lv ;								// The output screen list view
	private ArrayList<String> output;					// The output screen text lines
	private boolean blockQuote = false;

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// if BACK key leave original Editor text unmodified
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (theBackground != null) synchronized (Editor.DisplayText) {
				theBackground.cancel(true);
			}
			finish();
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		output = new ArrayList<String>();
		output.add("");
		AA = new ArrayAdapter<String>(this, R.layout.simple_list_layout, output);  // Establish the output screen
		lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setTextFilterEnabled(false);
		lv.setSelection(0);

		theBackground = new Background();						// Start the background task to format
		theBackground.execute("");								// and show progress
	}

	public class Background extends AsyncTask<String, String, String> {

		private String[] originalText;				// The original text, split into lines
		private String formattedText;				// The formatted text
		private final String SPACES = " ";			// Spaces for indenting
		private int indentLevel = 0;

		@Override
		protected String doInBackground(String...str) {
			blockQuote = false;
			for (String line : originalText) { ProcessLine(line); }
			return "";
		}

		@Override
		protected void onPreExecute() {
			originalText = Editor.DisplayText.split("\n");	// The original text from the editor
			formattedText = "";								// The resulting formatted text
		}

		@Override
		protected void onProgressUpdate(String... str) {	// Called when publishProgress() is executed.

			for (int i = 0; i < str.length; ++i) {			// Form strings of 20 characters
				String s = output.get(output.size() - 1);
				s = s + str[i];
				output.set(output.size() - 1, s);
				if (s.length() >= 20) {						// After the 20th character
					output.add("");							// start a new line
				}
			}

			setListAdapter(AA);								// show the output
			lv.setSelection(output.size() - 1);				// set last line as the selected line to scroll
		}

		protected void onPostExecute(String result) {				// Done
			synchronized (Editor.DisplayText) {						// protect from cancel
				Editor.DisplayText = formattedText;					// Transfer the formatted text
				Editor.mText.setText(Editor.DisplayText);			// to the Editor
				Editor.mText.setSelection(0, 0);					// Set the cursor at the top
			}
			finish();
		}

		private void ProcessLine(String theLine) {					// Process one line
			int blanks = CountBlanks(theLine, 0);
			if (!isBlockQuote(theLine, blanks)) {
				theLine = ProcessKeyWords(theLine);					// Do the key words
				theLine = ProcessIndents(theLine);					// And then do the indents
			}
			formattedText += theLine + '\n';
			publishProgress(".");									// Show one dot for each line processed
		}

		private int KeyWordOccurences(String line, String kw) {
			int count = 0;
			int k = FindKeyWord(":" + kw, line, 0);					// Inline key word (multi-commands per line)
			while (k >= 0) {
				count++;
				k = FindKeyWord(":" + kw, line, k+1);
			}
			if (line.startsWith(kw)) { count++; }					// Leading key word (starts the line)
			return count;
		}

		private String ProcessIndents(String aLine) {				// Do the indenting
			String any_ws = "["+WHITESPACE+"]*";					// zero or more spaces or tabs
			String some_ws = "["+WHITESPACE+"]+";					// one or more spaces or tabs
			boolean debug=false;

			aLine = aLine.replaceFirst(any_ws, "");					// remove leading blanks
			if (aLine.equals(""))		{ return aLine; }			// skip blank or empty line
			if (aLine.startsWith("!"))	{ return DoIndent() + aLine; }	// indent comment line

			String theLine = aLine.toLowerCase(Locale.US);			// convert to lower case
			theLine = theLine.replaceAll(some_ws, "");				// remove all white spaces

			String temp = "";										// temp will contain the indent spaces
			int indent_delta = 0;									// Necessary for multi-commands per line

			indent_delta -= KeyWordOccurences(theLine, "endif");	// Indenting is determined by key words
			indent_delta -= KeyWordOccurences(theLine, "until");	// This group ends a block,
			indent_delta -= KeyWordOccurences(theLine, "repeat");	// reducing current indent by 1
			indent_delta -= KeyWordOccurences(theLine, "fn.end");
			indent_delta -= KeyWordOccurences(theLine, "next");
			indent_delta -= 2*KeyWordOccurences(theLine, "sw.end");	// x2 because SW.BEGIN = +1 and SW.CASE = +1

			indent_delta += KeyWordOccurences(theLine, "do");		// This group starts a block,
			indent_delta += KeyWordOccurences(theLine, "while");	// increasing subsequent indent by 1
			indent_delta += KeyWordOccurences(theLine, "fn.def");
			indent_delta += KeyWordOccurences(theLine, "for");
			indent_delta += 2*KeyWordOccurences(theLine, "sw.begin");

			if (debug) {
				aLine+="  %";
				int n=0;
				temp="endif"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="until"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="repeat"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="fn.end"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="next"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="sw.end"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="do"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="while"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="fn.def"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="for"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
				temp="sw.begin"; n=KeyWordOccurences(theLine, temp); if (n>0) aLine+="{"+temp+" x"+String.valueOf(n)+"}";
			}

			// Post-treatment for leading IF, ELSE/ELSEIF, and SW.CASE/SW.DEFAULT
			int k = 0;												// Identify all cases of leading IF
			if (!aLine.startsWith("IF"))
				k = FindKeyWord("IF", aLine, 0);
			while (k >= 0) {
				int then = FindKeyWord("THEN", aLine, k+1);
				int colon = aLine.indexOf(':', k+1);
				if (colon >= 0) {									// If colon present (multi-commands per line)...
					char after_then = ' ';
					if (then >= 0 && then+4 < aLine.length())
						after_then = aLine.charAt(then+4);
					if ((then < 0) || (then > colon) ||				// ...and no THEN before colon,
						(after_then == ':') ) {						// or THEN immediately followed by a colon,
							indent_delta++;							// indent
							if (debug) aLine+="{mcpl_if}";
					}
				} else if (then < 0) {								// If no colon (single command) and no THEN...
					String end = aLine.substring(k);
					String reg = "IF" + any_ws;
					if (!end.matches(reg) &&
						!end.matches(reg+"%.*")) {					// ...and "IF" not at end of line (!="END IF"),
						indent_delta++;								// indent
						if (debug) aLine+="{scpl_if}";
					}
				} else {
					String end = aLine.substring(k+1);
					String reg = ".*THEN" + any_ws;
					String reg2 = reg + "%.*"; 
					if ( !aLine.startsWith("ELSE") &&				// If no colon (single command) and no ELSE/ELSEIF
						(end.matches(reg) || end.matches(reg2)) ) {	// and ending THEN,
						indent_delta++;								// indent
						if (debug) aLine+="{scpl_if+then+EOL}";
					}
				}
				k = FindKeyWord("IF", aLine, k+1);					// Iterate on next inline starting IF
			}

			boolean leading_else = (aLine.startsWith("ELSE"));		// This is both ELSE and ELSEIF,
			if (leading_else) {										// they end one block and start another
				indent_delta--;										// (impacts indent ONLY if they start the
			}														// line, NOT within multi-commands per line)
			if (debug & leading_else) aLine+="{elseif}";

			boolean in_sw_case = ( aLine.startsWith("SW.CASE") ||	// Special SW.CASE/SW.DEFAULT blocks,
								   aLine.startsWith("SW.DEFAULT") );
			if (in_sw_case) { indent_delta--; }						// (they act a lot like ELSE/ELSEIF)
			if (debug & in_sw_case) aLine+="{sw.case}";

			// Now do the indenting itself
			if (indent_delta < 0) { indentLevel += indent_delta; }	// Decrement current line indent level
			temp = DoIndent();										// Make the indent for the current line
			if (indent_delta > 0) { indentLevel += indent_delta; }	// Increment subsequent lines indent level
			if (leading_else) { indentLevel++; }					// Increment too if leading ELSE/ELSEIF
			if (in_sw_case) { indentLevel++; }						//		  ... or if leading SW.CASE/SW.DEFAULT
			return temp + aLine;							// Concat line onto spaces
		}

		private String DoIndent() {							// Add spaces determined by indent level
			if (indentLevel < 0) indentLevel = 0;

			String temp = "";
			if (indentLevel > 0) {
				for (int i = 0; i < indentLevel; ++i) {
					temp = temp + SPACES;
				}
			}
			return temp;
		}

	} // end class Background


	// ************************** Process Key Words Stuff ********************

	//********************  Code for formatting a line **************************************

	private static int CountBlanks(String aLine, int start) {	// Count spaces and tabs from current position
		int current;

		for (current = start; current < aLine.length(); ++current) {
			char c = aLine.charAt(current);
			if (WHITESPACE.indexOf(c) == -1) break;
		}
		return current - start;
	}

	private boolean isBlockQuote(String aLine, int blanks) {	// Track start and end of block quotes
		if (aLine.startsWith("!!", blanks)) {
			blockQuote = !blockQuote;
			return true;
		}
		return blockQuote;
	}

	public static String ProcessKeyWords(String actualLine) {	// Find and capitalize the key words
		actualLine = fixQuotesAndNbsp(actualLine);				// Fix quotes, remove &nbsp
		int blanks = CountBlanks(actualLine, 0);				// Skip leading blanks
		if (actualLine.startsWith("!", blanks)) { return actualLine; }	// Skip comment line

		String aLine = ProcessKeyWords(actualLine, blanks);		// Do the key words
		actualLine = "";
		int colon = aLine.indexOf(":");
		while (colon >= 0) {									// Continue to treat multi-commands per line
			String theRest = aLine.substring(colon);
			if (theRest.matches(":["+WHITESPACE+"]*") ||
				theRest.matches(":["+WHITESPACE+"]*%.*") ||
				theRest.matches(":["+WHITESPACE+"]*!.*")
				) {												// Break if ':' at end of line (label)
				break;
			}
			int startnext = colon + 1;							// start of command after ':'
			startnext += CountBlanks(aLine, startnext);			// skip the blanks, too
			actualLine += aLine.substring(0, startnext);		// Store already processed part of line
			aLine = aLine.substring(startnext);
			aLine = ProcessKeyWords(aLine, 0);
			colon = aLine.indexOf(":");
		}
		return actualLine + aLine;								// Return full treated line
	}

	private static String ProcessKeyWords(String actualLine, int blanks) {
																// Find and capitalize the key words,
																// leading blanks already counted
		if (actualLine.equals("")) 			{ return actualLine; }		// Skip empty line
		if (blanks == actualLine.length())	{ return actualLine; }		// Skip blank line

		String lcLine = actualLine.toLowerCase(Locale.US);				// Convert to lower case

		for (int i = 0; i < Run.MathFunctions.length; ++i) {			// Process math functions
			actualLine = TestAndReplaceAll(Run.MathFunctions[i], lcLine, actualLine);
		}

		for (int i = 0; i < Run.StringFunctions.length; ++i) {			// Process String Functions
			actualLine = TestAndReplaceAll(Run.StringFunctions[i], lcLine, actualLine);
		}

		actualLine = StartOfLineKW(lcLine, actualLine, blanks);			// Process BASIC! keyword starting a line

		if (actualLine.startsWith("IF", blanks)) {						// Line starts with IF
			int k = FindKeyWord("THEN", actualLine, blanks);			// and contains THEN
			if (k >= 0) {
				k += 4;													// skip THEN
				actualLine = StartOfSubLineKW(actualLine, k);			// process KW after THEN
				k = FindKeyWord("ELSE", actualLine, k);					// and contains ELSE
				if (k >= 0) {
					actualLine = StartOfSubLineKW(actualLine, k + 4);	// process KW after ELSE
				}
			}
		}
		actualLine = actualLine.replaceAll("ELSE["+WHITESPACE+"]*IF", "ELSEIF");
		actualLine = actualLine.replaceAll("END["+WHITESPACE+"]*IF", "ENDIF");
		// Todo: treat special case of line continuation character: IF cond ~\nTHEN command1~\nELSE command2

		return actualLine;			// Done, return results
	}

	private static String StartOfSubLineKW(String actualLine, int start) {
		String subLine = actualLine.substring(start);
		String lcLine = subLine.toLowerCase(Locale.US);
		int blanks = CountBlanks(subLine, 0);
		subLine = StartOfLineKW(lcLine, subLine, blanks);
		if (subLine.length() > 0) {
			actualLine = actualLine.substring(0, start) + subLine;
		}
		return actualLine;
	}

	private static String StartOfLineKW(String lcLine, String actualLine, int blanks) {
		String kw = "";
		int k = -1;
		for (int i = 0; i < Run.BasicKeyWords.length; ++i) {				// If the line starts with a key word
			kw = Run.BasicKeyWords[i];
			if (!kw.equals(" ")) {
				k = lcLine.indexOf(kw, blanks);
				if (k >= 0 && k == blanks) {
					String xkw = actualLine.substring(blanks, blanks + kw.length());
					if (xkw.equals("inkey$")) { xkw = "inkey"; }
					actualLine = actualLine.replaceFirst(xkw, kw.toUpperCase(Locale.US)); // Capitalize it
					k += kw.length();
					break;
				}
			}
		}

		if (kw.equals("if")) {													// Process IF statement
			actualLine = TestAndReplaceFirst("then", lcLine, actualLine, k);
			actualLine = TestAndReplaceFirst("else", lcLine, actualLine, k);
		}
		else
		if (kw.equals("elseif")) {												// Process ELSEIF statement
			actualLine = TestAndReplaceFirst("then", lcLine, actualLine, k);
		}
		else
		if (kw.equals("for")) {													// Process FOR statement
			actualLine = TestAndReplaceFirst("to", lcLine, actualLine, k);
			actualLine = TestAndReplaceFirst("step", lcLine, actualLine, k);
		}
		else
		if (kw.equals("end")) {											// Process ENDIF statement
			actualLine = TestAndReplaceFirst("if", lcLine, actualLine, k);
		}
		else
		if (kw.endsWith(".")) {											//Process multipart commands
			String[] list = Run.getKeywordLists().get(kw);
			if (list != null) {
				actualLine = ExpandedKeyWord(list, lcLine, actualLine, k);
			}
		}
		return actualLine;
	}

	private static String ExpandedKeyWord(String[] words, String lcLine, String actualLine, int start) {  // The stuff after xxx.
		for (String word : words) {
			if (lcLine.startsWith(word, start)) {
				String xkw = actualLine.substring(start, start + word.length());
				actualLine = actualLine.replaceFirst(xkw, word.toUpperCase(Locale.US));
			}
		}
		return actualLine;
	}

																			// Find and replace first occurrence after start position
	private static String TestAndReplaceFirst(String kw, String lcLine, String actualLine, int start) {
		StringBuilder sb = new StringBuilder(actualLine);

		int k = FindKeyWord(kw, lcLine, start);		// find keyword, not embedded in string, commented, nor in a variable name
		if (k >= 0) {
			String KW = kw.toUpperCase(Locale.US);	// found! replace with upper-case version of keyword
			sb.replace(k, k + kw.length(), KW);
		}
		return sb.toString();
	}

	private static String TestAndReplaceAll(String kw, String lcLine, String actualLine) {		// Find and replace all occurrences
		return TestAndReplaceAll(kw, lcLine, actualLine, 0);				// start position 0
	}
																			// Find and replace all occurrences after start position
	private static String TestAndReplaceAll(String kw, String lcLine, String actualLine, int start) {
		StringBuilder sb = new StringBuilder(actualLine);

		int k = FindKeyWord(kw, lcLine, 0);			// find keyword, not embedded in string, commented, nor in a variable name
		while (k >= 0) {
			String KW = kw.toUpperCase(Locale.US);	// found! replace with upper-case version of keyword
			sb.replace(k, k + kw.length(), KW);
			k = FindKeyWord(kw, lcLine, k+1);		// iterate to find next valid occurrence of this keyword
		}
		return sb.toString();
	}

	private static boolean isVarChar(char c) {
		return ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '@' || c == '#');
	}

	public static int FindKeyWord(String kw, String line, int start) {	// Find instance of keyword, not quoted nor
		int k = line.indexOf(kw, start);								// commented nor embedded in a variable name
		if (k < 0) { return -1; }									// no instances of this keyword - quick exit

		int kl = kw.length();
		int limit = line.length() - (kl - 1);						// another keyword won't fit after limit
		while (k >= 0) {
			int q1 = line.indexOf(ASCII_QUOTE, start);
			int q2 = skipQuotedSubstring(line, q1);
			int ct = line.indexOf(COMMENT, start);

			if ((q2 >= 0) && (q2 < k)) { start = ++q2; continue; }	// skip quoted substrings before keyword

			while ((k >= 0) && ((q2 < 0) || (k < q2))) {			// analyze all keywords before end of quoted substring
				if ((q1 < 0) || (k < q1)) {							// keyword is not in the quoted substring
					if ((ct >= 0) && (ct < k))						// there is a valid comment mark before the keyword (not
						return -1;									// in a quote) -> invalid (commented) keyword
					boolean embedded = (!isVarChar(kw.charAt(0))) ? false	// embedded if preceding character
						: ((k > 0) && isVarChar(line.charAt(k - 1)));		// is part of a variable name
					if (!embedded) {
						return k;									// this is a valid keyword, not quoted/commented/embedded
					}
					start = k + kl;									// else skip to the end of the keyword
				} else {
					start = 1 + ((q2 >= 0) ? q2 : limit);			// skip to the end of the quoted substring
				}
				k = (start <= limit) ? line.indexOf(kw, start) : -1;	// look for next instance of keyword
			}
		}
		return -1;													// keyword not found, or found in a bad configuration
	}

	private static String fixQuotesAndNbsp(String line) {
		if ((line.indexOf(LEFT_QUOTE) == -1) &&
			(line.indexOf(RIGHT_QUOTE) == -1) &&
			(line.indexOf(ASCII_QUOTE) == -1) &&
			(line.indexOf(NBSP) == -1))		{ return line; }		// nothing to do

		StringBuilder sb = new StringBuilder(line);
		int size = line.length();
		for (int ci = 0; ci < size; ++ci) {
			char ch = sb.charAt(ci);
			if      (ch == LEFT_QUOTE)		{ sb.setCharAt(ci, ASCII_QUOTE); }
			else if (ch == RIGHT_QUOTE)		{ sb.setCharAt(ci, ASCII_QUOTE); }
			if (ch == ASCII_QUOTE) {								// start of quoted string
				ci = skipQuotedSubstring(line, ci);
				if (ci < 0)					{ break; }				// no closing quote, done

				if      (ch == LEFT_QUOTE)	{ sb.setCharAt(ci, ASCII_QUOTE); }
				else if (ch == RIGHT_QUOTE)	{ sb.setCharAt(ci, ASCII_QUOTE); }
			}
			else if (ch == NBSP)			{ sb.setCharAt(ci, ASCII_SPACE); }
		}
		return sb.toString();
	}

	private static int skipQuotedSubstring(String line, int start) {	// start is index of opening quotation mark
		if (start < 0) return start;					// no quoted substring

		int size = line.length();
		int ci = start + 1;								// skip the opening quote
		while (ci < size) {
			int cq = line.indexOf(ASCII_QUOTE, ci);
			if (cq < 0) { return -1; }					// no closing quote, return -1
			int cs = line.indexOf(BACKSLASH, ci);
			if ((cs < 0) || (cs > cq)) { return cq; }	// no backslash before quote, return index of closing quote

			ci = cs + 2;								// skip the escaped character and search again
		}
		return -1;										// no unescaped closing quote, return -1
	}

}
