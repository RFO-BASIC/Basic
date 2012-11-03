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
    
	*************************************************************************************************/

package com.rfo.basic;



import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;

import com.rfo.basic.R;
import com.rfo.basic.LoadFile.ColoredTextAdapter;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

//Log.v(Delete.LOGTAG, " " + Delete.CLASSTAG + " String Var Value =  " + d);

/* The User Interface used to delete files.
 * The user is presented with a list of files and directories from which
 * she can select files or directories to delete.
 */

public class Delete extends ListActivity {
    private static final String LOGTAG = "Delete File";

    private static final String CLASSTAG = Delete.class.getSimpleName();
    private static ListView lv ;
    private static   ArrayList<String> FL1 = new ArrayList<String>();
    private static   ArrayList<String> DL1 = new ArrayList<String>();
    private static String FilePath = "";
    public static String SD_FilePath = "rfo-basic";
    public static String IM_FilePath = "";
  	private static String FL[] = null;

    public class ColoredTextAdapter extends ArrayAdapter<String> {
        Activity context;
        ArrayList<String> list;
        int textColor;
        int backgroundColor;

        public ColoredTextAdapter(Activity aContext, ArrayList<String> alist) {
                super(aContext, Settings.getLOadapter(aContext), alist);
                context = aContext;
                this.list = alist;
                if (Settings.getEditorColor(context).equals("BW")){
              	  textColor = 0xff000000;
              	  backgroundColor = 0xffffffff;
                } else
                  if (Settings.getEditorColor(context).equals("WB")){
                	  textColor = (0xffffffff);
                	  backgroundColor = 0xff000000;
                } else
                   if (Settings.getEditorColor(context).equals("WBL")){
                	   textColor =  (0xffffffff);
                	   backgroundColor = 0xff006478;
                }  

        }

        public View getView(int position, View convertView, ViewGroup parent) {

                View row = convertView;
                if (row == null) {
                        LayoutInflater inflater = (LayoutInflater) context
                                        .getLayoutInflater();
                        row = inflater.inflate(Settings.getLOadapter(context), null);

                }
                TextView text = (TextView) row.findViewById(R.id.the_text);
                text.setTextColor(textColor);
                text.setText(list.get(position));
                text.setBackgroundColor(backgroundColor);

                return row;
        }
    }



    
    
@Override
public void onCreate(Bundle savedInstanceState) {
	
  super.onCreate(savedInstanceState);
  setRequestedOrientation(Settings.getSreenOrientation(this));
  Delete1();                                 // This is so that we can re-enter delete from within delete 
  											 // without have to re-create the delete intent.
}

private void Delete1(){
  
  	FL1 = new ArrayList<String>();
  	DL1 = new ArrayList<String>();
	
	FilePath = SD_FilePath;			  						   // Set Delete's file path
 
	File lbDir = GetFileList();								   // Get the list of files in this directory
	
	// Note: The checking for SD card present was done in the Editor just before calling delete

  int m = FL.length;
  DL1.add("..");												// put  the ".." to the top of the list
  
  // Go through the file list and mark directory entries with (d)
  
  for (int i=0; i<m; ++i){
	  String s = FL[i];
	  File test = new File(lbDir.getAbsoluteFile() + "/" + s);  // If files is a directory, add "(d)"
	  if (test.isDirectory()){
		  s = s + "(d)";
		  DL1.add(s);											// and add to display list
	  } else {
		  FL1.add(s);											// else add name without the (d)
	  	}
  	}
  
  Collections.sort(DL1);										// Sort the directory list
  Collections.sort(FL1);                                        // Sort the file list
  for (int i=0; i<FL1.size(); ++i){									// copy the file list to end of dir list
	  DL1.add(FL1.get(i));
  	}
  FL1 = DL1;
  
  setListAdapter(new ColoredTextAdapter(this,  FL1));
  lv = getListView();
  lv.setTextFilterEnabled(false);
  
  if (Settings.getEditorColor(this).equals("BW")){
	    lv.setBackgroundColor(0xffffffff);
} else
  if (Settings.getEditorColor(this).equals("WB")){
	  lv.setBackgroundColor(0xff000000);
} else
    if (Settings.getEditorColor(this).equals("WBL")){
    	  lv.setBackgroundColor(0xff006478);
}  

  // Now wait for the user to select a file or directory. If the entry is a directory, check
  // to see if that directory is empty. If empty, it can be deleted. If not empty, then
  // then display the list of files in that directory
  
  lv.setOnItemClickListener(new OnItemClickListener() {			// when user taps a filename line
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	if (!SelectionIsFile(position)) {                     // If selection is directory
    	  if (position >0){									  // and not the UP
    		  File lbDir = GetFileList();					  // see if directory is empty
    		  lbDir = GetFileList();
    		  int length = FL.length;
    		  if (length == 0){ OptionalDelete(true, position);}			  // if empty, delete it.
    		  else{											 	              // other wise display files in it
    				SD_FilePath = FilePath;
    				Delete1();
    		  } 
    	  	  }else{
  				SD_FilePath = FilePath;
				
//    	  		  startActivity( new Intent(Delete.this, Delete.class));     //Display for UP
//    	  		  finish();
  				Delete1();;
    	  	  	}

    	}else{                                                  // Not Directory, delete the file
//    			File lbDir = GetFileList();
//    			File xbDir = new File(lbDir.getAbsoluteFile() + "/" + FL[position-1]);
    			OptionalDelete(false, position);
    	  }
    }
    
  });
  
  
  
