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
import java.util.Stack;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Format extends ListActivity {

	private static final String LOGTAG = "Run";

	private static final char ASCII_SPACE = ' ';
	private static final char ASCII_QUOTE = '\"';
	private static final char BACK_SLASH  = '\\';
	private static final char LEFT_QUOTE  = '\u201C';
	private static final char RIGHT_QUOTE = '\u201D';
	private static final char NBSP        = '\uC2A0';

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
    	private String formattedText;			   // The formatted text
        private final String SPACES = " ";	  	   // Spaces for indenting
        private int indentLevel = 0;
        private Stack<Integer> swStack = new Stack<Integer>();

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
        protected void onProgressUpdate(String... str) {     // Called when publishProgress() is executed.

        	for (int i=0; i < str.length; ++i) {				  // Form strings of 20 characters
        		String s = output.get(output.size() - 1);
        		s = s + str[i];
        		output.set(output.size() - 1, s);
        		if (s.length() >= 20) {						  // After the 20th character
        			output.add("");							  // start a new line
        		}
        	}

        	setListAdapter(AA);						// show the output
	    	lv.setSelection(output.size() - 1);		// set last line as the selected line to scroll
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
				theLine = fixQuotesAndNbsp(theLine);				// Fix quotes, remove &nbsp
				theLine = ProcessKeyWords(theLine, blanks);			// Do the key words
				theLine = ProcessIndents(theLine);					// And then do the indents
			}
			formattedText += theLine + '\n';
			publishProgress(".");									// Show one dot for each line processed
		}

        private String ProcessIndents(String aLine) {							// Do the indenting
        	String temp = "";													// temp will contain the indent spaces
        	
        	aLine = aLine.trim();       									// Remove trailing and leading blanks
        	if (aLine.equals("")) return aLine;								// Skip blank or empty line

        	String xLine = aLine.toLowerCase(Locale.US);					// Convert to lower case
        	String theLine = "";
        	for (int i = 0; i < xLine.length(); ++i) {						// remove all blanks and tabs
        		char c = xLine.charAt(i);
        		if (c != ' ') 
        			if (c != '\t') theLine = theLine + c;
        	}

        	if (theLine.startsWith("endif") ||						// Indenting is determined by start of line key words
        		theLine.startsWith("until") ||						// This group ends a block, reducing indent by 1
        		theLine.startsWith("repeat") ||
        		theLine.startsWith("fn.end") ||
        		theLine.startsWith("next") 
        		) {
				indentLevel -= 1;					// Decrement indent level
				temp = DoIndent();					// Do the indent for this line
        	}
			else
        	if (theLine.startsWith("fn.def") ||						// This group starts a block, increasing subsequent indent by 1
				theLine.startsWith("do") ||
				theLine.startsWith("while") ||
				theLine.startsWith("for")
				) {
				temp = DoIndent();
				indentLevel += 1;
			}
			else
			if (theLine.startsWith("else")) {						// This is both "else" and "elseif";
				indentLevel -= 1;									// they end one block and start another
				temp = DoIndent();
				indentLevel += 1;
			}
			else
        	if (theLine.startsWith("if")) {							// Tricky processing for the varieties of IF syntax
        		temp = DoIndent();
        		if (theLine.contains("then")) {
        			int x = theLine.indexOf("then");
        			String q = theLine.substring(x + 4);
        			if (q.length() > 0) {
        				if (q.startsWith("%"))  indentLevel += 1;
        			}
           			if (q.length() == 0) indentLevel += 1;
        		} else indentLevel += 1;
        	}
			else
			if (theLine.startsWith("sw.")) {						// Nasty stuff to deal with Switch
				if (theLine.startsWith("begin, 3")) {
					temp = DoIndent();
					swStack.push(indentLevel);
					indentLevel += 1;
				}
				else
				if (theLine.startsWith("end", 3)) {
					indentLevel = swStack.empty() ? 0 : swStack.pop();
					temp = DoIndent();
				}
				else
				if (theLine.startsWith("case", 3) ||
					theLine.startsWith("default", 3)
					) {
					indentLevel = swStack.empty() ? 1 : (swStack.peek() + 1);
					temp = DoIndent();
					indentLevel += 1;
				}
				else {												// If break (valid) or anything else (invalid)
					temp = DoIndent();								// just do normal indenting
				}
			}
			else {											// Not processed above so do it now.
        		temp = DoIndent();
        	}

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
    		if ((c != ' ') && (c != '\t')) break;
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
    	int blanks = CountBlanks(actualLine, 0);				// First skip leading blanks
    	return ProcessKeyWords(actualLine, blanks);
    }

    private static String ProcessKeyWords(String actualLine, int blanks) {
    															// Find and capitalize the key words,
    															// leading blanks already counted
    	if (actualLine.equals("")) return actualLine;					// Skip empty line
    	if (blanks == actualLine.length()) return actualLine;			// Skip blank line
    	if (actualLine.startsWith("!", blanks)) return actualLine;		// Skip comment line

    	String lcLine = actualLine.toLowerCase(Locale.US);				// Convert to lower case

    	actualLine = StartOfLineKW(lcLine, actualLine, blanks);

    	for (int i = 0; i < Run.MathFunctions.length; ++i) {										// Process math functions
    		actualLine = TestAndReplaceAll(Run.MathFunctions[i], lcLine, actualLine);
    	}

    	for (int i = 0; i < Run.StringFunctions.length; ++i) {									// Process String Functions
    		actualLine = TestAndReplaceAll(Run.StringFunctions[i], lcLine, actualLine);
    	}

    	return actualLine;			// Done, return results

    }

    private static String StartOfLineKW(String lcLine, String actualLine, int blanks) {
    	String kw = "";
    	for (int i = 0; i < Run.BasicKeyWords.length; ++i) {				// If the line starts with a key word
    		kw = Run.BasicKeyWords[i];
    		if (!kw.equals(" ")) {
    			int k = lcLine.indexOf(kw, blanks);
    			if (k >= 0 && k == blanks) {
    				String xkw = actualLine.substring(blanks, blanks + kw.length());
    				if (xkw.equals("inkey$")) xkw = "inkey";
    				actualLine = actualLine.replaceFirst(xkw, kw.toUpperCase(Locale.US)); // Capitalize it
    				break;
    			}
    		}
		}

		if (kw.equals("if")) {													// Process IF statement
			actualLine = TestAndReplaceFirst("then", lcLine, actualLine);
			actualLine = TestAndReplaceFirst("else", lcLine, actualLine);
		}

		if (kw.equals("elseif")) {												// Process ELSEIF statement
			actualLine = TestAndReplaceFirst("then", lcLine, actualLine);
		}

		if (kw.equals("for")) {													// Process FOR statement
			actualLine = TestAndReplaceFirst("to", lcLine, actualLine);
			actualLine = TestAndReplaceFirst("step", lcLine, actualLine);
		}

		if (kw.endsWith(".")) {														//Process mulitipart commands.
			int start = blanks + kw.length();
			String[] list = Run.getKeywordLists().get(kw);
			if (list != null) {
				actualLine = ExpandedKeyWord(list, lcLine, actualLine, start);
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

    private static String TestAndReplaceFirst(String kw, String lcLine, String actualLine) {			//Find and replace first occurrence
		int k = lcLine.indexOf(kw);
		if (k >= 0) {
			int kl = kw.length();
			String xkw = actualLine.substring(k, k + kl);
			actualLine = actualLine.replaceFirst(xkw, kw.toUpperCase(Locale.US));

			if (kw.equals("then") || kw.equals("else")) {							// Special case THEN and ELSE
				int start = k + kl + CountBlanks(lcLine, kl);						// Skip keyword and blanks
				actualLine = StartOfLineKW(lcLine, actualLine, start);
			}
 		}
		return actualLine;

    }

	private static String TestAndReplaceAll(String kw, String lcLine, String actualLine) {	// Find and replace all occurrences
		int start = 0;
		int k = lcLine.indexOf(kw, start);							// find instance of keyword
		if (k < 0) { return actualLine; }							// no instances of this keyword - quick exit

		StringBuilder sb = new StringBuilder(actualLine);
		String KW = kw.toUpperCase(Locale.US);						// upper-case version of keyword
		int kl = kw.length();
		int limit = lcLine.length() - (kl - 1);						// another keyword won't fit after limit
		while (k >= 0) {
			int q1 = lcLine.indexOf(ASCII_QUOTE, start);
			int q2 = skipQuotedSubstring(lcLine, q1);
			if ((q2 >= 0) && (q2 < k)) { start = ++q2; continue; }	// skip quoted substrings before keyword

			while ((k >= 0) && ((q2 < 0) || (k < q2))) {			// process all keywords before end of quoted substring
				if ((q1 < 0) || (k < q1)) {							// keyword is not in the quoted substring
					boolean embedded = ((k > 0) &&
						Run.isVarChar(lcLine.charAt(k - 1)));		// embedded if preceding character is part of a variable name
					if (!embedded) { sb.replace(k, k + kl, KW); }	// replace kw with upper-case copy
					start = k + kl;									// skip to the end of the keyword
				} else {
					start = 1 + ((q2 >= 0) ? q2 : limit);			// skip to the end of the quoted substring
				}
				k = (start <= limit) ? lcLine.indexOf(kw, start) : -1;	// look for next instance of keyword
			}
		}

		return sb.toString();
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
		if (start < 0) return start;								// no quoted substring

		int size = line.length();
		int ci = start + 1;											// skip the opening quote
		for ( ; ci < size; --ci) {
			ci = line.indexOf('\"', ci);
			if (ci < 0) break;										// no closing quote, done
			if (line.charAt(ci - 1) != BACK_SLASH) break;			// closing quote, unless escaped
		}
		return ci;													// return index of closing quote, or -1 if none
	}

}
