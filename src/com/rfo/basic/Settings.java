/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2015 Paul Laughton

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;


// Called from Editor when user presses Menu->Settings

public class Settings extends PreferenceActivity {

	private static float  Small_font = 12;
	private static float  Medium_font = 18;
	private static float  Large_font = 24;

	public static boolean changeBaseDrive = false;

//Log.v(Settings.LOGTAG, " " + Settings.CLASSTAG + " context =  " + context);

	@Override
	protected void onCreate(Bundle savedInstanceState) {	// The method sets the initial displayed
		super.onCreate(savedInstanceState);					// checked state from the xml file
		addPreferencesFromResource(R.xml.settings);			// it does not affect the above variables
		setBaseDriveList();
	}

	@Override
	protected void onPause() {
		// Sledge-hammer approach to updating the defaultTextStyle.
		// TODO: something a little more graceful, like a change listener.
		if (Basic.defaultTextStyle != null) { Basic.defaultTextStyle.refresh(); }
		super.onPause();
	}

	public void setBaseDriveList() {
		String defaultPath = Environment.getExternalStorageDirectory().getPath();
		String entries[] = getStorageDirectories(defaultPath);
		String values[] = entries.clone();

		final String sdcard = "/sdcard";
		String sdcardPath;
		try { sdcardPath = new File(sdcard).getCanonicalPath(); }
		catch (IOException e) { sdcardPath = ""; }
		if (entries[0].equals(sdcardPath)) { entries[0] = sdcard; }

		PreferenceManager PM = getPreferenceManager();
		ListPreference baseDrivePref = (ListPreference) PM.findPreference("base_drive_pref");
		baseDrivePref.setEntries(entries);
		baseDrivePref.setEntryValues(values);

		String value = getBaseDrive(getApplicationContext());
		if (value.equals("none")) {
			baseDrivePref.setValueIndex(0);
		}
	}

	private static String[] getStorageDirectories(String defaultPath)
	{
		defaultPath = Environment.getExternalStorageDirectory().getPath();
		ArrayList <String> list = new ArrayList <String>();
		list.add(defaultPath);

	       BufferedReader bufReader = null;
	       try {
	           bufReader = new BufferedReader(new FileReader("/proc/mounts"));
	           String line;
	           while ((line = bufReader.readLine()) != null) {
	               if (line.contains("vfat") || line.contains("exfat") || line.contains("/mnt")) {
	                   StringTokenizer tokens = new StringTokenizer(line, " ");
	                   String s = tokens.nextToken();
	                   s = tokens.nextToken(); // Take the second token, i.e. mount point

	                   if (s.equals(defaultPath)) {
	                       continue;
	                   } else if (line.contains("/dev/block/vold")) {
	                       if (!line.contains("/mnt/secure") && !line.contains("/mnt/asec") && !line.contains("/mnt/obb") && !line.contains("/dev/mapper") && !line.contains("tmpfs")) {
	                           list.add(s);
	                       }
	                   }
	               }
	           }
	       }
	       catch (FileNotFoundException e) {}
	       catch (IOException e) {}
	       finally {
	           if (bufReader != null) {
	               try { bufReader.close(); }
	               catch (IOException e) {}
	           }
	       }

		String[] dirs = new String[list.size()];
		return list.toArray(dirs);
	}

