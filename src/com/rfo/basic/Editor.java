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

 *************************************************************************************************/

package com.rfo.basic;

import com.rfo.basic.R;
import com.rfo.basic.Basic;
import 	android.content.pm.ActivityInfo;

import android.app.AlertDialog;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.text.Editable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.view.View.BaseSavedState;
import android.view.View.OnKeyListener;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import 	android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.Toast;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import 	android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.SensorManager;
import 	android.graphics.Typeface;

import java.util.ArrayList;


public class Editor extends Activity {
    private static final String LOGTAG = "Editor";
    private static final String CLASSTAG = Editor.class.getSimpleName();
//  Log.v(Editor.LOGTAG, " " + Editor.CLASSTAG + " String Var Value =  " + d);

//    public static Intent theProgramRunner;           // Intents that will be called
    public static Intent theDeleter;
    public static Intent theEditor;

    public static EditText mText;					// The Editors display text buffers

    public static String DisplayText = "REM Start of BASIC! Program\n";
    public static int SyntaxErrorDisplacement = -1;
 
    public static boolean LoadAfterSave = false;    // Various flags. Should be private, not public
    public static boolean ClearAfterSave = false;
    public static boolean RunAfterSave = false;
    public static boolean BlockFlag = false;
	public static String stemp="";
    public static View theView;

    public static int selectionStart;
    public static int selectionEnd;
    public static final String Name = "BASIC! Program Editor - ";

    public static boolean waitSelect = false;
    public static long startTime;						// Time used for blocking re-run




    public static class LinedEditText extends EditText {          // Part of the edit screen setup
        private Rect mRect;
        private Paint mPaint;

        private Scroller mScroller;							// The scroller object
        private VelocityTracker mVelocityTracker;			// The velocity tracker
        private int mScrollY = 0;							// The current scroll location
        private float mLastMotionY;							// Start of last movement
        private int mMinScroll;                             // Minimum scroll distance
        private int mFlingV;								// Minimum velocity for fling
        public static int sHeight;							// Screen height minus the crap at top
        public static boolean didMove;						// Determines if super called on UP
        public static Context LEcontext;



// ************** Methods for drawing Lined Edit Text ****************8         


        public LinedEditText(Context context, AttributeSet attrs) {
            super(context, attrs);

            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(0x800000FF);
            theView = findViewById(R.id.basic_text);
            InitScroller(context);
            LEcontext = context;

        }

        @Override
        protected void  onTextChanged(CharSequence  text, int start, int before, int after) {

// Here we are monitoring for text changes so that we can set Saved properly

			int i = text.length();						        // When the text is changed
			if (i > 0 && i != Basic.InitialProgramSize) {			// Make sure it is a real change
            	Basic.Saved = false;					        // and indicate then indicate not saved
            }

			super.onTextChanged(text, start, before, after);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if (Settings.getEditorColor(LEcontext).equals("BW")) {
          	    mText.setTextColor(0xff000000);
          	    mText.setBackgroundColor(0xffffffff);
          	    mPaint.setColor(0x80000000);
            } else
			if (Settings.getEditorColor(LEcontext).equals("WB")) {
				mText.setTextColor(0xffffffff);
				mText.setBackgroundColor(0xff000000);
				mPaint.setColor(0x80ffffff);
            } else
			if (Settings.getEditorColor(LEcontext).equals("WBL")) {
				mText.setTextColor(0xffffffff);
				mText.setBackgroundColor(0xff006478);
				mPaint.setColor(0x80000000);
			}             	

            mText.setTextSize(1, Settings.getFont(LEcontext));
            int count = getLineCount();					// Draw the lines under each line of text

            Rect r = mRect;
            Paint paint = mPaint;

            if (Settings.getLinedEditor(LEcontext)) {
				for (int i = 0; i < count; i++) {
					int baseline = getLineBounds(i, r);
					canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
				}
            }
            sHeight = getHeight();						// This is where we get the screen height
            super.onDraw(canvas);
        }

		// *************  Methods for scrolling *****************************8

