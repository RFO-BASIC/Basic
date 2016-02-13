/****************************************************************************************************

 BASIC! is an implementation of the Basic programming language for
 Android devices.


 Copyright (C) 2010 - 2016 Paul Laughton

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

*************************************************************************************************/

package com.rfo.basic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.Toast;


public class Editor extends Activity {
	private static final String LOGTAG = "Editor";

	// Things to save and restore if the system kills us.
	private static final String STATE_PROGRAM_PATH = "programPath";
	private static final String STATE_PROGRAM_FILE_NAME = "programFileName";
	private static final String STATE_INITIAL_SIZE = "initialSize";
	private static final String STATE_SAVED = "isSaved";
	private static final String STATE_ERROR_DISPLACEMENT = "errorDisplacement";
	private static final String STATE_MTEXT_DATA = "theText";
	public static final String EXTRA_RESTART = "com.rfo.basic.doRestart";
	public static final String EXTRA_LOADPATH = "com.rfo.basic.initLoadPath";

	// Since Android 4.2, "external storage" may be emulated and the path may included the user id.
	// There is another path that uses "legacy" instead of the user id.
	// E.g., getExternalStorageDirectory() may return /storage/emulated/0",
	// but then the "/sdcard link would resolve to "/storage/emulated/legacy".
	// The default basePath and filePath come from getExternalStorageDirectory().
	// For comparisons with other canonical paths we need the "legacy" version.
	// mAltHomePath and mAltBasePath, if non-null, use the "legacy" paths.

	private static String mBasePathAndSep;				// canonical base path (WITH trailing separator)
	private static String mAltBasePathAndSep;			// if non-null, this is mBasePath with "legacy"
	private static String mHomePath;					// canonical base path + app path (NO trailing separator)
	private static String mAltHomePath;					// if non-null, this is mHomePath with "legacy"
	public static String ProgramPath;					// canonical path to ProgramFileName used by LOAD/SAVE
	public static String ProgramFileName;				// set when program loaded or saved
	public static final String SEPARATOR = "" + File.separatorChar; // '/' in a String
	public static final String DIR_MARK = "(d)";		// marker that indicates a file name refers to a directory
	public static final String GO_UP = "..";			// "file name" that means move up the directory tree (toward root)

	private static final int LOAD_FILE_INTENT = 1;

	public static LinedEditText mText;					// The Editors display text buffers
	public static String DisplayText = "REM Start of BASIC! Program\n";
	public static int SyntaxErrorDisplacement = -1;

	public static int selectionStart;
	public static int selectionEnd;
	public static final String Name = "BASIC! Program Editor - ";
	public static int InitialProgramSize;				// Used to determine if program has changed
	public static boolean Saved = true;

	private Menu mMenu = null;
	private enum Action { NONE, CLEAR, LOAD, RUN, LOAD_RUN, EXIT }

	private Bundle mSavedInstanceState = null;			// carry saved state across callbacks

	// ****************** Class for drawing Lined Edit Text *******************

	public static class LinedEditText extends EditText {	// Part of the edit screen setup
		private Rect mRect;
		private Paint mPaint;
		private boolean mLinesSetting;						// Lines preference setting for onDraw
		private boolean mLineWrapSetting;					// Line-wrap preference setting

		private Scroller mScroller;							// The scroller object
		private VelocityTracker mVelocityTracker;			// The velocity tracker
		private int mScrollY = 0;							// The current scroll location
		private float mLastMotionY;							// Start of last movement
		private int mMinScroll;								// Minimum scroll distance
		private int mFlingV;								// Minimum velocity for fling
		public static int sHeight;							// Screen height minus the crap at top
		public static boolean didMove;						// Determines if super called on UP

		public LinedEditText(Context context, AttributeSet attrs) {
			super(context, attrs);

			mRect = new Rect();
			mPaint = new Paint();
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(0x800000FF);
			InitScroller(context);
		}

