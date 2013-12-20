/****************************************************************************************************


BASIC! is an implementation of the Basic programming language for
Android devices.

This file is part of BASIC! for Android

Copyright (C) 2010 - 2013 Paul Laughton

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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


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
	private static String filePath = "";
	private static String basePath = "";

    private static final String LOGTAG = "Basic";
    private static final String CLASSTAG = Basic.class.getSimpleName();

    private static final String SOURCE_DIR    = "source";
    private static final String DATA_DIR      = "data";
    private static final String DATABASES_DIR = "databases";
    private static final String SAMPLES_DIR   = "Sample_Programs";
    private static final String SOURCE_SAMPLES_PATH = SOURCE_DIR + '/' + SAMPLES_DIR;

    public static ArrayList<String> lines;       //Program lines for execution
    
    public static Boolean Saved ;				// False when program has changed and not saved
    public static int InitialProgramSize = 0;	// Used to determine if program has changed
    public static String ProgramFileName = "";  // Set when program loaded or saved
    
    public static Context BasicContext;			// saved so we do not have to pass it around
    public static Context theRunContext = null;
    private String BasicPackage = null;
    
    public static String DataPath = "";			// Used in RunProgram to determine where data is stored
    public static String SD_ProgramPath = "";		// Used by Load/Save
    public static String IM_ProgramPath = "help";	// Used by Load/Save
    
    public static Intent theProgramRunner = null;
    
    
    private Background theBackground;						// Background task ID
    private ArrayAdapter<String> AA;
    private ListView lv;							    	// The output screen list view
    private ArrayList<String> output;						// The output screen text lines

	public static boolean checkSDCARD(char mount) {			// mount is 'w' for writable,
															// 'r' for either readable or writable 
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) { return true; }	// mounted for both read and write
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) && (mount == 'r')) return true;
		return false;
	}

	public static void setFilePaths(String basePath) {		// set both basePath and filePath
															// public so LauncherShortcuts can use it
		if (basePath.equals("none"))
			basePath = Environment.getExternalStorageDirectory().getPath();
		Basic.basePath = basePath;
		Basic.filePath = basePath + "/" + AppPath;
	}

	public static String getBasePath() {
		return basePath;
	}

	public static String getFilePath() {
		return filePath;
	}

	public static String getFilePath(String subdir, String subPath) {
		// if (!subPath.startsWith("/"))	uncomment to enable absolute paths
		{
			StringBuilder path = new StringBuilder(filePath);
			if (subdir != null) { path.append('/').append(subdir); }
			if (subPath != null) { path.append('/').append(subPath); }
			subPath = path.toString();
		}
		return subPath;
	}

	// Usage note: if subPath is null, function returns "/<basePath>/<AppPath>/source"
	// but if subPath is "", function returns "/<basePath>/<AppPath>/source/", with "/" appended
	public static String getSourcePath(String subPath) {
		return getFilePath(SOURCE_DIR, subPath);
	}

	public static String getSamplesPath(String subPath) {
		return getFilePath(SOURCE_SAMPLES_PATH, subPath);
	}

	public static String getDataPath(String subPath) {
		return getFilePath(DATA_DIR, subPath);
	}

	public static String getDataBasePath(String subPath) {
		return getFilePath(DATABASES_DIR, subPath);
	}

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);					// Set up of fresh start
        
        Log.v(LOGTAG, CLASSTAG + " onCreate");
        BasicContext = getApplicationContext();
        BasicPackage = BasicContext.getPackageName();

        String base = Settings.getBaseDrive(this);
        setFilePaths(base);

        if (isAPK) {
        	createForAPK();
        } else {
        	createForSB();
        }
       
    }
    
    private void createForSB() {							// Create code for Standard Basic
        
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
        
/* If Basic is entered from a launcher shortcut, then there will an Intent with a
 * bundle. The bundle will contain the shortcut launcher program name.
 * 
 * Auto run will be called with a new bundle with just the filename. 
 */
 
        Intent myIntent = getIntent();
        String FileName = myIntent.getStringExtra(LauncherShortcuts.EXTRA_LS_FILENAME); // Launched by shortcut?
        if (FileName == null) {		// Launched with a .bas as argument?
	    if (myIntent.getData() != null) FileName = myIntent.getData().getPath();
	}

	if ((FileName != null) && ! FileName.equals("")) {
        	Bundle bb = new Bundle();
        	bb.putString("fn", FileName);								// fn is the tag for the filename parameter
        	Intent intent = new  Intent(BasicContext, AutoRun.class);	// in the bundle going to AutoRun
        	intent.putExtras(bb);
		DoAutoRun = true;
        	startActivity( intent );
        	finish();
        } else if (AreSamplesLoaded()) {							// This is not a launcher short cut 
        	DoAutoRun = false;
        	startActivity(new Intent(BasicContext, Editor.class));		// 	Goto the Editor
        	finish();
	} else {											// The sample files have not been loaded
		runBackgroundLoader();							// Start the background task to load samples and graphics
	}
    }
    
    private void createForAPK() {											// Create code for APK
    	runBackgroundLoader();
    }
    
    private void runBackgroundLoader() {
// Establish an output screen so that file load progress can be shown.        
        
  	  	output = new ArrayList<String>();
    	AA=new ArrayAdapter<String>(this, R.layout.simple_list_layout, output);  // Establish the output screen
  	  	lv = getListView();
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

		if (checkSDCARD('w')) {									// Insure that SD card is mounted and writable
    		File sdDir;
    		if (!isAPK) {
				sdDir = new File(getSourcePath(null));			// source directory
        		sdDir.mkdirs();

				sdDir = new File(getSamplesPath(null));			// sample programs directory
        		sdDir.mkdirs();

				sdDir = new File(getDataPath(null));			// data directory
    			sdDir.mkdirs();

				sdDir = new File(getDataBasePath(null));		// databases directory
    			sdDir.mkdirs();
    		}
    		
    		if (isAPK && apkCreateDataDir) {
				sdDir = new File(getDataPath(null));			// data directory
    			sdDir.mkdirs();
    		}
    		
    		if (isAPK && apkCreateDataBaseDir) {
				sdDir = new File(getDataBasePath(null));		// databases directory
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
    }
    
    
    private boolean AreSamplesLoaded(){				// Sample program files have not been loaded
    												// if the sample programs directory is empty
    	String samplesPath = getSamplesPath("");	// get path with trailing '/'
    	File sdDir = new File(samplesPath);
    	sdDir.mkdirs();
    	String FL[] = sdDir.list();

    	if ((FL != null) && (FL.length != 0)) {		// if the help directory is not empty
    												// then sort the files
    		
    		ArrayList<String> FL1 = new ArrayList<String>(Arrays.asList(FL));
    		Collections.sort(FL1);					// Convert to array list to easily sort
    		String f1 = FL1.get(0);					// The top of the list should be the
    		if (f1.length()>11){					// f00_vnn.nn_xxx file, 
    			String f2 = f1.substring(5, 7);
    			String f3 = f1.substring(8,10);
    			f1 = f2 + "." + f3;
    			f2 = BasicContext.getString(R.string.version);         // Get the version string
    			if (f1.equals(f2)) { return true; }		               // Compare version numbers
    		}
    		for (String fileName : FL) {			// If different, empty the directory
    			File file = new File(samplesPath + fileName);
    			file.delete();
    		}
    	}
    	return false;
    }

	public static String getRawFileName(String input) {
		// Converts a file name to an Android internal resource name. 
		// Upper-case characters are converted to lower-case.
		// If there is a dot in the name, the dot and everything after it are dropped.

		// MyFile.png = myfile
		// bigSound.mp3 = bigsound
		// Earth.jpg = earth

		if (input == null) return "";
		String output = input.toLowerCase();		// Convert to lower case
		int index = output.indexOf(".");			// Find the dot
		if (index == -1) return output;				// if no dot, return as is
		return output.substring(0, index);			// else isolate stuff in front of dot
	}

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
    	            startActivity(new Intent(BasicContext, Editor.class));	// 	Goto the Editor
    	            finish();
    	        }
    	        
    	        private void doBGforAPK() {								// Background code of APK
    	            InitDirs();											// Initialize Basic directories every time
    	            LoadGraphics();										// Load the graphics files

    	            lines = new ArrayList <String>();                    // Program will be loaded into this array list

    	            LoadTheFile();                                       // Load the basic program file into memory

    	            theProgramRunner = new Intent(BasicContext, Run.class);		//now go run the program
    	            theRunContext = null;
    	            DoAutoRun = true;
    	            startActivity(theProgramRunner);                     // The program is now running
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

    	        private void LoadGraphics(){
    	        	
    	        	// Loads the icons and audio used for the example programs
    	        	// The files are copied from res.raw to the SD Card

    	        	String dataPath = getDataPath("");				// get paths with trailing '/'
    	        	String databasesPath = getDataBasePath("");

    	        	for (String fileName : loadFileNames) {
    	        		if (fileName.equals("")) return;

    	        		String fn = getRawFileName(fileName);
    	        		int resID = BasicContext.getResources().getIdentifier(fn, "raw", BasicPackage);
    	        		InputStream inputStream = BasicContext.getResources().openRawResource(resID);
    	        		String path = (fileName.endsWith(".db")) ? databasesPath : dataPath;
    	        		Load1Graphic(inputStream, path + fileName);
    	        	}
    	        }

    	        private void Load1Graphic(InputStream inputStream, String PathA ){
    	        	    	
    	        	// Does the actual loading of one icon or audio file
    	        	    	
    	        	        DataInputStream dis = new DataInputStream(inputStream);
    	        	 		File aFile = new File(PathA);
    	        	 		DataOutputStream dos1 = null;

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
//    	        	         	  Log.v(LOGTAG, CLASSTAG + " I/O Exception 2 ");
    	        	          }
    	        	          finally {
    	        	  			if (dos1 != null) {
    	        	  				try {
    	        	  					dos1.flush();
    	        	  					dos1.close();
    	        	  				} catch (IOException e) {
//    	        	  		        	  Log.v(LOGTAG, CLASSTAG + " I/O Exception 4 ");
    	        	  				}
    	        	  			}
    	        	          }

    	        	    }
    	        
    	        private void LoadSamples(){
    	        	// The files are packaged int res.raw.
    	        	// The file, the_list contain the list of files to be loaded.
    	        	// Get that list, and load those files
    	        	
    	    		SD_ProgramPath = SAMPLES_DIR;   // This setting will also force LoadFile to so the help directory
    	    		
    	    // The first thing done is to load the file, the_list. This file contains a list of the files
    	    // to be loaded.
    	    		
    	    		// Using this round about method to get the resID for the_list
    	    		// because the the_list will not be there in APKs
    	    		
	        		String fn = getRawFileName("the_list");
	        		int resID = BasicContext.getResources().getIdentifier (fn, "raw", BasicPackage);
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
//    	            	  Log.v(LOGTAG, CLASSTAG + " I/O Exception 1  ");
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
    	        	
    	        	int ResId = BasicContext.getResources().getIdentifier(theFileName, "raw", BasicPackage);
    	            InputStream inputStream = BasicContext.getResources().openRawResource(ResId);
    	            InputStreamReader inputreader = new InputStreamReader(inputStream);
    	            BufferedReader buffreader = new BufferedReader(inputreader, 8192);
    	            String line;

    	            SD_ProgramPath = SAMPLES_DIR;
    	            FileWriter writer = null;
    	            String path = getSamplesPath(theFileName + ".bas");				// add .bas to the created file name.

    	            try {
    	               writer = new FileWriter(path);   
    	               while (( line = buffreader.readLine()) != null) {			 // Read and write one line at a time
    	            	   line = line + "\n";
    	            	   writer.write(line);
    	                 }
    	             } catch (IOException e) {
//    	            	  Log.v(LOGTAG, CLASSTAG + " I/O Exception 3 ");
    	             }
    	             finally {
    	     			if (writer != null) {
    	     				try {
    	     					writer.flush();
    	     					writer.close();
    	     				} catch (IOException e) {
//    	     		        	  Log.v(LOGTAG, CLASSTAG + " I/O Exception 4 ");
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
    	        	String ResName = "my_program";
    	        	int ResId = BasicContext.getResources().getIdentifier(ResName, "raw", BasicPackage);
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

    } // class Background

	public static class ScreenColors {
		public int textColor;
		public int backgroundColor;
		public int lineColor;

		public ScreenColors() {
			// Resources res = BasicContext.getResources();
			int black = 0xff000000;		// res.getInteger(R.integer.color_black);
			int white = 0xffffffff;		// res.getInteger(R.integer.color_white);
			int blue  = 0xff006478;		// res.getInteger(R.integer.color_blue);
			String colorSetting = Settings.getEditorColor(BasicContext);
			if (colorSetting.equals("BW")) {
				textColor = black;
				backgroundColor = white;
				lineColor = textColor;
			} else
			if (colorSetting.equals("WB")) {
				textColor = white;
				backgroundColor = black;
				lineColor = textColor;
			} else
			if (colorSetting.equals("WBL")) {
				textColor = white;
				backgroundColor = blue;
				lineColor &= black;
			}
			lineColor &= 0x80ffffff;		// half alpha
		}
	} // class ScreenColors

	public static class ColoredTextAdapter extends ArrayAdapter<String> {
		private final Activity mActivity;
		private final ArrayList<String> mList;
		private Typeface mTypeface = null;
		public int textColor;
		public int backgroundColor;

		public ColoredTextAdapter(Activity activity, ArrayList<String> alist, Typeface typeface) {
			this(activity, alist);
			mTypeface = typeface;
		}

		public ColoredTextAdapter(Activity activity, ArrayList<String> alist) {
			super(activity, Settings.getLOadapter(activity), alist);
			mActivity = activity;
			mList = alist;
			ScreenColors colors = new ScreenColors();
			textColor = colors.textColor;
			backgroundColor = colors.backgroundColor;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) mActivity.getLayoutInflater();
				row = inflater.inflate(Settings.getLOadapter(mActivity), null);
			}
			TextView text = (TextView) row.findViewById(R.id.the_text);
			text.setTextColor(textColor);
			text.setText(mList.get(position));
			if (mTypeface != null) { text.setTypeface(mTypeface); }
			text.setBackgroundColor(backgroundColor);

			return row;
		}
	} // class ColoredTextAdapter

	public static void toaster(Context context, CharSequence msg) {		// Tell the user "msg" via Toast
		if ((context == null) || (msg == null) || msg.equals("")) return;
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 50);			// default: short, high toast
		toast.show();
	}

}
