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

  Contains contributions from Michael Camacho. 2012

 *************************************************************************************************/


package com.rfo.basic;

import java.util.ArrayList;
import java.util.Stack;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class Format extends ListActivity {

    private static Background theBackground;					// Background task ID
    private static ArrayAdapter<String> AA;
    private static ListView lv ;							    // The output screen list view
    private static ArrayList<String> output;					// The output screen text lines
    private static String restoreText;
    public static boolean blockQuote = false;


	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// if BACK key restore original text
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Editor.DisplayText = restoreText;
			Editor.mText.setText(Editor.DisplayText);
			Editor.mText.setSelection(0, 0);
			finish();
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}



    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        restoreText = Editor.DisplayText;

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

    	private String originalText;               // The original text
    	private String formattedText;			   // The formatted text
        private final String SPACES = " ";	  	   // Spaces for indenting
        private int indentLevel = 0;
        private boolean wasCase = false;
        private Stack<Integer> swStack = new Stack<Integer>();

        @Override
        protected  String doInBackground(String...str) {
			String Temp = "";
			blockQuote = false;
			for (int charCount=0; charCount < originalText.length(); ++charCount) {      // get each line
				if (originalText.charAt(charCount) == '\n') {							 // and and process it
					ProcessLine(Temp);							                     
					Temp = "";								
				} else {
					Temp = Temp + originalText.charAt(charCount);
				}
			}
            return "";
        }

        @Override
        protected void onPreExecute() {
            originalText = Editor.DisplayText;					// The original text from the editor
            formattedText = "";									// The resulting formatted text

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

        protected void onPostExecute(String result) {
            Editor.DisplayText = formattedText;					// Done. Transfer the formatted text
            Editor.mText.setText(Editor.DisplayText);			// to the Editor
            Editor.mText.setSelection(0, 0);					// Set the cursor at the top
            finish();
        }

        private void ProcessLine(String theLine) {							// Process one line
        	int blanks = CountBlanks(theLine, 0);
        	if (!isBlockQuote(theLine, blanks)) {
        		String aLine = ProcessKeyWords(theLine, blanks);			// Do the key words
        		theLine = ProcessIndents(aLine);							// And then do the indents
        	}
        	formattedText += theLine + '\n';
         	publishProgress(".");											// Show one dot for each line processed
        }
        
        private String ProcessIndents(String aLine) {							// Do the indenting
        	String temp = "";													// temp will contain the indent spaces
        	
        	aLine = aLine.trim();       									// Remove trailing and leading blanks
        	if (aLine.equals("")) return aLine;								// Skip blank or empty line

        	String xLine = aLine.toLowerCase();								// Convert to lower case
        	String theLine = "";
        	for (int i = 0; i < xLine.length(); ++i) {						// remove all blanks and tabs
        		char c = xLine.charAt(i);
        		if (c != ' ') 
        			if (c != '\t') theLine = theLine + c;
        	}

        	boolean processed = false;

        	if (theLine.startsWith("elseif") ||						// Indenting is determined by start of line key words
        		theLine.startsWith("else") ||
        		theLine.startsWith("endif") ||        		
        		theLine.startsWith("until") ||
        		theLine.startsWith("repeat") ||
        		theLine.startsWith("fn.end") ||
        		theLine.startsWith("next") 
        		) {
				indentLevel -= 1;					// Decrement indent level
				temp = DoIndent();					// Do the indent for this line
				processed = true;					// Indicate the line was processed
				wasCase = false;					// Nasty stuff to deal with Switch commands
        	}

        	if (theLine.startsWith("elseif") ||
				theLine.startsWith("fn.def") ||
				theLine.startsWith("else") ||
				theLine.startsWith("do") ||
				theLine.startsWith("while") ||
				theLine.startsWith("for")
				) {  
				if (wasCase) ++indentLevel;
				temp = DoIndent();
				indentLevel += 1;
				processed = true;
				wasCase = false;           			
			}

        	if (theLine.startsWith("if")) {							// Tricky processing for the
				if (wasCase) ++indentLevel;							// varieties of IF syntax
        		temp = DoIndent();
        		if (theLine.contains("then")) {
        			int x = theLine.indexOf("then");
        			String q = theLine.substring(x + 4);
        			if (q.length() > 0) {
        				if (q.startsWith("%"))  indentLevel += 1;
        			}
           			if (q.length() == 0) indentLevel += 1;
        		} else indentLevel += 1;
    			wasCase = false;
        		processed = true;
        	}

    		if (theLine.startsWith("sw.begin")) {						// Nasty stuff to deal with Switch
				if (wasCase) ++indentLevel;
				temp = DoIndent();
				swStack.push(indentLevel);
    			indentLevel += 1;
    			processed = true;
    			wasCase = true;
    		}

    		if (theLine.startsWith("sw.end")) {
    			if (!swStack.empty()) {
    				indentLevel = swStack.pop();
    			} else indentLevel = 0;
				temp = DoIndent();
    			processed = true;
    			wasCase = false;
			}

    		if (theLine.startsWith("sw.case")) {
    			temp = DoIndent();
//    			if (!wasCase) indentLevel += 1;
    			wasCase = true;
    			processed = true;
    		}

    		if (theLine.startsWith("sw.default")) {
    			temp = DoIndent();
    			if (!wasCase) indentLevel += 1;
    			wasCase = true;
    			processed = true;
    		}


        	if (theLine.startsWith("sw.break")) {
				if (wasCase) ++indentLevel;
        		temp = DoIndent();
        		indentLevel -= 1;
        		processed = true;
    			wasCase = true;
        	}

        	if (!processed) {								// Not processed above so do it now.
        		if (!theLine.startsWith("!"))
        			if (wasCase) ++indentLevel;
        		temp = DoIndent();
    			wasCase = false;
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

    private static boolean isBlockQuote(String aLine, int blanks) {	// Track start and end of block quotes
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

    private static synchronized String ProcessKeyWords(String actualLine, int blanks) {
    															// Find and capitalize the key words,
    															// leading blanks already counted
    	if (actualLine.equals("")) return actualLine;					// Skip empty line
    	if (blanks == actualLine.length()) return actualLine;			// Skip blank line
    	if (actualLine.startsWith("!", blanks)) return actualLine;		// Skip comment line

    	String lcLine = actualLine.toLowerCase();						// Convert to lower case

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
    				actualLine = actualLine.replaceFirst(xkw, kw.toUpperCase());      // Capitalize it
    				break;
    			}
    		}
		}

		if (kw.equals("if")) {													// Process IF statement
			actualLine = TestAndReplaceFirst("then", lcLine, actualLine);
			actualLine = TestAndReplaceFirst("else", lcLine, actualLine);
		}

		if (kw.equals("for")) {													// Process FOR statement
			actualLine = TestAndReplaceFirst("to", lcLine, actualLine);
			actualLine = TestAndReplaceFirst("step", lcLine, actualLine);
		}

		if (kw.endsWith(".")) {														//Process mulitipart commands.
			int start = blanks + kw.length();
			if (kw.equals("sql."))
				actualLine = ExpandedKeyWord(Run.SQL_kw, lcLine, actualLine, start);
			else if (kw.equals("gr."))
				actualLine = ExpandedKeyWord(Run.GR_kw, lcLine, actualLine, start);
			else if (kw.equals("audio."))
				actualLine = ExpandedKeyWord(Run.Audio_KW, lcLine, actualLine, start);
			else if (kw.equals("sensors."))
				actualLine = ExpandedKeyWord(Run.Sensors_KW, lcLine, actualLine, start);
			else if (kw.equals("gps."))
				actualLine = ExpandedKeyWord(Run.GPS_KW, lcLine, actualLine, start);
			else if (kw.equals("array."))
				actualLine = ExpandedKeyWord(Run.Array_KW, lcLine, actualLine, start);
			else if (kw.equals("list."))
				actualLine = ExpandedKeyWord(Run.List_KW, lcLine, actualLine, start);
			else if (kw.equals("bundle."))
				actualLine = ExpandedKeyWord(Run.Bundle_KW, lcLine, actualLine, start);
			else if (kw.equals("socket."))
				actualLine = ExpandedKeyWord(Run.Socket_KW, lcLine, actualLine, start);
			else if (kw.equals("debug."))
				actualLine = ExpandedKeyWord(Run.Debug_KW, lcLine, actualLine, start);
			else if (kw.equals("stack."))
					actualLine = ExpandedKeyWord(Run.Stack_KW, lcLine, actualLine, start);
			else if (kw.equals("ftp."))
				actualLine = ExpandedKeyWord(Run.ftp_KW, lcLine, actualLine, start);
			else if (kw.equals("bt."))
				actualLine = ExpandedKeyWord(Run.bt_KW, lcLine, actualLine, start);
			else if (kw.equals("ftp."))
				actualLine = ExpandedKeyWord(Run.ftp_KW, lcLine, actualLine, start);
			else if (kw.equals("su."))
				actualLine = ExpandedKeyWord(Run.su_KW, lcLine, actualLine, start);
			else if (kw.equals("soundpool."))
				actualLine = ExpandedKeyWord(Run.sp_KW, lcLine, actualLine, start);
			else if (kw.equals("html."))
				actualLine = ExpandedKeyWord(Run.html_KW, lcLine, actualLine, start);
			else if (kw.equals("timer."))
				actualLine = ExpandedKeyWord(Run.Timer_KW, lcLine, actualLine, start);
			else if (kw.equals("timezone."))
				actualLine = ExpandedKeyWord(Run.TimeZone_KW, lcLine, actualLine, start);
		}
    	return actualLine;
    }

    private static String ExpandedKeyWord(String[] words, String lcLine, String actualLine, int start) {  // The stuff after xxx.
    	for (int i = 0; i < words.length; ++i) {
			if (lcLine.startsWith(words[i], start)) {
     			String xkw = actualLine.substring(start, start + words[i].length());
    			actualLine = actualLine.replaceFirst(xkw, words[i].toUpperCase());
			}
    	}
    	return actualLine;
    }

    private static String TestAndReplaceFirst(String kw, String lcLine, String actualLine) {			//Find and replace first occurrence
		int k = lcLine.indexOf(kw);
		if (k >= 0) {
			int kl = kw.length();
			String xkw = actualLine.substring(k, k + kl);
			actualLine = actualLine.replaceFirst(xkw, kw.toUpperCase());

			if (kw.equals("then") || kw.equals("else")) {							// Special case THEN and ELSE
				int start = k + kl + CountBlanks(lcLine, kl);						// Skip keyword and blanks
				actualLine = StartOfLineKW(lcLine, actualLine, start);
			}
 		}
		return actualLine;

    }

    private static String TestAndReplaceAll(String kw, String lcLine, String actualLine) {				// Find and replace all occurrences
		int k = lcLine.indexOf(kw, 0);								// Find instance of keyword
		if (k < 0) { return actualLine; }							// No instances of this keyword - quick exit

		StringBuilder sb = new StringBuilder(actualLine);
		String KW = kw.toUpperCase();								// Upper-case version of keyword
		int kl = kw.length();
		int limit = lcLine.length() - (kl - 1);						// Another keyword won't fit after limit
		int start = 0;
		do {
			int q1 = lcLine.indexOf("\"", start);
			while (q1 >= 0 && q1 < k) {								// Is this instance in a quoted string?
				int q2 = lcLine.indexOf("\"", q1 + 1);
				if (q2 < 0) { return sb.toString(); }				// Unterminated string, give up
				if (k < q2) { k = lcLine.indexOf(kw, q2 + 1); }		// This instance is in quotes, find next
				q1 = lcLine.indexOf("\"", q2 + 1);
			}
			if (k < 0) break;										// Last instance is in quotes

			char c = (k > 0) ? lcLine.charAt(k - 1) : '\0';			// Is keyword string embedded in a variable name?
			if (c == '_' || c == '@' || c == '#' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9')) {
				start = k + kl;										// Not a keyword, skip it
			} else {
				sb.replace(k, k + kl, KW);
				start = k + kl;
			}

			if (start > limit) { break; }							// No more instances of this keyword
			k = lcLine.indexOf(kw, start);							// Find next instance of keyword
		} while (k >= 0);

		return sb.toString();
    }

}
