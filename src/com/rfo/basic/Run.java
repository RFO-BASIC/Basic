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
    
    Apache Commons Net
    Copyright 2001-2011 The Apache Software Foundation

    This product includes software developed by
    The Apache Software Foundation (http://www.apache.org/).
    
    Contains contributions from Marc Sammartano.
    
	*************************************************************************************************/




package com.rfo.basic;

//Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " Line Buffer  " + ExecutingLineBuffer);

import android.util.Log;
import java.util.*;

import com.rfo.basic.*;

import static com.rfo.basic.Basic.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
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
import java.io.UnsupportedEncodingException;
import	java.io.BufferedReader;
import 	java.lang.Thread;
import 	java.lang.reflect.Array;
import java.lang.String;
import 	java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Stack;
import java.util.UUID;
import java.lang.Math;
import java.lang.Double;
import java.lang.Character;
import java.lang.String;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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
import android.view.View.OnKeyListener;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
//import android.content.ClipData;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
//import android.content.ClipboardManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.SystemClock;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Images.Media;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.text.format.Time;
import android.util.Log;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import 	android.hardware.SensorManager;
import 	android.text.ClipboardManager;
import android.text.Editable;
import android.util.Base64;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import 	android.database.sqlite.SQLiteException;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnUtteranceCompletedListener;
import android.text.format.Time;
import android.util.Log;

import org.apache.commons.net.ftp.*;
import android.widget.*;

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
    	"encrypt", "decrypt", "split",
    	"undim", "byte.open", "byte.close",
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
    	"debug.", "console.save",
    	"tts.init","tts.speak","tts.local",
    	"ftp.","console.front","bt.",
    	"call", "su.", "console.line.new",
    	"console.line.char", "tget",
    	"f_n.break", "w_r.break", "d_u.break",
    	"text.position.get", "text.position.set",
    	"byte.position.get", "byte.position.set",
    	"byte.read.buffer", "byte.write.buffer",
    	"soundpool.", "myphonenumber", "headset",
    	"sms.send", "phone.call", "email.send",
    	"html.", "run", "@@@", "back.resume",
    	"notify", "swap", "sms.rcv.init",
    	"sms.rcv.next", "stt.listen", "stt.results",
    	"timer.set", "timer.clear", "timer.resume",
    	"time", "key.resume", "menukey.resume",
    	"onmenukey","ontimer", "onkeypress",			// For Format
    	"ontouch", "onbtreadready",						// For Format
    	"home", "background.resume","onbackground",
    	"phone.rcv.init", "phone.rcv.next",
    	"read.data", "read.next", "read.from"
    	
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
    public static final int BKWmull1 = 32;             // Time moved to after Timer
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
    public static final int BKWsplit =49;
    public static final int BKWundim =50;
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
    public static final int BKWconsole_dump = 87;
    public static final int BKWtts_init = 88;
    public static final int BKWtts_speak = 89;
    public static final int BKWtts_local = 90;
    public static final int BKWftp = 91;
    public static final int BKWconsole_front = 92;
    public static final int BKWbt = 93;
    public static final int BKWcall = 94;
    public static final int BKWsu = 95;
    public static final int BKWconsole_line_new = 96;
    public static final int BKWconsole_line_char = 97;
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
    public static final int BKWtimer_set = 124;
    public static final int BKWtimer_clear = 125;
    public static final int BKWtimer_resume = 126;
    public static final int BKWtime = 127;
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
    	"floor(", "mod(","log(",
    	"round(", "toradians(", "todegrees(",
    	" ", "exp(",										// EQUALS function deleted
    	"is_in(", "clock(", 
    	"bor(", "band(", "bxor(",
    	"gr_collision(",
    	"ascii(", "starts_with(", "ends_with(",
    	"hex(", "oct(", "bin(", "shift(",
    	"randomize(", "background(",
    	"atan(", "cbrt(", "cosh(", "hypot(",
    	"sinh(", "pow(", "log10("
    	
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
    public static final int MFnull1 = 18;
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
/*
 * The values of the constants that follow must be in the same sequence as
 * the math functions.     
 */
    public static final int FSIN = 22;
    public static final int FCOS = 23;
    public static final int FTAN = 24;
    public static final int FSQR = 25;
    public static final int FABS = 26;
    public static final int FRND = 27;

    public static final int FVAL = 28;
    public static final int FLEN = 29;
    
	public static final int FACOS = 30;
    public static final int FASIN = 31;
    public static final int FATAN  = 32;
    public static final int FCEIL = 33;
    
    public static final int FFLOOR = 34;
    public static final int FMOD = 35;
    public static final int FLOG = 36;
    public static final int FROUND = 37;
    
    public static final int FTORADIANS = 38;
    public static final int FTODEGREES = 39;
    public static final int FENULL1 = 40;
    public static final int FEXP = 41;
    public static final int FISIN = 42;
    public static final int FCLOCK = 43;
    public static final int FBOR = 44;
    public static final int FBAND = 45;
    public static final int FBXOR = 46;
    
    public static final int FakeEOL = 47;
   	
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
    	"bin$(", "geterror$("
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
    
    // *****************************   Various execution control variables *********************
    
    public static final int BASIC_GENERAL_INTENT = 255;
    public static Random randomizer;
    public static boolean background = false;
    public static String errorMsg;
    public static boolean kbShown = false;
    
    public static int OnErrorLine =0 ;              // Line number for OnError: label
    public static int OnBackKeyLine=0;
    public static boolean BackKeyHit = false;
    public static int BackKeyResume = -1 ;
    public static int OnMenuKeyLine = 0;
    public static boolean MenuKeyHit = false;
    public static int OnMenuKeyResume = -1;
    public static boolean bgStateChange = false;
    public static int OnBGResume = -1;
    public static int OnBGLine = 0;
 
	
    public static int LineIndex = 0;				// Current displacement into ExecutingLineBuffer
    public static String ExecutingLineBuffer ="";		// Holds the current line being executed
    public static int ExecutingLineIndex = 0;		// Points to the current line in Basic.lines
    public static boolean SEisLE = false;				// If a String expression result is a logical expression

    public static Stack<Integer> GosubStack;			// Stack used for Gosub/Return
    public static Stack<Bundle> ForNextStack;			// Stack used for For/Next
    public static Stack<Integer> WhileStack;			// Stack used for While/Repeat
    public static Stack<Integer> DoStack;				// Stack used for Do/Until
    public static Bundle s= new Bundle();				// A generic bundle
    
    public static Stack <Integer> IfElseStack;			// Stack for IF-ELSE-ENDIF operations
    public static final int IEskip1 = 1;			     // Skip statements until ELSE, ELSEIF or ENDIF
    public static final int IEskip2 = 2;				// Skip to until ENDIF
    public static final int IEexec = 3;			        // Execute to ELSE, ELSEIF or ENDIF
   
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
	
	
	// debugger dialog and ui thread vars
	  public boolean WaitForResume = false ; 
	  public boolean DebuggerStep = false;
  	public boolean DebuggerHalt = false;
		public boolean WaitForSwap = false;
		public boolean WaitForSelect = false;
		public boolean dbSwap = false;
		public boolean dbSelect = false;
	  public AlertDialog.Builder DebugDialog;
	  public AlertDialog theDebugDialog;
		public AlertDialog.Builder DebugSwapDialog;
		public AlertDialog theDebugSwapDialog;
		public AlertDialog.Builder DebugSelectDialog;
	  public AlertDialog theDebugSelectDialog;
  	public boolean dbDialogScalars;
	  public boolean dbDialogArray;
	  public boolean dbDialogList;
	  public boolean dbDialogStack;
	  public boolean dbDialogBundle;
	  public boolean dbDialogWatch;
	  public boolean dbDialogProgram;
	  public boolean dbDialogConsole;
	  public String dbConsoleHistory;
  	public String dbConsoleExecute;
	  public int dbConsoleELBI;
  	public static ArrayList <Integer> WatchVarIndex;
  	public static ArrayList <String> Watch_VarNames;
	  public static int WatchedArray;
	  public static int WatchedList;
	  public static int WatchedStack;
	  public static int WatchedBundle;
	// end debugger ui vars
    
    public static boolean GrStop = false;						// Signal from GR that it has died
    public static boolean Stop = false;	   						// Stops program from running
    public static boolean GraphicsPaused = false;               // Signal from GR that it has been paused
    public static boolean RunPaused = false;                    // Used to control the media player
    public static boolean StopDisplay = false;
    public static boolean DisplayStopped = false;
    public static String PrintLine = "";				// Hold the Print line currently being built
    public static String textPrintLine = "";			// Hold the TextPrint line currently being built
    public static boolean textPrintLineReady = false;   // Signals a text print line is ready to write to file
    public static String btPrintLine = "";				// Hold the Blue Tooth line currently being built
    public static boolean btPrintLineReady = false;    // Signals a blue tooth print line is ready to write to file
    
    public static InputMethodManager IMM;
	public static ArrayList<String> LabelNames;         // A list of all the label names found in the program
	public static ArrayList<Integer> LabelValues;       // The line numbers associated with Label Names

    
    public static ArrayList<String> VarNames ;			// Each entry has the variable name string
    public static ArrayList<Integer> VarIndex;			// Each entry is an index into
    													// NumberVarValues or
    													// StringVarValues or
    													// ArrayTable or
    													// FunctionTable
    public static int VarNumber = 0;				// An index for both VarNames and NVarValue
    public static ArrayList<Double> NumericVarValues;   // if a Var is a number, the VarIndex is an
    													// index into this list. The values of numeric
    													// array elements are also kept here
    public static ArrayList<String> StringVarValues;    // if a Var is a string, the VarIndex is an
    													// index into this list. The values of string
    													// array elements are also kept here
    public static ArrayList<Bundle> ArrayTable;			//Each DIMed array has an entry in this table
    public static String StringConstant = "";			// Storage for a string constant
    public static int theValueIndex;				// The index into the value table for the current var
	public static int ArrayValueStart = 0;				// Value index for newly created array 
	
	public static boolean VarIsFunction = false;		// Flag set by getVar() when var is a user function
	public static boolean DoingDef = false;
	public static ArrayList<Bundle> FunctionTable;      // A bundle is created for each defined function
	public static Bundle ufBundle;						// Bundle set by isUserFunction and used by doUserFunction
    public static Stack<Bundle> FunctionStack;			// Stack contains the currently executing functions
    public static int VarSearchStart;					// Used to limit search for var names to executing function vars
    public static boolean fnRTN = false;				// Set true by fn.rtn. Cause RunLoop() to return
    public static int scOpValue;						// An instance variable that needs to be saved when executing function
												

    public static boolean doingDim = false;				// Signal to get Var that un dimed array var is ok
    public static boolean unDiming = false;             // Signal to get Var that an array is being undimed
    public static boolean SkipArrayValues = false;      // Set true for some array.xxx commands
    public static boolean FindingLabels = false;		// Signal to get var to report var is label if it is
    public static boolean VarIsNew = true;				// Signal from get var that this var is new
    public static boolean VarIsNumeric = true;			// if false, Var is string
    public static boolean VarIsArray = false;			// if true, Var is an Array
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
    
    public static final String SQL_kw[] = {				// SQL Commands
    	"open", "close", "insert", "query",
    	"next", "delete", "update", "exec",
    	"raw_query", "drop_table", "new_table"
    };
    
    public static final int sql_open = 0;				// SQL Command enums
    public static final int sql_close = 1;				
    public static final int sql_insert = 2;
    public static final int sql_query = 3;
    public static final int sql_next = 4;
    public static final int sql_delete = 5;
    public static final int sql_update = 6;
    public static final int sql_exec = 7;
    public static final int sql_raw_query =8;
    public static final int sql_drop_table =9;
    public static final int sql_new_table =10;
    public static final int sql_none = 98;
    
	public static boolean SQL_return = true;
    public static ArrayList<SQLiteDatabase> DataBases; 	 // List of created data bases
    public static ArrayList<Cursor> Cursors; 	 		 // List of created data bases
    
    // ******************************** Variables for the INKEY$ command ***********************
    													
    public static boolean KeyPressed = false;
    public static final String Numbers = "0123456789";    // translations for key codes
    public static final String Chars = "abcdefghijklmnopqrstuvwxyz";
    public static String Buffer = "0123456789";
    public static ArrayList<String> InChar ;
    public static int OnKeyLine;
    public static int OnKeyResume;
    
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
    public static int OnTouchResume;
    public static Canvas drawintoCanvas = null;

    
    public static final String GR_kw[] = {
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
    	 "get.textbounds", "text.typeface", "ontouch.resume",
    	 "camera.select"
    
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
    
    public static boolean GetSensorList = false;
    public static ArrayList<String> SensorCensus;
    public static ArrayList<Integer> SensorOpenList;
    public static double SensorValues[][];
    public static Intent SensorsClass;
    public static boolean SensorsStop;
    public static boolean SensorsRunning;
    public static final int MaxSensors = 15;
    
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
    
    public static Intent theGPS;
	public static boolean GPSoff = true;
	public static boolean GPSrunning = false;
	
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
		"client.connect", "client.read.line",
		"client.write.line", "server.create",
		"server.connect",
		"server.write.line", "server.read.line",
		"server.close", "client.close",
		"myip", "client.read.ready",
		"server.read.ready", "server.disconnect",
		"server.client.ip", "client.server.ip",
		"server.write.file", "client.read.file",
		"server.write.bytes", "client.write.bytes",
		"client.write.file"
	};
	
	public static final int client_connect = 0;
	public static final int client_read_line = 1;
	public static final int client_write_line = 2;
	public static final int server_create = 3;
	public static final int server_accept = 4;
	public static final int server_write_line = 5;
	public static final int server_read_line = 6;
	public static final int server_close = 7;
	public static final int client_close = 8;
	public static final int myip = 9;
	public static final int client_read_ready = 10;
	public static final int server_read_ready = 11;
	public static final int server_disconnect = 12;
	public static final int server_client_ip = 13;
	public static final int client_server_ip = 14;
	public static final int server_putfile = 15;
	public static final int client_getfile = 16;
	public static final int server_putbytes = 17;
	public static final int client_putbytes = 18;
	public static final int client_putfile= 19;
	
	public static Socket theClientSocket ;
	public static BufferedReader ClientBufferedReader ;
	public static PrintWriter ClientPrintWriter ;
	
	public static ServerSocket newSS;
	public static Socket theServerSocket ;
	public static BufferedReader ServerBufferedReader ;
	public static PrintWriter ServerPrintWriter ;
	
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
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

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
    public static boolean btCSrunning = false;
    public static boolean btReadReady = false;
    public static int OnBTReadResume = -1;
    public static int OnBTReadLine = 0;
    
    public static Looper btLooper;

    public static final String bt_KW[] = {				// Bluetooth Commands
    	"open", "close", "status", 
    	"connect", "device.name",
    	"write", "read.ready", "read.bytes",
    	"set.uuid", "listen", "reconnect",
    	"onreadready.resume"
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
    
/**************************************  Superuser **********************************************/

    public static final String su_KW[] = {
    	"open", "write", "read.ready",
    	"read.line", "close"
    	
    };
    
    public static final int su_open = 0;
    public static final int su_write = 1;
    public static final int su_read_ready = 2;
    public static final int su_read_line = 3;
    public static final int su_close = 4;
    
    
    DataOutputStream SUoutputStream;
    DataInputStream SUinputStream;
    Process SUprocess;
    public ArrayList <String> SU_ReadBuffer;
    SUReader theSUReader = null;
    
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
    
    public static int OnTimerLine;
    public static int timerResumeLine;
    public static Timer theTimer;
    public static boolean timerExpired;
    
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

/****************************** Semaphore to count pending progress updates ********************
 * Note: This code and the code used to make console.save work properly was supplied by the
 * BASIC! forum user, jMarc.
 **********************************************/

    private static class SynchronizedCounter {
        private int counter = 0;
        public synchronized void reset() { counter = 0; notify(); }
        public synchronized int inc() { return ++counter; }
        public synchronized int dec() { if (counter > 0) { if (--counter == 0) notify(); } return counter; }
        public synchronized int await(long timeout) {
            while (counter != 0) {
                // Log.d(LOGTAG, "SynchronizedCounter " + counter + ", waiting...");
                try { wait(timeout); }
                catch (InterruptedException e) { reset(); Log.e(LOGTAG, "SynchronizedCounter exception!", e); }
            }
            return counter;
        }
    }
    // Counter used to keep the background task from reading the console
    // before the UI (foreground) task finishes writing it.
    // Incremented in Background by publishProgress.
    // Decremented in UI by onProgressUpdate.
    private SynchronizedCounter ProgressUpdateCount;
	
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
    												


public class Background extends AsyncTask<String, String, String>{
	
// The execution of the basic program is done by this Async Background task. This is done to keep the UI task
// responsive. This method controls Run as it is running in the background.
    	
        @Override
        protected  String doInBackground(String...str ) {
        	
        	boolean flag = true;
//    		Basic.Echo = Settings.getEcho(Basic.BasicContext);
        	Echo = false;
        	VarSearchStart = 0;
    		fnRTN = false;
    		ProgressUpdateCount.reset();	// No progress updates pending yet!
    		setVolumeControlStream(AudioManager.STREAM_MUSIC);

        	if (PreScan()) { 				// The execution starts by scanning the source for labels and read.data
        		
        	ExecutingLineIndex = 0;			// Just in case PreScan ever changes it
        	flag = RunLoop();
        	       	
        	Stop = true;		// If Stop is not already set, set it so that menu code can display the right thing
        	PrintLine = "";     // Clear the Print Line buffer
        	textPrintLine = "";
        	textPrintLineReady = false;
        	btPrintLine = "";
        	btPrintLineReady = false;
        	
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
         	publishProgress("@@6");
         	cleanUp();
  		    cancel(true);		// Done with program. Cancel the background task
        	}
        	
        	else{              // We get here if PreScan found error or duplicate label
        		for (int i=0; i<TempOutputIndex; ++i){				// if new output lines, the send them
        			publishProgress(TempOutput[i]);					// to UI task
        			}
        		TempOutputIndex = 0;
        	}
        	return "true";
        }
        
// The RunLoop() drives the execution of the program. It is called from doInBackground and
// from recursively from doUserFunction.
        
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
        		}
        		
        		if (BackKeyHit ) {
        			BackKeyHit = false;
        			if (OnBackKeyLine != 0) {
            			if (BackKeyResume == -1) {
            				BackKeyResume = ExecutingLineIndex;
            				ExecutingLineIndex = OnBackKeyLine;
            			}
        			}
        		}

        		
        		if (MenuKeyHit ) {
        			MenuKeyHit = false;
        			if (OnMenuKeyLine != 0) {
            			if (OnMenuKeyResume == -1) {
            				OnMenuKeyResume = ExecutingLineIndex;
            				ExecutingLineIndex = OnMenuKeyLine;
            			}
        			}
        		}


        		if (timerExpired ) {
        			timerExpired = false;
        			if (OnTimerLine != 0) {
            			if (timerResumeLine == -1) {
            				timerResumeLine = ExecutingLineIndex;
            				ExecutingLineIndex = OnTimerLine;
            			}
        			}
        		}
        		
        		if (KeyPressed ) {
        			KeyPressed = false;
        			if (OnKeyLine != 0) {
            			if (OnKeyResume == -1) {
            				OnKeyResume = ExecutingLineIndex;
            				ExecutingLineIndex = OnKeyLine;
            			}
        			}
        		}

        		if (NewTouch[2] ) {
        			NewTouch[2] = false;
        			if (OnTouchLine != 0) {
            			if (OnTouchResume == -1) {
            				OnTouchResume = ExecutingLineIndex;
            				ExecutingLineIndex = OnTouchLine;
            			}
        			}
        		}
        		
        		if (btReadReady) {
        			btReadReady = false;
        			if (OnBTReadLine != 0) {
            			if (OnBTReadResume == -1) {
            				OnBTReadResume = ExecutingLineIndex;
            				ExecutingLineIndex = OnBTReadLine;
            			}
        			}
        		}
        		

        		if (bgStateChange) {
        			bgStateChange = false;
//        			Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " Looper  " + "KBS=" + kbShown + " BG=" + background);
        			if (!background) {
        				if (kbShown) 
            				if (GRFront) {
            					GR.GraphicsImm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            				}else {
            					IMM.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            				}
        			}
        			if (OnBGLine != 0) {
            			if (OnBGResume == -1) {
            				OnBGResume = ExecutingLineIndex;
            				ExecutingLineIndex = OnBGLine;
            			}
        			}
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
                	try {Thread.sleep(500);}catch(InterruptedException e){};
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

        // AsyncTask.publishProgess is final, no override allowed. Instead, we overload it here
        // so it can increment the pending update counter that is decremented by onProgressUpdate.
        private void publishProgress(String message) {
            ProgressUpdateCount.inc();									// increment count of pending progress updates
            super.publishProgress(message);
        }

// The following method is in the UI (foreground) Task part of Run. It gets control when the background
// task sends it a message in the form of a string. Most of the strings are just text for the output
// screen. A few are signals. Signals start with "@@"
        
        @Override    
        protected void onProgressUpdate(String... str ) {
        	
        	Context context = getApplicationContext();

        	for (int i=0; i<str.length; ++i){							// Check for signals
        		if (str[i].startsWith("@@1")){      					// Input dialog signal
        			doInputDialog();
        			if (InputDialog != null)
        			theAlertDialog = InputDialog.show();				// show the Input dialog box
        			}
        		
        		else if (str[i].startsWith("@@2")){						// Invalid Input Dialog Input
        			  CharSequence text = "Not a number. Try Again.";
        			  int duration = Toast.LENGTH_SHORT;

        			  Toast toast = Toast.makeText(context, text, duration);
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
    			  Toast toast = Toast.makeText(context, text, ToastDuration);
    			  toast.setGravity(Gravity.CENTER|Gravity.CENTER, ToastX, ToastY);
    			  toast.show();
        		
        		}else if (str[i].startsWith("@@5")){            // Clear Screen Signal
        			output.clear();
        		}else if (str[i].startsWith("@@6")){            // Done with background task
        			cancel(true);
        		}else if (str[i].startsWith("@@7")){
    	            if (mChatService == null){
    	            	mChatService = new BluetoothChatService(context, mHandler);
    	            	mChatService.start(bt_Secure);
    	            	btCSrunning = true;
    	            }

        		}else if (str[i].startsWith("@@8")){
    		    	Intent serverIntent = null;
    	            serverIntent = new Intent(context, DeviceListActivity.class);
    	            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
    	            
//        		}else if (str[i].startsWith("@@9")){
//        			theSUReader = new SUReader(context, SUinputStream);
//        			theSUReader.start();
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
            }else if (str[i].startsWith("@@E")){      					// Input dialog signal
        			doDebugDialog();
        		}else if (str[i].startsWith("@@F")){						// Debug step completed
        			DebuggerStep = false;
          	  doDebugDialog();
        		}else if (str[i].startsWith("@@G")){					// User canceled dialog with back key or halt
        			output.add("Execution halted");
        		}else if (str[i].startsWith("@@H")){					// Swap dialog called
							doDebugSwapDialog();
						}else if (str[i].startsWith("@@I")){					// Select dialog called
							doDebugSelectDialog();
						}else if (str[i].startsWith("@@J")){					// Alert dialog var not set called
						
        		}else {output.add(str[i]);};			// Not a signal, just write msg to screen.

        		ProgressUpdateCount.dec();				// decrement count of pending progress updates
    	    	setListAdapter(AA);						// show the output
    	    	lv.setSelection(output.size()-1);		// set last line as the selected line to scroll
//    	    	Log.d(LOGTAG, "onProgressUpdate: print <" + str[i] + ">");
        		}
        	
	    	
        }
        
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
        


} // End of Background class

// *********************Run Entry Point. Called from Editor or AutoRun *******************


@Override
public void onCreate(Bundle savedInstanceState) {
	
	super.onCreate(savedInstanceState);
	Log.v(Run.LOGTAG, " " + Run.CLASSTAG + " On Create  " + ExecutingLineIndex );
	
	if (Basic.lines == null){
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

	ProgressUpdateCount = new SynchronizedCounter();		// Count of pending progress updates

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
	theBackground = new Background();						// Start the background task
	theBackground.execute("");
	
	}

private void InitVars(){
	
		
    OnErrorLine =0 ;              // Line number for OnError: label
    OnBackKeyLine=0;
    BackKeyHit = false;
    BackKeyResume = -1 ;
    OnMenuKeyLine = 0;
    MenuKeyHit = false;
    OnMenuKeyResume = -1;
    timerResumeLine = -1 ;  
    bgStateChange = false;
    OnBGResume = -1;
    OnBGLine = 0;

    errorMsg = "No error";
    
    InChar = new ArrayList<String>();
    KeyPressed = false;
    OnKeyLine = 0;
    OnKeyResume = -1;
	
    LineIndex = 0;				// Current displacement into ExecutingLineBuffer
    ExecutingLineBuffer ="\n";		// Holds the current line being executed
    ExecutingLineIndex = 0;		// Points to the current line in Basic.lines
    SEisLE = false;				// If a String expression result is a logical expression

    GosubStack = new Stack<Integer>();			// Stack used for Gosub/Return
    ForNextStack = new	Stack<Bundle>();		// Stack used for For/Next
    WhileStack = new Stack<Integer>() ;			// Stack used for While/Repeat
    DoStack = new Stack<Integer>();				// Stack used for Do/Until
    s= new Bundle();							// A generic bundle
    
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
   btPrintLine = "";					// Hold the Text Print line currently being built
   btPrintLineReady = false;          // Signals the text print is ready to write to file
    
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
    
	SQL_return = true;
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
    OnTouchResume = -1;
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
    
    GetSensorList = false;
    SensorCensus = new ArrayList<String>() ;
    SensorOpenList = new ArrayList<Integer>() ;
    //SensorValues[][];
    SensorsClass = null;
    SensorsStop = true;
    SensorsRunning = false;
	
    theGPS = null;
	GPSoff = true;
	GPSrunning = false;
	
	DoAverage = false;;
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
	theServerSocket = null ;
	ServerBufferedReader = null ;
	ServerPrintWriter = null ;
	
	TextToSpeech mTts = null;;
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
    OnBTReadResume = -1;
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
     timerResumeLine = -1;
     theTimer = null;
     timerExpired = false;
     
	 phoneState = 0;
	 phoneNumber = "";
	 phoneRcvInited = false;
	 mTM = null;
	 
	 readNext =0;
	 readData = new ArrayList <Bundle>();


}

public void cleanUp(){
	if (theMP != null){
		try {Run.theMP.stop();} catch (IllegalStateException e){};
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
	SensorsClass = null;
	SensorsStop = true;
	
	if (theServerSocket != null){
	try{
		theServerSocket.close();
		}catch (Exception e) {
		}
	theServerSocket = null;
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
	
	execute_audio_record_stop();

	Stop = true;										// make sure the background task stops
	Basic.theRunContext = null;
	GraphicsPaused = false;
	RunPaused = false;
	GPSoff = true;
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

	public boolean onKeyDown(int keyCode, KeyEvent event)  {						// The user hit the back key


		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
    	if (OnBackKeyLine != 0){
    		BackKeyHit = true;
    		return true;
    	}
    	
    	if (Stop == false) Stop = true;
    	else {
    		if (DoAutoRun) android.os.Process.killProcess(android.os.Process.myPid()) ;
    		else finish();
    	}
        return false;
    	}
    	return super.onKeyDown(keyCode, event);
	}
	

	public boolean onKeyUp( int keyCode, KeyEvent event)  {
		
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
   Menu theMenu = menu;
   MenuItem item;
   item = menu.getItem(1);
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

@Override
public  boolean onOptionsItemSelected(MenuItem item) {  // A menu item is selected
   switch (item.getItemId()) {

   case R.id.stop:										// User wants to stop execution
	   Show("Stopped by user.");						// Tell user and then
	   Stop = true;										// signal main loop to stop
	   OnBackKeyLine = 0;
      return true;

   case R.id.editor:									// User pressed Editor
    	if (!DoAutoRun && SyntaxError){
			Editor.SyntaxErrorDisplacement = ExecutingLineIndex;
    	}


	   
	   /*    	if (!DoAutoRun && SyntaxError){
        	if (ExecutingLineIndex>=0 &&
        			ExecutingLineIndex < Editor.lineCharCounts.size()){	// If run ended in error, select error line
      
        		int end = Editor.lineCharCounts.get(ExecutingLineIndex);  // Get selection end
        		int start = end -1;										// back up over the new line
        		for (start = end-1; start >0 ; --start){				// Scan for previous nl or start
        			char c = Editor.DisplayText.charAt(start);
        			if (c == '\n'){
        				start = start + 1;
        				break;
        			}
        		}
         	   Editor.mText.setText(Editor.DisplayText);				// Set the Editor's EditText TextView text
               Editor.mText.setSelection(start, end);							// Set the selection
        	}*/
    	

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
		  try {theMP.pause();} catch (IllegalStateException e){};
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
	  SensorsClass = null;
	  SensorsStop = true;
  	  GPSoff = true;
  	  
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
	if (c == '_' || c == '@' || c == '#' || (c >= 'a' && c <= 'z')) { // if first character matches
		do {														// there's a word
			if (++li >= max) break; 								// done if no more characters
 
			if (stopOnPossibleKeyword &&							// caller wants to stop at keyword
				line.startsWith(PossibleKeyWord, li)) { break; }	// THEN, TO, or STEP

			c = line.charAt(li);									// get next character
		}
		while (c == '_' || c == '@' || c == '#' ||					// and check it, stop if not valid
				(c >= 'a' && c <= 'z') || (c >= '0' && c <= '9'));
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
	if (name.equals("ontouch")) OnTouchLine = LineNumber;
	if (name.equals("onbtreadready")) OnBTReadLine = LineNumber;
	if (name.equals("onbackground")) OnBGLine = LineNumber;

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
			int q;
			if (!IfElseStack.empty()){					// if inside IF-ELSE-ENDIF
				q = IfElseStack.peek();				// decide if we should skip to ELSE or ENDIF
				switch (q){
					case IEskip1:
						if (KeyWordValue == BKWelse || 
								KeyWordValue == BKWelseif ||
								KeyWordValue == BKWif ||
								KeyWordValue == BKWendif){}
						else{KeyWordValue = SKIP;}
						break;
					case IEskip2:
						if (KeyWordValue == BKWendif ||
							KeyWordValue == BKWif ){}
						else {KeyWordValue = SKIP;}
						break;
					case IEexec:
						
					}
				}
			
			if (KeyWordValue != SKIP)
        		if (Echo)
        			{PrintShow(ExecutingLineBuffer.substring(0, ExecutingLineBuffer.length()-1));}

	        switch (KeyWordValue){						// Execute the key word
	        
	        	case BKWrem:
	        		break;
	        	case BKWdim:
	        		if (!executeDIM()){SyntaxError();return false;};
	        		break;
	        	case BKWlet:
	        		if (!executeLET()){SyntaxError();return false;};
	        		break;
	        	case BKWelseif:
	        		if (!executeELSEIF()){SyntaxError();return false;};
	        		break;
	        	case BKWend:
	        	    PrintShow("END");
//	        		OnErrorLine = 0;
//	        		SyntaxError = false;
	        	    Stop = true;
	        		return true;
	        	case BKWprint:
	        		if (!executePRINT(true)){SyntaxError();return false;};
	        		break;
	        	case BKWinput:
	        		if (!executeINPUT()){SyntaxError();return false;};
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
	        		if (getVar()) {
	        			int gln = isLabel(VarNumber);
	        			if (gln <0){
	        				RunTimeError("Undefined Label at:");
	        				return false;
	        			}
	        			if (!checkEOL(true)) return false;
//	        			if (!IfElseStack.empty()) IfElseStack.pop();
	        			ExecutingLineIndex = gln;
	        		}else{
	        			SyntaxError();
	        			return false;
	        		};
	        		break;
	        	case BKWgosub:
	        		if (getVar()) {
	        			int gln = isLabel(VarNumber);
	        			if (gln <0){
	        				RunTimeError("Undefined Label at:");
	        				return false;
	        			}
	        			if (!checkEOL(true)) return false;
	        			GosubStack.push(ExecutingLineIndex);
	        			ExecutingLineIndex = gln;
	        		}else{
	        			SyntaxError();
	        			return false;
	        		};
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
	        		if (!executeSPLIT()){SyntaxError(); return false;}
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
	        	case BKWconsole_dump:
	        		if (!executeCONSOLE_DUMP()){SyntaxError(); return false;}
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
	        	case BKWconsole_front:
	        		if (!executeCONSOLE_FRONT()){SyntaxError(); return false;}
	        		break;
	        	case BKWbt:
	        		if (!executeBT()) {SyntaxError(); return false;}
	        		break;
	        	case BKWcall:
	        		if (!executeCALL()) {SyntaxError(); return false;}
	        		break;
	        	case BKWsu:
	        		if (!executeSU()) {SyntaxError(); return false;}
	        		break;
	        	case BKWconsole_line_new:
	        		if (!executeCONSOLE_LINE_NEW()) {SyntaxError(); return false;}
	        		break;
	        	case BKWconsole_line_char:
	        		if (!executeCONSOLE_LINE_CHAR()) {SyntaxError(); return false;}
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
	        	case BKWtimer_set:
	        		if (!executeTIMER_SET())   {SyntaxError(); return false;}
	        		break;
	        	case BKWtimer_clear:
	        		if (!executeTIMER_CLEAR())   {SyntaxError(); return false;}
	        		break;
	        	case BKWtimer_resume:
	        		if (!executeTIMER_RESUME())   {SyntaxError(); return false;}
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
			try {Thread.sleep(300);}catch(InterruptedException e){};
			lv.performHapticFeedback(2, 1);
			try {Thread.sleep(300);}catch(InterruptedException e){};
			lv.performHapticFeedback(2, 1);
		}

	}
		
   private void RunTimeError(String msg){
	   Show(msg);
	   Show(ExecutingLineBuffer);
	   SyntaxError = true;
	   errorMsg = msg + "\nLine: " + ExecutingLineBuffer;
   }

   private boolean checkEOL(){
	   if (LineIndex >= ExecutingLineBuffer.length()) return true;
	   if (ExecutingLineBuffer.charAt(LineIndex) == '\n') return true;
	   String ec = ExecutingLineBuffer.substring(LineIndex);
	   RunTimeError("Extraneous characters in line: " + ec);
	   return false;
   }

   private boolean checkEOL(boolean increment){
	   if (increment) ++LineIndex;
	   if (LineIndex >= ExecutingLineBuffer.length()) return true;
	   if (ExecutingLineBuffer.charAt(LineIndex) == '\n') return true;
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

   private  boolean GetKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		int i = 0;
		for (i = 0; i<BasicKeyWords.length; ++i){		// loop through the key word list
			 if (ExecutingLineBuffer.startsWith(BasicKeyWords[i], LineIndex)){    // if there is a match
				 KeyWordValue = i;						// set the key word number
				 LineIndex = LineIndex + BasicKeyWords[i].length(); // move the line index to end of key word
				 return true;							// and report back
			 	};
			};
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
        
        double d = 0 ;
        int kk = 0;
        if (!VarIsNumeric){									// if var is string
        	if (!VarIsArray){								// and not an array
        		kk = StringVarValues.size();				// then the value index is the index
        		StringVarValues.add("");					// to the next unused entry in string values		
        		VarIndex.set(VarNumber,kk);					// list
        		theValueIndex = kk;
        		return true;
        	}else{												// New Var is a string array								
            	if (!doingDim){									// If not doing dim then we have a problem
            		RunTimeError("Array must be DIMed before using");
            		LineIndex = LI;
    				VarNames.set(VarNumber, "!");
            		return false;
            	}
//        		VarIndex.add(0);								// Var is new array being DIMed
        		return true;									// Set the var index to zero for now
        	}
        }
        														// New Var is numeric
        if (!VarIsArray){										// Var is not a numeric array
        	kk = NumericVarValues.size();                       // then the value index is the index
        	NumericVarValues.add(d);                            // to the next unused entry in numeric values
        	VarIndex.set(VarNumber, kk);				        // The var index is the next avail Numeric Value
    		theValueIndex = kk;
        	return true;
        }else{													// New Numeric Var is Array
        	if (!doingDim){										// If not doing dim, problem	
        		RunTimeError("Array must be DIMed before using");
        		LineIndex = LI;
				VarNames.set(VarNumber, "!");
        		return false;
        	}
//    		VarIndex.add(0);									// Doing Dim, set var index = 0 for now
        	return true;
        }
		
	}
	
	private  boolean getNumber(){						// Get a number if there is one
														// Attempt to parse out a number

		int i = LineIndex;
		char c = ExecutingLineBuffer.charAt(i);
		if (c<'0' || c>'9'){   // Must start with 0-9
			return false;								// not a number.
			};
		
		while ( (c >= '0' && c <='9') || c=='.' ){ // May have decimal point
				++i;
				if (i >= ExecutingLineBuffer.length()) return false;
				c = ExecutingLineBuffer.charAt(i);

			};
																			// Terminated on non-numeric char
		String num = ExecutingLineBuffer.substring(LineIndex,i);			// isolate the numeric characters
		if (c=='e' || c == 'E'){
			num = num + "E";
			++i;
			c = ExecutingLineBuffer.charAt(i);
			while ( (c >= '0' && c <='9') ){
				num = num + c;
				++i;
				if (i >= ExecutingLineBuffer.length()) return false;
				c = ExecutingLineBuffer.charAt(i);
			};
		}
		LineIndex = i;
		double d = 0;
		try { d = Double.parseDouble(num);}									// have java parse it into a double
		catch (Exception e) {
			RunTimeError("Error: " + e );
			return false;
		};
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
		do {															// Get the rest of the String
			++i;														// copy character until
			if (i >= max) return false;
			c = ExecutingLineBuffer.charAt(i);
			if (c == '\n'){
				--i;
				break;
			}
			if (c == '\r'){
				c = '\n';
			}
			
			boolean flag = false;								// Look for \"
			if (c == '\\') {									// and replace with "
				if (ExecutingLineBuffer.charAt(i+1)== '"'){		// So that user can put quotes in a string
					++i;
					StringConstant = StringConstant+'"';
					flag = true;
				}
			}
			if (c != '"' && !flag){StringConstant = StringConstant+c;}			// if not " then add to String Constant
		}while (i< max && c != '"');			        // terminate by EOL or "
		
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
		
//		if (theValueIndex == null) return false;            // Why? Because it is sometimes null
		
		int AssignToVarNumber = theValueIndex;				// Save the assign to Var Number
		
		if (!isNext('=')) {									// Var must be followed by =
				return false;
		}
		
		if (VarIsNumeric){									// if var is number then 
			if (!evalNumericExpression()){					// evaluate following numeric expression
				return false;
			}
			NumericVarValues.set(AssignToVarNumber, EvalNumericExpressionValue);  //and assign results to the var
			if (!checkEOL())return false;

			return true;									// Done with Numeric assignment
		}
															// Assignment is string expression
		if (!evalStringExpression()){return false;}			// Evaluate the string expression
		if (SEisLE) return false;
		StringVarValues.set(AssignToVarNumber, StringConstant); // Assign result to the string var
		
		if (!checkEOL())return false;
		
//		return checkEOL();

		return true;										// Done with string assignment
		
		
	}	
	
	private  boolean evalNumericExpression(){			// Evaluate a numeric expression

		if (LineIndex >= ExecutingLineBuffer.length() ) return false;
															// Evaluate a Numeric Expression
		if (ExecutingLineBuffer.charAt(LineIndex)=='\n'){   // If eol, there is not expression
			return false;
		};
		if (ExecutingLineBuffer.charAt(LineIndex)==')'){   // If eol, there is not expression
			return false;
		};
		Stack<Double> ValueStack = new Stack<Double>();     // Each call to eval gets it's own stack
		Stack<Integer>OpStack = new Stack<Integer>();		// thus we can recursively call eval
		int SaveIndex = LineIndex;
		
		OpStack.push(SOE);									// Push Start of Expression onto stack
		
		
		if (!ENE(OpStack, ValueStack)){						// Now do the recursive evaluation
			LineIndex = SaveIndex;							// if it fails, back up
			return false;									// and die
		};

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
				if (!evalNumericExpression()){return false;} // if ( then eval expression inside the parens
				theValueStack.push(EvalNumericExpressionValue);
				if (!isNext(')')) {return false;}
		}
		else {
			return false;									// nothing left, fail
			};

		if (LineIndex >= ExecutingLineBuffer.length() ) return false;
		c = ExecutingLineBuffer.charAt(LineIndex);
		
		int k = LineIndex;
		
		if (!PossibleKeyWord.equals("")){
			if (ExecutingLineBuffer.startsWith(PossibleKeyWord, LineIndex)){
				if (!handleOp(EOL,  theOpStack, theValueStack)){return false;}
				return true;
			}
		}
		if (c==','){										// treat a comma as an eol
			if (!handleOp(EOL,  theOpStack, theValueStack)){return false;}
			return true;
		};

		if (c==';'){										// treat a ; as an EOL
			if (!handleOp(EOL,  theOpStack, theValueStack)){return false;}
			return true;
		};

		if (c==']'){										// treat a ] as an EOL
			if (!handleOp(EOL,  theOpStack, theValueStack)){return false;}
			return true;
		};

		k = LineIndex;
		if (!getOp()){return false;};						// If operator does not follow, then fail
		
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
		
		if (!ENE( theOpStack, theValueStack)){				// Recursively call ENE for rest of expression
			return false;
		};
		
		return true;						// Done
	
	}

		private  boolean getOp(){						// Get an expression operator if there is one
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
			 	};
			};
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
					RunTimeError("Error: " + e );
					return false;
				};
				theValueStack.push(d1);							// Push number onto value stack
				break;
			
			case MFascii:
				if (!getStringArg()) { return false; }			// Get and check the string expression
	        	if (StringConstant.equals("")) d1 =256;
	        	else {
	        		d1 = (double)(StringConstant.charAt(0) & 0x00FF);
	        	}
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
					d1 = EvalNumericExpressionValue;
					start = (int) d1;
					if (start < 1 ){
						RunTimeError("Start value must be >= 1");
						return false;
					}
	        	}
	        	if (start > Search_in.length()) start = Search_in.length();
	        	start = start - 1;
	        	int k = Search_in.indexOf( Search_for, start);      // Do the search
	        	d1 = (double) k+1 ;                         		// Make results one based
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
				long time = SystemClock.elapsedRealtime();
				theValueStack.push((double) time);
				break;
			
			
			case MFcollision:
				if (!evalNumericExpression()){return false;}	// Get the first object number
	        	int Object1 = (int)(double)EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get the second object number
	        	int Object2 = (int)(double)EvalNumericExpressionValue;

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
	        	int xvalue = (int)(double)EvalNumericExpressionValue;
				if (!isNext(',')) { return false; }
				if (!evalNumericExpression()){return false;}	// Get the shift amount and direction
	        	int bits = (int)(double)EvalNumericExpressionValue;

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
			 	};
			};
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
			try {Temp2 = Temp2 + StringConstant;}					// build up the right side string
			catch (Exception e) {
				RunTimeError("Error: " + e );
				   return false;
			   };
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
		if (!getStringFunction()){; return false;}

		double d = 0;
		double d1 = 0;
		int e = 0;
		int e1 = 0;
		
		String SSC;
																// Have a string function, do it
		switch (SFNumber){
			case  SFleft:																		// LEFT$
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				if (ExecutingLineBuffer.charAt(LineIndex)!= ',') {SyntaxError();return false;}
				++LineIndex;
				SSC = StringConstant;
				if (!evalNumericExpression()){SyntaxError();return false;}
				StringConstant = SSC;
				if (!isNext(')')) { return false; }				// Function must end with ')'
				if (StringConstant.length()==0){return true;};
				d = EvalNumericExpressionValue;
				e = (int) d;
				if (e <= 0){
					StringConstant=""; 
					return true;
				}
				if (e>=StringConstant.length()){e=StringConstant.length();}
				StringConstant = StringConstant.substring(0, e );
				return true;

			case SFmid:																			// MID$
				if (!evalStringExpression()){SyntaxError();return false;}						// Get the string
				if (SEisLE) return false;
				if (ExecutingLineBuffer.charAt(LineIndex)!= ',') {SyntaxError();return false;}
				++LineIndex;
				SSC = StringConstant;
				
				if (!evalNumericExpression()){SyntaxError();return false;}						// Get the displacement
				StringConstant = SSC;
//				if (StringConstant.length()==0){SyntaxError();return true;};
				d = EvalNumericExpressionValue;
				e = (int) d;
				if (e <= 0) e = 1;

				e1 = StringConstant.length();													// Get the length of the string

				if (ExecutingLineBuffer.charAt(LineIndex) == ',') {								// IF there is a count, get it
					++LineIndex;
					SSC = StringConstant;
					if (!evalNumericExpression()){SyntaxError();return false;}
					StringConstant = SSC;
					d1 = EvalNumericExpressionValue;
					e1 = (int) d1;
					if (e1 < 0 ){
						RunTimeError("Count < 0");
						return false;
						}
					if (e1>StringConstant.length()){e1 = StringConstant.length();}
				} 
/*				else {
					if (ExecutingLineBuffer.charAt(LineIndex) != ')') return false;
					e1 = StringConstant.length();}*/

				if (!isNext(')')) { return false; }				// Function must end with ')'
				if (e>StringConstant.length()){
					SSC=""; 
					StringConstant = SSC;
					return true;
				}
				e = e -1;
				e1 = e + e1;
				StringConstant = SSC;
				if (e1>=StringConstant.length()){e1 = StringConstant.length();}
				if (e>=StringConstant.length()){e = StringConstant.length();}
				StringConstant = StringConstant.substring(e, e1 );
				return true;

			case SFright:																		// RIGHT$
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				if (ExecutingLineBuffer.charAt(LineIndex)!= ',') {SyntaxError();return false;}
				++LineIndex;
				SSC = StringConstant;
				if (!evalNumericExpression()){
					SyntaxError();
					return false;
					}
				StringConstant = SSC;
				if (!isNext(')')) { return false; }				// Function must end with ')'
				if (StringConstant.length()==0){SyntaxError();return true;};
				d = EvalNumericExpressionValue;
				e = (int) d;
				if (e <= 0){
					StringConstant=""; 
					return true;
				}				
				if (e>=StringConstant.length()){e=StringConstant.length()-1;}
				e = StringConstant.length() - e;
				StringConstant = StringConstant.substring(e, StringConstant.length());
				return true;

			case SFstr:																			// STR$
				if (!evalNumericExpression()){SyntaxError();return false;}
				StringConstant = String.valueOf(EvalNumericExpressionValue);
				break;

			case SFupper:																		// UPPER$
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				StringConstant = StringConstant.toUpperCase();
				break;

			case SFlower:																		// LOWER $
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				StringConstant = StringConstant.toLowerCase();
				break;

			case SFformat:																		// FORMAT $
				if (!evalStringExpression()){SyntaxError();return false;}						// get the pattern string
				if (SEisLE) return false;
				if (ExecutingLineBuffer.charAt(LineIndex)!= ',') {SyntaxError();return false;}
				++LineIndex;
				SSC = StringConstant;
				if (!evalNumericExpression()){SyntaxError();return false;}						// Get the number to format
				if (!isNext(')')) { return false; }				// Function must end with ')'
				return Format(SSC, EvalNumericExpressionValue);									// and then do the format

			case SFchr:																			// CHR$
				if (!evalNumericExpression()){SyntaxError();return false;}
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
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				String target = StringConstant;

				if (ExecutingLineBuffer.charAt(LineIndex)!= ',') {return false;}
				++LineIndex;
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				String argument = StringConstant;

				if (ExecutingLineBuffer.charAt(LineIndex)!= ',') {return false;}
				++LineIndex;
				if (!evalStringExpression()){SyntaxError();return false;}
				if (SEisLE) return false;
				String replacment = StringConstant;

				if ( argument == null || replacment == null){
					RunTimeError("Invalid string");
					return false;
				}
				
				StringConstant = target.replace(argument, replacment);
				break;
			case SFhex:
				if (!evalNumericExpression()){SyntaxError();return false;}
				int value = (int) (double) EvalNumericExpressionValue;
				StringConstant = Integer.toHexString(value);
				break;
			case SFoct:
				if (!evalNumericExpression()){SyntaxError();return false;}
				value = (int) (double) EvalNumericExpressionValue;
				StringConstant = Integer.toOctalString(value);
				break;
			case SFbin:
				if (!evalNumericExpression()){SyntaxError();return false;}
				value = (int) (double) EvalNumericExpressionValue;
				StringConstant = Integer.toBinaryString(value);
				break;
			default:
		}
		return isNext(')');										// Function must end with ')'

	}
	
	private  boolean Format(String Fstring, double Fvalue){			// Format a number for output
																					// Do the heart of the FORMAT$ function
		BigDecimal B = BigDecimal.valueOf(0.0);
		try { B = BigDecimal.valueOf(Math.abs(Fvalue));}
		catch (Exception e) {
			RunTimeError("Error: " + e );
			return false;
		};
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
		do {
		if (ExecutingLineBuffer.charAt(LineIndex)== ','){++LineIndex;}  // Multiple Arrays can be DIMed in one DIM Statement
																		// seperated by commas
		doingDim = true;
		if (!getVar()){													// Get the array name var
			SyntaxError();
			return false;
		};
		doingDim = false;
		
		if (!VarIsArray){												//if there was no [ char, error
			SyntaxError();
			return false;
		};

		int svn = VarNumber;										// save the array variable table number
		boolean svt = VarIsNumeric;										// and the array vaiable type
		
		ArrayList <Integer> dimValues =new ArrayList<Integer>();        // A list to hold the array index values
		if (ExecutingLineBuffer.charAt(LineIndex)== ']') return false;
		
		while (ExecutingLineBuffer.charAt(LineIndex)!= ']'){					// get each index value
			if (!evalNumericExpression()){SyntaxError(); return false; }
			dimValues.add(EvalNumericExpressionValue.intValue());				// and add it to the list
			if (ExecutingLineBuffer.charAt(LineIndex)== ','){++LineIndex;}
			else if (ExecutingLineBuffer.charAt(LineIndex)!= ']'){SyntaxError(); return false;}
		}
		++LineIndex;
		
		if (!BuildBasicArray( svn , svt, dimValues)){return false;}			// Build the array

		} while ((ExecutingLineBuffer.charAt(LineIndex)== ','));		// continue while there are arrays to be DIMed
		
		return true;													// Then done
	}
	
	private boolean executeUNDIM(){
		unDiming = true;
		if (!getVar()){													// Get the array name var
			SyntaxError();
			return false;
		};
		unDiming = false;

		return true;
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

			if (TotalElements>10000){						// Limit the size of any one array
				RunTimeError("Array exceeds 10,000 elements");		// to 10,000 elements
				return false;
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
			for (int i=0; i < TotalElements; ++i){	//
				StringVarValues.add("");					// Strings inited to empty
			}
		}
		}
		catch (Exception e) {
			RunTimeError("Error: " + e );
			   return false;
		   };

	
		Bundle ArrayEntry = new Bundle();						// Build the array table entry bundle
		ArrayEntry.putIntegerArrayList("dims", DimList);        // The array index values
		ArrayEntry.putIntegerArrayList("sizes", ArraySizes);	// The array sizes
		ArrayEntry.putInt("length", TotalElements);
		ArrayEntry.putInt("base", ArrayValueStart);				// The pointer the first array element value
		VarIndex.set(VarNumber, ArrayTable.size());		        // The VAR's value is it's array table index		
		ArrayTable.add(ArrayEntry);								// Put the element bundle into the array table
		
		return true;
	}// end BuildBasicArray
			
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
		
		Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
		ArrayEntry = ArrayTable.get(VarIndex.get(theVarNumber));
		ArrayList<Integer> dims = ArrayEntry.getIntegerArrayList("dims");
		ArrayList<Integer> sizes = ArrayEntry.getIntegerArrayList("sizes");

		if (dims.size() != indicies.size()){						// insure index count = dim count
			Show(
					"Indices count(" +
					indicies.size()+
					") not same a dimension count ("+
					dims.size()+
					") at:");
			Show(ExecutingLineBuffer);
			SyntaxError = true;
			return false;
		}
		
		int offset = 0;
		
		for (int i=0; i<indicies.size(); ++i){					
			int p = indicies.get(i);						// p = index for this call
			int q = dims.get(i);							// q = DIMed value for this index
			int r = sizes.get(i);							// r = size for this index
			if (p>q){
				Show("Index #"+
						(i+1) +
						" (" +
						p +
						") exceeds dimension (" +
						q +
						") at:");		// insure Index <= DIMed index
				RunTimeError(ExecutingLineBuffer);
				SyntaxError = true;
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
		
	private  boolean executePRINT(boolean doPrint){
		boolean WasSemicolon = false;
		do {									// do each field in the print statement
			int LI = LineIndex;
			if (evalNumericExpression()){
				PrintLine = PrintLine + Double.toString(EvalNumericExpressionValue);	// convert to string
				WasSemicolon = false;
			}else{
				if (evalStringExpression()){
				PrintLine = PrintLine + StringConstant;										// field is string
				WasSemicolon = false;
			}else{
				if (VarIsFunction){SyntaxError(); return false;};
			}
			if (LI == LineIndex) return false;
			}
			char c = ExecutingLineBuffer.charAt(LineIndex);

	        if (c == '\n'){							// Done processing the line
					if (!WasSemicolon){				// if not ended in semi-colon
						if (doPrint){
							PrintShow(PrintLine);	// then output the line
							PrintLine = "";
							return true;
						}
						else return true;
					} else {return true;}
			}
			
			if (c == ','){							// if the seperator is a comma
				PrintLine = PrintLine + ", ";		// add comma + blank to the line
				++LineIndex;
			}else if ( c== ';'){					// if seperator is semi-colon
				++LineIndex;						// don't add anything to output
				WasSemicolon = true;				// and signal we had a semicolon
				if (ExecutingLineBuffer.charAt(LineIndex) == '\n'){return true;} // if now eol, return without outputting
			}
		} while (true);								// Exit loop happens internal to loop
		
		
		
	};
	
	private  boolean executeIF(){
		int q =0;
		
		if (!IfElseStack.empty()){			 								// if inside of an IF block		
			q = IfElseStack.peek();
			if (q != IEexec){												// and not executing
				int index = ExecutingLineBuffer.indexOf("then");
				if (index > 0){												// if there is a THEN
					LineIndex = index + 4;									// skip over the THEN
					if	(ExecutingLineBuffer.charAt(LineIndex)=='\n'){      // if not single line if
						IfElseStack.push(IEskip2);							// need to skip to this if's end
						return true;
						}
					else return true;										// is single line no skip needed
					}
				IfElseStack.push(IEskip2);                                  // No THEN, need skip
				return true;
			}			
		}
		
		
		PossibleKeyWord = "then";					// Tell get var to expect a THEN
	    if (!evalNumericExpression()){SyntaxError();return false;}
		PossibleKeyWord = "";						// Get var should not longer expect THEN
		boolean condition = true;
		if (EvalNumericExpressionValue == 0) condition = false;
		
		boolean SingleLine = false;
		if	(ExecutingLineBuffer.charAt(LineIndex)!='\n'){
			if (!ExecutingLineBuffer.startsWith("then", LineIndex)) return false;
			LineIndex = LineIndex + 4;
			if (ExecutingLineBuffer.charAt(LineIndex)!='\n') SingleLine = true;
		}
		
		if (SingleLine) return SingleLineIf(condition);
		
	    // SingleLine = false at this point
		
		if (condition) IfElseStack.push(IEexec);
		else IfElseStack.push(IEskip1);

		return true;
	}
	
	private boolean SingleLineIf(boolean condition){
		int index = ExecutingLineBuffer.lastIndexOf("else");
		String SaveELB = "";
		if (index > 0 ){
			SaveELB = ExecutingLineBuffer;
			ExecutingLineBuffer = ExecutingLineBuffer.substring(0, index);
			ExecutingLineBuffer = ExecutingLineBuffer + "\n";
		}
		
		if (condition){
			return StatementExecuter();
		}

		if (index > 0) {
			ExecutingLineBuffer = SaveELB;
			LineIndex = index + 4;
			return StatementExecuter();
		}
		return true;
	}
	
	private  boolean executeELSE(){
		int q =0;
		
		if (IfElseStack.empty()){			// If there is nothing on the stack
			RunTimeError("Misplaced ELSE");			// we should find an ELSE
			return false;
		}
		
		if (!checkEOL() ) return false;

		q = IfElseStack.pop();
		
		if (q == IEexec) IfElseStack.push(IEskip2);
		else IfElseStack.push(IEexec);
		return true;
	}
	
	private boolean executeELSEIF(){
		int q =0;
		
		if (IfElseStack.empty()){			// If there is nothing on the stack
			RunTimeError("Misplaced ELSEIF");			// we should find an ELSE
			return false;
		}
		
		q = IfElseStack.pop();

		if (q == IEexec) {
			IfElseStack.push(IEskip2);
			return true;
		}
		PossibleKeyWord = "then";					// Tell get var to expect a THEN
	    if (!evalNumericExpression()){return false;}
		PossibleKeyWord = "";					   // Tell get var to expect a THEN
	    if (EvalNumericExpressionValue == 0) IfElseStack.push(IEskip1);
	    else IfElseStack.push(IEexec);
	    
	    if (ExecutingLineBuffer.startsWith("then", LineIndex)) LineIndex = LineIndex + 4;
	    if (!checkEOL() ) return false;
	    
		
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
		
		if (ExecutingLineIndex+1 >= Basic.lines.size() ){SyntaxError(); return false;}
		b.putInt("line",ExecutingLineIndex);							// Loop return location

		if (!getNVar()){SyntaxError();return false;}
		b.putInt("var", theValueIndex);					// For Var
		int IndexValueIndex = theValueIndex;
		
		if (ExecutingLineBuffer.charAt(LineIndex)!='=') {SyntaxError();return false;}	// For Var =
		++LineIndex;

		PossibleKeyWord = "to";						// Tell get var that a TO is expected
		if (!evalNumericExpression()){SyntaxError();return false;}						// for Var = <exp>
		PossibleKeyWord = "";
		double fstart = EvalNumericExpressionValue;
		NumericVarValues.set(IndexValueIndex, fstart);   // assign <exp> to Var

		if (!ExecutingLineBuffer.startsWith("to", LineIndex)) return false;
		LineIndex = LineIndex +2;
		
		PossibleKeyWord = "step";
		if (!evalNumericExpression()){SyntaxError();return false;}			// For Var = <exp> to <exp>
		PossibleKeyWord = "";
		b.putDouble("limit", EvalNumericExpressionValue);
		double flimit = EvalNumericExpressionValue;

		double step = 1.0;													//For Var = <exp> to <exp> <default step> 
		if (ExecutingLineBuffer.startsWith("step", LineIndex)){
			LineIndex = LineIndex + 4;
			if (!evalNumericExpression()){SyntaxError();return false;}		// For Var = <exp> to <exp> step <exp>
			step = EvalNumericExpressionValue;
		}
		
		if (!checkEOL(true)) return false;
		
		b.putDouble("step", step);
		
		if (step > 0){											// Test the initial condition
			if (fstart > flimit) {return SkipToNext();}			// If exceeds limit then skip to NEXT
		}else 
			if (fstart < flimit) {return SkipToNext();}

		ForNextStack.push(b);
		
		return true;
//		return true;
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
		};
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
	                    };
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
		if (BackKeyResume == -1){
			RunTimeError("Back key not hit");
			return false;
		}
		ExecutingLineIndex = BackKeyResume;
		BackKeyResume = -1 ;
		return true;
	}
	
	private boolean executeONMENUKEY_RESUME(){
		if (OnMenuKeyResume == -1){
			RunTimeError("Menu key not hit");
			return false;
		}
		ExecutingLineIndex = OnMenuKeyResume;
		OnMenuKeyResume = -1 ;
		return true;
	}
	
