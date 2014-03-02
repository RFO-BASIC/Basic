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

    You may contact the author or current maintainers at http://rfobasic.freeforums.org

*************************************************************************************************/

package com.rfo.basic;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/* Called when creating a launcher shortcut.
 * 
 * Code in the Manifest file directs the launcher
 * here for the create task.
 * 
 * Displays the create shortcut form and uses
 * the results to create the shortcut.
 * 
 */

public class LauncherShortcuts extends Activity {
	public static final String EXTRA_LS_FILENAME = "com.rfo.basic.fn";
	private EditText theText1;
	private EditText theText2;
	private EditText theText3;
	private Button okButton;
	private Button cancelButton;

	private String mAppName = "";			// the launcher shortcut name
	private String mProgPath = "";			// the BASIC! program file name
	private Bitmap mIcon = null;			// the icon bitmap

	private boolean mHaveProgName = false;
	private boolean mHaveIconName = false;
	private boolean mHaveAppName = false;
	private boolean mProgExists = false;
	private boolean mIconExists = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// The Intent get action call should be create shortcut.
		// if not, done.

		final Intent intent = getIntent();
		final String action = intent.getAction();

		if (!Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
			finish();
			return;
		}

		// Set up the form. The form layout is in res.layout.get_sc_parms.xml
		// and res.layout-land.get_sc_parms.xml
		// The OS will choose which layout to use depending upon the
		// device orientation

		setContentView(R.layout.get_sc_parms);

		theText1 = (EditText) findViewById(R.id.the_text1);		// field for BASIC! program file name
		theText2 = (EditText) findViewById(R.id.the_text2);		// field for icon file name
		theText3 = (EditText) findViewById(R.id.the_text3);		// field for launcher shortcut name

		okButton = (Button) findViewById(R.id.ok_button);
		cancelButton = (Button) findViewById(R.id.cancel_button);

		// Act on the dialog button presses

		cancelButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				finish();
			}
		});

		okButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				handleOK();
			}
		});

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		// Do not allow exiting the form by BACK key press
		// The dialog has a Cancel key for that purpose

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void handleOK() {

		// The user has pressed OK

		String base = Basic.getFilePath();
		if ((base == null) || base.equals("")) {				// If BASIC! has not been run in a while,
																// then filePath will be an empty string.
																// In this case, establish paths as Basic.java
																// does when it is run.
			String basePath = Settings.getBaseDrive(this);
			Basic.setFilePaths(basePath);
		}

		String progFileName = theText1.getText().toString().trim();	// the BASIC! program file name
		String iconFileName = theText2.getText().toString().trim();	// the icon bitmap file name
		String appName = theText3.getText().toString().trim();		// the launcher shortcut name

		mHaveProgName = (progFileName != null && !progFileName.equals(""));
		mHaveIconName = (iconFileName != null && !iconFileName.equals(""));
		mHaveAppName = (appName != null && !appName.trim().equals(""));

		String progPath = mHaveProgName ? Basic.getSourcePath(progFileName) : "";
		String iconPath = mHaveIconName ? Basic.getDataPath(iconFileName) : "";

		mProgExists = mHaveProgName && new File(progPath).exists();
		mIconExists = mHaveIconName && new File(iconPath).exists();

		mAppName = appName;
		mProgPath = progPath;
		mIcon = mIconExists ? BitmapFactory.decodeFile(iconPath) : null;	// get the actual bitmap

		if (mHaveAppName && mProgExists && (mIcon != null)) {
			setupShortcut();									// go finish the shortcut setup
		} else {
			showWarning();
		}
		return;
	}

	private void setupShortcut(){

		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);		// tells launcher what to launch
		shortcutIntent.setClassName(this, Basic.class.getName());	// which is Basic.java

		shortcutIntent.putExtra(EXTRA_LS_FILENAME, mProgPath);		// use the full path for the program to load

		Intent intent = new Intent();
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, mAppName);		// set the title
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, mIcon);			// set the icon bitmap
		setResult(RESULT_OK, intent);								// tell caller all is ok

		finish();
	}

	private void showWarning() {
		final String prefix = "  ";
		final String defIconMsg = "\n    (shortcut will use default Android icon)\n";
		final TextView text = new EditText(this);
		final Resources res = getResources();

		String msg = "";
		if (!mHaveProgName) { msg += prefix + res.getText(R.string.no_prog_name) + '\n'; }
		if (!mHaveIconName) { msg += prefix + res.getText(R.string.no_icon_name) + defIconMsg; }
		if (!mHaveAppName) { msg += prefix + res.getText(R.string.no_shortcut_name) + '\n'; }
		if (mHaveProgName && !mProgExists) { msg += prefix + res.getText(R.string.no_prog_file) + '\n'; }
		if (mHaveIconName && !mIconExists) { msg += prefix + res.getText(R.string.no_icon_file) + defIconMsg; }
		if (mIconExists && (mIcon == null)) { msg += prefix + res.getText(R.string.no_icon) + defIconMsg; }
		msg += "\n" + res.getText(R.string.instr_1) + '\n' + res.getText(R.string.instr_2);
		text.setText(msg);

		new AlertDialog.Builder(this)
			.setCancelable(true)
			.setTitle("Warning")
			.setView(text)
			.setPositiveButton(R.string.ok_button_label, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int id) {
						setupShortcut();
					}
				})
			.setNegativeButton(R.string.cancel_button_label, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dlg, int id) {
					}
				})
			.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dlg) {
					}
				})
			.create().show();
	}

}