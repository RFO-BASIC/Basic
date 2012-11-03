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

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.rfo.basic.R;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;


public class TGet extends Activity {
    private Button finishedButton;			// The buttons
    private EditText theTextView;		//The EditText TextView
    private int PromptIndex;
    private static String theText;
    

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event)  {
    	// if BACK key restore original text
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	Run.Stop = true;
//        	theTextView.getText().toString();
        	finish();
        	return true;
        	}
        return super.onKeyUp(keyCode, event);

    }
    

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);

       this.setContentView(R.layout.tget);  // Layouts xmls exist for both landscape or portrait modes
       
//       this.finishedButton = (Button) findViewById(R.id.finished_button);		 // The buttons
       
       this.theTextView = (EditText) findViewById(R.id.the_text);		// The text display area
       
       TextView txtInput = (TextView)findViewById(R.id.the_text);
       
       theText = "";
       for (int i = 0; i < Run.output.size(); ++ i){
    	   theText = theText + Run.output.get(i) + '\n';
       }   
       
       theText = theText + Run.TextInputString ;
       PromptIndex = theText.length();

    	   
       theTextView.setText(theText);							// The Editor's display text
       theTextView.setTypeface(Typeface.MONOSPACE);
       theTextView.setSelection(theText.length());
       if (Settings.getEditorColor(this).equals("BW")){
    	   theTextView.setTextColor(0xff000000);
    	   theTextView.setBackgroundColor(0xffffffff);
    	   theTextView.setCursorVisible(true);
       } else
         if (Settings.getEditorColor(this).equals("WB")){
        	 theTextView.setTextColor(0xffffffff);
        	 theTextView.setBackgroundColor(0xff000000);
       } else 	
           if (Settings.getEditorColor(this).equals("WBL")){
        	   theTextView.setTextColor(0xffffffff);
        	   theTextView.setBackgroundColor(0xff006478);
             }  
       
       theTextView.setTextSize(1, Settings.getFont(this));
       
       txtInput.addTextChangedListener(inputTextWatcher);
      

       txtInput.setOnKeyListener(new View.OnKeyListener() {
           public boolean onKey(View v, int keyCode, KeyEvent event) {
   	        // If the event is a key-down event on the "enter" button
   	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
   	            (keyCode == KeyEvent.KEYCODE_ENTER)) {
   	        	String s = theTextView.getText().toString();
   	        	Run.TextInputString = "";
   	        	int n = PromptIndex;
   	        	if (PromptIndex < s.length())
   	        		Run.TextInputString = s.substring(PromptIndex);
   	        	Run.HaveTextInput = true;
   	        	finish();
   	        	return true;
   	        }
           return false;
           }
       });
       
//       final EditText edittext = (EditText) findViewById(R.id.the_text);
       
/*       theTextView.setOnKeyListener(new OnKeyListener() {
    	    public boolean onKey(View v, int keyCode, KeyEvent event) {
    	        // If the event is a key-down event on the "enter" button
    	        if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
    	            (keyCode == KeyEvent.KEYCODE_ENTER)) {
    	        	String s = theTextView.getText().toString();
    	        	Run.TextInputString = "";
    	        	int n = PromptIndex;
    	        	if (PromptIndex < s.length())
    	        		Run.TextInputString = s.substring(PromptIndex);
    	        	Run.HaveTextInput = true;
    	        	finish();
    	          return true;
    	        }
    	        return false;
    	    }
       });*/
       
    }

       private TextWatcher inputTextWatcher = new TextWatcher() {
    	    public void afterTextChanged(Editable s) { }
    	    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    	        { }
    	    public void onTextChanged(CharSequence s, int start, int before, int count) {
    	    	Log.d("start ", " " + start);
    	    	Log.d("before ", " " + before);
    	    	Log.d("count ", " " + count);
    	    	if (before>0) return;
    	    	char c = s.charAt(start);
   	    		if (c=='\n') {
    	        	String s1 = theTextView.getText().toString();
    	        	Run.TextInputString = "";
    	        	int n = PromptIndex;
    	        	if (PromptIndex < s1.length())
    	        		Run.TextInputString = s1.substring(PromptIndex);
    	        	Run.HaveTextInput = true;
    	        	finish();

    	    	}
    	    }
    	};
       
    
    
}