// ************************************************** Read Commands **********************************
	
	// Parse and bundle the data list of a READ.DATA statement
	// Called from PreScan() and NOT from 
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
		int newIndex = (int) (double) EvalNumericExpressionValue;
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
			  };
		  };
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
		if (Debug) executePRINT(true);
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
			  
			  doingDim = false;									// Get the array variable
			  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
			  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
			  SkipArrayValues = false;
			  doingDim = false;
			  
			  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
			  if (!VarIsNumeric){								// and that it is a numeric array
				  RunTimeError("Array not numeric");
			  }
			  if (VarIsNew){									// and that it has been DIMed
				  RunTimeError("Array not DIMed");
				  return false;
			  }
			  
			  PrintShow("Dumping Array " + VarNames.get(VarNumber) + "]");
			  
				Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
				ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
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
				int listIndex = (int) (double)EvalNumericExpressionValue;
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
				int stackIndex = (int) (double)EvalNumericExpressionValue;
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
				int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
				doingDim = false;									// Get the array variable
			  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
			  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
			  SkipArrayValues = false;
			  doingDim = false;
			  
			  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
			  if (!VarIsNumeric){								// and that it is a numeric array
				  RunTimeError("Array not numeric");
			  }
			  if (VarIsNew){									// and that it has been DIMed
				  RunTimeError("Array not DIMed");
				  return false;
			  }
				WatchedArray = VarNumber;
				DialogSelector(2);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_LIST(){
				if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;					// Get the list pointer
				int listIndex = (int) (double)EvalNumericExpressionValue;
				if (listIndex < 1 || listIndex >= theLists.size()){
					RunTimeError("Invalid List Pointer");
					return false;
				}
				WatchedList = listIndex;
				DialogSelector(3);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_STACK(){
				if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;							// Get the Stack pointer
				int stackIndex = (int) (double)EvalNumericExpressionValue;
				if (stackIndex < 1 || stackIndex >= theStacks.size()){
					RunTimeError("Invalid Stack Pointer");
					return false;
				}
				WatchedStack = stackIndex;
				DialogSelector(4);
				executeDEBUG_SHOW();
				return true;
			}
			private boolean executeDEBUG_SHOW_BUNDLE(){
				if (!Debug) return true;
			  
				if (!evalNumericExpression()) return false;							// Get the Bundle pointer
				int bundleIndex = (int) (double)EvalNumericExpressionValue;
				if (bundleIndex < 1 || bundleIndex >= theBundles.size()){
					RunTimeError("Invalid Bundle Pointer");
					return false;
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
		
	  DebugDialog.setTitle("Basic! Debugger");
		
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
			  
				Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
				ArrayEntry = ArrayTable.get(VarIndex.get(WatchedArray));
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
		if (!getVar()){SyntaxError();return false;}      				// Get the function name
		DoingDef = false;
		if (!VarIsFunction) {SyntaxError();return false;}               // Make sure it is a function
		if (!VarIsNew){
			RunTimeError("Function previoulsy defined at:");
			return false;
		}
		
		int fVarNumber = VarNumber;                                  // Save the VarNumber of the function name
		boolean fType = VarIsNumeric;
		int NVV = NumericVarValues.size();							// Save for array trim
		int SVV = StringVarValues.size();
		
//		int saveVarSearchStart = VarSearchStart;					// Save current VarSearchStart
//		VarSearchStart = fVarNumber;								// Set new start

		ArrayList<String> fVarName = new ArrayList<String>();        // List of parameter names
		ArrayList<Integer> fVarType = new ArrayList<Integer>();      // List of parameter types
		ArrayList<Integer> fVarArray = new ArrayList<Integer>();     // A list of indicating if parm is array

		char c;
		int i;

		if (ExecutingLineBuffer.charAt(LineIndex)==')')
			{++LineIndex;} else
			
			do{															// Get each of the parameter names
				doingDim = true;
				if (!getVar()){SyntaxError();return false;}
				doingDim = false;
				if (VarIsArray){										// Process array
					if (ExecutingLineBuffer.charAt(LineIndex)!=']')return false;
					++LineIndex;
					fVarArray.add(1);      // 1 Indicates var is array
				} else fVarArray.add(0);   // 0 Indicates var is not an array
						
				fVarName.add(VarNames.get(VarNumber));					// Save the name
				if (VarIsNumeric) i=1; else i=0;
				fVarType.add(i);										// Save the type
				c = ExecutingLineBuffer.charAt(LineIndex);
				++LineIndex;
			} while (c == ',');
//			if (c != ')') return false;
//			if (! checkEOL()) return false;

			
		
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
		return checkEOL(false);
	}
	
	private boolean executeFN_END(){
		if (FunctionStack.empty()){							// Insure RTN actually called from executing function
			RunTimeError("misplaced fn.rtn");
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
		return checkEOL(false);

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
			 	};
			};
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
	if (pCount != 0)													// For each parameter
		do {
			boolean isGlobal = false;
			
			if (i >= pCount){														// Insure no more parms than defined
				RunTimeError("Calling parameter count exceeds defined parameter count");
				SyntaxError = true;
				return false;
				}
		
			if (fVarArray.get(i) == 1){												// if this parm is an array
				SkipArrayValues = true;
				getVar();															// get the caling array
				SkipArrayValues = false;
				if (!VarIsArray) return false;										// make sure it is an array
				if (!isNext(']')) return false;
				boolean t = true ;
				if (fVarType.get(i) == 1 && !VarIsNumeric) t=false;					// Insure type (string or number) match
				if (fVarType.get(i) == 0 && VarIsNumeric) t=false;
				if (!t){
					RunTimeError("Array parameter type mismatch at:");
					SyntaxError = true;
					return false;
					}
				
				VarNames.add(fVarName.get(i));						// and add the var name
				VarIndex.add(VarIndex.get(VarNumber));				// copy array table pointer to the new array.
			}
			else{
				isGlobal = isNext('&');   // if this is a Global Var

				if (fVarType.get(i) == 0){                      		// if parm is string
					
					if (isGlobal){										// If Global
						if (!getVar()) return false;					// then must be var not expression
						if (VarIsNew){
							RunTimeError("Call by reference vars must be predefined");
							return false;
						}
						if (VarIsNumeric) return false;					// and must be numeric var
						int thisValueIndex = VarIndex.get(VarNumber);	// get the calling var's value index
						VarNames.add(fVarName.get(i));					// add the called var name to the var name table		
						VarIndex.add(thisValueIndex);					// but give it the value index of the calling var
					}else {
						if (!evalStringExpression()){					// get the string value
							RunTimeError("Parameter type mismatch at:");
							SyntaxError = true;
							return false;
						}else{
							VarIndex.add(StringVarValues.size());		// Put the string value into the
							StringVarValues.add(StringConstant);        // string var values table
							VarNames.add(fVarName.get(i));				// and add the var name
						}
					}
				}else{
					if (isGlobal){										// If Global
						if (!getVar()) return false;					// then must be var not expression
						if (VarIsNew){
							RunTimeError("Call by reference vars must be predefined");
							return false;
						}
						if (!VarIsNumeric) return false;					// and must be numeric var
						int thisValueIndex = VarIndex.get(VarNumber);	// get the calling var's value index
						VarNames.add(fVarName.get(i));					// add the called var name to the var name table		
						VarIndex.add(thisValueIndex);					// but give it the value index of the calling var
					}else {
						int iSave = i;
						int pcountSave = pCount;
						if (!evalNumericExpression()){						// if parm is number
							RunTimeError("Parameter type mismatch at:");	// get the numeric value
							SyntaxError = true;
							return false;
						}else{
							VarIndex.add(NumericVarValues.size());				// Put the number values into the
							NumericVarValues.add(EvalNumericExpressionValue);   // numeric var values table
							i = iSave;
							pCount = pcountSave;
							VarNames.add(fVarName.get(i));						// and add the var name
						}
					}
				}
			}
			
		++i;											//  Keep going while calling parms exist
				
	}while ( isNext(','));
	
	// Every function must have a closing right parenthesis.
	if (!isNext(')')) return false;
	
	if (i!=pCount){
		RunTimeError("Too few calling parameters at:");
		return false;
	}
	

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
		
   		++ExecutingLineIndex;  // Next program line
		while (ExecutingLineIndex < Basic.lines.size()){
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);  // Next program line
			if (ExecutingLineBuffer.startsWith("sw.end")) {
				LineIndex = LineIndex + 6;
				if (!checkEOL()) return false;
				return true;
			}else if (ExecutingLineBuffer.startsWith("sw.default")){
				LineIndex = LineIndex + 10;
				if (!checkEOL()) return false;
				return true;
			}else if (ExecutingLineBuffer.startsWith("sw.case")){
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
			++ExecutingLineIndex;
		}
		RunTimeError("No sw.end after sw.begin");
		return false;
	}
	
	private boolean executeSW_CASE(){
		return true;
	}
	
	private boolean executeSW_BREAK(){
		
   		++ExecutingLineIndex;  // Next program line
		while (ExecutingLineIndex < Basic.lines.size()){
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);  // Next program line
			if (ExecutingLineBuffer.startsWith("sw.end")) return true;
			++ExecutingLineIndex;  // Next program line	
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
	    
	private  boolean executeTEXT_OPEN(){
													// Open a file
		
		boolean append = false;												// Assume not append
		char c = ExecutingLineBuffer.charAt(LineIndex);
		int FileMode = 0;
		if (c == 'a'){						// First parm is a, w or r
				append = true;				// append is a special case
				c = 'w';					// of write
		}
		if (c == 'w'){						// w
			FileMode =FMW;
			++LineIndex;
		}else if (c == 'r'){				// of r
			FileMode = FMR;
			++LineIndex;
			c = ExecutingLineBuffer.charAt(LineIndex);
//			if (c=='w'){
//				FileMode = FMRW;
//				++LineIndex;
//			}
		}
		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;}
		++LineIndex;
		if (!getNVar()){return false;}				// Next parameter if the FileNumber variable
		NumericVarValues.set(theValueIndex, (double) FileTable.size());
		int saveValueIndex = theValueIndex;         // Save in case read file not found.

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;}
		++LineIndex;

		if (!evalStringExpression()){return false;} // Final paramter is the filename
		if (SEisLE) return false;
		String fileName = StringConstant;
		if (!checkEOL()) return false;

		Bundle FileEntry = new Bundle();			// Prepare the filetable bundle
		FileEntry.putInt("mode", FileMode);
		FileEntry.putBoolean("eof", false);
		FileEntry.putBoolean("closed",false);
		FileEntry.putInt("position", 1);
		FileEntry.putBoolean("isText", true);

		BufferedReader buf = null;

		if (FileMode == FMR){												// Read was seleced

			    	
					   String fn0 = Basic.filePath + "/data/" + fileName;				// Add the path to the filename
					   String fn = new File(fn0).getAbsolutePath();							// convert to absolute path				
					   
					   String packageName = Basic.BasicContext.getPackageName();			// Get the package name
					  
					   boolean loadRaw = false;												// Assume not loading raw resource

					   if (!Basic.isAPK){							// if not standard BASIC!
						   																	// then is user APK
						   File theFile = new File(fn);										// if the file has not been loaded onto the SDCARD									
					       if (!theFile.exists())  loadRaw = true;							// then the file to be loaded from a raw resource
					   }
					   
					   Uri theURI = null;
					   int resID = 0;
					   
					   if (loadRaw) {														// If loading a raw resource file
						   String rawFileName = getRawFileName(fileName);					// Convert conventional filename to raw resource name
						   if (rawFileName.equals("")) {
							   RunTimeError("Error converting to raw filename");
							   return false;
						   }
						   try {
							   resID = Basic.BasicContext.getResources().getIdentifier (rawFileName, "raw", packageName);  // Get the resourc ID
			    	           InputStream inputStream = BasicContext.getResources().openRawResource(resID);
			    	           InputStreamReader inputreader = new InputStreamReader(inputStream);
			    	           buf = new BufferedReader(inputreader, 8192);
						   }
						   catch (Exception e) {
								RunTimeError("Error: " + e );
							   return false;
						   };
					   }else {																// Not loading from a raw resource
						   try {
							   File file = new File(fn);
							   buf = new BufferedReader(new FileReader(file), 8192);
							   if (buf != null ) buf.mark((int)file.length());
						   }
						   catch (Exception e) {
								RunTimeError("Error: " + e );
							   return false;
						   };
					   }

				  

			  
			  FileEntry.putInt("stream", BRlist.size()); 	 //The stream parm indexes
			  BRlist.add(buf);								 //into the FISlist
			  FileTable.add(FileEntry);
		}
		
		if (FileMode == FMW){										// Write Selected
        	File file = new File(Basic.filePath + "/data/"+ fileName);
        	if (append){
        		if (!file.exists()){
        		try {
            		file.createNewFile();
            		} catch (Exception e){
            			RunTimeError("Error: " +e);
            			return false;
            			}
            	}
        		
        	} else {
        	try {
        		file.createNewFile();
        		} catch (Exception e){
        			RunTimeError("Error: " +e);
        			return false;
        			}
        	}
        	if (!file.exists() || !file.canWrite()){
        		RunTimeError("Problem opening " + fileName);
	    		return false;
        	}
            FileWriter writer = null;
        	try{
        		writer = new FileWriter(file, append);						// open the filewriter for the SD Card
        		}catch (Exception e) {
        			RunTimeError("Error: " + e );
        				return false;
        		}
			FileEntry.putInt("stream", FWlist.size()); 	 	 //The stream parm indexes
			FWlist.add(writer);								 //into the FISlist
			FileTable.add(FileEntry);

		}
	return true;												// Done
		
	}
	
		private  boolean executeTEXT_CLOSE(){
			
		if (FileTable.size() == 0){
			return true;
		}

		if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
		double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber >= FileTable.size() || FileNumber < 0 ){
			RunTimeError("Invalid File Number at");
			return false;
		}
		if (!checkEOL()) return false;

		int FileMode;							//     Variables for the bundle
		BufferedReader buf = null;
		FileWriter writer = null;
		boolean closed;

		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);  // and the values from the bundle
		FileMode = FileEntry.getInt("mode");
		closed = FileEntry.getBoolean("closed");

		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		
		if (closed){return true;};				// Already closed
		

		if (FileMode == FMR){								// Close file open for read
			buf = BRlist.get(FileEntry.getInt("stream"));
    		try {
    			buf.close();
    		} catch (Exception e) {
    			RunTimeError("Error:" + e);
    			return false;
			}

		}
		if (FileMode == FMW){									// close file open for write
			writer = FWlist.get(FileEntry.getInt("stream"));
			try {
				writer.flush();
				writer.close();
			} catch (Exception e) {
    			RunTimeError("Error:" + e);
    			return false;
			}
		}
		FileEntry.putBoolean("closed", true);					// mark the file closed.
		FileTable.set(FileNumber, FileEntry);
		return true;
	}
	
		private  boolean executeTEXT_READLN(){

			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the string var
		++LineIndex;
		if (!getSVar()){return false;};						

		int FileMode;							//     Variables for the bundle
		boolean eof;
		BufferedReader buf = null;
		boolean closed;

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		eof = FileEntry.getBoolean("eof");
		buf = BRlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
		
		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		
		if (closed){
			RunTimeError("File is closed");
		}
		
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}
		
