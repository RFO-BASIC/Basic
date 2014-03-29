/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

This file is part of BASIC! for Android

Copyright (C) 2010 - 2014 Paul Laughton

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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

	private static final String LOGTAG = "Basic";
	private static final String CLASSTAG = Basic.class.getSimpleName();

	public static final String SOURCE_DIR    = "source";
	public static final String DATA_DIR      = "data";
	public static final String DATABASES_DIR = "databases";
	public static final String SAMPLES_DIR   = "Sample_Programs";
	public static final String SOURCE_SAMPLES_PATH = SOURCE_DIR + '/' + SAMPLES_DIR;

	public static String AppPath = "rfo-basic";				// Set to the path name for application directories
	public static boolean isAPK = false;					// If building APK, set true
	private static boolean apkCreateDataDir;				// If APK needs a /data/ directory, set true
	private static boolean apkCreateDataBaseDir;			// If APK needs a /database/ director, set true

	public static boolean DoAutoRun = false;
	private static String filePath = "";
	private static String basePath = "";

	public static ArrayList<String> lines;					//Program lines for execution

	public static String ProgramFileName;					// Set when program loaded or saved

	public static Context BasicContext;						// saved so we do not have to pass it around
	public static Context theRunContext;

	public static String SD_ProgramPath = "";	// Used by Load/Save
	public static Intent theProgramRunner;

	private Background theBackground;						// Background task ID
	private ArrayAdapter<String> AA;
	private ListView lv;									// The output screen list view
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
		Basic.filePath = basePath + File.separatorChar + AppPath;
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
			if (subdir != null) { path.append(File.separatorChar).append(subdir); }
			if (subPath != null) { path.append(File.separatorChar).append(subPath); }
			subPath = path.toString();
		}
		try {
			subPath = new File(subPath).getCanonicalPath();
			if (subPath.isEmpty()) { subPath = new File(subPath).getAbsolutePath(); }
		} catch(IOException e) { /* use subPath */ }
		return subPath;
	}

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

	public static String getAppFilePath(String subdir, String subPath) { // path for assets
														// same as getFilePath() but no basePath
		StringBuilder sb = new StringBuilder(AppPath);
		if (subdir != null) { sb.append(File.separatorChar).append(subdir); }
		if (subPath != null) { sb.append(File.separatorChar).append(subPath); }
		String path = sb.toString();								// path may have ".." elements AssetManager can't handle
		File file = new File(path.toString());						// get canonical path to remove them
		try { path = file.getCanonicalPath().substring(1); }		// strip leading '/' from canonical path
		catch (IOException e) { Log.w(LOGTAG, "getAppFilePath - getCanonicalPath: " + e); }
		return path;												// unmodified path if getCanonicalPath threw exception
	}

	private void initVars() {
		// Some of these may not need initialization; if so I choose to err on the side of caution
		BasicContext = getApplicationContext();

		Resources res = getResources();
		AppPath = res.getString(R.string.app_path);
		isAPK   = res.getBoolean(R.bool.is_apk);
		apkCreateDataDir     = res.getBoolean(R.bool.apk_create_data_dir);
		apkCreateDataBaseDir = res.getBoolean(R.bool.apk_create_database_dir);

		DoAutoRun = false;

		ProgramFileName = "";
		theProgramRunner = null;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);					// Set up of fresh start
		Log.v(LOGTAG, CLASSTAG + " onCreate");

		Settings.setDefaultValues(this, isAPK);				// if isAPK, force to default settings
		initVars();
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
		 * interfere with that run. We will just exit this attempt. A program running
		 * is indicated by theRunContext ! = null
		 */
		if (theRunContext != null) {
			finish();
			return;
		}
		InitDirs();											// Initialize Basic directories every time
															// The user may have put in a new SD Card

		clearProgram();										// Clear the basic program

		/* If Basic is entered from a launcher shortcut, then there will an Intent with a
		 * bundle. The bundle will contain the shortcut launcher program name.
		 * Auto run will be called with a new bundle with just the filename.
		 */
		Intent myIntent = getIntent();
		String FileName = myIntent.getStringExtra(LauncherShortcuts.EXTRA_LS_FILENAME); // Launched by shortcut?
		if (FileName == null) {								// Launched with a .bas as argument?
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
		} else if (AreSamplesLoaded()) {								// This is not a launcher short cut
			DoAutoRun = false;
			startActivity(new Intent(BasicContext, Editor.class));		// 	Goto the Editor
			finish();
		} else {														// The sample files have not been loaded
			runBackgroundLoader();							// Start the background task to load samples and graphics
		}
	}

	private void createForAPK() {										// Create code for APK
		runBackgroundLoader();
	}

	private void runBackgroundLoader() {
		// Establish an output screen so that file load progress can be shown.

		output = new ArrayList<String>();
		AA=new ArrayAdapter<String>(this, R.layout.simple_list_layout, output);	// Establish the output screen
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

	public static void clearProgram(){

		lines = new ArrayList<String>();					// The lines array list is the program
		lines.add("");										// add an empty string to lines
		Editor.DisplayText="REM Start of BASIC! Program\n";	// Display text is the editors program storage for display
	}

	private static boolean AreSamplesLoaded(){		// Sample program files have not been loaded
													// if the sample programs directory is empty
		String samplesPath = getSamplesPath(null);
		File sdDir = new File(samplesPath);
		sdDir.mkdirs();
		String FL[] = sdDir.list();

		if ((FL != null) && (FL.length != 0)) {		// if the help directory is not empty
													// then sort the files

			ArrayList<String> FL1 = new ArrayList<String>(Arrays.asList(FL));
			Collections.sort(FL1);					// Convert to array list to easily sort
			String f1 = FL1.get(0);					// The top of the list should be the
			if (f1.length() > 11) {					// f00_vnn.nn_xxx file
				String f2 = f1.substring(5, 7);
				String f3 = f1.substring(8,10);
				f1 = f2 + "." + f3;
				f2 = BasicContext.getString(R.string.version);         // Get the version string
				if (f1.equals(f2)) { return true; }		               // Compare version numbers
			}
			for (String fileName : FL) {			// If different, empty the directory
				File file = new File(samplesPath + File.separatorChar + fileName);
				file.delete();
			}
		}
		return false;
	}

	/******************************* static file/resource utilities ******************************/

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

	public static String getAlternateRawFileName(String input) {
		// Converts a file name with upper and lower case characters to a lower case filename.
		// The dot extension is appended to the end of the filename preceeded by "_".
		// Any other dots in the file are also converted to "_".

		// MyFile.png = myfile_png
		// bigSound.mp3 = bigsound_mp3
		// Earth.jpg = earth_jpg
		// Earth.blue.jpg = earth_blue_jpg

		// if there is no dot extension, returns original string

		int idx = input.lastIndexOf("/");
		return idx >= 0 ? input.substring(idx + 1).toLowerCase().replace(".", "_") : input.toLowerCase().replace(".", "_"); // Convert to lower case, convert all '.' to '_'
	}

	public static int getRawResourceID(String fileName) {
		if (fileName == null) fileName = "";
		String packageName = BasicContext.getPackageName();				// Get the package name
		int resID = 0;													// 0 is not a valid resource ID
		for (int attempt = 1; (resID == 0) && (attempt <= 2); ++attempt) {
			String rawFileName =
				(attempt == 1) ? getAlternateRawFileName(fileName) :	// Convert conventional filename to raw resource name, BASIC!-style
				(attempt == 2) ? getRawFileName(fileName) : "";			// If first try didn't work, try again, Android-style.
			if (!rawFileName.equals("")) {
				Resources res = BasicContext.getResources();
				String fullName = packageName + ":raw/" + rawFileName;	// "fully-qualified resource name"
				resID = res.getIdentifier(fullName, null, null);		// Get the resource ID
			}
		}
		return resID;
	}

	public static InputStream streamFromResource(String dir, String path) throws Exception {
		InputStream inputStream = null;
		int resID = getRawResourceID(path);
		if (resID != 0) {
			Resources res = BasicContext.getResources();				// open an input stream from raw resource
			inputStream = res.openRawResource(resID);					// this call may throw NotFoundException
		} else {
			inputStream = BasicContext.getAssets().						// open an input stream from an asset
							open(getAppFilePath(dir, path));			// this call may throw IOException
		}
		return inputStream;
	}

	public static BufferedReader getBufferedReader(String dir, String path) throws Exception {
		File file = new File((dir == null)	? path						// no dir, use path as given
											: getFilePath(dir, path));	// dir is SOURCE_DIR, DATA_DIR, etc
		BufferedReader buf = null;
		if (file.exists()) {
			try {
				buf = new BufferedReader(new FileReader(file));			// open an input stream from the file
				if (buf != null) buf.mark((int)file.length());			// this call may throw an exception
			} catch (IOException ex) {
				Log.e(LOGTAG, "getBufferedReader: " + ex);
				if (buf != null) {
					try { buf.close(); } catch (IOException e) { }		// ignore second exception, if any
				}
				throw ex;												// throw first exception
			}
		} else if (isAPK) {
			InputStream inputStream = streamFromResource(dir, path);
			if (inputStream != null) { buf = new BufferedReader(new InputStreamReader(inputStream)); }
			// TODO: how to mark a non-file buffered stream?
		}
		return buf;
	}

	public static BufferedInputStream getBufferedInputStream(String dir, String path) throws Exception {
		File file = new File((dir == null)	? path						// no dir, use path as given
											: getFilePath(dir, path));	// dir is SOURCE_DIR, DATA_DIR, etc
		BufferedInputStream buf = null;
		if (file.exists()) {
			buf = new BufferedInputStream(new FileInputStream(file));	// open an input stream from the file
			if (buf != null) buf.mark((int)file.length());				// this call can NOT throw any exception
		} else if (isAPK) {
			InputStream inputStream = streamFromResource(dir, path);	// this call may throw an exception
			if (inputStream != null) { buf = new BufferedInputStream(inputStream); }
			// TODO: how to mark a non-file buffered stream?
		}
		return buf;
	}

	public static int loadProgramFileToList(boolean isFullPath, String path, ArrayList<String> list) {

		// MES 2013/01/13 - We got in a jam when moving resources to assets.
		// Files and assets have a different base path. For backward compatibility,
		// we're letting callers use the full path if they only want a file.
		// If a caller wants to use an asset "file", it must set isFullPath false
		// and use a path relative to "source" so getBufferedReader can find the asset.

		int size = 0;
		BufferedReader buf = null;
		try { buf = getBufferedReader((isFullPath ? null : Basic.SOURCE_DIR), path); }
		catch (Exception e) { return size; }

		// Read the file. Insert the the lines into the ArrayList.
		String data = null;
		do {
			try { data = buf.readLine(); }
			catch (IOException e) { data = null; }
			if (data != null) {
				list.add(data);											// add the line
				size += data.length() + 1;
			}
		} while (data != null);											// while not EOF and no error

		if (list.isEmpty()){
			list.add("!");
			size = 2;
		}
		return size;
	}

	public static String loadProgramListToString(ArrayList<String> list, int size) {
		StringBuilder sb = new StringBuilder(size);
		for (String line : list)	 {									// Copy the lines to the String
			sb.append(line + '\n');
		}
		return sb.toString();
	}

																// build program in Basic.lines
	public static void loadProgramFromString(String text, AddProgramLine APL) {
		if (APL == null) { APL = new AddProgramLine(); }
		StringBuilder sb = new StringBuilder();
		int length = text.length();
		int offset = AddProgramLine.charCount;
		for (int k = 0; k < length; ++k) {
			char c = text.charAt(k);							// grab the display text
			if (c == '\n') {
				AddProgramLine.charCount = k + offset;			// and add it to Basic.Lines
				APL.AddLine(sb.toString());						// with some editing
				sb.setLength(0);
			} else {
				sb.append(c);
			}
		}
		if (sb.length() != 0) {									// if last line has no newline
			 APL.AddLine(sb.toString());						// add the line now
		}
	}

	public static IOException closeStreams(InputStream in, OutputStream out) {
		IOException ex = null;
		if (in != null) {
			try { in.close(); }
			catch (IOException e) { ex = e; }
		}
		if (out != null) {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				//Log.v(LOGTAG, CLASSTAG + " I/O Exception 4");
				if (ex == null) { ex = e; }
			}
		}
		return ex;
	}

	public static IOException closeStreams(Reader in, Writer out) {
		IOException ex = null;
		if (in != null) {
			try { in.close(); }
			catch (IOException e) { ex = e; }
		}
		if (out != null) {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				//Log.v(LOGTAG, CLASSTAG + " I/O Exception 4");
				if (ex == null) { ex = e; }
			}
		}
		return ex;
	}

	/************************************* background loader *************************************/

	// The loading of the sample files and graphics is done in a background AsyncTask rather than the UI task
	// Progress is shown by sending progress messages to the UI task.

	public class Background extends AsyncTask<String, String, String>{

		private String[] mLoadingMsg;						// Displayed while files are loading
		private String mProgressMarker;						// Displayed as a unit of progress while files are loading
		private Resources mRes;

		@Override
		protected void onPreExecute() {
			mRes = BasicContext.getResources();
			mLoadingMsg = mRes.getStringArray(R.array.loading_msg);
			mProgressMarker = mRes.getString(R.string.progress_marker);
			if ((mLoadingMsg != null) && (mLoadingMsg.length != 0)) {
				for (String s : mLoadingMsg) { output.add(s); }			// The message lines that tell what we are doing.
			}
		}

		private final int CHAR_PER_LINE = 20;							// Number of dots per line
		private final int LINES_PER_UPDATE = 30;						// Number of line between Progress Messages
		private int maxUpdateCount = 0;									// Change to maximum number of progress messages, if known.
		private int lineCount = 0;

		@Override
		protected void onProgressUpdate(String... str) {				// Called when publishProgress() is executed.
			for (String s : str) {										// Form line of CHAR_PER_LINE progress characters
				int linenum = output.size() - 1;
				s = output.get(linenum) + s;
				output.set(linenum, s);

				if (s.length() >= CHAR_PER_LINE) {						// After the CHAR_PER_LINEth character
					++lineCount;										// output a new line

					if ((lineCount % LINES_PER_UPDATE) == 0) {			// After every LINES_PER_UPDATE
						StringBuilder msg = new StringBuilder();		// output a progress message

						int updates = lineCount/LINES_PER_UPDATE;
						if (maxUpdateCount != 0) {						// If the max is known
							msg.append((updates * 100)/maxUpdateCount);	// convert progress to a percent
							msg.append("%");
						} else {
							msg.append(updates);						// else just use the number of progress line
						}
						output.add("Continuing load (" + msg + ')');
					}
					output.add("");										// start a new line
				}
			}

			setListAdapter(AA);						// show the output
			lv.setSelection(output.size() - 1);		// set last line as the selected line to scroll
		}

		@Override
		protected String doInBackground(String... str) {
			if (isAPK) {
				doBGforAPK();
			} else {
				doBGforSB();
			}
			return "";
		}

		private void doBGforSB() {								// Background code for Standard Basic
			SD_ProgramPath = SAMPLES_DIR;						// This setting will also force LoadFile to show the samples directory
			if (new File(getFilePath()).exists()) {
				copyAssets(AppPath);
				doFirstLoad();									// First load shows a first load basic program
			} else {
				doCantLoad();									// Can't load: show an error message
			}
			DoAutoRun = false;
			startActivity(new Intent(BasicContext, Editor.class));	// Goto the Editor
			finish();
		}

		private void doBGforAPK() {								// Background code of APK
			InitDirs();											// Initialize Basic directories every time
			LoadGraphicsForAPK();								// Load the sound and graphics files

			lines = new ArrayList <String>();					// Program will be loaded into this array list
			LoadTheProgram();									// Load the basic program into memory

			theProgramRunner = new Intent(BasicContext, Run.class);	// now go run the program
			theRunContext = null;
			DoAutoRun = true;
			startActivity(theProgramRunner);					// The program is now running
			finish();
		}

		private void copyAssets(String path) {	// Recursively copy all the assets in the named subdirectory to the SDCard
			String[] dirs = null;
			try { dirs = getAssets().list(path); }
			catch (IOException e) { Log.e(LOGTAG, "copyAssets: " + e); }
			if (dirs == null) return;

			if (dirs.length == 0) {
				copyAssetToFile(path);
				return;
			}

			new File(getBasePath() + File.separatorChar + path).mkdirs();
			for (String dir : dirs) {
				copyAssets(path + File.separatorChar + dir);
			}
		}

		private void copyAssetToFile(String path) {
			if (path.endsWith(".bas") || path.endsWith(".txt") || path.endsWith(".html")) {
				copyTextAssetToFile(path);
			} else {
				copyBinaryAssetToFile(path);
			}
		}

		private void copyTextAssetToFile(String path) {
			BufferedReader in = null;
			BufferedWriter out = null;

			publishProgress(mProgressMarker);						// Show progress for each program loaded
			File file = new File(getBasePath() + File.separatorChar + path);
			File dir = new File(file.getParent());
			if (!dir.exists()) { dir.mkdirs(); }

			try {
				in = new BufferedReader(new InputStreamReader(getAssets().open(path)));
				out = new BufferedWriter(new FileWriter(file));
				String line;
				while ((line = in.readLine()) != null) {			// Read and write one line at a time
					line +=  "\n";
					out.write(line);
				}
			} catch (IOException e) {
				Log.w(LOGTAG, "copyTextAssetToFile: " + e);
			}
			closeStreams(in,out);
		}

		private void copyBinaryAssetToFile(String path) {
			BufferedInputStream in = null;
			BufferedOutputStream out = null;

			File file = new File(getBasePath() + File.separatorChar + path);
			byte[] bytes = new byte[8192];
			int count = 0;
			try {
				in = new BufferedInputStream(getAssets().open(path));
				out = new BufferedOutputStream(new FileOutputStream(file));
				do {
					count = in.read(bytes, 0, 8192);
					if (count > 0) {
						out.write(bytes, 0, count);
						publishProgress(mProgressMarker);
					}
				} while (count != -1);
			} catch (IOException e) {
				Log.w(LOGTAG, "copyBinaryAssetToFile: " + e);
			}
			closeStreams(in,out);
		}

		private void LoadGraphicsForAPK(){

			// Loads icons and audio to the SDcard.
			// The files to create are listed in setup.xml:load_file_names.
			// The names are relative to "<AppPath>/data/", except that
			// names ending in ".db" are relative to "<AppPath>/databases/".
			// The content is copied from res/raw/ or assets/.

			String[] loadFileNames = mRes.getStringArray(R.array.load_file_names);
			for (String fileName : loadFileNames) {
				if (fileName.equals("")) continue;

				String dir = fileName.endsWith(".db") ? DATABASES_DIR : DATA_DIR;
				Load1Graphic(dir, fileName);
			}
		}

		private void Load1Graphic(String dir, String fileName){
			BufferedInputStream in = null;
			BufferedOutputStream out = null;

			File file = new File(getFilePath(dir, fileName));
			File parent = new File(file.getParent());
			if (!parent.exists()) { parent.mkdirs(); }
			if (!parent.exists()) {
				Log.w(LOGTAG, "Load1Graphic: can not create directory " + file.getPath());
				return;
			}

			byte[] bytes = new byte[8192];
			int count = 0;
			try {
				in = new BufferedInputStream(streamFromResource(dir, fileName), 8192);
				out = new BufferedOutputStream(new FileOutputStream(file));
				do {
					count = in.read(bytes, 0, 8192);
					if (count > 0) {
						out.write(bytes, 0, count);
						publishProgress(mProgressMarker);
					}
				} while (count != -1);
			} catch (Exception e) {
				Log.w(LOGTAG, "Load1Graphic: " + e);
			}
			closeStreams(in, out);
		}

		public void doFirstLoad(){
			// The first load is a short program of comments that will be displayed
			// by the Editor

			Editor.DisplayText="!!\n\n" +
					"Welcome to BASIC!\n\n" +
					"Press Menu->More->About\n" +
					"to get more information\n" +
					"about this release.\n\n" +
					"The BASIC! User's Manual,\n" +
					"De Re BASIC!, can also be\n" +
					"found at that location.\n\n" +
					"Press Menu->Clear to clear\n" +
					"this message and start\n" +
					"writing your own BASIC!\n" +
					"program.\n\n"+
					"!!"
					;  // Initialize the Display Program Lines
		}

		public void doCantLoad(){
			// A short program of comments that will be displayed
			// by the Editor to indicate the Base Drive is not writable

			Editor.DisplayText="!!\n\n" +
					"BASIC! is unable to write\n" +
					"its sample programs.\n" +
					"You can write and run\n" +
					"programs, but you cannot\n" +
					"save them. You can press\n" +
					"Menu->More->Preferences\n" +
					"and select \"Base Drive\"\n" +
					"to change the setting\n" +
					"to writable storage.\n\n" +
					"!!";
		}

		private void LoadTheProgram(){

			// Reads the program file from res/raw or assets/<AppPath>/source and puts it into memory
			AddProgramLine APL = new AddProgramLine();
			String name = mRes.getString(R.string.my_program);
			InputStream inputStream = null;
			try { inputStream = streamFromResource(SOURCE_DIR, name); }
			catch (Exception ex) {											// If not found, give up
				Log.e(LOGTAG, "LoadTheProgram - error getting stream from resource: " + ex);
				return;
			}

			BufferedReader buffreader = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			int count = 0;

			try {
				while ((line = buffreader.readLine()) != null) {			// Read and write one line at a time
					APL.AddLine(line);										// add the line to memory
					++count;
					if (count >= 200) {										// Show progress every 200 lines.
						publishProgress(mProgressMarker);
						count = 0;
					}
				}
			} catch (IOException e) {
				Log.e(LOGTAG, "LoadTheProgram - error reading, about line " + count + ": " + e);
			} finally {
				if (buffreader != null) {
					try { buffreader.close(); }
					catch (IOException e) {}
				}
			}
		}
	} // class Background

	/************************************** utility classes **************************************/

	public static class ScreenColors {
		public int textColor;
		public int backgroundColor;
		public int lineColor;

		public ScreenColors() {
			// By default, color1 is solid black, color2 is solid white,
			// and color3 is a shade of blue that Paul originally chose for "WBL".
			// The programmer may define the colors any way she likes in res/values/setup.xml.
			Resources res = BasicContext.getResources();
			int black = res.getInteger(R.integer.color1);
			int white = res.getInteger(R.integer.color2);
			int blue  = res.getInteger(R.integer.color3);
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
				lineColor = black;
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
		public int lineColor;

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
			lineColor = colors.lineColor;
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
