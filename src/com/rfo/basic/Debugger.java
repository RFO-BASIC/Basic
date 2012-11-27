package com.rfo.basic;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.ReportingInteractionMode;


import android.app.Application;

/* This code was created by the Android development team. 
 * When BASIC! crashes, it will send a fairly complete crash
 * report to a Google Docs spread sheet. This report can then
 * be examined for debug purposes. 
 * 
 * See http://acra.ch/ for more details
 * 
 */
//@ReportsCrashes(formKey = "dDQ4cXhUQjdJbTZrUzlUN3NsWDctdWc6MQ" ,
@ReportsCrashes(formKey = "dGY1MVdrZUljVmxUelZCekJyUkwwQ3c6MQ" ,
		mode = ReportingInteractionMode.NOTIFICATION,
//		forceCloseDialogAfterToast = true,
      resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resNotifTickerText = R.string.crash_notif_ticker_text,
        resNotifTitle = R.string.crash_notif_title,
        resNotifText = R.string.crash_notif_text,
        resNotifIcon = android.R.drawable.stat_notify_error, // optional. default is a warning sign
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast // optional. displays a Toast message when the user accepts to send a report.
        )
public class Debugger extends Application {
	
    @Override
    public void onCreate() {
        // The following line triggers the initialization of ACRA
        ACRA.init(this);
        super.onCreate();
    }
}