/*		if (eof){											//Check not EOF
			RunTimeError("Attempt to read beyond the EOF at:");
			return false;
		}*/
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}

		
        try {												// Read a line
            	String data = buf.readLine();
            	BRlist.set(FileEntry.getInt("stream"), buf);
            	if (data == null){
            		data="EOF";
            		FileEntry.putBoolean("eof", true);
//            		FileEntry.putBoolean("closed", true);
        			}
            	else {
            		int p = FileEntry.getInt("position");
            		++p;
            		FileEntry.putInt("position", p);
            	}
        		FileTable.set(FileNumber, FileEntry);
        		StringVarValues.set(theValueIndex, data);
             }
        	catch (IOException e) {
        		RunTimeError("I/O error at:");
        		return false;
        	}
        return true;
	}
	
/*		private  boolean executeTEXT_WRITELN(){

			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}

			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber >= FileTable.size() || FileNumber < 0 ){
			RunTimeError("Invalid File Number at");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the string var
		++LineIndex;

		if (!evalStringExpression()){SyntaxError(); return false;}
		if (SEisLE) return false;
		if (!checkEOL()) return false;
	
		
		int FileMode;							//     Variables for the bundle
		FileWriter writer = null;
		boolean closed;

		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		if (FWlist.size() == 0 ){
			RunTimeError("Wait 10 seconds before re-running");
			Stop = true;
			return false;
		}
		writer = FWlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
		
		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		
		if (FileMode != FMW){						// Varify open for read
			RunTimeError("File not opened for write at");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}
		try {
				writer.write(StringConstant);					//Oh, and write the line
				writer.write("\r\n");
		} catch (IOException e) {
			RunTimeError("I/O error at");
			return false;
		}
		
    	int p = FileEntry.getInt("position");
    	++p;
    	FileEntry.putInt("position", p);

		return true;
	}*/
		
		private boolean executeTEXT_WRITELN(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
			int FileNumber =  (int) d;
			if (FileNumber >= FileTable.size() || FileNumber < 0 ){
				RunTimeError("Invalid File Number at");
				return false;
			}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Set up to parse the stuff to print
		++LineIndex;

		if (!TextPRINT(true)) return false;				// build up the text line
		if (!textPrintLineReady) return true;			// If not ready to print, wait
		
		textPrintLineReady = false;						// Reset the signal
		StringConstant = textPrintLine;					// Copy the line to StringConstant for writing
		textPrintLine = "";								// clear the text print line
		
		int FileMode;							//     Variables for the bundle
		FileWriter writer = null;
		boolean closed;

		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		if (FWlist.size() == 0 ){
			RunTimeError("Wait 10 seconds before re-running");
			Stop = true;
			return false;
		}
		writer = FWlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
		
		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		
		if (FileMode != FMW){						// Varify open for read
			RunTimeError("File not opened for write at");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}
		try {
				writer.write(StringConstant);					//Oh, and write the line
				writer.write("\r\n");
		} catch (IOException e) {
			RunTimeError("I/O error at");
			return false;
		}
		
    	int p = FileEntry.getInt("position");
    	++p;
    	FileEntry.putInt("position", p);

			return true;
		}
		
		private  boolean TextPRINT(boolean doPrint){
			boolean WasSemicolon = false;
			do {									// do each field in the print statement
				int LI = LineIndex;
				if (evalNumericExpression()){
					textPrintLine = textPrintLine + Double.toString(EvalNumericExpressionValue);	// convert to string
					WasSemicolon = false;
				}else{
					if (evalStringExpression()){
						textPrintLine = textPrintLine + StringConstant;										// field is string
					WasSemicolon = false;
				}else{
					if (VarIsFunction){SyntaxError(); return false;};
				}
				if (LI == LineIndex) return false;
				}
				char c = ExecutingLineBuffer.charAt(LineIndex);

		        if (c == '\n'){							// Done processing the line
						if (!WasSemicolon){				// if not ended in semi-colon
							if (doPrint){
								textPrintLineReady = true;
								return true;
							}
							else return true;
						} else {return true;}
				}
				
				if (c == ','){							// if the seperator is a comma
					textPrintLine = textPrintLine + ", ";		// add comma + blank to the line
					++LineIndex;
				}else if ( c== ';'){					// if seperator is semi-colon
					++LineIndex;						// don't add anything to output
					WasSemicolon = true;				// and signal we had a semicolon
					if (ExecutingLineBuffer.charAt(LineIndex) == '\n'){return true;} // if now eol, return without outputting
				}
			} while (true);								// Exit loop happens internal to loop
			
			
			
		};

		
		private boolean executeTEXT_INPUT(){
			if (!getVar()) return false;
			if (VarIsNumeric) return false;
			int saveValueIndex = theValueIndex;
			
			TextInputString = "";
			if (ExecutingLineBuffer.charAt(LineIndex)==','){ 
				++LineIndex;
				if (!evalStringExpression()) return false;
			    TextInputString = StringConstant;
			}
			if (!checkEOL()) return false;
			
			HaveTextInput = false;
		    startActivityForResult(new Intent(this, TextInput.class), BASIC_GENERAL_INTENT);
		    while (!HaveTextInput) Thread.yield();
		    
		    StringVarValues.set(saveValueIndex, TextInputString);
			return true;
		}
		
		private boolean executeTEXT_POSITION_SET(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the position var expression
		++LineIndex;
		if (!evalNumericExpression()){return false;};	
		int pto = (int) (double)EvalNumericExpressionValue;
		if (pto < 1 ){
			RunTimeError("Set position must be >= 1");
			return false;
		}

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		int FileMode = FileEntry.getInt("mode");
		boolean eof = FileEntry.getBoolean("eof");
		boolean closed = FileEntry.getBoolean("closed");
		
		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		
		if (closed){
			RunTimeError("File is closed");
		}
		
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read");
			return false;
		}
		
		BufferedReader buf = BRlist.get(FileEntry.getInt("stream"));

		int pnow = FileEntry.getInt("position");
		
		if (pnow == pto) return true;
		
		if (pto < pnow) {
			try {
				buf.reset();
				FileEntry.putBoolean("eof", false);
				FileEntry.putInt("position", 1);
				FileTable.set(FileNumber, FileEntry);
			}
			catch (Exception e){
//				RunTimeError("Error: " + e);
//				return false;
			}
			pnow = 1;
			if (pto == 1 ) return true;
		}
		String data = null;
		while (pnow < pto ){
			
	        try {												// Read a line
            	data = buf.readLine();
            	BRlist.set(FileEntry.getInt("stream"), buf);
            	if (data == null){
            		data="EOF";
            		FileEntry.putBoolean("eof", true);
            		pto = pnow;
        			}
            	else {
            		++pnow;
            		FileEntry.putInt("position", pnow);
            		StringVarValues.set(theValueIndex, data);
            	}
             }
        	catch (Exception e) {
//        		RunTimeError("Error: " + e);
//        		return false;
        	}
		} 

		FileTable.set(FileNumber, FileEntry);
		return true;
		}
		
		private boolean executeTEXT_POSITION_GET(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the position var
		++LineIndex;
		if (!getNVar()){return false;};						

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		
		boolean isText = FileEntry.getBoolean("isText");
		if (!isText){
			RunTimeError("File not opened for text");
			return false;
		}

		int p = FileEntry.getInt("position");
		double dp = (double) p;
		NumericVarValues.set(theValueIndex, dp);

			return true;
		}
		
		private boolean executeTGET(){
			if (!getSVar()) return false;
			int saveValueIndex = theValueIndex;
			
			TextInputString = "";
			if (ExecutingLineBuffer.charAt(LineIndex)!=',')return false; 
			++LineIndex;
			
			if (!evalStringExpression()) return false;
			TextInputString = StringConstant;
			String Prompt = StringConstant;
			if (!checkEOL()) return false;
			
			HaveTextInput = false;
		    startActivityForResult(new Intent(this, TGet.class), BASIC_GENERAL_INTENT);
		    while (!HaveTextInput) Thread.yield();
		    Show(Prompt + TextInputString);
		    
		    StringVarValues.set(saveValueIndex, TextInputString);
			return true;
		}

		private  boolean executeBYTE_OPEN(){
			// Open a file
			
			boolean append = false;												// Assume not append
			char c = ExecutingLineBuffer.charAt(LineIndex);
			int FileMode = 0;
			if (c == 'a'){						// First parm is a, w or r
					append = true;				// append is a special case
					c = 'w';					// of write
			}
			if (c == 'w'){						// w
				FileMode =FMW;
				++LineIndex;
			}else if (c == 'r'){				// of r
				FileMode = FMR;
				++LineIndex;
				c = ExecutingLineBuffer.charAt(LineIndex);
			}
			
			if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;}
			++LineIndex;
			if (!getNVar()){return false;}				// Next parameter if the FileNumber variable
			NumericVarValues.set(theValueIndex, (double) FileTable.size());
			int saveValueIndex = theValueIndex;

			if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;}
			++LineIndex;

			if (!evalStringExpression()){return false;} // Final paramter is the filename
			if (SEisLE) return false;
			String theFileName = StringConstant;
			if (!checkEOL()) return false;

			Bundle FileEntry = new Bundle();			// Prepare the filetable bundle
			FileEntry.putInt("mode", FileMode);
			FileEntry.putBoolean("eof", false);
			FileEntry.putBoolean("closed",false);
			FileEntry.putInt("position", 1);
			FileEntry.putBoolean("isText", false);
			
			String FullFileName = "";

			if (FileMode == FMR){												// Read was seleced
//				DataInputStream dis = null;
				BufferedInputStream bis = null;
				if (theFileName.startsWith("http")){
					try{
						URL url = new URL(theFileName);
						URLConnection connection = url.openConnection();
						InputStream fis = connection.getInputStream();
						bis = new BufferedInputStream(fis, 8192);
					}catch (Exception e){
						RunTimeError("Problem: " + e + " at:");
						return false;
					}
					
				}else {
					
					   String fn0 = Basic.filePath + "/data/" + theFileName;				// Add the path to the filename
					   String fn = new File(fn0).getAbsolutePath();							// convert to absolute path				
					   
					   String packageName = Basic.BasicContext.getPackageName();			// Get the package name
					  
					   boolean loadRaw = false;												// Assume not loading raw resource

					   if (!Basic.isAPK){							// if not standard BASIC!
						   																	// then is user APK
						   File theFile = new File(fn);										// if the file has not been loaded onto the SDCARD									
					       if (!theFile.exists())  loadRaw = true;							// then the file to be loaded from a raw resource
					   }
					   
					   
					   Uri theURI = null;
					   int resID = 0;
					   
					   if (loadRaw) {														// If loading a raw resource file
						   String rawFileName = getRawFileName(theFileName);					// Convert conventional filename to raw resource name
						   if (rawFileName.equals("")) {
							   RunTimeError("Error converting to raw filename");
							   return false;
						   }
						   try {
							   resID = Basic.BasicContext.getResources().getIdentifier (rawFileName, "raw", packageName);  // Get the resourc ID
			    	           InputStream inputStream = BasicContext.getResources().openRawResource(resID);
							   bis = new BufferedInputStream(inputStream, 8192);

						   }
						   catch (Exception e) {
								RunTimeError("Error: " + e );
							   return false;
						   };
					   }else {																// Not loading from a raw resource
						   try {
							   	  File file = new File(fn);
								  FileInputStream fis = new FileInputStream(file);
							      bis = new BufferedInputStream(fis, 8192);
								  if (bis != null) bis.mark((int)file.length());
						   }
						   catch (Exception e) {
								RunTimeError("Error: " + e );
							   return false;
						   };
					   }

				  
					}
				FileEntry.putInt("stream", BISlist.size()); 	 //The stream parm indexes
				BISlist.add(bis);								 //into the FISlist
				FileTable.add(FileEntry);
				  
			}
			
		
			if (FileMode == FMW){										// Write Selected
		    		

		    																	//Write to SD Card
		        	File file = new File(Basic.filePath + "/data/" + theFileName);
		        	if (append){
		        		if (!file.exists()){
		            		try {
		                		file.createNewFile();
		                		} catch (Exception e){
		                			RunTimeError("Error: " +e);
		                			return false;
		                			}
		                	}else{
		                		FileEntry.putInt("position", (int) file.length()+1);
		                	}
		        	}else {
		        	try {
		        		file.createNewFile();
		        		} catch (Exception e){
		        			RunTimeError("Error: " +e);
		        			return false;
		        			}
		        	}
		        	if (!file.exists() || !file.canWrite()){
		        		RunTimeError("Problem opening " + theFileName);
			    		return false;
		        	}
		        	
					  String afile = file.getAbsolutePath();
					  DataOutputStream dos = null;
					  try{
						  FileOutputStream fos = new FileOutputStream(afile, append);
					      dos = new DataOutputStream(fos);
					  }				
					  catch (Exception e) {
							RunTimeError("Error: " + e );
						  return false;
						  };
				  
				  FileEntry.putInt("stream", DOSlist.size()); 	 //The stream parm indexes
				  DOSlist.add(dos);								 //into the FISlist
				  FileTable.add(FileEntry);

			}
		return true;												// Done
			
		}
		
			private  boolean executeBYTE_CLOSE(){
				if (FileTable.size() == 0){
					
					return true;
				}

				if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
				double d = EvalNumericExpressionValue;
			int FileNumber =  (int) d;
			if (FileNumber >= FileTable.size() || FileNumber < 0 ){
				RunTimeError("Invalid File Number at");
				return false;
			}
			if (!checkEOL()) return false;


			int FileMode;							//     Variables for the bundle
			BufferedInputStream bis = null;
			DataOutputStream dos = null;
			boolean closed;

			Bundle FileEntry = new Bundle();		// Get the bundle 
			FileEntry = FileTable.get(FileNumber);  // and the values from the bundle
			FileMode = FileEntry.getInt("mode");
			closed = FileEntry.getBoolean("closed");
			
			boolean isText = FileEntry.getBoolean("isText");
			if (isText){
				RunTimeError("File not opened for byte");
				return false;
			}

			
			if (closed){return true;};				// Already closed

			if (FileMode == FMR){								// Close file open for read
				bis = BISlist.get(FileEntry.getInt("stream"));
	    		try {
	    			bis.close();
	    		} catch (IOException e) {
//	    			Log.e(Run.LOGTAG, e.getLocalizedMessage() + " 3");
				}

			}
			if (FileMode == FMW){									// close file open for write
				dos = DOSlist.get(FileEntry.getInt("stream"));
				try {
					dos.flush();
					dos.close();
				} catch (IOException e) {
//	    			Log.e(Run.LOGTAG, e.getLocalizedMessage() + " 3");
				}
			}
			FileEntry.putBoolean("closed", true);					// mark the file closed.
			FileTable.set(FileNumber, FileEntry);
			return true;
		}
		 
		private boolean executeBYTE_COPY(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}
		if (FileNumber >= FileTable.size()){				// Make sure it is a real file table number
			RunTimeError("Invalid File Number at");
			return false;
		}


		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the byte var
		++LineIndex;

		if (!evalStringExpression()) return false;
		if (!checkEOL()) return false;
		
		int FileMode;							//     Variables for the bundle
		boolean eof;
		BufferedInputStream bis = null;
		boolean closed;

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}
		eof = FileEntry.getBoolean("eof");
		closed = FileEntry.getBoolean("closed");
		
		boolean isText = FileEntry.getBoolean("isText");
		if (isText){
			RunTimeError("File not opened for byte");
			return false;
		}

		
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}
		
		if (eof){											//Check not EOF
			RunTimeError("Attempt to read beyond the EOF at:");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}
		
		bis = BISlist.get(FileEntry.getInt("stream"));
		
		String theFileName = StringConstant;
		String FullFileName = "";
		
    	File file = new File(Basic.filePath + "/data/" + theFileName);

    	try {
    		file.createNewFile();
    		} catch (IOException e){
    			RunTimeError(theFileName + " I/O Error");
    			return false;
    			}
    	if (!file.exists() || !file.canWrite()){
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
    	    }
    	} catch (Exception e) {
    	     RunTimeError("Exception: " + e);
    	     return false;
    	} finally {
    	
    		try { bos.flush(); } catch (Exception e) {
    			RunTimeError("Error: " + e );}

    	    try { bis.close(); } catch (Exception e) {
    			RunTimeError("Error: " + e );}

    	    try { bos.close(); } catch (Exception e) {
    			RunTimeError("Error: " + e );}

    	   
    	}
    	return true;
		}

    	
		private  boolean executeBYTE_READ_BYTE(){

				if (FileTable.size() == 0){
					RunTimeError("No files opened");
					return false;
				}
				
				if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
				double d = EvalNumericExpressionValue;
			int FileNumber =  (int) d;
			if (FileNumber <0 ){
				RunTimeError("Read file did not exist");
				SyntaxError = true;
				return false;
			}
			if (FileNumber >= FileTable.size()){				// Make sure it is a real file table number
				RunTimeError("Invalid File Number at");
				return false;
			}

			if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Third parm is the return buffer string
			++LineIndex;
			if (!getNVar()) return false;
			
		
			int FileMode;							//     Variables for the bundle
			boolean eof;
			BufferedInputStream bis = null;
			boolean closed;

			
			Bundle FileEntry = new Bundle();		// Get the bundle 
			FileEntry = FileTable.get(FileNumber);
			FileMode = FileEntry.getInt("mode");
			eof = FileEntry.getBoolean("eof");
			closed = FileEntry.getBoolean("closed");
			
			boolean isText = FileEntry.getBoolean("isText");
			if (isText){
				RunTimeError("File not opened for byte");
				return false;
			}
			
			if (FileMode != FMR){						// Varify open for read
				RunTimeError("File not opened for read at");
				return false;
			}
			
			if (closed){											//Check not Closed
				RunTimeError("File is closed");
				return false;
			}

			bis = BISlist.get(FileEntry.getInt("stream"));
			
			String buffer = "";

	        try {												// Read a byte
	            	int data = bis.read();
	            	if (data < 0){
	            		FileEntry.putBoolean("eof", true);
	        			}
	            	else{
	                    int p = FileEntry.getInt("position");
	                    FileEntry.putInt("position", ++p);
	            	}
	        		NumericVarValues.set(theValueIndex, (double) data);
	             }
	        	catch (Exception e) {
	        		RunTimeError("Error: ");
	        		return false;
	        }
        	FileTable.set(FileNumber, FileEntry);
	        	
	        return true;
		}
		
		private boolean executeBYTE_READ_BUFFER(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			SyntaxError = true;
			return false;
		}
		if (FileNumber >= FileTable.size()){				// Make sure it is a real file table number
			RunTimeError("Invalid File Number at");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the byte count
		++LineIndex;
		if (!evalNumericExpression()) return false;
		int byteCount = (int) (double) EvalNumericExpressionValue;
		
		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the byte count
		++LineIndex;
		if (!getSVar()) return false;
		
		int FileMode;							//     Variables for the bundle
		boolean eof;
		BufferedInputStream bis = null;
		boolean closed;

		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		eof = FileEntry.getBoolean("eof");
		closed = FileEntry.getBoolean("closed");
		
		boolean isText = FileEntry.getBoolean("isText");
		if (isText){
			RunTimeError("File not opened for byte");
			return false;
		}

		
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}

		bis = BISlist.get(FileEntry.getInt("stream"));