        public void InitScroller(Context context) {
        	mScroller = new Scroller(context);       // Get a scroller object
        	mScrollY = 0 ;   					    // Set beginning of program as top of screen.
//        	mMinScroll = getLineHeight ()/2;	        // Set minimum scroll distance
        	mMinScroll = 1;	        // Set minimum scroll distance

        	mFlingV = 750;                         // Minimum fling velocity
//        	mScroller.setFriction((float) 10);

        }

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);

			if (mVelocityTracker == null) {						// If we do not have velocity tracker
				mVelocityTracker = VelocityTracker.obtain();   // then get one
			}
			mVelocityTracker.addMovement(event);               // add this movement to it

			final int action = event.getAction();  // Get action type
			final float y = event.getY();          // Get the displacement for the action

			switch (action) {

				case MotionEvent.ACTION_DOWN:       	// User has touched screen
					if (!mScroller.isFinished()) {		// If scrolling, then stop now
						mScroller.abortAnimation();
					}
					mLastMotionY = y;                  // Save start (or end) of motion
					mScrollY = this.getScrollY();				// Save where we ended up
					mText.setCursorVisible(true);
					didMove = false;

					break;

				case MotionEvent.ACTION_MOVE:          // The user finger is on the move
					didMove = true;
					final int deltaY = (int) (mLastMotionY - y);  // Calculate distance moved since last report
					mLastMotionY = y;							   // Save the start of this motion

					if (deltaY < 0) {								// If user is moving finger up screen
						if (mScrollY > 0) {						// and we are not at top of text
							int m = mScrollY - mMinScroll;         // Do not go beyond top of text
							if (m < 0) {
								m = mScrollY; 
							} else m = mMinScroll;

							scrollBy(0, -m);                           // Scroll the text up
						}
					} else 
					if (deltaY > 0) {            				 // The user finger is moving up
						int max = getLineCount() * getLineHeight() - sHeight;   // Set max up value
						if (mScrollY < max - mMinScroll) {
							scrollBy(0, mMinScroll);			// Scroll up
						}
					}
//             postInvalidate();
					break;

				case MotionEvent.ACTION_UP:                       // User finger lifted up
					final VelocityTracker velocityTracker = mVelocityTracker;  	// Find out how fast the finger was moving
					velocityTracker.computeCurrentVelocity(mFlingV);          
					int velocityY = (int) velocityTracker.getYVelocity();

					if (Math.abs(velocityY) > mFlingV) {								// if the velocity exceeds threshold
						int maxY = getLineCount() * getLineHeight() - sHeight;		// calculate maximum Y movement
						mScroller.fling(0, mScrollY, 0, -velocityY, 0, 0, 0, maxY);	// Do the filng
					} else {
						if (mVelocityTracker != null) {								// If the velocity less than threshold
							mVelocityTracker.recycle();								// recycle the tracker
							mVelocityTracker = null;
						}
					}
					break;
			}

			mScrollY = this.getScrollY();				// Save where we ended up

			return true ;									// Tell caller we handled the move event
		}

		@Override
		public void computeScroll() {					// Called while flinging to execute a fling step
			if (mScroller.computeScrollOffset()) {		 
				mScrollY = mScroller.getCurrY();		// Get where we should scroll to 
				scrollTo(0, mScrollY);					// and do it
				postInvalidate();						// the redraw the sreem
			}
		}



    }

// ************************* End of LinedEdit Class  ****************************** //

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);                 // Setup and the display the text to be edited

		if (Basic.BasicContext == null) {							         // If we have lost context then
			android.os.Process.killProcess(android.os.Process.myPid()) ;   // things have gone very bad. Die!
		}

		/*
		 * Open up the view.
		 * 
		 * The LinedEdit Class code will get control at this point and draw the blank screen.
		 * 
		 * When that is done, the rest of the code here will be execute.
		 */
		setContentView(R.layout.editor);
		setTitle(Name + Basic.ProgramFileName);

		mText = (EditText) theView;		// mText is the TextView Object
		mText.setTypeface(Typeface.MONOSPACE);
		mText.setMinLines(4096);
		mText.setText(DisplayText);		// Put the text lines into Object
		mText.setCursorVisible(true);

		int SO = Settings.getSreenOrientation(this);
		setRequestedOrientation(SO);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Ignore the back key
			return true;									// Do not allow backing out of Editor
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event)  {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			// Ignore the back key
			return true;									// Do not allow backing out of Editor
		}

