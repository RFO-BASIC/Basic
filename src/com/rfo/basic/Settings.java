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

    You may contact the author, Paul Laughton, at basic@laughton.com
    
	*************************************************************************************************/

package com.rfo.basic;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;

// Called from Editor when user presses Menu->Settings

public class Settings extends PreferenceActivity {

	private static float  Small_font = 12;
	private static float  Medium_font = 18;
	private static float  Large_font = 24;
	   
	   
//Log.v(Settings.LOGTAG, " " + Settings.CLASSTAG + " context =  " + context);

	   @Override
	   protected void onCreate(Bundle savedInstanceState) {   // The method sets the initial displayed
	      super.onCreate(savedInstanceState);				  // checked state from the xml file
	      addPreferencesFromResource(R.xml.settings);         // it does not effect the above variables
//	      finish();
	   }
	   
	   public boolean onKeyUp(int keyCode, KeyEvent event)  {						// If back key pressed
		    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		       finish();
		       return false;
		    }
		    return super.onKeyUp(keyCode, event);

	   }
	   
	   public static float getFont(Context context) {
		      
		      String font = PreferenceManager.getDefaultSharedPreferences(context)
		            .getString("font_pref", "Medium");
		      
		      if (font.equals("Small")) return Small_font;
		      if (font.equals("Medium")) return Medium_font;
		      return Large_font;
	   }
	   
	   public static int  getLOadapter(Context context){
		      String font = PreferenceManager.getDefaultSharedPreferences(context)
	            .getString("font_pref", "Medium");
	      
	      if (font.equals("Small")) return R.layout.simple_list_layout_s;
	      if (font.equals("Medium")) return R.layout.simple_list_layout_m;
	      return R.layout.simple_list_layout_l;

		   
	   }
	   
	   public static Typeface  getConsoleTypeface(Context context){
		      String font = PreferenceManager.getDefaultSharedPreferences(context)
	            .getString("csf_pref", "Medium");
	      
	      if (font.equals("MS")) return Typeface.MONOSPACE;
	      if (font.equals("SS")) return Typeface.SANS_SERIF;
	      if (font.equals("S")) return Typeface.SERIF;
	      return Typeface.MONOSPACE;

		   
	   }
	   

	   
	   public static boolean getLinedEditor(Context context){
		   return PreferenceManager.getDefaultSharedPreferences(context)
           .getBoolean("lined_editor", true);
	   }
	   
	   public static boolean getAutoIndent(Context context){
		   return PreferenceManager.getDefaultSharedPreferences(context)
           .getBoolean("autoindent", false);
	   }

	   
	   public static boolean getLinedConsole(Context context){
		   return PreferenceManager.getDefaultSharedPreferences(context)
           .getBoolean("lined_console", true);
	   }

	   
	   public static String getEditorColor(Context context){
		   return PreferenceManager.getDefaultSharedPreferences(context)
           .getString("es_pref", "BW");
	   }
	   
	   public static int getSreenOrientation(Context context){
		   String SO = PreferenceManager.getDefaultSharedPreferences(context)
           .getString("so_pref", "0");
		   
		   int RV = 0;
		   int nSO = 0;
		   if (SO.equals("0")) nSO = 0;
		   if (SO.equals("1")) nSO = 1;
		   if (SO.equals("2")) nSO = 2;
		   if (SO.equals("3")) nSO = 3;
		   if (SO.equals("4")) nSO = 4;
		   
		   switch (nSO){
			case 0:
				RV = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
				break;
			case 1:
				RV = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
				break;
			case 2:
				RV = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
				break;
			case 3:
				RV = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
				break;
			case 4:
				RV = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
				break;
			default:
				RV = ActivityInfo.SCREEN_ORIENTATION_SENSOR;	
		}
		   
		return RV;
		   
	   }

}