//		ByteArrayBuffer byteArray = new ByteArrayBuffer(byteCount);
		byte[] byteArray = new byte[byteCount];

		int dataByte = 0;
		boolean isFull = false;
		int count = 0;

        try {												// Read a byte
		    
		    count = bis.read(byteArray, 0, byteCount);
             
        	}catch (Exception e) {
        		RunTimeError("Error: " + e);
        		return false;
        	}
        	
        int p = FileEntry.getInt("position");
        
        if (count<0){
        	StringVarValues.set(theValueIndex, "");
        	return true; 
        }
        FileEntry.putInt("position", p + count);        
        FileTable.set(FileNumber, FileEntry);
        
        String buff = new String(byteArray, 0);
        if (count < byteCount){
        	buff = buff.substring(0, count);
        }
        StringVarValues.set(theValueIndex, buff);

        return true;
		}
		
		private  boolean executeBYTE_WRITE_BYTE(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber >= FileTable.size() || FileNumber < 0 ){
			RunTimeError("Invalid File Number at");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the byte var
		++LineIndex;

		byte b = 0;
		boolean OutputIsByte = true;
		if (evalNumericExpression()){
			d = EvalNumericExpressionValue;
			int i = (int) d;
			 b = (byte) i;
		} else{
			if (!evalStringExpression()) return false;
			OutputIsByte = false;
		}
		if (!checkEOL()) return false;
	
		
		int FileMode;							//     Variables for the bundle
		DataOutputStream dos = null;
		boolean closed;

		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		dos = DOSlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
		
		if (FileMode != FMW){						// Varify open for read
			RunTimeError("File not opened for write at");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}
		try {
			if (OutputIsByte){
				dos.writeByte(b);					//Oh, and write the byte
	            int p = FileEntry.getInt("position");
	            ++p;
	            FileEntry.putInt("position", p);
			}else{
				
				for (int k=0; k<StringConstant.length(); ++k){
					byte bb = (byte)StringConstant.charAt(k);
					dos.writeByte(bb);
				}
	            int p = FileEntry.getInt("position");
	            p = p + StringConstant.length();
	            FileEntry.putInt("position", p);
			}
		} catch (IOException e) {
			RunTimeError("I/O error at");
		}
		return true;
		}
		
		private boolean executeBYTE_WRITE_BUFFER(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber >= FileTable.size() || FileNumber < 0 ){
			RunTimeError("Invalid File Number at");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the buffer
		++LineIndex;

		if (!evalStringExpression()) return false;

		if (!checkEOL()) return false;
	
		
		int FileMode;							//     Variables for the bundle
		DataOutputStream dos = null;
		boolean closed;

		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		dos = DOSlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
		
		if (FileMode != FMW){						// Varify open for read
			RunTimeError("File not opened for write at");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}
		try {
				for (int k=0; k<StringConstant.length(); ++k){
					byte bb = (byte)StringConstant.charAt(k);
					dos.writeByte(bb);
				}
	            int p = FileEntry.getInt("position");
	            p = p + StringConstant.length();
	            FileEntry.putInt("position", p);
		} catch (IOException e) {
			RunTimeError("I/O error at");
		}
		return true;
		}

		
		private boolean executeBYTE_POSITION_SET(){
			
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
		if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
		double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}


			if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the position var expression
			++LineIndex;
			if (!evalNumericExpression()){return false;};	
			int pto = (int) (double)EvalNumericExpressionValue;
			if (pto < 1 ){
				RunTimeError("Set position must be >= 1");
				return false;
			}

			if (FileNumber >= FileTable.size()){
				RunTimeError("Invalid File Number at");
				return false;
			}
			
			Bundle FileEntry = new Bundle();		// Get the bundle 
			FileEntry = FileTable.get(FileNumber);
			int FileMode = FileEntry.getInt("mode");
			boolean eof = FileEntry.getBoolean("eof");
			boolean closed = FileEntry.getBoolean("closed");
			
			boolean isText = FileEntry.getBoolean("isText");
			if (isText){
				RunTimeError("File not opened for byte");
				return false;
			}

			
			if (closed){
				RunTimeError("File is closed");
			}
			
			if (FileMode != FMR){						// Varify open for read
				RunTimeError("File not opened for read");
				return false;
			}
			
			BufferedInputStream bis = null;
			bis = BISlist.get(FileEntry.getInt("stream"));
			int pnow = FileEntry.getInt("position");
			
			if (pnow == pto) return true;
			
			if (pto < pnow) {
				try {
					bis.reset();
					FileEntry.putBoolean("eof", false);
					FileEntry.putInt("position", 1);
					FileTable.set(FileNumber, FileEntry);
				}
				catch (Exception e){
					RunTimeError("Error: " + e);
					return false;
				}
				pnow = 1;
				if (pto == 1 ) return true;
			}
			
			int data = -1;
			while (pnow < pto ){
				
		        try {												// Read a byte
	            	data = bis.read();
	            	if (data < 0){
	            		FileEntry.putBoolean("eof", true);
	        			}
	            	else{
	                    FileEntry.putInt("position", ++pnow);
	            	}
	             }
	        	catch (Exception e) {
	        		RunTimeError("Error: ");
	        		return false;
	        	}
	        }

			FileTable.set(FileNumber, FileEntry);
			return true;
		}
		
		private boolean executeBYTE_POSITION_GET(){
			if (FileTable.size() == 0){
				RunTimeError("No files opened");
				return false;
			}
			
			if (!evalNumericExpression()){return false;};						// First parm is the filenumber expression
			double d = EvalNumericExpressionValue;
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}

		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} // Second parm is the position nexp
		++LineIndex;
		if (!getNVar()){return false;};						

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		
		boolean isText = FileEntry.getBoolean("isText");
		if (isText){
			RunTimeError("File not opened for byte");
			return false;
		}

		int p = FileEntry.getInt("position");
		double dp = (double) p;
		NumericVarValues.set(theValueIndex, dp);

			return true;
		}
		
	private boolean executeMKDIR(){
		if (!evalStringExpression())return false;					//get the path
		if (SEisLE) return false;
		if (!checkEOL()) return false;

		  File lbDir = new File(Basic.filePath + "/data/" +StringConstant);
		  lbDir.mkdirs();
		  if (!lbDir.exists()){									// did we get a dir?
			  RunTimeError(StringConstant + " makdr failed");
			  return false;
		  }
		
		return true;
	}
	
	private boolean executeRENAME(){
		if (!evalStringExpression())return false;					//get the old file name
		if (SEisLE) return false;
		String Old = StringConstant;
		if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
		++LineIndex;
		if (!evalStringExpression())return false;					//get the old file name
		if (SEisLE) return false;
		String New = StringConstant;
		if (!checkEOL()) return false;

		
		File sdDir = null;
		File lbDir = null;

	     		String state = Environment.getExternalStorageState();
		    	if (!Environment.MEDIA_MOUNTED.equals(state)) {
		    		RunTimeError("SDCRD not available.");
	        		return false;
	        	}
				  	 sdDir = new File(Basic.filePath + "/data/"  );
			  		 lbDir = new File(sdDir.getAbsoluteFile() + "/" +  Old);
		  if (!lbDir.exists()){									// did we get a dir?
			  RunTimeError(Old + " directory/file not in this path");
			  return false;
		  }
		File newFile = new File(sdDir.getAbsoluteFile() + "/" + New);
      	boolean success = lbDir.renameTo(newFile);
      	
      	if (!success) {
      		RunTimeError("Rename of " + Old + " to " + New + " failed");
      		return false;
      	}
      		
		
		return true;
	}
	
	private boolean executeDELETE(){

		  if (!getNVar()) return false;						// get the var to put the size value into
		  int SaveValueIndex = theValueIndex;
		  
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
		  ++LineIndex;

		
		if (!evalStringExpression())return false;					//get the old file name
		if (SEisLE) return false;
		if (!checkEOL()) return false;

		File sdDir = null;
		File lbDir = null;

	     		String state = Environment.getExternalStorageState();
		    	if (!Environment.MEDIA_MOUNTED.equals(state)) {
		    		RunTimeError("SDCRD not available.");
	        		return false;
	        	}
				  	 sdDir = new File(Basic.filePath + "/data/"  );
			  		 lbDir = new File(sdDir.getAbsoluteFile() + "/" +  StringConstant);
	/*		  	 
		  if (!lbDir.exists()){									// did we get a dir?
			  RunTimeError(StringConstant + " delete failed");
			  return false;
		  }
*/		  
		  double result = 0;
		  if (lbDir.delete()) result = 1;
		  
		  NumericVarValues.set(SaveValueIndex, result);

		
		return true;
	}
	
	private boolean executeFILE_EXISTS(){
		if (!getNVar()){return false;}				// Next parameter if the FileNumber variable
		int saveValueIndex = theValueIndex;
		
		if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
		++LineIndex;

		if (!evalStringExpression()) return false;
		if (SEisLE) return false;
		if (!checkEOL()) return false;


 		String state = Environment.getExternalStorageState();
    	if (!Environment.MEDIA_MOUNTED.equals(state)) {
    		RunTimeError("SDCARD not available.");
    		return false;
    	}
	  	File lbDir = new File(Basic.filePath +   "/data/" + StringConstant);

	  	double  exists = 0;								// Assume file does not exists
	  	if (lbDir.exists()) exists = 1;
		NumericVarValues.set(saveValueIndex, exists);
		return true;
	}
	
	private boolean executeFILE_SIZE(){
		  if (!getNVar()) return false;						// get the var to put the size value into
		  int SaveValueIndex = theValueIndex;
	
		  
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
		  ++LineIndex;

		
		if (!evalStringExpression())return false;					//get the old file name
		if (SEisLE) return false;
		if (!checkEOL()) return false;

		File sdDir = null;
		File lbDir = null;

	     		String state = Environment.getExternalStorageState();
		    	if (!Environment.MEDIA_MOUNTED.equals(state)) {
		    		RunTimeError("SDCRD not available.");
	        		return false;
	        	}
				  	 sdDir = new File(Basic.filePath + "/");
			  		 lbDir = new File(sdDir.getAbsoluteFile() + "/" + "/data/" +  StringConstant);
			  	 
		  if (!lbDir.exists()){									// did we get a dir?
			  RunTimeError(StringConstant + " not found");
			  return false;
		  }
		  
		  long size = lbDir.length();
		  NumericVarValues.set(SaveValueIndex, (double) size); // Put the item selected into the var
		
		return true;
	}
	
	private boolean executeFILE_ROOTS(){
		
		File lbDir = null;


 		String state = Environment.getExternalStorageState();
    	if (!Environment.MEDIA_MOUNTED.equals(state)) {
    		RunTimeError("SDCRD not available.");
    		return false;
    	}
	  	
	  	lbDir = new File(Basic.filePath +"/data/");
	    String s = "";
	    
	  	try {s = lbDir.getCanonicalPath();} catch(IOException e) {};
	  	
	  	if (!getVar()) return false;
	  	if (VarIsNumeric) return false;
	  	StringVarValues.set(theValueIndex, s);
		if (!checkEOL()) return false;


		return true;
	}

	
	private boolean executeDIR(){
	  	ArrayList <String> FL1 = new ArrayList<String>();
	  	ArrayList <String> DL1 = new ArrayList<String>();

		if (!evalStringExpression())return false;					//get the path
		if (SEisLE) return false;

	  	String FL[] = null;
	 
		  File lbDir = new File(Basic.filePath + "/data/"  + StringConstant);
			  		
		  if (!lbDir.exists() || lbDir == null){									// did we get a dir?
			  RunTimeError(StringConstant + " is invalid path");
			  return false;
		  }
          
		  FL = lbDir.list();									// get the list of files in the dir
		  String F[] = {" "};
		  if (FL == null){
			  FL = F;
		  }
		  int FLlength = FL.length;
		  
		  int m = FL.length;
//		  DL1.add("..");												// put  the ".." to the top of the list
		  
		  // Go through the file list and mark directory entries with (d)
		  
		  for (int i=0; i<m; ++i){
			  String s = FL[i];
			  File test = new File(lbDir.getAbsoluteFile() + "/" + s);  // If files is a directory, add "(d)"
			  if (test.isDirectory()){
				  s = s + "(d)";
				  DL1.add(s);											// and add to display list
			  } else {
				  FL1.add(s);											// else add name without the (d)
			  	}
		  	}
		  
		  Collections.sort(DL1);										// Sort the directory list
		  Collections.sort(FL1);                                        // Sort the file list
		  for (int i=0; i<FL1.size(); ++i){									// copy the file list to end of dir list
			  DL1.add(FL1.get(i));
		  	}
		  FL1 = DL1;

		  														// parse the ,D[]
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
		  ++LineIndex;
		  doingDim = true;
		  if (!getVar()){SyntaxError(); return false;}
		  doingDim = false;
		  if (VarIsNumeric){SyntaxError(); return false;} 
		  if (!VarIsArray){SyntaxError(); return false;}
		  if (!VarIsNew){
			  RunTimeError("DIR array must not be DIMed");
			  return false;
		  }
		  if (ExecutingLineBuffer.charAt(LineIndex)!=']'){SyntaxError(); return false;}
			if (!checkEOL(true)) return false;

		  
		  ArrayList <Integer> dimValues = new ArrayList<Integer>();  // Build the D[]
		  
		  if (FLlength == 0) dimValues.add(1);              // make at least one element if dir is empty
		  													// If dir is empty, the one element will be an
		  													// empty string
		  else dimValues.add(FLlength);
		  
		  BuildBasicArray(VarNumber, false, dimValues);
		  
		  for (int i = 0; i<FLlength; ++i){								// stuff the array
			  String s = FL1.get(i);
			  StringVarValues.set(ArrayValueStart, s);
			  ++ArrayValueStart;
		  }
		  
		  
		  
		return true;
	}
	
	private boolean executeGRABFILE(){
		if (!getVar()){return false;};		                           // First parm is string var				
		if (VarIsNumeric){return false;}
		int saveVarIndex = theValueIndex;
		
		if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} 
		++LineIndex;

		if (!evalStringExpression()){return false;}                    // Second parm is the filename               
	    if (SEisLE) return false;
	    String theFileName = StringConstant;
		if (!checkEOL()) return false;

	    
	    String result = null;

     	String state = Environment.getExternalStorageState();   // Make sure the SD is mounted
	    if (!Environment.MEDIA_MOUNTED.equals(state)) {
	    	RunTimeError("SDCRD not available.");
        	return false;
        	}
		  
		File file = null;
        String  FullFileName = null;
		FullFileName = Basic.filePath + "/data";

        if (Basic.DataPath.length() != 0){ 
			FullFileName = FullFileName + Basic.DataPath + "/" + theFileName;  // Add the filename to the base path
        }else{
			FullFileName = FullFileName + "/" + theFileName;
        }

		  file = new File(FullFileName);
		  BufferedInputStream bis;
	
		  try{                                      // Open the reader for the SD Card
			  FileInputStream fis = new FileInputStream(file);
//		      dis = new DataInputStream(fis);
		      bis = new BufferedInputStream(fis);
		  }				
		  catch (Exception e) {
				RunTimeError("Error: " + e );
			  return false;												// as the file table number
			  };

	    // Construct a String object from the byte array containing the response
	    try{
		    ByteArrayBuffer byteArray = new ByteArrayBuffer(1024*8);
		    int current = 0;
		    while((current = bis.read()) != -1){
		    byteArray.append((byte)current);
		    }
		    result = new String(byteArray.toByteArray(),0);

	    } catch (Exception e) {
			RunTimeError("Error: " + e );
			   return false;
		   };
	   
	    StringVarValues.set(saveVarIndex, result);

		return true;
	}
	
	  private boolean executeGRABURL(){
		  
			if (!getVar()){return false;};		                           // First parm is string var				
			if (VarIsNumeric){return false;}
			int saveVarIndex = theValueIndex;
			
			if (ExecutingLineBuffer.charAt(LineIndex)!=','){return false;} 
			++LineIndex;

			if (!evalStringExpression()){return false;}                    // Second parm is the url               
		    if (SEisLE) return false;
			if (!checkEOL()) return false;


		    
		    String result = null;

		    try {
		    // This assumes that you have a URL from which the response will come
		    URL url = new URL(StringConstant);

		    // Open a connection to the URL and obtain a buffered input stream
		    URLConnection connection = url.openConnection();
		    InputStream inputStream = connection.getInputStream();
		    
		    BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);

		    // Read the response into a byte array
		    ByteArrayBuffer byteArray = new ByteArrayBuffer(50);
		    int current = 0;
		    while((current = bufferedInput.read()) != -1){
		    byteArray.append((byte)current);
		    }

		    // Construct a String object from the byte array containing the response
		    result = new String(byteArray.toByteArray());
		    
		    }
		    catch (Exception e) {
				RunTimeError("Error: " + e );
				   return false;
			   };

		   
		    StringVarValues.set(saveVarIndex, result);

		  return true;
	  }

	  
	// ************************************** Miscellaneous Non-core commands **************************
	
	private boolean executeTIME(){
		
		Time time = new Time();
		time.setToNow ();
		String zone = time.getCurrentTimezone ();
		String theTime = time.format("%Y%m%dT%H%M%S");
		String year = theTime.substring(0, 4);
		String month = theTime.substring(4, 6);
		String day = theTime.substring(6, 8);
		String hour = theTime.substring(9, 11);
		String minute = theTime.substring(11, 13);
		String second = theTime.substring(13, 15);
		
		   if (!getVar())return false;										// year
		   if (VarIsNumeric)return false;						
		   StringVarValues.set(theValueIndex, year);
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
			
		   if (!getVar())return false;										// month
		   if (VarIsNumeric)return false;						
		   StringVarValues.set(theValueIndex, month);
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
			
		   if (!getVar())return false;										// day
		   if (VarIsNumeric)return false;						
		   StringVarValues.set(theValueIndex, day);
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
			
		   if (!getVar())return false;										// hour
		   if (VarIsNumeric)return false;						
		   StringVarValues.set(theValueIndex, hour);
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
			
		   if (!getVar())return false;										// minute
		   if (VarIsNumeric)return false;						
		   StringVarValues.set(theValueIndex, minute);
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
			
		   if (!getVar())return false;										// second
		   if (VarIsNumeric)return false;						
		   StringVarValues.set(theValueIndex, second);
		   
			if (!checkEOL()) return false;
		
		return true;
	}
		
	  private boolean executePAUSE(){
			 if (!evalNumericExpression())return false;							// Get pause duration value
		     double d = EvalNumericExpressionValue;
		     if (d<1){
		    	 RunTimeError("Pause must be greater than zero");
		    	 return false;
		     }
			if (!checkEOL()) return false;

		  
		  try {Thread.sleep((int) d);}catch(InterruptedException e){};

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
      	try {Thread.sleep(500);}catch(InterruptedException e){};              // Sleep here stopped forced stop exceptions

		  try {startActivity(i);}
//		  catch ( android.content.ActivityNotFoundException  e){
		  catch ( Exception  e){
			  RunTimeError("Error: " + e);
			  return false;
		  };
		  return true;
	  }
	  
	  
	  private boolean executeINKEY(){
		  
		  if (!getVar()) return false;						// get the var to put the key value into
		  if (VarIsNumeric) return false;					
		  if (!checkEOL()) return false;
		  if (InChar.size() > 0){
			  StringVarValues.set(theValueIndex, InChar.get(0));
			  InChar.remove(0);
		  } else
			  StringVarValues.set(theValueIndex, "@");
		  return true;
	  }
	  
	  private boolean executeONKEY_RESUME(){
		  if (OnKeyResume == -1 ) {
			  RunTimeError("No Current Key Interrupt");
			  return false;
		  }
		  ExecutingLineIndex = OnKeyResume ;
		  OnKeyResume = -1;
		  return true;
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

		  if (!getVar()) return false;						// get the var to put the key value into
		  if (!VarIsNumeric) return false;					
		  int SaveValueIndex = theValueIndex;
		  
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
		  ++LineIndex;
		  
		  int saveLineIndex = LineIndex;
		  SkipArrayValues = true;

		  if (evalNumericExpression()){
			    SkipArrayValues = false;
				int listIndex = (int) (double)EvalNumericExpressionValue;
				if (listIndex < 1 || listIndex >= theLists.size()){
					RunTimeError("Invalid List Pointer");
					return false;
				}
				if (theListsType.get(listIndex) != list_is_string){
					RunTimeError("Not a string list");
					return false;
				}
				SelectList = theLists.get(listIndex);
				
		  }else{
			  LineIndex = saveLineIndex;
			  doingDim = false;									// Get the array variable
			  SkipArrayValues = true;
			  if (!getVar()){
				  SyntaxError(); 
				  SkipArrayValues = false; 
				  return false;
			  	}
			  SkipArrayValues = false;
			  doingDim = false;
			  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
			  if (VarIsNumeric) return false;
			  if (VarIsNew){									// and that it has been DIMed
				  RunTimeError("Array not DIMed");
				  return false;
			  }
		  
			  if (ExecutingLineBuffer.charAt(LineIndex)!=']'){SyntaxError(); return false;}
			  ++LineIndex;

		  
			  Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
			  ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));   
			  int length = ArrayEntry.getInt("length");               // get the array length
			  int base = ArrayEntry.getInt("base");                   // and the start of values in the value space
			
			  SelectList = new ArrayList<String>();    // Create a list to copy array values into
				
			  for (int i =0; i<length; ++i){                          // Copy the array values into that list
				  SelectList.add(StringVarValues.get(base+i));
			  }
			
		  }
			
			if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
			++LineIndex;

			if (!evalStringExpression()) return false;
			if (SEisLE) return false;
//			if (!checkEOL()) return false;

			
			SelectMessage = StringConstant ;
			if (SelectMessage == null ) SelectMessage = "";
			
			SelectedItem = 0;
			ItemSelected = false;
			SelectLongClick = false;
			
		    startActivityForResult(new Intent(this, Select.class), BASIC_GENERAL_INTENT);

			
		    while (!ItemSelected) Thread.yield();				    // Wait for signal from Selected.java thread
		    
		    NumericVarValues.set(SaveValueIndex, (double) SelectedItem); // Put the item selected into the var
		    
			if (ExecutingLineBuffer.charAt(LineIndex)!=','){return checkEOL();} // If no comma then not long press var
			++LineIndex;
			
			if (!getNVar()) return false;									// Get the long press var
			double isLongPress = 0;											// Get the actual value
			if (SelectLongClick) isLongPress = 1;
			NumericVarValues.set(theValueIndex, (double) isLongPress);		// Set the return value	

		  
			return checkEOL();
	  }
	  
	  private boolean executeSPLIT(){
		  
		  doingDim = true;                                       // Get the result array variable
		  if (!getVar()){SyntaxError(); return false;}           
		  doingDim = false;
		  if (VarIsNumeric){SyntaxError(); return false;} 
		  if (!VarIsArray){SyntaxError(); return false;}
		  if (!VarIsNew){
			  RunTimeError("SPLIT array must not be DIMed");
			  return false;
		  }
		  if (ExecutingLineBuffer.charAt(LineIndex)!=']'){SyntaxError(); return false;}
		  int SaveArrayVarNumber = VarNumber;
			++LineIndex;
		  
			if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
			++LineIndex;
			
			if (!evalStringExpression()) return false;    // Get the string to split
			if (SEisLE) return false;
			String SearchString = StringConstant ;

			if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;}
			++LineIndex;
			
			if (!evalStringExpression()) return false;    // Get the regular expression string
			if (SEisLE) return false;
			String REString = StringConstant ;
			if (!checkEOL()) return false;
			
			String r[] = new String[1];
			try {
				r = SearchString.split(REString);              // split the string
			}
				catch (Exception e){
					RunTimeError("Error: " + e);
					return false;
			}
							
			int length = Array.getLength(r);              // Get the number of strings generated
			
			if (length == 0){
				RunTimeError(REString + " is invalid argument at");
				return false;
			}
			
			ArrayList <Integer> dimValues = new ArrayList<Integer>();  // Set that number as the dimension of the array
			dimValues.add(length);
		  
		  BuildBasicArray(SaveArrayVarNumber, false, dimValues);   // Now go build the an array of the specified size
		  
		  for (int i = 0; i<length; ++i){						  // now stuff the array
			  StringVarValues.set(ArrayValueStart, r[i]);
			  ++ArrayValueStart;
		  }

		  return true;
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
		  
		  int code  = (int) (double) EvalNumericExpressionValue;
		  
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
		  
		    double duration = 1; // seconds
		    double freqOfTone = 1000; // hz