  Toaster("Select file to Delete");

}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event)  {                     // If user presses back key
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {	 // got back to the editor
//    	startActivity( new Intent(Delete.this, Editor.class));
    	finish();
        return true;
    }
    return super.onKeyUp(keyCode, event);

//    return super.onKeyDown(keyCode, event);
}

	private File GetFileList(){
		
		// Get the list of files in a directory
		
		Basic.InitDirs();										// Make sure we have base directories.
		File sdDir = null;
		File lbDir = null;
		
		sdDir = new File("/sdcard/");							// Base dir is sdcard
		lbDir = new File(sdDir.getAbsoluteFile()
						 + "/" + FilePath);						// plus delete file path
		FL = lbDir.list();
		
		String F[] = {" "};
		if (FL == null) FL = F ;								// If the list was null, make a blank entry.
		
		return lbDir;
	}

	private boolean SelectionIsFile(int Index){
		
		// Tests the user selection for file or directory
		
		String theFileName = FL1.get(Index);
		if (Index == 0) {										// if GoUp is selected
			if (FilePath.equals("")){return false;}				// if up at top level dir, just return
			if (!FilePath.contains("/")){						// if path does not contain a /
				FilePath = "";									// then delete path now empty
				return false;
			}
			int k = FilePath.length();							// If there is a / in the delete path
			char c = ' ';										// find the right most /
			do {
				--k;
				c = FilePath.charAt(k);
			} while (c != '/');									// and then 
			FilePath = FilePath.substring(0, k);  				// eliminate / to end to make a new path
			return false;										// and report selection was not a file
		}
																// up is not selected
		int theLength = theFileName.length();					// see if selection has (d)
		int x = theLength - 3;
		if ( x > 0){
			String s1 = theFileName.substring(x);
			String s2 = "(d)";
			if (s1.equals(s2)){									// if has (d), then is directory
				s1 = theFileName.substring(0, x);				// add this name to the directory path
				FilePath = FilePath + "/" + s1;					// and report selection is not a file
				return false;
			}
		}
		return true;											// Finally, the selection must be a file
	}
	
    private void Toaster(CharSequence msg){						// Tell the user "msg"
		  Context context = getApplicationContext();			// via toast
		  CharSequence text = msg;
		  int duration = Toast.LENGTH_SHORT;
		  Toast toast = Toast.makeText(context, text, duration);
		  toast.setGravity(Gravity.TOP|Gravity.CENTER,0 , 0);
		  toast.show();
  }
 
    private void OptionalDelete(boolean dir, int position){					// we have a named file to delete
    																		// ask the user if she really wants to delete it
    	 File aFile;
    	 String theFileName;
    	 if (dir ){
    		 theFileName = "";
    	 }else{
    		theFileName = FL1.get(position);
    	 }
    	 
    	 aFile = new File(theFileName);
		  	 File sdDir = new File("/sdcard/");					// Base dir is sdcard
			 aFile = new File(sdDir.getAbsoluteFile()
					 + "/" + FilePath + "/" + theFileName);		// plus delete file path
		final File theFile = aFile;

    	AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);		// using a dialog box.

    	alt_bld.setMessage("Delete " + FL1.get(position))					// give the user the selected file name
    	.setCancelable(false)												// Do not allow user BACK key out of dialog
    	
// The click listeners ****************************
    	.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

    	public void onClick(DialogInterface dialog, int id) {				
    																		// Action for 'Yes' Button
        	dialog.cancel();

        	theFile.delete();												// Delete the file
    		Delete1();
   	}
    	})
    	.setNegativeButton("No", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int id) {
    																		//  Action for 'NO' Button
    	dialog.cancel();													// Do not delete the file
    	Delete1();
    	}
    	});
// End of Click Listeners ****************************************
    	
    	AlertDialog alert = alt_bld.create();								// Display the dialog
    	alert.setTitle("Confirm Delete");
    	alert.show();
    
    
    }
}