// Do on the fly formatting upon ENTER
        if (!Settings.getAutoIndent(this)) return false;     // Don't do the formatting if the user does not want it

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0) {
            int selection = mText.getSelectionEnd();								// Split the text into two parts
            String theText = mText.getText().toString();							// The text before the ENTER
            String fText = "";														// and the Text after the ENTER
            if (selection > 1)
            	fText = theText.substring(0, selection - 1);
            String eText = "";
            eText = theText.substring(selection);

            int lineStart = 0;														// Backtrack over the before text
            if (selection - 2 > 0) {													// to find the start of the last
            	for (lineStart = selection - 2; lineStart > 0 ; --lineStart) {			// before ENTER line
            		char c = theText.charAt(lineStart);
            		if (c == '\n') break;
            	}
            	if (lineStart > 0) ++lineStart;
            } else lineStart = 0;


            String blanks = "";														// Now, count the leading blanks
            for (int i = lineStart; i < selection - 1; ++i) {							// in the last before ENTER line
            	char c = fText.charAt(i);
            	if (c != ' ')
            		if (c != '\t') break;
            	blanks = blanks + " ";
            }

            if (fText.endsWith("#")) {												// If formatting of line was wanted
            	String theLine = fText.substring(lineStart, fText.length() - 1);		// go format the line
            	String newLine = ProcessKeyWords(theLine);
            	String aLine = fText.substring(0, lineStart);
            	fText = aLine + newLine;
            }

            theText = fText + "\n" + blanks + eText;								// Put together the final text
            mText.setText(theText);													// and set the selection after the blanks							
            mText.setSelection(fText.length() + 1 + blanks.length(), fText.length() + 1 + blanks.length());
        	return true;									
        }


		return super.onKeyUp(keyCode, event);
	}

    @Override
    protected void onResume() {
        super.onResume();
//        Log.v(Editor.LOGTAG, " " + Editor.CLASSTAG + " onResume " + Basic.DoAutoRun);
        if (Basic.DoAutoRun) {
			android.os.Process.killProcess(android.os.Process.myPid()) ;
        } else {
        	if (SyntaxErrorDisplacement >= 0 &&
				SyntaxErrorDisplacement < AddProgramLine.lineCharCounts.size()) {	// If run ended in error, select error line

        		int end = AddProgramLine.lineCharCounts.get(SyntaxErrorDisplacement);  // Get selection end
        		if (end >= DisplayText.length()) end = DisplayText.length() - 1;
        		int start = end - 1;										// back up over the new line

        		for (start = end - 1; start > 0 ; --start) {				// Scan for previous nl or start
        			char c = DisplayText.charAt(start);
        			if (c == '\n') {
        				start = start + 1;
        				break;
        			}
        		}
				DisplayText = mText.getText().toString();
				Editor.mText.setText(Editor.DisplayText);
				mText.setSelection(start, end);							// Set the selection
				mText.setCursorVisible(true);
				SyntaxErrorDisplacement = -1;							// Reset the value

				int SO = Settings.getSreenOrientation(this);

				setRequestedOrientation(SO);

        	}
        	setTitle(Name + Basic.ProgramFileName);


        }

    }    

    @Override
    protected void onPause() {
        super.onPause();
//        Log.v(Editor.LOGTAG, " " + Editor.CLASSTAG + " onPause");
    }    

    @Override
    protected void onStart() {
        super.onStart();
//        Log.v(Editor.LOGTAG, " " + Editor.CLASSTAG + " onStart");
    }    

    @Override
    protected void onRestart() {
        super.onRestart();
//        Log.v(Editor.LOGTAG, " " + Editor.CLASSTAG + " onRestart");
    }    
    @Override
    protected void onDestroy() {
        super.onDestroy();
//        Log.v(Editor.LOGTAG, " " + Editor.CLASSTAG + " onDestroy");
    }    


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {     // When the user presses Menu
		super.onCreateOptionsMenu(menu);					// set up and display the Menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {   // A menu item has been selected
    	AlertDialog.Builder builder = null;
    	AlertDialog alert = null;

		switch (item.getItemId()) {

			case R.id.run:										// Run
				if (Basic.Saved) {								// If current program has been saved
					ClearAfterSave = false;						// we are not doing clear and
					RunAfterSave = false;						// we do not need to save first
					Run();									// then load the program
					return(true);
				}

				// The current program has not been saved. Ask the user if she wants to save it
				// before we load a new program

				builder = new AlertDialog.Builder(this);   // Current program not saved
				builder.setMessage("Current Program Not Saved!")				  // Ask user if she wants to save first
					.setCancelable(true)									  // Do not allow user to BACK key out of dialog
					.setPositiveButton("Save", new DialogInterface.OnClickListener() {   // User selected save first
						public void onClick(DialogInterface dialog, int id) {
							RunAfterSave = true;								// Tell the saver to do Load
							saveFile();											// after the save is done
						}
					})
					.setNegativeButton("Continue", new DialogInterface.OnClickListener() { 	// User did not want to save
						public void onClick(DialogInterface dialog, int id) {
							Run();									  					// load the file
						}
					})
					.setOnCancelListener(new DialogInterface.OnCancelListener(){
						public void onCancel(DialogInterface arg0) {			// User has canceled save
							return;												// done
						}
					});
				alert = builder.create();
				alert.show();

				return true;

			case R.id.load:										// LOAD

				if (Basic.Saved) {								// If current program has been saved
					ClearAfterSave = false;						// we are not doing clear and
					LoadAfterSave = false;						// we do not need to save first
					loadFile();									// then load the program
					return(true);
				}

				// The current program has not been saved. Ask the user if she wants to save it
				// before we load a new program

				builder = new AlertDialog.Builder(this);   // Current program not saved
				builder.setMessage("Current Program Not Saved!")				  // Ask user if she wants to save first
					.setCancelable(true)									  // Do not allow user to BACK key out of dialog
					.setPositiveButton("Save", new DialogInterface.OnClickListener() {   // User selected save first
						public void onClick(DialogInterface dialog, int id) {
							LoadAfterSave = true;								// Tell the saver to do Load
							saveFile();											// after the save is done
						}
					})
					.setNegativeButton("Continue", new DialogInterface.OnClickListener() { 	// User did not want to save
						public void onClick(DialogInterface dialog, int id) {
							loadFile();									  					// load the file
						}
					})
					.setOnCancelListener(new DialogInterface.OnCancelListener(){
						public void onCancel(DialogInterface arg0) {			// User has canceled save
							return;												// done
						}
					});
				alert = builder.create();
				alert.show();

				return true;

			case R.id.save:										// SAVE

				saveFile();
				return true;

			case R.id.search:
				if (mText == null) android.os.Process.killProcess(android.os.Process.myPid()) ;
				DisplayText = mText.getText().toString();
				selectionStart = mText.getSelectionStart();
				selectionEnd = mText.getSelectionEnd();
				startActivity(new Intent(this, Search.class));			// Start the search activity
				return true;

			case R.id.format:
				if (mText == null) android.os.Process.killProcess(android.os.Process.myPid()) ;
				DisplayText = mText.getText().toString();
				startActivity(new Intent(this, Format.class));			// Start the format activity
				Basic.Saved = false;
				return true;

			case R.id.delete:									// DELETE
				DisplayText = mText.getText().toString();				// get the text being displayed


				// First make sure that the SD Card is present and can be written to

				boolean mExternalStorageAvailable = false;
				boolean mExternalStorageWriteable = false;
				String state = Environment.getExternalStorageState();

				if (Environment.MEDIA_MOUNTED.equals(state)) {			// Insure that SD is mounted
					// We can read and write the media
					mExternalStorageAvailable = mExternalStorageWriteable = true;
				} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
					// We can only read the media
					mExternalStorageAvailable = true;
					mExternalStorageWriteable = false;
				} else {
					// Something else is wrong. It may be one of many other states, but all we need
					//  to know is we can neither read nor write
					mExternalStorageAvailable = mExternalStorageWriteable = false;
				}   	

				boolean CanWrite = mExternalStorageAvailable && mExternalStorageWriteable;

				// if the SD Card is not available or writable pop some toast and do
				// not call Delete

				if (!CanWrite) {
					Toaster("External storage not available or not writable.");
					return true;
				}


				theDeleter = new Intent(this, Delete.class);		//Go to Delete Method
				startActivity(theDeleter);
//           finish();
				return true;

			case R.id.clear:										// Clear

				if (Basic.Saved) {								// If program has been saved
					ClearAfterSave = false;						// then do the clear
					LoadAfterSave = false;
					Basic.clearProgram();
					Basic.ProgramFileName = "";
					setTitle(Name + Basic.ProgramFileName);
					mText.setText(DisplayText);
					Editor.mText.setText(Editor.DisplayText);
					Editor.mText.setTextSize(1, Settings.getFont(this));
					/*               if (Settings.getEditorColor(this).equals("BW")){
					 Editor.mText.setTextColor(0xff000000);
					 Editor.mText.setBackgroundColor(0xffffffff);
					 } else{
					 Editor. mText.setTextColor(0xffffffff);
					 Editor. mText.setBackgroundColor(0xff000000);
					 }*/
					int SO = Settings.getSreenOrientation(this);
					setRequestedOrientation(SO);


					return(true);
				}

				// The current program has not been saved. Ask the user if he wants
				// to save before doing the clear.

				builder = new AlertDialog.Builder(this);
				builder.setMessage("Current Program Not Saved!")
					.setCancelable(true)												// Do not allow user to BACK key out
					// of the dialog

					.setPositiveButton("Save", new DialogInterface.OnClickListener() {   // User says to save first
						public void onClick(DialogInterface dialog, int id) {
							ClearAfterSave = true;										// Indicate to saver to
							saveFile();													// clear after saving
						}
					})

					.setNegativeButton("Continue", new DialogInterface.OnClickListener() {    // User says Do not save
						public void onClick(DialogInterface dialog, int id) {
							Basic.clearProgram();									// Clear the program
							Basic.ProgramFileName = "";								// Clear the current program file name
							setTitle(Name + Basic.ProgramFileName); 
							Basic.Saved = true;
							Basic.InitialProgramSize = Editor.DisplayText.length();	// Restart editor with cleared program
//    	      		  startActivity( new Intent(Editor.this, Editor.class));
//    	        	   finish();
							mText.setText(DisplayText);
						}

					})
					.setOnCancelListener(new DialogInterface.OnCancelListener(){
           				public void onCancel(DialogInterface arg0) {			// User has canceled save
           					return;												// done
           				}
           		    });

				alert = builder.create();
				alert.show();
				return(true);

			case R.id.help:											// HELP
				startActivity(new Intent(this, Help.class));			// Start the help activity
				return true;

			case R.id.about:											// ABOUT
				String version = Basic.BasicContext.getString(R.string.version);   // Get the version string
				String url = "http://laughton.com/basic/versions/v";				  // add it to the URL
				url = url + version + "/index.html";
				Intent i = new Intent(Intent.ACTION_VIEW);						  // Go to the About web page
				i.setData(Uri.parse(url));
				startActivity(i);           
				return true;

			case R.id.settings:										// SETTINGS
				startActivity(new Intent(this, Settings.class));		// Start the Setting activity
				waitSelect = true;
				return true;

			case R.id.exit:
				finish();
				android.os.Process.killProcess(android.os.Process.myPid()) ;
				return true;

			default:
				return true;
		}
    }

    private void  Run() {

		/* Run a program
		 * Create a new Basic.lines object and then copy
		 * the display text buffer to it. 
		 * 
		 * The display buffer is one big string. We need
		 * to step through it looking for \n (newline) characters.
		 * Each \n marks a new line for Basic.lines
		 */
    	
    	AddProgramLine APL = new AddProgramLine();
    	
		DisplayText = mText.getText().toString();
		String Temp = "";


		for (AddProgramLine.charCount = 0; AddProgramLine.charCount < DisplayText.length(); ++AddProgramLine.charCount) {      // Grab the display text
			if (DisplayText.charAt(AddProgramLine.charCount) == '\n') {						// and add it to Basic.Lines
				APL.AddLine(Temp, true);												// with some editing.
				Temp = "";								
			} else {
				Temp = Temp + DisplayText.charAt(AddProgramLine.charCount);
			}
		}
		if (Temp.length() != 0) APL.AddLine(Temp, true);				// Do not add empty lines

		if (Basic.lines.size() == 0) {						// If the program is empty
			Basic.lines.add("@@@");						// add Nothing to run command
		}												

		Basic.theProgramRunner = new Intent(this, Run.class);		//now go run the program
		Basic.theRunContext = null;                      			// Run will set theRunContext to non-null value
		SyntaxErrorDisplacement = -1 ;


		/*       long NowTime = SystemClock.uptimeMillis();
		 long interval = NowTime - startTime;
		 if ( interval < 10000){
		 try {Thread.sleep((int) 10000 - interval);}catch(InterruptedException e){};
		 } */
		startActivity(Basic.theProgramRunner);
    }



    private void loadFile() {


    	boolean CanRead = true;
    	// Insure that the SD Card is present

		String state = Environment.getExternalStorageState();	// Make sure SD is care is present
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			CanRead = false;
			Context context = getApplicationContext();          // if SD card not available, popup
			CharSequence text = "External storage not available.";		// some toast and do not go to LoadFile
			int duration = Toast.LENGTH_LONG;
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
//	     		  startActivity( new Intent(Editor.this, Editor.class));
//		        finish();
			return;
		}

    	if (CanRead) {												    // If the SD Card can be read
    		Intent intent = new  Intent(Editor.this, LoadFile.class);   // Go to the LoadFile method
    		startActivity(intent);    // Now go Load
//     		finish();
    	}
	}

    private void saveFile() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);          // Get the filename from user
		final EditText input = new EditText(this);
		input.setText(Basic.ProgramFileName);                         // If the program has a name
		// put it in the dialog box
		alert.setView(input);
		alert.setCancelable(true);									// Allow the dialog to be canceled
		alert.setTitle("Save As..");
		alert.setOnCancelListener(new DialogInterface.OnCancelListener(){
				public void onCancel(DialogInterface arg0) {			// User has canceled save
					return;												// done
				}
			});

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  //Have a filename        	        	
				public void onClick(DialogInterface dialog, int whichButton) {
					String theFilename = input.getText().toString().trim();

					Basic.clearProgram();									// Clear Basic.lines
					Basic.lines.remove(0);									// including that REM statement
					DisplayText = mText.getText().toString();				// get the text being displayed
					String Temp1 = "";
					boolean LineAdded = false;
					for (int k=0; k < DisplayText.length(); ++k) {			    // Move the display text to Basic.lines
						if (DisplayText.charAt(k) == '\n') {
							Basic.lines.add(Temp1);
							Temp1 = "";
							LineAdded = true;
						} else {
							Temp1 = Temp1 + DisplayText.charAt(k);
							LineAdded = false;
						}
					}
					if (!LineAdded) {											// Special case for line
						Basic.lines.add(Temp1);								// without \n
					}

					writeTheFile(theFilename);								// Now go write the file
				}});
		alert.show();
    }

    private void writeTheFile(String theFileName) { 	

        String DirPart = "";
        if (theFileName.contains("/")) {                     // if name contains a path seperator 

        	int k = theFileName.length() - 1;
        	char c;
        	do {                                            // Find the rightmost /
        		c = theFileName.charAt(k);
        		--k;
        	} while (c != '/') ;
        	++k;

        	if (k == theFileName.length() - 1) {					    // form of xxx/ (no filename given)
        		Toaster(theFileName + "is an invalid filename");	// tell user
        		DirPart = "";
        		theFileName = "invalid_file_name";
        	} else if (k > 0) {											// form "xxx/yyy"
        		DirPart = theFileName.substring(0, k);				// the dir part includes the /
        		theFileName = theFileName.substring(k + 1);           // the filename is the yyy part
        	} else {												// from "/yyy"
        		theFileName = theFileName.substring(k + 1);           // the filename is the yyy part
        	}
        }

        if (theFileName.length() < 5) {								// if the filename does not 
        	theFileName = theFileName + ".bas";						// have the .bas extension
        } else {														// then add it.
        	int x = theFileName.length() - 4;
        	String s = theFileName.substring(x);
        	if (!s.equals(".bas")) {
        		theFileName = theFileName + ".bas";
        	}
        }
		// now we can start the write process


		{														// Write to SD Card

        	// First insure the SD Card is available and writtable 

        	boolean mExternalStorageAvailable = false;
        	boolean mExternalStorageWriteable = false;
        	String state = Environment.getExternalStorageState();

        	if (Environment.MEDIA_MOUNTED.equals(state)) {			// Insure that SD is mounted
        	    // We can read and write the media
        	    mExternalStorageAvailable = mExternalStorageWriteable = true;
        	} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
        	    // We can only read the media
        	    mExternalStorageAvailable = true;
        	    mExternalStorageWriteable = false;
        	} else {
        	    // Something else is wrong. It may be one of many other states, but all we need
        	    //  to know is we can neither read nor write
        	    mExternalStorageAvailable = mExternalStorageWriteable = false;
        	}   	

        	boolean CanWrite = mExternalStorageAvailable && mExternalStorageWriteable;

        	if (!CanWrite) {                                           // If can't use SD card, pop up some
        		Toaster("External Storage not available or not writeable.");    // toast,
        	} else {
				//Write to SD Card
				File sdDir = new File(Environment.getExternalStorageDirectory().getPath());
				if (sdDir.exists() && sdDir.canWrite()) {
					if (Basic.SD_ProgramPath.equals("Sample_Programs") || Basic.SD_ProgramPath.equals("/Sample_Programs")) Basic.SD_ProgramPath = "";
					String PathA = "/" + Basic.AppPath + "/source/" + "/" + Basic.SD_ProgramPath;  // Base path
					File lbDir = new File(sdDir.getAbsoluteFile() + PathA);
					lbDir.mkdirs();													// make the dirs
					String PathB = PathA + "/" + DirPart;
					File xbDir = new File(sdDir.getAbsoluteFile() + PathB);			// now add the new path
					xbDir.mkdirs();													// make the dirs
					if (xbDir.exists() && xbDir.canWrite()) {
						File file = new File(xbDir.getAbsoluteFile()
											 + "/" + theFileName);									// add the filename
						try {
							file.createNewFile();
						} catch (Exception e) {
							Toaster("File not saved: " + e);
						}
						if (file.exists() && file.canWrite()) {
							FileWriter writer = null;

							try {
								writer = new FileWriter(file);							// write the program
								for (int i=0; i < Basic.lines.size(); i++) {
									writer.write(Basic.lines.get(i));
									writer.write("\n");
								}
							} catch (Exception e) {
								Toaster("File not saved: " + e);
							} finally {
								if (writer != null) {
									try {
										writer.flush();
										writer.close();
									} catch (Exception e) {
										Toaster("File not saved: " + e);
									}
								}
							}

						}
					}
				}
        	}

        }

    	Basic.ProgramFileName = theFileName;				// Set new Program file name
    	setTitle(Name + Basic.ProgramFileName);
		Basic.Saved = true;									// Indicate the program has been saved
        Basic.InitialProgramSize = mText.length();			// Reset initial program size


    	if (LoadAfterSave) {									// if diverted from Doing Load
        	LoadAfterSave = false;
        	ClearAfterSave = false;
        	RunAfterSave = false;
        	loadFile();										// then go do load
    	}

    	if (RunAfterSave) {									// if diverted from Doing Load
        	LoadAfterSave = false;
        	ClearAfterSave = false;
        	RunAfterSave = false;
        	Run();										// then go do load
    	}

    	if (ClearAfterSave) {								// if diverted from Doing Clear
			ClearAfterSave = false;
			LoadAfterSave = false;
			RunAfterSave = false;
			Basic.clearProgram();
			Basic.ProgramFileName = "";
			setTitle(Name + Basic.ProgramFileName);
			mText.setText(DisplayText);
			Editor.mText.setText(Editor.DisplayText);
			Editor.mText.setTextSize(1, Settings.getFont(this));
			if (Settings.getEditorColor(this).equals("BW")) {
				Editor.mText.setTextColor(0xff000000);
				Editor.mText.setBackgroundColor(0xffffffff);
			} else {
				Editor. mText.setTextColor(0xffffffff);
				Editor. mText.setBackgroundColor(0xff000000);
			}
			int SO = Settings.getSreenOrientation(this);
			setRequestedOrientation(SO);
        }
    }

    private void Toaster(CharSequence msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0 , 0);

		toast.show();


    }

    // ************************** Process Key Words Stuff ********************

    //********************  Code for formatting a line **************************************

    private String ProcessKeyWords(String actualLine) {					// Find and capitalize the key words.

    	if (actualLine.equals("")) return actualLine;					// Skip empty line

    	String lcLine = actualLine.toLowerCase();						// Count the blanks at the start of the lne
    	int blanks = 0;
    	for (blanks = 0; blanks < lcLine.length(); ++blanks) {
    		char c = lcLine.charAt(blanks);
    		if (c != ' ') 
    			if (c != '\t')
    				break;
    	}

    	if (blanks == lcLine.length()) return actualLine;				// If the line is all blanks, return

    	if (lcLine.startsWith("!", blanks)) return actualLine;			// If a comment line, done

    	actualLine = StartOfLineKW(lcLine, actualLine, blanks);

    	for (int i = 0; i < Run.MathFunctions.length; ++i) {										// Process math functions
    		actualLine = TestAndReplaceAll(Run.MathFunctions[i], lcLine, actualLine);
    	}

    	for (int i = 0; i < Run.StringFunctions.length; ++i) {									// Process String Functions
    		actualLine = TestAndReplaceAll(Run.StringFunctions[i], lcLine, actualLine);
    	}

    	return actualLine;			// Done, return results

    }

    private String StartOfLineKW(String lcLine, String actualLine, int blanks) {
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
		}
    	return actualLine;
    }

    private String ExpandedKeyWord(String[] words, String lcLine, String actualLine, int start) {  // The stuff after xxx.
    	for (int i = 0; i < words.length; ++i) {
			if (lcLine.startsWith(words[i], start)) {
     			String xkw = actualLine.substring(start, start + words[i].length());
    			actualLine = actualLine.replaceFirst(xkw, words[i].toUpperCase());
			}
    	}
    	return actualLine;
    }

    private String TestAndReplaceFirst(String kw, String lcLine, String actualLine) {			//Find and replace first occurrence
		int k = lcLine.indexOf(kw);
		if (k >= 0) {
			String xkw = actualLine.substring(k, k + kw.length());
			actualLine = actualLine.replaceFirst(xkw, kw.toUpperCase());

			int blanks = k + 4;
			if (kw.equals("then") || kw.equals("else")) {							// Special case THEN and ELSE
	        	for (blanks = blanks; blanks < lcLine.length(); ++blanks) {
	        		char c = lcLine.charAt(blanks);
	        		if (c != ' ') 
	        			if (c != '\t')
	        				break;
	        	}
				actualLine = StartOfLineKW(lcLine, actualLine, blanks);
			}
 		}
		return actualLine;

    }

    private String TestAndReplaceAll(String kw, String lcLine, String actualLine) {				// Find and replace all occurrences
    	int start = 0;
    	while (start < lcLine.length()) {
    		int k = lcLine.indexOf(kw, start);
    		if (k >= 0) {
    			String xkw = actualLine.substring(k, k + kw.length());
    			actualLine = actualLine.replace(xkw, kw.toUpperCase());
    			start = k + kw.length();
    		} else start = lcLine.length() + 1 ;
    	}
		return actualLine;

    }


}
    
 