//		    int sampleRate = 44100;
		    int sampleRate = 8000;

		  if (!evalNumericExpression())return false;							// Get frequency
		  freqOfTone =  EvalNumericExpressionValue;
		  
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get duration
		  duration= EvalNumericExpressionValue/1000;
		  
	    	double dnumSamples = duration * sampleRate;
	    	dnumSamples = Math.ceil(dnumSamples);
	    	int numSamples = (int) dnumSamples;
	    	double sample[] = new double[numSamples];
	    	byte generatedSnd[] = new byte[2 * numSamples];
	    	
	    	int minBuffer = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
        			AudioFormat.ENCODING_PCM_16BIT);
	    	if (numSamples< minBuffer){
	    		double minDuration = Math.ceil(1000 * (double)minBuffer/(double)sampleRate);
	    		RunTimeError("Minimum tone duration for this device: " + (int) minDuration + " milliseconds");
	    		return false;
	    	}

	        // fill out the array
	        for (int i = 0; i < numSamples; ++i) {
	            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
	        }

	        // convert to 16 bit pcm sound array
	        // assumes the sample buffer is normalized.
	        // convert to 16 bit pcm sound array
	        // assumes the sample buffer is normalised.
	        int idx = 0;
	        for (final double dVal : sample) {
	            // scale to maximum amplitude
	            final short val = (short) ((dVal * 32767));
	            // in 16 bit wav PCM, first byte is the low order byte
	            generatedSnd[idx++] = (byte) (val & 0x00ff);
	            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
	        }
	        
	        AudioTrack audioTrack = null;
	        try {
	        	audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
	        			sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
	        			AudioFormat.ENCODING_PCM_16BIT, (int)numSamples,
	        			AudioTrack.MODE_STATIC);
	        	audioTrack.write(generatedSnd, 0, generatedSnd.length);
	        	audioTrack.play();
	        }
	        catch (Exception e){
	        	RunTimeError("Error: " + e);
	        	return false;
	        }
	        int x =0;
	        do{
	        	 if (audioTrack != null) 
	        		 x = audioTrack.getPlaybackHeadPosition(); 
	        	 else x = numSamples;	        }while (x<numSamples/2);
	        
	        if (audioTrack != null) audioTrack.release();

	        return true;
	  }
	  
	  private boolean executeVIBRATE(){

			doingDim = false;									// Get the array variable
			  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
			  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
			  SkipArrayValues = false;
			  doingDim = false;
			  
			  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
			  if (!VarIsNumeric){								// and that it is a numeric array
				  RunTimeError("Array not numeric");
				  return false;
			  }
			  if (VarIsNew){									// and that it has been DIMed
				  RunTimeError("Array not DIMed");
				  return false;
			  }
			  
				Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
				ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
				int length = ArrayEntry.getInt("length");				// get the array length
				int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space
				
				long Pattern[];
				Pattern = new long[length];								// Pattern array
				
				for (int i = 0; i<length; ++i){							// Copy user array into pattern
					double d = NumericVarValues.get(base+i);
					Pattern[i] = (long) d;
				}
				if (ExecutingLineBuffer.charAt(LineIndex) != ']')return false; // Get the repeat value 
				++LineIndex;
				
				if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false; // Get the repeat value 
				++LineIndex;
				
				if(!evalNumericExpression()) return false;
				double d = EvalNumericExpressionValue;
				if (!checkEOL()) return false;
				
				if (d>0) myVib.cancel(); else myVib.vibrate( Pattern, (int) d) ; // Do the vibrate
				
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
		   int theListIndex = (int) (double)EvalNumericExpressionValue;
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
		    	} 

		   if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		   ++LineIndex;		   
		   if (!getSVar()) return false;		   
		   StringVarValues.set(theValueIndex, Result);
		   
		   if (!checkEOL()) return false;

		  
		  return true;
	  }
	  
	  
	  // ************************************************ SQL Package ***************************************	  


     public  boolean executeSQL(){
    	if (!GetSQLKeyWord()){
    	    	  return false;
    	      }else {
    	    	  switch (KeyWordValue){
    	    	  	case sql_open:
    	    	  		if (!execute_sql_open()){return false;};
    	    	  		break;
    	    	  	case sql_close:
    	    	  		if (!execute_sql_close()){return false;};
    	    	  		break;
    	    	  	case sql_insert:
    	    	  		if (!execute_sql_insert()){return false;};
    	    	  		break;
    	    	  	case sql_query:
    	    	  		if (!execute_sql_query()){return false;};
    	    	  		break;
    	    	  	case sql_next:
    	    	  		if (!execute_sql_next()){return false;};
    	    	  		break;
    	    	  	case sql_delete:
    	    	  		if (!execute_sql_delete()){return false;};
    	    	  		break;
    	    	  	case sql_update:
    	    	  		if (!execute_sql_update()){return false;};
    	    	  		break;
    	    	  	case sql_exec:
    	    	  		if (!execute_sql_exec()){return false;};
    	    	  		break;
    	    	  	case sql_raw_query:
    	    	  		if (!execute_sql_raw_query()){return false;};
    	    	  		break;
    	    	  	case sql_drop_table:
    	    	  		if (!execute_sql_drop_table()){return false;};
    	    	  		break;
    	    	  	case sql_new_table:
    	    	  		if (!execute_sql_new_table()){return false;};
    	    	  		break;
    	    	  	default:
    	    	  		return false;
    	    	  }
    	    	  return true;
    	      }
    	   }

    	   
    	  private  boolean GetSQLKeyWord(){							// Get a Basic key word if it is there
    																// is the current line index at a key word?
    			String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
    			int i = 0;
    			for (i = 0; i<SQL_kw.length; ++i){					// loop through the key word list
    				if (Temp.startsWith(SQL_kw[i])){    			// if there is a match
    					KeyWordValue = i;							// set the key word number
    					LineIndex = LineIndex + SQL_kw[i].length(); // move the line index to end of key word
    					return true;								// and report back
    				};
    			};
    			KeyWordValue = sql_none;							// no key word found
    			return false;										// report fail

    		}
    	   
    	  private boolean execute_sql_open(){

    		   if (!getVar())return false;							// DB Pointer Variable
    		   if (!VarIsNumeric)return false;						// for the DB table pointer
    		   int SaveValueIndex = theValueIndex;

    		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
    		   ++LineIndex;
    		   
    		   if (!evalStringExpression())return false;							// Get Data Base Name
    		   if (SEisLE) return false;
    		   String DBname = StringConstant;
    		   
    			if (!checkEOL()) return false;
    			
    	     	String state = Environment.getExternalStorageState();   // Make sure the SD is mounted
    	    	if (!Environment.MEDIA_MOUNTED.equals(state)) {
    	    		RunTimeError("SDCARD not available.");
    	    		return false;
    	    	}

    		   String fn;
    		   if (DBname.startsWith(":"))  fn = DBname;
    		   else fn = Basic.filePath + "/databases/" +DBname; 
    		    
    		   File theFile = new File(fn);

    		   SQLiteDatabase db;
    		   
    		   try{															// Do the open or create
    			   db = SQLiteDatabase.openOrCreateDatabase(theFile, null );
    		   } catch  (Exception e){
    			   RunTimeError("SQL Exception: "+e);
    			   return false;
    		   }
    		   
    		   // The newly opened data base is added to the DataBases list.
    		   // The list index of the new data base is added returned to the user

    		   NumericVarValues.set(SaveValueIndex, (double) DataBases.size()+1);
    		   DataBases.add(db);
    		   return true;
    	   }
    	   
    	  private boolean execute_sql_close(){
    		  
    		  if (DataBases.isEmpty()){						// If no databases have been opened
    			  RunTimeError("Database not opened at:");
    			  return false;
    		  }
    		  
   		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);
			if (!checkEOL()) return false;

		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened at:");					// DB has been prviously closed
			  return false;		   
		   }
		   
		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base
		   try {
			   db.close();										   // Try closeing it
		   }catch (Exception e) {
				RunTimeError("Error: " + e );
			   return false;
		   }
		   
		   NumericVarValues.set(theValueIndex, 0.0);			// Set the pointer to 0 to indicate closed.
		   
    	   return true;
    		  
    	  }

    	  private boolean execute_sql_insert(){
    		  
 		  if (DataBases.isEmpty()){								// If no databases have been opened...
 			 RunTimeError("Database not opened at:");
			  return false;
		  }

  		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);

		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened at:");					// DB has been closed
			  return false;		   
		   }
		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!evalStringExpression())return false;			   // Table Name
		   if (SEisLE) return false;
		   String TableName = StringConstant;

	       ContentValues values = new ContentValues();
	       
	       // Get column/value pairs from user command
	       
		   char c;
		   String Column;
		   String Value;
		   if (ExecutingLineBuffer.charAt(LineIndex)!=',') return false;
		   ++LineIndex;
		   
		   do{
    		   if (!evalStringExpression())return false;						// Columns
    		   if (SEisLE) return false;
    		   Column = StringConstant;

    		   if (ExecutingLineBuffer.charAt(LineIndex)!=',') return false;
    		   ++LineIndex;

    		   if (!evalStringExpression())return false;						// Values
    		   if (SEisLE) return false;
    		   Value = StringConstant;

    		   values.put(Column, Value);										// save the pair

    	       c = ExecutingLineBuffer.charAt(LineIndex);
    		   ++LineIndex;
		   } while (c == ',');

		   try {													// Now insert the pairs into the named table
	        db.insertOrThrow(TableName, null, values);
		   }catch (Exception e) {
				RunTimeError("Error: " + e );
			   return false;
		   }

		   return true;
   	   }
    	  
       private boolean execute_sql_query(){
    	   
  		  if (DataBases.isEmpty()){								// If no databases opened ..
  			RunTimeError("Invalid Database Pointer at:");
			  return false;
		  }

		   if (!getVar())return false;							// Quary Cursor Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

  		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);

		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Invalid Database Pointer at:");					// DB has been closed
			  return false;		   
		   }
		   
		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalStringExpression())return false;			// Table Name
		   if (SEisLE) return false;
		   String TableName = StringConstant;
		   
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalStringExpression())return false;			// String of comma seperated columns to get
		   if (SEisLE) return false;
		   String RawColumns = StringConstant;
		   
		   ArrayList<String> cList = new ArrayList<String>();	// Must convert string to an array of columns
		   														// starting by creating an Array List of column names
		   int i = 0;
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
		   
		   if (ExecutingLineBuffer.charAt(LineIndex) == ','){	// if no comma, then no optional Where
			   ++LineIndex;
			   if (!evalStringExpression())return false;		// Where Value
			   if (SEisLE) return false;
			   Where = StringConstant;
			   
			   if (ExecutingLineBuffer.charAt(LineIndex) == ','){	// if no comma, then no optional Where
				   ++LineIndex;
				   if (!evalStringExpression())return false;		// Where Value
				   if (SEisLE) return false;
				   Order = StringConstant;
			   }
			   
		   }

		   Cursor cursor;
		   try{													// Do the quary and get the cursor
	           cursor = db.query(TableName, Q_Columns, Where, null, null,
	              null, Order);
		   } catch (Exception e) {
				RunTimeError("Error: " + e );

			   return false;
		   }
		   
		   NumericVarValues.set(SaveValueIndex, (double) Cursors.size()+1); // Save the Cursor index into the var
		   Cursors.add(cursor);												// and save the cursor.
		   
		   return true;
       }

       private boolean execute_sql_next(){

    	   if (Cursors.isEmpty()) {								// Make sure there is at least one cursor
    		   RunTimeError("Cursor not available at:");
			  return false;
		  }
    	   
		   if (!getVar())return false;							// Done Flag Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveDoneIndex = theValueIndex;
		   NumericVarValues.set(SaveDoneIndex, 0.00);           // set Not Done
		   if (!isNext(',')) return false;

  		   if (!getVar())return false;							// DB Cursor Variable
		   if (!VarIsNumeric)return false;						// for the Cursor pointer
		   double d = NumericVarValues.get(theValueIndex);
		   int SaveCursorIndex = theValueIndex;
		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Cursor done at:");							// then cursor is used up
			  return false;		   
		   }
		   int I = (int) d;
		   Cursor cursor = Cursors.get(I-1);					// get the cursor

		   String result;
		   if (cursor.moveToNext()) {							// if there is another row, go there
			   for (int index = 0; isNext(','); ++index) {
				   if (!getVar())return false;							// Get next result variable
				   if (VarIsNumeric)return false;						// 
				   try{
				   result = cursor.getString(index); 			// get the result
				   } catch (Exception e) {
						RunTimeError("Error: " + e );
					   return false;
				   }
				   if (result == null) { result = ""; }
				   StringVarValues.set(theValueIndex, result);			// set result into var
			   }
		   } else {												// no next row, cursor is used up
			   cursor.close();
			   NumericVarValues.set(SaveDoneIndex, 1.00);
			   NumericVarValues.set(SaveCursorIndex, 0.0);
		   }
		   
    	   return true;
       }

       private boolean execute_sql_delete(){

    	   if (DataBases.isEmpty()){
    		   RunTimeError("Database not opened at:");
			  return false;
		  }
		  
		   if (!getVar())return false;							// DB Pointer Variable
	   if (!VarIsNumeric)return false;						    // for the DB table pointer
	   double d = NumericVarValues.get(theValueIndex);

	   if (d == 0.0) {										// If pointer is zero
		   RunTimeError("Database not opened at:");					// DB has been closed
		  return false;		   
	   }
	   int I = (int) d;
	   SQLiteDatabase db = DataBases.get(I-1);					// get the data base
	   
	   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	   ++LineIndex;
	   if (!evalStringExpression())return false;			// Table Name
	   if (SEisLE) return false;
	   String TableName = StringConstant;

	   String Where = null;									// if no Where given, set null
	   if (ExecutingLineBuffer.charAt(LineIndex) == ','){	// if no comma, then no optional Where
		   ++LineIndex;
		   if (!evalStringExpression())return false;		// Where Value
		   Where = StringConstant;
	   }
	   try {
		   db.delete(TableName, Where, null);					// do the deletes
	   }
	   catch (Exception e){
		   RunTimeError("Error: " + e);
	   };

	   return true;
       }

       private boolean execute_sql_update() {

  		  if (DataBases.isEmpty()){
  			RunTimeError("Database not opened at:");
			  return false;
		  }

  		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);
		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened at:");					// DB has been closed
			  return false;		   
		   }

		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!evalStringExpression())return false;			// Table Name
		   if (SEisLE) return false;
		   String TableName = StringConstant;

	       ContentValues values = new ContentValues();
	       
		   char c;
		   String Column;
		   String Value;
		   if (ExecutingLineBuffer.charAt(LineIndex)!=',') return false;
		   ++LineIndex;
		   do{
    		   if (!evalStringExpression())return false;						// Columns
    		   if (SEisLE) return false;
    		   Column = StringConstant;
    		   if (ExecutingLineBuffer.charAt(LineIndex)!=',') return false;
    		   ++LineIndex;
    		   if (!evalStringExpression())return false;						// Values
    		   if (SEisLE) return false;
    		   Value = StringConstant;
    	       values.put(Column, Value);
    		   c = ExecutingLineBuffer.charAt(LineIndex);
    		   ++LineIndex;
		   } while (c == ',');
		   
		   String Where = null;									// Where is optional
		   if (c == ':'){										// Colon indicates Where follows
			   if (!evalStringExpression())return false;		// Where Value
			   if (SEisLE) return false;
			   Where = StringConstant;
		   }
		   
		   try {
	        db.update(TableName, values, Where, null);
		   }catch (Exception e) {
				RunTimeError("Error: " + e );
			   return false;
		   }

    	   
    	   return true;
       }
       
       private boolean execute_sql_exec(){
    	   
   		  if (DataBases.isEmpty()){
   			RunTimeError("Database not opened at:");
			  return false;
		  }

  		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);
		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened");					// DB has been closed
			  return false;		   
		   }

		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!evalStringExpression())return false;			// Command string
		   String CommandString = StringConstant;

		   try {
		        db.execSQL(CommandString);
			   } catch (Exception e){
				   RunTimeError("SQL Exception: " + e);
				   return false;
			   }

    	   return true;
       }
       
      private boolean execute_sql_raw_query(){
  		  if (DataBases.isEmpty()){
  			RunTimeError("Database not opened at:");
			  return false;
		  }

		   if (!getVar())return false;							// Quary Cursor Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

  		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);
		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened at:");					// DB has been closed
			  return false;		   
		   }

		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!evalStringExpression())return false;			// Command string
		   if (SEisLE) return false;
		   String QuaryString = StringConstant;

		   Cursor cursor;
		   try{													// Do the quary and get the cursor
	           cursor = db.rawQuery(QuaryString, null);
		   }catch (Exception e) {
				RunTimeError("Error: " + e );
			   return false;
		   }
		   
		   NumericVarValues.set(SaveValueIndex, (double) Cursors.size()+1); // Save the Cursor index into the var
		   Cursors.add(cursor);												// and save the cursor.

		   
    	  return true;
      }
      
      private boolean execute_sql_drop_table(){

  		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);
		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened at:");					// DB has been closed
			  return false;		   
		   }

		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!evalStringExpression())return false;			// Table Name
		   if (SEisLE) return false;
		   String TableName = StringConstant;

		   String CommandString = StringConstant;
		   
		   CommandString = "DROP TABLE IF EXISTS " + TableName;

		   try {
		        db.execSQL(CommandString);
			   }catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   }
		   
    	  return true;
      }

      private boolean execute_sql_new_table(){

 		   if (!getVar())return false;							// DB Pointer Variable
		   if (!VarIsNumeric)return false;						// for the DB table pointer
		   double d = NumericVarValues.get(theValueIndex);
		   if (d == 0.0) {										// If pointer is zero
			   RunTimeError("Database not opened at:");					// DB has been closed
			  return false;		   
		   }

		   int I = (int) d;
		   SQLiteDatabase db = DataBases.get(I-1);					// get the data base

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   if (!evalStringExpression())return false;			// Table Name
		   String TableName = StringConstant;

		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;
		   char c;
		   ArrayList <String> Columns = new ArrayList<String>();
		   do{
    		   if (!evalStringExpression())return false;						// Columns
    		   if (SEisLE) return false;
    		   Columns.add(StringConstant);
    		   c = ExecutingLineBuffer.charAt(LineIndex);
    		   ++LineIndex;
		   } while (c == ',');
		   
		   String columns = "";
		   int cc = Columns.size();
		   for (int i =0; i<cc; ++i){
			   columns = columns + Columns.get(i) + " TEXT";
			   if (i != cc-1) columns = columns +  " , ";
		   }
		   
		   String CommandString = StringConstant;	   
		   CommandString = "CREATE TABLE " + TableName + "( "
		   + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " 
		   + columns + " )";

		   try {
		        db.execSQL(CommandString);
			   }catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
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
   	    	  		if (!execute_gr_open()){return false;};
   	    	  		break;
   	    	  	case gr_render:
   	    	  		if (!execute_gr_render()){return false;};
   	    	  		break;
   	    	  	case gr_color:
   	    	  		if (!execute_gr_color()){return false;};
   	    	  		break;
   	    	  	case gr_line:
   	    	  		if (!execute_gr_line()){return false;};
   	    	  		break;
   	    	  	case gr_rect:
   	    	  		if (!execute_gr_rect()){return false;};
   	    	  		break;
   	    	  	case gr_arc:
   	    	  		if (!execute_gr_arc()){return false;};
   	    	  		break;
   	    	  	case gr_circle:
   	    	  		if (!execute_gr_circle()){return false;};
   	    	  		break;
   	    	  	case gr_oval:
   	    	  		if (!execute_gr_oval()){return false;};
   	    	  		break;
   	    	  	case gr_cls:
   	    	  		if (!execute_gr_cls()){return false;};
   	    	  		break;
   	    	  	case gr_hide:
   	    	  		if (!execute_gr_hide()){return false;};
   	    	  		break;
   	    	  	case gr_show:
   	    	  		if (!execute_gr_show()){return false;};
   	    	  		break;
   	    	  	case gr_touch:
   	    	  		if (!execute_gr_touch(0)){return false;};
   	    	  		break;
   	    	  	case gr_touch2:
   	    	  		if (!execute_gr_touch(1)){return false;};
   	    	  		break;
  	    	  	case gr_text_draw:
   	    	  		if (!execute_gr_text_draw()){return false;};
   	    	  		break;
   	    	  	case gr_text_align:
   	    	  		if (!execute_gr_text_align()){return false;};
   	    	  		break;
   	    	  	case gr_text_size:
   	    	  		if (!execute_gr_text_size()){return false;};
   	    	  		break;
   	    	  	case gr_text_underline:
   	    	  		if (!execute_gr_text_underline()){return false;};
   	    	  		break;
   	    	  	case gr_text_skew:
   	    	  		if (!execute_gr_text_skew()){return false;};
   	    	  		break;
   	    	  	case gr_text_bold:
   	    	  		if (!execute_gr_text_bold()){return false;};
   	    	  		break;
   	    	  	case gr_text_strike:
   	    	  		if (!execute_gr_text_strike()){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_load:
   	    	  		if (!execute_gr_bitmap_load()){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_scale:
   	    	  		if (!execute_gr_bitmap_scale()){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_draw:
   	    	  		if (!execute_gr_bitmap_draw()){return false;};
   	    	  		break;
   	    	  	case gr_rotate_start:
   	    	  		if (!execute_gr_rotate_start()){return false;};
   	    	  		break;
   	    	  	case gr_rotate_end:
   	    	  		if (!execute_gr_rotate_end()){return false;};
   	    	  		break;
   	    	  	case gr_modify:
   	    	  		if (!execute_gr_modify()){return false;};
   	    	  		break;
   	    	  	case gr_orientation:
   	    	  		if (!execute_gr_orientation()){return false;};
   	    	  		break;
   	    	  	case gr_screen:
   	    	  		if (!execute_gr_screen()){return false;};
   	    	  		break;
   	    	  	case gr_close:
   	    	  		if (!execute_gr_close()){return false;};
   	    	  		break;
   	    	  	case gr_front:
   	    	  		if (!execute_gr_front()){return false;};
   	    	  		break;
   	    	  	case gr_bound_touch:
   	    	  		if (!execute_gr_bound_touch(0)){return false;};
   	    	  		break;
   	    	  	case gr_bound_touch2:
   	    	  		if (!execute_gr_bound_touch(1)){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_size:
   	    	  		if (!execute_gr_bitmap_size()){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_delete:
   	    	  		if (!execute_gr_bitmap_delete()){return false;};
   	    	  		break;
   	    	  	case gr_set_pixels:
   	    	  		if (!execute_gr_set_pixels()){return false;};
   	    	  		break;
   	    	  	case gr_get_pixel:
   	    	  		if (!execute_gr_get_pixel()){return false;};
   	    	  		break;
   	    	  	case gr_save:
   	    	  		if (!execute_gr_save()){return false;};
   	    	  		break;
   	    	  	case gr_text_width:
   	    	  		if (!execute_gr_text_width()){return false;};
   	    	  		break;
   	    	  	case gr_scale:
   	    	  		if (!execute_gr_scale()){return false;};
   	    	  		break;
   	    	  	case gr_newdl:
   	    	  		if (!execute_gr_newdl()){return false;};
   	    	  		break;
   	    	  	case gr_clip:
   	    	  		if (!execute_gr_clip()){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_crop:
   	    	  		if (!execute_gr_bitmap_crop()){return false;};
   	    	  		break;
   	    	  	case gr_stroke_width:
   	    	  		if (!execute_gr_stroke_width()){return false;};
   	    	  		break;
   	    	  	case gr_poly:
   	    	  		if (!execute_gr_poly()){return false;};
   	    	  		break;
   	    	  	case gr_statusbar_show:
   	    	  		if (!execute_statusbar_show()){return false;};
   	    	  		break;
   	    	  	case gr_bitmap_save:
   	    	  		if (!execute_bitmap_save()){return false;};
   	    	  		break;
   	    	  	case gr_camera_shoot:
   	    	  		CameraAuto = false;
   	    	  		CameraManual = false;
   	    	  		if (!execute_camera_shoot()){return false;};
   	    	  		break;
   	    	  	case gr_camera_autoshoot:
   	    	  		CameraAuto = true;
   	    	  		CameraManual = false;
   	    	  		if (!execute_camera_shoot()){return false;};
   	    	  		break;
   	    	  	case gr_camera_manualshoot:
   	    	  		CameraAuto = false;
   	    	  		CameraManual = true;
   	    	  		if (!execute_camera_shoot()){return false;};
   	    	  		break;
   	    	  	case gr_screen_to_bitmap:
   	    	  		if (!execute_screen_to_bitmap()){return false;};
   	    	  		break;
   	    	  	case gr_paint_get:
   	    	  		if (!execute_paint_get()){return false;};
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
		  for (i = 0; i<GR_kw.length; ++i){					// loop through the key word list
			  if (Temp.startsWith(GR_kw[i])){    				// if there is a match
				  KeyWordValue = i;								// set the key word number
				  LineIndex = LineIndex + GR_kw[i].length(); 	// move the line index to end of key word
				  return true;									// and report back
			  };
		  };
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
		   int q = (int) (double) EvalNumericExpressionValue;;
		   if (q<1 | q >= BitmapList.size()){
			   RunTimeError("Invalid Bitmap Pointer");
			   return false;			   
		   }
		   Bitmap SrcBitMap = BitmapList.get(q);
		   if (!SrcBitMap.isMutable()){
			   RunTimeError("Bitmaps loaded from files not changeable.");
			   return false;
		   }
		   if (SrcBitMap == null){
			   RunTimeError("Bitmap was deleted");
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
//	    	try {Thread.sleep(500);}catch(InterruptedException e){};    // Give GR some time to do it
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
	  
	  public boolean execute_gr_newdl(){
		  
		  doingDim = false;									// Get the array variable
		  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
		  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
		  SkipArrayValues = false;
		  doingDim = false;
		  
		  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
		  if (!VarIsNumeric){								// and that it is a numeric array
			  RunTimeError("Array not numeric");
		  }
		  if (VarIsNew){									// and that it has been DIMed
			  RunTimeError("Array not DIMed");
		  }
			if (!checkEOL(true)) return false;

		  
			Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
			ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
			int length = ArrayEntry.getInt("length");				// get the array length
			int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space

			RealDisplayList.clear();
			RealDisplayList.add(0);									// First entry points to null bundle
			
			for (int i = 0; i < length; ++i){						// Copy the bundle pointers 
				double d = NumericVarValues.get(base+i);
				int id = (int) d;
				if (id < 0 || id >= DisplayList.size()){
					RunTimeError("Invalid Object Number");
					return false;
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
				  ShowStatusBar = (int)(double) EvalNumericExpressionValue;
			   }
			   
			   GRorientation = 0;
			   if (ExecutingLineBuffer.charAt(LineIndex) == ','){
					  ++LineIndex;
					  if (!evalNumericExpression())return false;
					  GRorientation = (int)(double) EvalNumericExpressionValue;
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
		  
		  if (GR.Rendering) return true;
		  
		  GR.Rendering = true;
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
		   float width = (float) (double) EvalNumericExpressionValue;
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
	  
	  private String getRawFileName(String input) {
		  
		  // Converts a file name with upper and lower case characters
		  // to a lower case filename.
		  // The dot extension is appended to the end of the filename
		  // preceeded by "_"
		  
		  // MyFile.png = myfile_png
		  // bigSound.mp3 = bigsound_mp3
		  // Earth.jpg = earth_jpg
		  
		  // if there is no dot extension, returns empty string ""
		  
		  // If the isAudio flag is true then
		  // the _mp3 (or whatever) will not be appended
		  
		  String output = "";                   // Set to empty string
		  input = input.toLowerCase();			// Convert to lower case
		  int index = input.indexOf(".");		// Find the dot
		  if (index == -1) return output;		// if no dot, return ""
		  output = input.substring(0, index);   // isolate suff in front of dot
		  return output;						// return the output filename
	  }
	  
	  private boolean execute_gr_bitmap_load(){
		   if (!getVar())return false;							// Graphic Bitmap Pointer Variable
		   if (!VarIsNumeric)return false;						// 
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalStringExpression()) return false;							// Get the file path
		   if (SEisLE) return false;
		   if (!checkEOL()) return false;
		   String fileName = StringConstant;									// The filename as given by the user

		   String fn0 = Basic.filePath + "/data/" + fileName;				// Add the path to the filename
		   String fn = new File(fn0).getAbsolutePath();							// convert to absolute path				
		   
		   String packageName = Basic.BasicContext.getPackageName();			// Get the package name
		  
		   boolean loadRaw = false;												// Assume not loading raw resource

		   if (!Basic.isAPK){							// if not standard BASIC!
			   																	// then is user APK
			   File theFile = new File(fn);										// if the file has not been loaded onto the SDCARD									
		       if (!theFile.exists())  loadRaw = true;							// then the file to be loaded from a raw resource
		   }
		   
		   InputStream inputStream = null;										// Establish an input stream
		   
		   if (loadRaw) {														// If loading a raw resource file
			   String rawFileName = getRawFileName(fileName);			// Convert conventional filename to raw resource name
			   if (rawFileName.equals("")) {
				   RunTimeError("Error converting to raw filename");
				   return false;
			   }
			   int id = Basic.BasicContext.getResources().getIdentifier (rawFileName, "raw", packageName);  // Get the resourc ID
			   try {
				   inputStream = Basic.BasicContext.getResources().openRawResource(id);  // Open an input stream from raw resource
			   }
			   catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   };
			   
		   }else {																// Not loading from a raw resource
			   try {
				   inputStream = new FileInputStream(fn);						// Open an input stream from the SDCARD file
			   }
			   catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   };
		   }
		   
		   System.gc();															// Garbage collect
		   
		   try{
			   aBitmap = BitmapFactory.decodeStream(inputStream);				// Create bitmap from the input stream
		   }           
		   catch (Exception e) {
				RunTimeError("Error: " + e );
			   return false;
		   };

		   if (aBitmap == null ){
			   RunTimeError("Bitmap load failed at:");
			   return false;
		   }
		   
		   NumericVarValues.set(SaveValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
		   
		   BitmapList.add(aBitmap); // Add the new bit map to the bitmap list
		   
		   return true;
	  }
	  
	  private boolean execute_gr_bitmap_delete(){
		   if (!evalNumericExpression()) return false;					// 
		   if (!checkEOL()) return false;
		   int q = (int) (double) EvalNumericExpressionValue;;
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
				   RunTimeError("Error: " + e);
				   return false;
			   };
			   
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
		   int SourceBitmapIndex = (int) ( double)EvalNumericExpressionValue;
		   if (SourceBitmapIndex < 0 || SourceBitmapIndex >= BitmapList.size()){
			   RunTimeError("Invalid Source Bitmap Pointer");
			   return false;
		   }
		   Bitmap SourceBitmap = BitmapList.get(SourceBitmapIndex);
		   
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get x
		  int x = (int) ( double)EvalNumericExpressionValue;
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get y
		  int y = (int) (double) EvalNumericExpressionValue;
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;
		  
		  if (!evalNumericExpression())return false;							// Get width
		  int width = (int) (double) EvalNumericExpressionValue;
		  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		  ++LineIndex;

		  if (!evalNumericExpression())return false;							// Get height
		  int height = (int) (double) EvalNumericExpressionValue;
		  if (!checkEOL()) return false;
		  
		  
		  try {
			  aBitmap = Bitmap.createBitmap(SourceBitmap, x, y, width, height);
		  }
		  catch (Exception e){
			  RunTimeError("Error: " + e);
		  };

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
		   int width = (int) (double) EvalNumericExpressionValue;
		   if (width <= 0 ){
			   RunTimeError("Width must be >= 0");
			   return false;
		   }
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!evalNumericExpression()) return false;							// Get the height
		   int height = (int) (double) EvalNumericExpressionValue;
		   if (height <= 0 ){
			   RunTimeError("Height must be >= 0");
			   return false;
		   }
		   
		   if (!checkEOL()) return false;
		   
		   try{
			   aBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); // Create the bitamp
		   }
		   catch (Exception e) {
			   RunTimeError("Error: " + e);
			   return false;
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
		  double d = EvalNumericExpressionValue;
		  GR.drawView.SetOrientation((int) (double) d);
		  return true;
	  }
	  
	  private boolean execute_gr_screen(){
		   if (!getVar())return false;							// Width variable 
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex,(double) GR.Width); 
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!getVar())return false;							// Heigth Variable
		   if (!VarIsNumeric)return false;						// 
		   NumericVarValues.set(theValueIndex, (double) GR.Heigth); 
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
		  try {Thread.sleep(100);}catch(InterruptedException e){};
		  return true;
	  }
	  
	  private boolean execute_gr_set_pixels(){		  
		  	Bundle aBundle = new Bundle();
	  		aBundle.putInt("type", GR.dsetPixels);
	  		aBundle.putInt("hide", 0);

	  		if (!getVar())return false;							// Graphic Object Variable
	  		if (!VarIsNumeric)return false;						// 
	  		int SaveValueIndex = theValueIndex;
	  		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  		++LineIndex;

			  doingDim = false;									// Get the array variable
			  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
			  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
			  SkipArrayValues = false;
			  doingDim = false;
			  
			  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
			  if (!VarIsNumeric){								// and that it is a numeric array
				  RunTimeError("Array not numeric");
			  }
			  if (VarIsNew){									// and that it has been DIMed
				  RunTimeError("Array not DIMed");
				  return false;
			  }
			  
				Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
				ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
				int length = ArrayEntry.getInt("length");				// get the array length
				if ((length % 2) != 0){
					RunTimeError("Not an even number of elements in pixel array");
					SyntaxError = true;
					return false;
				}
				int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space
		  
/*				int pixelBase = PixelPoints.size();
				for (int i = 0; i < length; ++i){
					double d = NumericVarValues.get(base+i);
					PixelPoints.add((float) d);
				}
*/				
				aBundle.putInt("pbase", base);
				aBundle.putInt("plength", length);
			
				int x = 0;
				int y = 0;
				if (ExecutingLineBuffer.charAt(LineIndex) != ']')return false;
				++LineIndex;
		  		if (ExecutingLineBuffer.charAt(LineIndex) == ','){
		  			++LineIndex;

		  			if (!evalNumericExpression()) return false;
		  			x = (int) (double) EvalNumericExpressionValue;
		  			if (ExecutingLineBuffer.charAt(LineIndex) != ',') return false;
		  			++LineIndex;
		  			if (!evalNumericExpression()) return false;
		  			y =  (int) (double) EvalNumericExpressionValue;
		  		}
		  		
		  		aBundle.putInt("x", x);
		  		aBundle.putInt("y", y);
		  		
				int p = PaintList.size();								// Set the most current paint as the paint
				aBundle.putInt("paint", p-1);							// paint for these pixels
				NumericVarValues.set(SaveValueIndex, (double) DisplayList.size()); 	// Save the GR Object index into the var

				DisplayListAdd(aBundle);				// Add the line to the display list

				
		  return true;
	  }
	  
	  private boolean execute_gr_get_bmpixel(){
		  if (!evalNumericExpression()) return false;
		  int SourceBitmapIndex = (int) (double) EvalNumericExpressionValue;
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
            GR.drawView.setDrawingCacheEnabled(true);
            GR.Rendering = true;                                        // buildDrawingCache() renders
			GR.drawView.buildDrawingCache();							// Build the cache
			Bitmap b = GR.drawView.getDrawingCache();					// get the bitmap
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
	  		int green = Color.green(pixel);;
	  		int blue = Color.blue(pixel);;
	  		
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
		boolean isPNG = true;											// Assume png
		String tFN = fn.toUpperCase();									// temp convert fn to upper case
		if (tFN.endsWith(".JPG")) isPNG = false;						// Test jpg
		else if (!tFN.endsWith(".PNG")) fn = fn + ".png";				// Test png

		File file = new File(Basic.filePath +"/data/"+fn);		// build full path

		try {															// Write the file
			file.createNewFile();
			FileOutputStream ostream = new FileOutputStream(file);

			if (isPNG)													// Write png or jpg
				b.compress(CompressFormat.PNG, quality, ostream);
			else
				b.compress(CompressFormat.JPEG, quality, ostream);
			ostream.close();
		} 
		catch (Exception e) {
			RunTimeError("Error: " + e);
			return false;
		}
		return true;
	}

	  private boolean execute_gr_save(){
		  
		  	if (!evalStringExpression()) return false;					// Get the filename
		  	String fn = StringConstant;
		  	
		  	int quality = 50;											// set default jpeg quality
			if (ExecutingLineBuffer.charAt(LineIndex) == ',')			// if there is an optional quality parm
			{
				++LineIndex;
				if (!evalNumericExpression())return false;				// evaluate it
				quality = (int) (double) EvalNumericExpressionValue;
				if (quality < 0 || quality >100){
					RunTimeError("Quality must be between 0 and 100");
					return false;
					}
			}
			
			if (!checkEOL()) return false;
			
            boolean retval = true;
            GR.drawView.setDrawingCacheEnabled(true);
            GR.Rendering = true;                                        // buildDrawingCache() renders
			GR.drawView.buildDrawingCache();							// Build the cache
			Bitmap b = GR.drawView.getDrawingCache();					// get the bitmap
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
          GR.drawView.setDrawingCacheEnabled(true);
          GR.Rendering = true;                                       // buildDrawingCache() renders
		  GR.drawView.buildDrawingCache();							// Build the cache
		  Bitmap BM = GR.drawView.getDrawingCache();				// get the bitmap
		  if (BM == null) {
			  RunTimeError("Could not capture screen bitmap. Sorry.");
			  retval = false;
		  } else {

			  NumericVarValues.set(theValueIndex, (double) BitmapList.size()); // Save the GR Object index into the var
			  BitmapList.add(BM.copy(Bitmap.Config.ARGB_8888 , true));			
			  retval = true;

			  BM.recycle();												// clean up bitmap
			  BM = null;
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
				quality = (int) (double) EvalNumericExpressionValue;
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
		   int theListIndex = (int) (double)EvalNumericExpressionValue;
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
	  			x = (int) (double) EvalNumericExpressionValue;
	  			if (ExecutingLineBuffer.charAt(LineIndex) != ',') return false;
	  			++LineIndex;
	  			if (!evalNumericExpression()) return false;
	  			y =  (int) (double) EvalNumericExpressionValue;
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
		   int choice = (int) (double)EvalNumericExpressionValue;
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
				RunTimeError("Error: " + e);
				return false;
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
				   CameraFlashMode = (int) (double)EvalNumericExpressionValue;
			   }

			CameraBitmap = null;
			CameraDone = false;
			
	    	Intent cameraIntent = new Intent(this, CameraView.class);				// Start the Camera
		    try{
		    	startActivityForResult(cameraIntent, BASIC_GENERAL_INTENT);
		    } catch (Exception e){
		    	RunTimeError("Error: " + e);
		    	return false;
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
		   int face = (int) (double) EvalNumericExpressionValue;

			if (!checkEOL(true)) return false;
			
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
		  if (OnTouchResume == -1) {
			  RunTimeError("No onTouch Interrupt");
			  return false;
		  }
		  ExecutingLineIndex = OnTouchResume;
		  OnTouchResume = -1;
		  return true;
	  }
	  	  
//*************************************************************  Audio  ******************************************
	  private boolean executeAUDIO(){
	    	if (!GetAudioKeyWord()){
  	    	  return false;
  	      	}else {
  	    	  switch (KeyWordValue){
  	    	  	case audio_load:
  	    	  		if (!execute_audio_load()){return false;};
  	    	  		break;
  	    	  	case audio_play:
  	    	  		if (!execute_audio_play()){return false;};
  	    	  		break;
  	    	  	case audio_loop:
  	    	  		if (!execute_audio_loop()){return false;};
  	    	  		break;
  	    	  	case audio_stop:
  	    	  		if (!execute_audio_stop()){return false;};
  	    	  		break;
  	    	  	case audio_volume:
  	    	  		if (!execute_audio_volume()){return false;};
  	    	  		break;
  	    	  	case audio_pcurrent:
  	    	  		if (!execute_audio_pcurrent()){return false;};
  	    	  		break;
  	    	  	case audio_pseek:
  	    	  		if (!execute_audio_pseek()){return false;};
  	    	  		break;
  	    	  	case audio_length:
  	    	  		if (!execute_audio_length()){return false;};
  	    	  		break;
  	    	  	case audio_release:
  	    	  		if (!execute_audio_release()){return false;};
  	    	  		break;
  	    	  	case audio_pause:
  	    	  		if (!execute_audio_pause()){return false;};
  	    	  		break;
  	    	  	case audio_isdone:
  	    	  		if (!execute_audio_isdone()){return false;};
  	    	  		break;
  	    	  	case audio_record_start:
  	    	  		if (!execute_audio_record_start()){return false;};
  	    	  		break;
  	    	  	case audio_record_stop:
  	    	  		if (!execute_audio_record_stop()){return false;};
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
			  };
		  };
		  KeyWordValue = audio_none;						  // no key word found
		  return false;									      // report fail

	  		}
	  	  
	  private boolean execute_audio_load(){
		  
		  /* If there is already an audio running,
		   * then stop it and 
		   * release its resources.
		   */

/*		  if (theMP != null){
			  try {theMP.stop();} catch (IllegalStateException e){};
			  theMP.release();
			  theMP = null;
		  }*/
		  setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		  AudioManager audioSM = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		  MediaPlayer aMP;
		  
		  if (!getNVar())return false;											// Get the Player Number Var
		  int saveValueIndex = theValueIndex;
	  	  if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
	  	  ++LineIndex;
		  
		  if (!evalStringExpression()) return false;							// Get the file path
		  if (SEisLE) return false;
			
		  if (!checkEOL()) return false;
		  
		   String fileName = StringConstant;									// The filename as given by the user

		   String fn0 = Basic.filePath + "/data/" + fileName;				// Add the path to the filename
		   String fn = new File(fn0).getAbsolutePath();							// convert to absolute path				
		   
		   String packageName = Basic.BasicContext.getPackageName();			// Get the package name
		  
		   boolean loadRaw = false;												// Assume not loading raw resource

		   if (!Basic.isAPK){							// if not standard BASIC!
			   																	// then is user APK
			   File theFile = new File(fn);										// if the file has not been loaded onto the SDCARD									
		       if (!theFile.exists())  loadRaw = true;							// then the file to be loaded from a raw resource
		   }
		   
		   Uri theURI = null;
		   int resID = 0;
		   
		   if (loadRaw) {														// If loading a raw resource file
			   String rawFileName = getRawFileName(fileName);					// Convert conventional filename to raw resource name
			   if (rawFileName.equals("")) {
				   RunTimeError("Error converting to raw filename");
				   return false;
			   }
			   try {
				   resID = Basic.BasicContext.getResources().getIdentifier (rawFileName, "raw", packageName);  // Get the resourc ID
				   aMP = MediaPlayer.create(Basic.BasicContext, resID);
			   }
			   catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   };

		   }else {																// Not loading from a raw resource
			   try {
				   File mFile = new File(fn0);
				   theURI = Uri.fromFile(mFile);                // Create Uri for the file
				   if (theURI == null){
					   RunTimeError(StringConstant + "Not Found at:");
					   return false;
				   }
				   aMP = MediaPlayer.create(Basic.BasicContext, theURI);  // Create a new Media Player
			   }
			   catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   };
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
//				RunTimeError("Error: " + e );
//				return false;
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
//	  	 try {Thread.sleep(1000);}catch(InterruptedException e){};
	  	  try {Run.theMP.stop();}catch (Exception e) {
				RunTimeError("Error: " + e );
				return false;
	  	  }

	  	  theMP = null;                                                 // Signal MP stopped
		  return true;
	  }
	  
	  private boolean execute_audio_pause(){
			
		  if (!checkEOL()) return false;
		  if (theMP == null) return true;                               // if theMP is null, Media player has stopped
	  	  try {Run.theMP.pause();} catch (Exception e) {
				RunTimeError("Error: " + e );
				return false;
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
		  if (!evalStringExpression()) return false;
		  String recordFileName = Basic.filePath +"/data/" + StringConstant;
		  
		  int source = 0;								// Get optional source
		  if (ExecutingLineBuffer.charAt(LineIndex) == ','){
			  ++LineIndex;
			  if (!evalNumericExpression()) return false;
			  source = (int) (double)EvalNumericExpressionValue;
			  
		  }
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
		    catch (Exception e){};
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
	    	  		if (!execute_sensors_list()){return false;};
	    	  		break;
	    	  	case sensors_open:
	    	  		if (!execute_sensors_open()){return false;};
	    	  		break;
	    	  	case sensors_read:
	    	  		if (!execute_sensors_read()){return false;};
	    	  		break;
	    	  	case sensors_close:
	    	  		if (!execute_sensors_close()){return false;};
	    	  		break;
	    	  	case sensors_rotate:
	    	  		if (!execute_sensors_rotate()){return false;};
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
			  };
		  };
		  KeyWordValue = sensors_none;							// no key word found
		  return false;									// report fail

	  		}
	  
	  private boolean execute_sensors_list(){
		  
		  if (SensorsClass != null){                 // If SensorsClass is null ....
			  RunTimeError("Sensors alread opened");
			  return false;
		  }
		  
		  
		  GetSensorList = true;                        // Tells SensorsActivity class the we want the list
     	  SensorCensus = new ArrayList<String>();      // Create a new list for the list
      	  SensorsClass = new Intent(this, SensorActivity.class);				// Start the Graphics
          Run.SensorsRunning = false;                  // SensorsActivity will set to true when it is running
      	  startActivityForResult(SensorsClass, BASIC_GENERAL_INTENT);				   // Start the Sensor class
	      while (!SensorsRunning) 					   // Wait for the running signal
	    	  Thread.yield();

	      int SClength = SensorCensus.size();              // If no sensors reported.....
	      if (SClength==0){
	    	  RunTimeError("This device reports no Sensors");
	    	  return false;
	      }
	      
	      /* Puts the list of sensors into an unDIMed array
	       * The first element of the array will be the length of the array
	       */
	      
		  doingDim = true;
		  if (!getVar()){SyntaxError(); return false;}
		  doingDim = false;
		  if (VarIsNumeric){SyntaxError(); return false;} 
		  if (!VarIsArray){SyntaxError(); return false;}
		  if (!VarIsNew){
			  RunTimeError("DIR array must not be DIMed");
			  return false;
		  }
		  if (ExecutingLineBuffer.charAt(LineIndex)!=']'){SyntaxError(); return false;}
			
		  if (!checkEOL(true)) return false;

		  ArrayList <Integer> dimValues = new ArrayList<Integer>();  // Build the D[]
		  dimValues.add(SClength);                                 // Set the array dimension
		  BuildBasicArray(VarNumber, false, dimValues);              // Go build an array of the proper size

		  
		  for (int i = 0; i<SClength; ++i){								// stuff the array
			  String s = SensorCensus.get(i).toString();
			  StringVarValues.set(ArrayValueStart, s);
			  ++ArrayValueStart;
		  }


		  return true;
	  }
	  
	  private boolean execute_sensors_open(){
		  
		  if (SensorsClass != null){               // If null.............
			  RunTimeError("Sensors alread opened at:");
			  return false;
		  }
		  
		  SensorValues = new double[MaxSensors][4];                       // an array to hold the value
		  for (int i=0; i<10; ++i){								  // initialize the array to zero
			  for (int j=0; j<4; ++j) SensorValues[i][j]= 0;
		  }
		  
		  if (ExecutingLineBuffer.charAt(LineIndex)=='\n'){SyntaxError(); return false;}
		  --LineIndex;
		  
		  SensorOpenList = new ArrayList<Integer>();       // Create the list for putting the sensors to open into
		  
		  do {														// Get the user's list of sensors to open
				++LineIndex;
				if (!evalNumericExpression())return false;			// Get the sensor number
				double d = EvalNumericExpressionValue;
				int s = (int)d;
				SensorOpenList.add(s);								// add to the list
		  } while (ExecutingLineBuffer.charAt(LineIndex)==',');


		  if (!checkEOL()) return false;
		  GetSensorList = false;				// Signals with SensorsActivity class
		  SensorsStop = false;					// Will be set when SensorsActivity should stop
		  SensorsRunning = false;				// will be set true when SensorsActivity thread is running
      	  SensorsClass = new Intent(this, SensorActivity.class);	// Start the Sensors Class
      	  startActivityForResult(SensorsClass, BASIC_GENERAL_INTENT);
	      while (!SensorsRunning)               // Wait until activity is running
	    	  Thread.yield();
	      
	      if (SensorOpenList == null){
	    	  RunTimeError("Pause 10 seconds before next run.");
	    	  return false;
	      }
	      
		  if (!GRFront){
			  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(Basic.theProgramRunner);
			  GRFront = false;
		  }else{
			  if (GRclass != null){
				  GRclass.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
				  startActivity(GRclass);
				  GRFront = true;
			  } else {
				  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
				  startActivity(Basic.theProgramRunner);
				  GRFront = false;
			  }
		  }



		  return true;
	  }
	  
	  private boolean execute_sensors_read(){
		  
		  if (SensorsClass == null){            // if null.....
			  RunTimeError("Sensors not opened at:");
			  return false;
		  }
		  
			if (!evalNumericExpression())return false;							// Get Sensor Type 
			double d = EvalNumericExpressionValue;
			int type = (int) d;
			if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			++LineIndex;
			
			if (type <0 || type>MaxSensors){
				RunTimeError("Sensor type not 0 to " + MaxSensors);
				return false;
			}
			
			
			   if (!getVar())return false;							// Sensor Variable
			   if (!VarIsNumeric)return false;
			   NumericVarValues.set(theValueIndex, SensorValues[type][1]); 
			   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			   ++LineIndex;

			   if (!getVar())return false;							// Sensor Variable
			   if (!VarIsNumeric)return false;						// 
			   NumericVarValues.set(theValueIndex, SensorValues[type][2]); 
			   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
			   ++LineIndex;


			   if (!getVar())return false;							// Sensor Variable
			   if (!VarIsNumeric)return false;						// 
			   NumericVarValues.set(theValueIndex, SensorValues[type][3]); 
				
			   if (!checkEOL()) return false;
		  
			   return true;
	  }
	  
	  private boolean execute_sensors_rotate(){
		  
		  /* This is a test. 
		   * It has failed so far
		   * This command has not been exposed to the users.
		   * Someday....?
		   */
		  
		  float mOrientation[] = new float[3];

		  float g[] = new float[3];
		  g[0] = (float) SensorValues[1][1];
		  g[1] = (float) SensorValues[1][2];
		  g[2] = (float) SensorValues[1][3];
		  
		  float m[] = new float[3];
		  m[0] = (float) SensorValues[2][1];
		  m[1] = (float) SensorValues[2][2];
		  m[2] = (float) SensorValues[2][3];
		  
		  float r[] = new float[16];
		  float R[] = new float[16];
		  float i[] = new float[16];

		  SensorManager.getRotationMatrix (r, i, g, m);
/*		  SensorManager.remapCoordinateSystem(R,
				  SensorManager.AXIS_X,
				  SensorManager.AXIS_Z, r);*/
		  
	      SensorManager.getOrientation(r, mOrientation);
	      
          final float rad2deg = (float)(180.0f/Math.PI);
          
	      
		   if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;
		   NumericVarValues.set(theValueIndex, (double) mOrientation[0]);
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		   if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   NumericVarValues.set(theValueIndex, (double) mOrientation[1]); 
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;


		   if (!getVar())return false;							// Graphic Object Variable
		   if (!VarIsNumeric)return false;						// 
		   NumericVarValues.set(theValueIndex, (double) mOrientation[2]); 

			if (!checkEOL()) return false;

		  return true;
	  }
	  
 	  private boolean execute_sensors_close(){
 			if (!checkEOL()) return false;
		  SensorsStop = true;     // Signals SensorsActivity to stop
		  SensorsClass = null;    // Tell everyone that Sensors are closed
		  return true;
	  }
 	  
// ************************************************ GPS Package ***********************************
 	  
 	  private boolean executeGPS(){
	    	if (!GetGPSKeyWord()){
		    	  return false;
		      	}else {
		      	  if (theGPS == null && KeyWordValue != gps_open){
		      		RunTimeError("GPS not opened at:");
		      		  return false;
		      	  }
		    	  switch (KeyWordValue){
		    	  	case gps_open:
		    	  		if (!execute_gps_open()){return false;};
		    	  		break;
		    	  	case gps_close:
		    	  		if (!execute_gps_close()){return false;};
		    	  		break;
		    	  	case gps_altitude:
		    	  		if (!execute_gps_altitude()){return false;};
		    	  		break;
		    	  	case gps_longitude:
		    	  		if (!execute_gps_longitude()){return false;};
		    	  		break;
		    	  	case gps_bearing:
		    	  		if (!execute_gps_bearing()){return false;};
		    	  		break;
		    	  	case gps_accuracy:
		    	  		if (!execute_gps_accuracy()){return false;};
		    	  		break;
		    	  	case gps_speed:
		    	  		if (!execute_gps_speed()){return false;};
		    	  		break;
		    	  	case gps_latitude:
		    	  		if (!execute_gps_latitude()){return false;};
		    	  		break;
		    	  	case gps_provider:
		    	  		if (!execute_gps_provider()){return false;};
		    	  		break;
		    	  	case gps_time:
		    	  		if (!execute_gps_time()){return false;};
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
			  };
		  };
		  KeyWordValue = sensors_none;							// no key word found
		  return false;									// report fail

	  		}
	  
	  public boolean execute_gps_open(){
			if (!checkEOL()) return false;
		  if (theGPS != null) return true;    // If already opened.....
		  
		  GPSoff = false;                                   // If true, signals GPS thread to stop and go away
		  GPSrunning = false;								// GPS thread will set to true when it is running
      	  theGPS = new Intent(this, GPS.class);				// Start the GPS
	      startActivityForResult(theGPS, BASIC_GENERAL_INTENT);
	      while (!GPSrunning) Thread.yield();				// Wait for signal from GPS thread
	      
		  if (!GRFront){
			  Basic.theProgramRunner.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(Basic.theProgramRunner);
			  GRFront = false;
		  }else{
			  GRclass.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			  startActivity(GRclass);
			  GRFront = true;
		  }

		  return true;
		  
	  }
	  
	  public boolean execute_gps_close(){
			if (!checkEOL()) return false;
		  GPSoff = true;						// Tell GPS thread to commit sepuku
		  theGPS = null;						// Says that GPS is not opened.
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
		  if (!getVar()){SyntaxError(); return false;}
		  if (VarIsNumeric){SyntaxError(); return false;}
		  StringVarValues.set(theValueIndex, GPS.Provider);
			if (!checkEOL()) return false;
		  return true;
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
		    	  		if (!execute_array_length()){return false;};
		    	  		break;
		    	  	case array_load:
		    	  		if (!execute_array_load()){return false;};
		    	  		break;
		    	  	case array_delete:
		    	  		if (!executeUNDIM()){return false;};
		    	  		break;
		    	  	case array_sort:
		    	  		DoSort = true;
		    	  		if (!execute_array_collection()){return false;};
		    	  		break;
		    	  	case array_shuffle:
		    	  		DoShuffle = true;
		    	  		if (!execute_array_collection()){return false;};
		    	  		break;
		    	  	case array_reverse:
		    	  		DoReverse = true;
		    	  		if (!execute_array_collection()){return false;};
		    	  		break;
		    	  	case array_sum:
		    	  		DoSum = true;
		    	  		if (!execute_array_sum()){return false;};
		    	  		break;
		    	  	case array_average:
		    	  		DoAverage = true;
		    	  		if (!execute_array_sum()){return false;};
		    	  		break;
		    	  	case array_min:
		    	  		DoMin = true;
		    	  		if (!execute_array_sum()){return false;};
		    	  		break;
		    	  	case array_max:
		    	  		DoMax = true;
		    	  		if (!execute_array_sum()){return false;};
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
		    	  		if (!execute_array_copy()){return false;};
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
				};
		};
		KeyWordValue = array_none;						// no key word found
		return false;										// report fail

	}
	
	private boolean execute_array_length(){
		
		   if (!getVar())return false;							// Length Variable
		   if (!VarIsNumeric)return false;
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

			  doingDim = false;									// Get the array variable
			  SkipArrayValues = true;
			  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
			  SkipArrayValues = false;
			  doingDim = false;
			  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
			  if (VarIsNew){									// and that it has been DIMed
				  RunTimeError("Array not DIMed");
				  return false;
			  }
			  
				Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
				ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
				int length = ArrayEntry.getInt("length");               // Get the length from the bundle

		   NumericVarValues.set(SaveValueIndex, (double) length);       // Set the length into the var value
			if (!checkEOL(true)) return false;

		return true;
	}
		
	private boolean execute_array_load(){
		
		  doingDim = true;                                      // get the array var name as a new array
		  if (!getVar()){SyntaxError(); return false;}
		  doingDim = false;
		  if (!VarIsArray){SyntaxError(); return false;}        // if var is not any array...
		  if (!VarIsNew){
			  RunTimeError("Array must not be previously DIMed");
			  return false;
		  }
		  if (ExecutingLineBuffer.charAt(LineIndex)!=']'){SyntaxError(); return false;} // Array must not have any indicies
		  ++LineIndex;
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} // Comma before the list
		  ++LineIndex;
		  
		  if (VarIsNumeric){									// If the array is numeric
			  if (!LoadNumericArray(VarNumber)) return false;   // load numeric array
		  }else{
			  if (!LoadStringArray(VarNumber)) return false;    // else load string array
		  }
		  
		  return true;

	}
	
	private boolean LoadNumericArray(int theVarNumber){
		
		ArrayList <Double> Values = new ArrayList<Double>();    // Create a list for the numeric values
		char c;
		
		do{                                                     // loop through the list
			if (!evalNumericExpression()) return false;         // values may be expressions
			Values.add(EvalNumericExpressionValue);
			
			c = ExecutingLineBuffer.charAt(LineIndex);          // get the next character
			if (c=='~'){             							// if it is a line continue character
        		if (ExecutingLineIndex+1 >= Basic.lines.size()) return false; // If we are not at the end of program
				++ExecutingLineIndex;										  // Get the next program line
        		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);    // Next program line
        		LineIndex = 0 ;                                               // reset LineIndex to zero
        		c = ',';                                                      // pretend we had a comma
			}else ++LineIndex;          // If not line continue character, increment to next character
			
		}while (c == ',');              // Continue looping while there are comma seperators
		
		  ArrayList <Integer> dimValues = new ArrayList<Integer>();  // Build the D[]
		  int ValuesSize = Values.size();
		  dimValues.add(ValuesSize);                                 // Set the array dimension
		  if (!BuildBasicArray(theVarNumber, true, dimValues)) return false;            // Go build an array of the proper size
		  
		  for (int i = 0; i<ValuesSize; ++i){						// stuff the array with the values list
			  NumericVarValues.set(ArrayValueStart, Values.get(i));
			  ++ArrayValueStart;
		  }
		
		return true;
		
	}
	
	private boolean LoadStringArray(int theVarNumber){
		
		ArrayList <String> Values = new ArrayList<String>();    // Create a list for the numeric values
		char c;
		
		do{                                                     // loop through the list
			if (!evalStringExpression()) return false;          // values may be expressions
			if (SEisLE) return false;							// can not be Logical Expression
			Values.add(StringConstant);
			if (LineIndex >= ExecutingLineBuffer.length()) return false;
			
			c = ExecutingLineBuffer.charAt(LineIndex);       	// get the next character
			if (c=='~'){										// if it is a line continue char
        		if (ExecutingLineIndex+1 >= Basic.lines.size()) return false;  // if not at end of program
				++ExecutingLineIndex;										   // get next line
        		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);     // Next program line
        		LineIndex = 0 ;												   // Reset LineIndex
        		c = ',';													   // pretend the char was a comma
			}else ++LineIndex;
			
		}while (c == ',');   // Loop while there are comma seperators
		
		  ArrayList <Integer> dimValues = new ArrayList<Integer>();  // Build the D[]
		  int ValuesSize = Values.size();
		  dimValues.add(ValuesSize);                                 // Set the array dimension
		  if (!BuildBasicArray(theVarNumber, false, dimValues)) return false;           // Go build an array of the proper size
		  
		  for (int i = 0; i<ValuesSize; ++i){						 // stuff the array
			  StringVarValues.set(ArrayValueStart, Values.get(i));
			  ++ArrayValueStart;
		  }

		return true;
		
	}

	
	private boolean execute_array_collection(){
		
		// This method implements several array commands
		
		  doingDim = false;									// Get the array variable
		  SkipArrayValues = true;
		  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
		  SkipArrayValues = false;
		  doingDim = false;
		  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
		  if (VarIsNew){									// and that it has been DIMed
			  RunTimeError("Array not DIMed");
			  return false;
		  }
		  
			Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
			ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));   
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
		
		   if (!getVar())return false;							// The value return variable
		   if (!VarIsNumeric)return false;						// These commands only for numeric arrays
		   int SaveValueIndex = theValueIndex;
		   if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		   ++LineIndex;

		
		  doingDim = false;									// Get the array variable
		  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
		  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
		  SkipArrayValues = false;
		  doingDim = false;
		  
		  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
		  if (!VarIsNumeric){								// and that it is a numeric array
			  RunTimeError("Array not numeric");
		  }
		  if (VarIsNew){									// and that it has been DIMed
			  RunTimeError("Array not DIMed");
			  return false;
		  }
		  
			Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
			ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
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
			if (!checkEOL(true)) return false;

		return true;
	}
	
	private boolean execute_array_copy(){
															// **** Source Array ****
		
		  doingDim = false;									// Get the array variable
		  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
		  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
		  SkipArrayValues = false;
		  doingDim = false;
		  
		  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
		  boolean SourceArrayNumeric = true;
		  if (!VarIsNumeric){								// and that it is a numeric array
			  SourceArrayNumeric = false;
		  }
		  if (VarIsNew){									// and that it has been DIMed
			  RunTimeError("Sourece Array not DIMed");
			  return false;
		  }
		  
		  Bundle SourceArray = new Bundle();						// Get the array table bundle for this array	
		  SourceArray = ArrayTable.get(VarIndex.get(VarNumber));
		  int SourceLength = SourceArray.getInt("length");			// get the array length
		  int SourceBase = SourceArray.getInt("base");				// and the start of the array in the variable space
		  
		  int SourceStartIndex = 0;										// Get the Source Array Index
		  int usl = SourceLength ;
		  char c = ExecutingLineBuffer.charAt(LineIndex);	
		  if (c != ']') {
			  
			  if (!evalNumericExpression()) return false;
			  c = ExecutingLineBuffer.charAt(LineIndex);
			  SourceStartIndex = (int) (double)EvalNumericExpressionValue;
			  SourceStartIndex = SourceStartIndex - 1;
			  if (SourceStartIndex <0) SourceStartIndex = 0;
			  if (SourceStartIndex >= SourceLength){
				  RunTimeError("Source array start > length");
				  return false;
			  }
			  
			  if (c == ','){
				  ++LineIndex;
				  if (!evalNumericExpression()) return false;
				  usl = (int) (double)EvalNumericExpressionValue;
				  c = ExecutingLineBuffer.charAt(LineIndex);
			  }
			  if (c != ']') return false;
		  }
		  
		  if (SourceStartIndex + usl >  SourceLength) usl = SourceLength - SourceStartIndex;
		  int SourceCopyLimit = SourceStartIndex + usl;
		  
			  
		  ++LineIndex;
		  c = ExecutingLineBuffer.charAt(LineIndex);
		  if (c != ',') return false;
		  ++LineIndex;
		  													    	// *** Destination Array ***
		  
		  doingDim = true;                                    	    // get the array var name as a new array
		  if (!getVar()){return false;}
		  doingDim = false;
		  if (!VarIsArray){return false;}       	    			// if var is not any array...
		  if (!VarIsNew){
			  RunTimeError("Destination array previously DIMed");
			  return false;
		  }
			  int DestVarNumber = VarNumber;
		  
		  if ((SourceArrayNumeric && !VarIsNumeric) ||				// Insure both arrays are same type
		     (!SourceArrayNumeric && VarIsNumeric)){
			  RunTimeError("Arrays not of same type");
			  return false;
		  }
			  
		  int Extras = 0;											// Get the extras parameter
		  c = ExecutingLineBuffer.charAt(LineIndex);
		  if (c != ']') {
			  if (!evalNumericExpression()) return false;
			  c = ExecutingLineBuffer.charAt(LineIndex);
			  if (c != ']') return false;
			  Extras = (int) (double)EvalNumericExpressionValue;
		  }
			  
		  if (!checkEOL(true)) return false;
			  
		  ArrayList <String> StringSourceArray = new ArrayList <String>();     // Prepare the array lists
		  ArrayList <Double> NumericSourceArray = new ArrayList <Double>();
	
		  int ValuesSize = 0;	  
		  if (SourceArrayNumeric){												// Do numeric array
			  if (Extras < 0) {													// if Extras < 0, add to front
				  for (int k = 0; k < -Extras; ++k){
					  NumericSourceArray.add(0.0);
				  }
			  }
			  for (int i=SourceStartIndex ; i < SourceCopyLimit; ++i){								// Copy the source array values
				  NumericSourceArray.add( NumericVarValues.get(SourceBase+i));
			  }
			  if (Extras > 0) {													// if Extras > 0, add to back
				  for (int k = 0; k < Extras; ++k){
					  NumericSourceArray.add(0.0);
				  }
			  }
			  ValuesSize = NumericSourceArray.size();							// Get the final size
			  
		  }else{																// Do String array
			  if (Extras < 0) {													// if Extras < 0, add to front
				  for (int k = 0; k < -Extras; ++k){
					  StringSourceArray.add("");
				  }
			  }
			  for (int i=SourceStartIndex ; i < SourceCopyLimit; ++i){								// Copy the source array values
				  StringSourceArray.add( StringVarValues.get(SourceBase+i));
			  }
			  if (Extras > 0) {													// if Extras > 0, add to back
				  for (int k = 0; k < Extras; ++k){
					  StringSourceArray.add("");
				  }
			  }
			  ValuesSize = StringSourceArray.size();							// Get the final size
		  }
		  
		  
		  ArrayList <Integer> dimValues = new ArrayList<Integer>();  		// Build the D[]
		  dimValues.add(ValuesSize);                                 		// Set the array dimension
		  if (!BuildBasicArray(DestVarNumber, SourceArrayNumeric, dimValues)) return false;    // Go build an array of the proper size

		  for (int i = 0; i<ValuesSize; ++i){						// stuff the array with the values list
			  if (SourceArrayNumeric)
				  NumericVarValues.set(ArrayValueStart, NumericSourceArray.get(i));
			  else
				  StringVarValues.set(ArrayValueStart, StringSourceArray.get(i));
			  ++ArrayValueStart;
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
				};
		};
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

		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		++LineIndex;
		
		   if (!getVar())return false;							// List pointer variable
		   if (!VarIsNumeric)return false;
		   int SaveValueIndex = theValueIndex;
	
	 int theIndex = 0;
		   
	 if ( type == list_is_string){										// Create a string list
		 ArrayList<String> theStringList = new ArrayList <String>();
		 theIndex = theLists.size();
		 theLists.add(theStringList);
	 }
	 else if ( type == list_is_numeric){								// Create a numeric list
		 ArrayList<Double> theNumericList = new ArrayList <Double>();
		 theIndex = theLists.size();
		 theLists.add(theNumericList);
	 }
	 
	   theListsType.add(type);											// add the type
	   NumericVarValues.set(SaveValueIndex, (double) theIndex);   		// Return the list pointer    
	   if (!checkEOL(true)) return false;
		
		return true;
	}
		
	private boolean execute_LIST_ADDARRAY(){
		if (!evalNumericExpression()) return false;					// Get the destination list pointer
		int destListIndex = (int) (double)EvalNumericExpressionValue;
		if (destListIndex < 1 || destListIndex >= theLists.size()){
			RunTimeError("Invalid Destination List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		  doingDim = false;									// Get the array variable
		  SkipArrayValues = true;                           // Tells getVar not to look at the indicies 
		  if (!getVar()){SyntaxError(); SkipArrayValues = false; return false;}
		  SkipArrayValues = false;
		  doingDim = false;
		  
		  if (!VarIsArray){SyntaxError(); return false;}    // Insure that it is an array
		  if (VarIsNew){									// and that it has been DIMed
			  RunTimeError("Array not DIMed");
			  return false;
		  }
		  
			Bundle ArrayEntry = new Bundle();						// Get the array table bundle for this array	
			ArrayEntry = ArrayTable.get(VarIndex.get(VarNumber));
			int size = ArrayEntry.getInt("length");				    // get the array length
			int base = ArrayEntry.getInt("base");					// and the start of the array in the variable space


			switch (theListsType.get(destListIndex))						// Get this lists type
			{
			case list_is_string:										// String
				if (VarIsNumeric) {
					RunTimeError("Type mismatch");
					return false;
				}
				ArrayList<String> thisDestStringList = theLists.get(destListIndex);  	// Get the destination list
				
				for (int i = 0; i < size; ++i ){							// Copy source to destination
					thisDestStringList.add(StringVarValues.get(base+i));
				}
				break;
				
			case list_is_numeric:												// Number
				if (!VarIsNumeric) {
					RunTimeError("Type mismatch");
					return false;
				}
				ArrayList<Double> thisDestNumericList = theLists.get(destListIndex);  	// Get the destination list
				for (int i = 0; i < size; ++i ){							// Copy source to destination
					thisDestNumericList.add(NumericVarValues.get(base+i));
				}
				break;
				
			default:
				RunTimeError("Internal problem. Notify developer");
				return false;
		
			}


		
		return true;
	}
	
	private boolean execute_LIST_ADDLIST(){
		if (!evalNumericExpression()) return false;					// Get the destination list pointer
		int destListIndex = (int) (double)EvalNumericExpressionValue;
		if (destListIndex < 1 || destListIndex >= theLists.size()){
			RunTimeError("Invalid Destination List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalNumericExpression()) return false;					// Get the source list pointer
		int sourceListIndex = (int) (double)EvalNumericExpressionValue;
		if (sourceListIndex < 1 || sourceListIndex >= theLists.size()){
			RunTimeError("Invalid Source List Pointer");
			return false;
		}
		int size = 0;
		switch (theListsType.get(destListIndex))						// Get this lists type
		{
		case list_is_string:										// String
			if (theListsType.get(sourceListIndex) != list_is_string) {
				RunTimeError("Type mismatch");
				return false;
			}
			ArrayList<String> thisDestStringList = theLists.get(destListIndex);  	// Get the destination list
			ArrayList<String> thisSourceStringList = theLists.get(sourceListIndex); // Get the source list
			size = thisSourceStringList.size();
			for (int i = 0; i < size; ++i ){							// Copy source to destination
				thisDestStringList.add(thisSourceStringList.get(i));
			}
			break;
			
		case list_is_numeric:												// Number
			if (theListsType.get(sourceListIndex) == list_is_string) {
				RunTimeError("Type mismatch");
				return false;
			}
			ArrayList<Double> thisDestNumericList = theLists.get(destListIndex);  	// Get the destination list
			ArrayList<Double> thisSourceNumericList = theLists.get(sourceListIndex);// Get the source list
			size = thisSourceNumericList.size();
			for (int i = 0; i < size; ++i ){							// Copy source to destination
				thisDestNumericList.add(thisSourceNumericList.get(i));
			}
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		
		
		return true;
	}
	
	private boolean execute_LIST_SEARCH(){
		
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;
		
		int found = -1;
		int savedVarIndex = 0;
		
		switch (theListsType.get(listIndex))
		{
		case list_is_string:											 // String type list
			ArrayList<String> SValues = theLists.get(listIndex);		 // Get the string list
			if (!evalStringExpression()) return false;         			 // values may be expressions
			if (SEisLE) return false;									 // can not be Logical Expression
			String sfind = StringConstant;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					 // move to the result var
			if ( c != ',') return false;
			++LineIndex;
			if (!getNVar()) return false;
			savedVarIndex = theValueIndex;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					 // move to the start index
			
			int start = 0;
			if ( c == ',') {
				++LineIndex;
				if (!evalNumericExpression()) return false;
				start = (int)(double) EvalNumericExpressionValue;
			}
			
			if (start>0) start = --start;
			
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

			c = ExecutingLineBuffer.charAt(LineIndex);					 // move to the result var
			if ( c != ',') return false;
			++LineIndex;
			if (!getNVar()) return false;
			savedVarIndex = theValueIndex;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					 // move to the start index

			start = 0;
			if ( c == ',') {
				++LineIndex;
				if (!evalNumericExpression()) return false;
				start = (int)(double) EvalNumericExpressionValue;
			}
			
			if (start>0) start = --start;
			
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

		NumericVarValues.set(savedVarIndex, (Double) (double) found);
		return true;
	}
	
	private boolean execute_LIST_ADD(){
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;
		
		switch (theListsType.get(listIndex))
		{
		case list_is_string:												// String type list
			ArrayList<String> SValues = theLists.get(listIndex);		// Get the string list
			do{                                                     // loop through the list
				if (!evalStringExpression()) return false;          // values may be expressions
				if (SEisLE) return false;							// can not be Logical Expression
				SValues.add(StringConstant);
				
				c = ExecutingLineBuffer.charAt(LineIndex);       	// get the next character
				if (c=='~'){										// if it is a line continue char
	        		if (ExecutingLineIndex+1 >= Basic.lines.size()) return false;  // if not at end of program
					++ExecutingLineIndex;										   // get next line
	        		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);     // Next program line
	        		LineIndex = 0 ;												   // Reset LineIndex
	        		c = ',';													   // pretend the char was a comma
				}else ++LineIndex;
				
			}while (c == ',');   // Loop while there are comma seperators
			break;
			
		case list_is_numeric:
			
			ArrayList<Double> NValues = theLists.get(listIndex);	// Get the numeric list
			do{                                                     // loop through the list
				if (!evalNumericExpression()) return false;         // values may be expressions
				NValues.add(EvalNumericExpressionValue);
				
				c = ExecutingLineBuffer.charAt(LineIndex);          // get the next character
				if (c=='~'){             							// if it is a line continue character
	        		if (ExecutingLineIndex+1 >= Basic.lines.size()) return false; // If we are not at the end of program
					++ExecutingLineIndex;										  // Get the next program line
	        		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);    // Next program line
	        		LineIndex = 0 ;                                               // reset LineIndex to zero
	        		c = ',';                                                      // pretend we had a comma
				}else ++LineIndex;          // If not line continue character, increment to next character
				
			}while (c == ',');              // Continue looping while there are comma seperators
			

			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		return true;
	}
	
	private boolean execute_LIST_SET(){
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalNumericExpression()) return false;					// Get the index to get
		int getIndex = (int)(double) EvalNumericExpressionValue;
		--getIndex;													// Ones based for Basic user

		c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			if (!evalStringExpression()) {
				RunTimeError("Type mismatch");
				return false;
			}
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
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalNumericExpression()) return false;					// Get the index to get
		int getIndex = (int)(double) EvalNumericExpressionValue;
		--getIndex;													// Ones based for Basic user

		c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!getVar()) return false;								// Get the return value variable
		
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			if (VarIsNumeric) {
				RunTimeError("Type mismatch");
				return false;
			}
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){		
				RunTimeError("Index out of bounds");
				return false;
			}
			String thisString = thisStringList.get(getIndex);			//Get the requested string
			StringVarValues.set(theValueIndex, thisString);
			break;
			
		case list_is_numeric:												// Number
			if (!VarIsNumeric) {
				RunTimeError("Type mismatch");
				return false;
			}
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
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!getSVar()) return false;
		
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
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			thisStringList.clear();
			break;
			
		case list_is_numeric:												// Number
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			thisNumericList.clear();
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		return true;
	}
	
	private boolean execute_LIST_REMOVE(){
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalNumericExpression()) return false;					// Get the index to remove
		int getIndex = (int)(double) EvalNumericExpressionValue;
		--getIndex;													// Ones based for Basic user

		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:											 // String
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()){		
				RunTimeError("Index out of bounds");
				return false;
			}
			thisStringList.remove(getIndex);
			break;
			
		case list_is_numeric:												// Number
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()){
				RunTimeError("Index out of bounds");
				return false;
			}
			thisNumericList.remove(getIndex);
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		return true;
	}

	private boolean execute_LIST_INSERT(){
		
		if (!evalNumericExpression()) return false;					// Get the list pointer
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;
		
		if (!evalNumericExpression()) return false;					// Get the index insert at
		int getIndex = (int)(double) EvalNumericExpressionValue;
		--getIndex;													// Ones based for Basic user

		c = ExecutingLineBuffer.charAt(LineIndex);
		if ( c != ',') return false;
		++LineIndex;

		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:											 // String
			if (!evalStringExpression()) {
				RunTimeError("Type mismatch");
				return false;
			}
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
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the return var
		if ( c != ',') return false;
		++LineIndex;
		
		if (!getNVar()) return false;
		
		int size = 0;
		
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			size = thisStringList.size();
			break;
			
		case list_is_numeric:												// Number
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			size = thisNumericList.size();
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}
		
		NumericVarValues.set(theValueIndex, (double) size);
		
		return true;
	}
		
	private boolean execute_LIST_CONTAINS(){
		return false;
	}


	private boolean execute_LIST_TOARRAY(){
		if (!evalNumericExpression()) return false;							// Get the List pointer
		int listIndex = (int) (double)EvalNumericExpressionValue;
		if (listIndex < 1 || listIndex >= theLists.size()){
			RunTimeError("Invalid List Pointer");
			return false;
		}
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the array var
		if ( c != ',') return false;
		++LineIndex;
		
		doingDim = true;
		if (!getVar()){													// Get the array name var
			SyntaxError();
			return false;
		};
		doingDim = false;
		
		if (!VarIsArray){												//if there was no [ char, error
			SyntaxError();
			return false;
		};

		int svn = VarNumber;										// save the array variable table number
		
		ArrayList <Integer> dimValues =new ArrayList<Integer>();        // A list to hold the array index values

		int size = 0;
		switch (theListsType.get(listIndex))						// Get this lists type
		{
		case list_is_string:										// String
			if (VarIsNumeric) {
				RunTimeError("Type mismatch");
				return false;
			}
			ArrayList<String> thisStringList = theLists.get(listIndex);  // Get the string list
			size = thisStringList.size();
			if (size == 0) {
				RunTimeError("The list is empty");
				return false;
			}
			dimValues.add(size);
			if (!BuildBasicArray( svn , false, dimValues)){return false;}
			for (int i = 0; i < size; ++i){
				StringVarValues.set(ArrayValueStart+i, thisStringList.get(i));
			}

			break;
			
		case list_is_numeric:												// Number
			if (!VarIsNumeric) {
				RunTimeError("Type mismatch");
				return false;
			}
			ArrayList<Double> thisNumericList = theLists.get(listIndex);	//Get the numeric list
			size = thisNumericList.size();
			if (size == 0) {
				RunTimeError("The list is empty");
				return false;
			}
			dimValues.add(size);
			if (!BuildBasicArray( svn , true, dimValues)){return false;}
			for (int i = 0; i < size; ++i){
				NumericVarValues.set(ArrayValueStart+i, thisNumericList.get(i));
			}
			
			break;
			
		default:
			RunTimeError("Internal problem. Notify developer");
			return false;
	
		}

		
		return true;
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
				};
		};
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
		int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
		int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
		int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
		int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
		int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
		   int bundleIndex = (int) (double)EvalNumericExpressionValue;
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
				};
		};
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

		if (ExecutingLineBuffer.charAt(LineIndex) != ',')return false;
		++LineIndex;
		
		   if (!getVar())return false;							// Stack pointer variable
		   if (!VarIsNumeric)return false;
		   int SaveValueIndex = theValueIndex;
	
		   
	 Stack theStack = new Stack();
	 int theIndex = theStacks.size();
	 theStacks.add(theStack);
	 
	   theStacksType.add(type);											// add the type
	   NumericVarValues.set(SaveValueIndex, (double) theIndex);   		// Return the list pointer    
	   if (!checkEOL(true)) return false;
		
		return true;
	}

	private boolean execute_STACK_PUSH(){

		if (!evalNumericExpression()) return false;							// Get the List pointer
		int stackIndex = (int) (double)EvalNumericExpressionValue;
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
		int stackIndex = (int) (double)EvalNumericExpressionValue;
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
		int stackIndex = (int) (double)EvalNumericExpressionValue;
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
		int stackIndex = (int) (double)EvalNumericExpressionValue;
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
		int stackIndex = (int) (double)EvalNumericExpressionValue;
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
		int stackIndex = (int) (double)EvalNumericExpressionValue;
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
        	RunTimeError("Error: " + e);
        	return false;
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
        	RunTimeError("Error: " + e);
        	return false;
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
        	RunTimeError("Error: " + e);
        	return false;
        }

        if (Dest.equals("@@@@")){
        	RunTimeError( "Invalid Password at:");
        	return false;
        }
        
        StringVarValues.set(theValueIndex, Dest);  // Put the decoded string into the user variable.

		return true;
	}
	
