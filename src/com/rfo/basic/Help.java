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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


// Called from Editor when user selected Menu->Help
// Displays Help text residing in res.values.strings

public class Help extends ListActivity {
	public static ColoredTextAdapter AA;
	public  static ListView lv;
	ArrayList <String> output = new ArrayList<String>();
	public static final String Chars = "abcdefghijklmnopqrstuvwxyz";
    public static InputMethodManager IMM;
	
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
                text.setTypeface(Typeface.MONOSPACE);
                text.setBackgroundColor(backgroundColor);

                return row;
        }
    }


	
   @Override
   protected void onCreate(Bundle savedInstanceState) {   // Help text located in R.Strings
      super.onCreate(savedInstanceState);
//      setContentView(R.layout.help);					  // which is pointed to from Help Layout
      AA= new ColoredTextAdapter(this,  output);
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
	setRequestedOrientation(Settings.getSreenOrientation(this));

	Resources res = getResources();
	int ResId = res.getIdentifier("f01_commands", "raw", Basic.BasicPackage);
	InputStream inputStream = res.openRawResource(ResId);
    InputStreamReader inputreader = new InputStreamReader(inputStream);
    BufferedReader buffreader = new BufferedReader(inputreader, 8192);
    String line;
    
    try {
    	while (( line = buffreader.readLine()) != null) {			 // Read and write one line at a time
    		output.add(line);
    	}
    } catch (IOException e) {
    }
    
    setListAdapter(AA);
    
    lv.setOnItemClickListener(new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        															// If a line is clicked on
        String command = output.get(position);						// Get the command
        int index = command.lastIndexOf (",");						// trim off the page number
        if (index >= 0) command = command.substring(0, index);   					
        
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		clipboard.setText(command);                  // Put the user expression into the clipboard
		finish();									 // Exit back to Editor

        }
        
      });

    IMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
    IMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
   }
   
   
   public boolean onKeyUp(int keyCode, KeyEvent event)  {
	   
	    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
		       finish();
		       return true;
		    }

	   int n;
	   char theChar;
	   String theText;
	   int index = 0;
	   
	   if (keyCode >=29 && keyCode <= 54){
	    	n = keyCode -29;
	    	theChar = Chars.charAt(n);
	    	
	    	for (int i = 0; i < output.size(); ++i){
	    		String line = output.get(i);
	    		char c = line.charAt(0);
	    		c = Character.toLowerCase(c);
	    		if (c == theChar){
	    			lv.setSelection(i);
//	    			lv.smoothScrollToPosition(i);
	    			break;
	    		}
	    		index = index + line.length();
	    	}
	    		
	   }
	   return super.onKeyUp(keyCode, event);
   }
   


}
