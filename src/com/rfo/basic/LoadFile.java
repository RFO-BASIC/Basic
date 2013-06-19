/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.


Copyright (C) 2010 - 2013 Paul Laughton

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

    You may contact the author, Paul Laughton, at basic@laughton.com
    
	*************************************************************************************************/

package com.rfo.basic;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

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

//Log.v(LoadFile.LOGTAG, " " + LoadFile.CLASSTAG + " String Var Value =  " + d);

// Loads a file. Called from the Editor when user selects Menu->Load

public class LoadFile extends ListActivity {
    private static final String LOGTAG = "Load File";
    private static final String CLASSTAG = LoadFile.class.getSimpleName();

    private Context mContext;
    private ColoredTextAdapter mAdapter;
    private String ProgramPath = "";								   // Load file directory path
    private ArrayList<String> FL1 = new ArrayList<String>();

    public static class ColoredTextAdapter extends ArrayAdapter<String> {
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
  mContext = getApplicationContext();

	updateList();												// put file list in FL1

	//setListAdapter(new ArrayAdapter<String>(this, Settings.getLOadapter(this), FL1));  // Display the list
	mAdapter = new ColoredTextAdapter(this, FL1);
	setListAdapter(mAdapter);
	ListView lv = getListView();
	lv.setTextFilterEnabled(false);
	lv.setBackgroundColor(mAdapter.backgroundColor);

	// The click listener for the user selection *******************

	lv.setOnItemClickListener(new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			// User has selected a file.
			// If the selection is a directory, change the program path
			// and then display the list of files in that directory
			// otherwise load the selected file

			if (!SelectionIsFile(position)){
				Basic.SD_ProgramPath = ProgramPath;
				updateList();
			} else {
				FileLoader(FL1.get(position));
				return;
			}
		}
	});

	// End of Click Listener **********************************************
}