//************************************** Socket Commands  ******************************************
	
	private boolean executeSOCKET(){
		if (!GetSocketKeyWord()){ return false;}
		switch (KeyWordValue){
		case client_connect:
			if (!executeCLIENT_CONNECT())return false;
			break;
		case client_read_line:
			if (!executeCLIENT_READ_LINE()) return false;
			break;
		case client_putbytes:
			if (!executeCLIENT_WRITE_LINE(true)) return false;
			break;
		case client_write_line:
			if (!executeCLIENT_WRITE_LINE(false)) return false;
			break;
		case server_create:
			if (!executeSERVER_CREATE()) return false;
			break;
		case server_accept:
			if (!executeSERVER_ACCEPT()) return false;
			break;
		case server_read_line:
			if (!executeSERVER_READ_LINE()) return false;
			break;
		case server_putbytes:
			if (!executeSERVER_WRITE_LINE(true)) return false;
			break;
		case server_write_line:
			if (!executeSERVER_WRITE_LINE(false)) return false;
			break;
		case server_close:
			if (!executeSERVER_CLOSE()) return false;
			break;
		case client_close:
			if (!executeCLIENT_CLOSE()) return false;
			break;
		case myip:
			if (!executeMYIP()) return false;
			break;
		case client_read_ready:
			if (!executeCLIENT_READ_READY()) return false;
			break;
		case server_read_ready:
			if (!executeSERVER_READ_READY()) return false;
			break;
		case server_disconnect:
			if (!executeSERVER_DISCONNECT()) return false;
			break;
		case server_client_ip:
			if (!executeSERVER_CLIENT_IP()) return false;
			break;
		case client_server_ip:
			if (!executeCLIENT_SERVER_IP()) return false;
			break;
		case client_getfile:
			if (!executeCLIENT_GETFILE()) return false;
			break;
		case server_putfile:
			if (!executeSERVER_PUTFILE()) return false;
			break;
		case client_putfile:
			if (!executeCLIENT_PUTFILE()) return false;
			break;
			
		default:
		}
		return true;
	}
	
	private  boolean GetSocketKeyWord(){						// Get a Basic key word if it is there
		// is the current line index at a key word?
		String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
		int i = 0;
		for (i = 0; i<Socket_KW.length; ++i){				// loop through the key word list
			if (Temp.startsWith(Socket_KW[i])){    			// if there is a match
				KeyWordValue = i;							// set the key word number
				LineIndex = LineIndex + Socket_KW[i].length(); // move the line index to end of key word
				return true;								// and report back
				};
		};
		KeyWordValue = list_none;						// no key word found
		return false;										// report fail

	}
	
	private boolean executeCLIENT_CONNECT(){
		
		if (!evalStringExpression()) return false;
		String SocketClientsServerAdr = StringConstant;
		
		char c = ExecutingLineBuffer.charAt(LineIndex);						// move to the value
		if ( c != ',') return false;
		++LineIndex;

		if (!evalNumericExpression()) return false;							// Get the List pointer
		int SocketClientsServerPort  = (int) (double)EvalNumericExpressionValue;
		
        try{
        	theClientSocket = new Socket(SocketClientsServerAdr, SocketClientsServerPort);
        	ClientBufferedReader = new BufferedReader(new InputStreamReader(theClientSocket.getInputStream()));
        	ClientPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(theClientSocket.getOutputStream())), true);
        	}catch (Exception e) {
    			RunTimeError("Error: " + e );
        		theClientSocket = null;
        		return false;
        	}
		
		return true;
	}
	
	private boolean executeCLIENT_READ_READY(){
		
		if (theClientSocket == null){
			RunTimeError("Client Socket Not Opened");
			return false;
		}
		
		double ready = 0 ;
		
		try{
		if (ClientBufferedReader.ready()) ready = 1;
			} catch (IOException e) {
				RunTimeError( e.toString());
				theClientSocket = null;
				return false;
			}
    	
		if (!getNVar()) return false;
		
		NumericVarValues.set(theValueIndex, ready);
		
		return true;
	}
	
	private boolean executeCLIENT_READ_LINE(){

		if (theClientSocket == null){
			RunTimeError("Client Socket Not Opened");
			return false;
		}
		
		if (!theClientSocket.isConnected()){
			RunTimeError("Client Connection Disrupted");
			return false;
		}
		
		String line = null;
		
		try{
			line = ClientBufferedReader.readLine();
			}catch (Exception e) {
				RunTimeError("Error: " + e );
				theClientSocket = null;
				return false;
    	}
    	
    	if (line == null){
    		line = "NULL";
    	}
    	
		if (!getSVar()) return false;
		StringVarValues.set(theValueIndex, line);
    	
		return true;
	}
	
	private boolean executeCLIENT_WRITE_LINE(boolean byteMode){
		if (theClientSocket == null){
			RunTimeError("Client Socket Not Opened");
			return false;
		}
		
		if (!theClientSocket.isConnected()){
			RunTimeError("Client Connection Disrupted");
			return false;
		}
		
		if (!evalStringExpression()) return false;
		
		if (byteMode){
			try {
				OutputStream os = theClientSocket.getOutputStream();
				for (int k=0; k<StringConstant.length(); ++k){
					byte bb = (byte)StringConstant.charAt(k);
					os.write(bb);
				}
			}
			catch(Exception e){}
			return true;
		}
		
		ClientPrintWriter.println(StringConstant);
		return true;
	}
	
	private boolean executeCLIENT_SERVER_IP(){
		
		if (theClientSocket == null){
			RunTimeError("Client Socket Not Opened");
			return false;
		}
		
		if (!theClientSocket.isConnected()){
			RunTimeError("Client Connection Disrupted");
			return false;
		}
		
		InetAddress ia = theClientSocket.getInetAddress();
		String sia = ia.toString();
		 
		if (!getSVar()) return false;
		StringVarValues.set(theValueIndex, sia);


		return true;
	}
	
	
	private boolean executeSERVER_CREATE(){
		try{
			
			if (!evalNumericExpression()) return false;							// Get the List pointer
			int SocketServersServerPort  = (int) (double)EvalNumericExpressionValue;

			newSS = new ServerSocket(SocketServersServerPort);
			}catch (Exception e) {
				RunTimeError("Error: " + e );

				theClientSocket = null;
				return false;
			}
			
		return true;
	}
	
	private boolean executeSERVER_ACCEPT(){
		
		if (newSS == null) {
			RunTimeError("Server not created");
			return false;
		}
		try{
			
			theServerSocket = newSS.accept();
        	ServerBufferedReader = new BufferedReader(new InputStreamReader(theServerSocket.getInputStream()));
        	ServerPrintWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(theServerSocket.getOutputStream())), true);

			}catch (Exception e) {
				RunTimeError("Error: " + e );
				theClientSocket = null;
				return false;
			}
			
		
		return true;
	}
	
	private boolean executeSERVER_READ_READY(){
		if (theServerSocket == null){
			RunTimeError("No current client accepted");
			return false;
		}
		
		double ready = 0 ;
		
		try{
		if (ServerBufferedReader.ready()) ready = 1;
			} catch (IOException e) {
				RunTimeError( e.toString());
				theServerSocket = null;
				return false;
			}
    	
		if (!getNVar()) return false;
		
		NumericVarValues.set(theValueIndex, ready);
		
		return true;
	}

	
	private boolean executeSERVER_READ_LINE(){
		if (theServerSocket == null){
			RunTimeError("No current connection");
			return false;
		}
		
		if (!getSVar()) return false;

		
		if (!theServerSocket.isConnected()){
			StringVarValues.set(theValueIndex, "Server Connection Disrupted");		
			return true;
		}

		String line = null;
		
		try{
			line = ServerBufferedReader.readLine();
		}
			catch (Exception e) {
				RunTimeError("Error: " + e );
				theServerSocket = null;
				return false;
    	}
    	
    	if (line == null){
    		line = "NULL";
    	}
    	
		StringVarValues.set(theValueIndex, line);

		return true;
	}
	
	private boolean executeSERVER_WRITE_LINE(boolean byteMode){
		if (theServerSocket == null){
			RunTimeError("No current connection");
			return false;
		}
		
		if (!theServerSocket.isConnected()){
			RunTimeError("Server Connection Disrupted");
			return false;
		}
		
		if (!evalStringExpression()) return false;
		
		if (byteMode){
			try {
				OutputStream os = theServerSocket.getOutputStream();
				for (int k=0; k<StringConstant.length(); ++k){
					byte bb = (byte)StringConstant.charAt(k);
					os.write(bb);
				}
			}
			catch(Exception e){}
			return true;
		}
		
		ServerPrintWriter.println(StringConstant);
		return true;
	}
	
	private boolean executeSERVER_PUTFILE(){

		if (theServerSocket == null){
			RunTimeError("No current connection");
			return false;
		}
		
		if (!theServerSocket.isConnected()){
			RunTimeError("Server Connection Disrupted");
			return false;
		}
		
		if (FileTable.size() == 0){
			RunTimeError("No files opened");
			return false;
		}
		
		if (!getVar()){return false;};						// First parm is the filenumber vaiable
		if (!VarIsNumeric){return false;}
		double d = NumericVarValues.get(theValueIndex);
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}
		if (FileNumber >= FileTable.size()){				// Make sure it is a real file table number
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		int FileMode;							//     Variables for the bundle
		boolean eof;
		BufferedInputStream bis = null;
		boolean closed;

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}

		eof = FileEntry.getBoolean("eof");
		bis = BISlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
				
		if (eof){											//Check not EOF
			RunTimeError("Attempt to read beyond the EOF at:");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}

    	DataOutputStream dos ;
	    ByteArrayBuffer byteArray = new ByteArrayBuffer(1024*16);

        try {
			OutputStream os = theServerSocket.getOutputStream();
		    dos = new DataOutputStream(os);
        	
        	int current = 0;
		    
		    while((current = bis.read()) != -1){
		    	long ts = SystemClock.elapsedRealtime();
		    	byteArray.append((byte)current);
		    	if (byteArray.isFull()){
		    		byte[] b = byteArray.toByteArray();
		    		dos.write(b, 0, 1024*16);
		    		byteArray.clear();
		    	}
		    	long te = SystemClock.elapsedRealtime();          // If rate is slower than 1kb/sec
		    	if (te - ts > 16000){							  // terminate transmission
			    	dos.flush();
			    	dos.close();
	        		bis.close();
	        		FileEntry.putBoolean("eof", true);
	        		FileEntry.putBoolean("closed", true);
	        		FileTable.set(FileNumber, FileEntry);
	        		executeSERVER_DISCONNECT();
	        		RunTimeError("Data transmission time out.");
	        		return false;
		    	}

		    }
		    int count = byteArray.length();
		    byte[] b = byteArray.toByteArray();
		    dos.write(b, 0, count);
		    	dos.flush();
		    	dos.close();
        		bis.close();
        		FileEntry.putBoolean("eof", true);
        		FileEntry.putBoolean("closed", true);
        		FileTable.set(FileNumber, FileEntry);
         }
    	 catch (Exception e) {
    		 RunTimeError("Error: " + e);
    		return false;
    		}
		return true;
	}
	
	private boolean executeCLIENT_PUTFILE(){

		if (theClientSocket == null){
			RunTimeError("No current connection");
			return false;
		}
		
		if (!theClientSocket.isConnected()){
			RunTimeError("Client Connection Disrupted");
			return false;
		}
		
		if (FileTable.size() == 0){
			RunTimeError("No files opened");
			return false;
		}
		
		if (!getVar()){return false;};						// First parm is the filenumber vaiable
		if (!VarIsNumeric){return false;}
		double d = NumericVarValues.get(theValueIndex);
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}
		if (FileNumber >= FileTable.size()){				// Make sure it is a real file table number
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		int FileMode;							//     Variables for the bundle
		boolean eof;
		BufferedInputStream bis = null;
		boolean closed;

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		if (FileMode != FMR){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}

		eof = FileEntry.getBoolean("eof");
		bis = BISlist.get(FileEntry.getInt("stream"));
		closed = FileEntry.getBoolean("closed");
				
		if (eof){											//Check not EOF
			RunTimeError("Attempt to read beyond the EOF at:");
			return false;
		}
		
		if (closed){											//Check not Closed
			RunTimeError("File is closed");
			return false;
		}

    	DataOutputStream dos ;
	    ByteArrayBuffer byteArray = new ByteArrayBuffer(1024*16);

        try {
			OutputStream os = theClientSocket.getOutputStream();
		    dos = new DataOutputStream(os);
        	
        	int current = 0;
		    
		    while((current = bis.read()) != -1){
		    	long ts = SystemClock.elapsedRealtime();
		    	byteArray.append((byte)current);
		    	if (byteArray.isFull()){
		    		byte[] b = byteArray.toByteArray();
		    		dos.write(b, 0, 1024*16);
		    		byteArray.clear();
		    	}
		    	long te = SystemClock.elapsedRealtime();          // If rate is slower than 1kb/sec
		    	if (te - ts > 16000){							  // terminate transmission
			    	dos.flush();
			    	dos.close();
	        		bis.close();
	        		FileEntry.putBoolean("eof", true);
	        		FileEntry.putBoolean("closed", true);
	        		FileTable.set(FileNumber, FileEntry);
	        		executeSERVER_DISCONNECT();
	        		RunTimeError("Data transmission time out.");
	        		return false;
		    	}

		    }
		    int count = byteArray.length();
		    byte[] b = byteArray.toByteArray();
		    dos.write(b, 0, count);
		    	dos.flush();
		    	dos.close();
        		bis.close();
        		FileEntry.putBoolean("eof", true);
        		FileEntry.putBoolean("closed", true);
        		FileTable.set(FileNumber, FileEntry);
         }
    	 catch (Exception e) {
    		 RunTimeError("Error: " + e);
    		return false;
    		}
		return true;
	}
	
	
	private boolean executeCLIENT_GETFILE(){

		if (theClientSocket == null){
			RunTimeError("Client Socket Not Opened");
			return false;
		}
		
		if (!theClientSocket.isConnected()){
			RunTimeError("Client Connection Disrupted");
			return false;
		}
		
		if (!getVar()){return false;};						// First parm is the filenumber vaiable
		if (!VarIsNumeric){return false;}
		double d = NumericVarValues.get(theValueIndex);
		int FileNumber =  (int) d;
		if (FileNumber <0 ){
			RunTimeError("Read file did not exist");
			return false;
		}
		if (FileNumber >= FileTable.size()){				// Make sure it is a real file table number
			RunTimeError("Invalid File Number at");
			return false;
		}

		if (!checkEOL()) return false;
		
		int FileMode;							//     Variables for the bundle
		boolean eof;
		DataOutputStream dos = null;

		if (FileNumber >= FileTable.size()){
			RunTimeError("Invalid File Number at");
			return false;
		}
		
		Bundle FileEntry = new Bundle();		// Get the bundle 
		FileEntry = FileTable.get(FileNumber);
		FileMode = FileEntry.getInt("mode");
		if (FileMode != FMW){						// Varify open for read
			RunTimeError("File not opened for read at");
			return false;
		}
		dos = DOSlist.get(FileEntry.getInt("stream"));

		BufferedInputStream bis = null;
		
	    ByteArrayBuffer byteArray = new ByteArrayBuffer(1024*512);

        try {
    		dos = DOSlist.get(FileEntry.getInt("stream"));
    		InputStream is = theClientSocket.getInputStream();
    		bis = new BufferedInputStream(is);
        	int current = 0;
		    
		    while((current = bis.read()) != -1){
		    	byteArray.append((byte)current);
		    	if (byteArray.isFull()){
		    		byte[] b = byteArray.toByteArray();
		    		dos.write(b, 0, 1024*512);
		    		byteArray.clear();
		    	}
		    }
		    int count = byteArray.length();
		    byte[] b = byteArray.toByteArray();
		    dos.write(b, 0, count);
		    	dos.flush();
		    	dos.close();
        		bis.close();
        		FileEntry.putBoolean("eof", true);
        		FileEntry.putBoolean("closed", true);
        		FileTable.set(FileNumber, FileEntry);
         }
        catch (Exception e) {
			RunTimeError("Error: " + e );
			   return false;
		   };

		
	
		return true;
	}
	
	
	private boolean executeSERVER_DISCONNECT(){
		if (theServerSocket == null) return true;

		try{
			theServerSocket.close();
			
			} catch (Exception e) {
				RunTimeError( e.toString());
				theServerSocket = null;
				return false;
			}
		theServerSocket = null;

		return true;
	}
	
	private boolean executeSERVER_CLOSE(){
		try{
			if (theServerSocket != null) theServerSocket.close();
			if (newSS != null) newSS.close();
			}catch (Exception e) {
				RunTimeError("Error: " + e );
				theServerSocket = null;
				return false;
			}
		theServerSocket = null;
		newSS = null;
		return true;
	}
	
	private boolean executeCLIENT_CLOSE(){
		if (theClientSocket == null) return true;
		try{
			theClientSocket.close();
			}catch (Exception e) {
				RunTimeError("Error: " + e );
				theClientSocket = null;
				return false;
			}
			theClientSocket = null;
		return true;
	}
	
	private boolean executeSERVER_CLIENT_IP(){
		
		if (theServerSocket == null){
			RunTimeError("Server not connected to a client");
			return false;
		}
		
		if (!theServerSocket.isConnected()){
			RunTimeError("Server Connection Disrupted");
			return false;
		}
		
		InetAddress ia = theServerSocket.getInetAddress();
		String sia = ia.toString();
		 
		if (!getSVar()) return false;
		StringVarValues.set(theValueIndex, sia);
		return true;
	}
	
	private boolean executeMYIP(){
		String IP = "";
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!(inetAddress. isLoopbackAddress() || inetAddress. isLinkLocalAddress ())) {
							IP = inetAddress.getHostAddress().toString();
//							IP = inetAddress.getHostAddress().toString();
							if (!getSVar()) return false;
							StringVarValues.set(theValueIndex, IP);
							return true;
						}
					}
				}
			} catch (Exception e) {
				RunTimeError("Error: " + e );
				return false;
			}
			
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
			  };
		  };
		  KeyWordValue = 99;							// no key word found
		  return false;									// report fail

	  		}

	  	private boolean executeFTP_OPEN(){
	  		if (!evalStringExpression()) return false;					// URL
	  		String url = StringConstant;
	  		
			char c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;
			
			if (!evalNumericExpression()) return false;					// Port
			int port = (int) (double) EvalNumericExpressionValue;
			
			c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

	  		if (!evalStringExpression()) return false;					// User Name
	  		String user = StringConstant;
	  		
			c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

	  		if (!evalStringExpression()) return false;					// Pass word
	  		String pw = StringConstant;
			
			if (! ftpConnect( url, user, pw, port)) return false;
			
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
				RunTimeError( "Error: " + e);
			}

			return false;
	}
		
		public String ftpGetCurrentWorkingDirectory()
		{
		    try {
		        String workingDir = mFTPClient.printWorkingDirectory();
		        return workingDir;
		    } catch(Exception e) {
		        RunTimeError( "Error: " + e);
		    }
		    return null;
		}
		
		private boolean executeFTP_CLOSE(){
			if (FTPdir == null) return true;
			
			   try {
			        mFTPClient.logout();
			        mFTPClient.disconnect();
			        FTPdir = null;
			        return true;
			    } catch (Exception e) {
			        RunTimeError("Error: " + e);
			    }

			    return false;
		}
		
		private boolean executeFTP_DIR(){
			
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
			if (!getNVar()) return false;								// get the list VAR
			
			ArrayList<String> theStringList = new ArrayList <String>(); // Create a new user list
			int theIndex = theLists.size();
			theLists.add(theStringList);
			 
			theListsType.add(list_is_string);								// add the type
			NumericVarValues.set(theValueIndex, (double) theIndex);   		// Return the list pointer
			

			   try {
			        FTPFile[] ftpFiles = mFTPClient.listFiles();
			        int length = ftpFiles.length;

			        for (int i = 0; i < length; i++) {
			            String name = ftpFiles[i].getName();
			            if ( ftpFiles[i].isDirectory() ) name= name + "(d)";
			            theStringList.add(name);
			        }
			    } catch(Exception e) {
			        RunTimeError("Error: " + e);
			        return false;
			    }
			
			return true;
		}
		
		private boolean executeFTP_CD(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String directory_path = "/"+StringConstant;

		    try {
		        boolean status = mFTPClient.changeWorkingDirectory(directory_path);
		        if (!status) {
		        	RunTimeError("Directory change failed.");
		        	return false;
		        }
		    } catch(Exception e) {
		        RunTimeError("Error: " + e);
		        return false;
		    }
		    FTPdir = directory_path;

		    return true;
		}
		
		private boolean executeFTP_GET(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String srcFile = StringConstant;
	  		
			char c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

	  		if (!evalStringExpression()) return false;					// Destination file name
	  		String destFile = StringConstant;
	  		
	  		destFile = Basic.filePath +"/data/" + destFile;
	  		
	  		return ftpDownload(srcFile, destFile);
		}
		
		public boolean ftpDownload(String srcFilePath, String desFilePath)
		{
		    boolean status = false;
		    try {
		        FileOutputStream desFileStream = new FileOutputStream(desFilePath);
		        status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
		        desFileStream.close();
		        if (!status) RunTimeError("Download error");

		        return status;
		    } catch (Exception e) {
		        RunTimeError( "Error: " + e);
		        return false;
		    }

//		    return status;
		}
		
		private boolean executeFTP_PUT(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String srcFile = StringConstant;
	  		
			char c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

	  		if (!evalStringExpression()) return false;					// Destination file name
	  		String destFile = StringConstant;
	  		
	  		srcFile = Basic.filePath +"/data/" + srcFile;
	  		
	  		return ftpUpload(srcFile, destFile);
		}
		
		public boolean ftpUpload(String srcFilePath, String desFileName)
		{
			boolean status = false;

			try {

				FileInputStream srcFileStream = new FileInputStream(srcFilePath);

				// change working directory to the destination directory
					status = mFTPClient.storeFile(desFileName, srcFileStream);
					if (!status) RunTimeError("Upload problem");
				srcFileStream.close();
				return status;
			} catch (Exception e) {
				RunTimeError( "Error: " + e);
				return false;
			}

//			return status;
		}	
		
		public boolean executeFTP_CMD(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String cmd = StringConstant;
	  		
			char c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

	  		if (!evalStringExpression()) return false;					// Destination file name
	  		String parms = StringConstant;
	  		
			c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;
			
			if (!getNVar()) return false;
			
			try {
				String[] response = mFTPClient.doCommandAsStrings( cmd, parms );
				int l = response.length;
				for (int i = 0; i < l; ++i)
					PrintShow(response[i]);
			} catch (Exception e) {
				RunTimeError( "Error: " + e);
				return false;
			}
			
			return true;
			
		}
		
		private boolean executeFTP_DELETE(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String filePath = StringConstant;

			
		    try {
		        boolean status = mFTPClient.deleteFile(filePath);
		        if (!status){
		        	RunTimeError("File not deleted");
		        }
		        return status;
		    } catch (Exception e) {
		        RunTimeError("Error " + e);
		        return false;
		    }

//		    return false;
		}
		
		private boolean executeFTP_RMDIR(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String filePath = StringConstant;

			
		    try {
		        boolean status = mFTPClient.removeDirectory(filePath);
		        if (!status){
		        	RunTimeError("Directory not deleted");
		        }
		        return status;
		    } catch (Exception e) {
		        RunTimeError("Error " + e);
		        return false;
		    }

//		    return false;
		}
		
		private boolean executeFTP_MKDIR(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String filePath = StringConstant;
			
		    try {
		        boolean status = mFTPClient.makeDirectory(filePath);
		        if (!status){
		        	RunTimeError("Directory not created");
		        }
		        return status;
		    } catch (Exception e) {
		        RunTimeError("Error " + e);
		        return false;
		    }

//		    return false;
		}
		
		private boolean executeFTP_RENAME(){
			if (FTPdir == null){
				RunTimeError("FTP not opened");
				return false;
			}
			
	  		if (!evalStringExpression()) return false;					// Source file name
	  		String oldName = StringConstant;
	  		
			char c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

	  		if (!evalStringExpression()) return false;					// Source file name
	  		String newName = StringConstant;

		    try {
		        boolean status = mFTPClient.rename(oldName, newName);
		        if (!status){
		        	RunTimeError("File not renamed");
		        }
		        return status;
		    } catch (Exception e) {
		        RunTimeError("Error " + e);
		    }
			return false;
		}
		