		@Override
		protected void onTextChanged(CharSequence  text, int start, int before, int after) {

			// Here we are monitoring for text changes so that we can set Saved properly

			int i = text.length();							// When the text is changed
			if (i > 0 && i != InitialProgramSize) {			// Make sure it is a real change
				Saved = false;								// then indicate not saved
			}

			super.onTextChanged(text, start, before, after);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			if (mLinesSetting) {
				Rect r = mRect;
				Paint paint = mPaint;
				int count = getLineCount();					// Draw the lines under each line of text
				for (int i = 0; i < count; i++) {
					int baseline = getLineBounds(i, r);
					canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
				}
			}
			sHeight = getHeight();							// This is where we get the screen height
			super.onDraw(canvas);
		}

		// ********************** Methods for scrolling ***********************

		public void InitScroller(Context context) {
			mScroller = new Scroller(context);					// Get a scroller object
			mScrollY = 0;										// Set beginning of program as top of screen.
//			mMinScroll = getLineHeight ()/2;					// Set minimum scroll distance
			mMinScroll = 1;										// Set minimum scroll distance

			mFlingV = 750;										// Minimum fling velocity
//			mScroller.setFriction((float) 10);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);

			if (mVelocityTracker == null) {						// If we do not have velocity tracker
				mVelocityTracker = VelocityTracker.obtain();	// then get one
			}
			mVelocityTracker.addMovement(event);				// add this movement to it

			final int action = event.getAction();				// Get action type
			final float y = event.getY();						// Get the displacement for the action

			switch (action) {

				case MotionEvent.ACTION_DOWN:					// User has touched screen
					if (!mScroller.isFinished()) {				// If scrolling, then stop now
						mScroller.abortAnimation();
					}
					mLastMotionY = y;							// Save start (or end) of motion
					mScrollY = this.getScrollY();				// Save where we ended up
					mText.setCursorVisible(true);
					didMove = false;

					break;

				case MotionEvent.ACTION_MOVE:					// The user finger is on the move
					didMove = true;
					final int deltaY = (int) (mLastMotionY - y);	// Calculate distance moved since last report
					mLastMotionY = y;								// Save the start of this motion

					if (deltaY < 0) {							// If user is moving finger up screen
						if (mScrollY > 0) {						// and we are not at top of text
							int m = mScrollY - mMinScroll;		// Do not go beyond top of text
							if (m < 0) {
								m = mScrollY;
							} else m = mMinScroll;

							scrollBy(0, -m);					// Scroll the text up
						}
					} else
					if (deltaY > 0) {							// The user finger is moving up
						int max = getLineCount() * getLineHeight() - sHeight;	// Set max up value
						if (mScrollY < max - mMinScroll) {
							scrollBy(0, mMinScroll);			// Scroll up
						}
					}
//					postInvalidate();
					break;

				case MotionEvent.ACTION_UP:						// User finger lifted up
					final VelocityTracker velocityTracker = mVelocityTracker;		// Find out how fast the finger was moving
					velocityTracker.computeCurrentVelocity(mFlingV);
					int velocityY = (int) velocityTracker.getYVelocity();

					if (Math.abs(velocityY) > mFlingV) {							// if the velocity exceeds threshold
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

			mScrollY = this.getScrollY();						// Save where we ended up

			return true ;										// Tell caller we handled the move event
		}

		@Override
		public void computeScroll() {					// Called while flinging to execute a fling step
			if (mScroller.computeScrollOffset()) {
				mScrollY = mScroller.getCurrY();		// Get where we should scroll to
				scrollTo(0, mScrollY);					// and do it
				postInvalidate();						// the redraw the sreem
			}
		}

		private void getPreferences(Context context) {
			Basic.TextStyle style = Basic.defaultTextStyle;
			mText.setTextColor(style.mTextColor);
			mText.setBackgroundColor(style.mBackgroundColor);
			mText.setHighlightColor(style.mHighlightColor);
			mPaint.setColor(style.mLineColor);
			setTextSize(1, Settings.getFont(context));
			mLinesSetting = Settings.getLinedEditor(context);
			mLineWrapSetting = Settings.getEditorLineWrap(context);
		}
	}

	// ************************ End of LinedEdit Class ************************

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(LOGTAG, "onCreate");
		super.onCreate(savedInstanceState);						// Setup and the display the text to be edited

		mSavedInstanceState = savedInstanceState;				// preserve for onResume
		Intent intent = getIntent();
		if (savedInstanceState == null) {						// if no state from system
			mSavedInstanceState = intent.getBundleExtra(EXTRA_RESTART); // look for state from Basic Activity
		}
		Run.Exit = false; 										// Clear this in case it was set last time BASIC! exited.

		/*
		 * Open up the view.
		 * 
		 * The LinedEdit Class code will get control at this point and draw the blank screen.
		 * 
		 * When that is done, the rest of the code here will be execute.
		 */
		setContentView(R.layout.editor);

		mHomePath = Basic.getFilePath();
		mBasePathAndSep = new File(mHomePath).getParent() + SEPARATOR;
		mAltHomePath = mAltBasePathAndSep = null;

		File base = new File(mBasePathAndSep);
		File sdBase = new File(SEPARATOR + "sdcard");
		try { sdBase = sdBase.getCanonicalFile(); }
		catch (IOException ex) { }
		if (sdBase.getName().equals("legacy")) {				// if /sdcard points to legacy
			base = new File(base.getParent(), "legacy");		// replace base leaf with "legacy"
			if (sdBase.getPath().equals(base.getPath()) &&
					base.exists() && sdBase.exists()) {
				// We're as sure as we can be that base and sdBase are the same.
				mAltBasePathAndSep = sdBase.getPath() + SEPARATOR;
				mAltHomePath = new File(sdBase, Basic.AppPath).getPath();
			}
		}

		String initLoadPath = intent.getStringExtra(EXTRA_LOADPATH);
		if (initLoadPath == null) { initLoadPath = ""; }
		// These two fields may be overwritten from mSavedInstanceState in onResume().
		ProgramPath = Basic.getSourcePath(initLoadPath);
		ProgramFileName = "";

		mText = (LinedEditText)findViewById(R.id.basic_text);	// mText is the TextView Object
		mText.setTypeface(Typeface.MONOSPACE);

		InputFilter[] filters = mText.getFilters();				// some devices (Samsung) have a filter that limits EditText size
		if (filters.length != 0) {
			mText.setFilters(new InputFilter[0]);				// if there are any filters, remove them
		}

		mText.setMinLines(4096);
		mText.setCursorVisible(true);
		mText.setText(DisplayText);								// Put the text lines into Object
		InitialProgramSize = DisplayText.length();
		Saved = true;
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
		if (!Settings.getAutoIndent(this)) return false;	// Don't do the formatting if the user does not want it

        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getRepeatCount() == 0) {
            int selection = mText.getSelectionEnd();								// Split the text into two parts
            String theText = mText.getText().toString();							// The text before the ENTER
            String fText = "";														// and the Text after the ENTER
            if (selection > 1)
            	fText = theText.substring(0, selection - 1);
            String eText = "";
            eText = theText.substring(selection);

            int lineStart = 0;														// Backtrack over the before text
            if (selection - 2 > 0) {												// to find the start of the last
            	for (lineStart = selection - 2; lineStart > 0 ; --lineStart) {		// before ENTER line
            		char c = theText.charAt(lineStart);
            		if (c == '\n') break;
            	}
            	if (lineStart > 0) ++lineStart;
            } else lineStart = 0;


            String blanks = "";														// Now, count the leading blanks
            for (int i = lineStart; i < selection - 1; ++i) {						// in the last before ENTER line
            	char c = fText.charAt(i);
            	if (c != ' ')
            		if (c != '\t') break;
            	blanks = blanks + " ";
            }

            if (fText.endsWith("#")) {												// If formatting of line was wanted
            	String theLine = fText.substring(lineStart, fText.length() - 1);	// go format the line
            	String newLine = Format.ProcessKeyWords(theLine);
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

		if (Basic.getContextManager() == null) {				// if we have lost context then restart Basic Activity
			Log.e(LOGTAG, "onCreate: lost Context. Restarting BASIC!.");
			Intent intent = new Intent(getApplicationContext(), Basic.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (mSavedInstanceState != null) {					// send saved state so Basic can send it back
				mSavedInstanceState.putString(STATE_MTEXT_DATA, mText.getText().toString());
				intent.putExtra(EXTRA_RESTART, mSavedInstanceState);
			}
			startActivity(intent);
			finish();
			return;
		}

		if (mSavedInstanceState != null) {
			Log.d(LOGTAG, "onResume: found savedInstanceState");
			ProgramPath = mSavedInstanceState.getString(STATE_PROGRAM_PATH);
			ProgramFileName = mSavedInstanceState.getString(STATE_PROGRAM_FILE_NAME);
			String text = mSavedInstanceState.getString(STATE_MTEXT_DATA);
			if (text != null) { mText.setText(text); }
			InitialProgramSize = mSavedInstanceState.getInt(STATE_INITIAL_SIZE);
			SyntaxErrorDisplacement = mSavedInstanceState.getInt(STATE_ERROR_DISPLACEMENT);
			Saved = mSavedInstanceState.getBoolean(STATE_SAVED);
			mSavedInstanceState = null;
		}

		if (Settings.changeBaseDrive) {
			doBaseDriveChange();
		}

		if (Run.Exit) {		// Somebody told Run to exit, so exit Editor, too.
			finish();		// Do not clear Exit here; it might still be seen by another Activity
			return;			// Instead, clear it in onCreate() the next time the Editor starts
		}

		if (Basic.DoAutoRun) {
			Log.e(LOGTAG, "onResume: AutoRun is set. Shutting down.");
			finish();
		} else {
			setTitle(ProgramFileName);
			menuItemsToActionBar(mMenu);

			mText.getPreferences(this);
			int SO = Settings.getSreenOrientation(this);
			setRequestedOrientation(SO);
			mText.setHorizontallyScrolling(!mText.mLineWrapSetting);		// set scrolling per Preferences

			if (SyntaxErrorDisplacement >= 0 &&
				SyntaxErrorDisplacement < AddProgramLine.lineCharCounts.size()) {	// If run ended in error, select error line

				int end = AddProgramLine.lineCharCounts.get(SyntaxErrorDisplacement);  // Get selection end
				if (end >= DisplayText.length()) end = DisplayText.length();
				int start = end - 1;										// back up over the new line

				for (start = end - 1; start > 0 ; --start) {				// Scan for previous nl or start
					char c = DisplayText.charAt(start);
					if (c == '\n') {
						start = start + 1;
						break;
					}
				}

				if (start >= 0 && end >= 0 && start <= end &&				// make sure values are not crash bait
					end <= mText.length()) {								// Note: if RUN command, DisplayText does not match mText. TODO: FIX THIS?
					mText.setSelection(start, end);							// Set the selection
				}
				mText.setCursorVisible(true);
				SyntaxErrorDisplacement = -1;								// Reset the value
			}
		}

	}

	@Override
	public void setTitle(CharSequence programName) {
		CharSequence title = Name + (((programName != null) && !programName.equals("")) ? programName : "unnamed program");
		super.setTitle(title);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		savedInstanceState.putString(STATE_PROGRAM_PATH, ProgramPath);
		savedInstanceState.putString(STATE_PROGRAM_FILE_NAME, ProgramFileName);
		savedInstanceState.putInt(STATE_INITIAL_SIZE, InitialProgramSize);
		savedInstanceState.putBoolean(STATE_SAVED, Saved);
		savedInstanceState.putInt(STATE_ERROR_DISPLACEMENT, SyntaxErrorDisplacement);

		super.onSaveInstanceState(savedInstanceState);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Returning from LoadFile Activity for LOAD_RUN menu selection.
		if (requestCode == LOAD_FILE_INTENT) {
			if (resultCode == RESULT_OK) { Run(); }		// user loaded a program; run it
		}
	}

/*
	@Override
	protected void onPause() {
		super.onPause();
		Log.v(LOGTAG, "onPause");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.v(LOGTAG, "onStart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.v(LOGTAG, "onRestart");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.v(LOGTAG, "onDestroy");
	}
*/

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void menuItemsToActionBar(Menu menu) {
		if (menu == null) return;
		if (Build.VERSION.SDK_INT < 11) return;				// no action needed

		if (Settings.menuItemsToActionBar(this, menu)) {
			invalidateOptionsMenu();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {			// When the user presses Menu
		super.onCreateOptionsMenu(menu);					// set up and display the Menu
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.editor, menu);
		mMenu = menu;
		Settings.menuItemsToActionBar(this, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	// A menu item has been selected
		switch (item.getItemId()) {

			case R.id.run:									// RUN
				if (Saved) {								// If current program has been saved
					Run();									// then run the program
				} else {
					doSaveDialog(Action.RUN);				// Ask if the user wants to save before running
				}
				return true;

			case R.id.load:									// LOAD
				if (Saved) {								// If current program has been saved
					loadFile(false);						// then load the program, but don't run it
				} else {
					doSaveDialog(Action.LOAD);				// Ask if the user wants to save before loading
				}
				return true;

			case R.id.save:									// SAVE
				askNameSaveFile(Action.NONE);				// Just do it; no action needed after Save
				return true;

			case R.id.clear:								// CLEAR
				if (Saved) {								// If program has been saved
					clearProgram();							// then clear the Editor
				} else {
					doSaveDialog(Action.CLEAR);				// Ask if the user wants to save before clearing
				}
				return(true);

			case R.id.search:								// SEARCH
				if (mText == null) {
					throw new RuntimeException("Editor: attempt to Search with null mText");
				}
				DisplayText = mText.getText().toString();
				selectionStart = mText.getSelectionStart();
				selectionEnd = mText.getSelectionEnd();
				startActivity(new Intent(this, Search.class));	// Start the search activity
				return true;

			case R.id.load_run:								// LOAD and RUN
				if (Saved) {								// If program has been saved
					loadFile(true);							// then load the program, and run it
				} else {
					doSaveDialog(Action.LOAD_RUN);			// Ask if the user wants to save before clearing
				}
				return true;

			case R.id.save_run:								// SAVE and RUN
				if (Saved) {
					Run();									// no change, just run the program
				} else if (ProgramFileName.equals("")) {	// if no file name...
					askNameSaveFile(Action.RUN);			// ... get a name, save the program and run it
				} else {									// else have a file name
					writeTheFile(ProgramFileName);			// save the program, overwriting existing file
					Run();									// run the program
				}
				return true;

			case R.id.format:								// FORMAT
				if (mText == null) {
					throw new RuntimeException("Editor: attempt to Format with null mText");
				}
				doFormatDialog();
				return true;

			case R.id.delete:								// DELETE
				DisplayText = mText.getText().toString();	// get the text being displayed

				// First make sure that the SD Card is present and can be written to

				// if the SD Card is not available or writable pop some toast and do
				// not call Delete

				if (!Basic.checkSDCARD('w')) {
					Basic.toaster(this, "External storage not available or not writable.");
					return true;
				}

				startActivity(new Intent(this, Delete.class));	// Go to Delete Activity
				return true;

			case R.id.settings:								// SETTINGS
				startActivity(new Intent(this, Settings.class));// Start the Setting activity
				return true;

			case R.id.help:									// COMMANDS
				startActivity(new Intent(this, Help.class));	// Start the help activity
				return true;

			case R.id.about:								// ABOUT
				String version = getString(R.string.version);	// Get the version string
				String url = "https://bintray.com/rfo-basic/android/RFO-BASIC/v"	// add it to the URL
							+ version + "/view/read";
				Intent i = new Intent(Intent.ACTION_VIEW);		// Go to the About web page
				i.setData(Uri.parse(url));
				startActivity(i);
				return true;

			case R.id.exit:									// EXIT
				if (Saved) {								// If program has been saved
					finish();								// exit immediately
				} else {
					doSaveDialog(Action.EXIT);				// Ask if the user wants to save before exiting
				}
				return true;

			default:
				return true;
		}
	}

	private void doSaveDialog(final Action afterSave) {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Current Program Not Saved!")
			.setCancelable(true)										// Allow user to BACK key out of the dialog

			.setPositiveButton("Save", new DialogInterface.OnClickListener() {		// User says to save first
				public void onClick(DialogInterface dialog, int id) {
					askNameSaveFile(afterSave);							// Tell the saver what to do after the save is done
				}
			})

			.setNegativeButton("Continue", new DialogInterface.OnClickListener() {	// User says Do not save
				public void onClick(DialogInterface dialog, int id) {
					doAfterSave(afterSave);								// Finish what the Save dialog interrupted
				}
			})

			.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface arg0) {			// User has canceled save
					return;												// done
				}
			});

		alert.show();
 	}


	private void doFormatDialog() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setMessage("Format your program?")
			.setCancelable(true)

			.setPositiveButton("Format", new DialogInterface.OnClickListener() {	// User says to do the format
				public void onClick(DialogInterface dialog, int id) {
					DisplayText = mText.getText().toString();
					startActivity(new Intent(Editor.this, Format.class));			// Start the format activity
					Saved = false;
				}
			})

			.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {	// User says to cancel
				public void onClick(DialogInterface dialog, int id) {
					return;
				}
			})

