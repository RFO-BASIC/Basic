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

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;


//Log.v(Select.LOGTAG, " " + Select.CLASSTAG + " String Var Value =  " + d);

/* The User Interface used to select items from a list.
 * The user is presented with a list of things from which
 * she can select a thing.
 */

public class Select extends ListActivity {
    private static final String LOGTAG = "Select";

    private static final String CLASSTAG = Select.class.getSimpleName();
    private static ListView lv ;
    
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
  if (Run.SelectList == null){
	  Run.SelectList = new ArrayList<String>();
	  Run.SelectList.add("System Problem");
	  Run.SelectList.add("Please contact developer");
	  Run.SelectList.add("basic@laughton.com");
  }
  setListAdapter(new ColoredTextAdapter(this,  Run.SelectList));
  lv = getListView();
  lv.setTextFilterEnabled(false);
  setTitle(Run.SelectMessage);
  
  if (Settings.getEditorColor(this).equals("BW")){
	    lv.setBackgroundColor(0xffffffff);
} else
  if (Settings.getEditorColor(this).equals("WB")){
	  lv.setBackgroundColor(0xff000000);
} else
    if (Settings.getEditorColor(this).equals("WBL")){
    	  lv.setBackgroundColor(0xff006478);
}             	
  
  lv.setOnItemLongClickListener(new OnItemLongClickListener(){
	  public boolean  onItemLongClick(AdapterView<?> parent, View view, int position, long id){
	    	Run.SelectedItem = position + 1;    // Set item selected base 1
	    	Run.ItemSelected = true;            // Tell Run, which is waiting, that we have the selection
	    	Run.SelectLongClick = true;
	    	finish();                           // Done
	    	return true;
		}
  });

// Wait for user to select something
  
  lv.setOnItemClickListener(new OnItemClickListener() {			// when user taps a filename line
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    	Run.SelectedItem = position + 1;    // Set item selected base 1
    	Run.ItemSelected = true;            // Tell Run, which is waiting, that we have the selection
    	Run.SelectLongClick = false;
    	finish();                           // Done
    }
    
  });
  
  Toaster(Run.SelectMessage);  // Display the user's toast

}

@Override
public boolean onKeyUp(int keyCode, KeyEvent event)  {                     // If user presses back key
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {	 // go back to Run without a selection

    	Run.SelectedItem = 0;            // Zero indicates no selction made
    	Run.ItemSelected = true;         // Tell Run, which is waiting, that we are done.
    	
    	finish();
        return true;
    }
    return super.onKeyUp(keyCode, event);

}


	
    private void Toaster(CharSequence msg){						// Tell the user "msg"
		  Context context = getApplicationContext();			// via toast
		  if (msg == null) return;
		  CharSequence text = msg;
		  int duration = Toast.LENGTH_SHORT;
		  Toast toast = Toast.makeText(context, text, duration);
		  if (!msg.equals(""))									// If the message is blank, don't toast
			  toast.show();
  }
 
}