private void updateList(){

	 ProgramPath = Basic.SD_ProgramPath;                 // Set Load path to current program path
  	 File lbDir = new File(Basic.getSourcePath(ProgramPath));
	 lbDir.mkdirs();

	 String[] FL = lbDir.list();									// Get the list of files in this dir
	 if (FL == null){
		 Toaster(mContext, "System Error. File not directory");
		 return;
	 }
	 

	// Go through the list of files and mark directories with (d)
	// also only display files with the .bas extension
	ArrayList<String> dirs = new ArrayList<String>();
	ArrayList<String> files = new ArrayList<String>();
	String absPath = lbDir.getAbsolutePath() + '/';
	for (String s : FL) {
		File test = new File(absPath + s);
		if (test.isDirectory()) {								// If file is a directory, add "(d)"
			dirs.add(s + "(d)");								// and add to display list
		} else {
			if (s.endsWith(".bas")) {							// 	Only put files ending in
				files.add(s);									// .bas into the display list
			}
		}
	}
	Collections.sort(dirs);									// Sort the directory list
	Collections.sort(files);								// Sort the file list

	FL1.clear();
	FL1.add("..");											// put  the ".." to the top of the list
	FL1.addAll(dirs);										// copy the directory list to the adapter list
	FL1.addAll(files);										// copy the file list to the end of the adapter list

	if (mAdapter != null) { mAdapter.notifyDataSetChanged(); }

	Toaster(mContext, "Select File To Load");				// Tell the user what to do using Toast
}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event)  {						// If back key pressed
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {      // and the key is the BACK key
       finish();																// then done with LoadFile
       return true;
    }
    return super.onKeyUp(keyCode, event);
}

	public static String goUp(String path){					// Return the parent directory
		if (path.equals("")) { return path; }					// if up at top level dir, just return
		if (!path.contains("/")) {								// if path does not contain a /
			return "";											// then path now empty
		}
		int k = path.lastIndexOf('/');							// find the right most /
		return path.substring(0, k);							// eliminate / to end to make a new path
	}

	private boolean SelectionIsFile(int Index){
		
		// Test to see if the user selection is a file or a directory
		// If is directory, then change the path so that the files
		// in that directory will be displayed.
		
		String theFileName = FL1.get(Index);
		if (Index == 0) {										// if GoUp is selected
			ProgramPath = goUp(ProgramPath);					// change FilePath to its parent directory
			return false;										// report this is not a file
		}
																	// Not UP, is selection a dir
		if (theFileName.endsWith("(d)")) {							// if has (d), then is directory
			int k = theFileName.length() - 3;
			theFileName = theFileName.substring(0, k);
			ProgramPath = ProgramPath + "/" + theFileName;			// then add this dir to the path
			return false;											// and report not a file
		}
		return true;												// if none of the above, it is a file
	}

	private void  FileLoader(String aFileName){							// Loads the selected file
		
		// The user has selected a file to load, load it.

		Basic.ProgramFileName=aFileName;
		BufferedReader buf = null;
		boolean FileNotFound = false;

    																	//Write to SD Card

		String FullFileName = Basic.getSourcePath( 							// Base Source dir 
			  	Basic.SD_ProgramPath + 										// plus load path
			  	"/" + 
			  	aFileName);													// plus filename
		File file = new File(FullFileName);									// is full path to the file to load

			  try{ buf = new BufferedReader(new FileReader(file), 8096);}	// Open the reader.
			  catch(FileNotFoundException e){								// FNF should never happen
				  Log.e(LoadFile.LOGTAG, e.getMessage(), e);
				  FileNotFound = true;
				  };
			  
		  
		  									// File now opened
		String data = null;
		Basic.clearProgram(); 				// Clear the old program      
        Basic.lines.remove(0);				// Remove the the first line created by ClearProgam
    	Editor.DisplayText = "";			// Clear the display text buffer
    	
    	// read the file. Insert the the lines into Basic.lines.
//    	Log.v(LoadFile.LOGTAG, " " + LoadFile.CLASSTAG + " t1");
    	int size = 0;
     if (!FileNotFound){
		do
	        try {												// Read lines
            	data = buf.readLine();
	        	if (data == null){								// if null, say EOF
            		data="EOF";
        		}else {
        			Basic.lines.add(data);						// add the line 
        			size = size + data.length()+1;
        		}
             }
        	catch (IOException e) {
//				  Log.e(LoadFile.LOGTAG, e.getMessage(), e);
        		data="EOF";
        	}
        	while (!data.equals("EOF"));						// while not EOF
        														// File all read int
        	if (Basic.lines.isEmpty()){
        		Basic.lines.add("!");
        	}
     }else{
    	 
    	 // Special case of file not found and doing Launcher Shortcut
    	 // Turn the program file into an error message
    	 // and act as if we loaded a file
    	 
    	 Basic.lines.add("! Auto Run Error: \"" + aFileName + "\" not found");
    	 size = 30;
     }
     
     // The file is now loaded into Basic.lines. Next we need to move it
     // the lines into the Editor display buffer.
// 	Log.v(LoadFile.LOGTAG, " " + LoadFile.CLASSTAG + " t2");
 	StringBuilder sb = new StringBuilder(size);
     
        	String s = Basic.lines.get(0);						// Get the first line
//        	Basic.lines.add(" ");                               // Insures DisplayText does not end up null
//        	sb.append(' ');
        	if (s.length()==0){Basic.lines.remove(0);}          // If is an empty line, toss it
        	
            for (String line : Basic.lines){					// Copy the lines to the display buffer
            	sb.append(line + '\n');
//            	Editor.DisplayText = Editor.DisplayText+ line +"\n";
                }
//        	Log.v(LoadFile.LOGTAG, " " + LoadFile.CLASSTAG + " t3");
        	Editor.DisplayText = sb.toString();
            
            Basic.InitialProgramSize = Editor.DisplayText.length();		// Save the initial size for changed
            Basic.Saved =true;											// check
            if (Editor.mText == null) {
//            	android.os.Process.killProcess(Basic.ProcessID) ;
            	android.os.Process.killProcess(android.os.Process.myPid()) ;
            }
            Editor.mText.setText(Editor.DisplayText);
            finish();													// LoadFile is done
	}

	public static void Toaster(Context context, CharSequence msg){		// Tell the user "msg"
  		  CharSequence text = msg;										// via toast
  		  int duration = Toast.LENGTH_SHORT;
  		  Toast toast = Toast.makeText(context, text, duration);
		  toast.setGravity(Gravity.TOP|Gravity.CENTER,0 , 0);
  		  toast.show();
    }

}