			.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface arg0) {			// User has canceled format
					return;												// done
				}
			});

		alert.show();
	}

	private void Run() {

		/* Run a program
		 * Create a new Basic.lines object and then copy
		 * the display text buffer to it.
		 * 
		 * The display buffer is one big string. We need
		 * to step through it looking for \n (newline) characters.
		 * Each \n marks a new line for Basic.lines.
		 */

		DisplayText = mText.getText().toString();
		Basic.loadProgramFromString(DisplayText, null);			// build program in Basic.lines

		if (Basic.lines.size() == 0) {							// If the program is empty
			Basic.lines.add(new Run.ProgramLine("@@@"));		// add Nothing to run command
		}

		SyntaxErrorDisplacement = -1;
		startActivity(new Intent(this, Run.class));				// now go run the program
	}

	private void loadFile(boolean doRun) {
		if (!Basic.checkSDCARD('r')) {							// Make sure SD card is present. If not, popup
			CharSequence text = "External storage not available.";	// some toast and do not go to LoadFile
			Toast.makeText(this, text, Toast.LENGTH_LONG).show();
		} else {												// If the SD Card can be read
			Intent intent = new  Intent(this, LoadFile.class);
			// Go to the LoadFile Activity. If doRun, catch returned intent and run the loaded program.
			startActivityForResult(intent, doRun ? LOAD_FILE_INTENT : -1);
		}
	}

	private void clearProgram() {
		Basic.clearProgram();									// then do the clear
		ProgramFileName = "";
		setTitle(ProgramFileName);
		Saved = true;
		InitialProgramSize = DisplayText.length();
		mText.setText(DisplayText);
	}

	private void askNameSaveFile(final Action afterSave) {

		final AlertDialog.Builder alert = new AlertDialog.Builder(this);	// get the filename from user
		final EditText input = new EditText(this);
		input.setText(ProgramFileName);										// if the program has a name put it in the dialog box
		alert.setView(input);
		alert.setCancelable(true);											// allow the dialog to be canceled

		String path = getDisplayPath(ProgramPath);							// get the save path to display in the dialog box
		if (path.endsWith(Basic.SAMPLES_DIR)) { path = goUp(path); }		// don't offer to save in sample programs directory
		alert.setTitle("Save to " + path);
		alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface arg0) {					// user has canceled save
				return;														// done
			}
		});

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {// have a filename
			public void onClick(DialogInterface dialog, int whichButton) {
				String theFilename = input.getText().toString().trim();
				writeTheFile(theFilename);									// write the program to a file
				doAfterSave(afterSave);										// and finish what was interrupted by Save dialog
			}});

		alert.show();
	}

	private ArrayList<String> captureProgram() {
		ArrayList<String> lines = new ArrayList<String>();
		DisplayText = mText.getText().toString();				// get the text being displayed
		String line = "";
		boolean LineAdded = false;
		for (int k = 0; k < DisplayText.length(); ++k) {		// move the display text to a String array
			if (DisplayText.charAt(k) == '\n') {
				lines.add(line);
				line = "";
				LineAdded = true;
			} else {
				line += DisplayText.charAt(k);
				LineAdded = false;
			}
		}
		if (!LineAdded) {										// Special case for line
			lines.add(line);									// without \n
		}
		return lines;
	}

	private void writeTheFile(String fileName) {
		// First ensure the SD Card is available and writable
		if (!Basic.checkSDCARD('w')) {								// If can't use SD card, pop up some
			Basic.toaster(this, "External Storage not available or not writeable.");	// toast
			return;
		}

		String seps = "^" + File.separatorChar + "+";				// regex for leading slashes
		fileName = fileName.replaceFirst(seps, "");					// remove leading slashes
		String path = "";
		int k = fileName.lastIndexOf(File.separatorChar);			// does name contain a path separator?
		if (k > 0) {												// form is "path/file"
			path = fileName.substring(0, k);						// path is part before last separator
			fileName = fileName.substring(k + 1);					// the filename is the part after the last separator
		}
		if (fileName.length() == 0) {								// if no file name
			fileName = "default.bas";								// use the default
		} else if (!fileName.endsWith(".bas")) {					// if the filename does not
			fileName += ".bas";										// have the .bas extension
		}															// then add it

		File dir;
		if (path.length() == 0) {									// not changing directory
			path = ProgramPath;										// get absolute path
		} else {													// directory change
			dir = new File(ProgramPath, path);						// add new path element(s) to absolute path
			try { path = dir.getCanonicalPath(); }					// resolve new path
			catch (IOException e) { path = dir.getAbsolutePath(); }	// if error just fix slashes
		}
		dir = new File(path);

		if (path.endsWith(Basic.SAMPLES_DIR)) {						// don't save in sample programs directory
			path = dir.getParent();
			dir = new File(path);
		}
		if (!dir.exists()) { dir.mkdirs(); }						// make the dirs if needed

		File file = new File(path, fileName);

		// Now dir and file are File objects
		// for Strings path and path/fileName.

		boolean success = false;
		IOException ex = null;
		if (dir.exists() && dir.canWrite()) {						// good to go
			ArrayList<String> lines = captureProgram();				// copy the program to a String array

			FileWriter writer = null;
			try {
				file.createNewFile();
				writer = new FileWriter(file);						// write the program to the file
				for (String line : lines) {
					writer.write(line + '\n');
				}
				success = true;
			} catch (IOException e) {
				ex = e;
			} finally {
				if (writer != null) {
					try { writer.flush(); } catch (IOException e) { ex = e; }
					try { writer.close(); } catch (IOException e) { ex = e; }
				}
			}
		}

		if (success) {
			ProgramPath = path;									// record new path
			ProgramFileName = fileName;							// and file name

			String display = getDisplayPath(file.getPath());
			Basic.toaster(this, "Saved " + display);			// notify the user

			setTitle(ProgramFileName);
			InitialProgramSize = mText.length();				// reset initial program size
			Saved = true;										// indicate the program has been saved
		} else {
			String msg = "File not saved: " + ((ex == null) ? path : ex.getMessage());
			Basic.toaster(this, msg);
		}
	} // writeTheFile

	private void doAfterSave(Action afterSave) {
		switch (afterSave) {
		case NONE:											// No action needed
			break;
		case LOAD:											// if diverted from Doing Load
			loadFile(false);								// then go do load, but don't run
			break;
		case RUN:											// if diverted from Doing Run
			Run();											// then go do run
			break;
		case LOAD_RUN:										// if diverted from Doing LOAD_RUN
			loadFile(true);									// then go do load, then run
			break;
		case CLEAR:											// if diverted from Doing Clear
			clearProgram();									// then go do clear
			break;
		case EXIT:											// if diverted from Doing Exit
			finish();										// then exit
			break;
		}
	}

	private void doBaseDriveChange() {
		Settings.changeBaseDrive = false;

		String newBaseDrive = Settings.getBaseDrive(this);

		if (newBaseDrive.equals("none")) return;
		if (newBaseDrive.equals(Basic.getBasePath())) return;

		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);		// using a dialog box.

		alt_bld.setMessage("When BASIC! restarts the new Base Drive will be used.\n\n" +
							"Restart BASIC! Now\n" +
							"or Wait and restart BASIC! yourself.")
		.setCancelable(false)												// Do not allow user BACK key out of dialog

		// The click listeners ****************************

		.setPositiveButton("Restart Now", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				Intent restart = new Intent(getApplicationContext(), Basic.class);
				startActivity(restart);
				finish();
			}
		})

		.setNegativeButton("Wait", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				waitMessage();
			}
		});

		// End of click listeners ****************************************

		AlertDialog alert = alt_bld.create();								// Display the dialog
		alert.setTitle("Base Drive Changed");
		alert.show();
	}

	private void waitMessage() {
		AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);		// using a dialog box.

		alt_bld.setMessage("When ready to resart with new base drive:\n\n " +
							"Tap Menu -> Exit and then\n" +
							"Restart BASIC!")
		.setCancelable(false)												// Do not allow user BACK key out of dialog

		// The click listeners ****************************

		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				return;
			}
		});

		// End of click listeners ****************************************

		AlertDialog alert = alt_bld.create();								// Display the dialog
		alert.setTitle("Restart Later");
		alert.show();
	}

	// *************************** Static utilities for LOAD/SAVE/DELETE **************************

	public static String getDisplayPath(String path) {
		File pathFile = new File(path);
		try { path = pathFile.getCanonicalPath(); }
		catch (IOException ex) { path = pathFile.getAbsolutePath(); }
		// mHomePath is absolute path to "rfo-basic",
		// mAltHomePath is absolute path through "legacy" (if applicable)
		// mBasePathAndSep is its parent plus a trailing "/".
		if (path.startsWith(mHomePath)) {					// if starts with home path
			path = path.substring(mBasePathAndSep.length());	// clip off base path and "/"
		} else if ((mAltHomePath != null) && path.startsWith(mAltHomePath)) {
			path = path.substring(mAltBasePathAndSep.length());	// ditto alt path
		}														// else leave absolute path
		return path;
	}

	public static boolean isMarkedDir(String name) {
		return name.endsWith(DIR_MARK);
	}

	public static String addDirMark(String name) {
		return name + DIR_MARK;
	}

	public static String stripDirMark(String name) {
		int k = name.length() - DIR_MARK.length();
		return name.substring(0, k);
	}

	public static String goUp(String path) {					// change path to go up the directory tree
		path = new File(path).getParent();
		if (path == null) { path = SEPARATOR; }					// no parent: assume absolute path and set to root
		return path;
	}

	public static String quote(String str) {
		return '\"' + str + '\"';
	}
}
