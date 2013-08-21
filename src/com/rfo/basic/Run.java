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
    
    Apache Commons Net
    Copyright 2001-2011 The Apache Software Foundation

    This product includes software developed by
    The Apache Software Foundation (http://www.apache.org/).
    
	*************************************************************************************************/




package com.rfo.basic;

//Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " Line Buffer  " + ExecutingLineBuffer);

import android.util.DisplayMetrics;
import android.util.Log;

import static com.rfo.basic.Basic.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.Flushable;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.Iterator;
import java.util.List;
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

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

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

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import android.app.Activity;
import android.content.Intent;
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
import android.content.DialogInterface;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
//import android.content.ClipboardManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.SensorManager;
import android.text.ClipboardManager;
import android.util.Base64;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;

import org.apache.commons.net.ftp.*;
import android.widget.AdapterView.OnItemClickListener;

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
//  Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " Line Buffer  " + ExecutingLineBuffer);

	// ********************* Message types for the Handler *********************
																// message numbers < 256 are in "default" group 0
	public static final int MESSAGE_PUBLISH_PROGRESS = 1;
	public static final int MESSAGE_TOAST            = 2;		// carried over from BluetoothChatService, not presently used

	public static final int MESSAGE_GROUP_MASK       = 0x0F00;	// groups can be 0x1 through 0xF

	public static final int MESSAGE_DEFAULT_GROUP    = 0x0000;
	public static final int MESSAGE_BT_GROUP         = 0x0100;	// add this offset to messages from BlueTooth commands
	public static final int MESSAGE_HTML_GROUP       = 0x0200;	// add this offset to messages from HTML commands

	public static final int MESSAGE_DEBUG_GROUP      = 0x0F00;	// add this offset to messages from debug commands

	// ***************************** Command class *****************************

	public static class Command {						// Map a command key word string to its execution function
		public final String name;						// The command key word
		public Command(String name) { this.name = name; }
		public boolean run() { return false; }			// Run the command execution function
	}

	private boolean executeCommand(Command[] commands, String type){// If the current line starts with a key word in a command list
																	// execute the command. The "type" is used only to report errors.
		for (Command c : commands) {								// loop through the command list
			if (ExecutingLineBuffer.startsWith(c.name, LineIndex)) {// if there is a match
				LineIndex += c.name.length();						// move the line index to end of key word
				return c.run();										// run the function and report back
			}
		}
		RunTimeError("Unknown " + type + " command");
		return false;												// no key word found
	}

// **********  The variables for the Basic Key words ****************************    
    
    public static final String BasicKeyWords[]={
    	"rem", "dim", "let", 
    	"elseif", 									
    	"endif", "print", "input",
    	"if", "onerror","else","end",					// onerror here for Format
    	"for", "include", " ", "next",                  // to and step deleted
    	"goto", "gosub", "return",
    	"text.open","text.close","text.readln","text.writeln",
    	"while","repeat","do","until",
    	"onbackkey", 								    // onbackkey for Format	
    	"dir", "mkdir", "rename",				// same as file.dir, file.mkdir, file.rename
    	"file.delete",							//delete command deleted now undeleted
    	"sql.", " ", "gr.", "pause",
    	"browse", "inkey$", "audio.", 
    	"popup", "sensors.", "gps.",
    	"cls", "array.", "select",
    	"exit", "clipboard.get",
    	"clipboard.put",
    	"encrypt", "decrypt", "split.all",		// split.all new/2013-07-25 gt
    	"split", "byte.open", "byte.close",
    	"byte.read.byte", "byte.write.byte",
    	"graburl", "fn.def", "fn.rtn",
    	"fn.end", "byte.copy",
    	"sw.begin", "sw.case","sw.break",
    	"sw.default", "sw.end", "vibrate",
    	"kb.toggle", "kb.hide", "echo.on",
    	"echo.off", "text.input","file.size",
    	"file.dir", "file.mkdir", "file.rename",   // Same as dir, mkdir, rename
    	"file.root", "file.exists", "grabfile",
    	"wakelock", "tone", "list.", "bundle.",
    	"stack.", "socket.", "http.post", "device",
    	"debug.", "console.",
    	"tts.init","tts.speak","tts.local",
    	"ftp."," ","bt.",
    	"call", "su.", "system.",
    	"undim", "tget",
    	"f_n.break", "w_r.break", "d_u.break",
    	"text.position.get", "text.position.set",
    	"byte.position.get", "byte.position.set",
    	"byte.read.buffer", "byte.write.buffer",
    	"soundpool.", "myphonenumber", "headset",
    	"sms.send", "phone.call", "email.send",
    	"html.", "run", "@@@", "back.resume",
    	"notify", "swap", "sms.rcv.init",
    	"sms.rcv.next", "stt.listen", "stt.results",
    	"timer.", "timezone.", "time",				// moved three "timer" commands to Timer_cmd
    	" ", "key.resume", "menukey.resume",
    	"onmenukey","ontimer", "onkeypress",			// For Format
    	"ongrtouch", "onbtreadready",						// For Format
    	"home", "background.resume","onbackground",
    	"phone.rcv.init", "phone.rcv.next",
    	"read.data", "read.next", "read.from",
    	"onconsoletouch", "consoletouch.resume"
    	
    };
    

    public static final int BKWrem  = 0;		// Enumerated names for the key words
    public static final int BKWdim = 1;
    public static final int BKWlet = 2;
    public static final int BKWelseif = 3;
    public static final int BKWendif = 4;

    public static final int BKWprint = 5;
    public static final int BKWinput = 6;

    public static final int BKWif = 7;
    public static final int BKWonerror = 8;
    public static final int BKWelse = 9;
    public static final int BKWend = 10;

    public static final int BKWfor = 11;
    public static final int BKWinclude = 12;
    public static final int BKWstep = 13;
    public static final int BKWnext = 14;

    public static final int BKWgoto = 15;
    public static final int BKWgosub = 16;
    public static final int BKWreturn = 17;
    public static final int BKWtext_open = 18;
    public static final int BKWtext_close = 19;
    public static final int BKWtext_readln = 20;
    public static final int BKWtext_writeln = 21;
    public static final int BKWwhile = 22;
    public static final int BKWrepeat = 23;
    public static final int BKWdo = 24;
    public static final int BKWuntil = 25;
    public static final int BKWonbreak = 26;
    public static final int BKWdir = 27;
    public static final int BKWmkdir = 28;
    public static final int BKWrename = 29;
    public static final int BKWdelete = 30;
    public static final int BKWsql = 31;
    public static final int BKWnull32 = 32;             // Time moved to after Timer
    public static final int BKWgr = 33;
    public static final int BKWpause = 34;
    public static final int BKWbrowse = 35;
    public static final int BKWinkey = 36;
    public static final int BKWaudio = 37;
    public static final int BKWpopup = 38;
    public static final int BKWsensors = 39;
    public static final int BKWgps = 40;
    public static final int BKWcls = 41;
    public static final int BKWarray = 42;
    public static final int BKWselect = 43;
    public static final int BKWexit =44;
    public static final int BKWclipboard_get =45;
    public static final int BKWclipboard_put =46;
    public static final int BKWencrypt =47;
    public static final int BKWdecrypt =48;
    public static final int BKWsplit_all =49;
    public static final int BKWsplit =50;
    public static final int BKWbyte_open =51;
    public static final int BKWbyte_close =52;
    public static final int BKWbyte_read_byte =53;
    public static final int BKWbyte_write_byte =54;
    public static final int BKWgraburl = 55;
    public static final int BKWfn_def = 56;
    public static final int BKWfn_rtn = 57;
    public static final int BKWfn_end = 58;
    public static final int BKWbyte_copy = 59;
    public static final int BKWsw_begin = 60;
    public static final int BKWsw_case = 61;
    public static final int BKWsw_break = 62;
    public static final int BKWsw_default = 63;
    public static final int BKWsw_end = 64;
    public static final int BKWvibrate = 65;
    public static final int BKWkbshow = 66;
    public static final int BKWkbhide = 67;
    public static final int BKWecho_on = 68;
    public static final int BKWecho_off = 69;
    public static final int BKWtext_input = 70;
    public static final int BKWfile_size = 71;
    public static final int BKWfile_dir = 72;
    public static final int BKWfile_mkdir = 73;
    public static final int BKWfile_rename = 74;
    public static final int BKWfile_roots = 75;
    public static final int BKWfile_exists = 76;
    public static final int BKWgrabfile = 77;
    public static final int BKWwakelock = 78;
    public static final int BKWtone = 79;
    public static final int BKWlist = 80;
    public static final int BKWbundle = 81;
    public static final int BKWstack = 82;
    public static final int BKWsocket = 83;
    public static final int BKWhttp_post = 84;
    public static final int BKWdevice = 85;
    public static final int BKWdebug = 86;
    public static final int BKWconsole = 87;
    public static final int BKWtts_init = 88;
    public static final int BKWtts_speak = 89;
    public static final int BKWtts_local = 90;
    public static final int BKWftp = 91;
    public static final int BKWnull92 = 92;
    public static final int BKWbt = 93;
    public static final int BKWcall = 94;
    public static final int BKWsu = 95;
    public static final int BKWsystem = 96;
    public static final int BKWundim = 97;
    public static final int BKWtget = 98;
    public static final int BKWf_n_break = 99;
    public static final int BKWw_r_break = 100;
    public static final int BKWd_u_break = 101;
    public static final int BKWtext_position_get = 102;
    public static final int BKWtext_position_set = 103;
    public static final int BKWbyte_position_get = 104;
    public static final int BKWbyte_position_set = 105;
    public static final int BKWbyte_read_buffer = 106;
    public static final int BKWbyte_write_buffer = 107;
    public static final int BKWsound_pool = 108;
    public static final int BKWmy_phone_number = 109;
    public static final int BKWheadset = 110;
    public static final int BKWsms_send = 111;
    public static final int BKWphone_call = 112;
    public static final int BKWemail_send = 113;
    public static final int BKWhtml = 114;
    public static final int BKWrun = 115;
    public static final int BKWempty_program = 116;
    public static final int BKWback_resume = 117;
    public static final int BKWnotify = 118;
    public static final int BKWswap = 119;
    public static final int BKWsms_rcv_init = 120;
    public static final int BKWsms_rcv_next = 121;
    public static final int BKWstt_listen = 122;
    public static final int BKWstt_results = 123;
    public static final int BKWtimer = 124;
    public static final int BKWtimezone = 125;
    public static final int BKWtime = 126;             // Timer commands moved to Timer_cmd
    public static final int BKWnull127 = 127;
    public static final int BKWonkey_resume = 128;
    public static final int BKWmenukey_resume = 129;
    
    public static final int BKWonmenukey = BKWonerror;			//130    Dummy entries for Format
    public static final int BKWontimer = BKWonerror;			//131
    public static final int BKWonkeypress = BKWonerror;			//132
    public static final int BKWontouch = BKWonerror;			//133
    public static final int BKWonbtreadready = BKWonerror;		//134
    
    public static final int BKWhome = 135;
    public static final int BKWbackground_resume = 136;
    public static final int BKWonbackground = BKWonerror;		//137
    public static final int BKWphone_rcv_init = 138;
    public static final int BKWphone_rcv_next = 139;
    public static final int BKWread_data = 140;
    public static final int BKWread_next = 141;
    public static final int BKWread_from = 142;
    public static final int BKWonconsoletouch = BKWonerror;     //143
    public static final int BKWconsole_resume = 144;
    
    public static final int BKWnone= 198;
    public static final int SKIP = 199;
    
    public static int KeyWordValue = 0;            	// Will contain an enumerated key word value
    public static String PossibleKeyWord = "";	// Used when TO, STEP, THEN are expected

    // **************** The variables for the math function names ************************
    
    public static final String MathFunctions[] = {
    	"sin(", "cos(", "tan(",
    	"sqr(", "abs(", "rnd(",
    	"val(", "len(", "acos(",
    	"asin(", "atan2(", "ceil(",
    	"floor(", "mod(", "log(",
    	"round(", "toradians(", "todegrees(",
    	"time(", "exp(",
    	"is_in(", "clock(", 
    	"bor(", "band(", "bxor(",
    	"gr_collision(",
    	"ascii(", "starts_with(", "ends_with(",
    	"hex(", "oct(", "bin(", "shift(",
    	"randomize(", "background(",
    	"atan(", "cbrt(", "cosh(", "hypot(",
    	"sinh(", "pow(", "log10(",
    	"ucode(", "pi(", "min(", "max(",		// pi, min, max: new/2013-07-29 gt
    };
    
    public static final int MFsin = 0;			// Enumerated name for the Match Functions
    public static final int MFcos = 1;
    public static final int MFtan = 2;
    public static final int MFsqr = 3;
    public static final int MFabs = 4;
    public static final int MFrnd = 5;
    public static final int MFval = 6;
    public static final int MFlen = 7;
    public static final int MFacos = 8;
    public static final int MFasin = 9;
    public static final int MFatan2 = 10;
    public static final int MFceil = 11;
    public static final int MFfloor = 12;
    public static final int MFmod = 13;
    public static final int MFlog = 14;
    public static final int MFround = 15;
    public static final int MFtoradians = 16;
    public static final int MFtodegrees = 17;
    public static final int MFtime = 18;
    public static final int MFexp = 19;
    public static final int MFis_in = 20;
    public static final int MFclock = 21;
    public static final int MFbor = 22;
    public static final int MFband = 23;
    public static final int MFbxor = 24;
    public static final int MFcollision = 25;
    public static final int MFascii = 26;
    public static final int MFstarts_with = 27;
    public static final int MFends_with = 28;
    public static final int MFhex = 29;
    public static final int MFoct = 30;
    public static final int MFbin = 31;
    public static final int MFshift = 32;
    public static final int MFrandomize = 33;
    public static final int MFbackground = 34;
    public static final int MFatan = 35;
    public static final int MFcbrt = 36;
    public static final int MFcosh = 37;
    public static final int MFhypot = 38;
    public static final int MFsinh = 39;
    public static final int MFpow = 40;
    public static final int MFlog10 = 41;
    public static final int MFucode = 42;
    public static final int MFpi = 43;				// pi new/2013-07-29 gt
    public static final int MFmin = 44;				// min new/2013-07-29 gt
    public static final int MFmax = 45;				// max new/2013-07-29 gt

    public static  int MFNumber = 0;				// Will contain a math function's enumerated name value
    
    //*********************** The variables for the Operators  ************************
 
    public static final String OperatorString[]={
    	"<=", "<>", ">=", ">", "<",
    	 "=","^", "*", "+", "-",
    	"/", "!", "|", "&", "(",
    	")", "=", "+", "-"," ",
    	"\n"
    	};
    	
    public static final int LE = 0;					// Enumerated names of operators
    public static final int NE = 1;
    public static final int GE = 2;
    public static final int GT = 3;
    public static final int LT = 4;

    public static final int LEQ = 5;
    public static final int EXP = 6;
    public static final int MUL = 7;
    public static final int PLUS = 8;
    public static final int MINUS = 9;
    
    public static final int DIV = 10;
    public static final int NOT = 11;
    public static final int OR = 12;
    public static final int AND = 13;
    public static final int LPRN = 14;
    
    public static final int RPRN = 15;
    public static final int ASSIGN = 16;
    public static final int UPLUS = 17;
    public static final int UMINUS = 18;

    public static final int SOE = 19;
    public static final int EOL = 20;
    public static final int FLPRN = 21;

    public static final int GoesOnPrecedence[]={     // Precedence for going onto stack
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
        15, 15, 15};
    
    public static final int ComesOffPrecedence[]={		// Precedence for coming off stack
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
    
    public static int OperatorValue = 0 ;           // Will hold enumerated operator name value
    
    //**********************  The variables for the string functions  *******************
    
    public static final String StringFunctions[] = {
    	"left$(", "mid$(", "right$(",
    	"str$(", "upper$(", "lower$(",
    	"format$(", "chr$(", "version$(",
    	"replace$(", "hex$(", "oct$(",
    	"bin$(", "geterror$(", "word$("
    };
    public static  int SFNumber = 0;					// Will hold enumerated string function name value
    
    public static final int SFleft = 0;					// Enumerated string function name values
    public static final int SFmid = 1;
    public static final int SFright = 2;
    public static final int SFstr = 3;
    public static final int SFupper = 4;
    public static final int SFlower = 5;
    public static final int SFformat = 6;
    public static final int SFchr = 7;
    public static final int SFversion = 8;
    public static final int SFreplace = 9;
    public static final int SFhex = 10;
    public static final int SFoct= 11;
    public static final int SFbin =12;
    public static final int SFgeterror =13;
    public static final int SFword =14;
    
    // *****************************   Various execution control variables *********************
    
    public static final int BASIC_GENERAL_INTENT = 255;
    public static Random randomizer;
    public static boolean background = false;
    public static String errorMsg;
    public static boolean kbShown = false;
    
    public static int OnErrorLine =0 ;              // Line number for OnError: label
    public static int OnBackKeyLine=0;
    public static boolean BackKeyHit = false;
    public static int OnMenuKeyLine = 0;
    public static boolean MenuKeyHit = false;
    public static boolean bgStateChange = false;
    public static int OnBGLine = 0;
    public static int onCTLine = 0;
    public static boolean ConsoleTouched = false;
    public static int interruptResume = -1 ;
 
	
    public static int LineIndex = 0;				// Current displacement into ExecutingLineBuffer
    public static String ExecutingLineBuffer ="";		// Holds the current line being executed
    public static int ExecutingLineIndex = 0;		// Points to the current line in Basic.lines
    public static boolean SEisLE = false;				// If a String expression result is a logical expression

    public static Stack<Integer> GosubStack;			// Stack used for Gosub/Return
    public static Stack<Bundle> ForNextStack;			// Stack used for For/Next
    public static Stack<Integer> WhileStack;			// Stack used for While/Repeat
    public static Stack<Integer> DoStack;				// Stack used for Do/Until
    
    public static Stack <Integer> IfElseStack;			// Stack for IF-ELSE-ENDIF operations
    public static final Integer IEskip1 = 1;			// Skip statements until ELSE, ELSEIF or ENDIF
    public static final Integer IEskip2 = 2;			// Skip to until ENDIF
    public static final Integer IEexec = 3;				// Execute to ELSE, ELSEIF or ENDIF
    public static final Integer IEinterrupt = 4;
   
    public static Double GetNumberValue = (double)0;				// Return value from GetNumber()
    public static Double EvalNumericExpressionValue = (double)0;	// Return value from EvalNumericExprssion()
    Vibrator myVib;

    public  static ListView lv ;							// The output screen list view
    public static ArrayList<String> output;					// The output screen text lines
    public static ColoredTextAdapter AA;					// The output screen array adapter
    public static final int MaxTempOutput = 500;
    public static String TempOutput[] = new String[MaxTempOutput];	// Holds waiting output screen line
    public static int TempOutputIndex = 0;					// Index to next available TempOutput entry

    public static Background theBackground;					// Background task ID
    public static boolean SyntaxError = false;		        // Set true when Syntax Error message has been output		
    
    public static String UserPrompt = "";
    public static String theInput = "";						// Holds Input from Input dialog box
    public Context RunContext;	
    
    
    public boolean WaitForInput ;							// Signals between background task and foreground
    public boolean BadInput = false;
    public boolean InputCancelled = false;
    public boolean InputDismissed = false;
    public AlertDialog.Builder InputDialog;
    public AlertDialog theAlertDialog;
    public boolean InputIsNumeric = true;
    public String InputDefault = "";
    public int inputVarNumber;
    private boolean ProgressPending = false;
	
	// debugger dialog and ui thread vars
	private boolean WaitForResume = false ; 
	private boolean DebuggerStep = false;
	private boolean DebuggerHalt = false;
	private boolean WaitForSwap = false;
	private boolean WaitForSelect = false;
	private boolean dbSwap = false;
	private boolean dbSelect = false;
	private AlertDialog.Builder DebugDialog;
	private AlertDialog theDebugDialog;
	private AlertDialog.Builder DebugSwapDialog;
	private AlertDialog theDebugSwapDialog;
	private AlertDialog.Builder DebugSelectDialog;
	private AlertDialog theDebugSelectDialog;
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
    
    public static boolean GrStop = false;						// Signal from GR that it has died
    public static boolean Stop = false;	   						// Stops program from running
    public static boolean GraphicsPaused = false;               // Signal from GR that it has been paused
    public static boolean RunPaused = false;                    // Used to control the media player
    public static boolean StopDisplay = false;
    public static boolean DisplayStopped = false;
	private String PrintLine = "";						// Hold the Print line currently being built
	private String textPrintLine = "";					// Hold the TextPrint line currently being built
	private boolean PrintLineReady = false;				// Signals a line is ready to print or write
    
    public static InputMethodManager IMM;
	private ArrayList<String> LabelNames;				// A list of all the label names found in the program
	private ArrayList<Integer> LabelValues;				// The line numbers associated with Label Names

	private ArrayList<String> VarNames ;				// Each entry has the variable name string
	private ArrayList<Integer> VarIndex;				// Each entry is an index into
    													// NumberVarValues or
    													// StringVarValues or
    													// ArrayTable or
    													// FunctionTable
	private int VarNumber = 0;							// An index for both VarNames and NVarValue
	public static ArrayList<Double> NumericVarValues;	// if a Var is a number, the VarIndex is an
														// index into this list. The values of numeric
														// array elements are also kept here
	private ArrayList<String> StringVarValues;			// if a Var is a string, the VarIndex is an
    													// index into this list. The values of string
    													// array elements are also kept here
	private ArrayList<Bundle> ArrayTable;				//Each DIMed array has an entry in this table
	private String StringConstant = "";					// Storage for a string constant
	private int theValueIndex;							// The index into the value table for the current var
	private int ArrayValueStart = 0;					// Value index for newly created array 

	private boolean VarIsFunction = false;				// Flag set by getVar() when var is a user function
	private boolean DoingDef = false;
	private ArrayList<Bundle> FunctionTable;      		// A bundle is created for each defined function
	private Bundle ufBundle;							// Bundle set by isUserFunction and used by doUserFunction
	private Stack<Bundle> FunctionStack;				// Stack contains the currently executing functions
	private int VarSearchStart;							// Used to limit search for var names to executing function vars
	private boolean fnRTN = false;						// Set true by fn.rtn. Cause RunLoop() to return
	private int scOpValue;								// An instance variable that needs to be saved when executing function

	private boolean doingDim = false;					// Signal to get Var that un dimed array var is ok
	private boolean unDiming = false;					// Signal to get Var that an array is being undimed
	private boolean SkipArrayValues = false;			// Set true for some array.xxx commands
	private boolean FindingLabels = false;				// Signal to get var to report var is label if it is
	private boolean VarIsNew = true;					// Signal from get var that this var is new
	private boolean VarIsNumeric = true;				// if false, Var is string
	private boolean VarIsArray = false;					// if true, Var is an Array
    													// if the Var is any array, the VarIndex it
    													// and index into ArrayTable
    
    ClipboardManager clipboard; 
    
    
    // ******************************** Wakelock variables *********************************
    
    public static PowerManager.WakeLock theWakeLock;
    public static final int partial = 1;
    public static final int dim = 2;
    public static final int bright = 3;
    public static final int full = 4;
    public static final int release = 5;

    
    // ******************************* File I/O operation variables  **************************

    public static int FMR = 0;			// File Mode Read
    public static int FMW = 1;			// File Mode Write
    public static int FMRW = 2;			// File Mode Read-Write
    
    public static ArrayList<Bundle> FileTable;			// File table list
    public static ArrayList<BufferedReader> BRlist;		// A list of of file readers
    public static ArrayList<FileWriter> FWlist;			// A list of file writers
    public static ArrayList<DataOutputStream> DOSlist;			// A list of file writers
    public static ArrayList<BufferedInputStream> BISlist;			// A list of file writers

    //  ******************  Console Command variables ********************************

    public static final String Console_KW[] = {			// Console commands
    	"front", "save", "title", "line.new", "line.char"
    };

	private final Command[] Console_cmd = new Command[] {	// Map console command key words to their execution functions
		new Command("front")            { public boolean run() { return executeCONSOLE_FRONT(); } },
		new Command("save")             { public boolean run() { return executeCONSOLE_DUMP(); } },
		new Command("title")            { public boolean run() { return executeCONSOLE_TITLE(); } },
		new Command("line.new")         { public boolean run() { return executeCONSOLE_LINE_NEW(); } },
		new Command("line.char")        { public boolean run() { return executeCONSOLE_LINE_CHAR(); } }
	};

    private String ConsoleTitle = null;					// null means use default string resource

    //  ******************  Popup Command variables ********************************
    
    public static String ToastMsg;
    public static int ToastX;
    public static int ToastY;
    public static int ToastDuration;
    
    //  ******************* Variables for the SELECT Command ***********************
    
    public static ArrayList<String> SelectList ;        // The list of things to select from
    public static int SelectedItem;						// The index of the selected item
    public static boolean ItemSelected;                 // Signal from Select.java saying an item has been selected 
    public static String SelectMessage ; 				// The toast message for Select.java to display
    public static boolean SelectLongClick ;             // True if long click
    
    // *********************  SQL Variables  **************************************
    
    public static final String SQL_KW[] = {				// SQL Commands
    	"open", "close", "insert",
    	"query.length", "query.position", "query",
    	"next", "delete", "update", "exec",
    	"raw_query", "drop_table", "new_table"
    };

	private final Command[] SQL_cmd = new Command[] {	// Map SQL command key words to their execution functions
		new Command("open")             { public boolean run() { return execute_sql_open(); } },
		new Command("close")            { public boolean run() { return execute_sql_close(); } },
		new Command("insert")           { public boolean run() { return execute_sql_insert(); } },
		new Command("query.length")     { public boolean run() { return execute_sql_query_length(); } },
		new Command("query.position")   { public boolean run() { return execute_sql_query_position(); } },
		new Command("query")            { public boolean run() { return execute_sql_query(); } },
		new Command("next")             { public boolean run() { return execute_sql_next(); } },
		new Command("delete")           { public boolean run() { return execute_sql_delete(); } },
		new Command("update")           { public boolean run() { return execute_sql_update(); } },
		new Command("exec")             { public boolean run() { return execute_sql_exec(); } },
		new Command("raw_query")        { public boolean run() { return execute_sql_raw_query(); } },
		new Command("drop_table")       { public boolean run() { return execute_sql_drop_table(); } },
		new Command("new_table")        { public boolean run() { return execute_sql_new_table(); } }
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
    
    // ********************************* Variables for text.input command ********************
    
    public static String TextInputString = "";
    public static boolean HaveTextInput;
    
    // ******************************** Graphics Declarations **********************************
    
    public static Intent GRclass ;						// Graphics Intent Class
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
    public static int ShowStatusBar;
    public static int GRorientation;
    public static boolean Touched;
    public static double TouchX[] = {0,0,0};
    public static double TouchY[] = {0,0,0};
    public static boolean NewTouch[] = {false, false, false};
    public static int OnTouchLine;
    public static Canvas drawintoCanvas = null;

    
    public static final String GR_KW[] = {
    	"open", "render", "color", "line", "rect",
    	"arc", "circle", "oval", "cls", "hide",
    	 "show", "touch2", "text.draw", "text.size",
    	 "text.align", "text.underline", "text.skew",
    	 "text.bold", "text.strike",
    	 "bitmap.load", "get.position", "rotate.start",
    	 "rotate.end", "modify", "orientation",
    	 "screen.to_bitmap", "close", "bitmap.scale",
    	 "front", "bounded.touch2", "bitmap.size",
    	 "bitmap.delete", "set.pixels", "get.pixel",
    	 "save", "text.width", "scale", "newdl",
    	 "clip", "bitmap.crop", "set.stroke",
    	 "poly", "statusbar.show","touch",
    	 "bounded.touch", "bitmap.save",
    	 "camera.shoot", "screen", "camera.autoshoot",
    	 "camera.manualshoot", "paint.get", "brightness",
    	 "bitmap.create", "bitmap.drawinto.start",
    	 "bitmap.drawinto.end", "bitmap.draw",
    	 "get.bmpixel", "get.value", "set.antialias",
    	 "get.textbounds", "text.typeface", "ongrtouch.resume",
    	 "camera.select", "getdl"
    
    };

    public static final int gr_open = 0;				// Graphics Commands enums
    public static final int gr_render = 1;
    public static final int gr_color = 2;
    public static final int gr_line = 3;
    public static final int gr_rect = 4;
    public static final int gr_arc = 5;
    public static final int gr_circle = 6;
    public static final int gr_oval = 7;
    public static final int gr_cls = 8;
    public static final int gr_hide = 9;
    public static final int gr_show = 10;
    public static final int gr_touch2 = 11;
    public static final int gr_text_draw =12;
    public static final int gr_text_size =13;
    public static final int gr_text_align =14;
    public static final int gr_text_underline =15;
    public static final int gr_text_skew =16;
    public static final int gr_text_bold =17;
    public static final int gr_text_strike =18;
    public static final int gr_bitmap_load =19;
    public static final int gr_get_position = 20;
    public static final int gr_rotate_start = 21;
    public static final int gr_rotate_end = 22;
    public static final int gr_modify = 23;
    public static final int gr_orientation = 24;
    public static final int gr_screen_to_bitmap = 25;
    public static final int gr_close = 26;
    public static final int gr_bitmap_scale = 27;
    public static final int gr_front = 28;
    public static final int gr_bound_touch2 = 29;
    public static final int gr_bitmap_size = 30;
    public static final int gr_bitmap_delete = 31;
    public static final int gr_set_pixels = 32;
    public static final int gr_get_pixel = 33;
    public static final int gr_save = 34;
    public static final int gr_text_width = 35;
    public static final int gr_scale = 36;
    public static final int gr_newdl = 37;
    public static final int gr_clip = 38;
    public static final int gr_bitmap_crop = 39;
    public static final int gr_stroke_width = 40;
    public static final int gr_poly = 41;
    public static final int gr_statusbar_show = 42;
    public static final int gr_touch = 43;
    public static final int gr_bound_touch = 44;
    public static final int gr_bitmap_save = 45;
    public static final int gr_camera_shoot = 46;
    public static final int gr_screen = 47;
    public static final int gr_camera_autoshoot = 48;
    public static final int gr_camera_manualshoot = 49;
    public static final int gr_paint_get = 50;
    public static final int gr_brightness =51;
    public static final int gr_bitmap_create =52;
    public static final int gr_bitmap_drawinto_start =53;
    public static final int gr_bitmap_drawinto_end  =54;
    public static final int gr_bitmap_draw = 55;    
    public static final int gr_get_bmpixel = 56;
    public static final int gr_get_value = 57;
    public static final int gr_antialias = 58;
    public static final int gr_get_textbounds = 59;
    public static final int gr_text_typeface = 60;
    public static final int gr_ontouch_resume = 61;
    public static final int gr_camera_select = 62;
    public static final int gr_getdl = 63;
    
    public static final int gr_none = 98;
    
    // ******************************** Variables for Audio commands ****************************

    public static final String Audio_KW[] ={
    	"load", "play", "loop", "stop",
    	"volume", "position.current", "position.seek",
    	"length", "release", "pause",
    	"isdone", "record.start", "record.stop"
    };
    
    public static final int audio_load = 0;
    public static final int audio_play = 1;
    public static final int audio_loop = 2;
    public static final int audio_stop = 3;
    public static final int audio_volume = 4;
    public static final int audio_pcurrent = 5;
    public static final int audio_pseek = 6;
    public static final int audio_length = 7;
    public static final int audio_release = 8;
    public static final int audio_pause = 9;
    public static final int audio_isdone = 10;
    public static final int audio_record_start = 11;
    public static final int audio_record_stop = 12;
    public static final int audio_none = 98;
    
    public static Uri theURI = null;
    public static MediaPlayer theMP = null;
    public static String MPfilename ="";
    public static ArrayList<MediaPlayer> theMPList;
    public static ArrayList<Uri> theMpUriList;
    public static ArrayList<Integer> theMpResIDList;
    public static boolean seekComplete;
    public static boolean PlayIsDone;
    public MediaRecorder mRecorder = null;    
    // ******************************* Variables for Sensor Commands **********************************

    private SensorActivity theSensors;
    
    public static final String Sensors_KW[] = {
    	"list","open","read","close", "rotate"
    };
    
    public static final int sensors_list = 0;
    public static final int sensors_open = 1;
    public static final int sensors_read = 2;
    public static final int sensors_close = 3;
    public static final int sensors_rotate = 4;
    public static final int sensors_none = 98;
    
    // ***********************  Variables for GPS Commands  ******************************************
    
    public static final String GPS_KW[] = {
    	"altitude", "latitude", "longitude",
    	"bearing", "accuracy", "speed",
    	"provider", "open", "close", "time"
    };
    
    public static final int gps_altitude = 0;
    public static final int gps_latitude = 1;
    public static final int gps_longitude = 2;
    public static final int gps_bearing = 3;
    public static final int gps_accuracy = 4;
    public static final int gps_speed = 5;
    public static final int gps_provider = 6;
    public static final int gps_open = 7;
    public static final int gps_close = 8;
    public static final int gps_time = 9;
    
    public GPS theGPS;
	
	// ************************* Variables for Array Commands
	
	public static final String Array_KW[] = {
		"length", "load", "sort",
		"sum", "average", "reverse",
		"shuffle", "min","max", "delete",
		"variance", "std_dev", "copy"
	};
	
	public static final int array_length = 0;
	public static final int array_load = 1;
	public static final int array_sort = 2;
	public static final int array_sum = 3;
	public static final int array_average = 4;
	public static final int array_reverse = 5;
	public static final int array_shuffle = 6;
	public static final int array_min = 7;
	public static final int array_max = 8;
	public static final int array_delete = 9;
	public static final int array_variance = 10;
	public static final int array_std_dev = 11;
	public static final int array_copy = 12;	
	public static final int array_none =99;
	
	public static boolean DoAverage;
	public static boolean DoReverse;
	public static boolean DoShuffle;
	public static boolean DoVariance;
	public static boolean DoStdDev;
	public static boolean DoSort;
	public static boolean DoMin;
	public static boolean DoMax;
	public static boolean DoSum;
	
	public static long sTime;
	
// ************************************ List command variables *********************************

	public static final String List_KW[] = {
		"create", "add.list", "add.array", "add", "replace", 
		"type","get", "clear", "remove", "insert", "size",
		"contains", "toarray", "search"
	};
	
	public static final int list_new = 0;
	public static final int list_addlist = 1;
	public static final int list_addarray = 2;
	public static final int list_add = 3;
	public static final int list_set = 4;
	public static final int list_gettype = 5;
	public static final int list_get = 6;
	public static final int list_clear = 7;
	public static final int list_remove = 8;
	public static final int list_insert = 9;
	public static final int list_size = 10;
	public static final int list_contains = 11;
	public static final int list_toarray = 12;
	public static final int list_search = 13;
	public static final int list_none = 99;
	
	public static final int list_is_numeric = 1;
	public static final int list_is_string = 0;

	public static ArrayList <ArrayList> theLists;
	public static ArrayList <Integer> theListsType;  
	
// ************************************ Bundle Variables ****************************************
	
	public static final String Bundle_KW[]= {
		"create", "put", "get", "type",
		"keys", "copy", "clear", "contain"
	};
	
	public static final int bundle_create = 0;
	public static final int bundle_put = 1;
	public static final int bundle_get = 2;
	public static final int bundle_type = 3;
	public static final int bundle_keyset = 4;
	public static final int bundle_copy = 5;
	public static final int bundle_clear = 6;
	public static final int bundle_contain = 7;         // ******* El Condor	
	
	public static ArrayList <Bundle> theBundles;
	
// *********************************** Stack Variables **********************************************
	public static final String Stack_KW[]= {
		"create", "push", "pop", "peek",
		"type", "isempty", "clear"
	};
	
	public static final int stack_create = 0;
	public static final int stack_push = 1;
	public static final int stack_pop = 2;
	public static final int stack_peek = 3;
	public static final int stack_type = 4;
	public static final int stack_isempty = 5;
	public static final int stack_clear = 6;
	
	public static ArrayList<Stack> theStacks;
	public static ArrayList <Integer> theStacksType; 
	
	public static final int stack_is_numeric = 1;
	public static final int stack_is_string = 0;

//  ******************************* Socket Variables **************************************************

	public static final String Socket_KW[] = {
		"myip", "client.connect", "client.status",
		"client.read.ready", "client.read.line",
		"client.write.line", "client.write.bytes",
		"client.close", "client.server.ip",
		"client.read.file", "client.write.file",
		"server.create", "server.connect", "server.status",
		"server.read.ready", "server.read.line",
		"server.write.line", "server.write.bytes",
		"server.disconnect", "server.close", "server.client.ip",
		"server.read.file", "server.write.file"
	};

	private final Command[] Socket_cmd = new Command[] {	// Map Socket command key words to their execution functions
		new Command("client.")          { public boolean run() { return executeSocketClient(); } },
		new Command("server.")          { public boolean run() { return executeSocketServer(); } },
		new Command("myip")             { public boolean run() { return executeMYIP(); } }
	};
                                    
	private final Command[] SocketClient_cmd = new Command[] {	// Map Socket client command key words to their execution functions
		new Command("connect")          { public boolean run() { return executeCLIENT_CONNECT(); } },
		new Command("status")           { public boolean run() { return executeCLIENT_STATUS(); } },
		new Command("read.ready")       { public boolean run() { return executeCLIENT_READ_READY(); } },
		new Command("read.line")        { public boolean run() { return executeCLIENT_READ_LINE(); } },
		new Command("write.line")       { public boolean run() { return executeCLIENT_WRITE_LINE(); } },
		new Command("write.bytes")      { public boolean run() { return executeCLIENT_WRITE_BYTES(); } },
		new Command("close")            { public boolean run() { return executeCLIENT_CLOSE(); } },
		new Command("server.ip")        { public boolean run() { return executeCLIENT_SERVER_IP(); } },
		new Command("read.file")        { public boolean run() { return executeCLIENT_GETFILE(); } },
		new Command("write.file")       { public boolean run() { return executeCLIENT_PUTFILE(); } }
	};

	private final Command[] SocketServer_cmd = new Command[] {	// Map Socket server command key words to their execution functions
		new Command("create")           { public boolean run() { return executeSERVER_CREATE(); } },
		new Command("connect")          { public boolean run() { return executeSERVER_ACCEPT(); } },
		new Command("status")           { public boolean run() { return executeSERVER_STATUS(); } },
		new Command("read.ready")       { public boolean run() { return executeSERVER_READ_READY(); } },
		new Command("read.line")        { public boolean run() { return executeSERVER_READ_LINE(); } },
		new Command("write.line")       { public boolean run() { return executeSERVER_WRITE_LINE(); } },
		new Command("write.bytes")      { public boolean run() { return executeSERVER_WRITE_BYTES(); } },
		new Command("disconnect")       { public boolean run() { return executeSERVER_DISCONNECT(); } },
		new Command("close")            { public boolean run() { return executeSERVER_CLOSE(); } },
		new Command("client.ip")        { public boolean run() { return executeSERVER_CLIENT_IP(); } },
		new Command("read.file")        { public boolean run() { return executeSERVER_GETFILE(); } },
		new Command("write.file")       { public boolean run() { return executeSERVER_PUTFILE(); } }
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

    private Socket theClientSocket ;
    private BufferedReader ClientBufferedReader ;
    private PrintWriter ClientPrintWriter ;
	
    private ServerSocket newSS;
    private SocketConnectThread serverSocketConnectThread; 
    private Socket theServerSocket ;
    private BufferedReader ServerBufferedReader ;
    private PrintWriter ServerPrintWriter ;
	
	// *************************************************** Debug Commands ****************************
	
	public static final String Debug_KW[] = {
		"on","off","print","echo.on",
		"echo.off", "dump.scalars",
		"dump.array", "dump.list",
		"dump.stack", "dump.bundle",
		"watch.clear","watch", "show.scalars",
		"show.array","show.list","show.stack",
		"show.bundle","show.watch","show.program",
		"show","console"
	};
	
	public static final int debug_on = 0;
	public static final int debug_off = 1;
	public static final int debug_print = 2;
	public static final int debug_echo_on = 3;
	public static final int debug_echo_off = 4;
	public static final int debug_dump_scalars = 5;
	public static final int debug_dump_array = 6;
	public static final int debug_dump_list = 7;
	public static final int debug_dump_stack = 8;
	public static final int debug_dump_bundle = 9;
	public static final int debug_watch_clear = 10;
	public static final int debug_watch = 11;
	public static final int debug_show_scalars = 12;
	public static final int debug_show_array = 13;
	public static final int debug_show_list = 14;
	public static final int debug_show_stack = 15;
	public static final int debug_show_bundle = 16;
	public static final int debug_show_watch = 17;
	public static final int debug_show_program = 18;
	public static final int debug_show = 19;
	public static final int debug_console = 20;
	public static final int debug_none = 99;
	
	public static boolean Debug = false;
	public static boolean Echo = false;
	
	// *********************************************** Text to Speech *******************************
	
	public static TextToSpeech mTts;
	public static boolean ttsInit;
	public static int ttsInitResult;
	public static Intent ttsIntent; 
	public static boolean ttsSpeakDone;
	
	// *********************************************** FTP Client *************************************
	
    public static final String ftp_KW[] = {				// SQL Commands
    	"open", "close", "dir", "cd",
    	"get", "put", "delete", "rmdir",
    	"mkdir", "rename"
    };
    
    public static final int ftp_open = 0;
    public static final int ftp_close = 1;
    public static final int ftp_dir = 2;
    public static final int ftp_cd = 3;
    public static final int ftp_get = 4;
    public static final int ftp_put = 5;
    public static final int ftp_delete = 6;
    public static final int ftp_rmdir = 7;
    public static final int ftp_mkdir = 8;
    public static final int ftp_rename = 9;
    

	public FTPClient mFTPClient = null;
	public String FTPdir = null;
	
	// *********************************************** Camera *****************************************
	
	public static Bitmap CameraBitmap;
	public static boolean CameraDone;
	public static boolean CameraAuto;
	public static boolean CameraManual;
	public static int CameraFlashMode;
	public static int CameraNumber;
	
	// ***************************************  Bluetooth  ********************************************
	
    // Message types sent from the BluetoothChatService Handler
    public static final int MESSAGE_STATE_CHANGE = MESSAGE_BT_GROUP + 1;
    public static final int MESSAGE_READ         = MESSAGE_BT_GROUP + 2;
    public static final int MESSAGE_WRITE        = MESSAGE_BT_GROUP + 3;
    public static final int MESSAGE_DEVICE_NAME  = MESSAGE_BT_GROUP + 4;

    // Key names received from the BluetoothChatService Handler
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
    
    public static Looper btLooper;

    public static final String bt_KW[] = {				// Bluetooth Commands
    	"open", "close", "status", 
    	"connect", "device.name",
    	"write", "read.ready", "read.bytes",
    	"set.uuid", "listen", "reconnect",
    	"onreadready.resume", "disconnect"
    };
    
    public static final int bt_open = 0;
    public static final int bt_close = 1;
    public static final int bt_status = 2;
    public static final int bt_connect = 3;
    public static final int bt_device_name = 4;
    public static final int bt_write = 5;
    public static final int bt_read_ready = 6;
    public static final int bt_read_bytes = 7;
    public static final int bt_set_uuid = 8;
    public static final int bt_listen = 9;
    public static final int bt_reconnect = 10;
    public static final int bt_readready_resume = 11;
    public static final int bt_disconnect = 12;
    
/**************************************  Superuser and System  ***************************/

    public static final String su_KW[] = {
    	"open", "write", "read.ready",
    	"read.line", "close"
    	
    };
    public static final String[] System_KW = su_KW;

	private final Command[] SU_cmd = new Command[] {	// Map SU/System command key words to their execution functions
		new Command("open")             { public boolean run() { return execute_SU_open(); } },
		new Command("write")            { public boolean run() { return execute_SU_write(); } },
		new Command("read.ready")       { public boolean run() { return execute_SU_read_ready(); } },
		new Command("read.line")        { public boolean run() { return execute_SU_read_line(); } },
		new Command("close")            { public boolean run() { return execute_SU_close(); } }
	};

    private boolean isSU = true;						// set true for SU commands, false for System commands
    private DataOutputStream SUoutputStream;
    private DataInputStream SUinputStream;
    private Process SUprocess;
    private ArrayList <String> SU_ReadBuffer;
    private SUReader theSUReader = null;
    
/***************************************  SOUND POOL  ************************************/
    
    public static final String sp_KW[] = {
    	"open", "load","play", "stop",
    	"unload", "pause", "resume", 
    	"release", "setvolume","setpriority",
    	"setloop", "setrate"
    };
    
    public static final int sp_open = 0;
    public static final int sp_load = 1;
    public static final int sp_play = 2;
    public static final int sp_stop = 3;
    public static final int sp_unload = 4;
    public static final int sp_pause = 5;
    public static final int sp_resume = 6;
    public static final int sp_release = 7;
    public static final int sp_setvolume = 8;
    public static final int sp_setpriority = 9;
    public static final int sp_setloop = 10;
    public static final int sp_setrate = 11;
    
    public static SoundPool theSoundPool ;
    
    // **************** Headset Vars **************************************
    
    int headsetState;
    String headsetName;
    int headsetMic;
    
    //******************* html Vars ******************************************
    
    public static final String html_KW[]= {
    	"open", "load.url", "load.string",
    	"get.datalink", "close" , "go.back",
    	"go.forward", "clear.cache", 
    	"clear.history", "post"
    };
    
    public static final int html_open = 0;
    public static final int html_load_url = 1;
    public static final int html_load_string = 2;
    public static final int html_get_datalink = 3;
    public static final int html_close = 4;
    public static final int html_go_back = 5;
    public static final int html_go_forward = 6;
    public static final int html_clear_cache = 7;
    public static final int html_clear_history = 8;
    public static final int html_post = 9;
    
    Intent htmlIntent;
    public static ArrayList <String> htmlData_Buffer;
    public boolean htmlOpening;
    public String htmlCmd;
    public static String htmlPostString;
    
    public static boolean Notified;
   
    //********************* SMS Receive Vars ***********************************

    public static ArrayList <String> smsRcvBuffer;
    
    // ******************** Speech to text Vars ********************************
    
    public static final int  VOICE_RECOGNITION_REQUEST_CODE = 1234;
    public static ArrayList <String> sttResults;
    public static boolean sttListening;
    public static boolean sttDone;
    
    // ******************** Timer Variables *******************************

	public static final String Timer_KW[] = {
		"set", "clear", "resume"
	};

	private final Command[] Timer_cmd = new Command[] {		// Map Timer command key words to their execution functions
		new Command("set")              { public boolean run() { return executeTIMER_SET(); } },
		new Command("clear")            { public boolean run() { return executeTIMER_CLEAR(); } },
		new Command("resume")           { public boolean run() { return executeTIMER_RESUME(); } }
	};

    public static int OnTimerLine;
    public static Timer theTimer;
    public static boolean timerExpired;
    public static boolean timerStarting;

    // ******************** TimeZone Variables *******************************
    
    public static final String TimeZone_KW[] = {
    	"set", "get", "list"
    };

	private final Command[] TimeZone_cmd = new Command[] {		// Map TimeZone command key words to their execution functions
		new Command("set")              { public boolean run() { return executeTIMEZONE_SET(); } },
		new Command("get")              { public boolean run() { return executeTIMEZONE_GET(); } },
		new Command("list")             { public boolean run() { return executeTIMEZONE_LIST(); } }
    };

    public String theTimeZone = "";

    //********************** Phone RCV variables *************************
    
	public static int phoneState = 0;
	public static String phoneNumber = "";
	public static boolean phoneRcvInited = false;
	public static TelephonyManager mTM;
	
	// ********************* READ variables *****************************
	
	public static int readNext =0;
	public static ArrayList <Bundle> readData;
	    
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

/*************************************** Colored Text Adapter ********************************/
    
    
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
                text.setTypeface(Settings.getConsoleTypeface(context));
                text.setTextColor(textColor);
                text.setText(list.get(position));
                text.setBackgroundColor(backgroundColor);

                return row;
        }
        
    }

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
    	
        @Override
        public void run() {
        	
        	boolean flag = true;
//    		Basic.Echo = Settings.getEcho(Basic.BasicContext);
        	Echo = false;
        	VarSearchStart = 0;
    		fnRTN = false;
    		setVolumeControlStream(AudioManager.STREAM_MUSIC);

        	if (PreScan()) { 				// The execution starts by scanning the source for labels and read.data
        		
        	ExecutingLineIndex = 0;			// Just in case PreScan ever changes it
        	flag = RunLoop();
        	       	
        	Stop = true;		// If Stop is not already set, set it so that menu code can display the right thing
        	PrintLine = "";     // Clear the Print Line buffer
        	PrintLineReady = false;
        	textPrintLine = "";
        	
        	OnBackKeyLine = 0;
        	
    		  if (OnErrorLine == 0 && !SyntaxError ){
      			  
    	  		  
    	  		    if (!ForNextStack.empty()){
    	  		    	publishProgress("Program ended with FOR without NEXT");
    	  		    }
    	  		    
    	  		    if (!WhileStack.empty()){
    	  		    	publishProgress("Program ended with WHILE without REPEAT");
    	  		    }
    	  		    
    	  		    if (!DoStack.empty()){
    	  		    	publishProgress("Program ended with DO without UNTIL");
    	  		    }
    	  		    
    	  		  }

         	Basic.theRunContext = null;  // Signals that the background task has stopped
         	cleanUp();
        	}
        	
        	else{              // We get here if PreScan found error or duplicate label
        		for (int i=0; i<TempOutputIndex; ++i){				// if new output lines, the send them
        			publishProgress(TempOutput[i]);					// to UI task
        			}
        		TempOutputIndex = 0;
        	}
        }

        private void publishProgress(String s) {
        	mHandler.obtainMessage(MESSAGE_PUBLISH_PROGRESS, s).sendToTarget();
        }

// The RunLoop() drives the execution of the program. It is called from doInBackground and
// recursively from doUserFunction.
        
        public  boolean RunLoop(){
        	boolean flag = true;
        	do {
        		if (ExecutingLineIndex >= Basic.lines.size())break;
        		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);  // Next program line
//        		Log.v(Run.LOGTAG, " RunLoop: " + ExecutingLineBuffer);
        		LineIndex = 0 ;
        		sTime = SystemClock.uptimeMillis();
 
        		flag = StatementExecuter();							// execute the next statement
        															// returns true if no problems executing statement
        		
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
        		if (WaitForResume||DebuggerStep){
						DebuggerStep = false;
					do{
						try{Thread.sleep(0);} catch(InterruptedException e){} // dont sleep
						if (DebuggerHalt){
							publishProgress("@@G");
							Stop =true;
							}
						if (DebuggerStep){
							publishProgress("@@F");
						}
						
						if (dbSwap){
									publishProgress("@@H");
									WaitForSwap =true;
									do{
										try{Thread.sleep(0);} catch(InterruptedException e){} // dont sleep
										/*
										if(dbSelect){
											publishProgress("@@I");
											WaitForSelect = true;
											dbSelect = false;
							
											do{
												try{Thread.sleep(0);} catch(InterruptedException e){} // dont sleep
											}while(WaitForSelect);
										}
										*/
									}while(WaitForSwap);
					  			publishProgress("@@E");
									dbSwap = false;
								}
								
						
					}while(WaitForResume && !DebuggerStep);
				}
				
           		if (WaitForInput){									// if waiting for Input
        			do{												// wait a bit
                	try {Thread.sleep(500);}catch(InterruptedException e){}
                	if (BadInput){									// If user input was bad
                		publishProgress("@@2");						// tell her and then
                		}
                	if (InputCancelled){							// User hit back on Input Dialog
                		if (OnErrorLine != 0){
                			ExecutingLineIndex = OnErrorLine;
                			WaitForInput = false;
                		} else {
                			publishProgress("@@3");						// Signal for message
                			Stop = true;
                			// and stop executing
                		}
                	 }
        			} while (WaitForInput && !Stop);				// and try again

        			WaitForInput = false;							// have the Input, no longer waiting
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
        	if (interruptResume != -1 ) return true;   	// If we are handling an interrupt then do not cancel this one
        	interruptResume = ExecutingLineIndex;	   	// Set the resume Line Number
        	ExecutingLineIndex = gotoLine;			   	// Set the goto line number
        	IfElseStack.push(IEinterrupt);
        	return false;								//Turn off the interrupt
        }

} // End of Background class

	// This Handler is in the UI (foreground) Task part of Run. It gets control when the background
	// task sends it a Message. Most of the messages carry strings that are just text for the output
	// screen. A few are signals. Signals start with "@@"

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_PUBLISH_PROGRESS:
				onProgressUpdate((String)msg.obj);
				break;
			case MESSAGE_TOAST:		// carried over from BluetoothChatService, not presently used
				Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
						Toast.LENGTH_SHORT).show();
				break;
			default:
				switch (msg.what & MESSAGE_GROUP_MASK) {
				case MESSAGE_BT_GROUP:
					handleBTMessage(msg);								// handle BluetoothMessages
					break;
				case MESSAGE_HTML_GROUP:
				case MESSAGE_DEBUG_GROUP:
				default:												// unrecognized Message
					break;												// ignore it
				}
			}
		}
	};

	protected void onProgressUpdate(String... str) {

		for (int i=0; i<str.length; ++i) {								// Check for signals
			if (str[i].startsWith("@")) {								// Fast check for possible signal
        		if (str[i].startsWith("@@1")){      					// Input dialog signal
        			doInputDialog();
        			if (InputDialog != null)
        			theAlertDialog = InputDialog.show();				// show the Input dialog box
        			}
        		
        		else if (str[i].startsWith("@@2")){						// Invalid Input Dialog Input
        			  CharSequence text = "Not a number. Try Again.";
        			  int duration = Toast.LENGTH_SHORT;

        			  Toast toast = Toast.makeText(this, text, duration);
        			  toast.setGravity(Gravity.TOP,0,50);
        			  toast.show();
        			  BadInput = false;
          			  doInputDialog();
          			  theAlertDialog = InputDialog.show();				// show the Input dialog box
        			  
        		}else if (str[i].startsWith("@@3")){					// User canceled dialog with back key
        			  output.add("Input dialog cancelled");				// Tell user we are stopping
        			  output.add("Execution halted");
        			  
        		}else if (str[i].startsWith("@@4")){					// Does the toast for popup command
      			  CharSequence text = ToastMsg;
    			  Toast toast = Toast.makeText(this, text, ToastDuration);
    			  toast.setGravity(Gravity.CENTER, ToastX, ToastY);
    			  toast.show();
        		
        		}else if (str[i].startsWith("@@5")){					// Clear Screen Signal
        			output.clear();
        		}else if (str[i].startsWith("@@6")){					// NOT CURRENTLY USED
        		}else if (str[i].startsWith("@@7")){					// Set the console title
        			if (ConsoleTitle != null) setTitle(ConsoleTitle);
        			else setTitle(getResources().getString(R.string.run_name));
        		}else if (str[i].startsWith("@@8")){					// Start GPS
        			if (theGPS == null) theGPS = new GPS(Run.this);
        		}else if (str[i].startsWith("@@9")){					// from checkpointProgress
        			ProgressPending = false;							// progress is published, done waiting
        		}else if (str[i].startsWith("@@A")){
        			output.add("");
        		}else if (str[i].startsWith("@@B")){
        			char c = str[i].charAt(3);
        			String s = output.get(output.size()-1) + c;
        			output.set(output.size()-1, s);
        		}else if (str[i].startsWith("@@C")){
        			doHTMLCommand(str[i].substring(3));
        		}else if (str[i].startsWith("@@D")){
        			startVoiceRecognitionActivity();
        		}else if (str[i].startsWith("@@E")){					// Input dialog signal
        			doDebugDialog();
        		}else if (str[i].startsWith("@@F")){					// Debug step completed
        			DebuggerStep = false;
        			doDebugDialog();
        		}else if (str[i].startsWith("@@G")){					// User canceled dialog with back key or halt
        			output.add("Execution halted");
        		}else if (str[i].startsWith("@@H")){					// Swap dialog called
        			doDebugSwapDialog();
        		}else if (str[i].startsWith("@@I")){					// Select dialog called
        			doDebugSelectDialog();
        		}else if (str[i].startsWith("@@J")){					// Alert dialog var not set called
        		} else {output.add(str[i]);}		// Not a signal, just write msg to screen.
			} else {output.add(str[i]);}			// Not a signal, just write msg to screen.

			setListAdapter(AA);						// show the output
			lv.setSelection(output.size()-1);		// set last line as the selected line to scroll
//			Log.d(LOGTAG, "onProgressUpdate: print <" + str[i] + ">");
		} // for each string
	} // onProgressUpdate()

        public void doHTMLCommand(String cmd){			// Executes HTML commands from the UI thread.
        	if (cmd.startsWith("OP") && htmlIntent != null){
  	          startActivityForResult(htmlIntent, BASIC_GENERAL_INTENT);		   
        	}else if (Web.aWebView == null){
        		// Do nothing
        	}else if (cmd.startsWith("GB")){
        		Web.aWebView.goBack();
        	}else if (cmd.startsWith("GF")){
        		Web.aWebView.goForward();
        	}else if (cmd.startsWith("CC")){
        		Web.aWebView.clearCache();
        	}else if (cmd.startsWith("CH")){
        		Web.aWebView.clearHistory();
        	}else if (cmd.startsWith("LU")){
        		Web.aWebView.webLoadUrl(cmd.substring(2));
        	}else if (cmd.startsWith("LS")){
        		Web.aWebView.webLoadString(cmd.substring(2));
        	}else if (cmd.startsWith("PS")){
        		Web.aWebView.webPost(cmd.substring(2));
        	}
        	
        }


// *********************Run Entry Point. Called from Editor or AutoRun *******************


@Override
public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " On Create  " + ExecutingLineIndex );
	
	if (Basic.lines == null){
//        android.os.Process.killProcess(Basic.ProcessID) ;
        android.os.Process.killProcess(android.os.Process.myPid()) ;
        
	}
	
//	System.gc();
//	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " isOld  " + isOld);
	if (isOld){
		if (theWakeLock != null){
			theWakeLock.release();
		}
	}
	theWakeLock = null;
	isOld = true;

	InitVars();	
//	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " On Create 2 " + ExecutingLineIndex );

	myVib = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
	AA= new ColoredTextAdapter(this,  output);
//	AA=new ArrayAdapter<String>(this, R.layout.simple_list_layout, output);  // Establish the output screen
	setListAdapter(AA);
	  lv = getListView();
	  lv.setTextFilterEnabled(false);
	  lv.setSelection(0);
	  
	  if (!Settings.getLinedConsole(this))lv.setDividerHeight(0);
	  
//	  IMM.restartInput(lv);
	  executeKBHIDE();
	  setVolumeControlStream(AudioManager.STREAM_MUSIC);
	  setRequestedOrientation(Settings.getSreenOrientation(this));
	  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	  
//	  lv.setBackgroundColor(0xff006478);  // Set the Console Background color
//	  lv.setTextColor(0xffffffff);        // Set the Console Text color
	  
      if (Settings.getEditorColor(this).equals("BW")){
    	    lv.setBackgroundColor(0xffffffff);
      } else
        if (Settings.getEditorColor(this).equals("WB")){
      	  lv.setBackgroundColor(0xff000000);
      } else
          if (Settings.getEditorColor(this).equals("WBL")){
          	  lv.setBackgroundColor(0xff006478);
      }             	

      headsetBroadcastReceiver = new BroadcastsHandler();
      this.registerReceiver(headsetBroadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));
	  
	  Basic.theRunContext = this;
	  
	  // Listener for Console Touch
	  
	    lv.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (onCTLine != 0) {
					ConsoleTouched = true;
				}
	        }
	      });

	theBackground = new Background();						// Start the background task
	theBackground.start();
	
	}

private void InitVars(){
	
		
    OnErrorLine =0 ;              // Line number for OnError: label
    OnBackKeyLine=0;
    BackKeyHit = false;
    OnMenuKeyLine = 0;
    MenuKeyHit = false;
    bgStateChange = false;
    OnBGLine = 0;
    onCTLine = 0;
    ConsoleTouched = false;


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

//    lv = new ListView() ;							// The output screen list view
    output = new ArrayList<String>() ;					// The output screen text lines
//    AA = new ArrayAdapter<String> ;					// The output screen array adapter
   TempOutput = new String[MaxTempOutput];	// Holds waiting output screen line
    TempOutputIndex = 0;					// Index to next available TempOutput entry

    theBackground = null;					// Background task ID
    SyntaxError = false;		        // Set true when Syntax Error message has been output		
    
    UserPrompt = "";
    theInput = "";						// Holds Input from Input dialog box
    RunContext = null;	
    
    
    WaitForInput = false ;							// Signals between background task and foreground
    BadInput = false;
    InputCancelled = false;
    InputDismissed = false;
    InputDialog = null;
   theAlertDialog = null;
   InputIsNumeric = true;
   InputDefault = "";
   inputVarNumber = 0;
   ProgressPending = false;
   randomizer = null;
   background = false;
   
  
    // debugger ui vars
   Watch_VarNames = new ArrayList<String>(); // watch list of string names
   WatchVarIndex = new ArrayList<Integer>();// watch list of variable indexes
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
	 WatchedArray =-1;
 	WatchedList =-1;
	WatchedStack =-1;
	WatchedBundle =-1;
	DebugDialog = null;
	theDebugDialog = null;
	DebugSwapDialog = null;
	theDebugSwapDialog = null;
	DebugSelectDialog = null;
	theDebugSelectDialog = null;
	dbSwap = false;
	
    
    GrStop = false;						// Signal from GR that it has died
    Stop = false;	   						// Stops program from running
    GraphicsPaused = false;               // Signal from GR that it has been paused
    RunPaused = false;                    // Used to control the media player
   StopDisplay = false;
   GRFront = false;
   DisplayStopped = false;
   ShowStatusBar = 0;
   IMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

   PrintLine = "";					// Hold the Print line currently being built
    PrintLineReady = false;			// Signals a line is ready to print or write
    
	LabelNames = new ArrayList<String>() ;         // A list of all the label names found in the program
	LabelValues = new ArrayList<Integer>() ;       // The line numbers associated with Label Names

    
    VarNames = new ArrayList<String>() ;			// Each entry has the variable name string
    VarIndex = new ArrayList<Integer>();			// Each entry is an index into
    VarNumber = 0;				// An index for both VarNames and NVarValue
    NumericVarValues = new ArrayList<Double>() ;   // if a Var is a number, the VarIndex is an
   StringVarValues  =new ArrayList<String>() ;    // if a Var is a string, the VarIndex is an
   ArrayTable = new ArrayList<Bundle>() ;			//Each DIMed array has an entry in this table
   StringConstant = "";			// Storage for a string constant
    theValueIndex = 0;				// The index into the value table for the current var
	ArrayValueStart = 0;				// Value index for newly created array 
	
	VarIsFunction = false;		// Flag set by getVar() when var is a user function
	DoingDef = false;
	FunctionTable = new ArrayList<Bundle>() ;      // A bundle is created for each defined function
	ufBundle = null ;						// Bundle set by isUserFunction and used by doUserFunction
    FunctionStack = new Stack<Bundle>() ;			// Stack contains the currently executing functions
    VarSearchStart = 0;					// Used to limit search for var names to executing function vars
   fnRTN = false;				// Set true by fn.rtn. Cause RunLoop() to return
   scOpValue = 0;						// An instance variable that needs to be saved when executing function
   Debug = false;
												

    doingDim = false;				// Signal to get Var that un dimed array var is ok
    unDiming = false;             // Signal to get Var that an array is being undimed
    SkipArrayValues = false;      // Set true for some array.xxx commands
   FindingLabels = false;		// Signal to get var to report var is label if it is
    VarIsNew = true;				// Signal from get var that this var is new
    VarIsNumeric = true;			// if false, Var is string(
    VarIsArray = false;			// if true, Var is an Array
     
    FileTable = new ArrayList<Bundle>() ;			// File table list
    BRlist = new ArrayList<BufferedReader>() ;		// A list of of file readers
    FWlist = new ArrayList<FileWriter>() ;			// A list of file writers
    DOSlist = new ArrayList<DataOutputStream> () ;			// A list of file writers
    BISlist = new ArrayList<BufferedInputStream> () ;			// A list of file writers
    
    clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

    ConsoleTitle = null;							// null means use default string resource

    //  ******************  Popup Command variables ********************************
    
    ToastMsg = "";
    ToastX = 0 ;
    ToastY = 0;
    ToastDuration = 1;
	
    //  ******************* Variables for the SELECT Command ***********************
    
    SelectList = new ArrayList<String>() ;        // The list of things to select from
    SelectedItem = 0 ;						// The index of the selected item
    ItemSelected = false;                 // Signal from Select.java saying an item has been selected 
    SelectMessage = "" ; 				// The toast message for Select.java to display
    
    DataBases = new ArrayList<SQLiteDatabase>() ; 	 // List of created data bases
   Cursors = new ArrayList<Cursor>() ; 	 		 // List of created data bases
   
   TextInputString = "";
   HaveTextInput = false;
    
    GRclass = null ;						// Graphics Intent Class
    GRopen = false;				// Graphics Open Flag
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
    
    theURI = null;
    theMP = null;
    MPfilename ="";
    theMPList = new ArrayList<MediaPlayer>();
    theMpUriList = new ArrayList<Uri>() ;
    theMpUriList.add(null);
    theMPList.add(null);
    theMpResIDList = new ArrayList<Integer>();
    theMpResIDList.add(0);
 
    
    // ******************************* Variables for Sensor Commands **********************************

    theSensors = null;

    theGPS = null;
	
	DoAverage = false;
	DoReverse = false;
	DoShuffle = false;
	DoVariance = false;
	DoStdDev = false;
	DoSort = false;
	DoMin = false;
	DoMax = false;
	DoSum = false;
	
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
	ClientBufferedReader = null;
	ClientPrintWriter = null;
	
	newSS = null;
	serverSocketConnectThread = null;
	theServerSocket = null ;
	ServerBufferedReader = null ;
	ServerPrintWriter = null ;

	clientSocketState = STATE_NONE;
	serverSocketState = STATE_NONE;

	TextToSpeech mTts = null;
	ttsInit = false;
	ttsInitResult = 0;
	ttsIntent = null; 
	ttsSpeakDone = false;
	
	mFTPClient = null;
	FTPdir = null;
	
	CameraBitmap = null;
	CameraDone = true;
	CameraAuto = false;
	CameraManual = false;
	CameraFlashMode = 0; 
	
	mConnectedDeviceName = null;
    mOutStringBuffer = null;
    mBluetoothAdapter = null;
    mChatService = null;
    btLooper = null;
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
	 
	 readNext =0;
	 readData = new ArrayList <Bundle>();


}

public void cleanUp(){
	if (theMP != null){
		try {Run.theMP.stop();} catch (IllegalStateException e){}
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

	
	myVib.cancel();

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

	execute_audio_record_stop();

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

	if (!DoAutoRun && SyntaxError){
		Editor.SyntaxErrorDisplacement = ExecutingLineIndex;
	} else Editor.SyntaxErrorDisplacement = -1;
	
	for (int i = 0; i <BitmapList.size(); ++i ){
		   Bitmap SrcBitMap = BitmapList.get(i);
		   if (SrcBitMap != null)
		      SrcBitMap.recycle();
		   BitmapList.set(i, null);
		}

	
		if (mChatService != null) {
				mChatService.stop();
				mChatService = null;
			}
		
			if (btLooper != null){
				btLooper.quit();
				btLooper = null;
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

public boolean onTouchEvent(MotionEvent event){
	super.onTouchEvent(event);
	int action = event.getAction();  // Get action type
	return false;
}

	public boolean onKeyDown(int keyCode, KeyEvent event)  {						// The user hit the back key
		Log.v(Run.LOGTAG, " " + Run.CLASSTAG + "onKeyDown" + keyCode);
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
    	
    	if (Stop == false) Stop = true;
    	else {
    		if (DoAutoRun) {
//    			android.os.Process.killProcess(Basic.ProcessID) ;
    			android.os.Process.killProcess(android.os.Process.myPid()) ;
    		}
    		else finish();
    	}
        return false;
    	}
    	return super.onKeyDown(keyCode, event);
	}
	

	public boolean onKeyUp( int keyCode, KeyEvent event)  {
		Log.v(Run.LOGTAG, " " + Run.CLASSTAG + "onKeyUp" + keyCode);

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (OnMenuKeyLine != 0) {
				MenuKeyHit = true;
				return true;
			}
			if (Basic.isAPK)			// If menu key hit in APK and not trapped by OnMenuKey									
				return true;			// then tell OS to ignore it
			
			return false;				// Let Android create the Run Menu.
		}
		
		if (Run.kbShown)
			IMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


		if (keyCode == KeyEvent.KEYCODE_BACK && OnBackKeyLine != 0) return true;             // If Menu key, then handle it
	    
	    
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
 public boolean onCreateOptionsMenu(Menu menu) {				// Called when the menu key is pressed.
   super.onCreateOptionsMenu(menu);
   MenuInflater inflater = getMenuInflater();
   inflater.inflate(R.menu.run, menu);
   MenuItem item = menu.getItem(1);
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
    	if (!DoAutoRun && SyntaxError){
			Editor.SyntaxErrorDisplacement = ExecutingLineIndex;
    	}

   	Basic.theRunContext = null;
		if (mChatService != null) {
				mChatService.stop();
				mChatService = null;
			}
		
			if (btLooper != null){
  				btLooper.quit();
  				btLooper = null;
  			}

	   finish();

   }
   return true;
}

@Override
protected void onResume() {
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " On Resume  " + kbShown);

	  RunPaused = false;
	  background = false;
	  bgStateChange = true;
//	  if (Stop) InitVars();

//	  if (WaitForInput) Show("@@1");
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
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " onPause " + kbShown);
	if (kbShown)  IMM.hideSoftInputFromWindow(lv.getWindowToken(), 0);
	
/*	  if (theMP != null){
		  try {theMP.pause();} catch (IllegalStateException e){}
		  }
*/	  
	  RunPaused = true;

	super.onPause();
}

protected void onStart(){
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " On Start  " );
//	InitVars();
	super.onStart();
}

protected void onStop(){
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " onStop " + kbShown);
	System.gc();
	if (!GRrunning) {
		background = true;
		bgStateChange = true;
//		if (kbShown)  IMM.hideSoftInputFromWindow(lv.getWindowToken(), 0);

	}
	super.onStop();
}

protected void onRestart(){
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " onRestart ");

	super.onRestart();

}

protected void onDestroy(){
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " On Destroy  " );

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
		
		for (int i = 0; i <BitmapList.size(); ++i ){
		   Bitmap SrcBitMap = BitmapList.get(i);
		   if (SrcBitMap != null)
		      SrcBitMap.recycle();
		   BitmapList.set(i, null);
		}
		   System.gc();

	unregisterReceiver(headsetBroadcastReceiver);

	super.onDestroy();
}

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
private  String getWord(String line, int start, boolean stopOnPossibleKeyword) {
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

// Store a label and its line number; called from PreScan()
private boolean storeLabel(String name, int LineNumber) {
	for (int i = 0; i < LabelNames.size(); ++i) {			  		// scan for duplicate name
		if (LabelNames.get(i).equals(name)) {
			RunTimeError("Duplicate label name");
			return false;
		}
	}

	LabelNames.add(name);											// Not Duplicate, so save it
	LabelValues.add(LineNumber);									// and its line number
																	// Check for Interrupt Labels
	if (name.equals("onerror")) OnErrorLine = LineNumber;
	if (name.equals("onbackkey")) OnBackKeyLine = LineNumber;
	if (name.equals("onmenukey")) OnMenuKeyLine = LineNumber;
	if (name.equals("ontimer")) OnTimerLine = LineNumber;
	if (name.equals("onkeypress")) OnKeyLine = LineNumber;
	if (name.equals("ongrtouch")) OnTouchLine = LineNumber;
	if (name.equals("onbtreadready")) OnBTReadLine = LineNumber;
	if (name.equals("onbackground")) OnBGLine = LineNumber;
	if (name.equals("onconsoletouch")) onCTLine = LineNumber;

	return checkEOL();
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

		if (isNext(':')) {											// if word really is a label
			if (!storeLabel(word, LineNumber)) { return false; }	// if error or duplicate, report it
		}
		else if (line.startsWith("read.data")) {					// Is not a label. If it is READ.DATA
			LineIndex = 9;											// set LineIndex just past READ.DATA
			if (!executeREAD_DATA())           { return false; }	// parse and store the data list
		}
	}
	return true;
}

private int  isLabel(int theVarNumber){
	int nope = -1;
	int s = LabelNames.size();
	if (s==0) {return nope;}
	String name = VarNames.get(theVarNumber);
	
	for (int i=0; i < s; ++i ){
		if (LabelNames.get(i).equals(name)) return LabelValues.get(i);
	}
	return nope;
}

private static  void Show(String str){					// Display a Error message on  output screen. Message will be
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

private static  void PrintShow(String str){				// Display a PRINT message on  output screen. Message will be
	if (TempOutputIndex >= MaxTempOutput) return;
	
	TempOutput[TempOutputIndex]= str;					// picked up up in main loop and sent to the UI task.
	++TempOutputIndex;
}




private  boolean StatementExecuter(){					// Execute one basic line (statement)

		Double d1 = (double)0.0;
		PossibleKeyWord = "";
			
		GetKeyWord();
		{															// Get the keyword that may start the line
			

			if (KeyWordValue == BKWnone) KeyWordValue = BKWlet;    // If no key word, then assume pseudo LET

			if (!IfElseStack.empty()){					// if inside IF-ELSE-ENDIF
				Integer q = IfElseStack.peek();			// decide if we should skip to ELSE or ENDIF
				if (q == IEskip1) {
						if (KeyWordValue == BKWelse || 
								KeyWordValue == BKWelseif ||
								KeyWordValue == BKWif ||
								KeyWordValue == BKWendif){}
						else{KeyWordValue = SKIP;}
				} else if (q == IEskip2) {
						if (KeyWordValue == BKWendif ||
							KeyWordValue == BKWif ){}
						else {KeyWordValue = SKIP;}
				}
			}
			
			if (KeyWordValue != SKIP)
        		if (Echo)
        			{PrintShow(ExecutingLineBuffer.substring(0, ExecutingLineBuffer.length()-1));}

	        switch (KeyWordValue){						// Execute the key word
	        
	        	case BKWrem:
	        		break;
	        	case BKWdim:
	        		if (!executeDIM()){SyntaxError();return false;}
	        		break;
	        	case BKWlet:
	        		if (!executeLET()){SyntaxError();return false;}
	        		break;
	        	case BKWelseif:
	        		if (!executeELSEIF()){SyntaxError();return false;}
	        		break;
	        	case BKWend:
	        		if (!executeEND()){SyntaxError();return false;}
	        		break;
	        	case BKWprint:
	        		if (!executePRINT()){SyntaxError();return false;}
	        		break;
	        	case BKWinput:
	        		if (!executeINPUT()){SyntaxError();return false;}
	        		break;
	        	case BKWif:
	        		if (!executeIF()){SyntaxError();return false;}
	        		break;
	        	case BKWonerror:
	        		return true;
	        	case BKWelse:
	        		if (!executeELSE()){SyntaxError();return false;}
	        		break;
	        	case BKWendif:
	        		if (!executeENDIF()){SyntaxError();return false;}
	        		break;
	        	case BKWfor:
	        		if (!executeFOR()){SyntaxError();return false;}
	        		break;
	        	case BKWinclude:
	        		break;
	        	case BKWstep:
	        		break;
	        	case BKWnext:
	        		if (!executeNext()){SyntaxError();return false;}
	        		break;
	        	case BKWgoto:
	        		if (!executeGOTO()){SyntaxError();return false;}
	        		break;
	        	case BKWgosub:
	        		if (getVar()) {
	        			int gln = isLabel(VarNumber);
	        			if (gln <0){
	        				RunTimeError("Undefined Label at:");
	        				return false;
	        			}
	        			if (!checkEOL()) return false;
	        			GosubStack.push(ExecutingLineIndex);
	        			ExecutingLineIndex = gln;
	        		}else{
	        			SyntaxError();
	        			return false;
	        		}
	        		break;
	        	case BKWreturn:
	        		if (!checkEOL()) return false;
	        		if (GosubStack.empty()){
	        			RunTimeError("RETURN without GOSUB");
	        			return false;
	        		}
	        		ExecutingLineIndex = GosubStack.pop();
	        		break;
	        	case BKWtext_open:
	        		if (!executeTEXT_OPEN()){SyntaxError(); return false;}
	        		break;
	        	case BKWtext_close:
	        		if (!executeTEXT_CLOSE()){SyntaxError(); return false;}
	        		break;
	        	case BKWtext_readln:
	        		if (!executeTEXT_READLN()){SyntaxError(); return false;}
	        		break;
	        	case BKWtext_writeln:
	        		if (!executeTEXT_WRITELN()){SyntaxError(); return false;}
	        		break;
	        	case BKWwhile:
	        		if (!executeWHILE()){SyntaxError(); return false;}
	        		break;
	        	case BKWrepeat:
	        		if (!executeREPEAT()){SyntaxError(); return false;}
	        		break;
	        	case BKWdo:
	        		if (!executeDO()){SyntaxError(); return false;}
	        		break;
	        	case BKWuntil:
	        		if (!executeUNTIL()){SyntaxError(); return false;}
	        		break;
	        	case BKWonbreak:
	        		return true;
	        	case BKWdir:
	        		if (!executeDIR()){SyntaxError(); return false;}
	        		break;
	        	case BKWmkdir:
	        		if (!executeMKDIR()){SyntaxError(); return false;}
	        		break;
	        	case BKWrename:
	        		if (!executeRENAME()){SyntaxError(); return false;}
	        		break;
	        	case BKWdelete:
	        		if (!executeDELETE()){SyntaxError(); return false;}
	        		break;
	        	case BKWsql:
	        		if (!executeSQL()){SyntaxError(); return false;}
	        		break;
	        	case BKWtime:
	        		if (!executeTIME()){SyntaxError(); return false;}
	        		break;
	        	case BKWgr:
	        		if (!executeGR()){SyntaxError(); return false;}
	        		break;
	        	case BKWpause:
	        		if (!executePAUSE()){SyntaxError(); return false;}
	        		break;
	        	case BKWbrowse:
	        		if (!executeBROWSE()){SyntaxError(); return false;}
	        		break;
	        	case BKWinkey:
	        		if (!executeINKEY()){SyntaxError(); return false;}
	        		break;
	        	case BKWaudio:
	        		if (!executeAUDIO()){SyntaxError(); return false;}
	        		break;
	        	case BKWpopup:
	        		if (!executePOPUP()){SyntaxError(); return false;}
	        		break;
	        	case BKWsensors:
	        		if (!executeSENSORS()){SyntaxError(); return false;}
	        		break;
	        	case BKWgps:
	        		if (!executeGPS()){SyntaxError(); return false;}
	        		break;
	        	case BKWcls:
	        		if (!executeCLS()){SyntaxError(); return false;}
	        		break;
	        	case BKWarray:
	        		if (!executeARRAY()){SyntaxError(); return false;}
	        		break;
	        	case BKWselect:
	        		if (!executeSELECT()){SyntaxError(); return false;}
	        		break;
	        	case BKWexit:
	        		Stop = true;
	                android.os.Process.killProcess(Basic.ProcessID) ;
	                android.os.Process.killProcess(android.os.Process.myPid()) ;
	                break;
	        	case BKWclipboard_get:
	        		if (!executeCLIPBOARD_GET()){SyntaxError(); return false;}
	        		break;
	        	case BKWclipboard_put:
	        		if (!executeCLIPBOARD_PUT()){SyntaxError(); return false;}
	        		break;
	        	case BKWencrypt:
	        		if (!executeENCRYPT()){SyntaxError(); return false;}
	        		break;
	        	case BKWdecrypt:
	        		if (!executeDECRYPT()){SyntaxError(); return false;}
	        		break;
	        	case BKWsplit:
	        		if (!executeSPLIT(0)){SyntaxError(); return false;}
	        		break;
	        	case BKWsplit_all:		// split.all new/2013-07-25 gt
	        		if (!executeSPLIT(-1)){SyntaxError(); return false;}
	        		break;
	        	case BKWundim:
	        		if (!executeUNDIM()){SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_open:
	        		if (!executeBYTE_OPEN()){SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_close:
	        		if (!executeBYTE_CLOSE()){SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_read_byte:
	        		if (!executeBYTE_READ_BYTE()){SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_write_byte:
	        		if (!executeBYTE_WRITE_BYTE()){SyntaxError(); return false;}
	        		break;
	        	case BKWgraburl:
	        		if (!executeGRABURL()){SyntaxError(); return false;}
	        		break;
	        	case BKWfn_def:
	        		if (!executeFN_DEF()){SyntaxError(); return false;}
	        		break;
	        	case BKWfn_rtn:
	        		if (!executeFN_RTN()){SyntaxError(); return false;}
	        		break;
	        	case BKWfn_end:
	        		if (!executeFN_END()){SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_copy:
	        		if (!executeBYTE_COPY()){SyntaxError(); return false;}
	        		break;
	        	case BKWsw_begin:
	        		if (!executeSW_BEGIN()){SyntaxError(); return false;}
	        		break;
	        	case BKWsw_case:
	        		if (!executeSW_CASE()){SyntaxError(); return false;}
	        		break;
	        	case BKWsw_break:
	        		if (!executeSW_BREAK()){SyntaxError(); return false;}
	        		break;
	        	case BKWsw_default:
	        		if (!executeSW_DEFAULT()){SyntaxError(); return false;}
	        		break;
	        	case BKWsw_end:
	        		if (!executeSW_END()){SyntaxError(); return false;}
	        		break;
	        	case BKWvibrate:
	        		if (!executeVIBRATE()){SyntaxError(); return false;}
	        		break;
	        	case BKWkbshow:
	        		if (!executeKBSHOW()){SyntaxError(); return false;}
	        		break;
	        	case BKWkbhide:
	        		if (!executeKBHIDE()){SyntaxError(); return false;}
	        		break;
	        	case BKWecho_on:
	        		if (!executeECHO_ON()){SyntaxError(); return false;}
	        		break;
	        	case BKWecho_off:
	        		if (!executeECHO_OFF()){SyntaxError(); return false;}
	        		break;
	        	case BKWtext_input:
	        		if (!executeTEXT_INPUT()){SyntaxError(); return false;}
	        		break;
	        	case BKWfile_dir:
	        		if (!executeDIR()){SyntaxError(); return false;}
	        		break;
	        	case BKWfile_mkdir:
	        		if (!executeMKDIR()){SyntaxError(); return false;}
	        		break;
	        	case BKWfile_rename:
	        		if (!executeRENAME()){SyntaxError(); return false;}
	        		break;
	        	case BKWfile_size:
	        		if (!executeFILE_SIZE()){SyntaxError(); return false;}
	        		break;
	        	case BKWfile_roots:
	        		if (!executeFILE_ROOTS()){SyntaxError(); return false;}
	        		break;
	        	case BKWfile_exists:
	        		if (!executeFILE_EXISTS()){SyntaxError(); return false;}
	        		break;
	        	case BKWgrabfile:
	        		if (!executeGRABFILE()){SyntaxError(); return false;}
	        		break;
	        	case BKWwakelock:
	        		if (!executeWAKELOCK()){SyntaxError(); return false;}
	        		break;
	        	case BKWtone:
	        		if (!executeTONE()){SyntaxError(); return false;}
	        		break;
	        	case BKWlist:
	        		if (!executeLIST()){SyntaxError(); return false;}
	        		break;
	        	case BKWbundle:
	        		if (!executeBUNDLE()){SyntaxError(); return false;}
	        		break;
	        	case BKWstack:
	        		if (!executeSTACK()){SyntaxError(); return false;}
	        		break;
	        	case BKWdevice:
	        		if (!executeDEVICE()){SyntaxError(); return false;}
	        		break;
	        	case BKWhttp_post:
	        		if (!executeHTTP_POST()){SyntaxError(); return false;}
	        		break;
	        	case BKWsocket:
	        		if (!executeSOCKET()){SyntaxError(); return false;}
	        		break;
	        	case BKWdebug:
	        		if (!executeDEBUG()){SyntaxError(); return false;}
	        		break;
	        	case BKWconsole:
	        		if (!executeCONSOLE()){SyntaxError(); return false;}
	        		break;
	        	case BKWtts_init:
	        		if (!executeTTS_INIT()){SyntaxError(); return false;}
	        		break;
	        	case BKWtts_speak:
	        		if (!executeTTS_SPEAK()){SyntaxError(); return false;}
	        		break;
	        	case BKWtts_local:
	        		if (!executeTTS_LOCAL()){SyntaxError(); return false;}
	        		break;
	        	case BKWftp:
	        		if (!executeFTP()){SyntaxError(); return false;}
	        		break;
	        	case BKWbt:
	        		if (!executeBT()) {SyntaxError(); return false;}
	        		break;
	        	case BKWcall:
	        		if (!executeCALL()) {SyntaxError(); return false;}
	        		break;
	        	case BKWsu:
	        		if (!executeSU(true)) {SyntaxError(); return false;}
	        		break;
	        	case BKWsystem:
	        		if (!executeSU(false)) {SyntaxError(); return false;}
	        		break;
	        	case BKWtget:
	        		if (!executeTGET()) {SyntaxError(); return false;}
	        		break;
	        	case BKWf_n_break:
        			if (!executeF_N_BREAK()) {SyntaxError(); return false;}
        			break;
	        	case BKWw_r_break:
        			if (!executeW_R_BREAK()) {SyntaxError(); return false;}
        			break;
	        	case BKWd_u_break:
        			if (!executeD_U_BREAK()) {SyntaxError(); return false;}
        			break;
	        	case BKWtext_position_get:
	        		if (!executeTEXT_POSITION_GET()) {SyntaxError(); return false;}
	        		break;
	        	case BKWtext_position_set:
	        		if (!executeTEXT_POSITION_SET()) {SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_position_set:
	        		if (!executeBYTE_POSITION_SET()) {SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_position_get:
	        		if (!executeBYTE_POSITION_GET()) {SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_read_buffer:
	        		if (!executeBYTE_READ_BUFFER()) {SyntaxError(); return false;}
	        		break;
	        	case BKWbyte_write_buffer:
	        		if (!executeBYTE_WRITE_BUFFER()) {SyntaxError(); return false;}
	        		break;
	        	case BKWsound_pool:
	        		if (!executeSP()) {SyntaxError(); return false;}
	        		break;
	        	case BKWmy_phone_number:
	        		if (!execute_my_phone_number()) {SyntaxError(); return false;}
	        		break;
	        	case BKWheadset:
	        		if (!executeHEADSET())  {SyntaxError(); return false;}
	        		break;
	        	case BKWsms_send:
	        		if (!executeSMS_SEND())  {SyntaxError(); return false;}
	        		break;
	        	case BKWphone_call:
	        		if (!executePHONE_CALL())  {SyntaxError(); return false;}
	        		break;
	        	case BKWemail_send:
	        		if (!executeEMAIL_SEND())  {SyntaxError(); return false;}
	        		break;
	        	case BKWhtml:
	        		if (!executehtml())   {SyntaxError(); return false;}
	        		break;
	        	case BKWrun:
	        		if (!executeRUN())   {SyntaxError(); return false;}
	        		break;
	        	case BKWempty_program:
	        		if (!executeEMPTY_PROGRAM())   {SyntaxError(); return false;}
	        		break;
	        	case BKWback_resume:
	        		if (!executeBACK_RESUME())   {SyntaxError(); return false;}
	        		break;
	        	case BKWnotify:
	        		if (!executeNOTIFY())   {SyntaxError(); return false;}
	        		break;
	        	case BKWswap:
	        		if (!executeSWAP())   {SyntaxError(); return false;}
	        		break;
	        	case BKWsms_rcv_init:
	        		if (!executeSMS_RCV_INIT())   {SyntaxError(); return false;}
	        		break;
	        	case BKWsms_rcv_next:
	        		if (!executeSMS_RCV_NEXT())   {SyntaxError(); return false;}
	        		break;
	        	case BKWstt_listen:
	        		if (!executeSTT_LISTEN())   {SyntaxError(); return false;}
	        		break;
	        	case BKWstt_results:
	        		if (!executeSTT_RESULTS())   {SyntaxError(); return false;}
	        		break;
	        	case BKWtimer:
	        		if (!executeTIMER())   {SyntaxError(); return false;}
	        		break;
	        	case BKWtimezone:
	        		if (!executeTIMEZONE())   {SyntaxError(); return false;}
	        		break;
	        	case BKWonkey_resume:
	        		if (!executeONKEY_RESUME())   {SyntaxError(); return false;}
	        		break;
	        	case BKWmenukey_resume:
	        		if (!executeONMENUKEY_RESUME())   {SyntaxError(); return false;}
	        		break;
	        	case BKWhome:
	        		if (!executeHOME())   {SyntaxError(); return false;}
	        		break;
	        	case BKWbackground_resume:
	        		if (!executeBACKGROUND_RESUME())   {SyntaxError(); return false;}
	        		break;
	        	case BKWphone_rcv_init:
	        		if (!executePHONE_RCV_INIT())   {SyntaxError(); return false;}
	        		break;
	        	case BKWphone_rcv_next:
	        		if (!executePHONE_RCV_NEXT())   {SyntaxError(); return false;}
	        		break;
	        	case BKWread_data:
	        		return true;		// Do NOT call executeREAD_DATA, that was done in PreScan
	        	case BKWread_next:
	        		if (!executeREAD_NEXT())   {SyntaxError(); return false;}
	        		break;
	        	case BKWread_from:
	        		if (!executeREAD_FROM())   {SyntaxError(); return false;}
	        		break;
	        	case BKWconsole_resume:
	        		if (!executeCONSOLE_RESUME())   {SyntaxError(); return false;}
	        		break;
	        	default:
	        		break;
	        }
	       }
	return true;     // Statement executed ok. Return to main looper.
	}
		
	private  void SyntaxError(){						// Called to output Syntax Error Message
/*		if (OnErrorLine != 0){
			return;
		}*/
		if (!SyntaxError){								// If a previous Syntax error message has
			RunTimeError("Syntax Error");					// not been displayed them display
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
		
   private boolean RunTimeError(String msg){
	   Show(msg);
	   Show(ExecutingLineBuffer);
	   SyntaxError = true;
	   errorMsg = msg + "\nLine: " + ExecutingLineBuffer;
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

   private  boolean GetKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		int i = 0;
		for (i = 0; i<BasicKeyWords.length; ++i){		// loop through the key word list
			 if (ExecutingLineBuffer.startsWith(BasicKeyWords[i], LineIndex)){    // if there is a match
				 KeyWordValue = i;						// set the key word number
				 LineIndex = LineIndex + BasicKeyWords[i].length(); // move the line index to end of key word
				 return true;							// and report back
			 	}
			}
		KeyWordValue = BKWnone;							// no key word found
		return false;									// report fail
			
		}
	
    private boolean getNVar(){							// Get Var and assure that it is numeric
    	int i = LineIndex;
    	if (!getVar()) return false;
    	if (VarIsNumeric) return true;
    	LineIndex = i;
    	return false;
    }
    
    private boolean getSVar(){							// Get Var and assure that it is not numeric
    	int i = LineIndex;
    	if (!getVar()) return false;
    	if (!VarIsNumeric) return true;
    	LineIndex = i;
    	return false;
    }

	private boolean getArrayVar(){						// Get Var and assure that it an array
		int i = LineIndex;
		if (!getVar()) return false;
		if (VarIsArray) return true;

		LineIndex = i;
		return RunTimeError("Array variable expected");	// Not an array var
	}

	private boolean getArrayVarForWrite() {				// get the array var name as a new, undimensioned array
		doingDim = true;
		if (!getArrayVar()) { doingDim = false; return false; }
		doingDim = false;
		return (VarIsNew || RunTimeError("Array must not be DIMed"));
	}

	private boolean getArrayVarForRead() {				// get the array var name as a previously-dimensioned array
		SkipArrayValues = true;
		if (!getArrayVar()) { SkipArrayValues = false; return false; }
		SkipArrayValues = false;
		return (!VarIsNew || RunTimeError("Array not DIMed"));
	}

	private   boolean  getVar(){							// Get a variable if there is one

		int LI = LineIndex;
    	VarIsNumeric = true;						// Assume the var is going to be numeric
    	int max = ExecutingLineBuffer.length();
		
		// For the special cases where a var could be followed by keyword THEN, TO or STEP
		boolean stopOnPossibleKeyWord = !PossibleKeyWord.equals("");
																// Isolate the var characters
		String var = getWord(ExecutingLineBuffer, LineIndex, stopOnPossibleKeyWord);
		if (var.length() == 0) { return false; }				// length is 0, no var
		LineIndex += var.length();
		
		VarIsNumeric = !isNext('$');							// Is this a string var?
		if (!VarIsNumeric) var += '$';

		VarIsArray = isNext('[');								// Is this an Array?
		if (VarIsArray) {
			var += '[';
			VarIsFunction = false;
		} else {
			VarIsFunction = isNext('(');						// Is this a Function?
			if (VarIsFunction) {
				var += '(';
				if (!DoingDef) {
					LineIndex = LI;
					return false;
				}
			}
		}
		if (LineIndex >= max) return false;

        int j = VarSearchStart;								// Usually zero but will change when 
        													// executing User Function

        while (j<VarNames.size()){							// Look up this var in the variable table
        		if (var.equals(VarNames.get(j))){           // Var is not new
        			VarNumber = j;
        			VarIsNew = false;
        			theValueIndex = VarIndex.get(j);		// Get the value index from the var table 
        			if (!VarIsArray) {return true;}			// If this is not array, done
        													// var is an array
        			if (unDiming){                          // If unDiming
        				VarNames.set(j," ");                // Change name in table
        				return true;
        			}
        			if (doingDim){							// If doing dim, then var has been used or dimed
        				RunTimeError("Array previously dimensioned");
        				LineIndex = LI;
        				VarNames.set(VarNumber, "!");
        				return false;
        			}
        			
        			if (SkipArrayValues) return true;
        			int svn = VarNumber;					// Save the VarNumber
        			boolean svt = VarIsNumeric;
        			if (!GetArrayValue(VarNumber)){return false;}   // Get the value based upon the index values
        			VarIsNumeric= svt;
        			VarNumber = svn;
        			return true;							// Get Var is now done
        		}
        		++j;
        	}
        if (unDiming) return true;                          // If unDiming then don't create new array
        
        VarIsNew = true;									// var was not in var table, thus it is new
        VarNumber = VarNames.size();						// thus we will make a new var table entry
        VarNames.add(var);
        VarIndex.add(0);									// Keep VarIndex in sync
        
        if (VarIsArray) {									// New Var is an array								
        	if (!doingDim) {								// If not doing dim then we have a problem
        		RunTimeError("Array must be DIMed before using");
        		LineIndex = LI;
        		VarNames.set(VarNumber, "!");
        		return false;
        	}												// Else doing Dim, leave var index = 0 for now
        } else {											// New var is a scalar
        	int kk = 0;
        	if (!VarIsNumeric){								// if var is string
        		kk = StringVarValues.size();				// then the value index is the index
        		StringVarValues.add("");					// to the next unused entry in string values list
        	} else {
        													// New Var is numeric scalar
        		kk = NumericVarValues.size();				// then the value index is the index
        		NumericVarValues.add(0.0);					// to the next unused entry in numeric values list
        	}
        	VarIndex.set(VarNumber, kk);					// The var index is the next avail Numeric Value
        	theValueIndex = kk;
        }
        return true;
	}
	
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
		
	private  boolean MathFunction(){					// get a Math Function if there is one
													// Test for Math Function
													// chop off used part of the line
		
		int i = 0;
		for (i = 0; i<MathFunctions.length; ++i){                   // Does the chopped line
			 if (ExecutingLineBuffer.startsWith(MathFunctions[i],LineIndex)){				// start with a math function?
				 MFNumber = i;										// if it does, set the Enumerated name
				 LineIndex = LineIndex + MathFunctions[i].length();	// set line index to end of MF
				 if (LineIndex >= ExecutingLineBuffer.length()) return false;
				 return true;										// We have a math function
			 	}
			}
		return false;												// Not a math function
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
		
		
		if (!ENE(OpStack, ValueStack)){						// Now do the recursive evaluation
			LineIndex = SaveIndex;							// if it fails, back up
			return false;									// and die
		}

		if (ValueStack.empty())return false;
		EvalNumericExpressionValue = ValueStack.pop();		// Recursive eval succeeded. Pop stack for results
		return true;
	}
		
	private  boolean ENE(Stack<Integer> theOpStack, Stack<Double> theValueStack){ // Part of evaluating a number expression

																// The recursive part of Eval Expression
		Bundle ufb = ufBundle;
			
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

		else if (MathFunction()){						// Try Match Function
			if (!doMathFunction(theOpStack, theValueStack )) return false;
		}
		
		else if (evalStringExpression()){		           // Try String Logical Expression
			if (!SEisLE)return false;                      // If was not a logical string expression, fail
			theValueStack.push(EvalNumericExpressionValue);			
		}

		else if (isUserFunction(true)){						// Try User Function
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
		
		switch  (OperatorValue){							// Handle special case operators
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
		case LPRN:
			return false;
		default:
			if (!handleOp(OperatorValue,  theOpStack, theValueStack)){return false;} // Handles non special case ops
		}

		return ENE(theOpStack, theValueStack);				// Recursively call ENE for rest of expression
	}

	private boolean getOp(){							// Get an expression operator if there is one
														// Get a possible Operator
			
		int i = 0;
		for (i = 0; i<OperatorString.length; ++i){
			
			 if (ExecutingLineBuffer.charAt(LineIndex) == '~'){          // Look for the array.load continue line character
				 ++LineIndex;
				 OperatorValue = EOL;			 // Change it to EOL
				 return true;
			 }
			 
			 if (ExecutingLineBuffer.startsWith(OperatorString[i], LineIndex)){
				 OperatorValue = i;
				 LineIndex = LineIndex + OperatorString[i].length();
				 return true;
			 	}
			}
		return false;
		}
	
		private  boolean handleOp(int op, Stack<Integer> theOpStack, Stack<Double> theValueStack ){		// handle and exprssion operator

											// Execute operator in turn by their precedence
			
		double d1 = 0;
		double d2 = 0;
		int id1 = 0;
		int id2 = 0;
		int ExecOp = 0;

											// If the operator stack is empty, push an SOE (should never happen)
		if (theOpStack.empty()) {
//            theOpStack.push(SOE);
			return false;
        }
											// If the current operator's Goes Onto Stack Precedence
											// is less than the top of stack' Come Off precedence
											// then pop the top of stack operator and execute it
											// keep doing this until the Goes Onto Precedence
											// is less then the TOS Come Off Precedence and then
											// push the current operator onto the operator stack
		
		while (ComesOffPrecedence[theOpStack.peek()] >= GoesOnPrecedence[op]){

				if (theValueStack.empty()) return false;        // Avoid a crash
				ExecOp = theOpStack.pop();

																// Execute the popped operator
																// In general values are popped
																// from the value stack and then
																// operated on by the operator
																// the result is then pushed onto
																// the value stack
				
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
					if (d1==0){
						RunTimeError("DIVIDE BY ZERO AT:");
						return false;
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
					if (d2 <= d1){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;
					
				case NE:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					if (d2 != d1){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;
					
				case GE:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					if (d2 >= d1){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;
					
				case GT:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					if (d2 > d1){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;
					
				case LT:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					if (d2 < d1){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;
					
				case LEQ:                        // Logical Equals 
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					if (d2 == d1){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;

				case OR:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					d1 = d1 + d2;
					if (d1 != 0){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;

				case AND:
					d1 = theValueStack.pop();
					d2 = theValueStack.pop();
					if ((d1 != 0) && (d2 != 0)){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
					theValueStack.push(d1);
					break;

				case NOT:
					d1 = theValueStack.pop();
					if (d1 == 0){
						d1 = 1.0;
					} else{
						d1 = 0.0;
					}
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
		
		private boolean doMathFunction(Stack<Integer> theOpStack, Stack<Double> theValueStack){
			double d1 = 0.0;
			double d2 = 0.0;
			
			switch (MFNumber){
			
			case MFsin:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.sin(d1));
				break;

			case MFcos:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.cos(d1));
				break;

			case MFtan:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.tan(d1));
				break;

			case MFsqr:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.sqrt(d1));
				break;
				
			case MFabs:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.abs(d1));
				break;

			case MFceil:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.ceil(d1));
				break;
				
			case MFfloor:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.floor(d1));
				break;

			case MFmin:											// min new/2013-07-29 gt
				if (!evalNumericExpression()){return false;}
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}
				d2 = EvalNumericExpressionValue;
				theValueStack.push(Math.min(d1, d2));
				break;

			case MFmax:											// max new/2013-07-29 gt
				if (!evalNumericExpression()){return false;}
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}
				d2 = EvalNumericExpressionValue;
				theValueStack.push(Math.max(d1, d2));
				break;

			case MFlog:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.log(d1));
				break;

			case MFexp:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.exp(d1));
				break;
			
			case MFtodegrees:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.toDegrees(d1));
				break;

			case MFtoradians:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.toRadians(d1));
				break;

			case MFpi:											// pi new/2013-07-29 gt
				theValueStack.push(Math.PI);
				break;

			case MFatan:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.atan(d1));
				break;

			case MFacos:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				if (d1<-1 || d1> 1){
					RunTimeError("ACOS parameter out of range");
					return false;
				}
				theValueStack.push(Math.acos(d1));
				break;

			case MFasin:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				if (d1<-1 || d1> 1){
					RunTimeError("ASIN parameter out of range");
					return false;
				}
				theValueStack.push(Math.asin(d1));
				break;

			case MFround:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push((double) Math.round(d1));
				break;

			case MFlen:								// LEN(s$
				if (!getStringArg()) { return false; }			// Get and check the string expression
				 d1= (double) StringConstant.length();			// then get it's length
				 theValueStack.push(d1);						// and push onto value stack
				 break;
			
			case MFrandomize:
				if (!evalNumericExpression()) return false;
			    d1 = EvalNumericExpressionValue;
				if ( d1 == 0 )randomizer = new Random();
				else randomizer = new Random((long) d1);
				theValueStack.push(0.0);
				break;
			
			
			case MFrnd:
				if (randomizer == null) randomizer = new Random();
				theValueStack.push( randomizer.nextDouble());
				break;
			
			case MFbackground:
				if (background) theValueStack.push( 1.0);
				else theValueStack.push( 0.0 );
				break;


			case MFval:								// VAL(s$
				if (!getStringArg()) { return false; }			// Get and check the string expression
				StringConstant = StringConstant.trim();
				if (StringConstant.length()== 0 ) {
					RunTimeError("VAL of empty string is not valid");
					return false;
				}
			
				try { d1 = Double.parseDouble(StringConstant);}			// have java parse it into a double
				catch (Exception e) {
					return RunTimeError(e);
				}
				theValueStack.push(d1);							// Push number onto value stack
				break;
			
			case MFascii:
				if (!getStringArg()) { return false; }			// Get and check the string expression
				if (StringConstant.equals("")) d1 =256;
				else d1 = (double)(StringConstant.charAt(0) & 0x00FF);
				theValueStack.push(d1);							// Push number onto value stack
				break;

			case MFucode:
				if (!getStringArg()) { return false; }			// Get and check the string expression
				if (StringConstant.equals("")) d1 = 0x10000;
				else d1 = (double)(StringConstant.charAt(0));
				theValueStack.push(d1);							// Push number onto value stack
				break;
			
			case MFmod:									// MOD( d1,d2
				if (!evalNumericExpression()){return false;}				// Get d1
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}				// Get d2
				d2 = EvalNumericExpressionValue;
				
				if (d2==0){
					RunTimeError("DIVIDE BY ZERO AT:");
					return false;
				}
				theValueStack.push(d1 % d2);
				break;
			
			case MFbor:
				if (!evalNumericExpression()){return false;}				// Get d1
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}				// Get d2
				d2 = EvalNumericExpressionValue;
				theValueStack.push((double)((long)d1 | (long)d2));
				break;
			
			
			case MFband:
				if (!evalNumericExpression()){return false;}				// Get d1
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}				// Get d2
				d2 = EvalNumericExpressionValue;
				theValueStack.push((double)((long)d1 & (long)d2));
				break;
			
			
			case MFbxor:
				if (!evalNumericExpression()){return false;}				// Get d1
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}				// Get d2
				d2 = EvalNumericExpressionValue;
				theValueStack.push((double)((long)d1 ^ (long)d2));
				break;
			

				
			case MFis_in:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	String Search_for = StringConstant;               
				if (!isNext(',')) { return false; }
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	String Search_in = StringConstant;
	        	
	        	int start = 1;
				if (isNext(',')) {
					if (!evalNumericExpression()) return false;
					start = EvalNumericExpressionValue.intValue();
					if (start < 1 ){
						RunTimeError("Start value must be >= 1");
						return false;
					}
	        	}
	        	start = start - 1;									// Make start index zero-based

	        	if (start < Search_in.length()) {					// If starting inside the string
					int k = Search_in.indexOf(Search_for, start);	// Do the search
					d1 = (double) k+1 ;								// Make results one based
				} else {
					d1 = 0.0;										// else "not found"
				}
	        	theValueStack.push(d1);                     		// Push result onto stack
				break;
			
			case MFstarts_with:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	Search_for = StringConstant;               
				if (!isNext(',')) { return false; }
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	Search_in = StringConstant;
	        	
	        	start = 1;
				if (isNext(',')) {
					if (!evalNumericExpression()) return false;
					d1 = EvalNumericExpressionValue;
					start = (int) d1;
					if (start < 1 ){
						RunTimeError("Start value must be >= 1");
						return false;
					}
	        	}
	        	if (start > Search_in.length()) start = Search_in.length();
	        	start = start - 1;

	        	d1 = 0;
	        	if (Search_in.startsWith(Search_for, start)){
	        		d1 = Search_for.length();
	        	}

	        	theValueStack.push(d1);                     		// Push result onto stack
				break;

			case MFends_with:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	Search_for = StringConstant;               
				if (!isNext(',')) { return false; }
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	Search_in = StringConstant;
	        	
	        	start = 1;
				if (isNext(',')) {
					if (!evalNumericExpression()) return false;
					d1 = EvalNumericExpressionValue;
					start = (int) d1;
					if (start < 1 ){
						RunTimeError("Start value must be >= 1");
						return false;
					}
	        	}
	        	if (start > Search_in.length()) start = Search_in.length();
	        	start = start - 1;

	        	d1 = 0;
	        	if (Search_in.endsWith(Search_for)){
	        		d1 = Search_in.length()-Search_for.length()+1;
	        	}

	        	theValueStack.push(d1);                     		// Push result onto stack
				break;
			
			case MFclock:
				theValueStack.push((double) SystemClock.elapsedRealtime());
				break;
			
			case MFtime:
				if (ExecutingLineBuffer.charAt(LineIndex)== ')') {	// If no args, use current time
					theValueStack.push((double) System.currentTimeMillis());
				} else {											// Otherwise, get user-supplied time
					Time time = theTimeZone.equals("") ? new Time() : new Time(theTimeZone);
					if (!parseTimeArgs(time)) { return false; }
					theValueStack.push((double) time.toMillis(true));
				}
				break;
			
			case MFcollision:
				if (!evalNumericExpression()){return false;}	// Get the first object number
	        	int Object1 = EvalNumericExpressionValue.intValue();
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get the second object number
	        	int Object2 = EvalNumericExpressionValue.intValue();

				double ff = gr_collide(Object1, Object2);
				if (ff == -1) return false;							// -1 is run time error
				theValueStack.push(ff);								// else f = 0 (false) or 1 (true)
				break;
			
			
			case MFhex:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	long  value = 0;
	        	try {
	        		value = Integer.parseInt(StringConstant, 16);
	        	} catch (Exception e) {
	        		RunTimeError("Error" + e);
	        		return false;
	        	}
				value = value & 0xffffffff; 

	        	theValueStack.push((double) value);
				break;
			
			
			case MFoct:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	value = 0;
	        	try {
	        		value = Integer.parseInt(StringConstant, 8);
	        	} catch (Exception e) {
	        		RunTimeError("Error" + e);
	        		return false;
	        	}
				value = value & 0xffffffff; 

	        	theValueStack.push((double) value);
				break;
			
			case MFbin:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	value = 0;
	        	try {
	        		value = Integer.parseInt(StringConstant, 2);
	        	} catch (Exception e) {
	        		RunTimeError("Error" + e);
	        		return false;
	        	}
				value = value & 0xffffffff; 
	        	theValueStack.push((double) value);
				break;
			
			
			case MFshift:
				if (!evalNumericExpression()){return false;}	// Get the value to shift
	        	int xvalue = EvalNumericExpressionValue.intValue();
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get the shift amount and direction
	        	int bits = EvalNumericExpressionValue.intValue();

				int result = 0;
				if (bits < 0){
					result = xvalue << -1*bits;
				}else{
					result = xvalue >> bits;
				}
				
				theValueStack.push((double) result);
				break;
				
			case MFatan2:
				if (!evalNumericExpression()) return false;		// Get the x-coordinate
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get the y-coordinate
	        	d2 = EvalNumericExpressionValue;
				theValueStack.push(Math.atan2(d1, d2));
				break;

			case MFcbrt:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.cbrt(d1));
				break;

			case MFcosh:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.cosh(d1));
				break;

			case MFhypot:
				if (!evalNumericExpression()) return false;		// Get x
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get y
	        	d2 = EvalNumericExpressionValue;
				theValueStack.push(Math.hypot(d1, d2));
				break;

			case MFsinh:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.sinh(d1));
				break;

			case MFpow:
				if (!evalNumericExpression()) return false;		// Get the base
				d1 = EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get the exponent
	        	d2 = EvalNumericExpressionValue;
				theValueStack.push(Math.pow(d1, d2));
				break;

			case MFlog10:
				if (!evalNumericExpression()) return false;
				d1 = EvalNumericExpressionValue;
				theValueStack.push(Math.log10(d1));
				break;
				
			default:
				
			}
			// Every function must have a closing right parenthesis.
			return (isNext(')'));
		}
															


		
	private  boolean getStringFunction(){				// Get a string function if there is one
													// are the next characters a String Function?
													// start by chopping off the part of the line
													// that we are done with
		
		int i = 0;
		for (i = 0; i<StringFunctions.length; ++i){				// scan the String Function List
			 if (ExecutingLineBuffer.startsWith(StringFunctions[i],LineIndex)){					// if in list
				 SFNumber = i;											// the function number is the table index
				 LineIndex = LineIndex + StringFunctions[i].length();	// move the line index over the function
				 return true;											// report found
			 	}
			}
		return false;													// report fail
	}

	private  boolean evalStringExpression(){			// Evaluate a string expression

		SEisLE = false;		// Assume not Logical Expression
																// Evaluate a string exprssion
		if (!ESE())
			{return false;}								// Get the next element (constant, var, function, etc)
		String Temp1 = StringConstant;							// save the resulting string

													            // Do concatenations
		int max = ExecutingLineBuffer.length();
		if (LineIndex >= max) return false;
		while (ExecutingLineBuffer.charAt(LineIndex)== '+'){	// If concat then
			++LineIndex;										
			if (LineIndex >= max) return false;
			if (!ESE()){SyntaxError();return false;}			// then get the other half
			Temp1 = Temp1 + StringConstant;						// and do the concat
		}
													            // Not a concat, try logical compares
		
		StringConstant = Temp1;
		EvalNumericExpressionValue = 0.0;                      // Set Logical Compare Result to false
		int SaveLineIndex = LineIndex;
		if (LineIndex >= max) return false;

		if (!getOp()) {
			if (ExecutingLineBuffer.charAt(LineIndex)!= ',' &&
				ExecutingLineBuffer.charAt(LineIndex)!= ';'	&&
			    ExecutingLineBuffer.charAt(LineIndex)!= ':'	) return false;
			return true;				// Get the potential operator. If none then done
		}
		
	    if (OperatorValue == LE || 				// Insure the operator is a logical compare
	    		OperatorValue == NE ||
	    		OperatorValue == GE || 
	    		OperatorValue == GT || 
	    		OperatorValue == LEQ || 
	    		OperatorValue == LT  ) {
	    	
	    } else{
	    	if (OperatorValue != EOL &&
	    		OperatorValue != RPRN	) return false;
	    	LineIndex = SaveLineIndex; return true;
	    	}
	    scOpValue=OperatorValue;							// Save incase user function call causes change
		if (!ESE()) {return false;}							// get the string to compare
		OperatorValue = scOpValue;							// Restore saved value
		String Temp2 = StringConstant;						// do any concat on the right side
		boolean flag = true; 
		while (flag && ExecutingLineBuffer.charAt(LineIndex)== '+'){
			SaveLineIndex = LineIndex;
			++LineIndex;
			if (LineIndex >= max) return false;

			if (!ESE()){									// If what follow not string exp
				LineIndex = SaveLineIndex;					// assume the + operation is numeric
				flag = false;
			}else {
				try {Temp2 = Temp2 + StringConstant;}		// build up the right side string
				catch (Exception e) {
				   return RunTimeError(e);
			   }
			}
		}
		
		SEisLE = true;										// Signal logical string expression
		
		if (Temp1 ==  null || Temp2 == null) return false;
		int cv = Temp1.compareTo(Temp2); 		// Do the compare
		
		/* if Temp1 < Temp2, cv < 0
		 * if Temp1 = Temp2, cv = 0
		 * if Temp1 > Temp2, cv >0
		 */
		
		EvalNumericExpressionValue = 0.0;      // assume false
		
		switch (OperatorValue){

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
			return false;
		}
		SEisLE = true;
		return true;											// Done
	}
	
	
	
	private  boolean ESE(){								// Part of evaluating a string expression

														// Get the String expression element
		if (GetStringConstant()) {return true;}					// Try String Constant

		int LI = LineIndex;										// Try sting variable
/*		
		if (getNumber()) {
			LineIndex = i;
			return false;
		}
*/
		if (getVar()){
//			if (VarIsFunction) return false;
			if (VarIsNumeric){
				LineIndex = LI;
//				SyntaxError();
				return false;
			}
			StringConstant = StringVarValues.get(theValueIndex);
			return true;
		}
		
		LI = LineIndex;
		Bundle ufb = ufBundle;
		if (isUserFunction(false)){
			boolean flag = doUserFunction();
			ufBundle = ufb;
			if (flag){
				return true; 
			}else {
				LineIndex = LI;
				return false;
			}
		}
		LineIndex = LI;
																// Try String Functions
		if (!getStringFunction()) { return false; }
		if (!doStringFunction()) { SyntaxError();  return false; }
		return true;
	}

	private boolean doStringFunction() {
		double d = 0;
		double d1 = 0;
		int e = 0;
		int e1 = 0;
		int length;
		
		String SSC;
																// Have a string function, do it
		switch (SFNumber){
			case  SFleft:																		// LEFT$
				if (!getStringArg()) { return false; }
				if (!isNext(',')) { return false; }
				SSC = StringConstant;
				if (!evalNumericExpression()) { return false; }
				if (!isNext(')')) { return false; }				// Function must end with ')'

				if (SSC.length() > 0) {
					e = EvalNumericExpressionValue.intValue();
					if (e <= 0) { SSC = ""; }
					else if (e < SSC.length()) { SSC = SSC.substring(0, e); }
				}
				StringConstant = SSC;
				return true;

			case SFmid:																			// MID$
				if (!getStringArg()) { return false; }			// Get the string
				SSC = StringConstant;
				length = SSC.length();

				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()) { return false; }	// Get the start index
				e = EvalNumericExpressionValue.intValue();

				if (isNext(',')) {								// If there is a count, get it
					if (!evalNumericExpression()) { return false; }
					e1 = EvalNumericExpressionValue.intValue();
					if (e1 < 0) { return RunTimeError("Count < 0"); }
				} else {
					e1 = length;								// Default count is whole string
				}
				if (!isNext(')')) { return false; }				// Function must end with ')'

				if (length > 0) {
					if (e > length) { SSC = ""; }
					else {
						if (--e < 0) { e = 0; }					// Change 1-based index to 0-based
						e1 += e;								// Change count to end index
						if (e1 > length) { e1 = length; }
						SSC = SSC.substring(e, e1);
					}
				}
				StringConstant = SSC;
				return true;

			case SFright:																		// RIGHT$
				if (!getStringArg()) { return false; }
				if (!isNext(',')) { return false; }
				SSC = StringConstant;
				if (!evalNumericExpression()) { return false; }
				if (!isNext(')')) { return false; }				// Function must end with ')'

				length = SSC.length();
				if (length > 0) {
					e = EvalNumericExpressionValue.intValue();
					if (e <= 0) { SSC = ""; }
					else if (e < length) { SSC = SSC.substring(length - e); }
				}
				StringConstant = SSC;
				return true;

			case SFword:																		// WORD$
				if (!getStringArg()) { return false; }			// String to split
				String SearchString = StringConstant;

				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()) { return false; }	// Which word to return
				int wordIndex = EvalNumericExpressionValue.intValue();

				String r[] = doSplit(SearchString, 0);			// Get regex arg, if any, and split the string.
				if (!isNext(')')) { return false; }				// Function must end with ')'

				length = r.length;								// Get the number of strings generated
				if (length == 0) { return false; }				// error in doSplit()

				wordIndex--;									// Convert to 0-based index
				if (r[0].length() == 0) { wordIndex++; }		// Special case: first character was a delimiter
				StringConstant = ((wordIndex < 0) || (wordIndex >= length)) ? "" : r[wordIndex];
				return true;

			case SFstr:																			// STR$
				if (!evalNumericExpression()) { return false; }
				StringConstant = String.valueOf(EvalNumericExpressionValue);
				break;

			case SFupper:																		// UPPER$
				if (!getStringArg()) { return false; }
				StringConstant = StringConstant.toUpperCase();
				break;

			case SFlower:																		// LOWER $
				if (!getStringArg()) { return false; }
				StringConstant = StringConstant.toLowerCase();
				break;

			case SFformat:																		// FORMAT $
				if (!getStringArg()) { return false; }			// get the pattern string
				if (!isNext(',')) { return false; }
				SSC = StringConstant;

				if (!evalNumericExpression()) { return false; }	// Get the number to format
				if (!isNext(')')) { return false; }				// Function must end with ')'

				return Format(SSC, EvalNumericExpressionValue);	// and then do the format

			case SFchr:																			// CHR$
				if (!evalNumericExpression()) { return false; }
				d = EvalNumericExpressionValue;
				StringConstant = "" + (char) d;
				break;
			
			case SFversion:
				StringConstant = Basic.BasicContext.getString(R.string.version);
				break;

			case SFgeterror:
				StringConstant = errorMsg;
				break;
				
			case SFreplace:
				if (!getStringArg()) { return false; }
				String target = StringConstant;

				if (!isNext(',')) { return false; }
				if (!getStringArg()) { return false; }
				String argument = StringConstant;

				if (!isNext(',')) { return false; }
				if (!getStringArg()) { return false; }
				String replacment = StringConstant;

				if (argument == null || replacment == null) {
					return RunTimeError("Invalid string");
				}
				
				StringConstant = target.replace(argument, replacment);
				break;

			case SFhex:
				if (!evalNumericExpression()) { return false; }
				e = EvalNumericExpressionValue.intValue();
				StringConstant = Integer.toHexString(e);
				break;

			case SFoct:
				if (!evalNumericExpression()) { return false; }
				e = EvalNumericExpressionValue.intValue();
				StringConstant = Integer.toOctalString(e);
				break;

			case SFbin:
				if (!evalNumericExpression()) { return false; }
				e = EvalNumericExpressionValue.intValue();
				StringConstant = Integer.toBinaryString(e);
				break;

			default:
		}
		return isNext(')');										// Function must end with ')'

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
		FWstring = Fstring.substring(0, FdecimalIndex-1);									// FW is whole number part (includes decimaal)
		if (FhasDecimal) {FDstring = Fstring.substring(FdecimalIndex, Fstring.length());}	// FD is decimal string

      
        for (i=0; i<FDstring.length(); ++i){								// insure FD only has # chars
        	if (FDstring.charAt(i) != '#'){SyntaxError(); return false;}
        }
		
		for (i=0; i< Vstring.length(); ++i){								// Scan the number string for it's decimal index
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
	
	private  boolean executeDIM(){									// DIM
																		// Execute a DIM Comman
		do {									// Multiple Arrays can be DIMed in one DIM statement separated by commas
			doingDim = true;
			if (!getArrayVar()) { doingDim = false; return false; }		// Get the array name var
			doingDim = false;

			int svn = VarNumber;										// save the array variable table number
			boolean svt = VarIsNumeric;									// and the array variable type

			if (isNext(']')) { return false; }							// Must have dimension(s)

			ArrayList<Integer> dimValues = new ArrayList<Integer>();	// A list to hold the array dimension values
			do {														// get each index value
				if (!evalNumericExpression()) { return false; }
				dimValues.add(EvalNumericExpressionValue.intValue());	// and add it to the list
			} while (isNext(','));

			if (!isNext(']')) { return false; }							// must have closing bracket

			if (!BuildBasicArray(svn, svt, dimValues)) { return false; }// Build the array

		} while (isNext(','));											// continue while there are arrays to be DIMed
		return checkEOL();												// Then done
	}
	
	private boolean executeUNDIM(){
		boolean unDimed = false;
		unDiming = true;
		do {									// Multiple Arrays can be UNDIMed in one Statement separated by commas
			unDimed = getArrayVar() && isNext(']');						// Get the array name var and UNDIM it
		} while (unDimed && isNext(','));								// continue while there are arrays to be UNDIMed
		unDiming = false;

		return unDimed && checkEOL();
	}

	private   boolean BuildBasicArray(int VarNumber, boolean IsNumeric, ArrayList<Integer> DimList){		//Part of DIM

													// Build a basic array
													// Makes a bundle with information about the 
													// array and put the bundle into the array table
													// The index to bundle in the array table
													// The index is put into the VAR table as the
													// the var value
		
													// In addition, a value element is created for
													// each element in the array
		
		ArrayValueStart = 0;
		int TotalElements = 1;
		int theDim = 0;
		ArrayList<Integer> ArraySizes = new ArrayList<Integer>();

		for (int i = 0; i<DimList.size(); ++i){   // Initialize array sizes
			ArraySizes.add(0);					  // the purpose of this list will be explained later			
		}
		ArraySizes.add(1);						 // and add the unitary 
		
		for (int dim=DimList.size()-1; dim >= 0; --dim){	//for each Dim from last to first
			theDim = DimList.get(dim);						//get the Dim
			if (theDim < 1){								// insure >=1
				RunTimeError("DIMs must be >= 1 at");
				return false;
			}
			TotalElements= TotalElements*theDim;			//multiply this Index by the previous size
			ArraySizes.set(dim, TotalElements);				//and set in the ArraySizes List
															//The list of sizes is used to quickly
															//calculate array element offset
															//when array is referenced

			if (TotalElements>50000){						// Limit the size of any one array
				return RunTimeError("Array exceeds 50,000 elements");	// to 50,000 elements
			}
		}
		ArraySizes.remove(0);								//remove the last size (the first size)
		try{
		if (IsNumeric){										//Initialize Numeric Array Values
			ArrayValueStart = NumericVarValues.size();		// All numeric var values kept in NumericVarValues
			for (int i=0; i < TotalElements; ++i){			// Number inited to 0.0
				NumericVarValues.add(0.0);
			}
		}else{												//Initialize String Array Values
			ArrayValueStart = StringVarValues.size();		//All string var values kept in StringVarValues
			for (int i=0; i < TotalElements; ++i){
				StringVarValues.add("");					// Strings inited to empty
			}
		}
		}
		catch (Exception e) {
			return RunTimeError(e);
		}

	
		Bundle ArrayEntry = new Bundle();						// Build the array table entry bundle
		ArrayEntry.putIntegerArrayList("dims", DimList);        // The array index values
		ArrayEntry.putIntegerArrayList("sizes", ArraySizes);	// The array sizes
		ArrayEntry.putInt("length", TotalElements);
		ArrayEntry.putInt("base", ArrayValueStart);				// The pointer the first array element value
		VarIndex.set(VarNumber, ArrayTable.size());		        // The VAR's value is it's array table index		
		ArrayTable.add(ArrayEntry);								// Put the element bundle into the array table
		
		return true;
	}// end BuildBasicArray

	private boolean BuildBasicArray(int VarNumber, boolean IsNumeric, int length){	// Build 1D array
		ArrayList <Integer> dimValues = new ArrayList<Integer>();		// list of dimensions
		dimValues.add(length);											// only one dimension
		return (BuildBasicArray(VarNumber, IsNumeric, dimValues));		// go build an array of the proper size
	}

	private boolean ListToBasicNumericArray(int VarNumber, List <Double> Values, int length) {
		if (!BuildBasicArray(VarNumber, true, length)) return false;	// go build an array of the proper size and type
		int i = ArrayValueStart;
		for (Double d : Values) {										// stuff the array
			NumericVarValues.set(i++, d);
		}
		return true;
	}

	private boolean ListToBasicStringArray(int VarNumber, List <String> Values, int length) {
		if (!BuildBasicArray(VarNumber, false, length)) return false;	// go build an array of the proper size and type
		int i = ArrayValueStart;
		for (String s : Values) {										// stuff the array
			StringVarValues.set(i++, s);
		}
		return true;
	}

	private  boolean GetArrayValue(int theVarNumber) {			//Get the value of an array element given it's index values

													// Get the index into the numeric or string
													// value table pointed to by the given
													// index values in this call
		
		ArrayList<Integer> indicies = new ArrayList<Integer>();
		
													// Parse out the index values for this call
		
		while (ExecutingLineBuffer.charAt(LineIndex)!= ']'){
			if (!evalNumericExpression()){SyntaxError(); return false; }
			indicies.add(EvalNumericExpressionValue.intValue());
			if (ExecutingLineBuffer.charAt(LineIndex)== ','){++LineIndex;}
			else if (ExecutingLineBuffer.charAt(LineIndex)!= ']'){SyntaxError(); return false;}
		}
		++LineIndex;
		
		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(theVarNumber)); // Get the array table bundle for this array
		ArrayList<Integer> dims = ArrayEntry.getIntegerArrayList("dims");
		ArrayList<Integer> sizes = ArrayEntry.getIntegerArrayList("sizes");

		if (dims.size() != indicies.size()){						// insure index count = dim count
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

	private boolean executePRINT(){
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
	private boolean buildPrintLine(String line, String newline){
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

	private boolean executeGOTO() {
		
		int maxStack = 50000 ;						// 50,000 should be enough
		
		if (IfElseStack.size() > maxStack) {
			RunTimeError("Stack overflow. See manual about use of GOTO.");
			return false;
		}
		
		if (ForNextStack.size() > maxStack) {
			RunTimeError("Stack overflow. See manual about use of GOTO");
			return false;
		}
		
		if (getVar()) {
			int gln = isLabel(VarNumber);
			if (gln <0){
				RunTimeError("Undefined Label at:");
				return false;
			}
			if (!checkEOL()) return false;
			ExecutingLineIndex = gln;
		}else{
			return false;
		}

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
		
		PossibleKeyWord = "then";					// Tell getVar to expect a THEN
		if (!evalNumericExpression()) { return false; }
		PossibleKeyWord = "";						// getVar should no longer expect THEN
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
			PossibleKeyWord = "then";					// Tell getVar to expect a THEN
			if (!evalNumericExpression()) { return false; }
			PossibleKeyWord = "";						// getVar should no longer expect THEN
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
	
	private  boolean executeFOR(){
		

		
		Bundle b= new Bundle();						// A bundle to hold value for stack
		
		if (ExecutingLineIndex+1 >= Basic.lines.size()) { return false; }
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
		
		if (step > 0){											// Test the initial condition
			if (fstart > flimit) {return SkipToNext();}			// If exceeds limit then skip to NEXT
		}else 
			if (fstart < flimit) {return SkipToNext();}

		ForNextStack.push(b);
		
		return true;
	}
	
		private boolean executeF_N_BREAK(){
			if (ForNextStack.empty()){				// If the stack is empty
				RunTimeError("No For Loop Active");			// then we have a misplaced NEXT
				return false;
			}
			if (!checkEOL()) return false;

			if (!SkipToNext()) return false;
			
			
			
			ForNextStack.pop();
			
			return true;

		}
		
		private  boolean SkipToNext(){
															// Skip statments until NEXT found
			++ExecutingLineIndex;
	    	do {
	    		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
	    		LineIndex = 0 ;
				if (ExecutingLineBuffer.startsWith("next")){
					LineIndex = 4;
					return true;
				}
				
				if (ExecutingLineBuffer.startsWith("for")){   // Found found nested FOR
		    		++ExecutingLineIndex;					  // Recursively seek it's 
	    			if (!SkipToNext()){return false;}		  // NEXT
	    		}
	    		++ExecutingLineIndex;
	    	}while (ExecutingLineIndex < Basic.lines.size());
	    	
	    	RunTimeError("FOR without NEXT");							// If end of program...
	    	return false; // End of program. No Next found;

		}
	
		private  boolean executeNext(){
		
		
		if (ForNextStack.empty()){				// If the stack is empty
			RunTimeError("NEXT with out FOR");			// then we have a misplaced NEXT
			return false;
		}
		Bundle b = ForNextStack.peek();			// Peek at the TOS Bundle
		int line = b.getInt("line");
		int varindex = b.getInt("var");					// We did not store the index value, we stored a pointer to it
		double var = NumericVarValues.get(varindex);
		double limit = b.getDouble("limit");
		double step = b.getDouble("step");
		
		var = var + step;						// Do the STEP
		NumericVarValues.set(varindex, var); 	// Assign the result to the index
		
		if (step<0){							// Test limit
			if (var >= limit){
				ExecutingLineIndex = line;
				return true;
			}
			b = ForNextStack.pop();				// If done, pop the stack 
			return true;
		}
		if (var <= limit){						// If not done, go back the statement
			ExecutingLineIndex = line;			// following NEXT
			return true;
			}
		
		b = ForNextStack.pop();
		
		return true;
	}

	private boolean executeWHILE(){
		if (!evalNumericExpression()) return false;

		if (EvalNumericExpressionValue != 0){		   // if true
			WhileStack.push(ExecutingLineIndex-1);	   // push line number onto while stack.
			return checkEOL();
		}
		if (!SkipToRepeat()){return false;}		  // False, find the REPEAT for the nested FOR
		return true;
	}
	
	private boolean executeW_R_BREAK(){
		if (WhileStack.empty()){				// If the stack is empty
			RunTimeError("No While Statement Active");			// then we have a misplaced NEXT
			return false;
		}
		if (!checkEOL()) return false;
		if (!SkipToRepeat()) return false;
		WhileStack.pop();
		return true;
	}

	
		private boolean SkipToRepeat(){
		++ExecutingLineIndex;						   // if conditional is false, find the repeat
		if (ExecutingLineIndex >= Basic.lines.size()){
			RunTimeError("WHILE without REPEAT");
	    	return false;
		}
    	do {
    		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
    		LineIndex = 0 ;
    		if (ExecutingLineBuffer.startsWith("repeat")){
    			return true;
    		}
    		if (ExecutingLineBuffer.startsWith("while")){   // Found found nested WHILE
	    		++ExecutingLineIndex;
    			if (!SkipToRepeat()){return false;}		  // Find the REPEAT for the nested WHILE
    		}
    		++ExecutingLineIndex;
    	}while (ExecutingLineIndex < Basic.lines.size());
    	RunTimeError("WHILE without REPEAT");
    	return false; // End of program. No Next found;
	}
	
		private boolean executeREPEAT(){
		if (WhileStack.empty()){				// Empty stack = error
			RunTimeError("REPEAT without WHILE");
	    	return false; // End of program. No Next found;
		}
		ExecutingLineIndex = WhileStack.pop();	// Pop the line number of the WHILE

		return checkEOL();
	}
	
	private boolean executeDO(){
		DoStack.push(ExecutingLineIndex-1);	   // push line number onto DO stack.
		return checkEOL();

	}
	
	private boolean executeD_U_BREAK(){
		if (DoStack.empty()){				// Empty stack = error
			RunTimeError("No DO loop active");
	    	return false; // End of program. No UNTIL found;
		}
		if (!checkEOL()) return false;
		if (!SkipToUntil());
		DoStack.pop();
		return true;
	}
	
	private boolean SkipToUntil(){
		++ExecutingLineIndex;						   // if conditional is false, find the repeat
		if (ExecutingLineIndex >= Basic.lines.size()){
			RunTimeError("DO without UNTIL");
	    	return false;
		}
    	do {
    		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
    		LineIndex = 0 ;
    		if (ExecutingLineBuffer.startsWith("until")){
    			return true;
    		}
    		if (ExecutingLineBuffer.startsWith("do")){   // Found found nested WHILE
	    		++ExecutingLineIndex;
    			if (!SkipToUntil()){return false;}		  // Find the REPEAT for the nested WHILE
    		}
    		++ExecutingLineIndex;
    	}while (ExecutingLineIndex < Basic.lines.size());
    	RunTimeError("DO without UNTIL");
    	return false; // End of program. No Next found;
	}

	
		private boolean executeUNTIL(){
		if (DoStack.empty()){				// Empty stack = error
			RunTimeError("UNTIL without DO");
	    	return false; // End of program. No UNTIL found;
		}
		if (!evalNumericExpression()) return false;
		if (EvalNumericExpressionValue == 0){     // If false
			ExecutingLineIndex = DoStack.pop();   // pop the DO line number and go to it.
			return true;
		}
		DoStack.pop();								// if true, pop the stack and
		return checkEOL();
	}
	
	private  boolean executeINPUT(){
		
		if (evalStringExpression() && !SEisLE){
			UserPrompt = StringConstant;
			if (ExecutingLineBuffer.charAt(LineIndex)!= ','){
				SyntaxError();
				return false;
			}
			++LineIndex;
		}
		for (int i = UserPrompt.length(); i < 19 ; ++i){
			UserPrompt = UserPrompt + (" ");
		}
		if (!getVar()){SyntaxError();return false;}
		InputIsNumeric = VarIsNumeric;
		inputVarNumber = VarNumber;
		
		InputDefault = "";
		
		if (ExecutingLineBuffer.charAt(LineIndex )== ','){
			++LineIndex;
			if (InputIsNumeric) {
				if (!evalNumericExpression())return false;
				StringConstant = String.valueOf(EvalNumericExpressionValue);
			} else {
				if (!evalStringExpression()) return false;
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

	private void doInputDialog(){

		InputDialog = new AlertDialog.Builder(this);
	    final EditText input = new EditText(this);
	    input.setText(InputDefault);
	    if (InputIsNumeric) input.setInputType(0x00003002); // Limits keys to signed decimal numbers
	    InputDialog.setView(input);
	    InputDialog.setCancelable(true);
	    InputDialog.setTitle(UserPrompt);
	    InputDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){
			public void onCancel(DialogInterface arg0) {
				InputCancelled = true;
				WaitForInput = false;
			}
	    });
	    InputDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

// ****************************************  start of Dialog Click Listener**************************************
//
//  This methond is not executed until the user presses OK on the Dialog Box
	    	
	          public void onClick(DialogInterface dialog, int whichButton) {
	              if (InputIsNumeric){                                 // Numeric Input Handling
	                WaitForInput = false; //<=== moved to here
	                 theInput = input.getText().toString().trim();
	                 double d = 0;
	                 try { d = Double.parseDouble(theInput);}                           // have java parse it into a double
	                 catch (Exception e) {
	                       BadInput=true;
	                       WaitForInput = true;//<====changed to true
	                    }
	                 NumericVarValues.set(VarIndex.get(inputVarNumber), d);
	                 
	              }else {                                          // String Input Handling
	                 theInput = input.getText().toString();
	                 StringVarValues.set(VarIndex.get(inputVarNumber), theInput);
	                 WaitForInput = false;
	              }
	          }});
//***************End of Input Click Listener *******************************************
	}
	
	private boolean executeBACK_RESUME(){
		if (interruptResume == -1){
			RunTimeError("Back key not hit");
			return false;
		}
		return doResume();
	}
	
	private boolean executeONMENUKEY_RESUME(){
		if (interruptResume == -1){
			RunTimeError("Menu key not hit");
			return false;
		}
		return doResume();
	}
	
	private boolean executeCONSOLE_RESUME(){
		if (interruptResume == -1){
			RunTimeError("Console not touched");
			return false;
		}
		return doResume();
	}
	
	private boolean doResume(){
		ExecutingLineIndex = interruptResume;
		interruptResume = -1;
		// Pull the IEinterrupt from the If Else stack
		// It is possible that IFs were executed in the interrupt code
		// so pop entries until we get to the IEinterrupt
		while (IfElseStack.peek() != IEinterrupt) {
			IfElseStack.pop();
		}
		IfElseStack.pop();  // Top is stack is now IEInterrupt, pop it
				
		return true;
	}
	
// ************************************************** Read Commands **********************************
	
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
	
	
// **************************************************  Debug Commands *********************************
	
	private boolean executeDEBUG(){
		
		if (!GetDebugKeyWord()) return false;
	
		switch (KeyWordValue){

			case debug_on:
				if (!executeDEBUG_ON()){SyntaxError(); return false;}
				break;
			case debug_off:
				if (!executeDEBUG_OFF()){SyntaxError(); return false;}
				break;
			case debug_print:
				if (!executeDEBUG_PRINT()){SyntaxError(); return false;}
				break;
			case debug_echo_on:
				if (!executeECHO_ON()){SyntaxError(); return false;}
				break;				
			case debug_echo_off:
				if (!executeECHO_OFF()){SyntaxError(); return false;}
				break;
			case debug_dump_scalars:
				if (!executeDUMP_SCALARS()){SyntaxError(); return false;}
				break;
			case debug_dump_array:
				if (!executeDUMP_ARRAY()){SyntaxError(); return false;}
				break;
			case debug_dump_list:
				if (!executeDUMP_LIST()){SyntaxError(); return false;}
				break;
			case debug_dump_stack:
				if (!executeDUMP_STACK()){SyntaxError(); return false;}
				break;
			case debug_dump_bundle:
				if (!executeDUMP_BUNDLE()){SyntaxError(); return false;}
				break;
	  	case debug_watch_clear:
			  if (!executeDEBUG_WATCH_CLEAR()){SyntaxError(); return false;}
				break;
			case debug_watch:
			  if(!executeDEBUG_WATCH()){SyntaxError(); return false;}
				break;
			case debug_show_scalars:
			  if (!executeDEBUG_SHOW_SCALARS()){SyntaxError(); return false;}
				break;
			case debug_show_array:
			  if (!executeDEBUG_SHOW_ARRAY()){SyntaxError(); return false;}
				break;
			case debug_show_list:
			  if (!executeDEBUG_SHOW_LIST()){SyntaxError(); return false;}
				break;
			case debug_show_stack:
			  if (!executeDEBUG_SHOW_STACK()){SyntaxError(); return false;}
				break;
			case debug_show_bundle:
			  if (!executeDEBUG_SHOW_BUNDLE()){SyntaxError(); return false;}
				break;
			case debug_show_watch:
			  if (!executeDEBUG_SHOW_WATCH()){SyntaxError(); return false;}
				break;
			case debug_show_program:
			  if (!executeDEBUG_SHOW_PROGRAM()){SyntaxError();return false;}
				break;
			case debug_show:
				if (!executeDEBUG_SHOW()){SyntaxError(); return false;}
				break;
			case debug_console:
			  if (!executeDEBUG_CONSOLE()){SyntaxError();return false;}
				break;
			default:
		}

		return true;
	}
	
	  private  boolean GetDebugKeyWord(){							// Get a Basic key word if it is there
			// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<Debug_KW.length; ++i){					// loop through the key word list
			  if (Temp.startsWith(Debug_KW[i])){    			// if there is a match
				  KeyWordValue = i;							// set the key word number
				  LineIndex = LineIndex + Debug_KW[i].length(); // move the line index to end of key word
				  return true;								// and report back
			  }
		  }
		  KeyWordValue = debug_none;							// no key word found
		  return false;										// report fail

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
			if (Debug){
			  Echo = true;
			}
			  return true;
			
		  }
		  
		  private boolean executeECHO_OFF(){
			if (!checkEOL()) return false;
			  Echo = false;
			  return true;
		  }
		  
		  private boolean executeDUMP_SCALARS(){
			  if (!Debug) return true;
			  
			  int count = VarNames.size();
			  PrintShow("Scalar Dump");
			  for (int i = 0 ; i < count; ++ i){
				  boolean isString = false;
				  boolean doIt = true;
				  if (VarNames.get(i).endsWith("(")) doIt = false;
				  if (VarNames.get(i).endsWith("[")) doIt = false;
				  if (doIt) {
					  if (VarNames.get(i).endsWith("$")) isString = true;
					  String Line = VarNames.get(i) + " = ";
					  if (isString) Line = Line + StringVarValues.get(VarIndex.get(i));
				 	  else {
				 		  Line = Line + Double.toString(NumericVarValues.get(VarIndex.get(i)));
				 	  }
				  	PrintShow(Line);
				  }
			  }
			  PrintShow("....");
			  
			  return true;
		  }

		  private boolean executeDUMP_ARRAY(){
			  if (!Debug) return true;

			  if (!getArrayVarForRead()) { return false; }

			  PrintShow("Dumping Array " + VarNames.get(VarNumber) + "]");
			  
				Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber)); // Get the array table bundle for this array
				int length = ArrayEntry.getInt("length");				// get the array length
				int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space
				
				for (int i = 0; i <length; ++i){ 
					if (VarIsNumeric){
						PrintShow(Double.toString(NumericVarValues.get(base+i)));
					}
					else{
						PrintShow(StringVarValues.get(base + i));
					}
				}
				
				PrintShow("....");
			  return true;
		  }

		  private boolean executeDUMP_LIST(){
			  if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;					// Get the list pointer
				int listIndex = EvalNumericExpressionValue.intValue();
				if (listIndex < 1 || listIndex >= theLists.size()){
					RunTimeError("Invalid List Pointer");
					return false;
				}
				
				PrintShow("Dumping List " + Double.toString(EvalNumericExpressionValue));
				
				switch (theListsType.get(listIndex))						// Get this lists type
				{
				case list_is_string:										// String
					ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
					int length = thisStringList.size();
					if (length == 0 ){
						PrintShow("Empty List");
						break;
					}
					for (int i=0; i < length; ++i){
						PrintShow (thisStringList.get(i));			//Get the requested string
					}
					break;
					
				case list_is_numeric:												// Number
					ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
					length = thisNumericList.size();
					if (length == 0 ){
						PrintShow("Empty List");
						break;
					}
					for (int i=0; i < length; ++i){
						PrintShow (Double.toString(thisNumericList.get(i)));			//Get the requested string
					}
					break;
					
				default:
					RunTimeError("Internal problem. Notify developer");
					return false;
			
				}
				PrintShow("....");

			  return true;
		  }

		  private boolean executeDUMP_STACK(){
			  if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;							// Get the Stack pointer
				int stackIndex = EvalNumericExpressionValue.intValue();
				if (stackIndex < 1 || stackIndex >= theStacks.size()){
					RunTimeError("Invalid Stack Pointer");
					return false;
				}

				Stack thisStack = theStacks.get(stackIndex);		               // Get the Stack
				if (thisStack.isEmpty()){
					RunTimeError("Stack is empty");
					return false;
				}
				
				Stack tempStack = new Stack();
				tempStack = (Stack) thisStack.clone();
				
				PrintShow("Dumping stack " + Double.toString(EvalNumericExpressionValue));
				
				switch (theStacksType.get(stackIndex))
				{
				case stack_is_string:												// String type list
					if (tempStack.isEmpty()){
						PrintShow("Empty Stack");
						break;
					}
					do {
						String thisString = (String) tempStack.pop();
						PrintShow(thisString);
					} while (!tempStack.isEmpty());
					
					break;
					
				case stack_is_numeric:
					if (tempStack.isEmpty()){
						PrintShow("Empty Stack");
						break;
					}
					do {
						Double thisNumber = (Double) tempStack.pop();
						PrintShow(Double.toString(thisNumber));
					} while (!tempStack.isEmpty());
					break;
					
				default:
					RunTimeError("Internal problem. Notify developer");
					return false;
			
				}
				PrintShow("....");
			  return true;
		  }

		  private boolean executeDUMP_BUNDLE(){
			  if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;							// Get the Bundle pointer
				int bundleIndex = EvalNumericExpressionValue.intValue();
				if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
					RunTimeError("Invalid Bundle Pointer");
					return false;
				}
				
			    Bundle b = theBundles.get(bundleIndex);
			    
			    PrintShow("Dumping Bundle " + Double.toString(EvalNumericExpressionValue));
			    
			    Set<String> set= b.keySet();
			    
			    if (set.size() == 0){
			    	PrintShow("Empty Bundle");
			    	PrintShow("....");
			    	return true;
			    }
			    
			    Iterator it = set.iterator();
			    
			    for (int i=0; i<set.size(); ++i){
			    	String s = (String) it.next();
			    	if (!s.startsWith("@@@N.")) {
			    		boolean isNumeric = b.getBoolean("@@@N."+ s);
			    		if (isNumeric) {
			    			PrintShow(s + ": " + Double.toString(b.getDouble(s)));
			    		}else{
			    			PrintShow(s + ": " +  b.getString(s));
			    		}
			    	}
			    	
			    }
			    
			    PrintShow("....");
			    
			  return true;
		  }//=====================DEBUGGER DIALOG STUFF========================
		  private boolean executeDEBUG_WATCH_CLEAR(){
			  if(!Debug) return true;
				WatchVarIndex.clear();
			  Watch_VarNames.clear();
			  if (!WatchVarIndex.isEmpty()||!Watch_VarNames.isEmpty()) return false;
			  return true;
		  }
		   private boolean executeDEBUG_WATCH(){ // separate the names and store them
			  if (!Debug){return true;}
			 
			  int ti=LineIndex;
			  int i=LineIndex;
			  boolean add = true;
			  for( i = LineIndex;i<ExecutingLineBuffer.length();++i){
				  char c = ExecutingLineBuffer.charAt(i);
				  if (c==','||i==ExecutingLineBuffer.length()-1){
					  LineIndex =ti;
					  getVar();
					  add = true;
					  //PrintShow('"'+ExecutingLineBuffer.substring(ti,i)+'"');
					  for (int j = 0;j<WatchVarIndex.size();++j){
						  if (WatchVarIndex.get(j)==VarNumber)add = false;
						  //PrintShow(Integer.toString(WatchVarIndex.get(j))+"   "+Integer.toString(VarNumber));
					  }
					  if (add){
					  Watch_VarNames.add(ExecutingLineBuffer.substring(ti,i));
					  WatchVarIndex.add(VarNumber);
					  }
					  ti = i+1;
				  }
				  
			 }
			 return true;
		  }
		  private boolean executeDEBUG_SHOW_SCALARS(){
				DialogSelector(1);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_ARRAY(){
			  if (!Debug) return true;

			  if (!getArrayVarForRead()) { return false; }

				WatchedArray = VarNumber;
				DialogSelector(2);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_LIST(){
				if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;					// Get the list pointer
				int listIndex = EvalNumericExpressionValue.intValue();
				if (listIndex < 1 || listIndex >= theLists.size()){
					return RunTimeError("Invalid List Pointer");
				}
				WatchedList = listIndex;
				DialogSelector(3);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_STACK(){
				if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;							// Get the Stack pointer
				int stackIndex = EvalNumericExpressionValue.intValue();
				if (stackIndex < 1 || stackIndex >= theStacks.size()){
					return RunTimeError("Invalid Stack Pointer");
				}
				WatchedStack = stackIndex;
				DialogSelector(4);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_BUNDLE(){
				if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;							// Get the Bundle pointer
				int bundleIndex = EvalNumericExpressionValue.intValue();
				if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
					return RunTimeError("Invalid Bundle Pointer");
				}
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
				if(!Debug) return true;
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
			
		  private boolean executeDEBUG_SHOW(){   // trigger do debug dialog
			  if (!Debug){return true;}
			  WaitForResume = true;
			  PrintShow("@@E");
			  return true;
		  }
			
			
	private void doDebugDialog(){
		
		//remove the instance of dialog cleanly
		//if(theDebugDialog != null) {theDebugDialog.dismiss();theDebugDialog = null;}
		//if(theDebugSwapDialog != null) {theDebugSwapDialog.dismiss();theDebugSwapDialog = null;}
		
		ArrayList<String> msg = new ArrayList<String>();
		
	  if(!dbDialogProgram){
			msg = doFunc();
      msg.add ("Executable Line #:    "+Integer.toString(ExecutingLineIndex+1));
			msg.add("Executing:\n"+ExecutingLineBuffer);
		}
		
		if(dbDialogScalars) msg.addAll(doScalars());
		if(dbDialogArray) msg.addAll( doArray());
		if(dbDialogList) msg.addAll(doList());
		if(dbDialogStack) msg.addAll(doStack());
		if(dbDialogBundle) msg.addAll( doBundle());
		if(dbDialogWatch) msg.addAll(doWatch());
 
		if(dbDialogProgram){
			for (int i = 0; i < Basic.lines.size();++i){
				if (i==ExecutingLineIndex){
					msg.add(" >>"+Integer.toString(1+i)+": "+Basic.lines.get(i).substring(0,Basic.lines.get(i).length()-1));
			  }else{	msg.add( "   "+Integer.toString(1+i)+": "+Basic.lines.get(i).substring(0,Basic.lines.get(i).length()-1));}
			}
		}
			
		DebugDialog = new AlertDialog.Builder(this);
	
		DebugDialog.setCancelable(true);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialoglayout = inflater.inflate(R.layout.debug_dialog_layout, (ViewGroup) findViewById(R.id.debug_list));
		ListView debugView = (ListView)dialoglayout.findViewById(R.id.debug_list); 
		ArrayAdapter<String>dbListAdapter = new ArrayAdapter<String>(this,R.layout.debug_list_layout,msg);
		debugView.setVerticalScrollBarEnabled(true);
		debugView.setAdapter(dbListAdapter);
		
	  DebugDialog.setTitle("BASIC! Debugger");
		
		DebugDialog.setOnCancelListener(new 
		DialogInterface.OnCancelListener(){
		    public void onCancel(DialogInterface arg0) {
			  	DebuggerHalt = true;
			  	WaitForResume = false;
			  }
	  });	
	  DebugDialog.setPositiveButton("Resume", new 
		DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog,int which) {
        	WaitForResume = false;
	    	}
		});
		DebugDialog.setNeutralButton("Step",new
		DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				DebuggerStep = true;
				WaitForResume = true;
			}
		});

		  // leave out until the switcher is done.
		DebugDialog.setNegativeButton("View Swap",new 
		DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which) {
				dbSwap = true;
		}
	  });
	
		theDebugDialog = DebugDialog.setView(dialoglayout).show();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(theDebugDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		theDebugDialog.getWindow().setAttributes(lp);
		
		if(dbDialogProgram){debugView.setSelection (ExecutingLineIndex);}
	}
	
	private void doDebugSwapDialog(){
		//if(theDebugSwapDialog != null) {theDebugSwapDialog.dismiss();theDebugSwapDialog=null;}
		//if(theDebugDialog != null) {theDebugDialog.dismiss();theDebugDialog=null; }
		
		ArrayList<String> msg = new ArrayList<String>();
		String[] options = new String[]{"Program","Scalars","Array","List","Stack","Bundle","Watch"};
		msg.addAll(Arrays.asList(options));
		
		DebugSwapDialog = new AlertDialog.Builder(this);
	
		DebugSwapDialog.setCancelable(true);
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View dialoglayout = inflater.inflate(R.layout.debug_list_s_layout, (ViewGroup) findViewById(R.id.debug_list_s));
		ListView debugView = (ListView)dialoglayout.findViewById(R.id.debug_list_s); 
		ArrayAdapter<String>dbListAdapter = new ArrayAdapter<String>(this,R.layout.simple_list_layout_1,msg);
		debugView.setVerticalScrollBarEnabled(true);
		debugView.setAdapter(dbListAdapter);
		debugView.setClickable(true);
	  DebugSwapDialog.setTitle("Select View:");
		
		debugView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				DialogSelector(position);
				boolean dosel = false;
				if(dbDialogArray == true & WatchedArray == -1) dosel = true;
				if(dbDialogList == true & WatchedList == -1) dosel = true;
				if(dbDialogStack == true & WatchedStack == -1) dosel = true;
				if(dbDialogBundle == true & WatchedBundle==-1) dosel = true;
				if (dosel) {
					// if the element has not been defined ask if user wishes to do so.
					// or at least this is where it will go.
					// for now, default to view program.
					DialogSelector(0);
					position =0;
				}
				String Name="";
				switch (position){
					case 0:
					Name = "View Program";
					break;
					case 1:
					Name = "View Scalars";
					break;
					case 2:
					Name = "View Array";
					break;
					case 3:
					Name = "View List";
					break;
					case 4:
					Name = "View Stack";
					break;
					case 5:
					Name = "View Bundle";
					break;
					case 6:
					Name = "View Watch";
					break;
					case 7:
					Name = "View Console";
					break;
				}
				
				Toaster(Name);
				
			}
		});
		DebugSwapDialog.setOnCancelListener(new 
		DialogInterface.OnCancelListener(){
		    public void onCancel(DialogInterface arg0) {
			  	WaitForSwap = false;
			  }
	  });	
	  DebugSwapDialog.setPositiveButton("Confirm", new 
		DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog,int which) {
        	WaitForSwap = false;
					dbSwap = false;
	    	}
		});
		
		/*  // leave out until the element selector is done.
		DebugSwapDialog.setNeutralButton("Choose Element",new
		DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog,int which){
				WaitForSelect = true;
				
			}
		});
		*/
		
		theDebugSwapDialog = DebugSwapDialog.setView(dialoglayout).show();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(theDebugSwapDialog.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.FILL_PARENT;
		lp.height = WindowManager.LayoutParams.FILL_PARENT;
		theDebugSwapDialog.getWindow().setAttributes(lp);
		
	}
	private void Toaster(CharSequence msg) {
		Context context = getApplicationContext();
		CharSequence text = msg;
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.TOP | Gravity.CENTER, 0 , 50);

		toast.show();


    }
	private void doDebugSelectDialog(){
		if(theDebugSelectDialog != null) theDebugSelectDialog.dismiss();
		
		ArrayList<String> msg = new ArrayList<String>();
		
	}
	
	private ArrayList<String> doWatch(){
		ArrayList<String> msg = new ArrayList<String>();
		msg.add ("Watching:");
	   
		int count = VarNames.size();
		if(!WatchVarIndex.isEmpty()){
			int watchcount = WatchVarIndex.size();
			for(int j = 0;j< watchcount;++j){
				 int wvi = WatchVarIndex.get(j);
				 if (wvi < count){
					boolean isString = false;
					boolean doIt = true;
				  	if (VarNames.get(wvi).endsWith("(")) doIt = false;
				  	if (VarNames.get(wvi).endsWith("[")) doIt = false;
				  	if (doIt) {
						  if (VarNames.get(wvi).endsWith("$")) isString = true;
					  	String Line = VarNames.get(wvi) + " = ";
					  	if (isString) Line = Line + StringVarValues.get(VarIndex.get(wvi));
				 	  		else {
				 		  		Line = Line + Double.toString(NumericVarValues.get(VarIndex.get(wvi)));
				 	  	}
				  		msg.add(Line);
					 }
				}else{msg.add(Watch_VarNames.get(j)+" = Undefined");}
		  }
		}else{msg.add( "\n"+"Undefined.");}
		return msg;
	}
	
	private ArrayList<String> doFunc(){
		ArrayList<String> msg = new ArrayList<String>();
	  String msgs = "";
			if(!FunctionStack.isEmpty()){
					Stack<Bundle> tempStack = (Stack<Bundle>) FunctionStack.clone();
					do {
						msgs =(tempStack.pop().getString("fname"))+ msgs;
					} while (!tempStack.isEmpty());
			}else{msgs+="MainProgram";}
		msg.add("In Function: "+msgs);
		return msg;
	}
	
	private ArrayList<String> doScalars(){
			
			  int count = VarNames.size();
			  ArrayList<String> msg= new ArrayList<String>();
				msg.add("Scalar Dump");
			  for (int i = 0 ; i < count; ++ i){
				  boolean isString = false;
				  boolean doIt = true;
				  if (VarNames.get(i).endsWith("(")) doIt = false;
				  if (VarNames.get(i).endsWith("[")) doIt = false;
				  if (doIt) {
					  if (VarNames.get(i).endsWith("$")) isString = true;
					  String Line = VarNames.get(i) + " = ";
					  if (isString) Line = Line + StringVarValues.get(VarIndex.get(i));
				 	  else {
				 		  Line = Line + Double.toString(NumericVarValues.get(VarIndex.get(i)));
				 	  }
				  	msg.add("  "+Line);
				  }
			  }
			  return msg;
	}
	private ArrayList<String> doArray(){
			  ArrayList<String> msg=new ArrayList<String>();
			  msg.add("Dumping Array " + VarNames.get(WatchedArray) + "]");

				Bundle ArrayEntry = ArrayTable.get(VarIndex.get(WatchedArray)); // Get the array table bundle for this array
				int length = ArrayEntry.getInt("length");				// get the array length
				int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space
				
				boolean ArrayIsNumeric = true;
				if(VarNames.get(WatchedArray).endsWith("$["))ArrayIsNumeric = false;
				for (int i = 0; i <length; ++i){ 
					if (ArrayIsNumeric){
						msg.add(Double.toString(NumericVarValues.get(base+i)));
					}
					else{
						msg.add(StringVarValues.get(base + i));
					}
				}
			  return msg;
		  }
			
			private ArrayList<String> doList(){
				ArrayList<String> msg=new ArrayList<String>();
				
				msg.add("Dumping List " + Double.toString(WatchedList));
				
				switch (theListsType.get(WatchedList))						// Get this lists type
				{
				case list_is_string:										// String
					ArrayList<String> thisStringList = theLists.get(WatchedList);  // Get the string list
					int length = thisStringList.size();
					if (length == 0 ){
						msg.add ("Empty List");
						break;
					}
					for (int i=0; i < length; ++i){
						msg.add(thisStringList.get(i));			//Get the requested string
					}
					break;
					
				case list_is_numeric:												// Number
					ArrayList<Double> thisNumericList = theLists.get(WatchedList);	//Get the numeric list
					length = thisNumericList.size();
					if (length == 0 ){
						msg.add("Empty List");
						break;
					}
					for (int i=0; i < length; ++i){
						msg.add(Double.toString(thisNumericList.get(i)));			//Get the requested string
					}
					break;
					
				default:
					//RunTimeError("Internal problem. Notify developer");
					//return false;
			
				}
			  return msg;
		  }
			
			private ArrayList<String> doStack(){
			  ArrayList<String> msg=new ArrayList<String>();

				Stack thisStack = theStacks.get(WatchedStack);		               // Get the Stack
				if (thisStack.isEmpty()){
					msg.add("Stack has not been created.");
				  return msg;
				}
				
				Stack tempStack = new Stack();
				tempStack = (Stack) thisStack.clone();
				
				msg.add("Dumping stack " + Double.toString(WatchedStack));
				
				switch (theStacksType.get(WatchedStack)){
				case stack_is_string:												// String type list
					if (tempStack.isEmpty()){
						msg.add("Empty Stack");
						break;
					}
					do {
						String thisString = (String) tempStack.pop();
						msg.add(thisString);
					} while (!tempStack.isEmpty());
					
					break;
					
				case stack_is_numeric:
					if (tempStack.isEmpty()){
						msg.add("Empty Stack");
						break;
					}
					do {
						Double thisNumber = (Double) tempStack.pop();
						msg.add(Double.toString(thisNumber));
					} while (!tempStack.isEmpty());
					break;
					
				default:
					msg.add("Internal problem. Notify developer");
					return msg;
				}
			  return msg;
			}
			
			private ArrayList<String> doBundle(){
					ArrayList<String> msg=new ArrayList<String>();
			    Bundle b = theBundles.get(WatchedBundle);
			    
			    msg.add("Dumping Bundle " + Double.toString(WatchedBundle));
			    
			    Set<String> set= b.keySet();
			    
			    if (set.size() == 0){
			    	msg.add("Empty Bundle");
						return msg;
			    }
			    
			    Iterator it = set.iterator();
			    
			    for (int i=0; i<set.size(); ++i){
			    	String s = (String) it.next();
			    	if (!s.startsWith("@@@N.")) {
			    		boolean isNumeric = b.getBoolean("@@@N."+ s);
			    		if (isNumeric) {
			    			msg.add(s + ": " + Double.toString(b.getDouble(s)));
			    		}else{
			    			msg.add(s + ": " +  b.getString(s));
			    		}
			    	}
			    	
			    }
			  return msg;
			}
		  

// ******************************* User Defined Functions ******************************

		private boolean  executeFN_DEF(){									// Define Function
		
		DoingDef = true;
		if (!getVar()) { DoingDef = false; return false; }			// Get the function name
		DoingDef = false;
		if (!VarIsFunction) { return false; }						// Make sure it is a function
		if (!VarIsNew) { return RunTimeError("Function previously defined at:"); }
		
		int fVarNumber = VarNumber;                                  // Save the VarNumber of the function name
		boolean fType = VarIsNumeric;
		int NVV = NumericVarValues.size();							// Save for array trim
		int SVV = StringVarValues.size();
		
//		int saveVarSearchStart = VarSearchStart;					// Save current VarSearchStart
//		VarSearchStart = fVarNumber;								// Set new start

		ArrayList<String> fVarName = new ArrayList<String>();        // List of parameter names
		ArrayList<Integer> fVarType = new ArrayList<Integer>();      // List of parameter types
		ArrayList<Integer> fVarArray = new ArrayList<Integer>();     // A list of indicating if parm is array

		if (!isNext(')')) {
			do{															// Get each of the parameter names
				doingDim = true;
				if (!getVar()) { return false; }
				doingDim = false;
				if (VarIsArray){										// Process array
					if (!isNext(']')) { return RunTimeError("Expected '[]'"); } // Array must not have any indices
					fVarArray.add(1);      // 1 Indicates var is array
				} else fVarArray.add(0);   // 0 Indicates var is not an array
						
				fVarName.add(VarNames.get(VarNumber));					// Save the name
				fVarType.add(VarIsNumeric ? 1 : 0);						// Save the type
			} while (isNext(','));
			if ( !(isNext(')') && checkEOL()) ) { return false; }
		}
		
		Bundle b = new Bundle();									// Build the bundle for the FunctionTable
		b.putInt("line", ExecutingLineIndex);                       // Line number of fn.def
		b.putString("fname", VarNames.get(fVarNumber));             // Name of this function
		b.putBoolean("isNumeric", fType);							// Type of function
		b.putStringArrayList("pnames", fVarName);                   // List of parameter names
		b.putIntegerArrayList("ptype", fVarType);                   // List of parameter types
		b.putIntegerArrayList("array", fVarArray);					// List of parameter array flags
		
		int fn = FunctionTable.size();
		FunctionTable.add(b);										// Add the Bundle to the function table
		
		VarIndex.set(fVarNumber, fn);								// Associate the function number with
																	// the function name
		
//		VarSearchStart = saveVarSearchStart;					// Restore Var Search Start
		trimArray(VarNames, fVarNumber+1);						// Remove parameter names from the
		trimArray(VarIndex, fVarNumber+1);						// variable name and index tables
		trimArray(NumericVarValues, NVV);						// Numeric var values
		trimArray(StringVarValues, SVV);						// String var values
		
		++ExecutingLineIndex;
		do{																	  // Now scan for the fn.end
			if (ExecutingLineIndex >= Basic.lines.size()) break;
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);        
			if (ExecutingLineBuffer.startsWith("fn.end")) break;              // Break out when found
			if (ExecutingLineBuffer.startsWith("fn.def")){					  // Insure not trying to define function in function
				RunTimeError("Can not define a function within a function at:");
				return false;
			}
			++ExecutingLineIndex;
		}while (ExecutingLineIndex < Basic.lines.size());
		
		if (ExecutingLineIndex >= Basic.lines.size()){                        // if at end of program, fn.end not found                 
			RunTimeError("No fn.end for this function");
			return false;
		}

		return true;
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
		if (isUserFunction(true)) return doUserFunction();
		if (!isUserFunction(false)) return false;
		return doUserFunction();
			}
	
	private boolean isUserFunction(boolean isNumeric){
		
		Bundle b;
		if (FunctionTable.size() == 0) return false;					// If function table empty, return fail
		
		int i = 0;

		for (i = 0; i<FunctionTable.size(); ++i){				        // scan the Function Tables
			 b = FunctionTable.get(i);									// get the bundle
			 String name = b.getString("fname");						// get the function name
			 if (ExecutingLineBuffer.startsWith(name, LineIndex)){					            // if in list
				 if (isNumeric != b.getBoolean("isNumeric")) return false;
				 ufBundle = b;
				 LineIndex = LineIndex + name.length();
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
	fsb.putInt("SCOV", scOpValue);
	fsb.putString("PKW", PossibleKeyWord);
	fsb.putString("fname",ufBundle.getString("fname"));

	ArrayList<String> fVarName = new ArrayList<String>();				// The list of Parm Var Names
	ArrayList<Integer> fVarType = new ArrayList<Integer>();				// and the parm types
	ArrayList<Integer> fVarArray = new ArrayList<Integer>();			// and the parm types
	
	fVarName = ufBundle.getStringArrayList("pnames"); 					// Get the Names and Types from the ufBundle                  
	fVarType = ufBundle.getIntegerArrayList("ptype");					// ufBundle set by isUserFunction()
	fVarArray = ufBundle.getIntegerArrayList("array");
	
	int pCount = fVarName.size();										// The number of parameter

	int i = 0;
	if (pCount != 0) {													// For each parameter
		do {
			if (i >= pCount){														// Insure no more parms than defined
				RunTimeError("Calling parameter count exceeds defined parameter count");
				SyntaxError = true;
				return false;
				}
		
			boolean typeIsNumeric = (fVarType.get(i) != 0);
			if (fVarArray.get(i) == 1){												// if this parm is an array
				SkipArrayValues = true;
				if (!getVar()) { SkipArrayValues = false; return false; }			// get the calling array
				SkipArrayValues = false;
				if (!(VarIsArray && isNext(']'))) { return false; }					// make sure it is an array with no index
				if (typeIsNumeric != VarIsNumeric) {								// Insure type (string or number) match
					RunTimeError("Array parameter type mismatch at:");
					return false;
				}
				
				VarNames.add(fVarName.get(i));						// and add the var name
				VarIndex.add(VarIndex.get(VarNumber));				// copy array table pointer to the new array.
			} // end array
			else{
				boolean isGlobal = isNext('&');   // if this is a Global Var
				if (isGlobal) {
					if (!getVar()) { return false; }				// then must be var not expression
					if (VarIsNew){
						RunTimeError("Call by reference vars must be predefined");
						return false;
					}
					if (typeIsNumeric != VarIsNumeric) {								// Insure type (string or number) match
						RunTimeError("Global parameter type mismatch at:");
						return false;
					}
					int thisValueIndex = VarIndex.get(VarNumber);	// get the calling var's value index
					VarNames.add(fVarName.get(i));					// add the called var name to the var name table		
					VarIndex.add(thisValueIndex);					// but give it the value index of the calling var
				} // end global
				else {
					if (!typeIsNumeric) {								// if parm is string
						if (!evalStringExpression()){					// get the string value
							RunTimeError("Parameter type mismatch at:");
							return false;
						}else{
							VarIndex.add(StringVarValues.size());		// Put the string value into the
							StringVarValues.add(StringConstant);        // string var values table
							VarNames.add(fVarName.get(i));				// and add the var name
						}
					}else{
						if (!evalNumericExpression()){						// if parm is number
							RunTimeError("Parameter type mismatch at:");	// get the numeric value
							return false;
						}else{
							VarIndex.add(NumericVarValues.size());				// Put the number values into the
							NumericVarValues.add(EvalNumericExpressionValue);   // numeric var values table
							VarNames.add(fVarName.get(i));						// and add the var name
						}
					}
				}
			} // end scalar
			
			++i;											//  Keep going while calling parms exist
				
		} while ( isNext(','));
	} // end if

	if (i!=pCount){
		RunTimeError("Too few calling parameters at:");
		return false;
	}

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
	scOpValue = fsb.getInt("SCOV");
//	trimArray(NumericVarValues, fsb.getInt("NVV"));
//	trimArray(StringVarValues, fsb.getInt("SVV"));
//	trimArray(ArrayTable, fsb.getInt("AT"));
	ExecutingLineIndex = fsb.getInt("ELI");
	LineIndex = fsb.getInt("LI");
	PossibleKeyWord = fsb.getString("PKW");
	
	ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
	
	return flag;                                      // Pass on the pass/fail state from the function
		}

// **************************** SWITCH Statements *************************************
	
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
		if (!getNVar()){return false;}				// Next parameter is the FileNumber variable
		NumericVarValues.set(theValueIndex, (double) FileTable.size());
		int saveValueIndex = theValueIndex;         // Save in case read file not found.

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }		// Final paramter is the filename
		String fileName = StringConstant;
		if (!checkEOL()) return false;

		Bundle FileEntry = new Bundle();			// Prepare the filetable bundle
		FileEntry.putInt("mode", FileMode);
		FileEntry.putBoolean("eof", false);
		FileEntry.putBoolean("closed",false);
		FileEntry.putInt("position", 1);
		FileEntry.putBoolean("isText", true);

		File file = new File(Basic.getDataPath(fileName));

		if (FileMode == FMR) {											// Read was selected
			BufferedReader buf = null;
			if (file.exists()) {
				try {
					buf = new BufferedReader(new FileReader(file), 8192);
					if (buf != null) buf.mark((int) file.length());
				} catch (Exception e) {
					return RunTimeError(e);
				}
			} else {													// file does not exist
				if (Basic.isAPK) {										// if not standard BASIC! then is user APK
					int resID = getRawResourceID(fileName);				// try to load the file from a raw resource
					if (resID == 0) { return false; }
					try {
						InputStream inputStream = BasicContext.getResources().openRawResource(resID);
						InputStreamReader inputreader = new InputStreamReader(inputStream);
						buf = new BufferedReader(inputreader, 8192);
					} catch (Exception e) {
						return RunTimeError(e);
					}
				} else {												// standard BASIC!
					NumericVarValues.set(saveValueIndex, (double) -1);	// report file does not exist
					return true;
				}
			}

			FileEntry.putInt("stream", BRlist.size());		// The stream parm indexes
			BRlist.add(buf);								// into the FISlist
			FileTable.add(FileEntry);
		}

		else if (FileMode == FMW) {										// Write Selected
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
			FWlist.add(writer);								// into the FISlist
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
			if (!getVar()) return false;
			if (VarIsNumeric) return false;
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
			if (title != null) intent.putExtra("title", title);
			HaveTextInput = false;
		    startActivityForResult(intent, BASIC_GENERAL_INTENT);
		    while (!HaveTextInput) Thread.yield();
		    
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

			if (!isNext(',')) { return false; }
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
			if (title != null) intent.putExtra("title", title);
			HaveTextInput = false;
		    startActivityForResult(intent, BASIC_GENERAL_INTENT);
		    while (!HaveTextInput) Thread.yield();
		    Show(Prompt + TextInputString);
		    
		    StringVarValues.set(saveValueIndex, TextInputString);
			return true;
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
		if (!getNVar()){return false;}				// Next parameter is the FileNumber variable
		NumericVarValues.set(theValueIndex, (double) FileTable.size());
		int saveValueIndex = theValueIndex;         // Save in case read file not found.

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
			BufferedInputStream bis = null;
			if (fileName.startsWith("http")) {
				try {
					URL url = new URL(fileName);
					URLConnection connection = url.openConnection();
					InputStream fis = connection.getInputStream();
					bis = new BufferedInputStream(fis, 8192);
				} catch (Exception e) {
					RunTimeError("Problem: " + e + " at:");
					return false;
				}
			} else {
				File file = new File(Basic.getDataPath(fileName));
				if (file.exists()) {
					try {
						FileInputStream fis = new FileInputStream(file);
						bis = new BufferedInputStream(fis, 8192);
						if (bis != null) bis.mark((int) file.length());
					} catch (Exception e) {
						return RunTimeError(e);
					}
				} else {													// file does not exist
					if (Basic.isAPK) {										// if not standard BASIC! then is user APK
						int resID = getRawResourceID(fileName);				// try to load the file from a raw resource
						if (resID == 0) { return false; }
						try {
							InputStream inputStream = BasicContext.getResources().openRawResource(resID);
							bis = new BufferedInputStream(inputStream, 8192);
						} catch (Exception e) {
							return RunTimeError(e);
						}
					} else {												// standard BASIC!
						NumericVarValues.set(saveValueIndex, (double) -1);	// report file does not exist
						return true;
					}
				}
			}

			FileEntry.putInt("stream", BISlist.size());		// The stream parm indexes
			BISlist.add(bis);								// into the FISlist
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
			DOSlist.add(dos);								// into the FISlist
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
//				Log.e(Run.LOGTAG, e.getLocalizedMessage() + " 3");
				return RunTimeError(e);
			}
		} else if (FileMode == FMW) {						// close file open for write
			DataOutputStream dos = DOSlist.get(FileEntry.getInt("stream"));
			try {
				dos.flush();
				dos.close();
			} catch (IOException e) {
//				Log.e(Run.LOGTAG, e.getLocalizedMessage() + " 3");
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

	// ***************************** File Operations ***********************************

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

	private boolean executeFILE_EXISTS(){
		if (!getNVar()) { return false; }					// get the var to put the result into
		int saveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the file name
		String fileName = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		File file = new File(Basic.getDataPath(fileName));
		double exists = file.exists() ? 1 : 0;				// does it exist?
		NumericVarValues.set(saveValueIndex, exists);
		return true;
	}

	private boolean executeFILE_SIZE(){
		if (!getNVar()) { return false; }					// get the var to put the size value into
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }				// get the file name
		String fileName = StringConstant;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		File file = new File(Basic.getDataPath(fileName));
		if (!file.exists()) {								// error if the file does not exist
			return RunTimeError(fileName + " not found");
		}
		double size = file.length();
		NumericVarValues.set(SaveValueIndex, size);			// Put the file size into the var

		return true;
	}

	private boolean executeFILE_ROOTS(){
		if (!getSVar()) { return false; }
		int SaveVarIndex = theValueIndex;

		if (!checkEOL()) { return false; }
		if (!checkSDCARD('r')) { return false; }

		File file = new File(Basic.getDataPath(null));		// get path to default data directory
		String s = "";
		try { s = file.getCanonicalPath(); }
		catch(IOException e) {}
		
		StringVarValues.set(SaveVarIndex, s);
		return true;
	}

	private boolean executeDIR(){
		if (!getStringArg()) { return false; }						// get the path
		String filePath = StringConstant;

		if (!isNext(',')) { return false; }							// parse the ,D$[]
		if (!getArrayVarForWrite()) { return false; }				// Get the array variable
		int SaveVarNumber = VarNumber;

		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (VarIsNumeric) { return RunTimeError("Not string array"); } // Insure that it is a string array
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
											// Go through the file list and mark directory entries with (d)
			String absPath = lbDir.getAbsolutePath() + '/';
			for (String s : FL) {
				File test = new File(absPath + s);
				if (test.isDirectory()) {							// If file is a directory, add "(d)"
					dirs.add(s + "(d)");							// and add to display list
				} else {
					files.add(s);									// else add name without the (d)
				}
			}
			Collections.sort(dirs);									// Sort the directory list
			Collections.sort(files);								// Sort the file list
			dirs.addAll(files);										// copy the file list to end of dir list
		}
		int length = dirs.size();									// number of directories and files in list
		if (length == 0) { length = 1; }							// make at least one element if dir is empty
																	// it will be an empty string
		return ListToBasicStringArray(SaveVarNumber, dirs, length);	// Copy the list to a BASIC! array
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
		String result = null;
		IOException ex = null;

		File file = new File(Basic.getDataPath(theFileName));		// Add the filename to the base path
		try {														// Open the reader for the SD Card
			FileInputStream fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			result = grabStream(bis, textFlag);
		} catch (IOException e) {
			ex = e;
		} finally {
			ex = closeStream(bis, ex);
			if (ex != null) { return RunTimeError(ex); }			// Report first exception, if any, and if no previous RTE set
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

	private boolean executeTIMEZONE(){										// Get TimeZone command key word if it is there
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
			 if (!evalNumericExpression())return false;							// Get pause duration value
		     double d = EvalNumericExpressionValue;
		     if (d<1){
		    	 RunTimeError("Pause must be greater than zero");
		    	 return false;
		     }
			if (!checkEOL()) return false;

		  
		  try {Thread.sleep((int) d);}catch(InterruptedException e){}

		  return true;
	  }
	  
	  private boolean executeBROWSE(){
		  
		  if (!evalStringExpression()){return false;}
		  if (SEisLE) return false;
		  if (!checkEOL()) return false;

		  String url = StringConstant;
		  Intent i = new Intent(Intent.ACTION_VIEW);						  // Go to the html page
//		  Intent i = new Intent(Intent.CATEGORY_BROWSABLE);
		  i.setData(Uri.parse(url));
      	try {Thread.sleep(500);}catch(InterruptedException e){}              // Sleep here stopped forced stop exceptions

		  try {startActivity(i);}
//		  catch ( android.content.ActivityNotFoundException  e){
		  catch ( Exception  e){
			  return RunTimeError(e);
		  }
		  return true;
	  }
	  
	  
	  private boolean executeINKEY(){
		  
		  if (!getSVar()) return false;						// get the var to put the key value into
		  if (!checkEOL()) return false;
		  if (InChar.size() > 0){
			  StringVarValues.set(theValueIndex, InChar.get(0));
			  InChar.remove(0);
		  } else
			  StringVarValues.set(theValueIndex, "@");
		  return true;
	  }
	  
	  private boolean executeONKEY_RESUME(){
		  if (interruptResume == -1 ) {
			  RunTimeError("No Current Key Interrupt");
			  return false;
		  }
		  return doResume();
	  }
	  
	  private boolean executePOPUP(){
		  if (!evalStringExpression()) return false;							// get the message
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  if (SEisLE) return false;
		  ToastMsg  = StringConstant;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get x
		   double d = EvalNumericExpressionValue;
		   ToastX = (int) d;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get y
		   d = EvalNumericExpressionValue;
		   ToastY = (int) d;
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get duration
		   d = EvalNumericExpressionValue;

		   if (!checkEOL()) return false;

		   ToastDuration = 0;
		   if (d!=0) ToastDuration = 1;
		   PrintShow("@@4");															// Signals On Progres Update
		   																		// (the UI Task)
		   																		// to show the toast

		  return true;
	  }
	  
	  public boolean executeCLS(){							// Clear Screen
		if (!checkEOL()) return false;
		  PrintShow("@@5");										// Signal UI task
		  return true;
	  }

	public boolean executeSELECT(){

		if (!getNVar()) return false;								// get the var to put the key value into
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		int saveLineIndex = LineIndex;
		SkipArrayValues = true;
		boolean isArrayVar = getVar() && VarIsArray && isNext(']');
		SkipArrayValues = false;
		if (SyntaxError) return false;

		if (isArrayVar) {
			if (VarIsNumeric) { return RunTimeError("Not string array"); }

			Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber)); // Get the array table bundle for this array
			int length = ArrayEntry.getInt("length");				// get the array length
			int base = ArrayEntry.getInt("base");					// and the start of values in the value space

			SelectList = new ArrayList<String>();					// Create a list to copy array values into

			for (int i = 0; i < length; ++i) { 						// Copy the array values into that list
				SelectList.add(StringVarValues.get(base+i));
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
			SelectList = theLists.get(listIndex);
		}

		if ( !(isNext(',') && getStringArg()) ) return false;
		SelectMessage = StringConstant;

		int isLongClickValueIndex = -1;
		if (isNext(',')) {											// If no comma then not long press var
			if (!getNVar()) return false;							// Get the long press var
			isLongClickValueIndex = theValueIndex;
		}
		if (!checkEOL()) return false;
		
		SelectedItem = 0;
		ItemSelected = false;
		SelectLongClick = false;
		
		startActivityForResult(new Intent(this, Select.class), BASIC_GENERAL_INTENT);
		while (!ItemSelected) Thread.yield();						// Wait for signal from Selected.java thread
		
		NumericVarValues.set(SaveValueIndex, (double) SelectedItem); // Put the item selected into the var
		
		if (isLongClickValueIndex != -1) {
			double isLongPress = SelectLongClick ? 1 : 0;			// Get the actual value
			NumericVarValues.set(isLongClickValueIndex, isLongPress); // Set the return value
		}

		return true;
	}

	private boolean executeSPLIT(int limit){

		if (!getArrayVarForWrite()) { return false; }				// Get the result array variable
		if (VarIsNumeric) { return RunTimeError("Not string array"); } // Insure that it is a string array
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		int SaveArrayVarNumber = VarNumber;

		if (!isNext(',')) { return false; }
		if (!getStringArg()) { return false; }						// Get the string to split
		String SearchString = StringConstant;

		String r[] = doSplit(SearchString, limit);					// Get regex arg, if any, and split the string.
		if (!checkEOL()) { return false; }

		int length = r.length;										// Get the number of strings generated
		if (length == 0) { return false; }							// error in doSplit()

		return ListToBasicStringArray(SaveArrayVarNumber, Arrays.asList(r), length);
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
		} catch (PatternSyntaxException pse) {
			RunTimeError(REString + " is invalid argument at");
		} catch (Exception e) {
			RunTimeError(e);
		}
		return r;
	}

	  private boolean executeKBSHOW(){
		  if (!checkEOL()) return false;
			Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " KBSHOW  " + kbShown );

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
	  
	  
	  private boolean executeKBHIDE(){
			if (!checkEOL()) return false;
			Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " KBHIDE  " + kbShown);

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
		  PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		  
		  if (!evalNumericExpression())return false;							// Get x
		  
		  int code  = EvalNumericExpressionValue.intValue();
		  
		  if (theWakeLock != null){
			  theWakeLock.release();
			  theWakeLock = null;
		  }
		  
		  
		  switch (code){
		  	case partial:
		  		theWakeLock = pm.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK,
                        "BASIC!");
		  		theWakeLock.acquire();
		  		break;
		  	case dim:
		  		theWakeLock = pm.newWakeLock(
                        PowerManager.SCREEN_DIM_WAKE_LOCK,
                        "BASIC!");
		  		theWakeLock.acquire();
		  		break;
		  	case bright:
		  		theWakeLock = pm.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                        "BASIC!");
		  		theWakeLock.acquire();
		  		break;
		  	case full:
		  		theWakeLock = pm.newWakeLock(
                        PowerManager.FULL_WAKE_LOCK,
                        "BASIC!");
		  		theWakeLock.acquire();
		  		break;
		  	case release:
		  		break;
		  	default:
		  		RunTimeError("WakeLock code not 1 - 5");
		  		return false;
		  }

		  return true;
	  }
	  
	  
	  private boolean executeTONE(){
		  
		    double duration = 1; 				// seconds
		    double freqOfTone = 1000; 			// hz
		    int sampleRate = 8000;				// a number

		  if (!evalNumericExpression())return false;			// Get frequency
		  freqOfTone =  EvalNumericExpressionValue;
		  
		  if (!isNext(',')) return false;

		  if (!evalNumericExpression())return false;			// Get duration
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
	    	
	    		int minBuffer = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
	    				AudioFormat.ENCODING_PCM_16BIT);
	    		if (numSamples< minBuffer){
	    			double minDuration = Math.ceil(1000 * (double)minBuffer/(double)sampleRate);
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
	        			sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
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

		if (!getArrayVarForRead()) return false;					// Get the array variable
		if (!VarIsNumeric) { return RunTimeError("Array not numeric"); } // Insure that it is a numeric array
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));// Get the array table bundle for this array

		if (!isNext(',')) return false;								// Get the repeat value
		if(!evalNumericExpression()) return false;
		int repeat = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;

		int length = ArrayEntry.getInt("length");					// get the array length
		int base = ArrayEntry.getInt("base");						// and the start of the array in the variable space
		
		long Pattern[] = new long[length];							// Pattern array
		for (int i = 0; i<length; ++i){								// Copy user array into pattern
			Pattern[i] = NumericVarValues.get(base+i).longValue();
		}

		if (repeat > 0) myVib.cancel();
		else myVib.vibrate(Pattern, repeat);						// Do the vibrate

		return true;
	}

	  private boolean executeDEVICE(){
		  
		  if (!getSVar()) return false;
		  
		  String info = "";
		  info = info + "Brand = " + android.os.Build.BRAND + "\n";
		  info = info + "Model = " + android.os.Build.MODEL + "\n";
		  info = info + "Device = " + android.os.Build.DEVICE + "\n";
		  info = info + "Product = " + android.os.Build.PRODUCT + "\n";
		  info = info + "OS = " + android.os.Build.VERSION.RELEASE;
		  
		  StringVarValues.set(theValueIndex, info);
		  
		  return true;
	  }
	  
	  private boolean executeHTTP_POST(){
		  if (!evalStringExpression()) return false;
		  String url = StringConstant;
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		  ++LineIndex;
		  
		   if (!evalNumericExpression())return false;
		   int theListIndex = EvalNumericExpressionValue.intValue();
		   if (theListIndex <1 || theListIndex>= theLists.size()){
			   RunTimeError("Invalid list pointer");
			   return false;
		   }
		   if (theListsType.get(theListIndex) == list_is_numeric){
			   RunTimeError("List must be of string type.");
			   return false;
		   }
		   
		   List<String> thisList = theLists.get(theListIndex);
		   
		   int r = thisList.size() % 2;
		   if (r !=0 ) {
			   RunTimeError("List must have even number of elements");
			   return false;
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

		    	} catch (Exception e){
		    		RunTimeError("! " + e);
		    		return false;
		    	} 

		   if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		   ++LineIndex;		   
		   if (!getSVar()) return false;		   
		   StringVarValues.set(theValueIndex, Result);
		   
		   if (!checkEOL()) return false;

		  
		  return true;
	  }
	  
	  
	  // ************************************************ SQL Package ***************************************	  

	private boolean executeSQL(){									// Get SQL command key word if it is there
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

		if (!getVar()) return false;								// DB Pointer Variable
		if (!VarIsNumeric) return false;							// for the DB table pointer
		int SaveValueIndex = theValueIndex;

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
				   if (!getSVar())return false;					// Get next result variable
				   try{
				   result = cursor.getString(index); 			// get the result
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

		String Where = null;										// if no Where given, set null
		if (isNext(',')) {										// if no comma, then no optional Where
			if (!getStringArg()) return false;					// Where Value
			Where = StringConstant;
		}
		if (!checkEOL()) return false;

	   try {
		   db.delete(TableName, Where, null);					// do the deletes
	   }
	   catch (Exception e){
		   return RunTimeError(e);
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

// **********************************************  Graphics Package **********************************************
   
   public  boolean executeGR(){
   	if (!GetGRKeyWord()){ return false;
   	}else if (!GRopen && KeyWordValue != gr_open) {
   		RunTimeError("Graphics not opened at:");
   		return false;
   		}
   		else{
   	    	  switch (KeyWordValue){
   	    	  	case gr_open:
   	    	  		if (!execute_gr_open()){return false;}
   	    	  		break;
   	    	  	case gr_render:
   	    	  		if (!execute_gr_render()){return false;}
   	    	  		break;
   	    	  	case gr_color:
   	    	  		if (!execute_gr_color()){return false;}
   	    	  		break;
   	    	  	case gr_line:
   	    	  		if (!execute_gr_line()){return false;}
   	    	  		break;
   	    	  	case gr_rect:
   	    	  		if (!execute_gr_rect()){return false;}
   	    	  		break;
   	    	  	case gr_arc:
   	    	  		if (!execute_gr_arc()){return false;}
   	    	  		break;
   	    	  	case gr_circle:
   	    	  		if (!execute_gr_circle()){return false;}
   	    	  		break;
   	    	  	case gr_oval:
   	    	  		if (!execute_gr_oval()){return false;}
   	    	  		break;
   	    	  	case gr_cls:
   	    	  		if (!execute_gr_cls()){return false;}
   	    	  		break;
   	    	  	case gr_hide:
   	    	  		if (!execute_gr_hide()){return false;}
   	    	  		break;
   	    	  	case gr_show:
   	    	  		if (!execute_gr_show()){return false;}
   	    	  		break;
   	    	  	case gr_touch:
   	    	  		if (!execute_gr_touch(0)){return false;}
   	    	  		break;
   	    	  	case gr_touch2:
   	    	  		if (!execute_gr_touch(1)){return false;}
   	    	  		break;
  	    	  	case gr_text_draw:
   	    	  		if (!execute_gr_text_draw()){return false;}
   	    	  		break;
   	    	  	case gr_text_align:
   	    	  		if (!execute_gr_text_align()){return false;}
   	    	  		break;
   	    	  	case gr_text_size:
   	    	  		if (!execute_gr_text_size()){return false;}
   	    	  		break;
   	    	  	case gr_text_underline:
   	    	  		if (!execute_gr_text_underline()){return false;}
   	    	  		break;
   	    	  	case gr_text_skew:
   	    	  		if (!execute_gr_text_skew()){return false;}
   	    	  		break;
   	    	  	case gr_text_bold:
   	    	  		if (!execute_gr_text_bold()){return false;}
   	    	  		break;
   	    	  	case gr_text_strike:
   	    	  		if (!execute_gr_text_strike()){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_load:
   	    	  		if (!execute_gr_bitmap_load()){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_scale:
   	    	  		if (!execute_gr_bitmap_scale()){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_draw:
   	    	  		if (!execute_gr_bitmap_draw()){return false;}
   	    	  		break;
   	    	  	case gr_rotate_start:
   	    	  		if (!execute_gr_rotate_start()){return false;}
   	    	  		break;
   	    	  	case gr_rotate_end:
   	    	  		if (!execute_gr_rotate_end()){return false;}
   	    	  		break;
   	    	  	case gr_modify:
   	    	  		if (!execute_gr_modify()){return false;}
   	    	  		break;
   	    	  	case gr_orientation:
   	    	  		if (!execute_gr_orientation()){return false;}
   	    	  		break;
   	    	  	case gr_screen:
   	    	  		if (!execute_gr_screen()){return false;}
   	    	  		break;
   	    	  	case gr_close:
   	    	  		if (!execute_gr_close()){return false;}
   	    	  		break;
   	    	  	case gr_front:
   	    	  		if (!execute_gr_front()){return false;}
   	    	  		break;
   	    	  	case gr_bound_touch:
   	    	  		if (!execute_gr_bound_touch(0)){return false;}
   	    	  		break;
   	    	  	case gr_bound_touch2:
   	    	  		if (!execute_gr_bound_touch(1)){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_size:
   	    	  		if (!execute_gr_bitmap_size()){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_delete:
   	    	  		if (!execute_gr_bitmap_delete()){return false;}
   	    	  		break;
   	    	  	case gr_set_pixels:
   	    	  		if (!execute_gr_set_pixels()){return false;}
   	    	  		break;
   	    	  	case gr_get_pixel:
   	    	  		if (!execute_gr_get_pixel()){return false;}
   	    	  		break;
   	    	  	case gr_save:
   	    	  		if (!execute_gr_save()){return false;}
   	    	  		break;
   	    	  	case gr_text_width:
   	    	  		if (!execute_gr_text_width()){return false;}
   	    	  		break;
   	    	  	case gr_scale:
   	    	  		if (!execute_gr_scale()){return false;}
   	    	  		break;
   	    	  	case gr_getdl:
   	    	  		if (!execute_gr_getdl()){return false;}
   	    	  		break;
   	    	  	case gr_newdl:
   	    	  		if (!execute_gr_newdl()){return false;}
   	    	  		break;
   	    	  	case gr_clip:
   	    	  		if (!execute_gr_clip()){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_crop:
   	    	  		if (!execute_gr_bitmap_crop()){return false;}
   	    	  		break;
   	    	  	case gr_stroke_width:
   	    	  		if (!execute_gr_stroke_width()){return false;}
   	    	  		break;
   	    	  	case gr_poly:
   	    	  		if (!execute_gr_poly()){return false;}
   	    	  		break;
   	    	  	case gr_statusbar_show:
   	    	  		if (!execute_statusbar_show()){return false;}
   	    	  		break;
   	    	  	case gr_bitmap_save:
   	    	  		if (!execute_bitmap_save()){return false;}
   	    	  		break;
   	    	  	case gr_camera_shoot:
   	    	  		CameraAuto = false;
   	    	  		CameraManual = false;
   	    	  		if (!execute_camera_shoot()){return false;}
   	    	  		break;
   	    	  	case gr_camera_autoshoot:
   	    	  		CameraAuto = true;
   	    	  		CameraManual = false;
   	    	  		if (!execute_camera_shoot()){return false;}
   	    	  		break;
   	    	  	case gr_camera_manualshoot:
   	    	  		CameraAuto = false;
   	    	  		CameraManual = true;
   	    	  		if (!execute_camera_shoot()){return false;}
   	    	  		break;
   	    	  	case gr_screen_to_bitmap:
   	    	  		if (!execute_screen_to_bitmap()){return false;}
   	    	  		break;
   	    	  	case gr_paint_get:
   	    	  		if (!execute_paint_get()){return false;}
   	    	  		break;
   	    	  	case gr_brightness:
   	    	  		if (!execute_brightness()) return false;
   	    	  		break;
   	    	  	case gr_bitmap_create:
   	    	  		if (!execute_gr_bitmap_create()) return false;
   	    	  		break;
   	    	  	case gr_bitmap_drawinto_start:
   	    	  		if (!execute_gr_bitmap_drawinto_start()) return false;
   	    	  		break;
   	    	  	case gr_bitmap_drawinto_end:
   	    	  		if (!execute_gr_bitmap_drawinto_end()) return false;
   	    	  		break;
   	    	  	case gr_get_position:
   	    	  		if (!execute_gr_get_position()) return false;
   	    	  		break;
   	    	  	case gr_get_bmpixel:
   	    	  		if (!execute_gr_get_bmpixel()) return false;
   	    	  		break;
   	    	  	case gr_get_value:
   	    	  		if (!execute_gr_get_value()) return false;
   	    	  		break;
   	    	  	case gr_antialias:
   	    	  		if (!execute_gr_antialias()) return false;
   	    	  		break;
   	    	  	case gr_get_textbounds:
   	    	  		if (!execute_gr_get_texbounds()) return false;
   	    	  		break;
   	    	  	case gr_text_typeface:
   	    	  		if (!execute_gr_text_typeface()) return false;
   	    	  		break;
   	    	  	case gr_ontouch_resume:
   	    	  		if (!execute_gr_touch_resume()) return false;
   	    	  		break;
   	    	  	case gr_camera_select:
   	    	  		if (!execute_gr_camera_select()) return false;
   	    	  		break;
   	    	  		
   	    	  	default:
   	    	  		return false;
   	    	  }
   	    	  return true;
   	      }
   	   }
 
   
	  private  boolean GetGRKeyWord(){						// Get a Basic key word if it is there
															// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<GR_KW.length; ++i){					// loop through the key word list
			  if (Temp.startsWith(GR_KW[i])){    				// if there is a match
				  KeyWordValue = i;								// set the key word number
				  LineIndex = LineIndex + GR_KW[i].length(); 	// move the line index to end of key word
				  return true;									// and report back
			  }
		  }
		  KeyWordValue = gr_none;								// no key word found
		  return false;											// report fail
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

		if (!getArrayVarForWrite()) { return false; }				// Get the array variable
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (!VarIsNumeric) { return RunTimeError("Array not numeric"); } // Insure that it is a numeric array
		int arrayVarNumber = VarNumber;
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

		if (!BuildBasicArray(arrayVarNumber, true, count)) { return false; } // build the array
		for (int i = 0, j = ArrayValueStart; i < count; ++i, ++j) {	// stuff the array
			NumericVarValues.set(j, list[i]);						// count may be < list.length
		}
		return true;
	}

	  private boolean execute_gr_newdl(){
		  
		  if (!getArrayVarForRead()) return false;					// Get the array variable
		  if (!isNext(']')) { return RunTimeError("Expected '[]'"); } // Array must not have any indices
		  if (!VarIsNumeric) { return RunTimeError("Array not numeric"); } // Insure that it is a numeric array
		  if (!checkEOL()) { return false; }						// line must end with ']'

			Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));// Get the array table bundle for this array
			int length = ArrayEntry.getInt("length");				// get the array length
			int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space

			RealDisplayList.clear();
			RealDisplayList.add(0);									// First entry points to null bundle
			
			for (int i = 0; i < length; ++i){						// Copy the bundle pointers 
				double d = NumericVarValues.get(base+i);
				int id = (int) d;
				if (id < 0 || id >= DisplayList.size()){
					return RunTimeError("Invalid Object Number");
				}
				RealDisplayList.add(id);
			}

		  return true;
	  }
	  
	  public void PaintListAdd(Paint p){
		  PaintList.add(p);
	  }
	  
	  public Paint newPaint(Paint fromPaint){				// Does a new Paint
		  Typeface tf = fromPaint.getTypeface();			// while prsereving the type face
		  Paint rPaint = new Paint(fromPaint);
		  rPaint.setTypeface(tf);
		  return rPaint;
	  }
	  
 	  private boolean execute_gr_open(){
		  
		  if (GRopen) {
			  RunTimeError("Graphics already Opened");
			  return false;
		  }
		   if (!evalNumericExpression())return false;								// Get alpha
			  double d = EvalNumericExpressionValue;
			  int a = (int) d;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get red
			   d = EvalNumericExpressionValue;
			   int r = (int) d;
			   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get green
			   d = EvalNumericExpressionValue;
			   int g = (int) d;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;

			  if (!evalNumericExpression())return false;							// Get blue
			   d = EvalNumericExpressionValue;
			   int b = (int) d;
			   
			  GR.theBackGround = a * 0x1000000 +									// Set the appropriate bytes
			  					 r * 0x10000 +
			  					 g * 0x100 +
			  					 b ;

			  ShowStatusBar = 0;
			   if (ExecutingLineBuffer.charAt(LineIndex) == ','){
				  ++LineIndex;
				  if (!evalNumericExpression())return false;
				  ShowStatusBar = EvalNumericExpressionValue.intValue();
			   }
			   
			   GRorientation = 0;
			   if (ExecutingLineBuffer.charAt(LineIndex) == ','){
					  ++LineIndex;
					  if (!evalNumericExpression())return false;
					  GRorientation = EvalNumericExpressionValue.intValue();
				   }

			   
				if (!checkEOL()) return false;

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

		  GraphicsPaused = false;							// Set up the signals
		  GRrunning = false;
     	  GRclass = new Intent(this, GR.class);				// Start the Graphics
     	  startActivityForResult(GRclass, BASIC_GENERAL_INTENT);
	       while (!GRrunning) Thread.yield();				// Do not continue until GR signals it is running
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
		  
		   if (!evalNumericExpression())return false;								// Get alph
			  double d = EvalNumericExpressionValue;
			  int a = (int) d;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get red
			   d = EvalNumericExpressionValue;
			   int r = (int) d;
			   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get green
			   d = EvalNumericExpressionValue;
			   int g = (int) d;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;

			  if (!evalNumericExpression())return false;							// Get blue
			   d = EvalNumericExpressionValue;
			   int b = (int) d;
			   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
				++LineIndex;

			  if (!evalNumericExpression())return false;							// Get fill
  			  d = EvalNumericExpressionValue;
  			  
  			if (!checkEOL()) return false;


		   Paint tPaint = newPaint(aPaint);					// Create a newPaint object
		   tPaint.setARGB(a, r, g, b);							// Set the colors, etc
//		   tPaint.setAntiAlias(true);
		   if (d ==0) tPaint.setStyle(Paint.Style.STROKE );
		   else if (d == 1) tPaint.setStyle(Paint.Style.FILL );
		   else tPaint.setStyle(Paint.Style.FILL_AND_STROKE);
		   aPaint = newPaint(tPaint);							// Copy the temp paint to aPaint
	       PaintListAdd(aPaint);								// Add the newPaint to the Paint List
		  return true;
	  }
	  
	  private boolean execute_gr_antialias(){
		  if (!evalNumericExpression())return false;		    // Get the boolean
		  Paint tPaint = newPaint(aPaint);
		  if (EvalNumericExpressionValue == 0) tPaint.setAntiAlias(false);
		  else tPaint.setAntiAlias(true);
		   aPaint = newPaint(tPaint);							// Copy the temp paint to aPaint
	       PaintListAdd(aPaint);								// Add the newPaint to the Paint List
		  return true;
	  }
	  
	  private boolean execute_gr_stroke_width(){
		   if (!evalNumericExpression())return false;								// Get the width
		   float width = EvalNumericExpressionValue.floatValue();
		   if (width<0){
			   RunTimeError("Width must be >= 0");
			   return false;
		   }
		   Paint tPaint = newPaint(aPaint);					// Create a newPaint object
		   tPaint.setStrokeWidth(width);						// Set the stroke width
		   aPaint = newPaint(tPaint);							// Copy the temp paint to aPaint
	       PaintListAdd(aPaint);								// Add the newPaint to the Paint List
			
		  return true;
	  }
	  	  
	  private boolean execute_gr_line(){
		  Bundle aBundle = new Bundle();
		  aBundle.putInt("type", GR.dLine);
		  aBundle.putInt("hide", 0);

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get x1
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("x1", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get y1
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("y1", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get x2
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("x2", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get y2
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("y2", (int) d);
		  
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

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get left
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("left", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get top
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("top", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get right
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("right", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get bottom
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("bottom", (int) d);
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

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get left
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("left", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get top
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("top", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get right
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("right", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get bottom
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("bottom", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get start angle
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("start_angle", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get end angle
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("sweep_angle", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get flag
		   d = EvalNumericExpressionValue;
		   aBundle.putInt("fill_mode", (int) d);
		
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
		  
		  
		   if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get x
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("x", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get x
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("y", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get r
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("radius", (int) d);

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

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get left
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("left", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get top
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("top", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get right
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("right", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get bottom
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("bottom", (int) d);

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
		  if (b1.getInt("hide") != 0)  return xfalse;								// If hidden then no collide
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

	  private Rect gr_getRect(Bundle b){
		  Rect theRect = null;
		  
		  int type = b.getInt("type");
     		switch (type) {
    		case GR.dCircle:
                theRect = new Rect();
    			int cx= b.getInt("x");
    			int cy =b.getInt("y");
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
    			Bitmap theBitmap = Run.BitmapList.get(b.getInt("bitmap"));
    			theRect.bottom = theRect.top + theBitmap.getHeight();
    			theRect.right = theRect.left + theBitmap.getWidth();
                break;
     		}


		  return theRect;
	  }

	  private boolean execute_gr_cls(){
			if (!checkEOL()) return false;
		  
		  DisplayListClear();
		  return true;
	  }

	  private boolean execute_gr_hide(){
		 if (!evalNumericExpression())return false;							// Get Object Number
	     double d = EvalNumericExpressionValue;
			if (!checkEOL()) return false;
	     if (d<0 || d>= DisplayList.size()){
	    	 Show("Hide parameter out of range");
	    	 Show(ExecutingLineBuffer);
	    	 SyntaxError= true;
	    	 return false;
	     }
	     Bundle b = DisplayList.get((int) d);			// Get the specified display object
	     b.putInt("hide", 1);							// Set hide to true
	     DisplayList.set((int) d, b);					// put the modified object back
		  return true;
	  }

	  private boolean execute_gr_show(){
			 if (!evalNumericExpression())return false;							// Get Object Number
		     double d = EvalNumericExpressionValue;
		     if (d<0 || d>= DisplayList.size()){
		    	 RunTimeError("Show parameter out of range");
		    	 return false;
		     }
				if (!checkEOL()) return false;
		     Bundle b = DisplayList.get((int) d);     // Get the specified display object
		     b.putInt("hide", 0);					  // Set hide to false
		     DisplayList.set((int) d, b);			  // put the modified object back
		  return true;
	  }
	  
	  private boolean execute_gr_get_position(){
			 if (!evalNumericExpression())return false;							// Get Object Number
		     double d = EvalNumericExpressionValue;
		     if (d<0 || d>= DisplayList.size()){
		    	 RunTimeError("Show parameter out of range");
		    	 return false;
		     }
		     Bundle b = DisplayList.get((int) d);     // Get the specified display object
		     
		     int x = 0;									// get the position value
		     int y = 0;
		     if (b.containsKey("x")) x = b.getInt("x");
		     else x = b.getInt("left");
		     if (b.containsKey("y")) y = b.getInt("y");
		     else y = b.getInt("top");

			 if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		     ++LineIndex;
		     if (!getNVar()) return false;
		  	 NumericVarValues.set(theValueIndex, (double) x);

			 if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		     ++LineIndex;
		     if (!getNVar()) return false;
		  	 NumericVarValues.set(theValueIndex, (double) y);

			 if (!checkEOL()) return false;

		  return true;
	  }
	  
	  private boolean execute_gr_get_value(){
			 if (!evalNumericExpression())return false;							// Get Object Number
		     double d = EvalNumericExpressionValue;
		     if (d<0 || d>= DisplayList.size()){
		    	 RunTimeError("Show parameter out of range");
		    	 return false;
		     }
		     Bundle b = DisplayList.get((int) d);     // Get the specified display object
		     
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;

			  if (!evalStringExpression()) return false;							// get the parameter string
			  if (SEisLE) return false;

			  String parm = StringConstant;
			  
			  if (!b.containsKey(parm)){
				  RunTimeError("Object does not contain " + parm);
				  return false;
			  }

			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (getNVar()){
//				  if (!checkEOL()) return false;
				  int value = b.getInt(parm);
				  NumericVarValues.set(theValueIndex, (double) value);
				  return true;
			  }
			  
			  if (!parm.equals("text")){
				  RunTimeError("Invalid tag: " + parm);
			  }
			  
			  if (!getSVar()) return false;
			  if (!checkEOL()) return false;

			  String theText = b.getString(parm);
			  StringVarValues.set(theValueIndex, theText);

		  return true;
	  }
	  
	  private boolean execute_gr_touch(int p){
		   if (!getVar())return false;							// Graphic boolean Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveBooleanIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!getVar())return false;							// Graphic X variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveXIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!getVar())return false;							// Graphic Y variable
		   if (!VarIsNumeric)return false;						// 
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
		  
		   if (!getVar())return false;							// Graphic boolean Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveBooleanIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   
		   	  if (!evalNumericExpression())return false;							// Get left
			  double left = EvalNumericExpressionValue;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get top
			  double top = EvalNumericExpressionValue;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get right
			  double right = EvalNumericExpressionValue;
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;

			  if (!evalNumericExpression())return false;							// Get bottom
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
			   }else NumericVarValues.set(SaveBooleanIndex, 0.0);  // else retrun the false boolean to the user
			   
		  return true;
	  }

	  private boolean execute_gr_text_draw(){
		  Bundle aBundle = new Bundle();       // Create a new object of type text
		  aBundle.putInt("type", GR.dText);
		  aBundle.putInt("hide", 0);

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get x
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("x", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get y
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("y", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalStringExpression()) return false;							// Get the text
		  if (SEisLE) return false;
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
		  if (!evalNumericExpression())return false;							// Get Align parameter
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
			if (!evalNumericExpression())return false;   // Get desired size
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
			if (!evalNumericExpression())return false;							// Get boolean
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
			if (!evalNumericExpression())return false;							// Get Skew
			double d = EvalNumericExpressionValue;
			if (!checkEOL()) return false;
			tPaint.setTextSkewX((float)d);
			 aPaint = newPaint(tPaint);
			 PaintListAdd(aPaint);
			 return true;
		  }
	  
	  private boolean execute_gr_text_bold(){
			Paint tPaint = newPaint(aPaint);
			if (!evalNumericExpression())return false;							// Get boolean 
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
			if (!evalNumericExpression())return false;							// Get boolean 
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

	  private boolean execute_gr_text_width(){
		   if (!getVar())return false;							// Width return  Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   
		   if (!evalStringExpression()) return false;			// Get the string
			if (!checkEOL()) return false;
		   
		   float w = aPaint.measureText(StringConstant);        // Get the strings width
		   NumericVarValues.set(SaveValueIndex, (double) w);    // Save the width into the var
		   
		  return true;
	  }
	  
	private String getAlternateRawFileName(String input) {
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

	private int getRawResourceID(String fileName) {
		if (fileName == null) fileName = "";
		String packageName = BasicContext.getPackageName();				// Get the package name
		int resID = 0;													// 0 is not a valid resource ID
		for (int attempt = 1; (resID == 0) && (attempt <= 2); ++attempt) {
			String rawFileName =
				(attempt == 1) ? getAlternateRawFileName(fileName) :	// Convert conventional filename to raw resource name, BASIC!-style
				(attempt == 2) ? Basic.getRawFileName(fileName) : "";	// If first try didn't work, try again, Android-style.
			if (!rawFileName.equals("")) {
				resID = BasicContext.getResources().getIdentifier(rawFileName, "raw", packageName);	// Get the resource ID
			}
		}
		if (resID == 0) {
			RunTimeError("Error getting raw resource");
		}
		return resID;
	}

	private boolean execute_gr_bitmap_load(){
		if (!getNVar()) return false;									// Graphic Bitmap Pointer Variable
		int SaveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;								// Get the file path
		if (!checkEOL()) return false;

		String fileName = StringConstant;								// The filename as given by the user
		File file = new File(Basic.getDataPath(fileName));
		InputStream inputStream = null;									// Establish an input stream

		if (file.exists()) {
			try {
				inputStream = new FileInputStream(file);				// Open an input stream from the SDCARD file
			} catch (IOException e) {
				closeStream(inputStream, e);
				return RunTimeError(e);
			}
		} else {														// file does not exist
			if (Basic.isAPK) {											// if not standard BASIC! then is user APK
				int resID = getRawResourceID(fileName);					// try to load the file from a raw resource
				if (resID == 0) { return false; }
				try {
					inputStream = BasicContext.getResources().openRawResource(resID);	// Open an input stream from raw resource
				} catch (Exception e) {
					closeStream(inputStream, null);
					return RunTimeError(e);
				}
			}															// else standard BASIC!, inputStream is still null
		}
		   
		   System.gc();															// Garbage collect

		aBitmap = BitmapFactory.decodeStream(inputStream);				// Create bitmap from the input stream
		try { inputStream.close(); }
		catch (Exception e) { return RunTimeError(e); }

		if (aBitmap == null) return RunTimeError("Bitmap load failed at:");
		   
		   NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
		   
		   BitmapList.add(aBitmap); // Add the new bit map to the bitmap list
		   
		   return true;
	  }
	  
	  private boolean execute_gr_bitmap_delete(){
		   if (!evalNumericExpression()) return false;					// 
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

		  if (!getVar())return false;							// Graphic Destination Bitmap Pointer Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

			if (!evalNumericExpression())return false;			// Get Source Bitmap
		   double d = EvalNumericExpressionValue;
		   int q = (int) d;
		   if (q<1 | q >= BitmapList.size()){
			   RunTimeError("Invalid Bitmap Pointer");
			   return false;			   
		   }
		   
		   Bitmap SrcBitMap = BitmapList.get(q);
		   if (SrcBitMap == null){
			   RunTimeError("Bitmap was deleted");
			   return false;
		   }
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   
			if (!evalNumericExpression())return false;							// Get Width 
			d = EvalNumericExpressionValue;
			int Width = (int) d;
			
			if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			++LineIndex;
			   
			if (!evalNumericExpression())return false;							// Get Height
			d = EvalNumericExpressionValue;
			int Height = (int) d;
			
			boolean parm = true;
			if (ExecutingLineBuffer.charAt(LineIndex) == ','){					// optional scale paramter
				++LineIndex;
				if (!evalNumericExpression())return false;
				if (EvalNumericExpressionValue == 0.0) parm = false;
			}
			if (!checkEOL()) return false;

			if (Width == 0 || Height == 0){
				RunTimeError("Width and Height must not be zero");
				   return false;
			}
			
			try {aBitmap = Bitmap.createScaledBitmap(SrcBitMap, Width, Height, parm);}
			   catch (Exception e){
				   return RunTimeError(e);
			   }
			   
			System.gc();   
			NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
			System.gc();
			BitmapList.add(aBitmap);
		   
		  return true;
	  }
	  
	  private boolean execute_gr_bitmap_size(){
		  
		   if (!getVar())return false;							// Graphic Source Bitmap Pointer Variable
		   if (!VarIsNumeric)return false;						 
		   double d = NumericVarValues.get(theValueIndex);
		   int q = (int) d;
		   if (q<1 | q >= BitmapList.size()){
			   RunTimeError("Invalid Bitmap Pointer");
			   return false;			   
		   }
		   Bitmap SrcBitMap = BitmapList.get(q);                // Access the bitmap
		   
		   if (SrcBitMap == null){
			   RunTimeError("Bitmap was deleted");
			   return false;
		   }


		   int w = SrcBitMap.getWidth();                 // Get the image width
		   int h = SrcBitMap.getHeight();                // Get the image height
		   
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!getVar())return false;							// Get the height variable
		   if (!VarIsNumeric)return false;						
		   NumericVarValues.set(theValueIndex, (double) w);     // Set the height value
		   
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!getVar())return false;							// Get the width variable
		   if (!VarIsNumeric)return false;						
		   NumericVarValues.set(theValueIndex, (double) h);     // Set the width value
			if (!checkEOL()) return false;
		   
		  return true;
	  }
	  
	  private boolean execute_gr_bitmap_crop(){
		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get source bitmap index
		   int SourceBitmapIndex = EvalNumericExpressionValue.intValue();
		   if (SourceBitmapIndex < 0 || SourceBitmapIndex >= BitmapList.size()){
			   RunTimeError("Invalid Source Bitmap Pointer");
			   return false;
		   }
		   Bitmap SourceBitmap = BitmapList.get(SourceBitmapIndex);
		   
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get x
		  int x = EvalNumericExpressionValue.intValue();
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get y
		  int y = EvalNumericExpressionValue.intValue();
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get width
		  int width = EvalNumericExpressionValue.intValue();
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get height
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
		  
		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		  

		   if (!evalNumericExpression())return false;							// Get Bitmap obj pointer
		   double d = EvalNumericExpressionValue;
		   int q = (int) d;

		   if (q<1 | q >= BitmapList.size()){
			   RunTimeError("Invalid Bitmap Pointer");
			   return false;			   
		   }
		   
		   if (BitmapList.get(q) == null){
			   RunTimeError("Bitmap was deleted");
			   return false;
		   }


		  aBundle.putInt("bitmap", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get x
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("x", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get y
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("y", (int) d);
		  
			if (!checkEOL()) return false;
		  int p = PaintList.size();						// Set current paint as this circle's paint
		  aBundle.putInt("paint", p-1);
		  
		  aBundle.putInt("alpha", 255);

		  NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		  DisplayListAdd(aBundle);

		  return true;
	  }
	  
	  private boolean execute_gr_bitmap_create(){
		  if (!getNVar()) return false;											// Get bitmap ptr var
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression()) return false;							// Get the width
		   int width = EvalNumericExpressionValue.intValue();
		   if (width <= 0 ){
			   RunTimeError("Width must be >= 0");
			   return false;
		   }
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression()) return false;							// Get the height
		   int height = EvalNumericExpressionValue.intValue();
		   if (height <= 0 ){
			   RunTimeError("Height must be >= 0");
			   return false;
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


		  if (!evalNumericExpression())return false;							// Get angle
			  double d = EvalNumericExpressionValue;
			  aBundle.putInt("angle", (int) d);
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get x
			   d = EvalNumericExpressionValue;
			  aBundle.putInt("x", (int) d);
			  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			  ++LineIndex;
			  
			  if (!evalNumericExpression())return false;							// Get y
			   d = EvalNumericExpressionValue;
			  aBundle.putInt("y", (int) d);

			  if (ExecutingLineBuffer.charAt(LineIndex) == ','){
				  ++LineIndex;
				  if (!getVar()) return false;
				  if (!VarIsNumeric) return false;
				  NumericVarValues.set(theValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
			  }
				if (!checkEOL()) return false;

			  DisplayListAdd(aBundle);          // Put the new object into the display list

		  return true;
	  }
	  
	  private boolean execute_gr_rotate_end(){
		  Bundle aBundle = new Bundle();           // Create a new object of type Rotate end
		  aBundle.putInt("type", GR.dRotate_End); 
		  aBundle.putInt("hide", 0);
		  
		  if (getVar()){
			  if (!VarIsNumeric) return false;
			  NumericVarValues.set(theValueIndex, (double) DisplayList.size()); // Save the GR Object index into the var
		  }
			if (!checkEOL()) return false;
		  
		  DisplayListAdd(aBundle);					// add the object to the display list
		  return true;
	  }
	  
	  private boolean execute_gr_modify(){
		  
		  if (!evalNumericExpression())return false;							// Get Object Number
		  double d = EvalNumericExpressionValue;
		  int index = (int) d;
		  if (index <0 || index >= DisplayList.size()){
			  RunTimeError("Object Number out of range");
			  return false;
		  }
		  Bundle b = DisplayList.get(index);									// Get the bundle to change
		  
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalStringExpression()) return false;							// get the parameter string
		  if (SEisLE) return false;
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  String parm = StringConstant;
		  int value = 0;
		  
		  if (!b.containsKey(parm)){
			  RunTimeError("Object does not contain: " + parm);
			  return false;
		  }
		  
		  
		  if (StringConstant.equals("text")){
			  parm = StringConstant;
			  if (!evalStringExpression()) return false;							// get the parameter string
			  if (SEisLE) return false;
			  b.putString(parm, StringConstant);
		  }else{
			  if (!evalNumericExpression())return false;							// Get paramter value
			  d = EvalNumericExpressionValue;
			  value = (int) d;
			  if (parm.equals("bitmap")){
				  if (value <0 | value >= BitmapList.size()){
					  RunTimeError("Bitmap pointer out of range");
					  return false;
				  }
			  }
			  if (parm.equals("paint")){
				  if (value < 1 || value >= PaintList.size()){
					  RunTimeError ("Invalid Paint object number");
					  return false;
				  }
			  }

			  b.putInt(parm, value);
		  }
			if (!checkEOL()) return false;

		  DisplayList.set(index, b);

		  return true;
		  
	  }
	  
	  private boolean execute_gr_orientation(){

		  if (!evalNumericExpression())return false; // get the mode (landscape or portrait)							
			if (!checkEOL()) return false;
		  int mode = EvalNumericExpressionValue.intValue();
		  GR.drawView.SetOrientation(mode);
		  return true;
	  }
	  
	  private boolean execute_gr_screen(){
		   if (!getNVar()) return false;						// Width variable 
		   NumericVarValues.set(theValueIndex,(double) GR.Width); 
		   if (!isNext(',')) return false;

		   if (!getNVar()) return false;						// Heigth Variable
		   NumericVarValues.set(theValueIndex, (double) GR.Heigth); 
		   if (isNext(',')) {
			   if (!getNVar()) return false;					// Optional Density variable
			   DisplayMetrics dm = new DisplayMetrics();
			   getWindowManager().getDefaultDisplay().getMetrics(dm);
			   NumericVarValues.set(theValueIndex, (double) dm.densityDpi);
		   }
		   if (!checkEOL()) return false;

		  return true;
	  }
	  
	  private boolean execute_gr_front(){
		  if (!evalNumericExpression())return false;							// Get y
		  if (EvalNumericExpressionValue == 0){
			  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(Basic.theProgramRunner);
			  GRFront = false;
		  }else{
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
		if (!getArrayVarForRead())  return false;				// Get the array variable
		if (!VarIsNumeric) { return RunTimeError("Array not numeric"); }
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));// Get the array table bundle for this array

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

		int length = ArrayEntry.getInt("length");				// get the array length
		if ((length % 2) != 0){
			return RunTimeError("Not an even number of elements in pixel array");
		}
		int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space
	
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

			int pixel = 0;

			if (!evalNumericExpression())return false;							// Get x
			double d = EvalNumericExpressionValue;
			int x = (int)d;
			if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			++LineIndex;
				  
			if (!evalNumericExpression())return false;							// Get y
			d = EvalNumericExpressionValue;
			int y = (int)d;
			if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			++LineIndex;

			int w = b.getWidth();                 // Get the image width
			   int h = b.getHeight();                // Get the image height
			   
			   if (x<0 || x>=w || y<0 || y>=h){
				   RunTimeError("x or y exceeds size of screen");
				   return false;
			   }
			
			
			if (b != null) pixel = b.getPixel(x, y);					// get the pixel from the bitmap

			
	  		int alpha = Color.alpha(pixel);								// get the components of the pixel
	  		int red = Color.red(pixel);
	  		int green = Color.green(pixel);
	  		int blue = Color.blue(pixel);
	  		
	  		if (!getNVar())return false;									// Alpha Var
	  		NumericVarValues.set(theValueIndex, (double) alpha);
	  		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  		++LineIndex;

	  		if (!getNVar())return false;									// Red Var
	  		NumericVarValues.set(theValueIndex, (double) red);
	  		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  		++LineIndex;

	  		if (!getNVar())return false;									// Blue Green
	  		NumericVarValues.set(theValueIndex, (double) green);
	  		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  		++LineIndex;

	  		if (!getNVar())return false;									// Green Blue
	  		NumericVarValues.set(theValueIndex, (double) blue);
			if (!checkEOL()) return false;
		  
		  return true;
	  }

	private boolean writeBitmapToFile(Bitmap b, String fn, int quality) {
		CompressFormat format = CompressFormat.PNG;						// Assume png
		String tFN = fn.toUpperCase();									// temp convert fn to upper case
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
		  
		  	if (!evalStringExpression()) return false;					// Get the filename
		  	String fn = StringConstant;
		  	
		  	int quality = 50;											// set default jpeg quality
			if (isNext(','))											// if there is an optional quality parm
			{
				if (!evalNumericExpression())return false;				// evaluate it
				quality = EvalNumericExpressionValue.intValue();
				if (quality < 0 || quality >100){
					RunTimeError("Quality must be between 0 and 100");
					return false;
					}
			}
			
			if (!checkEOL()) return false;
			
            boolean retval = true;
			Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
			if (b==null){
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
          boolean retval = true;
		  Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
		  if (b == null) {
			  RunTimeError("Could not capture screen bitmap. Sorry.");
			  retval = false;
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

	  private boolean execute_gr_get_texbounds(){
		  if (!evalStringExpression()) return false;

		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  if (!getNVar()) return false;
		  int leftIndex = theValueIndex;

		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  if (!getNVar()) return false;
		  int topIndex = theValueIndex;
		  
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  if (!getNVar()) return false;
		  int rightIndex = theValueIndex;
		  
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  if (!getNVar()) return false;
		  int bottomIndex = theValueIndex;

		  Rect bounds = new Rect();
		  
		  aPaint.getTextBounds(StringConstant, 0, StringConstant.length(), bounds);
		  
		  int left = bounds.left;
		  int top = bounds.top;
		  int right = bounds.right;
		  int bottom = bounds.bottom;
		  
		  NumericVarValues.set(topIndex, (double) top);
		  NumericVarValues.set(leftIndex, (double) left);
		  NumericVarValues.set(bottomIndex, (double) bottom);
		  NumericVarValues.set(rightIndex, (double) right);
		  
		  return true;
	  
	  
	  }
	  
	  private boolean execute_bitmap_save(){
		  
			if (!evalNumericExpression())return false;			// Get Source Bitmap
			   double d = EvalNumericExpressionValue;
			   int q = (int) d;
			   if (q<1 | q >= BitmapList.size()){
				   RunTimeError("Invalid Bitmap Pointer");
				   return false;			   
			   }
			   
			   Bitmap SrcBitMap = BitmapList.get(q);
			   if (SrcBitMap == null){
				   RunTimeError("Bitmap was deleted");
				   return false;
			   }
			   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			   ++LineIndex;

			if (!evalStringExpression()) return false;					// Get the filename
		  	String fn = StringConstant;
		  	
		  	int quality = 50;											// set default jpeg quality
			if (ExecutingLineBuffer.charAt(LineIndex) == ',')			// if there is an optional quality parm
			{
				++LineIndex;
				if (!evalNumericExpression())return false;				// evaluate it
				quality = EvalNumericExpressionValue.intValue();
				if (quality < 0 || quality >100){
					RunTimeError("Quality must be between 0 and 100");
					return false;
					}
			}
			
			if (!checkEOL()) return false;
			
			boolean retval = writeBitmapToFile(SrcBitMap, fn, quality);

		    SrcBitMap = null;
		    System.gc();
			return retval;
	  }
	  
	  
	  private boolean execute_gr_scale(){

		   if (!evalNumericExpression())return false;							// Get x
		   double x = EvalNumericExpressionValue;

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
			  
			if (!evalNumericExpression())return false;							// Get y
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

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression())return false;							// Get left
		  double d = EvalNumericExpressionValue;
		  aBundle.putInt("left", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get top
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("top", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get right
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("right", (int) d);
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get bottom
		   d = EvalNumericExpressionValue;
		  aBundle.putInt("bottom", (int) d);
		  
		  int RegionOp = 0 ;
		  if (ExecutingLineBuffer.charAt(LineIndex) == ','){
			  ++LineIndex;
			  if (!evalNumericExpression())return false;
			  d = EvalNumericExpressionValue;
			  if (d<0 || d>5){
				  RunTimeError("Region Operator not 0 to 5");
				  return false;
			  }
			  aBundle.putInt("RO", (int) d);
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

		  if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   
		   if (!evalNumericExpression())return false;
		   int theListIndex = EvalNumericExpressionValue.intValue();
		   if (theListIndex <1 || theListIndex>= theLists.size()){
			   RunTimeError("Invalid list pointer");
			   return false;
		   }
		   if (theListsType.get(theListIndex) != list_is_numeric){
			   RunTimeError("List must be numeric");
			   return false;
		   }
		   
		   ArrayList<Double> thisList = theLists.get(theListIndex);
		   if (thisList.size()<6){
			   RunTimeError("List must have at least three points");
			   return false;
		   }
		   
		   int r = thisList.size() % 2;
		   if (r !=0 ) {
			   RunTimeError("List must have even number of elements");
			   return false;
		   }
		   aBundle.putInt("list", theListIndex);
		   
			int x = 0;
			int y = 0;
	  		if (ExecutingLineBuffer.charAt(LineIndex) == ','){
	  			++LineIndex;

	  			if (!evalNumericExpression()) return false;
	  			x = EvalNumericExpressionValue.intValue();
	  			if (ExecutingLineBuffer.charAt(LineIndex) != ',') return false;
	  			++LineIndex;
	  			if (!evalNumericExpression()) return false;
	  			y =  EvalNumericExpressionValue.intValue();
	  		}
	  		
	  		aBundle.putInt("x", x);
	  		aBundle.putInt("y", y);


		   int p = PaintList.size();						// Set current paint as this circle's paint
		   aBundle.putInt("paint", p-1);
		   NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

		   DisplayListAdd(aBundle);


		  return true;
	  }
	  
	  private boolean execute_gr_camera_select(){
		  
		  int BACK = 1;
		  int FRONT = 2;
		  int cameraChoice = BACK;
		  
		  int level = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
		  int cameraCount = 0;
		  
		  if (level >= 9) {												// if SDK >= then May be more than one camera
			  cameraCount = Camera.getNumberOfCameras();						// so get the count
		   } else {														// otherwise there can only be one camera
			   Camera tCamera = Camera.open();							// Check to see if there is any camera at all
			   if (tCamera == null) {
				   RunTimeError("This device does not have a camera.");
				   return false;										// No Camera
			   }
			   CameraNumber = -1;										// There is a camera
			   tCamera.release();
			   return true;
		   }
		  
		   Camera tCamera = Camera.open(0);									// Determine which camera number is BACK
		   Camera.CameraInfo CI = new Camera.CameraInfo();				    // Assume 0 is BACK
		   tCamera.getCameraInfo(0, CI);
		   boolean zero_is_back = true;
		   if (CI.facing == CameraInfo.CAMERA_FACING_FRONT) zero_is_back = false;
		   tCamera.release();

		  
		   if (!evalNumericExpression())return false;						// Get user's choice
		   int choice = EvalNumericExpressionValue.intValue();
		   if (choice != BACK && choice != FRONT) {
			   RunTimeError("Select value must be 1 (Back) or 2 (Front).");
			   return false;
		   }
		   
		   if (cameraCount == 1) {
			   if (choice == BACK && zero_is_back) {
				   CameraNumber = 0;
				   return true;
			   }
			   if (choice == BACK && !zero_is_back) {
				   RunTimeError("Device has no back camera");
				   return false;
			   }
			   
			   if (choice == FRONT && !zero_is_back) {
				   CameraNumber = 0;
				   return true;
			   }

			   if (choice == FRONT && zero_is_back) {
				   RunTimeError("Device has no front camera");
				   return false;
			   }
		   }
		   
		   if (choice == BACK && zero_is_back) CameraNumber = 0;
		   if (choice == BACK && !zero_is_back) CameraNumber = 1;
		   if (choice == FRONT && zero_is_back) CameraNumber = 1;
		   if (choice == FRONT && !zero_is_back) CameraNumber = 0;

		  return true;
	  }
	  
		private boolean execute_camera_shoot(){
			
			  int level = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
			  int cameraCount = 0;
			  if (CameraNumber == -1) {
				  if (level >= 9) {
					  int count = Camera.getNumberOfCameras();
					  if (count == 0) {
					    	RunTimeError("This device does not have a camera.");
					    	return false;
					  }
					  CameraNumber = 0;
				  }
			  }
			  
			Camera tCamera = null;
			try{
				if (CameraNumber == -1) tCamera = Camera.open();
				else tCamera = Camera.open(CameraNumber);
			}
			catch (Exception e){
				return RunTimeError(e);
			}
			
		    if (tCamera == null){
		    	RunTimeError("This device does not have a camera.");
		    	return false;
		    }
		    tCamera.release();

			if (!getNVar()) return false;
			int saveValueIndex = theValueIndex;
			   
			   CameraFlashMode = 0 ;
			   if (ExecutingLineBuffer.charAt(LineIndex) == ','){
				   ++LineIndex;
				   if (!evalNumericExpression()) return false;
				   CameraFlashMode = EvalNumericExpressionValue.intValue();
			   }

			CameraBitmap = null;
			CameraDone = false;
			
	    	Intent cameraIntent = new Intent(this, CameraView.class);				// Start the Camera
		    try{
		    	startActivityForResult(cameraIntent, BASIC_GENERAL_INTENT);
		    } catch (Exception e){
		    	return RunTimeError(e);
		    }

			while (!CameraDone) Thread.yield();
//			CameraNumber = -1;
			
			if (CameraBitmap != null){
			   NumericVarValues.set(saveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
			   BitmapList.add(CameraBitmap);
			}else{
			   NumericVarValues.set(saveValueIndex, (double) 0); // Save the GR Object index into the var
			}
			CameraBitmap = null;
		    System.gc();

			return true;
		}
		
	  
	  private boolean execute_statusbar_show(){
		  Show("This command deprecated.");
		  Show("To show status bar, use:");
		  Show("gr.open alpha, red, green, blue, 1");
		  SyntaxError = true;
		  return false;
	  }
	  
	  private boolean execute_brightness(){
		  if (!evalNumericExpression()) return false;
		  double value = EvalNumericExpressionValue;
		  if (value < 0.01) value = 0.01;
		  if (value > 1.0) value = 1.0;
		  GR.Brightness = (float) value;
		  return true;
	  }
	  
	  private boolean execute_gr_text_typeface(){
		   if (!evalNumericExpression())return false;							// Get type
		   int face = EvalNumericExpressionValue.intValue();

			if (!checkEOL()) { return false; }
			
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
				RunTimeError("Typface must be 1, 2. 3 or 4");
				return false;
			}
			
			Paint tPaint = newPaint(aPaint);						// Put the typeface into Paint
			tPaint.setTypeface(tf);
			aPaint =  newPaint(tPaint);							// Copy the temp paint to aPaint
		    PaintListAdd(aPaint);								// Add the newPaint to the Paint List
			
		  return true;
	  }
	  
	  private boolean execute_gr_touch_resume(){
		  if (interruptResume == -1) {
			  RunTimeError("No onTouch Interrupt");
			  return false;
		  }
		  return doResume();
	  }
	  	  
//*************************************************************  Audio  ******************************************
	  private boolean executeAUDIO(){
	    	if (!GetAudioKeyWord()){
  	    	  return false;
  	      	}else {
  	    	  switch (KeyWordValue){
  	    	  	case audio_load:
  	    	  		if (!execute_audio_load()){return false;}
  	    	  		break;
  	    	  	case audio_play:
  	    	  		if (!execute_audio_play()){return false;}
  	    	  		break;
  	    	  	case audio_loop:
  	    	  		if (!execute_audio_loop()){return false;}
  	    	  		break;
  	    	  	case audio_stop:
  	    	  		if (!execute_audio_stop()){return false;}
  	    	  		break;
  	    	  	case audio_volume:
  	    	  		if (!execute_audio_volume()){return false;}
  	    	  		break;
  	    	  	case audio_pcurrent:
  	    	  		if (!execute_audio_pcurrent()){return false;}
  	    	  		break;
  	    	  	case audio_pseek:
  	    	  		if (!execute_audio_pseek()){return false;}
  	    	  		break;
  	    	  	case audio_length:
  	    	  		if (!execute_audio_length()){return false;}
  	    	  		break;
  	    	  	case audio_release:
  	    	  		if (!execute_audio_release()){return false;}
  	    	  		break;
  	    	  	case audio_pause:
  	    	  		if (!execute_audio_pause()){return false;}
  	    	  		break;
  	    	  	case audio_isdone:
  	    	  		if (!execute_audio_isdone()){return false;}
  	    	  		break;
  	    	  	case audio_record_start:
  	    	  		if (!execute_audio_record_start()){return false;}
  	    	  		break;
  	    	  	case audio_record_stop:
  	    	  		if (!execute_audio_record_stop()){return false;}
  	    	  		break;
  	    	  	default:
  	    	  		return false;
  	    	  }
  	      	}
		  return true;
	  }

	  private  boolean GetAudioKeyWord(){						// Get a Basic key word if it is there
			// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<Audio_KW.length; ++i){		           // loop through the key word list
			  if (Temp.startsWith(Audio_KW[i])){               // if there is a match
				  KeyWordValue = i;						       // set the key word number
				  LineIndex = LineIndex + Audio_KW[i].length(); // move the line index to end of key word
				  return true;							      // and report back
			  }
		  }
		  KeyWordValue = audio_none;						  // no key word found
		  return false;									      // report fail

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
//		  AudioManager audioSM = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		MediaPlayer aMP = null;
		  
		if (!getNVar()) return false;									// Get the Player Number Var
		int saveValueIndex = theValueIndex;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;								// Get the file path
		if (!checkEOL()) return false;

		String fileName = StringConstant;								// The filename as given by the user
		File file = new File(Basic.getDataPath(fileName));

		Uri theURI = null;
		int resID = 0;
		if (file.exists()) {
			try {
				theURI = Uri.fromFile(file);							// Create Uri for the file
				if (theURI == null) {
					RunTimeError(StringConstant + "Not Found at:");
					return false;
				}
				aMP = MediaPlayer.create(BasicContext, theURI);			// Create a new Media Player
			} catch (Exception e) {
				return RunTimeError(e);
			}
		} else {														// file does not exist
			if (Basic.isAPK) {											// if not standard BASIC! then is user APK
				resID = getRawResourceID(fileName);						// try to load the file from a raw resource
				if (resID == 0) { return false; }
				aMP = MediaPlayer.create(BasicContext, resID);
			}															// else standard BASIC!, aMP is still null
		}

		   if (aMP == null){
			   RunTimeError(StringConstant + "Not Found at:");
			   return false;
		   }
		   
		   aMP.setAudioStreamType(AudioManager.STREAM_MUSIC);
		   setVolumeControlStream(AudioManager.STREAM_MUSIC);
		   
		   NumericVarValues.set(saveValueIndex, (double) theMPList.size());
		   theMPList.add(aMP);
		   theMpUriList.add(theURI);
		   theMpResIDList.add(resID);

		return true;
	}
	  
	  private boolean execute_audio_release(){

		  if (!evalNumericExpression()) return false;
		  double d = EvalNumericExpressionValue;
		  int index = (int) d;
		  if (index <=0 || index >= theMPList.size()){
			  RunTimeError("Invalid Player List Value");
			  return false;
		  }
		  
		  if (!checkEOL()) return false;

		  MediaPlayer aMP = theMPList.get(index);
		  if (aMP == null) return true;
		  
		  if (theMP == aMP) {
			  RunTimeError("Must stop player before releasing");
			  return false;
		  }
		  
		  aMP.release();
		  theMPList.set(index, null);

		  return true;
	  }
	  
	  private boolean execute_audio_play(){
		  
		  if (!evalNumericExpression()) return false;
		  double d = EvalNumericExpressionValue;
		  int index = (int) d;
		  if (index <=0 || index >= theMPList.size()){
			  RunTimeError("Invalid Player List Value");
			  return false;
		  }
			
		  if (!checkEOL()) return false;
		  
		  MediaPlayer aMP = theMPList.get(index);
		  if (aMP == null){
			  RunTimeError("Audio not loaded at:");
			   return false;			  
		  }
		  
		  if (theMP != null){
			  RunTimeError("Stop Current Audio Before Starting New Audio");
			   return false;			  
		  }
		  
		  setVolumeControlStream(AudioManager.STREAM_MUSIC);
		  
		  theMP = aMP;
//		  Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " play " +theMP );
		  try {theMP.prepare();}
		  catch (Exception e) {
//				return RunTimeError(e);
		  }
		  theMP.start();
		  
		  if (!theMP.isPlaying()){
			  
			  Uri thisUri = theMpUriList.get(index);
			  if (thisUri == null) {
				  int id = theMpResIDList.get(index);
				  aMP = MediaPlayer.create(Basic.BasicContext, id);
			  }
			  else {
				  aMP = MediaPlayer.create(Basic.BasicContext, thisUri);
			  }
			  if (aMP == null){
				  RunTimeError("Media player synchronous problem.");
				  return true;
			  }
			  theMPList.set(index, aMP);
			  theMP.release();
			  theMP = aMP;
			  theMP.start();
		  }
		  
		  PlayIsDone = false;
		  
		  theMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			
			public void onCompletion(MediaPlayer mp) {
				PlayIsDone = true;
				}
		  });

//		  Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " is playing " + theMP.isPlaying());
		  
		  return true;
	  }
	  
	  private boolean execute_audio_isdone(){
		  if (theMP == null){
			  PlayIsDone = true;		  }
		  
		  if (!getNVar())return false;	
		  if (!checkEOL()) return false;
		  double d = 0;
		  if (PlayIsDone) d =1;
		  NumericVarValues.set(theValueIndex, d);
		  return true;
	  }
	  
	  private boolean execute_audio_loop(){
			if (!checkEOL()) return false;
		  if (theMP == null){
			   Show("Audio not playing at:");
			   Show (ExecutingLineBuffer);
			   SyntaxError= true;
			   return false;			  
		  }
		  
		  theMP.setLooping(true);
		  return true;
	  }
	  

	  
	  private boolean execute_audio_stop(){
			if (!checkEOL()) return false;

			if (theMP == null) return true;                               // if theMP is null, Media player has stopped
//		  MediaPlayer.setOnSeekCompleteListener (mSeekListener);
//	  	 try {Thread.sleep(1000);}catch(InterruptedException e){}
	  	  try {Run.theMP.stop();}catch (Exception e) {
				return RunTimeError(e);
	  	  }

	  	  theMP = null;                                                 // Signal MP stopped
		  return true;
	  }
	  
	  private boolean execute_audio_pause(){
			
		  if (!checkEOL()) return false;
		  if (theMP == null) return true;                               // if theMP is null, Media player has stopped
	  	  try {Run.theMP.pause();} catch (Exception e) {
				return RunTimeError(e);
	  	  }
	  	  theMP = null;                                                 // Signal MP stopped
		  return true;
	  }
	  
	  private boolean execute_audio_volume(){

		  if (theMP == null){
			  RunTimeError("Audio not playing at:");
			   return false;			  
		  }
		  if (!evalNumericExpression()) return false;
		  double d = EvalNumericExpressionValue;
		  float  left = (float) d;

	  	  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  	  ++LineIndex;
		  if (!evalNumericExpression()) return false;
		  d = EvalNumericExpressionValue;
		  float right = (float) d;
		  setVolumeControlStream(AudioManager.STREAM_MUSIC);
		  theMP.setVolume(left, right);
		if (!checkEOL()) return false;

		  return true;
	  }
	  
	  private boolean execute_audio_pcurrent(){
		  if (theMP == null){
			  RunTimeError("Audio not playing");
			   return false;			  
		  }
		  
		  if (!getNVar())return false;											
		  NumericVarValues.set(theValueIndex, (double)theMP.getCurrentPosition());

			if (!checkEOL()) return false;
		  return true;
	  }
	  
	  private boolean execute_audio_pseek(){
		  if (theMP == null){
			  RunTimeError("Audio not playing at:");
			   return false;			  
		  }
		  
		  if (!evalNumericExpression()) return false;
		  double d = EvalNumericExpressionValue;
		  int dd = (int) d;
		  theMP.seekTo(dd); 
			if (!checkEOL()) return false;
		  return true;
	  }
	  
	  private boolean execute_audio_length(){
		  
		  if (!getNVar())return false;											// Get the Player Number Var
		  int saveValueIndex = theValueIndex;
	  	  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  	  ++LineIndex;

		  
		  if (!evalNumericExpression()) return false;
		  double d = EvalNumericExpressionValue;
		  int index = (int) d;
		  if (index <=0 || index >= theMPList.size()){
			  RunTimeError("Invalid Player List Value");
			  return false;
		  }
			if (!checkEOL()) return false;

		  
		  MediaPlayer aMP = theMPList.get(index);
		  if (aMP == null){
			  RunTimeError("Audio not loaded at:");
			   return false;			  
		  }
		  
		  double  length = (double)aMP.getDuration();
		  
		  NumericVarValues.set(saveValueIndex, length);
		  
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
	  
	  private boolean execute_audio_record_stop(){
		    if (mRecorder == null) return true;
		    try {
		    	mRecorder.stop();
		    	mRecorder.release();
		    }
		    catch (Exception e){}
	        mRecorder = null;

		  return true;
	  }

	  
// *********************************************  Sensors Package **********************************
	  
	  private boolean executeSENSORS(){
	    	if (!GetSensorsKeyWord()){
	    	  return false;
	      	}else {
	    	  switch (KeyWordValue){
	    	  	case sensors_list:
	    	  		if (!execute_sensors_list()){return false;}
	    	  		break;
	    	  	case sensors_open:
	    	  		if (!execute_sensors_open()){return false;}
	    	  		break;
	    	  	case sensors_read:
	    	  		if (!execute_sensors_read()){return false;}
	    	  		break;
	    	  	case sensors_close:
	    	  		if (!execute_sensors_close()){return false;}
	    	  		break;
	    	  	case sensors_rotate:
	    	  		if (!execute_sensors_rotate()){return false;}
	    	  		break;
	    	  	default:
	    	  		return false;
	    	  }
	      	}
		  return true;
	  }
	  
	  private  boolean GetSensorsKeyWord(){						// Get a Basic key word if it is there
			// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<Sensors_KW.length; ++i){		// loop through the key word list
			  if (Temp.startsWith(Sensors_KW[i])){    // if there is a match
				  KeyWordValue = i;						// set the key word number
				  LineIndex = LineIndex + Sensors_KW[i].length(); // move the line index to end of key word
				  return true;							// and report back
			  }
		  }
		  KeyWordValue = sensors_none;							// no key word found
		  return false;									// report fail

	  		}

		private boolean execute_sensors_list(){
			if (!getArrayVarForWrite()) { return false; }				// Get the array variable
			if (VarIsNumeric) { return RunTimeError("Not string array"); }
			if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
			if (!checkEOL()) { return false; }							// line must end with ']'
			int theVarNumber = VarNumber;

			if (theSensors == null) {
				theSensors = new SensorActivity(BasicContext);
			}
			ArrayList<String> census = theSensors.takeCensus();
			int nSensors = census.size();						// If no sensors reported.....
			if (nSensors==0){
				return RunTimeError("This device reports no Sensors");
			}

			/* Puts the list of sensors into an unDIMed array */
			return ListToBasicStringArray(theVarNumber, census, nSensors);
		}

		private boolean execute_sensors_open(){
			if (theSensors == null) {
				theSensors = new SensorActivity(BasicContext);
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

			if (!evalNumericExpression())return false;			// Get Sensor Type
			int type = EvalNumericExpressionValue.intValue();
			if (!isNext(',')) return false;

			if (type < 0 || type > SensorActivity.MaxSensors) {
				return RunTimeError("Sensor type not 0 to " + SensorActivity.MaxSensors);
			}

			int[] valueIndex = new int[4];
			if (!getNVar())return false;						// Sensor Variable
			valueIndex[1] = theValueIndex;
			if (!isNext(',')) return false;

			if (!getNVar())return false;						// Sensor Variable
			valueIndex[2] = theValueIndex;
			if (!isNext(',')) return false;

			if (!getNVar())return false;						// Sensor Variable
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
/*		  SensorManager.remapCoordinateSystem(R,
				  SensorManager.AXIS_X,
				  SensorManager.AXIS_Z, r);*/
		  
	      SensorManager.getOrientation(r, mOrientation);
	      
          final float rad2deg = (float)(180.0f/Math.PI);
          
		   if (!getNVar())return false;							// Graphic Object Variable
		   NumericVarValues.set(theValueIndex, (double) mOrientation[0]);
		   if (!isNext(',')) return false;

		   if (!getNVar())return false;							// Graphic Object Variable
		   NumericVarValues.set(theValueIndex, (double) mOrientation[1]); 
		   if (!isNext(',')) return false;

		   if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
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

// ************************************************ GPS Package ***********************************
 	  
 	  private boolean executeGPS(){
	    	if (!GetGPSKeyWord()){
		    	  return false;
		      	}else {
		      	  if (theGPS == null && KeyWordValue != gps_open){
					theBackground.checkpointProgress();						// if singal @@8 is going to open GPS,
					while (ProgressPending) { Thread.yield(); }				// this will wait for it to complete
					if (theGPS == null) {
						return RunTimeError("GPS not opened at:");
		    		}
		      	  }
		    	  switch (KeyWordValue){
		    	  	case gps_open:
		    	  		if (!execute_gps_open()){return false;}
		    	  		break;
		    	  	case gps_close:
		    	  		if (!execute_gps_close()){return false;}
		    	  		break;
		    	  	case gps_altitude:
		    	  		if (!execute_gps_altitude()){return false;}
		    	  		break;
		    	  	case gps_longitude:
		    	  		if (!execute_gps_longitude()){return false;}
		    	  		break;
		    	  	case gps_bearing:
		    	  		if (!execute_gps_bearing()){return false;}
		    	  		break;
		    	  	case gps_accuracy:
		    	  		if (!execute_gps_accuracy()){return false;}
		    	  		break;
		    	  	case gps_speed:
		    	  		if (!execute_gps_speed()){return false;}
		    	  		break;
		    	  	case gps_latitude:
		    	  		if (!execute_gps_latitude()){return false;}
		    	  		break;
		    	  	case gps_provider:
		    	  		if (!execute_gps_provider()){return false;}
		    	  		break;
		    	  	case gps_time:
		    	  		if (!execute_gps_time()){return false;}
		    	  		break;
		    	  	default:
		    	  		return false;
		    	  }
		      	}
			  return true;
 	  }
 	  
	  private  boolean GetGPSKeyWord(){						// Get a Basic key word if it is there
			// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<GPS_KW.length; ++i){		// loop through the key word list
			  if (Temp.startsWith(GPS_KW[i])){    // if there is a match
				  KeyWordValue = i;						// set the key word number
				  LineIndex = LineIndex + GPS_KW[i].length(); // move the line index to end of key word
				  return true;							// and report back
			  }
		  }
		  KeyWordValue = sensors_none;							// no key word found
		  return false;									// report fail

	  		}
	  
	public boolean execute_gps_open() {
		if (!checkEOL()) return false;
		if (theGPS != null) return true;				// If already opened.....

		Show("@@8");								// tell Activity to start GPS
		return true;
	}
	  
	public boolean execute_gps_close(){
		if (!checkEOL()) return false;
		if (theGPS != null) {
			Log.d(LOGTAG, "Stopping GPS on command");
			theGPS.stop();								// Close GPS
			theGPS = null;
		}
		return true;
	}
	  
	  private boolean execute_gps_altitude(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex, GPS.Altitude);   // Set value into variable
			if (!checkEOL()) return false;
		  return true;
	  }
	  
	  private boolean execute_gps_latitude(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex, GPS.Latitude); 
			if (!checkEOL()) return false;
		  return true;
	  }

	  private boolean execute_gps_longitude(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex, GPS.Longitude); 
			if (!checkEOL()) return false;
		  return true;
	  }

	  private boolean execute_gps_bearing(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex, (double) GPS.Bearing); 
			if (!checkEOL()) return false;
		  return true;
	  }

	  private boolean execute_gps_accuracy(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex, (double) GPS.Accuracy); 
			if (!checkEOL()) return false;
		  return true;
	  }

	  private boolean execute_gps_speed(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex,(double) GPS.Speed); 
			if (!checkEOL()) return false;
		  return true;
	  }

	  private boolean execute_gps_time(){
		   if (!getVar())return false;							// Sensor Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex,(double) GPS.Time); 
			if (!checkEOL()) return false;
		  return true;
	  }


	  private boolean execute_gps_provider(){
		  if (!getVar() || VarIsNumeric) { return false; }
		  String provider = GPS.Provider;
		  if (provider == null) { provider = ""; }
		  StringVarValues.set(theValueIndex, provider);
		  return checkEOL();
	  }
	  
// ********************************************  Array Package  ***************************************
	  
	  private boolean executeARRAY(){
		  DoShuffle = false;
		  DoReverse = false;
		  DoAverage = false;
		  DoVariance = false;
		  DoStdDev = false;
		  DoSort = false;
		  DoSum = false;
		  DoMin = false;
		  DoMax = false;
	    	if (!GetArrayKeyWord()){ return false;}
		    	  switch (KeyWordValue){
		    	  	case array_length:
		    	  		if (!execute_array_length()){return false;}
		    	  		break;
		    	  	case array_load:
		    	  		if (!execute_array_load()){return false;}
		    	  		break;
		    	  	case array_delete:
		    	  		if (!executeUNDIM()){return false;}
		    	  		break;
		    	  	case array_sort:
		    	  		DoSort = true;
		    	  		if (!execute_array_collection()){return false;}
		    	  		break;
		    	  	case array_shuffle:
		    	  		DoShuffle = true;
		    	  		if (!execute_array_collection()){return false;}
		    	  		break;
		    	  	case array_reverse:
		    	  		DoReverse = true;
		    	  		if (!execute_array_collection()){return false;}
		    	  		break;
		    	  	case array_sum:
		    	  		DoSum = true;
		    	  		if (!execute_array_sum()){return false;}
		    	  		break;
		    	  	case array_average:
		    	  		DoAverage = true;
		    	  		if (!execute_array_sum()){return false;}
		    	  		break;
		    	  	case array_min:
		    	  		DoMin = true;
		    	  		if (!execute_array_sum()){return false;}
		    	  		break;
		    	  	case array_max:
		    	  		DoMax = true;
		    	  		if (!execute_array_sum()){return false;}
		    	  		break;
		    	  	case array_variance:
		    	  		DoVariance = true;
		    	  		if (!execute_array_sum())return false;
		    	  		break;
		    	  	case array_std_dev:
		    	  		DoVariance = true;
		    	  		DoStdDev = true;
		    	  		if (!execute_array_sum())return false;
		    	  		break;
		    	  	case array_copy:
		    	  		if (!execute_array_copy()){return false;}
		    	  		break;
		    	  	default:
		    	  		return false;
		    	  	}
		      	
		  return true;
	  }

	private  boolean GetArrayKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		int i = 0;
		for (i = 0; i<Array_KW.length; ++i){				// loop through the key word list
			if (Temp.startsWith(Array_KW[i])){    			// if there is a match
				KeyWordValue = i;							// set the key word number
				LineIndex = LineIndex + Array_KW[i].length(); // move the line index to end of key word
				return true;								// and report back
				}
		}
		KeyWordValue = array_none;						// no key word found
		return false;										// report fail

	}

	private boolean execute_array_length(){

		if (!getNVar()) { return false; }							// Length Variable
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getArrayVarForRead()) { return false; }				// Get the array variable
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (!checkEOL()) { return false; }							// line must end with ']'

		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));// Get the array table bundle for this array
		int length = ArrayEntry.getInt("length");					// Get the length from the bundle

		NumericVarValues.set(SaveValueIndex, (double) length);		// Set the length into the var value

		return true;
	}

	private boolean execute_array_load(){

		if (!getArrayVarForWrite()) { return false; }			// get the array var name as a new array
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		int theVarNumber = VarNumber;
		if (!isNext(',')) { return false; }						// Comma before the list

		if (VarIsNumeric) {										// If the array is numeric
			ArrayList<Double> Values = new ArrayList<Double>();	// Create a list for the numeric values
			if (!LoadNumericList(Values)) return false;		// load numeric list
			if (!checkEOL()) return false;
			return ListToBasicNumericArray(theVarNumber, Values, Values.size());// Copy the list to a BASIC! array
		} else {
			ArrayList<String> Values = new ArrayList<String>();	// Create a list for the numeric values
			if (!LoadStringList(Values)) return false;			// load string list
			if (!checkEOL()) return false;
			return ListToBasicStringArray(theVarNumber, Values, Values.size());	// Copy the list to a BASIC! array
		}
	}
	
	private boolean LoadNumericList(ArrayList <Double> Values){
		while (true) {											// loop through the list
			if (!evalNumericExpression()) return false;         // values may be expressions
			Values.add(EvalNumericExpressionValue);

			if (LineIndex >= ExecutingLineBuffer.length()) return false;
			char c = ExecutingLineBuffer.charAt(LineIndex);		// get the next character
			if (c == ',') {										// if it is a comma
				++LineIndex;									// skip it and continue looping
			} else if (c == '~') {            					// if it is a line continue character
				if (!nextLine()) return false;					// get next line if there is one
			} else break;										// else no more values in the list
		}
		return true;
	}

	private boolean LoadStringList(ArrayList <String> Values){
		while (true) {											// loop through the list
			if (!getStringArg()) return false;          		// values may be expressions
			Values.add(StringConstant);

			if (LineIndex >= ExecutingLineBuffer.length()) return false;
			char c = ExecutingLineBuffer.charAt(LineIndex);		// get the next character
			if (c == ',') {										// if it is a comma
				++LineIndex;									// skip it and continue looping
			} else if (c == '~') {            					// if it is a line continue character
				if (!nextLine()) return false;					// get next line if there is one
			} else break;										// else no more values in the list
		}
		return true;
	}

	private boolean execute_array_collection(){

		// This method implements several array commands

		if (!getArrayVarForRead()) { return false; }				// Get the array variable
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (!checkEOL()) { return false; }							// line must end with ']'

		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));// Get the array table bundle for this array   
			int length = ArrayEntry.getInt("length");               // get the array length
			int base = ArrayEntry.getInt("base");                   // and the start of values in the value space
			
			if (VarIsNumeric){											// Numeric Array
				ArrayList <Double> Values = new ArrayList<Double>();    // Create a list to copy array values into
				
				for (int i =0; i<length; ++i){                          // Copy the array values into that list
					Values.add(NumericVarValues.get(base+i));
				}
																		// Execute the command specific procedure
				if (DoReverse)Collections.reverse(Values);				// Reverse
				else if (DoSort) Collections.sort(Values);				// Sport
				else if (DoShuffle) Collections.shuffle(Values);		// Shuffle
				
				for (int i =0; i<length; ++i){							// Couple the results back to the array
					NumericVarValues.set(base+i, Values.get(i));
				}
				
			}else{														// Do the same stuff for a string array
				ArrayList <String> Values = new ArrayList<String>();
				for (int i =0; i<length; ++i){
					Values.add(StringVarValues.get(base+i));
				}
				
				if (DoReverse)Collections.reverse(Values);
				else if (DoSort) Collections.sort(Values);
				else if (DoShuffle) Collections.shuffle(Values);

				for (int i =0; i<length; ++i){
					StringVarValues.set(base+i, Values.get(i));
				}
				
			}

		return true;
	}
	
	
	
	
	private boolean execute_array_sum(){

		if (!getNVar()) { return false; }							// The value return variable
		int SaveValueIndex = theValueIndex;

		if (!isNext(',')) { return false; }
		if (!getArrayVarForRead()) { return false; }				// Get the array variable
		if (!VarIsNumeric) { return RunTimeError("Array not numeric"); }
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (!checkEOL()) { return false; }							// line must end with ']'

		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber)); // Get the array table bundle for this array
			int length = ArrayEntry.getInt("length");				// get the array length
			int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space
			
			double Sum = 0;
			double Min = NumericVarValues.get(base);
			double Max = NumericVarValues.get(base);
			
			for (int i = 0; i <length; ++i){						// Loop through the array values
				double d = NumericVarValues.get(base+i);			// Pick up the elements value
				Sum = Sum + d;										// build the Sum
				if (d < Min) Min = d;								// find the minimum value
				if (d > Max) Max = d;								// and the maxium value
			}
			
			double Average = Sum / length;							// Calculate the average
																			// Set the return value according to the command
			if (DoAverage) NumericVarValues.set(SaveValueIndex, Average);
			else if (DoSum) NumericVarValues.set(SaveValueIndex, Sum);
			else if (DoMax) NumericVarValues.set(SaveValueIndex, Max);
			else if (DoMin) NumericVarValues.set(SaveValueIndex, Min);
			else if (DoVariance){
				double T = 0;
				double W = 0;
				for (int i =0; i <length; ++i){
					double d = NumericVarValues.get(base+i);			// Pick up the elements value
					W = d - Average;
					T = T + W*W;
				}
				double variance = T/(length-1);
				double sd = Math.sqrt(variance);
				if (DoStdDev) NumericVarValues.set(SaveValueIndex, sd);
				else NumericVarValues.set(SaveValueIndex, variance);
			}

		return true;
	}
	
	private boolean execute_array_copy(){
															// **** Source Array ****
		
		doingDim = false;											// Get the array variable
		SkipArrayValues = true;										// Tells getVar not to look at the indices
		if (!getVar()) { SkipArrayValues = false; return false; }
		SkipArrayValues = false;
		doingDim = false;

		if (!VarIsArray) { return RunTimeError("Source not array"); }
		if (VarIsNew) { return RunTimeError("Source array not DIMed"); }
		boolean SourceArrayNumeric = VarIsNumeric;

		Bundle SourceArray = ArrayTable.get(VarIndex.get(VarNumber)); // Get the array table bundle for this array
		int SourceLength = SourceArray.getInt("length");			// get the array length
		int SourceBase = SourceArray.getInt("base");				// and the start of the array in the variable space
		int SourceCopyLimit = SourceBase + SourceLength;

		if (!isNext(']')) {
			if (!evalNumericExpression()) return false;				// get user's source start index
			int usi = EvalNumericExpressionValue.intValue();
			if (usi > 1) { SourceBase += usi - 1; }					// convert to 0-based index

			if (isNext(',')) {
				if (!evalNumericExpression()) return false;			// get user's length to copy
				SourceLength = EvalNumericExpressionValue.intValue();
				if (SourceLength < 0) { return RunTimeError("Length must be positive"); }
			}
			if (!isNext(']')) { return false; }

			if (SourceBase + SourceLength > SourceCopyLimit) {
				SourceLength = SourceCopyLimit - SourceBase;		// don't go past end of source array
			} else {
				SourceCopyLimit = SourceBase + SourceLength;		// range may not include end of array
			}
			if (SourceBase >= SourceCopyLimit) {
				return RunTimeError("Source array start > length");
			}
		}
		if (!isNext(',')) { return false; }
															// *** Destination Array ***

		doingDim = true;											// get the array var name as a new array
		if (!getVar()) { doingDim = false; return false; }
		doingDim = false;
		if (!VarIsArray) { return RunTimeError("Destination not array"); }
		if (!VarIsNew) { return RunTimeError("Destination array previously DIMed"); }

		if (SourceArrayNumeric != VarIsNumeric) { return RunTimeError("Arrays not of same type"); }
		int DestVarNumber = VarNumber;

		int Extras = 0;												// Get the extras parameter
		if (!isNext(']')) {
			if (!evalNumericExpression()) { return false; }
			if (!isNext(']')) { return false; }
			Extras = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL()) { return false; }

		int totalLength = SourceLength + Math.abs(Extras);			// Go build an array of the proper size and type
		if (!BuildBasicArray(DestVarNumber, SourceArrayNumeric, totalLength)) { return false; }

		if (SourceArrayNumeric) {									// Do numeric array
			  for (int i = Extras; i < 0; ++i) {					// if Extras < 0, add to front
				  NumericVarValues.set(ArrayValueStart++, 0.0);
			  }
			  for (int i = SourceBase; i < SourceCopyLimit; ++i) {	// Copy the source array values
				  NumericVarValues.set(ArrayValueStart++, NumericVarValues.get(i));
			  }
			  for (int i = 0; i < Extras; ++i) {					// if Extras > 0, add to back
				  NumericVarValues.set(ArrayValueStart++, 0.0);
			  }
		  } else {													// Do String array
			  for (int i = Extras; i < 0; ++i) {					// if Extras < 0, add to front
				  StringVarValues.set(ArrayValueStart++, "");
			  }
			  for (int i = SourceBase; i < SourceCopyLimit; ++i) {	// Copy the source array values
				  StringVarValues.set(ArrayValueStart++, StringVarValues.get(i));
			  }
			  for (int i = 0; i < Extras; ++i) {					// if Extras > 0, add to back
				  StringVarValues.set(ArrayValueStart++, "");
			  }
		  }
		return true;
	}
	
//***********************************  List Package ************************************************
	
	private boolean executeLIST(){
		if (!GetListKeyWord()){ return false;}

		switch (KeyWordValue){
  	  	case list_new:
  	  		if (!execute_LIST_NEW()) return false;
  	  		break;
  	  	case list_addarray:
  	  		if (!execute_LIST_ADDARRAY()) return false;
  	  		break;
  	  	case list_addlist:
  	  		if (!execute_LIST_ADDLIST()) return false;
  	  		break;
  	  	case list_add:
  	  		if (!execute_LIST_ADD()) return false;
  	  		break;
  	  	case list_set:
  	  		if (!execute_LIST_SET()) return false;
  	  		break;
  	  	case list_get:
  	  		if (!execute_LIST_GET()) return false;
  	  		break;
  	  	case list_gettype:
  	  		if (!execute_LIST_GETTYPE()) return false;
  	  		break;
  	  	case list_clear:
  	  		if (!execute_LIST_CLEAR()) return false;
  	  		break;
  	  	case list_remove:
  	  		if (!execute_LIST_REMOVE()) return false;
  	  		break;
  	  	case list_insert:
  	  		if (!execute_LIST_INSERT()) return false;
  	  		break;
  	  	case list_size:
  	  		if (!execute_LIST_SIZE()) return false;
  	  		break;
  	  	case list_contains:
  	  		if (!execute_LIST_CONTAINS()) return false;
  	  		break;
  	  	case list_toarray:
  	  		if (!execute_LIST_TOARRAY()) return false;
  	  		break;
  	  	case list_search:
  	  		if (!execute_LIST_SEARCH()) return false;
  	  		break;

  	  	default:
  	  		return false;
  	  	}
		return true;
		
	}
	
	
	private  boolean GetListKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		int i = 0;
		for (i = 0; i<List_KW.length; ++i){				// loop through the key word list
			if (Temp.startsWith(List_KW[i])){    			// if there is a match
				KeyWordValue = i;							// set the key word number
				LineIndex = LineIndex + List_KW[i].length(); // move the line index to end of key word
				return true;								// and report back
				}
		}
		KeyWordValue = list_none;						// no key word found
		return false;										// report fail

	}
	
	private boolean execute_LIST_NEW(){
		char c = ExecutingLineBuffer.charAt(LineIndex);    // Get the type, s or n
		++LineIndex;
		int type = 0;
		
		if (c == 'n' ){
			type = list_is_numeric;
		} 
		else if (c == 's'){
			type = list_is_string;
		} else return false;

		if (!isNext(',')) { return false; }
		
		   if (!(getVar() && VarIsNumeric)) { return false; }	// List pointer variable
		   int SaveValueIndex = theValueIndex;
	
		if (!checkEOL()) { return false; }

		int theIndex = createNewList(type);								// Try to create list
		if (theIndex < 0) { return false; }								// Create failed

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

	private boolean execute_LIST_ADDARRAY(){
		if (!evalNumericExpression()) return false;					// Get the destination list pointer
		int destListIndex = EvalNumericExpressionValue.intValue();
		if (destListIndex < 1 || destListIndex >= theLists.size()){
			return RunTimeError("Invalid Destination List Pointer");
		}
		if (!isNext(',')) return false;
		if (!getArrayVarForRead()) return false;					// Get the array variable
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (!checkEOL()) return false;

		boolean isListNumeric = (theListsType.get(destListIndex) == list_is_numeric);
		if (isListNumeric != VarIsNumeric) { return RunTimeError("Type mismatch"); }

		Bundle ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));// Get the array table bundle for this array
		int size = ArrayEntry.getInt("length");						// get the array length
		int base = ArrayEntry.getInt("base");						// and the start of the array in the variable space

		ArrayList destList = theLists.get(destListIndex);
		ArrayList sourceList = (isListNumeric) ? NumericVarValues : StringVarValues;
		for (int i = 0; i < size; ++i ){							// Copy array to list
			destList.add(sourceList.get(base + i));
		}
		return true;
	}
	
	private boolean execute_LIST_ADDLIST(){
		if (!evalNumericExpression()) return false;					// Get the destination list pointer
		int destListIndex = EvalNumericExpressionValue.intValue();
		if (destListIndex < 1 || destListIndex >= theLists.size()){
			return RunTimeError("Invalid Destination List Pointer");
		}
		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;					// Get the source list pointer
		int sourceListIndex = EvalNumericExpressionValue.intValue();
		if (sourceListIndex < 1 || sourceListIndex >= theLists.size()){
			return RunTimeError("Invalid Source List Pointer");
		}
		if (!checkEOL()) return false;

		boolean isDestNumeric = (theListsType.get(destListIndex) == list_is_numeric);
		boolean isSourceNumeric = (theListsType.get(sourceListIndex) == list_is_numeric);
		if (isDestNumeric != isSourceNumeric) { return RunTimeError("Type mismatch"); }

		theLists.get(destListIndex).addAll(theLists.get(sourceListIndex));
		return true;
	}
	
	private boolean execute_LIST_SEARCH(){
		
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;										// move to the value

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
			RunTimeError("Internal problem. Notify developer");
			return false;
		}
		
		++found;// Found is ones based

		NumericVarValues.set(savedVarIndex, (double) found);
		return true;
	}
	
	private boolean execute_LIST_ADD(){
		if (!evalNumericExpression()) return false;					// Get the List pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
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
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// Get the index to get
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(',')) return false;

		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			if (!evalStringExpression()) {
				RunTimeError("Type mismatch");
				return false;
			}
			if (!checkEOL()) return false;
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){		
				RunTimeError("Index out of bounds");
				return false;
			}
			thisStringList.set(getIndex, StringConstant);
			break;
			
		case list_is_numeric:												// Number
			if (!evalNumericExpression()){
				RunTimeError("Type mismatch");
				return false;
			}
			if (!checkEOL()) return false;
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()){
				RunTimeError("Index out of bounds");
				return false;
			}
			thisNumericList.set(getIndex, EvalNumericExpressionValue);
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		return true;
	}
	
	private boolean execute_LIST_GET(){
		
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
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
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){		
				RunTimeError("Index out of bounds");
				return false;
			}
			String thisString = thisStringList.get(getIndex);			//Get the requested string
			StringVarValues.set(theValueIndex, thisString);
			break;
			
		case list_is_numeric:												// Number
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()){
				RunTimeError("Index out of bounds");
				return false;
			}
			Double thisNumber = thisNumericList.get(getIndex);				// Get the requested number
			NumericVarValues.set(theValueIndex, thisNumber);
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		return true;
	}
	
	private boolean execute_LIST_GETTYPE(){
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;
		if (!getSVar()) return false;
		if (!checkEOL()) return false;
		
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			StringVarValues.set(theValueIndex, "S");
			break;
			
		case list_is_numeric:												// Number
			StringVarValues.set(theValueIndex, "N");
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}

		return true;
	}
	
	private boolean execute_LIST_CLEAR(){
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!checkEOL()) return false;
		theLists.get(listIndex).clear();
		return true;
	}
	
	private boolean execute_LIST_REMOVE(){
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;					// Get the index to remove
		if (!checkEOL()) return false;
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		ArrayList theList = theLists.get(listIndex);				// Get the  list
		if (getIndex < 0 || getIndex >= theList.size()) {		
			RunTimeError("Index out of bounds");
			return false;
		}
		theList.remove(getIndex);
		return true;
	}

	private boolean execute_LIST_INSERT(){
		
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;
		if (!evalNumericExpression()) return false;					// Get the index insert at
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(',')) return false;

		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:											 // String
			if (!getStringArg()) {
				RunTimeError("Type mismatch");
				return false;
			}
			if (!checkEOL()) return false;

			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){		
				RunTimeError("Index out of bounds");
				return false;
			}
			thisStringList.add(getIndex, StringConstant);
			break;
			
		case list_is_numeric:												// Number
			if (!evalNumericExpression()){
				RunTimeError("Type mismatch");
				return false;
			}
			if (!checkEOL()) return false;

			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()){
				RunTimeError("Index out of bounds");
				return false;
			}
			thisNumericList.add(getIndex, EvalNumericExpressionValue);
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}

		return true;
	}

	private boolean execute_LIST_SIZE(){
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;										// move to the return var
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
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = EvalNumericExpressionValue.intValue();
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		if (!isNext(',')) return false;										// move to the array var
		if (!getArrayVarForWrite()) return false;							// Get the array name var
		if (!isNext(']')) { return RunTimeError("Expected '[]'"); }	// Array must not have any indices
		if (!checkEOL()) return false;
		int svn = VarNumber;										// save the array variable table number

		int listType = theListsType.get(listIndex);						// Get this lists type
		boolean isListNumeric = (listType == list_is_numeric);
		if (isListNumeric != VarIsNumeric) { return RunTimeError("Type mismatch"); }

		if (isListNumeric) {
			ArrayList<Double> Values = theLists.get(listIndex);			// Get the numeric list
			return ListToBasicNumericArray(svn, Values, Values.size());	// Copy the list to a BASIC! array
		} else {
			ArrayList<String> Values = theLists.get(listIndex);			// Get the string list
			return ListToBasicStringArray(svn, Values, Values.size());	// Copy the list to a BASIC! array
		}
	}
	
// ***************************************Bundle Commands *******************************************
	
	private boolean executeBUNDLE(){
		if (!GetBundleKeyWord()){ return false;}
		switch (KeyWordValue){
		case bundle_create:
  	  		if (!execute_BUNDLE_CREATE()) return false;
  	  		break;
		case bundle_put:
  	  		if (!execute_BUNDLE_PUT()) return false;
  	  		break;
		case bundle_get:
  	  		if (!execute_BUNDLE_GET()) return false;
  	  		break;
		case bundle_type:
  	  		if (!execute_BUNDLE_TYPE()) return false;
  	  		break;
		case bundle_keyset:
  	  		if (!execute_BUNDLE_KEYSET()) return false;
  	  		break;
		case bundle_copy:
  	  		if (!execute_BUNDLE_COPY()) return false;
  	  		break;
		case bundle_clear:
  	  		if (!execute_BUNDLE_CLEAR()) return false;
  	  		break;
		case bundle_contain:               // Condor
			if (!execute_BUNDLE_CONTAIN()) return false;   // Condor
			break;                     // Condor             
  	  	default:
  	  		return false;
  	  	
		}
		return true;
	}
	
	private  boolean GetBundleKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		int i = 0;
		for (i = 0; i<Bundle_KW.length; ++i){				// loop through the key word list
			if (Temp.startsWith(Bundle_KW[i])){    			// if there is a match
				KeyWordValue = i;							// set the key word number
				LineIndex = LineIndex + Bundle_KW[i].length(); // move the line index to end of key word
				return true;								// and report back
				}
		}
		KeyWordValue = list_none;						// no key word found
		return false;										// report fail

	}

	private boolean execute_BUNDLE_CREATE(){
		
		   if (!getVar())return false;							// List pointer variable
		   if (!VarIsNumeric)return false;
		   int SaveValueIndex = theValueIndex;
		   Bundle b = new Bundle();

		   int bundleIndex = theBundles.size();
		   theBundles.add(b);
		   
		   NumericVarValues.set(SaveValueIndex, (double) bundleIndex);
		   
		return true;
	}
	
	private boolean execute_BUNDLE_PUT(){
		
		if (!evalNumericExpression()) return false;							// Get the Bundle pointer
		int bundleIndex = EvalNumericExpressionValue.intValue();
		if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
			RunTimeError("Invalid Bundle Pointer");
			return false;
		}
		
	    Bundle b = theBundles.get(bundleIndex);
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the tag
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalStringExpression()) return false;
		String tag = StringConstant;
		
		c = ExecutingLineBuffer.charAt(LineIndex);							// move to the value
		if ( c != ',') return false;
		++LineIndex;
		
		int LI = LineIndex;
		if (evalNumericExpression()){
			b.putDouble(tag, EvalNumericExpressionValue);
			b.putBoolean("@@@N."+tag, true);
			return true;			
		}
		LineIndex = LI;

		if (!evalStringExpression()) return false;
		b.putString(tag, StringConstant);
		b.putBoolean("@@@N."+tag, false);
		
		return true;
	}
	
	private boolean execute_BUNDLE_GET(){
		if (!evalNumericExpression()) return false;							// Get the Bundle pointer
		int bundleIndex = EvalNumericExpressionValue.intValue();
		if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
			RunTimeError("Invalid Bundle Pointer");
			return false;
		}
		
	    Bundle b = theBundles.get(bundleIndex);
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the tag
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalStringExpression()) return false;
		String tag = StringConstant;
		
		if (!b.containsKey(tag)) {
			RunTimeError(tag +" not in bundle");
			return false;
		}
		
		c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;

		
		boolean isNumeric = b.getBoolean("@@@N."+tag);
		
		if (!getVar()) return false;
		
		if (isNumeric) {
			if (!VarIsNumeric){
				RunTimeError(tag + " is not a string");
				return false;
			}
			NumericVarValues.set(theValueIndex, b.getDouble(tag));
			return true;
		}
		
		if (VarIsNumeric) {
			RunTimeError(tag + " is not numeric");
			return false;
		}
		
		StringVarValues.set(theValueIndex, b.getString(tag));
		return true;
	}
	
	private boolean execute_BUNDLE_TYPE(){
		if (!evalNumericExpression()) return false;							// Get the Bundle pointer
		int bundleIndex = EvalNumericExpressionValue.intValue();
		if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
			RunTimeError("Invalid Bundle Pointer");
			return false;
		}
		
	    Bundle b = theBundles.get(bundleIndex);
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the tag
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalStringExpression()) return false;
		String tag = StringConstant;
		
		if (!b.containsKey(tag)) {
			RunTimeError(tag +" not in bundle");
			return false;
		}
		
		c = ExecutingLineBuffer.charAt(LineIndex);						// move to the tag
		if ( c != ',') return false;
		++LineIndex;

		
		boolean isNumeric = b.getBoolean("@@@N."+tag);
		String type = "S";
		if (isNumeric) type = "N";
		
		if (!getSVar()) return false;
		
		StringVarValues.set(theValueIndex, type);
		return true;
		
	}
	
	private boolean execute_BUNDLE_KEYSET(){
		if (!evalNumericExpression()) return false;							// Get the Bundle pointer
		int bundleIndex = EvalNumericExpressionValue.intValue();
		if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
			RunTimeError("Invalid Bundle Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the tag
		if ( c != ',') return false;
		++LineIndex;
		
		if (!getNVar()) return false;
		
		 ArrayList<String> theStringList = new ArrayList <String>();
		 int theIndex = theLists.size();
		 theLists.add(theStringList);
		 theListsType.add(list_is_string);
		 NumericVarValues.set(theValueIndex, (double) theIndex);

	    Bundle b = theBundles.get(bundleIndex);
	    Set<String> set= b.keySet();
	    
	    Iterator it = set.iterator();
	    for (int i=0; i<set.size(); ++i){
	    	String s = (String) it.next();
	    	if (!s.startsWith("@@@N."))theStringList.add(s);
	    }
	    
		return true;
	}
	
	private boolean execute_BUNDLE_COPY(){
		return false;
	}
	
	private boolean execute_BUNDLE_CLEAR(){
		if (!evalNumericExpression()) return false;							// Get the Bundle pointer
		int bundleIndex = EvalNumericExpressionValue.intValue();
		if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
			RunTimeError("Invalid Bundle Pointer");
			return false;
		}
		
	    Bundle b = theBundles.get(bundleIndex);
	    b.clear();
		return true;
	}
	
	private boolean execute_BUNDLE_CONTAIN(){
		   if (!evalNumericExpression()) return false;                  // Get the Bundle pointer
		   int bundleIndex = EvalNumericExpressionValue.intValue();
		   if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
		      RunTimeError("Invalid Bundle Pointer");
		      return false;
		   }      
		   Bundle b = theBundles.get(bundleIndex);      

		   char c = ExecutingLineBuffer.charAt(LineIndex);               // move to the tag
		   if ( c != ',') return false;
		   ++LineIndex;      

		   if (!evalStringExpression()) return false;
		   String tag = StringConstant;      

		   double thereis = (!b.containsKey(tag)) ? 0.0:1.0;      

		   c = ExecutingLineBuffer.charAt(LineIndex);                  // move to the value
		   if ( c != ',') return false;
		   ++LineIndex;

		   if (!getNVar()) return false;
		   NumericVarValues.set(theValueIndex,thereis);

		   return true;
		}

// ************************************** Stack Commands ********************************************
	
	private boolean executeSTACK(){
		if (!GetStackKeyWord()){ return false;}
		switch (KeyWordValue){
		case stack_create:
  	  		if (!execute_STACK_CREATE()) return false;
  	  		break;
		case stack_push:
  	  		if (!execute_STACK_PUSH()) return false;
  	  		break;
		case stack_pop:
  	  		if (!execute_STACK_POP()) return false;
  	  		break;
		case stack_peek:
  	  		if (!execute_STACK_PEEK()) return false;
  	  		break;
		case stack_type:
  	  		if (!execute_STACK_TYPE()) return false;
  	  		break;
		case stack_isempty:
  	  		if (!execute_STACK_ISEMPTY()) return false;
  	  		break;
		case stack_clear:
  	  		if (!execute_STACK_CLEAR()) return false;
  	  		break;
  	  	default:
  	  		return false;
  	  	
		}
		return true;
	}
	

	
	private  boolean GetStackKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		int i = 0;
		for (i = 0; i<Stack_KW.length; ++i){				// loop through the key word list
			if (Temp.startsWith(Stack_KW[i])){    			// if there is a match
				KeyWordValue = i;							// set the key word number
				LineIndex = LineIndex + Stack_KW[i].length(); // move the line index to end of key word
				return true;								// and report back
				}
		}
		KeyWordValue = list_none;						// no key word found
		return false;										// report fail

	}
	
	private boolean execute_STACK_CREATE(){
		char c = ExecutingLineBuffer.charAt(LineIndex);    // Get the type, s or n
		++LineIndex;
		int type = 0;
		
		if (c == 'n' ){
			type = stack_is_numeric;
		} 
		else if (c == 's'){
			type = stack_is_string;
		} else return false;

		if (!isNext(',')) { return false; }

		   if (!(getVar() && VarIsNumeric)) { return false; }	// Stack pointer variable
		   int SaveValueIndex = theValueIndex;
	
		if (!checkEOL()) { return false; }
		   
	 Stack theStack = new Stack();
	 int theIndex = theStacks.size();
	 theStacks.add(theStack);
	 
	   theStacksType.add(type);											// add the type
	   NumericVarValues.set(SaveValueIndex, (double) theIndex);   		// Return the stack pointer    
		
		return true;
	}

	private boolean execute_STACK_PUSH(){

		if (!evalNumericExpression()) return false;							// Get the List pointer
		int stackIndex = EvalNumericExpressionValue.intValue();
		if (stackIndex < 1 || stackIndex >= theStacks.size()){
			RunTimeError("Invalid Stack Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;
		
		switch (theStacksType.get(stackIndex))
		{
		case stack_is_string:												// String type list
			if (!evalStringExpression()) {
				RunTimeError("Type mismatch");
				return false;
			}
			Stack thisStack = theStacks.get(stackIndex);		        // Get the string list
			thisStack.push(StringConstant);								// Add the string to the list
			break;
			
		case stack_is_numeric:
			if (!evalNumericExpression()){
				RunTimeError("Type mismatch");
				return false;
			}
			thisStack = theStacks.get(stackIndex);		        // Get the string list
			thisStack.push(EvalNumericExpressionValue);								// Add the string to the list
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		return true;
	}

	private boolean execute_STACK_POP(){
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int stackIndex = EvalNumericExpressionValue.intValue();
		if (stackIndex < 1 || stackIndex >= theStacks.size()){
			RunTimeError("Invalid Stack Pointer");
			return false;
		}

		Stack thisStack = theStacks.get(stackIndex);		        // Get the string list
		if (thisStack.isEmpty()){
			RunTimeError("Stack is empty");
			return false;
		}
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;
		
		switch (theStacksType.get(stackIndex))
		{
		case stack_is_string:												// String type list
			if (!getSVar()) {
				RunTimeError("Type mismatch");
				return false;
			}
			String thisString = (String) thisStack.pop();
			StringVarValues.set(theValueIndex, thisString);
			break;
			
		case stack_is_numeric:
			if (!getNVar()) {
				RunTimeError("Type mismatch");
				return false;
			}
			Double thisNumber = (Double) thisStack.pop();
			NumericVarValues.set(theValueIndex, thisNumber);
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		return true;
	}

	private boolean execute_STACK_PEEK(){
		if (!evalNumericExpression()) return false;							// Get the Stack pointer
		int stackIndex = EvalNumericExpressionValue.intValue();
		if (stackIndex < 1 || stackIndex >= theStacks.size()){
			RunTimeError("Invalid Stack Pointer");
			return false;
		}

		Stack thisStack = theStacks.get(stackIndex);		               // Get the Stack
		if (thisStack.isEmpty()){
			RunTimeError("Stack is empty");
			return false;
		}
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;
		
		switch (theStacksType.get(stackIndex))
		{
		case stack_is_string:												// String type list
			if (!getSVar()) {
				RunTimeError("Type mismatch");
				return false;
			}
			String thisString = (String) thisStack.peek();
			StringVarValues.set(theValueIndex, thisString);
			break;
			
		case stack_is_numeric:
			if (!getNVar()) {
				RunTimeError("Type mismatch");
				return false;
			}
			Double thisNumber = (Double) thisStack.peek();
			NumericVarValues.set(theValueIndex, thisNumber);
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		return true;
	}

	private boolean execute_STACK_TYPE(){
		if (!evalNumericExpression()) return false;							// Get the Stack pointer
		int stackIndex = EvalNumericExpressionValue.intValue();
		if (stackIndex < 1 || stackIndex >= theStacks.size()){
			RunTimeError("Invalid Stack Pointer");
			return false;
		}
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;

		if (!getSVar()) return false;
		
		switch (theStacksType.get(stackIndex))						// Get this Stack type
		{
		case stack_is_string:										// String
			StringVarValues.set(theValueIndex, "S");
			break;
			
		case stack_is_numeric:												// Number
			StringVarValues.set(theValueIndex, "N");
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
		}

		
		return true;
	}

	private boolean execute_STACK_ISEMPTY(){
		if (!evalNumericExpression()) return false;							// Get the Stack pointer
		int stackIndex = EvalNumericExpressionValue.intValue();
		if (stackIndex < 1 || stackIndex >= theStacks.size()){
			RunTimeError("Invalid Stack Pointer");
			return false;
		}
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;

		if (!getNVar()) return false;
		
		double empty = 0;
		Stack thisStack = theStacks.get(stackIndex);		               // Get the Stack
		if (thisStack.isEmpty()){
			empty = 1;
		}
		
		NumericVarValues.set(theValueIndex, empty);
		
		return true;
	}

	private boolean execute_STACK_CLEAR(){
		if (!evalNumericExpression()) return false;							// Get the Stack pointer
		int stackIndex = EvalNumericExpressionValue.intValue();
		if (stackIndex < 1 || stackIndex >= theStacks.size()){
			RunTimeError("Invalid Stack Pointer");
			return false;
		}
		
		Stack thisStack = theStacks.get(stackIndex);		               // Get the Stack

		while (!thisStack.isEmpty()){thisStack.pop();}

		return true;
	}

	
	//*********************************** Clipboard Commands *****************************************

	/*
	// This code does not work on devices with API level < 11
	 
	private boolean executeCLIPBOARD_GET(){
		  if (!getVar()) return false;						// get the var to put the clip into
		  if (VarIsNumeric) return false;
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
		int v = Integer.valueOf(android.os.Build.VERSION.SDK);
		if (!evalStringExpression()) return false;          // Get the string to put into the clipboard
		if (SEisLE) return false;
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		CharSequence cs = StringConstant;
		ClipData clip = ClipData.newPlainText("simple text",cs);
		clipboard.setPrimaryClip(clip);
		if (!checkEOL()) return false;
		return true;
	}
	*/
	
	private boolean executeCLIPBOARD_GET(){
		  if (!getVar()) return false;						// get the var to put the clip into
		  if (VarIsNumeric) return false;
		  String data;
		  if (clipboard.hasText()){                        // If clip board has text
			  CharSequence data1 = clipboard.getText();    // Get the clip
			  data = data1.toString();       
		  }else data ="";									// If no clip, set data to null
		  StringVarValues.set(theValueIndex, data);         // Return the result to user
			if (!checkEOL()) return false;
		return true;
	}
	
	private boolean executeCLIPBOARD_PUT(){
		if (!evalStringExpression()) return false;          // Get the string to put into the clipboard
		if (SEisLE) return false;
		clipboard.setText(StringConstant);                  // Put the user expression into the clipboard
		if (!checkEOL()) return false;
		return true;
	}
	
	// ********************************Encryption commands *****************************************
	
	
/*************************************  Cypher Commands  *****************************************/
	
	private boolean executeENCRYPT(){
		int q = 0;
		q = 1;
		if (!evalStringExpression()) return false;          // Get the Pass Word
		if (SEisLE) return false;
		String PW = StringConstant;
		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		++LineIndex;

		if (!evalStringExpression()) return false;          // Get the Src string
		if (SEisLE) return false;
		String Src = StringConstant;
		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		++LineIndex;
		
		if (!getVar())return false;							// Get the destination Var string
		if (VarIsNumeric)return false;
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
	

	private boolean executeDECRYPT(){
		if (!evalStringExpression()) return false;          // Get the Pass Word
		if (SEisLE) return false;
		String PW = StringConstant;
		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		++LineIndex;

		if (!evalStringExpression()) return false;          // Get the Src string
		if (SEisLE) return false;
		String Src = StringConstant;
		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		++LineIndex;
		
		if (!getVar())return false;							// Get the destination Var string
		if (VarIsNumeric)return false;
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
	
//************************************** Socket Commands  ******************************************

	private boolean executeSOCKET(){								// Get Socket command key word if it is there
		return executeCommand(Socket_cmd, "Socket");
	}

	private boolean executeSocketServer(){							// Get Socket Server command key word if it is there
		return executeCommand(SocketServer_cmd, "Socket Server");
	}

	private boolean executeSocketClient(){							// Get Socket Client command key word if it is there
		return executeCommand(SocketClient_cmd, "Socket Client");
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

	private boolean executeSERVER_ACCEPT(){
		
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
		serverSocketConnectThread = new SocketConnectThread();
		serverSocketConnectThread.start();
		if (block) {
			while (serverSocketState == STATE_LISTENING) { Thread.yield(); }
			if (serverSocketState != STATE_CONNECTED) { return false; }
		}
		return true;
	}

	private class SocketConnectThread extends Thread {
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
		}

		@Override
		public void interrupt() {
			if (serverSocketState == STATE_LISTENING) {						// in case SERVER_DISCONNECT interrupts thread
				serverSocketState = STATE_NONE;								// change state or SERVER_STATUS will report LISTENING
			}
			super.interrupt();
		}
	}

	private boolean executeCLIENT_CONNECT(){

		if (!getStringArg()) return false;									// get the server address
		String SocketClientsServerAdr = StringConstant;
		
		if (!isNext(',')) return false;										// move to the port number
		if (!evalNumericExpression()) return false;							// get the port number
		int SocketClientsServerPort = EvalNumericExpressionValue.intValue();
		if (!checkEOL()) return false;
		
		try {
			theClientSocket = new Socket(SocketClientsServerAdr, SocketClientsServerPort);
			ClientBufferedReader = new BufferedReader(new InputStreamReader(theClientSocket.getInputStream()));
			ClientPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(theClientSocket.getOutputStream())), true);
			clientSocketState = STATE_CONNECTED;
		} catch (Exception e) {
			clientSocketState = STATE_NONE;
			return RunTimeError(e);
		}

		return true;
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

		double status = (theClientSocket == null)               ? STATE_NONE
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
				executeSERVER_DISCONNECT();
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

	private boolean executeSERVER_DISCONNECT(){
		if (!checkEOL()) return false;

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

	private boolean executeSERVER_CLOSE(){
		if (!checkEOL()) return false;
		if (theServerSocket != null) executeSERVER_DISCONNECT();

		try {
			if (newSS != null) newSS.close();
		} catch (Exception e) {
			return RunTimeError(e);
		} finally {
			newSS = null;
		}
		return true;
	}

	private boolean executeCLIENT_CLOSE(){
		if (!checkEOL()) return false;
		if (theClientSocket == null) return true;

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
	
	private boolean executeTTS_INIT(){
		  
		  ttsInit = false;
    	  ttsIntent = new Intent(this, TextToSpeechActivity.class);				// Start the GPS

	      startActivityForResult(ttsIntent, BASIC_GENERAL_INTENT);
	      while (!ttsInit) Thread.yield();				// Wait for signal from GPS thread
	      
	      switch (ttsInitResult){
	      case TextToSpeech.SUCCESS:
	    	  break;
	    	  
	      case TextToSpeech.LANG_MISSING_DATA:
	    	  RunTimeError("Language Data Missing");
	    	  return false;
	    	  
	      case TextToSpeech.LANG_NOT_SUPPORTED:
	    	  RunTimeError("Language Not Supported");
	    	  return false;
	    	  
	      default:
	    	  RunTimeError("TTS Init Failed. Code = " + ttsInitResult);
	    	  return false;
	      }
		  if (!GRFront){
			  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(Basic.theProgramRunner);
			  GRFront = false;
		  }else if (GRclass == null){
			  GRFront = false;
		  }else {
			  GRclass.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(GRclass);
			  GRFront = true;
		  }


		return true;
	}
	
	
	private boolean executeTTS_SPEAK(){
		if (mTts == null) {
			RunTimeError("Text to speech not initialized");
			return false;
		}
		
		if (!evalStringExpression()) return false;
		  setVolumeControlStream(AudioManager.STREAM_MUSIC);
		  ttsSpeakDone = false;
	      HashMap<String, String> myHashAlarm = new HashMap<String, String>();
	      myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
	      myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "Done");
	      Run.mTts.speak(StringConstant, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
	      while (!ttsSpeakDone) 
	    	  Thread.yield();
		return true;
	}
	
	private boolean executeTTS_LOCAL() {
		return true;
		
	}
	
// **********************************************  FTP **************************************************


	public boolean executeFTP(){
		if (!GetFTPKeyWord()){ return false;}
		switch (KeyWordValue){
		case ftp_open:
			if (!executeFTP_OPEN()) return false;
			break;
		case ftp_close:
			if (!executeFTP_CLOSE()) return false;
			break;
		case ftp_dir:
			if (!executeFTP_DIR()) return false;
			break;
		case ftp_cd:
			if (!executeFTP_CD()) return false;
			break;
		case ftp_put:
			if (!executeFTP_PUT()) return false;
			break;
		case ftp_get:
			if (!executeFTP_GET()) return false;
			break;
		case ftp_delete:
			if (!executeFTP_DELETE()) return false;
			break;
		case ftp_rmdir:
			if (!executeFTP_RMDIR()) return false;
			break;
		case ftp_mkdir:
			if (!executeFTP_MKDIR()) return false;
			break;
		case ftp_rename:
			if (!executeFTP_RENAME()) return false;
			break;
		default:
		}
		return true;
	}
	
	  private  boolean GetFTPKeyWord(){						// Get a Basic key word if it is there
			// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<ftp_KW.length; ++i){		// loop through the key word list
			  if (Temp.startsWith(ftp_KW[i])){    // if there is a match
				  KeyWordValue = i;						// set the key word number
				  LineIndex = LineIndex + ftp_KW[i].length(); // move the line index to end of key word
				  return true;							// and report back
			  }
		  }
		  KeyWordValue = 99;							// no key word found
		  return false;									// report fail

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
				if (file.isDirectory()) { name += "(d)"; }				// mark directories
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

// *****************************************  Bluetooth ********************************
		
		public boolean executeBT(){
			if (!GetBTKeyWord()){ return false;}

			if (KeyWordValue == bt_status) { return execute_BT_status(); }

			if ( mChatService == null && KeyWordValue != bt_open){
				RunTimeError("Bluetooth not opened");
				return false;
			}

			switch (KeyWordValue){
			case bt_open:
				if (!execute_BT_open()) return false;
				break;
			case bt_close:
				if (!execute_BT_close()) return false;
				break;
			case bt_connect:
				if (!execute_BT_connect()) return false;
				break;
			case bt_device_name:
				if (!execute_BT_device_name()) return false;
				break;
			case bt_write:
				if (!execute_BT_write()) return false;
				break;
			case bt_read_ready:
				if (!execute_BT_read_ready()) return false;
				break;
			case bt_read_bytes:
				if (!execute_BT_read_bytes()) return false;
				break;
			case bt_set_uuid:
				if (!execute_BT_set_uuid()) return false;
				break;
			case bt_listen:
				if (!execute_BT_listen()) return false;
				break;
			case bt_reconnect:
				if (!execute_BT_reconnect()) return false;
				break;
			case bt_readready_resume:
				if (!execute_BT_readReady_Resume()) return false;
				break;
			case bt_disconnect:
				if (!execute_BT_disconnect()) return false;
				break;
			case bt_status:			// already handled
			default:
			}
			return true;
		}
		
		  private  boolean GetBTKeyWord(){						// Get a Basic key word if it is there
				// is the current line index at a key word?
			  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
			  int i = 0;
			  for (i = 0; i<bt_KW.length; ++i){		// loop through the key word list
				  if (Temp.startsWith(bt_KW[i])){    // if there is a match
					  KeyWordValue = i;						// set the key word number
					  LineIndex = LineIndex + bt_KW[i].length(); // move the line index to end of key word
					  return true;							// and report back
				  }
			  }
			  KeyWordValue = 99;							// no key word found
			  return false;									// report fail

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

//    ***********************************  Superuser and System ***************************

	private boolean executeSU(boolean isSU) {	// SU command (isSU true) or system comand (isSU false)
		for (Command c : SU_cmd) {								// loop through the command list
			if (ExecutingLineBuffer.startsWith(c.name, LineIndex)) { // if there is a match
				LineIndex += c.name.length();					// move the line index to end of key word
				if (SUprocess == null) {
					if (c.name.equals("open")) this.isSU = isSU;
					else return RunTimeError((isSU ? "Superuser" : "System shell") + " not opened");
				}
				return c.run();									// run the function and report back
			}
		}
		RunTimeError("Unknown " + (isSU ? "SU" : "System") + " command");
		return false;											// no key word found
	}

		    private boolean execute_SU_open(){
		    	if (!checkEOL()) return false;
		    	if (SUprocess != null) return true;
		    	SU_ReadBuffer = new ArrayList<String>();    		// Initialize buffer
		    	
		    	try
		        {
		            if (isSU) SUprocess = Runtime.getRuntime().exec("su");				// Request Superuser
		            else {
		            	File dir = new File(Basic.getFilePath());
		            	if (!dir.exists()) { dir.mkdirs(); }
		            	SUprocess = Runtime.getRuntime().exec("sh", null, dir);			// Open ordinary shell
		            }
		            SUoutputStream = new DataOutputStream(SUprocess.getOutputStream()); // Open the output stream
		            SUinputStream = new DataInputStream(SUprocess.getInputStream());    // Open the input stream
		        }
		        catch (Exception e)
		        {
		            RunTimeError("SU Exception: " + e);
		            return false;
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
		            RunTimeError("SU Exception: " + e);
		            return false;	    		
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
		    
/****************************************** CONSOLE Commands  ************************************/

	private boolean executeCONSOLE() {							// Get Console command key word if it is there
		return executeCommand(Console_cmd, "Console");
	}

	private boolean executeCONSOLE_TITLE() {					// Set the console title string
		if (isEOL()) {
			ConsoleTitle = null;								// Use default
		} else {
			if (!getStringArg() || !checkEOL()) return false;	// Get new title
			ConsoleTitle = StringConstant;
		}
		Show("@@7");											// Signal UI to update its title
		return true;
	}

	private boolean executeCONSOLE_DUMP(){

		if (!getStringArg() || !checkEOL()) { return false; }	// Only paramter is the filename
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
		// Log.d(LOGTAG, "executeCONSOLE_DUMP: file " + theFileName + " written");

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
	  
/******************************************** SOUND POOL ******************************************/
	  
		public boolean executeSP(){
			
			if (!GetSPKeyWord()){ return false;}
			
			if (theSoundPool == null &&
				KeyWordValue != sp_open){
				RunTimeError("SoundPool not opened");
				return false;
			}

			switch (KeyWordValue){
			case sp_open:
				if (!execute_SP_open()) return false;
				break;
			case sp_load:
				if (!execute_SP_load()) return false;
				break;
			case sp_play:
				if (!execute_SP_play()) return false;
				break;
			case sp_stop:
				if (!execute_SP_stop()) return false;
				break;
			case sp_unload:
				if (!execute_SP_unload()) return false;
				break;
			case sp_pause:
				if (!execute_SP_pause()) return false;
				break;
			case sp_resume:
				if (!execute_SP_resume()) return false;
				break;
			case sp_release:
				if (!execute_SP_release()) return false;
				break;
			case sp_setvolume:
				if (!execute_SP_setvolume()) return false;
				break;
			case sp_setpriority:
				if (!execute_SP_setpriority()) return false;
				break;
			case sp_setloop:
				if (!execute_SP_setloop()) return false;
				break;
			case sp_setrate:
				if (!execute_SP_setrate()) return false;
				break;
					
			default:
			}
			return true;
		}
	  
	  private  boolean GetSPKeyWord(){						// Get a Basic key word if it is there
			// is the current line index at a key word?
		  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		  int i = 0;
		  for (i = 0; i<sp_KW.length; ++i){		// loop through the key word list
			  if (Temp.startsWith(sp_KW[i])){    // if there is a match
				  KeyWordValue = i;						// set the key word number
				  LineIndex = LineIndex + sp_KW[i].length(); // move the line index to end of key word
				  return true;							// and report back
			  }
		  }
		  KeyWordValue = 99;							// no key word found
		  return false;									// report fail

	  		}
	  
	  private boolean execute_SP_open(){
		  
		  if (!evalNumericExpression()) return false;
		  if (!checkEOL()) return false;
		  int SP_max = EvalNumericExpressionValue.intValue();
		  if (SP_max <=0 ){
			  RunTimeError("Stream count must be > 0");
			  return false;			  
		  }
//		  this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
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
		File file = new File(fn);

		int SoundID = 0;
		if (file.exists()) {
			SoundID = theSoundPool.load(fn, 1);
		} else {														// file does not exist
			if (Basic.isAPK) {											// if not standard BASIC! then is user APK
				int resID = getRawResourceID(fileName);					// try to load the file from a raw resource
				if (resID == 0) { return false; }
				SoundID = theSoundPool.load(BasicContext, resID, 1);
			}															// else standard BASIC!, SoundID is 0
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
	  
	  
	  private boolean execute_SP_pause(){
			if (!evalNumericExpression()) return false;
			if (!checkEOL()) return false;
			int streamID = EvalNumericExpressionValue.intValue();
			if (streamID == 0 ) theSoundPool.autoPause();
			else theSoundPool.pause(streamID);
		  return true;
	  }
	  
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
	  
	  //*********************************** Get my phone number ************************
	  
	  private boolean execute_my_phone_number(){
		  
		  if (!getSVar()) return false;
		  if (!checkEOL()) return false;
		  
		  TelephonyManager mTelephonyMgr;
	        mTelephonyMgr = (TelephonyManager)
	            getSystemService(Context.TELEPHONY_SERVICE);
	      String pn =  mTelephonyMgr.getLine1Number();
	      
	      if (pn == null) pn = "Get phone number failed." ;
	      
	      StringVarValues.set(theValueIndex, pn);
	        
		  return true;
	  }
	  
	  // ********************************* Headset *****************************************
	  
	  private boolean executeHEADSET(){
		  
		if (!getNVar()) return false;
		NumericVarValues.set(theValueIndex, (double) headsetState) ;
		  
			char c = ExecutingLineBuffer.charAt(LineIndex);					// Loop value					
			if ( c != ',') return false;
			++LineIndex;

		if (!getSVar()) return false;
	    StringVarValues.set(theValueIndex, headsetName);
	      
			c = ExecutingLineBuffer.charAt(LineIndex);					// Loop value					
			if ( c != ',') return false;
			++LineIndex;
			
		if (!getNVar()) return false;
		NumericVarValues.set(theValueIndex, (double) headsetMic) ;
		  
		  return true;
	  }
	  
	  // ******************************** SMS ***************************************
	  
	  private boolean executeSMS_SEND(){
		  
		  if (!evalStringExpression()) return false;
		  String number = StringConstant;

			char c = ExecutingLineBuffer.charAt(LineIndex);					// Loop value					
			if ( c != ',') return false;
			++LineIndex;

		  if (!evalStringExpression()) return false;
		  String msg = StringConstant;
		  
		  SmsManager sm = android.telephony.SmsManager.getDefault();
		  try {
			  sm.sendTextMessage(number, null, msg, null, null);
		  } catch (Exception e) {
			  return RunTimeError(e);
		  }
		  
		  return true;
	  }
	  
	  private boolean executeSMS_RCV_INIT(){
		  
		  registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		  smsRcvBuffer = new ArrayList <String>();
		  return true;
	  }
	  
	  private boolean executeSMS_RCV_NEXT(){
		  if (smsRcvBuffer == null){
			  RunTimeError("SMS.RCV.INIT not executed)");
			  return false;
		  }
		  
		  if (!getSVar()) return false;
		  
		  if (smsRcvBuffer.size() == 0){
			  StringVarValues.set(theValueIndex, "@");
			  return true;
		  }
		  
		  StringVarValues.set(theValueIndex, smsRcvBuffer.get(0));
		  smsRcvBuffer.remove(0);
		  
		  return true;
	  }
	  
	  private BroadcastReceiver receiver = new BroadcastReceiver () {

          @Override
          public void onReceive(Context arg0, Intent arg1) {
              Bundle bundle = arg1.getExtras();
              SmsMessage[] recievedMsgs = null;
              String str = "";
              if (bundle != null)
              {
                  Object[] pdus = (Object[]) bundle.get("pdus");
                  recievedMsgs = new SmsMessage[pdus.length];
                  for (int i=0; i < recievedMsgs.length; ++i)
                  {
                  	  recievedMsgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                      str += "SMS from " + recievedMsgs[i].getOriginatingAddress()+ " :" + recievedMsgs[i].getMessageBody().toString();
                      if (smsRcvBuffer != null)
                    	  smsRcvBuffer.add(str);
                  }
              }
          }
      };
	  
	  //******************************* Phone Call ***********************************
	  
 	  private boolean executePHONE_CALL(){
		  if (!evalStringExpression()) return false;
		  String number = "tel:" + StringConstant;

		  Intent callIntent = new Intent(Intent.ACTION_CALL);  
		  callIntent.setData(Uri.parse(number));
		  callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.

		  try {
			  startActivityForResult(callIntent, BASIC_GENERAL_INTENT);  
		  } catch (Exception e){
			  return RunTimeError(e);
		  }
		  return true;
	  }
	  
 	  private boolean executePHONE_RCV_INIT(){
  		if (phoneRcvInited) return true;
  		
  		phoneRcvInited = true;
  		
  		mTM =  (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
  		mTM.listen(PSL, PhoneStateListener.LISTEN_CALL_STATE);
  		
 		return true;
 	  }
 	  
		PhoneStateListener PSL = new PhoneStateListener() {
 			public void onCallStateChanged(int state,  String incomingNumber) {
 				phoneState = state;
 				if (phoneState == TelephonyManager.CALL_STATE_RINGING) phoneNumber = incomingNumber;
 			}
 		}; 		 

 	  
 	  private boolean executePHONE_RCV_NEXT(){
 		 if (!phoneRcvInited) {
 			 RunTimeError("phone.rcv.init not executed");
 			 return false;
 		 }
 		 
 		 
 		 int callState = mTM.getCallState();
 		 if (callState == TelephonyManager.CALL_STATE_IDLE) phoneNumber = "";
 		 
 		  if (!getNVar()) return false;
 		  int stateIndex = theValueIndex;
 		  
			char c = ExecutingLineBuffer.charAt(LineIndex);									
			if ( c != ',') return false;
			++LineIndex;
			
			if (!getSVar()) return false;
			int numberIndex = theValueIndex;
			
			
			NumericVarValues.set(stateIndex, (double)callState);
			StringVarValues.set(numberIndex, phoneNumber);
			
			
			return true;
 	  }
 	  
	  //********************************  EMAIL  ************************************
	  
	  private boolean executeEMAIL_SEND(){
		  
		  if (!evalStringExpression()) return false;
		  String recipiant = "mailto:" + StringConstant;

			char c = ExecutingLineBuffer.charAt(LineIndex);									
			if ( c != ',') return false;
			++LineIndex;

		  if (!evalStringExpression()) return false;
		  String subject = StringConstant;

			c = ExecutingLineBuffer.charAt(LineIndex);										
			if ( c != ',') return false;
			++LineIndex;

		  if (!evalStringExpression()) return false;
		  String body = StringConstant;
		  
		  Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
		  intent.setType("text/plain");
		  intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		  intent.putExtra(Intent.EXTRA_TEXT, body);
		  intent.setData(Uri.parse(recipiant)); // or just "mailto:" for blank
		  intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
		  try {
			  startActivityForResult(intent, BASIC_GENERAL_INTENT);
		  }catch (Exception e){
			  return RunTimeError(e);
		  }
		  
		  return true;
	  }
	  
	  // *********************************** html ********************************************
	  
		public boolean executehtml(){
			
			if (htmlOpening){
				while (Web.aWebView == null) Thread.yield();
				htmlOpening = false;
			}
			
			
			if (!GethtmlKeyWord()){ return false;}
			
			if (KeyWordValue == html_open ||            // Allow open and get.datalink if not opened
			    KeyWordValue == html_get_datalink){}
			    else
			
			    	if (htmlIntent == null ||
			    		Web.aWebView == null)
			    	{
			    		if (KeyWordValue != html_close) {      // If already closed and user asking
			    			RunTimeError("html not opened");   // to close, then don't give an error
			    			return false;
			    		}
			    		else return true;
			    	}

			switch (KeyWordValue){
			case html_open:
				if (!execute_html_open()) return false;
				break;
			case html_load_url:
				if (!execute_html_load_url()) return false;
				break;
			case html_load_string:
				if (!execute_html_load_string()) return false;
				break;
			case html_get_datalink:
				if (!execute_html_get_datalink()) return false;
				break;
			case html_close:
				if (!execute_html_close()) return false;
				break;
			case html_go_back:
				if (!execute_html_go_back()) return false;
				break;
			case html_go_forward:
				if (!execute_html_go_forward()) return false;
				break;
			case html_clear_cache:
				if (!execute_html_clear_cache()) return false;
				break;
			case html_clear_history:
				if (!execute_html_clear_history()) return false;
				break;
			case html_post:
				if (!execute_html_post()) return false;
				break;
			default:
			}
			return true;
		}
		
		  private  boolean GethtmlKeyWord(){						// Get a Basic key word if it is there
				// is the current line index at a key word?
			  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
			  int i = 0;
			  for (i = 0; i<html_KW.length; ++i){		// loop through the key word list
				  if (Temp.startsWith(html_KW[i])){    // if there is a match
					  KeyWordValue = i;						// set the key word number
					  LineIndex = LineIndex + html_KW[i].length(); // move the line index to end of key word
					  return true;							// and report back
				  }
			  }
			  KeyWordValue = 99;							// no key word found
			  return false;									// report fail

		  		}


	  
	  private boolean execute_html_open(){
		  if (Web.aWebView != null) {
			  RunTimeError("HTML previously open and not closed");
			  return false;
		  }
		  
		  ShowStatusBar = 0;
		  if (evalNumericExpression()){
			  ShowStatusBar = EvalNumericExpressionValue.intValue();
		  }
		  if (!checkEOL()) return false;

          htmlIntent= new Intent(this, Web.class);         // Intent variable used to tell if opened
          Web.aWebView = null;							   // Will be set int Web.java
          htmlData_Buffer = new ArrayList<String>();	   // Initialize the datalink buffer
          PrintShow("@@COP");									// Start Web View in UI thread.
          htmlOpening = true;
          
          return true;
	  }
	  
      private boolean execute_html_load_url(){              // Load an internet url
          if (!evalStringExpression()) return false;

          String urlString = StringConstant;
          String protocolName = urlString.substring(0,4);
          if (!protocolName.equals("http") && !protocolName.equals("java") && !protocolName.equals("file")) {
              if (Basic.isAPK) {                              // if not standard BASIC! then is user APK
                  urlString = "file:///android_asset/" + urlString;       // try to load the file from the assets resource
              } else {
                  urlString = "file://" + Basic.getDataPath(urlString);   // try to load the file from the rfo-bsaic/data folder
              }
          }
//        Web.aWebView.webLoadUrl(urlString);
          PrintShow("@@CLU" + urlString);
          return true;
      }
	  
	  private boolean execute_html_load_string(){			// Load an html string
		  if (!evalStringExpression()) return false;
//		  Web.aWebView.webLoadString(StringConstant);
		  PrintShow("@@CLS" + StringConstant);
		  return true;
	  }
	  
	  private boolean execute_html_get_datalink(){			// Gets a data sring from datalink queue
		  
		  if (!getSVar()) return false;						// The string return variable
		  String data = "";
		  
		  if (htmlData_Buffer != null)
			  if (htmlData_Buffer.size()>0 ) {					// If the buffer is not empty
				  data = htmlData_Buffer.get(0);				// get the oldest entry
				  htmlData_Buffer.remove(0);					// and remove it from the buffer
			  }
		  
	      StringVarValues.set(theValueIndex, data);			// return the data to the user
	      
	      if (Web.aWebView == null) return true;			// if already closed, return now
	      													// else check to see if we should close
	      
//	      if (data.startsWith("FOR:")) return execute_html_close();   // if Form, close the html
	      if (data.startsWith("ERR:")) return execute_html_close();   // if error, close the html
		  return true;
	  }
	  
	  private boolean execute_html_go_back(){
//		  Web.aWebView.goBack();
		  PrintShow("@@CGB");
		  return true;
	  }
	  
	  private boolean execute_html_go_forward(){
//		  Web.aWebView.goForward();
		  PrintShow("@@CGF");
		  return true;
	  }
	  
	  private boolean execute_html_clear_cache(){
//		  Web.aWebView.clearCache();
		  PrintShow("@@CCC");
		  return true;
	  }
	  
	  private boolean execute_html_clear_history(){
//		  Web.aWebView.clearHistory();
		  PrintShow("@@CCH");
		  return true;
	  }
	  
	  private boolean execute_html_close(){							// Close to the html
		  if (Web.aWebView != null) Web.aWebView.webClose();        // if it is open
		  while (Web.aWebView != null) Thread.yield();              // wait for the close signal
		  htmlIntent = null;										// Indicate not open
		  return true;
	  }
	  
	  private boolean execute_html_post(){
		  if (!evalStringExpression()) return false;
		  String url = StringConstant;
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		  ++LineIndex;
		  
		   if (!evalNumericExpression())return false;
		   int theListIndex = EvalNumericExpressionValue.intValue();
		   if (theListIndex <1 || theListIndex>= theLists.size()){
			   RunTimeError("Invalid list pointer");
			   return false;
		   }
		   
		   if (!checkEOL()) return false;

		   if (theListsType.get(theListIndex) == list_is_numeric){
			   RunTimeError("List must be of string type.");
			   return false;
		   }
		   
		   List<String> thisList = theLists.get(theListIndex);
		   
		   int r = thisList.size() % 2;
		   if (r !=0 ) {
			   RunTimeError("List must have even number of elements");
			   return false;
		   }
		   
		   htmlPostString = "";
	       for (int i = 0; i <thisList.size(); ++i){
	    	   htmlPostString = htmlPostString + thisList.get(i) + "=";
	    	   ++i;
	    	   htmlPostString = htmlPostString + thisList.get(i) + "&";
	       }
	       htmlPostString.substring(0, htmlPostString.length()-2);
	       		   
	       PrintShow("@@CPS" + url);
		   

		  
		  return true;

	  }

	//************************************** Run Command ************************************

	private boolean executeRUN(){

		if (Basic.isAPK) { return RunTimeError("Run not permitted in a compiled apk"); }

		if (!getStringArg()) { return false; }								// get program filename
		String fileName = StringConstant;

		String data = "";
		if (isNext(',')) {													// optional
			if (!getStringArg()) { return false; }							// parameter to pass to program
			data = StringConstant;
		}
		if (!checkEOL()) { return false; }

		String fn = Basic.getSourcePath(fileName);
		File file = new File(fn);
		if (!file.exists()) {												// error if the program file does not exist
			return RunTimeError(fileName + " not found");
		}

		Bundle bb = new Bundle();
		bb.putString("fn", fn);
		bb.putString("data", data);

		Intent intent = new Intent(this, AutoRun.class);
		intent.putExtras(bb);

		Basic.DoAutoRun = false;
		startActivity(intent);
		finish();

		return true;
	}

	  //**************************************** Empty Program Command *********************************
	  
	  private boolean executeEMPTY_PROGRAM(){
		  Show("Nothing to execute.");
		  Stop = true;
		  return true;
	  }
	  
	  // *************************************** Notify Command ***********************************
	  
	  private boolean executeNOTIFY(){
		  
		  int NOTIFICATION_ID = 1;    // These two constants are without meaning in this application
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
		double wait = EvalNumericExpressionValue;

		  Notified = false;
		  
		  NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		  Notification notification = new Notification(R.drawable.icon, msg, System.currentTimeMillis());

		  // The PendingIntent will launch activity if the user selects this notification
		  PendingIntent contentIntent = PendingIntent.getActivity(this, REQUEST_CODE, new Intent(this, HandleNotify.class), 0);

		  notification.setLatestEventInfo(this, title, subtitle, contentIntent);
		  notification.flags = Notification.FLAG_AUTO_CANCEL;
		  
		  manager.notify(NOTIFICATION_ID, notification);
		  
		  if (wait != 0) 
			  while (!Notified) Thread.yield();
		  
		  return true;
	  }
	  
	  // ***********************************  SWAP Command *********************************************
	  
	  private boolean executeSWAP(){
		  
		  if (getNVar()) {
			  int aIndex = theValueIndex;
			  double aValue = NumericVarValues.get(aIndex);
			  
			  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
			  ++LineIndex;
			  
			  if (!getNVar()) return false;
			  int bIndex = theValueIndex;
			  double bValue = NumericVarValues.get(bIndex);
			  
			  NumericVarValues.set(aIndex, bValue);
			  NumericVarValues.set(bIndex, aValue);
			  
			  return true;
			  
		  }
		  else if (!getSVar()) return false;
		  
		  int aIndex = theValueIndex;
		  String aValue = StringVarValues.get(aIndex);
		  
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		  ++LineIndex;
		  
		  if (!getSVar()) return false;
		  int bIndex = theValueIndex;
		  String bValue = StringVarValues.get(bIndex);
		  
		  StringVarValues.set(aIndex, bValue);
		  StringVarValues.set(bIndex, aValue);
		  
		  
		  return true;
	  }
	  
	  // ****************************** Speach to text Command ******************************
	  
	  private boolean executeSTT_LISTEN(){
		  
	        PackageManager pm = getPackageManager();
	        List<ResolveInfo> activities = pm.queryIntentActivities(
	                new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
	        if (activities.size() == 0) {
	        	RunTimeError("Recognizer not present");
	        	return false;
	        }
	        
	        sttListening = true;
	        sttDone = false;
//	        Show("@@D");
	        if (GRopen) GR.doSTT = true;
	        else startVoiceRecognitionActivity();
	        return true;
	  }
	        
	  private boolean executeSTT_RESULTS(){
		  if (!sttListening) {
			  RunTimeError("STT_LISTEN not executed.");
			  return false;
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
			
			if (!getNVar()) return false;
			NumericVarValues.set(theValueIndex, (double) theIndex);   		// Return the list pointer    

		  return true;
	  }
	  
	    private void startVoiceRecognitionActivity() {
	        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
	        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
	                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
	        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "BASIC! Speech To Text");
	        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	    }
	    
	  // ******************************* Timer commands ***************************************
	    
	private boolean executeTIMER(){											// Get Timer command key word if it is there
		return executeCommand(Timer_cmd, "Timer");
	}

	   private boolean executeTIMER_SET(){
		   if (theTimer != null) {
			   RunTimeError("Previous Timer Not Cleared");
			   return false;
		   }
		   
		   if (OnTimerLine == 0) {
			   RunTimeError("No OnTimer: Label");
			   return false;
		   }
		   
		   
		   if (!evalNumericExpression()) return false;
		   long interval = EvalNumericExpressionValue.intValue();
		   if (interval < 100) {
			   RunTimeError("Interval Must Be >= 100");
			   return false;
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


	    
	   private boolean executeTIMER_CLEAR(){
		   if (!checkEOL()) return false;
		   
		   if (theTimer != null) {
			   theTimer.cancel();
			   theTimer = null;
		   }
	    	return true;
	   }
	   
	   private boolean executeTIMER_RESUME(){
		   if (interruptResume == -1) {
			   RunTimeError("No timer interrupt to reseume");
			   return false; 
		   }
	       return doResume();
	    }
	   
	   // ***************************** Home Command **************************************
	   
	   private boolean executeHOME(){
		   moveTaskToBack(true);
		   return true;
	   }

	   private boolean executeBACKGROUND_RESUME() {
		   if (interruptResume == -1) {
			   RunTimeError("No background state change");
			   return false;
		   }
		   return doResume();
	   }

} // End of Run