	   @Override
	   public boolean onKeyUp(int keyCode, KeyEvent event) {						// If back key pressed
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		       finish();
		       return false;
		    }
		    return super.onKeyUp(keyCode, event);

	   }

	@Override
	public boolean onPreferenceTreeClick (PreferenceScreen preferenceScreen, Preference preference) {
		String title = preference.getTitle().toString();
		changeBaseDrive |= title.equals("Base Drive");
		return false;
	}

	public static void setDefaultValues(Context context, boolean force) {
		if (force) {
			SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
			pref.edit().clear().commit();
		}
		PreferenceManager.setDefaultValues(context, R.xml.settings, force);
	}

	public static String getBaseDrive(Context context) {
		String baseDrive = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("base_drive_pref", "none");
		return baseDrive;
	}

	public static float getFont(Context context) {

		String font = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("font_pref", "Medium");

		if (font.equals("Small")) return Small_font;
		if (font.equals("Medium")) return Medium_font;
		return Large_font;
	}

	public static int  getLOadapter(Context context) {
		String font = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("font_pref", "Medium");

		if (font.equals("Small")) return R.layout.simple_list_layout_s;
		if (font.equals("Medium")) return R.layout.simple_list_layout_m;
		return R.layout.simple_list_layout_l;
	}

	public static Typeface getConsoleTypeface(Context context) {
		String font = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("csf_pref", "MS");

		if (font.equals("MS")) return Typeface.MONOSPACE;
		if (font.equals("SS")) return Typeface.SANS_SERIF;
		if (font.equals("S")) return Typeface.SERIF;
		return Typeface.MONOSPACE;
	}

	public static boolean getConsoleMenu(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("console_menu", true);
	}

	public static boolean getLinedConsole(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("lined_console", true);
	}

	public static boolean getLinedEditor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("lined_editor", true);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static boolean menuItemsToActionBar(Context context, Menu menu) {
		if (menu == null) return false;				// no action needed
		if (Build.VERSION.SDK_INT < 11) return false;

		int[][] idMap = {
			{ R.string.pref_MAB_run_key,    R.id.run    },
			{ R.string.pref_MAB_load_key,   R.id.load   },
			{ R.string.pref_MAB_save_key,   R.id.save   },
			{ R.string.pref_MAB_clear_key,  R.id.clear  },
			{ R.string.pref_MAB_search_key, R.id.search },
			{ R.string.pref_MAB_exit_key,   R.id.exit   },
		};
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Resources res = context.getResources();
		for (int[] ids : idMap) {
			MenuItem item = menu.findItem(ids[1]);
			String key = res.getString(ids[0]);
			int action = prefs.getBoolean(key, false)
				? MenuItem.SHOW_AS_ACTION_IF_ROOM : MenuItem.SHOW_AS_ACTION_NEVER;
			item.setShowAsAction(action);
		}
		return true;
	}

	public static boolean getEditorLineWrap(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("wrap_editor", true);
	}

	public static boolean getAutoIndent(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("autoindent", false);
	}

	public static boolean getGraphicAcceleration(Context context){
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean("gr_accel", false);
	}

	public static String getEmptyConsoleColor(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("empty_color_pref", "background");
	}

	public static String getColorScheme(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString("es_pref", "BW");
	}

	public static boolean useCustomColors(Context context) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		boolean useCustom = pref.getBoolean("custom_colors_pref", false);
		if (!useCustom) {
			SharedPreferences.Editor prefEdit = pref.edit();
			Resources res = context.getResources();
			prefEdit.putString("tc_pref", String.format("%#06x", res.getInteger(R.integer.color2)));
			prefEdit.putString("bc_pref", String.format("%#06x", res.getInteger(R.integer.color3)));
			prefEdit.putString("lc_pref", String.format("%#06x", res.getInteger(R.integer.color1)));
			prefEdit.putString("hc_pref", String.format("%#06x", res.getInteger(R.integer.color4)));
			prefEdit.commit();
		}
		return useCustom;
	}

	public static String[] getCustomColors(Context context) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		String[] colors = new String[4];				// use same mapping as "WBL"
		colors[0] = pref.getString("lc_pref", "");		// color1
		colors[1] = pref.getString("tc_pref", "");		// color2
		colors[2] = pref.getString("bc_pref", "");		// color3
		colors[3] = pref.getString("hc_pref", "");		// color4 (highlight)
		return colors;
	}

	public static int getSreenOrientation(Context context) {
		String SO = PreferenceManager.getDefaultSharedPreferences(context)
				.getString("so_pref", "0");

		int RV = ActivityInfo.SCREEN_ORIENTATION_SENSOR;	// value "0", default
		if      (SO.equals("1")) RV = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		else if (SO.equals("2")) RV = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
		else if (SO.equals("3")) RV = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		else if (SO.equals("4")) RV = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;

		return RV;
	}

}

