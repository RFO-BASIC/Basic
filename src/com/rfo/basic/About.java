/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

This file is part of BASIC! for Android

Copyright (C) 2010 - 2017 Paul Laughton

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


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class About extends Activity {
	private static final String LOGTAG = "About";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.about);
		final String version = "v" + getString(R.string.version);

		TextView tv1 = (TextView)findViewById(R.id.about_tv1);
		tv1.setText(getString(R.string.about_text1, version));

		TextView tv2 = (TextView)findViewById(R.id.about_tv2);
		tv2.setText(getString(R.string.about_text2));

		setupButton(R.id.about_btn_home,     "http://rfo-basic.com");
		setupButton(R.id.about_btn_forum,    "http://rfobasic.freeforums.org/index.php?mobile=mobile");
		setupButton(R.id.about_btn_programs, "http://laughton.com/basic/programs");
		String url =			// add version to the URL
			"https://bintray.com/rfo-basic/android/RFO-BASIC/"+ version + "/view/read";
		setupButton(R.id.about_btn_bintray,  url);
		setupButton(R.id.about_btn_github,   "https://github.com/RFO-BASIC");
		setupButton(R.id.about_btn_license,  "http://laughton.com/basic/license.html");
		setupButton(R.id.about_btn_privpol,  "http://rfo-basic.com/PrivacyPolicy.html");
	}

	private void setupButton(int id, final String url) {
		Button btn = (Button)findViewById(id);
		btnSetText(btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(url));
				startActivity(i);
			}
		});
	}

	private void btnSetText(Button btn) {
		String btnText = btn.getText().toString();
		int pos = btnText.indexOf('\n');
		if (pos > 0) {
			// String has a two lines. Make the second line smaller.
			int len = btnText.length();
			Spannable span = new SpannableString(btnText);
			span.setSpan(new RelativeSizeSpan(1.0f), 0, pos, 0);
			span.setSpan(new RelativeSizeSpan(0.7f), pos, len, 0);
			btn.setText(span);
		}
	}
}
