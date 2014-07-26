/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2014 Paul Laughton

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

    Apache Commons Net
    Copyright 2001-2011 The Apache Software Foundation

    This product includes software developed by
    The Apache Software Foundation (http://www.apache.org/).

*************************************************************************************************/

package com.rfo.basic;

//Log.v(LOGTAG, CLASSTAG + " Line Buffer  " + ExecutingLineBuffer);

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.Flushable;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.net.ftp.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.ByteArrayBuffer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
//import android.content.ClipData;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources.NotFoundException;
//import android.content.ClipboardManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.SensorManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.SystemClock;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.text.ClipboardManager;

import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


/* Executes the Basic program. Run splits into two parts.
 * The UI thread part and the background thread part.
 * 
 * The actual execution of the program occurs in the background
 * task. UI activities must all be handled in the UI task.
 * 
 * This leads to a little complication.
 */

public class Run extends ListActivity {

	public static boolean isOld = false;
	private static final String LOGTAG = "Run";
	private static final String CLASSTAG = Run.class.getSimpleName();
//	Log.v(LOGTAG, CLASSTAG + " Line Buffer  " + ExecutingLineBuffer);

	public static Object LOCK = new Object();

	// ********************* Message types for the Handler *********************
																// message numbers < 256 are in "default" group 0
	private static final int MESSAGE_INPUT_DIALOG      = 1;		// for INPUT command
	private static final int MESSAGE_TOAST             = 2;		// for POPUP command
	private static final int MESSAGE_CONSOLE_WRITE     = 3;		// write to the console
	private static final int MESSAGE_CLEAR_SCREEN      = 4;		// for CLS command
	private static final int MESSAGE_CONSOLE_TITLE     = 5;		// for CONSOLE.TITLE command
	private static final int MESSAGE_CONSOLE_LINE_NEW  = 6;		// for CONSOLE.LINE.NEW command
	private static final int MESSAGE_CONSOLE_LINE_CHAR = 7;		// for CONSOLE.LINE.CHAR command
	private static final int MESSAGE_START_GPS         = 8;		// for GPS.OPEN command
	private static final int MESSAGE_CHECKPOINT        = 9;		// for checkpointMessage() method
	private static final int MESSAGE_PUBLISH_PROGRESS  = 20;

	private static final int MESSAGE_GROUP_MASK        = 0x0F00;// groups can be 0x1 through 0xF

	private static final int MESSAGE_DEFAULT_GROUP     = 0x0000;
	private static final int MESSAGE_BT_GROUP          = 0x0100;// add this offset to messages from BlueTooth commands
	private static final int MESSAGE_HTML_GROUP        = 0x0200;// add this offset to messages from HTML commands

	private static final int MESSAGE_DEBUG_GROUP       = 0x0F00;// add this offset to messages from debug commands

	// ***************************** Command class *****************************

	public static class Command {						// Map a command keyword string to its execution function
		public final String name;						// The command keyword
		public final int id;							// Normally 0, may be set non-zero to indicate special case
		public Command(String name) { this(name, 0); }
		public Command(String name, int id) { this.name = name; this.id = id; }
		public boolean run() { return false; }			// Run the command execution function
	}

	// If the current line starts with a keyword in a command list execute the command.
	// The "type" is used only to report errors.
	private boolean executeCommand(Command[] commands, String type) {
		Command c = findCommand(commands, type);
		return (c != null) ? c.run() : false;
	}

	// If the current line starts with a keyword in a command list return the Command object.
	// If not found return null and set an error. The "type" is used only for the error message.
	private Command findCommand(Command[] commands, String type) {
		Command c = findCommand(commands);
		if (c == null) { RunTimeError("Unknown " + type + " command"); }	// no keyword found
		return c;
	}

	// If the current line starts with a keyword in a command list return the Command object.
	// If not found return null.
	private Command findCommand(Command[] commands) {
		for (Command c : commands) {								// loop through the command list
			if (ExecutingLineBuffer.startsWith(c.name, LineIndex)) {// if there is a match
				LineIndex += c.name.length();						// move the line index to end of keyword
				return c;											// return the Command object
			}
		}
		return null;												// no keyword found
	}


	// **********  The variables for the Basic Keywords ****************************

	// First, an alphabetical list of all of the top-level keywords.
	// Every constant in this list must appear in both BasicKeyWords[] and BASIC_cmd[].
	private static final String BKW_ARRAY_GROUP = "array.";
	private static final String BKW_AUDIO_GROUP = "audio.";
	private static final String BKW_BACK_RESUME = "back.resume";
	private static final String BKW_BACKGROUND_RESUME = "background.resume";
	private static final String BKW_BROWSE = "browse";
	private static final String BKW_BT_GROUP = "bt.";
	private static final String BKW_BUNDLE_GROUP = "bundle.";
	private static final String BKW_BYTE_GROUP = "byte.";
	private static final String BKW_CALL = "call";
	private static final String BKW_CLIPBOARD_GET = "clipboard.get";
	private static final String BKW_CLIPBOARD_PUT = "clipboard.put";
	private static final String BKW_CLS = "cls";
	private static final String BKW_CONSOLE_GROUP = "console.";
	private static final String BKW_CONSOLETOUCH_RESUME = "consoletouch.resume";
	private static final String BKW_D_U_BREAK = "d_u.break";
	private static final String BKW_D_U_CONTINUE = "d_u.continue";
	private static final String BKW_DEBUG_GROUP = "debug.";
	private static final String BKW_DECRYPT = "decrypt";
	private static final String BKW_DEVICE = "device";
	private static final String BKW_DIM = "dim";
	private static final String BKW_DIR = "dir";				// same as "file.dir"
	private static final String BKW_DO = "do";
	private static final String BKW_ECHO_OFF = "echo.off";
	private static final String BKW_ECHO_ON = "echo.on";
	private static final String BKW_ELSE = "else";
	private static final String BKW_ELSEIF = "elseif";
	private static final String BKW_EMAIL_SEND = "email.send";
	private static final String BKW_EMPTY_PROGRAM = "@@@";
	private static final String BKW_ENCRYPT = "encrypt";
	private static final String BKW_END = "end";
	private static final String BKW_ENDIF = "endif";
	private static final String BKW_EXIT = "exit";
	private static final String BKW_F_N_BREAK = "f_n.break";
	private static final String BKW_F_N_CONTINUE = "f_n.continue";
	private static final String BKW_FILE_GROUP = "file.";
	private static final String BKW_FN_GROUP = "fn.";
	private static final String BKW_FOR = "for";
	private static final String BKW_FTP_GROUP = "ftp.";
	private static final String BKW_GOSUB = "gosub";
	private static final String BKW_GOTO = "goto";
	private static final String BKW_GPS_GROUP = "gps.";
	private static final String BKW_GR_GROUP = "gr.";
	private static final String BKW_GRABFILE = "grabfile";
	private static final String BKW_GRABURL = "graburl";
	private static final String BKW_HEADSET = "headset";
	private static final String BKW_HOME = "home";
	private static final String BKW_HTML_GROUP = "html.";
	private static final String BKW_HTTP_POST = "http.post";
	private static final String BKW_IF = "if";
	private static final String BKW_INCLUDE = "include";
	private static final String BKW_INKEY = "inkey$";
	private static final String BKW_INPUT = "input";
	private static final String BKW_KB_HIDE = "kb.hide";
	private static final String BKW_KB_TOGGLE = "kb.toggle";
	private static final String BKW_KEY_RESUME = "key.resume";
	private static final String BKW_LET = "let";
	private static final String BKW_LIST_GROUP = "list.";
	private static final String BKW_MENUKEY_RESUME = "menukey.resume";
	private static final String BKW_MKDIR = "mkdir";			// same as "file.mkdir"
	private static final String BKW_MYPHONENUMBER = "myphonenumber";
	private static final String BKW_NEXT = "next";
	private static final String BKW_NOTIFY = "notify";
	private static final String BKW_ONBACKGROUND = "onbackground";
	private static final String BKW_ONBACKKEY = "onbackkey";
	private static final String BKW_ONBTREADREADY = "onbtreadready";
	private static final String BKW_ONCONSOLETOUCH = "onconsoletouch";
	private static final String BKW_ONERROR = "onerror";
	private static final String BKW_ONGRTOUCH = "ongrtouch";
	private static final String BKW_ONKEYPRESS = "onkeypress";
	private static final String BKW_ONMENUKEY = "onmenukey";
	private static final String BKW_ONTIMER = "ontimer";
	private static final String BKW_PAUSE = "pause";
	private static final String BKW_PHONE_GROUP = "phone.";
	private static final String BKW_POPUP = "popup";
	private static final String BKW_PRINT = "print";
	private static final String BKW_READ_GROUP = "read.";
	private static final String BKW_REM = "rem";
	private static final String BKW_RENAME = "rename";			// same as "file.rename"
	private static final String BKW_REPEAT = "repeat";
	private static final String BKW_RETURN = "return";
	private static final String BKW_RINGER_GROUP = "ringer.";
	private static final String BKW_RUN = "run";
	private static final String BKW_SELECT = "select";
	private static final String BKW_SENSORS_GROUP = "sensors.";
	private static final String BKW_SMS_GROUP = "sms.";
	private static final String BKW_SOCKET_GROUP = "socket.";
	private static final String BKW_SOUNDPOOL_GROUP = "soundpool.";
	private static final String BKW_SPLIT = "split";
	private static final String BKW_SPLIT_ALL = "split.all";	// split.all new/2013-07-25 gt
	private static final String BKW_SQL_GROUP = "sql.";
	private static final String BKW_STACK_GROUP = "stack.";
	private static final String BKW_STT_LISTEN = "stt.listen";
	private static final String BKW_STT_RESULTS = "stt.results";
	private static final String BKW_SU_GROUP = "su.";
	private static final String BKW_SW_GROUP = "sw.";
	private static final String BKW_SWAP = "swap";
	private static final String BKW_SYSTEM_GROUP = "system.";
	private static final String BKW_TEXT_GROUP = "text.";
	private static final String BKW_TGET = "tget";
	private static final String BKW_TIME = "time";
	private static final String BKW_TIMER_GROUP = "timer.";
	private static final String BKW_TIMEZONE_GROUP = "timezone.";
	private static final String BKW_TONE = "tone";
	private static final String BKW_TTS_GROUP = "tts.";
	private static final String BKW_UNDIM = "undim";
	private static final String BKW_UNTIL = "until";
	private static final String BKW_VIBRATE = "vibrate";
	private static final String BKW_W_R_BREAK = "w_r.break";
	private static final String BKW_W_R_CONTINUE = "w_r.continue";
	private static final String BKW_WAKELOCK = "wakelock";
	private static final String BKW_WHILE = "while";
	private static final String BKW_WIFILOCK = "wifilock";

	// This array lists all of the top-level keywords so Format can find them.
	// The order of this list determines the order Format uses for its linear search.
	public static final String BasicKeyWords[] = {
		BKW_LET, BKW_PRINT,
		BKW_IF, BKW_ELSEIF, BKW_ELSE, BKW_ENDIF,
		BKW_FOR, BKW_NEXT,
		BKW_WHILE, BKW_REPEAT, BKW_DO, BKW_UNTIL,
		BKW_F_N_BREAK, BKW_W_R_BREAK, BKW_D_U_BREAK,
		BKW_F_N_CONTINUE, BKW_W_R_CONTINUE, BKW_D_U_CONTINUE,
		BKW_SW_GROUP, BKW_FN_GROUP, BKW_CALL,
		BKW_GOTO, BKW_GOSUB, BKW_RETURN,
		BKW_GR_GROUP, BKW_DIM, BKW_UNDIM,
		BKW_ARRAY_GROUP, BKW_BUNDLE_GROUP,
		BKW_LIST_GROUP, BKW_STACK_GROUP,
		BKW_INKEY, BKW_INPUT, BKW_SELECT, BKW_TGET,
		BKW_FILE_GROUP, BKW_TEXT_GROUP, BKW_BYTE_GROUP, BKW_READ_GROUP,
		BKW_DIR, BKW_MKDIR, BKW_RENAME,
		BKW_GRABFILE, BKW_GRABURL,
		BKW_BROWSE, BKW_BT_GROUP, BKW_FTP_GROUP,
		BKW_HTML_GROUP, BKW_HTTP_POST, BKW_SOCKET_GROUP, BKW_SQL_GROUP,
		BKW_GPS_GROUP, BKW_POPUP, BKW_SENSORS_GROUP,
		BKW_AUDIO_GROUP, BKW_SOUNDPOOL_GROUP,
		BKW_RINGER_GROUP, BKW_TONE,
		BKW_CLIPBOARD_GET, BKW_CLIPBOARD_PUT,
		BKW_ENCRYPT, BKW_DECRYPT, BKW_SWAP,
		BKW_SPLIT_ALL, BKW_SPLIT,
		BKW_CLS, BKW_CONSOLE_GROUP, BKW_DEBUG_GROUP,
		BKW_DEVICE, BKW_ECHO_ON, BKW_ECHO_OFF,
		BKW_KB_TOGGLE, BKW_KB_HIDE,
		BKW_NOTIFY, BKW_RUN, // BKW_EMPTY_PROGRAM,		// Format does not need EMPTY_PROGRAM
		BKW_SU_GROUP, BKW_SYSTEM_GROUP,
		BKW_STT_LISTEN, BKW_STT_RESULTS, BKW_TTS_GROUP,
		BKW_TIMER_GROUP, BKW_TIMEZONE_GROUP, BKW_TIME,
		BKW_VIBRATE, BKW_WAKELOCK, BKW_WIFILOCK,
		BKW_END, BKW_EXIT, BKW_HOME,
		BKW_INCLUDE, BKW_PAUSE, BKW_REM,
		BKW_HEADSET, BKW_MYPHONENUMBER,
		BKW_EMAIL_SEND, BKW_PHONE_GROUP, BKW_SMS_GROUP,
		BKW_BACK_RESUME, BKW_BACKGROUND_RESUME,
		BKW_CONSOLETOUCH_RESUME,
		BKW_KEY_RESUME, BKW_MENUKEY_RESUME,
		BKW_ONERROR,
		BKW_ONBACKKEY, BKW_ONBACKGROUND, BKW_ONBTREADREADY,
		BKW_ONCONSOLETOUCH, BKW_ONGRTOUCH,
		BKW_ONKEYPRESS, BKW_ONMENUKEY, BKW_ONTIMER,
	};

	/* Markers for IF, etc., to facilitate skipping them in StatementExecuter() */
	private final int CID_SKIP_TO_ELSE  = 1;	// Ok to execute when skipping to ELSE or ENDIF
	private final int CID_SKIP_TO_ENDIF = 2;	// Ok to execute when skipping to ENDIF
	/* Other markers to make special-case handling faster */
	private final int CID_GROUP = 3;
	private final int CID_OPEN = 4;
	private final int CID_CLOSE = 5;
	private final int CID_STATUS = 6;
	private final int CID_DATALINK = 7;

	/* Special case: need a reference to LET */
	private final Command CMD_LET = new Command(BKW_LET) { public boolean run() { return executeLET(); } };

	// Map BASIC! command keywords to their execution functions.
	// The order of this list determines the order of the linear keyword search, which affects performance.
	private final Command[] BASIC_cmd = new Command[] {
		CMD_LET,
		new Command(BKW_IF,     CID_SKIP_TO_ENDIF) { public boolean run() { return executeIF(); } },
		new Command(BKW_ENDIF,  CID_SKIP_TO_ENDIF) { public boolean run() { return executeENDIF(); } },
		new Command(BKW_ELSEIF, CID_SKIP_TO_ELSE)  { public boolean run() { return executeELSEIF(); } },
		new Command(BKW_ELSE,   CID_SKIP_TO_ELSE)  { public boolean run() { return executeELSE(); } },
		new Command(BKW_PRINT)                  { public boolean run() { return executePRINT(); } },
		new Command(BKW_FOR)                    { public boolean run() { return executeFOR(); } },
		new Command(BKW_NEXT)                   { public boolean run() { return executeNEXT(); } },
		new Command(BKW_WHILE)                  { public boolean run() { return executeWHILE(); } },
		new Command(BKW_REPEAT)                 { public boolean run() { return executeREPEAT(); } },
		new Command(BKW_DO)                     { public boolean run() { return executeDO(); } },
		new Command(BKW_UNTIL)                  { public boolean run() { return executeUNTIL(); } },
		new Command(BKW_F_N_BREAK)              { public boolean run() { return executeF_N_BREAK(); } },
		new Command(BKW_W_R_BREAK)              { public boolean run() { return executeW_R_BREAK(); } },
		new Command(BKW_D_U_BREAK)              { public boolean run() { return executeD_U_BREAK(); } },
		new Command(BKW_F_N_CONTINUE)           { public boolean run() { return executeF_N_CONTINUE(); } },
		new Command(BKW_W_R_CONTINUE)           { public boolean run() { return executeW_R_CONTINUE(); } },
		new Command(BKW_D_U_CONTINUE)           { public boolean run() { return executeD_U_CONTINUE(); } },
		new Command(BKW_SW_GROUP, CID_GROUP)    { public boolean run() { return executeSW(); } },
		new Command(BKW_FN_GROUP, CID_GROUP)    { public boolean run() { return executeFN(); } },
		new Command(BKW_CALL)                   { public boolean run() { return executeCALL(); } },
		new Command(BKW_GOTO)                   { public boolean run() { return executeGOTO(); } },
		new Command(BKW_GOSUB)                  { public boolean run() { return executeGOSUB(); } },
		new Command(BKW_RETURN)                 { public boolean run() { return executeRETURN(); } },
		new Command(BKW_GR_GROUP, CID_GROUP)    { public boolean run() { return executeGR(); } },
		new Command(BKW_DIM)                    { public boolean run() { return executeDIM(); } },
		new Command(BKW_UNDIM)                  { public boolean run() { return executeUNDIM(); } },
		new Command(BKW_ARRAY_GROUP, CID_GROUP) { public boolean run() { return executeARRAY(); } },
		new Command(BKW_BUNDLE_GROUP,CID_GROUP) { public boolean run() { return executeBUNDLE(); } },
		new Command(BKW_LIST_GROUP,  CID_GROUP) { public boolean run() { return executeLIST(); } },
		new Command(BKW_STACK_GROUP, CID_GROUP) { public boolean run() { return executeSTACK(); } },
		new Command(BKW_INKEY)                  { public boolean run() { return executeINKEY(); } },
		new Command(BKW_INPUT)                  { public boolean run() { return executeINPUT(); } },
		new Command(BKW_SELECT)                 { public boolean run() { return executeSELECT(); } },
		new Command(BKW_TGET)                   { public boolean run() { return executeTGET(); } },
		new Command(BKW_FILE_GROUP, CID_GROUP)  { public boolean run() { return executeFILE(); } },
		new Command(BKW_TEXT_GROUP, CID_GROUP)  { public boolean run() { return executeTEXT(); } },
		new Command(BKW_BYTE_GROUP, CID_GROUP)  { public boolean run() { return executeBYTE(); } },
		new Command(BKW_READ_GROUP, CID_GROUP)  { public boolean run() { return executeREAD(); } },
		new Command(BKW_DIR)                    { public boolean run() { return executeDIR(); } },
		new Command(BKW_MKDIR)                  { public boolean run() { return executeMKDIR(); } },
		new Command(BKW_RENAME)                 { public boolean run() { return executeRENAME(); } },
		new Command(BKW_GRABFILE)               { public boolean run() { return executeGRABFILE(); } },
		new Command(BKW_GRABURL)                { public boolean run() { return executeGRABURL(); } },
		new Command(BKW_BROWSE)                 { public boolean run() { return executeBROWSE(); } },
		new Command(BKW_BT_GROUP, CID_GROUP)    { public boolean run() { return executeBT(); } },
		new Command(BKW_FTP_GROUP, CID_GROUP)   { public boolean run() { return executeFTP(); } },
		new Command(BKW_HTML_GROUP, CID_GROUP)  { public boolean run() { return executeHTML(); } },
		new Command(BKW_HTTP_POST)              { public boolean run() { return executeHTTP_POST(); } },
		new Command(BKW_SOCKET_GROUP,CID_GROUP) { public boolean run() { return executeSOCKET(); } },
		new Command(BKW_SQL_GROUP, CID_GROUP)   { public boolean run() { return executeSQL(); } },
		new Command(BKW_GPS_GROUP, CID_GROUP)   { public boolean run() { return executeGPS(); } },
		new Command(BKW_POPUP)                  { public boolean run() { return executePOPUP(); } },
		new Command(BKW_SENSORS_GROUP,CID_GROUP){ public boolean run() { return executeSENSORS(); } },
		new Command(BKW_AUDIO_GROUP, CID_GROUP) { public boolean run() { return executeAUDIO(); } },
		new Command(BKW_SOUNDPOOL_GROUP,CID_GROUP){ public boolean run() { return executeSOUNDPOOL(); } },
		new Command(BKW_RINGER_GROUP,CID_GROUP) { public boolean run() { return executeRINGER(); } },
		new Command(BKW_TONE)                   { public boolean run() { return executeTONE(); } },
		new Command(BKW_CLIPBOARD_GET)          { public boolean run() { return executeCLIPBOARD_GET(); } },
		new Command(BKW_CLIPBOARD_PUT)          { public boolean run() { return executeCLIPBOARD_PUT(); } },
		new Command(BKW_ENCRYPT)                { public boolean run() { return executeENCRYPT(); } },
		new Command(BKW_DECRYPT)                { public boolean run() { return executeDECRYPT(); } },
		new Command(BKW_SWAP)                   { public boolean run() { return executeSWAP(); } },
		new Command(BKW_SPLIT_ALL)              { public boolean run() { return executeSPLIT(-1); } },
		new Command(BKW_SPLIT)                  { public boolean run() { return executeSPLIT(0); } },
		new Command(BKW_CLS)                    { public boolean run() { return executeCLS(); } },
		new Command(BKW_CONSOLE_GROUP,CID_GROUP){ public boolean run() { return executeCONSOLE(); } },
		new Command(BKW_DEBUG_GROUP, CID_GROUP) { public boolean run() { return executeDEBUG(); } },
		new Command(BKW_DEVICE)                 { public boolean run() { return executeDEVICE(); } },
		new Command(BKW_ECHO_ON)                { public boolean run() { return executeECHO_ON(); } },
		new Command(BKW_ECHO_OFF)               { public boolean run() { return executeECHO_OFF(); } },
		new Command(BKW_KB_TOGGLE)              { public boolean run() { return executeKB_TOGGLE(); } },
		new Command(BKW_KB_HIDE)                { public boolean run() { return executeKB_HIDE(); } },
		new Command(BKW_NOTIFY)                 { public boolean run() { return executeNOTIFY(); } },
		new Command(BKW_RUN)                    { public boolean run() { return executeRUN(); } },
		new Command(BKW_EMPTY_PROGRAM)          { public boolean run() { return executeEMPTY_PROGRAM(); } },
		new Command(BKW_SU_GROUP, CID_GROUP)    { public boolean run() { return executeSU(true); } },
		new Command(BKW_SYSTEM_GROUP,CID_GROUP) { public boolean run() { return executeSU(false); } },
		new Command(BKW_STT_LISTEN)             { public boolean run() { return executeSTT_LISTEN(); } },
		new Command(BKW_STT_RESULTS)            { public boolean run() { return executeSTT_RESULTS(); } },
		new Command(BKW_TTS_GROUP, CID_GROUP)   { public boolean run() { return executeTTS(); } },
		new Command(BKW_TIMER_GROUP, CID_GROUP) { public boolean run() { return executeTIMER(); } },
		new Command(BKW_TIMEZONE_GROUP,CID_GROUP){ public boolean run() { return executeTIMEZONE(); } },
		new Command(BKW_TIME)                   { public boolean run() { return executeTIME(); } },
		new Command(BKW_VIBRATE)                { public boolean run() { return executeVIBRATE(); } },
		new Command(BKW_WAKELOCK)               { public boolean run() { return executeWAKELOCK(); } },
		new Command(BKW_WIFILOCK)               { public boolean run() { return executeWIFILOCK(); } },
		new Command(BKW_END)                    { public boolean run() { return executeEND(); } },
		new Command(BKW_EXIT)                   { public boolean run() { Stop = Exit = true; return true; } },
		new Command(BKW_HOME)                   { public boolean run() { return executeHOME(); } },
		new Command(BKW_INCLUDE)                { public boolean run() { return true; } },
		new Command(BKW_PAUSE)                  { public boolean run() { return executePAUSE(); } },
		new Command(BKW_REM)                    { public boolean run() { return true; } },
		new Command(BKW_HEADSET)                { public boolean run() { return executeHEADSET(); } },
		new Command(BKW_MYPHONENUMBER)          { public boolean run() { return executeMYPHONENUMBER(); } },
		new Command(BKW_EMAIL_SEND)             { public boolean run() { return executeEMAIL_SEND(); } },
		new Command(BKW_PHONE_GROUP, CID_GROUP) { public boolean run() { return executePHONE(); } },
		new Command(BKW_SMS_GROUP, CID_GROUP)   { public boolean run() { return executeSMS(); } },

		new Command(BKW_BACK_RESUME)            { public boolean run() { return executeBACK_RESUME(); } },
		new Command(BKW_BACKGROUND_RESUME)      { public boolean run() { return executeBACKGROUND_RESUME(); } },
		new Command(BKW_CONSOLETOUCH_RESUME)    { public boolean run() { return executeCONSOLETOUCH_RESUME(); } },
		new Command(BKW_KEY_RESUME)             { public boolean run() { return executeKEY_RESUME(); } },
		new Command(BKW_MENUKEY_RESUME)         { public boolean run() { return executeMENUKEY_RESUME(); } },

		new Command(BKW_ONERROR)                { public boolean run() { return true; } },
		new Command(BKW_ONBACKKEY)              { public boolean run() { return true; } },
		new Command(BKW_ONBACKGROUND)           { public boolean run() { return true; } },
		new Command(BKW_ONBTREADREADY)          { public boolean run() { return true; } },
		new Command(BKW_ONCONSOLETOUCH)         { public boolean run() { return true; } },
		new Command(BKW_ONGRTOUCH)              { public boolean run() { return true; } },
		new Command(BKW_ONKEYPRESS)             { public boolean run() { return true; } },
		new Command(BKW_ONMENUKEY)              { public boolean run() { return true; } },
		new Command(BKW_ONTIMER)                { public boolean run() { return true; } },
	};

	private String PossibleKeyWord = "";						// Used when TO, STEP, THEN are expected

	private static HashMap<String, String[]> keywordLists = null;	// For Format: map associates a keyword group
																	// with the prefix common to the group.
	public static HashMap<String, String[]> getKeywordLists() {
		if (keywordLists == null) {
			keywordLists = new HashMap<String, String[]>();		// If you add a new keyword group, add it to this list!

			keywordLists.put(BKW_ARRAY_GROUP,     Array_KW);
			keywordLists.put(BKW_AUDIO_GROUP,     Audio_KW);
			keywordLists.put(BKW_BT_GROUP,        bt_KW);
			keywordLists.put(BKW_BUNDLE_GROUP,    Bundle_KW);
			keywordLists.put(BKW_BYTE_GROUP,      byte_KW);
			keywordLists.put(BKW_CONSOLE_GROUP,   Console_KW);
			keywordLists.put(BKW_DEBUG_GROUP,     Debug_KW);
			keywordLists.put(BKW_FILE_GROUP,      file_KW);
			keywordLists.put(BKW_FN_GROUP,        fn_KW);
			keywordLists.put(BKW_FTP_GROUP,       ftp_KW);
			keywordLists.put(BKW_GPS_GROUP,       GPS_KW);
			keywordLists.put(BKW_GR_GROUP,        GR_KW);
			keywordLists.put(BKW_HTML_GROUP,      html_KW);
			keywordLists.put(BKW_LIST_GROUP,      List_KW);
			keywordLists.put(BKW_PHONE_GROUP,     phone_KW);
			keywordLists.put(BKW_READ_GROUP,      read_KW);
			keywordLists.put(BKW_RINGER_GROUP,    ringer_KW);
			keywordLists.put(BKW_SENSORS_GROUP,   Sensors_KW);
			keywordLists.put(BKW_SMS_GROUP,       SMS_KW);
			keywordLists.put(BKW_SOCKET_GROUP,    Socket_KW);
			keywordLists.put(BKW_SOUNDPOOL_GROUP, sp_KW);
			keywordLists.put(BKW_SQL_GROUP,       SQL_KW);
			keywordLists.put(BKW_STACK_GROUP,     Stack_KW);
			keywordLists.put(BKW_SU_GROUP,        su_KW);
			keywordLists.put(BKW_SW_GROUP,        sw_KW);
			keywordLists.put(BKW_SYSTEM_GROUP,    System_KW);
			keywordLists.put(BKW_TEXT_GROUP,      text_KW);
			keywordLists.put(BKW_TIMER_GROUP,     Timer_KW);
			keywordLists.put(BKW_TIMEZONE_GROUP,  TimeZone_KW);
			keywordLists.put(BKW_TTS_GROUP,       tts_KW);
		}
		return keywordLists;
	}

	// **************** The variables for the math function names ************************

	private static final String MF_ABS = "abs(";
	private static final String MF_ACOS = "acos(";
	private static final String MF_ASCII = "ascii(";
	private static final String MF_ASIN = "asin(";
	private static final String MF_ATAN = "atan(";
	private static final String MF_ATAN2 = "atan2(";
	private static final String MF_BACKGROUND = "background(";
	private static final String MF_BAND = "band(";
	private static final String MF_BIN = "bin(";
	private static final String MF_BOR = "bor(";
	private static final String MF_BXOR = "bxor(";
	private static final String MF_CBRT = "cbrt(";
	private static final String MF_CEIL = "ceil(";
	private static final String MF_CLOCK = "clock(";
	private static final String MF_COS = "cos(";
	private static final String MF_COSH = "cosh(";
	private static final String MF_ENDS_WITH = "ends_with(";
	private static final String MF_EXP = "exp(";
	private static final String MF_FLOOR = "floor(";
	private static final String MF_FRAC = "frac(";		// new/2014-03-16 gt
	private static final String MF_GR_COLLISION = "gr_collision(";
	private static final String MF_HEX = "hex(";
	private static final String MF_HYPOT = "hypot(";
	private static final String MF_INT = "int(";		// new/2014-03-16 gt
	private static final String MF_IS_IN = "is_in(";
	private static final String MF_LEN = "len(";
	private static final String MF_LOG = "log(";
	private static final String MF_LOG10 = "log10(";
	private static final String MF_MAX = "max(";		// new/2013-07-29 gt
	private static final String MF_MIN = "min(";		// new/2013-07-29 gt
	private static final String MF_MOD = "mod(";
	private static final String MF_OCT = "oct(";
	private static final String MF_PI = "pi(";			// new/2013-07-29 gt
	private static final String MF_POW = "pow(";
	private static final String MF_RANDOMIZE = "randomize(";
	private static final String MF_RND = "rnd(";
	private static final String MF_ROUND = "round(";
	private static final String MF_SGN = "sgn(";		// new/2014-03-16 gt
	private static final String MF_SHIFT = "shift(";
	private static final String MF_SIN = "sin(";
	private static final String MF_SINH = "sinh(";
	private static final String MF_SQR = "sqr(";
	private static final String MF_STARTS_WITH = "starts_with(";
	private static final String MF_TAN = "tan(";
	private static final String MF_TIME = "time(";
	private static final String MF_TODEGREES = "todegrees(";
	private static final String MF_TORADIANS = "toradians(";
	private static final String MF_UCODE = "ucode(";
	private static final String MF_VAL = "val(";

	public static final String MathFunctions[] = {
		MF_SIN, MF_COS, MF_TAN,
		MF_SQR, MF_ABS, MF_RND,
		MF_VAL, MF_LEN, MF_ACOS,
		MF_ASIN, MF_ATAN2, MF_CEIL,
		MF_FLOOR, MF_MOD, MF_LOG,
		MF_ROUND, MF_TORADIANS, MF_TODEGREES,
		MF_TIME, MF_EXP,
		MF_IS_IN, MF_CLOCK,
		MF_BOR, MF_BAND, MF_BXOR,
		MF_GR_COLLISION,
		MF_ASCII, MF_STARTS_WITH, MF_ENDS_WITH,
		MF_HEX, MF_OCT, MF_BIN, MF_SHIFT,
		MF_RANDOMIZE, MF_BACKGROUND,
		MF_ATAN, MF_CBRT, MF_COSH, MF_HYPOT,
		MF_SINH, MF_POW, MF_LOG10,
		MF_UCODE, MF_PI, MF_MIN, MF_MAX,
		MF_INT, MF_FRAC, MF_SGN,
	};

	private final Command[] MF_cmd = new Command[] {	// Map math function names to their functions
		new Command(MF_SIN)                     { public boolean run() { return executeMF_SIN(); } },
		new Command(MF_COS)                     { public boolean run() { return executeMF_COS(); } },
		new Command(MF_TAN)                     { public boolean run() { return executeMF_TAN(); } },
		new Command(MF_SQR)                     { public boolean run() { return executeMF_SQR(); } },
		new Command(MF_ABS)                     { public boolean run() { return executeMF_ABS(); } },
		new Command(MF_RND)                     { public boolean run() { return executeMF_RND(); } },
		new Command(MF_VAL)                     { public boolean run() { return executeMF_VAL(); } },
		new Command(MF_LEN)                     { public boolean run() { return executeMF_LEN(); } },
		new Command(MF_ACOS)                    { public boolean run() { return executeMF_ACOS(); } },
		new Command(MF_ASIN)                    { public boolean run() { return executeMF_ASIN(); } },
		new Command(MF_ATAN2)                   { public boolean run() { return executeMF_ATAN2(); } },
		new Command(MF_CEIL)                    { public boolean run() { return executeMF_CEIL(); } },
		new Command(MF_FLOOR)                   { public boolean run() { return executeMF_FLOOR(); } },
		new Command(MF_MOD)                     { public boolean run() { return executeMF_MOD(); } },
		new Command(MF_LOG)                     { public boolean run() { return executeMF_LOG(); } },
		new Command(MF_ROUND)                   { public boolean run() { return executeMF_ROUND(); } },
		new Command(MF_TORADIANS)               { public boolean run() { return executeMF_TORADIANS(); } },
		new Command(MF_TODEGREES)               { public boolean run() { return executeMF_TODEGREES(); } },
		new Command(MF_TIME)                    { public boolean run() { return executeMF_TIME(); } },
		new Command(MF_EXP)                     { public boolean run() { return executeMF_EXP(); } },
		new Command(MF_IS_IN)                   { public boolean run() { return executeMF_IS_IN(); } },
		new Command(MF_CLOCK)                   { public boolean run() { return executeMF_CLOCK(); } },
		new Command(MF_BOR)                     { public boolean run() { return executeMF_BOR(); } },
		new Command(MF_BAND)                    { public boolean run() { return executeMF_BAND(); } },
		new Command(MF_BXOR)                    { public boolean run() { return executeMF_BXOR(); } },
		new Command(MF_GR_COLLISION)            { public boolean run() { return executeMF_GR_COLLISION(); } },
		new Command(MF_ASCII)                   { public boolean run() { return executeMF_ASCII(); } },
		new Command(MF_STARTS_WITH)             { public boolean run() { return executeMF_STARTS_WITH(); } },
		new Command(MF_ENDS_WITH)               { public boolean run() { return executeMF_ENDS_WITH(); } },
		new Command(MF_HEX)                     { public boolean run() { return executeMF_base(16); } },
		new Command(MF_OCT)                     { public boolean run() { return executeMF_base(8); } },
		new Command(MF_BIN)                     { public boolean run() { return executeMF_base(2); } },
		new Command(MF_SHIFT)                   { public boolean run() { return executeMF_SHIFT(); } },
		new Command(MF_RANDOMIZE)               { public boolean run() { return executeMF_RANDOMIZE(); } },
		new Command(MF_BACKGROUND)              { public boolean run() { return executeMF_BACKGROUND(); } },
		new Command(MF_ATAN)                    { public boolean run() { return executeMF_ATAN(); } },
		new Command(MF_CBRT)                    { public boolean run() { return executeMF_CBRT(); } },
		new Command(MF_COSH)                    { public boolean run() { return executeMF_COSH(); } },
		new Command(MF_HYPOT)                   { public boolean run() { return executeMF_HYPOT(); } },
		new Command(MF_SINH)                    { public boolean run() { return executeMF_SINH(); } },
		new Command(MF_POW)                     { public boolean run() { return executeMF_POW(); } },
		new Command(MF_LOG10)                   { public boolean run() { return executeMF_LOG10(); } },
		new Command(MF_UCODE)                   { public boolean run() { return executeMF_UCODE(); } },
		new Command(MF_PI)                      { public boolean run() { return executeMF_PI(); } },
		new Command(MF_MIN)                     { public boolean run() { return executeMF_MIN(); } },
		new Command(MF_MAX)                     { public boolean run() { return executeMF_MAX(); } },
		new Command(MF_INT)                     { public boolean run() { return executeMF_INT(); } },
		new Command(MF_FRAC)                    { public boolean run() { return executeMF_FRAC(); } },
		new Command(MF_SGN)                     { public boolean run() { return executeMF_SGN(); } },
	};

	private final HashMap<String, Command> MF_map = new HashMap<String, Command>(64) {{
		for (Command c : MF_cmd) { put(c.name, c); }
	}};

	private static final HashMap<String, Integer> mRoundingModeTable = new HashMap<String, Integer>(7) {{
		put("hd", BigDecimal.ROUND_HALF_DOWN);
		put("he", BigDecimal.ROUND_HALF_EVEN);
		put("hu", BigDecimal.ROUND_HALF_UP);
		put("d",  BigDecimal.ROUND_DOWN);
		put("u",  BigDecimal.ROUND_UP);
		put("f",  BigDecimal.ROUND_FLOOR);
		put("c",  BigDecimal.ROUND_CEILING);
	}};

	//*********************** The variables for the Operators  ************************

	public static final String OperatorString[]={
		"<=", "<>", ">=", ">", "<",
		"=", "^", "*", "+", "-",
		"/", "!", "|", "&", "(",
		")", "=", "+", "-", " ",
		"\n"
	};

    private static final int LE = 0;					// Enumerated names of operators
    private static final int NE = 1;
    private static final int GE = 2;
    private static final int GT = 3;
    private static final int LT = 4;

    private static final int LEQ = 5;
    private static final int EXP = 6;
    private static final int MUL = 7;
    private static final int PLUS = 8;
    private static final int MINUS = 9;

    private static final int DIV = 10;
    private static final int NOT = 11;
    private static final int OR = 12;
    private static final int AND = 13;
    private static final int LPRN = 14;

    private static final int RPRN = 15;
    private static final int ASSIGN = 16;
    private static final int UPLUS = 17;
    private static final int UMINUS = 18;

    private static final int SOE = 19;
    private static final int EOL = 20;
    private static final int FLPRN = 21;

    private static final int GoesOnPrecedence[] = {		// Precedence for going onto stack
        8,  8,  8, 8, 8,
        8, 12, 10, 9,  9,
        10, 7,  5,  6, 15,
        2, 15, 13, 13,
        0, 0, 15,
        15, 15, 15, 15, 15, 15,
        15, 15,
        15, 15, 15, 15,
        15, 15, 15, 15,
        15, 15, 15, 15,
        15, 15, 15 };

    private static final int ComesOffPrecedence[] = {	// Precedence for coming off stack
        8, 8, 8, 8, 8,
        8, 12, 10, 9, 9,
        10, 7, 5, 6, 2,
        14, 1, 13, 13,
        0, 0, 2,
        13,13,13,13,13,13,
        13, 13,
        13, 13, 13, 13,
        13, 13, 13, 13,
        13, 13, 13, 13,
        13, 13, 13 };

    private int OperatorValue = 0;						// Will hold enumerated operator name value

	//**********************  The variables for the string functions  *******************

	private static final String SF_BIN = "bin$(";
	private static final String SF_CHR = "chr$(";
	private static final String SF_FORMAT = "format$(";
	private static final String SF_FORMAT_USING = "format_using$(";
	private static final String SF_GETERROR = "geterror$(";
	private static final String SF_HEX = "hex$(";
	private static final String SF_INT = "int$(";
	private static final String SF_LEFT = "left$(";
	private static final String SF_LOWER = "lower$(";
	private static final String SF_MID = "mid$(";
	private static final String SF_OCT = "oct$(";
	private static final String SF_REPLACE = "replace$(";
	private static final String SF_RIGHT = "right$(";
	private static final String SF_STR = "str$(";
	private static final String SF_UPPER = "upper$(";
	private static final String SF_USING = "using$(";
	private static final String SF_VERSION = "version$(";
	private static final String SF_WORD = "word$(";

	public static final String StringFunctions[] = {
		SF_LEFT, SF_MID, SF_RIGHT,
		SF_STR, SF_UPPER, SF_LOWER,
		SF_USING, SF_FORMAT, SF_FORMAT_USING,
		SF_CHR, SF_REPLACE, SF_WORD,
		SF_INT, SF_HEX, SF_OCT, SF_BIN,
		SF_GETERROR, SF_VERSION,
	};

	private final Command[] SF_cmd = new Command[] {	// Map string function names to their functions
		new Command(SF_LEFT)                    { public boolean run() { return executeSF_LEFT(); } },
		new Command(SF_MID)                     { public boolean run() { return executeSF_MID(); } },
		new Command(SF_RIGHT)                   { public boolean run() { return executeSF_RIGHT(); } },
		new Command(SF_STR)                     { public boolean run() { return executeSF_STR(); } },
		new Command(SF_UPPER)                   { public boolean run() { return executeSF_UPPER(); } },
		new Command(SF_LOWER)                   { public boolean run() { return executeSF_LOWER(); } },
		new Command(SF_FORMAT_USING)            { public boolean run() { return executeSF_USING(); } },
		new Command(SF_FORMAT)                  { public boolean run() { return executeSF_FORMAT(); } },
		new Command(SF_USING)                   { public boolean run() { return executeSF_USING(); } },
		new Command(SF_CHR)                     { public boolean run() { return executeSF_CHR(); } },
		new Command(SF_REPLACE)                 { public boolean run() { return executeSF_REPLACE(); } },
		new Command(SF_WORD)                    { public boolean run() { return executeSF_WORD(); } },
		new Command(SF_INT)                     { public boolean run() { return executeSF_INT(); } },
		new Command(SF_HEX)                     { public boolean run() { return executeSF_HEX(); } },
		new Command(SF_OCT)                     { public boolean run() { return executeSF_OCT(); } },
		new Command(SF_BIN)                     { public boolean run() { return executeSF_BIN(); } },
		new Command(SF_GETERROR)                { public boolean run() { return executeSF_GETERROR(); } },
		new Command(SF_VERSION)                 { public boolean run() { return executeSF_VERSION(); } },
	};

	private final HashMap<String, Command> SF_map = new HashMap<String, Command>(64) {{
		for (Command c : SF_cmd) { put(c.name, c); }
	}};

    // *****************************   Various execution control variables *********************

    public static final int BASIC_GENERAL_INTENT = 255;
    private Random randomizer;
    public static boolean background = false;
    private String errorMsg;
    private boolean kbShown = false;

    public static int OnErrorLine = 0;					// Line number for OnError: label
    public static int OnBackKeyLine = 0;
    public static boolean BackKeyHit = false;
    public static int OnMenuKeyLine = 0;
    public static boolean MenuKeyHit = false;
    public static boolean bgStateChange = false;
    private int OnBGLine = 0;
    private int onCTLine = 0;
    private boolean ConsoleTouched = false;
    private boolean ConsoleLongTouch = false;
    private int TouchedConsoleLine = 0;					// first valid line number is 1
    private int interruptResume = -1;

    private int LineIndex = 0;							// Current displacement into ExecutingLineBuffer
    private String ExecutingLineBuffer ="";				// Holds the current line being executed
    private int ExecutingLineIndex = 0;					// Points to the current line in Basic.lines
    private boolean SEisLE = false;						// If a String expression result is a logical expression

    private Stack<Integer> GosubStack;					// Stack used for Gosub/Return
    private Stack<Bundle> ForNextStack;					// Stack used for For/Next
    private Stack<Integer> WhileStack;					// Stack used for While/Repeat
    private Stack<Integer> DoStack;						// Stack used for Do/Until

    private Stack <Integer> IfElseStack;				// Stack for IF-ELSE-ENDIF operations
    private static final Integer IEskip1 = 1;				// Skip statements until ELSE, ELSEIF or ENDIF
    private static final Integer IEskip2 = 2;				// Skip to until ENDIF
    private static final Integer IEexec = 3;				// Execute to ELSE, ELSEIF or ENDIF
    private static final Integer IEinterrupt = 4;

    private Double GetNumberValue = (double)0;				// Return value from GetNumber()
    private Double EvalNumericExpressionValue = (double)0;	// Return value from EvalNumericExprssion()
    private Long EvalNumericExpressionIntValue = 0L;		// Integer copy of EvalNumericExpressionValue when VarIsInt is true
    private Vibrator myVib;

    public static ArrayList<String> output;					// The output screen text lines
    private Basic.ColoredTextAdapter AA;					// The output screen array adapter
    private ListView lv;									// The output screen list view
    private static final int MaxTempOutput = 500;
    private static String TempOutput[] = new String[MaxTempOutput];	// Holds waiting output screen line
    private static int TempOutputIndex = 0;					// Index to next available TempOutput entry

    private Background theBackground;						// Background task ID
    private boolean SyntaxError = false;					// Set true when Syntax Error message has been output

    private boolean ProgressPending = false;

	// debugger dialog and ui thread vars
	private static final int MESSAGE_DEBUG_DIALOG = MESSAGE_DEBUG_GROUP + 1;
	private static final int MESSAGE_DEBUG_SWAP   = MESSAGE_DEBUG_GROUP + 2;
	private static final int MESSAGE_DEBUG_SELECT = MESSAGE_DEBUG_GROUP + 3;
	private boolean WaitForResume = false;
	private boolean DebuggerStep = false;
	private boolean DebuggerHalt = false;
	private boolean WaitForSwap = false;
	private boolean WaitForSelect = false;
	private boolean dbSwap = false;
	private boolean dbSelect = false;
	private AlertDialog dbDialog;
	private AlertDialog dbSwapDialog;
	private AlertDialog dbSelectDialog;
	private boolean dbDialogScalars;
	private boolean dbDialogArray;
	private boolean dbDialogList;
	private boolean dbDialogStack;
	private boolean dbDialogBundle;
	private boolean dbDialogWatch;
	private boolean dbDialogProgram;
	private boolean dbDialogConsole;
	private String dbConsoleHistory;
	private String dbConsoleExecute;
	private int dbConsoleELBI;
	private ArrayList <Integer> WatchVarIndex;
	private ArrayList <String> Watch_VarNames;
	private int WatchedArray;
	private int WatchedList;
	private int WatchedStack;
	private int WatchedBundle;
	// end debugger ui vars

	public static boolean Stop = false;					// Stops program from running
	public static boolean Exit = false;					// Exits program and signals caller to exit, too
	public static boolean GraphicsPaused = false;		// Signal from GR that it has been paused
	public static boolean RunPaused = false;			// Used to control the media player
	public static boolean StopDisplay = false;
	public static boolean DisplayStopped = false;
	private String PrintLine = "";						// Hold the Print line currently being built
	private String textPrintLine = "";					// Hold the TextPrint line currently being built
	private boolean PrintLineReady = false;				// Signals a line is ready to print or write

	private InputMethodManager IMM;
	private HashMap<String,Integer> Labels;				// A list of all labels and associated line numbers

	private ArrayList<String> VarNames ;				// Each entry has the variable name string
	private ArrayList<Integer> VarIndex;				// Each entry is an index into
														// NumberVarValues or
														// StringVarValues or
														// ArrayTable or
														// FunctionTable
	private int VarNumber = 0;							// An index for both VarNames and NVarValue
	public static ArrayList<Double> NumericVarValues;	// if a var is a number, the VarIndex is an
														// index into this list. The values of numeric
														// array elements are also kept here
	private ArrayList<String> StringVarValues;			// if a var is a string, the VarIndex is an
														// index into this list. The values of string
														// array elements are also kept here
	private ArrayList<Bundle> ArrayTable;				// Each DIMed array has an entry in this table
	private String StringConstant = "";					// Storage for a string constant
	private int theValueIndex;							// The index into the value table for the current var
	private int ArrayValueStart = 0;					// Value index for newly created array

	private ArrayList<Bundle> FunctionTable;			// A bundle is created for each defined function
	private Bundle ufBundle;							// Bundle set by isUserFunction and used by doUserFunction
	private Stack<Bundle> FunctionStack;				// Stack contains the currently executing functions
	private boolean fnRTN = false;						// Set true by fn.rtn. Cause RunLoop() to return

	private boolean VarIsNew = true;					// Signal from getVar() that this var is new
	private boolean VarIsNumeric = true;				// if false, var is a string
	private boolean VarIsInt = false;					// temporary integer status used only by fprint
	private boolean VarIsArray = false;					// if true, var is an array
														// if the var is an array, the VarIndex is
														// an index into ArrayTable
	private boolean VarIsFunction = false;				// Flag set by parseVar() when var is a user function
	private int VarSearchStart;							// Used to limit search for var names to executing function vars
	private int interruptVarSearchStart;				// Save VarSearchStart across interrupt

	ClipboardManager clipboard;
	private static long sTime;

	// ******************* Variables for User-defined Functions ************************

	private static final String BKW_FN_DEF = "def";
	private static final String BKW_FN_RTN = "rtn";
	private static final String BKW_FN_END = "end";

	private static final String fn_KW[] = {				// Command list for Format
		BKW_FN_DEF, BKW_FN_RTN, BKW_FN_END
	};

	private final Command[] fn_cmd = new Command[] {	// Map user function command keywords to their execution functions
		new Command(BKW_FN_DEF)         { public boolean run() { return executeFN_DEF(); } },
		new Command(BKW_FN_RTN)         { public boolean run() { return executeFN_RTN(); } },
		new Command(BKW_FN_END)         { public boolean run() { return executeFN_END(); } },
	};

	// ******************************** SWITCH variables ********************************

	private static final String BKW_SW_BEGIN = "begin";
	private static final String BKW_SW_CASE = "case";
	private static final String BKW_SW_BREAK = "break";
	private static final String BKW_SW_DEFAULT = "default";
	private static final String BKW_SW_END = "end";

	private static final String sw_KW[] = {				// Command list for Format
		BKW_SW_BEGIN, BKW_SW_CASE, BKW_SW_BREAK,
		BKW_SW_DEFAULT, BKW_SW_END
	};

	private final Command[] sw_cmd = new Command[] {	// Map sw (switch) command keywords to their execution functions
		new Command(BKW_SW_BEGIN)       { public boolean run() { return executeSW_BEGIN(); } },
		new Command(BKW_SW_CASE)        { public boolean run() { return executeSW_CASE(); } },
		new Command(BKW_SW_BREAK)       { public boolean run() { return executeSW_BREAK(); } },
		new Command(BKW_SW_DEFAULT)     { public boolean run() { return executeSW_DEFAULT(); } },
		new Command(BKW_SW_END)         { public boolean run() { return executeSW_END(); } },
	};

	// ******************************** Wakelock variables *********************************

	private static PowerManager.WakeLock theWakeLock;
	private static final int partial = 1;
	private static final int dim = 2;
	private static final int bright = 3;
	private static final int full = 4;
	private static final int release = 5;

	// ******************************** Wifilock variables *********************************

	private static WifiManager.WifiLock theWifiLock;
	private static final int wifi_mode_scan = 1;
	private static final int wifi_mode_full = 2;
	private static final int wifi_mode_high = 3;
	private static final int wifi_release = 4;

	// ******************************* File I/O operation variables ************************

	private static final String BKW_FILE_DELETE = "delete";
	private static final String BKW_FILE_SIZE = "size";
	private static final String BKW_FILE_DIR = "dir";
	private static final String BKW_FILE_MKDIR = "mkdir";
	private static final String BKW_FILE_RENAME = "rename";
	private static final String BKW_FILE_ROOT = "root";
	private static final String BKW_FILE_EXISTS = "exists";
	private static final String BKW_FILE_TYPE = "type";

	private static final String file_KW[] = {			// Command list for Format
		BKW_FILE_DELETE, BKW_FILE_SIZE, BKW_FILE_DIR, BKW_FILE_MKDIR,
		BKW_FILE_RENAME, BKW_FILE_ROOT, BKW_FILE_EXISTS, BKW_FILE_TYPE
	};

	private final Command[] file_cmd = new Command[] {	// Map File command keywords to their execution functions
		new Command(BKW_FILE_DELETE)    { public boolean run() { return executeDELETE(); } },
		new Command(BKW_FILE_SIZE)      { public boolean run() { return executeFILE_SIZE(); } },
		new Command(BKW_FILE_DIR)       { public boolean run() { return executeDIR(); } },
		new Command(BKW_FILE_MKDIR)     { public boolean run() { return executeMKDIR(); } },
		new Command(BKW_FILE_RENAME)    { public boolean run() { return executeRENAME(); } },
		new Command(BKW_FILE_ROOT)      { public boolean run() { return executeFILE_ROOTS(); } },
		new Command(BKW_FILE_EXISTS)    { public boolean run() { return executeFILE_EXISTS(); } },
		new Command(BKW_FILE_TYPE)      { public boolean run() { return executeFILE_TYPE(); } }
	};

	private static final int FMR = 0;						// File Mode Read
	private static final int FMW = 1;						// File Mode Write
	private static final int FMRW = 2;						// File Mode Read-Write

	public static ArrayList<Bundle> FileTable;				// File table list
	public static ArrayList<BufferedReader> BRlist;			// A list of of file readers
	public static ArrayList<FileWriter> FWlist;				// A list of file writers
	public static ArrayList<DataOutputStream> DOSlist;		// A list of output streams
	public static ArrayList<BufferedInputStream> BISlist;	// A list of input streams

	// ********************************* TEXT I/O variables *********************************

	private static final String BKW_TEXT_OPEN = "open";
	private static final String BKW_TEXT_CLOSE = "close";
	private static final String BKW_TEXT_READLN = "readln";
	private static final String BKW_TEXT_WRITELN = "writeln";
	private static final String BKW_TEXT_INPUT = "input";
	private static final String BKW_TEXT_POSITION_GET = "position.get";
	private static final String BKW_TEXT_POSITION_SET = "position.set";

	private static final String text_KW[] = {			// Command list for Format
		BKW_TEXT_OPEN, BKW_TEXT_CLOSE,
		BKW_TEXT_READLN, BKW_TEXT_WRITELN,
		BKW_TEXT_INPUT, BKW_TEXT_POSITION_GET, BKW_TEXT_POSITION_SET
	};

	private final Command[] text_cmd = new Command[] {	// Map Text I/O command keywords to their execution functions
		new Command(BKW_TEXT_OPEN)          { public boolean run() { return executeTEXT_OPEN(); } },
		new Command(BKW_TEXT_CLOSE)         { public boolean run() { return executeTEXT_CLOSE(); } },
		new Command(BKW_TEXT_READLN)        { public boolean run() { return executeTEXT_READLN(); } },
		new Command(BKW_TEXT_WRITELN)       { public boolean run() { return executeTEXT_WRITELN(); } },
		new Command(BKW_TEXT_INPUT)         { public boolean run() { return executeTEXT_INPUT(); } },
		new Command(BKW_TEXT_POSITION_GET)  { public boolean run() { return executeTEXT_POSITION_GET(); } },
		new Command(BKW_TEXT_POSITION_SET)  { public boolean run() { return executeTEXT_POSITION_SET(); } },
	};

	// ******************************* BYTE I/O variables *******************************

	private static final String BKW_BYTE_OPEN = "open";
	private static final String BKW_BYTE_CLOSE = "close";
	private static final String BKW_BYTE_READ_BYTE = "read.byte";
	private static final String BKW_BYTE_WRITE_BYTE = "write.byte";
	private static final String BKW_BYTE_READ_BUFFER = "read.buffer";
	private static final String BKW_BYTE_WRITE_BUFFER = "write.buffer";
	private static final String BKW_BYTE_COPY = "copy";
	private static final String BKW_BYTE_POSITION_GET = "position.get";
	private static final String BKW_BYTE_POSITION_SET = "position.set";

	private static final String byte_KW[] = {			// Command list for Format
		BKW_BYTE_OPEN, BKW_BYTE_CLOSE,
		BKW_BYTE_READ_BYTE, BKW_BYTE_WRITE_BYTE,
		BKW_BYTE_READ_BUFFER, BKW_BYTE_WRITE_BUFFER,
		BKW_BYTE_COPY, BKW_BYTE_POSITION_GET, BKW_BYTE_POSITION_SET
	};

	private final Command[] byte_cmd = new Command[] {	// Map Byte I/O command keywords to their execution functions
		new Command(BKW_BYTE_OPEN)          { public boolean run() { return executeBYTE_OPEN(); } },
		new Command(BKW_BYTE_CLOSE)         { public boolean run() { return executeBYTE_CLOSE(); } },
		new Command(BKW_BYTE_READ_BYTE)     { public boolean run() { return executeBYTE_READ_BYTE(); } },
		new Command(BKW_BYTE_WRITE_BYTE)    { public boolean run() { return executeBYTE_WRITE_BYTE(); } },
		new Command(BKW_BYTE_READ_BUFFER)   { public boolean run() { return executeBYTE_READ_BUFFER(); } },
		new Command(BKW_BYTE_WRITE_BUFFER)  { public boolean run() { return executeBYTE_WRITE_BUFFER(); } },
		new Command(BKW_BYTE_COPY)          { public boolean run() { return executeBYTE_COPY(); } },
		new Command(BKW_BYTE_POSITION_GET)  { public boolean run() { return executeBYTE_POSITION_GET(); } },
		new Command(BKW_BYTE_POSITION_SET)  { public boolean run() { return executeBYTE_POSITION_SET(); } },
	};

	// ******************** READ variables *******************************************

	private static final String BKW_READ_DATA = "data";
	private static final String BKW_READ_NEXT = "next";
	private static final String BKW_READ_FROM = "from";

	private static final String read_KW[] = {			// Command list for Format
		BKW_READ_DATA, BKW_READ_NEXT, BKW_READ_FROM
	};

	private final Command[] read_cmd = new Command[] {	// Map Read command keywords to their execution functions
										// Do NOT call executeREAD_DATA, that was done in PreScan
		new Command(BKW_READ_DATA)          { public boolean run() { return true; } },
		new Command(BKW_READ_NEXT)          { public boolean run() { return executeREAD_NEXT(); } },
		new Command(BKW_READ_FROM)          { public boolean run() { return executeREAD_FROM(); } },
	};

	private int readNext = 0;
	private ArrayList <Bundle> readData;

	// ******************** Console Command variables ********************************

	private static final String BKW_CONSOLE_FRONT = "front";
	private static final String BKW_CONSOLE_SAVE = "save";
	private static final String BKW_CONSOLE_TITLE = "title";
	private static final String BKW_CONSOLE_LINE_COUNT = "line.count";
	private static final String BKW_CONSOLE_LINE_TEXT = "line.text";
	private static final String BKW_CONSOLE_LINE_TOUCHED = "line.touched";
	private static final String BKW_CONSOLE_LINE_NEW = "line.new";
	private static final String BKW_CONSOLE_LINE_CHAR = "line.char";

	private static final String Console_KW[] = {		// Console command list for Format
		BKW_CONSOLE_FRONT, BKW_CONSOLE_SAVE, BKW_CONSOLE_TITLE,
		BKW_CONSOLE_LINE_COUNT, BKW_CONSOLE_LINE_TEXT, BKW_CONSOLE_LINE_TOUCHED,
		BKW_CONSOLE_LINE_NEW, BKW_CONSOLE_LINE_CHAR
	};

	private final Command[] Console_cmd = new Command[] {	// Map console command keywords to their execution functions
		new Command(BKW_CONSOLE_FRONT)          { public boolean run() { return executeCONSOLE_FRONT(); } },
		new Command(BKW_CONSOLE_SAVE)           { public boolean run() { return executeCONSOLE_DUMP(); } },
		new Command(BKW_CONSOLE_TITLE)          { public boolean run() { return executeCONSOLE_TITLE(); } },
		new Command(BKW_CONSOLE_LINE_COUNT)     { public boolean run() { return executeCONSOLE_LINE_COUNT(); } },
		new Command(BKW_CONSOLE_LINE_TEXT)      { public boolean run() { return executeCONSOLE_LINE_TEXT(); } },
		new Command(BKW_CONSOLE_LINE_TOUCHED)   { public boolean run() { return executeCONSOLE_LINE_TOUCHED(); } },
		new Command(BKW_CONSOLE_LINE_NEW)       { public boolean run() { return executeCONSOLE_LINE_NEW(); } },
		new Command(BKW_CONSOLE_LINE_CHAR)      { public boolean run() { return executeCONSOLE_LINE_CHAR(); } }
	};

	// ******************** Input Command variables ********************************

	private boolean WaitForInput ;							// Signals between background task and foreground
	private boolean BadInput = false;
	private boolean InputCancelled = false;
	private boolean InputIsNumeric = true;
	private int InputVarIndex;								// Index into Numeric or StringVarValues, depending on VarIsNumeric
	private String UserPrompt = "";
	private String InputDefault = "";
	private boolean InputDismissed = false;					// These two will be used only if we dismiss the dialog in onPause
	private AlertDialog theAlertDialog;

	// ******************** Popup Command variables ********************************

	public static String ToastMsg;
	public static int ToastX;
	public static int ToastY;
	public static int ToastDuration;

	// ******************** Variables for the SELECT Command ***********************

	public static int SelectedItem;						// The index of the selected item
	public static boolean ItemSelected;					// Signal from Select.java saying an item has been selected 
	public static boolean SelectLongClick;				// True if long click

	// ******************** SQL Variables ******************************************

	private static final String BKW_SQL_OPEN = "open";
	private static final String BKW_SQL_CLOSE = "close";
	private static final String BKW_SQL_INSERT = "insert";
	private static final String BKW_SQL_QUERY_LENGTH = "query.length";
	private static final String BKW_SQL_QUERY_POSITION = "query.position";
	private static final String BKW_SQL_QUERY = "query";
	private static final String BKW_SQL_NEXT = "next";
	private static final String BKW_SQL_DELETE = "delete";
	private static final String BKW_SQL_UPDATE = "update";
	private static final String BKW_SQL_EXEC = "exec";
	private static final String BKW_SQL_RAW_QUERY = "raw_query";
	private static final String BKW_SQL_DROP_TABLE = "drop_table";
	private static final String BKW_SQL_NEW_TABLE = "new_table";

	private static final String SQL_KW[] = {			// SQL command list for Format
		BKW_SQL_OPEN, BKW_SQL_CLOSE, BKW_SQL_INSERT,
		BKW_SQL_QUERY_LENGTH, BKW_SQL_QUERY_POSITION, BKW_SQL_QUERY,
		BKW_SQL_NEXT, BKW_SQL_DELETE,
		BKW_SQL_UPDATE, BKW_SQL_EXEC,
		BKW_SQL_RAW_QUERY, BKW_SQL_DROP_TABLE, BKW_SQL_NEW_TABLE
	};

	private final Command[] SQL_cmd = new Command[] {	// Map SQL command keywords to their execution functions
		new Command(BKW_SQL_OPEN)           { public boolean run() { return execute_sql_open(); } },
		new Command(BKW_SQL_CLOSE)          { public boolean run() { return execute_sql_close(); } },
		new Command(BKW_SQL_INSERT)         { public boolean run() { return execute_sql_insert(); } },
		new Command(BKW_SQL_QUERY_LENGTH)   { public boolean run() { return execute_sql_query_length(); } },
		new Command(BKW_SQL_QUERY_POSITION) { public boolean run() { return execute_sql_query_position(); } },
		new Command(BKW_SQL_QUERY)          { public boolean run() { return execute_sql_query(); } },
		new Command(BKW_SQL_NEXT)           { public boolean run() { return execute_sql_next(); } },
		new Command(BKW_SQL_DELETE)         { public boolean run() { return execute_sql_delete(); } },
		new Command(BKW_SQL_UPDATE)         { public boolean run() { return execute_sql_update(); } },
		new Command(BKW_SQL_EXEC)           { public boolean run() { return execute_sql_exec(); } },
		new Command(BKW_SQL_RAW_QUERY)      { public boolean run() { return execute_sql_raw_query(); } },
		new Command(BKW_SQL_DROP_TABLE)     { public boolean run() { return execute_sql_drop_table(); } },
		new Command(BKW_SQL_NEW_TABLE)      { public boolean run() { return execute_sql_new_table(); } }
	};

	public static ArrayList<SQLiteDatabase> DataBases; 	 // List of created data bases
	public static ArrayList<Cursor> Cursors; 	 		 // List of created data bases

	// ******************************** Variables for the INKEY$ command ***********************

	public static boolean KeyPressed = false;
	public static final String Numbers = "0123456789";    // translations for key codes
	public static final String Chars = "abcdefghijklmnopqrstuvwxyz";
	public static String Buffer = "0123456789";
	public static ArrayList<String> InChar ;
	public static int OnKeyLine;

	// ********************************* Variables for text.input command **********************

	public static String TextInputString = "";
	public static boolean HaveTextInput;

	// ******************************** Graphics Declarations **********************************

	public static Intent GRclass;						// Graphics Intent Class
	public static boolean GRopen = false;				// Graphics Open Flag
	public static ArrayList<Bundle> DisplayList;
	public static ArrayList<Integer> RealDisplayList;
	public static ArrayList<Paint> PaintList;
	public static ArrayList<Bitmap> BitmapList;
	public static ArrayList<Float> PixelPoints;
	public static Paint aPaint;
	public static Bitmap aBitmap ;
	public static boolean GRrunning;
	public static boolean GRFront;
	public static boolean Touched;
	public static double TouchX[] = {0,0,0};
	public static double TouchY[] = {0,0,0};
	public static boolean NewTouch[] = {false, false, false};
	public static int OnTouchLine;
	public static Canvas drawintoCanvas = null;

	// Graphics command keywords
	private static final String BKW_GR_ARC = "arc";
	private static final String BKW_GR_BOUNDED_TOUCH = "bounded.touch";
	private static final String BKW_GR_BOUNDED_TOUCH2 = "bounded.touch2";
	private static final String BKW_GR_BRIGHTNESS = "brightness";
	private static final String BKW_GR_CIRCLE = "circle";
	private static final String BKW_GR_CLIP = "clip";
	private static final String BKW_GR_CLOSE = "close";
	private static final String BKW_GR_CLS = "cls";
	private static final String BKW_GR_COLOR = "color";
	private static final String BKW_GR_FRONT = "front";
	private static final String BKW_GR_GETDL = "getdl";
	private static final String BKW_GR_HIDE = "hide";
	private static final String BKW_GR_LINE = "line";
	private static final String BKW_GR_MODIFY = "modify";
	private static final String BKW_GR_NEWDL = "newdl";
	private static final String BKW_GR_ONGRTOUCH_RESUME = "ongrtouch.resume";
	private static final String BKW_GR_OPEN = "open";
	private static final String BKW_GR_ORIENTATION = "orientation";
	private static final String BKW_GR_OVAL = "oval";
	private static final String BKW_GR_PAINT_GET = "paint.get";
	private static final String BKW_GR_POINT = "point";
	private static final String BKW_GR_POLY = "poly";
	private static final String BKW_GR_RECT = "rect";
	private static final String BKW_GR_RENDER = "render";
	private static final String BKW_GR_ROTATE_END = "rotate.end";
	private static final String BKW_GR_ROTATE_START = "rotate.start";
	private static final String BKW_GR_SAVE = "save";
	private static final String BKW_GR_SCALE = "scale";
	private static final String BKW_GR_SCREEN = "screen";
	private static final String BKW_GR_SCREEN_TO_BITMAP = "screen.to_bitmap";
	private static final String BKW_GR_SET_ANTIALIAS = "set.antialias";
	private static final String BKW_GR_SET_PIXELS = "set.pixels";
	private static final String BKW_GR_SET_STROKE = "set.stroke";
	private static final String BKW_GR_SHOW = "show";
	private static final String BKW_GR_STATUSBAR_SHOW = "statusbar.show";
	private static final String BKW_GR_TOUCH = "touch";
	private static final String BKW_GR_TOUCH2 = "touch2";

	// gr bitmap group
	private static final String BKW_GR_BITMAP_GROUP = "bitmap.";
	private static final String BKW_GR_BITMAP_CREATE = "create";
	private static final String BKW_GR_BITMAP_CROP = "crop";
	private static final String BKW_GR_BITMAP_DELETE = "delete";
	private static final String BKW_GR_BITMAP_DRAW = "draw";
	private static final String BKW_GR_BITMAP_DRAWINTO_END = "drawinto.end";
	private static final String BKW_GR_BITMAP_DRAWINTO_START = "drawinto.start";
	private static final String BKW_GR_BITMAP_LOAD = "load";
	private static final String BKW_GR_BITMAP_SAVE = "save";
	private static final String BKW_GR_BITMAP_SCALE = "scale";
	private static final String BKW_GR_BITMAP_SIZE = "size";
	// gr camera group
	private static final String BKW_GR_CAMERA_GROUP = "camera.";
	private static final String BKW_GR_CAMERA_AUTOSHOOT = "autoshoot";
	private static final String BKW_GR_CAMERA_BLINDSHOOT = "blindshoot";
	private static final String BKW_GR_CAMERA_MANUALSHOOT = "manualshoot";
	private static final String BKW_GR_CAMERA_SELECT = "select";
	private static final String BKW_GR_CAMERA_SHOOT = "shoot";
	// gr get group
	private static final String BKW_GR_GET_GROUP = "get.";
	private static final String BKW_GR_GET_BMPIXEL = "bmpixel";
	private static final String BKW_GR_GET_PARAMS = "params";
	private static final String BKW_GR_GET_PIXEL = "pixel";
	private static final String BKW_GR_GET_POSITION = "position";
	private static final String BKW_GR_GET_TEXTBOUNDS = "textbounds";
	private static final String BKW_GR_GET_TYPE = "type";
	private static final String BKW_GR_GET_VALUE = "value";
	// gr text group
	private static final String BKW_GR_TEXT_GROUP = "text.";
	private static final String BKW_GR_TEXT_ALIGN = "align";
	private static final String BKW_GR_TEXT_BOLD = "bold";
	private static final String BKW_GR_TEXT_DRAW = "draw";
	private static final String BKW_GR_TEXT_SIZE = "size";
	private static final String BKW_GR_TEXT_SKEW = "skew";
	private static final String BKW_GR_TEXT_STRIKE = "strike";
	private static final String BKW_GR_TEXT_TYPEFACE = "typeface";
	private static final String BKW_GR_TEXT_UNDERLINE = "underline";
	private static final String BKW_GR_TEXT_WIDTH = "width";

	private static final String GR_KW[] = {				// Command list for Format
		BKW_GR_RENDER, BKW_GR_MODIFY,
		BKW_GR_BOUNDED_TOUCH2, BKW_GR_BOUNDED_TOUCH,
		BKW_GR_TOUCH2, BKW_GR_TOUCH,
		BKW_GR_ARC, BKW_GR_BRIGHTNESS, BKW_GR_CIRCLE,
		BKW_GR_CLIP, BKW_GR_CLOSE, BKW_GR_CLS,
		BKW_GR_COLOR, BKW_GR_FRONT,
		BKW_GR_GETDL, BKW_GR_NEWDL, BKW_GR_HIDE, BKW_GR_SHOW,
		BKW_GR_LINE, BKW_GR_ONGRTOUCH_RESUME,
		BKW_GR_OPEN, BKW_GR_ORIENTATION, BKW_GR_OVAL,
		BKW_GR_PAINT_GET, BKW_GR_POINT, BKW_GR_POLY,
		BKW_GR_RECT, BKW_GR_ROTATE_START, BKW_GR_ROTATE_END,
		BKW_GR_SAVE, BKW_GR_SCALE,
		BKW_GR_SCREEN, BKW_GR_SCREEN_TO_BITMAP,
		BKW_GR_SET_ANTIALIAS, BKW_GR_SET_PIXELS,
		BKW_GR_SET_STROKE, BKW_GR_STATUSBAR_SHOW,

		// GR subgroups - Format can handle only one level of grouping
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_CREATE,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_CROP,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DELETE,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DRAWINTO_START,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DRAWINTO_END,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DRAW,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_LOAD,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_SAVE,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_SCALE,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_SIZE,
		BKW_GR_CAMERA_GROUP + BKW_GR_CAMERA_AUTOSHOOT,
		// BKW_GR_CAMERA_GROUP + BKW_GR_CAMERA_BLINDSHOOT,
		BKW_GR_CAMERA_GROUP + BKW_GR_CAMERA_MANUALSHOOT,
		BKW_GR_CAMERA_GROUP + BKW_GR_CAMERA_SELECT,
		BKW_GR_CAMERA_GROUP + BKW_GR_CAMERA_SHOOT,
		BKW_GR_GET_GROUP + BKW_GR_GET_BMPIXEL,
		BKW_GR_GET_GROUP + BKW_GR_GET_PARAMS,
		BKW_GR_GET_GROUP + BKW_GR_GET_PIXEL,
		BKW_GR_GET_GROUP + BKW_GR_GET_POSITION,
		BKW_GR_GET_GROUP + BKW_GR_GET_TEXTBOUNDS,
		BKW_GR_GET_GROUP + BKW_GR_GET_TYPE,
		BKW_GR_GET_GROUP + BKW_GR_GET_VALUE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_ALIGN,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_BOLD,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_DRAW,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_SIZE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_SKEW,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_STRIKE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_TYPEFACE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_UNDERLINE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_WIDTH,
	};

	private final Command[] GR_cmd = new Command[] {	// Map GR command keywords to their execution functions
		new Command(BKW_GR_RENDER)                  { public boolean run() { return execute_gr_render(); } },
		new Command(BKW_GR_MODIFY)                  { public boolean run() { return execute_gr_modify(); } },
		new Command(BKW_GR_BOUNDED_TOUCH2)          { public boolean run() { return execute_gr_bound_touch(1); } },
		new Command(BKW_GR_BOUNDED_TOUCH)           { public boolean run() { return execute_gr_bound_touch(0); } },
		new Command(BKW_GR_TOUCH2)                  { public boolean run() { return execute_gr_touch(1); } },
		new Command(BKW_GR_TOUCH)                   { public boolean run() { return execute_gr_touch(0); } },

		new Command(BKW_GR_BITMAP_GROUP, CID_GROUP) { public boolean run() { return executeGR_BITMAP(); } },
		new Command(BKW_GR_CAMERA_GROUP, CID_GROUP) { public boolean run() { return executeGR_CAMERA(); } },
		new Command(BKW_GR_GET_GROUP, CID_GROUP)    { public boolean run() { return executeGR_GET(); } },
		new Command(BKW_GR_TEXT_GROUP, CID_GROUP)   { public boolean run() { return executeGR_TEXT(); } },

		new Command(BKW_GR_ARC)                     { public boolean run() { return execute_gr_arc(); } },
		new Command(BKW_GR_BRIGHTNESS)              { public boolean run() { return execute_brightness(); } },
		new Command(BKW_GR_CIRCLE)                  { public boolean run() { return execute_gr_circle(); } },
		new Command(BKW_GR_CLIP)                    { public boolean run() { return execute_gr_clip(); } },
		new Command(BKW_GR_CLOSE)                   { public boolean run() { return execute_gr_close(); } },
		new Command(BKW_GR_CLS)                     { public boolean run() { return execute_gr_cls(); } },
		new Command(BKW_GR_COLOR)                   { public boolean run() { return execute_gr_color(); } },
		new Command(BKW_GR_FRONT)                   { public boolean run() { return execute_gr_front(); } },
		new Command(BKW_GR_GETDL)                   { public boolean run() { return execute_gr_getdl(); } },
		new Command(BKW_GR_NEWDL)                   { public boolean run() { return execute_gr_newdl(); } },
		new Command(BKW_GR_HIDE)                    { public boolean run() { return execute_gr_hide(); } },
		new Command(BKW_GR_LINE)                    { public boolean run() { return execute_gr_line(); } },
		new Command(BKW_GR_ONGRTOUCH_RESUME)        { public boolean run() { return execute_gr_touch_resume(); } },
		new Command(BKW_GR_OPEN, CID_OPEN)          { public boolean run() { return execute_gr_open(); } },
		new Command(BKW_GR_ORIENTATION)             { public boolean run() { return execute_gr_orientation(); } },
		new Command(BKW_GR_OVAL)                    { public boolean run() { return execute_gr_oval(); } },
		new Command(BKW_GR_PAINT_GET)               { public boolean run() { return execute_paint_get(); } },
		new Command(BKW_GR_POINT)                   { public boolean run() { return execute_gr_point(); } },
		new Command(BKW_GR_POLY)                    { public boolean run() { return execute_gr_poly(); } },
		new Command(BKW_GR_RECT)                    { public boolean run() { return execute_gr_rect(); } },
		new Command(BKW_GR_ROTATE_END)              { public boolean run() { return execute_gr_rotate_end(); } },
		new Command(BKW_GR_ROTATE_START)            { public boolean run() { return execute_gr_rotate_start(); } },
		new Command(BKW_GR_SAVE)                    { public boolean run() { return execute_gr_save(); } },
		new Command(BKW_GR_SCALE)                   { public boolean run() { return execute_gr_scale(); } },
		new Command(BKW_GR_SCREEN_TO_BITMAP)        { public boolean run() { return execute_screen_to_bitmap(); } },
		new Command(BKW_GR_SCREEN)                  { public boolean run() { return execute_gr_screen(); } },
		new Command(BKW_GR_SET_ANTIALIAS)           { public boolean run() { return execute_gr_antialias(); } },
		new Command(BKW_GR_SET_PIXELS)              { public boolean run() { return execute_gr_set_pixels(); } },
		new Command(BKW_GR_SET_STROKE)              { public boolean run() { return execute_gr_stroke_width(); } },
		new Command(BKW_GR_SHOW)                    { public boolean run() { return execute_gr_show(); } },
		new Command(BKW_GR_STATUSBAR_SHOW)          { public boolean run() { return execute_statusbar_show(); } },
	};

	private final Command[] GrBitmap_cmd = new Command[] {	// Map GR.bitmap command keywords to their execution functions
		new Command(BKW_GR_BITMAP_CREATE)           { public boolean run() { return execute_gr_bitmap_create(); } },
		new Command(BKW_GR_BITMAP_CROP)             { public boolean run() { return execute_gr_bitmap_crop(); } },
		new Command(BKW_GR_BITMAP_DELETE)           { public boolean run() { return execute_gr_bitmap_delete(); } },
		new Command(BKW_GR_BITMAP_DRAWINTO_START)   { public boolean run() { return execute_gr_bitmap_drawinto_start(); } },
		new Command(BKW_GR_BITMAP_DRAWINTO_END)     { public boolean run() { return execute_gr_bitmap_drawinto_end(); } },
		new Command(BKW_GR_BITMAP_DRAW)             { public boolean run() { return execute_gr_bitmap_draw(); } },
		new Command(BKW_GR_BITMAP_LOAD)             { public boolean run() { return execute_gr_bitmap_load(); } },
		new Command(BKW_GR_BITMAP_SAVE)             { public boolean run() { return execute_bitmap_save(); } },
		new Command(BKW_GR_BITMAP_SCALE)            { public boolean run() { return execute_gr_bitmap_scale(); } },
		new Command(BKW_GR_BITMAP_SIZE)             { public boolean run() { return execute_gr_bitmap_size(); } },
	};

	private final Command[] GrCamera_cmd = new Command[] {	// Map GR.camera command keywords to their execution functions
		new Command(BKW_GR_CAMERA_AUTOSHOOT)        { public boolean run() { return execute_camera_shoot(CameraView.PICTURE_MODE_AUTO); } },
		// new Command(BKW_GR_CAMERA_BLINDSHOOT)       { public boolean run() { return execute_camera_shoot(CameraView.PICTURE_MODE_BLIND); } },
		new Command(BKW_GR_CAMERA_MANUALSHOOT)      { public boolean run() { return execute_camera_shoot(CameraView.PICTURE_MODE_MANUAL); } },
		new Command(BKW_GR_CAMERA_SELECT)           { public boolean run() { return execute_gr_camera_select(); } },
		new Command(BKW_GR_CAMERA_SHOOT)            { public boolean run() { return execute_camera_shoot(CameraView.PICTURE_MODE_USE_UI); } },
	};

	private final Command[] GrGet_cmd = new Command[] {		// Map GR.get command keywords to their execution functions
		new Command(BKW_GR_GET_BMPIXEL)             { public boolean run() { return execute_gr_get_bmpixel(); } },
		new Command(BKW_GR_GET_PARAMS)              { public boolean run() { return execute_gr_get_params(); } },
		new Command(BKW_GR_GET_PIXEL)               { public boolean run() { return execute_gr_get_pixel(); } },
		new Command(BKW_GR_GET_POSITION)            { public boolean run() { return execute_gr_get_position(); } },
		new Command(BKW_GR_GET_TEXTBOUNDS)          { public boolean run() { return execute_gr_get_textbounds(); } },
		new Command(BKW_GR_GET_TYPE)                { public boolean run() { return execute_gr_get_type(); } },
		new Command(BKW_GR_GET_VALUE)               { public boolean run() { return execute_gr_get_value(); } },
	};

	private final Command[] GrText_cmd = new Command[] {	// Map GR.text command keywords to their execution functions
		new Command(BKW_GR_TEXT_ALIGN)              { public boolean run() { return execute_gr_text_align(); } },
		new Command(BKW_GR_TEXT_BOLD)               { public boolean run() { return execute_gr_text_bold(); } },
		new Command(BKW_GR_TEXT_DRAW)               { public boolean run() { return execute_gr_text_draw(); } },
		new Command(BKW_GR_TEXT_SIZE)               { public boolean run() { return execute_gr_text_size(); } },
		new Command(BKW_GR_TEXT_SKEW)               { public boolean run() { return execute_gr_text_skew(); } },
		new Command(BKW_GR_TEXT_STRIKE)             { public boolean run() { return execute_gr_text_strike(); } },
		new Command(BKW_GR_TEXT_TYPEFACE)           { public boolean run() { return execute_gr_text_typeface(); } },
		new Command(BKW_GR_TEXT_UNDERLINE)          { public boolean run() { return execute_gr_text_underline(); } },
		new Command(BKW_GR_TEXT_WIDTH)              { public boolean run() { return execute_gr_text_width(); } },
	};

	// ******************************** Variables for Audio commands ****************************

	private static final String BKW_AUDIO_LOAD = "load";
	private static final String BKW_AUDIO_PLAY = "play";
	private static final String BKW_AUDIO_LOOP = "loop";
	private static final String BKW_AUDIO_STOP = "stop";
	private static final String BKW_AUDIO_VOLUME = "volume";
	private static final String BKW_AUDIO_POSITION_CURRENT = "position.current";
	private static final String BKW_AUDIO_POSITION_SEEK = "position.seek";
	private static final String BKW_AUDIO_LENGTH = "length";
	private static final String BKW_AUDIO_RELEASE = "release";
	private static final String BKW_AUDIO_PAUSE = "pause";
	private static final String BKW_AUDIO_ISDONE = "isdone";
	private static final String BKW_AUDIO_RECORD_START = "record.start";
	private static final String BKW_AUDIO_RECORD_STOP = "record.stop";

	private static final String Audio_KW[] = {			// Command list for Format
		BKW_AUDIO_LOAD, BKW_AUDIO_PLAY,
		BKW_AUDIO_LOOP, BKW_AUDIO_STOP, BKW_AUDIO_VOLUME,
		BKW_AUDIO_POSITION_CURRENT, BKW_AUDIO_POSITION_SEEK,
		BKW_AUDIO_LENGTH, BKW_AUDIO_RELEASE, BKW_AUDIO_PAUSE,
		BKW_AUDIO_ISDONE, BKW_AUDIO_RECORD_START, BKW_AUDIO_RECORD_STOP
	};

	private final Command[] audio_cmd = new Command[] {	// Map audio command keywords to their execution functions
		new Command(BKW_AUDIO_LOAD)             { public boolean run() { return execute_audio_load(); } },
		new Command(BKW_AUDIO_PLAY)             { public boolean run() { return execute_audio_play(); } },
		new Command(BKW_AUDIO_LOOP)             { public boolean run() { return execute_audio_loop(); } },
		new Command(BKW_AUDIO_STOP)             { public boolean run() { return execute_audio_stop(); } },
		new Command(BKW_AUDIO_VOLUME)           { public boolean run() { return execute_audio_volume(); } },
		new Command(BKW_AUDIO_POSITION_CURRENT) { public boolean run() { return execute_audio_pcurrent(); } },
		new Command(BKW_AUDIO_POSITION_SEEK)    { public boolean run() { return execute_audio_pseek(); } },
		new Command(BKW_AUDIO_LENGTH)           { public boolean run() { return execute_audio_length(); } },
		new Command(BKW_AUDIO_RELEASE)          { public boolean run() { return execute_audio_release(); } },
		new Command(BKW_AUDIO_PAUSE)            { public boolean run() { return execute_audio_pause(); } },
		new Command(BKW_AUDIO_ISDONE)           { public boolean run() { return execute_audio_isdone(); } },
		new Command(BKW_AUDIO_RECORD_START)     { public boolean run() { return execute_audio_record_start(); } },
		new Command(BKW_AUDIO_RECORD_STOP)      { public boolean run() { return execute_audio_record_stop(); } },
	};

	private static MediaPlayer theMP = null;
	private static ArrayList<MediaPlayer> theMPList;
	private static ArrayList<String> theMPNameList;
	private static boolean PlayIsDone;
	private MediaRecorder mRecorder = null;

	// ******************************* Variables for Sensor Commands **********************************

	private static final String BKW_SENSORS_LIST = "list";
	private static final String BKW_SENSORS_OPEN = "open";
	private static final String BKW_SENSORS_READ = "read";
	private static final String BKW_SENSORS_CLOSE = "close";
	private static final String BKW_SENSORS_ROTATE = "rotate";

	private static final String Sensors_KW[] = {		// Command list for Format
		BKW_SENSORS_LIST, BKW_SENSORS_OPEN,
		BKW_SENSORS_READ, BKW_SENSORS_CLOSE, BKW_SENSORS_ROTATE
	};

	private final Command[] sensors_cmd = new Command[] {	// Map sensor command keywords to their execution functions
		new Command(BKW_SENSORS_LIST)           { public boolean run() { return execute_sensors_list(); } },
		new Command(BKW_SENSORS_OPEN)           { public boolean run() { return execute_sensors_open(); } },
		new Command(BKW_SENSORS_READ)           { public boolean run() { return execute_sensors_read(); } },
		new Command(BKW_SENSORS_CLOSE)          { public boolean run() { return execute_sensors_close(); } },
		new Command(BKW_SENSORS_ROTATE)         { public boolean run() { return execute_sensors_rotate(); } },
	};

	private SensorActivity theSensors;

	// ***********************  Variables for GPS Commands  ******************************************

	private static final String BKW_GPS_ALTITUDE = "altitude";
	private static final String BKW_GPS_LATITUDE = "latitude";
	private static final String BKW_GPS_LONGITUDE = "longitude";
	private static final String BKW_GPS_BEARING = "bearing";
	private static final String BKW_GPS_ACCURACY = "accuracy";
	private static final String BKW_GPS_SPEED = "speed";
	private static final String BKW_GPS_PROVIDER = "provider";
	private static final String BKW_GPS_OPEN = "open";
	private static final String BKW_GPS_CLOSE = "close";
	private static final String BKW_GPS_TIME = "time";

	private static final String GPS_KW[] = {			// Command list for Format
		BKW_GPS_ALTITUDE, BKW_GPS_LATITUDE, BKW_GPS_LONGITUDE,
		BKW_GPS_BEARING, BKW_GPS_ACCURACY, BKW_GPS_SPEED,
		BKW_GPS_PROVIDER, BKW_GPS_OPEN, BKW_GPS_CLOSE, BKW_GPS_TIME
	};

	private final Command[] GPS_cmd = new Command[] {	// Map GPS command keywords to their execution functions
		new Command(BKW_GPS_ALTITUDE)           { public boolean run() { return execute_gps_altitude(); } },
		new Command(BKW_GPS_LATITUDE)           { public boolean run() { return execute_gps_latitude(); } },
		new Command(BKW_GPS_LONGITUDE)          { public boolean run() { return execute_gps_longitude(); } },
		new Command(BKW_GPS_BEARING)            { public boolean run() { return execute_gps_bearing(); } },
		new Command(BKW_GPS_ACCURACY)           { public boolean run() { return execute_gps_accuracy(); } },
		new Command(BKW_GPS_SPEED)              { public boolean run() { return execute_gps_speed(); } },
		new Command(BKW_GPS_PROVIDER)           { public boolean run() { return execute_gps_provider(); } },
		new Command(BKW_GPS_TIME)               { public boolean run() { return execute_gps_time(); } },
		new Command(BKW_GPS_OPEN, CID_OPEN)     { public boolean run() { return execute_gps_open(); } },
		new Command(BKW_GPS_CLOSE)              { public boolean run() { return execute_gps_close(); } },
	};

	private GPS theGPS;

	// ************************* Variables for Array Commands *********************************

	private enum ArrayOrderOps { DoSort, DoShuffle, DoReverse }
	private enum ArrayMathOps { DoSum, DoAverage, DoMin, DoMax, DoVariance, DoStdDev }

	private static final String BKW_ARRAY_LENGTH = "length";
	private static final String BKW_ARRAY_LOAD = "load";
	private static final String BKW_ARRAY_SORT = "sort";
	private static final String BKW_ARRAY_SUM = "sum";
	private static final String BKW_ARRAY_AVERAGE = "average";
	private static final String BKW_ARRAY_REVERSE = "reverse";
	private static final String BKW_ARRAY_SHUFFLE = "shuffle";
	private static final String BKW_ARRAY_MIN = "min";
	private static final String BKW_ARRAY_MAX = "max";
	private static final String BKW_ARRAY_DELETE = "delete";
	private static final String BKW_ARRAY_VARIANCE = "variance";
	private static final String BKW_ARRAY_STD_DEV = "std_dev";
	private static final String BKW_ARRAY_COPY = "copy";
	private static final String BKW_ARRAY_SEARCH = "search";

	private static final String Array_KW[] = {			// Command list for Format
		BKW_ARRAY_LENGTH, BKW_ARRAY_LOAD, BKW_ARRAY_SORT,
		BKW_ARRAY_SUM, BKW_ARRAY_AVERAGE, BKW_ARRAY_REVERSE,
		BKW_ARRAY_SHUFFLE, BKW_ARRAY_MIN, BKW_ARRAY_MAX,
		BKW_ARRAY_DELETE, BKW_ARRAY_VARIANCE, BKW_ARRAY_STD_DEV,
		BKW_ARRAY_COPY, BKW_ARRAY_SEARCH
	};

	private final Command[] array_cmd = new Command[] {	// Map array command keywords to their execution functions
		new Command(BKW_ARRAY_LENGTH)           { public boolean run() { return execute_array_length(); } },
		new Command(BKW_ARRAY_LOAD)             { public boolean run() { return execute_array_load(); } },
		new Command(BKW_ARRAY_DELETE)           { public boolean run() { return executeUNDIM(); } },
		new Command(BKW_ARRAY_REVERSE)          { public boolean run() { return execute_array_collection(ArrayOrderOps.DoReverse); } },
		new Command(BKW_ARRAY_SHUFFLE)          { public boolean run() { return execute_array_collection(ArrayOrderOps.DoShuffle); } },
		new Command(BKW_ARRAY_SORT)             { public boolean run() { return execute_array_collection(ArrayOrderOps.DoSort); } },
		new Command(BKW_ARRAY_SUM)              { public boolean run() { return execute_array_sum(ArrayMathOps.DoSum); } },
		new Command(BKW_ARRAY_AVERAGE)          { public boolean run() { return execute_array_sum(ArrayMathOps.DoAverage); } },
		new Command(BKW_ARRAY_MIN)              { public boolean run() { return execute_array_sum(ArrayMathOps.DoMin); } },
		new Command(BKW_ARRAY_MAX)              { public boolean run() { return execute_array_sum(ArrayMathOps.DoMax); } },
		new Command(BKW_ARRAY_VARIANCE)         { public boolean run() { return execute_array_sum(ArrayMathOps.DoVariance); } },
		new Command(BKW_ARRAY_STD_DEV)          { public boolean run() { return execute_array_sum(ArrayMathOps.DoStdDev); } },
		new Command(BKW_ARRAY_COPY)             { public boolean run() { return execute_array_copy(); } },
		new Command(BKW_ARRAY_SEARCH)           { public boolean run() { return execute_array_search(); } },
	};

	// ************************************ List command variables *********************************

	private static final String BKW_LIST_CREATE = "create";
	private static final String BKW_LIST_ADD_LIST = "add.list";
	private static final String BKW_LIST_ADD_ARRAY = "add.array";
	private static final String BKW_LIST_ADD = "add";
	private static final String BKW_LIST_REPLACE = "replace";
	private static final String BKW_LIST_TYPE = "type";
	private static final String BKW_LIST_GET = "get";
	private static final String BKW_LIST_CLEAR = "clear";
	private static final String BKW_LIST_REMOVE = "remove";
	private static final String BKW_LIST_INSERT = "insert";
	private static final String BKW_LIST_SIZE = "size";
	private static final String BKW_LIST_CONTAINS = "contains";
	private static final String BKW_LIST_TOARRAY = "toarray";
	private static final String BKW_LIST_SEARCH = "search";

	private static final String List_KW[] = {			// Command list for Format
		BKW_LIST_CREATE, BKW_LIST_ADD_LIST,
		BKW_LIST_ADD_ARRAY, BKW_LIST_ADD, BKW_LIST_REPLACE,
		BKW_LIST_TYPE, BKW_LIST_GET, BKW_LIST_CLEAR,
		BKW_LIST_REMOVE, BKW_LIST_INSERT, BKW_LIST_SIZE,
		BKW_LIST_CONTAINS, BKW_LIST_TOARRAY, BKW_LIST_SEARCH
	};

	private final Command[] list_cmd = new Command[] {	// Map list command keywords to their execution functions
		new Command(BKW_LIST_CREATE)            { public boolean run() { return execute_LIST_NEW(); } },
		new Command(BKW_LIST_ADD_LIST)          { public boolean run() { return execute_LIST_ADDLIST(); } },
		new Command(BKW_LIST_ADD_ARRAY)         { public boolean run() { return execute_LIST_ADDARRAY(); } },
		new Command(BKW_LIST_ADD)               { public boolean run() { return execute_LIST_ADD(); } },
		new Command(BKW_LIST_REPLACE)           { public boolean run() { return execute_LIST_SET(); } },
		new Command(BKW_LIST_TYPE)              { public boolean run() { return execute_LIST_GETTYPE(); } },
		new Command(BKW_LIST_GET)               { public boolean run() { return execute_LIST_GET(); } },
		new Command(BKW_LIST_CLEAR)             { public boolean run() { return execute_LIST_CLEAR(); } },
		new Command(BKW_LIST_REMOVE)            { public boolean run() { return execute_LIST_REMOVE(); } },
		new Command(BKW_LIST_INSERT)            { public boolean run() { return execute_LIST_INSERT(); } },
		new Command(BKW_LIST_SIZE)              { public boolean run() { return execute_LIST_SIZE(); } },
		new Command(BKW_LIST_CONTAINS)          { public boolean run() { return execute_LIST_CONTAINS(); } },
		new Command(BKW_LIST_TOARRAY)           { public boolean run() { return execute_LIST_TOARRAY(); } },
		new Command(BKW_LIST_SEARCH)            { public boolean run() { return execute_LIST_SEARCH(); } },
	};

	private static final int list_is_numeric = 1;
	private static final int list_is_string = 0;

	public static ArrayList <ArrayList> theLists;
	public static ArrayList <Integer> theListsType;

	// ************************************ Bundle Variables ****************************************

	private static final String BKW_BUNDLE_CREATE = "create";
	private static final String BKW_BUNDLE_PUT = "put";
	private static final String BKW_BUNDLE_GET = "get";
	private static final String BKW_BUNDLE_TYPE = "type";
	private static final String BKW_BUNDLE_KEYS = "keys";
	private static final String BKW_BUNDLE_COPY = "copy";
	private static final String BKW_BUNDLE_CLEAR = "clear";
	private static final String BKW_BUNDLE_CONTAIN = "contain";

	private static final String Bundle_KW[] = {			// Command list for Format
		BKW_BUNDLE_CREATE, BKW_BUNDLE_PUT, BKW_BUNDLE_GET,
		BKW_BUNDLE_TYPE, BKW_BUNDLE_KEYS,
		BKW_BUNDLE_COPY, BKW_BUNDLE_CLEAR, BKW_BUNDLE_CONTAIN
	};

	private final Command[] bundle_cmd = new Command[] {// Map bundle command keywords to their execution functions
		new Command(BKW_BUNDLE_CREATE)          { public boolean run() { return execute_BUNDLE_CREATE(); } },
		new Command(BKW_BUNDLE_PUT)             { public boolean run() { return execute_BUNDLE_PUT(); } },
		new Command(BKW_BUNDLE_GET)             { public boolean run() { return execute_BUNDLE_GET(); } },
		new Command(BKW_BUNDLE_TYPE)            { public boolean run() { return execute_BUNDLE_TYPE(); } },
		new Command(BKW_BUNDLE_KEYS)            { public boolean run() { return execute_BUNDLE_KEYSET(); } },
		new Command(BKW_BUNDLE_COPY)            { public boolean run() { return execute_BUNDLE_COPY(); } },
		new Command(BKW_BUNDLE_CLEAR)           { public boolean run() { return execute_BUNDLE_CLEAR(); } },
		new Command(BKW_BUNDLE_CONTAIN)         { public boolean run() { return execute_BUNDLE_CONTAIN(); } },
	};

	private static ArrayList <Bundle> theBundles;

	// *********************************** Stack Variables **********************************************

	private static final String BKW_STACK_CREATE = "create";
	private static final String BKW_STACK_PUSH = "push";
	private static final String BKW_STACK_POP = "pop";
	private static final String BKW_STACK_PEEK = "peek";
	private static final String BKW_STACK_TYPE = "type";
	private static final String BKW_STACK_ISEMPTY = "isempty";
	private static final String BKW_STACK_CLEAR = "clear";

	private static final String Stack_KW[] = {			// Command list for Format
		BKW_STACK_CREATE, BKW_STACK_PUSH, BKW_STACK_POP,
		BKW_STACK_PEEK, BKW_STACK_TYPE,
		BKW_STACK_ISEMPTY, BKW_STACK_CLEAR
	};

	private final Command[] stack_cmd = new Command[] {	// Map stack command keywords to their execution functions
		new Command(BKW_STACK_CREATE)           { public boolean run() { return execute_STACK_CREATE(); } },
		new Command(BKW_STACK_PUSH)             { public boolean run() { return execute_STACK_PUSH(); } },
		new Command(BKW_STACK_POP)              { public boolean run() { return execute_STACK_POP(); } },
		new Command(BKW_STACK_PEEK)             { public boolean run() { return execute_STACK_PEEK(); } },
		new Command(BKW_STACK_TYPE)             { public boolean run() { return execute_STACK_TYPE(); } },
		new Command(BKW_STACK_ISEMPTY)          { public boolean run() { return execute_STACK_ISEMPTY(); } },
		new Command(BKW_STACK_CLEAR)            { public boolean run() { return execute_STACK_CLEAR(); } },
	};

	private static ArrayList<Stack> theStacks;
	private static ArrayList <Integer> theStacksType; 

	private static final int stack_is_numeric = 1;
	private static final int stack_is_string = 0;

//  ******************************* Socket Variables **************************************************

	// socket commands
	private static final String BKW_SOCKET_MYIP = "myip";
	private static final String BKW_SOCKET_CLIENT_GROUP = "client.";
	private static final String BKW_SOCKET_SERVER_GROUP = "server.";

	// socket subgroup commands
	private static final String BKW_SOCKET_CLIENT_IP = "client.ip";
	private static final String BKW_SOCKET_CLOSE = "close";
	private static final String BKW_SOCKET_CONNECT = "connect";
	private static final String BKW_SOCKET_CREATE = "create";
	private static final String BKW_SOCKET_DISCONNECT = "disconnect";
	private static final String BKW_SOCKET_READ_FILE = "read.file";
	private static final String BKW_SOCKET_READ_LINE = "read.line";
	private static final String BKW_SOCKET_READ_READY = "read.ready";
	private static final String BKW_SOCKET_SERVER_IP = "server.ip";
	private static final String BKW_SOCKET_STATUS = "status";
	private static final String BKW_SOCKET_WRITE_BYTES = "write.bytes";
	private static final String BKW_SOCKET_WRITE_FILE = "write.file";
	private static final String BKW_SOCKET_WRITE_LINE = "write.line";

	private static final String Socket_KW[] = {			// Command list for Format
		BKW_SOCKET_MYIP,

		// SOCKET subgroups - Format can handle only one level of grouping
		// SOCKET.CLIENT. subgroup
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_CONNECT,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_STATUS,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_READ_READY,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_READ_LINE,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_WRITE_LINE,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_WRITE_BYTES,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_CLOSE,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_SERVER_IP,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_READ_FILE,
		BKW_SOCKET_CLIENT_GROUP + BKW_SOCKET_WRITE_FILE,
		// SOCKET.SERVER. subgroup
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_CREATE,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_CONNECT,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_STATUS,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_READ_READY,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_READ_LINE,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_WRITE_LINE,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_WRITE_BYTES,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_DISCONNECT,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_CLOSE,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_CLIENT_IP,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_READ_FILE,
		BKW_SOCKET_SERVER_GROUP + BKW_SOCKET_WRITE_FILE,
	};

	private final Command[] Socket_cmd = new Command[] {		// Map Socket command keywords to their execution functions
		new Command(BKW_SOCKET_CLIENT_GROUP, CID_GROUP) { public boolean run() { return executeSocketClient(); } },
		new Command(BKW_SOCKET_SERVER_GROUP, CID_GROUP) { public boolean run() { return executeSocketServer(); } },
		new Command(BKW_SOCKET_MYIP)                    { public boolean run() { return executeMYIP(); } }
	};

	private final Command[] SocketClient_cmd = new Command[] {	// Map Socket.client command keywords to their execution functions
		new Command(BKW_SOCKET_CONNECT)         { public boolean run() { return executeCLIENT_CONNECT(); } },
		new Command(BKW_SOCKET_STATUS)          { public boolean run() { return executeCLIENT_STATUS(); } },
		new Command(BKW_SOCKET_READ_READY)      { public boolean run() { return executeCLIENT_READ_READY(); } },
		new Command(BKW_SOCKET_READ_LINE)       { public boolean run() { return executeCLIENT_READ_LINE(); } },
		new Command(BKW_SOCKET_WRITE_LINE)      { public boolean run() { return executeCLIENT_WRITE_LINE(); } },
		new Command(BKW_SOCKET_WRITE_BYTES)     { public boolean run() { return executeCLIENT_WRITE_BYTES(); } },
		new Command(BKW_SOCKET_CLOSE)           { public boolean run() { return executeCLIENT_CLOSE(); } },
		new Command(BKW_SOCKET_SERVER_IP)       { public boolean run() { return executeCLIENT_SERVER_IP(); } },
		new Command(BKW_SOCKET_READ_FILE)       { public boolean run() { return executeCLIENT_GETFILE(); } },
		new Command(BKW_SOCKET_WRITE_FILE)      { public boolean run() { return executeCLIENT_PUTFILE(); } }
	};

	private final Command[] SocketServer_cmd = new Command[] {	// Map Socket.server command keywords to their execution functions
		new Command(BKW_SOCKET_CREATE)          { public boolean run() { return executeSERVER_CREATE(); } },
		new Command(BKW_SOCKET_CONNECT)         { public boolean run() { return executeSERVER_ACCEPT(); } },
		new Command(BKW_SOCKET_STATUS)          { public boolean run() { return executeSERVER_STATUS(); } },
		new Command(BKW_SOCKET_READ_READY)      { public boolean run() { return executeSERVER_READ_READY(); } },
		new Command(BKW_SOCKET_READ_LINE)       { public boolean run() { return executeSERVER_READ_LINE(); } },
		new Command(BKW_SOCKET_WRITE_LINE)      { public boolean run() { return executeSERVER_WRITE_LINE(); } },
		new Command(BKW_SOCKET_WRITE_BYTES)     { public boolean run() { return executeSERVER_WRITE_BYTES(); } },
		new Command(BKW_SOCKET_DISCONNECT)      { public boolean run() { return executeSERVER_DISCONNECT(); } },
		new Command(BKW_SOCKET_CLOSE)           { public boolean run() { return executeSERVER_CLOSE(); } },
		new Command(BKW_SOCKET_CLIENT_IP)       { public boolean run() { return executeSERVER_CLIENT_IP(); } },
		new Command(BKW_SOCKET_READ_FILE)       { public boolean run() { return executeSERVER_GETFILE(); } },
		new Command(BKW_SOCKET_WRITE_FILE)      { public boolean run() { return executeSERVER_PUTFILE(); } }
	};

	// Constants that indicate the current connection state
	public static final int STATE_NOT_ENABLED = -1;	// channel is not enabled, or server socket not created
	public static final int STATE_NONE = 0;			// channel is doing nothing (initial state)
	public static final int STATE_LISTENING = 1;	// now listening for incoming connections
	public static final int STATE_CONNECTING = 2;	// now initiating an outgoing connection
	public static final int STATE_CONNECTED = 3;	// now connected to a remote device
	public static final int STATE_READING = 4;		// now reading from a remote device
	public static final int STATE_WRITING = 5;		// now writing to a remote device

	private int clientSocketState;
	private int serverSocketState;

	private Socket theClientSocket;
	private ClientSocketConnectThread clientSocketConnectThread;
	private BufferedReader ClientBufferedReader;
	private PrintWriter ClientPrintWriter;

	private ServerSocket newSS;
	private ServerSocketConnectThread serverSocketConnectThread;
	private Socket theServerSocket;
	private BufferedReader ServerBufferedReader;
	private PrintWriter ServerPrintWriter;

	// *************************************************** Debug Commands ****************************

	private static final String BKW_DEBUG_ON = "on";
	private static final String BKW_DEBUG_OFF = "off";
	private static final String BKW_DEBUG_PRINT = "print";
	private static final String BKW_DEBUG_ECHO_ON = "echo.on";
	private static final String BKW_DEBUG_ECHO_OFF = "echo.off";
	private static final String BKW_DEBUG_DUMP_SCALARS = "dump.scalars";
	private static final String BKW_DEBUG_DUMP_ARRAY = "dump.array";
	private static final String BKW_DEBUG_DUMP_LIST = "dump.list";
	private static final String BKW_DEBUG_DUMP_STACK = "dump.stack";
	private static final String BKW_DEBUG_DUMP_BUNDLE = "dump.bundle";
	private static final String BKW_DEBUG_WATCH_CLEAR = "watch.clear";
	private static final String BKW_DEBUG_WATCH = "watch";
	private static final String BKW_DEBUG_SHOW_SCALARS = "show.scalars";
	private static final String BKW_DEBUG_SHOW_ARRAY = "show.array";
	private static final String BKW_DEBUG_SHOW_LIST = "show.list";
	private static final String BKW_DEBUG_SHOW_STACK = "show.stack";
	private static final String BKW_DEBUG_SHOW_BUNDLE = "show.bundle";
	private static final String BKW_DEBUG_SHOW_WATCH = "show.watch";
	private static final String BKW_DEBUG_SHOW_PROGRAM = "show.program";
	private static final String BKW_DEBUG_SHOW = "show";
	private static final String BKW_DEBUG_CONSOLE = "console";

	private static final String Debug_KW[] = {			// Command list for Format
		BKW_DEBUG_ON, BKW_DEBUG_OFF, BKW_DEBUG_PRINT, BKW_DEBUG_ECHO_ON,
		BKW_DEBUG_ECHO_OFF, BKW_DEBUG_DUMP_SCALARS,
		BKW_DEBUG_DUMP_ARRAY, BKW_DEBUG_DUMP_LIST,
		BKW_DEBUG_DUMP_STACK, BKW_DEBUG_DUMP_BUNDLE,
		BKW_DEBUG_WATCH_CLEAR, BKW_DEBUG_WATCH, BKW_DEBUG_SHOW_SCALARS,
		BKW_DEBUG_SHOW_ARRAY, BKW_DEBUG_SHOW_LIST, BKW_DEBUG_SHOW_STACK,
		BKW_DEBUG_SHOW_BUNDLE, BKW_DEBUG_SHOW_WATCH, BKW_DEBUG_SHOW_PROGRAM,
		BKW_DEBUG_SHOW, BKW_DEBUG_CONSOLE
	};

	private final Command[] debug_cmd = new Command[] {	// Map debug command keywords to their execution functions
		new Command(BKW_DEBUG_ON)               { public boolean run() { return executeDEBUG_ON(); } },
		new Command(BKW_DEBUG_OFF)              { public boolean run() { return executeDEBUG_OFF(); } },
		new Command(BKW_DEBUG_PRINT)            { public boolean run() { return executeDEBUG_PRINT(); } },
		new Command(BKW_DEBUG_ECHO_ON)          { public boolean run() { return executeECHO_ON(); } },
		new Command(BKW_DEBUG_ECHO_OFF)         { public boolean run() { return executeECHO_OFF(); } },
		new Command(BKW_DEBUG_DUMP_SCALARS)     { public boolean run() { return executeDUMP_SCALARS(); } },
		new Command(BKW_DEBUG_DUMP_ARRAY)       { public boolean run() { return executeDUMP_ARRAY(); } },
		new Command(BKW_DEBUG_DUMP_LIST)        { public boolean run() { return executeDUMP_LIST(); } },
		new Command(BKW_DEBUG_DUMP_STACK)       { public boolean run() { return executeDUMP_STACK(); } },
		new Command(BKW_DEBUG_DUMP_BUNDLE)      { public boolean run() { return executeDUMP_BUNDLE(); } },
		new Command(BKW_DEBUG_WATCH_CLEAR)      { public boolean run() { return executeDEBUG_WATCH_CLEAR(); } },
		new Command(BKW_DEBUG_WATCH)            { public boolean run() { return executeDEBUG_WATCH(); } },
		new Command(BKW_DEBUG_SHOW_SCALARS)     { public boolean run() { return executeDEBUG_SHOW_SCALARS(); } },
		new Command(BKW_DEBUG_SHOW_ARRAY)       { public boolean run() { return executeDEBUG_SHOW_ARRAY(); } },
		new Command(BKW_DEBUG_SHOW_LIST)        { public boolean run() { return executeDEBUG_SHOW_LIST(); } },
		new Command(BKW_DEBUG_SHOW_STACK)       { public boolean run() { return executeDEBUG_SHOW_STACK(); } },
		new Command(BKW_DEBUG_SHOW_BUNDLE)      { public boolean run() { return executeDEBUG_SHOW_BUNDLE(); } },
		new Command(BKW_DEBUG_SHOW_WATCH)       { public boolean run() { return executeDEBUG_SHOW_WATCH(); } },
		new Command(BKW_DEBUG_SHOW_PROGRAM)     { public boolean run() { return executeDEBUG_SHOW_PROGRAM(); } },
		new Command(BKW_DEBUG_SHOW)             { public boolean run() { return executeDEBUG_SHOW(); } },
		new Command(BKW_DEBUG_CONSOLE)          { public boolean run() { return executeDEBUG_CONSOLE(); } },
	};

	public static boolean Debug = false;
	public static boolean Echo = false;

	// *********************************************** Text to Speech *******************************

	private static final String BKW_TTS_INIT = "init";
	private static final String BKW_TTS_SPEAK_TOFILE = "speak.tofile";
	private static final String BKW_TTS_SPEAK = "speak";
	private static final String BKW_TTS_STOP = "stop";

	private static final String tts_KW[] = {			// TTS command list for Format
		BKW_TTS_INIT, BKW_TTS_SPEAK_TOFILE,
		BKW_TTS_SPEAK, BKW_TTS_STOP
	};

	private final Command[] tts_cmd = new Command[] {	// Map TTS command keywords to their execution functions
		new Command(BKW_TTS_INIT)               { public boolean run() { return executeTTS_INIT(); } },
		new Command(BKW_TTS_SPEAK_TOFILE)       { public boolean run() { return executeTTS_SPEAK_TOFILE(); } },
		new Command(BKW_TTS_SPEAK)              { public boolean run() { return executeTTS_SPEAK(); } },
		new Command(BKW_TTS_STOP)               { public boolean run() { return executeTTS_STOP(); } }
	};

	public static TextToSpeechActivity theTTS;
	public static boolean ttsInit;

	// *********************************************** FTP Client *************************************

	private static final String BKW_FTP_OPEN = "open";
	private static final String BKW_FTP_CLOSE = "close";
	private static final String BKW_FTP_DIR = "dir";
	private static final String BKW_FTP_CD = "cd";
	private static final String BKW_FTP_GET = "get";
	private static final String BKW_FTP_PUT = "put";
	private static final String BKW_FTP_DELETE = "delete";
	private static final String BKW_FTP_RMDIR = "rmdir";
	private static final String BKW_FTP_MKDIR = "mkdir";
	private static final String BKW_FTP_RENAME = "rename";

	private static final String ftp_KW[] = {			// FTP command list for Format
		BKW_FTP_OPEN, BKW_FTP_CLOSE, BKW_FTP_DIR, BKW_FTP_CD,
		BKW_FTP_GET, BKW_FTP_PUT, BKW_FTP_DELETE, BKW_FTP_RMDIR,
		BKW_FTP_MKDIR, BKW_FTP_RENAME
	};

	private final Command[] ftp_cmd = new Command[] {	// Map FTP command keywords to their execution functions
		new Command(BKW_FTP_OPEN)               { public boolean run() { return executeFTP_OPEN(); } },
		new Command(BKW_FTP_CLOSE)              { public boolean run() { return executeFTP_CLOSE(); } },
		new Command(BKW_FTP_DIR)                { public boolean run() { return executeFTP_DIR(); } },
		new Command(BKW_FTP_CD)                 { public boolean run() { return executeFTP_CD(); } },
		new Command(BKW_FTP_GET)                { public boolean run() { return executeFTP_GET(); } },
		new Command(BKW_FTP_PUT)                { public boolean run() { return executeFTP_PUT(); } },
		new Command(BKW_FTP_DELETE)             { public boolean run() { return executeFTP_DELETE(); } },
		new Command(BKW_FTP_RMDIR)              { public boolean run() { return executeFTP_RMDIR(); } },
		new Command(BKW_FTP_MKDIR)              { public boolean run() { return executeFTP_MKDIR(); } },
		new Command(BKW_FTP_RENAME)             { public boolean run() { return executeFTP_RENAME(); } },
	};

	public FTPClient mFTPClient = null;
	public String FTPdir = null;

	// *********************************************** Camera *****************************************

	public static Bitmap CameraBitmap;
	public static boolean CameraDone;
	private int CameraNumber;
	private int NumberOfCameras;			// -1 if we don't know yet

	// ***************************************  Bluetooth  ********************************************

    // Message types sent from the BluetoothChatService
    public static final int MESSAGE_STATE_CHANGE = MESSAGE_BT_GROUP + 1;
    public static final int MESSAGE_READ         = MESSAGE_BT_GROUP + 2;
    public static final int MESSAGE_WRITE        = MESSAGE_BT_GROUP + 3;
    public static final int MESSAGE_DEVICE_NAME  = MESSAGE_BT_GROUP + 4;

    // Key names received from the BluetoothChatService
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_ENABLE_BT = 3;
    
    public static  UUID MY_UUID_SECURE =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static  UUID MY_UUID_INSECURE =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    
    public static int bt_enabled = 0;
    public static int bt_state;
    public static boolean bt_Secure;

    // Name of the connected device
    public static String mConnectedDeviceName = null;
    // Array adapter for the conversation thread
    public static StringBuffer mOutStringBuffer;
    // Local Bluetooth adapter
    public static BluetoothAdapter mBluetoothAdapter = null;
    // Member object for the chat services
    public static BluetoothChatService mChatService = null;
    //
    public static ArrayList <String> BT_Read_Buffer;
    public static BluetoothDevice btConnectDevice = null;
    public static boolean btReadReady = false;
    public static int OnBTReadLine = 0;

	private static final String BKW_BT_OPEN = "open";
	private static final String BKW_BT_CLOSE = "close";
	private static final String BKW_BT_STATUS = "status";
	private static final String BKW_BT_CONNECT = "connect";
	private static final String BKW_BT_DEVICE_NAME = "device.name";
	private static final String BKW_BT_WRITE = "write";
	private static final String BKW_BT_READ_READY = "read.ready";
	private static final String BKW_BT_READ_BYTES = "read.bytes";
	private static final String BKW_BT_SET_UUID = "set.uuid";
	private static final String BKW_BT_LISTEN = "listen";
	private static final String BKW_BT_RECONNECT = "reconnect";
	private static final String BKW_BT_ONREADREADY_RESUME = "onreadready.resume";
	private static final String BKW_BT_DISCONNECT = "disconnect";

	private static final String bt_KW[] = {				// Bluetooth command list for Format
		BKW_BT_OPEN, BKW_BT_CLOSE, BKW_BT_STATUS,
		BKW_BT_CONNECT, BKW_BT_DEVICE_NAME,
		BKW_BT_WRITE, BKW_BT_READ_READY, BKW_BT_READ_BYTES,
		BKW_BT_SET_UUID, BKW_BT_LISTEN, BKW_BT_RECONNECT,
		BKW_BT_ONREADREADY_RESUME, BKW_BT_DISCONNECT
	};

	private final Command[] bt_cmd = new Command[] {	// Map Bluetooth command keywords to their execution functions
		new Command(BKW_BT_OPEN,   CID_OPEN)    { public boolean run() { return execute_BT_open(); } },
		new Command(BKW_BT_CLOSE)               { public boolean run() { return execute_BT_close(); } },
		new Command(BKW_BT_STATUS, CID_STATUS)  { public boolean run() { return execute_BT_status(); } },
		new Command(BKW_BT_CONNECT)             { public boolean run() { return execute_BT_connect(); } },
		new Command(BKW_BT_DEVICE_NAME)         { public boolean run() { return execute_BT_device_name(); } },
		new Command(BKW_BT_WRITE)               { public boolean run() { return execute_BT_write(); } },
		new Command(BKW_BT_READ_READY)          { public boolean run() { return execute_BT_read_ready(); } },
		new Command(BKW_BT_READ_BYTES)          { public boolean run() { return execute_BT_read_bytes(); } },
		new Command(BKW_BT_SET_UUID)            { public boolean run() { return execute_BT_set_uuid(); } },
		new Command(BKW_BT_LISTEN)              { public boolean run() { return execute_BT_listen(); } },
		new Command(BKW_BT_RECONNECT)           { public boolean run() { return execute_BT_reconnect(); } },
		new Command(BKW_BT_ONREADREADY_RESUME)  { public boolean run() { return execute_BT_readReady_Resume(); } },
		new Command(BKW_BT_DISCONNECT)          { public boolean run() { return execute_BT_disconnect(); } },
	};

	/**************************************  Superuser and System  ***************************/

	private static final String BKW_SU_OPEN = "open";
	private static final String BKW_SU_WRITE = "write";
	private static final String BKW_SU_READ_READY = "read.ready";
	private static final String BKW_SU_READ_LINE = "read.line";
	private static final String BKW_SU_CLOSE = "close";

	private static final String su_KW[] = {				// Command list for Format
		BKW_SU_OPEN, BKW_SU_WRITE, BKW_SU_READ_READY,
		BKW_SU_READ_LINE, BKW_SU_CLOSE
	};
	private static final String[] System_KW = su_KW;	// Command list for Format

	private final Command[] SU_cmd = new Command[] {	// Map SU/System command keywords to their execution functions
		new Command(BKW_SU_OPEN, CID_OPEN)      { public boolean run() { return execute_SU_open(); } },
		new Command(BKW_SU_WRITE)               { public boolean run() { return execute_SU_write(); } },
		new Command(BKW_SU_READ_READY)          { public boolean run() { return execute_SU_read_ready(); } },
		new Command(BKW_SU_READ_LINE)           { public boolean run() { return execute_SU_read_line(); } },
		new Command(BKW_SU_CLOSE)               { public boolean run() { return execute_SU_close(); } }
	};

    private boolean isSU = true;						// set true for SU commands, false for System commands
    private DataOutputStream SUoutputStream;
    private DataInputStream SUinputStream;
    private Process SUprocess;
    private ArrayList <String> SU_ReadBuffer;
    private SUReader theSUReader = null;

	/***************************************  SOUND POOL  ************************************/

	private static final String BKW_SOUNDPOOL_OPEN = "open";
	private static final String BKW_SOUNDPOOL_LOAD = "load";
	private static final String BKW_SOUNDPOOL_PLAY = "play";
	private static final String BKW_SOUNDPOOL_STOP = "stop";
	private static final String BKW_SOUNDPOOL_UNLOAD = "unload";
	private static final String BKW_SOUNDPOOL_PAUSE = "pause";
	private static final String BKW_SOUNDPOOL_RESUME = "resume";
	private static final String BKW_SOUNDPOOL_RELEASE = "release";
	private static final String BKW_SOUNDPOOL_SETVOLUME = "setvolume";
	private static final String BKW_SOUNDPOOL_SETPRIORITY = "setpriority";
	private static final String BKW_SOUNDPOOL_SETLOOP = "setloop";
	private static final String BKW_SOUNDPOOL_SETRATE = "setrate";

	private static final String sp_KW[] = {				// Command list for Format
		BKW_SOUNDPOOL_OPEN, BKW_SOUNDPOOL_LOAD,
		BKW_SOUNDPOOL_PLAY, BKW_SOUNDPOOL_STOP,
		BKW_SOUNDPOOL_UNLOAD, BKW_SOUNDPOOL_PAUSE,
		BKW_SOUNDPOOL_RESUME, BKW_SOUNDPOOL_RELEASE,
		BKW_SOUNDPOOL_SETVOLUME, BKW_SOUNDPOOL_SETPRIORITY,
		BKW_SOUNDPOOL_SETLOOP, BKW_SOUNDPOOL_SETRATE
	};

	private final Command[] sp_cmd = new Command[] {	// Map soundpool command keywords to their execution functions
		new Command(BKW_SOUNDPOOL_OPEN, CID_OPEN)   { public boolean run() { return execute_SP_open(); } },
		new Command(BKW_SOUNDPOOL_LOAD)             { public boolean run() { return execute_SP_load(); } },
		new Command(BKW_SOUNDPOOL_PLAY)             { public boolean run() { return execute_SP_play(); } },
		new Command(BKW_SOUNDPOOL_STOP)             { public boolean run() { return execute_SP_stop(); } },
		new Command(BKW_SOUNDPOOL_UNLOAD)           { public boolean run() { return execute_SP_unload(); } },
		new Command(BKW_SOUNDPOOL_PAUSE)            { public boolean run() { return execute_SP_pause(); } },
		new Command(BKW_SOUNDPOOL_RESUME)           { public boolean run() { return execute_SP_resume(); } },
		new Command(BKW_SOUNDPOOL_RELEASE)          { public boolean run() { return execute_SP_release(); } },
		new Command(BKW_SOUNDPOOL_SETVOLUME)        { public boolean run() { return execute_SP_setvolume(); } },
		new Command(BKW_SOUNDPOOL_SETPRIORITY)      { public boolean run() { return execute_SP_setpriority(); } },
		new Command(BKW_SOUNDPOOL_SETLOOP)          { public boolean run() { return execute_SP_setloop(); } },
		new Command(BKW_SOUNDPOOL_SETRATE)          { public boolean run() { return execute_SP_setrate(); } },
	};

	public static SoundPool theSoundPool ;

	// *************************************** Ringer Vars ****************************************

	private static final String BKW_RINGER_GET_MODE = "get.mode";
	private static final String BKW_RINGER_SET_MODE = "set.mode";
	private static final String BKW_RINGER_GET_VOLUME = "get.volume";
	private static final String BKW_RINGER_SET_VOLUME = "set.volume";

	private static final String ringer_KW[] = {			// Command list for Format
		BKW_RINGER_GET_MODE, BKW_RINGER_SET_MODE,
		BKW_RINGER_GET_VOLUME, BKW_RINGER_SET_VOLUME
	};

	private final Command[] ringer_cmd = new Command[] {	// Map ringer command keywords to their execution functions
			new Command(BKW_RINGER_GET_MODE)    { public boolean run() { return executeRINGER_GET_MODE(); } },
			new Command(BKW_RINGER_SET_MODE)    { public boolean run() { return executeRINGER_SET_MODE(); } },
			new Command(BKW_RINGER_GET_VOLUME)  { public boolean run() { return executeRINGER_GET_VOLUME(); } },
			new Command(BKW_RINGER_SET_VOLUME)  { public boolean run() { return executeRINGER_SET_VOLUME(); } },
	};

	private static final int RINGER_UNKNOWN = -1;
	private static final int RINGER_SILENT = 0;
	private static final int RINGER_VIBRATE = 1;
	private static final int RINGER_NORMAL = 2;

	// **************** Headset Vars **************************************

	int headsetState;
	String headsetName;
	int headsetMic;

	//******************* html Vars ******************************************

	private static final String BKW_HTML_OPEN = "open";
	private static final String BKW_HTML_ORIENTATION = "orientation";
	private static final String BKW_HTML_LOAD_URL = "load.url";
	private static final String BKW_HTML_LOAD_STRING = "load.string";
	private static final String BKW_HTML_GET_DATALINK = "get.datalink";
	private static final String BKW_HTML_CLOSE = "close";
	private static final String BKW_HTML_GO_BACK = "go.back";
	private static final String BKW_HTML_GO_FORWARD = "go.forward";
	private static final String BKW_HTML_CLEAR_CACHE = "clear.cache";
	private static final String BKW_HTML_CLEAR_HISTORY = "clear.history";
	private static final String BKW_HTML_POST = "post";

	private static final String html_KW[] = {			// Command list for Format
		BKW_HTML_OPEN, BKW_HTML_ORIENTATION,
		BKW_HTML_LOAD_URL, BKW_HTML_LOAD_STRING,
		BKW_HTML_GET_DATALINK, BKW_HTML_CLOSE, BKW_HTML_GO_BACK,
		BKW_HTML_GO_FORWARD, BKW_HTML_CLEAR_CACHE,
		BKW_HTML_CLEAR_HISTORY, BKW_HTML_POST
	};

	private final Command[] html_cmd = new Command[] {	// Map HTML command keywords to their execution functions
		new Command(BKW_HTML_OPEN, CID_OPEN)    { public boolean run() { return execute_html_open(); } },
		new Command(BKW_HTML_ORIENTATION)       { public boolean run() { return execute_html_orientation(); } },
		new Command(BKW_HTML_LOAD_URL)          { public boolean run() { return execute_html_load_url(); } },
		new Command(BKW_HTML_LOAD_STRING)       { public boolean run() { return execute_html_load_string(); } },
		new Command(BKW_HTML_GET_DATALINK,
						CID_DATALINK)           { public boolean run() { return execute_html_get_datalink(); } },
		new Command(BKW_HTML_CLOSE, CID_CLOSE)  { public boolean run() { return execute_html_close(); } },
		new Command(BKW_HTML_GO_BACK)           { public boolean run() { return execute_html_go_back(); } },
		new Command(BKW_HTML_GO_FORWARD)        { public boolean run() { return execute_html_go_forward(); } },
		new Command(BKW_HTML_CLEAR_CACHE)       { public boolean run() { return execute_html_clear_cache(); } },
		new Command(BKW_HTML_CLEAR_HISTORY)     { public boolean run() { return execute_html_clear_history(); } },
		new Command(BKW_HTML_POST)              { public boolean run() { return execute_html_post(); } },
	};

	// Message types for the HTML commands
	private static final int MESSAGE_HTML_OPEN     = MESSAGE_HTML_GROUP + 1;
	private static final int MESSAGE_GO_BACK       = MESSAGE_HTML_GROUP + 2;
	private static final int MESSAGE_GO_FORWARD    = MESSAGE_HTML_GROUP + 3;
	private static final int MESSAGE_CLEAR_CACHE   = MESSAGE_HTML_GROUP + 4;
	private static final int MESSAGE_CLEAR_HISTORY = MESSAGE_HTML_GROUP + 5;
	private static final int MESSAGE_LOAD_URL      = MESSAGE_HTML_GROUP + 6;
	private static final int MESSAGE_LOAD_STRING   = MESSAGE_HTML_GROUP + 7;
	private static final int MESSAGE_POST          = MESSAGE_HTML_GROUP + 8;

	public static ArrayList <String> htmlData_Buffer;
	private Intent htmlIntent;
	private boolean htmlOpening;

	public static boolean Notified;

	//********************* SMS Vars ***********************************

	private static final String BKW_SMS_RCV_INIT = "rcv.init";
	private static final String BKW_SMS_RCV_NEXT = "rcv.next";
	private static final String BKW_SMS_SEND = "send";

	private static final String SMS_KW[] = {			// Command list for Format
		BKW_SMS_RCV_INIT, BKW_SMS_RCV_NEXT, BKW_SMS_SEND
	};

	private final Command[] sms_cmd = new Command[] {	// Map SMS command keywords to their execution functions
		new Command(BKW_SMS_RCV_INIT)           { public boolean run() { return executeSMS_RCV_INIT(); } },
		new Command(BKW_SMS_RCV_NEXT)           { public boolean run() { return executeSMS_RCV_NEXT(); } },
		new Command(BKW_SMS_SEND)               { public boolean run() { return executeSMS_SEND(); } }
	};

	public static ArrayList <String> smsRcvBuffer;

	// ******************** Speech to text Vars ********************************

	public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	public static ArrayList <String> sttResults;
	public static boolean sttListening;
	public static boolean sttDone;

	// ******************** Timer Variables *******************************

	private static final String BKW_TIMER_SET = "set";
	private static final String BKW_TIMER_CLEAR = "clear";
	private static final String BKW_TIMER_RESUME = "resume";

	private static final String Timer_KW[] = {			// Command list for Format
		BKW_TIMER_SET, BKW_TIMER_CLEAR, BKW_TIMER_RESUME
	};

	private final Command[] Timer_cmd = new Command[] {	// Map Timer command keywords to their execution functions
		new Command(BKW_TIMER_SET)              { public boolean run() { return executeTIMER_SET(); } },
		new Command(BKW_TIMER_CLEAR)            { public boolean run() { return executeTIMER_CLEAR(); } },
		new Command(BKW_TIMER_RESUME)           { public boolean run() { return executeTIMER_RESUME(); } }
	};

	public static int OnTimerLine;
	public static Timer theTimer;
	public static boolean timerExpired;
	public static boolean timerStarting;

	// ******************** TimeZone Variables *******************************

	private static final String BKW_TIMEZONE_SET = "set";
	private static final String BKW_TIMEZONE_GET = "get";
	private static final String BKW_TIMEZONE_LIST = "list";

	private static final String TimeZone_KW[] = {		// Command list for Format
		BKW_TIMEZONE_SET, BKW_TIMEZONE_GET, BKW_TIMEZONE_LIST
	};

	private final Command[] TimeZone_cmd = new Command[] {	// Map TimeZone command keywords to their execution functions
		new Command(BKW_TIMEZONE_SET)           { public boolean run() { return executeTIMEZONE_SET(); } },
		new Command(BKW_TIMEZONE_GET)           { public boolean run() { return executeTIMEZONE_GET(); } },
		new Command(BKW_TIMEZONE_LIST)          { public boolean run() { return executeTIMEZONE_LIST(); } }
	};

	public String theTimeZone = "";

	//************************ Phone variables ***************************

	private static final String BKW_PHONE_CALL = "call";
	private static final String BKW_PHONE_RCV_INIT = "rcv.init";
	private static final String BKW_PHONE_RCV_NEXT = "rcv.next";

	private static final String phone_KW[] = {			// Command list for Format
		BKW_PHONE_CALL, BKW_PHONE_RCV_INIT, BKW_PHONE_RCV_NEXT
	};

	private final Command[] phone_cmd = new Command[] {	// Map phone command keywords to their execution functions
		new Command(BKW_PHONE_CALL)             { public boolean run() { return executePHONE_CALL(); } },
		new Command(BKW_PHONE_RCV_INIT)         { public boolean run() { return executePHONE_RCV_INIT(); } },
		new Command(BKW_PHONE_RCV_NEXT)         { public boolean run() { return executePHONE_RCV_NEXT(); } }
	};

	public static int phoneState = 0;
	public static String phoneNumber = "";
	public static boolean phoneRcvInited = false;
	public static TelephonyManager mTM;

	// ****************** Headset Broadcast Receiver ***********************

	public class BroadcastsHandler extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
				String data = intent.getDataString();
				Bundle extraData = intent.getExtras();

				headsetState = intent.getIntExtra("state", -1);
				headsetName = intent.getStringExtra("name");
				headsetMic = intent.getIntExtra("microphone", -1);
			}
		}
	}
	private BroadcastsHandler headsetBroadcastReceiver = null;

/*  ***********************************  Start of Basic's run program code **********************
 * 
 * The code is organized (?) as follows:
 * 
 * The first chunk is the the background task doInBackGround. This  runs in a background thread.
 * It actually controls the running of the program by dispatching each line of code to be executed.
 * 
 * The second chunk is a foreground (UI) thread code that receives user interface messages from
 * doInBackground
 * 
 *  The third chunk is the Run Intent entry code. This code initializes things and starts
 *  running the background task.
 *  
 *  The next chunk is the methods that handle asynchronous events generally created by user
 *  key presses. These methods run in the foreground (UI) task.
 *  
 *  The final chunk and the largest part of Run is code that actually executes user program statements.
 *  This code all runs in the background task.
 *  
 */


	public class Background extends Thread {

	// The execution of the basic program is done by this background Thread. This is done to keep the UI task
	// responsive. This method controls Run as it is running in the background.

		private UncaughtExceptionHandler mDefaultExceptionHandler;

		private UncaughtExceptionHandler mUncaughtExceptionHandler =
			new UncaughtExceptionHandler() {
				public void uncaughtException(Thread thread, Throwable ex) {
					if (ex instanceof OutOfMemoryError) {
						finishRun("Out of memory");
					} else if (ex instanceof NullPointerException) {
						publishProgress("Internal error! Please notify developer.");
						finishRun("Null pointer exception");
					} else {
						mDefaultExceptionHandler.uncaughtException(thread, ex);
					}
				}

				private void finishRun(String err) {
					publishProgress(err + ", near line:");
					publishProgress(ExecutingLineBuffer);
					SyntaxError = true;		// This blocks "Program ended" checks in finishUp()
					OnErrorLine = 0;		// Don't allow OnError: to catch OOM, it's fatal
					finishUp();
				}
			};

		@Override
		public void run() {

//			Basic.Echo = Settings.getEcho(Basic.BasicContext);
			Echo = false;
			VarSearchStart = 0;
			fnRTN = false;
			setVolumeControlStream(AudioManager.STREAM_MUSIC);

			mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);

			if (PreScan()) { 				// The execution starts by scanning the source for labels and read.data
				ExecutingLineIndex = 0;		// Just in case PreScan ever changes it
				RunLoop();
				finishUp();
			} else {						// We get here if PreScan found error or duplicate label
				for (int i = 0; i < TempOutputIndex; ++i) {			// if new output lines, the send them
					publishProgress(TempOutput[i]);					// to UI task
				}
				TempOutputIndex = 0;
			}
			if (Exit) {
				finish();
			}
		}

		private void finishUp() {			// Called from run() when done running, and from UncaughtExceptionHandler

			Stop = true;		// If Stop is not already set, set it so that menu code can display the right thing
			PrintLine = "";		// Clear the Print Line buffer
			PrintLineReady = false;
			textPrintLine = "";

			OnBackKeyLine = 0;

			if (OnErrorLine == 0 && !SyntaxError && !Exit) {
				if (!ForNextStack.empty())	{ publishProgress("Program ended with FOR without NEXT"); }
				if (!WhileStack.empty())	{ publishProgress("Program ended with WHILE without REPEAT"); }
				if (!DoStack.empty())		{ publishProgress("Program ended with DO without UNTIL"); }
			}

			Basic.theRunContext = null;  // Signals that the background task has stopped
			cleanUp();
		}

		private void publishProgress(String... s) {
			mHandler.obtainMessage(MESSAGE_PUBLISH_PROGRESS, s).sendToTarget();
		}

		// The RunLoop() drives the execution of the program. It is called from doInBackground and
		// recursively from doUserFunction.

        public  boolean RunLoop(){
        	boolean flag = true;
        	do {
        		if (ExecutingLineIndex >= Basic.lines.size())break;
        		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);  // Next program line
//        		Log.v(LOGTAG, "Background.RunLoop: " + ExecutingLineBuffer);
        		LineIndex = 0 ;
        		sTime = SystemClock.uptimeMillis();
 
        		flag = StatementExecuter();							// execute the next statement
        															// returns true if no problems executing statement
        		if (Exit) return flag;								// if Exit skip all other processing
        		
        		if (!flag && (OnErrorLine != 0)){                   // If Error and there is an OnError label
        			ExecutingLineIndex = OnErrorLine;               // Go to the OnError line
        			SyntaxError = false;
        			flag = true;									// and indicate no error
        		} else
        		
        		if (BackKeyHit && OnBackKeyLine != 0) {
        				BackKeyHit = doInterrupt(OnBackKeyLine);
        			
        		} else

        		if (MenuKeyHit && OnMenuKeyLine != 0) {
        				MenuKeyHit = doInterrupt(OnMenuKeyLine);
        			
        		} else

        		if (timerExpired && OnTimerLine != 0) {
        				timerExpired = doInterrupt(OnTimerLine);
        			
        		} else
        		
        		if (KeyPressed && OnKeyLine != 0) {
        				KeyPressed = doInterrupt(OnKeyLine);
         			
        		} else

        		if (NewTouch[2] && OnTouchLine != 0) {							// and is not tracked like NewTouch[0] and NewTouch[1]
        				NewTouch[2] = doInterrupt(OnTouchLine);					// used with onGRtouch.
        			
        		} else
        			
        		if (btReadReady && OnBTReadLine != 0) {
        				btReadReady = doInterrupt(OnBTReadLine);
        			
        		} else
        		
        		if (ConsoleTouched && onCTLine != 0) {
        				ConsoleTouched = doInterrupt(onCTLine);
        			
        		} else

        		if (bgStateChange && OnBGLine != 0) {
        				bgStateChange = doInterrupt(OnBGLine);
        		}
       		
        		for (int i=0; i<TempOutputIndex; ++i){				// if new output lines, the send them
        			publishProgress(TempOutput[i]);					// to UI task
        			}
        		TempOutputIndex = 0;
				
				// Debugger control
				// Michael/paulon0n also defined a @@J for "Alert dialog var not set called". TODO?
				while (WaitForResume) {
					Thread.yield();
					if (DebuggerHalt) {
						publishProgress("Execution halted");
						Stop = true;
					}
					if (DebuggerStep) {
						DebuggerStep = false;
						sendMessage(MESSAGE_DEBUG_DIALOG);			// signal UI to run debugger dialog again
						break;
					}
					if (dbSwap) {
						WaitForSwap = true;
						sendMessage(MESSAGE_DEBUG_SWAP);
						while (WaitForSwap) {
							Thread.yield();
							/*
							if(dbSelect){
								dbSelect = false;
								WaitForSelect = true;
								sendMessage(MESSAGE_DEBUG_SELECT);
								while (WaitForSelect) {
									Thread.yield();
								}
							}
							*/
						}
						dbSwap = false;
						sendMessage(MESSAGE_DEBUG_DIALOG);			// signal UI to run debugger dialog again
					}
				}

				while (WaitForInput && !Stop) {						// if waiting for Input
					try {Thread.sleep(500);}catch(InterruptedException e){}
					if (BadInput) {									// If user input was bad
						publishProgress("@@1");						// tell her and try again
					} else if (InputCancelled) {					// User hit back on Input Dialog
						WaitForInput = false;
						if (OnErrorLine != 0) {
							ExecutingLineIndex = OnErrorLine;
						} else {									// Tell user we are stopping
							publishProgress("Input dialog cancelled", "Execution halted");
							Stop = true;							// and stop executing
						}
					}
				}

        		++ExecutingLineIndex;								// Step to next line

        		if (fnRTN){					// fn_rtn signal. If true make RunLoop() return
        			fnRTN = false;			// to doUserFunction
        			return true;
        		}
        		
    		} while (ExecutingLineIndex < Basic.lines.size() && flag && !Stop) ;  //keep executing statements until end
        	return flag;
        }

        private void checkpointProgress() {		// give Run methods a way to checkpoint publishProgress
            ProgressPending = true;
            publishProgress("@@9");				// signal foreground task to clear ProgressPending
        }

        private boolean doInterrupt(int gotoLine) {
        	if (interruptResume != -1) return true;		// If we are handling an interrupt then do not cancel this one
        	interruptResume = ExecutingLineIndex;		// Set the resume Line Number
        	ExecutingLineIndex = gotoLine;				// Set the goto line number
        	interruptVarSearchStart = VarSearchStart;	// Save current VarSearchStart
        	VarSearchStart = 0;							// Force to predictable value
        	IfElseStack.push(IEinterrupt);
        	return false;								//Turn off the interrupt
        }

    } // End of Background class

	private Context getContext() {
		return GRFront ? GR.context : this;
	}

	// These sendMessage methods are used by Background to send messages to mHandler.
	// For convenience, there are several combinations of message parameters provided.

	private void sendMessage(int what) {
		mHandler.obtainMessage(what).sendToTarget();
	}

	private void sendMessage(int what, Object obj) {			// Use this to send a String or other Object
		mHandler.obtainMessage(what, obj).sendToTarget();
	}

	private void sendMessage(int what, int arg1, int arg2) {	// Use this to send one or two int arguments
		mHandler.obtainMessage(what, arg1, arg2).sendToTarget();
	}

	// This Handler is in the UI (foreground) Task part of Run.
	// It gets control when the background task sends a Message.

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what & MESSAGE_GROUP_MASK) {
			case MESSAGE_DEFAULT_GROUP:
				switch (msg.what) {
				case MESSAGE_INPUT_DIALOG:
					if (BadInput) {
						BadInput = false;
						Toaster("Not a number. Try Again.").show();
					}
					theAlertDialog = doInputDialog().show();
					break;
				case MESSAGE_TOAST:
					Toast toast = Toaster(ToastMsg, ToastDuration);
					toast.setGravity(Gravity.CENTER, ToastX, ToastY);
					toast.show();
					break;
				case MESSAGE_CLEAR_SCREEN:
					output.clear();
					break;
				case MESSAGE_CONSOLE_TITLE:
					String title = (String)msg.obj;
					setTitle((title != null) ? title : getResources().getString(R.string.run_name));
					break;
				case MESSAGE_PUBLISH_PROGRESS:
					onProgressUpdate((String[])msg.obj);
					break;
				default:
				}
				break;
			case MESSAGE_BT_GROUP:
				handleBTMessage(msg);								// handle Bluetooth Messages
				break;
			case MESSAGE_HTML_GROUP:
				handleHtmlMessage(msg);								// handle HTML Messages
				break;
			case MESSAGE_DEBUG_GROUP:
				handleDebugMessage(msg);							// handle debug Messages
				break;
			default:												// unrecognized Message
				break;												// ignore it
			}
		}
	};

	protected void onProgressUpdate(String... str) {

		for (int i=0; i<str.length; ++i) {								// Check for signals
			if (str[i].startsWith("@")) {								// Fast check for possible signal
				if (str[i].startsWith("@@1")){      					// Input dialog signal
					if (BadInput) {
						BadInput = false;
						Toaster("Not a number. Try Again.").show();
					}
					theAlertDialog = doInputDialog().show();
				}else if (str[i].startsWith("@@4")){					// Does the toast for popup command
					Toast toast = Toaster(ToastMsg, ToastDuration);
					toast.setGravity(Gravity.CENTER, ToastX, ToastY);
					toast.show();
        		}else if (str[i].startsWith("@@5")){					// Clear Screen Signal
        			output.clear();
        		}else if (str[i].startsWith("@@9")){					// from checkpointProgress
        			ProgressPending = false;							// progress is published, done waiting
        		}else if (str[i].startsWith("@@A")){
        			output.add("");
        		}else if (str[i].startsWith("@@B")){
        			char c = str[i].charAt(3);
        			String s = output.get(output.size()-1) + c;
        			output.set(output.size()-1, s);
        		} else {output.add(str[i]);}		// Not a signal, just write msg to screen.
			} else {output.add(str[i]);}			// Not a signal, just write msg to screen.

			setListAdapter(AA);						// show the output
			lv.setSelection(output.size()-1);		// set last line as the selected line to scroll
//			Log.d(LOGTAG, "onProgressUpdate: print <" + str[i] + ">");
		} // for each string
	} // onProgressUpdate()


	// ********************* Run Entry Point. Called from Editor or AutoRun *******************

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.v(LOGTAG, CLASSTAG + " On Create " + ExecutingLineIndex );

		if (Basic.lines == null){
			Log.e(LOGTAG, CLASSTAG + ".onCreate: Basic.lines null. Restarting BASIC!.");
			Intent intent = new Intent(getApplicationContext(), Basic.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();
			return;
		}

//		System.gc();
//		Log.v(LOGTAG, CLASSTAG + " isOld  " + isOld);
		if (isOld){
			if (theWakeLock != null){
				theWakeLock.release();
			}
			if (theWifiLock != null){
				theWifiLock.release();
			}
		}
		theWakeLock = null;
		theWifiLock = null;
		isOld = true;

		InitVars();
//		Log.v(LOGTAG, CLASSTAG + " On Create 2 " + ExecutingLineIndex );

		myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
															// Establish the output screen
		AA = new Basic.ColoredTextAdapter(this, output, Settings.getConsoleTypeface(this));
		setListAdapter(AA);
		lv = getListView();
		lv.setTextFilterEnabled(false);
		lv.setSelection(0);
		lv.setBackgroundColor(AA.backgroundColor);
		if (Settings.getLinedConsole(this)) {
			lv.setDivider(new ColorDrawable(AA.lineColor));				// override default from theme, sometimes it's invisible
			if (lv.getDividerHeight() < 1) { lv.setDividerHeight(1); }	// make sure the divider shows
		} else {
			lv.setDividerHeight(0);							// don't show the divider
		}

//		IMM.restartInput(lv);
		executeKB_HIDE();
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setRequestedOrientation(Settings.getSreenOrientation(this));
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		headsetBroadcastReceiver = new BroadcastsHandler();
		this.registerReceiver(headsetBroadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

		Basic.theRunContext = this;

		// Listeners for Console Touch

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (onCTLine != 0) {
					TouchedConsoleLine = position + 1;
					ConsoleLongTouch = false; 
					ConsoleTouched = true;
				}
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean  onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (onCTLine != 0) {
					TouchedConsoleLine = position + 1;
					ConsoleLongTouch = true; 
					ConsoleTouched = true;
					return true;
				}
				return false;
			}
		});

		theBackground = new Background();						// Start the background task
		theBackground.start();

	}

private void InitVars(){

    OnErrorLine = 0;							// Line number for OnError: label
    OnBackKeyLine = 0;
    BackKeyHit = false;
    OnMenuKeyLine = 0;
    MenuKeyHit = false;
    bgStateChange = false;
    OnBGLine = 0;
    onCTLine = 0;
    ConsoleTouched = false;
    ConsoleLongTouch = false;
    TouchedConsoleLine = 0;						// first valid line number is 1


    errorMsg = "No error";
    
    InChar = new ArrayList<String>();
    KeyPressed = false;
    OnKeyLine = 0;
	
    LineIndex = 0;				// Current displacement into ExecutingLineBuffer
    ExecutingLineBuffer ="\n";		// Holds the current line being executed
    ExecutingLineIndex = 0;		// Points to the current line in Basic.lines
    SEisLE = false;				// If a String expression result is a logical expression

    GosubStack = new Stack<Integer>();			// Stack used for Gosub/Return
    ForNextStack = new	Stack<Bundle>();		// Stack used for For/Next
    WhileStack = new Stack<Integer>() ;			// Stack used for While/Repeat
    DoStack = new Stack<Integer>();				// Stack used for Do/Until
    
    IfElseStack = new Stack <Integer>() ;			// Stack for IF-ELSE-ENDIF operations
    GetNumberValue = (double)0;				// Return value from GetNumber()
    EvalNumericExpressionValue = (double)0;	// Return value from EvalNumericExprssion()

    output = new ArrayList<String>();				// The output screen text lines
//    AA = new ArrayAdapter<String>;				// The output screen array adapter
//    lv = new ListView();							// The output screen list view
   TempOutput = new String[MaxTempOutput];	// Holds waiting output screen line
    TempOutputIndex = 0;					// Index to next available TempOutput entry

    theBackground = null;					// Background task ID
    SyntaxError = false;		        // Set true when Syntax Error message has been output

   ProgressPending = false;
   randomizer = null;
   background = false;

	// debugger ui vars
	Watch_VarNames = new ArrayList<String>();			// watch list of string names
	WatchVarIndex = new ArrayList<Integer>();			// watch list of variable indexes
	dbDialogScalars = false;
	dbDialogArray = false;
	dbDialogList = false;
	dbDialogStack = false;
	dbDialogBundle = false;
	dbDialogWatch = false;
	dbDialogProgram = true;
	dbConsoleHistory = "";
	dbConsoleExecute = "";
	dbConsoleELBI = 0;
	WatchedArray = -1;
	WatchedList =-1;
	WatchedStack =-1;
	WatchedBundle =-1;
	dbSwap = false;
	dbDialog = null;
	dbSwapDialog = null;
	dbSelectDialog = null;

	Stop = false;										// Stops program from running
	Exit = false;										// Exits program and signals caller to exit, too
	GraphicsPaused = false;								// Signal from GR that it has been paused
	RunPaused = false;									// Used to control the media player
	StopDisplay = false;
	GRFront = false;
	DisplayStopped = false;
	IMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

	PrintLine = "";										// Hold the Print line currently being built
	PrintLineReady = false;								// Signals a line is ready to print or write

	Labels = new HashMap<String, Integer>();			// A list of all labels and associated line numbers

	VarNames = new ArrayList<String>() ;				// Each entry has the variable name string
	VarIndex = new ArrayList<Integer>();				// Each entry is an index into [...]
	VarNumber = 0;										// An index for both VarNames and NVarValue
	NumericVarValues = new ArrayList<Double>();			// if a var is a number, the VarIndex is an [...]
	StringVarValues  = new ArrayList<String>();			// if a var is a string, the VarIndex is an [...]
	ArrayTable = new ArrayList<Bundle>();				// Each DIMed array has an entry in this table
	StringConstant = "";								// Storage for a string constant
	theValueIndex = 0;									// The index into the value table for the current var
	ArrayValueStart = 0;								// Value index for newly created array 

	FunctionTable = new ArrayList<Bundle>();			// A bundle is created for each defined function
	ufBundle = null ;									// Bundle set by isUserFunction and used by doUserFunction
	FunctionStack = new Stack<Bundle>() ;				// Stack contains the currently executing functions
	fnRTN = false;										// Set true by fn.rtn. Cause RunLoop() to return
	Debug = false;

	VarIsNew = true;									// Signal from getVar() that this var is new
	VarIsNumeric = true;								// if false, var is a string
	VarIsInt = false;									// temporary integer status used only by fprint
	VarIsArray = false;									// if true, var is an array
	VarIsFunction = false;								// Flag set by parseVar() when var is a user function
	VarSearchStart = 0;									// Used to limit search for var names to executing function vars
	interruptVarSearchStart = 0;						// Save VarSearchStart across interrupt

	clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

	// ******************************* File I/O operation variables ************************

	FileTable = new ArrayList<Bundle>() ;				// File table list
	BRlist = new ArrayList<BufferedReader>() ;			// A list of of file readers
	FWlist = new ArrayList<FileWriter>() ;				// A list of file writers
	DOSlist = new ArrayList<DataOutputStream> () ;		// A list of output streams
	BISlist = new ArrayList<BufferedInputStream> () ;	// A list of input streams

	// ******************** READ variables *******************************************

	readNext = 0;
	readData = new ArrayList <Bundle>();

	// ******************** Input Command variables ********************************

	WaitForInput = false;
	BadInput = false;
	InputCancelled = false;
	InputIsNumeric = true;
	InputVarIndex = -1;
	UserPrompt = "";
	InputDefault = "";
	InputDismissed = false;
	theAlertDialog = null;

	// ******************** Popup Command variables ********************************

	ToastMsg = "";
	ToastX = 0 ;
	ToastY = 0;
	ToastDuration = 1;

	// ******************** SQL Variables ******************************************

	DataBases = new ArrayList<SQLiteDatabase>();	// List of created data bases
	Cursors = new ArrayList<Cursor>();				// List of created data bases

	// ********************************* Variables for text.input command ********************

	TextInputString = "";
	HaveTextInput = false;

	// ******************************** Graphics Declarations **********************************

    GRclass = null;									// Graphics Intent Class
    GRopen = false;									// Graphics Open Flag
    DisplayList = new ArrayList<Bundle>() ;
    RealDisplayList = new ArrayList<Integer>() ;
    PaintList = new ArrayList<Paint>();
    BitmapList = new ArrayList<Bitmap>();
    PixelPoints = new ArrayList<Float> ();
    aPaint = new Paint();
	PaintList = new ArrayList<Paint>();
    GRrunning = false;
    OnTouchLine = 0;
    Touched = false;

    // ********************************* Variables for Audio Commands

    theMP = null;
    theMPList = new ArrayList<MediaPlayer>();
    theMPNameList = new ArrayList<String>();
    theMPList.add(null);		// We don't use the [0] element of these Lists
    theMPNameList.add(null);

	// ******************************* Variables for Sensor Commands **********************************

	theSensors = null;

	theGPS = null;

	theLists = new ArrayList <ArrayList>();
	ArrayList<ArrayList> aList = new ArrayList <ArrayList>();
	theLists.add(aList);
	
	theListsType = new ArrayList <Integer>();
	theListsType.add(0);
	
	theBundles = new ArrayList<Bundle>();
	Bundle aBundle = new Bundle();
	theBundles.add(aBundle);
	
	theStacks = new ArrayList<Stack>();
	Stack aStack = new Stack();
	theStacks.add(aStack);
	
	theStacksType = new ArrayList<Integer>();
	theStacksType.add(0);
	
	theClientSocket = null ;
	clientSocketConnectThread = null;
	ClientBufferedReader = null;
	ClientPrintWriter = null;
	
	newSS = null;
	serverSocketConnectThread = null;
	theServerSocket = null ;
	ServerBufferedReader = null ;
	ServerPrintWriter = null ;

	clientSocketState = STATE_NONE;
	serverSocketState = STATE_NONE;

	theTTS = null;
	ttsInit = false;
	
	mFTPClient = null;
	FTPdir = null;
	
	CameraBitmap = null;
	CameraDone = true;
	NumberOfCameras = -1;

	mConnectedDeviceName = null;
    mOutStringBuffer = null;
    mBluetoothAdapter = null;
    mChatService = null;
    btReadReady = false;
    interruptResume = -1;
    OnBTReadLine = 0;
    
    SUoutputStream = null;
    SUinputStream = null;
    SUprocess = null;
    SU_ReadBuffer = null;
    theSUReader = null;
    
    theSoundPool = null ;
    
     headsetState = -1;
     headsetName = "NA" ;
     headsetMic = -1;
     
     htmlIntent = null;
     htmlOpening = false;
     
     sttListening = false;
     
     OnTimerLine = 0;
     theTimer = null;
     timerExpired = false;
     
     theTimeZone = "";
     
	 phoneState = 0;
	 phoneNumber = "";
	 phoneRcvInited = false;
	 mTM = null;

}

public void cleanUp(){
	if (theMP != null){
		try {theMP.stop();} catch (IllegalStateException e){}
		if (theMP != null) theMP.release();
		theMP = null;
	}
	
	  if (theSoundPool != null){
			  theSoundPool.release();
			  theSoundPool = null;
		  }
	  
	if ( Web.aWebView != null) Web.aWebView.webClose();

	if (theTimer != null) {
	   theTimer.cancel();
	   theTimer = null;
	}

	ttsStop();

	myVib.cancel();

	if (theMP != null) {
		theMP.release();
		theMP = null;
	}
	if (theMPList != null) {
		for (MediaPlayer mp : theMPList) {
			if (mp != null) { mp.release(); }
		}
		theMPList = null;
		theMPNameList = null;
	}

	if (theSensors != null) {
		theSensors.stop();
		theSensors = null;
	}

	if (theServerSocket != null){
	try{
		theServerSocket.close();
		}catch (Exception e) {
		}
	theServerSocket = null;
	}

	if (clientSocketConnectThread != null) {
		clientSocketConnectThread.interrupt();
		clientSocketConnectThread = null;
	}

	if (serverSocketConnectThread != null) {
		serverSocketConnectThread.interrupt();
		serverSocketConnectThread = null;
	}

	if (newSS != null){
		try{
			newSS.close();
			}catch (Exception e) {
			}
			newSS = null;
		}

	if (theClientSocket != null){
		try{
			theClientSocket.close();
			}catch (Exception e) {
			}
			theClientSocket = null;
		}

	clientSocketState = STATE_NONE;
	serverSocketState = STATE_NONE;

	audioRecordStop();

	Stop = true;										// make sure the background task stops
	Basic.theRunContext = null;
	GraphicsPaused = false;
	RunPaused = false;
	ProgressPending = false;

	if (theGPS != null) {
		Log.d(LOGTAG, "Stopping GPS from cleanUp");
		theGPS.stop();
		theGPS = null;
	}

	if (!Basic.DoAutoRun && SyntaxError){
		Editor.SyntaxErrorDisplacement = ExecutingLineIndex;
	} else Editor.SyntaxErrorDisplacement = -1;
	
	for (int i = 0; i <BitmapList.size(); ++i ){
		   Bitmap SrcBitMap = BitmapList.get(i);
		   if (SrcBitMap != null)
		      SrcBitMap.recycle();
		   BitmapList.set(i, null);
		}
	BitmapList.clear();

	
		if (mChatService != null) {
				mChatService.stop();
				mChatService = null;
			}

			if ( theSUReader != null ){
				theSUReader.stop();
				theSUReader = null;
			}
			if (SUprocess != null) {
				SUprocess.destroy();
				SUprocess = null;
			}
			
//			InitVars();   // Why not?
			
			finishActivity(BASIC_GENERAL_INTENT);

}


// The following methods run in the foreground. The are called by asynchronous user events
// caused by the user pressing a key.

@Override
public boolean onTouchEvent(MotionEvent event){
	super.onTouchEvent(event);
	int action = event.getAction();  // Get action type
	return false;
}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)  {						// The user hit a key
		// Log.v(LOGTAG, CLASSTAG + " onKeyDown" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (OnMenuKeyLine != 0) {
				MenuKeyHit = true;
				return true;
			}
			if (Basic.isAPK)			// If menu key hit in APK and not trapped by OnMenuKey									
				return true;			// then tell OS to ignore it
			
			return false;				// Let Android create the Run Menu.
		}

		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (OnBackKeyLine != 0){
				BackKeyHit = true;
				return true;
			}

			if (Basic.DoAutoRun) Exit = true;	// If AutoRun, back key always means exit
			if (!Stop) {
				Stop = true;				// If running a program, stop it
			}
			else finish();				// else already stopped, return to the Editor
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	

	@Override
	public boolean onKeyUp( int keyCode, KeyEvent event)  {
		// Log.v(LOGTAG, CLASSTAG + " onKeyUp" + keyCode);

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (OnMenuKeyLine != 0) {
				MenuKeyHit = true;
				return true;
			}
			if (Basic.isAPK)			// If menu key hit in APK and not trapped by OnMenuKey									
				return true;			// then tell OS to ignore it
			
			return false;				// Let Android create the Run Menu.
		}
		
		if (kbShown)
			IMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


		if (keyCode == KeyEvent.KEYCODE_BACK && OnBackKeyLine != 0) return true;
	    
	    
	    char c;
	    String theKey = "@";
	    int n ;
	    if (keyCode >= 7 && keyCode <= 16){
	    	n = keyCode - 7;
	    	c = Numbers.charAt(n);
	    	theKey = Character.toString(c);
	    	
	    }else if (keyCode >=29 && keyCode <= 54){
	    	n = keyCode -29;
	    	c = Chars.charAt(n);
	    	theKey = Character.toString(c);
	    }else if (keyCode == 62){ 
	    	c = ' ';
	    	theKey = Character.toString(c);
	    }else if (keyCode >= 19 && keyCode <= 23){
	    	switch (keyCode) {
	    	case 19: theKey = "up"; break;
	    	case 20: theKey = "down"; break;
	    	case 21: theKey = "left"; break;
	    	case 22: theKey = "right"; break;
	    	case 23: theKey = "go"; break;
	    	}
	    } else {
	    	theKey = "key " + keyCode;
	    }
	    
	    synchronized (this) {
	    	InChar.add(theKey);
	    }
    	KeyPressed = true;

	    return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		// Called when the menu key is pressed.
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.run, menu);
		MenuItem item = menu.getItem(1);
		if (Basic.DoAutoRun) {							// If APK or shortcut, menu action is "Exit", not "Editor"
			item.setTitle(getString(R.string.exit));
		}
		item.setEnabled(false);
		return true;
	}

@Override
public boolean onPrepareOptionsMenu(Menu menu) {   // Executed when Menu key is pressed (before onCreateOptionsMenu() above.
	
    super.onPrepareOptionsMenu(menu);
    MenuItem item;
    if (Stop){									   // If program running display with Editor dimmed
 	   item = menu.getItem(0);					   // Other wise dim stop and undim Editor
	   item.setEnabled(false);
    	item = menu.getItem(1);
    	item.setEnabled(true);
    }
    return true;
}

public static void MenuStop() {
	Show("Stopped by user.");							// Tell user and then
	Stop = true;										// signal main loop to stop
	OnBackKeyLine = 0;
}

@Override
public  boolean onOptionsItemSelected(MenuItem item) {  // A menu item is selected
	switch (item.getItemId()) {

	case R.id.stop:										// User wants to stop execution
		MenuStop();
		return true;

	case R.id.editor:									// User pressed Editor
		if (!Basic.DoAutoRun && SyntaxError) {
			Editor.SyntaxErrorDisplacement = ExecutingLineIndex;
		}

		Basic.theRunContext = null;
		if (mChatService != null) {
			mChatService.stop();
			mChatService = null;
		}

		finish();

	}
	return true;
}

@Override
protected void onResume() {
	Log.v(LOGTAG, CLASSTAG + " On Resume " + kbShown);

	  RunPaused = false;
	  background = false;
	  bgStateChange = true;
//	  if (Stop) InitVars();

//	  if (WaitForInput) theAlertDialog = doInputDialog().show(); maybe???
	  super.onResume();

}

@Override
protected void onPause() {	
	// The Android OS wants me to dismiss dialog while paused so I will
	
/*	if (WaitForInput){
		theAlertDialog.dismiss();
		InputDismissed = true;
		}*/
	  
	// If there is a Media Player running, pause it and hope
	// that it works.
	Log.v(LOGTAG, CLASSTAG + " onPause " + kbShown);
	if (kbShown)  IMM.hideSoftInputFromWindow(lv.getWindowToken(), 0);
	
/*	  if (theMP != null){
		  try {theMP.pause();} catch (IllegalStateException e){}
		  }
*/	  
	  RunPaused = true;

	super.onPause();
}

@Override
protected void onStart(){
	Log.v(LOGTAG, CLASSTAG + " On Start");
//	InitVars();
	super.onStart();
}

@Override
protected void onStop(){
	Log.v(LOGTAG, CLASSTAG + " onStop " + kbShown);
	System.gc();
	if (!GRrunning) {
		background = true;
		bgStateChange = true;
//		if (kbShown)  IMM.hideSoftInputFromWindow(lv.getWindowToken(), 0);

	}
	super.onStop();
}

@Override
protected void onRestart(){
	Log.v(LOGTAG, CLASSTAG + " onRestart");

	super.onRestart();

}

@Override
protected void onDestroy(){
	Log.v(LOGTAG, CLASSTAG + " On Destroy");

	if (theSensors != null) {
		theSensors.stop();
		theSensors = null;
	}

	if (theGPS != null) {
		Log.d(LOGTAG, "Stopping GPS from onDestroy");
		theGPS.stop();
		theGPS = null;
	}

	if (theWakeLock != null){
		theWakeLock.release();
		theWakeLock = null;
	}

	if (theWifiLock != null){
		theWifiLock.release();
		theWifiLock = null;
	}

	if (BitmapList != null) {
		for (int i = 0; i <BitmapList.size(); ++i) {
			Bitmap SrcBitMap = BitmapList.get(i);
			if (SrcBitMap != null)
				SrcBitMap.recycle();
			BitmapList.set(i, null);
		}
		BitmapList.clear();
		   System.gc();
	}

	if (headsetBroadcastReceiver != null) {
		unregisterReceiver(headsetBroadcastReceiver);
	}

	super.onDestroy();
}

@Override
public void onLowMemory(){
	Show("Warning: Low Memory");
}

//********************** Everything from here to end runs in the background task ***********************

// The methods starting here are the core code for running a Basic program

private void trimArray(ArrayList Array, int start){
	int last = Array.size()-1;
	int k = last;
	while (k>=start){
		Array.remove(k);
		--k;
	}
}

// Look for a BASIC! word: [_@#\l]?[_@#\l\d]*
private String getWord(String line, int start, boolean stopOnPossibleKeyword) {
	int max = line.length();
	if (start >= max || start < 0) { return ""; }
	int li = start;
	char c = line.charAt(li);
	if ((c >= 'a' && c <= 'z') || c == '_' || c == '@' || c == '#') { // if first character matches
		do {														// there's a word
			if (++li >= max) break; 								// done if no more characters

			if (stopOnPossibleKeyword &&							// caller wants to stop at keyword
				line.startsWith(PossibleKeyWord, li)) { break; }	// THEN, TO, or STEP

			c = line.charAt(li);									// get next character
		}
		while ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') ||	// and check it, stop if not valid
				c == '_' || c == '@' || c == '#');
	}
	return line.substring(start, li);
}

private void getInterruptLabels() {								// check for interrupt labels
	Integer line;
	line = Labels.get("onerror");        OnErrorLine   = (line == null) ? 0 : line.intValue();
	line = Labels.get("onbackkey");      OnBackKeyLine = (line == null) ? 0 : line.intValue();
	line = Labels.get("onmenukey");      OnMenuKeyLine = (line == null) ? 0 : line.intValue();
	line = Labels.get("ontimer");        OnTimerLine   = (line == null) ? 0 : line.intValue();
	line = Labels.get("onkeypress");     OnKeyLine     = (line == null) ? 0 : line.intValue();
	line = Labels.get("ongrtouch");      OnTouchLine   = (line == null) ? 0 : line.intValue();
	line = Labels.get("onbtreadready");  OnBTReadLine  = (line == null) ? 0 : line.intValue();
	line = Labels.get("onbackground");   OnBGLine      = (line == null) ? 0 : line.intValue();
	line = Labels.get("onconsoletouch"); onCTLine      = (line == null) ? 0 : line.intValue();
}

// Scan the entire program. Find all the labels and read.data statements.
// Note: at present it seems nobody downstream needs to have ExecutingLineIndex set.
private boolean PreScan() {
	for (int LineNumber = 0; LineNumber < Basic.lines.size(); ++LineNumber) {
		String line = Basic.lines.get(LineNumber);					// One line at a time
		
		int li = line.indexOf(":");									// fast check
		if ((li <= 0) && (line.charAt(0) != 'r')) { continue; }		// not label or READ.DATA, next line

		ExecutingLineBuffer = line;									// set global for called functions
		// ExecutingLineIndex = LineNumber;
		String word = getWord(line, 0, false);
		LineIndex = word.length();

		if (isNext(':')) {											// if word really is a label, store it
			if (Labels.put(word, LineNumber) != null) { return RunTimeError("Duplicate label name"); }
			if (!checkEOL())                   { return false; }
		}
		else if (line.startsWith("read.data")) {					// Is not a label. If it is READ.DATA
			LineIndex = 9;											// set LineIndex just past READ.DATA
			if (!executeREAD_DATA())           { return false; }	// parse and store the data list
		}
	}
	getInterruptLabels();
	return true;
}

private static void Show(String str){					// Display a Error message on  output screen. Message will be
														// picked up up in main loop and sent to the UI task.
  if (str.startsWith("@@")){							// If this an interprocess message
	  TempOutput[TempOutputIndex]= str;					// send the message
	  ++TempOutputIndex;
  } else												// Not an interprocess message
	if (OnErrorLine == 0){								// If there is an OnError label, not show the message.
		TempOutput[TempOutputIndex]= str;					
		++TempOutputIndex;
	}
}

private static  void PrintShow(String str){				// Display a PRINT message on output screen. Message will be
	if (TempOutputIndex >= MaxTempOutput) return;
	
	TempOutput[TempOutputIndex]= str;					// picked up up in main loop and sent to the UI task.
	++TempOutputIndex;
}


	private  boolean StatementExecuter() {				// Execute one basic line (statement)

		PossibleKeyWord = "";
		Command c = findCommand(BASIC_cmd);				// get the keyword that may start the line
		if (c == null) { c = CMD_LET; }					// if no keyword, then assume pseudo LET

		if (!IfElseStack.empty()) {						// if inside IF-ELSE-ENDIF
			Integer q = IfElseStack.peek();				// decide if we should skip to ELSE or ENDIF
			if ((q == IEskip1) &&(c.id != CID_SKIP_TO_ELSE)
							   && (c.id != CID_SKIP_TO_ENDIF)) {
				return true;							// skip unless IF, ELSEIF, ELSE, or ENDIF
			} else if ((q == IEskip2) && (c.id != CID_SKIP_TO_ENDIF)) {
				return true;							// skip unless IF or ENDIF
			}
		}

		if (Echo) {
			PrintShow(ExecutingLineBuffer.substring(0, ExecutingLineBuffer.length() - 1));
		}

		if (!c.run()) { SyntaxError(); return false; }
		return true;									// Statement executed ok. Return to main looper.
	}

	private  void SyntaxError(){						// Called to output Syntax Error Message
/*		if (OnErrorLine != 0){
			return;
		}*/
		if (!SyntaxError){								// If a previous Syntax error message has
			RunTimeError("Syntax Error");				// not been displayed them display
			SyntaxError = true;							// Then set the flag so we don't do it again.
		}
		
		
		// If graphics is opened then the user will not be able to see error messages
		// Provide a Haptic notice
		
		if (GRopen){
			lv.performHapticFeedback(2, 1);
			try {Thread.sleep(300);}catch(InterruptedException e){}
			lv.performHapticFeedback(2, 1);
			try {Thread.sleep(300);}catch(InterruptedException e){}
			lv.performHapticFeedback(2, 1);
		}

	}

	private boolean RunTimeError(String... msgs){
		Show(msgs[0]);								// Display error message
		Show(ExecutingLineBuffer);					// Display offending line
		for (int i = 1; i < msgs.length; ++i) {		// Display any supplemental text
			Show(msgs[i]);
		}

		SyntaxError = true;
		errorMsg = msgs[0] + "\nLine: " + ExecutingLineBuffer;
		return false;						// Always return false as convenience for caller
	}

	private boolean RunTimeError(Exception e) {
		return RunTimeError("Error: " + e);
	}

   private boolean nextLine() {				// Move to beginning of next line
	   if (++ExecutingLineIndex < Basic.lines.size()) {	// if not at end of program
		   ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
		   LineIndex = 0;
		   return true;
	   }
	   --ExecutingLineIndex;				// No next line
	   return false;
   }

   private boolean isEOL(){
	   return (LineIndex >= ExecutingLineBuffer.length()) ||
			  (ExecutingLineBuffer.charAt(LineIndex) == '\n');
   }

   private boolean checkEOL(){
	   if (isEOL()) return true;
	   String ec = ExecutingLineBuffer.substring(LineIndex);
	   RunTimeError("Extraneous characters in line: " + ec);
	   return false;
   }

	private boolean isNext(char c) {		// Check the current character
		if ((LineIndex < ExecutingLineBuffer.length()) &&	// if it is as expected...
			(ExecutingLineBuffer.charAt(LineIndex) == c)) {
			++LineIndex;									// ... increment the character pointer
			return true;
		}
		return false;
	}

	private boolean getStringArg() {		// Get and validate a string
		return (evalStringExpression()			// Get the string expression
			&& !SEisLE							// Okay if not logical expression
			&& (StringConstant != null));		//      and not null
		// Leaves evaluation result in StringConstant
	}

	private boolean getArgAsNum() {			// Get and validate a numeric expression
		if (!evalNumericExpression()) {		// or string that evaluates to a number
			if (SyntaxError || !getStringArg()) { return false; }
			try { EvalNumericExpressionValue = Double.valueOf(StringConstant); }
			catch (NumberFormatException e) { return false; }
		}
		return true;							// return value in EvalNumericExpressionValue
	}

	// ************************* start of getVar() and its derivatives ****************************

	private static final boolean TYPE_NUMERIC = true;	// true: type is numeric
	private static final boolean TYPE_STRING  = false;	// false: type is NOT numeric
	private static final boolean USER_FN_OK = true;		// flag used as parseVar() argument, when false
														// user-defined function names are not recognized as valid symbols

	private static final String EXPECT_ARRAY_VAR = "Array variable expected";
	private static final String EXPECT_NEW_ARRAY = "Array previously dimensioned";
	// private static final String EXPECT_UNDIM_ARRAY = "Array must not be DIMed";
	private static final String EXPECT_DIM_ARRAY = "Array must be DIMed before using";
	// private static final String EXPECT_DIM_ARRAY = "Array not DIMed";
	private static final String EXPECT_ARRAY_NO_INDEX = "Expected '[]'";
	private static final String EXPECT_NUM_ARRAY = "Array not numeric";
	private static final String EXPECT_STRING_ARRAY = "Not string array";
	private static final String EXPECT_NEW_FN_NAME = "Function previously defined at:";

	// getVar:
	// This function parses a function name out of the input stream, then searches for
	// it in the variable lists. If an existing variable is not found, it creates one.
	// It writes global flags, variables, and data structures for results and status.
	// There are other functions that duplicate getVar() except for small changes that
	// make certain cases more efficient by leaving out some of the work.
	//
	// In Paul's original design, this function handled all cases of scalar and array
	// variables, and user-defined function names for FN.DEF (but not function calls)
	// with special cases directed by global these global flags:
	//     doingDim, unDiming, SkipArrayValues, DoingDef
	// This implementation behaves as the original did when all flags were set false.
	// There are now dedicated functions for some of the special cases.
	// All other cases must be built up from the primitives found below.
	//
	private boolean getVar() {							// Get variable name if there is one.
		int LI = LineIndex;
		if (getVarValue(getVarAndType())) return true;	// If there is one, find it in the symbol table.
														// If not found in table, create new variable.
		LineIndex = LI;
		return false;
	}

	private boolean getNVar() {							// get var and assure that it is numeric
		int LI = LineIndex;
		if (getVarValue(getVarAndType(TYPE_NUMERIC))) return true;
		LineIndex = LI;
		return false;
	}

	private boolean getSVar() {							// get var and assure that it is not numeric
		int LI = LineIndex;
		if (getVarValue(getVarAndType(TYPE_STRING))) return true;
		LineIndex = LI;
		return false;
	}

	private String getArrayVarForWrite() {				// get the array var name as a new, undimensioned array
														// returns the name, does NOT create a variable
		int LI = LineIndex;
		String var = getVarAndType();					// either string or numeric type is ok
		if (validArrayVarForWrite(var)) { return var; }	// no error, return name, caller must create variable
		LineIndex = LI;									// error, return null
		return null;
	}

	private boolean validArrayVarForWrite(String var) {
		if ((var == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (!VarIsNew)						{ return RunTimeError(EXPECT_NEW_ARRAY); }
		if (!isNext(']'))					{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
		return true;									// no error
	}

	private String getArrayVarForWrite(boolean type) {	// get the array var name as a new, undimensioned array
														// returns the name, does NOT create a variable
		int LI = LineIndex;
		String var = parseVar(!USER_FN_OK);
		if (validArrayVarForWrite(var, type)) { return var; } // no error, return name, caller must create variable
		LineIndex = LI;									// error, return null
		return null;
	}

	private boolean validArrayVarForWrite(String var, boolean type) {
		if ((var == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (type != VarIsNumeric)			{ return RunTimeError(type ? EXPECT_NUM_ARRAY : EXPECT_STRING_ARRAY); }
		if (searchVar(var))					{ return RunTimeError(EXPECT_NEW_ARRAY); }
		if (!isNext(']'))					{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
		return true;									// no error
	}

	private String getArrayVarForRead() {				// get the array var as a previously-dimensioned array
														// returns the var name, null if error or no var
		int LI = LineIndex;
		String var = getVarAndType();					// type must match expected type
		if (validArrayVarForRead(var)) { return var; }	// no error, return name, array index is in theValueIndex
		LineIndex = LI;
		return null;									// error, theVarIndex is not valid
	}

	private boolean validArrayVarForRead(String var) {
		if ((var == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		else if (VarIsNew)					{ return RunTimeError(EXPECT_DIM_ARRAY); }
		return true;									// no error
	}

	private String getNewFNVar() {						// get var and assure that it is a new function name
														// returns the name, does NOT create a variable
		int LI = LineIndex;
		String var = parseVar(USER_FN_OK);				// Get function name if there is one.
		if ((var != null) && VarIsFunction) {			// If there is one...
			searchVar(var);								// ... look for it in the symbol table.
			if (VarIsNew) {
				return var;
			} else {
				RunTimeError(EXPECT_NEW_FN_NAME);
			}
		}
		LineIndex = LI;
		return null;
	}

	// ************************* top half of getVar() *************************

	private String getVarAndType() {
		String var = parseVar(!USER_FN_OK);				// Get variable name if there is one.
		if (var != null) {								// If there is one...
			searchVar(var);								// ... find it in the symbol table.
		}
		return var;										// note: LineIndex does not change if var is null
	}

	private String getVarAndType(boolean needNumeric) {	// specify TYPE_NUMERIC or TYPE_STRING
		String var = parseVar(!USER_FN_OK);				// get variable name if there is one
		if (var != null) {
			if (needNumeric == VarIsNumeric) {			// if type matches expected type
				searchVar(var);							// look up the variable name
				return var;								// note: parseVar changed LineIndex
			}											// else type mismatch, return no var
		}
		return null;									// note: LineIndex does not change
	}

	// Gets the variable name and type. Sets VarIsNumeric, VarIsArray, VarIsFunction.
	// Returns variable name. Name includes $ for strings, [ for arrays, ( for functions.
	// If arg is false, user-defined function names are not valid variable names.
	// Does not advance LineIndex unless it finds a valid variable name.
	private String parseVar(boolean isUserFnAllowed) {
		int LI = LineIndex;
		int max = ExecutingLineBuffer.length();

		// For the special cases where a var could be followed by keyword THEN, TO or STEP
		boolean stopOnPossibleKeyWord = !PossibleKeyWord.equals("");
																// Isolate the var characters
		String var = getWord(ExecutingLineBuffer, LI, stopOnPossibleKeyWord);
		if (var.length() == 0) { return null; }					// length is 0, no var
		LI += var.length();
		if (LI < max) {
			char c = ExecutingLineBuffer.charAt(LI);
			VarIsInt = false;									// Never an integer
			VarIsNumeric = (c != '$');							// Is this a string var?
			if (!VarIsNumeric && (++LI < max)) {
				var += c;
				c = ExecutingLineBuffer.charAt(LI);
			}
			VarIsArray = (c == '[');							// Is this an array?
			VarIsFunction = (c == '(');							// Is this a function?
			if (VarIsArray && (++LI < max)) {
				var += c;
			} else if (VarIsFunction && (++LI < max)) {
				if (!isUserFnAllowed) { return null; } 			// Do not write LineIndex
				var += c;
			}
		}
		if (LI >= max) { LineIndex = max; return null; }
		LineIndex = LI;
		return var;
	}

	private boolean searchVar(String var) {			// search for a variable by name
		int j = VarSearchStart;						// VarSearchStart is usually zero but will change when executing User Function
		for ( ; j < VarNames.size(); ++j) {			// look up this var in the variable table
			if (var.equals(VarNames.get(j))) {		// found it
				VarIsNew = false;
				VarNumber = j;
				theValueIndex = VarIndex.get(j);	// get the value index from the var table
				return true;
			}
		}
		VarIsNew = true;
		return false;								// not in list of variable names
	}

	// ************************* bottom half of getVar() **********************

	private boolean getVarValue(String var) {		// bottom half of getVar()
													// do NOT call before calling parseVar() and searchVar(String)
													// can automatically create salar but not array
		if (var == null) return false;				// no var to get
		if (VarIsArray) {
			if (VarIsNew) {							// new array: error
				return RunTimeError(EXPECT_DIM_ARRAY);
			} else {								// old array (has real VarIndex):
				return GetArrayValue();				// set theValueIndex based upon user's index values
			}
		} else if (VarIsNew) {
			createNewScalar(var);					// create new scalar with real VarIndex, theVarIndex is valid
		}
		return true;
	}

	private void createNewScalar(String var) {		// make a new var table entry and put a scalar in it
		int kk = 0;
		if (!VarIsNumeric) {						// if var is string
			kk = StringVarValues.size();			// then the value index is the index
			StringVarValues.add("");				// to the next unused entry in string values list
		} else {									// else var is numeric
			kk = NumericVarValues.size();			// then the value index is the index
			NumericVarValues.add(0.0);				// to the next unused entry in numeric values list
		}
		createNewVar(var, kk);						// make a new var table entry
		theValueIndex = kk;
	}

	private int createNewVar(String var) {			// make a new var table entry with default value
		return createNewVar(var, 0);
	}

	private int createNewVar(String var, int val) {	// make a new var table entry
		VarNumber = VarNames.size();
		VarNames.add(var);
		VarIndex.add(val);							// keep VarIndex in sync
		return VarNumber;
	}

	// ************************************* end of getVar() **************************************

	private  boolean getNumber(){						// Get a number if there is one
														// Attempt to parse out a number
		char c = 0;
		int max = ExecutingLineBuffer.length();
		int i = LineIndex;
		while (i < max) {								// Must start with one or more digits
			c = ExecutingLineBuffer.charAt(i);
			if (c > '9' || c < '0') { break; }			// If not a digit, done with whole part
			++i;
		}
		if (i == LineIndex) { return false; }			// No digits, not a number

		if (c == '.') {									// May have a decimal point
			while (++i < max) {							// Followed by more digits
				c = ExecutingLineBuffer.charAt(i);
				if (c > '9' || c < '0') { break; }		// If not a digit, done with fractional part
			}
		}
		if (c == 'e' || c == 'E') {						// Is there an exponent
			if (++i < max) {
				c = ExecutingLineBuffer.charAt(i);
				if (c == '+' || c == '-') { ++i; }		// Is there a sign on the exponent
			}
			while (i < max) {							// Get the exponent
				c = ExecutingLineBuffer.charAt(i);
				if (c > '9' || c < '0') { break; }		// If not a digit, done with exponent
				++i;
			}
		}
		String num = ExecutingLineBuffer.substring(LineIndex,i);			// isolate the numeric characters
		LineIndex = i;
		double d = 0;
		try { d = Double.parseDouble(num);}									// have java parse it into a double
		catch (Exception e) {
			return RunTimeError(e);
		}
		GetNumberValue = d;													// Report the value 
		return true;														// Say we found a number
	}
	
	private  boolean GetStringConstant(){				// Get a string constant if there is one
																		// parse out string constant
		int max = ExecutingLineBuffer.length();
		if (LineIndex >= max || LineIndex < 0) return false;
		
		int i = LineIndex;
		StringConstant = "";
		char c = ExecutingLineBuffer.charAt(i);
		if (c!= '"'){return false;}										// first char not "", not String Constant
		while (true) {													// Get the rest of the String
			++i;														// copy character until " or EOL
			if (i >= max) return false;
			c = ExecutingLineBuffer.charAt(i);
			if (c == '"') break;										// if " we're done

			if (c == '\r') {			// AddProgramLine hides embedded newline as carriage return
				c = '\n';
			} else if (c == '\\') {		// AddProgramLine allows only quote or backslash after backslash
				c = ExecutingLineBuffer.charAt(++i);
			}
			StringConstant += c;										// add to String Constant
		}
		
		if (i< max-1){ ++i;}							// do not let index be >= line length
		LineIndex = i;
		return true;															// Say we have a string constant
	}

	private Command getFunction(Map<String, Command> map) {			// get a Math or String Function if there is one
		Command fn = null;
		int i = ExecutingLineBuffer.indexOf('(', LineIndex);
		if (i >= 0) {
			String token = ExecutingLineBuffer.substring(LineIndex, ++i);	// token could be a function name
			fn = map.get(token);
			if (fn != null) {												// null if not a function
				if (i >= ExecutingLineBuffer.length()) { fn = null; }		// nothing after the '('
				else { LineIndex = i; }										// set line index past end of function name
			}
		}
		return fn;
	}

	private  boolean executeLET(){						// Execute a real or pseudo LET
															// Execute LET (an assignment statement)
		if (!getVar()){										// Must start with a variable
			return false;									// to assign a value to
		}
		
		if (isNext(':')) {									// If this is a label,			
			return checkEOL();								// then done
		}
		
		if (!isNext('=')) {									// Var must be followed by =
			return false;
		}
		
//		if (theValueIndex == null) return false;            // Why? Because it is sometimes null
		
		int AssignToVarNumber = theValueIndex;				// Save the assign to Var Number
		
		if (VarIsNumeric) {									// if var is number then 
			if (!evalNumericExpression()) { return false; }	// evaluate following numeric expression
			NumericVarValues.set(AssignToVarNumber, EvalNumericExpressionValue);  // and assign results to the var
		} else {											// Assignment is string expression
			if (!getStringArg()) { return false; }			// Evaluate the string expression
			StringVarValues.set(AssignToVarNumber, StringConstant); // Assign result to the string var
		}
		return checkEOL();
	}	
	
	private  boolean evalNumericExpression(){			// Evaluate a numeric expression

		if (LineIndex >= ExecutingLineBuffer.length() ) return false;
															// Evaluate a Numeric Expression
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if (c == '\n' || c == ')') { return false; }		// If eol or starts with ')', there is not an expression

		Stack<Double> ValueStack = new Stack<Double>();     // Each call to eval gets its own stack
		Stack<Integer>OpStack = new Stack<Integer>();		// thus we can recursively call eval
		int SaveIndex = LineIndex;

		OpStack.push(SOE);									// Push Start of Expression onto stack
		if (!ENE(OpStack, ValueStack)) {					// Now do the recursive evaluation
			LineIndex = SaveIndex;							// if it fails, back up
			return false;									// and die
		}

		if (ValueStack.empty()) return false;
		EvalNumericExpressionValue = ValueStack.pop();		// Recursive eval succeeded. Pop stack for results
		return true;
	}
		
	private boolean ENE(Stack<Integer> theOpStack, Stack<Double> theValueStack){ // Part of evaluating a number expression

																// The recursive part of Eval Expression
		Bundle ufb = ufBundle;
		Command cmd;
			
		char c = ExecutingLineBuffer.charAt(LineIndex);         // First character
 
		if (c == '+'){											// Check for unary operators
			theOpStack.push(UPLUS);
			++LineIndex;
		}
		else if(c == '-'){
			theOpStack.push(UMINUS);
			++LineIndex;
		}
		else if (c == '!'){
			theOpStack.push(NOT);
			++LineIndex;
		}
		
		if (getNumber()){								// Not a var, must be a number
			theValueStack.push(GetNumberValue);			// if it is, push the number
		}
		
		else if (getNVar()){							// Try numeric variable									
			theValueStack.push(NumericVarValues.get(theValueIndex));	// Push value of Var
		}

		else if ((cmd = getFunction(MF_map)) != null) {		// Try Math Function
			if (!doMathFunction(cmd)) return false;			// Math Function failed
			theValueStack.push(EvalNumericExpressionValue);
		}

		else if (evalStringExpression()){					// Try String Logical Expression
			if (!SEisLE) return false;						// If was not a logical string expression, fail
			theValueStack.push(EvalNumericExpressionValue);
		}

		else if (isUserFunction(TYPE_NUMERIC)) {			// Try User Function
			if (!doUserFunction()) return false;
			ufBundle = ufb;
			theValueStack.push(EvalNumericExpressionValue);
		}
		else if (isNext('(')) {								// Handle possible (
			String holdPKW = PossibleKeyWord;
			PossibleKeyWord = "";
			if (!evalNumericExpression()){return false;} 	// if ( then eval expression inside the parens
			theValueStack.push(EvalNumericExpressionValue);
			PossibleKeyWord = holdPKW;
			if (!isNext(')')) {return false;}
		}
		else {
			return false;									// nothing left, fail
		}

		if (LineIndex >= ExecutingLineBuffer.length() ) return false;
		c = ExecutingLineBuffer.charAt(LineIndex);
		
		int k = LineIndex;
		
		if (!PossibleKeyWord.equals("")){
			if (ExecutingLineBuffer.startsWith(PossibleKeyWord, LineIndex)){
				return handleOp(EOL,  theOpStack, theValueStack);
			}
		}

		if (",:;]".indexOf(c) >= 0) {						// treat any of these characters as an eol
			return handleOp(EOL, theOpStack, theValueStack);
		}

		k = LineIndex;
		if (!getOp()){return false;}						// If operator does not follow, then fail
		
		switch (OperatorValue) {							// Handle special case operators
															// (This is probably reduntant given the above)
		case EOL:
			if (!handleOp(EOL,  theOpStack, theValueStack)){return false;}
			--LineIndex;
			return true;
		case RPRN:
			if (!handleOp(RPRN,  theOpStack, theValueStack)){return false;}
			if (theOpStack.isEmpty()) { return true; }		// ')' was removed with matching '('
			if ((theOpStack.pop() == RPRN) && !theOpStack.isEmpty()) {
				if (theOpStack.pop() == SOE) {
					--LineIndex;							// LineIndex points at ')' 
					return true;							// Let caller try to match ')'
				}
			}
			return false;									// op stack got corrupted?
		case NOT:											// can't use unary operator after operand
		case LPRN:											// can't start new expression after operand
			return false;
		default:
			if (!handleOp(OperatorValue,  theOpStack, theValueStack)){return false;} // Handles non special case ops
		}

		return ENE(theOpStack, theValueStack);				// Recursively call ENE for rest of expression
	}

	private boolean getOp(){								// Get an expression operator if there is one

		int lastOp = OperatorString.length;					// Look for operator
		for (int i = 0; i < lastOp; ++i) {
			String op = OperatorString[i];
			if (ExecutingLineBuffer.startsWith(op, LineIndex)) {
				OperatorValue = i;
				LineIndex += op.length();
				return true;
			}
		}
		if (isNext('~')) {									// Look for the array.load continue line character
			OperatorValue = EOL;							// Change it to EOL
			return true;
		}
		return false;
	}

	private  boolean handleOp(int op, Stack<Integer> theOpStack, Stack<Double> theValueStack ){	// handle an expression operator

											// Execute operator in turn by their precedence

		double d1 = 0;
		double d2 = 0;
		int ExecOp = 0;

											// If the operator stack is empty, push an SOE (should never happen)
		if (theOpStack.empty()) {
//			theOpStack.push(SOE);
			return false;
		}
											// If the current operator's Goes Onto Stack Precedence
											// is less than the top of stack' Come Off precedence
											// then pop the top of stack operator and execute it
											// keep doing this until the Goes Onto Precedence
											// is less then the TOS Come Off Precedence and then
											// push the current operator onto the operator stack
		
		while (ComesOffPrecedence[theOpStack.peek()] >= GoesOnPrecedence[op]){

				if (theValueStack.empty()) return false;	// Avoid a crash
				ExecOp = theOpStack.pop();

											// Execute the popped operator
											// In general values are popped from the stack and then
											// operated on by the operator
											// the result is then pushed onto the value stack
				switch (ExecOp){
				case UMINUS:
					d1 = theValueStack.pop();
					d1 = -d1;
					theValueStack.push(d1);
					break;
				case UPLUS:
					break;
					
				case PLUS:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = d2 + d1;
					theValueStack.push(d1);
					break;
					
				case MINUS:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = d2 - d1;
					theValueStack.push(d1);
					break;

				case MUL:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = d2 * d1;
					theValueStack.push(d1);
					break;

				case DIV:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					// handle divide by zero
					if (d1 == 0) {
						return RunTimeError("DIVIDE BY ZERO AT:");
					}
					d1 = d2 / d1;
					theValueStack.push(d1);
					break;

				case EXP:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = Math.pow(d2,d1);
					theValueStack.push(d1);
					break;
					
				case LE:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = (d2 <= d1) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;
					
				case NE:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = (d2 != d1) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;
					
				case GE:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = (d2 >= d1) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;
					
				case GT:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = (d2 > d1) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;
					
				case LT:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = (d2 < d1) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;
					
				case LEQ:						// Logical Equals 
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = (d2 == d1) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;

				case OR:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = ((d1 != 0) || (d2 != 0)) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;

				case AND:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = ((d1 != 0) && (d2 != 0)) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;

				case NOT:
					d1 = theValueStack.pop();
					d1 = (d1 == 0) ? 1.0 : 0.0;
					theValueStack.push(d1);
					break;
				case LPRN:
					if (op != RPRN) { return false; }
					break;
				case FLPRN:
					break;
				case RPRN:
					break;
				case SOE:
					return true;
				case EOL:
					d1=d2;
					break;
				default:
				}
//			if (op == RPRN) break;


		}  // End of while pop stack operations
		
		theOpStack.push(op);          // Push the current operator
		return true;
	}

	private boolean evalStringExpression(){						// Evaluate a string expression

		int max = ExecutingLineBuffer.length();
		if (LineIndex >= max) { return false; }

		char c = ExecutingLineBuffer.charAt(LineIndex);
		if (c == '\n' || c == ')') { return false; }			// If eol or starts with ')', there is not an expression

		SEisLE = false;											// Assume not Logical Expression

		String Temp1 = "";
		do {
			if (!ESE()) { return false; }						// Get the next element (constant, var, function, etc)
			Temp1 += StringConstant;							// save the resulting string 
		} while (isNext('+'));									// Another piece to concatenate?

		StringConstant = Temp1;
		EvalNumericExpressionValue = 0.0;						// Set Logical Compare Result to false
		if (LineIndex >= max) { return false; }
		c = ExecutingLineBuffer.charAt(LineIndex);
		if ((c == '\n') ||		// end of line
			(c == ')')  ||		// end of parenthesized expression
			(c == ',')  ||		// parameter separator
			(c == ';')  ||		// PRINT separator
			(c == ':')			// SQL.UPDATE separator
			) { return true; }									// string expression done

																// logical comparison operator required
		int SaveLineIndex = LineIndex;
		boolean isOp = getOp();
		int operator = OperatorValue;
		isOp &=	operator == LE  ||
				operator == NE  ||
				operator == GE  ||
				operator == GT  ||
				operator == LT  ||
				operator == LEQ ;
		if (!isOp) {											// not a logical comparison op
			LineIndex = SaveLineIndex;
			return true;										// string expression done
		}

		if (!ESE()) { return false; }							// get the string to compare

		SEisLE = true;											// signal logical string expression
		String Temp2 = StringConstant;							// do any concat on the right side
		while (isNext('+')) {
			SaveLineIndex = LineIndex - 1;						// index of the '+'
			if (ESE()) {
				Temp2 += StringConstant;						// build up the right side string
			} else {											// what follows is not a string expression
				LineIndex = SaveLineIndex;						// assume the + operation is numeric
				break;
			}
		}

		if (Temp1 ==  null || Temp2 == null) { return false; }
		int cv = Temp1.compareTo(Temp2);						// Do the compare
		/* if Temp1 < Temp2, cv < 0
		 * if Temp1 = Temp2, cv = 0
		 * if Temp1 > Temp2, cv > 0
		 */

		EvalNumericExpressionValue = 0.0;						// assume false

		switch (operator) {

		case LE:
			if (cv <= 0) EvalNumericExpressionValue = 1.0;
			break;
		case NE:
			if (cv != 0) EvalNumericExpressionValue = 1.0;
			break;
		case GE:
			if (cv >= 0) EvalNumericExpressionValue = 1.0;
			break;
		case GT:
			if (cv > 0) EvalNumericExpressionValue = 1.0;
			break;
		case LEQ:
			if (cv == 0) EvalNumericExpressionValue = 1.0;
			break;
		case LT:
			if (cv < 0) EvalNumericExpressionValue = 1.0;
			break;
		default:
			return false;										// Can't happen
		}
		return true;
	}

	private boolean ESE(){										// Get a String expression element

		if (GetStringConstant()) { return true; }				// Try String Constant

		int LI = LineIndex;

		if (isNext('(')) {										// Try parenthesized string expression
			if (getStringArg() && isNext(')')) { return true; }	// logical expresson not allowed here
			LineIndex = LI;
			return false;
		}

		// Try string variable
		String var = getVarAndType();							// top half of getVar()
		if (var != null) {
			if (VarIsNumeric) {
				LineIndex = LI;
				return false;
			}
			if (getVarValue(var)) {								// bottom half of getVar()
				StringConstant = StringVarValues.get(theValueIndex);
				return true;
			}
		}
		LineIndex = LI;

		boolean flag = false;
		Bundle ufb = ufBundle;
		if (isUserFunction(TYPE_STRING)) {
			flag = doUserFunction();
			ufBundle = ufb;
		} else {
			LineIndex = LI;										// Try String Functions
			Command cmd = getFunction(SF_map);
			flag = (cmd != null) && cmd.run();					// value returned in StringConstant
		}
		if (!flag) { LineIndex = LI; }
		return flag;
	}

	// ************************************** Math Functions **************************************

	private boolean doMathFunction(Command cmd) {
		// If the function exists, run it, and make verify that the closing ')' is present.
		// Function value is returned in EvalNumericExpressionValue.
		return (cmd != null) && cmd.run() && isNext(')');
	}

	private double[] getArgsDD() {								// get two numeric arguments (doubles)
		if (!evalNumericExpression())	{ return null; }
		double d1 = EvalNumericExpressionValue;
		if (!isNext(','))				{ return null; }
		if (!evalNumericExpression())	{ return null; }
		double d2 = EvalNumericExpressionValue;
		double[] args = {d1, d2};
		return args;
	}

	private long[] getArgsLL() {								// get two numeric arguments (longs)
		if (!evalNumericExpression())	{ return null; }
		long l1 = EvalNumericExpressionValue.longValue();
		if (!isNext(','))				{ return null; }
		if (!evalNumericExpression())	{ return null; }
		long l2 = EvalNumericExpressionValue.longValue();
		long[] args = {l1, l2};
		return args;
	}

	private int[] getArgsII() {									// get two numeric arguments (ints)
		if (!evalNumericExpression())	{ return null; }
		int i1 = EvalNumericExpressionValue.intValue();
		if (!isNext(','))				{ return null; }
		if (!evalNumericExpression())	{ return null; }
		int i2 = EvalNumericExpressionValue.intValue();
		int[] args = {i1, i2};
		return args;
	}

	private int[] getArgs4I() {									// get four numeric int arguments
		if (!evalNumericExpression())	{ return null; }
		int i1 = EvalNumericExpressionValue.intValue();
		if (!isNext(','))				{ return null; }
		if (!evalNumericExpression())	{ return null; }
		int i2 = EvalNumericExpressionValue.intValue();
		if (!isNext(','))				{ return null; }
		if (!evalNumericExpression())	{ return null; }
		int i3 = EvalNumericExpressionValue.intValue();
		if (!isNext(','))				{ return null; }
		if (!evalNumericExpression())	{ return null; }
		int i4 = EvalNumericExpressionValue.intValue();
		int[] args = {i1, i2, i3, i4};
		return args;
	}

	private int[] getArgs4NVar() {								// get four numeric var arguments
		if (!getNVar())		{ return null; }
		int i1 = theValueIndex;
		if (!isNext(','))	{ return null; }
		if (!getNVar())		{ return null; }
		int i2 = theValueIndex;
		if (!isNext(','))	{ return null; }
		if (!getNVar())		{ return null; }
		int i3 = theValueIndex;
		if (!isNext(','))	{ return null; }
		if (!getNVar())		{ return null; }
		int i4 = theValueIndex;
		int[] args = {i1, i2, i3, i4};
		return args;
	}

	private String[] getArgsSS() {								// get two string arguments
		if (!getStringArg())			{ return null; }
		String s1 = StringConstant;
		if (!isNext(','))				{ return null; }
		if (!getStringArg())	{ return null; }
		String s2 = StringConstant;
		String[] args = {s1, s2};
		return args;
	}

	private boolean executeMF_SIN() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.sin(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_COS() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.cos(EvalNumericExpressionValue);
		return true;
	}
	
	private boolean executeMF_TAN() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.tan(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_SQR() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.sqrt(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_ABS() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.abs(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_SGN() {					// 2014-03-16 gt
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.signum(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_CEIL() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.ceil(EvalNumericExpressionValue);
		EvalNumericExpressionIntValue = EvalNumericExpressionValue.longValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_FLOOR() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.floor(EvalNumericExpressionValue);
		EvalNumericExpressionIntValue = EvalNumericExpressionValue.longValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_INT() {					// 2014-03-16 gt
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionIntValue = EvalNumericExpressionValue.longValue();
		EvalNumericExpressionValue = Double.valueOf(EvalNumericExpressionIntValue);
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_FRAC() {					// 2014-03-16 gt
		if (!evalNumericExpression()) return false;
		String str = EvalNumericExpressionValue.toString();
		String sgn = (EvalNumericExpressionValue < 0) ? "-" : "";
		int point = str.indexOf('.');
		EvalNumericExpressionValue = (point < 0) ? 0.0 : Double.parseDouble(sgn + str.substring(point));
		return true;
	}

	private boolean executeMF_MIN() {					// 2013-07-29 gt
		double[] args = getArgsDD();
		if (args == null) return false;
		EvalNumericExpressionValue = Math.min(args[0], args[1]);
		return true;
	}

	private boolean executeMF_MAX() {					// 2013-07-29 gt
		double[] args = getArgsDD();
		if (args == null) return false;
		EvalNumericExpressionValue = Math.max(args[0], args[1]);
		return true;
	}

	private boolean executeMF_LOG() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.log(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_EXP() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.exp(EvalNumericExpressionValue);
		return true;
	}
	
	private boolean executeMF_TODEGREES() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.toDegrees(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_TORADIANS() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.toRadians(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_PI() {					// 2013-07-29 gt
		EvalNumericExpressionValue = Math.PI;
		return true;
	}

	private boolean executeMF_ATAN() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.atan(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_ACOS() {
		if (!evalNumericExpression()) return false;
		double d1 = EvalNumericExpressionValue;
		if (d1 < -1 || d1 > 1){
			return RunTimeError("ACOS parameter out of range");
		}
		EvalNumericExpressionValue = Math.acos(d1);
		return true;
	}

	private boolean executeMF_ASIN() {
		if (!evalNumericExpression()) return false;
		double d1 = EvalNumericExpressionValue;
		if (d1 < -1 || d1 > 1) {
			return RunTimeError("ASIN parameter out of range");
		}
		EvalNumericExpressionValue = Math.asin(d1);
		return true;
	}

	private boolean executeMF_ROUND() {
		if (!evalNumericExpression()) return false;
		double d1 = EvalNumericExpressionValue;				// look for optional place count arg
		if (!isNext(',')) {									// no optional parameters, legacy behavior
			d1 = (double)Math.round(d1);
		} else {
			int places = 0;									// default decimal place count
			int roundingMode = BigDecimal.ROUND_HALF_DOWN;	// default rounding mode
			boolean isComma = isNext(',');
			if (!isComma) {
				if (!evalNumericExpression()) return false;	// get decimal place count
				places = EvalNumericExpressionValue.intValue();
				if (places < 0) { return RunTimeError("Decimal place count (" + places + ") must be >= 0"); }
				isComma = isNext(',');
			}
			if (isComma) {									// look for optional rounding mode
				if (!getStringArg()) return false;
				String roundingArg = StringConstant.toLowerCase(Locale.US);
				Integer mode = mRoundingModeTable.get(roundingArg);
				if (mode == null) { return RunTimeError("Invalid rounding mode: " + roundingArg); }
				roundingMode = mode.intValue();
			}
			d1 = new BigDecimal(d1).setScale(places, roundingMode).doubleValue();
		}
		EvalNumericExpressionValue = d1;
		return true;
	}

	private boolean executeMF_LEN() {					// LEN(s$
		if (!getStringArg()) { return false; }			// Get and check the string expression
		double d1 = StringConstant.length();			// then get its length
		EvalNumericExpressionValue = d1;
		return true;
	}

	private boolean executeMF_RANDOMIZE() {
		if (!evalNumericExpression()) return false;
		long seed = EvalNumericExpressionValue.longValue();
		randomizer = (seed == 0L) ? new Random() : new Random(seed);
		EvalNumericExpressionValue = 0.0;
		return true;
	}

	private boolean executeMF_RND() {
		if (randomizer == null) { randomizer = new Random(); }
		EvalNumericExpressionValue = randomizer.nextDouble();
		return true;
	}

	private boolean executeMF_BACKGROUND() {
		EvalNumericExpressionValue = background ? 1.0 : 0.0;
		return true;
	}

	private boolean executeMF_VAL() {					// VAL(s$
		if (!getStringArg()) { return false; }			// Get and check the string expression
		StringConstant = StringConstant.trim();
		if (StringConstant.length() == 0) {
			return RunTimeError("VAL of empty string is not valid");
		}
		try {
			double d1 = Double.parseDouble(StringConstant);	// have java parse it into a double
			EvalNumericExpressionValue = d1;
		}
		catch (Exception e) {
			return RunTimeError(e);
		}
		return true;
	}

	private boolean executeMF_ASCII() {
		if (!getStringArg()) { return false; }			// Get and check the string expression
		EvalNumericExpressionIntValue = StringConstant.equals("") ? 256L : (StringConstant.charAt(0) & 0x00FF);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_UCODE() {
		if (!getStringArg()) { return false; }			// Get and check the string expression
		EvalNumericExpressionIntValue = StringConstant.equals("") ? 0x10000L : StringConstant.charAt(0);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_MOD() {					// MOD( d1,d2
		double[] args = getArgsDD();
		if (args == null) return false;
		if (args[1] == 0.0) {
			return RunTimeError("DIVIDE BY ZERO AT:");
		}
		EvalNumericExpressionValue = (args[0] % args[1]);
		return true;
	}

	private boolean executeMF_BOR() {
		long[] args = getArgsLL();
		if (args == null) return false;
		EvalNumericExpressionIntValue = (args[0] | args[1]);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_BAND() {
		long[] args = getArgsLL();
		if (args == null) return false;
		EvalNumericExpressionIntValue = (args[0] & args[1]);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_BXOR() {
		long[] args = getArgsLL();
		if (args == null) return false;
		EvalNumericExpressionIntValue = (args[0] ^ args[1]);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean MF_startArg() {
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			if (EvalNumericExpressionValue < 1) {
				return RunTimeError("Start value must be >= 1");
			}
		}
		return true;
	}

	private boolean executeMF_IS_IN() {
		String[] args = getArgsSS();
		if (args == null) return false;
		String searchFor = args[0];
		String searchIn = args[1];

		if (!MF_startArg()) return false;
		int start = EvalNumericExpressionValue.intValue();
		--start;											// make start index zero-based

		int i1;
		if (start < searchIn.length()) {					// if starting inside the string
			int k = searchIn.indexOf(searchFor, start);		// do the search
			i1 = k + 1;										// make results one-based
		} else {
			i1 = 0;											// else "not found"
		}
		EvalNumericExpressionValue = (double)i1;
		return true;
	}

	private boolean executeMF_STARTS_WITH() {
		String[] args = getArgsSS();
		if (args == null) return false;
		String searchFor = args[0];
		String searchIn = args[1];

		if (!MF_startArg()) return false;
		int start = EvalNumericExpressionValue.intValue();

		if (start > searchIn.length()) { start = searchIn.length(); }
		if (--start < 0) { start = 0; }					// make one-based start index zero-based

		int i1 = searchIn.startsWith(searchFor, start)
				? searchFor.length() : 0;
		EvalNumericExpressionValue = (double)i1;
		return true;
	}

	private boolean executeMF_ENDS_WITH() {
		String[] args = getArgsSS();
		if (args == null) return false;
		String searchFor = args[0];
		String searchIn = args[1];

		int i1 = searchIn.endsWith(searchFor)
				? (searchIn.length() - searchFor.length() + 1) : 0;
		EvalNumericExpressionValue = (double)i1;
		return true;
	}

	private boolean executeMF_CLOCK() {
		EvalNumericExpressionValue = (double)SystemClock.elapsedRealtime();
		return true;
	}

	private boolean executeMF_TIME() {
		if (ExecutingLineBuffer.charAt(LineIndex)== ')') {	// If no args, use current time
			EvalNumericExpressionIntValue = System.currentTimeMillis();
		} else {											// Otherwise, get user-supplied time
			Time time = theTimeZone.equals("") ? new Time() : new Time(theTimeZone);
			if (!parseTimeArgs(time)) { return false; }
			EvalNumericExpressionIntValue = time.toMillis(true);
		}
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_GR_COLLISION() {
		int[] args = getArgsII();
		if (args == null) return false;

		EvalNumericExpressionValue = gr_collide(args[0], args[1]);
		return (EvalNumericExpressionValue != -1);		// -1 is run time error
	}

	private boolean executeMF_base(int base) {			// BIN, OCT, or HEX, depending on the base parameter
		if (!getStringArg()) { return false; }			// Get and check the string expression
		int value = 0;
		try { value = Integer.parseInt(StringConstant, base); }
		catch (Exception e) { return RunTimeError(e); }

		EvalNumericExpressionValue = (double)(value &= 0xffffffff);
		return true;
	}

	private boolean executeMF_SHIFT() {
		long[] args = getArgsLL();
		if (args == null) return false;
		long value = args[0];
		long bits = args[1];

		EvalNumericExpressionIntValue = (bits < 0) ? (value << -bits) : (value >> bits);;
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_ATAN2() {
		double[] args = getArgsDD();
		if (args == null) return false;
		EvalNumericExpressionValue = Math.atan2(args[0], args[1]);
		return true;
	}

	private boolean executeMF_CBRT() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.cbrt(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_COSH() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.cosh(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_HYPOT() {
		double[] args = getArgsDD();
		if (args == null) return false;
		EvalNumericExpressionValue = Math.hypot(args[0], args[1]);
		return true;
	}

	private boolean executeMF_SINH() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.sinh(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeMF_POW() {
		double[] args = getArgsDD();
		if (args == null) return false;
		EvalNumericExpressionValue = Math.pow(args[0], args[1]);
		return true;
	}

	private boolean executeMF_LOG10() {
		if (!evalNumericExpression()) return false;
		EvalNumericExpressionValue = Math.log10(EvalNumericExpressionValue);
		return true;
	}

	// ************************************ end Math Functions ************************************

	// ************************************* String Functions *************************************

	// Each string function must check for the closing parenthesis (')').

	private boolean executeSF_LEFT() {													// LEFT$
		if (!getStringArg()) { return false; }
		String str = StringConstant;
		if (!isNext(',')) { return false; }
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'

		int length = str.length();
		if (length > 0) {
			int count = EvalNumericExpressionValue.intValue();
			if (count <= 0) { str = ""; }
			else if (count < length) { str = str.substring(0, count); }
		}
		StringConstant = str;
		return true;
	}

	private boolean executeSF_RIGHT() {													// RIGHT$
		if (!getStringArg()) { return false; }
		if (!isNext(',')) { return false; }
		String str = StringConstant;
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'

		int length = str.length();
		if (length > 0) {
			int count = EvalNumericExpressionValue.intValue();
			if (count <= 0) { str = ""; }
			else if (count < length) { str = str.substring(length - count); }
		}
		StringConstant = str;
		return true;
	}

	private boolean executeSF_MID() {													// MID$
		if (!getStringArg()) { return false; }			// Get the string
		String str = StringConstant;
		int length = str.length();

		if (!isNext(',')) { return false; }
		if (!evalNumericExpression()) { return false; }	// Get the start index
		int start = EvalNumericExpressionValue.intValue();
		int count;

		if (isNext(',')) {								// If there is a count, get it
			if (!evalNumericExpression()) { return false; }
			count = EvalNumericExpressionValue.intValue();
			if (count < 0) { return RunTimeError("Count < 0"); }
		} else {
			count = length;								// Default count is whole string
		}
		if (!isNext(')')) { return false; }				// Function must end with ')'

		if (length > 0) {
			if (start > length) { str = ""; }
			else {
				if (--start < 0) { start = 0; }			// Change 1-based index to 0-based
				count += start;							// Change count to end index
				if (count > length) { count = length; }
				str = str.substring(start, count);
			}
		}
		StringConstant = str;
		return true;
	}

	private boolean executeSF_WORD() {													// WORD$
		if (!getStringArg()) { return false; }			// string to split
		String SearchString = StringConstant;

		if (!isNext(',')) { return false; }
		if (!evalNumericExpression()) { return false; }	// which word to return
		int wordIndex = EvalNumericExpressionValue.intValue();

		String r[] = doSplit(SearchString, 0);			// get regex arg, if any, and split the string.
		if (!isNext(')')) { return false; }				// Function must end with ')'

		int length = r.length;							// get the number of strings generated
		if (length == 0) { return false; }				// error in doSplit()

		wordIndex--;									// convert to 0-based index
		for (int i = 0; (i < length) && (r[i].length() == 0); ++i) {
			wordIndex++;								// special case: string started with delimiter(s)
		}
		StringConstant = ((wordIndex < 0) || (wordIndex >= length)) ? "" : r[wordIndex];
		return true;
	}

	private boolean executeSF_STR() {													// STR$
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		StringConstant = String.valueOf(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeSF_UPPER() {													// UPPER$
		if (!getStringArg()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		StringConstant = StringConstant.toUpperCase(Locale.getDefault());
		return true;
	}

	private boolean executeSF_LOWER() {													// LOWER$
		if (!getStringArg()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		StringConstant = StringConstant.toLowerCase(Locale.getDefault());
		return true;
	}

	private boolean executeSF_FORMAT() {												// FORMAT$
		if (!getStringArg()) { return false; }			// get the pattern string
		String str = StringConstant;

		if (!isNext(',')) { return false; }
		if (!evalNumericExpression()) { return false; }	// Get the number to format
		if (!isNext(')')) { return false; }				// Function must end with ')'

		return Format(str, EvalNumericExpressionValue);	// and then do the format
	}

	private boolean executeSF_USING() {
		Locale locale;
		if (isNext(',')) { StringConstant = ""; }	// force default Locale
		else {
			if (!getStringArg()) return false;		// get user-specified Locale
			if (!isNext(',')) return false;
		}
		locale = parseLocale(StringConstant);
		if (locale == null) { return RunTimeError("Unknown locale " + StringConstant); }

		if (!getStringArg()) return false;			// get format string
		String fmt = StringConstant;

		Object[] args = getUsingArgs();				// get data to format
		if (args == null) return false;				// error getting args
		if (!isNext(')')) return false;				// Function must end with ')'

		try {
			StringConstant = String.format(locale, fmt, args);
		} catch (Exception e) {
			return RunTimeError("Cannot complete FPRINT\n" + e);
		}

		return true;
	}

	private boolean executeSF_CHR() {													// CHR$
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		StringConstant = "" + (char)EvalNumericExpressionValue.intValue();
		return true;
	}

	private boolean executeSF_VERSION() {												// VERSION$
		if (!isNext(')')) { return false; }				// Function must end with ')'
		StringConstant = getString(R.string.version);
		return true;
	}

	private boolean executeSF_GETERROR() {												// GETERROR$
		if (!isNext(')')) { return false; }				// Function must end with ')'
		StringConstant = errorMsg;
		return true;
	}

	private boolean executeSF_REPLACE() {												// REPLACE$
		if (!getStringArg()) { return false; }
		String target = StringConstant;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }
		String argument = StringConstant;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }
		String replacment = StringConstant;
		if (!isNext(')')) { return false; }				// Function must end with ')'

		if (argument == null || replacment == null) {
			return RunTimeError("Invalid string");
		}
		
		StringConstant = target.replace(argument, replacment);
		return true;
	}

	private boolean executeSF_INT() {													// INT$
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		int val = EvalNumericExpressionValue.intValue();
		StringConstant = Integer.toString(val);
		return true;
	}

	private boolean executeSF_HEX() {													// HEX$
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		int e = EvalNumericExpressionValue.intValue();
		StringConstant = Integer.toHexString(e);
		return true;
	}

	private boolean executeSF_OCT() {													// OCT$
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		int e = EvalNumericExpressionValue.intValue();
		StringConstant = Integer.toOctalString(e);
		return true;
	}

	private boolean executeSF_BIN() {													// BIN$
		if (!evalNumericExpression()) { return false; }
		if (!isNext(')')) { return false; }				// Function must end with ')'
		int e = EvalNumericExpressionValue.intValue();
		StringConstant = Integer.toBinaryString(e);
		return true;
	}

	private boolean parseTimeArgs(Time time) {						// Convert time parameters to Time object fields
		int year, month, day, hour, minute, second;					// Requires six parameters,
		if (!getArgAsNum()) return false;							// either numeric or string containing a number
		year = EvalNumericExpressionValue.intValue();				// Year$
		if (!isNext(',') || !getArgAsNum()) return false;
		month = EvalNumericExpressionValue.intValue() - 1;			// Month$ (convert to 0-index)
		if (!isNext(',') || !getArgAsNum()) return false;
		day = EvalNumericExpressionValue.intValue();				// Day$
		if (!isNext(',') || !getArgAsNum()) return false;
		hour = EvalNumericExpressionValue.intValue();				// Hour$
		if (!isNext(',') || !getArgAsNum()) return false;
		minute = EvalNumericExpressionValue.intValue();				// Minute$
		if (!isNext(',') || !getArgAsNum()) return false;
		second = EvalNumericExpressionValue.intValue();				// Second$
		time.set(second, minute, hour, day, month, year);
		return true;
	}

	private Locale parseLocale(String localeStr) {
		if ((localeStr == null) || (localeStr.length() == 0)) return Locale.getDefault();
		Locale locale;
		String[] f = localeStr.split("_");
		switch (f.length) {
			default:								// ignore excess fields
			case 3: locale = new Locale(f[0], f[1], f[2]); break;
			case 2: locale = new Locale(f[0], f[1]); break;
			case 1: locale = new Locale(f[0]); break;
		}
		return locale;
	}

	private Object[] getUsingArgs() {
		ArrayList<Object> args = new ArrayList<Object>();
		while (isNext(',')) {
			if (evalNumericExpression()) {			// field is numeric
				if (VarIsInt) { args.add(EvalNumericExpressionIntValue); }
				else		  { args.add(EvalNumericExpressionValue); }
			} else {
				if (getStringArg()) {
					args.add(StringConstant);			// field is string
				} else if (VarIsFunction) { return null; }
			}
			if (SyntaxError) { return null; }
		}
		return args.toArray();
	}

	private  boolean Format(String Fstring, double Fvalue){			// Format a number for output
																					// Do the heart of the FORMAT$ function
		BigDecimal B = BigDecimal.valueOf(0.0);
		try { B = BigDecimal.valueOf(Math.abs(Fvalue));}
		catch (Exception e) {
			return RunTimeError(e);
		}
		String Vstring =B.toPlainString();											// and convert the big decimal to a string

		String FWstring = "";  		// Will hold the whole number part of the pattern (format) string
		String FDstring = "";  		// Will hold the decimal part of the pattern string
		String VWstring = "";		// Will hold the whole number part of the number
		String VDstring = "";		// Will hold the decimal part of the number
		String Temp = "";
		int i = 0;
		char c = ' ';
		boolean FhasDecimal = false;
		int FdecimalIndex = 0;
		boolean VhasDecimal = false;
		int VdecimalIndex = 0;
		
																			// First find pattern string decimal index
		for (i=0; i< Fstring.length(); ++i){
			if (Fstring.charAt(i)== '.'){
				if (FhasDecimal){SyntaxError(); return false;}				// if more than one decimal, error
				FhasDecimal = true;
				FdecimalIndex = i;
			}
		}
		if (!FhasDecimal){													// If no decimal in pattern
			FdecimalIndex = i+1;											// set decimal index past end of pattern
			}else {															// else move index past the decimal
				++FdecimalIndex;
				}
																			// Split the pattern string
		FWstring = Fstring.substring(0, FdecimalIndex-1);									// FW is whole number part (includes decimal)
		if (FhasDecimal) {FDstring = Fstring.substring(FdecimalIndex, Fstring.length());}	// FD is decimal string

		for (i=0; i<FDstring.length(); ++i){								// insure FD only has # chars
			if (FDstring.charAt(i) != '#'){SyntaxError(); return false;}
		}

		for (i=0; i< Vstring.length(); ++i){								// Scan the number string for its decimal index
			if (Vstring.charAt(i)== '.'){
				if (VhasDecimal){SyntaxError(); return false;}				// more than one decimal should never happen
				VhasDecimal = true;
				VdecimalIndex = i;											// Set the value decimal index
			}
		}
		if (!VhasDecimal){													// If value has not decimal (should never happen)
			VdecimalIndex = i+1;
			}else {
				++VdecimalIndex;											// move the decimal index past the decimal
				}
																			// Split the value string
		VWstring = Vstring.substring(0, VdecimalIndex-1);										// VW is whole number part (includes decimal)
		if (VhasDecimal) {VDstring = Vstring.substring(VdecimalIndex, Vstring.length());}		// VD is the decimal part 
																			// Build the decimal part of the output string
		Temp = "";
        if (FDstring.length()>0){Temp = ".";}								// If any pattern for decimal, output the decimal
        
        for (i=0; i<FDstring.length(); ++i){								// Copy Decimal digits to output as long as there
        	if (FDstring.charAt(i) != '#'){SyntaxError(); return false;}	// are pattern # chars. If run out of digits before
        	if (i<VDstring.length()){										// pattern digits, output 0 characters			
        		Temp = Temp + VDstring.charAt(i);
        	}else{
        		Temp = Temp + "0";
        	}
        }
        VDstring = Temp;													// Save the Decimal Value output string
        
        Temp = "";
        int FWI = FWstring.length();
        int VWI = VWstring.length();
        																	// Now work on the floating pattern
        
        String FloatChar = " ";												// Initialize float charcter to none
        if (FWI >0 &&														// If there is a float character
        	FWstring.charAt(0)!= '#' &&
        	FWstring.charAt(0)!= '%'){
        	FloatChar = Character.toString(FWstring.charAt(0));				// then move it to FloatChar
        	FWstring = FWstring.substring(1,FWI);
        	--FWI;
        	}
        
        String Header = "";													// Initialize Header to empty
        Header=Header + FloatChar;											// add the float char to the header
        if (Fvalue<0){														// if value < 0 output minus sign
        	Header = Header + "-";											// else execute space
        }else{																// note: reverse order will be unreversed later
        	Header = Header + " ";
        }

        																	// Now work on the whole number par
        StringConstant = "";
       if ( FWI ==0 && VWI >0 ){					// No Whole format characters and Whole Value characters remain
        	if (VWstring.charAt(0) != '0'){			// and the Whole character is not '0'
        		StringConstant = "*" + VDstring;	// then show error
        		return true;
        	}
            if (Fvalue<0){											// No whole number format chars and not whole number digits
            	StringConstant = StringConstant + "-";				// If the decimal digits are < 0, output minus
            }else{
            	StringConstant = StringConstant + " ";				// otherwise output blank
            }
            StringConstant = StringConstant+FloatChar+VDstring;		// Build result for decimal digits with no whole number
        	return true;											// done
        }
       												// We have whole format chars and whole value digits
        --FWI;
        --VWI;
        boolean blanks = true;
        while (FWI >=0){                                  							// While there are format characters
        	c = FWstring.charAt(FWI);					  							// get the format character
        	switch (c){

        	case '#':									  							// format charcter = #
        		blanks = true;
        		if (VWI >= 0){
        			if (VWI == 0 && VWstring.charAt(VWI) == '0'){ Temp = Temp + " ";} // if there are no more digits, output space
        			else {Temp = Temp + Character.toString(VWstring.charAt(VWI));}    // else output the digit
        			--VWI;															  // go to the next digit
        		}else if (VWI == -1){												  // No digits left, if we just ran out, output the header
        				Temp = Temp + Header + " ";									  //output the header
        				--VWI;
        				}else {
        					Temp = Temp + " ";											// output space
        					}
        		break;
 
        	case '%':																	// format charcter = %
        		blanks = false;
        		if (VWI >= 0){
        			Temp = Temp + Character.toString(VWstring.charAt(VWI));  			// if more digits, output it
        			--VWI;
        		}else
        			{Temp = Temp + "0";}									 			// else output 0
        		break;

        	default:														 			// format character not # or %
        		if (blanks){												 			// if doing blanks
        		if (VWI>=0){Temp = Temp + c;}								 			// if more digits, output char
        		else if (VWI == -1) {Temp = Temp + Header + " "; --VWI;}	 			// if just now ran out,
        																	 			// output header and blank
        		else { Temp = Temp + " ";}									 			// else just a blank
        		} else{ Temp = Temp + c;									 			// not blanks, output the char
        		}
        	}
        	--FWI;
        }

        String t1 = "";

        if (VWI==-1){Temp = Temp + Header;}							// add the header to the end of the whole number

        for (i=Temp.length()-1; i>=0; --i){							// now reverse the whole thing
        	t1 = t1+Character.toString(Temp.charAt(i));
        	}
        Temp = t1;

        StringConstant="";
        if (VWI>=0){												// If value decimal digits remain
        	StringConstant = "**"  + Temp + VDstring;				// show the error in place of the header
        	return true;											// and we are done
        }
        
        
        StringConstant = StringConstant + Temp + VDstring;			// Build the final result
        return true;												// and we are done

	}

	// *********************************** end String Functions ***********************************

	private boolean executeDIM() {									// DIM
																		// Execute a DIM Comman
		do {									// Multiple Arrays can be DIMed in one DIM statement separated by commas
			String var = getVarAndType();								// get the array name var
			if ((var == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
			if (!VarIsNew)						{ return RunTimeError(EXPECT_NEW_ARRAY); }
			if (isNext(']')) 					{ return false; }		// must have dimension(s)
			boolean avt = VarIsNumeric;									// preserve the array's type

			ArrayList<Integer> dimValues = new ArrayList<Integer>();	// a list to hold the array dimension values
			do {														// get each index value
				if (!evalNumericExpression())	{ return false; }
				dimValues.add(EvalNumericExpressionValue.intValue());	// and add it to the list
			} while (isNext(','));
			if (!isNext(']'))					{ return false; }		// must have closing bracket

			if (!BuildBasicArray(var, avt, dimValues)) { return false; }// no error, build the array

		} while (isNext(','));											// continue while there are arrays to be DIMed
		return checkEOL();												// then done
	}

	private boolean executeUNDIM(){
		do {									// Multiple Arrays can be UNDIMed in one Statement separated by commas
			if ((getVarAndType() == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
			if (!isNext(']'))								{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
			if (!VarIsNew) {
				VarNames.set(VarNumber, " ");							// if DIMed, UNDIM it
			}
		} while (isNext(','));											// continue while there are arrays to be UNDIMed

		return checkEOL();
	}

	// ************************************* array utilities **************************************

	private boolean BuildBasicArray(String var, boolean IsNumeric, ArrayList<Integer> DimList){

		// Build a basic array attached to a new variable.
		// Makes a bundle with information about the array and put the bundle into the array table.
		// The index into the array table is put into the variable table as the the var value.
		// In addition, a value element is created for each element in the array.

		// This list of sizes is used to quickly calculate the array element offset
		// when the array is referenced.
		ArrayList<Integer> ArraySizes = new ArrayList<Integer>();

		int TotalElements = 1;
		for (int d = DimList.size() - 1; d >= 0; --d) {		// for each Dim from last to first
			int dim = DimList.get(d);						// get the Dim
			if (dim < 1) { return RunTimeError("DIMs must be >= 1 at"); }

			ArraySizes.add(0, TotalElements);				// insert the previous total in the ArraySizes List
			TotalElements= TotalElements * dim;				// multiply this dimension by the previous size

			if (TotalElements > 50000) {					// Limit the size of any one array
				return RunTimeError("Array exceeds 50,000 elements");	// to 50,000 elements
			}
		}

		if (IsNumeric) {									// Initialize Numeric Array Values
			ArrayValueStart = NumericVarValues.size();		// All numeric var values kept in NumericVarValues
			for (int i = 0; i < TotalElements; ++i) {		// Number inited to 0.0
				NumericVarValues.add(0.0);
			}
		} else {											// Initialize String Array Values
			ArrayValueStart = StringVarValues.size();		// All string var values kept in StringVarValues
			for (int i = 0; i < TotalElements; ++i) {
				StringVarValues.add("");					// Strings inited to empty
			}
		}

		Bundle ArrayEntry = new Bundle();						// Build the array table entry bundle
		ArrayEntry.putIntegerArrayList("dims", DimList);		// The array index values
		ArrayEntry.putIntegerArrayList("sizes", ArraySizes);	// The array sizes
		ArrayEntry.putInt("length", TotalElements);
		ArrayEntry.putInt("base", ArrayValueStart);				// The pointer the first array element value

		int varNum = createNewVar(var);
		VarIndex.set(varNum, ArrayTable.size());				// The var's value is its array table index
		ArrayTable.add(ArrayEntry);								// Put the element bundle into the array table

		return true;
	} // end BuildBasicArray

	private boolean BuildBasicArray(String var, boolean IsNumeric, int length) { // Build 1D array
		ArrayList <Integer> dimValues = new ArrayList<Integer>();		// list of dimensions
		dimValues.add(length);											// only one dimension
		return (BuildBasicArray(var, IsNumeric, dimValues));			// go build an array of the proper size
	}

	private boolean ListToBasicNumericArray(String var, List <Double> Values, int length) {
		if (!BuildBasicArray(var, true, length)) return false;			// go build an array of the proper size and type
		int i = ArrayValueStart;
		for (Double d : Values) {										// stuff the array
			NumericVarValues.set(i++, d);
		}
		return true;
	}

	private boolean ListToBasicStringArray(String var, List <String> Values, int length) {
		if (!BuildBasicArray(var, false, length)) return false;			// go build an array of the proper size and type
		int i = ArrayValueStart;
		for (String s : Values) {										// stuff the array
			StringVarValues.set(i++, s);
		}
		return true;
	}

	private boolean GetArrayValue() {				// Get the value of an array element using its index values
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		if (!isNext(']')) {							// Parse out the index values for this call
			int avn = VarNumber;					// preserve the array's VarNumber
			boolean avt = VarIsNumeric;				// and type
			do {
				if (!evalNumericExpression())	{ SyntaxError(); return false; }
				indicies.add(EvalNumericExpressionValue.intValue());
			} while (isNext(','));
			if (!isNext(']'))					{ SyntaxError(); return false; }
			VarNumber = avn;						// restore the array's varNumber
			VarIsNumeric = avt;						// and type
		}

		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber)); // Get the array table bundle for this array
		ArrayList<Integer> dims = ArrayEntry.getIntegerArrayList("dims");
		ArrayList<Integer> sizes = ArrayEntry.getIntegerArrayList("sizes");

		if (dims.size() != indicies.size()) {		// insure index count = dim count
			RunTimeError(
					"Indices count(" +
					indicies.size()+
					") not same as dimension count ("+
					dims.size()+
					") at:");
			return false;
		}
		
		int offset = 0;
		
		for (int i=0; i<indicies.size(); ++i){					
			int p = indicies.get(i);						// p = index for this call
			int q = dims.get(i);							// q = DIMed value for this index
			int r = sizes.get(i);							// r = size for this index
			if (p>q){
				RunTimeError("Index #"+
						(i+1) +
						" (" +
						p +
						") exceeds dimension (" +
						q +
						") at:");		// insure Index <= DIMed index
				return false;
			}
			if (p<=0){											// insure index >= 1
				RunTimeError("Index must be >=1 at:");
				return false;
			}
			offset = offset + (p-1)*r;							// calculate offset
		}
		
		int base = ArrayEntry.getInt("base");            // base + offset gives
		theValueIndex = base + offset;					 // displacement into value table for
		return true;									 // index combination
		
	}

	private boolean getIndexPair(Integer[] pair) {					// get contents of [] from command line
																	// at most two index values accepted
		boolean isBracket = isNext(']');
		if (!isBracket) {
			boolean isComma = isNext(',');
			if (!isComma) {
				if (!evalNumericExpression()) return false;
				pair[0] = EvalNumericExpressionValue.intValue();	// first index
				isComma = isNext(',');
			}
			isBracket = isNext(']');
			if (!isBracket && isComma) {
				if (!evalNumericExpression()) return false;
				pair[1] = EvalNumericExpressionValue.intValue();	// second index
				isBracket = isNext(']');
			}
		}
		return (isBracket);											// must end with ']'
	}

	private boolean getArraySegment(int arrayTableIndex, Integer[] pair) { // get var base and length of array segment
																	// pair in is [start, length], out is [base, length]
		Bundle b = ArrayTable.get(arrayTableIndex);					// get the array table bundle for this array
		if (b == null) { return RunTimeError("Array does not exist"); }
		int length = b.getInt("length");							// get the array length
		int base = b.getInt("base");								// and the start of the array in the variable space
		int max = base + length;

		if (pair[0] != null) {
			int start = pair[0].intValue();							// start index, 1-based, default 1
			if (start > 1) { base += start - 1; }					// convert to 0-based index, ignore if less than 1
			if (base > max) { base = max; }							// not an error, just force length to 0
		}
		if (pair[1] != null) {
			length = pair[1].intValue();							// requested length
			if (length < 0) { length = 0; }							// eliminate negative input
		}
		if ((base + length) > max) { length = max - base; }			// don't go off end of array

		pair[0] = base;
		pair[1] = length;
		return true;
	}

	// ************************************* end array utilities **************************************

	private boolean executePRINT() {
		if (!buildPrintLine(PrintLine, "")) return false;	// build up the print line in StringConstant
		if (!PrintLineReady) {							// flag set by buildPrintLine
			PrintLine = StringConstant;					// not ready to print; hold line
			return true;								// and wait for next Print command
		}
		PrintLine = "";									// clear the accumulated print line
		PrintShow(StringConstant);						// then output the line
		return true;
	}

	// Convert the fields of a print command into a String for printing.
	// The line param can hold an existing String to add the new String to.
	// If the line ends with a semicolon set PrintLineReady false,
	// else add the newline param to the String and set PrintLineReady true.
	// If the String is valid, put it in StringConstant and return true,
	// else return false to signal a syntax error.
	private boolean buildPrintLine(String line, String newline) {
		StringBuilder printLine = new StringBuilder((line == null) ? "" : line);
		boolean WasSemicolon = false;
		do {										// do each field in the print statement
			int LI = LineIndex;
			if (evalNumericExpression()) {
				printLine.append(EvalNumericExpressionValue);	// convert to string
				WasSemicolon = false;
			} else {
				if (evalStringExpression()) {
					printLine.append(StringConstant);			// field is string
					WasSemicolon = false;
				} else {
					if (VarIsFunction) { return false; }
					if (LI == LineIndex) {						// if no more fields, append newline
						if (!checkEOL()) { return false; }		// unless there is junk on the line
					}
				}
			}
			if (SyntaxError) { return false; }

			char c = ExecutingLineBuffer.charAt(LineIndex);
			if (c == ',') {							// if the separator is a comma
				printLine.append(", ");				// add comma + blank to the line
				++LineIndex;
			} else if (c == ';') {					// if separator is semicolon
													// don't add anything to output
				WasSemicolon = true;				// and signal we had a semicolon
				c = ExecutingLineBuffer.charAt(++LineIndex);	// is next char eol?
			}

			if (c == '\n') {						// Done processing the line
				if (!WasSemicolon) {				// if not ended in semicolon
					printLine.append(newline);		// add newline character(s)
				}
				break;
			}
		} while (true);								// Exit loop happens internal to loop
		
		PrintLineReady = !WasSemicolon;
		StringConstant = printLine.toString();
		return true;
	}

	private boolean executeEND() {

		Stop = true;								// ALWAYS stop

		String endMsg = "END";						// Default END message
		boolean ok = true;
		if (!isEOL()) {
			ok = getStringArg();					// Get user's END message
			if (ok) {
				endMsg = StringConstant;
				ok = checkEOL();
			}
		}

		if (endMsg.length() > 0) { PrintShow(endMsg); }
		return ok;

	}

	private boolean getLabelLineNum() {
		int li = LineIndex;
		String label = getWord(ExecutingLineBuffer, LineIndex, false);	// get label name
		Integer lineRef = Labels.get(label);
		LineIndex += label.length();									// move LineIndex for isEOL()
		// If EOL, this is a simple "GOTO/GOSUB label" statement.
		// Otherwise it is a "GOTO/GOSUB index, label_list..." statement.
		if (!isEOL()) {
			LineIndex = li;
			if (!evalNumericExpression()) return false;
			int index = (int)(EvalNumericExpressionValue + 0.5);		// round index
			for (int i = 0; i < index; ++i) {							// skip to indexed label
				if (isEOL()) return true;								// no target, fall through
				if (!isNext(',') || isEOL()) return false;				// no comma, or no label after it
				label = getWord(ExecutingLineBuffer, LineIndex, false);	// get label name
				LineIndex += label.length();
			}
			if (!isNext(',') && !isEOL()) return false;					// does line end with a word?
			if (index < 1) return true;									// no target, fall through
			lineRef = Labels.get(label);								// is it a valid label?
		}
		if (lineRef == null) return RunTimeError("Undefined Label \"" + label + "\" at:");
		ExecutingLineIndex = lineRef.intValue();
		return true;
	}

	private boolean executeGOTO() {

		int maxStack = 50000 ;						// 50,000 should be enough

		if ((IfElseStack.size() > maxStack) || (ForNextStack.size() > maxStack)) {
			return RunTimeError("Stack overflow. See manual about use of GOTO.");
		}

		return getLabelLineNum();
	}

	private boolean executeGOSUB() {
		int returnAddress = ExecutingLineIndex;
		if (!getLabelLineNum()) return false;
		if (ExecutingLineIndex != returnAddress) {
			GosubStack.push(returnAddress);			// Found a valid gosub address, expect a return
		}
		return true;
	}

	private boolean executeRETURN() {
		if (!checkEOL()) return false;
		if (GosubStack.empty()){
			return RunTimeError("RETURN without GOSUB");
		}
		ExecutingLineIndex = GosubStack.pop();
		return true;
	}

	private  boolean executeIF(){
		
		if (!IfElseStack.empty()){			 								// if inside of an IF block		
			Integer q = IfElseStack.peek();
			if ((q != IEexec) && (q != IEinterrupt)) {						// and not executing
				int index = ExecutingLineBuffer.indexOf("then");
				if (index < 0) {											// if there is no THEN
					IfElseStack.push(IEskip2);								// need to skip to this if's end
				} else {
					LineIndex = index + 4;									// skip over the THEN
					if (isNext('\n')) {      								// if not single line if
						IfElseStack.push(IEskip2);							// need to skip to this if's end
					}														// else is single line, no skip needed
				}
				return true;
			}
		}
		// Execute this IF
		
		PossibleKeyWord = "then";					// Tell parseVar to expect a THEN
		if (!evalNumericExpression()) { return false; }
		PossibleKeyWord = "";						// parseVar should no longer expect THEN
		boolean condition = (EvalNumericExpressionValue != 0);
		
		if (ExecutingLineBuffer.charAt(LineIndex) != '\n') {
			if (!ExecutingLineBuffer.startsWith("then", LineIndex)) { checkEOL(); return false; }
			LineIndex = LineIndex + 4;

			if (!isNext('\n')) { return SingleLineIf(condition); }			// assume single-line IF

			// at this point: "IF condition THEN\n" and LineIndex is after '\n'
		}
		
		IfElseStack.push((condition) ? IEexec : IEskip1);

		return true;
	}
	
	private boolean SingleLineIf(boolean condition){
		int index = ExecutingLineBuffer.lastIndexOf("else");
		
		if (condition) {
			if (index > 0 ) {
				ExecutingLineBuffer = ExecutingLineBuffer.substring(0, index) + "\n";
			}
			return StatementExecuter();
		}

		if (index > 0) {
			LineIndex = index + 4;
			return StatementExecuter();
		}
		return true;
	}
	
	private  boolean executeELSE(){
		
		if (IfElseStack.empty()) {			// prior IF or ELSEIF should have put something on the stack
			RunTimeError("Misplaced ELSE");
			return false;
		}
		if (!checkEOL() ) return false;

		Integer q = IfElseStack.pop();
		IfElseStack.push((q == IEexec) ? IEskip2 : IEexec);

		return true;
	}
	
	private boolean executeELSEIF(){
		
		if (IfElseStack.empty()) {			// prior IF or ELSEIF should have put something on the stack
			RunTimeError("Misplaced ELSEIF");
			return false;
		}
		
		Integer q = IfElseStack.pop();

		if (q == IEexec) {
			IfElseStack.push(IEskip2);
		} else {
			PossibleKeyWord = "then";					// Tell parseVar to expect a THEN
			if (!evalNumericExpression()) { return false; }
			PossibleKeyWord = "";						// parseVar should no longer expect THEN
			boolean condition = (EvalNumericExpressionValue != 0);

			if (ExecutingLineBuffer.startsWith("then", LineIndex)) LineIndex = LineIndex + 4;
			if (!checkEOL()) { return false; }

			IfElseStack.push((condition) ? IEexec : IEskip1);
		}
		return true;
	}
	
		private boolean executeENDIF(){
			
			if (IfElseStack.empty()){			// Something must be on the stack
				RunTimeError("Misplaced ENDIF");
				return false;
			}
			if (!checkEOL() ) return false;
			IfElseStack.pop();					// but we do not care what it is
			return true;

		}

	private boolean skipTo(String target, String nest, String errMsg) {
		int lineNum;
		int limit = Basic.lines.size();
		for (lineNum = ExecutingLineIndex + 1; lineNum < limit; ++lineNum) {
			String line = Basic.lines.get(lineNum);
			if (line.startsWith(target)) {
				ExecutingLineIndex = lineNum;					// found the target
				ExecutingLineBuffer = line;
				LineIndex = target.length();
				return true;
			}
			if (line.startsWith(nest)) {
				ExecutingLineIndex = lineNum;					// found nested block of same type
				if (!skipTo(target, nest, errMsg)) return false;// recursively seek its end
				lineNum = ExecutingLineIndex;
			}
		}
		return RunTimeError(errMsg);							// end of program, target not found
	}

	private boolean executeFOR(){

		Bundle b = new Bundle();						// A bundle to hold value for stack

		b.putInt("line",ExecutingLineIndex);							// Loop return location

		if (!getNVar()) { return false; }
		b.putInt("var", theValueIndex);										// For Var
		int IndexValueIndex = theValueIndex;
		
		if (!isNext('=')) { return false; }									// For Var =

		PossibleKeyWord = "to";						// Tell get var that a TO is expected
		if (!evalNumericExpression()) { return false; }						// for Var = <exp>
		PossibleKeyWord = "";
		double fstart = EvalNumericExpressionValue;
		NumericVarValues.set(IndexValueIndex, fstart);   // assign <exp> to Var

		if (!ExecutingLineBuffer.startsWith("to", LineIndex)) return false;
		LineIndex = LineIndex +2;
		
		PossibleKeyWord = "step";
		if (!evalNumericExpression()) { return false; }						// For Var = <exp> to <exp>
		PossibleKeyWord = "";
		b.putDouble("limit", EvalNumericExpressionValue);
		double flimit = EvalNumericExpressionValue;

		double step = 1.0;													//For Var = <exp> to <exp> <default step> 
		if (ExecutingLineBuffer.startsWith("step", LineIndex)){
			LineIndex = LineIndex + 4;
			if (!evalNumericExpression()) { return false; }					// For Var = <exp> to <exp> step <exp>
			step = EvalNumericExpressionValue;
		}
		
		if (!checkEOL()) return false;
		
		b.putDouble("step", step);
		
		if (step > 0) {											// Test the initial condition
			if (fstart > flimit) {return SkipToNext();}			// If exceeds limit then skip to NEXT
		} else {
			if (fstart < flimit) {return SkipToNext();}
		}
		ForNextStack.push(b);
		
		return true;
	}

	private boolean SkipToNext() {
		return skipTo("next", "for", "FOR without NEXT");
	}

	private boolean executeF_N_CONTINUE(){
		if (ForNextStack.empty()) {								// If the stack is empty
			return RunTimeError("No For Loop Active");			// then we have a misplaced CONTINUE
		}
		if (!checkEOL()) return false;

		if (!SkipToNext()) return false;
		doNext();
		return true;
	}

	private boolean executeF_N_BREAK(){
		if (ForNextStack.empty()) {								// If the stack is empty
			return RunTimeError("No For Loop Active");			// then we have a misplaced BREAK
		}
		if (!checkEOL()) return false;

		if (!SkipToNext()) return false;
		ForNextStack.pop();
		return true;
	}

	private boolean executeNEXT(){
		if (ForNextStack.empty()) {								// If the stack is empty
			return RunTimeError("NEXT without FOR");			// then we have a misplaced NEXT
		}
		return doNext();
	}

	private boolean doNext() {
		Bundle b = ForNextStack.peek();							// Peek at the TOS Bundle
		int line = b.getInt("line");
		int varindex = b.getInt("var");							// We did not store the index value, we stored a pointer to it
		double var = NumericVarValues.get(varindex);
		double limit = b.getDouble("limit");
		double step = b.getDouble("step");
		
		var += step;											// Do the STEP
		NumericVarValues.set(varindex, var);					// Assign the result to the index
		
		if (((step > 0) && (var <= limit)) || ((step <= 0) && (var >= limit))) { // Test limit
			ExecutingLineIndex = line;
		} else {
			ForNextStack.pop();									// If done, pop the stack
		}
		return true;
	}

	private boolean executeWHILE(){
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		if (EvalNumericExpressionValue != 0) {
			WhileStack.push(ExecutingLineIndex - 1);			// true: push line number onto while stack
			return true;
		}
		return SkipToRepeat();									// false: find the REPEAT for the WHILE
	}

	private boolean SkipToRepeat() {
		return skipTo("repeat", "while", "WHILE without REPEAT");
	}

	private boolean executeW_R_CONTINUE(){
		if (WhileStack.empty()) {								// If the stack is empty
			return RunTimeError("No While Loop Active");		// then we have a misplaced CONTINUE
		}
		if (!checkEOL()) return false;

		ExecutingLineIndex = WhileStack.pop();					// Pop the line number of the WHILE
		return true;
	}

	private boolean executeW_R_BREAK(){
		if (WhileStack.empty()) {								// If the stack is empty
			return RunTimeError("No While Statement Active");	// then we have a misplaced BREAK
		}
		if (!checkEOL()) return false;

		if (!SkipToRepeat()) return false;
		WhileStack.pop();
		return true;
	}

	private boolean executeREPEAT(){
		if (WhileStack.empty()) {								// If the stack is empty
			return RunTimeError("REPEAT without WHILE");		// then we have a misplaced REPEAT
		}
		if (!checkEOL()) return false;

		ExecutingLineIndex = WhileStack.pop();					// Pop the line number of the WHILE
		return true;
	}

	private boolean executeDO(){
		if (!checkEOL()) return false;
		DoStack.push(ExecutingLineIndex - 1);					// push line number onto DO stack.
		return true;
	}

	private boolean SkipToUntil() {
		return skipTo("until", "do", "DO without UNTIL");
	}

	private boolean executeD_U_CONTINUE(){
		if (DoStack.empty()) {									// If the stack is empty
			return RunTimeError("No DO loop active");			// then we have a misplaced CONTINUE
		}
		if (!checkEOL()) return false;

		if (!SkipToUntil()) return false;
		return doUntil();
	}

	private boolean executeD_U_BREAK(){
		if (DoStack.empty()) {									// If the stack is empty
			return RunTimeError("No DO loop active");			// then we have a misplaced BREAK
		}
		if (!checkEOL()) return false;

		if (!SkipToUntil()) return false;
		DoStack.pop();
		return true;
	}

	private boolean executeUNTIL(){
		if (DoStack.empty()) {									// If the stack is empty
			return RunTimeError("UNTIL without DO");			// then we have a misplaced UNTIL
		}
		return doUntil();
	}

	private boolean doUntil() {
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		if (EvalNumericExpressionValue == 0) {
			ExecutingLineIndex = DoStack.pop();					// false: pop the DO line number and go to it.
		} else {
			DoStack.pop();										// true: pop the stack
		}
		return true;
	}

	private boolean executeINPUT(){

		if (!getStringArg()) return false;
		UserPrompt = StringConstant;
		if (!isNext(',')) return false;
		for (int i = UserPrompt.length(); i < 19 ; ++i) {
			UserPrompt = UserPrompt + (" ");
		}
		if (!getVar()) return false;
		InputIsNumeric = VarIsNumeric;
		InputVarIndex = theValueIndex;

		InputDefault = "";
		if (isNext(',')) {
			if (InputIsNumeric) {
				if (!evalNumericExpression()) return false;
				StringConstant = String.valueOf(EvalNumericExpressionValue);
			} else {
				if (!getStringArg()) return false;
			}
			InputDefault = StringConstant;
		}
		if (!checkEOL()) return false;

		WaitForInput = true;
		BadInput = false;
		InputCancelled = false;
		PrintShow("@@1");
		return true;
	}

	private AlertDialog.Builder doInputDialog(){
		Context context = getContext();
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		final EditText input = new EditText(context);
		input.setText(InputDefault);
		if (InputIsNumeric) input.setInputType(0x00003002); // Limits keys to signed decimal numbers
		dialog.setView(input);
		dialog.setCancelable(true);
		dialog.setTitle(UserPrompt);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface arg0) {
				InputCancelled = true;
			}
	    });
	    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

// ****************************************  start of Dialog Click Listener**************************************
//
//  This method is not executed until the user presses OK on the Dialog Box

			public void onClick(DialogInterface dialog, int whichButton) {
				String theInput = input.getText().toString();
				if (InputIsNumeric) {							// Numeric Input Handling
					double d = 0;
					try {
						d = Double.parseDouble(theInput.trim());// have java parse it into a double
					} catch (Exception e) {
						BadInput = true;
						return;
					}
					NumericVarValues.set(InputVarIndex, d);
				} else {										// String Input Handling
					StringVarValues.set(InputVarIndex, theInput);
				}
				WaitForInput = false;
				theAlertDialog = null;
			}});
//***************End of Input Click Listener *******************************************

		return dialog;
	}
	
	private boolean executeBACK_RESUME(){
		if (interruptResume == -1){
			RunTimeError("Back key not hit");
			return false;
		}
		return doResume();
	}
	
	private boolean executeMENUKEY_RESUME(){
		if (interruptResume == -1){
			RunTimeError("Menu key not hit");
			return false;
		}
		return doResume();
	}
	
	private boolean executeCONSOLETOUCH_RESUME(){
		if (interruptResume == -1){
			RunTimeError("Console not touched");
			return false;
		}
		return doResume();
	}
	
	private boolean doResume(){
		ExecutingLineIndex = interruptResume;
		interruptResume = -1;
		VarSearchStart = interruptVarSearchStart;
		// Pull the IEinterrupt from the If Else stack
		// It is possible that IFs were executed in the interrupt code
		// so pop entries until we get to the IEinterrupt
		while (IfElseStack.peek() != IEinterrupt) {
			IfElseStack.pop();
		}
		IfElseStack.pop();  // Top is stack is now IEInterrupt, pop it
				
		return true;
	}

	// ************************************** Read Commands ***************************************

	private boolean executeREAD() {								// Get READ command keyword if it is there
		return executeCommand(read_cmd, "Read");				// and execute the command
	}

	// Parse and bundle the data list of a READ.DATA statement
	// Called from PreScan() and NOT from statementExecuter().
	private boolean executeREAD_DATA() {
		do {													// Sweep up the data values
			Bundle b = new Bundle();
			if (GetStringConstant()) {							// If it is a string
				b.putBoolean("isNumber", false);				// Create a bundle for it
				b.putString("string", StringConstant);
			}
			else {												// Should be a number
				double signum = 1.0;							// Assume positive
				if (isNext('-')) { signum = -1.0; }				// Catch minus sign
				else if (isNext('+')) { ; }						// If not negative, eat optional '+'

				if (getNumber()) {								// If it is a number
					b.putBoolean("isNumber", true);				// Create a bundle for it
					b.putDouble("number", signum * GetNumberValue);
				}
				else {											// Else is a run time error
					RunTimeError("Invalid Data Value");
					return false;
				}
			}
			readData.add(b);									// Add the bundle to the list
		} while (isNext(','));									// and do again if more data

		return checkEOL();										// Nothing allowed after scan stops
	}

	private boolean executeREAD_NEXT(){
		
		do {
		
			if (readNext >= readData.size()) {					// Insure there is more data to read
				RunTimeError("No more data to read");
				return false;
			}
		
			Bundle b = readData.get(readNext);					// Get the data bundle
			++readNext;											// And increment to next bundle
			
			if (!getVar()) return false;						// Get the variable
		
			if (VarIsNumeric) {									// If var is numeric
				if (!b.getBoolean("isNumber")) {				// Insure data is a number
					RunTimeError("Data type (String) does match variable type (Number)");
					return false;
				}
				double d = b.getDouble("number");				// Get the number
				NumericVarValues.set(theValueIndex, d );		// and put it into the variable
				
			}else {												// else var is string
				if (b.getBoolean("isNumber")) {					// Insure data is a string
					RunTimeError("Data type (Number) does match variable type (String)");
					return false;
				}
				String s = b.getString("string");				// Get the string
				StringVarValues.set(theValueIndex, s );			// and put it into the variable
			}
		
		} while (isNext(','));									// loop while there are variables
		
		return checkEOL();
	}
	
	
	private boolean executeREAD_FROM(){
		if (!evalNumericExpression()) return false;
		int newIndex = EvalNumericExpressionValue.intValue();
		--newIndex;
		if (newIndex < 0 || newIndex >= readData.size()) {
			RunTimeError("Index out of range");
			return false;
		}
		
		readNext = newIndex;
		
		return checkEOL();
	}

	// ************************************** Debug Commands **************************************

	private boolean executeDEBUG() {							// Get debug command keyword if it is there
		return executeCommand(debug_cmd, "Debug");				// and execute the command
	}

	private boolean executeDEBUG_ON() {
		if (!checkEOL()) return false;
		Debug = true;
		return true;
	}

	private boolean executeDEBUG_OFF() {
		if (!checkEOL()) return false;
		Debug = false;
		Echo = false;
		return true;
	}

	private boolean executeDEBUG_PRINT() {
		if (Debug) executePRINT();
		return true;
	}

	private boolean executeECHO_ON(){
		if (!checkEOL()) return false;
		if (Debug) Echo = true;
		return true;
	}

	private boolean executeECHO_OFF(){
		if (!checkEOL()) return false;
		Echo = false;
		return true;
	}

	private boolean executeDUMP_SCALARS(){
		if (!Debug) return true;
		if (!checkEOL()) return false;

		ArrayList<String> lines = dbDoScalars("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_ARRAY(){
		if (!Debug) return true;

		String var = getVarAndType();
		if ((var == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (VarIsNew)						{ return RunTimeError(EXPECT_DIM_ARRAY); }
		// No checkEOL: ignore anything after the '['

		WatchedArray = VarNumber;
		ArrayList<String> lines = dbDoArray("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_LIST(){
		if (!Debug) return true;

		int listIndex = getListArg();							// get the list pointer
		if (listIndex == 0) return false;
		if (!checkEOL()) return false;

		WatchedList = listIndex;
		ArrayList<String> lines = dbDoList("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_STACK(){
		if (!Debug) return true;

		int stackIndex = getStackIndexArg();					// get the stack pointer
		if (stackIndex == 0) return false;
		if (!checkEOL()) return false;

		WatchedStack = stackIndex;
		ArrayList<String> lines = dbDoList("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_BUNDLE(){
		if (!Debug) return true;

		int bundleIndex = getBundleArg();						// get the Bundle pointer
		if (bundleIndex == 0) return false;
		if (!checkEOL()) return false;

		WatchedBundle = bundleIndex;
		ArrayList<String> lines = dbDoBundle("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	//=====================DEBUGGER DIALOG STUFF========================

	private boolean executeDEBUG_WATCH_CLEAR(){
		if(!Debug) return true;
		if (!checkEOL()) return false;

		WatchVarIndex.clear();
		Watch_VarNames.clear();
		return (WatchVarIndex.isEmpty() && Watch_VarNames.isEmpty());
	}

	private boolean executeDEBUG_WATCH(){				// separate the names and store them
		if (!Debug) return true;

		int max = ExecutingLineBuffer.length() - 1;
		int ni = LineIndex;								// start of name string
		do {
			int i = ExecutingLineBuffer.indexOf(',', ni);
			if (i < 0) { i = max; }
			String name = ExecutingLineBuffer.substring(ni, i);
			getVar();
			boolean add = true;
			for (int j = 0; j < WatchVarIndex.size(); ++j) {
				if (WatchVarIndex.get(j)==VarNumber) { add = false; }
			}
			if (add) {
				Watch_VarNames.add(name);
				WatchVarIndex.add(VarNumber);
			}
			LineIndex = ni = i + 1;
		} while (ni < max);
		return true;
	}

	private boolean executeDEBUG_SHOW_SCALARS(){
		DialogSelector(1);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_ARRAY(){
		if (!Debug) return true;

		String var = getVarAndType();
		if ((var == null) || !VarIsArray)	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (VarIsNew)						{ return RunTimeError(EXPECT_DIM_ARRAY); }

		WatchedArray = VarNumber;
		DialogSelector(2);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_LIST(){
		if (!Debug) return true;

		int listIndex = getListArg();							// get the list pointer
		if (listIndex == 0) return false;

		WatchedList = listIndex;
		DialogSelector(3);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_STACK(){
		if (!Debug) return true;

		int stackIndex = getStackIndexArg();					// get the stack pointer
		if (stackIndex == 0) return false;

		WatchedStack = stackIndex;
		DialogSelector(4);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_BUNDLE(){
		if (!Debug) return true;

		int bundleIndex = getBundleArg();						// get the Bundle pointer
		if (bundleIndex == 0) return false;

		WatchedBundle = bundleIndex;
		DialogSelector(5);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_WATCH(){
		if (!Debug) return true;
		DialogSelector(6);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_CONSOLE(){
		if (!Debug) return true;
		DialogSelector(7);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_PROGRAM(){
		if(!Debug) return true;
		DialogSelector(8);
		executeDEBUG_SHOW();
		return true;
	}

			private void DialogSelector(int selection){
				dbDialogScalars = false;
				dbDialogArray = false;
				dbDialogList = false;
				dbDialogStack = false;
				dbDialogBundle = false;
				dbDialogWatch = false;
				dbDialogConsole = false;
				dbDialogProgram = false;
				switch (selection){
				  case 1:
					  dbDialogScalars = true;
					  break;
					case 2:
				  	dbDialogArray = true;
					  break;
					case 3:
					  dbDialogList = true;
				  	break;
					case 4:
				  	dbDialogStack = true;
				  	break;
					case 5:
				  	dbDialogBundle = true;
					  break;
					case 6:
					  dbDialogWatch = true;
					  break;
					case 7:
				  	dbDialogConsole = true;
				  	break;
					default:
					  dbDialogProgram = true;
						break;
				}
			}

	private boolean executeDEBUG_SHOW() {				// trigger do debug dialog
		if (!Debug) return true;
		WaitForResume = true;
		sendMessage(MESSAGE_DEBUG_DIALOG);
		return true;
	}

	private String chomp(String str) {
		return str.substring(0, str.length() - 1);
	}

	private String quote(String str) {
		return '\"' + str + '\"';
	}

	private void doDebugDialog() {

		ArrayList<String> msg = new ArrayList<String>();

		if (!dbDialogProgram) {
			msg = dbDoFunc();
			msg.add("Executable Line #:    " + Integer.toString(ExecutingLineIndex + 1)
					+ '\n' + chomp(ExecutingLineBuffer));
		}

		if (dbDialogScalars) msg.addAll(dbDoScalars("  "));
		if (dbDialogArray)   msg.addAll(dbDoArray("  "));
		if (dbDialogList)    msg.addAll(dbDoList("  "));
		if (dbDialogStack)   msg.addAll(dbDoStack("  "));
		if (dbDialogBundle)  msg.addAll(dbDoBundle("  "));
		if (dbDialogWatch)   msg.addAll(dbDoWatch("  "));
 
		if (dbDialogProgram) {
			for (int i = 0; i < Basic.lines.size(); ++i) {
				msg.add(((i == ExecutingLineIndex) ? " >>" : "   ")	// mark current line
						+ (i + 1) + ": "							// one-based line index
						+ chomp(Basic.lines.get(i)));				// remove newline
			}
		}

		LayoutInflater inflater = getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.debug_dialog_layout, null);

		ListView debugView = (ListView)dialogLayout.findViewById(R.id.debug_list); 
		debugView.setAdapter(new ArrayAdapter<String>(this, R.layout.debug_list_layout, msg));
		debugView.setVerticalScrollBarEnabled(true);
		if (dbDialogProgram) { debugView.setSelection(ExecutingLineIndex); }

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setCancelable(true)
			.setTitle(R.string.debug_name)
			.setView(dialogLayout);

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface arg0) {
				DebuggerHalt = true;
				WaitForResume = false;
			}
		});

		builder.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				WaitForResume = false;
			}
		});

		builder.setNeutralButton("Step", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which){
				DebuggerStep = true;
				WaitForResume = true;
			}
		});

		// leave out until the switcher is done.
		builder.setNegativeButton("View Swap",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				dbSwap = true;
			}
		});

		dbDialog = builder.show();
		dbDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
									   WindowManager.LayoutParams.FILL_PARENT);
	}

	private void doDebugSwapDialog() {

		ArrayList<String> msg = new ArrayList<String>();
		msg.addAll(Arrays.asList("Program", "Scalars", "Array", "List", "Stack", "Bundle", "Watch"));
		final String[] names = {
			"View Program", "View Scalars", "View Array", "View List",
			"View Stack",   "View Bundle",  "View Watch", "View Console"
		};

		LayoutInflater inflater = getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.debug_list_s_layout, null);

		ListView debugView = (ListView)dialogLayout.findViewById(R.id.debug_list_s);
		debugView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_layout_1, msg));
		debugView.setVerticalScrollBarEnabled(true);
		debugView.setClickable(true);

		debugView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DialogSelector(position);
				boolean dosel = 
					(dbDialogArray  && WatchedArray  == -1) ||
					(dbDialogList   && WatchedList   == -1) ||
					(dbDialogStack  && WatchedStack  == -1) ||
					(dbDialogBundle && WatchedBundle == -1);
				if (dosel) {
					// if the element has not been defined ask if user wishes to do so.
					// or at least this is where it will go.
					// for now, default to view program.
					DialogSelector(0);
					position = 0;
				}
				String name = (position < names.length) ? names[position] : "";
				Toaster(name).show();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(this)
			.setCancelable(true)
			.setTitle("Select View:")
			.setView(dialogLayout);

		builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface arg0) {
				WaitForSwap = false;
			}
		});

		builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				WaitForSwap = false;
				dbSwap = false;
			}
		});

		/*  // leave out until the element selector is done.
		builder.setNeutralButton("Choose Element", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which){
				WaitForSelect = true;
			}
		});
		*/

		dbSwapDialog = builder.show();
		dbSwapDialog.getWindow().setLayout(WindowManager.LayoutParams.FILL_PARENT,
										   WindowManager.LayoutParams.FILL_PARENT);
	}

	private Toast Toaster(CharSequence msg) {			// default: short, high toast
		Toast toast = Toaster(msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
		return toast;
	}

	private Toast Toaster(CharSequence msg, int duration) {
		Toast toast = Toast.makeText(this, msg, duration);
		return toast;
	}

	private void doDebugSelectDialog() {
		if (dbSelectDialog != null) { dbSelectDialog.dismiss(); }

		ArrayList<String> msg = new ArrayList<String>();
		// TODO: What did Michael have in mind?
	}

	private ArrayList<String> dbDoWatch(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Watching:");

		int count = VarNames.size();
		if (!WatchVarIndex.isEmpty()) {
			int watchcount = WatchVarIndex.size();
			for (int j = 0; j < watchcount; ++j) {
				int wvi = WatchVarIndex.get(j);
				if (wvi < count) {
					String line = dbDoOneScalar(wvi, prefix);
					if (line != null) { msg.add(line); }
				} else {
					msg.add(Watch_VarNames.get(j) + " = Undefined");
				}
			}
		} else { msg.add("\n" + "Undefined."); }
		return msg;
	}

	private ArrayList<String> dbDoFunc() {
		ArrayList<String> msg = new ArrayList<String>();
		String msgs = "";
		if (!FunctionStack.isEmpty()) {
			Stack<Bundle> tempStack = (Stack<Bundle>) FunctionStack.clone();
			do {
				msgs = tempStack.pop().getString("fname") + msgs;
			} while (!tempStack.isEmpty());
		} else { msgs += "MainProgram"; }
		msg.add("In Function: " + msgs);
		return msg;
	}

	private ArrayList<String> dbDoScalars(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Scalar Dump");
		int count = VarNames.size();
		for (int varNum = 0; varNum < count; ++varNum) {
			String line = dbDoOneScalar(varNum, prefix);
			if (line != null) { msg.add(line); }
		}
		return msg;
	}

	private String dbDoOneScalar(int varNum, String prefix) {
		String var = VarNames.get(varNum);
		int len = (var == null) ? 0 : var.length();
		if (len == 0) {
			return(prefix + "Warning: zero-length variable name");
		}
		char last = var.charAt(len - 1);
		boolean isScalar = (last != '(') && (last != '[');
		if (isScalar) {
			boolean isString = (last == '$');
			String line = prefix + var;
			Integer Index = VarIndex.get(varNum).intValue();
			if (Index == null) {
				line += ": Warning: null variable index";
			} else {
				int index = Index.intValue();
				line += " = "
					 + (isString ? quote(StringVarValues.get(index))
								 : NumericVarValues.get(index).toString());
			}
			return line;
		}
		return null;
	}

	private ArrayList<String> dbDoArray(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		String var = VarNames.get(WatchedArray);
		msg.add("Dumping Array " + var + "]");

		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(WatchedArray));	// Get the array table bundle for this array
		if (ArrayEntry == null) {
			msg.add(prefix + "Warning: null array table entry");
		} else {
			int length = ArrayEntry.getInt("length");			// get the array length
			int base = ArrayEntry.getInt("base");				// and the start of the array in the variable space
			// ArrayList<Integer> dims = ArrayEntry.getIntegerArrayList("dims");
			// ArrayList<Integer> sizes = ArrayEntry.getIntegerArrayList("sizes");
			// msg.add("dims: " + dims.toString());
			// msg.add("sizes: " + sizes.toString());
			boolean isString = var.endsWith("$[");
			for (int i = 0; i < length; ++i) {
				msg.add(prefix +
						(isString ? quote(StringVarValues.get(base + i))
								  : NumericVarValues.get(base + i).toString()));
			}
		}
		return msg;
	}

	private ArrayList<String> dbDoList(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Dumping List " + WatchedList);

		if ((WatchedList < 0) || (WatchedList >= theLists.size())) {
			msg.add(prefix + "List has not been created.");
			return msg;
		}

		ArrayList list = theLists.get(WatchedList);				// get the list
		if (list == null) {
			msg.add(prefix + "Warning: null list variable");
			return msg;
		}

		int length = list.size();
		if (length == 0) {
			msg.add(prefix + "Empty List");
		} else {
			boolean isString = (theListsType.get(WatchedList) == list_is_string);
			for (Object item : list) {							// get each item
				String line;
				if (item == null) {
					line = "Warning: null list item";
				} else {
					line = item.toString();
					if (isString) { line = quote(line); }
				}
				msg.add(prefix + line);
			}
		}
		return msg;
	}

	private ArrayList<String> dbDoStack(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Dumping stack " + WatchedStack);

		if ((WatchedStack < 0) || (WatchedStack >= theStacks.size())) {
			msg.add(prefix + "Stack has not been created.");
			return msg;
		}

		Stack stack = theStacks.get(WatchedStack);				// get the stack
		if (stack == null) {
			msg.add(prefix + "Warning: null list variable");
		} else if (stack.isEmpty()) {
			msg.add(prefix + "Empty Stack");
		} else {
			Stack tempStack = (Stack)stack.clone();
			boolean isString = (theStacksType.get(WatchedStack) == list_is_string);
			do {
				String line;
				Object item = tempStack.pop();					// get each item
				if (item == null) {
					line = "Warning: null stack item";
				} else {
					line = item.toString();
					if (isString) { line = quote(line); }
				}
				msg.add(prefix + line);
			} while (!tempStack.isEmpty());
		}
		return msg;
	}

	private ArrayList<String> dbDoBundle(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Dumping Bundle " + WatchedBundle);

		if ((WatchedBundle < 0) || (WatchedBundle >= theBundles.size())) {
			msg.add(prefix + "Bundle has not been created.");
			return msg;
		}

		Bundle b = theBundles.get(WatchedBundle);				// get the bundle
		if (b == null) {
			msg.add(prefix + "Warning: null bundle variable");
			return msg;
		}

		Set<String> set = b.keySet();
		if (set.size() == 0) {
			msg.add(prefix + "Empty Bundle");
			return msg;
		}

		for (String s : set) {
			if (!s.startsWith("@@@N.")) {
				boolean isNumeric = b.getBoolean("@@@N." + s);
				msg.add(prefix + s + ": " +
						(isNumeric ? Double.toString(b.getDouble(s))
								   : quote(b.getString(s))));
			}
		}
		return msg;
	}

	public boolean handleDebugMessage(Message msg) {
		switch (msg.what) {
		case MESSAGE_DEBUG_DIALOG: doDebugDialog();       break;
		case MESSAGE_DEBUG_SWAP:   doDebugSwapDialog();   break;
		case MESSAGE_DEBUG_SELECT: doDebugSelectDialog(); break;
		default:
			return false;										// message not recognized
		}
		return true;											// message handled
	}

	// ********************************** User-Defined Functions **********************************

	private boolean executeFN() {									// Get User-defined Function (FN) command keyword if it is there
		return executeCommand(fn_cmd, "FN");
	}

	private boolean  executeFN_DEF(){								// Define Function

		String var = getNewFNVar();									// Get the function name
		if (var == null) { return false; }
		int fVarNumber = createNewVar(var);							// Save the VarNumber of the function name
		boolean fType = VarIsNumeric;

		ArrayList<String> fVarName = new ArrayList<String>();		// List of parameter names
		ArrayList<Integer> fVarType = new ArrayList<Integer>();		// List of parameter types
		ArrayList<Integer> fVarArray = new ArrayList<Integer>();	// A list of indicating if parm is array

		if (!isNext(')')) {
			do {													// Get each of the parameter names
				String name = parseVar(!USER_FN_OK);				// without creating any new vars
				if (name == null) { return false; }
				if (VarIsArray) {									// Process array
					if (!isNext(']')) {								// Array must not have any indices
						return RunTimeError(EXPECT_ARRAY_NO_INDEX);
					}
					fVarArray.add(1);								// 1 Indicates var is array
				} else fVarArray.add(0);							// 0 Indicates var is not an array

				fVarName.add(name);									// Save the name
				fVarType.add(VarIsNumeric ? 1 : 0);					// Save the type
			} while (isNext(','));
			if ( !(isNext(')') && checkEOL()) ) { return false; }
		}

		Bundle b = new Bundle();									// Build the bundle for the FunctionTable
		b.putInt("line", ExecutingLineIndex);						// Line number of fn.def
		b.putString("fname", VarNames.get(fVarNumber));				// Name of this function
		b.putBoolean("isNumeric", fType);							// Type of function
		b.putStringArrayList("pnames", fVarName);					// List of parameter names
		b.putIntegerArrayList("ptype", fVarType);					// List of parameter types
		b.putIntegerArrayList("array", fVarArray);					// List of parameter array flags
		
		int fn = FunctionTable.size();
		FunctionTable.add(b);										// Add the Bundle to the function table

		VarIndex.set(fVarNumber, fn);								// Associate the function number with
																	// the function name
		int max = Basic.lines.size();
		while (++ExecutingLineIndex < max) {						// Now scan for the fn.end
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
			if (ExecutingLineBuffer.startsWith("fn.end")) {			// Break out when found
				return true;
			}
			if (ExecutingLineBuffer.startsWith("fn.def")) {			// Insure not trying to define function in function
				return RunTimeError("Can not define a function within a function at:");
			}
		}
		return RunTimeError("No fn.end for this function");			// end of program, fn.end not found
	}

	private boolean  executeFN_RTN(){
		if (FunctionStack.empty()){							// Insure RTN actually called from executing function
			RunTimeError("misplaced fn.rtn");
			return false;
		}
		
		Bundle fsb = new Bundle();							
		fsb = FunctionStack.peek();							// Look at the Function Bundle on the stack
		
		if (fsb.getBoolean("isNum")){						// to determine if function is string or numeric
			if (!evalNumericExpression()) return false;
		}else{
			if (!evalStringExpression()) return false;
		}
		
		fnRTN = true;										// Signal RunLoop() to return
		return checkEOL();
	}

	private boolean executeFN_END(){
		if (FunctionStack.empty()){							// Insure END actually called from executing function
			RunTimeError("misplaced fn.end");
			return false;
		}

		Bundle fsb = new Bundle();							
		fsb = FunctionStack.peek();							// Look at the Function Bundle on the stack

		if (fsb.getBoolean("isNum")){						// to determine if function is string or numeric
			EvalNumericExpressionValue = 0.0;				// Set default value
		}else{
			StringConstant = "";							// Set default value
		}

		fnRTN = true;										// Signal RunLoop() to return
		return checkEOL();
	}

	private boolean executeCALL(){
		if (isUserFunction(TYPE_NUMERIC)) return doUserFunction();
		if (isUserFunction(TYPE_STRING)) return doUserFunction();
		return false;
	}

	private boolean isUserFunction(boolean isNumeric){

		if (FunctionTable.size() == 0) return false;					// If function table empty, return fail

		for (Bundle b : FunctionTable) {								// for each Bundle in the Function Table
			String name = b.getString("fname");							// get the function name
			if (ExecutingLineBuffer.startsWith(name, LineIndex)) {		// if in list
				if (isNumeric != b.getBoolean("isNumeric")) return false;
				ufBundle = b;
				LineIndex += name.length();
				return true;											// report found
			}
		}
		return false;													// report fail
	}

	private boolean doUserFunction(){

	Bundle fsb = new Bundle();											// The Function Stack Bundle

	int sVarNames = VarNames.size();									// Save a bunch of things that will need to be restored
	int sVarIndex = VarIndex.size();
	fsb.putBoolean("isNum", ufBundle.getBoolean("isNumeric"));
	fsb.putInt("SVN", sVarNames);
	fsb.putInt("VI", sVarIndex);
	fsb.putInt("VSS", VarSearchStart);
	fsb.putInt("NVV", NumericVarValues.size());
	fsb.putInt("SVV", StringVarValues.size());
	fsb.putInt("AT", ArrayTable.size());
	fsb.putInt("ELI", ExecutingLineIndex);
	fsb.putString("PKW", PossibleKeyWord);
	fsb.putString("fname",ufBundle.getString("fname"));

	ArrayList<String> fVarName;											// The list of Parm Var Names
	ArrayList<Integer> fVarType;										// and the parm types
	ArrayList<Integer> fVarArray;										// and the parm types
	
	fVarName = ufBundle.getStringArrayList("pnames"); 					// Get the Names and Types from the ufBundle                  
	fVarType = ufBundle.getIntegerArrayList("ptype");					// ufBundle set by isUserFunction()
	fVarArray = ufBundle.getIntegerArrayList("array");

	int pCount = fVarName.size();										// The number of parameter

	int i = 0;
	if (pCount != 0) {													// For each parameter
		do {
			if (i >= pCount) {											// Insure no more parms than defined
				return RunTimeError("Calling parameter count exceeds defined parameter count");
			}

			boolean isGlobal = isNext('&');								// optional for scalars, ignored for arrays
			boolean typeIsNumeric = (fVarType.get(i) != 0);
			if (fVarArray.get(i) == 1){									// if this parm is an array
				if (getArrayVarForRead() == null) { return false; }		// get the array name var
				if (!isNext(']')) {										// must be no indices
					return RunTimeError(EXPECT_ARRAY_NO_INDEX);
				} else  if (typeIsNumeric != VarIsNumeric) {			// insure type (string or number) match
					return RunTimeError("Array parameter type mismatch at:");
				}

				VarNames.add(fVarName.get(i));							// and add the var name
				VarIndex.add(VarIndex.get(VarNumber));					// copy array table pointer to the new array.
			} // end array
			else {
				if (isGlobal) {
					String var = getVarAndType();						// if this is a Global Var
					if (var == null)		{ return false; }			// then must be var not expression
					if (VarIsNew) { return RunTimeError("Call by reference vars must be predefined"); }
					if (typeIsNumeric != VarIsNumeric) {				// insure type (string or number) match
						return RunTimeError("Global parameter type mismatch at:");
					}
					if (!getVarValue(var))	{ return false; }			// bottom half of getVar()

					VarNames.add(fVarName.get(i));						// add the called var name to the var name table
					VarIndex.add(theValueIndex);						// but give it the value index of the calling var
				} // end global
				else {
					if (!typeIsNumeric) {								// if parm is string
						if (!evalStringExpression()) {					// get the string value
							return RunTimeError("Parameter type mismatch at:");
						}else{
							VarIndex.add(StringVarValues.size());				// Put the string value into the
							StringVarValues.add(StringConstant);				// string var values table
							VarNames.add(fVarName.get(i));						// and add the var name
						}
					} else {
						if (!evalNumericExpression()) {					// if parm is number get the numeric value
							return RunTimeError("Parameter type mismatch at:");
						} else {
							VarIndex.add(NumericVarValues.size());				// Put the number values into the
							NumericVarValues.add(EvalNumericExpressionValue);	// numeric var values table
							VarNames.add(fVarName.get(i));						// and add the var name
						}
					}
				}
			} // end scalar

			++i;											//  Keep going while calling parms exist

		} while ( isNext(','));
	} // end if

	if (i != pCount) { return RunTimeError("Too few calling parameters at:"); }

	// Every function must have a closing right parenthesis.
	if (!isNext(')')) { return false; }

	fsb.putInt("LI", LineIndex);                        // Save out index into the line buffer

	FunctionStack.push(fsb);							// Push the function bundle
	VarSearchStart = sVarNames;							// Set the new start location for var name searches

	ExecutingLineIndex = ufBundle.getInt("line")+1;     // Set to execute first line after fn.def statement

	fnRTN = false;										// Will be set true by fn.rtn
														// cause RunLoop() to return when true

	boolean flag = theBackground.RunLoop();				// Now go execute the function

	if (FunctionStack.isEmpty()){
		RunTimeError("System problem. Wait 10 seconds before rerunning.");
		return false;
	}

	fsb = FunctionStack.pop();							// Function execution done. Restore stuff

	trimArray(VarNames, fsb.getInt("SVN"));
	trimArray(VarIndex, fsb.getInt("VI"));
	VarSearchStart = fsb.getInt("VSS");
	trimArray(NumericVarValues, fsb.getInt("NVV"));
	trimArray(StringVarValues, fsb.getInt("SVV"));
	trimArray(ArrayTable, fsb.getInt("AT"));
	ExecutingLineIndex = fsb.getInt("ELI");
	LineIndex = fsb.getInt("LI");
	PossibleKeyWord = fsb.getString("PKW");

	ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);

	return flag;                                      // Pass on the pass/fail state from the function
	}

	// ************************************ Switch Statements *************************************

	private boolean executeSW() {								// Get Switch (SW) command keyword if it is there
		return executeCommand(sw_cmd, "SW");
	}

	private boolean executeSW_BEGIN(){
		boolean isNumeric;
		double nexp = 0;
		String sexp = "";
		if (evalNumericExpression()){
			isNumeric = true;
			nexp = EvalNumericExpressionValue;
		}else if (evalStringExpression()){
			isNumeric = false;
			sexp = StringConstant;
		}else return false;
		if (!checkEOL()) { return false; }

		while (++ExecutingLineIndex < Basic.lines.size()) {
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);  // Next program line
			if (ExecutingLineBuffer.startsWith("sw.end")) {
				LineIndex = 6;
				return (checkEOL());
			}
			if (ExecutingLineBuffer.startsWith("sw.default")){
				LineIndex = 10;
				return (checkEOL());
			}
			if (ExecutingLineBuffer.startsWith("sw.case")){
				LineIndex = 7;
				if (isNumeric){
					if (!evalNumericExpression()) return false;
					if (!checkEOL()) return false;
					if (nexp == EvalNumericExpressionValue) return true;
				}else{
					if (!evalStringExpression()) return false;
					if (!checkEOL()) return false;
					if (sexp.equals(StringConstant)) return true;
				}
			}
		}
		RunTimeError("No sw.end after sw.begin");
		return false;
	}
	
	private boolean executeSW_CASE(){
		return true;
	}
	
	private boolean executeSW_BREAK(){
		if (!checkEOL()) { return false; }
		while (++ExecutingLineIndex < Basic.lines.size()){
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);  // Next program line
			if (ExecutingLineBuffer.startsWith("sw.end")) {
				LineIndex = 6;
				return checkEOL();
			}
		}
		RunTimeError("sw.xxxx without sw.end");
		return false;
	}
	
	private boolean executeSW_DEFAULT(){
		return true;
	}
	
	private boolean executeSW_END(){
		return true;
	}
	
// *****************************  End of core Basic Methods ****************************
	
// ***************************** Data I/O Operations ***********************************

	// args: stream to flush, previous exception or null;
	// return: previous exception if any, else new exception if any, else null 
	private static IOException flushStream(Flushable stream, IOException ex) {	// flush a stream
		if (stream == null) { return ex; }
		try { stream.flush(); return ex; }
		catch ( IOException e ) { return (ex == null) ? e : ex; }
	}

	// args: stream to close, previous exception or null
	// return: previous exception if any, else new exception if any, else null 
	private static IOException closeStream(Closeable stream, IOException ex) {	// close a stream
		if (stream == null) { return ex; }
		try { stream.close(); return ex; }
		catch ( IOException e ) { return (ex == null) ? e : ex; }
	}

	private boolean checkReadFile(int FileNumber, ArrayList<?> list){		// Validate input file for read commands
		// Note: if list is null, list size is not checked - needed for POSITION_GET
		if (FileTable.size() == 0)                  { RunTimeError("No files opened"); }
		else if (FileNumber < 0)                    { RunTimeError("Read file did not exist"); }
		else if (list != null && list.size() == 0)  { RunTimeError("No files opened for read"); }
		else if (FileNumber >= FileTable.size())    { RunTimeError("Invalid File Number at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkWriteFile(int FileNumber, ArrayList<?> list){		// Validate input file for write commands 
		if (FileTable.size() == 0)                  { RunTimeError("No files opened"); }
		else if (list.size() == 0)                  { RunTimeError("No files opened for write"); }
		else if (FileNumber >= FileTable.size() || FileNumber < 0)
													{ RunTimeError("Invalid File Number at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkReadTextAttributes(Bundle FileEntry) {				// Validate common FileEntry items
																			// for commands that read text files
		int FileMode = FileEntry.getInt("mode");
		boolean closed = FileEntry.getBoolean("closed");
		boolean isText = FileEntry.getBoolean("isText");

		if (!isText)              { RunTimeError("File not opened for text"); }
		else if (closed)          { RunTimeError("File is closed"); }
		else if (FileMode != FMR) { RunTimeError("File not opened for read at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkReadByteAttributes(Bundle FileEntry) {				// Validate common FileEntry items
																			// for commands that read binary files
		int FileMode = FileEntry.getInt("mode");
		boolean closed = FileEntry.getBoolean("closed");
		boolean isText = FileEntry.getBoolean("isText");

		if (isText)               { RunTimeError("File not opened for byte"); }
		else if (closed)          { RunTimeError("File is closed"); }
		else if (FileMode != FMR) { RunTimeError("File not opened for read at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkWriteTextAttributes(Bundle FileEntry) {			// Validate common FileEntry items
																			// for commands that write text files
		int FileMode = FileEntry.getInt("mode");
		boolean closed = FileEntry.getBoolean("closed");
		boolean isText = FileEntry.getBoolean("isText");

		if (!isText)              { RunTimeError("File not opened for text"); }
		else if (closed)          { RunTimeError("File is closed"); }
		else if (FileMode != FMW) { RunTimeError("File not opened for write at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkWriteByteAttributes(Bundle FileEntry) {			// Validate common FileEntry items
																			// for commands that write binary files
		int FileMode = FileEntry.getInt("mode");
		boolean closed = FileEntry.getBoolean("closed");
		boolean isText = FileEntry.getBoolean("isText");

		if (isText)               { RunTimeError("File not opened for byte"); }
		else if (closed)          { RunTimeError("File is closed"); }
		else if (FileMode != FMW) { RunTimeError("File not opened for write at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	// ************************************* Text Stream I/O **************************************

	private boolean executeTEXT() {									// Get Text command keyword if it is there
		return executeCommand(text_cmd, "Text");
	}

	private boolean executeTEXT_OPEN(){										// Open a file
		boolean append = false;												// Assume not append
		int FileMode = 0;													// Default to FMR
		switch (ExecutingLineBuffer.charAt(LineIndex)) {					// First parm is a, w or r
		case 'a':
			append = true;					// append is a special case of write
		case 'w':							// write
			FileMode = FMW;
			++LineIndex;
			break;
		case 'r':							// read
			FileMode = FMR;
			++LineIndex;
		}
		if (!isNext(',')) { return false; }
		if (!getNVar()) { return false; }			// Next parameter is the FileNumber variable
		NumericVarValues.set(theValueIndex, (double) FileTable.size());
		int saveValueIndex = theValueIndex;			// Save in case read file not found.

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }		// Final parameter is the filename
		String fileName = StringConstant;
		if (!checkEOL()) return false;

		Bundle FileEntry = new Bundle();			// Prepare the filetable bundle
		FileEntry.putInt("mode", FileMode);
		FileEntry.putBoolean("eof", false);
		FileEntry.putBoolean("closed",false);
		FileEntry.putInt("position", 1);
		FileEntry.putBoolean("isText", true);

		if (FileMode == FMR) {											// Read was selected
			BufferedReader buf = null;
			try { buf = Basic.getBufferedReader(Basic.DATA_DIR, fileName); } // closes stream if error on mark()
			catch (FileNotFoundException ex1) { }
			catch (NotFoundException ex2) { }
			catch (Exception e) { return RunTimeError("Problem: " + e + " at:"); } // Something other than not found (?)
			if (buf == null) {
				if (Basic.isAPK) {
					return false;
				} else {												// standard BASIC!
					NumericVarValues.set(saveValueIndex, (double) -1);	// change file index to report file does not exist
					return true;
				}
			}

			FileEntry.putInt("stream", BRlist.size());		// The stream parm indexes
			BRlist.add(buf);								// into the BRlist
			FileTable.add(FileEntry);
		}

		else if (FileMode == FMW) {										// Write Selected
			File file = new File(Basic.getDataPath(fileName));
			if (append && file.exists()) {
				FileEntry.putInt("position", (int) file.length()+1);
			} else {										// if not appending overwrite existing file
				try {										// if no file create a new one
					file.createNewFile();
				} catch (Exception e) {
					return RunTimeError(e);
				}
			}
			if (!(file.exists() && file.canWrite())) {
				RunTimeError("Problem opening " + fileName);
				return false;
			}

			FileWriter writer = null;
			try {
				writer = new FileWriter(file, append);		// open the filewriter for the SD Card
			} catch (Exception e) {
				return RunTimeError(e);
			}

			FileEntry.putInt("stream", FWlist.size());		// The stream parm indexes
			FWlist.add(writer);								// into the FWlist
			FileTable.add(FileEntry);
		}
		return true;													// Done
	}
	
	private  boolean executeTEXT_CLOSE(){
		if (FileTable.size() == 0){
			return true;
		}

		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (FileNumber >= FileTable.size() || FileNumber < 0 ) {
			RunTimeError("Invalid File Number at");
			return false;
		}
		if (!checkEOL()) return false;

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		int FileMode = FileEntry.getInt("mode");
		boolean closed = FileEntry.getBoolean("closed");
		boolean isText = FileEntry.getBoolean("isText");

		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}
		
		if (closed) { return true; }							// Already closed
		
		if (FileMode == FMR) {									// Close file open for read
			BufferedReader buf = BRlist.get(FileEntry.getInt("stream"));
    		try {
    			buf.close();
    		} catch (Exception e) {
    			return RunTimeError(e);
			}
		}
		else if (FileMode == FMW) {								// close file open for write
			FileWriter writer = FWlist.get(FileEntry.getInt("stream"));
			try {
				writer.flush();
				writer.close();
			} catch (Exception e) {
    			return RunTimeError(e);
			}
		} else {
			RunTimeError("File not opened for read or write");
			return false;
		}
		FileEntry.putBoolean("closed", true);					// mark the file closed.
		return true;
	}
	
	private  boolean executeTEXT_READLN(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, BRlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }
		if (!getSVar()) { return false; }									// Second parm is the string var
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadTextAttributes(FileEntry)) { return false; }			// Check runtime errors

		boolean eof = FileEntry.getBoolean("eof");
		String data = null;
		if (eof) {											// If already eof don't read
			data = "EOF";									// but force returned data to "EOF"
		} else {
			BufferedReader buf = BRlist.get(FileEntry.getInt("stream"));
			try {											// Read a line
				data = buf.readLine();
			} catch (IOException e) {
				RunTimeError("I/O error at:");
				return false;
			}
			if (data == null){
				data = "EOF";								// Hit eof, force returned data
				FileEntry.putBoolean("eof", true);			// and mark Bundle
			} else {
				int p = FileEntry.getInt("position");		// Not eof, update position in Bundle
				FileEntry.putInt("position", ++p);
			}
		}
		StringVarValues.set(theValueIndex, data);			// Give the data to the user
		return true;
	}
	
	private boolean executeTEXT_WRITELN(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();

		if (!isNext(',')) { return false; }				// Set up to parse the stuff to print

		if (!buildPrintLine(textPrintLine, "\r\n")) return false;	// build up the text line in StringConstant
		if (!PrintLineReady) {							// flag set by buildPrintLine
			textPrintLine = StringConstant;				// not ready to print; hold line
			return true;								// and wait for next Text.Writeln command
		}
		textPrintLine = "";								// clear the accumulated text print line

		if (!checkWriteFile(FileNumber, FWlist)) { return false; }			// Check runtime errors
		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkWriteTextAttributes(FileEntry)) { return false; }			// Check runtime errors

		FileWriter writer = FWlist.get(FileEntry.getInt("stream"));
		try {
			writer.write(StringConstant);				// Oh, and write the line
		} catch (IOException e) {
			RunTimeError("I/O error at");
			return false;
		}
		
		int p = FileEntry.getInt("position");
		FileEntry.putInt("position", ++p);

		return true;
	}

	private boolean executeTEXT_INPUT(){
		if (!getSVar()) return false;
		int saveValueIndex = theValueIndex;

		TextInputString = "";
		String title = null;
		if (isNext(',')) {													// Check for optional parameter(s)
			boolean isComma = isNext(',');									// Look for second comma, two commas together
																			// mean initial text is skipped, use empty string
			if (!isComma) {
				if (!getStringArg()) return false;							// One comma so far; get initial input text
				TextInputString = StringConstant;
				isComma = isNext(',');										// Look again for second comma
			}
			if (isComma) {
				if (!getStringArg()) return false;							// Second comma; get title
				title = StringConstant;
			}
		}
		if (!checkEOL()) return false;

		Intent intent = new Intent(this, TextInput.class);
		if (title != null) { intent.putExtra("title", title); }
		HaveTextInput = false;
		startActivityForResult(intent, BASIC_GENERAL_INTENT);

		synchronized (LOCK) {
			while (!HaveTextInput) {
				try { LOCK.wait(); }										// Wait for signal from TextInput.java thread
				catch (InterruptedException e) { HaveTextInput = true; }
			}
		}
		StringVarValues.set(saveValueIndex, TextInputString);
		return true;
	}
		
	private boolean executeTEXT_POSITION_SET(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, BRlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }									// Second parm is the position var expression
		if (!evalNumericExpression()) {return false; }
		int pto = EvalNumericExpressionValue.intValue();
		if (pto < 1) {
			RunTimeError("Set position must be >= 1");
			return false;
		}
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadTextAttributes(FileEntry)) { return false; }			// Check runtime errors
		
		BufferedReader buf = BRlist.get(FileEntry.getInt("stream"));
		int pnow = FileEntry.getInt("position");
		boolean eof = FileEntry.getBoolean("eof");
		
		if (pto < pnow) {
			try {
				buf.reset();								// Back to start of file
			} catch (Exception e) {
				return RunTimeError(e);
			}
			eof = false;
			pnow = 1;
		}
		String data = null;
		while ((pnow < pto) && !eof) {
			try {											// Read a line
				data = buf.readLine();
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (data == null) {
				eof = true;									// Hit eof, mark Bundle
			} else {
				++pnow;										// Not eof, update position for Bundle
			}
		}
		FileEntry.putInt("position", pnow);					// Update Bundle
		FileEntry.putBoolean("eof", eof);
		return true;
	}

	private boolean executeTEXT_POSITION_GET(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, null)) { return false; }				// Check runtime errors

		if (!isNext(',')) { return false; }									// Second parm is the position var
		if (!getNVar()) { return false; }
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);		// Get the bundle
		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		double p = FileEntry.getInt("position");
		NumericVarValues.set(theValueIndex, p);

		return true;
	}
		
	private boolean executeTGET(){
		if (!getSVar()) return false;
		int saveValueIndex = theValueIndex;

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;
		TextInputString = StringConstant;
		String Prompt = StringConstant;

		String title = null;
		if (isNext(',')) {
			if (!getStringArg()) return false;
			title = StringConstant;
		}
		if (!checkEOL()) return false;

		Intent intent = new Intent(this, TGet.class);
		if (title != null) { intent.putExtra("title", title); }
		HaveTextInput = false;
		startActivityForResult(intent, BASIC_GENERAL_INTENT);

		synchronized (LOCK) {
			while (!HaveTextInput) {
				try { LOCK.wait(); }										// Wait for signal from TGet.java thread
				catch (InterruptedException e) { HaveTextInput = true; }
			}
		}
		PrintShow(Prompt + TextInputString);

		StringVarValues.set(saveValueIndex, TextInputString);
		return true;
	}

	// ************************************* Byte Stream I/O **************************************

	private boolean executeBYTE() {							// Get Byte command keyword if it is there
		return executeCommand(byte_cmd, "Byte");
	}

	private boolean executeBYTE_OPEN(){										// Open a file
		boolean append = false;												// Assume not append
		int FileMode = 0;													// Default to FMR
		switch (ExecutingLineBuffer.charAt(LineIndex)) {					// First parm is a, w or r
		case 'a':
			append = true;					// append is a special case of write
		case 'w':							// write
			FileMode = FMW;
			++LineIndex;
			break;
		case 'r':							// read
			FileMode = FMR;
			++LineIndex;
		}
		if (!isNext(',')) { return false; }
		if (!getNVar()) { return false; }			// Next parameter is the FileNumber variable
		NumericVarValues.set(theValueIndex, (double) FileTable.size());
		int saveValueIndex = theValueIndex;			// Save in case read file not found.

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }		// Final parameter is the filename
		String fileName = StringConstant;
		if (!checkEOL()) return false;

		Bundle FileEntry = new Bundle();			// Prepare the filetable bundle
		FileEntry.putInt("mode", FileMode);
		FileEntry.putBoolean("eof", false);
		FileEntry.putBoolean("closed",false);
		FileEntry.putInt("position", 1);
		FileEntry.putBoolean("isText", false);
		
		if (FileMode == FMR) {												// Read was selected
			BufferedInputStream buf = null;
			if (fileName.startsWith("http")) {
				try {
					URL url = new URL(fileName);
					URLConnection connection = url.openConnection();
					buf = new BufferedInputStream(connection.getInputStream());
				} catch (Exception e) {
					return RunTimeError("Problem: " + e + " at:");
				}
			} else {
				try { buf = Basic.getBufferedInputStream(Basic.DATA_DIR, fileName); }
				catch (FileNotFoundException ex1) { }
				catch (NotFoundException ex2) { }
				catch (Exception e) { return RunTimeError("Problem: " + e + " at:"); } // Something other than not found (?)
				if (buf == null) {
					if (Basic.isAPK) {
						return false;
					} else {												// standard BASIC!
						NumericVarValues.set(saveValueIndex, (double) -1);	// change file index report file does not exist
						return true;
					}
				}
			}

			FileEntry.putInt("stream", BISlist.size());		// The stream parm indexes
			BISlist.add(buf);								// into the BISlist
			FileTable.add(FileEntry);
		}

		else if (FileMode == FMW) {											// Write Selected
																			// Write to SD Card
			File file = new File(Basic.getDataPath(fileName));
			if (append && file.exists()) {
				FileEntry.putInt("position", (int) file.length()+1);
			} else {										// if not appending overwrite existing file
				try {										// if no file create a new one
					file.createNewFile();
				} catch (Exception e) {
					return RunTimeError(e);
				}
			}
			if (!(file.exists() && file.canWrite())) {
				RunTimeError("Problem opening " + fileName);
				return false;
			}

			String afile = file.getAbsolutePath();
			DataOutputStream dos = null;
			try {
				FileOutputStream fos = new FileOutputStream(afile, append);
				dos = new DataOutputStream(fos);
			}				
			catch (Exception e) {
				return RunTimeError(e);
			}
			
			FileEntry.putInt("stream", DOSlist.size());		// The stream parm indexes
			DOSlist.add(dos);								// into the DOSlist
			FileTable.add(FileEntry);
		}
		return true;														// Done
	}
	
	private  boolean executeBYTE_CLOSE(){
		if (FileTable.size() == 0){
			return true;
		}

		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (FileNumber >= FileTable.size() || FileNumber < 0) {
			RunTimeError("Invalid File Number at");
			return false;
		}
		if (!checkEOL()) return false;

		Bundle FileEntry = FileTable.get(FileNumber);		// Get the bundle
		int FileMode = FileEntry.getInt("mode");
		boolean closed = FileEntry.getBoolean("closed");
		boolean isText = FileEntry.getBoolean("isText");

		if (isText){
			RunTimeError("File not opened for byte");
			return false;
		}

		if (closed) { return true; }						// Already closed

		if (FileMode == FMR) {								// Close file open for read
			BufferedInputStream bis = BISlist.get(FileEntry.getInt("stream"));
			try {
				bis.close();
			} catch (IOException e) {
//				Log.e(LOGTAG, e.getLocalizedMessage() + " 3");
				return RunTimeError(e);
			}
		} else if (FileMode == FMW) {						// close file open for write
			DataOutputStream dos = DOSlist.get(FileEntry.getInt("stream"));
			try {
				dos.flush();
				dos.close();
			} catch (IOException e) {
//				Log.e(LOGTAG, e.getLocalizedMessage() + " 3");
				return RunTimeError(e);
			}
		} else {
			RunTimeError("File not opened for read or write");
			return false;
		}
		FileEntry.putBoolean("closed", true);				// mark the file closed.
		return true;
	}
	
	private boolean executeBYTE_COPY(){
		if (!evalNumericExpression()) { return false; }						// First parm is the source filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, BISlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }
		if (!evalStringExpression()) { return false; }						// Second parm is the destination file name
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		boolean eof = FileEntry.getBoolean("eof");
		if (eof){											// Check not EOF
			RunTimeError("Attempt to read beyond the EOF at:");
			return false;
		}
		
		BufferedInputStream bis = BISlist.get(FileEntry.getInt("stream"));
		int p = FileEntry.getInt("position");
		
		String theFileName = StringConstant;
		File file = new File(Basic.getDataPath(theFileName));

		try {
			file.createNewFile();
		} catch (IOException e) {
			RunTimeError(theFileName + " I/O Error");
			return false;
		}
		if (!file.exists() || !file.canWrite()) {
			RunTimeError("Problem opening " + theFileName);
			return false;
		}
		
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file), 8192);
			byte[] buffer = new byte[1024];
			int len = 0;

			while((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				p += len;
			}
		} catch (Exception e) {
			RunTimeError("Exception: " + e);
			return false;
		} finally {
			try { bos.flush(); } catch (Exception e) { return RunTimeError(e); }
			try { bis.close(); } catch (Exception e) { return RunTimeError(e); }
			try { bos.close(); } catch (Exception e) { return RunTimeError(e); }
		}
		FileEntry.putInt("position", p);
		FileEntry.putBoolean("eof", true);
		return true;
	}

	private  boolean executeBYTE_READ_BYTE(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, BISlist)) { return false; }			// Check runtime errors
		
		if (!isNext(',')) { return false; }
		if (!getNVar()) { return false; }									// Second parm is the return data var
		if (!checkEOL()) { return false; }
		
		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		boolean eof = FileEntry.getBoolean("eof");
		int data = -1;
		if (!eof) {											// If already eof don't read
			BufferedInputStream bis = BISlist.get(FileEntry.getInt("stream"));
			int p = FileEntry.getInt("position");
			try {
				data = bis.read();							// Read a byte
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (data < 0) {
				FileEntry.putBoolean("eof", true);			// Hit eof, mark Bundle
			} else {
				FileEntry.putInt("position", ++p);			// Not eof, update position in Bundle
			}
		}
		NumericVarValues.set(theValueIndex, (double) data);	// Give the data to the user
		return true;
	}
		
	private boolean executeBYTE_READ_BUFFER(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, BISlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }
		if (!evalNumericExpression()) { return false; }						// Second parm is the byte count
		int byteCount = EvalNumericExpressionValue.intValue();

		if (!isNext(',')) { return false; }									// Third parm is the return buffer string
		if (!getSVar()) { return false; }
		if (!checkEOL()) { return false; }
		
		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		boolean eof = FileEntry.getBoolean("eof");
		String buff = "";
		if (!eof) {											// If already eof don't read
			BufferedInputStream bis = BISlist.get(FileEntry.getInt("stream"));
			int p = FileEntry.getInt("position");
			byte[] byteArray = new byte[byteCount];
			int count = 0;
			try {
				count = bis.read(byteArray, 0, byteCount);	// Read the bytes
			}catch (Exception e) {
				return RunTimeError(e);
			}
			if (count < 0) {
				FileEntry.putBoolean("eof", true);			// Hit eof, mark Bundle
			} else {
				FileEntry.putInt("position", p + count);	// No eof, update position in Bundle
				buff = new String(byteArray, 0);			// convert bytes to String for user
				if (count < byteCount) {
					buff = buff.substring(0, count);
				}
			}
		}
		StringVarValues.set(theValueIndex, buff);			// Give the data to the user
		return true;
	}
	
	private  boolean executeBYTE_WRITE_BYTE(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkWriteFile(FileNumber, DOSlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }									// Second parm is the byte var

		byte b = 0;
		boolean OutputIsByte = true;
		if (evalNumericExpression()) {
			b = EvalNumericExpressionValue.byteValue();
		} else {
			if (!evalStringExpression()) { return false; }
			OutputIsByte = false;
		}
		if (!checkEOL()) { return false; }
	
		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkWriteByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		DataOutputStream dos = DOSlist.get(FileEntry.getInt("stream"));
		int p = FileEntry.getInt("position");
		try {
			if (OutputIsByte) {
				dos.writeByte(b);							// Oh, and write the byte
				++p;
			} else {
				int len = StringConstant.length();
				for (int k = 0; k < len; ++k) {				// or bytes
					b = (byte)StringConstant.charAt(k);
					dos.writeByte(b);
				}
				p += len;
			}
		} catch (IOException e) {
			RunTimeError("I/O error at");
			return false;
		}
		FileEntry.putInt("position", p);					// update position in Bundle
		return true;
	}
		
	private boolean executeBYTE_WRITE_BUFFER(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkWriteFile(FileNumber, DOSlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }									// Second parm is the buffer
		if (!evalStringExpression()) { return false; }
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkWriteByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		DataOutputStream dos = DOSlist.get(FileEntry.getInt("stream"));
		int p = FileEntry.getInt("position");
		int len = StringConstant.length();
		try {
			for (int k = 0; k < len; ++k) {									// Write the buffer
				byte bb = (byte)StringConstant.charAt(k);
				dos.writeByte(bb);
			}
		} catch (IOException e) {
			RunTimeError("I/O error at");
			return false;
		}
		p += len;
		FileEntry.putInt("position", p);					// update position in Bundle
		return true;
		}

		
	private boolean executeBYTE_POSITION_SET(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, BISlist)) { return false; }			// Check runtime errors

		if (!isNext(',')) { return false; }									// Second parm is the position var expression
		if (!evalNumericExpression()) { return false; }
		int pto = EvalNumericExpressionValue.intValue();
		if (pto < 1) {
			RunTimeError("Set position must be >= 1");
			return false;
		}
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadByteAttributes(FileEntry)) { return false; }			// Check runtime errors
		
		BufferedInputStream bis = BISlist.get(FileEntry.getInt("stream"));
		int pnow = FileEntry.getInt("position");
		boolean eof = FileEntry.getBoolean("eof");

		if (pto < pnow) {
			try {
				bis.reset();								// Back to start of file
			} catch (Exception e) {
				return RunTimeError(e);
			}
			eof = false;
			pnow = 1;
		}
		if ((pnow != pto) && !eof) {
			int skip = pto - pnow - 1;						// Skip plus single read will get to target position
			int skipped = 0;
			int data = -1;
			do {
				try {
					int avail = bis.available();			// Don't skip past eof
					skipped += (int)bis.skip(Math.min(skip - skipped, avail));
					data = bis.read();						// Read to check eof
				} catch (Exception e) {
					return RunTimeError(e);
				}
				if (data >= 0) { ++skipped; }				// If byte was read, count it
				else { eof = true; break; }					// otherwise mark eof in Bundle
			} while (skipped < skip);
			pnow += skipped;								// Count bytes skipped
		}
		FileEntry.putInt("position", pnow);					// Update Bundle
		FileEntry.putBoolean("eof", eof);
		return true;
	}
		
	private boolean executeBYTE_POSITION_GET(){
		if (!evalNumericExpression()) { return false; }						// First parm is the filenumber expression
		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkReadFile(FileNumber, null)) { return false; }				// Check runtime errors

		if (!isNext(',')) { return false; }									// Second parm is the position var
		if (!getNVar()){return false;}
		if (!checkEOL()) { return false; }

		Bundle FileEntry = FileTable.get(FileNumber);		// Get the bundle
		boolean isText = FileEntry.getBoolean("isText");
		if (isText){
			RunTimeError("File not opened for byte");
			return false;
		}

		double p = FileEntry.getInt("position");
		NumericVarValues.set(theValueIndex, p);

		return true;
	}

	// ************************************* File Operations **************************************

	private boolean checkSDCARD(char mount) {				// mount is 'w' for writable,
															// 'r' for either readable or writable 
		boolean okay = Basic.checkSDCARD(mount);
		if (!okay) { RunTimeError("SDCARD not available."); }
		return okay;
	}

	private boolean executeMKDIR(){
		if (!getStringArg()) { return false; }				// get the path
		String path = StringConstant;
		if (!checkEOL()) { return false; }

		File file = new File(Basic.getDataPath(path));
		file.mkdirs();
		if (!file.exists()){								// did we get a dir?
			return RunTimeError(path + " mkdir failed");
		}
		
		return true;
	}

	private boolean executeRENAME(){
		if (!getStringArg()) { return false; }				// get the old file name
		String Old = StringConstant;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the new file name
		String New = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('w')) { return false; }

		File oldFile = new File(Basic.getDataPath(Old));
		if (!oldFile.exists()) {							// does the file exist?
			return RunTimeError(Old + " directory/file not in this path");
		}
		File newFile = new File(Basic.getDataPath(New));

		if (!oldFile.renameTo(newFile)) {					// try to rename it
			return RunTimeError("Rename of " + Old + " to " + New + " failed");
		}
		return true;
	}

	private boolean executeDELETE(){

		if (!getNVar()) { return false; }					// get the var to put the result into
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the file name
		String fileName = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('w')) { return false; }

		File file = new File(Basic.getDataPath(fileName));
		double result = file.delete() ? 1 : 0;				// try to delete it
		NumericVarValues.set(SaveValueIndex, result);
		return true;
	}

	private boolean executeFILE() {							// Get File command keyword if it is there
		return executeCommand(file_cmd, "File");
	}

	private boolean executeFILE_EXISTS(){
		if (!getNVar()) { return false; }					// get the var to put the result into
		int saveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the file name
		String fileName = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		double exists = 0.0;								// "false"
		if (!fileName.equals("")) {							// empty file name would report parent dir exists; catch it and report false
			File file = new File(Basic.getDataPath(fileName));
			if (file.exists()) exists = 1.0;				// if file exists, report "true"
		}
		NumericVarValues.set(saveValueIndex, exists);
		return true;
	}

	private boolean isResourceFile(int resID) {
		boolean isFile = false;
		if (resID != 0) {
			InputStream inputStream= null;
			try {
				inputStream = getResources().openRawResource(resID);
				isFile = true;
			}
			catch (Exception ex) { }						// eat exception and return false
			finally { closeStream(inputStream, null); }
		}
		return isFile;
	}

	private String getAssetType(String assetPath) {			// return type "d" or "f", or null if not found
		AssetManager am = getAssets();
		try {
			String[] list = am.list(assetPath);
			if (list.length != 0) { return "d"; }			// it's a directory (no empty directories in assets)
		} catch (IOException e) { }

		try {												// only works for some file extensions
			AssetFileDescriptor afd = am.openFd(assetPath);
			try { afd.close(); } catch (IOException e) { }	// clean up
			return "f";										// it's a file
		} catch (IOException e) { Log.d(LOGTAG, "getAssetType:openFD:" + e); }

		try {												// last ditch, should always work
			InputStream is = am.open(assetPath);
			try { is.close(); } catch (IOException e) { }	// clean up
			return "f";										// it's a file
		} catch (IOException e) { Log.d(LOGTAG, "getAssetType:open:" + e); }

		return null;										// asset not found
	}

	private long getResourceSize(String fileName) {
		long size = -1;
		int resID = Basic.getRawResourceID(fileName);
		if (resID != 0) {
			InputStream inputStream= null;
			try {
				inputStream = getResources().openRawResource(resID);
				size = inputStream.available();
			}
			catch (Exception ex) { }						// eat exception and return -1
			finally { closeStream(inputStream, null); }
		}
		return size;
	}

	private long getAssetSize(String fileName) {			// get the size of a file in assets
		String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, fileName);
		AssetManager am = getAssets();

		try {
			String[] list = am.list(assetPath);
			if (list.length != 0) { return 0; }				// it's a directory (no empty directories in assets)
		} catch (IOException e) { }

		long size = AssetFileDescriptor.UNKNOWN_LENGTH;
		try {												// try the easy way:
			AssetFileDescriptor afd = am.openFd(assetPath);	// get afd
			size = afd.getLength();							// and ask it for the length
			try { afd.close(); } catch (IOException e) { }	// clean up
			if (size != AssetFileDescriptor.UNKNOWN_LENGTH) { return size; }
		} catch (IOException e) { Log.d(LOGTAG, "getAssetSize:openFD:" + e); }

		BufferedInputStream bis = null;
		try {												// no afd or length unknown
			InputStream is = am.open(assetPath);			// open the file
			size = is.available();							// and check available
			if (size != AssetFileDescriptor.UNKNOWN_LENGTH) { return size; }

			// Opened file but length is unknown.
			// Last ditch attempt: read the file and count its bytes
			byte[] bytes = new byte[8192];
			long bytesRead = 0;
			bis = new BufferedInputStream(is);
			for (int count = 0; count != -1; count = bis.read(bytes, 0, 8192)) {
				bytesRead += count;
			}
			size = bytesRead;
		} catch (IOException e) { Log.d(LOGTAG, "getAssetSize:open:" + e); }
		finally { closeStream(bis, null); }

		return ((size == AssetFileDescriptor.UNKNOWN_LENGTH) ? -1 : size);
	}

	private boolean executeFILE_SIZE(){
		if (!getNVar()) { return false; }					// get the var to put the size value into
		int SaveValueIndex = theValueIndex;
		long size = -1;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the file name
		String fileName = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		File file = new File(Basic.getDataPath(fileName));
		if (file.exists()) {
			size = file.length();							// the file exists
		} else {											// the file does not exist
			if (Basic.isAPK) {								// we are in APK
				size = getResourceSize(fileName);			// try to get it from raw resource
				if (size == -1) {							// resource not found
					size = getAssetSize(fileName);			// try to get it from assets
				}
			}
		}
		if (size == -1) {									// not file, resource, or asset
			return RunTimeError(fileName + " not found");
		}
		NumericVarValues.set(SaveValueIndex, (double)size);	// Put the file size into the var

		return true;
	}


	private boolean executeFILE_TYPE() {
		if (!getSVar()) { return false; }					// get the var to put the type info into
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the file name
		String fileName = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		String type = "x";										// assume does not exist
		File file = new File(Basic.getDataPath(fileName));
		if (file.exists()) {
			type = file.isDirectory() ? "d" :
				   file.isFile()      ? "f" : "o";
		} else {												// does not exist in file system
			if (Basic.isAPK) {									// we are in APK
				int resID = Basic.getRawResourceID(fileName);
				if (resID != 0) {
					type = isResourceFile(resID) ? "f" : "o";	// file or other, can't be a directory
				} else {
					String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, fileName);
					String aType = getAssetType(assetPath);		// file or directory, can't be other
					if (aType != null) { type = aType; }		// else "x", does not exist
				}
			}													// else "x", does not exist
		}
		StringVarValues.set(SaveValueIndex, type);			// put the file type into the var

		return true;
	}

	private boolean executeFILE_ROOTS(){
		if (!getSVar()) { return false; }
		int SaveVarIndex = theValueIndex;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		String path = Basic.getDataPath(null);		// get canonical path to default data directory
		StringVarValues.set(SaveVarIndex, path);
		return true;
	}

	private boolean executeDIR(){
		if (!getStringArg()) { return false; }						// get the path
		String filePath = StringConstant;

		if (!isNext(',')) { return false; }							// parse the ,D$[]
		String var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
		if (var == null) { return false; }							// must name a new string array variable
		String dirMark = "(d)";
		if (isNext(',')) {											// optional directory marker
			if (!getStringArg()) { return false; }
			dirMark = StringConstant;
		}
		if (!checkEOL()) { return false; }							// line must end with ']'

		File lbDir = new File(Basic.getDataPath(filePath));
		if (!lbDir.exists()) {										// error if directory does not exist
			return RunTimeError(filePath + " is invalid path");
		}

		ArrayList <String> files = new ArrayList<String>();
		ArrayList <String> dirs = new ArrayList<String>();

		String FL[] = lbDir.list();									// get the list of files in the dir
		if (FL == null) {											// if not a dir
			dirs.add(" ");											// make list with one element
		} else {
											// Go through the file list and mark directory entries with dirMark
			String absPath = lbDir.getAbsolutePath() + '/';
			for (String s : FL) {
				File test = new File(absPath + s);
				if (test.isDirectory()) {							// If file is a directory
					dirs.add(s + dirMark);							// mark it and add it to display list
				} else {
					files.add(s);									// else add name without the directory mark
				}
			}
			Collections.sort(dirs);									// Sort the directory list
			Collections.sort(files);								// Sort the file list
			dirs.addAll(files);										// copy the file list to end of dir list
		}
		int length = dirs.size();									// number of directories and files in list
		if (length == 0) { length = 1; }							// make at least one element if dir is empty
																	// it will be an empty string
		return ListToBasicStringArray(var, dirs, length);			// Copy the list to a BASIC! array
	}

	private boolean executeGRABFILE(){
		if (!getSVar()) { return false; }							// First parm is string var
		int saveVarIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }						// Second parm is the filename
		String theFileName = StringConstant;

		boolean textFlag = false;									// Default: assume ASCII/binary
		if (isNext(',')) {											// Optional third parm
			if (!evalNumericExpression()) { return false; }
			textFlag = (EvalNumericExpressionValue != 0.0);			// is the text flag: unicode if non-zero
		}
		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		BufferedInputStream bis = null;
		String result = "";
		IOException ioex = null;
		Exception ex = null;

		try {
			bis = Basic.getBufferedInputStream(Basic.DATA_DIR, theFileName);
			result = grabStream(bis, textFlag);
		}
		catch (IOException ie) { ioex = ie; }
		catch (Exception e) { ex = e; }
		finally {
			ioex = closeStream(bis, ioex);
			if (ioex != null) { return RunTimeError(ioex); }		// Report first exception, if any, and if no previous RTE set
			if (ex != null) { return RunTimeError(ex); }
		}
		StringVarValues.set(saveVarIndex, result);
		return true;
	}

	private boolean executeGRABURL(){
		if (!getSVar()) { return false; }							// First parm is string var
		int saveVarIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }						// Second parm is the url

		int timeoutMillis = 0;										// Default: assume infinite timeout
		if (isNext(',')) {											// Optional third parm
			if (!evalNumericExpression()) { return false; }
			timeoutMillis = EvalNumericExpressionValue.intValue();	// is the timeout: infinite if 0
			if (timeoutMillis < 0) { timeoutMillis *= -1; }			// negative value would throw an exception
		}
		if (!checkEOL()) { return false; }

		BufferedInputStream bis = null;
		String result = null;

		URL url = null;
		try {
			// This assumes that you have a URL from which the response will come
			url = new URL(StringConstant);
			// Open a connection to the URL and obtain a buffered input stream
			URLConnection connection = url.openConnection();
			if (timeoutMillis != 0) {
				connection.setConnectTimeout(timeoutMillis);
				connection.setReadTimeout(timeoutMillis);
			}
			InputStream inputStream = connection.getInputStream();
			bis = new BufferedInputStream(inputStream);
			result = grabStream(bis, true);							// Read as encoded text stream, not byte stream
// Alternate implementation: uncomment this catch block to handle Timeout explicitly.
//		} catch (SocketTimeoutException ste) {						// Connect or Read timeout
//			result = "";											// Empty result, finally will close stream but will not return
		} catch (Exception e) {										// Report exception in run time error
			return RunTimeError(e);									// finally will close stream before return happens
		} finally {
			IOException ex = closeStream(bis, null);				// Close stream if not already closed
			if (ex != null) { return RunTimeError(ex); }			// Report first exception, if any, and if no previous RTE set
		}
		StringVarValues.set(saveVarIndex, result);
		return true;
	}

	private String grabStream(BufferedInputStream bis, boolean textFlag) throws IOException {
		ByteArrayBuffer byteArray = new ByteArrayBuffer(1024*8);
		int current = 0;
		// Read from the stream into a byte array
		while ((current = bis.read()) != -1) {
			byteArray.append((byte) current);
		}

		// Construct a String object from the byte array containing the response
		if (textFlag) {
			return new String(byteArray.toByteArray());			// Text: keep full two-byte encoding
		} else {
			return new String(byteArray.toByteArray(), 0);		// ASCII or binary: force upper byte 0
		}
	}

	// ************************************** Time and TimeZone commands **************************
	
	private boolean executeTIME(){								// Get the date and time
		Time time = theTimeZone.equals("") ? new Time() : new Time(theTimeZone); // If user has set a time zone, use it
		if (evalNumericExpression()) {							// If there is a numeric argument it is a time in ms
			if (!isNext(',')) { return checkEOL(); }			// Done if no other arguments
			time.set(EvalNumericExpressionValue.longValue());	// Use the time argument
		} else {
			time.setToNow();									// No arg, or first arg is not numeric: time is now
		}
		String theTime[] = time.format("%Y:%m:%d:%H:%M:%S").split(":");
		int i = 0;
		do {													// String vars for time components
																// Commas hold places for up to six svars.
			if (getSVar()) { StringVarValues.set(theValueIndex, theTime[i]); } // If svar, use it; if nothing, skip to next comma.
		} while ((++i < 6) && isNext(','));						// Anything else will get caught by checkEOL
		if (isNext(',') && getNVar()) {							// Another comma holds a place for an optional nvar
			double weekDay = time.weekDay + 1;					// for day of week: 1 is Sunday
			NumericVarValues.set(theValueIndex, weekDay);
		}
		if (isNext(',') && getNVar()) {							// Another comma holds a place for an optional nvar
																// For Daylight Saving Time flag
			NumericVarValues.set(theValueIndex, Math.signum((double) time.isDst)); // 1 yes, 0 no, -1 unknown
		}
		return checkEOL();
	}

	private boolean executeTIMEZONE(){										// Get TimeZone command keyword if it is there
		return executeCommand(TimeZone_cmd, "TimeZone");
	}

	private boolean executeTIMEZONE_SET() {									// Set a global Time Zone string for TIME and TIME(
		String zone = Time.getCurrentTimezone();							// default to local time zone
		if (getStringArg()) {
			TimeZone tz = TimeZone.getTimeZone(StringConstant);				// if arg, use it as TimeZone ID
			zone = tz.getID();												// read back ID, "GMT" if user-string invalid
		}
		if (!checkEOL()) { return false; }
		theTimeZone = zone;
		return true;
	}

	private boolean executeTIMEZONE_GET() {									// Get the time zone setting
		if (!(getSVar() && checkEOL())) { return false; }
		String zone = theTimeZone;
		if (zone.equals("")) {
			zone = Time.getCurrentTimezone();								// If user never set a time zone, use local
		}
		StringVarValues.set(theValueIndex, zone);
		return true;
	}

	private boolean executeTIMEZONE_LIST() {								// Get a list of all valid time zone strings
		if (!(getNVar() && checkEOL())) { return false; }

		int theIndex = createNewList(list_is_string);						// Create a new empty string list
		if (theIndex < 0) { return false; }									// Create failed
		NumericVarValues.set(theValueIndex, (double) theIndex);				// Return the list pointer

		ArrayList<String> theList = theLists.get(theIndex);
		for (String zone : TimeZone.getAvailableIDs()) {					// Get all the zones the system knows 
			theList.add(zone);												// Put them in the list
		}
		return true;
	}

	// ************************************** Miscellaneous Non-core commands **************************

	private boolean executePAUSE(){
		if (!evalNumericExpression()) return false;							// Get pause duration value
		if (!checkEOL()) return false;
		long dur = EvalNumericExpressionValue.longValue();
		if (dur < 1) {
			return RunTimeError("Pause must be greater than zero");
		}

		try { Thread.sleep(dur); } catch (InterruptedException e) {}
		return true;
	}

	private boolean executeBROWSE(){

		if (!getStringArg()) return false;
		if (!checkEOL()) return false;
		String url = StringConstant;

		Intent i = new Intent(Intent.ACTION_VIEW);			// Intent to launch browser
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setData(Uri.parse(url));

		// try { Thread.sleep(500); }						// Sleep here stopped forced stop exceptions
		// catch (InterruptedException e) { }

		try { startActivity(i); }							// Launch browser at url
		catch ( Exception  e) { return RunTimeError(e); }

		return true;
	}

	private boolean executeINKEY() {

		if (!getSVar()) return false;						// get the var to put the key value into
		if (!checkEOL()) return false;
		if (InChar.size() > 0){
			StringVarValues.set(theValueIndex, InChar.get(0));
			InChar.remove(0);
		} else {
			StringVarValues.set(theValueIndex, "@");
		}
		return true;
	}

	private boolean executeKEY_RESUME() {
		if (interruptResume == -1 ) {
			return RunTimeError("No Current Key Interrupt");
		}
		return doResume();
	}

	private boolean executePOPUP(){
		if (!getStringArg()) return false;						// get the message
		ToastMsg = StringConstant;

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;				// get x
		ToastX = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;				// get y
		ToastY = EvalNumericExpressionValue.intValue();

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;				// get duration
		ToastDuration = (EvalNumericExpressionValue == 0.0)
					  ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
		if (!checkEOL()) return false;

		PrintShow("@@4");										// tell the UI Task to pop the toast
		return true;
	}

	public boolean executeCLS(){							// Clear Screen
		if (!checkEOL()) return false;
		PrintShow("@@5");										// signal UI task
		return true;
	}

	public boolean executeSELECT(){

		if (!getNVar()) return false;								// get the var to put the key value into
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		ArrayList<String> selectList;
		int saveLineIndex = LineIndex;
		if ((getVarAndType() != null) && VarIsArray) {
			if (!isNext(']'))	{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); } // Array must not have any indices
			if (VarIsNumeric)	{ return RunTimeError(EXPECT_STRING_ARRAY); }
			if (VarIsNew)		{ return RunTimeError(EXPECT_DIM_ARRAY); }

			Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber)); // Get the array table bundle for this array
			int length = ArrayEntry.getInt("length");				// get the array length
			int base = ArrayEntry.getInt("base");					// and the start of values in the value space

			selectList = new ArrayList<String>();					// Create a list to copy array values into

			for (int i = 0; i < length; ++i) { 						// Copy the array values into that list
				selectList.add(StringVarValues.get(base+i));
			}
		} else {
			LineIndex = saveLineIndex;
			if (!evalNumericExpression()) return false;

			int listIndex = EvalNumericExpressionValue.intValue();
			if (listIndex < 1 || listIndex >= theLists.size()){
				return RunTimeError("Invalid List Pointer");
			}
			if (theListsType.get(listIndex) != list_is_string){
				return RunTimeError("Not a string list");
			}
			selectList = theLists.get(listIndex);
		}

		String title = null;										// set defaults
		String msg = null;
		int isLongClickValueIndex = -1;

		if (isNext(',')) {											// comma indicates optional arguments
			boolean isComma = true;
			if (!isNext(',') && !isEOL() && getStringArg()) {
				title = msg = StringConstant;						// user provided a title argument
				isComma = isNext(',');
			}
			if (isComma) {
				if (isNext(',')) {
					msg = "";										// user suppressed message
				} else if (isEOL()) {
					isComma = false;
				} else if (getStringArg()) {
					msg = StringConstant;							// user provided a message argument
					isComma = isNext(',');
				}
			}
			if (isComma) {
				if (!getNVar()) return false;						// get the long press var
				isLongClickValueIndex = theValueIndex;
			}
		}
		if (!checkEOL()) return false;

		SelectedItem = 0;											// intialize return values
		ItemSelected = false;
		SelectLongClick = false;

		Intent intent = new Intent(this, Select.class);
		if (title != null) { intent.putExtra(Select.EXTRA_TITLE, title); }
		if (msg != null)   { intent.putExtra(Select.EXTRA_MSG, msg); }
		intent.putStringArrayListExtra(Select.EXTRA_LIST, selectList);
		startActivityForResult(intent, BASIC_GENERAL_INTENT);

		synchronized (LOCK) {
			while (!ItemSelected) {
				try { LOCK.wait(); }								// Wait for signal from Selected.java thread
				catch (InterruptedException e) { ItemSelected = true; }
			}
		}
		NumericVarValues.set(SaveValueIndex, (double) SelectedItem); // Put the item selected into the var

		if (isLongClickValueIndex != -1) {
			double isLongPress = SelectLongClick ? 1 : 0;			// Get the actual value
			NumericVarValues.set(isLongClickValueIndex, isLongPress); // Set the return value
		}

		return true;
	}

	private boolean executeSPLIT(int limit){

		String var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
		if (var == null) { return false; }							// must name a new string array variable

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }						// Get the string to split
		String SearchString = StringConstant;

		String r[] = doSplit(SearchString, limit);					// Get regex arg, if any, and split the string.
		if (!checkEOL()) { return false; }

		int length = r.length;										// Get the number of strings generated
		if (length == 0) { return false; }							// error in doSplit()

		return ListToBasicStringArray(var, Arrays.asList(r), length);
	}

	private String[] doSplit(String SearchString, int limit) {		// Split a string
		// If limit < 0, keep all fields. If limit is 0, trim trailing blank fields.
		// If limit > 0, keep only up to limit fields.
		String r[] = new String[0];									// If error, return zero-length string
		String REString = null;
		if (isNext(',')) {											// If user command supplied a regex
			if (!getStringArg()) { return r; }						// get it
			REString = StringConstant;
		} else {
			REString = "\\s+";										// Otherwise split on whitespace
		}
		try {
			r = SearchString.split(REString, limit);
			if (r.length == 0) {									// Special case: REString same as SearchString
				r = new String[1];									// Return non-empty array
				r[0] = "";											// with one empty String
			}
		} catch (PatternSyntaxException pse) {
			RunTimeError(REString + " is invalid argument at");
		}
		return r;
	}

	private boolean executeKB_TOGGLE() {
		if (!checkEOL()) return false;
		Log.v(LOGTAG, CLASSTAG + " KB_TOGGLE " + kbShown );

		if (kbShown) return true;

		  if (GRFront) {
//			  GR.GraphicsImm.toggleSoftInputFromWindow(GR.drawView.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//			  GR.GraphicsImm.showSoftInput(GR.drawView, InputMethodManager.SHOW_FORCED);
		      GR.GraphicsImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		      kbShown = true;
		  }
		  else {
//			  IMM.toggleSoftInputFromWindow(lv.getWindowToken(), InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
//			  IMM.showSoftInput(lv, 0);
			  IMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
			  kbShown = true;
		  }
//		  IMM.showSoftInputFromInputMethod(lv.getWindowToken(), InputMethodManager.SHOW_FORCED);
//		  IMM.showSoftInputFromInputMethod (lv.getWindowToken(), IMM.SHOW_FORCED);
		return true;
	}

	private boolean executeKB_HIDE() {
		if (!checkEOL()) return false;
		Log.v(LOGTAG, CLASSTAG + " KBHIDE " + kbShown);

		  if (GRFront) {
//			  GR.GraphicsImm.toggleSoftInputFromWindow(GR.drawView.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			  GR.GraphicsImm.hideSoftInputFromWindow(GR.drawView.getWindowToken(), 0);
		      kbShown = false;
		      }
		  else {
//			  IMM.toggleSoftInputFromWindow(lv.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			  IMM.hideSoftInputFromWindow(lv.getWindowToken(), 0);
			  kbShown = false;
		  }
		return true;
	}

	private boolean executeWAKELOCK(){
		if (!evalNumericExpression()) return false;					// Get setting
		int code  = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		if (theWakeLock != null) {
			theWakeLock.release();
			theWakeLock = null;
		}

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		String tag = "BASIC!";
		switch (code) {
			case partial:
				theWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
				theWakeLock.acquire();
				break;
			case dim:
				theWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, tag);
				theWakeLock.acquire();
				break;
			case bright:
				theWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, tag);
				theWakeLock.acquire();
				break;
			case full:
				theWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, tag);
				theWakeLock.acquire();
				break;
			case release:
				break;
			default:
				return RunTimeError("WakeLock code not 1 - 5");
		}

		return true;
	}

	@SuppressLint("InlinedApi")										// Uses a value from API 12
	private boolean executeWIFILOCK(){
		if (!evalNumericExpression()) return false;					// Get setting
		int code  = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		if (theWifiLock != null) {
			theWifiLock.release();
			theWifiLock = null;
		}

		WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
		String tag = "BASIC!";
		switch (code) {
			case wifi_mode_high:
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {	// >= 12
					theWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF, tag);
					theWifiLock.acquire();
					break;
				}							// Lower API versions fall through to MODE_FULL
			case wifi_mode_full:
				theWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_FULL, tag);
				theWifiLock.acquire();
				break;
			case wifi_mode_scan:
				theWifiLock = wm.createWifiLock(WifiManager.WIFI_MODE_SCAN_ONLY, tag);
				theWifiLock.acquire();
				break;
			case wifi_release:
				break;
			default:
				return RunTimeError("WifiLock code not 1 - 4");
		}

		return true;
	}

	  private boolean executeTONE(){
		  
		    double duration = 1; 				// seconds
		    double freqOfTone = 1000; 			// hz
		    int sampleRate = 8000;				// a number

		  if (!evalNumericExpression()) return false;			// Get frequency
		  freqOfTone =  EvalNumericExpressionValue;
		  
		  if (!isNext(',')) return false;

		  if (!evalNumericExpression()) return false;			// Get duration
		  duration= EvalNumericExpressionValue/1000 ;
	
	    	double dnumSamples = duration * sampleRate;
	    	dnumSamples = Math.ceil(dnumSamples);
	    	int numSamples = (int) dnumSamples;
	    	double sample[] = new double[numSamples];
	    	ByteBuffer generatedSnd = ByteBuffer.allocate(2 * numSamples);
	    	generatedSnd.order(ByteOrder.LITTLE_ENDIAN);
	    	ShortBuffer shortView = generatedSnd.asShortBuffer();
	    	
	    	boolean flagMinBuff = true;							// Optionally skip checking min buffer size
	    	if (isNext(',')) {
	    		if (!evalNumericExpression()) return false;
	    		if (EvalNumericExpressionValue == 0 )
	    			flagMinBuff = false;
	    	}
	    	
	    	if (flagMinBuff) {
	    	
	    		int minBuffer = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO,
	    				AudioFormat.ENCODING_PCM_16BIT);
	    		if (2 * numSamples< minBuffer){
	    			double minDuration = Math.ceil(1000 * (double)minBuffer/(2 * (double)sampleRate));
	    			RunTimeError("Minimum tone duration for this device: " + (int) minDuration + " milliseconds");
	    			return false;
	    		}
	    	}

	    	if (!checkEOL()) return false;				// No more parameters expected
	    	
	        for (int i = 0; i < numSamples; ++i) {		// Fill the sample array
	        	sample[i] = Math.sin(freqOfTone * 2 * Math.PI * i / (sampleRate));
	        }

	        // convert to 16 bit pcm sound array
	        // assumes the sample buffer is normalised.
	        int i = 0 ;
	        
	        int ramp = numSamples / 20 ;									// Amplitude ramp as a percent of sample count
	       
	        
	        for (i = 0; i< ramp; ++i) {										// Ramp amplitude up to max (to avoid clicks)
	            short val = (short) (sample[i] * 32767 * i/ramp);
	            shortView.put(val);
	        }

	        
	        for ( ; i< numSamples - ramp; ++i) {							// Max amplitude for most of the samples
	            short val = (short) (sample[i] * 32767);					// scale to maximum amplitude
	            shortView.put(val);
	        }
	        
	        for ( ; i< numSamples; ++i) {									// Ramp amplitude down to 0
	            short val = (short) (sample[i] * 32767 * (numSamples-i)/ramp);
	            shortView.put(val);
	        }
	        
	        AudioTrack audioTrack = null;									// Get audio track
	        try {
	        	audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
	        			sampleRate, AudioFormat.CHANNEL_OUT_MONO,
	        			AudioFormat.ENCODING_PCM_16BIT, numSamples*2,
	        			AudioTrack.MODE_STATIC);
	        	audioTrack.write(generatedSnd.array(), 0, numSamples*2);			// Load the track
	        	audioTrack.play();											// Play the track
	        }
	        catch (Exception e){
	        	return RunTimeError(e);
	        }
	        
	        int x =0;
	        do{														// Montior playback to find when done
	        	 if (audioTrack != null) 
	        		 x = audioTrack.getPlaybackHeadPosition(); 
	        	 else 
	        		 x = numSamples;	        
	        }while (x<numSamples);
	        
	        if (audioTrack != null) audioTrack.release();			// Track play done. Release track.
	        
	        audioTrack = null;										// Release storage
	        shortView = null;
	        generatedSnd = null;
	        sample = null;
	        System.gc();
	        

	        return true;
	  }

	private boolean executeVIBRATE(){

		if (getArrayVarForRead() == null) return false;				// Get the array variable
		if (!VarIsNumeric) { return RunTimeError(EXPECT_NUM_ARRAY); } // Insure that it is a numeric array
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;							// Get values inside [], if any

		if (!isNext(',')) return false;								// Get the repeat value
		if(!evalNumericExpression()) return false;
		int repeat = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		long Pattern[] = new long[length];							// Pattern array
		for (int i = 0; i < length; ++i) {							// Copy user array into pattern
			Pattern[i] = NumericVarValues.get(base + i).longValue();
		}

		if (repeat > 0) myVib.cancel();
		else myVib.vibrate(Pattern, repeat);						// Do the vibrate

		return true;
	}

	private boolean executeDEVICE() {

		if (!getVar() || !checkEOL()) return false;
		if (VarIsNumeric) {											// bundle format
			Bundle b = new Bundle();
			int bundleIndex = theBundles.size();
			theBundles.add(b);
			NumericVarValues.set(theValueIndex, (double)bundleIndex);

			bundlePut(b, "Brand", Build.BRAND);						// same info as executeDEVICE()
			bundlePut(b, "Model", Build.MODEL);
			bundlePut(b, "Device", Build.DEVICE);
			bundlePut(b, "Product", Build.PRODUCT);
			bundlePut(b, "OS", Build.VERSION.RELEASE);
																	// new stuff for Bundle format
			bundlePut(b, "Language", Locale.getDefault().getDisplayLanguage());
			bundlePut(b, "Locale", Locale.getDefault().toString());
			bundlePut(b, "PhoneNumber", getMyPhoneNumber());
		} else {
			String info =											// legacy string format
				"Brand = " + Build.BRAND + "\n" +
				"Model = " + Build.MODEL + "\n" +
				"Device = " + Build.DEVICE + "\n" +
				"Product = " + Build.PRODUCT + "\n" +
				"OS = " + Build.VERSION.RELEASE;
			StringVarValues.set(theValueIndex, info);
		}
		return true;
	}
											
	private boolean executeHTTP_POST() {
		if (!getStringArg()) return false;
		String url = StringConstant;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;
		int theListIndex = EvalNumericExpressionValue.intValue();
		if (theListIndex < 1 || theListIndex>= theLists.size()) {
			return RunTimeError("Invalid list pointer");
		}
		if (theListsType.get(theListIndex) == list_is_numeric) {
			return RunTimeError("List must be of string type.");
		}
	
		List<String> thisList = theLists.get(theListIndex);
		int r = thisList.size() % 2;
		if (r !=0 ) {
			return RunTimeError("List must have even number of elements");
		}
	
	       List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	       for (int i = 0; i <thisList.size(); ++i){
	    	   nameValuePairs.add(new BasicNameValuePair(thisList.get(i), thisList.get(++i)));
	       }
	       

		   String Result = "";
		   HttpClient client = new DefaultHttpClient();
		   HttpPost post = new HttpPost(url);
		   try {
			   post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//			   HttpResponse response = client.execute(post);

		        ResponseHandler<String> responseHandler=new BasicResponseHandler();
		         Result = client.execute(post, responseHandler);

			} catch (Exception e) {
				return RunTimeError("! " + e);
			}

		if (!isNext(',')) return false;
		if (!getSVar()) return false;
		if (!checkEOL()) return false;
		StringVarValues.set(theValueIndex, Result);

		return true;
	}

	// ************************************************ SQL Package ***************************************

	private boolean executeSQL(){									// Get SQL command keyword if it is there
		return executeCommand(SQL_cmd, "SQL");
	}

	private boolean getDbPtrArg() {									// first arg of command is DB Pointer Variable
		String errStr = "Database not opened at:";
		if (DataBases.isEmpty()) {
			RunTimeError(errStr);
			return false;
		}
		if (!getNVar()) { return false; }							// the DB table pointer
		int i = NumericVarValues.get(theValueIndex).intValue();
		if (i == 0) {												// If pointer is zero
			RunTimeError(errStr);									// DB has been closed
			return false;
		}
		if (i < 0 || i > DataBases.size()) {
			RunTimeError("Invalid Database Pointer at:");
			return false;
		}
		return true;
	}

	private boolean getVarAndDbPtrArgs(int[] args) {				// first arg of command is a numeric variable
																	// and second is a DB table pointer
		String errStr = "Database not opened at:";
		if (DataBases.isEmpty()){
			RunTimeError(errStr);
			return false;
		}
		if (!getNVar()) { return false; }							// user's nvar
		args[0] = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getNVar()) { return false; }							// the DB table pointer
		args[1] = theValueIndex;

		int i = NumericVarValues.get(theValueIndex).intValue();
		if (i == 0) {												// If pointer is zero
			RunTimeError(errStr);									// DB has been closed
			return false;
		}
		if (i < 0 || i > DataBases.size()) {
			RunTimeError("Invalid Database Pointer at:");
			return false;
		}
		return true;
	}

	private boolean getVarAndCursorPtrArgs(int[] args) {			// first arg of command is a numeric variable
																	// and second is a DB cursor pointer
		if (Cursors.isEmpty()) {									// Make sure there is at least one cursor
			RunTimeError("Cursor not available at:");
			return false;
		}
		if (!getNVar()) { return false; }							// user's nvar
		args[0] = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getNVar()) { return false; }							// the DB cursor pointer
		args[1] = theValueIndex;

		int i = NumericVarValues.get(theValueIndex).intValue();
		if (i == 0) {												// If pointer is zero
			RunTimeError("Cursor done at:");						// then cursor is used up
			return false;
		}
		if (i < 0 || i > Cursors.size()) {
			RunTimeError("Invalid Cursor Pointer at:");
			return false;
		}
		return true;
	}

	private boolean getColumnValuePairs(ContentValues values) {		// Get column/value pairs from user command
		if (!isNext(',')) { return false; }
		do {
			if (!getStringArg()) { return false; }					// Column
			String Column = StringConstant;
			if (!isNext(',') || !getStringArg()) { return false; }	// Value
			String Value = StringConstant;
			values.put(Column, Value);								// Store the pair
		} while (isNext(','));
		return true;
	}

	private boolean execute_sql_open(){

		if (!getNVar()) return false;								// DB Pointer Variable
		int SaveValueIndex = theValueIndex;							// for the DB table pointer

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;							// Get Data Base Name
		String DBname = StringConstant;
		if (!checkEOL()) return false;

		String fn;
		if (DBname.startsWith(":")) {
			fn = DBname;
		} else {
			if (!checkSDCARD('w')) return false;
			fn = Basic.getDataBasePath(DBname);
		}

		SQLiteDatabase db;
		try{														// Do the open or create
			db = SQLiteDatabase.openOrCreateDatabase(new File(fn), null );
		} catch  (Exception e) {
			return RunTimeError("SQL Exception: "+e);
		}

		// The newly opened data base is added to the DataBases list.
		// The list index of the new data base is added returned to the user

		NumericVarValues.set(SaveValueIndex, (double) DataBases.size()+1);
		DataBases.add(db);
		return true;
	}

		private boolean execute_sql_close(){
		
			if (!getDbPtrArg()) return false;						// get variable for the DB table pointer
			int i = NumericVarValues.get(theValueIndex).intValue();
			if (!checkEOL()) return false;
			
			SQLiteDatabase db = DataBases.get(i-1);					// get the data base
		   try {
			   db.close();											// Try closing it
		   }catch (Exception e) {
			   return RunTimeError(e);
		   }
		   
		   NumericVarValues.set(theValueIndex, 0.0);				// Set the pointer to 0 to indicate closed.
		   
    	   return true;
    		  
    	  }

		private boolean execute_sql_insert(){
		
			if (!getDbPtrArg()) return false;						// get variable for the DB table pointer
			int i = NumericVarValues.get(theValueIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);					// get the data base

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;						// Table Name
			String TableName = StringConstant;

			ContentValues values = new ContentValues();
			if (!getColumnValuePairs(values)) return false;			// Get column/value pairs from user command

			if (!checkEOL()) return false;

		   try {													// Now insert the pairs into the named table
	        db.insertOrThrow(TableName, null, values);
		   }catch (Exception e) {
			   return RunTimeError(e);
		   }

		   return true;
   	   }

		private boolean execute_sql_query(){
		
			int[] args = new int[2];							// Get the first two args:
			if (!getVarAndDbPtrArgs(args)) return false;
			int SaveValueIndex = args[0];						// Query Cursor Variable
			int DbTablePointerIndex = args[1];					// DB table pointer

			int i = NumericVarValues.get(DbTablePointerIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);				// get the data base

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;					// Table Name
			String TableName = StringConstant;
			
			if (!isNext(',')) return false;
			if (!getStringArg()) return false;					// String of comma separated columns to get
			String RawColumns = StringConstant;
		   
		   ArrayList<String> cList = new ArrayList<String>();	// Must convert string to an array of columns
		   														// starting by creating an Array List of column names

		   String cTemp = "";									// Parse the Raw Columns
		   for (int j=0; j<RawColumns.length(); ++j){
			   char t = RawColumns.charAt(j);
			   if (t!=','){                                     // while tossing out blanks
				   if ( t != ' ') 
				   {cTemp = cTemp + t;}
				   else{}	// add characters to the column name
			   }else{
				   cList.add(cTemp);							// comma terminates a column name, add name to array list
				   cTemp = "";									// and start a new column name
			   }
		   }
		   cList.add(cTemp);									// add last column to the list
		   
		   String []Q_Columns = new String[cList.size()];		// Finally, convert the array list
		   cList.toArray(Q_Columns);		   					// to a String Array.
		   
		   String Where = "";									// if no Where given, set empty
		   String Order = "";									// if no Order given, set empty
		   
			if (isNext(',')) {									// if no comma, then no optional Where
				if (!getStringArg()) return false;				// Where Value
				Where = StringConstant;

				if (isNext(',')) {								// if no comma, then no optional Order
					if (!getStringArg()) return false;			// Oder Value
					Order = StringConstant;
				}
			}
			if (!checkEOL()) return false;

		   Cursor cursor;
		   try{													// Do the query and get the cursor
	           cursor = db.query(TableName, Q_Columns, Where, null, null,
	              null, Order);
		   } catch (Exception e) {
			   return RunTimeError(e);
		   }
		   
		   NumericVarValues.set(SaveValueIndex, (double) Cursors.size()+1); // Save the Cursor index into the var
		   Cursors.add(cursor);												// and save the cursor.
		   
		   return true;
       }

		private boolean execute_sql_next(){

			int[] args = new int[2];							// Get the first two args:
			if (!getVarAndCursorPtrArgs(args)) return false;
			int SaveDoneIndex = args[0];						// Done Flag variable
			int SaveCursorIndex = args[1];						// DB Cursor pointer

			NumericVarValues.set(SaveDoneIndex, 0.00);			// set Not Done
			int i = NumericVarValues.get(SaveCursorIndex).intValue();
			Cursor cursor = Cursors.get(i-1);					// get the cursor

		   String result;
		   if (cursor.moveToNext()) {							// if there is another row, go there
			   for (int index = 0; isNext(','); ++index) {
				   if (!getSVar()) return false;				// Get next result variable
				   try {
						result = cursor.getString(index); 		// get the result
				   } catch (Exception e) {
						return RunTimeError(e);
				   }
				   if (result == null) { result = ""; }
				   StringVarValues.set(theValueIndex, result);	// set result into var
			   }
			   return checkEOL();

		   } else {												// no next row, cursor is used up
			   cursor.close();
			   NumericVarValues.set(SaveDoneIndex, 1.00);
			   NumericVarValues.set(SaveCursorIndex, 0.0);

			   return true;
		   }
       }

	private boolean execute_sql_query_length(){					// Report the number of rows in a query result
		int[] args = new int[2];								// Get the first two args:
		if (!getVarAndCursorPtrArgs(args)) return false;
		int ValueIndex = args[0];								// variable for number of rows
		int CursorIndex = args[1];								// DB Cursor pointer
		if (!checkEOL()) return false;

		int i = NumericVarValues.get(CursorIndex).intValue();
		Cursor cursor = Cursors.get(i - 1);						// get the cursor
		double nRows = cursor.getCount();
		NumericVarValues.set(ValueIndex, nRows);				// return number of rows to user
		return true;
	}

	private boolean execute_sql_query_position(){				// Report current position in query results
		int[] args = new int[2];								// Get the first two args:
		if (!getVarAndCursorPtrArgs(args)) return false;
		int ValueIndex = args[0];								// variable for position
		int CursorIndex = args[1];								// DB Cursor pointer
		if (!checkEOL()) return false;

		int i = NumericVarValues.get(CursorIndex).intValue();
		Cursor cursor = Cursors.get(i - 1);						// get the cursor
		double position = cursor.getPosition();
		NumericVarValues.set(ValueIndex, position + 1);			// return position to user, 1-based
		return true;
	}

	private boolean execute_sql_delete(){

		if (!getDbPtrArg()) return false;						// get variable for the DB table pointer
		int i = NumericVarValues.get(theValueIndex).intValue();
		SQLiteDatabase db = DataBases.get(i-1);					// get the data base

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;						// Table Name
		String TableName = StringConstant;

		int returnValueIndex = 0;
		String Where = null;									// if no Where given, set null
		if (isNext(',')) {										// if no comma, then no optional Where
			if (!getStringArg()) return false;					// Where Value
			Where = StringConstant;
			if (isNext(',')) {									// if there's a where
				if (!getNVar()) return false;					// there can be a return value
				returnValueIndex = theValueIndex;
			}
		}
		if (!checkEOL()) return false;

		int count = 0;
		try {
			count = db.delete(TableName, Where, null);			// do the deletes
		} catch (Exception e) {
			return RunTimeError(e);
		}

		if (returnValueIndex != 0) {
			NumericVarValues.set(returnValueIndex, (double)count);
		}
		return true;
	}

		private boolean execute_sql_update() {

			if (!getDbPtrArg()) return false;					// get variable for the DB table pointer
			int i = NumericVarValues.get(theValueIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);				// get the data base

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;					// Table Name
			String TableName = StringConstant;

			ContentValues values = new ContentValues();
			if (!getColumnValuePairs(values)) return false;		// Get column/value pairs from user command

			String Where = null;								// Where is optional
			if (isNext(':')) {									// Colon indicates Where follows
				if (!getStringArg()) return false;				// Where Value
				Where = StringConstant;
			}
			if (!checkEOL()) return false;

		   try {
	        db.update(TableName, values, Where, null);
		   }catch (Exception e) {
				return RunTimeError(e);
		   }

    	   return true;
       }

		private boolean execute_sql_exec(){
			if (!getDbPtrArg()) return false;					// get variable for the DB table pointer
			int i = NumericVarValues.get(theValueIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);				// get the data base

			if (!isNext(',')) return false;
			if (!evalStringExpression()) return false;			// Command string
			String CommandString = StringConstant;
			if (!checkEOL()) return false;

		   try {
		        db.execSQL(CommandString);
			   } catch (Exception e){
				   RunTimeError("SQL Exception: " + e);
				   return false;
			   }

    	   return true;
       }

		private boolean execute_sql_raw_query(){

			int[] args = new int[2];							// Get the first two args:
			if (!getVarAndDbPtrArgs(args)) return false;
			int SaveValueIndex = args[0];						// Query Cursor Variable
			int DbTablePointerIndex = args[1];					// DB table pointer

			int i = NumericVarValues.get(DbTablePointerIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);				// get the data base

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;					// Command string
			String QueryString = StringConstant;
			if (!checkEOL()) return false;

		   Cursor cursor;
		   try{													// Do the query and get the cursor
	           cursor = db.rawQuery(QueryString, null);
		   }catch (Exception e) {
				return RunTimeError(e);
		   }
		   
		   NumericVarValues.set(SaveValueIndex, (double) Cursors.size()+1); // Save the Cursor index into the var
		   Cursors.add(cursor);												// and save the cursor.

    	  return true;
      }

		private boolean execute_sql_drop_table(){

			if (!getDbPtrArg()) return false;					// get variable for the DB table pointer
			int i = NumericVarValues.get(theValueIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);				// get the data base

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;					// Table Name
			String TableName = StringConstant;
			if (!checkEOL()) return false;

			String CommandString = "DROP TABLE IF EXISTS " + TableName;

		   try {
		        db.execSQL(CommandString);
			   }catch (Exception e) {
					return RunTimeError(e);
			   }
		   
    	  return true;
      }

		private boolean execute_sql_new_table(){

			if (!getDbPtrArg()) return false;						// get variable for the DB table pointer
			int i = NumericVarValues.get(theValueIndex).intValue();
			SQLiteDatabase db = DataBases.get(i-1);					// get the data base

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;			   			// Table Name
			String TableName = StringConstant;

			if (!isNext(',')) return false;
			ArrayList <String> Columns = new ArrayList<String>();
			do{
				if (!getStringArg()) return false;						// Columns
				Columns.add(StringConstant);
			} while (isNext(','));
			if (!checkEOL()) return false;

		   String columns = "";
		   int cc = Columns.size();
		   for (int j =0; j<cc; ++j){
			   columns = columns + Columns.get(j) + " TEXT";
			   if (j != cc-1) columns = columns +  " , ";
		   }
		   
		   String CommandString = StringConstant;	   
		   CommandString = "CREATE TABLE " + TableName + "( "
		   + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
		   + columns + " )";

		   try {
		        db.execSQL(CommandString);
			   }catch (Exception e) {
					return RunTimeError(e);
			   }
		   
    	  
    	  return true;
      }

	// ************************************  Graphics Package ***********************************

	private boolean executeGR() {
		Command c = findCommand(GR_cmd, "GR");
		if (c != null) {
			if (!GRopen && (c.id != CID_OPEN)) {
				return RunTimeError("Graphics not opened at:");
			}
			return c.run();
		}
		return false;
	}

	private boolean executeGR_BITMAP(){
		return executeCommand(GrBitmap_cmd, "Gr.Bitmap");
	}

	private boolean executeGR_CAMERA(){
		return executeCommand(GrCamera_cmd, "Gr.Camera");
	}

	private boolean executeGR_GET(){
		return executeCommand(GrGet_cmd, "Gr.Get");
	}

	private boolean executeGR_TEXT(){
		return executeCommand(GrText_cmd, "Gr.Text");
	}

	  public void DisplayListAdd(Bundle b){
		  b.putInt("alpha", 256);
		  b.putInt("paint", PaintList.size()-1);							// paint for this lines
		  
		  if (drawintoCanvas != null){
			  GR.drawView.doDraw(drawintoCanvas, b);
			  return;
		  }

		  RealDisplayList.add(DisplayList.size());
		  DisplayList.add(b);
	  }
	  
	  public boolean execute_gr_bitmap_drawinto_start(){
		   if (!evalNumericExpression()) return false;					// 
		   if (!checkEOL()) return false;
		   int q = EvalNumericExpressionValue.intValue();
		   if (q<1 | q >= BitmapList.size()){
			   RunTimeError("Invalid Bitmap Pointer");
			   return false;			   
		   }
		   Bitmap SrcBitMap = BitmapList.get(q);
		   if (SrcBitMap == null){
			   RunTimeError("Bitmap was deleted");
			   return false;
		   }
		   if (!SrcBitMap.isMutable()){
			   RunTimeError("Bitmaps loaded from files not changeable.");
			   return false;
		   }
		   
		   if (SrcBitMap.isRecycled()){
			   RunTimeError("Bitmap was recycled");
			   SrcBitMap = null;
			   return false;
		   }
		   drawintoCanvas = new Canvas(SrcBitMap);
		   SrcBitMap = null;
		  
		  return true;
	  }
	  
	  public boolean execute_gr_bitmap_drawinto_end(){
		  drawintoCanvas = null;
		  return true;
	  }
	  
	  
	  public  void DisplayListClear(){
//			StopDisplay = true;											// Sigmal GR to stop display
//	    	try {Thread.sleep(500);}catch(InterruptedException e){}     // Give GR some time to do it
//		BitmapList.clear();
//		BitmapList.add(null);                             // Set Zero entry as null

	  	DisplayList.clear();										// Clear the Display List
	  	RealDisplayList.clear();
	  	PaintList.clear();											// and the Paint list
	    PaintListAdd(aPaint);								        // Add dummy element 0

	  	Bundle b = new Bundle();									// Create a new Display list
          b.putInt("type", GR.dNull);								// with a null entry

		   Paint tPaint = newPaint(aPaint);							// Create a newPaint object
		   tPaint.setARGB(255, 0, 0, 0);							// Set the colors, etc
		   tPaint.setAntiAlias(true);
		   tPaint.setStyle(Paint.Style.FILL );
		   tPaint.setStrokeWidth(0);
		   int f= tPaint.getFlags();
//		   tPaint.setFlags(f | Paint.FILTER_BITMAP_FLAG);
		   aPaint = newPaint(tPaint);							// Copy the temp paint to aPaint
	       PaintListAdd(aPaint);								// Add the newPaint to the Paint List

	       DisplayListAdd(b);

//    	StopDisplay = false;										// Tell GR it can start displaying again
	  }

	private boolean execute_gr_getdl(){

		String var = getArrayVarForWrite(TYPE_NUMERIC);				// get the result array variable
		if (var == null) { return false; }							// must name a new numeric array variable

		boolean keepHiddenObjects = false;
		if (isNext(',')) {											// Optional "hidden" flag
			if (!evalNumericExpression()) { return false; }
			keepHiddenObjects = (EvalNumericExpressionValue != 0.0);
		}
		if (!checkEOL()) { return false; }							// line must end with ']'

		double[] list = new double[RealDisplayList.size() + 1];
		int count = 0;
		for (int idx : RealDisplayList) {							// For each object index
			boolean include = ((idx != 0) &&						// If not index of null object...
				(keepHiddenObjects ||								// ... and either keeping all objects...
					(DisplayList.get(idx).getInt("hide") == 0)));	// ... or object is not hidden...
			if (include) { list[count++] = idx; }					// ... then put index in the new list
		}
		if (count == 0) { count = 1; }								// if no objects, make a list with
																	// one entry that indexes the null object

		if (!BuildBasicArray(var, true, count)) { return false; }	// build the array
		for (int i = 0, j = ArrayValueStart; i < count; ++i, ++j) {	// stuff the array
			NumericVarValues.set(j, list[i]);						// count may be < list.length
		}
		return true;
	}

	private boolean execute_gr_newdl(){

		if (getArrayVarForRead() == null) { return false; }			// Get the array variable
		if (!VarIsNumeric) { return RunTimeError(EXPECT_NUM_ARRAY); } // Insure that it is a numeric array
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;							// Get values inside [], if any
		if (!checkEOL()) { return false; }							// line must end with ']'

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		RealDisplayList.clear();
		RealDisplayList.add(0);										// First entry points to null bundle

		for (int i = 0; i < length; ++i) {							// Copy the bundle pointers 
			int id = NumericVarValues.get(base + i).intValue();
			if (id < 0 || id >= DisplayList.size()) {
				return RunTimeError("Invalid Object Number");
			}
			RealDisplayList.add(id);
		}

		return true;
	}

	public void PaintListAdd(Paint p){
		PaintList.add(p);
	}

	public Paint newPaint(Paint fromPaint){							// Does a new Paint
		Typeface tf = fromPaint.getTypeface();						// while preserving the type face
		Paint rPaint = new Paint(fromPaint);
		rPaint.setTypeface(tf);
		return rPaint;
	}

	private boolean execute_gr_open(){
		if (GRopen) {
			return RunTimeError("Graphics already Opened");
		}

		if (!evalNumericExpression()) return false;					// Get alpha
		int a = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get red
		int r = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get green
		int g = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get blue
		int b = EvalNumericExpressionValue.intValue();

		int showStatusBar = 0;										// default to status bar not showing
		int orientation = 0;										// default to landscape
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			showStatusBar = EvalNumericExpressionValue.intValue();
			if (isNext(',')) {
				if (!evalNumericExpression()) return false;
				orientation = EvalNumericExpressionValue.intValue();
			}
		}
		if (!checkEOL()) return false;

		int backgroundColor =	a * 0x1000000 +						// Set the appropriate bytes
								r * 0x10000 +
								g * 0x100 +
								b;

		  drawintoCanvas = null;
		  DisplayListClear();
		  BitmapList.clear();
		  BitmapList.add(null);                             // Set Zero entry as null

		   Paint tPaint = new Paint();					// Create a newPaint object
		   tPaint.setARGB(a, r, g, b);							// Set the colors, etc
		   tPaint.setAntiAlias(true);
		   tPaint.setStyle(Paint.Style.FILL );
		   tPaint.setStrokeWidth(0f);
		   int f= tPaint.getFlags();
//		   tPaint.setFlags(f | Paint.FILTER_BITMAP_FLAG);
		   aPaint = newPaint(tPaint);							// Copy the temp paint to aPaint
	       PaintListAdd(aPaint);								// Add the newPaint to the Paint List as element 1

		GRclass = new Intent(this, GR.class);						// Set up parameters for the Graphics Activity
		GRclass.putExtra(GR.EXTRA_SHOW_STATUSBAR, showStatusBar);
		GRclass.putExtra(GR.EXTRA_ORIENTATION, orientation);
		GRclass.putExtra(GR.EXTRA_BACKGROUND_COLOR, backgroundColor);

		GraphicsPaused = false;										// Set up the signals
		GRrunning = false;
		startActivityForResult(GRclass, BASIC_GENERAL_INTENT);		// Start the Graphics Activity
		while (!GRrunning) Thread.yield();							// Do not continue until GR signals it is running

	       background = false;
	       GRopen = true;									// Set some more signals
	       RunPaused = false;
	       NewTouch[0] = false;
	       NewTouch[1] = false;
	       NewTouch[2] = false;
	       GR.doSTT = false;
	       GRFront = true;
	       CameraNumber = -1;
	       setVolumeControlStream(AudioManager.STREAM_MUSIC);
		  return true;
	  }
	  
	  private boolean execute_paint_get(){
		  if (!getNVar()) return false;
		  NumericVarValues.set(theValueIndex, (double) PaintList.size()-1);
		  return true;
	  }
	  
	  private boolean execute_gr_close(){
			if (!checkEOL()) return false;
		  
		  DisplayListClear();                 // Clear the existing display list
		  
		  Bundle aBundle = new Bundle();	  // Create a new display list bundle
		  aBundle.putInt("type", GR.dClose);  // which commands GR.java to close
		  aBundle.putInt("hide", 0);
		  DisplayListAdd(aBundle);
		  
			synchronized (GR.Rendering) {
				if (GR.Rendering) return true;
				GR.Rendering = true;
			}

		  GR.drawView.postInvalidate();		  // Start the draw so the command will get executed.
		  GRopen = false;
		  GRFront = false;
		  return true;
	  }
	  
	  private boolean execute_gr_render(){
			if (!checkEOL()) return false;
		  if (GR.drawView == null){				// Make sure drawView has not gone null
			  Stop = true;
			  return false;
		  }
		  
		synchronized (GR.Rendering) {
			if (GR.Rendering) return true;
			GR.Rendering = true;
		}
		  
		  GR.NullBitMap = false;
		  
		  GR.drawView.postInvalidate();			// Start GR drawing.
		  while (GR.Rendering)
		  	{Thread.yield();}

		  if (GR.NullBitMap){
			  RunTimeError("Display List had deleted bitmap.");
			  GR.NullBitMap = false;
			  return false;
		  }
		  return true;
	  }

	private boolean execute_gr_color(){

		if (!evalNumericExpression()) return false;							// Get alpha
		int a = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get red
		int r = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get green
		int g = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get blue
		int b = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get fill
		double d = EvalNumericExpressionValue;
		if (!checkEOL()) return false;

		   Paint tPaint = newPaint(aPaint);					// Create a newPaint object
		   tPaint.setARGB(a, r, g, b);							// Set the colors, etc
//		   tPaint.setAntiAlias(true);
		   if (d == 0) tPaint.setStyle(Paint.Style.STROKE );
		   else if (d == 1) tPaint.setStyle(Paint.Style.FILL);
		   else tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		   aPaint = newPaint(tPaint);							// Copy the temp paint to aPaint
	       PaintListAdd(aPaint);								// Add the newPaint to the Paint List
		  return true;
	}

	private boolean execute_gr_antialias(){
		if (!evalNumericExpression()) return false;				// Get the boolean
		Paint tPaint = newPaint(aPaint);
		tPaint.setAntiAlias(EvalNumericExpressionValue != 0);
		aPaint = newPaint(tPaint);								// Copy the temp paint to aPaint
		PaintListAdd(aPaint);									// Add the newPaint to the Paint List
		return true;
	}

	private boolean execute_gr_stroke_width(){
		if (!evalNumericExpression()) return false;				// Get the width
		float width = EvalNumericExpressionValue.floatValue();
		if (width < 0) {
			return RunTimeError("Width must be >= 0");
		}
		Paint tPaint = newPaint(aPaint);						// Create a newPaint object
		tPaint.setStrokeWidth(width);							// Set the stroke width
		aPaint = newPaint(tPaint);								// Copy the temp paint to aPaint
		PaintListAdd(aPaint);									// Add the newPaint to the Paint List

		return true;
	}

	private boolean execute_gr_point(){
		Bundle aBundle = new Bundle();
		aBundle.putInt("type", GR.dPoint);
		aBundle.putInt("hide", 0);

		if (!getNVar()) return false;							// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get x
		int x = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get y
		int y = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		aBundle.putInt("x", x);
		aBundle.putInt("y", y);
		int p = PaintList.size();								// Set the most current paint as the paint
		aBundle.putInt("paint", p-1);							// paint for this point
		NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		DisplayListAdd(aBundle);								// Add the line to the display list
		return true;
	}

	private boolean execute_gr_line(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dLine);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;										// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x1
		aBundle.putInt("x1", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get y1
		aBundle.putInt("y1", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x2
		aBundle.putInt("x2", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get y2
		aBundle.putInt("y2", EvalNumericExpressionValue.intValue());
		if (!checkEOL()) return false;


		  int p = PaintList.size();								// Set the most current paint as the paint
		  aBundle.putInt("paint", p-1);							// paint for this lines
		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		  DisplayListAdd(aBundle);				// Add the line to the display list
		  return true;
	}

	private boolean execute_gr_rect(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dRect);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;										// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get left
		aBundle.putInt("left", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get top
		aBundle.putInt("top", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get right
		aBundle.putInt("right", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get bottom
		aBundle.putInt("bottom", EvalNumericExpressionValue.intValue());
		if (!checkEOL()) return false;

		  int p = PaintList.size();						// Set current paint as this circle's paint
		  aBundle.putInt("paint", p-1);
		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		  DisplayListAdd(aBundle);
		  return true;
	}

	private boolean execute_gr_arc(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dArc);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;										// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get left
		aBundle.putInt("left", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get top
		aBundle.putInt("top", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get right
		aBundle.putInt("right", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get bottom
		aBundle.putInt("bottom", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get start angle
		aBundle.putInt("start_angle", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get end angle
		aBundle.putInt("sweep_angle", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get flag
		aBundle.putInt("fill_mode", EvalNumericExpressionValue.intValue());
		if (!checkEOL()) return false;

		  int p = PaintList.size();						// Set the current paint as this objct's paint
		  aBundle.putInt("paint", p-1);
		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		  DisplayListAdd(aBundle);
		  return true;
	}

	private boolean execute_gr_circle(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dCircle);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;										// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x
		aBundle.putInt("x", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x
		aBundle.putInt("y", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get r
		aBundle.putInt("radius", EvalNumericExpressionValue.intValue());
		if (!checkEOL()) return false;

		  int p = PaintList.size();					// Set the current paint as this object's paint
		  aBundle.putInt("paint", p-1);
		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		  DisplayListAdd(aBundle);
		  return true;
	}

	private boolean execute_gr_oval(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dOval);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;										// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get left
		aBundle.putInt("left", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get top
		aBundle.putInt("top", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get right
		aBundle.putInt("right", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get bottom
		aBundle.putInt("bottom", EvalNumericExpressionValue.intValue());
		if (!checkEOL()) return false;

		  int p = PaintList.size();						// Set the current paint as this object's paint
		  aBundle.putInt("paint", p-1);
		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var
		  DisplayListAdd(aBundle);
		  return true;
	}

	  private double gr_collide(int Object1, int Object2){

		  double fail = -1;
		  double xfalse = 0;
		  double xtrue = 1;
		  
		  if (Object1 <0 || Object1 >= DisplayList.size()){
			  RunTimeError("Object 1 Number out of range");
			  return fail;
		  }
		  Bundle b1 = DisplayList.get(Object1);									// Get the bundle to change
		  if (b1.getInt("hide") != 0) return xfalse;								// If hidden then no collide
		  Rect r1 = gr_getRect(b1);
		  if (r1 == null) return fail;
		  
		  if (Object2 <0 || Object2 >= DisplayList.size()){
			  RunTimeError("Object 2 Number out of range");
			  return fail;
		  }
		  Bundle b2 = DisplayList.get(Object2);									// Get the bundle to change
		  if (b2.getInt("hide") != 0) return xfalse;								// If hidden then no collide
		  Rect r2 = gr_getRect(b2);
		  if (r2 == null) return fail;
		  
		  if (r1.bottom < r2.top)				// Test for collision
			  return xfalse;
			if (r1.top > r2.bottom)
			  return xfalse;
			if (r1.right < r2.left)
			  return xfalse;
			if (r1.left > r2.right)
			  return xfalse;

		  return xtrue;
	  }

	private Rect gr_getRect(Bundle b) {
		Rect theRect = null;

		int type = b.getInt("type");
		switch (type) {
			case GR.dCircle:
				theRect = new Rect();
				int cx = b.getInt("x");
				int cy = b.getInt("y");
				int cr = b.getInt("radius");
				theRect.top = cy - cr;
				theRect.left = cx - cr;
				theRect.bottom = cy + cr;
				theRect.right = cx + cr;
				break;

			case GR.dRect:
			case GR.dOval:
			case GR.dArc:
				theRect = new Rect();
				theRect.left= b.getInt("left");
				theRect.top =b.getInt("top");
				theRect.bottom = b.getInt("bottom");
				theRect.right =b.getInt("right");
				break;

			case GR.dBitmap:
				theRect = new Rect();
				theRect.top = b.getInt("y");
				theRect.left = b.getInt("x");
				Bitmap theBitmap = BitmapList.get(b.getInt("bitmap"));
				theRect.bottom = theRect.top + theBitmap.getHeight();
				theRect.right = theRect.left + theBitmap.getWidth();
				break;

			case GR.dText:
				theRect = new Rect();
				Paint paint = PaintList.get(b.getInt("paint"));
				String text = b.getString("text");
				paint.getTextBounds(text, 0, text.length(), theRect);	// returns the minimum bounding box
																		// from implied origin (0,0)
				int tx = b.getInt("x");
				int ty = b.getInt("y");
				float tw = paint.measureText(text);						// width used for alignment
																		// generally more than theRect.width()
				Paint.Align align = paint.getTextAlign();
				switch (align) {										// adjust origin for alignment
					case LEFT:   theRect.offset(tx,               ty); break;
					case CENTER: theRect.offset((int)(tx - tw/2), ty); break;
					case RIGHT:  theRect.offset((int)(tx - tw),   ty); break;
				}
				break;
		}

		return theRect;
	}

	  private boolean execute_gr_cls(){
			if (!checkEOL()) return false;
		  
		  DisplayListClear();
		  return true;
	  }

	private Bundle getObjectBundle() {						// get and validate the user-sepecified graphics object
		Bundle b = null;
		if (!evalNumericExpression()) return b;					// Get Object Number
		int obj = EvalNumericExpressionValue.intValue();
		if (obj < 0 || obj >= DisplayList.size()) {
			RunTimeError("Object out of range");
		} else {
			b = DisplayList.get(obj);							// Get the specified display object
		}
		return b;
	}

	private boolean execute_gr_hide(){
		if (!evalNumericExpression()) return false;							// Get Object Number
		int obj = EvalNumericExpressionValue.intValue();
		if (obj < 0 || obj >= DisplayList.size()) {
			return RunTimeError("Hide parameter out of range");
		}
		if (!checkEOL()) return false;

		Bundle b = DisplayList.get(obj);				// Get the specified display object
		b.putInt("hide", 1);							// Set hide to true
		DisplayList.set(obj, b);						// put the modified object back
		return true;
	}

	private boolean execute_gr_show(){
		if (!evalNumericExpression()) return false;							// Get Object Number
		int obj = EvalNumericExpressionValue.intValue();
		if (obj < 0 || obj >= DisplayList.size()) {
			return RunTimeError("Show parameter out of range");
		}
		if (!checkEOL()) return false;

		Bundle b = DisplayList.get(obj);				// Get the specified display object
		b.putInt("hide", 0);							// Set hide to false
		DisplayList.set(obj, b);						// put the modified object back
		return true;
	}

	private boolean execute_gr_get_position(){
		Bundle b = getObjectBundle();
		if (b == null) return false;

		if (!isNext(',') || !getNVar()) return false;
		int xIndex = theValueIndex;
		if (!isNext(',') || !getNVar()) return false;
		int yIndex = theValueIndex;
		if (!checkEOL()) return false;

		     int x = 0;									// get the position value
		     int y = 0;
		     if (b.containsKey("x")) x = b.getInt("x");
		     else x = b.getInt("left");
		     if (b.containsKey("y")) y = b.getInt("y");
		     else y = b.getInt("top");

		NumericVarValues.set(xIndex, (double) x);
		NumericVarValues.set(yIndex, (double) y);

		return true;
	}

	private boolean execute_gr_get_value(){
		Bundle b = getObjectBundle();								// get the graphics object
		if (b == null) return false;

		if (!isNext(',') || !getStringArg()) return false;			// get the parameter string
		String parm = StringConstant;

		if (!b.containsKey(parm)) {
			return RunTimeError("Object does not contain " + parm);
		}

		if (!isNext(',') || !getVar() || !checkEOL()) return false;
		if (VarIsNumeric == parm.equals("text")) {					// error if numeric var and "text" tag
			return RunTimeError("Wrong var type for tag: " + parm);	// or string var and not "text" tag
		}
		if (VarIsNumeric) {
			int value = b.getInt(parm);
			NumericVarValues.set(theValueIndex, (double) value);
		} else {
			String theText = b.getString(parm);
			StringVarValues.set(theValueIndex, theText);
		}
		return true;
	}

	private boolean execute_gr_get_type() {
		Bundle b = getObjectBundle();								// get the graphics object
		if (b == null) return false;
		if (!isNext(',') || !getStringArg() || !checkEOL()) return false;// var for type string

		int type = b.getInt("type");
		StringVarValues.set(theValueIndex, GR.types[type]);
		return true;
	}

	private boolean execute_gr_get_params() {
		Bundle b = getObjectBundle();								// get the graphics object
		if (b == null) return false;

		if (!isNext(',')) { return false; }
		String var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
		if (var == null) { return false; }							// must name a new string array variable
		if (!checkEOL()) { return false; }							// line must end with ']'

		Set<String> keySet = b.keySet();							// get parameters from Bundle
		keySet.remove("type");
		keySet.remove("hide");
		ArrayList<String> keys = new ArrayList<String>(keySet);

		/* Puts the list of keys into a new array */
		return ListToBasicStringArray(var, keys, keys.size());
	}

	private boolean execute_gr_touch(int p){
		if (!getNVar()) return false;							// Graphic boolean Variable
		int SaveBooleanIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getNVar()) return false;							// Graphic X variable
		int SaveXIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getNVar()) return false;							// Graphic Y variable
		int SaveYIndex = theValueIndex;
		if (!checkEOL()) return false;

		   double flag = 0.0;
		   if (NewTouch[p]) {					// If touched
			   flag = 1;						// set flag to tue
//			   GR.NewTouch[p] = false;
		   }
		   
		   NumericVarValues.set(SaveBooleanIndex, flag);  // Return the values to the user
		   NumericVarValues.set(SaveXIndex, TouchX[p]);
		   NumericVarValues.set(SaveYIndex, TouchY[p]);
		   return true;
	}

	private boolean execute_gr_bound_touch(int p){
		if (!getNVar()) return false;							// Graphic boolean Variable
		int SaveBooleanIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get left
		double left = EvalNumericExpressionValue;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get top
		double top = EvalNumericExpressionValue;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get right
		double right = EvalNumericExpressionValue;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get bottom
		double bottom = EvalNumericExpressionValue;
		if (!checkEOL()) return false;

			   boolean flag = false;
			   if (NewTouch[p]) {					// If touched
				   flag = true;						// set flag to true
//				   GR.NewTouch[p] = false;				// then set not touched
			   }

			   if (!flag){                                       // If not touched
				   NumericVarValues.set(SaveBooleanIndex, 0.0);  // Return the false boolean to the user
				   return true;
			   }
			   
			   if (TouchX[p] >= left && TouchX[p] <= right &&      // If the touch was in the bounding rect
			       TouchY[p] >= top  && TouchY[p] <= bottom) {
				   NumericVarValues.set(SaveBooleanIndex, 1.0);    // Return the true boolean to the user
			   } else NumericVarValues.set(SaveBooleanIndex, 0.0); // else return the false boolean to the user

		return true;
	}

	private boolean execute_gr_text_draw(){
		  Bundle aBundle = new Bundle();       // Create a new object of type text
		  aBundle.putInt("type", GR.dText);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;										// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x
		aBundle.putInt("x", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get y
		aBundle.putInt("y", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;									// Get the text
		aBundle.putString("text", StringConstant);
		if (!checkEOL()) return false;

		  int p = PaintList.size();												// Set the current paint	
		  aBundle.putInt("paint", p-1);											// as the text paint

		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		  DisplayListAdd(aBundle);

		return true;
	}

	private boolean execute_gr_text_align(){
		  Paint tPaint = newPaint(aPaint);										// Clone the current paint
		  if (!evalNumericExpression()) return false;							// Get Align parameter
			  double d = EvalNumericExpressionValue;
			  if (d == 1){ tPaint.setTextAlign(Paint.Align.LEFT);}              // Set the paint align value
			  else if (d == 2){ tPaint.setTextAlign(Paint.Align.CENTER);}
			  else if (d == 3){ tPaint.setTextAlign(Paint.Align.RIGHT);}
			  else {
				  RunTimeError( "Align value not 1, 2 or 3 at ");
				  return false;
			  }
				if (!checkEOL()) return false;
			  aPaint = newPaint(tPaint);				// Set the new current paint
		      PaintListAdd(aPaint);						// and add it to the paint list
			  return true;
	}

	private boolean execute_gr_text_size(){
			Paint tPaint = newPaint(aPaint);            // Clone the current paint
			if (!evalNumericExpression()) return false; // Get desired size
			double d = EvalNumericExpressionValue;
			if (!checkEOL()) return false;
			if (d < 1){
				RunTimeError( "must be > 0");
				return false;
			 }
			tPaint.setTextSize((int)d);					// Set the text size in paint
			 aPaint = newPaint(tPaint);				// Clone the temp paint into current paint
			 PaintListAdd(aPaint);						// Add current paint to the paint list
			 return true;
	}

	private boolean execute_gr_text_underline(){
			Paint tPaint = newPaint(aPaint);
			if (!evalNumericExpression()) return false;							// Get boolean
			if (!checkEOL()) return false;
			double d = EvalNumericExpressionValue;
			if (d==0) tPaint.setUnderlineText(false);
			else tPaint.setUnderlineText(true);
			
			 aPaint = newPaint(tPaint);
			 PaintListAdd(aPaint);
			 return true;
	}

	private boolean execute_gr_text_skew(){
			Paint tPaint = newPaint(aPaint);
			if (!evalNumericExpression()) return false;							// Get Skew
			double d = EvalNumericExpressionValue;
			if (!checkEOL()) return false;
			tPaint.setTextSkewX((float)d);
			 aPaint = newPaint(tPaint);
			 PaintListAdd(aPaint);
			 return true;
	}

	private boolean execute_gr_text_bold(){
			Paint tPaint = newPaint(aPaint);
			if (!evalNumericExpression()) return false;							// Get boolean 
			double d = EvalNumericExpressionValue;
			if (!checkEOL()) return false;
			int flag = tPaint.getFlags();
			if ( d==0) flag = flag & 0xdf;
			else flag = flag | 0x20;
			tPaint.setFlags(flag);

			aPaint = newPaint(tPaint);
			 PaintListAdd(aPaint);
			 return true;
	}

	private boolean execute_gr_text_strike(){
			Paint tPaint = newPaint(aPaint);
			if (!evalNumericExpression()) return false;							// Get boolean 
			double d = EvalNumericExpressionValue;
			if (!checkEOL()) return false;
			int flag = tPaint.getFlags();
			if ( d==0) flag = flag & 0xef;
			else flag = flag | 0x10;
			tPaint.setFlags(flag);

			aPaint = newPaint(tPaint);
			 PaintListAdd(aPaint);
			 return true;
	}

	private Bundle getTextBundle(int dlIndex) {
		if (dlIndex < 0 || dlIndex >= DisplayList.size()) {
			RunTimeError("Object Number out of range");
			return null;
		}
		Bundle b = DisplayList.get(dlIndex);
		if (b.getInt("type") != GR.dText) {
			RunTimeError("Not a text object");
			return null;
		}
		return b;
	}

	private boolean execute_gr_get_textbounds() {
		Paint paint;
		String text;
		if (evalNumericExpression()) {						// if argument is an object number
			int index = EvalNumericExpressionValue.intValue();
			Bundle b = getTextBundle(index);
			if (b == null) return false;
			paint = PaintList.get(b.getInt("paint"));		// use the text object's paint
			text = b.getString("text");						// get text from the text object
		} else {
			if (SyntaxError) return false;
			if (!getStringArg()) return false;
			text = StringConstant;							// argument is the text to measure
			paint = aPaint;									// use current Paint
		}

		if (!isNext(',')) return false;
		int[] ind = getArgs4NVar();							// [left, top, right, bottom]
		if (ind == null) return false;						// error getting variables
		if (!checkEOL()) return false;

		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);

		NumericVarValues.set(ind[0], (double)bounds.left);
		NumericVarValues.set(ind[1], (double)bounds.top);
		NumericVarValues.set(ind[2], (double)bounds.right);
		NumericVarValues.set(ind[3], (double)bounds.bottom);

		return true;
	}

	private boolean execute_gr_text_width() {
		if (!getNVar()) return false;						// Width return  Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		Paint paint;
		String text;
		if (evalNumericExpression()) {						// if argument is an object number
			int index = EvalNumericExpressionValue.intValue();
			Bundle b = getTextBundle(index);
			if (b == null) return false;
			paint = PaintList.get(b.getInt("paint"));		// use the text object's paint
			text = b.getString("text");						// get text from the text object
		} else {
			if (SyntaxError) return false;
			if (!getStringArg()) return false;
			text = StringConstant;							// argument is the text to measure
			paint = aPaint;									// use current Paint
		}
		if (!checkEOL()) return false;

		double w = paint.measureText(text);					// Get the string's width
		NumericVarValues.set(SaveValueIndex, w);			// Save the width into the var

		return true;
	}

	private boolean execute_gr_bitmap_load(){
		if (!getNVar()) return false;									// Graphic Bitmap Pointer Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;								// Get the file path
		if (!checkEOL()) return false;

		String fileName = StringConstant;								// The filename as given by the user
		BufferedInputStream bis = null;									// Establish an input stream
		try { bis = Basic.getBufferedInputStream(Basic.DATA_DIR, fileName); }
		catch (Exception e) { return RunTimeError(e); }
		if (bis == null) { return RunTimeError("No bitmap found"); }	// Can this happen?

		System.gc();													// Garbage collect

		aBitmap = BitmapFactory.decodeStream(bis);				// Create bitmap from the input stream

		try { bis.close(); }
		catch (Exception e) { return RunTimeError(e); }

		if (aBitmap == null) return RunTimeError("Bitmap load failed at:");
		   
		   NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
		   
		   BitmapList.add(aBitmap); // Add the new bit map to the bitmap list
		   
		   return true;
	}

	private boolean execute_gr_bitmap_delete(){
		   if (!evalNumericExpression()) return false; 
		   if (!checkEOL()) return false;
		   int q = EvalNumericExpressionValue.intValue();
		   if (q<1 | q >= BitmapList.size()){
			   RunTimeError("Invalid Bitmap Pointer");
			   return false;			   
		   }
		   Bitmap SrcBitMap = BitmapList.get(q);
		   if (SrcBitMap != null)
		      SrcBitMap.recycle();
		   BitmapList.set(q, null);
		   System.gc();
		  return true;
	}

	private boolean execute_gr_bitmap_scale(){

		if (!getNVar()) return false;							// Graphic Destination Bitmap Pointer Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get Source Bitmap
		int q = EvalNumericExpressionValue.intValue();
		if (q < 1 | q >= BitmapList.size()) {
			return RunTimeError("Invalid Bitmap Pointer");
		}

		Bitmap SrcBitMap = BitmapList.get(q);
		if (SrcBitMap == null){
			return RunTimeError("Bitmap was deleted");
		}
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get Width
		int Width = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get Height
		int Height = EvalNumericExpressionValue.intValue();

		boolean parm = true;
		if (isNext(',')) {													// optional scale parameter
			if (!evalNumericExpression()) return false;
			if (EvalNumericExpressionValue == 0.0) parm = false;
		}
		if (!checkEOL()) return false;

		if (Width == 0 || Height == 0) {
			return RunTimeError("Width and Height must not be zero");
		}

		try { aBitmap = Bitmap.createScaledBitmap(SrcBitMap, Width, Height, parm); }
		catch (Exception e) { return RunTimeError(e); }

			System.gc();
			NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
			System.gc();
			BitmapList.add(aBitmap);

		return true;
	}

	private boolean execute_gr_bitmap_size(){

		if (!getNVar()) return false;							// Graphic Source Bitmap Pointer Variable
		int q = NumericVarValues.get(theValueIndex).intValue();
		if (q < 1 | q >= BitmapList.size()) {
			return RunTimeError("Invalid Bitmap Pointer");
		}

		Bitmap SrcBitMap = BitmapList.get(q);					// Access the bitmap
		if (SrcBitMap == null) {
			return RunTimeError("Bitmap was deleted");
		}

		int w = SrcBitMap.getWidth();							// Get the image width
		int h = SrcBitMap.getHeight();							// Get the image height

		if (!isNext(',')) return false;
		if (!getNVar()) return false;							// Get the height variable
		NumericVarValues.set(theValueIndex, (double) w);		// Set the height value

		if (!isNext(',')) return false;
		if (!getNVar()) return false;							// Get the width variable
		NumericVarValues.set(theValueIndex, (double) h);		// Set the width value
		if (!checkEOL()) return false;

		return true;
	}

	private boolean execute_gr_bitmap_crop(){
		if (!getNVar()) return false;							// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get source bitmap index
		int SourceBitmapIndex = EvalNumericExpressionValue.intValue();
		if (SourceBitmapIndex < 0 || SourceBitmapIndex >= BitmapList.size()) {
			return RunTimeError("Invalid Source Bitmap Pointer");
		}
		Bitmap SourceBitmap = BitmapList.get(SourceBitmapIndex);

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;							// Get x
		int x = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get y
		int y = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get width
		int width = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get height
		int height = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		  try {
			  aBitmap = Bitmap.createBitmap(SourceBitmap, x, y, width, height);
		  }
		  catch (Exception e){
			  return RunTimeError(e);
		  }

		   NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
		   BitmapList.add(aBitmap);

		return true;
	}

	private boolean execute_gr_bitmap_draw(){
		  Bundle aBundle = new Bundle();         // Create a new display object of type bitmap
		  aBundle.putInt("type", GR.dBitmap);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;							// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;				// Get Bitmap obj pointer
		int q = EvalNumericExpressionValue.intValue();
		if (q < 1 | q >= BitmapList.size()) {
			return RunTimeError("Invalid Bitmap Pointer");
		}

		if (BitmapList.get(q) == null) {
			return RunTimeError("Bitmap was deleted");
		}
		aBundle.putInt("bitmap", q);
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x
		aBundle.putInt("x", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get y
		aBundle.putInt("y", EvalNumericExpressionValue.intValue());
		if (!checkEOL()) return false;

		  int p = PaintList.size();						// Set current paint as this circle's paint
		  aBundle.putInt("paint", p-1);
		  
		  aBundle.putInt("alpha", 255);

		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		  DisplayListAdd(aBundle);

		return true;
	}

	private boolean execute_gr_bitmap_create(){
		if (!getNVar()) return false;										// Get bitmap ptr var
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get the width
		int width = EvalNumericExpressionValue.intValue();
		if (width <= 0) {
			return RunTimeError("Width must be >= 0");
		}
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get the height
		int height = EvalNumericExpressionValue.intValue();
		if (height <= 0) {
			return RunTimeError("Height must be >= 0");
		}
		if (!checkEOL()) return false;

		   try{
			   aBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // Create the bitamp
		   }
		   catch (Exception e) {
			   return RunTimeError(e);
		   }
		   
		   NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
		   BitmapList.add(aBitmap);
		   		  
		return true;
	}

	private boolean execute_gr_rotate_start(){

		  Bundle aBundle = new Bundle();            // Create a new display list object of type rotate
		  aBundle.putInt("type", GR.dRotate_Start);
		  aBundle.putInt("hide", 0);

		if (!evalNumericExpression()) return false;							// Get angle
		aBundle.putInt("angle", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get x
		aBundle.putInt("x", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get y
		aBundle.putInt("y", EvalNumericExpressionValue.intValue());

		if (isNext(',')) {
			if (!getNVar()) return false;
			NumericVarValues.set(theValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		}
		if (!checkEOL()) return false;

		DisplayListAdd(aBundle);					// Put the new object into the display list

		return true;
	}

	private boolean execute_gr_rotate_end(){
		  Bundle aBundle = new Bundle();           // Create a new object of type Rotate end
		  aBundle.putInt("type", GR.dRotate_End); 
		  aBundle.putInt("hide", 0);

		if (!isEOL()) {
			if (!getNVar()) return false; 
			NumericVarValues.set(theValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
			if (!checkEOL()) return false;
		}

		  DisplayListAdd(aBundle);					// add the object to the display list
		  return true;
	}

	private boolean execute_gr_modify(){

		if (!evalNumericExpression()) return false;							// Get Object Number
		int index = EvalNumericExpressionValue.intValue();
		if (index < 0 || index >= DisplayList.size()) {
			return RunTimeError("Object Number out of range");
		}

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;									// get the parameter string
		if (!isNext(',')) return false;
		String parm = StringConstant;

		Bundle b = DisplayList.get(index);									// Get the bundle to change
		if (!b.containsKey(parm)) {
			return RunTimeError("Object does not contain: " + parm);
		}

		if (parm.equals("text")) {
			if (!getStringArg()) return false;								// get the parameter string
			if (!checkEOL()) return false;
			b.putString(parm, StringConstant);
		} else {
			if (!evalNumericExpression()) return false;						// Get parameter value
			if (!checkEOL()) return false;
			int value = EvalNumericExpressionValue.intValue();
			if (parm.equals("bitmap")) {
				if (value < 0 | value >= BitmapList.size()) {
					return RunTimeError("Bitmap pointer out of range");
				}
			} else if (parm.equals("paint")) {
				if (value < 1 || value >= PaintList.size()) {
					return RunTimeError ("Invalid Paint object number");
				}
			}
			b.putInt(parm, value);
		}

		DisplayList.set(index, b);
		return true;
	}

	private boolean execute_gr_orientation(){
		if (!evalNumericExpression()) return false;		// get the mode (landscape or portrait)
		if (!checkEOL()) return false;
		int mode = EvalNumericExpressionValue.intValue();
		GR.drawView.setOrientation(mode);
		return true;
	}

	private boolean execute_gr_screen() {
		if (!getNVar()) return false;						// width variable
		int widthIndex = theValueIndex;

		if (!isNext(',')) return false;
		if (!getNVar()) return false;						// height variable
		int heightIndex = theValueIndex;

		int densityIndex = -1;
		if (isNext(',')) {
			if (!getNVar()) return false;					// optional density variable
			densityIndex = theValueIndex;
		}
		if (!checkEOL()) return false;

		Point size = new Point();
		int densityDpi = GR.drawView.getWindowMetrics(size);

		NumericVarValues.set(widthIndex, (double)size.x);
		NumericVarValues.set(heightIndex, (double)size.y);
		if (densityIndex != -1) {
			NumericVarValues.set(densityIndex, (double)densityDpi);
		}
		return true;
	}

	private boolean execute_gr_front(){
		if (!evalNumericExpression()) return false;						// Get flag
		if (EvalNumericExpressionValue == 0) {
			  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(Basic.theProgramRunner);
			  GRFront = false;
		} else {
			  GRclass.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			  startActivity(GRclass);
			  GRFront = true;
		}
		if (!checkEOL()) return false;
		try {Thread.sleep(100);}catch(InterruptedException e){}
		return true;
	}

	private boolean execute_gr_set_pixels(){

		if (!getNVar()) return false;							// Graphic Object Variable
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) return false;
		if (getArrayVarForRead() == null) return false;			// Get the array variable
		if (!VarIsNumeric) { return RunTimeError(EXPECT_NUM_ARRAY); }
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] pair = { null, null };
		if (!getIndexPair(pair)) return false;					// Get values inside [], if any

		int x = 0;
		int y = 0;
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			x = EvalNumericExpressionValue.intValue();
			if (!isNext(',')) return false;
			if (!evalNumericExpression()) return false;
			y = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL()) return false;

		if (!getArraySegment(arrayTableIndex, pair)) { return false; }	// Get array base and length
		int base = pair[0].intValue();
		int length = pair[1].intValue();
		if ((length % 2) != 0){
			return RunTimeError("Not an even number of elements in pixel array");
		}

/*		int pixelBase = PixelPoints.size();
		for (int i = 0; i < length; ++i){
			double d = NumericVarValues.get(base+i);
			PixelPoints.add((float) d);
		}
*/
		Bundle aBundle = new Bundle();
		aBundle.putInt("type", GR.dsetPixels);
		aBundle.putInt("hide", 0);
		aBundle.putInt("pbase", base);
		aBundle.putInt("plength", length);
		aBundle.putInt("x", x);
		aBundle.putInt("y", y);

		int p = PaintList.size();								// Set the most current paint as the paint
		aBundle.putInt("paint", p-1);							// paint for these pixels

		NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		DisplayListAdd(aBundle);								// Add the line to the display list

		return true;
	}

	  private boolean execute_gr_get_bmpixel(){
		  if (!evalNumericExpression()) return false;
		  int SourceBitmapIndex = EvalNumericExpressionValue.intValue();
		  if (!isNext(',')) { return false; }
		  
		   if (SourceBitmapIndex < 0 || SourceBitmapIndex >= BitmapList.size()){
			   RunTimeError("Invalid Source Bitmap Pointer");
			   return false;
		   }
		   Bitmap SourceBitmap = BitmapList.get(SourceBitmapIndex);
		   
		   return getTheBMpixel(SourceBitmap);
	  }
	  
	  private boolean execute_gr_get_pixel(){
	  		
            boolean retval = true;
			Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
			if (b == null){
				RunTimeError("Could not capture screen bitmap. Sorry.");
				retval = false;
			} else {

				retval = getTheBMpixel(b);								// get the requested pixel

				b.recycle();											// clean up bitmap
				b = null;
			}
			GR.drawView.destroyDrawingCache();							// clean up DrawingCache
			System.gc();
			return retval;
	  }

		// After it's done with the Bitmap, caller should call destroyDrawingCache()
		private Bitmap getTheBitmap() {
			synchronized (GR.Rendering) {
				GR.drawView.setDrawingCacheEnabled(true);
				GR.Rendering = true;									// buildDrawingCache() renders
				GR.drawView.buildDrawingCache();						// Build the cache
				return GR.drawView.getDrawingCache();					// get the bitmap
			}
		}

	private boolean getTheBMpixel(Bitmap b){

		if (!evalNumericExpression()) return false;						// Get x
		int x = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;						// Get y
		int y = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		int w = b.getWidth();											// Get the image width
		int h = b.getHeight();											// Get the image height
		if (x < 0 || x >= w || y < 0 || y >= h) {
			return RunTimeError("x or y exceeds size of screen");
		}

		int pixel = (b == null) ? 0 : b.getPixel(x, y);					// get the pixel from the bitmap

		int alpha = Color.alpha(pixel);									// get the components of the pixel
		int red = Color.red(pixel);
		int green = Color.green(pixel);
		int blue = Color.blue(pixel);

		if (!getNVar()) return false;									// Alpha Var
		NumericVarValues.set(theValueIndex, (double) alpha);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;									// Red Var
		NumericVarValues.set(theValueIndex, (double) red);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;									// Blue Green
		NumericVarValues.set(theValueIndex, (double) green);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;									// Green Blue
		NumericVarValues.set(theValueIndex, (double) blue);
		if (!checkEOL()) return false;

		return true;
	}

	private boolean writeBitmapToFile(Bitmap b, String fn, int quality) {
		CompressFormat format = CompressFormat.PNG;						// Assume png
		String tFN = fn.toUpperCase(Locale.getDefault());				// temp convert fn to upper case
		if (tFN.endsWith(".JPG")) format = CompressFormat.JPEG;			// Test jpg
		else if (!tFN.endsWith(".PNG")) fn = fn + ".png";				// Test png

		File file = new File(Basic.getDataPath(fn));					// build full path
		FileOutputStream ostream = null;

		try {															// Write the file
			file.createNewFile();
			ostream = new FileOutputStream(file);

			b.compress(format, quality, ostream);						// Write png or jpg
			ostream.close();
		} 
		catch (Exception e) {
			closeStream(ostream, null);
			return RunTimeError(e);
		}
		return true;
	}

	private boolean execute_gr_save(){

		if (!getStringArg()) return false;							// Get the filename
		String fn = StringConstant;

			int quality = 50;											// set default jpeg quality
			if (isNext(','))											// if there is an optional quality parm
			{
				if (!evalNumericExpression()) return false;				// evaluate it
				quality = EvalNumericExpressionValue.intValue();
				if (quality < 0 || quality > 100) {
					return RunTimeError("Quality must be between 0 and 100");
				}
			}
			if (!checkEOL()) return false;

			boolean retval = true;
			Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
			if (b == null) {
				RunTimeError("Problem creating bitmap");
				retval = false;
			} else {

				retval = writeBitmapToFile(b, fn, quality);

				b.recycle();											// clean up bitmap
				b = null;
			}
			GR.drawView.destroyDrawingCache();							// clean up DrawingCache
			System.gc();
			return retval;
	}

	private boolean  execute_screen_to_bitmap(){
		if (!getNVar()) return false;
		if (!checkEOL()) return false;
		boolean retval = true;
		Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
		if (b == null) {
			return RunTimeError("Could not capture screen bitmap. Sorry.");
		} else {

			  NumericVarValues.set(theValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
			  BitmapList.add(b.copy(Bitmap.Config.ARGB_8888 , true));			
			  retval = true;

			  b.recycle();												// clean up bitmap
			  b = null;
		  }
		  GR.drawView.destroyDrawingCache();							// clean up DrawingCache
		  System.gc();
		  return retval;
	}

	private boolean execute_bitmap_save(){

		if (!evalNumericExpression()) return false;			// Get Source Bitmap
		int q = EvalNumericExpressionValue.intValue();
		if (q < 1 | q >= BitmapList.size()) {
			return RunTimeError("Invalid Bitmap Pointer");
		}

		Bitmap SrcBitMap = BitmapList.get(q);
		if (SrcBitMap == null) {
			return RunTimeError("Bitmap was deleted");
		}
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;					// Get the filename
		String fn = StringConstant;

		int quality = 50;									// set default jpeg quality
		if (isNext(',')) {									// if there is an optional quality parm
			if (!evalNumericExpression()) return false;		// evaluate it
			quality = EvalNumericExpressionValue.intValue();
			if (quality < 0 || quality > 100) {
				return RunTimeError("Quality must be between 0 and 100");
			}
		}
		if (!checkEOL()) return false;

			boolean retval = writeBitmapToFile(SrcBitMap, fn, quality);

		    SrcBitMap = null;
		    System.gc();
			return retval;
	}

	private boolean execute_gr_scale(){

		if (!evalNumericExpression()) return false;								// Get x
		double x = EvalNumericExpressionValue;

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;								// Get y
		double y = EvalNumericExpressionValue;
		if (!checkEOL()) return false;

		GR.scaleX = (float) x;
		GR.scaleY = (float) y;

		return true;
	}

	private boolean execute_gr_clip(){

		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dClip);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;							// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get left
		aBundle.putInt("left", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get top
		aBundle.putInt("top", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get right
		aBundle.putInt("right", EvalNumericExpressionValue.intValue());
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;							// Get bottom
		aBundle.putInt("bottom", EvalNumericExpressionValue.intValue());

		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			int RegionOp = EvalNumericExpressionValue.intValue();
			if (RegionOp < 0 || RegionOp > 5) {
				return RunTimeError("Region Operator not 0 to 5");
			}
			aBundle.putInt("RO", RegionOp);
		}
		if (!checkEOL()) return false;

		  int p = PaintList.size();						// Set current paint as this circle's paint
		  aBundle.putInt("paint", p-1);
		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		  DisplayListAdd(aBundle);
		  return true;
	}

	private boolean execute_gr_poly(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dPoly);
		  aBundle.putInt("hide", 0);

		if (!getNVar()) return false;							// Graphic Object Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;
		int theListIndex = EvalNumericExpressionValue.intValue();
		if (theListIndex < 1 || theListIndex >= theLists.size()) {
			return RunTimeError("Invalid list pointer");
		}
		if (theListsType.get(theListIndex) != list_is_numeric){
			return RunTimeError("List must be numeric");
		}

		ArrayList<Double> thisList = theLists.get(theListIndex);
		if (thisList.size() < 6) {
			return RunTimeError("List must have at least three points");
		}

		int r = thisList.size() % 2;
		if (r != 0) {
			return RunTimeError("List must have even number of elements");
		}
		aBundle.putInt("list", theListIndex);

		int x = 0;
		int y = 0;
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			x = EvalNumericExpressionValue.intValue();
			if (!isNext(',')) return false;
			if (!evalNumericExpression()) return false;
			y = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL()) return false;

	  		aBundle.putInt("x", x);
	  		aBundle.putInt("y", y);


		   int p = PaintList.size();						// Set current paint as this circle's paint
		   aBundle.putInt("paint", p-1);
		   NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		   DisplayListAdd(aBundle);

		  return true;
	}

	@SuppressLint("NewApi")										// Uses value from API 9
	private static int getNumberOfCameras() {

			int cameraCount;
			int level = Build.VERSION.SDK_INT;
			if (level < 9) {											// if SDK < 9 there can be only one camera
				Camera tCamera = Camera.open();							// Check to see if there is any camera at all
				cameraCount = (tCamera == null) ? 0 : 1;
				tCamera.release();
			} else {
				cameraCount = Camera.getNumberOfCameras();				// May be more than one camera
			}
			return cameraCount;
	}

	@SuppressLint("NewApi")										// Uses value from API 9
	private boolean execute_gr_camera_select(){

		if (NumberOfCameras < 0) { NumberOfCameras = getNumberOfCameras(); }
		if (NumberOfCameras == 0) { return RunTimeError("This device does not have a camera."); }

		int level = Build.VERSION.SDK_INT;
		if (level < 9) {											// if SDK < 9 there can be only one camera
			CameraNumber = -1;										// so no selection is possible
			return (NumberOfCameras != 0);
		}

		int BACK = 1;
		int FRONT = 2;

		if (!evalNumericExpression()) { return false; }				// Get user's choice
		if (!checkEOL()) { return false; }

		int choice = EvalNumericExpressionValue.intValue();
		if (choice != BACK && choice != FRONT) {
			return RunTimeError("Select value must be 1 (Back) or 2 (Front).");
		}

		Camera.CameraInfo CI = new Camera.CameraInfo();				// Determine which camera number is BACK
		Camera.getCameraInfo(0, CI);								// Assume 0 is BACK
		boolean zero_is_back = (CI.facing == CameraInfo.CAMERA_FACING_BACK);

		if (NumberOfCameras == 1) {									// Camera 0 is the only camera
			CameraNumber = 0;
			if ((choice == BACK) && !zero_is_back) { return RunTimeError("Device has no back camera"); }
			if ((choice == FRONT) && zero_is_back) { return RunTimeError("Device has no front camera"); }
		} else {
			CameraNumber = ((choice == BACK) == zero_is_back) ? 0 : 1;
		}
		return true;
	}

	private boolean execute_camera_shoot(int pictureMode){

		if (NumberOfCameras < 0) { NumberOfCameras = getNumberOfCameras(); }
		if (NumberOfCameras == 0) { return RunTimeError("This device does not have a camera."); }

		int flashMode = 0;
		int focusMode = 0;
		if (!getNVar()) return false;
		int saveValueIndex = theValueIndex;

		if (isNext(',')) {
			if (!evalNumericExpression()) { return false; }
			flashMode = EvalNumericExpressionValue.intValue();

			if (isNext(',')) {											// Focus/2013-07-25 gt
				if (!evalNumericExpression()) { return false; }
				focusMode = EvalNumericExpressionValue.intValue();
			}
		}
		if (!checkEOL()) { return false; };

		CameraBitmap = null;
		CameraDone = false;
		Intent cameraIntent = new Intent(this, CameraView.class);		// Start the Camera
		cameraIntent.putExtra(CameraView.EXTRA_PICTURE_MODE, pictureMode);
		cameraIntent.putExtra(CameraView.EXTRA_CAMERA_NUMBER, CameraNumber);
		cameraIntent.putExtra(CameraView.EXTRA_FLASH_MODE, flashMode);
		cameraIntent.putExtra(CameraView.EXTRA_FOCUS_MODE, focusMode);
		try {
			startActivityForResult(cameraIntent, BASIC_GENERAL_INTENT);
		} catch (Exception e) {
			return RunTimeError(e);
		}
		while (!CameraDone) Thread.yield();

			if (CameraBitmap != null) {
			   NumericVarValues.set(saveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
			   BitmapList.add(CameraBitmap);
			} else {
			   NumericVarValues.set(saveValueIndex, (double) 0); // Save the GR Object index into the var
			}
			CameraBitmap = null;
		    System.gc();

			return true;
	}

	private boolean execute_statusbar_show(){
		String[] msg = {
			"This command deprecated.",							// First line is base of errorMsg
			"To show status bar, use:",
			"gr.open alpha, red, green, blue, 1"
		};
		return RunTimeError(msg);
	}

	private boolean execute_brightness(){
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;
		double value = EvalNumericExpressionValue;
		if (value < 0.01) value = 0.01;
		if (value > 1.0) value = 1.0;
		GR.Brightness = (float) value;
		return true;
	}

	private boolean execute_gr_text_typeface(){
		if (!evalNumericExpression()) return false;							// Get type
		if (!checkEOL()) { return false; }
		int face = EvalNumericExpressionValue.intValue();

			int style = Typeface.NORMAL;	

			Typeface tf ;														// Interpret typeface
			if (face == 1)
				tf = Typeface.create(Typeface.DEFAULT, style);
			else if (face == 2)
				tf = Typeface.create(Typeface.MONOSPACE, style);
			else if (face == 3)
				tf = Typeface.create(Typeface.SANS_SERIF, style);
			else if (face == 4)
				tf = Typeface.create(Typeface.SERIF, style);
			else {
				return RunTimeError("Typface must be 1, 2, 3 or 4");
			}
			
			Paint tPaint = newPaint(aPaint);						// Put the typeface into Paint
			tPaint.setTypeface(tf);
			aPaint =  newPaint(tPaint);							// Copy the temp paint to aPaint
		    PaintListAdd(aPaint);								// Add the newPaint to the Paint List
			
		  return true;
	}

	private boolean execute_gr_touch_resume(){
		if (interruptResume == -1) {
			return RunTimeError("No onTouch Interrupt");
		}
		return doResume();
	}

	// ****************************************** Audio *******************************************

	private boolean executeAUDIO() {							// Get Audio command keyword if it is there
		return executeCommand(audio_cmd, "Audio");				// and execute the command
	}

	private MediaPlayer getMP(String fileName) {
		MediaPlayer mp = null;
		File file = new File(Basic.getDataPath(fileName));
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);								// Create Uri for the file
			if (uri != null) {
				mp = MediaPlayer.create(Basic.BasicContext, uri);		// Create a new Media Player
			}
		} else {														// the file does not exist
			if (Basic.isAPK) {											// we are in APK
				int resID = Basic.getRawResourceID(fileName);			// try to load the file from a raw resource
				if (resID != 0) {
					mp = MediaPlayer.create(Basic.BasicContext, resID);
				} else {												// try to load the file from assets
					AssetFileDescriptor afd = null;
					try {
						String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, fileName);
						afd = getAssets().openFd(assetPath);
						mp = new MediaPlayer();
						mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
						mp.prepare();
					} catch (IOException e) {
						if (mp != null) {
							mp.release();
							mp = null;
						}
					} finally {
						if (afd != null) { try { afd.close(); } catch (IOException e) { } }
					}
				}
			}
		}
		return mp;
	}

	private boolean execute_audio_load(){
		  /* If there is already an audio running,
		   * then stop it and 
		   * release its resources.
		   */

/*		  if (theMP != null){
			  try {theMP.stop();} catch (IllegalStateException e){}
			  theMP.release();
			  theMP = null;
		  }*/
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		AudioManager audioSM = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);

		if (!getNVar()) return false;									// Get the Player Number Var
		int saveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;								// Get the file path
		if (!checkEOL()) return false;

		String fileName = StringConstant;								// The filename as given by the user
		MediaPlayer aMP = getMP(fileName);

		if (aMP == null) { return RunTimeError(fileName + " Not Found at:"); }
		aMP.setAudioStreamType(AudioManager.STREAM_MUSIC);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		NumericVarValues.set(saveValueIndex, (double)theMPList.size());
		theMPList.add(aMP);
		theMPNameList.add(fileName);
		return true;
	}

	private boolean execute_audio_release(){

		if (!evalNumericExpression()) return false;
		int index = EvalNumericExpressionValue.intValue();
		if (index <= 0 || index >= theMPList.size()) {
			return RunTimeError("Invalid Player List Value");
		}
		if (!checkEOL()) return false;

		MediaPlayer aMP = theMPList.get(index);
		if (aMP == null) return true;

		if (theMP == aMP) { return RunTimeError("Must stop player before releasing"); }

		aMP.release();
		theMPList.set(index, null);

		return true;
	}

	private boolean execute_audio_play(){

		if (!evalNumericExpression()) return false;
		int index = EvalNumericExpressionValue.intValue();
		if (index <= 0 || index >= theMPList.size()) {
			return RunTimeError("Invalid Player List Value");
		}
		if (!checkEOL()) return false;

		MediaPlayer aMP = theMPList.get(index);
		if (aMP == null) { return RunTimeError("Audio not loaded at:"); }
		if (theMP != null) { return RunTimeError("Stop Current Audio Before Starting New Audio"); }

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		Log.v(LOGTAG, CLASSTAG + " play " + aMP);

		try { aMP.prepare(); } catch (Exception e) { }
		aMP.start();

		if (!aMP.isPlaying()) {								// Somehow lost the player. Make a new one.
			aMP.release();
			aMP = getMP(theMPNameList.get(index));
			if (aMP == null) {
				RunTimeError("Media player synchronous problem.");
				return true;								// Is this correct?
			}
			theMPList.set(index, aMP);
			aMP.start();
		}

		aMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				PlayIsDone = true;
			}
		});
//		Log.v(LOGTAG, CLASSTAG + " is playing " + theMP.isPlaying());
		PlayIsDone = false;
		theMP = aMP;

		return true;
	}

	private boolean execute_audio_isdone(){
		if (!getNVar()) return false;	
		if (!checkEOL()) return false;

		if (theMP == null) {
			PlayIsDone = true;
		}
		double d = (PlayIsDone) ? 1 : 0;
		NumericVarValues.set(theValueIndex, d);

		return true;
	}

	private boolean execute_audio_loop(){
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!checkEOL()) return false;

		theMP.setLooping(true);
		return true;
	}

	private boolean execute_audio_stop(){
		if (!checkEOL()) return false;
		if (theMP == null) return true;								// if theMP is null, Media player has stopped

//		MediaPlayer.setOnSeekCompleteListener(mSeekListener);
//		try {Thread.sleep(1000);}catch(InterruptedException e){}
		try { theMP.stop(); }
		catch (Exception e) { return RunTimeError(e); }
		finally { theMP = null; }									// Signal MP stopped

		return true;
	}

	private boolean execute_audio_pause(){
		if (!checkEOL()) return false;
		if (theMP == null) return true;								// if theMP is null, Media player has stopped

		try { theMP.pause(); }
		catch (Exception e) { return RunTimeError(e); }
		finally { theMP = null; }									// Signal MP stopped

		return true;
	}

	private boolean execute_audio_volume(){
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!evalNumericExpression()) return false;
		float left = EvalNumericExpressionValue.floatValue();

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;
		float right = EvalNumericExpressionValue.floatValue();
		if (!checkEOL()) return false;

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		theMP.setVolume(left, right);

		return true;
	}

	private boolean execute_audio_pcurrent(){
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		NumericVarValues.set(theValueIndex, (double)theMP.getCurrentPosition());

		return true;
	}

	private boolean execute_audio_pseek(){
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		int pos = EvalNumericExpressionValue.intValue();
		theMP.seekTo(pos);

		return true;
	}

	private boolean execute_audio_length(){
		if (!getNVar()) return false;								// Get the Player Number Var
		int saveValueIndex = theValueIndex;

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;
		int index = EvalNumericExpressionValue.intValue();
		if (index <= 0 || index >= theMPList.size()) {
			return RunTimeError("Invalid Player List Value");
		}
		if (!checkEOL()) return false;

		MediaPlayer aMP = theMPList.get(index);
		if (aMP == null) {
			return RunTimeError("Audio not loaded at:");
		}

		int length = aMP.getDuration();
		NumericVarValues.set(saveValueIndex, (double)length);

		return true;
	}

	private boolean execute_audio_record_start(){
		if (!getStringArg()) return false;
		String recordFileName = Basic.getDataPath(StringConstant);

		int source = 0;									// Get optional source
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			source = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL()) return false;

		  try {
	        mRecorder = new MediaRecorder();
	        switch (source)
	        {
	        case 0:
	        	mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
	        	break;
	        case 1:
	        	mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
	        	break;
	        case 2:
	        	mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
	        	break;
	        case 3:
	        	mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK);
	        	break;
	        case 4:
	        	mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_UPLINK);
	        	break;
	        }
	        	
	        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
	        mRecorder.setOutputFile(recordFileName);
	        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
	        	        
	        mRecorder.prepare();
		    mRecorder.start();

	        } catch (Exception e) {
	            RunTimeError("Audio record error: " + e);
	            return false;
	        }

	        
	        return true;

	  }

	private boolean execute_audio_record_stop() {
		return checkEOL() && audioRecordStop();
	}

	private boolean audioRecordStop() {
		if (mRecorder == null) return true;
		try {
			mRecorder.stop();
			mRecorder.release();
		} catch (Exception e){}
		mRecorder = null;

		return true;
	}

	// ************************************* Sensors Package **************************************

	private boolean executeSENSORS() {							// Get Sensor command keyword if it is there
		return executeCommand(sensors_cmd, "Sensors");			// and execute the command
	}

		private boolean execute_sensors_list(){
			String var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
			if (var == null) { return false; }							// must name a new string array variable
			if (!checkEOL()) { return false; }							// line must end with ']'

			if (theSensors == null) {
				theSensors = new SensorActivity(this);
			}
			ArrayList<String> census = theSensors.takeCensus();
			int nSensors = census.size();						// If no sensors reported.....
			if (nSensors ==0 ) {
				return RunTimeError("This device reports no Sensors");
			}

			/* Puts the list of sensors into a new array */
			return ListToBasicStringArray(var, census, nSensors);
		}

		private boolean execute_sensors_open(){
			if (theSensors == null) {
				theSensors = new SensorActivity(this);
			}

			if (isEOL()) { return false; }
			boolean toOpen = false;								// anything to open?
			do {												// get the user's list of sensors to open
				if (!evalNumericExpression()) { return false; }	// get the sensor number
				int s = EvalNumericExpressionValue.intValue();
				int d = SensorManager.SENSOR_DELAY_NORMAL;
				if (isNext(':')) {
					if (!evalNumericExpression()) { return false; }	// get the sensor delay value
					d = EvalNumericExpressionValue.intValue();
				}
				toOpen |= theSensors.markForOpen(s, d);			// record selection
			} while (isNext(','));
			if (!checkEOL()) { return false; }

			if (toOpen) {										// if any valid selections
				theSensors.doOpen();							// open selected sensors
			}
			return true;
		}

		private boolean execute_sensors_read(){

			if (theSensors == null) {
				return RunTimeError("Sensors not opened at:");
			}

			if (!evalNumericExpression()) return false;			// Get Sensor Type
			int type = EvalNumericExpressionValue.intValue();
			if (!isNext(',')) return false;

			if (type < 0 || type > SensorActivity.MaxSensors) {
				return RunTimeError("Sensor type not 0 to " + SensorActivity.MaxSensors);
			}

			int[] valueIndex = new int[4];
			if (!getNVar()) return false;						// Sensor Variable
			valueIndex[1] = theValueIndex;
			if (!isNext(',')) return false;

			if (!getNVar()) return false;						// Sensor Variable
			valueIndex[2] = theValueIndex;
			if (!isNext(',')) return false;

			if (!getNVar()) return false;						// Sensor Variable
			valueIndex[3] = theValueIndex;
			if (!checkEOL()) return false;

			double[] SensorValues = theSensors.getValues(type);
			for (int i = 1; i <= 3; ++i) {
				NumericVarValues.set(valueIndex[i], SensorValues[i]); 
			}
			return true;
		}

	private boolean execute_sensors_rotate(){

		/* This is a test. 
		 * It has failed so far
		 * This command has not been exposed to the users.
		 * Someday....?
		 */

		float mOrientation[] = new float[3];
		double SensorValues[];

		SensorValues = theSensors.getValues(1);
		float g[] = new float[3];
		g[0] = (float) SensorValues[1];
		g[1] = (float) SensorValues[2];
		g[2] = (float) SensorValues[3];

		SensorValues = theSensors.getValues(1);
		float m[] = new float[3];
		m[0] = (float) SensorValues[1];
		m[1] = (float) SensorValues[2];
		m[2] = (float) SensorValues[3];

		float r[] = new float[16];
		float R[] = new float[16];
		float i[] = new float[16];

		SensorManager.getRotationMatrix (r, i, g, m);
/*		SensorManager.remapCoordinateSystem(R,
				SensorManager.AXIS_X,
				SensorManager.AXIS_Z, r);*/

		SensorManager.getOrientation(r, mOrientation);

		final float rad2deg = (float)(180.0f/Math.PI);

		if (!getNVar()) return false;
		NumericVarValues.set(theValueIndex, (double) mOrientation[0]);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;
		NumericVarValues.set(theValueIndex, (double) mOrientation[1]);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;
		NumericVarValues.set(theValueIndex, (double) mOrientation[2]);
		if (!checkEOL()) return false;

		return true;
	}

		private boolean execute_sensors_close(){
			if (!checkEOL()) return false;
			if (theSensors != null) {
				theSensors.stop();
				theSensors = null;								// Tell everyone that Sensors are closed
			}
			return true;
		}

	// *************************************** GPS Package ****************************************

	private boolean executeGPS() {
		Command c = findCommand(GPS_cmd, "GPS");
		if (c != null) {
			if ((theGPS == null) && (c.id != CID_OPEN)) {
				return RunTimeError("GPS not opened at:");
			}
			return c.run();
		}
		return false;
	}

	public boolean execute_gps_open() {
		if (!checkEOL()) return false;
		if (theGPS != null) return true;					// already opened

		theGPS = new GPS(this);								// start GPS
		return true;
	}

	public boolean execute_gps_close(){
		if (!checkEOL()) return false;
		if (theGPS != null) {
			Log.d(LOGTAG, "Stopping GPS on command");
			theGPS.stop();									// close GPS
			theGPS = null;
		}
		return true;
	}

	private boolean execute_gps_altitude(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex, GPS.Altitude);		// Set value into variable
		return true;
	}

	private boolean execute_gps_latitude(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex, GPS.Latitude);
		return true;
	}

	private boolean execute_gps_longitude(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex, GPS.Longitude);
		return true;
	}

	private boolean execute_gps_bearing(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex, (double) GPS.Bearing);
		return true;
	}

	private boolean execute_gps_accuracy(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex, (double) GPS.Accuracy);
		return true;
	}

	private boolean execute_gps_speed(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex,(double) GPS.Speed);
		return true;
	}

	private boolean execute_gps_time(){
		if (!getNVar()) return false;							// Sensor Variable
		if (!checkEOL()) return false;
		NumericVarValues.set(theValueIndex,(double) GPS.Time);
		return true;
	}

	private boolean execute_gps_provider(){
		if (!getSVar()) return false;
		if (!checkEOL()) return false;
		String provider = GPS.Provider;
		if (provider == null) { provider = ""; }
		StringVarValues.set(theValueIndex, provider);
		return true;
	}

	// ************************************* Array Package ****************************************

	private boolean executeARRAY() {							// Get array command keyword if it is there
		return executeCommand(array_cmd, "Array");				// and execute the command
	}

	private boolean execute_array_length(){

		if (!getNVar()) { return false; }							// Length Variable
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (getArrayVarForRead() == null) { return false; }			// Get the array variable
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;							// Get values inside [], if any
		if (!checkEOL()) { return false; }							// line must end with ']'

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int length = p[1].intValue();

		NumericVarValues.set(SaveValueIndex, (double) length);		// Set the length into the var value

		return true;
	}

	private boolean execute_array_load(){
		String var = getArrayVarForWrite();							// get the result array variable
		if (var == null) { return false; }							// must name a new array variable
		if (!isNext(',')) { return false; }							// Comma before the list

		if (VarIsNumeric) {											// If the array is numeric
			ArrayList<Double> Values = new ArrayList<Double>();		// Create a list for the numeric values
			if (!LoadNumericList(Values)) return false;				// load numeric list
			if (!checkEOL()) return false;
			return ListToBasicNumericArray(var, Values, Values.size()); // Copy the list to a BASIC! array
		} else {
			ArrayList<String> Values = new ArrayList<String>();		// Create a list for the numeric values
			if (!LoadStringList(Values)) return false;				// load string list
			if (!checkEOL()) return false;
			return ListToBasicStringArray(var, Values, Values.size()); // Copy the list to a BASIC! array
		}
	}
	
	private boolean LoadNumericList(ArrayList <Double> Values){
		while (true) {											// loop through the list
			if (!evalNumericExpression()) return false;			// values may be expressions
			Values.add(EvalNumericExpressionValue);

			if (LineIndex >= ExecutingLineBuffer.length()) return false;
			char c = ExecutingLineBuffer.charAt(LineIndex);		// get the next character
			if (c == ',') {										// if it is a comma
				++LineIndex;									// skip it and continue looping
			} else if (c == '~') {								// if it is a line continue character
				if (!nextLine()) return false;					// get next line if there is one
			} else break;										// else no more values in the list
		}
		return true;
	}

	private boolean LoadStringList(ArrayList <String> Values){
		while (true) {											// loop through the list
			if (!getStringArg()) return false;					// values may be expressions
			Values.add(StringConstant);

			if (LineIndex >= ExecutingLineBuffer.length()) return false;
			char c = ExecutingLineBuffer.charAt(LineIndex);		// get the next character
			if (c == ',') {										// if it is a comma
				++LineIndex;									// skip it and continue looping
			} else if (c == '~') {								// if it is a line continue character
				if (!nextLine()) return false;					// get next line if there is one
			} else break;										// else no more values in the list
		}
		return true;
	}

	private boolean execute_array_collection(ArrayOrderOps op) {

		// This method implements several array commands

		if (getArrayVarForRead() == null) { return false; }				// Get the array variable
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;								// Get values inside [], if any
		if (!checkEOL()) { return false; }								// line must end with ']'

		if (!getArraySegment(arrayTableIndex, p)) { return false; }		// Get array base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		if (VarIsNumeric) {											// Numeric Array
			ArrayList <Double> Values = new ArrayList<Double>();	// Create a list to copy array values into

			for (int i = 0; i < length; ++i) {						// Copy the array values into that list
				Values.add(NumericVarValues.get(base+i));
			}
			switch (op) {											// Execute the command specific procedure
				case DoReverse:		Collections.reverse(Values);	break;
				case DoSort:		Collections.sort(Values);		break;
				case DoShuffle:		Collections.shuffle(Values);	break;
			}
			for (int i = 0; i < length; ++i) {						// Copy the results back to the array
				NumericVarValues.set(base + i, Values.get(i));
			}

		} else {													// Do the same stuff for a string array
			ArrayList<String> Values = new ArrayList<String>();
			for (int i = 0; i < length; ++i) {
				Values.add(StringVarValues.get(base + i));
			}
			switch (op) {											// Execute the command specific procedure
				case DoReverse:		Collections.reverse(Values);	break;
				case DoSort:		Collections.sort(Values);		break;
				case DoShuffle:		Collections.shuffle(Values);	break;
			}
			for (int i = 0; i < length; ++i) {
				StringVarValues.set(base + i, Values.get(i));
			}
		}

		return true;
	}

	private boolean execute_array_sum(ArrayMathOps op) {

		if (!getNVar()) { return false; }							// The value return variable
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (getArrayVarForRead() == null) { return false; }			// Get the array variable
		if (!VarIsNumeric) { return RunTimeError(EXPECT_NUM_ARRAY); }
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;							// Get values inside [], if any
		if (!checkEOL()) { return false; }							// line must end with ']'

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		double Sum = 0;
		double Min = NumericVarValues.get(base);
		double Max = NumericVarValues.get(base);

		for (int i = 0; i < length; ++i){							// Loop through the array values
			double d = NumericVarValues.get(base+i);				// Pick up the elements value
			Sum = Sum + d;											// build the Sum
			if (d < Min) { Min = d; }								// find the minimum value
			if (d > Max) { Max = d; }								// and the maxium value
		}
		double Average = Sum / length;								// Calculate the average

		switch (op) {												// Set the return value according to the command
			case DoAverage:	NumericVarValues.set(SaveValueIndex, Average);	break;
			case DoSum:		NumericVarValues.set(SaveValueIndex, Sum);		break;
			case DoMax:		NumericVarValues.set(SaveValueIndex, Max);		break;
			case DoMin:		NumericVarValues.set(SaveValueIndex, Min);		break;
			case DoVariance:
			case DoStdDev:
				double T = 0;
				double W = 0;
				for (int i = 0; i <length; ++i) {
					double d = NumericVarValues.get(base + i);		// Pick up the elements value
					W = d - Average;
					T = T + W*W;
				}
				double variance = T/(length-1);
				if (op == ArrayMathOps.DoVariance) {
					NumericVarValues.set(SaveValueIndex, variance);
				} else {											// DoStdDev
					double sd = Math.sqrt(variance);
					NumericVarValues.set(SaveValueIndex, sd);
				}
				break;
		}

		return true;
	}

	private boolean execute_array_copy(){
															// **** Source Array ****

		if (getVarAndType() == null)			{ return false; }	// get the array variable
		if (!VarIsArray)						{ return RunTimeError("Source not array"); }
		if (VarIsNew) 							{ return RunTimeError("Source array not DIMed"); }
		boolean SourceArrayNumeric = VarIsNumeric;
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p))					{ return false; }	// get values inside [], if any

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int SourceBase = p[0].intValue();
		int SourceLength = p[1].intValue();

		if (!isNext(','))						{ return false; }
															// *** Destination Array ***

		String destVar = getVarAndType();							// Get the array variable
		if (destVar == null)					{ return false; }
		if (!VarIsArray)						{ return RunTimeError("Destination not array"); }
		if (SourceArrayNumeric != VarIsNumeric)	{ return RunTimeError("Arrays not of same type"); }
		boolean destIsNew = VarIsNew;
		int destVarNumber = VarNumber;								// Not valid if destIsNew!

		int extras = 0;												// Get the extras parameter
		if (!isNext(']')) {
			if (!evalNumericExpression())		{ return false; }
			if (!isNext(']'))					{ return false; }
			extras = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL())						{ return false; }

		int destStart = 0;
		int totalLength = 0;
		if (destIsNew) {											// copy to new array, optional extras arg adds element(s)

			if (extras == 0) {										// check for cases that would create empty array
				if (SourceLength < 1) { return RunTimeError("Source array [Start,Length] must specify at least one element"); }
			}
			totalLength = SourceLength + Math.abs(extras);			// go build a new array of the proper size and type
			if (!BuildBasicArray(destVar, SourceArrayNumeric, totalLength)) { return false; }
			destStart = ArrayValueStart;
			if (extras < 0) { destStart -= extras; }				// if negative extras, add absolute value to start index

		} else {													// copy over old array, optional extras arg is start index
			if (SourceLength < 1)				{ return true; }	// nothing to do

			Bundle ArrayEntry = ArrayTable.get(VarIndex.get(destVarNumber)); // Get the array table bundle for this array
			int destBase = ArrayEntry.getInt("base");				// and the start of the array in the variable space
			int destLength = ArrayEntry.getInt("length");			// get the destination array length
			if (extras > destLength)			{ return true; }	// dest start is past end of array, nothing to do

			if (--extras < 0) { extras = 0; }						// convert to 0-based index, ignore if less than 1
			if (extras + SourceLength > destLength) {				// start index + length to copy > space available
				SourceLength = destLength - extras;					// limit copy
			} else {
				destLength = extras + SourceLength;
				// ArrayEntry.putInt("length", destLength);			// shorten destination array
			}
			destStart = destBase + extras;
			totalLength = destLength - extras;
		}

		if (SourceArrayNumeric) {									// Do numeric array
			for (int i = 0; i < SourceLength; ++i) {				// Copy the source array values
				NumericVarValues.set(destStart++, NumericVarValues.get(SourceBase + i));
			}
		} else {													// Do String array
			for (int i = 0; i < SourceLength; ++i) {				// Copy the source array values
				StringVarValues.set(destStart++, StringVarValues.get(SourceBase + i));
			}
		}
		return true;
	}

	private boolean execute_array_search(){
		if (getArrayVarForRead() == null) { return false; }			// Get the array variable
		boolean isNumeric = VarIsNumeric;
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;							// Get values inside [], if any

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		if (!isNext(',')) return false;								// move to the value

		int found = -1;
		int savedVarIndex = 0;
		int start = 0;

		if (!isNumeric) {											// String type array
			if (!getStringArg()) return false;						// Get the string to search for
			String sfind = StringConstant;

			if (!isNext(',')) return false;							// move to the result var
			if (!getNVar()) return false;
			savedVarIndex = theValueIndex;

			if (isNext(',')) {										// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL()) return false;

			for (int i = start; i < length; ++i) {					// Search the list for a match
				if (sfind.equals(StringVarValues.get(base + i))) {
					found = i;
					break;
				}
			}
		} else {													// Numeric type array
			if (!evalNumericExpression()) return false;				// Get the value to search for
			double nfind = EvalNumericExpressionValue;

			if (!isNext(',')) return false;							// move to the result var
			if (!getNVar()) return false;
			savedVarIndex = theValueIndex;

			if (isNext(',')) {										// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL()) return false;

			for (int i = start; i < length; ++i) {					// Search the list for a match
				if (nfind == NumericVarValues.get(base + i)) {
					found = i;
					break;
				}
			}
		}

		++found;// Found is ones based
		NumericVarValues.set(savedVarIndex, (double)found);

		return true;
	}

	// *************************************** List Package ***************************************

	private boolean executeLIST() {								// Get list command keyword if it is there
		return executeCommand(list_cmd, "List");				// and execute the command
	}

	private boolean execute_LIST_NEW() {
		char c = ExecutingLineBuffer.charAt(LineIndex);					// Get the type, s or n
		++LineIndex;
		int type = 0;
		
		if (c == 'n' ){
			type = list_is_numeric;
		}
		else if (c == 's'){
			type = list_is_string;
		} else return false;

		if (!isNext(',')) return false;

		if (!getNVar()) return false;									// List pointer variable
		int SaveValueIndex = theValueIndex;

		if (!checkEOL()) return false;

		int theIndex = createNewList(type);								// Try to create list
		if (theIndex < 0) return false;									// Create failed

		NumericVarValues.set(SaveValueIndex, (double) theIndex);		// Return the list pointer
		return true;
	}

	private int createNewList(int type) {						// Put a new ArrayList in global theLists
																// Put its type in global theListsType 
		int index = theLists.size();
		if (type == list_is_string) {									// Create a string list
			theLists.add(new ArrayList <String>());
		} else if (type == list_is_numeric) {							// Create a numeric list
			theLists.add(new ArrayList <Double>());
		} else {
			return -1;													// Unknown type, don't create anything
		}
		theListsType.add(type);											// Add the type
		return index;
	}

	private int getListArg() {											// Get the List pointer
		return getListArg("Invalid List Pointer");
	}

	private int getListArg(String errorMsg) {							// Get the List pointer
		if (evalNumericExpression()) {
			int listIndex = EvalNumericExpressionValue.intValue();
			if ((listIndex > 0) && (listIndex < theLists.size())) {
				return listIndex;
			}
			RunTimeError(errorMsg);
		}
		return 0;
	}

	private boolean execute_LIST_ADDARRAY(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;

		if (!isNext(',')) return false;
		if (getArrayVarForRead() == null) return false;				// Get the array variable
		int arrayTableIndex = VarIndex.get(VarNumber);

		Integer[] p = { null, null };
		if (!getIndexPair(p)) return false;							// Get values inside [], if any
		if (!checkEOL()) return false;

		boolean isListNumeric = (theListsType.get(listIndex) == list_is_numeric);
		if (isListNumeric != VarIsNumeric) { return RunTimeError("Type mismatch"); }

		if (!getArraySegment(arrayTableIndex, p)) { return false; }	// Get array base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		ArrayList destList = theLists.get(listIndex);
		ArrayList sourceList = (isListNumeric) ? NumericVarValues : StringVarValues;
		for (int i = 0; i < length; ++i ) {							// Copy array to list
			destList.add(sourceList.get(base + i));
		}
		return true;
	}

	private boolean execute_LIST_ADDLIST(){
		int destListIndex = getListArg("Invalid Destination List Pointer");	// Get the destination list pointer
		if (destListIndex == 0) return false;
		if (!isNext(',')) return false;

		int sourceListIndex = getListArg("Invalid Source List Pointer");	// Get the source list pointer
		if (sourceListIndex == 0) return false;
		if (!checkEOL()) return false;

		boolean isDestNumeric = (theListsType.get(destListIndex) == list_is_numeric);
		boolean isSourceNumeric = (theListsType.get(sourceListIndex) == list_is_numeric);
		if (isDestNumeric != isSourceNumeric) { return RunTimeError("Type mismatch"); }

		theLists.get(destListIndex).addAll(theLists.get(sourceListIndex));
		return true;
	}

	private boolean execute_LIST_SEARCH(){
		int listIndex = getListArg();									// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;									// move to the value

		int found = -1;
		int savedVarIndex = 0;
		int start = 0;

		switch (theListsType.get(listIndex))
		{
		case list_is_string:											 // String type list
			ArrayList<String> SValues = theLists.get(listIndex);		 // Get the string list
			if (!getStringArg()) return false;							 // values may be expressions
			String sfind = StringConstant;

			if (!isNext(',')) return false;								 // move to the result var
			if (!getNVar()) return false;
			savedVarIndex = theValueIndex;

			if (isNext(',')) {											// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL()) return false;

			for (int i = start; i < SValues.size(); ++i){			// Search the list for a match
				if (sfind.equals(SValues.get(i))){
					found = i;
					break;
				}
			}
			break;

		case list_is_numeric:
			ArrayList<Double> NValues = theLists.get(listIndex);	// Get the numeric list
			if (!evalNumericExpression()) return false;             // values may be expressions
			double nfind = EvalNumericExpressionValue;

			if (!isNext(',')) return false;								// move to the result var
			if (!getNVar()) return false;
			savedVarIndex = theValueIndex;

			if (isNext(',')) {											// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL()) return false;

			for (int i = start; i < NValues.size(); ++i){           // Search the list for a match
				if (nfind == (NValues.get(i))){
					found = i;
					break;
				}
			}
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}

		++found;// Found is ones based

		NumericVarValues.set(savedVarIndex, (double) found);
		return true;
	}

	private boolean execute_LIST_ADD(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;								// move to the result value

		boolean isListNumeric = (theListsType.get(listIndex) == list_is_numeric);
		if (isListNumeric) {
			ArrayList<Double> Values = theLists.get(listIndex);		// Get the numeric list
			if (!LoadNumericList(Values)) return false;				// load numeric list
		} else {
			ArrayList<String> Values = theLists.get(listIndex);		// Get the string list
			if (!LoadStringList(Values)) return false;				// load string list
		}
		return checkEOL();
	}

	private boolean execute_LIST_SET(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get the index to get
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(',')) return false;

		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			if (!evalStringExpression()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){		
				return RunTimeError("Index out of bounds");
			}
			thisStringList.set(getIndex, StringConstant);
			break;

		case list_is_numeric:												// Number
			if (!evalNumericExpression()){
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()){
				return RunTimeError("Index out of bounds");
			}
			thisNumericList.set(getIndex, EvalNumericExpressionValue);
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}

		return true;
	}

	private boolean execute_LIST_GET(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get the index to get
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(',')) return false;
		if (!getVar()) return false;								// Get the return value variable
		if (!checkEOL()) return false;

		int listType = theListsType.get(listIndex);					// Get this lists type
		boolean isListNumeric = (listType == list_is_numeric);
		if (isListNumeric != VarIsNumeric) { return RunTimeError("Type mismatch"); }

		switch (listType)
		{
		case list_is_string:										// String
			ArrayList<String> thisStringList = theLists.get(listIndex);		// Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){
				return RunTimeError("Index out of bounds");
			}
			String thisString = thisStringList.get(getIndex);				// Get the requested string
			StringVarValues.set(theValueIndex, thisString);
			break;

		case list_is_numeric:										// Number
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	// Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()){
				return RunTimeError("Index out of bounds");
			}
			Double thisNumber = thisNumericList.get(getIndex);				// Get the requested number
			NumericVarValues.set(theValueIndex, thisNumber);
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}
		
		return true;
	}

	private boolean execute_LIST_GETTYPE(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;

		if (!getSVar()) return false;
		if (!checkEOL()) return false;
		
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			StringVarValues.set(theValueIndex, "S");
			break;

		case list_is_numeric:										// Number
			StringVarValues.set(theValueIndex, "N");
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}
		return true;
	}

	private boolean execute_LIST_CLEAR(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!checkEOL()) return false;
		theLists.get(listIndex).clear();
		return true;
	}

	private boolean execute_LIST_REMOVE(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get the index to remove
		if (!checkEOL()) return false;

		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		ArrayList theList = theLists.get(listIndex);				// Get the  list
		if (getIndex < 0 || getIndex >= theList.size()) {
			return RunTimeError("Index out of bounds");
		}
		theList.remove(getIndex);
		return true;
	}

	private boolean execute_LIST_INSERT(){
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get the index insert at
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(',')) return false;

		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:											 // String
			if (!getStringArg()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;

			ArrayList<String> thisStringList = theLists.get(listIndex);	// Get the string list
			if (getIndex < 0 || getIndex > thisStringList.size()) {		// if index == size element goes at end of list
				return RunTimeError("Index out of bounds");
			}
			thisStringList.add(getIndex, StringConstant);
			break;

		case list_is_numeric:											// Number
			if (!evalNumericExpression()){
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;

			ArrayList<Double> thisNumericList = theLists.get(listIndex);	// Get the numeric list
			if (getIndex < 0 || getIndex > thisNumericList.size()) {	// if index == size element goes at end of list
				return RunTimeError("Index out of bounds");
			}
			thisNumericList.add(getIndex, EvalNumericExpressionValue);
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}

		return true;
	}

	private boolean execute_LIST_SIZE(){
		int listIndex = getListArg();									// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;									// move to the return var

		if (!getNVar()) return false;
		if (!checkEOL()) return false;
		
		int size = theLists.get(listIndex).size();
		NumericVarValues.set(theValueIndex, (double) size);

		return true;
	}

	private boolean execute_LIST_CONTAINS(){
		return false;
	}

	private boolean execute_LIST_TOARRAY(){
		int listIndex = getListArg();									// Get the list pointer
		if (listIndex == 0) return false;
		if (!isNext(',')) return false;									// move to the array var

		String var = getArrayVarForWrite();								// get the result array variable
		if (var == null) return false;									// must name a new array variable
		if (!checkEOL()) return false;									// line must end with ']'

		int listType = theListsType.get(listIndex);						// Get this lists type
		boolean isListNumeric = (listType == list_is_numeric);
		if (isListNumeric != VarIsNumeric) { return RunTimeError("Type mismatch"); }

		if (isListNumeric) {
			ArrayList<Double> Values = theLists.get(listIndex);			// Get the numeric list
			return ListToBasicNumericArray(var, Values, Values.size());	// Copy the list to a BASIC! array
		} else {
			ArrayList<String> Values = theLists.get(listIndex);			// Get the string list
			return ListToBasicStringArray(var, Values, Values.size());	// Copy the list to a BASIC! array
		}
	}

	// ************************************** Bundle Package **************************************

	private boolean executeBUNDLE() {							// Get bundle command keyword if it is there
		return executeCommand(bundle_cmd, "Bundle");			// and execute the command
	}

	private boolean execute_BUNDLE_CREATE() {

		if (!getNVar()) return false;							// Bundle pointer variable
		if (!checkEOL()) return false;
		int SaveValueIndex = theValueIndex;
		Bundle b = new Bundle();

		int bundleIndex = theBundles.size();
		theBundles.add(b);

		NumericVarValues.set(SaveValueIndex, (double) bundleIndex);

		return true;
	}

	private int getBundleArg() {										// Get the Bundle pointer
		if (evalNumericExpression()) {
			int bundleIndex = EvalNumericExpressionValue.intValue();
			if ((bundleIndex > 0) && (bundleIndex < theBundles.size())) {
				return bundleIndex;
			}
			RunTimeError("Invalid Bundle Pointer");
		}
		return 0;
	}

	private boolean execute_BUNDLE_PUT(){
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex == 0) return false;

		if (!isNext(',')) return false;									// move to the tag
		if (!getStringArg()) return false;
		String tag = StringConstant;

		if (!isNext(',')) return false;									// move to the value

		Bundle b = theBundles.get(bundleIndex);

		int LI = LineIndex;
		if (evalNumericExpression()) {
			if (!checkEOL()) return false;
			bundlePut(b, tag, EvalNumericExpressionValue);
		} else {
			LineIndex = LI;
			if (!getStringArg()) return false;
			if (!checkEOL()) return false;
			bundlePut(b, tag, StringConstant);
		}
		return true;
	}

	private void bundlePut(Bundle b, String key, Double value) {
		b.putDouble(key, value);
		b.putBoolean("@@@N." + key, true);
	}

	private void bundlePut(Bundle b, String key, String value) {
		b.putString(key, value);
		b.putBoolean("@@@N." + key, false);
	}

	private boolean execute_BUNDLE_GET(){
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex == 0) return false;

		if (!isNext(',')) return false;									// move to the tag
		if (!getStringArg()) return false;
		String tag = StringConstant;

		if (!isNext(',')) return false;									// move to the value
		if (!getVar()) return false;
		if (!checkEOL()) return false;

		Bundle b = theBundles.get(bundleIndex);
		if (!b.containsKey(tag)) {
			return RunTimeError(tag + " not in bundle");
		}

		boolean isNumeric = b.getBoolean("@@@N." + tag);
		if (isNumeric) {
			if (!VarIsNumeric) {
				return RunTimeError(tag + " is not a string");
			}
			NumericVarValues.set(theValueIndex, b.getDouble(tag));
		} else {
			if (VarIsNumeric) {
				return RunTimeError(tag + " is not numeric");
			}
			StringVarValues.set(theValueIndex, b.getString(tag));
		}
		return true;
	}

	private boolean execute_BUNDLE_TYPE(){
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex == 0) return false;

		if (!isNext(',')) return false;									// move to the tag
		if (!getStringArg()) return false;
		String tag = StringConstant;

		if (!isNext(',')) return false;									// move to the result var
		if (!getSVar()) return false;
		if (!checkEOL()) return false;

		Bundle b = theBundles.get(bundleIndex);
		if (!b.containsKey(tag)) {
			return RunTimeError(tag + " not in bundle");
		}

		boolean isNumeric = b.getBoolean("@@@N." + tag);
		String type = (isNumeric) ? "N" : "S";

		StringVarValues.set(theValueIndex, type);
		return true;
	}

	private boolean execute_BUNDLE_KEYSET(){
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex == 0) return false;

		if (!isNext(',')) return false;									// move to the list var
		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		 ArrayList<String> theStringList = new ArrayList <String>();
		 int theIndex = theLists.size();
		 theLists.add(theStringList);
		 theListsType.add(list_is_string);
		 NumericVarValues.set(theValueIndex, (double) theIndex);

		Bundle b = theBundles.get(bundleIndex);
		Set<String> set= b.keySet();

		for (String s : set) {
			if (!s.startsWith("@@@N.")) { theStringList.add(s); }
		}

		return true;
	}

	private boolean execute_BUNDLE_COPY(){
		return false;
	}

	private boolean execute_BUNDLE_CLEAR(){
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex == 0) return false;
		if (!checkEOL()) return false;

		Bundle b = theBundles.get(bundleIndex);
		b.clear();
		return true;
	}

	private boolean execute_BUNDLE_CONTAIN(){
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex == 0) return false;

		if (!isNext(',')) return false;									// move to the tag
		if (!getStringArg()) return false;
		String tag = StringConstant;

		if (!isNext(',')) return false;									// move to the result var
		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		Bundle b = theBundles.get(bundleIndex);
		double thereis = (b.containsKey(tag)) ? 1.0 : 0.0;
		NumericVarValues.set(theValueIndex, thereis);

		return true;
	}

	// ************************************** Stack Package ***************************************

	private boolean executeSTACK() {							// Get stack command keyword if it is there
		return executeCommand(stack_cmd, "Stack");				// and execute the command
	}

	private boolean execute_STACK_CREATE(){
		char c = ExecutingLineBuffer.charAt(LineIndex);					// Get the type, s or n
		++LineIndex;
		int type = 0;

		if (c == 'n') {
			type = stack_is_numeric;
		} 
		else if (c == 's') {
			type = stack_is_string;
		} else return false;

		if (!isNext(',')) return false;

		if (!getNVar()) return false;									// Stack pointer variable
		int SaveValueIndex = theValueIndex;

		if (!checkEOL()) return false;

	 Stack theStack = new Stack();
	 int theIndex = theStacks.size();
	 theStacks.add(theStack);

	   theStacksType.add(type);											// add the type
	   NumericVarValues.set(SaveValueIndex, (double) theIndex);   		// Return the stack pointer    

		return true;
	}

	private int getStackIndexArg() {									// Get the Stack pointer
		if (evalNumericExpression()) {
			int stackIndex = EvalNumericExpressionValue.intValue();
			if ((stackIndex > 0) && (stackIndex < theStacks.size())) {
				return stackIndex;
			}
			RunTimeError("Invalid Stack Pointer");
		}
		return 0;
	}

	private boolean execute_STACK_PUSH(){
		int stackIndex = getStackIndexArg();								// Get the Stack pointer
		if (stackIndex == 0) return false;
		if (!isNext(',')) return false;										// move to the value

		Stack thisStack = theStacks.get(stackIndex);						// Get the stack
		switch (theStacksType.get(stackIndex))
		{
		case stack_is_string:												// String type stack
			if (!getStringArg()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			thisStack.push(StringConstant);									// Add the string to the stack
			break;

		case stack_is_numeric:
			if (!evalNumericExpression()){
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			thisStack.push(EvalNumericExpressionValue);						// Add the value to the stack
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}
		return true;
	}

	private boolean execute_STACK_POP(){
		int stackIndex = getStackIndexArg();								// Get the Stack pointer
		if (stackIndex == 0) return false;
		if (!isNext(',')) return false;										// move to the value

		Stack thisStack = theStacks.get(stackIndex);		 				// Get the Stack
		if (thisStack.isEmpty()){
			return RunTimeError("Stack is empty");
		}

		switch (theStacksType.get(stackIndex))
		{
		case stack_is_string:												// String type stack
			if (!getSVar()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			String thisString = (String) thisStack.pop();
			StringVarValues.set(theValueIndex, thisString);
			break;

		case stack_is_numeric:
			if (!getNVar()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			Double thisNumber = (Double) thisStack.pop();
			NumericVarValues.set(theValueIndex, thisNumber);
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}
		return true;
	}

	private boolean execute_STACK_PEEK(){
		int stackIndex = getStackIndexArg();								// Get the Stack pointer
		if (stackIndex == 0) return false;
		if (!isNext(',')) return false;										// move to the value

		Stack thisStack = theStacks.get(stackIndex);						// Get the Stack
		if (thisStack.isEmpty()){
			return RunTimeError("Stack is empty");
		}

		switch (theStacksType.get(stackIndex))
		{
		case stack_is_string:												// String type stack
			if (!getSVar()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			String thisString = (String) thisStack.peek();
			StringVarValues.set(theValueIndex, thisString);
			break;

		case stack_is_numeric:
			if (!getNVar()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL()) return false;
			Double thisNumber = (Double) thisStack.peek();
			NumericVarValues.set(theValueIndex, thisNumber);
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}
		return true;
	}

	private boolean execute_STACK_TYPE(){
		int stackIndex = getStackIndexArg();								// Get the Stack pointer
		if (stackIndex == 0) return false;
		if (!isNext(',')) return false;										// move to the value
		if (!getSVar()) return false;
		if (!checkEOL()) return false;

		switch (theStacksType.get(stackIndex))						// Get this Stack type
		{
		case stack_is_string:										// String
			StringVarValues.set(theValueIndex, "S");
			break;

		case stack_is_numeric:												// Number
			StringVarValues.set(theValueIndex, "N");
			break;

		default:
			return RunTimeError("Internal problem. Notify developer");
		}
		return true;
	}

	private boolean execute_STACK_ISEMPTY(){
		int stackIndex = getStackIndexArg();								// Get the Stack pointer
		if (stackIndex == 0) return false;
		if (!isNext(',')) return false;
		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		Stack thisStack = theStacks.get(stackIndex);						// Get the Stack
		double empty = thisStack.isEmpty() ? 1 : 0;
		NumericVarValues.set(theValueIndex, empty);

		return true;
	}

	private boolean execute_STACK_CLEAR(){
		int stackIndex = getStackIndexArg();								// Get the Stack pointer
		if (stackIndex == 0) return false;
		if (!checkEOL()) return false;

		Stack thisStack = theStacks.get(stackIndex);						// Get the Stack
		while (!thisStack.isEmpty()) { thisStack.pop(); }

		return true;
	}


	// ************************************ Clipboard Commands ************************************

	/*
	// This code does not work on devices with API level < 11
	 
	private boolean executeCLIPBOARD_GET(){
		  if (!getSVar()) return false;						// get the var to put the clip into
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		  String data = "";
		  if (clipboard.hasPrimaryClip()){                        // If clip board has text
			  CharSequence data1 = clipboard.getText();    // Get the clip
			  data = data1.toString(); 
			  if (data == null) data = "";
		  }else data ="";									// If no clip, set data to null
		  StringVarValues.set(theValueIndex, data);         // Return the result to user
			if (!checkEOL()) return false;
		return true;
	}
	
	private boolean executeCLIPBOARD_PUT(){
		int v = Integer.valueOf(Build.VERSION.SDK_INT);
		if (!getStringArg()) return false;          // Get the string to put into the clipboard
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		CharSequence cs = StringConstant;
		ClipData clip = ClipData.newPlainText("simple text",cs);
		clipboard.setPrimaryClip(clip);
		if (!checkEOL()) return false;
		return true;
	}
	*/
	
	private boolean executeCLIPBOARD_GET(){
		if (!getSVar()) return false;						// get the var to put the clip into
		if (!checkEOL()) return false;
		  String data;
		  if (clipboard.hasText()){                        // If clip board has text
			  CharSequence data1 = clipboard.getText();    // Get the clip
			  data = data1.toString();       
		  }else data ="";									// If no clip, set data to null
		  StringVarValues.set(theValueIndex, data);         // Return the result to user
		return true;
	}
	
	private boolean executeCLIPBOARD_PUT(){
		if (!getStringArg()) return false;					// Get the string to put into the clipboard
		if (!checkEOL()) return false;
		clipboard.setText(StringConstant);                  // Put the user expression into the clipboard
		return true;
	}

	// *********************************** Encryption Commands ************************************

	@SuppressLint("NewApi")									// Uses value from API 8
	private boolean executeENCRYPT(){
		if (!getStringArg()) return false;					// Get the Pass Word
		String PW = StringConstant;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;					// Get the Src string
		String Src = StringConstant;
		if (!isNext(',')) return false;

		if (!getSVar()) return false;						// Get the destination Var string
		if (!checkEOL()) return false;

	    // 8-byte Salt
	    byte[] salt = {
	        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
	    };

	    // Iteration count
	    int iterationCount = 19;
	    
	    Cipher ecipher = null;
        String Dest = "abc";

        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(PW.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(
                "PBEWithMD5AndDES").generateSecret(keySpec);

            ecipher = Cipher.getInstance(key.getAlgorithm());
            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            // Encode the string into bytes using utf-8
            byte[] utf8 = Src.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            Dest = Base64.encodeToString(enc, Base64.NO_WRAP);
            Dest = Dest.trim();
            
        } catch (Exception e) {
        	return RunTimeError(e);
        }
        

        StringVarValues.set(theValueIndex, Dest);    // Put the encrypted string into the user variable
		return true;
	}

	@SuppressLint("NewApi")									// Uses value from API 8
	private boolean executeDECRYPT(){
		if (!getStringArg()) return false;					// Get the Pass Word
		String PW = StringConstant;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;					// Get the Src string
		String Src = StringConstant;
		if (!isNext(',')) return false;

		if (!getSVar()) return false;						// Get the destination Var string
		if (!checkEOL()) return false;

	    // 8-byte Salt
	    byte[] salt = {
	        (byte)0xA9, (byte)0x9B, (byte)0xC8, (byte)0x32,
	        (byte)0x56, (byte)0x35, (byte)0xE3, (byte)0x03
	    };

	    // Iteration count
	    int iterationCount = 19;
	    
	    Cipher dcipher = null;
        try {
            // Create the key
            KeySpec keySpec = new PBEKeySpec(PW.toCharArray(), salt, iterationCount);
            SecretKey key = SecretKeyFactory.getInstance(
                "PBEWithMD5AndDES").generateSecret(keySpec);
            dcipher = Cipher.getInstance(key.getAlgorithm());

            // Prepare the parameter to the ciphers
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

            // Create the ciphers
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
            
            
        } 
        catch (Exception e) {
        	return RunTimeError(e);
        }
        
        String Dest = "@@@@";
        
        try {
            // Decode base64 to get bytes
            byte[] dec = Base64.decode(Src, Base64.DEFAULT);
            // Decrypt
            byte[] utf8 = dcipher.doFinal(dec);
            // Encode bytes to UTF 8 to get a string
            Dest = new String(utf8, "UTF8");
        } catch (Exception e) {
        	return RunTimeError(e);
        }

        if (Dest.equals("@@@@")){
        	RunTimeError( "Invalid Password at:");
        	return false;
        }
        
        StringVarValues.set(theValueIndex, Dest);  // Put the decoded string into the user variable.

		return true;
	}

	// ************************************* Socket Commands **************************************

	private boolean executeSOCKET(){								// Get Socket command keyword if it is there
		return executeCommand(Socket_cmd, "Socket");
	}

	private boolean executeSocketServer(){							// Get Socket Server command keyword if it is there
		return executeCommand(SocketServer_cmd, "Socket.Server");
	}

	private boolean executeSocketClient(){							// Get Socket Client command keyword if it is there
		return executeCommand(SocketClient_cmd, "Socket.Client");
	}

	private boolean isServerSocketConnected() {
		return isServerSocketConnected("No current connection");
	}

	private boolean isServerSocketConnected(String msgNullSocket) {
		if (theServerSocket == null){
			RunTimeError(msgNullSocket);
			return false;
		}
		if (!theServerSocket.isConnected()){
			RunTimeError("Server Connection Disrupted");
			return false;
		}
		return true;
	}

	private boolean isClientSocketConnected() {
		if (theClientSocket == null){
			RunTimeError("Client Socket Not Opened");
			return false;
		}
		if (!theClientSocket.isConnected()){
			RunTimeError("Client Connection Disrupted");
			return false;
		}
		return true;
	}

	private boolean executeSERVER_CREATE(){
		
		if (!evalNumericExpression()) return false;							// Get the List pointer
		if (!checkEOL()) return false;

		int SocketServersServerPort = EvalNumericExpressionValue.intValue();
		try {
			newSS = new ServerSocket(SocketServersServerPort);
		} catch (Exception e) {
			return RunTimeError(e);
		}

		return true;
	}

	private boolean executeSERVER_ACCEPT() {
		
		if (newSS == null) {
			RunTimeError("Server not created");
			return false;
		}
		boolean block = true;			// Default if no "wait" parameter is to block. This preserves
										// behavior from before v01.73, when the parameter was added.
		if (evalNumericExpression()) {										// Optional "wait" parameter
			block = (EvalNumericExpressionValue != 0.0);
		}
		if (!checkEOL()) return false;
		if ((theServerSocket != null) && theServerSocket.isConnected()) return true;

		serverSocketState = STATE_LISTENING;
		serverSocketConnectThread = new ServerSocketConnectThread();
		serverSocketConnectThread.start();
		if (block) {
			while (serverSocketState == STATE_LISTENING) { Thread.yield(); }
			if (serverSocketState != STATE_CONNECTED) {
				return RunTimeError("Server socket connection error: state " + serverSocketState);
			}
		}
		return true;
	}

	private class ServerSocketConnectThread extends Thread {
		public void run() {
			try {
				theServerSocket = newSS.accept();
				ServerBufferedReader = new BufferedReader(new InputStreamReader(theServerSocket.getInputStream()));
				ServerPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(theServerSocket.getOutputStream())), true);
				serverSocketState = STATE_CONNECTED;
			} catch (Exception e) {
				serverSocketState = STATE_NONE;
			} finally {
				serverSocketConnectThread = null;							// null global reference to itself
			}
			Log.d(LOGTAG, "serverSocketConnectThread exit, state " + serverSocketState);
		}

		@Override
		public void interrupt() {
			if (serverSocketState == STATE_LISTENING) {						// in case SERVER_DISCONNECT interrupts thread
				serverSocketState = STATE_NONE;								// change state or SERVER_STATUS will report LISTENING
			}
			super.interrupt();
		}
	}

	private boolean executeCLIENT_CONNECT() {

		if (!getStringArg()) return false;									// get the server address
		String SocketClientsServerAdr = StringConstant;

		if (!isNext(',')) return false;										// move to the port number
		if (!evalNumericExpression()) return false;							// get the port number
		int SocketClientsServerPort = EvalNumericExpressionValue.intValue();

		boolean block = true;												// Default if no "wait" parameter is to block.
		if (isNext(',')) {
			if (evalNumericExpression()) {									// Optional "wait" parameter
				block = (EvalNumericExpressionValue != 0.0);
			}
		}
		if (!checkEOL()) return false;

		clientSocketState = STATE_CONNECTING;
		clientSocketConnectThread = new ClientSocketConnectThread(SocketClientsServerAdr, SocketClientsServerPort);
		clientSocketConnectThread.start();
		if (block) {
			while (clientSocketState == STATE_CONNECTING) { Thread.yield(); }
			if (clientSocketState != STATE_CONNECTED) {
				return RunTimeError("Client socket connection error: state " + clientSocketState);
			}
		}
		return true;
	}

	private class ClientSocketConnectThread extends Thread {
		private final String mAddress;
		private final int mPort;

		public ClientSocketConnectThread(String address, int port) {
			super();
			mAddress = address;
			mPort = port;
		}

		public void run() {
			try {
				theClientSocket = new Socket(mAddress, mPort);
				ClientBufferedReader = new BufferedReader(new InputStreamReader(theClientSocket.getInputStream()));
				ClientPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(theClientSocket.getOutputStream())), true);
				clientSocketState = STATE_CONNECTED;
			} catch (Exception e) {
				clientSocketState = STATE_NONE;
			} finally {
				clientSocketConnectThread = null;							// null global reference to itself
			}
			Log.d(LOGTAG, "clientSocketConnectThread exit, state " + clientSocketState);
		}

		@Override
		public void interrupt() {
			if (clientSocketState == STATE_CONNECTING) {					// in case CLIENT_CLOSE interrupts thread
				clientSocketState = STATE_NONE;								// change state or CLIENT_STATUS will report CONNECTING
			}
			super.interrupt();
		}
	}

	private boolean executeSERVER_STATUS() {

		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		double status = (newSS == null)                         ? STATE_NOT_ENABLED
					  : (serverSocketState == STATE_LISTENING)  ? STATE_LISTENING
					  : (theServerSocket == null)               ? STATE_NONE
					  : theServerSocket.isConnected()           ? STATE_CONNECTED
					  :                                           STATE_NONE;
		NumericVarValues.set(theValueIndex, status);
		return true;
	}

	private boolean executeCLIENT_STATUS() {

		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		double status = (clientSocketState == STATE_CONNECTING) ? STATE_CONNECTING
					  : (theClientSocket == null)               ? STATE_NONE
					  : theClientSocket.isConnected()           ? STATE_CONNECTED
					  :                                           STATE_NONE;
		NumericVarValues.set(theValueIndex, status);
		return true;
	}

	private boolean executeSERVER_CLIENT_IP(){
		if (!isServerSocketConnected("Server not connected to a client")) return false;
		return socketIP(theServerSocket);
	}

	private boolean executeCLIENT_SERVER_IP(){
		if (!isClientSocketConnected()) return false;
		return socketIP(theClientSocket);
	}

	private boolean socketIP(Socket socket){
		
		if (!getSVar()) return false;
		if (!checkEOL()) return false;

		InetAddress ia = socket.getInetAddress();
		String sia = ia.toString();

		StringVarValues.set(theValueIndex, sia);
		return true;
	}

	private boolean executeSERVER_READ_READY(){
		if (!isServerSocketConnected("No current client accepted")) return false;
		return socketReadReady(ServerBufferedReader);
	}

	private boolean executeCLIENT_READ_READY(){
		if (!isClientSocketConnected()) return false;
		return socketReadReady(ClientBufferedReader);
	}

	private boolean socketReadReady(BufferedReader reader){

		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		double ready = 0 ;
		try {
			if (reader.ready()) { ready = 1; }
		} catch (IOException e) {
			return RunTimeError(e);
		}

		NumericVarValues.set(theValueIndex, ready);
		return true;
	}

	private boolean executeSERVER_READ_LINE(){
		if (!isServerSocketConnected()) return false;
		return socketReadLine(ServerBufferedReader);
	}

	private boolean executeCLIENT_READ_LINE(){
		if (!isClientSocketConnected()) return false;
		return socketReadLine(ClientBufferedReader);
	}

	private boolean socketReadLine(BufferedReader reader){

		if (!getSVar()) return false;
		if (!checkEOL()) return false;
		
		String line = null;
		try {
			line = reader.readLine();
		} catch (Exception e) {
			return RunTimeError(e);
		}
		
		if (line == null){
			line = "NULL";
		}

		StringVarValues.set(theValueIndex, line);
		return true;
	}

	private boolean executeSERVER_WRITE_LINE(){
		if (!isServerSocketConnected()) return false;
		return socketWrite(theServerSocket, ServerPrintWriter, false);
	}

	private boolean executeCLIENT_WRITE_LINE(){
		if (!isClientSocketConnected()) return false;
		return socketWrite(theClientSocket, ClientPrintWriter, false);
	}

	private boolean executeSERVER_WRITE_BYTES(){
		if (!isServerSocketConnected()) return false;
		return socketWrite(theServerSocket, ServerPrintWriter, true);
	}

	private boolean executeCLIENT_WRITE_BYTES(){
		if (!isClientSocketConnected()) return false;
		return socketWrite(theClientSocket, ClientPrintWriter, true);
	}

	private boolean socketWrite(Socket socket, PrintWriter writer, boolean byteMode){

		if (!getStringArg()) return false;
		if (!checkEOL()) return false;

		String err = null;
		if (byteMode){
			OutputStream os = null;
			try {
				os = socket.getOutputStream();
				for (int k=0; k<StringConstant.length(); ++k){
					byte bb = (byte)StringConstant.charAt(k);
					os.write(bb);
				}
			} catch (Exception e) {
				err = "Error: " + e;
			} finally {
				IOException ex = flushStream(os, null);
				ex = closeStream(os, ex);
				if (ex != null && err != null) {
					err = "Error: " + ex;
				}
			}
		} else {
			writer.println(StringConstant);
			if (writer.checkError()) {
				err = "Error writing to socket";
			}
		}
		if (err != null) {
			RunTimeError(err);
			return false;
		}
		return true;
	}

	private boolean executeSERVER_PUTFILE(){
		if (!isServerSocketConnected()) return false;
		return socketPutFile(theServerSocket);
	}

	private boolean executeCLIENT_PUTFILE(){
		if (!isClientSocketConnected()) return false;
		return socketPutFile(theClientSocket);
	}

	private boolean executeSERVER_GETFILE(){
		if (!isServerSocketConnected()) return false;
		return socketGetFile(theServerSocket);
	}

	private boolean executeCLIENT_GETFILE(){
		if (!isClientSocketConnected()) return false;
		return socketGetFile(theClientSocket);
	}

	private boolean socketPutFile(Socket socket) {

		if (!evalNumericExpression()) { return false; }						// Parm is the filenumber variable
		if (!checkEOL()) { return false; }

		int FileNumber = NumericVarValues.get(theValueIndex).intValue();
		if (!checkReadFile(FileNumber, BISlist)) { return false; }			// Check runtime errors

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkReadByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		boolean eof = FileEntry.getBoolean("eof");
		if (eof) {															// Check not EOF
			RunTimeError("Attempt to read beyond the EOF at:");
			return false;
		}
		
		BufferedInputStream bis = BISlist.get(FileEntry.getInt("stream"));
		int bufferSize = 1024*16;
		try {
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);

			// Set buffer size to 16K bytes, timeout to 16 sec, time out if rate is slower than 1kb/sec
			if (!streamCopy(bis, dos, bufferSize, (long)bufferSize)) {		// Copy from file to socket
				RunTimeError("Data transmission time out.");				// Timeout
				doServerDisconnect();
				return false;
			}
		} catch (Exception e) {
			return RunTimeError(e);
		}
		FileEntry.putBoolean("eof", true);
		FileEntry.putBoolean("closed", true);
		return true;														// Success
	}

	private boolean socketGetFile(Socket socket) {

		if (!evalNumericExpression()) { return false; }						// Parm is the filenumber variable
		if (!checkEOL()) { return false; }

		int FileNumber = EvalNumericExpressionValue.intValue();
		if (!checkWriteFile(FileNumber, DOSlist)) { return false; }			// Check runtime errors

		Bundle FileEntry = FileTable.get(FileNumber);						// Get the bundle
		if (!checkWriteByteAttributes(FileEntry)) { return false; }			// Check runtime errors

		DataOutputStream dos = DOSlist.get(FileEntry.getInt("stream"));
		int bufferSize = 1024*512;
		try {
			InputStream is = socket.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			streamCopy(bis, dos, bufferSize, 0L);							// Copy from socket to file

		} catch (Exception e) {
			return RunTimeError(e);
		}
		FileEntry.putBoolean("eof", true);
		FileEntry.putBoolean("closed", true);
		return true;														// Success
	}

	private static boolean streamCopy(BufferedInputStream bis, DataOutputStream dos, int bufferSize,
										long timeoutTime)		// time in ms, 0 means no timeout check
										throws IOException {
		IOException ex = null;
		ByteArrayBuffer byteArray = new ByteArrayBuffer(bufferSize);
		int current = 0;
		boolean timeout = false;
		long ts = SystemClock.elapsedRealtime();
		try {
			while (!timeout && ((current = bis.read()) != -1)) {			// Read the input stream
				byteArray.append((byte)current);
				if (byteArray.isFull()) {
					dos.write(byteArray.toByteArray(), 0, bufferSize);		// Write the output stream
					byteArray.clear();

					if (timeoutTime != 0) {							// If caller wants timeout checked
						long te = SystemClock.elapsedRealtime();	// If rate is too slow
						timeout = (te - ts > 16000); 				// terminate transmission
						ts = te;									// reset the start time
					}
				}
			}
			int count = byteArray.length();
			if (count > 0) {										// If there is anything in the buffer
				dos.write(byteArray.toByteArray(), 0, count);		// write it to the output stream
			}
			dos.flush();											// flush the output stream
			return !timeout;

		} catch (IOException e) {
			ex = e;
			return false;		// doesn't return
		} finally {
			ex = closeStream(dos, ex);								// close the streams
			ex = closeStream(bis, ex);
			if (ex != null) { throw ex; }
		}
	}

	private boolean executeSERVER_DISCONNECT() {
		return checkEOL() && doServerDisconnect();
	}

	private boolean doServerDisconnect() {
		if (serverSocketConnectThread != null) {
			serverSocketConnectThread.interrupt();
			serverSocketConnectThread = null;
		}

		if (theServerSocket == null) return true;
		try {
			theServerSocket.close();
		} catch (Exception e) {
			return RunTimeError(e);
		} finally {
			ServerPrintWriter = null;
			ServerBufferedReader = null;
			theServerSocket = null;
			serverSocketState = STATE_NONE;
		}

		return true;
	}

	private boolean executeSERVER_CLOSE() {
		if (!checkEOL()) return false;
		boolean disconnect = true;
		if (theServerSocket != null) {
			disconnect = doServerDisconnect();
		}
		try {
			if (newSS != null) newSS.close();
		} catch (Exception e) {
			return RunTimeError(e);
		} finally {
			newSS = null;
		}
		return disconnect;
	}

	private boolean executeCLIENT_CLOSE() {
		if (!checkEOL()) return false;
		if (theClientSocket == null) return true;

		if ((clientSocketState == STATE_CONNECTING) && (clientSocketConnectThread != null)) {
			clientSocketConnectThread.interrupt();
			clientSocketConnectThread = null;
		}

		try {
			theClientSocket.close();
		} catch (Exception e) {
			return RunTimeError(e);
		} finally {
			ClientPrintWriter = null;
			ClientBufferedReader = null;
			theClientSocket = null;
			clientSocketState = STATE_NONE;
		}
		return true;
	}

	private boolean executeMYIP(){
		if (!getSVar()) return false;
		if (!checkEOL()) return false;

		String IP = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!(inetAddress. isLoopbackAddress() || inetAddress. isLinkLocalAddress ())) {
						IP = inetAddress.getHostAddress().toString();
						break;
					}
				}
			}
		} catch (Exception e) {
			return RunTimeError(e);
		}
		
		StringVarValues.set(theValueIndex, IP);
		return true;
	}

	//****************************************** TTS *******************************************

	private boolean executeTTS(){								// Get TTS command keyword if it is there
		return executeCommand(tts_cmd, "TTS");
	}

	private boolean executeTTS_INIT(){
		if (!checkEOL()) return false;
		if (theTTS != null) return true;						// done if already opened

		ttsInit = false;
		theTTS = new TextToSpeechActivity(Run.this);
		if (theTTS == null) return false;
		while (!ttsInit) {
			Thread.yield();
		}

		switch (theTTS.mStatus) {
		case TextToSpeech.SUCCESS:            break;
		case TextToSpeech.LANG_MISSING_DATA:  return RunTimeError("Language Data Missing");
		case TextToSpeech.LANG_NOT_SUPPORTED: return RunTimeError("Language Not Supported");
		default:                              return RunTimeError("TTS Init Failed. Code = " + theTTS.mStatus);
		}

		return true;
	}

	private boolean executeTTS_SPEAK(){
		if (theTTS == null) {
			return RunTimeError("Text to speech not initialized");
		}
		if (!evalStringExpression()) return false;
		String speech = StringConstant;
		boolean block = true;					// Default if no "wait" parameter is to block. This preserves
		if (isNext(',')) {						// behavior from before v01.76, when the parameter was added.
			if (!evalNumericExpression()) { return false; }		// optional "wait" flag
			block = (EvalNumericExpressionValue != 0.0);		// block if non-zero
		}
		if (!checkEOL()) return false;

		if (!ttsWaitForDone()) return false;					// wait for any previous speaking to finish

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));

		theTTS.mDone = false;
		theTTS.speak(speech, params);
		if (block) { ttsWaitForDone(); }						// if requested, wait for speech to complete
		return true;
	}

	private boolean executeTTS_SPEAK_TOFILE(){
		if (theTTS == null) {
			return RunTimeError("Text to speech not initialized");
		}
		if (!evalStringExpression()) return false;
		String speech = StringConstant;
		String theFileName;
		if (isNext(',')) {										// optional file name parameter
			if (!getStringArg()) { return false; }
			theFileName = StringConstant;
		} else { theFileName = "tts.wav"; }						// default file name
		if (!checkEOL()) return false;

		if (!ttsWaitForDone()) return false;					// wait for any previous speaking to finish

		HashMap<String, String> params = new HashMap<String, String>();

		theFileName = Basic.getDataPath(theFileName);
		theTTS.mDone = false;
		theTTS.speakToFile(speech, params, theFileName);
		ttsWaitForDone();										// wait for speech to complete
		return true;
	}

	private boolean executeTTS_STOP() {
		return checkEOL() && ttsWaitForDone() && ttsStop();
	}

	private boolean ttsWaitForDone() {							// wait for any outstanding speaking to finish
		while (theTTS != null) {								// because cleanup() can kill theTTS while we're not looking
			if (theTTS.mDone) break; 
			Thread.yield();
		}
		return (theTTS != null);
	}

	private boolean ttsStop() {
		if (theTTS != null) {
			theTTS.shutdown();
			theTTS = null;
		}
		return true;
	}

	// ******************************************* FTP ********************************************

	private boolean executeFTP(){								// Get FTP command keyword if it is there
		return executeCommand(ftp_cmd, "FTP");					// and execute the command
	}

	private boolean executeFTP_OPEN(){
		if (!getStringArg()) return false;							// URL
		String url = StringConstant;

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;					// Port
		int port = EvalNumericExpressionValue.intValue();

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;							// User Name
		String user = StringConstant;

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;							// Pass word
		String pw = StringConstant;
		if (!checkEOL()) return false;

		if (!ftpConnect( url, user, pw, port)) return false;

		FTPdir = ftpGetCurrentWorkingDirectory();
		if (FTPdir == null) return false;

		return true;
	}

		public boolean ftpConnect(String host, String username,
	            String password, int port)
		{
			try {
				mFTPClient = new FTPClient();
				// connecting to the host
				mFTPClient.connect(host, port);

				// now check the reply code, if positive mean connection success
				if (FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
					// login using username & password
					boolean status = mFTPClient.login(username, password);

				/* Set File Transfer Mode
				 *
				 * To avoid corruption issue you must specified a correct
				 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
				 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
				 * for transferring text, image, and compressed files.
				 */
				 mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
				 mFTPClient.enterLocalPassiveMode();

					return status;
					}
			} catch(Exception e) {
				RunTimeError(e);
			}

			return false;
	}
		
		public String ftpGetCurrentWorkingDirectory()
		{
		    try {
		        String workingDir = mFTPClient.printWorkingDirectory();
		        return workingDir;
		    } catch(Exception e) {
		        RunTimeError(e);
		    }
		    return null;
		}
		
		private boolean executeFTP_CLOSE(){
			if (!checkEOL()) return false;
			if (FTPdir == null) return true;
			
			   try {
			        mFTPClient.logout();
			        mFTPClient.disconnect();
			        FTPdir = null;
			        return true;
			    } catch (Exception e) {
			        return RunTimeError(e);
			    }
		}

		private boolean executeFTP_DIR(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getNVar()) return false;								// get the list VAR
			String dirMark = "(d)";
			if (isNext(',')) {											// optional directory marker
				if (!getStringArg()) { return false; }
				dirMark = StringConstant;
			}
			if (!checkEOL()) return false;

			ArrayList<String> theStringList = new ArrayList <String>();	// create a new user list
			int theIndex = theLists.size();
			theLists.add(theStringList);

			theListsType.add(list_is_string);							// add the type
			NumericVarValues.set(theValueIndex, (double) theIndex);		// return the list pointer

			FTPFile[] ftpFiles;
			try { ftpFiles = mFTPClient.listFiles(); }					// get the list of files
			catch (Exception e) { return RunTimeError(e); }

			for (FTPFile file : ftpFiles) {								// write file names to list var
				String name = file.getName();
				if (file.isDirectory()) { name += dirMark; }			// mark directories
				theStringList.add(name);
			}

			return true;
		}

		private boolean executeFTP_CD(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// new directory name
			if (!checkEOL()) return false;

			String directory_path = "/" + StringConstant;

			boolean status = false;
			try {
				status = mFTPClient.changeWorkingDirectory(directory_path);
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (!status) { return RunTimeError("Directory change failed."); }

			FTPdir = directory_path;

			return true;
		}
		
		private boolean executeFTP_GET(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// Source file name
			String srcFile = StringConstant;

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;							// Destination file name
			String destFile = StringConstant;
			if (!checkEOL()) return false;

			destFile = Basic.getDataPath(destFile);

			return ftpDownload(srcFile, destFile);
		}

		public boolean ftpDownload(String srcFilePath, String desFilePath)
		{
			FileOutputStream desFileStream = null;
			boolean status = false;
			try {
				desFileStream = new FileOutputStream(desFilePath);
				status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
				desFileStream.close();
			} catch (Exception e) {
				closeStream(desFileStream, null);
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("Download error"); }
			return status;
		}

		private boolean executeFTP_PUT(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// Source file name
			String srcFile = StringConstant;

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;							// Destination file name
			String destFile = StringConstant;
			if (!checkEOL()) return false;

			srcFile = Basic.getDataPath(srcFile);

			return ftpUpload(srcFile, destFile);
		}

		public boolean ftpUpload(String srcFilePath, String desFilePath)
		{
			FileInputStream srcFileStream = null;
			boolean status = false;
			try {
				srcFileStream = new FileInputStream(srcFilePath);
				status = mFTPClient.storeFile(desFilePath, srcFileStream);
				srcFileStream.close();
			} catch (Exception e) {
				closeStream(srcFileStream, null);
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("Upload problem"); }
			return status;
		}	
		
		public boolean executeFTP_CMD(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// Command
			String cmd = StringConstant;

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;							// String parameter
			String parms = StringConstant;

			if (!isNext(',')) return false;
			if (!getNVar()) return false;								// Numeric parameter
			if (!checkEOL()) return false;

			String[] response = null;
			try {
				response = mFTPClient.doCommandAsStrings(cmd, parms);
			} catch (Exception e) {
				return RunTimeError(e);
			}
			for (String r : response) {
				PrintShow(r);
			}
			return true;
		}
		
		private boolean executeFTP_DELETE(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// get the file name
			String filePath = StringConstant;
			if (!checkEOL()) return false;

			boolean status = false;
			try {
				status = mFTPClient.deleteFile(filePath);				// try to delete the file
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("File not deleted"); }
			return status;
		}
		
		private boolean executeFTP_RMDIR(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// get the directory name
			String filePath = StringConstant;
			if (!checkEOL()) return false;

			boolean status = false;
			try {
				status = mFTPClient.removeDirectory(filePath);			// try to remove it
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("Directory not deleted"); }
			return status;
		}

		private boolean executeFTP_MKDIR(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// get the directory name
			String filePath = StringConstant;
			if (!checkEOL()) return false;

			boolean status = false;
			try {
				status = mFTPClient.makeDirectory(filePath);
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("Directory not created"); }
			return status;
		}
		
		private boolean executeFTP_RENAME(){
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg()) return false;							// old file name
			String oldName = StringConstant;

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;							// new file name
			String newName = StringConstant;
			if (!checkEOL()) return false;

			boolean status = false;
			try {
				status = mFTPClient.rename(oldName, newName);
			} catch (Exception e) {
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("File not renamed"); }
			return false;
		}

	// **************************************** Bluetooth *****************************************

	private boolean executeBT() {
		Command c = findCommand(bt_cmd, "BT");
		if (c != null) {
			if ((mChatService == null) && (c.id != CID_OPEN) && (c.id != CID_STATUS)) {
				return RunTimeError("Bluetooth not opened");
			}
			return c.run();
		}
		return false;
	}

	private synchronized boolean execute_BT_status() {
		if (!getNVar() || !checkEOL()) { return false; }
		int state = (mBluetoothAdapter == null) ? STATE_NOT_ENABLED :
					(mChatService == null)      ? STATE_NONE        : bt_state;
		NumericVarValues.set(theValueIndex, (double) state);
		return true;
	}

		  private synchronized boolean execute_BT_open(){

			  
			  if (mBluetoothAdapter == null) {
		            RunTimeError("Bluetooth is not available");
		            return false;
		        }
			  
			  
		    	bt_Secure = true;												// this flag will be used when starting 
		    	if (evalNumericExpression()) {									// the accept thread in BlueTootChatService
		    		if (EvalNumericExpressionValue == 0) bt_Secure = false;
		    	}
		    	if (!checkEOL()) { return false; }

		      bt_enabled = mBluetoothAdapter.isEnabled() ? 1 : 0;				// Is BT enabled?
		      if (bt_enabled == 0) {
		        bt_state = STATE_NOT_ENABLED;									// Enable BT
		        if (GRopen) {
		        	GR.doEnableBT = true;
          		  	GR.drawView.postInvalidate();									// Start GR drawing.
		        }else {
		        	Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		        	startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
		        }
	            while (bt_enabled == 0)											// Wait until enabled
	            	Thread.yield();
	            
	            if (bt_enabled == -1){											// Enable failed
	            	RunTimeError("Bluetooth Not Enabled");
	            	return false;
	            }
	          }
	            
	            bt_state = STATE_NONE;
	            btConnectDevice = null;
            	mOutStringBuffer = new StringBuffer("");
            	BT_Read_Buffer = new ArrayList<String>();

				mChatService = new BluetoothChatService(this, mHandler);		// Starts the accept thread
				mChatService.start(bt_Secure);

		        return true;

		  }
		  
		  private boolean execute_BT_close(){
			  if (mChatService != null) mChatService.stop();
			  return checkEOL();
		  }
		  
		    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		        switch (requestCode) {
		        case REQUEST_CONNECT_DEVICE_SECURE:
		            // When DeviceListActivity returns with a device to connect
		            if (resultCode == Activity.RESULT_OK) {
		                connectDevice(data, bt_Secure);
		            }
		            break;
		        case REQUEST_CONNECT_DEVICE_INSECURE:
		            // When DeviceListActivity returns with a device to connect
		            if (resultCode == Activity.RESULT_OK) {
		                connectDevice(data, false);
		            }
		            break;
		        case REQUEST_ENABLE_BT:
		            // When the request to enable Bluetooth returns
		            if (resultCode == Activity.RESULT_OK) {
		                // Bluetooth is now enabled, so set up a chat session
		            	bt_enabled = 1;
		            } else {
		                bt_enabled = -1;
		            }
		            break;
		        case VOICE_RECOGNITION_REQUEST_CODE:
		        	if (resultCode == RESULT_OK){
		    	        sttResults = new ArrayList<String>();
		        		sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
		        	}
		        	sttDone = true;
		        	break;
		        }
		    }
		    
		    public boolean handleBTMessage(Message msg) {
		        switch (msg.what) {
		            case MESSAGE_STATE_CHANGE:
		            	bt_state = msg.arg1;
		                break;
		            case MESSAGE_WRITE:
//		                byte[] writeBuf = (byte[]) msg.obj;
		                // construct a string from the buffer
//		                String writeMessage = new String(writeBuf);
		                break;
		            case MESSAGE_READ:
		                byte[] readBuf = (byte[]) msg.obj;
		                String readMessage = "";
		                // construct a string from the valid bytes in the buffer
//		                String readMessage = new String(readBuf, 0, msg.arg1);
		                try {
		                 readMessage = new String(readBuf, 0);
		                } catch (Exception e){
		                	Show ("Error: " + e);
		                }
		                readMessage = readMessage.substring(0, msg.arg1);
		                synchronized (this){
		                	if (BT_Read_Buffer.size() == 0) btReadReady = true;
		                	BT_Read_Buffer.add(readMessage);
		                }
		                break;
		            case MESSAGE_DEVICE_NAME:
		                // save the connected device's name
		                mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
		                break;
		            default:
		                return false;							// message not recognized
		        }
		        return true;									// message handled
		    }
		    
		    public void connectDevice(Intent data, boolean secure) {
		    	
		        String address = data.getExtras()
		            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		        btConnectDevice = null;
		        try {
		        	btConnectDevice = mBluetoothAdapter.getRemoteDevice(address);
			        if ( btConnectDevice != null) mChatService.connect(btConnectDevice, secure);
		        }
		        catch (Exception e){ 
		        	RunTimeError("Connect error: " + e);
		        }
		    }
		    
		    private boolean execute_BT_connect(){
		    	bt_Secure = true;
		    	if (evalNumericExpression()) {
		    		if (EvalNumericExpressionValue == 0) bt_Secure = false;
		    	}
		    	if (!checkEOL()) { return false; }
		    	
		        if (GRopen) {
		        	GR.startConnectBT = true;
          		  	GR.drawView.postInvalidate();									// Start GR drawing.
		        }else {
		        	Intent serverIntent = null;
		        	serverIntent = new Intent(this, DeviceListActivity.class);
		        	if (serverIntent == null){
		        		RunTimeError("Error selecting device");
		        		return false;
		        	}
		        	if (bt_Secure) {
		        		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
		        	}else {
		        		startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
		        	}
		        }
		    		return true;
		    }
		    
		    private boolean execute_BT_disconnect(){
		    	if (!checkEOL()) { return false; }
		    	mChatService.disconnect();
		    	return true;
		    }
		    
		    private boolean execute_BT_reconnect(){
		    	if (!checkEOL()) { return false; }
		    	if (btConnectDevice == null) {
		    		RunTimeError("Not previously connected");
		    		return false;
		    	}
		    	mChatService.connect(btConnectDevice, bt_Secure);
		    	return true;
		    }
		    
		    private boolean execute_BT_listen(){
		    	if (!checkEOL()) { return false; }
//		    	mChatService.start();
		    	return true;
		    }
		    
		    private boolean execute_BT_device_name(){
		        if (bt_state != STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");
		            return false;
		        }
		    	
		    	if (!getSVar() || !checkEOL()) return false;
		    	StringVarValues.set(theValueIndex, mConnectedDeviceName);
		    	return true;
		    }
		    
		    private boolean execute_BT_write(){
		        if (bt_state != STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");
		            return true;                                // Deliberately not making error fatal
		        }
		        
				if (!buildPrintLine("", "\n")) return false;	// build up the text line in StringConstant

		        // Check that there's actually something to send
		        if (StringConstant.length() > 0) {
		            // Get the message bytes and tell the BluetoothChatService to write
		        	byte[] send = new byte[StringConstant.length()];
					for (int k=0; k<StringConstant.length(); ++k){
						send[k] = (byte)StringConstant.charAt(k);
					}

		            mChatService.write(send);
		        }
		    	return true;
		    }

		    private boolean execute_BT_read_ready(){
		    	if (!getNVar() || !checkEOL()) return false;
		    	double d = 0;
		    	if (bt_state == STATE_CONNECTED) {
		    		synchronized (this){
		    		d = (double)BT_Read_Buffer.size();
		    		}
		    	}
		    	NumericVarValues.set(theValueIndex, d );
		    	return true;
		    	
		    }
		    
		    private boolean execute_BT_readReady_Resume(){
		    	if (interruptResume == -1) {
		    		RunTimeError("No Bluetooth Read Ready Interrupt");
		    		return false;	    		
		    	}
		    	return doResume();
		    }
		    
		    private boolean execute_BT_read_bytes(){
		    	
		        if (bt_state != STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");
		            return false;
		        }
		        
		        String msg = "";
		        if (bt_state == STATE_CONNECTED) {
		        	synchronized (this){
		        		int index = BT_Read_Buffer.size();
		        		if (index > 0 ){
		        			msg = BT_Read_Buffer.get(0);
		        			BT_Read_Buffer.remove(0);
		        		}
		        	}
		        }
		        
		        if (!getSVar() || !checkEOL()) return false;
		        StringVarValues.set(theValueIndex, msg);
		        
		    	return true;
		    }
		    
		    private boolean execute_BT_set_uuid(){
		    	if (!evalStringExpression() || !checkEOL()) return false;
		    	UUID MY_UUID_SECURE = UUID.fromString(StringConstant);
		    	UUID MY_UUID_INSECURE = UUID.fromString(StringConstant);
		    	return true;
		    }

	// *********************************** Superuser and System ***********************************

	private boolean executeSU(boolean isSU) {	// SU command (isSU true) or system comand (isSU false)
		Command c = findCommand(SU_cmd, (isSU ? "SU" : "System"));
		if (c != null) {
			if (SUprocess == null) {
				if (c.id == CID_OPEN) this.isSU = isSU;
				else return RunTimeError((isSU ? "Superuser" : "System shell") + " not opened");
			}
			return c.run();									// run the function and report back
		}
		return false;
	}

	private boolean execute_SU_open(){
		if (!checkEOL()) return false;
		if (SUprocess != null) return true;
		SU_ReadBuffer = new ArrayList<String>();				// Initialize buffer

		File dir = null;
		if (!isSU) {											// System.open: make sure AppPath exists
			dir = new File(Basic.getFilePath());
			if (!dir.exists() && !dir.mkdirs()) {
				return RunTimeError("Cannot create working directory " + dir);
			}
		}
		try {
			SUprocess = (isSU)	? Runtime.getRuntime().exec("su")				// Request Superuser
								: Runtime.getRuntime().exec("sh", null, dir);	// Open ordinary shell
			SUoutputStream = new DataOutputStream(SUprocess.getOutputStream());	// Open the output stream
			SUinputStream = new DataInputStream(SUprocess.getInputStream());	// Open the input stream
		} catch (Exception e) {
			return RunTimeError((isSU ? "SU" : "System") + " Exception: " + e);
		}
		theSUReader = new SUReader(SUinputStream, SU_ReadBuffer);
		theSUReader.start();

		return true;
	}

		    private boolean execute_SU_write(){
		    	if (!evalStringExpression()) return false;
		    	if (!checkEOL()) return false;
		    	String command = StringConstant;
		    	try {
		    		SUoutputStream.writeBytes(command + "\n");  // Write the command followed by new line character
		    		SUoutputStream.flush();
		    	}
		    	catch (Exception e){
					return RunTimeError((isSU ? "SU" : "System") + " Exception: " + e);
		    	}

		    	return true;
		    }
		    
		    private boolean execute_SU_read_ready(){
		    	if (!getNVar()) return false;
		    	synchronized (SU_ReadBuffer) {
		    		NumericVarValues.set(theValueIndex, (double) SU_ReadBuffer.size() ); // Return buffer size
		    	}
		    	return true;
		    }

		    private boolean execute_SU_read_line(){
		        String msg = "";
		        synchronized (SU_ReadBuffer){
		        	int index = SU_ReadBuffer.size();
		        	if (index > 0 ){
		        		msg = SU_ReadBuffer.get(0);    // Read the first item in the buffer
		        		SU_ReadBuffer.remove(0);       // and then remove it.
		        	}
		        }
		        if (!getSVar()) return false;
		        StringVarValues.set(theValueIndex, msg);
		    	return true;
		    }
		    
		    private boolean execute_SU_close(){
		    	theSUReader.stop();
		    	SUprocess.destroy();

		    	SUoutputStream = null;
		    	SUinputStream = null;
		    	SU_ReadBuffer = null;
		    	theSUReader = null;
		    	SUprocess = null;

		    	return true;
		    }

	// ************************************* CONSOLE Commands *************************************

	private boolean executeCONSOLE() {							// Get Console command keyword if it is there
		return executeCommand(Console_cmd, "Console");
	}

	private boolean executeCONSOLE_TITLE() {					// Set the console title string
		String title;
		if (isEOL()) {
			title = null;										// Use default
		} else {
			if (!getStringArg() || !checkEOL()) return false;	// Get new title
			title = StringConstant;
		}
		sendMessage(MESSAGE_CONSOLE_TITLE, title);				// Signal UI to update its title
		return true;
	}

	private boolean executeCONSOLE_LINE_COUNT(){
		if (!getNVar()) return false;							// variable to hold the number of lines
		if (!checkEOL()) return false;

		int count = output.size();								// number of lines written to console
		NumericVarValues.set(theValueIndex, (double)count);
		return true;
	}

	private boolean executeCONSOLE_LINE_TEXT(){
		if (!evalNumericExpression()) return false;				// line number to read
		int lineNum = EvalNumericExpressionValue.intValue();
		if (!isNext(',') || !getSVar() || !checkEOL()) return false; // variable for line content

		if (--lineNum < 0) {									// convert from 1-based user index to 0-based Java index
			return RunTimeError("Line number must be >= 1");
		}
		int max = output.size();								// number of lines written to console
		String lineText = (lineNum < max) ? output.get(lineNum) : "";
		StringVarValues.set(theValueIndex, lineText);
		return true;
	}

	private boolean executeCONSOLE_LINE_TOUCHED(){
		if (!getNVar()) return false; 							// variable for last line number touched
		int lineVarIndex = theValueIndex;
		int longTouchVarIndex = -1;
		if (isNext(',')) {
			if (!getNVar()) return false;						// optional variable indicating short or long touch
			longTouchVarIndex = theValueIndex;
		}
		if (!checkEOL()) return false;

		NumericVarValues.set(lineVarIndex, (double)TouchedConsoleLine);
		if (longTouchVarIndex != -1) {
			double isLongTouch = ConsoleLongTouch ? 1 : 0;
			NumericVarValues.set(longTouchVarIndex, isLongTouch);
		}
		return true;
	}

	private boolean executeCONSOLE_DUMP(){

		if (!getStringArg() || !checkEOL()) { return false; }	// Only parameter is the filename
		String theFileName = StringConstant;

		theBackground.checkpointProgress();						// allow any pending Console activity to complete
		while (ProgressPending) { Thread.yield(); }				// wait for checkpointProgress semaphore to clear

		File file = new File(Basic.getDataPath(theFileName));
		try {
			file.createNewFile();
		} catch (Exception e) {
			return RunTimeError(e);
		}
		if (!file.exists() || !file.canWrite()) {
			return RunTimeError("Problem opening " + theFileName);
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(file, false);				// open the filewriter for the SD Card
			for (String line : output) {
				writer.write(line + "\n");
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			return RunTimeError(e);
		}
		// Log.d(LOGTAG, CLASSTAG + " executeCONSOLE_DUMP: file " + theFileName + " written");

		return true;
	}

	  private boolean executeCONSOLE_FRONT(){
		  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
		  startActivity(Basic.theProgramRunner);
		  GRFront = false;

		  return true;
	  }

	  private boolean executeCONSOLE_LINE_NEW(){
		  Show("@@A");
		  return true;
	  }

	  private boolean executeCONSOLE_LINE_CHAR(){
		  if (!evalStringExpression()) return false;
		  char c = StringConstant.charAt(0);
		  Show("@@B" + c);
		  return true;
	  }

	// *************************************** Ringer Vars ****************************************

	private boolean executeRINGER() {							// Get RINGER command keyword if it is there
		return executeCommand(ringer_cmd, "Ringer");			// and execute the command
	}

	private boolean executeRINGER_GET_MODE() {

		if (!getNVar()) return false;							// Mode return variable
		if (!checkEOL()) return false;

		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int mode = am.getRingerMode();
		switch (mode) {											// convert from Android to internal values
			case AudioManager.RINGER_MODE_SILENT:  mode = RINGER_SILENT;  break;
			case AudioManager.RINGER_MODE_VIBRATE: mode = RINGER_VIBRATE; break;
			case AudioManager.RINGER_MODE_NORMAL:  mode = RINGER_NORMAL;  break;
			default:                               mode = RINGER_UNKNOWN; break;
		}

		NumericVarValues.set(theValueIndex, (double)mode );
		return true;
	}

	private boolean executeRINGER_SET_MODE() {

		if (!evalNumericExpression()) return false;				// Mode value
		int mode = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		switch (mode) {											// convert from internal to Android values
			case RINGER_SILENT:  mode = AudioManager.RINGER_MODE_SILENT;  break;
			case RINGER_VIBRATE: mode = AudioManager.RINGER_MODE_VIBRATE; break;
			case RINGER_NORMAL:  mode = AudioManager.RINGER_MODE_NORMAL;  break;
			default: return true;								// bad value: don't change anything
		}
		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		am.setRingerMode(mode);
		return true;
	}

	private boolean executeRINGER_GET_VOLUME() {

		if (!getNVar()) return false;							// Volume return variable
		int volIndex = theValueIndex;
		int maxIndex = -1;
		if (isNext(',')) {
			if (!getNVar()) return false;
			maxIndex = theValueIndex;
		}
		if (!checkEOL()) return false;

		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		int vol = am.getStreamVolume(AudioManager.STREAM_RING);

		NumericVarValues.set(volIndex, (double)vol);
		if (maxIndex != -1) {
			NumericVarValues.set(maxIndex, (double)max);
		}
		return true;
	}

	private boolean executeRINGER_SET_VOLUME() {

		if (!evalNumericExpression()) return false;				// Volume value
		int vol = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		if (vol < 0) vol = 0;
		else if (vol > max) vol = max;
		am.setStreamVolume(AudioManager.STREAM_RING, vol, 0);

		return true;
	}

	// **************************************** SOUND POOL ****************************************

	private boolean executeSOUNDPOOL() {
		Command c = findCommand(sp_cmd, "Soundpool");
		if (c != null) {
			if ((theSoundPool == null) && (c.id != CID_OPEN)) {
				return RunTimeError("SoundPool not opened");
			}
			return c.run();
		}
		return false;
	}

	private boolean execute_SP_open() {

		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;
		int SP_max = EvalNumericExpressionValue.intValue();
		if (SP_max <= 0) {
			return RunTimeError("Stream count must be > 0");
		}
//		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		theSoundPool = new SoundPool(SP_max,  AudioManager.STREAM_MUSIC, 0);

		return true;
	}

	private boolean execute_SP_load(){
		if (!getNVar()) return false;
		int savedIndex = theValueIndex;
		if (!isNext(',')) { return false; }

		if (!getStringArg()) return false;								// Get the file path
		if (!checkEOL()) return false;
		String fileName = StringConstant;								// The filename as given by the user
		String fn = Basic.getDataPath(fileName);

		int SoundID = theSoundPool.load(fn, 1);
		if (SoundID == 0) {												// if file does not exist
			if (Basic.isAPK) {											// and this is a user APK
				int resID = Basic.getRawResourceID(fileName);			// try to load the file from a raw resource
				if (resID != 0) {
					SoundID = theSoundPool.load(Basic.BasicContext, resID, 1);
				} else {												// try to load the file from assets
					AssetFileDescriptor afd = null;
					try {
						String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, fileName);
						afd = getAssets().openFd(assetPath);
						SoundID = theSoundPool.load(afd, 1);
					}
					catch (IOException ex) { }
					finally {
						if (afd != null) { try { afd.close(); } catch (IOException e) { } }
					}
				}
				if (SoundID == 0) { return false; }
			}
		}
		NumericVarValues.set(savedIndex, (double) SoundID );
		return true;
	}

	  private boolean execute_SP_play(){
		  
		  if (!getNVar()) return false;                        			// Stream return variable
		  int savedIndex = theValueIndex;

			if (!isNext(',')) return false;								// Sound ID
			if (!evalNumericExpression()) return false;
			int soundID = EvalNumericExpressionValue.intValue();
			
			if (!isNext(',')) return false;								// Left Volume
			if (!evalNumericExpression()) return false;
			float leftVolume = EvalNumericExpressionValue.floatValue();
			
			if (!isNext(',')) return false;								// Right Volume
			if (!evalNumericExpression()) return false;
			float rightVolume = EvalNumericExpressionValue.floatValue();
			
			if (!isNext(',')) return false;								// Priority
			if (!evalNumericExpression()) return false;
			int priority = EvalNumericExpressionValue.intValue();

			if (!isNext(',')) return false;								// Loop
			if (!evalNumericExpression()) return false;
			int loop = EvalNumericExpressionValue.intValue();
			
			if (!isNext(',')) return false;								// Rate
			if (!evalNumericExpression()) return false;
			float rate = EvalNumericExpressionValue.floatValue();
			if (!checkEOL()) return false;
			
			if (leftVolume < 0 || leftVolume >= 1.0){
				RunTimeError("Left volume out of range");
				return false;
			}

			if (rightVolume < 0 || rightVolume >= 1.0){
				RunTimeError("Right volume out of range");
				return false;
			}
			
			if (priority < 0 ){
				RunTimeError("Priority less than zero");
				return false;
			}

			if (rate < 0.5 || rate > 1.8  ){
				RunTimeError("Rate out of range");
				return false;
			}
			
			
		int streamID = theSoundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
			
		  	NumericVarValues.set(savedIndex, (double) streamID );
			
		  return true;
	  }
	  
	  private boolean execute_SP_stop(){
			if (!evalNumericExpression()) return false;
			if (!checkEOL()) return false;
			int streamID = EvalNumericExpressionValue.intValue();
			theSoundPool.stop(streamID);

		  return true;
	  }
	  
	  private boolean execute_SP_unload(){
			if (!evalNumericExpression()) return false;
			if (!checkEOL()) return false;
			int soundID = EvalNumericExpressionValue.intValue();
			theSoundPool.unload(soundID);

		  return true;
	  }

	@SuppressLint("NewApi")									// Uses value from API 8
	private boolean execute_SP_pause(){
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		int streamID = EvalNumericExpressionValue.intValue();
		if (streamID == 0 ) theSoundPool.autoPause();
		else theSoundPool.pause(streamID);

		return true;
	}

	@SuppressLint("NewApi")									// Uses value from API 8
	private boolean execute_SP_resume(){
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		int streamID = EvalNumericExpressionValue.intValue();
		if (streamID == 0 ) theSoundPool.autoResume();
		else theSoundPool.resume(streamID);

		return true;
	}

	  private boolean execute_SP_release(){
		  if (!checkEOL()) return false;
		  theSoundPool.release();
		  theSoundPool = null;
		  return true;
	  }
	  
	  private boolean execute_SP_setvolume(){
		  
			if (!evalNumericExpression()) return false;
			int streamID = EvalNumericExpressionValue.intValue();
			
			if (!isNext(',')) return false;								// Left Volume
			if (!evalNumericExpression()) return false;
			float leftVolume = EvalNumericExpressionValue.floatValue();
			
			if (!isNext(',')) return false;								// Right Volume
			if (!evalNumericExpression()) return false;
			float rightVolume = EvalNumericExpressionValue.floatValue();
			if (!checkEOL()) return false;
			
			if (leftVolume < 0 || leftVolume >= 1.0 ){
				return RunTimeError("Left volume out of range");
			}

			if (rightVolume < 0 || rightVolume >= 1.0){
				return RunTimeError("Right volume out of range");
			}

			theSoundPool.setVolume(streamID, leftVolume, rightVolume);


		  return true;
	  }
	  
	  private boolean execute_SP_setpriority(){
			if (!evalNumericExpression()) return false;
			int streamID = EvalNumericExpressionValue.intValue();
			
			if (!isNext(',')) return false;								// Priority
			if (!evalNumericExpression()) return false;
			int priority = EvalNumericExpressionValue.intValue();
			if (!checkEOL()) return false;
			
			if (priority < 0 ){
				RunTimeError("Priority less than zero");
				return false;
			}
			
			theSoundPool.setPriority(streamID, priority);

		  return true;
	  }
	  
	  private boolean execute_SP_setloop(){
			if (!evalNumericExpression()) return false;
			int streamID = EvalNumericExpressionValue.intValue();
			
			if (!isNext(',')) return false;								// Loop value
			if (!evalNumericExpression()) return false;
			int loop = EvalNumericExpressionValue.intValue();
			if (!checkEOL()) return false;
			
			theSoundPool.setLoop(streamID, loop);

		  return true;
	  }
	  

	  private boolean execute_SP_setrate(){
			if (!evalNumericExpression()) return false;
			int streamID = EvalNumericExpressionValue.intValue();
			
			if (!isNext(',')) return false;								// Rate
			if (!evalNumericExpression()) return false;
			float rate = EvalNumericExpressionValue.floatValue();
			if (!checkEOL()) return false;
			
			if (rate < 0.5 || rate > 1.8){
				RunTimeError("Rate out of range");
				return false;
			}
			
			theSoundPool.setRate(streamID, rate);
		  return true;
	  }

	// *********************************** Get my phone number ************************************

	private boolean executeMYPHONENUMBER() {

		if (!getSVar()) return false;
		if (!checkEOL()) return false;

		String pn = getMyPhoneNumber();
		StringVarValues.set(theValueIndex, pn);

		return true;		// Leave theValueIndex intact for executeDEVICE
	}

	private String getMyPhoneNumber() {
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String pn = (tm != null) ? tm.getLine1Number() : null;
		return (pn != null) ? pn : "Get phone number failed.";
	}

	// ***************************************** Headset ******************************************

	private boolean executeHEADSET() {

		if (!getNVar()) return false;
		int stateIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getSVar()) return false;
		int nameIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getNVar()) return false;
		int micIndex = theValueIndex;
		if (!checkEOL()) return false;

		NumericVarValues.set(stateIndex, (double)headsetState);
		StringVarValues.set(nameIndex, headsetName);
		NumericVarValues.set(micIndex, (double)headsetMic);

		return true;
	}

	// ******************************************* SMS ********************************************

	private boolean executeSMS() {								// Get SMS command keyword if it is there
		return executeCommand(sms_cmd, "SMS");					// and execute the command
	}

	private boolean executeSMS_SEND() {

		if (!getStringArg()) return false;
		String number = StringConstant;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;
		String msg = StringConstant;
		if (!checkEOL()) return false;

		SmsManager sm = android.telephony.SmsManager.getDefault();
		try {
			sm.sendTextMessage(number, null, msg, null, null);
		} catch (Exception e) {
			return RunTimeError(e);
		}

		return true;
	}

	private boolean executeSMS_RCV_INIT() {
		if (!checkEOL()) return false;

		registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		smsRcvBuffer = new ArrayList <String>();
		return true;
	}

	private boolean executeSMS_RCV_NEXT() {
		if (smsRcvBuffer == null) {
			return RunTimeError("SMS.RCV.INIT not executed)");
		}

		if (!getSVar()) return false;
		if (!checkEOL()) return false;

		if (smsRcvBuffer.size() == 0) {
			StringVarValues.set(theValueIndex, "@");
			return true;
		}

		StringVarValues.set(theValueIndex, smsRcvBuffer.get(0));
		smsRcvBuffer.remove(0);

		return true;
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			Bundle bundle = arg1.getExtras();
			SmsMessage[] recievedMsgs = null;
			String str = "";
			if (bundle != null)
			{
				Object[] pdus = (Object[]) bundle.get("pdus");
				recievedMsgs = new SmsMessage[pdus.length];
				for (int i = 0; i < recievedMsgs.length; ++i)
				{
					recievedMsgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
					str += "SMS from " + recievedMsgs[i].getOriginatingAddress()+ " :" + recievedMsgs[i].getMessageBody().toString();
					if (smsRcvBuffer != null)
						smsRcvBuffer.add(str);
				}
			}
		}
	};

	// **************************************** Phone Call ****************************************

	private boolean executePHONE() {							// Get phone command keyword if it is there
		return executeCommand(phone_cmd, "Phone");				// and execute the command
	}

	private boolean executePHONE_CALL() {
		if (!getStringArg()) return false;
		if (!checkEOL()) return false;
		String number = "tel:" + StringConstant;

		Intent callIntent = new Intent(Intent.ACTION_CALL);
		callIntent.setData(Uri.parse(number));
		// this will make such that when user returns to your app, your app is displayed, instead of the phone app.
		callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		try { startActivityForResult(callIntent, BASIC_GENERAL_INTENT); }
		catch (Exception e) { return RunTimeError(e); }

		return true;
	}

	private boolean executePHONE_RCV_INIT() {
		if (!checkEOL()) return false;

		if (phoneRcvInited) return true;
		phoneRcvInited = true;

		mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		mTM.listen(PSL, PhoneStateListener.LISTEN_CALL_STATE);
 
		return true;
	}

	PhoneStateListener PSL = new PhoneStateListener() {
		public void onCallStateChanged(int state,  String incomingNumber) {
			phoneState = state;
			if (phoneState == TelephonyManager.CALL_STATE_RINGING) {
				phoneNumber = incomingNumber;
			}
		}
	};

	private boolean executePHONE_RCV_NEXT() {
		if (!phoneRcvInited) {
			return RunTimeError("phone.rcv.init not executed");
		}

		if (!getNVar()) return false;
		int stateIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getSVar()) return false;
		int numberIndex = theValueIndex;
		if (!checkEOL()) return false;

		int callState = mTM.getCallState();
		if (callState == TelephonyManager.CALL_STATE_IDLE) { phoneNumber = ""; }

		NumericVarValues.set(stateIndex, (double)callState);
		StringVarValues.set(numberIndex, phoneNumber);

		return true;
	}

	// ****************************************** EMAIL *******************************************

	private boolean executeEMAIL_SEND() {

		if (!getStringArg()) return false;
		String recipiant = "mailto:" + StringConstant;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;
		String subject = StringConstant;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;
		String body = StringConstant;
		if (!checkEOL()) return false;

		Intent intent = new Intent(Intent.ACTION_SENDTO);	// it's not ACTION_SEND
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, body);
		intent.setData(Uri.parse(recipiant));				// or just "mailto:" for blank
		// this will make such that when user returns to your app, your app is displayed, instead of the email app.
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		try { startActivityForResult(intent, BASIC_GENERAL_INTENT); }
		catch (Exception e) { return RunTimeError(e); }

		return true;
	}

	// ******************************************* HTML *******************************************

	private boolean executeHTML() {

		if (htmlOpening) {
			while (Web.aWebView == null) Thread.yield();
			htmlOpening = false;
		}

		Command c = findCommand(html_cmd, "HTML");
		if (c != null) {
			if ((htmlIntent == null) || (Web.aWebView == null)) {
				if (c.id == CID_CLOSE) {					// Allow close if already closed
					return true;
				}
				if ((c.id != CID_OPEN) &&					// Allow open and get.datalink if not opened
					(c.id != CID_DATALINK)) {
					return RunTimeError("html not opened");
				}
			}
			return c.run();
		}
		return false;
	}

	private boolean execute_html_open() {
		if (Web.aWebView != null) {
			return RunTimeError("HTML previously open and not closed");
		}

		int showStatusBar = 0;							// default to status bar not showing
		int orientation = -1;							// default to orientation per sensor
		if (evalNumericExpression()) {
			showStatusBar = EvalNumericExpressionValue.intValue();
			if (isNext(',')) {
				if (!evalNumericExpression()) return false;
				orientation = EvalNumericExpressionValue.intValue();
			}
		}
		if (!checkEOL()) return false;

		htmlIntent = new Intent(this, Web.class);			// Intent variable used to tell if opened
		htmlIntent.putExtra(Web.EXTRA_SHOW_STATUSBAR, showStatusBar);
		htmlIntent.putExtra(Web.EXTRA_ORIENTATION, orientation);
		Web.aWebView = null;								// Will be set in Web.java
		htmlData_Buffer = new ArrayList<String>();			// Initialize the datalink buffer
		sendMessage(MESSAGE_HTML_OPEN);						// Start Web View in UI thread.
		htmlOpening = true;

		return true;
	}

	private String getURL(String path) {					// build a URL for a file or directory in the file system or assets
		String urlPath = Basic.getDataPath(path);			// if path is null, get full path of default data directory
															// if non-null, get full path of a file in the data directory
		if (!new File(urlPath).exists() && Basic.isAPK) {	// if file or dir does not exist in file system, and this is an APK
			String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, path);	// then look for it in assets
			String type = getAssetType(assetPath);			// type is null if asset not found
			String expType = (path == null) ? "d" : "f";	// are we looking for a directory or a file?
			if ((type != null) && type.equals(expType)) {	// if asset exists and is the expected type, use its path
				urlPath = "/android_asset/" + assetPath;
			}
		}													// else use the file system path
		return "file://" + urlPath;							// make the path into a URL
	}

	private boolean execute_html_orientation() {			// change the screen orientation
		if (!evalNumericExpression() || !checkEOL()) return false;
		Web.aWebView.setOrientation(EvalNumericExpressionValue.intValue());
		return true;
	}

	private boolean execute_html_load_url() {				// Load an internet url
		if (!getStringArg() || !checkEOL()) return false;

		String urlString = StringConstant;
		String protocolName = urlString.substring(0,4);
		if (!protocolName.equals("http") && !protocolName.equals("java") && !protocolName.equals("file")) {
		urlString = getURL(urlString);						// Get URL with full path to file in file system or assets.
															// If neither file nor asset exists,
															// path points to non-existent file system file.
		}
		sendMessage(MESSAGE_LOAD_URL, urlString);
		return true;
	}

	private boolean execute_html_load_string() {			// Load an html string
		if (!getStringArg() || !checkEOL()) return false;
		String baseURL = getURL(null) + File.separatorChar;	// baseURL is default data directory in file system or assets.
															// If directory does not exist in either file system or assets,
															// path points to non-existent file system directory.
		String[] data = { baseURL, StringConstant };
		sendMessage(MESSAGE_LOAD_STRING, data);
		return true;
	}

	private boolean execute_html_get_datalink() {			// Gets a data sring from datalink queue

		if (!getSVar()) return false;						// The string return variable
		if (!checkEOL()) return false;

		String data = "";
		if (htmlData_Buffer != null)
			if (htmlData_Buffer.size() > 0)					// If the buffer is not empty
				data = htmlData_Buffer.remove(0);			// get the oldest entry and remove it from the buffer

		StringVarValues.set(theValueIndex, data);			// return the data to the user

		if (Web.aWebView == null) return true;				// if already closed, return now
															// else check to see if we should close
//		if (data.startsWith("FOR:")) return execute_html_close();	// if Form, close the html
		if (data.startsWith("ERR:")) return execute_html_close();	// if error, close the html

		return true;
	}

	private boolean execute_html_go_back() {
		if (!checkEOL()) return false;
//		Web.aWebView.goBack();
		sendMessage(MESSAGE_GO_BACK);
		return true;
	}

	private boolean execute_html_go_forward() {
		if (!checkEOL()) return false;
//		Web.aWebView.goForward();
		sendMessage(MESSAGE_GO_FORWARD);
		return true;
	}

	private boolean execute_html_clear_cache() {
		if (!checkEOL()) return false;
//		Web.aWebView.clearCache();
		sendMessage(MESSAGE_CLEAR_CACHE);
		return true;
	}

	private boolean execute_html_clear_history() {
		if (!checkEOL()) return false;
//		Web.aWebView.clearHistory();
		sendMessage(MESSAGE_CLEAR_HISTORY);
		return true;
	}

	private boolean execute_html_close() {					// Close the html
		if (!checkEOL()) return false;
		if (Web.aWebView != null) Web.aWebView.webClose();	// if it is open
		while (Web.aWebView != null) Thread.yield();		// wait for the close signal
		htmlIntent = null;									// indicate not open
		return true;
	}

	private boolean execute_html_post() {
		if (!getStringArg()) return false;
		String url = StringConstant;

		if (!isNext(',')) return false; 
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		int theListIndex = EvalNumericExpressionValue.intValue();
		if (theListIndex < 1 || theListIndex >= theLists.size()) {
			return RunTimeError("Invalid list pointer");
		}
		if (theListsType.get(theListIndex) == list_is_numeric) {
			return RunTimeError("List must be of string type.");
		}
		List<String> thisList = theLists.get(theListIndex);
		int r = thisList.size() % 2;
		if (r != 0) {
			return RunTimeError("List must have even number of elements");
		}

		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < thisList.size()) {
			sb.append(thisList.get(i++)).append('=');
			sb.append(thisList.get(i++)).append('&');
		}
		String[] params = { url, sb.substring(0, sb.length() - 1) };

		sendMessage(MESSAGE_POST, params);
		return true;
	}

	public boolean handleHtmlMessage(Message msg) {
		if ((msg.what == MESSAGE_HTML_OPEN) && (htmlIntent != null)) {
			startActivityForResult(htmlIntent, BASIC_GENERAL_INTENT);
		} else if (Web.aWebView != null) {
			switch (msg.what) {
			case MESSAGE_GO_BACK:		Web.aWebView.goBack();		break;
			case MESSAGE_GO_FORWARD:	Web.aWebView.goForward();	break;
			case MESSAGE_CLEAR_CACHE:	Web.aWebView.clearCache();	break;
			case MESSAGE_CLEAR_HISTORY:	Web.aWebView.clearHistory();break;
			case MESSAGE_LOAD_URL:
				String url = (String)msg.obj;
				Web.aWebView.webLoadUrl(url);
				break;
			case MESSAGE_LOAD_STRING:
				String[] data = (String[])msg.obj;
				Web.aWebView.webLoadString(data[0], data[1]);	// baseURL and HTML.Load.String argument
				break;
			case MESSAGE_POST:
				String[] params = (String[])msg.obj;
				Web.aWebView.webPost(params[0], params[1]);		// URL and data for "POST" request
				break;
			default:
				return false;									// message not recognized
			}
		}
		return true;											// message handled
	}

	// *************************************** Run Command ****************************************

	private boolean executeRUN() {

		if (!getStringArg()) { return false; }								// get program filename
		String fileName = StringConstant;

		String data = "";
		if (isNext(',')) {													// optional
			if (!getStringArg()) { return false; }							// parameter to pass to program
			data = StringConstant;
		}
		if (!checkEOL()) { return false; }

		String path = Basic.getFilePath(Basic.SOURCE_DIR, fileName);
		boolean exists = false;
		if (!Basic.isAPK) { exists = new File(path).exists(); }				// standard BASIC can only RUN a file
		else if (Basic.getRawResourceID(fileName) != 0) { exists = true; }	// APK can run resource
		else try {															// or asset
			String assetPath = Basic.getAppFilePath(Basic.SOURCE_DIR, fileName);
			getAssets().openFd(assetPath); 									// exception if asset does not exist
			exists = true;
		} catch (IOException ex) { }
		if (!exists) {														// error if the program does not exist
			return RunTimeError(fileName + " not found");
		}

		Bundle bb = new Bundle();
		bb.putString("fn", fileName);										// without the path
		bb.putString("data", data);
		bb.putBoolean("RUN", true);											// tell AutoRun this is a RUN command

		Intent intent = new Intent(this, AutoRun.class);
		intent.putExtras(bb);

		startActivity(intent);
		finish();

		return true;
	}

	// ********************************** Empty Program Command ***********************************

	private boolean executeEMPTY_PROGRAM() {
		Show("Nothing to execute.");
		Stop = true;
		return true;
	}

	// ************************************** Notify Command **************************************

	private boolean executeNOTIFY() {

		int NOTIFICATION_ID = 1;	// These two constants are without meaning in this application
		int REQUEST_CODE = 2;

		if (!getStringArg()) return false;
		String title = StringConstant;

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;
		String subtitle = StringConstant;

		if (!isNext(',')) return false;
		if (!getStringArg()) return false;
		String msg = StringConstant;

		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;				// logical expression: wait flag
		boolean wait = (EvalNumericExpressionValue != 0);
		if (!checkEOL()) { return false; }

		Notified = false;

		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.icon, msg, System.currentTimeMillis());

		// The PendingIntent will launch activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, REQUEST_CODE, new Intent(this, HandleNotify.class), 0);

		notification.setLatestEventInfo(this, title, subtitle, contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		manager.notify(NOTIFICATION_ID, notification);

		if (wait) {
			while (!Notified) Thread.yield();
		}
		return true;
	}

	// *************************************** Swap Command ***************************************

	private boolean executeSWAP() {

		if (!getVar()) return false;
		int aIndex = theValueIndex;
		boolean aIsNumeric = VarIsNumeric;
		if (!isNext(',')) return false;

		if (!getVar()) return false;
		int bIndex = theValueIndex;
		if (aIsNumeric != VarIsNumeric) { return RunTimeError("Type mismatch"); }
		if (!checkEOL()) return false;

		if (VarIsNumeric) {
			double aValue = NumericVarValues.get(aIndex);
			double bValue = NumericVarValues.get(bIndex);
			NumericVarValues.set(aIndex, bValue);
			NumericVarValues.set(bIndex, aValue);
		} else {
			String aValue = StringVarValues.get(aIndex);
			String bValue = StringVarValues.get(bIndex);
			StringVarValues.set(aIndex, bValue);
			StringVarValues.set(bIndex, aValue);
		}
		return true;
	}

	// ********************************* Speech-to-Text Commands **********************************

	private boolean executeSTT_LISTEN() {
		if (!checkEOL()) return false;

		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			return RunTimeError("Recognizer not present");
		}

		sttListening = true;
		sttDone = false;
		if (GRopen)
			GR.doSTT = true;
		else
			startVoiceRecognitionActivity();
		return true;
	}

	private boolean executeSTT_RESULTS() {
		if (!getNVar()) return false;
		if (!checkEOL()) return false;

		if (!sttListening) {
			return RunTimeError("STT_LISTEN not executed.");
		}
		while (!sttDone) Thread.yield();
		sttListening = false;

		int theIndex = theLists.size();
		if (sttResults == null) {
			sttResults = new ArrayList <String>();
			sttResults.add("Recognition Cancelled");
		}
		theLists.add(sttResults);
		theListsType.add(list_is_string);

		NumericVarValues.set(theValueIndex, (double) theIndex);		// Return the list pointer
		return true;
	}

	private void startVoiceRecognitionActivity() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "BASIC! Speech To Text");
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	// ************************************** Timer Commands **************************************

	private boolean executeTIMER() {								// Get Timer command keyword if it is there
		return executeCommand(Timer_cmd, "Timer");
	}

	private boolean executeTIMER_SET() {
		if (theTimer != null) {
			return RunTimeError("Previous Timer Not Cleared");
		}

		if (OnTimerLine == 0) {
			return RunTimeError("No OnTimer: Label");
		}

		if (!evalNumericExpression()) return false;
		long interval = EvalNumericExpressionValue.longValue();
		if (interval < 100) {
			return RunTimeError("Interval Must Be >= 100");
		}

		if (!checkEOL()) return false;

		TimerTask tt = new TimerTask() {
			public void run() {
				// Delegate to the same runnable each time.
				toRunRepeatedly.run();
			}
		};

		timerExpired= false;
		timerStarting = true;
		theTimer = new Timer();
		theTimer.scheduleAtFixedRate (tt, 100, interval);

		return true;
	}

	Runnable toRunRepeatedly = new Runnable() {
		public void run() {
			if (timerStarting)
				timerStarting = false;
			else
				timerExpired= true;
		}
	};

	private boolean executeTIMER_CLEAR() {
		if (!checkEOL()) return false;

		if (theTimer != null) {
			theTimer.cancel();
			theTimer = null;
		}
		return true;
	}

	private boolean executeTIMER_RESUME() {
		if (interruptResume == -1) {
			return RunTimeError("No timer interrupt to reseume");
		}
		return doResume();
	}

	// *************************************** Home Command ***************************************

	private boolean executeHOME() {
		if (!checkEOL()) return false;

		moveTaskToBack(true);
		return true;
	}

	private boolean executeBACKGROUND_RESUME() {
		if (interruptResume == -1) {
			return RunTimeError("No background state change");
		}
		return doResume();
	}

} // End of Run
