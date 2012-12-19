/****************************************************************************************************


BASIC! is an implementation of the Basic programming language for
Android devices.

This file is part of BASIC! for Android

Copyright (C) 2010, 2011, 2012 Paul Laughton

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

import static com.rfo.basic.Basic.BasicContext;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import 	java.io.DataOutputStream;
import 	java.io.DataInputStream;

import android.app.ListActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import android.content.Intent;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;

import java.util.*;



public class Basic extends ListActivity  {
	
    public static String AppPath = "rfo-basic";             // Set to the path name for application directories
    public static boolean isAPK = false;					// If building APK, set true
    public static boolean apkCreateDataDir = true;			// If APK needs a /data/ directory, set true
    public static boolean apkCreateDataBaseDir = true;      // If APK needs a /database/ director, set true
    
    // If there are any files that need to be copied from raw resources into the data directory, list them here
    // If there are not files to be copied, make one entry of an empty string ("")
    // Most APKs can leave their files in raw resources and not copy them.
    // The various run commands will detect the situation and read the files from the raw resources.
    // Note: If the filename has the ".db" extension, it will be loaded into the /databases/ directory.
    
    public static final String loadFileNames [] = {			
    	
    	"boing.mp3", "cartman.png", "meow.wav",
    	"fly.gif", "galaxy.png", "whee.mp3",
    	"htmldemo1.html", "htmldemo2.html",
    	""
    };
    
    
	public static Boolean DoAutoRun = false;
	public static String filePath = "";
	public static String basePath = "";
    private static final String LOGTAG = "Basic";
    private static final String CLASSTAG = Basic.class.getSimpleName();
    public static ArrayList<String> lines;       //Program lines for execution
    
    public static Boolean Saved ;				// False when program has changed and not saved
    public static int InitialProgramSize = 0;	// Used to determine if program has changed
    public static String ProgramFileName = "";  // Set when program loaded or saved
    
    public static Context BasicContext;			// saved so we do not have to pass it around
    public static Context theRunContext = null;
    
    public static String DataPath = "";			// Used in RunProgram to determine where data is stored
    public static String SD_ProgramPath = "";		// Used by Load/Save
    public static String IM_ProgramPath = "help";	// Used by Load/Save
    
    public static Intent theProgramRunner = null;
    
    
    public static Background theBackground;					// Background task ID
    public static ArrayAdapter AA;
    public static ListView lv ;							    // The output screen list view
    public static ArrayList<String> output;					// The output screen text lines

        
//  Log.v(Basic.LOGTAG, " " + Basic.CLASSTAG + " String Var Value =  " + d);
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);					// Set up of fresh start
        
        String test = Settings.getBaseDrive(this);
        if (test.equals("none")) 
        	basePath = Environment.getExternalStorageDirectory().getPath();
        else 
        	basePath = test;
        
        filePath =  basePath + "/" + AppPath;
        if (isAPK) {
        	createForAPK();
        } else {
        	createForSB();
        }
        
       
    }
    
    private void createForSB() {							// Create code for Standard Basic

        BasicContext = getApplicationContext();
        
        
/* If we have entered Basic and there is a program running, then we should not
 * Interfere with that run. We will just exit this attempt. A program running
 * is indicated by theRunContext ! = null
 */
        
        if (theRunContext!=null) {
        	finish();
        	return;
        }
        InitDirs();											// Initialize Basic directories every time
        													// The user may have put in a new SD Card
        
        clearProgram();										// Clear the basic program
        
        boolean samplesLoaded = AreSamplesLoaded();				// Checks to see if the help files have been loaded