// *****************************************  Bluetooth ********************************
		
		public boolean executeBT(){
			if (!GetBTKeyWord()){ return false;}
			// mBluetoothAdapter is initialized during Run initialization
			// if there is no blue tooth then it will be null
			if (mBluetoothAdapter == null && KeyWordValue != bt_open){
				RunTimeError("Bluetooth not opened");
				return false;
			}
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
			case bt_status:
				if (!execute_BT_status()) return false;
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
				  };
			  };
			  KeyWordValue = 99;							// no key word found
			  return false;									// report fail

		  		}
		  
		  private boolean execute_BT_open(){

			  
			  if (mBluetoothAdapter == null) {
		            RunTimeError("Bluetooth is not available");
		            return false;
		        }
			  
			  
		    	bt_Secure = true;												// this flag will be used when starting 
		    	if (evalNumericExpression()) {									// the accept thread in BlueTootChatService
		    		if (EvalNumericExpressionValue == 0) bt_Secure = false;
		    	}

		        
		        bt_enabled = 0;													// Enable BT Chat Service
		        bt_state = BluetoothChatService.STATE_NOT_ENABLED;
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
	            
	            bt_state = BluetoothChatService.STATE_NONE;
	            btConnectDevice = null;
            	mOutStringBuffer = new StringBuffer("");
            	BT_Read_Buffer = new ArrayList<String>();
	            btCSrunning = false;
	            
           		Show("@@7");													// Starts the accept thread
		        return true;

		  }
		  
		  private boolean execute_BT_close(){
			  if (mChatService != null) mChatService.stop();
			  return true;
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
		    
		    public final Handler mHandler = new Handler() {
		        @Override
		        public void handleMessage(Message msg) {
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
		            case MESSAGE_TOAST:
//		                Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
//		                               Toast.LENGTH_SHORT).show();
		                break;
		            }
		        }
		    };
		    
		    public void connectDevice(Intent data, boolean secure) {
		    	
		        String address = data.getExtras()
		            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		        btConnectDevice = null;
	            while (!btCSrunning) Thread.yield();
		        try {
		        	btConnectDevice = mBluetoothAdapter.getRemoteDevice(address);
			        if ( btConnectDevice != null) mChatService.connect(btConnectDevice, secure);
		        }
		        catch (Exception e){ 
		        	RunTimeError("Connect error: " + e);
		        }
		    }

		    
		    public boolean execute_BT_status(){
		    	if (!getNVar()) return false;
		    	NumericVarValues.set(theValueIndex, (double) mChatService.mState);
		    	int state = mChatService.getState();
		    	
		    	return true;
		    }
		    
		    private boolean execute_BT_connect(){
		    	bt_Secure = true;
		    	if (evalNumericExpression()) {
		    		if (EvalNumericExpressionValue == 0) bt_Secure = false;
		    	}
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

//		    	Show("@@8");
		    	return true;
		    }
		    
		    private boolean execute_BT_reconnect(){
		    	if (btConnectDevice == null) {
		    		RunTimeError("No previously connected");
		    		return false;
		    	}
		    	mChatService.connect(btConnectDevice, bt_Secure);
		    	return true;
		    }
		    
		    private boolean execute_BT_listen(){
//		    	mChatService.start();
		    	return true;
		    }
		    
		    private boolean execute_BT_device_name(){
		        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");;
		            return false;
		        }
		    	
		    	if (!getSVar()) return false;
		    	StringVarValues.set(theValueIndex, mConnectedDeviceName);
		    	return true;
		    }
		    
		    private boolean execute_BT_write(){
		        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");;
		            return false;
		        }
		        
				if (!BT_PRINT(true)) return false;				// build up the text line
				if (!btPrintLineReady) return true;			// If not ready to print, wait
				
				btPrintLineReady = false;						// Reset the signal
				StringConstant = btPrintLine + "\n";					// Copy the line to StringConstant for writing
				btPrintLine = "";								// clear the text print line
		        

		        // Check that there's actually something to send
		        if (StringConstant.length() > 0) {
		            // Get the message bytes and tell the BluetoothChatService to write
		        	byte[] send = new byte[StringConstant.length()];
					for (int k=0; k<StringConstant.length(); ++k){
						byte bb = (byte)StringConstant.charAt(k);
						send[k] = bb;
					}

		            mChatService.write(send);

		        }

		    	return true;
		    }
		    
			private  boolean BT_PRINT(boolean doPrint){
				boolean WasSemicolon = false;
				do {									// do each field in the print statement
					int LI = LineIndex;
					if (evalNumericExpression()){
						btPrintLine = btPrintLine + Double.toString(EvalNumericExpressionValue);	// convert to string
						WasSemicolon = false;
					}else{
						if (evalStringExpression()){
							btPrintLine = btPrintLine + StringConstant;										// field is string
						WasSemicolon = false;
					}else{
						if (VarIsFunction){SyntaxError(); return false;};
					}
					if (LI == LineIndex) return false;
					}
					char c = ExecutingLineBuffer.charAt(LineIndex);

			        if (c == '\n'){							// Done processing the line
							if (!WasSemicolon){				// if not ended in semi-colon
								if (doPrint){
									btPrintLineReady = true;
									return true;
								}
								else return true;
							} else {return true;}
					}
					
					if (c == ','){							// if the seperator is a comma
						btPrintLine = btPrintLine + ", ";		// add comma + blank to the line
						++LineIndex;
					}else if ( c== ';'){					// if seperator is semi-colon
						++LineIndex;						// don't add anything to output
						WasSemicolon = true;				// and signal we had a semicolon
						if (ExecutingLineBuffer.charAt(LineIndex) == '\n'){return true;} // if now eol, return without outputting
					}
				} while (true);								// Exit loop happens internal to loop
				
				
				
			};


		    
		    private boolean execute_BT_read_ready(){
		        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");;
		            return false;
		        }
		    	if (!getNVar()) return false;
		    	double d = 0;
		    	synchronized (this){
		    		d = (double)BT_Read_Buffer.size();
		    	};
		    	NumericVarValues.set(theValueIndex, d );
		    	return true;
		    	
		    }
		    
		    private boolean execute_BT_readReady_Resume(){
		    	if (OnBTReadResume == -1) {
		    		RunTimeError("No Bluetooth Read Ready Interrupt");
		    		return false;	    		
		    	}
		    	ExecutingLineIndex = OnBTReadResume;
		    	OnBTReadResume = -1;
		    	return true;
		    }
		    
		    private boolean execute_BT_read_bytes(){
		    	
		        if (mChatService.getState() != BluetoothChatService.STATE_CONNECTED) {
		            RunTimeError("Bluetooth not connected");;
		            return false;
		        }
		        
		        String msg = "";
		        synchronized (this){
		            int index = BT_Read_Buffer.size();
		        	if (index > 0 ){
		        		msg = BT_Read_Buffer.get(0);
		        		BT_Read_Buffer.remove(0);
		        	}
		        }
		        if (!getSVar()) return false;
		        StringVarValues.set(theValueIndex, msg);
		        
		    	return true;
		    }
		    
		    private boolean execute_BT_set_uuid(){
		    	if (!evalStringExpression()) return false;
		    	UUID MY_UUID_SECURE = UUID.fromString(StringConstant);
		    	UUID MY_UUID_INSECURE = UUID.fromString(StringConstant);
		    	return true;
		    }

