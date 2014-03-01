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

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.graphics.Bitmap;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;


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

   @Override
   protected void onCreate(Bundle savedInstanceState) {   // Help text located in R.Strings
      super.onCreate(savedInstanceState);
      
      // The Intent get action call should be create shortcut.
      // if not, done.

      final Intent intent = getIntent();
      final String action = intent.getAction();

      // If the intent is a request to create a shortcut, we'll do that and exit
      if (!Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
          finish();
          return;
      }
      
      // Set up the form. The form layout is in res.layout.get_sc_parms.xml
      // and res.layout-land.get_sc_parms.xml
      // The OS will choose which layout to use depending upon the
      // device orientation
      
      this.setContentView(R.layout.get_sc_parms);
      
      this.theText1 = (EditText) findViewById(R.id.the_text1);
      this.theText2 = (EditText) findViewById(R.id.the_text2);
      this.theText3 = (EditText) findViewById(R.id.the_text3);

      this.okButton = (Button) findViewById(R.id.ok_button);
      this.cancelButton = (Button) findViewById(R.id.cancel_button);

      // Act on the dialog button presses
      
      this.okButton.setOnClickListener(new OnClickListener() {
          
          public void onClick(View v) {
              handleOK();                    
              finish();
              return;
          	}
      	});

          this.cancelButton.setOnClickListener(new OnClickListener() {
           
           public void onClick(View v) {
               handleCancel();
               finish();
               return;
              }
           });

  
  }   

  
  @Override
  public boolean onKeyUp(int keyCode, KeyEvent event)  {
	  
	  // Do not allow exiting the form by BACK key press
	  // The dialog has a Cancel key for that purpose
	  
      if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
          return true;
      }

      return super.onKeyUp(keyCode, event);
  }
  
  private void handleOK() {
	  
	  // The user has pressed OK
	  
	  if (Basic.getFilePath().equals("")) {						// If BASIC! had not been run in a while,
		  														// then filePath will be an empty string
		  														// In this case, establish paths as Basic.java
		  														// does when it is run.
	        String basePath = Settings.getBaseDrive(this);
	        Basic.setFilePaths(basePath);
	  }
	  
	  String FileName = this.theText1.getText().toString();
	  String BitmapFileName = this.theText2.getText().toString();
	  String AppName = this.theText3.getText().toString();

	  String FullFileName = Basic.getSourcePath(FileName);    // The Program File Name

	  String bmfn = Basic.getDataPath(BitmapFileName);        // The bitmap file name
	  Bitmap aBitmap = BitmapFactory.decodeFile(bmfn);        // get the actual bitmap

	  setupShortcut( AppName, FullFileName, aBitmap);         // Go finish the shortcut setup
	  return;
	  
	  
  }
  
  private void handleCancel() {
	  return;

  }

 

   private void setupShortcut(String AppName, String FileName, Bitmap aBitmap){
      
      Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);        // Tells launcher where what to launch
      shortcutIntent.setClassName(this, Basic.class.getName());		 // which is Basic.java
      
      shortcutIntent.putExtra(EXTRA_LS_FILENAME, FileName);			 // Use the full filename for the file to load

      Intent intent = new Intent();
      intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
      intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, AppName);          // Set the title
           
      intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, aBitmap);			 // Set the icon bitmap
      setResult(RESULT_OK, intent);                                  // Tell caller all is ok
   
   }
   

}