//        samplesLoaded = false;

        if (!samplesLoaded){        							// If sample files not loaded, then load them
      	  	output = new ArrayList<String>();
        	AA=new ArrayAdapter<String>(this, R.layout.simple_list_layout, output);  // Establish the output screen
      	  	lv = getListView();
      	  	lv.setTextFilterEnabled(false);
      	  	lv.setTextFilterEnabled(false);
      	  	lv.setSelection(0);
        	theBackground = new Background();						// Start the background task to load
        	theBackground.execute("");								// sample and graphics
        															// and then go to the editor
        } else 
        // ************ Samples were already loaded ***************************
        {

/* If Basic is entered from a launcher shortcut, then there will an Intent with a
 * bundle. The bundle will contain the shortcut launcher program name.
 * 
 * Auto run will be called with a new bundle with just the filename. 
 */
 
        Intent myIntent = getIntent();
        final String action = myIntent.getAction();
        Bundle b = myIntent.getExtras();
        if (b != null) {												   // There is a bundle, thus shortcut run.
        	String FileName = myIntent.getStringExtra("com.rfo.basic.fn"); // The tag for the filename parameter
        	Bundle bb = new Bundle();
        	bb.putString("fn", FileName);								  // fn is the tag for the filename parameter
        	Intent intent = new  Intent(Basic.this, AutoRun.class);		  // in the bundle going to AutoRun
        	intent.putExtras(bb);
			DoAutoRun = true;
        	startActivity( intent );
        	finish();
        } else {															// This is not a launcher short cut 
        	DoAutoRun = false;
        	startActivity(new Intent(this, Editor.class));					// 	Goto the Editor
        	finish();
        	}
        }
    }
    
    private void createForAPK() {											// Create code for APK
    	
        BasicContext = getApplicationContext();
        
// Establish an output screen so that file load progress can be shown.        
        
  	  	output = new ArrayList<String>();
    	AA=new ArrayAdapter<String>(this, R.layout.simple_list_layout, output);  // Establish the output screen
  	  	lv = getListView();
  	  	lv.setTextFilterEnabled(false);
  	  	lv.setTextFilterEnabled(false);
  	  	lv.setSelection(0);
  	  	
// In order to show progress in the user interface we have to start a background thread to 
// do the actual loading. That background thread will make calls to the UI thread to show
// show the progress.
//
// Once the files are loaded, the background task will start the loaded program running.
  	  	
    	theBackground = new Background();						// Start the background task to load
    	theBackground.execute("");								// sample and graphics

    }
    
    public static void InitDirs(){
    	
// Initializes (creates) the directories used by Basic
    	
// Start by making sure the SD Card is available and writable
    	
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
    	
// The SD Card is available and can be writen to. 
    	
    	if (CanWrite){
    		String PathA = "";
    		File sdDir;
    		if (!isAPK) {
    			PathA = filePath + "/source/";  					 // Source directory
        		sdDir = new File(PathA);
        		sdDir.mkdirs();
        		
        		PathA = filePath + "/source/Sample_Programs";     // help directory
        		sdDir = new File(PathA);
        		sdDir.mkdirs();
        		
    			PathA = filePath + "/data";            // data directory
    			sdDir = new File(PathA);
    			sdDir.mkdirs();
    			
    			PathA = filePath + "/databases";		// databases directory
    			sdDir = new File(PathA);
    			sdDir.mkdirs();
    		}
    		
    		if (isAPK && apkCreateDataDir) {
    			PathA = filePath + "/data";            // data directory
    			sdDir = new File(PathA);
    			sdDir.mkdirs();
    		}
    		
    		if (isAPK && apkCreateDataBaseDir) {
    			PathA = filePath + "/databases";		// databases directory
    			sdDir = new File(PathA);
    			sdDir.mkdirs();
    		}
    	}

   	
    }
    
    public  static void clearProgram(){
    	
      lines = new ArrayList<String>();					// The lines array list is the program
      String theString = "";								
      lines.add(theString);								// add an empty string to lines
	   Editor.DisplayText="REM Start of BASIC! Program\n";      // Display text is the editors program storage for display
       Basic.InitialProgramSize = Editor.DisplayText.length();  // Save the initial program size.
       															// This value will be used to determine if the program
       															// has changed.
      Saved = true;												// Indicates that the program has been saved.
    };
    
    
    private boolean AreSamplesLoaded(){
    	String PathA = filePath + "/source/Sample_Programs";
		File sdDir = new File(PathA);
    	sdDir.mkdirs();
    	String FL[] = null;							// Help files have not been loaded  
    	FL =sdDir.list();							// if the help directory is empty
    	if (FL == null) return false;
    	
    	if (FL.length != 0 ){						// if the help directory is not empty
    												// then sort the files
    		
    		ArrayList<String> FL1 = new ArrayList<String>(Arrays.asList(FL));
    		Collections.sort(FL1);					// Convert to array list to easily sort
    		String f1 = FL1.get(0);					// The top of the list should be the
    		if (f1.length()>11){					// f00_vnn.nn_xxx file, 
    			String f2 = f1.substring(5, 7);
    			String f3 = f1.substring(8,10);
    			f1 = f2 + "." + f3;
    			f2 = Basic.BasicContext.getString(R.string.version);   // Get the version string
    			if (f1.equals(f2)) return true;			               // Compare version numbers
    		}
    		for (int k = 0 ; k< FL.length; ++k){	// If different, empty the directory
    			File file = new File(filePath + "/source/Sample_Programs/" + FL[k]);
    			file.delete();
    		}
    		return false;
    	}
    	else return false;    }
 
    // The loading of the sample files and graphics is done in a background, Async Task rather than the UI task
    // Progress is shown by sending progress messages to the UI task.
 
    public class Background extends AsyncTask<String, String, String>{
  
    	        @Override
    	        protected  String doInBackground(String...str ) {
    	        	
    	        	if (isAPK) {
    	        		doBGforAPK();
    	        	} else {
    	        		doBGforSB();
    	        	}
    	        	return "";
    	        }
    	        
    	        private void doBGforSB() {									// Background code for Standard Basic
    	         	LoadSamples();
    	        	LoadGraphics();											// and the graphics files too
    	        	doFirstLoad();											// First load shows a first load basic program
   	            	DoAutoRun = false;
    	            startActivity(new Intent(Basic.this, Editor.class));	// 	Goto the Editor
    	            finish();
    	        }
    	        
    	        private void doBGforAPK() {								// Background code of APK
    	            InitDirs();											// Initialize Basic directories every time
    	            LoadGraphics();										// Load the graphics files

    	            Basic.lines = new ArrayList <String>();              // Program will be loaded into this array list

    	            LoadTheFile();                                       // Load the basic program file into memory

    	            theProgramRunner = new Intent(BasicContext, Run.class);		//now go run the program
    	            theRunContext = null;
    	            DoAutoRun = true;
    	            startActivity(Basic.theProgramRunner);               // The program is now running
    	            finish();

    	        }
    	        
    	        @Override
    	        protected void onPreExecute(){
    	        	output.add("Standby for initial file loading.");  // The message that tells what we are doing.
    	        	output.add("");
    	        }
    	        
    	        public double lineCount = 0;
    	        @Override    
    	        protected void onProgressUpdate(String... str ) {     // Called when publishProgress() is executed.
    	        	int CHAR_PER_LINE = 20;					  // Number of dots per line
    	        	int LINES_PER_UPDATE = 30;				  // Number of line between Progress Messages
    	        	int MAX_UPDATE_COUNT = 0;					  // Change to maximum number of progress messages, if known.
    	        	
    	        	for (int i=0; i<str.length; ++i){				  			  // Form line of CHAR_PER_LINE progress characters
    	        		String s = output.get(output.size() -1);
    	        		s = s + str[i];
    	        		output.set(output.size()-1, s);
    	        		if (s.length() >= CHAR_PER_LINE){						  // After the CHAR_PER_LINEth character
    	        			++lineCount;										  // output a new line
    	        																  // After every LINES_PER_UPDATE
    	        			double dd = lineCount%LINES_PER_UPDATE;
    	        			if (dd == 0) {				   
    	        			    double updates = Math.floor(lineCount/LINES_PER_UPDATE);  // Output a progress message

    	        			    if (MAX_UPDATE_COUNT > 0 ) {								 // if the max is known
    	        			    	updates = updates/MAX_UPDATE_COUNT * 100;				 // convert progress to a percent
    	        					output.add("Continuing load (" + updates + "%)");
    	        			    } else 														 // else just use the number of progress line
    	        			    	output.add("Continuing load (" + (int)updates + ")");
    	        			}
    	        			output.add("");							  // start a new line
    	        		}
    	        	}
    	        	
    	        	setListAdapter(AA);						// show the output
    		    	lv.setSelection(output.size()-1);		// set last line as the selected line to scroll
    	        }
    	        
    	  	  private String getRawFileName(String input) {
    			  
    			  // Converts a file name with upper and lower case characters
    			  // to a lower case filename.
    			  // The dot extension is appended to the end of the filename
    			  // preceeded by "_"
    			  
    			  // MyFile.png = myfile_png
    			  // bigSound.mp3 = bigsound_mp3
    			  // Earth.jpg = earth_jpg
    			  
    			  // if there is no dot extension, returns empty string ""
    			  
    			  // If the isAudio flag is true then
    			  // the _mp3 (or whatever) will not be appended
    			  
    			  String output = "";                   // Set to empty string
    			  input = input.toLowerCase();			// Convert to lower case
    			  int index = input.indexOf(".");		// Find the dot
    			  if (index == -1) return input;		// if no dot, return the input as is
    			  output = input.substring(0, index);   // isolate suff in front of dot
    			  return output;						// return the output filename
    		  }

    	        
    	        private void LoadGraphics(){
    	        	
    	        	// Loads the icons and audio used for the example programs
    	        	// The files are copied form res.raw to the SD Card
    	        	int resID;
    	        	String packageName = Basic.BasicContext.getPackageName();
    	        	String PathA;
    	        	
    	        	for (int index = 0; index <=loadFileNames.length; ++index) {
    	        		if (loadFileNames[index].equals("") )return;
    	        		String fn = getRawFileName(loadFileNames[index]);
    	        		resID = Basic.BasicContext.getResources().getIdentifier (fn, "raw", packageName);
		    	        InputStream inputStream = BasicContext.getResources().openRawResource(resID);
		    	        if (loadFileNames[index].endsWith(".db"))
		    	        	PathA = filePath + "/databases/" + loadFileNames[index];
		    	        else 
		    	        	PathA = filePath + "/data/" + loadFileNames[index];
	        	 		Load1Graphic(inputStream, PathA);
    	        	}
    	        	    	    	
    	        	                  
    	        	    }
    	        	    
    	        	    private void Load1Graphic(InputStream inputStream, String PathA ){
    	        	    	
    	        	// Does the actual loading of one icon or audio file
    	        	    	
    	        	        DataInputStream dis = new DataInputStream(inputStream);
    	        	 		File aFile = new File(PathA);
    	        	 		DataOutputStream dos1 = null;

    	        	    	
    	        	    	aFile = new File(PathA);
    	        	  		dos1 = null;
    	        	  		 
    	        	// Note the I/O methods used here specialized for reading and writing
    	        	// non-text data files; 
    	        	  		
    	        	         int Byte = 0;
    	        	         int count = 0;
    	        	         try {
    	        	              FileOutputStream fos = new FileOutputStream(aFile);
    	        	              dos1 = new DataOutputStream(fos);
    	        	             do {
    	        	            	 Byte = dis.readByte();
    	        	            	 dos1.writeByte(Byte);
    	        	            	 ++count;
    	        	            	 if (count >= 4096){			// Show progress every 4k bytes
    	        	            		 publishProgress(".");
    	        	            		 count = 0;
    	        	            	 }
    	        	         	 } while (dis != null);
    	        	              
    	        	          } catch (IOException e) {
//    	        	         	  Log.v(Basic.LOGTAG, " " + Basic.CLASSTAG + " I/O Exception 2 ");
    	        	          }
    	        	          finally {
    	        	  			if (dos1 != null) {
    	        	  				try {
    	        	  					dos1.flush();
    	        	  					dos1.close();
    	        	  				} catch (IOException e) {
//    	        	  		        	  Log.v(Basic.LOGTAG, " " + Basic.CLASSTAG + " I/O Exception 4 ");
    	        	  				}
    	        	  			}
    	        	          }

    	        	    }
    	        
    	        private void LoadSamples(){
    	        	// The files are packaged int res.raw.
    	        	// The file, the_list contain the list of files to be loaded.
    	        	// Get that list, and load those files
    	        	
    	    		SD_ProgramPath = "Sample_Programs";   // This setting will also force LoadFile to so the help directory
    	    		
    	    // The first thing done is to load the file, the_list. This file contains a list of the files
    	    // to be loaded.
    	    		
    	    		// Using this round about method to get the resID for the_list
    	    		// because the the_list will not be there in APKs
    	    		
    	        	int resID;
    	    		String packageName = Basic.BasicContext.getPackageName();
	        		String fn = getRawFileName("the_list");
	        		resID = Basic.BasicContext.getResources().getIdentifier (fn, "raw", packageName);
	    	        InputStream inputStream = BasicContext.getResources().openRawResource(resID);

    	            InputStreamReader inputreader = new InputStreamReader(inputStream);
    	            BufferedReader buffreader = new BufferedReader(inputreader, 8192);
    	            String line;
    	            ArrayList <String> LoadList = new ArrayList <String>();  // Stores the list of files to load

    	     // Read each line from the_list and add the filename to LoadList
    	            
    	             try {
    	               while (( line = buffreader.readLine()) != null) {
    	                   LoadList.add(line);
    	                 }
    	             } catch (IOException e) {
//    	            	  Log.v(Basic.LOGTAG, " " + Basic.CLASSTAG + " I/O Exception 1  ");
    	             }
    	             
    	    // Now go load each file in the list, one at a time
    	             
    	             for (int i=LoadList.size()-1; i >=0; --i){      // Now that we have the list of files to load
    	            	 LoadOneFile(LoadList.get(i));				 // load them one at a time
    	             }
    	        }

    	        private void LoadOneFile(String theFileName){
    	        	
    	        	// Reads one file from res.raw and writes it to the SD Card
    	        	// into the source/help directory
    	        	publishProgress(".");							 // Show progress for each program loaded
    	        	
    	        	String ResName = "com.rfo.basic:raw/"+theFileName;
    	        	int ResId = BasicContext.getResources().getIdentifier(ResName, null, null);
    	            InputStream inputStream = BasicContext.getResources().openRawResource(ResId);
    	            InputStreamReader inputreader = new InputStreamReader(inputStream);
    	            BufferedReader buffreader = new BufferedReader(inputreader, 8192);
    	            String line;

    	            FileWriter writer = null;
    	    		String PathA = filePath + "/source/Sample_Programs";
    	    		SD_ProgramPath = "Sample_Programs";
    	    		
    	             
    	             try {
    	               writer = new FileWriter(PathA +"/" + theFileName + ".bas");   // add .bas to the created file name.
    	               while (( line = buffreader.readLine()) != null) {			 // Read and write one line at a time
    	            	   line = line + "\n";
    	            	   writer.write(line);
    	                 }
    	             } catch (IOException e) {
//    	            	  Log.v(Basic.LOGTAG, " " + Basic.CLASSTAG + " I/O Exception 3 ");
    	             }
    	             finally {
    	     			if (writer != null) {
    	     				try {
    	     					writer.flush();
    	     					writer.close();
    	     				} catch (IOException e) {
//    	     		        	  Log.v(Basic.LOGTAG, " " + Basic.CLASSTAG + " I/O Exception 4 ");
    	     				}
    	     			}
    	     		}    
    	         }
    	        
    	        public   void doFirstLoad(){
    	        	// The first load is a short program of comments that will be displayed
    	        	// by the Editor
    	        	    	
    	        		   Editor.DisplayText="!!\n\n" +
    	        		   "Welcome to BASIC!\n\n" +
    	        		   "Press Menu->More->About to\n" +
    	        		   "get the full information\n" +
    	        		   "about this release.\n\n" +
    	        		   "The BASIC! User's Manual,\n" +
    	        		   "De Re BASIC!, can also be\n" +
    	        		   "accessed from that location.\n\n" +
    	        		   "Press Menu->Clear to clear\n" +
    	        		   "this program and start \n"+
    	        		   "editing your own BASIC!\n"+
    	        		   "program.\n\n"+
    	        		   "!!"
    	        		   ;  // Initialize the Display Program Lines
    	        	       Basic.InitialProgramSize = Editor.DisplayText.length();
    	        	      Saved = true;
    	        	    }
    	        
    	        private void LoadTheFile(){
    	        	
    	        	// Reads the program file from res.raw/my_program and 
    	            // puts it into memory
    	        	AddProgramLine APL = new AddProgramLine();
    	        	String ResName = "com.rfo.basic:raw/my_program";
    	        	int ResId = BasicContext.getResources().getIdentifier(ResName, null, null);
    	            InputStream inputStream = BasicContext.getResources().openRawResource(ResId);
    	            InputStreamReader inputreader = new InputStreamReader(inputStream);
    	            BufferedReader buffreader = new BufferedReader(inputreader, 8192);
    	            
    	            String line = "";
    	            int count = 0;
    	            
    	             try {
    	               while (( line = buffreader.readLine()) != null) {			 // Read and write one line at a time
    	            	   APL.AddLine(line, false);                         		 // add the line to memory
    	            	   ++count;
    	            	   if (count >= 200){										 // Show progress every 250 lines.
    	            		   publishProgress(" ");
    	            		   count = 0;
    	            	   }
    	                 }
    	             } catch (IOException e) {
    	             }
    	     		    
    	         }

    		}
    
    
    
}