//    ***********************************  Superuser ***************************
		    
		    public boolean executeSU(){
				if (!GetSUKeyWord()){ return false;}
				if (SUprocess == null && KeyWordValue != su_open){
					RunTimeError("Superuser not opened");
					return false;
				}

				switch (KeyWordValue){
				case su_open:
					
					if (!execute_SU_open()) return false;
					break;
				case su_close:
					if (!execute_SU_close()) return false;
					break;
				case su_read_ready:
					if (!execute_SU_read_ready()) return false;
					break;
				case su_read_line:
					if (!execute_SU_read_line()) return false;
					break;
				case su_write:
					if (!execute_SU_write()) return false;
					break;
						
				default:

				}
				return true;
			}
			
			  private  boolean GetSUKeyWord(){						// Get a Basic key word if it is there
					// is the current line index at a key word?
				  String Temp = ExecutingLineBuffer.substring(LineIndex, ExecutingLineBuffer.length());
				  int i = 0;
				  for (i = 0; i<su_KW.length; ++i){		// loop through the key word list
					  if (Temp.startsWith(su_KW[i])){    // if there is a match
						  KeyWordValue = i;						// set the key word number
						  LineIndex = LineIndex + su_KW[i].length(); // move the line index to end of key word
						  return true;							// and report back
					  };
				  };
				  KeyWordValue = 99;							// no key word found
				  return false;									// report fail

			  		}
		    
		    private boolean execute_SU_open(){
		    	SU_ReadBuffer = new ArrayList<String>();    		// Initialize buffer
		    	
		    	try
		        {
		            SUprocess = Runtime.getRuntime().exec("su");						// Request Superuser
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
		    	String command = "exit\n";
		    	theSUReader.stop();
		    	
		    	try {
		    		SUoutputStream.writeBytes(command + "\n");
		    		SUoutputStream.flush();
//		            SUprocess.waitFor();

		    	}
		    	catch (Exception e){
		            RunTimeError("SU Exception: " + e);
		            return false;	    		
		    	}
		    	SUoutputStream = null;
		    	SUinputStream = null;
		    	SU_ReadBuffer = null;
		    	theSUReader = null;
		    	SUprocess = null;

		    	return true;
		    }
		    
/****************************************** CONSOLE Commands  ************************************/

	private boolean executeCONSOLE_DUMP(){
		
		if (!evalStringExpression()){return false;} // Final paramter is the filename
		if (SEisLE) return false;
		String theFileName = StringConstant;
		if (!checkEOL()) return false;
		
		int FileMode = FMW;
		
		if (FileMode == FMW){										// Write Selected

			File file = new File(Basic.filePath + "/data/" + theFileName);
        	try {
        		file.createNewFile();
        		}catch (Exception e) {
        			RunTimeError("Error: " + e );
        			return false;
        			}
        	if (!file.exists() || !file.canWrite()){
        		RunTimeError("Problem opening " + theFileName);
	    		return false;
        	}

        	// Check the pending update count. If it's not zero, wait until it is, or for two seconds.
        	ProgressUpdateCount.await(2000);

            FileWriter writer = null;
        	try{
        		writer = new FileWriter(file, false);						// open the filewriter for the SD Card
        		int length = output.size();
        		for (int i = 0; i < length; ++i) {
        			writer.write(output.get(i)+ "\n");
        			
        		}
				writer.flush();
				writer.close();
        		}catch (Exception e) {
        			RunTimeError("Error: " + e );
        				return false;
        		}
//        	Log.d(LOGTAG, "executeCONSOLE_DUMP: file " + theFileName + " written");
		}

		
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
			  };
		  };
		  KeyWordValue = 99;							// no key word found
		  return false;									// report fail

	  		}
	  
	  private boolean execute_SP_open(){
		  
		  if (!evalNumericExpression()) return false;
		  int SP_max = (int) (double) EvalNumericExpressionValue;
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
		  
			char c = ExecutingLineBuffer.charAt(LineIndex);						
			if ( c != ',') return false;
			++LineIndex;

		  
		  if (!evalStringExpression() ) return false;
		  if (SEisLE) return false;
			
		  if (!checkEOL()) return false;
		  
		   String fileName = StringConstant;									// The filename as given by the user

		   String fn0 = Basic.filePath + "/data/" + fileName;				// Add the path to the filename
		   String fn = new File(fn0).getAbsolutePath();							// convert to absolute path				
		   
		   String packageName = Basic.BasicContext.getPackageName();			// Get the package name
		  
		   boolean loadRaw = false;												// Assume not loading raw resource

		   if (!Basic.isAPK){							// if not standard BASIC!
			   File theFile = new File(fn);										// if the file has not been loaded onto the SDCARD									
		       if (!theFile.exists())  loadRaw = true;							// then the file to be loaded from a raw resource
		   }
		   
		   int SoundID = -1;
		   
		   if (loadRaw) {														// If loading a raw resource file
			   String rawFileName = getRawFileName(fileName);					// Convert conventional filename to raw resource name
			   if (rawFileName.equals("")) {
				   RunTimeError("Error converting to raw filename");
				   return false;
			   }
			   try {
				   int resID = Basic.BasicContext.getResources().getIdentifier (rawFileName, "raw", packageName);  // Get the resourc ID
				   SoundID = theSoundPool.load(Basic.BasicContext, resID, 1);
			   }
			   catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   };

		   }else {																// Not loading from a raw resource
			   try {
				   SoundID = theSoundPool.load(fn, 1);
			   }
			   catch (Exception e) {
					RunTimeError("Error: " + e );
				   return false;
			   };
		   }

		  	
		  
		  	NumericVarValues.set(savedIndex, (double) SoundID );
		  
		  return true;
	  }
	  
	  private boolean execute_SP_play(){
		  
		  if (!getNVar()) return false;                        			// Stream return variable
		  int savedIndex = theValueIndex;

			char c = ExecutingLineBuffer.charAt(LineIndex);				// Sound ID						
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			int soundID = (int)(double) EvalNumericExpressionValue;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					// Left Volume					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			float leftVolume = (float)(double) EvalNumericExpressionValue;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					// Right Volume					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			float rightVolume = (float)(double) EvalNumericExpressionValue;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					// Priority					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			int priority = (int)(double) EvalNumericExpressionValue;

			c = ExecutingLineBuffer.charAt(LineIndex);					// Loop					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			int loop = (int)(double) EvalNumericExpressionValue;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					// Rate					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			float rate = (float)(double) EvalNumericExpressionValue;
			
			if (leftVolume < 0){
				RunTimeError("Left volume out of range");
				return false;
			}
			
			if (leftVolume >= 1.0 ){
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
			int streamID = (int)(double) EvalNumericExpressionValue;
			theSoundPool.stop(streamID);

		  return true;
	  }
	  
	  private boolean execute_SP_unload(){
			if (!evalNumericExpression()) return false;
			int soundID = (int)(double) EvalNumericExpressionValue;
			theSoundPool.unload(soundID);

		  return true;
	  }
	  
	  
	  private boolean execute_SP_pause(){
			if (!evalNumericExpression()) return false;
			int streamID = (int)(double) EvalNumericExpressionValue;
			if (streamID == 0 ) theSoundPool.autoPause();
			else theSoundPool.pause(streamID);
		  return true;
	  }
	  
	  private boolean execute_SP_resume(){
			if (!evalNumericExpression()) return false;
			int streamID = (int)(double) EvalNumericExpressionValue;
			if (streamID == 0 ) theSoundPool.autoResume();
			else theSoundPool.resume(streamID);

		  return true;
	  }
	  
	  private boolean execute_SP_release(){
		  theSoundPool.release();
		  theSoundPool = null;
		  return true;
	  }
	  
	  private boolean execute_SP_setvolume(){
		  
			if (!evalNumericExpression()) return false;
			int streamID = (int)(double) EvalNumericExpressionValue;
			
			char c = ExecutingLineBuffer.charAt(LineIndex);					// Left Volume					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			float leftVolume = (float)(double) EvalNumericExpressionValue;
			
			c = ExecutingLineBuffer.charAt(LineIndex);					// Right Volume					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			float rightVolume = (float)(double) EvalNumericExpressionValue;
			
			if (leftVolume < 0){
				RunTimeError("Left volume out of range");
				return false;
			}
			
			if (leftVolume >= 1.0 ){
				RunTimeError("Left volume out of range");
				return false;
			}


			if (rightVolume < 0 || rightVolume >= 1.0){
				RunTimeError("Right volume out of range");
				return false;
			}

			theSoundPool.setVolume(streamID, leftVolume, rightVolume);


		  return true;
	  }
	  
	  private boolean execute_SP_setpriority(){
			if (!evalNumericExpression()) return false;
			int streamID = (int)(double) EvalNumericExpressionValue;
			
			char c = ExecutingLineBuffer.charAt(LineIndex);					// Left Volume					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			int priority = (int)(double) EvalNumericExpressionValue;
			
			if (priority < 0 ){
				RunTimeError("Priority less than zero");
				return false;
			}
			
			theSoundPool.setPriority(streamID, priority);

		  return true;
	  }
	  
	  private boolean execute_SP_setloop(){
			if (!evalNumericExpression()) return false;
			int streamID = (int)(double) EvalNumericExpressionValue;
			
			char c = ExecutingLineBuffer.charAt(LineIndex);					// Loop value					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			int loop = (int)(double) EvalNumericExpressionValue;
			
			theSoundPool.setLoop(streamID, loop);

		  return true;
	  }
	  

	  private boolean execute_SP_setrate(){
			if (!evalNumericExpression()) return false;
			int streamID = (int)(double) EvalNumericExpressionValue;
			
			char c = ExecutingLineBuffer.charAt(LineIndex);					// Loop value					
			if ( c != ',') return false;
			++LineIndex;
 
			if (!evalNumericExpression()) return false;
			float rate = (float)(double) EvalNumericExpressionValue;
			
			if (rate < 0.5 || rate > 1.8  ){
				RunTimeError("Rate out of range");
				return false;
			}
			
			theSoundPool.setRate(streamID, rate);
		  return true;
	  }
	  
	  //*********************************** Get my phone number ************************
	  
	  private boolean execute_my_phone_number(){
		  
		  if (!getSVar()) return false;
		  
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
			  RunTimeError("Error: " + e);
			  return false;
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
			  RunTimeError("Error: " + e);
			  return false;
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
			  RunTimeError("Error: " + e);
			  return false;
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
				  };
			  };
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
			  ShowStatusBar = (int) (double) EvalNumericExpressionValue;
		  }
		  if (!checkEOL()) return false;

          htmlIntent= new Intent(this, Web.class);         // Intent variable used to tell if opened
          Web.aWebView = null;							   // Will be set int Web.java
          htmlData_Buffer = new ArrayList<String>();	   // Initialize the datalink buffer
          PrintShow("@@COP");									// Start Web View in UI thread.
          htmlOpening = true;
          
          return true;
	  }
	  
	  private boolean execute_html_load_url(){				// Load an internet url
		  if (!evalStringExpression()) return false;
//		  Web.aWebView.webLoadUrl(StringConstant);
		  PrintShow("@@CLU" + StringConstant);
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
		   int theListIndex = (int) (double)EvalNumericExpressionValue;
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
		  
		  if (Basic.isAPK) {
			  RunTimeError("Run not permitted in a complied apk");
			  return false;
		  }

		  if (!evalStringExpression()) return false;              // Get filename
		  
		  Intent intent = new  Intent(this, AutoRun.class);
		  Bundle bb = new Bundle();
		  String fn = Basic.filePath + "/source/" + StringConstant;
		  
		  String data = "";
		  
			char c = ExecutingLineBuffer.charAt(LineIndex);					// Loop value					
			if ( c == ',') {
				++LineIndex;
				if (!evalStringExpression()) return false;
				data = StringConstant;
			}

      	  bb.putString("fn", fn);								  // fn is the tag for the filename parameter
      	  bb.putString("data", data);
      	  intent.putExtras(bb);

      	  Basic.DoAutoRun = false;
          startActivity( intent );
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
		  
		  if (!evalStringExpression()) return false;
		  String title = StringConstant;
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		  ++LineIndex;
		  
		  if (!evalStringExpression()) return false;
		  String subtitle = StringConstant;
		  if (ExecutingLineBuffer.charAt(LineIndex)!=','){SyntaxError(); return false;} 
		  ++LineIndex;
		  
		  if (!evalStringExpression()) return false;
		  String msg = StringConstant;
		  ++LineIndex;
		  
		  if (!evalNumericExpression()) return false;
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
		   long interval = (int) (double)EvalNumericExpressionValue ;
		   if (interval < 100) {
			   RunTimeError("Interval Must Be >= 100");
			   return false;
		   }
		   timerResumeLine = -1;
		   
		   TimerTask tt = new TimerTask() {
			    public void run() {
			        // Delegate to the same runnable each time.
			        toRunRepeatedly.run();
			    }
			};

		   timerExpired= false;
		   theTimer = new Timer();
		   theTimer.scheduleAtFixedRate (tt, 100, interval);	   
		   
	    	return true;
	    }
	   
		
		   Runnable toRunRepeatedly = new Runnable() {
			    public void run() {
			    	timerExpired= true;
			    }
		   };


	    
	   private boolean executeTIMER_CLEAR(){
		   if (theTimer != null) {
			   theTimer.cancel();
			   theTimer = null;
		   }
	    	return true;
	   }
	   
	   private boolean executeTIMER_RESUME(){
		   if (timerResumeLine == -1) {
			   RunTimeError("No timer interrupt to reseume");
			   return false; 
		   }
		   ExecutingLineIndex = timerResumeLine;
		   timerResumeLine = -1;
	       return true;
	    }
	   
	   // ***************************** Home Command **************************************
	   
	   private boolean executeHOME(){
		   moveTaskToBack(true);
		   return true;
	   }

	   private boolean executeBACKGROUND_RESUME() {
		   if (OnBGResume == -1) {
			   RunTimeError("No background state change");
			   return false;
		   }
		   ExecutingLineIndex = OnBGResume;
		   OnBGResume = -1;
		   return true;
	   }

	    


	
} // End of Run
