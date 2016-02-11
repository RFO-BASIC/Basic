/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

This file is part of BASIC! for Android

Copyright (C) 2010 - 2015 Paul Laughton

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
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;


public class Basic extends Activity {

	private static final String LOGTAG = "Basic";

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

	public static ArrayList<Run.ProgramLine> lines;			// Program lines for execution

	public static ContextManager mContextMgr = null;
	public static String mBasicPackage = "";				// not valid but not null

	private TextView mProgressText;
	private Dialog mProgressDialog;
	private ImageView mSplash;								// ImageView for splash screen

	public static TextStyle defaultTextStyle;

	public static boolean checkSDCARD(char mount) {			// mount is 'w' for writable,
															// 'r' for either readable or writable
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) { return true; }	// mounted for both read and write
		if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state) && (mount == 'r')) { return true; }
		return false;
	}

	public static void setFilePaths(String basePath) {		// set both basePath and filePath
															// public so LauncherShortcuts can use it
		if (basePath.equals("none")) {
			basePath = Environment.getExternalStorageDirectory().getPath();
		}
		Basic.basePath = basePath;
		Basic.filePath = new File(basePath, AppPath).getPath();	// add AppPath and fix slashes
	}

	public static String getBasePath() {
		return basePath;
	}

	public static String getFilePath() {
		return filePath;
	}

	// A "" parameter adds a file separator. A null argument does not.
	public static String getFilePath(String subdir, String subPath) {
		String path = subPath;
		// if (!subPath.startsWith("/"))	uncomment to enable absolute paths
		{
			StringBuilder bldPath = new StringBuilder(filePath);
			if (subdir != null) { bldPath.append(File.separatorChar).append(subdir); }
			if (subPath != null) { bldPath.append(File.separatorChar).append(subPath); }
			path = bldPath.toString();
		}
		File file = new File(path);
		try                  { path = file.getCanonicalPath(); }
		catch(IOException e) { path = file.getAbsolutePath(); }
		return path;
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

	public static ContextManager getContextManager() {
		return mContextMgr;
	}

	public static void clearContextManager() {			// unregister Run-related Contexts
		mContextMgr.clearProgramContexts();				// but keep ACTIVITY_APP context
	}

	private void initVars() {
		// Some of these may not need initialization; if so I choose to err on the side of caution
		Context appContext = getApplicationContext();
		mContextMgr = new ContextManager(appContext);
		mBasicPackage = appContext.getPackageName();

		Resources res = getResources();
		AppPath = res.getString(R.string.app_path);
		isAPK   = res.getBoolean(R.bool.is_apk);
		apkCreateDataDir     = res.getBoolean(R.bool.apk_create_data_dir);
		apkCreateDataBaseDir = res.getBoolean(R.bool.apk_create_database_dir);

		DoAutoRun = false;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);					// Set up of fresh start
		Log.v(LOGTAG, "onCreate: " + this);

		initVars();
		Settings.setDefaultValues(this, isAPK);				// if isAPK, force to default settings

		defaultTextStyle = new TextStyle();

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
		 * is indicated by Run context != null.
		 */
		if (mContextMgr.getContext(ContextManager.ACTIVITY_RUN) != null) {
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
		String FileName = myIntent.getStringExtra(LauncherShortcuts.EXTRA_LS_FILENAME);	// Launched by shortcut?
		Bundle savedState = myIntent.getBundleExtra(Editor.EXTRA_RESTART);				// Restart from editor?
		if (FileName == null) {								// Launched with a .bas as argument?
			if (myIntent.getData() != null) FileName = myIntent.getData().getPath();
		}

		if ((FileName != null) && !FileName.equals("")) {				// launched by shortcut or as a file share intent
			AutoRun autoRun = new AutoRun(this, FileName, false, null);
			Intent intent = autoRun.load();								// load the program
			DoAutoRun = true;
			startActivity(intent);										// run the program
			finish();
		} else if (AreSamplesLoaded()) {								// this is not a launcher short cut
			DoAutoRun = false;
			Intent intent = new Intent(this, Editor.class);
			if (savedState != null) {									// if restarted by Editor
				intent.putExtra(Editor.EXTRA_RESTART, savedState);		// send saved state back
			}
			startActivity(intent);										// to the Editor
			finish();
		} else {														// The sample files have not been loaded
			runBackgroundLoader();							// Start the background thread to load samples and graphics
		}
	}

	private void createForAPK() {										// Create code for APK
		runBackgroundLoader();
	}

	private void runBackgroundLoader() {

		setContentView(R.layout.splash);		// always have a splash screen but sometimes it is blank
		mSplash = (ImageView)findViewById(R.id.splash);

		Resources res = getResources();
		boolean displaySplash = res.getBoolean(R.bool.splash_display);	// Display splash screen?
		int imageID = R.drawable.blank;
		if (displaySplash) {
			int id = res.getIdentifier("splash", "drawable", getPackageName());
			if (id > 0) { imageID = id; }						// if a splash image exists use it
		}
		mSplash.setImageResource(imageID);

		mProgressText = new TextView(this);						// Create dialog for displaying load progress
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		mProgressDialog = builder.setView(mProgressText).setCancelable(false).setIcon(R.drawable.icon).create();

		// Loading can take a while, so we have to start a background thread to do it.
		// The background thread will make calls to the UI thread to show the progress.
		// Once the files are loaded, the Loader will start the loaded program running.

		(new Loader()).execute("");								// Start thread to load samples and graphics

	}

	public static void InitDirs() {

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

	public static void clearProgram() {

		lines = new ArrayList<Run.ProgramLine>();			// The lines array list is the program
		lines.add(new Run.ProgramLine(""));					// add an empty string to lines
		Editor.DisplayText="REM Start of BASIC! Program\n";	// Display text is the editors program storage for display
	}

	private static boolean AreSamplesLoaded() {		// Sample program files have not been loaded
													// if the sample programs directory is empty
		String samplesPath = getSamplesPath(null);
		File sdDir = new File(samplesPath);
		sdDir.mkdirs();
		String FL[] = sdDir.list();

		if ((FL != null) && (FL.length != 0)) {		// if the help directory is not empty
													// then sort the files
			ArrayList<String> FL1 = new ArrayList<String>(Arrays.asList(FL));
			Collections.sort(FL1);					// Convert to array list to easily sort
			String f0 = FL1.get(0);					// The top of the list should be the
			if (f0.length() > 11) {					// f00_vnn_nn_xxx file
				String[] f = f0.substring(5).split("_");
				if (f.length > 1) {					// keep "0x.xx" of version number
					Context appContext = mContextMgr.getContext(ContextManager.ACTIVITY_APP);
					String version = appContext.getString(R.string.version).substring(0,5);
					if (version.equals(f[0] + "." + f[1])) {	// Compare version numbers
						return true;				// Versions match, correct files are loaded
					}
				}
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
		String output = input.toLowerCase(Locale.getDefault());	// Convert to lower case
		int index = output.indexOf(".");						// Find the dot
		if (index == -1) return output;							// if no dot, return as is
		return output.substring(0, index);						// else isolate stuff in front of dot
	}

	public static String getAlternateRawFileName(String input) {
		// Converts a file name with upper and lower case characters to a lower case filename.
		// The dot extension is appended to the end of the filename preceded by "_".
		// Any other dots in the file are also converted to "_".

		// MyFile.png = myfile_png
		// bigSound.mp3 = bigsound_mp3
		// Earth.jpg = earth_jpg
		// Earth.blue.jpg = earth_blue_jpg

		// if there is no dot extension, returns original string

		Locale locale = Locale.getDefault();
		int idx = input.lastIndexOf("/");
		return idx >= 0 ? input.substring(idx + 1).toLowerCase(locale).replace(".", "_")
						: input.toLowerCase(locale).replace(".", "_");	// Convert to lower case, convert all '.' to '_'
	}

	public static int getRawResourceID(String fileName) {
		if (fileName == null) fileName = "";
		int resID = 0;													// 0 is not a valid resource ID
		for (int attempt = 1; (resID == 0) && (attempt <= 2); ++attempt) {
			String rawFileName =
				(attempt == 1) ? getAlternateRawFileName(fileName) :	// Convert conventional filename to raw resource name, BASIC!-style
				(attempt == 2) ? getRawFileName(fileName) : "";			// If first try didn't work, try again, Android-style.
			if (!rawFileName.equals("")) {
				Context appContext = mContextMgr.getContext(ContextManager.ACTIVITY_APP);
				Resources res = appContext.getResources();
				String fullName = mBasicPackage + ":raw/" + rawFileName;// "fully-qualified resource name"
				resID = res.getIdentifier(fullName, null, null);		// Get the resource ID
			}
		}
		return resID;
	}

	public static InputStream streamFromResource(String dir, String path) throws Exception {
		Context appContext = mContextMgr.getContext(ContextManager.ACTIVITY_APP);
		InputStream inputStream = null;
		int resID = getRawResourceID(path);
		if (resID != 0) {
			Resources res = appContext.getResources();					// open an input stream from raw resource
			inputStream = res.openRawResource(resID);					// this call may throw NotFoundException
		} else {
			inputStream = appContext.getAssets().						// open an input stream from an asset
							open(getAppFilePath(dir, path));			// this call may throw IOException
		}
		return inputStream;
	}

	public static BufferedReader getBufferedReader(String dir, String path, boolean enableDecryption) throws Exception {
		File file = new File((dir == null)	? path						// no dir, use path as given
											: getFilePath(dir, path));	// dir is SOURCE_DIR, DATA_DIR, etc
		BufferedReader buf = null;
		if (file.exists()) {
			buf = new BufferedReader(new FileReader(file));				// open an input stream from the file
		} else if (isAPK) {
			InputStream inputStream = streamFromResource(dir, path);
			if (inputStream != null) {
				Context appContext = mContextMgr.getContext(ContextManager.ACTIVITY_APP);
				Resources res = appContext.getResources();
				if (enableDecryption & res.getBoolean(R.bool.apk_programs_encrypted)) {
					inputStream = getDecryptedStream(inputStream);
				}
				buf = new BufferedReader(new InputStreamReader(inputStream));
			}
		}
		return buf;
	}

	public static BufferedInputStream getBufferedInputStream(String dir, String path) throws Exception {
		File file = new File((dir == null)	? path						// no dir, use path as given
											: getFilePath(dir, path));	// dir is SOURCE_DIR, DATA_DIR, etc
		BufferedInputStream buf = null;
		if (file.exists()) {
			buf = new BufferedInputStream(new FileInputStream(file));	// open an input stream from the file
		} else if (isAPK) {
			InputStream inputStream = streamFromResource(dir, path);	// this call may throw an exception
			if (inputStream != null) { buf = new BufferedInputStream(inputStream); }
		}
		return buf;
	}

	public static InputStream getDecryptedStream(InputStream inputStream) throws Exception {
		// Decrypt program that was encrypted with PBEWithMD5AndDES
		String PW = mBasicPackage;
		Cipher cipher = new Basic.Encryption(Cipher.DECRYPT_MODE, PW).cipher();
		return new CipherInputStream(inputStream, cipher);
	}

	public static int loadProgramFileToList(boolean isFullPath, String path, ArrayList<String> list) {

		// MES 2013/01/13 - We got in a jam when moving resources to assets.
		// Files and assets have a different base path. For backward compatibility,
		// we're letting callers use the full path if they only want a file.
		// If a caller wants to use an asset "file", it must set isFullPath false
		// and use a path relative to "source" so getBufferedReader can find the asset.

		int size = 0;
		BufferedReader buf = null;
		String dir = isFullPath ? null : Basic.SOURCE_DIR;
		try { buf = getBufferedReader(dir, path, Basic.Encryption.ENABLE_DECRYPTION); }
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

		if (list.isEmpty()) {
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
			AddProgramLine.charCount = length + offset;			// add the line now
			APL.AddLine(sb.toString());
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
				//Log.v(LOGTAG, "I/O Exception 4");
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
				//Log.v(LOGTAG, "I/O Exception 4");
				if (ex == null) { ex = e; }
			}
		}
		return ex;
	}

	/************************************* background loader *************************************/

	// The loading of the sample files and graphics is done in a background AsyncTask rather than the UI thread.
	// Progress is shown by sending progress messages to the UI thread.

	public class Loader extends AsyncTask<String, String, String>{

		private String mProgressMarker;						// Displayed as a unit of progress while files are loading
		private boolean mDisplayProgress;
		private Resources mRes;
		private int mUpdates;
		private int maxUpdateCount = 0;						// Change to maximum number of progress messages, if known.

		@Override
		protected void onPreExecute() {
			mRes = Basic.this.getResources();
			String[] loadingMsg = mRes.getStringArray(R.array.loading_msg);	// Displayed while files are loading
			mProgressMarker = mRes.getString(R.string.progress_marker);
			mDisplayProgress = (loadingMsg != null) && (loadingMsg.length != 0);
			if (!mDisplayProgress) { return; }

			String title = loadingMsg[0];
			int last = loadingMsg.length - 1;
			for (int m = 1; m < last; ++m) {
				title += '\n' + loadingMsg[m];
			}
			if ((last > 0) && (loadingMsg[last].length() > 0)) {
				title += '\n' + loadingMsg[last];
			}
			mProgressDialog.setTitle(title);
			mProgressDialog.show();
			// mProgressText.setGravity(Gravity.CENTER_HORIZONTAL);
			mProgressText.setText("");

			mUpdates = 0;
		}

		@Override
		protected void onProgressUpdate(String... str) {				// Called when publishProgress() is executed.
			if (!mDisplayProgress) return;

			++mUpdates;
			for (String s : str) {
				s = mProgressText.getText() + s;
				mProgressText.setText(s);
			}
			int h = mProgressText.getHeight();
			int maxh = mSplash.getHeight() * 2 / 3;
			if (h > maxh) {
				StringBuilder msg = new StringBuilder("Continuing load (");
				if (maxUpdateCount != 0) {						// If the max is known
					msg.append((mUpdates * 100)/maxUpdateCount);// convert progress to a percent
					msg.append('%');
				} else {
					msg.append(mUpdates);						// else just use the number of progress line
				}
				msg.append(")\n");
				mProgressText.setText(msg);
			}
		}

		@Override
		protected String doInBackground(String... str) {
			Intent doNext = (isAPK) ? doBGforAPK() : doBGforSB();
			startActivity(doNext);			// Start program (APK) or the Editor (SB)
			if (mProgressDialog != null) { mProgressDialog.dismiss(); }
			finish();
			return "";
		}

		private Intent doBGforSB() {								// Background code for Standard Basic
			if (new File(getFilePath()).exists()) {
				copyAssets(AppPath);
				doFirstLoad();									// First load shows a first load basic program
			} else {
				doCantLoad();									// Can't load: show an error message
			}
			DoAutoRun = false;
			Intent intent = new Intent(Basic.this, Editor.class);
			// This LOADPATH setting will also force LoadFile to show the samples directory
			intent.putExtra(Editor.EXTRA_LOADPATH, SAMPLES_DIR);// start in Sample_Programs
			return intent;										// go to the Editor
		}

		private Intent doBGforAPK() {								// Background code of APK
			long startTime = System.currentTimeMillis();		// for splash screen timing
			InitDirs();											// Initialize Basic directories every time
			LoadGraphicsForAPK();								// Load the sound and graphics files

			lines = new ArrayList<Run.ProgramLine>();			// Program will be loaded into this array list
			LoadTheProgram();									// Load the basic program into memory

			if (mRes.getBoolean(R.bool.splash_display)) {		// if displaying splash screen, enforce minimum duration
				// This code was provided by forum user Luca_G, posted 2015/06/02.
				int splashTime = mRes.getInteger(R.integer.splash_time);
				int delay = splashTime - (int)(System.currentTimeMillis() - startTime);
				if (delay > 1) {
					try { Thread.sleep(delay); }
					catch (InterruptedException ex) { Thread.currentThread().interrupt(); }
				}
			}
			DoAutoRun = true;
			return new Intent(Basic.this, Run.class);			// Go run the program
		}

		private void copyAssets(String path) {	// Recursively copy all the assets in the named subdirectory to the SDCard
			String[] dirs = null;
			try { dirs = getAssets().list(path); }				// Get the names of subdirectories and files in this directory
			catch (IOException e) { Log.e(LOGTAG, "copyAssets: " + e); }
			if (dirs == null) return;

			if (dirs.length == 0) {
				copyAssetToFile(path);							// File, not directory. Copy it.
				return;
			}

			new File(getBasePath() + File.separatorChar + path).mkdirs();	// Create subdirectory to copy into
			for (String dir : dirs) {
				copyAssets(path + File.separatorChar + dir);	// Step into the new subdirectory
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
			try {													// Open the asset and copy it to the file
				in = new BufferedInputStream(getAssets().open(path));
				out = new BufferedOutputStream(new FileOutputStream(file));
				do {
					count = in.read(bytes, 0, 8192);
					if (count > 0) {
						out.write(bytes, 0, count);
						publishProgress(mProgressMarker);			// Show progress for each chunk, up to 8K
					}
				} while (count != -1);
			} catch (IOException e) {
				Log.w(LOGTAG, "copyBinaryAssetToFile: " + e);
			}
			closeStreams(in,out);
		}

		private void LoadGraphicsForAPK() {

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

		private void Load1Graphic(String dir, String fileName) {
			BufferedInputStream in = null;
			BufferedOutputStream out = null;

			File file = new File(getFilePath(dir, fileName));	// path must name a file
			File parent = new File(file.getParent());			// get the directory the path is in
			if (!parent.exists()) { parent.mkdirs(); }			// create the directory if it does not exist
			if (!parent.exists()) {
				Log.w(LOGTAG, "Load1Graphic: can not create directory " + file.getPath());
				return;
			}

			byte[] bytes = new byte[8192];						// copy the file
			int count = 0;
			try {												// source may be either a resource or an asset
				in = new BufferedInputStream(streamFromResource(dir, fileName), 8192);
				out = new BufferedOutputStream(new FileOutputStream(file));
				do {
					count = in.read(bytes, 0, 8192);
					if (count > 0) {
						out.write(bytes, 0, count);
						publishProgress(mProgressMarker);		// show progress for each chunk, up to 8K
					}
				} while (count != -1);
			} catch (Exception e) {
				Log.w(LOGTAG, "Load1Graphic: " + e);
			}
			closeStreams(in, out);
		}

		public void doFirstLoad() {
			// The first load is a short program of comments that will be displayed
			// by the Editor

			Editor.DisplayText = "!!\n\n" +			// Initialize the Display Program Lines
					"Welcome to BASIC!\n\n" +
					"Press Menu->More->About\n" +
					"to get more information\n" +
					"about this release, and\n" +
					"to see the User's Manual,\n" +
					"De Re BASIC!\n\n" +
					"Press Menu->Clear to clear\n" +
					"this message and start\n" +
					"writing your own BASIC!\n" +
					"program.\n\n";
			int level = Build.VERSION.SDK_INT;
			if (level >= 11) {
				Editor.DisplayText +=
					"Note: if you can't load a\n" +
					"program, check your settings.\n" +
					"\"Developer Options ->\n" +
					"Don't keep activities\"\n" +
					"must NOT be checked.\n\n";
			}
			Editor.DisplayText += 
					"!!";
		}

		public void doCantLoad() {
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

		private void LoadTheProgram() {

			// Reads the program file from res/raw or assets/<AppPath>/source and puts it into memory
			AddProgramLine APL = new AddProgramLine();
			String name = mRes.getString(R.string.my_program);
			InputStream inputStream = null;
			try {
				inputStream = streamFromResource(SOURCE_DIR, name);
				if ((inputStream != null) && mRes.getBoolean(R.bool.apk_programs_encrypted)) {
					inputStream = getDecryptedStream(inputStream);
				}
			} catch (Exception ex) {										// If not found or can't decrypt, give up
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
	} // class Loader

	/************************************** utility classes **************************************/

	public static class TextStyle {
		public int mTextColor;
		public int mBackgroundColor;
		public int mLineColor;
		public int mHighlightColor;
		public float mSize;
		public Typeface mTypeface;

		public TextStyle() {				// default constructor uses setup.xml values and Preferences settings
			refresh();
		}

		public TextStyle(TextStyle from, Typeface typeface) {	// clone from, override typeface
			this(from);
			mTypeface = typeface;
		}

		public TextStyle(TextStyle from) {
			this(from.mTextColor, from.mBackgroundColor,
				 from.mLineColor, from.mHighlightColor,
				 from.mSize, from.mTypeface);
		}

		public TextStyle(int fg, int bg, int lc, int hl, float size, Typeface typeface) {
			mTextColor = fg; mBackgroundColor = bg;
			mLineColor = lc; mHighlightColor = hl;
			mSize = size; mTypeface = typeface;
		}

		public void refresh() {									// set fields from setup.xml values and Preferences settings
			Context appContext = mContextMgr.getContext(ContextManager.ACTIVITY_APP);
			getScreenColors(appContext);
			mSize = Settings.getFont(appContext);
			mTypeface = Settings.getConsoleTypeface(appContext);
		}

		public boolean getCustomColors(Context appContext, int[] colors) {
			boolean useCustom = Settings.useCustomColors(appContext);
			if (useCustom) {
				String[] prefs = Settings.getCustomColors(appContext);
				for (int i = 0; i < 4; ++i) {
					String pref = prefs[i].trim().replace("0x", "#");
					if (!pref.contains("#")) pref = "#" + pref;
					try {
						colors[i] = Color.parseColor(pref);
					} catch (IllegalArgumentException ex) {	// leave unchanged
						Log.d(LOGTAG, "getPrefColors: failed to parse <" + pref + ">");
					}
				}
			}
			return useCustom;
		}

		public void getScreenColors(Context appContext) {
			int[] colors = new int[4];

			// The programmer may define the colors in res/values/setup.xml.
			Resources res = appContext.getResources();
			colors[0] = res.getInteger(R.integer.color1);		// default is solid black
			colors[1] = res.getInteger(R.integer.color2);		// default is solid white
			colors[2] = res.getInteger(R.integer.color3);		// default is blue Paul chose for "WBL"
			colors[3] = res.getInteger(R.integer.color4);		// default is green, same in all schemes

			// The user may change the colors in Preferences.
			if (getCustomColors(appContext, colors)) {
				mTextColor = colors[1];
				mBackgroundColor = colors[2];
				mLineColor = colors[0];
			} else {
				String colorSetting = Settings.getColorScheme(appContext);
				if (colorSetting.equals("BW")) {
					mTextColor = colors[0];
					mBackgroundColor = colors[1];
					mLineColor = mTextColor;
				} else
				if (colorSetting.equals("WB")) {
					mTextColor = colors[1];
					mBackgroundColor = colors[0];
					mLineColor = mTextColor;
				} else
				if (colorSetting.equals("WBL")) {
					mTextColor = colors[1];
					mBackgroundColor = colors[2];
					mLineColor = colors[0];
				}
				mLineColor &= 0x80ffffff;		// half alpha
			}
			mHighlightColor = colors[3];
		}
	} // class ScreenColors

	public static class ColoredTextAdapter extends ArrayAdapter<String> {
		private final Activity mActivity;
		private final ArrayList<String> mList;
		private TextStyle mTextStyle;

		public ColoredTextAdapter(Activity activity, ArrayList<String> alist, TextStyle style, Typeface typeface) {
			this(activity, alist, style);
			mTextStyle.mTypeface = typeface;
		}

		public ColoredTextAdapter(Activity activity, ArrayList<String> alist, TextStyle style) {
			super(activity, Settings.getLOadapter(activity), alist);
			mActivity = activity;
			mList = alist;
			mTextStyle = style;
		}

		public int getTextColor() { return mTextStyle.mTextColor; }
		public int getBackgroundColor() { return mTextStyle.mBackgroundColor; }
		public int getLineColor() { return mTextStyle.mLineColor; }
		public int getHighlightColor() { return mTextStyle.mHighlightColor; }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View row = convertView;
			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) mActivity.getLayoutInflater();
				row = inflater.inflate(Settings.getLOadapter(mActivity), null);
			}
			TextView text = (TextView) row.findViewById(R.id.the_text);
			text.setTextColor(mTextStyle.mTextColor);
			text.setText(mList.get(position));
			if (mTextStyle.mTypeface != null) { text.setTypeface(mTextStyle.mTypeface); }
			text.setBackgroundColor(mTextStyle.mBackgroundColor);
			text.setHighlightColor(mTextStyle.mHighlightColor);

			return row;
		}
	} // class ColoredTextAdapter

	public static Toast toaster(Context context, CharSequence msg) {	// Tell the user "msg" via Toast
		if ((context == null) || (msg == null) || msg.equals("")) return null;
		Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP|Gravity.CENTER, 0, 50);			// default: short, high toast
		toast.show();
		return toast;
	}

	public static class Encryption {
		public static final boolean ENABLE_DECRYPTION = true;
		public static final boolean NO_DECRYPTION = false;

		private final static byte[] SALT = {							// 8-byte Salt
			(byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
			(byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
		};
		private final static int ITERATION_COUNT = 19;

		private Cipher mCipher = null;
		public Cipher cipher() { return mCipher; }

		public Encryption(int mode, String PW) throws Exception {
			try {
				// Create the key
				KeySpec keySpec = new PBEKeySpec(PW.toCharArray(), SALT, ITERATION_COUNT);
				SecretKey key = SecretKeyFactory.getInstance(
						"PBEWithMD5AndDES").generateSecret(keySpec);
				mCipher = Cipher.getInstance(key.getAlgorithm());

				// Prepare the parameter to the ciphers
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

				// Create the ciphers
				mCipher.init(mode, key, paramSpec);
			}
			catch (Exception e) {
				throw e;
			}
		}
	} // class Encryption
}
