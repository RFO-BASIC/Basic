/****************************************************************************************************

BASIC! is an implementation of the Basic programming language for
Android devices.

Copyright (C) 2010 - 2016 Paul Laughton

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

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;

import javax.crypto.Cipher;

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

import com.rfo.basic.Basic.TextStyle;
import com.rfo.basic.GPS.GpsData;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
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
import android.content.res.Resources;
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
import android.net.wifi.WifiInfo;
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
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.format.Time;
import android.text.ClipboardManager;

import android.util.AttributeSet;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


/* Executes the Basic program. Run splits into two parts.
 * The UI thread part (console) and the background thread part (interpreter).
 * 
 * The actual execution of the program occurs in the background
 * thread. UI activities must all be handled in the UI thread.
 * 
 * This leads to a little complication.
 */

public class Run extends Activity {

	public static boolean isOld = false;
	private static final String LOGTAG = "Run";
//	Log.v(LOGTAG, "Line Buffer  " + ExecutingLineBuffer.line());

	public static final Object LOCK = new Object();
	public static boolean mWaitForLock;						// semaphore for Input, TGet, and Dialogs

	// ********************* Message types for the Handler *********************

	private static final int MESSAGE_GROUP_MASK        = 0x0F00;// groups can be 0x1 through 0xF

	private static final int MESSAGE_DEFAULT_GROUP     = 0x0000;
	private static final int MESSAGE_CONSOLE_GROUP     = 0x0100;
	private static final int MESSAGE_DIALOG_GROUP      = 0x0200;
	private static final int MESSAGE_BT_GROUP          = 0x0300;// add this offset to messages from BlueTooth commands
	private static final int MESSAGE_HTML_GROUP        = 0x0400;// add this offset to messages from HTML commands

	private static final int MESSAGE_DEBUG_GROUP       = 0x0F00;// add this offset to messages from debug commands

																// message numbers < 256 are in "default" group 0
	private static final int MESSAGE_CHECKPOINT        = 1;		// for checkpointMessage() method

	private static final int MESSAGE_UPDATE_CONSOLE    = MESSAGE_CONSOLE_GROUP + 0;
	private static final int MESSAGE_CONSOLE_LINE_CHAR = MESSAGE_CONSOLE_GROUP + 2;	// for CONSOLE.LINE.CHAR command
	private static final int MESSAGE_CLEAR_CONSOLE     = MESSAGE_CONSOLE_GROUP + 3;	// for CLS command
	private static final int MESSAGE_CONSOLE_TITLE     = MESSAGE_CONSOLE_GROUP + 4;	// for CONSOLE.TITLE command

	private static final int MESSAGE_TOAST             = MESSAGE_DIALOG_GROUP + 0;	// for POPUP command
	private static final int MESSAGE_INPUT_DIALOG      = MESSAGE_DIALOG_GROUP + 1;	// for INPUT command
	private static final int MESSAGE_ALERT_DIALOG      = MESSAGE_DIALOG_GROUP + 2;	// for DIALOG.* commands

	// ************************* ConsoleListView class *************************
	// Custom ListView, just to handle the BACK key

	private final KeyboardManager.KeyboardChangeListener mKeyboardChangeListener =
		new KeyboardManager.KeyboardChangeListener() {
			public void kbChanged() {						// required by KeyboardManager.Callbacks interface
				triggerInterrupt(Interrupt.KB_CHANGE_BIT);
			}
	};

	public static class ConsoleListView extends ListView {
		private final static String LOGTAG = "ConsoleListView";

		private KeyboardManager mKB;

		public ConsoleListView(Context context) {
			this(context, null);
		}

		public ConsoleListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public ConsoleListView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public void setKeyboardManager(KeyboardManager.KeyboardChangeListener listener) {
			mKB = new KeyboardManager(getContext(), this, listener);
		}

		@Override
		public boolean onKeyPreIme(int keyCode, KeyEvent event) {
			return (mKB != null) && mKB.onKeyPreIme(keyCode, event); // delegate to KeyboardManager
		}
	}

	// *************************** EventHolder class ***************************
	// Used to carry events between Activities.

	public static class EventHolder {
		public static final int KEY_DOWN = 1;
		public static final int KEY_UP = 2;
		public static final int BACK_KEY_PRESSED = 3;
		public static final int GR_BACK_KEY_PRESSED = 4;
		public static final int GR_KB_CHANGED = 5;
		public static final int GR_TOUCH = 6;
		public static final int GR_STATE = 7;
		public static final int WEB_STATE = 8;
		public static final int DATALINK_ADD = 9;

		public static final int ON_PAUSE = 1;
		public static final int ON_RESUME = 2;

		int mType;										// one of the above constants
		int mCode;
		KeyEvent mEvent;
		String mStringData;

		public EventHolder(int type, int code) {
			mType = type;
			mCode = code;
		}
		public EventHolder(int type, String data) {
			mType = type;
			mStringData = data;
		}
		public EventHolder(int type, int code, KeyEvent event) {
			mType = type;
			mCode = code;
			mEvent = event;
		}
	}

	// *************************** ProgramLine class ***************************

	public static class ProgramLine {
		private String mText;							// full text, after preprocessing
		private Command mCommand;						// Command object, once known
		private int mKeywordLength;						// skip past command keyword after Command is known
		private Command mSubCommand;					// sub-Command object, if mCommand is a group
		private int mSubKeywordLength;					// length of subcommand keyword(s)
		public Object mExtras;							// command-specific extra data

		public ProgramLine(String text) {
			mText = text;
			mCommand = null;
			mKeywordLength = 0;
			mSubCommand = null;
			mSubKeywordLength = 0;
			mExtras = null;
		}

		// Getters.
		public String text() { return mText; }
		public int length() { return (mText == null) ? 0 : mText.length(); }
		public Command cmd() { return mCommand; }
		public Command subcmd() { return mSubCommand; }
		public int offset() { return mKeywordLength; }
		public int subOffset() { return mSubKeywordLength; }

		// Delegates.
		public boolean startsWith(String prefix) { return mText.startsWith(prefix); }
		public boolean startsWith(String prefix, int start) { return mText.startsWith(prefix, start); }

		// Replace the remembered command with a new one; look up its keyword length.
		public void cmd(Command command) {
			cmd(command, command.name.length());
		}

		// Replace the remembered command with a new one; use the length provided.
		public void cmd(Command command, int length) {
			mCommand = command;
			mKeywordLength = length;
			mSubCommand = null;
			mSubKeywordLength = 0;
		}

		// Replace the remembered subcommand (one of a group) with a new one.
		// Add the subcommand keyword length to the remembered subcommand keyword length;
		// this allows getting a member of a subgroup.
		// Total keyword length is group command length plus sub-command(s) length.
		public void subcmd(Command sub) {
			mSubCommand = sub;
			mSubKeywordLength += (sub == null) ? 0 : sub.name.length();
		}

		// Replace the remembered command with the remembered subcommand.
		// Add the subcommand keyword length to the current command keyword length.
		public void promoteSubCommand() {
			cmd(mSubCommand, mKeywordLength + mSubKeywordLength);
		}

		private Command searchCommands(Command[] commands, int start) {
			for (Command c : commands) {					// loop through the command list
				if (mText.startsWith(c.name, start)) { return c; }
			}
			return null;									// no keyword found
		}

		public Command findCommand(Command[] commands, int start) {
			if (mCommand == null) {
				Command c = searchCommands(commands, start);
				if (c != null) { cmd(c); }					// remember the Command object
			}
			return mCommand;		// return remembered command, or null if no keyword found
		}

		public Command findSubCommand(Command[] commands, int start) {
			if (mSubCommand == null) {
				Command c = searchCommands(commands, start);
				subcmd(c);									// remember the Command object
			}
			return mSubCommand;		// return remembered command, or null if no keyword found
		}
	} // class ProgramLine

	// ***************************** Command class *****************************

	public static class Command {						// Map a command keyword string to its execution function
		public final String name;						// The command keyword
		public final int id;							// Normally 0, may be set non-zero to indicate special case
		public Command(String name) { this(name, 0); }
		public Command(String name, int id) { this.name = name; this.id = id; }
		public boolean run() { return false; }			// Run the command execution function
		public boolean run(int arg) { return false; }	// Run the command execution function with an argument
	}

	// **************************** Interrupt class ****************************

	public static class Interrupt {
		public final static Integer ERROR_BIT		= 0x0001;
		public final static Integer BACK_KEY_BIT	= 0x0002;
		public final static Integer MENU_KEY_BIT	= 0x0004;
		public final static Integer TIMER_BIT		= 0x0008;
		public final static Integer KEY_BIT			= 0x0010;
		public final static Integer GR_TOUCH_BIT	= 0x0020;
		public final static Integer BT_READY_BIT	= 0x0040;
		public final static Integer CONS_TOUCH_BIT	= 0x0080;
		public final static Integer BACKGROUND_BIT	= 0x0100;	// background state change
		public final static Integer KB_CHANGE_BIT	= 0x0200;
		public final static Integer LOW_MEM_BIT		= 0x0400;
		public final static Integer FN_RTN_BIT		= 0x1000;	// for user-defined function,  not an interrupt

		protected final int mLine;						// ISR entry line

		public Interrupt(int line) {
			mLine = line;
		}
		public int line() { return mLine; }				// ISR entry line
	} // class Interrupt

	// **************************** FileInfo class ****************************
	// Records information about an open file. Objects go in the FileTable.

	public enum FileType {
		FILE_TEXT("text"), FILE_BYTE("byte"), FILE_ZIP("zip");

		private final String mName;
		private FileType(String name) { mName = name; }

		public String toString() { return mName; }
	};

	public static abstract class FileInfo {
		private final FileType mType;
		protected final int mMode;
		protected boolean mIsEOF;
		protected boolean mIsClosed;
		protected long mPosition;
		protected long mMark;
		private int mMarkLimit;

		public FileInfo(int mode, FileType type) {
			mMode = mode;
			mType = type;
			mIsEOF = (mMode == FMW);					// initially at bof if reading, eof if writing/appending
			mIsClosed = false;
			mPosition = 1;
		}

		public void mark(long pos, int limit) {
			mMark = pos;
			mMarkLimit = limit;
		}
		public void markCurrentPosition(int newLimit) {
			mMark = mPosition;
			mMarkLimit = newLimit;
		}

		public void eof(boolean isEOF) { mIsEOF = isEOF; }
		protected void closed() { mIsClosed = true; }
		public void position(long pos) { mPosition = pos; }
		public void incPosition() { ++mPosition; }
		public void incPosition(long delta) { mPosition += delta; }

		public int mode() { return mMode; }
		public FileType type() { return mType; }
		public boolean isEOF() { return mIsEOF; }
		public boolean isClosed() { return mIsClosed; }
		public long position() { return mPosition; }
		public long mark() { return mMark; }
		public int markLimit() { return mMarkLimit; }

		// args: stream to flush, previous exception or null;
		// return: previous exception if any, else new exception if any, else null
		public static IOException flushStream(Flushable stream, IOException ex) {	// flush a stream
			if (stream == null) { return ex; }
			try { stream.flush(); return ex; }
			catch ( IOException e ) { return (ex == null) ? e : ex; }
		}

		// args: stream to close, previous exception or null
		// return: previous exception if any, else new exception if any, else null
		public static IOException closeStream(Closeable stream, IOException ex) {	// close a stream
			if (stream == null) { return ex; }
			try { stream.close(); return ex; }
			catch ( IOException e ) { return (ex == null) ? e : ex; }
		}

		protected IOException flush(IOException ex) {			// default implementation is a no-op
			return ex;											// can invoke on read types
		}
		protected abstract IOException close(IOException ex);
	} // class FileInfo

	public static class TextWriterInfo extends FileInfo {
		public FileWriter mTextWriter;
		public TextWriterInfo(int mode) { super(mode, FileType.FILE_TEXT); }

		public IOException flush(IOException ex) { return flushStream(mTextWriter, ex); }
		public IOException close(IOException ex) {
			IOException e = closeStream(mTextWriter, ex);
			mTextWriter = null;
			closed();
			return e;
		}
	}

	public static class TextReaderInfo extends FileInfo {
		public BufferedReader mTextReader;
		public TextReaderInfo(int mode) { super(mode, FileType.FILE_TEXT); }

		public IOException close(IOException ex) {
			IOException e = closeStream(mTextReader, ex);
			mTextReader = null;
			closed();
			return e;
		}
	}

	public static class ByteWriterInfo extends FileInfo {
		public FileOutputStream mByteWriter;					// stream for almost all operations

		// Both of these are built from the FileOutputStream.
		// Only one can be used at a time. Once one is used, we close the stream
		// to keep the other from being used in parallel.
		private DataOutputStream mDOStream;
		private FileChannel mChannel;

		public ByteWriterInfo(int mode) { super(mode, FileType.FILE_BYTE); }

		public DataOutputStream getDOS() {
			if (mDOStream == null) { mDOStream = new DataOutputStream(mByteWriter); }
			return mDOStream;
		}

		public void truncateFile(long length) throws IOException {
			IOException ex = null;
			if ((mByteWriter == null) || (mDOStream != null)) {
				ex = new IOException("Error getting FileChannel");
			} else {
				long pnow = position();
				if (length < 0) { length = 0; }
				if (length < (pnow - 1)) {
					mChannel = mByteWriter.getChannel();
					try {
						mChannel.truncate(length);				// truncate the file
						position(length + 1);
					} catch (IOException e) { ex = e; }
					eof(true);
				}
			}
			ex = close(flushChannel(ex));
			if (ex != null) { throw ex; }
		}

		// arg: previous exception or null;
		// return: previous exception if any, else new exception if any, else null
		private IOException flushChannel(IOException ex) {		// flush the channel
			FileChannel chnl = mChannel;
			mChannel = null;
			if (chnl == null) { return ex; }
			try { chnl.force(false); return ex; }
			catch ( IOException e ) { return (ex == null) ? e : ex; }
		}

		// arg: previous exception or null;
		// return: previous exception if any, else new exception if any, else null
		public IOException flush(IOException ex) {				// flush the channel
			return (mChannel != null) ?
					flushChannel(ex) :
					flushStream(((mDOStream != null) ? mDOStream : mByteWriter), ex);
		}

		// args: stream to close, previous exception or null
		// return: previous exception if any, else new exception if any, else null
		public IOException close(IOException ex) {
			Closeable stream = (mChannel != null) ? mChannel : ((mDOStream != null) ? mDOStream : mByteWriter);
			mChannel = null; mDOStream = null; mByteWriter = null;
			closed();
			return closeStream(stream, ex);
		}
	}

	public static class ByteReaderInfo extends FileInfo {
		public BufferedInputStream mByteReader;
		public ByteReaderInfo(int mode) { super(mode, FileType.FILE_BYTE); }
		private DataInputStream mDIStream;

		public DataInputStream getDIS() {
			if (mDIStream == null) { mDIStream = new DataInputStream(mByteReader); }
			return mDIStream;
		}

		public IOException close(IOException ex) {
			IOException e = closeStream(mByteReader, ex);
			mByteReader = null;
			closed();
			return e;
		}
	}

	public static class ZipWriterInfo extends FileInfo {
		public ZipOutputStream mZipWriter;
		public ZipWriterInfo(int mode) { super(mode, FileType.FILE_ZIP); }

		public IOException close(IOException ex) {
			IOException e = closeStream(mZipWriter, ex);
			mZipWriter = null;
			closed();
			return e;
		}
	}

	public static class ZipReaderInfo extends FileInfo {
		public ZipFile mZipReader;
		public ZipReaderInfo(int mode) { super(mode, FileType.FILE_ZIP); }

		public IOException close(IOException ex) {
			IOException e = closeStream((Closeable)mZipReader, ex);
			mZipReader = null;
			closed();
			return e;
		}
	}

	// ***************** The constants for the Basic Keywords *****************

	// First, an alphabetical list of all of the top-level keywords.
	// Every constant in this list must appear in both BasicKeyWords[] and BASIC_cmd[].
	private static final String BKW_APP_GROUP = "app.";
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
	private static final String BKW_DEVICE_GROUP = "device.";
	private static final String BKW_DIALOG_GROUP = "dialog.";
	private static final String BKW_DIM = "dim";
	private static final String BKW_DIR = "dir";				// same as "file.dir"
	private static final String BKW_DO = "do";
	private static final String BKW_ECHO_GROUP = "echo.";
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
	private static final String BKW_FONT_GROUP = "font.";
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
	private static final String BKW_JOIN = "join";
	private static final String BKW_JOIN_ALL = "join.all";
	private static final String BKW_KB_GROUP = "kb.";
	private static final String BKW_KEY_RESUME = "key.resume";
	private static final String BKW_LET = "let";
	private static final String BKW_LIST_GROUP = "list.";
	private static final String BKW_LOWMEM_RESUME = "lowmemory.resume";
	private static final String BKW_MENUKEY_RESUME = "menukey.resume";
	private static final String BKW_MKDIR = "mkdir";			// same as "file.mkdir"
	private static final String BKW_MYPHONENUMBER = "myphonenumber";
	private static final String BKW_NEXT = "next";
	private static final String BKW_NOTIFY = "notify";
	private static final String BKW_ONBACKGROUND = "onbackground:";
	private static final String BKW_ONBACKKEY = "onbackkey:";
	private static final String BKW_ONBTREADREADY = "onbtreadready:";
	private static final String BKW_ONCONSOLETOUCH = "onconsoletouch:";
	private static final String BKW_ONERROR = "onerror:";
	private static final String BKW_ONGRTOUCH = "ongrtouch:";
	private static final String BKW_ONKBCHANGE = "onkbchange:";
	private static final String BKW_ONKEYPRESS = "onkeypress:";
	private static final String BKW_ONLOWMEM = "onlowmemory:";
	private static final String BKW_ONMENUKEY = "onmenukey:";
	private static final String BKW_ONTIMER = "ontimer:";
	private static final String BKW_PAUSE = "pause";
	private static final String BKW_PHONE_GROUP = "phone.";
	private static final String BKW_POPUP = "popup";
	private static final String BKW_PREDEC = "--";				// omit from BasicKeyWords[]
	private static final String BKW_PREINC = "++";				// omit from BasicKeyWords[]
	private static final String BKW_PRINT = "print";
	private static final String BKW_PRINT_SHORTCUT = "?";
	private static final String BKW_READ_GROUP = "read.";
	private static final String BKW_REM = "rem";
	private static final String BKW_RENAME = "rename";			// same as "file.rename"
	private static final String BKW_REPEAT = "repeat";
	private static final String BKW_RETURN = "return";
	private static final String BKW_RINGER_GROUP = "ringer.";
	private static final String BKW_RUN = "run";
	private static final String BKW_SCREEN_GROUP = "screen.";
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
	private static final String BKW_ZIP_GROUP = "zip.";
	private static final String BKW_TGET = "tget";
	private static final String BKW_TIME = "time";
	private static final String BKW_TIMER_GROUP = "timer.";
	private static final String BKW_TIMEZONE_GROUP = "timezone.";
	private static final String BKW_TONE = "tone";
	private static final String BKW_TTS_GROUP = "tts.";
	private static final String BKW_UNDIM = "undim";
	private static final String BKW_UNTIL = "until";
	private static final String BKW_VIBRATE = "vibrate";
	private static final String BKW_VOLKEYS_GROUP = "volkeys.";
	private static final String BKW_W_R_BREAK = "w_r.break";
	private static final String BKW_W_R_CONTINUE = "w_r.continue";
	private static final String BKW_WAKELOCK = "wakelock";
	private static final String BKW_WHILE = "while";
	private static final String BKW_WIFI_INFO = "wifi.info";
	private static final String BKW_WIFILOCK = "wifilock";

	// Some generic words used by multiple commands
	private static final String BKW_ON = "on";
	private static final String BKW_OFF = "off";
	private static final boolean ON = true;		// true: enable something, turn it on
	private static final boolean OFF = false;	// false: disable something, turn it off

	// This array lists all of the top-level keywords so Format can find them.
	// The order of this list determines the order Format uses for its linear search.
	// BKW_PREDEC and BKW_PREINC are omitted as they look like regular expressions (!).
	// BKW_PRINT_SHORTCUT, too.
	// This array is also used by DEBUG.COMMANDS.
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
		// BKW_PREDEC, BKW_PREINC,						// Format can't handle these and doesn't need them
		BKW_INKEY, BKW_INPUT, BKW_DIALOG_GROUP,
		BKW_SELECT, BKW_TGET,
		BKW_TEXT_GROUP, BKW_BYTE_GROUP, BKW_ZIP_GROUP,
		BKW_FILE_GROUP, BKW_READ_GROUP,
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
		BKW_JOIN_ALL, BKW_JOIN, BKW_CLS,
		BKW_FONT_GROUP, BKW_CONSOLE_GROUP, BKW_DEBUG_GROUP,
		BKW_ECHO_GROUP, BKW_KB_GROUP,
		BKW_NOTIFY, BKW_RUN, // BKW_EMPTY_PROGRAM,		// Format does not need EMPTY_PROGRAM
		BKW_SU_GROUP, BKW_SYSTEM_GROUP,
		BKW_STT_LISTEN, BKW_STT_RESULTS, BKW_TTS_GROUP,
		BKW_TIMER_GROUP, BKW_TIMEZONE_GROUP, BKW_TIME,
		BKW_VIBRATE, BKW_WAKELOCK, BKW_WIFILOCK,
		BKW_END, BKW_EXIT, BKW_HOME,
		BKW_INCLUDE, BKW_PAUSE, BKW_REM,
		BKW_DEVICE_GROUP, BKW_DEVICE, BKW_SCREEN_GROUP,
		BKW_WIFI_INFO, BKW_HEADSET, BKW_MYPHONENUMBER,
		BKW_EMAIL_SEND, BKW_PHONE_GROUP, BKW_SMS_GROUP,
		BKW_VOLKEYS_GROUP, BKW_APP_GROUP,
		BKW_BACK_RESUME, BKW_BACKGROUND_RESUME,
		BKW_CONSOLETOUCH_RESUME, BKW_KEY_RESUME,
		BKW_LOWMEM_RESUME, BKW_MENUKEY_RESUME,

		BKW_ONERROR,											// Interrupt labels are listed for formatter, but have no Command objects
		BKW_ONBACKKEY, BKW_ONBACKGROUND, BKW_ONBTREADREADY,
		BKW_ONCONSOLETOUCH, BKW_ONGRTOUCH,
		BKW_ONKEYPRESS, BKW_ONKBCHANGE, BKW_ONLOWMEM,
		BKW_ONMENUKEY, BKW_ONTIMER,
	};

	private static HashMap<String, String[]> keywordLists = null;	// For Format: map associates a keyword group
																	// with the prefix common to the group.
	public static HashMap<String, String[]> getKeywordLists() {
		if (keywordLists == null) {
			keywordLists = new HashMap<String, String[]>();		// If you add a new keyword group, add it to this list!

			keywordLists.put(BKW_APP_GROUP,       app_KW);
			keywordLists.put(BKW_ARRAY_GROUP,     Array_KW);
			keywordLists.put(BKW_AUDIO_GROUP,     Audio_KW);
			keywordLists.put(BKW_BT_GROUP,        bt_KW);
			keywordLists.put(BKW_BUNDLE_GROUP,    Bundle_KW);
			keywordLists.put(BKW_BYTE_GROUP,      byte_KW);
			keywordLists.put(BKW_CONSOLE_GROUP,   Console_KW);
			keywordLists.put(BKW_DEVICE_GROUP,    Device_KW);
			keywordLists.put(BKW_DIALOG_GROUP,    Dialog_KW);
			keywordLists.put(BKW_DEBUG_GROUP,     debug_KW);
			keywordLists.put(BKW_ECHO_GROUP,      echo_KW);
			keywordLists.put(BKW_FILE_GROUP,      file_KW);
			keywordLists.put(BKW_FN_GROUP,        fn_KW);
			keywordLists.put(BKW_FONT_GROUP,      font_KW);
			keywordLists.put(BKW_FTP_GROUP,       ftp_KW);
			keywordLists.put(BKW_GPS_GROUP,       GPS_KW);
			keywordLists.put(BKW_GR_GROUP,        GR_KW);
			keywordLists.put(BKW_HTML_GROUP,      html_KW);
			keywordLists.put(BKW_KB_GROUP,        KB_KW);
			keywordLists.put(BKW_LIST_GROUP,      List_KW);
			keywordLists.put(BKW_PHONE_GROUP,     phone_KW);
			keywordLists.put(BKW_READ_GROUP,      read_KW);
			keywordLists.put(BKW_RINGER_GROUP,    ringer_KW);
			keywordLists.put(BKW_SCREEN_GROUP,    screen_KW);
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
			keywordLists.put(BKW_VOLKEYS_GROUP,   VolKeys_KW);
			keywordLists.put(BKW_ZIP_GROUP,       zip_KW);
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
	private static final String MF_BNOT = "bnot(";
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
	private static final String MF_IS_NUMBER = "is_number(";
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

	public static final String MathFunctions[] = {		// Command list for Format
		MF_ABS, MF_ACOS, MF_ASCII, MF_ASIN,
		MF_ATAN, MF_ATAN2, MF_BACKGROUND,
		MF_BAND, MF_BIN, MF_BNOT, MF_BOR, MF_BXOR,
		MF_CBRT, MF_CEIL, MF_CLOCK, MF_COS, MF_COSH,
		MF_ENDS_WITH, MF_EXP, MF_FLOOR, MF_FRAC,
		MF_GR_COLLISION, MF_HEX, MF_HYPOT, MF_INT,
		MF_IS_IN, MF_IS_NUMBER, MF_LEN, MF_LOG, MF_LOG10,
		MF_MAX, MF_MIN, MF_MOD, MF_OCT, MF_PI,
		MF_POW, MF_RANDOMIZE, MF_RND, MF_ROUND,
		MF_SGN, MF_SHIFT, MF_SIN, MF_SINH, MF_SQR,
		MF_STARTS_WITH, MF_TAN, MF_TIME,
		MF_TODEGREES, MF_TORADIANS, MF_UCODE, MF_VAL,
	};

	private static final HashMap<String, Integer> mRoundingModeTable = new HashMap<String, Integer>(7) {
		private static final long serialVersionUID = 101L;
		{
			put("hd", BigDecimal.ROUND_HALF_DOWN);
			put("he", BigDecimal.ROUND_HALF_EVEN);
			put("hu", BigDecimal.ROUND_HALF_UP);
			put("d",  BigDecimal.ROUND_DOWN);
			put("u",  BigDecimal.ROUND_UP);
			put("f",  BigDecimal.ROUND_FLOOR);
			put("c",  BigDecimal.ROUND_CEILING);
		}};

	//*********************** The variables for the Operators  ************************

	private static final String OP_INC = "++";
	private static final String OP_DEC = "--";

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
	private static final String SF_DECODE = "decode$(";
	private static final String SF_ENCODE = "encode$(";
	private static final String SF_FORMAT = "format$(";
	private static final String SF_FORMAT_USING = "format_using$(";
	private static final String SF_GETERROR = "geterror$(";
	private static final String SF_HEX = "hex$(";
	private static final String SF_INT = "int$(";
	private static final String SF_LEFT = "left$(";
	private static final String SF_LOWER = "lower$(";
	private static final String SF_LTRIM = "ltrim$(";
	private static final String SF_MID = "mid$(";
	private static final String SF_OCT = "oct$(";
	private static final String SF_REPLACE = "replace$(";
	private static final String SF_RIGHT = "right$(";
	private static final String SF_RTRIM = "rtrim$(";
	private static final String SF_STR = "str$(";
	private static final String SF_TRIM = "trim$(";
	private static final String SF_UPPER = "upper$(";
	private static final String SF_USING = "using$(";
	private static final String SF_VERSION = "version$(";
	private static final String SF_WORD = "word$(";

	public static final String StringFunctions[] = {	// Command list for Format
		SF_BIN, SF_CHR, SF_DECODE, SF_ENCODE,
		SF_FORMAT, SF_FORMAT_USING, SF_GETERROR,
		SF_HEX, SF_INT, SF_LEFT, SF_LOWER,
		SF_LTRIM, SF_MID, SF_OCT, SF_REPLACE,
		SF_RIGHT, SF_RTRIM, SF_STR, SF_TRIM,
		SF_UPPER, SF_USING, SF_WORD, SF_VERSION,
	};

	private static final int TLEFT = 1;					// control bits for SF_TRIM
	private static final int TRIGHT = 2;

	private static final boolean ENCODE = true;			// control bits for ENCODE/DECODE
	private static final boolean DECODE = false;

	private static final int ENCODING_ENCRYPT = 1;		// encode/decode types
	private static final int ENCODING_BASE64 = 2;
	private static final int ENCODING_URL = 3;
	private static final int ENCODING_OTHER = 4;		// simple string conversion

	private static final boolean STR_TEXT = true;		// true: string is (possibly UNICODE) text
	private static final boolean STR_BINARY = false;	// false: string is buffer of binary (or ASCII) data

	// *************************** Various execution control variables ****************************

	public static final int BASIC_GENERAL_INTENT = 255;

	private Intent mConsoleIntent;						// keep a reference to the Intent that started Run 

	public static boolean Exit = false;					// Exits program and signals caller to exit, too
	private boolean Stop = false;						// Stops program from running
	private boolean mInterpreterRunning;
	private boolean RunPaused = false;
														// events from other Activities
	public static final ArrayList<EventHolder> mEventList = new ArrayList<EventHolder>();

	private Interrupt mOnErrorInt = null;				// OnError gets a named interrupt. The others are anonymous.
	private boolean ConsoleLongTouch = false;
	private int TouchedConsoleLine = 0;					// first valid line number is 1
	private int interruptResume = -1;

	/* TODO: move these to Interpreter */				// Constants for use in the IfElseStack
	private static final Integer IEskip1 = 1;			// Skip statements until ELSE, ELSEIF or ENDIF
	private static final Integer IEskip2 = 2;			// Skip to until ENDIF
	private static final Integer IEexec = 3;			// Execute to ELSE, ELSEIF or ENDIF
	private static final Integer IEinterrupt = 4;

														// The output screen text lines
	final private static int MIN_CONSOLE_LINES = 100;	// starting capacity of mConsole
	private final ArrayList<String> mOutput = new ArrayList<String>(MIN_CONSOLE_LINES);
	private final ArrayList<String> mConsoleBuffer = new ArrayList<String>();	// carries output from mInterpreter to UI

	public Basic.ColoredTextAdapter mConsole;			// The output screen array adapter
	private ConsoleListView lv;							// The output screen list view
	private Interpreter mInterpreter;					// the program runner, runs in a separate Thread
	private boolean SyntaxError = false;				// Set true when Syntax Error message has been output

	private boolean mMessagePending;					// If true, may be messages pending

	// debugger dialog and ui thread vars
	private static final int MESSAGE_DEBUG_DIALOG = MESSAGE_DEBUG_GROUP + 1;
	private static final int MESSAGE_DEBUG_SWAP   = MESSAGE_DEBUG_GROUP + 2;
	private static final int MESSAGE_DEBUG_SELECT = MESSAGE_DEBUG_GROUP + 3;
	private Object DB_LOCK = new Object();
	private boolean mWaitForDebugLock = false;
	private boolean WaitForDebugResume = false;
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
	private ArrayList<Var> WatchedVars;
	private Var WatchedArray;
	private int WatchedList;
	private int WatchedStack;
	private int WatchedBundle;
	// end debugger ui vars

	private HashMap<String,Integer> Labels;				// A list of all labels and associated line numbers

	private ClipboardManager clipboard;
	public static boolean Notified;

	// ******************** Command Keywords for User-defined Functions ********************

	private static final String BKW_FN_DEF = "def";
	private static final String BKW_FN_RTN = "rtn";

	private static final String fn_KW[] = {				// Command list for Format
		BKW_FN_DEF, BKW_FN_RTN, BKW_END
	};

	// ********************************** SWITCH keywords **********************************

	private static final String BKW_SW_BEGIN = "begin";
	private static final String BKW_SW_CASE = "case";
	private static final String BKW_SW_BREAK = "break";
	private static final String BKW_SW_DEFAULT = "default";

	private static final String sw_KW[] = {				// Command list for Format
		BKW_SW_BEGIN, BKW_SW_CASE, BKW_SW_BREAK,
		BKW_SW_DEFAULT, BKW_END
	};

	// ********************************* WAKELOCK keywords *********************************

	private PowerManager.WakeLock theWakeLock;
	private static final int partial = 1;
	private static final int dim = 2;
	private static final int bright = 3;
	private static final int full = 4;
	private static final int release = 5;

	// ********************************* WIFILOCK keywords *********************************

	private WifiManager.WifiLock theWifiLock;
	private static final int wifi_mode_scan = 1;
	private static final int wifi_mode_full = 2;
	private static final int wifi_mode_high = 3;
	private static final int wifi_release = 4;

	// ********************* File I/O operation keywords and variables *********************

	// for both TEXT and BYTE
	private static final String BKW_OPEN = "open";
	private static final String BKW_CLOSE = "close";
	private static final String BKW_EOF = "eof";
	private static final String BKW_POSITION_GET = "position.get";
	private static final String BKW_POSITION_SET = "position.set";
	private static final String BKW_POSITION_MARK = "position.mark";

	// for FILE
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

	private static final int FMR = 0;					// File Mode Read
	private static final int FMW = 1;					// File Mode Write

	// ********************************* TEXT I/O keywords *********************************

	private static final String BKW_TEXT_READLN = "readln";
	private static final String BKW_TEXT_WRITELN = "writeln";
	private static final String BKW_TEXT_INPUT = "input";

	private static final String text_KW[] = {			// Command list for Format
		BKW_OPEN, BKW_CLOSE, BKW_EOF,
		BKW_TEXT_READLN, BKW_TEXT_WRITELN,
		BKW_TEXT_INPUT,
		BKW_POSITION_GET, BKW_POSITION_SET,
		BKW_POSITION_MARK,
	};

	// ********************************* BYTE I/O keywords *********************************

	private static final String BKW_BYTE_READ_BYTE = "read.byte";
	private static final String BKW_BYTE_WRITE_BYTE = "write.byte";
	private static final String BKW_BYTE_READ_BUFFER = "read.buffer";
	private static final String BKW_BYTE_WRITE_BUFFER = "write.buffer";
	private static final String BKW_BYTE_READ_NUMBER = "read.number";
	private static final String BKW_BYTE_WRITE_NUMBER = "write.number";
	private static final String BKW_BYTE_COPY = "copy";
	private static final String BKW_BYTE_TRUNCATE = "truncate";

	private static final String byte_KW[] = {			// Command list for Format
		BKW_OPEN, BKW_CLOSE, BKW_EOF,
		BKW_BYTE_READ_BYTE, BKW_BYTE_WRITE_BYTE,
		BKW_BYTE_READ_BUFFER, BKW_BYTE_WRITE_BUFFER,
		BKW_BYTE_READ_NUMBER, BKW_BYTE_WRITE_NUMBER,
		BKW_BYTE_COPY, BKW_BYTE_TRUNCATE,
		BKW_POSITION_GET, BKW_POSITION_SET,
		BKW_POSITION_MARK,
	};

	// ********************************* ZIP I/O keywords *********************************

	private static final String BKW_ZIP_READ = "read";
	private static final String BKW_ZIP_WRITE = "write";

	private static final String zip_KW[] = {			// Command list for Format
		BKW_OPEN, BKW_CLOSE, BKW_DIR,
		BKW_ZIP_READ, BKW_ZIP_WRITE,
	};

	// *********************************** READ keywords ***********************************

	private static final String BKW_READ_DATA = "data";
	private static final String BKW_READ_NEXT = "next";
	private static final String BKW_READ_FROM = "from";

	private static final String read_KW[] = {			// Command list for Format
		BKW_READ_DATA, BKW_READ_NEXT, BKW_READ_FROM
	};

	// ********************************** SCREEN keywords **********************************

	private static final String BKW_SCREEN_ROTATION = "rotation";
	private static final String BKW_SCREEN_SIZE = "size";

	private static final String screen_KW[] = {			// Command list for Format
		BKW_SCREEN_ROTATION, BKW_SCREEN_SIZE
	};

	// *********************************** FONT keywords ***********************************

	private static final String BKW_FONT_LOAD = "load";
	private static final String BKW_FONT_DELETE = "delete";
	private static final String BKW_FONT_CLEAR = "clear";

	private static final String font_KW[] = {			// Font command list for Format
		BKW_FONT_LOAD, BKW_FONT_DELETE, BKW_FONT_CLEAR
	};

	// ********************************* CONSOLE keywords **********************************

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

	// ************************************ KB keywords ************************************

	private static final String BKW_KB_HIDE = "hide";
	private static final String BKW_KB_RESUME = "resume";
	private static final String BKW_KB_SHOW = "show";
	private static final String BKW_KB_SHOWING = "showing";
	private static final String BKW_KB_TOGGLE = "toggle";

	private static final String KB_KW[] = {			// KB command list for Format
		BKW_KB_HIDE, BKW_KB_RESUME, BKW_KB_SHOWING, BKW_KB_SHOW, BKW_KB_TOGGLE
	};

	// ****************************** INPUT command variables ******************************

	private boolean mInputCancelled = false;			// Signal between Interpreter Thread and UI Thread
//	private boolean mInputDismissed = false;			// This will be used only if we dismiss the dialog in onPause

	// *********************** DEVICE command keywords and variables ***********************

	private static final String BKW_DEVICE_LANGUAGE = "language";
	private static final String BKW_DEVICE_LOCALE = "locale";

	private static final String Device_KW[] = {		// Device command list for Format
		BKW_DEVICE_LANGUAGE, BKW_DEVICE_LOCALE
	};

	// *********************** DIALOG command keywords and variables ***********************

	private static final String BKW_DIALOG_MESSAGE = "message";
	private static final String BKW_DIALOG_SELECT = "select";

	private static final String Dialog_KW[] = {		// Dialog command list for Format
		BKW_DIALOG_MESSAGE, BKW_DIALOG_SELECT
	};

	private int mAlertItemID = 0;							// index of button or list item

	// ***************************** SELECT Command variables ******************************

	public static int SelectedItem;							// The index of the selected item
	public static boolean SelectLongClick;					// True if long click

	// *********************************** SQL keywords ************************************

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

	// ********************************* INKEY$ variables **********************************

	public static final String Numbers = "0123456789";	// translations for key codes
	public static final String Chars = "abcdefghijklmnopqrstuvwxyz";
	static ArrayList<String> InChar;

	// *************************** TEXT.INPUT command variables ****************************

	public static String TextInputString = "";

	// ******************* Graphics (GR) command keywords and variables ********************

	private boolean GRopen = false;
	private boolean GRFront;

	public static ArrayList<GR.BDraw> DisplayList;
	public static ArrayList<Integer> RealDisplayList;

	public static ArrayList<Paint> PaintList;
	public static ArrayList<Bitmap> BitmapList;

	public static double TouchX[] = new double[2];
	public static double TouchY[] = new double[2];
	public static boolean NewTouch[] = new boolean[2];

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
	private static final String BKW_GR_GROUP_CMD = "group";
	private static final String BKW_GR_HIDE = "hide";
	private static final String BKW_GR_LINE = "line";
	private static final String BKW_GR_MODIFY = "modify";
	private static final String BKW_GR_MOVE = "move";
	private static final String BKW_GR_NEWDL = "newdl";
	private static final String BKW_GR_ONGRTOUCH_RESUME = "ongrtouch.resume";
	private static final String BKW_GR_OPEN = "open";
	private static final String BKW_GR_ORIENTATION = "orientation";
	private static final String BKW_GR_OVAL = "oval";
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
	private static final String BKW_GR_SHOW_TOGGLE = "show.toggle";
	private static final String BKW_GR_STATUSBAR = "statusbar";
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
	private static final String BKW_GR_BITMAP_FILL = "fill";
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
	// gr group group
	private static final String BKW_GR_GROUP_GROUP = "group.";
	private static final String BKW_GR_GROUP_LIST = "list";
	// use existing constants for getdl and newdl
	// gr paint group
	private static final String BKW_GR_PAINT_GROUP = "paint.";
	private static final String BKW_GR_PAINT_COPY = "copy";
	private static final String BKW_GR_PAINT_GET = "get";
	private static final String BKW_GR_PAINT_RESET = "reset";
	// gr text group
	private static final String BKW_GR_TEXT_GROUP = "text.";
	private static final String BKW_GR_TEXT_ALIGN = "align";
	private static final String BKW_GR_TEXT_BOLD = "bold";
	private static final String BKW_GR_TEXT_DRAW = "draw";
	private static final String BKW_GR_TEXT_HEIGHT = "height";
	private static final String BKW_GR_TEXT_SIZE = "size";
	private static final String BKW_GR_TEXT_SKEW = "skew";
	private static final String BKW_GR_TEXT_STRIKE = "strike";
	private static final String BKW_GR_TEXT_TYPEFACE = "typeface";
	private static final String BKW_GR_TEXT_UNDERLINE = "underline";
	private static final String BKW_GR_TEXT_WIDTH = "width";
	private static final String BKW_GR_TEXT_SETFONT = "setfont";

	private static final String GR_KW[] = {				// Command list for Format
		BKW_GR_RENDER, BKW_GR_MODIFY, BKW_GR_MOVE,
		BKW_GR_BOUNDED_TOUCH2, BKW_GR_BOUNDED_TOUCH,
		BKW_GR_TOUCH2, BKW_GR_TOUCH,
		BKW_GR_ARC, BKW_GR_BRIGHTNESS, BKW_GR_CIRCLE,
		BKW_GR_CLIP, BKW_GR_CLOSE, BKW_GR_CLS,
		BKW_GR_COLOR, BKW_GR_FRONT,
		BKW_GR_GETDL, BKW_GR_NEWDL, BKW_GR_GROUP_CMD,
		BKW_GR_HIDE, BKW_GR_SHOW_TOGGLE, BKW_GR_SHOW,
		BKW_GR_LINE, BKW_GR_ONGRTOUCH_RESUME,
		BKW_GR_OPEN, BKW_GR_ORIENTATION, BKW_GR_OVAL,
		BKW_GR_POINT, BKW_GR_POLY,
		BKW_GR_RECT, BKW_GR_ROTATE_START, BKW_GR_ROTATE_END,
		BKW_GR_SAVE, BKW_GR_SCALE,
		BKW_GR_SCREEN, BKW_GR_SCREEN_TO_BITMAP,
		BKW_GR_SET_ANTIALIAS, BKW_GR_SET_PIXELS, BKW_GR_SET_STROKE,
		BKW_GR_STATUSBAR, BKW_GR_STATUSBAR_SHOW,

		// GR subgroups - Format can handle only one level of grouping
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_CREATE,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_CROP,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DELETE,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DRAWINTO_START,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DRAWINTO_END,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_DRAW,
		BKW_GR_BITMAP_GROUP + BKW_GR_BITMAP_FILL,
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
		BKW_GR_GROUP_GROUP + BKW_GR_GROUP_LIST,
		BKW_GR_GROUP_GROUP + BKW_GR_GETDL,
		BKW_GR_GROUP_GROUP + BKW_GR_NEWDL,
		BKW_GR_PAINT_GROUP + BKW_GR_PAINT_COPY,
		BKW_GR_PAINT_GROUP + BKW_GR_PAINT_GET,
		BKW_GR_PAINT_GROUP + BKW_GR_PAINT_RESET,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_ALIGN,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_BOLD,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_DRAW,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_HEIGHT,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_SIZE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_SKEW,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_STRIKE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_TYPEFACE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_UNDERLINE,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_WIDTH,
		BKW_GR_TEXT_GROUP + BKW_GR_TEXT_SETFONT,
	};

	// ****************************** Audio command keywords *******************************

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

	// ************************** SENSORS keywords and variables ***************************

	private static final String BKW_SENSORS_LIST = "list";
	private static final String BKW_SENSORS_OPEN = "open";
	private static final String BKW_SENSORS_READ = "read";
	private static final String BKW_SENSORS_CLOSE = "close";
	private static final String BKW_SENSORS_ROTATE = "rotate";

	private static final String Sensors_KW[] = {		// Command list for Format
		BKW_SENSORS_LIST, BKW_SENSORS_OPEN,
		BKW_SENSORS_READ, BKW_SENSORS_CLOSE, BKW_SENSORS_ROTATE
	};

	private SensorActivity theSensors;

	// **************************** GPS keywords and variables *****************************

	private static final String BKW_GPS_ALTITUDE = "altitude";
	private static final String BKW_GPS_LATITUDE = "latitude";
	private static final String BKW_GPS_LONGITUDE = "longitude";
	private static final String BKW_GPS_BEARING = "bearing";
	private static final String BKW_GPS_ACCURACY = "accuracy";
	private static final String BKW_GPS_SPEED = "speed";
	private static final String BKW_GPS_PROVIDER = "provider";
	private static final String BKW_GPS_SATELLITES = "satellites";
	private static final String BKW_GPS_TIME = "time";
	private static final String BKW_GPS_LOCATION = "location";
	private static final String BKW_GPS_STATUS = "status";
	private static final String BKW_GPS_OPEN = "open";
	private static final String BKW_GPS_CLOSE = "close";

	private static final String GPS_KW[] = {			// Command list for Format
		BKW_GPS_ALTITUDE, BKW_GPS_LATITUDE, BKW_GPS_LONGITUDE,
		BKW_GPS_BEARING, BKW_GPS_ACCURACY, BKW_GPS_SPEED,
		BKW_GPS_PROVIDER, BKW_GPS_SATELLITES, BKW_GPS_TIME,
		BKW_GPS_LOCATION, BKW_GPS_STATUS,
		BKW_GPS_OPEN, BKW_GPS_CLOSE,
	};

	private GPS theGPS;

	// *********************** ARRAY command keywords and variables ************************

	private enum ArrayOrderOps { DoSort, DoShuffle, DoReverse }
	private enum ArrayMathOps { DoSum, DoAverage, DoMin, DoMax, DoVariance, DoStdDev }

	private static final String BKW_ARRAY_DELETE = "delete";
	private static final String BKW_ARRAY_DIMS = "dims";
	private static final String BKW_ARRAY_FILL = "fill";
	private static final String BKW_ARRAY_LENGTH = "length";
	private static final String BKW_ARRAY_LOAD = "load";
	private static final String BKW_ARRAY_SORT = "sort";
	private static final String BKW_ARRAY_SUM = "sum";
	private static final String BKW_ARRAY_AVERAGE = "average";
	private static final String BKW_ARRAY_REVERSE = "reverse";
	private static final String BKW_ARRAY_SHUFFLE = "shuffle";
	private static final String BKW_ARRAY_MIN = "min";
	private static final String BKW_ARRAY_MAX = "max";
	private static final String BKW_ARRAY_VARIANCE = "variance";
	private static final String BKW_ARRAY_STD_DEV = "std_dev";
	private static final String BKW_ARRAY_COPY = "copy";
	private static final String BKW_ARRAY_SEARCH = "search";

	private static final String Array_KW[] = {			// Command list for Format
		BKW_ARRAY_DELETE, BKW_ARRAY_DIMS,
		BKW_ARRAY_FILL, BKW_ARRAY_LENGTH, BKW_ARRAY_LOAD,
		BKW_ARRAY_SORT, BKW_ARRAY_SUM, BKW_ARRAY_AVERAGE,
		BKW_ARRAY_REVERSE, BKW_ARRAY_SHUFFLE,
		BKW_ARRAY_MIN, BKW_ARRAY_MAX,
		BKW_ARRAY_VARIANCE, BKW_ARRAY_STD_DEV,
		BKW_ARRAY_COPY, BKW_ARRAY_SEARCH
	};

	// ******************************* LIST command keywords *******************************

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
	private static final String BKW_LIST_TOARRAY = "toarray";
	private static final String BKW_LIST_SEARCH = "search";

	private static final String List_KW[] = {			// Command list for Format
		BKW_LIST_CREATE, BKW_LIST_ADD_LIST,
		BKW_LIST_ADD_ARRAY, BKW_LIST_ADD, BKW_LIST_REPLACE,
		BKW_LIST_TYPE, BKW_LIST_GET, BKW_LIST_CLEAR,
		BKW_LIST_REMOVE, BKW_LIST_INSERT, BKW_LIST_SIZE,
		BKW_LIST_TOARRAY, BKW_LIST_SEARCH
	};

	// ****************************** BUNDLE command keywords ******************************

	private static final String BKW_BUNDLE_CREATE = "create";
	private static final String BKW_BUNDLE_PUT = "put";
	private static final String BKW_BUNDLE_GET = "get";
	private static final String BKW_BUNDLE_NEXT = "next";
	private static final String BKW_BUNDLE_TYPE = "type";
	private static final String BKW_BUNDLE_KEYS = "keys";
	private static final String BKW_BUNDLE_COPY = "copy";
	private static final String BKW_BUNDLE_CLEAR = "clear";
	private static final String BKW_BUNDLE_CONTAIN = "contain";
	private static final String BKW_BUNDLE_REMOVE = "remove";

	private static final String Bundle_KW[] = {			// Command list for Format
		BKW_BUNDLE_CREATE, BKW_BUNDLE_PUT, BKW_BUNDLE_GET, BKW_BUNDLE_NEXT,
		BKW_BUNDLE_TYPE, BKW_BUNDLE_KEYS, BKW_BUNDLE_COPY,
		BKW_BUNDLE_CLEAR, BKW_BUNDLE_CONTAIN, BKW_BUNDLE_REMOVE
	};

	// ****************************** STACK command keywords *******************************

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

	// ************* SOCKET command keywords, classes, and variables *************

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

	// ************************* DEBUG and ECHO keywords *************************

	private static final String BKW_DEBUG_ECHO_ON = BKW_ECHO_GROUP + BKW_ON;
	private static final String BKW_DEBUG_ECHO_OFF = BKW_ECHO_GROUP + BKW_OFF;
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
	private static final String BKW_DEBUG_COMMANDS = "commands";
	private static final String BKW_DEBUG_STATS = "stats";

	private static final String debug_KW[] = {			// Command list for Format
		// Do not include BKW_PRINT_SHORTCUT
		BKW_ON, BKW_OFF, BKW_PRINT,
		BKW_DEBUG_ECHO_ON, BKW_DEBUG_ECHO_OFF,
		BKW_DEBUG_DUMP_SCALARS, BKW_DEBUG_DUMP_ARRAY,
		BKW_DEBUG_DUMP_LIST, BKW_DEBUG_DUMP_STACK, BKW_DEBUG_DUMP_BUNDLE,
		BKW_DEBUG_WATCH_CLEAR, BKW_DEBUG_WATCH, BKW_DEBUG_SHOW_SCALARS,
		BKW_DEBUG_SHOW_ARRAY, BKW_DEBUG_SHOW_LIST, BKW_DEBUG_SHOW_STACK,
		BKW_DEBUG_SHOW_BUNDLE, BKW_DEBUG_SHOW_WATCH, BKW_DEBUG_SHOW_PROGRAM,
		BKW_DEBUG_SHOW, BKW_DEBUG_CONSOLE,
		BKW_DEBUG_COMMANDS, BKW_DEBUG_STATS
	};

	// Legacy: can use DEBUG.ECHO or just ECHO
	private static final String echo_KW[] = {			// Command list for Format
		BKW_ON, BKW_OFF
	};

	// ******************** FTP Client keywords and variables ********************

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

	public FTPClient mFTPClient = null;
	public String FTPdir = null;

	// ************************ CAMERA command variables *************************

	public static Bitmap CameraBitmap;
	public static boolean CameraDone;
	private int CameraNumber;
	private int NumberOfCameras;			// -1 if we don't know yet

	// ****************** Bluetooth (BT) keywords and variables ******************

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
	private int bt_state;
	public static boolean bt_Secure;

	// Name of the connected device
	private String mConnectedDeviceName = null;
	// Array adapter for the conversation thread
	private StringBuffer mOutStringBuffer;
	// Local Bluetooth adapter
	public static BluetoothAdapter mBluetoothAdapter = null;
	// Member object for the chat services
	public static BluetoothChatService mChatService = null;
	// Input buffer - use this for synchronization lock
	private final ArrayList<String> BT_Read_Buffer = new ArrayList<String>();
	public static BluetoothDevice btConnectDevice = null;

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

	// *************** Superuser (SU) and SYSTEM command keywords ****************

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

	// *********************** SOUNDPOOL command keywords ************************

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

	// ****************** RINGER command keywords and variables ******************

	private static final String BKW_RINGER_GET_MODE = "get.mode";
	private static final String BKW_RINGER_SET_MODE = "set.mode";
	private static final String BKW_RINGER_GET_VOLUME = "get.volume";
	private static final String BKW_RINGER_SET_VOLUME = "set.volume";

	private static final String ringer_KW[] = {			// Command list for Format
		BKW_RINGER_GET_MODE, BKW_RINGER_SET_MODE,
		BKW_RINGER_GET_VOLUME, BKW_RINGER_SET_VOLUME
	};

	private static final int RINGER_UNKNOWN = -1;
	private static final int RINGER_SILENT = 0;
	private static final int RINGER_VIBRATE = 1;
	private static final int RINGER_NORMAL = 2;

	// ****************** HEADSET command classes and variables ******************

	private int headsetState;
	private String headsetName;
	private int headsetMic;

	public class BroadcastsHandler extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(Intent.ACTION_HEADSET_PLUG)) {
//				String data = intent.getDataString();
//				Bundle extraData = intent.getExtras();

				headsetState = intent.getIntExtra("state", -1);
				headsetName = intent.getStringExtra("name");
				headsetMic = intent.getIntExtra("microphone", -1);
			}
		}
	}
	private BroadcastsHandler headsetBroadcastReceiver = null;

	// ******************* HTML command keywords and variables *******************

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

	// Message types for the HTML commands
	private static final int MESSAGE_HTML_OPEN     = MESSAGE_HTML_GROUP + 1;
	private static final int MESSAGE_GO_BACK       = MESSAGE_HTML_GROUP + 2;
	private static final int MESSAGE_GO_FORWARD    = MESSAGE_HTML_GROUP + 3;
	private static final int MESSAGE_CLEAR_CACHE   = MESSAGE_HTML_GROUP + 4;
	private static final int MESSAGE_CLEAR_HISTORY = MESSAGE_HTML_GROUP + 5;
	private static final int MESSAGE_LOAD_URL      = MESSAGE_HTML_GROUP + 6;
	private static final int MESSAGE_LOAD_STRING   = MESSAGE_HTML_GROUP + 7;
	private static final int MESSAGE_POST          = MESSAGE_HTML_GROUP + 8;

	private Intent htmlIntent;
	private boolean mWebFront;

	// ************************** SMS command variables **************************

	private static final String BKW_SMS_RCV_INIT = "rcv.init";
	private static final String BKW_SMS_RCV_NEXT = "rcv.next";
	private static final String BKW_SMS_SEND = "send";

	private static final String SMS_KW[] = {			// Command list for Format
		BKW_SMS_RCV_INIT, BKW_SMS_RCV_NEXT, BKW_SMS_SEND
	};

	// ********************* Speech to Text (STT) variables **********************

	public static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
	private static String sttDefaultPrompt;
	private static String sttPrompt;
	public static ArrayList <String> sttResults;
	public static boolean sttListening;
	public static boolean sttDone;

	// ********************** Text to Speech (TTS) keywords **********************

	private static final String BKW_TTS_INIT = "init";
	private static final String BKW_TTS_SPEAK_TOFILE = "speak.tofile";
	private static final String BKW_TTS_SPEAK = "speak";
	private static final String BKW_TTS_STOP = "stop";

	private static final String tts_KW[] = {			// TTS command list for Format
		BKW_TTS_INIT, BKW_TTS_SPEAK_TOFILE,
		BKW_TTS_SPEAK, BKW_TTS_STOP
	};

	// ************************* TIMER command variables *************************

	private static final String BKW_TIMER_SET = "set";
	private static final String BKW_TIMER_CLEAR = "clear";
	private static final String BKW_TIMER_RESUME = "resume";

	private static final String Timer_KW[] = {			// Command list for Format
		BKW_TIMER_SET, BKW_TIMER_CLEAR, BKW_TIMER_RESUME
	};

	// *********************** TIMEZONE command variables ************************

	private static final String BKW_TIMEZONE_SET = "set";
	private static final String BKW_TIMEZONE_GET = "get";
	private static final String BKW_TIMEZONE_LIST = "list";

	private static final String TimeZone_KW[] = {		// Command list for Format
		BKW_TIMEZONE_SET, BKW_TIMEZONE_GET, BKW_TIMEZONE_LIST
	};

	// ************************* PHONE command variables *************************

	private static final String BKW_PHONE_CALL = "call";
	private static final String BKW_PHONE_DIAL = "dial";
	private static final String BKW_PHONE_RCV_INIT = "rcv.init";
	private static final String BKW_PHONE_RCV_NEXT = "rcv.next";
	private static final String BKW_PHONE_INFO = "info";

	private static final String phone_KW[] = {			// Command list for Format
		BKW_PHONE_CALL, BKW_PHONE_RCV_INIT, BKW_PHONE_RCV_NEXT, BKW_PHONE_INFO
	};

	// ***************** VOLKEYS command keywords and variables ******************

	private static final String VolKeys_KW[] = {		// VolKeys command list for Format
		BKW_ON, BKW_OFF
	};

	// If true, this flag blocks transmission of certain Key events to the system.
	// See onKeyDown() in Run.java and GR.java.
	public static boolean mBlockVolKeys = false;

	// ************************** APP command variables **************************

	private static final String BKW_APP_BROADCAST = "broadcast";
	private static final String BKW_APP_START = "start";

	private static final String app_KW[] = {			// Command list for Format
		BKW_APP_BROADCAST, BKW_APP_START
	};

	// ***************************************************************************

	private Context getContext() {
		ContextManager cm = Basic.getContextManager();
		return cm.getContext();
	}

	// These sendMessage methods are used by mInterpreter to send messages to mHandler.
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

	// This Handler is in the UI (foreground) thread part of Run.
	// It gets control when the interpreter thread sends a Message.

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what & MESSAGE_GROUP_MASK) {
			case MESSAGE_DEFAULT_GROUP:
				switch (msg.what) {
				case MESSAGE_CHECKPOINT:				// no more messages pending
					mMessagePending = false;
					break;
				}
				break;
			case MESSAGE_CONSOLE_GROUP:
				switch (msg.what) {
				case MESSAGE_UPDATE_CONSOLE:
					updateConsole((String[])msg.obj);
					break;
				case MESSAGE_CONSOLE_LINE_CHAR:
					char c = (char)msg.arg1;
					synchronized (mConsoleBuffer) {
						int n = mOutput.size() - 1;
						String s = mOutput.get(n) + c;
						mOutput.set(n, s);
						mConsole.notifyDataSetChanged();
					}
					break;
				case MESSAGE_CLEAR_CONSOLE:
					clearConsole();
					break;
				case MESSAGE_CONSOLE_TITLE:
					String title = (String)msg.obj;
					setTitle((title != null) ? title : getResources().getString(R.string.run_name));
					break;
				}
				break;
			case MESSAGE_DIALOG_GROUP:
				switch (msg.what) {
				case MESSAGE_INPUT_DIALOG:
					doInputDialog((DialogArgs)msg.obj);
					break;
				case MESSAGE_ALERT_DIALOG:
					doAlertDialog((DialogArgs)msg.obj);
					break;
				case MESSAGE_TOAST:
					String msgText = (String)msg.obj;
					Bundle b = msg.getData();
					int duration = b.getInt("dur", Toast.LENGTH_SHORT);
					Toast toast = Toaster(msgText, duration);
					int x = msg.arg1;
					int y = msg.arg2;
					toast.setGravity(Gravity.CENTER, x,y);
					toast.show();
					break;
				}
				break;
			case MESSAGE_BT_GROUP:
				handleBTMessage(msg);					// handle Bluetooth Messages
				break;
			case MESSAGE_HTML_GROUP:
				handleHtmlMessage(msg);					// handle HTML Messages
				break;
			case MESSAGE_DEBUG_GROUP:
				handleDebugMessage(msg);				// handle debug Messages
				break;
			default:									// unrecognized Message
				break;									// ignore it
			}
		}
	}; // mHandler

	private Toast Toaster(CharSequence msg) {			// default: short, high toast
		Toast toast = Toaster(msg, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 50);
		return toast;
	}

	private Toast Toaster(CharSequence msg, int duration) {
		Toast toast = Toast.makeText(Run.this, msg, duration);
		return toast;
	}

	private void updateConsole(String... strs) {

		synchronized (mConsoleBuffer) {
			if (mConsoleBuffer.size() != 0) {			// if any lines
				mOutput.addAll(mConsoleBuffer);			// write each line to screen
				mConsoleBuffer.clear();
			}
			if ((strs != null) && (strs.length != 0)) {
				for (String str : strs) {
					mOutput.add(str);
				}
			}
			mConsole.notifyDataSetChanged();
		}
	}

	private void errorToConsole(String str) {			// conditionally write an error message to the console
		if (mOnErrorInt == null) {						// if there is an OnError label, do not show the message.
			updateConsole(str);
		}
	}

	// TODO: Move this to Interpreter when Interpreter is put in its own class file
	public static Intent buildVoiceRecognitionIntent() {
		if (sttPrompt == null) { sttPrompt = sttDefaultPrompt; }
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
				RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
		intent.putExtra(RecognizerIntent.EXTRA_PROMPT, sttPrompt);
		return intent;
	}

	// ************************************* Run Entry Point **************************************
	// Called by Android runtime when Run is launched from Editor or AutoRun

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.v(LOGTAG, "onCreate " + this.toString());

		if (Basic.lines == null) {
			Log.e(LOGTAG, "onCreate: Lost context. Bail out.");
			finish();
			return;
		}

//		Log.v(LOGTAG, "isOld  " + isOld);
		if (isOld) {
			if (theWakeLock != null) {
				theWakeLock.release();
			}
			if (theWifiLock != null) {
				theWifiLock.release();
			}
		}
		theWakeLock = null;
		theWifiLock = null;
		isOld = true;

		ContextManager cm = Basic.getContextManager();
		cm.registerContext(ContextManager.ACTIVITY_RUN, this);
		cm.setCurrent(ContextManager.ACTIVITY_RUN);

		mConsoleIntent = getIntent();						// keep a reference to the Intent that started Run
		InitRunVars();

															// Establish the output screen
		setContentView(R.layout.console);
		TextStyle style = new TextStyle(Basic.defaultTextStyle, Settings.getConsoleTypeface(this));
		mConsole = new Basic.ColoredTextAdapter(this, mOutput, style);
		clearConsole();
		lv = (ConsoleListView)findViewById(R.id.console);
		lv.setAdapter(mConsole);
		lv.setKeyboardManager(mKeyboardChangeListener);
		lv.setTextFilterEnabled(false);
		lv.setBackgroundColor(
			Settings.getEmptyConsoleColor(this).equals("line")
				? mConsole.getLineColor()
				: mConsole.getBackgroundColor());			// default is "background"
		if (Settings.getLinedConsole(this)) {
			lv.setDivider(new ColorDrawable(mConsole.getLineColor()));	// override default from theme, sometimes it's invisible
			if (lv.getDividerHeight() < 1) { lv.setDividerHeight(1); }	// make sure the divider shows
		} else {
			lv.setDividerHeight(0);							// don't show the divider
		}

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		setRequestedOrientation(Settings.getSreenOrientation(this));

		headsetBroadcastReceiver = new BroadcastsHandler();
		this.registerReceiver(headsetBroadcastReceiver, new IntentFilter(Intent.ACTION_HEADSET_PLUG));

		// Listeners for Console Touch

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				triggerInterrupt(Interrupt.CONS_TOUCH_BIT);
				TouchedConsoleLine = position + 1;
				ConsoleLongTouch = false;
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				triggerInterrupt(Interrupt.CONS_TOUCH_BIT);
				TouchedConsoleLine = position + 1;
				ConsoleLongTouch = true;
				return true;
			}
		});

		mInterpreter = new Interpreter();					// start the interpreter in a background Thread
		mInterpreter.start();

	} // end onCreate

	private void InitRunVars() {							// init vars needed for Run
		clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		mInterpreter = null;								// reference to Interpreter Thread
	}

	// The following methods run in the foreground. The are called by asynchronous user events
	// caused by the user pressing a key.

	private boolean handleMenuKey(KeyEvent event) {
		boolean trapped = triggerInterrupt(Interrupt.MENU_KEY_BIT);
		if (trapped) return true;		// trapped by onMenuKey:

		if ( Basic.isAPK ||				// if MENU key hit in APK and not trapped by OnMenuKey
			(event == null) )			// or relayed via EventList with null event object
		{
			return true;				// then tell OS to ignore it
		}
		return false;					// let Android create the Run Menu
	}

	public void GR_BackPressed() {
		boolean trapped = triggerInterrupt(Interrupt.BACK_KEY_BIT);
		if (trapped) return;			// trapped by onBackKey

		if (mInterpreter != null) { mInterpreter.GRstop(); }	// stop GR, don't wait for it
		doBackPressed();				// stop interpreter, too
	}

	@Override
	public void onBackPressed() {		// system BACK key callback
		boolean trapped = triggerInterrupt(Interrupt.BACK_KEY_BIT);
		if (trapped) return;			// trapped by onBackKey:

		doBackPressed();				// untrapped BACK key
	}

	private void doBackPressed() {		// BACK key pressed in Run, interrupt check already done
		if (Basic.DoAutoRun) {
			Exit = true;				// if AutoRun, untrapped BACK key always means exit
		}
		if (mInterpreterRunning) {
			Stop = true;				// if running a program, stop it
		} else {
			finish();					// else already stopped, return to the Editor
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {		// The user hit a key
		// Log.v(LOGTAG, "onKeyDown" + keyCode);

		// Eat the MENU key Down event, will handle the Up event.
		if (keyCode == KeyEvent.KEYCODE_MENU)	return true;

		// If event is null, we called this directly from runLoop(),
		// so the return value does not matter.
		if (event == null)						return true;

		// Alternate: if the user program has set mBlockVolKeys, block everything but BACK
		// if (mBlockVolKeys && (keyCode != KeyEvent.KEYCODE_BACK)) return true;

		// If the user program has set mBlockVolKeys, block volume and headset keys.
		if (mBlockVolKeys && (
			(keyCode == KeyEvent.KEYCODE_VOLUME_UP) ||
			(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) ||
			(keyCode == KeyEvent.KEYCODE_VOLUME_MUTE) ||
			(keyCode == KeyEvent.KEYCODE_MUTE) ||
			(keyCode == KeyEvent.KEYCODE_HEADSETHOOK))) { return true; }

		// Otherwise pass the KeyEvent up the chain.
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// Log.v(LOGTAG, "onKeyUp" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return handleMenuKey(event);
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return super.onKeyUp(keyCode, event);
		}

		char c;
		String theKey = "@";
		int n ;
		if (keyCode >= 7 && keyCode <= 16) {
			n = keyCode - 7;
			c = Numbers.charAt(n);
			theKey = Character.toString(c);

		} else if (keyCode >=29 && keyCode <= 54) {
			n = keyCode -29;
			c = Chars.charAt(n);
			theKey = Character.toString(c);
		} else if (keyCode == 62) {
			c = ' ';
			theKey = Character.toString(c);
		} else if (keyCode >= 19 && keyCode <= 23) {
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
		triggerInterrupt(Interrupt.KEY_BIT);

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		// Called when the menu key is pressed.
		super.onCreateOptionsMenu(menu);
		if (!Settings.getConsoleMenu(this)) { return false; }

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
	public boolean onPrepareOptionsMenu(Menu menu) {	// Executed when Menu key is pressed (before onCreateOptionsMenu() above.

		super.onPrepareOptionsMenu(menu);
		MenuItem item;
		if (Stop) {										// If program running display with Editor dimmed
			item = menu.getItem(0);						// Other wise dim Stop and undim Editor
			item.setEnabled(false);
			item = menu.getItem(1);
			item.setEnabled(true);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	// A menu item is selected
		switch (item.getItemId()) {

		case R.id.stop:										// User wants to stop execution
			updateConsole("Stopped by user.");				// tell user
			Stop = true;									// signal main loop to stop
			disableInterrupt(Interrupt.BACK_KEY_BIT);		// menu-selected stop is not trappable
			return true;

		case R.id.editor:									// User pressed Editor
			if (!Basic.DoAutoRun && SyntaxError && (mInterpreter != null)) {
				Editor.SyntaxErrorDisplacement = mInterpreter.ExecutingLineIndex;
			}

			// TODO: This can't possibly be enough. Shouldn't we run cleanup() or equivalent?
			Basic.clearContextManager();					// unregister all Context references
			if (mChatService != null) {
				mChatService.stop();
				mChatService = null;
			}

			finish();

		}
		return true;
	}

	@Override
	protected void onStart() {
		Log.v(LOGTAG, "onStart " + this.toString());
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.v(LOGTAG, "onResume " + this.toString());

		RunPaused = false;
		triggerInterrupt(Interrupt.BACKGROUND_BIT);
		Basic.getContextManager().onResume(ContextManager.ACTIVITY_RUN);

//		if (WaitForInput) { theAlertDialog = doInputDialog().show(); } maybe???
		super.onResume();
	}

	@Override
	protected void onPause() {
		// The Android OS wants me to dismiss dialog while paused so I will

	/*	if (WaitForInput) {
			theAlertDialog.dismiss();
			InputDismissed = true;
		}
	*/
		Log.v(LOGTAG, "onPause " + this.toString());
		if (lv.mKB != null) { lv.mKB.forceHide(); }
		Basic.getContextManager().onPause(ContextManager.ACTIVITY_RUN);

		// If there is a Media Player running, pause it and hope
		// that it works.
	/*	if (theMP != null) {
			try { theMP.pause(); } catch (IllegalStateException e) {}
		}
	*/
		RunPaused = true;

		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.v(LOGTAG, "onStop " + this.toString());
		if (!GR.Running) {
			triggerInterrupt(Interrupt.BACKGROUND_BIT);
		}
		super.onStop();
	}

	@Override
	protected void onRestart() {
		Log.v(LOGTAG, "onRestart " + this.toString());
		super.onRestart();
	}

	@Override
	protected void onDestroy() {
		Log.v(LOGTAG, "onDestroy " + this.toString());

		if (theSensors != null) {
			theSensors.stop();
			theSensors = null;
		}

		if (theGPS != null) {
			Log.d(LOGTAG, "Stopping GPS from onDestroy");
			theGPS.stop();
			theGPS = null;
		}

		if (theWakeLock != null) {
			theWakeLock.release();
			theWakeLock = null;
		}

		if (theWifiLock != null) {
			theWifiLock.release();
			theWifiLock = null;
		}

		if (headsetBroadcastReceiver != null) {
			unregisterReceiver(headsetBroadcastReceiver);
		}

		// BitmapListClear();

		super.onDestroy();
	}

	@Override
	public void onLowMemory() {
		Log.d(LOGTAG, "onLowMemory");
		// If interpreter is running and OnLowMemory: label exists, set the Low Mem interrupt
		// otherwise notify the user via the Console.
		boolean trapped = triggerInterrupt(Interrupt.LOW_MEM_BIT);
		if (!trapped) { updateConsole("Warning: Low Memory"); }
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case REQUEST_CONNECT_DEVICE_SECURE:
				// When DeviceListActivity returns with a device to connect
				if ((resultCode == Activity.RESULT_OK) && (mInterpreter != null)) {
					mInterpreter.connectDevice(data, bt_Secure);
				}
				break;
			case REQUEST_CONNECT_DEVICE_INSECURE:
				// When DeviceListActivity returns with a device to connect
				if ((resultCode == Activity.RESULT_OK) && (mInterpreter != null)) {
					mInterpreter.connectDevice(data, false);
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
				if (resultCode == RESULT_OK) {
					sttResults = new ArrayList<String>();
					sttResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
				}
				sttDone = true;
				break;
		}
	}

	// ********************************************************************************************
	// Methods used to communicate between Run and Interpreter
	// or utilities used in both threads

	private boolean triggerInterrupt(Integer bit) {		// delegate to the interpreter if it exists
		return (mInterpreter != null) && (mInterpreter.triggerInterrupt(bit));
	}

	private void disableInterrupt(Integer bit) {		// delegate to the interpreter if it exists
		if (mInterpreter != null) { mInterpreter.disableInterrupt(bit); }
	}

	private void clearConsole() {
		synchronized (mConsoleBuffer) {
			mConsole.clear();
			mOutput.trimToSize();
			mOutput.ensureCapacity(MIN_CONSOLE_LINES);
			mConsole.notifyDataSetChanged();
		}
	}

	private void checkpointMessage() {
		mMessagePending = true;
		sendMessage(MESSAGE_CHECKPOINT);
	}

	private static String chomp(String str) {
		return str.substring(0, str.length() - 1);
	}

	private static String quote(String str) {
		return '\"' + str + '\"';
	}

	// ********************************************************************************************
	// Static methods that would be attached to a class if we didn't nest everything.
	// When this file is broken up, these should be attached to appropriate top-level classes.

	// Get the absolute index of the point where name can be inserted into
	// a list of names to preserve alphabetical order within the list.
	// The name must not already be in names.
	//
	// Use of Collections.binarySearch for speed: thanks to Nicolas Mougin.
	public static int newVarIndex(ArrayList<String> names, String name) {
		int index = Collections.binarySearch(names, name);
		if (index >= 0) { throw new InvalidParameterException("newVarIndex: variable " + name + " already exists"); }
		return -(index + 1);								// return the absolute index
//		Alternate ending to add log:
//		index = -(index + 1);								// make the index absolute
//		Log.v(LOGTAG, CLASSTAG + " newVarIndex() create var " + name + " at index " + index + "/" + vNames.size() + "(start=" + vStart + ")");
//		return index;
	}

	// ***************** Dialogs *****************

	// Superset of fields needed by all Dialogs.
	private class DialogArgs {
		public String mTitle = null;
		public String mInputDefault = null;
		public String mMessage = null;
		public String mButton1 = null;
		public String mButton2 = null;
		public String mButton3 = null;
		public String[] mList = null;
		public Var.Val mReturnVal = null;
		public Var.Val mLongClickVal = null;
	}

	private void doInputDialog(DialogArgs args) {
		Context context = getContext();
		Log.d(LOGTAG, "InputDialog context " + context);

		EditText text = new EditText(context);
		text.setText(args.mInputDefault);
		Var.Val returnVal = args.mReturnVal;
		if (returnVal.isNumeric()) {
			text.setInputType(0x00003002);					// Limits keys to signed decimal numbers
		}
		String btnLabel = args.mButton1;
		if (btnLabel == null) { btnLabel = "Ok"; }

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder
			.setView(text)
			.setCancelable(true)
			.setTitle(args.mTitle)							// default null, no title displayed if null or empty
			.setPositiveButton(btnLabel, null);				// need to override default View click handler to prevent
															// auto-dismiss, but can't do it until after show()

		builder.setOnCancelListener(
			new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					Log.d(LOGTAG, "Input Dialog onCancel");
					mInputCancelled = true;					// signal read by executeINPUT
				}
			});

		final AlertDialog dialog = builder.create();
		dialog.setOnDismissListener(
			new DialogInterface.OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					Log.d(LOGTAG, "Input Dialog onDismiss");
					releaseLOCK();							// release the lock that executeINPUT is waiting for
				}
			});
		dialog.getWindow().setSoftInputMode(android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		dialog.show();
		dialog.getButton(DialogInterface.BUTTON_POSITIVE)	// now we can replace the View click listener
			.setOnClickListener(new InputDialogClickListener(dialog, text, args));	// to prevent auto-dismiss
	}

	private class InputDialogClickListener implements View.OnClickListener {
		// Use inner class members so we don't need as many outer class members.
		private final AlertDialog mmDialog;
		private final EditText mmText;
		private final boolean mmIsNumeric;
		private final Var.Val mmVal;
		public InputDialogClickListener(AlertDialog dialog, EditText text, DialogArgs args) {
			mmDialog = dialog;
			mmText = text;
			mmVal = args.mReturnVal;
			mmIsNumeric = mmVal.isNumeric();
		}

		public void onClick(View view) {
			String theInput = mmText.getText().toString();
			if (mmIsNumeric) {								// Numeric Input Handling
				try {
					double d = Double.parseDouble(theInput.trim());	// have java parse it into a double
					mmVal.val(d);
				} catch (Exception e) {
					Log.d(LOGTAG, "Input Dialog bad input");
					Toaster("Not a number. Try Again.").show();
					return;
				}
			} else {										// String Input Handling
				mmVal.val(theInput);
			}
			mmDialog.dismiss();
			Log.d(LOGTAG, "Input Dialog done, positive");
		}
	}

	private void doAlertDialog(DialogArgs args) {
		Context context = getContext();
		Log.d(LOGTAG, "AlertDialog context " + context);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		String positive = args.mButton1;
		String neutral = args.mButton2;
		String negative = args.mButton3;
		String[] list = args.mList;
		AlertDialogClickListener listener = new AlertDialogClickListener();

		builder
			.setCancelable(true)
			.setTitle(args.mTitle);							// no title if null or empty
		if (positive != null) { builder.setPositiveButton(positive, listener); }
		if (neutral != null) { builder.setNeutralButton(neutral, listener); }
		if (negative != null) { builder.setNegativeButton(negative, listener); }

		if (list != null) {
			builder.setItems(list,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						mAlertItemID = which + 1;			// convert to 1-based index
					}
				});
		} else {
			builder.setMessage(args.mMessage);				// list and message are mutually exclusive
		}

		builder.setOnCancelListener(
			new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					Log.d(LOGTAG, "Alert Dialog onCancel");
					mAlertItemID = 0;						// no button clicked or item selected
				}
			});

		final AlertDialog dialog = builder.create();
		dialog.setOnDismissListener(
			new DialogInterface.OnDismissListener() {
				public void onDismiss( DialogInterface dialog) {
					Log.d(LOGTAG, "Alert Dialog onDismiss");
					releaseLOCK();							// release the lock that executeINPUT is waiting for
				}
			});
		dialog.show();
	}

	private class AlertDialogClickListener implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int buttonID) {
			Log.d(LOGTAG, "AlertDialog done, button " + buttonID);
			int id = 0;										// default: no button
			switch (buttonID) {
				case DialogInterface.BUTTON_POSITIVE: id = 1; break;
				case DialogInterface.BUTTON_NEUTRAL:  id = 2; break;
				case DialogInterface.BUTTON_NEGATIVE: id = 3; break;
			}
			mAlertItemID = id;
		}
	}

	private void releaseLOCK() {
		if (!mWaitForLock) return;

		synchronized (LOCK) {
			mWaitForLock = false;							// semaphore used in waitForLOCK
			LOCK.notify();									// release the lock
		}
	}

	// ***************** Bluetooth message handler *****************

	public boolean handleBTMessage(Message msg) {
		switch (msg.what) {
			case MESSAGE_STATE_CHANGE:
				bt_state = msg.arg1;
				break;
			case MESSAGE_WRITE:
//				byte[] writeBuf = (byte[]) msg.obj;
				// construct a string from the buffer
//				String writeMessage = new String(writeBuf);
				break;
			case MESSAGE_READ:
				byte[] readBuf = (byte[]) msg.obj;
				String readMessage = "";
				// construct a string from the valid bytes in the buffer
//				String readMessage = new String(readBuf, 0, msg.arg1);
				try {
					readMessage = new String(readBuf, "ISO-8859-1");
				} catch (Exception e) {
					errorToConsole("Error: " + e.getMessage());
				}
				readMessage = readMessage.substring(0, msg.arg1);
				synchronized (BT_Read_Buffer) {
					if (BT_Read_Buffer.size() == 0) {
						triggerInterrupt(Interrupt.BT_READY_BIT);
					}
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

	// ***************** HTML message handler *****************

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

	//=====================DEBUGGER DIALOG STUFF========================

	private void DialogSelector(int selection) {
		dbDialogScalars = false;
		dbDialogArray = false;
		dbDialogList = false;
		dbDialogStack = false;
		dbDialogBundle = false;
		dbDialogWatch = false;
		dbDialogConsole = false;
		dbDialogProgram = false;
		switch (selection) {
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

	private void doDebugDialog() {

		int lineIndex = 0;
		String text = "";
		if (mInterpreter != null) {
			lineIndex = mInterpreter.ExecutingLineIndex;
			text = mInterpreter.ExecutingLineBuffer.text();
		}

		ArrayList<String> msg = new ArrayList<String>();

		if (!dbDialogProgram) {
			msg = mInterpreter.dbDoFunc();
			msg.add("Executable Line #:    "
					+ Integer.toString(lineIndex + 1) + '\n'
					+ chomp(text));
		}

		if (dbDialogScalars)
			msg.addAll(mInterpreter.dbDoScalars("  "));
		if (dbDialogArray)
			msg.addAll(mInterpreter.dbDoArray("  "));
		if (dbDialogList)
			msg.addAll(mInterpreter.dbDoList("  "));
		if (dbDialogStack)
			msg.addAll(mInterpreter.dbDoStack("  "));
		if (dbDialogBundle)
			msg.addAll(mInterpreter.dbDoBundle("  "));
		if (dbDialogWatch)
			msg.addAll(mInterpreter.dbDoWatch("  "));

		if (dbDialogProgram) {
			for (int i = 0; i < Basic.lines.size(); ++i) {
				msg.add(((i == lineIndex) ? " >>" : "   ")	// mark current line
						+ (i + 1) + ": "					// one-based line index
						+ chomp(Basic.lines.get(i).text())); // remove newline
			}
		}

		LayoutInflater inflater = getLayoutInflater();
		View dialogLayout = inflater.inflate(R.layout.debug_dialog_layout, null);

		ListView debugView = (ListView)dialogLayout.findViewById(R.id.debug_list);
		debugView.setAdapter(new ArrayAdapter<String>(Run.this, R.layout.debug_list_layout, msg));
		debugView.setVerticalScrollBarEnabled(true);
		if (dbDialogProgram) {
			debugView.setSelection(lineIndex);
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(Run.this)
				.setCancelable(true).setTitle(R.string.debug_name)
				.setView(dialogLayout);

		builder.setOnCancelListener(
			new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface arg0) {
					DebuggerHalt = true;
					WaitForDebugResume = false;
					releaseDebugLOCK();
				}
			});

		builder.setPositiveButton("Resume",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					WaitForDebugResume = false;
					releaseDebugLOCK();
				}
			});

		builder.setNeutralButton("Step",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					DebuggerStep = true;
					WaitForDebugResume = true;
					releaseDebugLOCK();
				}
			});

		builder.setNegativeButton("View Swap",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dbSwap = true;
					releaseDebugLOCK();
				}
			});

		dbDialog = builder.show();
		dbDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
									   WindowManager.LayoutParams.MATCH_PARENT);
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
		debugView.setAdapter(new ArrayAdapter<String>(Run.this, R.layout.simple_list_layout_1, msg));
		debugView.setVerticalScrollBarEnabled(true);
		debugView.setClickable(true);

		debugView.setOnItemClickListener(
			new AdapterView.OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					DialogSelector(position);
					boolean dosel =
						(dbDialogArray  && WatchedArray  == null) ||
						(dbDialogList   && WatchedList   == -1)   ||
						(dbDialogStack  && WatchedStack  == -1)   ||
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

		AlertDialog.Builder builder = new AlertDialog.Builder(Run.this)
			.setCancelable(true)
			.setTitle("Select View:")
			.setView(dialogLayout);

		builder.setOnCancelListener(
			new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface arg0) {
					WaitForSwap = false;
				}
			});

		builder.setPositiveButton("Confirm",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					WaitForSwap = false;
					dbSwap = false;
				}
			});

/*  // leave out until the element selector is done.
		builder.setNeutralButton("Choose Element",
			new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,int which) {
					WaitForSelect = true;
				}
			});
*/
		dbSwapDialog = builder.show();
		dbSwapDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
										   WindowManager.LayoutParams.MATCH_PARENT);
	}

	private void doDebugSelectDialog() {
		if (dbSelectDialog != null) { dbSelectDialog.dismiss(); }

		ArrayList<String> msg = new ArrayList<String>();
		// TODO: What did Michael have in mind?
	}

	public boolean handleDebugMessage(Message msg) {
		switch (msg.what) {
			case MESSAGE_DEBUG_DIALOG: doDebugDialog();       break;
			case MESSAGE_DEBUG_SWAP:   doDebugSwapDialog();   break;
			case MESSAGE_DEBUG_SELECT: doDebugSelectDialog(); break;
			default:
				return false;									// message not recognized
		}
		return true;											// message handled
	}

	private void releaseDebugLOCK() {
		if (!mWaitForDebugLock) return;

		synchronized (DB_LOCK) {
			mWaitForDebugLock = false;							// semaphore used in waitForLOCK
			DB_LOCK.notify();									// release the lock
		}
	}

/* ****************************** Start of Basic's run program code *******************************
 * 
 * BASIC! program runner -- the BASIC! interpreter -- runs in a background thread.
 * The code is organized (?) as follows:
 * 
 * The first chunk is the code that initializes the interpreter and then controls
 * the running of the program by dispatching each line of code to be executed.
 * 
 * The second chunk is a set of Command tables that holds the function and command names.
 * 
 * The third chunk is made up of parsing methods. They identify the command on each line and call
 * its execution method. The execution methods call the parsing methods to identify variables and
 * evaluate expressions.
 * 
 * The final and largest part of Interpreter is code that actually executes user program statements.
 * 
 */

/* Implementation notes:
	1.	This class must eventually be moved to its own file. Perhaps to run as a service?
		Nested classes can't have static members, so until it moves, a few things
		that really belong to this class are owned by Run instead.
	2.	Command lines are in the Basic.lines array. Line 0 is valid.
		To jump to a new line, set ExecutingLineIndex to the line before.
		RunLoop() increments the index before loading ExecutingLineBuffer.
	3.	A command line is a ProgramLine object. The first time a line runs,
		it is fully parsed from the text to locate the right Command object.
		The ProgramLine stores the Command for future runs of the same line.
		Some commands use a second Command reference for a sub-command.
		The THEN and ELSE statements of a single-line IF are fully-parsed.
	4.	Interrupts: OnError is special, so it is in a named variable.
		The others are all anonymous. All, including OnError, are in
		mIntA for "armed" interrupts, i.e, those that have interrupt labels.
		All except OnError can be in mIntT, the triggered interrupts.
		Triggering copies the Interrupt object from mIntA to mIntT.
		Servicing removes it from mIntT. OnError is always serviced first.
		The rest are serviced in the order they occur.
	5.	Activity instances other than Run send events to the Interpreter
		through static Run.mEventList, and instance of Run.EventHoder.
	6.	Basic.mContextMgr is a static instance of ContextManager.
		The three major Activity instances (Run, GR, Web) are required
		to register actions with the ContextManager. The Interpreter
		uses these actions to track which context is active (if any).
	7.	Variables. The symbol table is two lists: VarNames and Vars.
		Each Var in Vars holds a reference to a Var.Val (for scalars)
		or a Var.ArrayDef or a Var.FnDef
		Both lists are internal, so index 0 is valid.
	8.	A scalar value is a Val object. Sometimes variables are named
		as if the value and the variable are the same, but they are not.
	9.	Paint objects are in the PaintList array. Object 0 cannot be attached
		to graphics objects, but is accessible otherwise. Objects 0 and 1 are
		initially opaque black, antialias on, style.FILL, stroke weight 0.0.
	10.	GR.OPEN adds another Paint the color of the background at index 2.
		GR.CLS reinitializes Paints 0 and 1 but does not add PaintList[2].
 */

	public class Interpreter extends Thread {

		// The execution of the basic program is done by this background Thread.
		// This is done to keep the UI responsive.

		// ***************************** ForNext class *****************************
		// Records information about a For/Next loop. Objects go on the ForNextStack.

		private class ForNext {
			private int mLine;								// loop return location
			private Var.Val mVal;							// loop index
			private double mStep;							// step value
			private double mLimit;							// limit value

			public ForNext(int line, Var.Val val, double step, double limit) {
				mLine = line;
				mVal = val;
				mStep = step;
				mLimit = limit;
			}

			public int line() { return mLine; }
			public boolean doStep() {
				double idx = mVal.nval();						// get the loop index
				idx += mStep;									// do the STEP
				mVal.val(idx);									// update the loop index
				return (((mStep > 0) && (idx > mLimit)) ||		// test limit
						((mStep <= 0) && (idx < mLimit)));		// return true if stepped past limit
			}
		} // class ForNext

		// *************************** WhileRepeat class ***************************
		// Records information about a While/Repeat loop. Objects go on the WhileStack.

		private class WhileRepeat {
			private int mLine;								// loop return location
			private int mArgOffset;							// offset to "while" condition expression

			public WhileRepeat(int line, int offset) {
				mLine = line;
				mArgOffset = offset;
			}

			public int line() { return mLine; }
			public int offset() { return mArgOffset; }
		} // class ForNext

		// **************************** SwitchDef class ****************************
		// Fields record line numbers of the Sw.xxx commands.

		private class SwitchDef {
			public final ArrayList<Integer> mCases = new ArrayList<Integer>();	// Sw.Case
			public int mDefault = -1;						// Sw.Default
			public int mEnd = -1;							// Sw.End
		}

		// ************************* CallStackFrame class *************************
		// System state saved across function calls. Objects go on the FunctionStack.

		private class CallStackFrame {
			private Var.FnDef mFnDef;						// reference to called function
			private ProgramLine mPL;						// reference to ProgramLine of function call
															// might not be Lines[mELI], could be substitute
			private int mELI;								// record line number of function call
			private int mLI;								// record LinIndex after closing paren
			private String mPKW;							// record PossibleKeyWord

			public Var.FnDef fnDef() { return mFnDef; }

			public String name() { return mFnDef.name(); }

			public void store(Var.FnDef fn) {
				mFnDef = fn;
				mPL = ExecutingLineBuffer;
				mELI = ExecutingLineIndex;
				mPKW = PossibleKeyWord;
			}
			public void storeLI() {
				mLI = LineIndex;
			}

			public void restore() {
				ExecutingLineBuffer = mPL;
				ExecutingLineIndex = mELI;
				LineIndex = mLI;
				PossibleKeyWord = mPKW;
			}
		}

		// **************************** START OF VARIABLE DEFINITIONS *****************************

		// ************************* Various execution control variables **************************

		private int ExecutingLineIndex = 0;					// Points to the current line in Basic.lines
		private ProgramLine ExecutingLineBuffer = null;		// Holds the current line being executed
		private int LineIndex = 0;							// Current displacement into ExecutingLineBuffer's line

		private Var.Table mGlobalSymbolTable;				// The symbol table that contains all global Vars
		private Var.Table mSymbolTable;						// The current symbol table, all non-global Vars in scope
		private Stack<Var.Table>mSymbolTables;				// Stack of symbol tables to search

		// "Armed" interrupts and "Triggered" interrupts. "Triggered" are serviced in order.
		private final HashMap<Integer, Interrupt> mIntA = new HashMap<Integer, Interrupt>();
		private final LinkedHashMap<Integer, Interrupt> mIntT = new LinkedHashMap<Integer, Interrupt>();

		private Stack<Integer> GosubStack;					// Stack used for Gosub/Return
		private Stack<ForNext> ForNextStack;				// Stack used for For/Next
		private Stack<WhileRepeat> WhileStack;				// Stack used for While/Repeat
		private Stack<Integer> DoStack;						// Stack used for Do/Until

		private Stack<Integer> IfElseStack;					// Stack for IF-ELSE-ENDIF operations
		private Stack<CallStackFrame> FunctionStack;		// State saved through the currently executing functions

		private HashMap<String, Var.FnDef> mFunctionTable;	// Globally-accessible table of user-defined functions
		private boolean fnRTN = false;						// Set true by fn.rtn. Cause RunLoop() to return

		// Working Var objects for the parser, one of each type.
		// These are used to avoid creating a new Var every time a variable is parsed.
		private Var.ScalarVar mScalarVar;					// Working Var for parser
		private Var.ArrayVar mArrayVar;						// Working Var for parser
		private Var.FunctionVar mFunctionVar;				// Working Var for parser

		private boolean VarIsInt = false;					// temporary integer status used only by fprint
		private Var.Val mVal;								// *TEMPORARY* bridge value set by getVar() and friends
															// TODO: this is a temporary stand-in for theValueIndex

		private String StringConstant = "";					// Storage for a string constant
		private boolean SEisLE = false;						// If a String expression result is a logical expression

		private Double EvalNumericExpressionValue;			// Return value from EvalNumericExprssion()
		private Long EvalNumericExpressionIntValue;			// Integer copy of EvalNumericExpressionValue when VarIsInt is true
		private Double GetNumberValue;						// Return value from GetNumber()
		private String PossibleKeyWord = "";				// Used when TO, STEP, THEN are expected
		private static final String NO_ERROR = "No error";
		private String mErrorMsg = NO_ERROR;

		// *************************** Random number function variables ***************************

		private Random randomizer = null;

		// ******************************* Print command variables ********************************

		private String PrintLine = "";						// Hold the Print line currently being built
		private String textPrintLine = "";					// Hold the TextPrint line currently being built
		private boolean PrintLineReady = false;				// Signals a line is ready to print or write

		// ******************************** List command variables ********************************

		private ArrayList<ArrayList> theLists;
		private ArrayList<Var.Type> theListsType;

		// ******************************* Bundle command variables *******************************

		private ArrayList<Bundle> theBundles;

		// ******************************* Stack command variables ********************************

		private ArrayList<Stack> theStacks;
		private ArrayList<Var.Type> theStacksType;

		// ******************************** Read command variables ********************************

		private ArrayList<Var.Val> readData;
		private int readNext;

		// ********************************** File I/O variables **********************************

		private ArrayList<FileInfo> FileTable;				// File table list

		// ******************************** SQL command variables *********************************

		private ArrayList<SQLiteDatabase> DataBases;		// List of created databases
		private ArrayList<Cursor> Cursors;					// List of database cursors

		// ******************************** Font command variables ********************************

		private ArrayList<Typeface> FontList;

		// ***************************** Graphics commands variables ******************************

		private Intent mGrIntent;							// Graphics Activity (GR) Intent
		private boolean mShowStatusBar;
		private Canvas drawintoCanvas = null;

		// ******************************** HTML command variables ********************************

		public ArrayList<String> htmlData_Buffer;
		private boolean htmlOpening;

		// ******************************* Audio command variables ********************************

		private MediaPlayer theMP = null;
		private ArrayList<MediaPlayer> theMPList;
		private ArrayList<String> theMPNameList;
		private boolean PlayIsDone;
		private MediaRecorder mRecorder = null;

		// ***************************** SoundPool command variables ******************************

		private SoundPool theSoundPool;

		// *************************** Text-to-Speech command variables ***************************

		private TextToSpeechActivity theTTS;

		// ****************************** Vibrate command variables *******************************

		private Vibrator myVib;

		// ******************************* Phone command variables ********************************

		public int phoneState = 0;
		public String phoneNumber = "";
		public boolean phoneRcvInited = false;
		public TelephonyManager mTM;
		public SignalStrength mSignalStrength = null;

		// ******************************** SMS command variables *********************************

		private ArrayList<String> smsRcvBuffer;

		// ******************************* Timer command variables ********************************

		public Timer theTimer;

		// ***************************** TimeZone commands variables ******************************

		public String theTimeZone = "";

		// ******************************** Run command variables *********************************

		private AutoRun mAutoRun;							// used to load the new program

		// ************************ Superuser and System command variables ************************

		private boolean mIsSU = true;						// set true for SU commands, false for System commands
		private DataOutputStream SUoutputStream;
		private BufferedReader SUinputStream;
		private Process SUprocess;
		private ArrayList <String> SU_ReadBuffer;
		private SUReader theSUReader = null;

		// ******************************* Debug commands variables *******************************

		private boolean Debug = false;
		private boolean Echo = false;

		// ****************************** START OF INTERPRETER CODE *******************************

		private UncaughtExceptionHandler mDefaultExceptionHandler;

		private UncaughtExceptionHandler mUncaughtExceptionHandler =
			new UncaughtExceptionHandler() {
				private static final String INTERNAL_ERROR = "Internal error! Please notify developer.";

				public void uncaughtException(Thread thread, Throwable ex) {
					if (ex instanceof OutOfMemoryError) {
						handleHere("Out of memory");
					} else if (ex instanceof NullPointerException) {
						PrintShow(INTERNAL_ERROR, Log.getStackTraceString(ex));
						handleHere("Null pointer exception");
					} else if (ex instanceof InvalidParameterException) {
						PrintShow(INTERNAL_ERROR, Log.getStackTraceString(ex));
						handleHere("Invalid parameter exception");
					} else {
						Log.e(LOGTAG, Log.getStackTraceString(ex));
						mDefaultExceptionHandler.uncaughtException(thread, ex);
					}
				}

				private void handleHere(String err) {
					PrintShow(err + ", near line:", ExecutingLineBuffer.text());
					SyntaxError = true;		// This blocks "Program ended" checks in finishRun()
					mOnErrorInt = null;		// Don't allow OnError to catch fatal exception
					disableInterrupt(Interrupt.ERROR_BIT);
					finishRun();
				}
			};

		@Override
		public void run() {

			InitVars();

//			Basic.Echo = Settings.getEcho(getApplicationContext());
			Echo = false;
			fnRTN = false;
			setVolumeControlStream(AudioManager.STREAM_MUSIC);

			mDefaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);

			boolean ok = PreScan();						// The execution starts by scanning the source for labels and read.data
			if (!ok) {
				sendMessage(MESSAGE_UPDATE_CONSOLE);	// PreScan found error or duplicate label
			} else {
				ExecutingLineIndex = 0;					// just in case PreScan ever changes it
				ok = RunLoop();							// run the program in the interpreter
			}

			finishRun();
			if (ok && mAutoRun != null) {				// program executed a RUN command
				Intent runIntent = mAutoRun.load();
				Run.this.startActivity(runIntent);		// start new Run
				Exit = true;							// and force this Run to finish
			}

			if (Exit) {
				finish();								// stop the Run Activity, too
			}
		}

		private void finishRun() {			// Called from run() when done running, and from UncaughtExceptionHandler

			Stop = true;		// If Stop is not already set, set it so that menu code can display the right thing
			PrintLine = "";		// Clear the Print Line buffer
			PrintLineReady = false;
			textPrintLine = "";

			disableInterrupt(Interrupt.BACK_KEY_BIT);

			// For debugging loop errors: if debug is on and errors are not trapped
			// and there is no other error and no immediate exit, then check the loop stacks.
			if (Debug && (mOnErrorInt == null) && !SyntaxError && !Exit) {
				if (!ForNextStack.empty())	{ PrintShow("Program ended with FOR without NEXT"); }
				if (!WhileStack.empty())	{ PrintShow("Program ended with WHILE without REPEAT"); }
				if (!DoStack.empty())		{ PrintShow("Program ended with DO without UNTIL"); }
			}
			if (mConsoleBuffer.size() != 0) {								// somebody changed the console
				sendMessage(MESSAGE_UPDATE_CONSOLE);
			}
			cleanUp();
		}

		// Scan the entire program. Find all the labels and read.data statements.
		// Must set ExecutingLineBuffer for use by called functions, but it will be reloaded when
		// RunLoop() starts. At present nobody downstream needs to have ExecutingLineIndex set.
		private boolean PreScan() {
			final String READ_DATA = BKW_READ_GROUP + BKW_READ_DATA;		// "read.data" command keyword
			for (int LineNumber = 0; LineNumber < Basic.lines.size(); ++LineNumber) {
				ExecutingLineBuffer = Basic.lines.get(LineNumber);			// scan one line at a time
				// ExecutingLineIndex = LineNumber;
				String text = ExecutingLineBuffer.text();

				int li = text.indexOf(":");									// fast check
				if ((li <= 0) && (text.charAt(0) != 'r'))	{ continue; }	// not label or READ.DATA, next line

				String word = getWord(text, 0, "");
				LineIndex = word.length();

				if (isNext(':')) {											// if word really is a label, store it
					if (Labels.put(word, LineNumber) != null) {				// if duplicate label
						Stop = true;										// non-recoverable error
						return RunTimeError("Duplicate label");
					}
					// Do not write CMD_LABEL into ExecutingLineBuffer.cmd() here.
					// If the program runs the label, let it get parsed once.
					if (!checkEOL())					{ return false; }
				}
				else if (text.startsWith(READ_DATA)) {						// Is not a label. If it is READ.DATA
					LineIndex = READ_DATA.length();							// set LineIndex just past READ.DATA
					ExecutingLineBuffer.cmd(CMD_READ_DATA, LineIndex);		// store the command reference
					if (!executeREAD_DATA())			{ return false; }	// parse and store the data list
					if (!checkEOL())					{ return false; }
				}
			}
			getInterruptLabels();
			return true;
		}

		private void getInterruptLabels() {								// check for interrupt labels
			mOnErrorInt =												// OnError: gets a named Interrupt object
			getInterruptLabel(BKW_ONERROR,			Interrupt.ERROR_BIT);
			getInterruptLabel(BKW_ONBACKKEY,		Interrupt.BACK_KEY_BIT);	// the rest can be anonymous
			getInterruptLabel(BKW_ONMENUKEY,		Interrupt.MENU_KEY_BIT);
			getInterruptLabel(BKW_ONTIMER,			Interrupt.TIMER_BIT);
			getInterruptLabel(BKW_ONKEYPRESS,		Interrupt.KEY_BIT);
			getInterruptLabel(BKW_ONGRTOUCH,		Interrupt.GR_TOUCH_BIT);
			getInterruptLabel(BKW_ONCONSOLETOUCH,	Interrupt.CONS_TOUCH_BIT);
			getInterruptLabel(BKW_ONBTREADREADY,	Interrupt.BT_READY_BIT);
			getInterruptLabel(BKW_ONBACKGROUND,		Interrupt.BACKGROUND_BIT);
			getInterruptLabel(BKW_ONKBCHANGE,		Interrupt.KB_CHANGE_BIT);
			getInterruptLabel(BKW_ONLOWMEM,			Interrupt.LOW_MEM_BIT);
		}

		private Interrupt getInterruptLabel(String label, Integer bit) {
			Interrupt intrpt = null;
			Integer line = Labels.get(chomp(label));					// remove colon from input string
			if (line != null) {											// if this interrupt's label exists
				intrpt = new Interrupt(line.intValue());
				mIntA.put(bit, intrpt);									// "arm" the interrupt
			}
			return intrpt;
		}

		// The RunLoop() drives the execution of the program. It is called from doInBackground and
		// recursively from doUserFunction.

		public boolean RunLoop() {
			boolean flag = true;
			while (ExecutingLineIndex < Basic.lines.size() && flag && !Stop) {	// keep executing statements until end
				ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);		// next program line
//				Log.d(LOGTAG, "RunLoop: " + ExecutingLineBuffer.line());
				LineIndex = 0;

				flag = StatementExecuter();							// execute the next statement
																	// returns true if no problems executing statement
				if (Exit) break;									// if Exit skip all other processing

				if (!flag) {										// error supersedes all other interrupt checking
					if (mOnErrorInt != null) {						// if there is an OnError label
						ExecutingLineIndex = mOnErrorInt.line();	// go to the OnError line
						SyntaxError = false;						// and clear the error status
						flag = true;
					}
				} else {
					readEventList();								// get events detected by other activities
					serviceInterrupt();								// sets ExecutingLineIndex if an interrupt triggered
				}

				if (mConsoleBuffer.size() != 0) {					// somebody changed the console
					sendMessage(MESSAGE_UPDATE_CONSOLE);
				}

				if (fnRTN) {										// fn_rtn signal
					fnRTN = false;									// make RunLoop() return to doUserFunction
					break;
				}

				// Debugger control
				// Michael/paulon0n also defined a signal for "Alert dialog var not set called". TODO?
				while (WaitForDebugResume && flag && !Stop) {
					mWaitForDebugLock = true;
					sendMessage(MESSAGE_DEBUG_DIALOG);				// signal UI to show debugger dialog
					waitForDebugLOCK();								// wait for user's selection

					if (DebuggerHalt) {
						PrintShow("Execution halted");
						Stop = true;
						break;
					}
					if (DebuggerStep) {
						DebuggerStep = false;
						break;										// let the next command run
					}
					if (dbSwap) {
						WaitForSwap = true;
						sendMessage(MESSAGE_DEBUG_SWAP);
						while (WaitForSwap) {
							Thread.yield();
							/*
							if(dbSelect) {
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
					}
				}

				++ExecutingLineIndex;								// step to next line
			}
			return flag;
		} // end RunLoop()

		private boolean StatementExecuter() {				// Execute one basic line (statement)
			Command c = ExecutingLineBuffer.findCommand(BASIC_cmd, LineIndex);
			if (c == null) {
				 c = CMD_IMPLICIT;							// no keyword, assume pseudo LET or CALL
				 ExecutingLineBuffer.cmd(c);				// and remember it
			} else {
				LineIndex += ExecutingLineBuffer.offset();	// skip keyword length
			}

			if (!IfElseStack.empty()) {						// if inside IF-ELSE-ENDIF
				Integer q = IfElseStack.peek();				// decide if we should skip to ELSE or ENDIF
				if ((q == IEskip1) && (c.id != CID_SKIP_TO_ELSE)
								   && (c.id != CID_SKIP_TO_ENDIF)) {
					return true;							// skip unless IF, ELSEIF, ELSE, or ENDIF
				} else if ((q == IEskip2) && (c.id != CID_SKIP_TO_ENDIF)) {
					return true;							// skip unless IF or ENDIF
				}
			}

			if (Echo) {
				String text = ExecutingLineBuffer.text();
				PrintShow(text.substring(0, text.length() - 1));
			}
			if (!c.run()) { SyntaxError(); return false; }
			return true;									// Statement executed ok. Return to main looper.
		} // StatementExecuter()

		private void readEventList() {
			synchronized (mEventList) {
				while (mEventList.size() != 0) {
					EventHolder e = mEventList.remove(0);
					if (e != null) {
						switch (e.mType) {
							case EventHolder.KEY_DOWN:			onKeyDown(e.mCode, e.mEvent);	break;
							case EventHolder.KEY_UP:			onKeyUp(e.mCode, e.mEvent);		break;
							case EventHolder.GR_BACK_KEY_PRESSED: GR_BackPressed();				break;
							case EventHolder.BACK_KEY_PRESSED:	onBackPressed();				break;
							case EventHolder.GR_KB_CHANGED:		triggerInterrupt(Interrupt.KB_CHANGE_BIT); break;
							case EventHolder.GR_TOUCH:			triggerInterrupt(Interrupt.GR_TOUCH_BIT); break;
							case EventHolder.GR_STATE:			grStateChange(e.mCode);			break;
							case EventHolder.WEB_STATE:			webStateChange(e.mCode);		break;
							case EventHolder.DATALINK_ADD:		addDataLink(e.mStringData);		break;
							default: break;
						}
					}
				}
			}
		} // readEventList()

		private boolean triggerInterrupt(Integer bit) {
			Interrupt intrpt = mIntA.get(bit);
			if (intrpt == null) return false;

			synchronized (mIntT) {
				mIntT.put(bit, intrpt);
			}
			return true;
		}

		private void serviceInterrupt() {
			if (interruptResume != -1) return;				// if servicing another interrupt, do not service another now
			if (mIntT.size() != 0) {						// if there are triggered interrupts
				Interrupt intrpt = null;					// service the oldest
				synchronized (mIntT) {
					Iterator<Integer> it = mIntT.keySet().iterator();
					while (it.hasNext()) {
						Integer key = it.next();
						if (key == null) continue;			// huh? should not happen
						intrpt = mIntT.remove(key);
						if (intrpt == null) continue;		// huh? should not happen
						break;
					}
				}
				if (intrpt != null) {
					interruptResume = ExecutingLineIndex;	// Set the resume Line Number
					ExecutingLineIndex = intrpt.line();		// Set the ISR line number
					IfElseStack.push(IEinterrupt);
				}
			}
		} // serviceInterrupt()

		private void disableInterrupt(Integer bit) {	// permanently disable an interrupt
			if (mIntA.remove(bit) != null) {			// remove from "armed" list
				mIntT.remove(bit);						// and from triggered list
			}
		}

		private boolean doResume(String errMsg) {
			if (interruptResume == -1) {
				return RunTimeError(errMsg);
			}

			ExecutingLineIndex = interruptResume;
			interruptResume = -1;
			// Pull the IEinterrupt from the If Else stack
			// It is possible that IFs were executed in the interrupt code
			// so pop entries until we get to the IEinterrupt
			while (IfElseStack.peek() != IEinterrupt) {
				IfElseStack.pop();
			}
			IfElseStack.pop();  // Top of stack is now IEInterrupt, pop it

			return true;
		}

		private void grStateChange(int newState) {
			switch (newState) {
				case EventHolder.ON_PAUSE:
					GRFront = false;
					break;
				case EventHolder.ON_RESUME:
					GRFront = true;
					break;
				default: return;
			}
			triggerInterrupt(Interrupt.BACKGROUND_BIT);
		}

		private void webStateChange(int newState) {
			switch (newState) {
				case EventHolder.ON_PAUSE:
					mWebFront = false;
					break;
				case EventHolder.ON_RESUME:
					mWebFront = true;
					break;
				default: return;
			}
			triggerInterrupt(Interrupt.BACKGROUND_BIT);
		}

	private void InitVars() {
		Log.d(LOGTAG, "InitVars() started");

		// Owned by Run class

		mInterpreterRunning = true;
		RunPaused = false;
		Stop = false;									// Stops program from running
		Exit = false;									// Exits program and signals caller to exit, too
		mEventList.clear();								// events from other Activities

		ConsoleLongTouch = false;
		TouchedConsoleLine = 0;							// first valid line number is 1
		interruptResume = -1;

		SyntaxError = false;							// Set true when Syntax Error message has been output

		mMessagePending = false;						// If true, may be messages pending

		// debugger dialog and ui vars
		WatchedVars = new ArrayList<Var>();				// list of watched variables
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
		WatchedArray = null;
		WatchedList = -1;
		WatchedStack = -1;
		WatchedBundle = -1;
		dbSwap = false;
		dbDialog = null;
		dbSwapDialog = null;
		dbSelectDialog = null;
		// end debugger ui vars

		Labels = new HashMap<String, Integer>();		// A list of all labels and associated line numbers

		InChar = new ArrayList<String>();
		TextInputString = "";

		GRopen = false;									// Graphics Open Flag
		GRFront = false;
		DisplayList = new ArrayList<GR.BDraw>();
		RealDisplayList = new ArrayList<Integer>();
		PaintList = new ArrayList<Paint>();
		BitmapList = new ArrayList<Bitmap>();
		initTouchVars();

		theSensors = null;
		theGPS = null;

		clientSocketState = STATE_NONE;
		serverSocketState = STATE_NONE;

		theClientSocket = null ;
		clientSocketConnectThread = null;
		ClientBufferedReader = null;
		ClientPrintWriter = null;

		newSS = null;
		serverSocketConnectThread = null;
		theServerSocket = null ;
		ServerBufferedReader = null ;
		ServerPrintWriter = null ;

		mFTPClient = null;
		FTPdir = null;

		CameraBitmap = null;
		CameraDone = true;
		NumberOfCameras = -1;

		bt_enabled = 0;
		mConnectedDeviceName = null;
		mOutStringBuffer = null;
		mChatService = null;
		BT_Read_Buffer.clear();

		headsetState = -1;
		headsetName = "NA" ;
		headsetMic = -1;

		htmlIntent = null;

		sttDefaultPrompt = getString(R.string.stt_prompt);
		sttPrompt = null;
		sttListening = false;

		// Owned by Interpreter class
		// allocate objects

		ExecutingLineBuffer = new ProgramLine("\n");		// Holds the current line being executed

		GosubStack = new Stack<Integer>();					// Stack used for Gosub/Return
		ForNextStack = new Stack<ForNext>();				// Stack used for For/Next
		WhileStack = new Stack<WhileRepeat>();				// Stack used for While/Repeat
		DoStack = new Stack<Integer>();						// Stack used for Do/Until

		IfElseStack = new Stack <Integer>();				// Stack for IF-ELSE-ENDIF operations
		FunctionStack = new Stack<CallStackFrame>() ;		// State saved through the currently executing functions
		mFunctionTable = new HashMap<String, Var.FnDef>();	// Globally-accessible table of user-defined functions

		mGlobalSymbolTable = new Var.Table();				// The symbol table that contains all global Vars
		mSymbolTable = new Var.Table();						// The current symbol table, all non-global Vars in scope
		mSymbolTables = new Stack<Var.Table>();				// List of all local symbol tables
		mSymbolTables.push(mSymbolTable);

		// Initialize the parser's working Var objects.
		// These are used to avoid creating a new Var every time a variable is parsed.
		mScalarVar = new Var.ScalarVar("", Var.Type.NOVAR);
		mArrayVar = new Var.ArrayVar("", Var.Type.NOVAR);
		mFunctionVar = new Var.FunctionVar("", Var.Type.NOVAR);

		mErrorMsg = NO_ERROR;

		theLists = new ArrayList <ArrayList>();
		theLists.add(new ArrayList <ArrayList>());

		theListsType = new ArrayList<Var.Type>();
		theListsType.add(Var.Type.NOVAR);

		theBundles = new ArrayList<Bundle>();
		theBundles.add(new Bundle());

		theStacks = new ArrayList<Stack>();
		theStacks.add(new Stack());

		theStacksType = new ArrayList<Var.Type>();
		theStacksType.add(Var.Type.NOVAR);

		readData = new ArrayList<Var.Val>();

		FileTable = new ArrayList<FileInfo>() ;				// File table list

		DataBases = new ArrayList<SQLiteDatabase>();		// List of created data bases
		Cursors = new ArrayList<Cursor>();					// List of database cursors

		FontList = new ArrayList<Typeface>();
		clearFontList();

		theMPList = new ArrayList<MediaPlayer>();
		theMPNameList = new ArrayList<String>();
		theMPList.add(null);								// We don't use the [0] element of these Lists
		theMPNameList.add(null);

		Log.d(LOGTAG, "InitVars() done");
	} // end InitVars

	public void cleanUp() {
		Log.d(LOGTAG, "cleanUp() started");
		mEventList.clear();								// events from other Activities
		mIntT.clear();									// clear all pending interrupts
		mIntA.clear();									// disable all interrupts

		if ((lv != null) && (lv.mKB != null)) {
			lv.mKB.release();
			lv.mKB = null;
		}
		if ((GR.drawView != null) && (GR.drawView.mKB != null)) {
			GR.drawView.mKB.release();
			GR.drawView.mKB = null;
		}

		if (theMP != null) {
			try { theMP.stop(); } catch (IllegalStateException e) {}
			theMP.release();
			theMP = null;
		}

		if (theSoundPool != null) {
			theSoundPool.release();
			theSoundPool = null;
		}

		if (Web.aWebView != null) { Web.aWebView.webClose(); }
		if (htmlData_Buffer != null) { htmlData_Buffer.clear(); }

		cancelTimer();
		ttsStop();
		cancelVibrator();

		((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();

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

		if (theServerSocket != null) {
			try { theServerSocket.close(); }
			catch (Exception e) {}
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

		if (newSS != null) {
			try { newSS.close(); }
			catch (Exception e) {}
			newSS = null;
		}

		if (theClientSocket != null) {
			try{
				theClientSocket.close();
				}catch (Exception e) {
				}
				theClientSocket = null;
			}

		clientSocketState = STATE_NONE;
		serverSocketState = STATE_NONE;

		audioRecordStop();

		Stop = true;								// make sure the interpreter thread stops
		Basic.clearContextManager();
		mMessagePending = false;

		if (theGPS != null) {
			Log.d(LOGTAG, "Stopping GPS from cleanUp");
			theGPS.stop();
			theGPS = null;
		}

		if (!SyntaxError || Basic.DoAutoRun) {		// if no error or no Editor
			Editor.SyntaxErrorDisplacement = -1;	// clear error highlighting, if any
		}

		BitmapListClear();

		if (mChatService != null) {
				mChatService.stop();
				mChatService = null;
			}

		if (theSUReader != null) {
			theSUReader.stop();
			theSUReader = null;
		}
		if (SUprocess != null) {
			SUprocess.destroy();
			SUprocess = null;
		}

		finishActivity(BASIC_GENERAL_INTENT);

		if (mTM != null) {
			Log.d(LOGTAG, "mTM: unlistening");
			mTM.listen(PSL, PhoneStateListener.LISTEN_NONE);
			mTM = null;
		}
		mSignalStrength = null;

		mInterpreterRunning = false;

		Log.d(LOGTAG, "cleanUp() done");
	} // end cleanup

	// ***************************************** UI Locks *****************************************

	private void waitForLOCK() {
		synchronized (LOCK) {
//			Log.d(LOGTAG, "set LOCK wait");
			while (mWaitForLock) {
				try { LOCK.wait(); }
				catch (InterruptedException e) { mWaitForLock = false; }
			}
		}
	}

	private void waitForGrLOCK() {
		synchronized (GR.LOCK) {
//			Log.d(LOGTAG, "set GR.LOCK wait");
			while (GR.waitForLock) {
				try { GR.LOCK.wait(); }
				catch (InterruptedException e) { GR.waitForLock = false; }
			}
		}
	}

	private void waitForDebugLOCK() {
		synchronized (DB_LOCK) {
//			Log.d(LOGTAG, "set DB_LOCK wait");
			while (mWaitForDebugLock) {
				try { DB_LOCK.wait(); }
				catch (InterruptedException e) { mWaitForDebugLock = false; }
			}
		}
	}

	// ************************************* Function Tables **************************************

	private final Command[] MF_cmd = new Command[] {	// Map math function names to their functions
		new Command(MF_ABS)                     { public boolean run() { return executeMF_ABS(); } },
		new Command(MF_ACOS)                    { public boolean run() { return executeMF_ACOS(); } },
		new Command(MF_ASCII)                   { public boolean run() { return executeMF_ASCII(); } },
		new Command(MF_ASIN)                    { public boolean run() { return executeMF_ASIN(); } },
		new Command(MF_ATAN)                    { public boolean run() { return executeMF_ATAN(); } },
		new Command(MF_ATAN2)                   { public boolean run() { return executeMF_ATAN2(); } },
		new Command(MF_BACKGROUND)              { public boolean run() { return executeMF_BACKGROUND(); } },
		new Command(MF_BAND)                    { public boolean run() { return executeMF_BAND(); } },
		new Command(MF_BIN)                     { public boolean run() { return executeMF_base(2); } },
		new Command(MF_BNOT)                    { public boolean run() { return executeMF_BNOT(); } },
		new Command(MF_BOR)                     { public boolean run() { return executeMF_BOR(); } },
		new Command(MF_BXOR)                    { public boolean run() { return executeMF_BXOR(); } },
		new Command(MF_CBRT)                    { public boolean run() { return executeMF_CBRT(); } },
		new Command(MF_CEIL)                    { public boolean run() { return executeMF_CEIL(); } },
		new Command(MF_CLOCK)                   { public boolean run() { return executeMF_CLOCK(); } },
		new Command(MF_COS)                     { public boolean run() { return executeMF_COS(); } },
		new Command(MF_COSH)                    { public boolean run() { return executeMF_COSH(); } },
		new Command(MF_ENDS_WITH)               { public boolean run() { return executeMF_ENDS_WITH(); } },
		new Command(MF_EXP)                     { public boolean run() { return executeMF_EXP(); } },
		new Command(MF_FLOOR)                   { public boolean run() { return executeMF_FLOOR(); } },
		new Command(MF_FRAC)                    { public boolean run() { return executeMF_FRAC(); } },
		new Command(MF_GR_COLLISION)            { public boolean run() { return executeMF_GR_COLLISION(); } },
		new Command(MF_HEX)                     { public boolean run() { return executeMF_base(16); } },
		new Command(MF_HYPOT)                   { public boolean run() { return executeMF_HYPOT(); } },
		new Command(MF_INT)                     { public boolean run() { return executeMF_INT(); } },
		new Command(MF_IS_IN)                   { public boolean run() { return executeMF_IS_IN(); } },
		new Command(MF_IS_NUMBER)               { public boolean run() { return executeMF_IS_NUMBER(); } },
		new Command(MF_LEN)                     { public boolean run() { return executeMF_LEN(); } },
		new Command(MF_LOG)                     { public boolean run() { return executeMF_LOG(); } },
		new Command(MF_LOG10)                   { public boolean run() { return executeMF_LOG10(); } },
		new Command(MF_MAX)                     { public boolean run() { return executeMF_MAX(); } },
		new Command(MF_MIN)                     { public boolean run() { return executeMF_MIN(); } },
		new Command(MF_MOD)                     { public boolean run() { return executeMF_MOD(); } },
		new Command(MF_OCT)                     { public boolean run() { return executeMF_base(8); } },
		new Command(MF_PI)                      { public boolean run() { return executeMF_PI(); } },
		new Command(MF_POW)                     { public boolean run() { return executeMF_POW(); } },
		new Command(MF_RANDOMIZE)               { public boolean run() { return executeMF_RANDOMIZE(); } },
		new Command(MF_RND)                     { public boolean run() { return executeMF_RND(); } },
		new Command(MF_ROUND)                   { public boolean run() { return executeMF_ROUND(); } },
		new Command(MF_SGN)                     { public boolean run() { return executeMF_SGN(); } },
		new Command(MF_SHIFT)                   { public boolean run() { return executeMF_SHIFT(); } },
		new Command(MF_SIN)                     { public boolean run() { return executeMF_SIN(); } },
		new Command(MF_SINH)                    { public boolean run() { return executeMF_SINH(); } },
		new Command(MF_SQR)                     { public boolean run() { return executeMF_SQR(); } },
		new Command(MF_STARTS_WITH)             { public boolean run() { return executeMF_STARTS_WITH(); } },
		new Command(MF_TAN)                     { public boolean run() { return executeMF_TAN(); } },
		new Command(MF_TIME)                    { public boolean run() { return executeMF_TIME(); } },
		new Command(MF_TODEGREES)               { public boolean run() { return executeMF_TODEGREES(); } },
		new Command(MF_TORADIANS)               { public boolean run() { return executeMF_TORADIANS(); } },
		new Command(MF_UCODE)                   { public boolean run() { return executeMF_UCODE(); } },
		new Command(MF_VAL)                     { public boolean run() { return executeMF_VAL(); } },
	};

	private final HashMap<String, Command> MF_map = new HashMap<String, Command>(64) {
		private static final long serialVersionUID = 102L;
		{
			for (Command c : MF_cmd) { put(c.name, c); }
		}};

	private final Command[] SF_cmd = new Command[] {	// Map string function names to their functions
		new Command(SF_BIN)                     { public boolean run() { return executeSF_BIN(); } },
		new Command(SF_CHR)                     { public boolean run() { return executeSF_CHR(); } },
		new Command(SF_DECODE)                  { public boolean run() { return executeSF_ENCODE(DECODE); } },
		new Command(SF_ENCODE)                  { public boolean run() { return executeSF_ENCODE(ENCODE); } },
		new Command(SF_FORMAT)                  { public boolean run() { return executeSF_FORMAT(); } },
		new Command(SF_FORMAT_USING)            { public boolean run() { return executeSF_USING(); } },
		new Command(SF_GETERROR)                { public boolean run() { return executeSF_GETERROR(); } },
		new Command(SF_HEX)                     { public boolean run() { return executeSF_HEX(); } },
		new Command(SF_INT)                     { public boolean run() { return executeSF_INT(); } },
		new Command(SF_LEFT)                    { public boolean run() { return executeSF_LEFT(); } },
		new Command(SF_LOWER)                   { public boolean run() { return executeSF_LOWER(); } },
		new Command(SF_LTRIM)                   { public boolean run() { return executeSF_TRIM(TLEFT); } },
		new Command(SF_MID)                     { public boolean run() { return executeSF_MID(); } },
		new Command(SF_OCT)                     { public boolean run() { return executeSF_OCT(); } },
		new Command(SF_REPLACE)                 { public boolean run() { return executeSF_REPLACE(); } },
		new Command(SF_RIGHT)                   { public boolean run() { return executeSF_RIGHT(); } },
		new Command(SF_RTRIM)                   { public boolean run() { return executeSF_TRIM(TRIGHT); } },
		new Command(SF_STR)                     { public boolean run() { return executeSF_STR(); } },
		new Command(SF_TRIM)                    { public boolean run() { return executeSF_TRIM(TLEFT | TRIGHT); } },
		new Command(SF_UPPER)                   { public boolean run() { return executeSF_UPPER(); } },
		new Command(SF_USING)                   { public boolean run() { return executeSF_USING(); } },
		new Command(SF_VERSION)                 { public boolean run() { return executeSF_VERSION(); } },
		new Command(SF_WORD)                    { public boolean run() { return executeSF_WORD(); } },
	};

	private final HashMap<String, Command> SF_map = new HashMap<String, Command>(64) {
		private static final long serialVersionUID = 103L;
		{
			for (Command c : SF_cmd) { put(c.name, c); }
		}};

	// ************************************** Command Tables **************************************

	/* Markers for IF, etc., to facilitate skipping them in StatementExecuter() */
	private final int CID_SKIP_TO_ELSE  = 1;	// Ok to execute when skipping to ELSE or ENDIF
	private final int CID_SKIP_TO_ENDIF = 2;	// Ok to execute when skipping to ENDIF
	/* Other markers to make special-case handling faster */
	private final int CID_GROUP = 3;
	private final int CID_OPEN = 4;
	private final int CID_CLOSE = 5;
	private final int CID_EX = 6;				// EXception within its command group
	private final int CID_READ = 7;
	private final int CID_WRITE = 8;
	private final int CID_STATUS = 9;
	private final int CID_DATALINK = 10;

	/* Special case: what to do if no command keyword at the beginning of the line. */
	private final Command CMD_IMPLICIT = new Command("")         { public boolean run() { return executeImplicitCommand(); } };
	private final Command CMD_IMPL_LET = new Command("")         { public boolean run() { return executeLET(0.0); } };
	/* Label: length is variable, but irrelevant. No command name, so DO NOT put this in a searchable command table. */
	private final Command CMD_LABEL    = new Command("")         { public boolean run() { return true; } };
	/* Other special cases where we need a named Command and also a command table entry. */
	private final Command CMD_CALL     = new Command(BKW_CALL)   { public boolean run() { return executeCALL(); } };
	private final Command CMD_LET      = new Command(BKW_LET)    { public boolean run() { return executeLET(); } };
	private final Command CMD_PREINC   = new Command(BKW_PREINC) { public boolean run() { return executeLET(1.0); } };
	private final Command CMD_PREDEC   = new Command(BKW_PREDEC) { public boolean run() { return executeLET(-1.0); } };
	private final Command CMD_FOR      = new Command(BKW_FOR)    { public boolean run() { return executeFOR(); } };
	private final Command CMD_NEXT     = new Command(BKW_NEXT)   { public boolean run() { return executeNEXT(); } };
	private final Command CMD_WHILE    = new Command(BKW_WHILE)  { public boolean run() { return executeWHILE(); } };
	private final Command CMD_REPEAT   = new Command(BKW_REPEAT) { public boolean run() { return executeREPEAT(); } };
	private final Command CMD_DO       = new Command(BKW_DO)     { public boolean run() { return executeDO(); } };
	private final Command CMD_UNTIL    = new Command(BKW_UNTIL)  { public boolean run() { return executeUNTIL(); } };
	private final Command CMD_SW_GROUP = new Command(BKW_SW_GROUP, CID_GROUP) { public boolean run() { return executeSW(); } };

	// Map BASIC! command keywords to their execution functions.
	// The order of this list determines the order of the linear keyword search, which affects performance.
	private final Command[] BASIC_cmd = new Command[] {
		CMD_LET,
		new Command(BKW_IF,     CID_SKIP_TO_ENDIF) { public boolean run() { return executeIF(); } },
		new Command(BKW_ENDIF,  CID_SKIP_TO_ENDIF) { public boolean run() { return executeENDIF(); } },
		new Command(BKW_ELSEIF, CID_SKIP_TO_ELSE)  { public boolean run() { return executeELSEIF(); } },
		new Command(BKW_ELSE,   CID_SKIP_TO_ELSE)  { public boolean run() { return executeELSE(); } },
		new Command(BKW_PRINT)                  { public boolean run() { return executePRINT(); } },
		new Command(BKW_PRINT_SHORTCUT)         { public boolean run() { return executePRINT(); } },
		CMD_FOR,
		CMD_NEXT,
		CMD_WHILE,
		CMD_REPEAT,
		CMD_DO,
		CMD_UNTIL,
		new Command(BKW_F_N_BREAK)              { public boolean run() { return executeF_N_BREAK(); } },
		new Command(BKW_W_R_BREAK)              { public boolean run() { return executeW_R_BREAK(); } },
		new Command(BKW_D_U_BREAK)              { public boolean run() { return executeD_U_BREAK(); } },
		new Command(BKW_F_N_CONTINUE)           { public boolean run() { return executeF_N_CONTINUE(); } },
		new Command(BKW_W_R_CONTINUE)           { public boolean run() { return executeW_R_CONTINUE(); } },
		new Command(BKW_D_U_CONTINUE)           { public boolean run() { return executeD_U_CONTINUE(); } },
		CMD_SW_GROUP,
		new Command(BKW_FN_GROUP, CID_GROUP)    { public boolean run() { return executeFN(); } },
		CMD_CALL,
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
		CMD_PREINC,
		CMD_PREDEC,
		new Command(BKW_INKEY)                  { public boolean run() { return executeINKEY(); } },
		new Command(BKW_INPUT)                  { public boolean run() { return executeINPUT(); } },
		new Command(BKW_DIALOG_GROUP,CID_GROUP) { public boolean run() { return executeDIALOG(); } },
		new Command(BKW_SELECT)                 { public boolean run() { return executeSELECT(); } },
		new Command(BKW_TGET)                   { public boolean run() { return executeTGET(); } },
		new Command(BKW_FILE_GROUP, CID_GROUP)  { public boolean run() { return executeFILE(); } },
		new Command(BKW_TEXT_GROUP, CID_GROUP)  { public boolean run() { return executeTEXT(); } },
		new Command(BKW_BYTE_GROUP, CID_GROUP)  { public boolean run() { return executeBYTE(); } },
		new Command(BKW_ZIP_GROUP, CID_GROUP)   { public boolean run() { return executeZIP(); } },
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
		new Command(BKW_ENCRYPT)                { public boolean run() { return executeENCRYPT(ENCODE); } },
		new Command(BKW_DECRYPT)                { public boolean run() { return executeENCRYPT(DECODE); } },
		new Command(BKW_SWAP)                   { public boolean run() { return executeSWAP(); } },
		new Command(BKW_SPLIT_ALL)              { public boolean run() { return executeSPLIT(-1); } },
		new Command(BKW_SPLIT)                  { public boolean run() { return executeSPLIT(0); } },
		new Command(BKW_JOIN_ALL)               { public boolean run() { return executeJOIN(true); } },
		new Command(BKW_JOIN)                   { public boolean run() { return executeJOIN(false); } },
		new Command(BKW_CLS)                    { public boolean run() { return executeCLS(); } },
		new Command(BKW_FONT_GROUP, CID_GROUP)  { public boolean run() { return executeFONT(); } },
		new Command(BKW_CONSOLE_GROUP,CID_GROUP){ public boolean run() { return executeCONSOLE(); } },
		new Command(BKW_DEBUG_GROUP, CID_GROUP) { public boolean run() { return executeDEBUG(); } },
		new Command(BKW_ECHO_GROUP, CID_GROUP)  { public boolean run() { return executeECHO(); } },
		new Command(BKW_KB_GROUP,  CID_GROUP)   { public boolean run() { return executeKB(); } },
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
		new Command(BKW_DEVICE)                 { public boolean run() { return executeDEVICE(); } },
		new Command(BKW_SCREEN_GROUP,CID_GROUP) { public boolean run() { return executeSCREEN(); } },
		new Command(BKW_WIFI_INFO)              { public boolean run() { return executeWIFI_INFO(); } },
		new Command(BKW_HEADSET)                { public boolean run() { return executeHEADSET(); } },
		new Command(BKW_MYPHONENUMBER)          { public boolean run() { return executeMYPHONENUMBER(); } },
		new Command(BKW_EMAIL_SEND)             { public boolean run() { return executeEMAIL_SEND(); } },
		new Command(BKW_PHONE_GROUP, CID_GROUP) { public boolean run() { return executePHONE(); } },
		new Command(BKW_SMS_GROUP, CID_GROUP)   { public boolean run() { return executeSMS(); } },
		new Command(BKW_VOLKEYS_GROUP,CID_GROUP){ public boolean run() { return executeVOLKEYS(); } },
		new Command(BKW_APP_GROUP,CID_GROUP)    { public boolean run() { return executeAPP(); } },

		new Command(BKW_BACK_RESUME)            { public boolean run() { return executeBACK_RESUME(); } },
		new Command(BKW_BACKGROUND_RESUME)      { public boolean run() { return executeBACKGROUND_RESUME(); } },
		new Command(BKW_CONSOLETOUCH_RESUME)    { public boolean run() { return executeCONSOLETOUCH_RESUME(); } },
		new Command(BKW_KEY_RESUME)             { public boolean run() { return executeKEY_RESUME(); } },
		new Command(BKW_LOWMEM_RESUME)          { public boolean run() { return executeLOWMEM_RESUME(); } },
		new Command(BKW_MENUKEY_RESUME)         { public boolean run() { return executeMENUKEY_RESUME(); } }
	}; // BASIC_cmd

	// **************** FN Group - user-defined functions

	private final Command[] fn_cmd = new Command[] {	// Map user function command keywords to their execution functions
		new Command(BKW_FN_DEF)         { public boolean run() { return executeFN_DEF(); } },
		new Command(BKW_FN_RTN)         { public boolean run() { return executeFN_RTN(); } },
		new Command(BKW_END)            { public boolean run() { return executeFN_END(); } },
	};

	// **************** SW Group - switch statements

	private final Command CMD_SW_BEGIN   = new Command(BKW_SW_BEGIN)   { public boolean run() { return executeSW_BEGIN(); } };
	private final Command CMD_SW_CASE    = new Command(BKW_SW_CASE)    { public boolean run() { return executeSW_CASE(); } };
	private final Command CMD_SW_BREAK   = new Command(BKW_SW_BREAK)   { public boolean run() { return executeSW_BREAK(); } };
	private final Command CMD_SW_DEFAULT = new Command(BKW_SW_DEFAULT) { public boolean run() { return executeSW_DEFAULT(); } };
	private final Command CMD_SW_END     = new Command(BKW_END)        { public boolean run() { return executeSW_END(); } };

	private final Command[] sw_cmd = new Command[] {	// Map sw (switch) command keywords to their execution functions
		CMD_SW_BEGIN,
		CMD_SW_CASE,
		CMD_SW_BREAK,
		CMD_SW_DEFAULT,
		CMD_SW_END
	};

	// **************** FILE Group

	private final Command[] file_cmd = new Command[] {	// Map File command keywords to their execution functions
		new Command(BKW_FILE_DELETE)    { public boolean run() { return executeDELETE(); } },
		new Command(BKW_FILE_DIR)       { public boolean run() { return executeDIR(); } },
		new Command(BKW_FILE_EXISTS)    { public boolean run() { return executeFILE_EXISTS(); } },
		new Command(BKW_FILE_MKDIR)     { public boolean run() { return executeMKDIR(); } },
		new Command(BKW_FILE_RENAME)    { public boolean run() { return executeRENAME(); } },
		new Command(BKW_FILE_ROOT)      { public boolean run() { return executeFILE_ROOT(); } },
		new Command(BKW_FILE_SIZE)      { public boolean run() { return executeFILE_SIZE(); } },
		new Command(BKW_FILE_TYPE)      { public boolean run() { return executeFILE_TYPE(); } }
	};

	// **************** TEXT Group - text file operations

	private final Command[] text_cmd = new Command[] {	// Map Text I/O command keywords to their execution functions
		new Command(BKW_OPEN, CID_EX)           { public boolean run()        { return executeTEXT_OPEN(); } },
		new Command(BKW_CLOSE, CID_EX)          { public boolean run()        { return executeCLOSE(FileType.FILE_TEXT); } },
		new Command(BKW_TEXT_READLN, CID_READ)  { public boolean run(int fId) { return executeTEXT_READLN(fId); } },
		new Command(BKW_TEXT_WRITELN,CID_WRITE) { public boolean run(int fId) { return executeTEXT_WRITELN(fId); } },
		new Command(BKW_EOF, CID_WRITE)         { public boolean run(int fId) { return executeEOF(fId, FileType.FILE_TEXT); } },
		new Command(BKW_TEXT_INPUT, CID_EX)     { public boolean run()        { return executeTEXT_INPUT(); } },
		new Command(BKW_POSITION_GET, CID_READ) { public boolean run(int fId) { return executePOSITION_GET(fId, FileType.FILE_TEXT); } },
		new Command(BKW_POSITION_SET, CID_READ) { public boolean run(int fId) { return executeTEXT_POSITION_SET(fId); } },
		new Command(BKW_POSITION_MARK, CID_EX)  { public boolean run()        { return executePOSITION_MARK(FileType.FILE_TEXT); } },
	};

	// **************** BYTE Group - binary file operations

	private final Command[] byte_cmd = new Command[] {	// Map Byte I/O command keywords to their execution functions
		new Command(BKW_OPEN, CID_EX)               { public boolean run()        { return executeBYTE_OPEN(); } },
		new Command(BKW_CLOSE, CID_EX)              { public boolean run()        { return executeCLOSE(FileType.FILE_BYTE); } },
		new Command(BKW_BYTE_READ_BYTE, CID_READ)   { public boolean run(int fId) { return executeBYTE_READ_BYTE(fId); } },
		new Command(BKW_BYTE_WRITE_BYTE,CID_WRITE)  { public boolean run(int fId) { return executeBYTE_WRITE_BYTE(fId); } },
		new Command(BKW_BYTE_READ_BUFFER, CID_READ) { public boolean run(int fId) { return executeBYTE_READ_BUFFER(fId); } },
		new Command(BKW_BYTE_WRITE_BUFFER,CID_WRITE){ public boolean run(int fId) { return executeBYTE_WRITE_BUFFER(fId); } },
		new Command(BKW_BYTE_READ_NUMBER, CID_READ) { public boolean run(int fId) { return executeBYTE_READ_NUMBER(fId); } },
		new Command(BKW_BYTE_WRITE_NUMBER,CID_WRITE){ public boolean run(int fId) { return executeBYTE_WRITE_NUMBER(fId); } },
		new Command(BKW_EOF, CID_WRITE)             { public boolean run(int fId) { return executeEOF(fId, FileType.FILE_BYTE); } },
		new Command(BKW_BYTE_COPY, CID_READ)        { public boolean run(int fId) { return executeBYTE_COPY(fId); } },
		new Command(BKW_BYTE_TRUNCATE, CID_WRITE)   { public boolean run(int fId) { return executeBYTE_TRUNCATE(fId); } },
		new Command(BKW_POSITION_GET, CID_READ)     { public boolean run(int fId) { return executePOSITION_GET(fId, FileType.FILE_BYTE); } },
		new Command(BKW_POSITION_SET, CID_READ)     { public boolean run(int fId) { return executeBYTE_POSITION_SET(fId); } },
		new Command(BKW_POSITION_MARK, CID_EX)      { public boolean run()        { return executePOSITION_MARK(FileType.FILE_BYTE); } },
	};

	// **************** ZIP Group - zip file operations

	private final Command[] zip_cmd = new Command[] {	// Map Text I/O command keywords to their execution functions
		new Command(BKW_OPEN, CID_EX)           { public boolean run()        { return executeZIP_OPEN(); } },
		new Command(BKW_CLOSE, CID_EX)          { public boolean run()        { return executeCLOSE(FileType.FILE_ZIP); } },
		new Command(BKW_DIR, CID_EX)            { public boolean run()        { return executeZIP_DIR(); } },
		new Command(BKW_ZIP_READ, CID_READ)     { public boolean run(int fId) { return executeZIP_READ(fId); } },
		new Command(BKW_ZIP_WRITE, CID_WRITE)   { public boolean run(int fId) { return executeZIP_WRITE(fId); } },
	};

	// **************** READ Group - READ.DATA

	private final Command CMD_READ_DATA = new Command(BKW_READ_DATA) { public boolean run() { return true; } };
	private final Command[] read_cmd = new Command[] {	// Map Read command keywords to their execution functions
										// Do NOT call executeREAD_DATA, that was done in PreScan
		CMD_READ_DATA,
		new Command(BKW_READ_NEXT)              { public boolean run() { return executeREAD_NEXT(); } },
		new Command(BKW_READ_FROM)              { public boolean run() { return executeREAD_FROM(); } },
	};

	// **************** SCREEN Group

	private final Command[] screen_cmd = new Command[] {// Map screen command keywords to their execution functions
		new Command(BKW_SCREEN_ROTATION)        { public boolean run() { return executeSCREEN_ROTATION(); } },
		new Command(BKW_SCREEN_SIZE)            { public boolean run() { return executeSCREEN_SIZE(); } },
	};

	// **************** FONT Group

	private final Command[] font_cmd = new Command[] {	// Map font command keywords to their execution functions
			new Command(BKW_FONT_LOAD)          { public boolean run() { return executeFONT_LOAD(); } },
			new Command(BKW_FONT_DELETE)        { public boolean run() { return executeFONT_DELETE(); } },
			new Command(BKW_FONT_CLEAR)         { public boolean run() { return executeFONT_CLEAR(); } },
	};

	// **************** CONSOLE Group

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

	// **************** DEVICE Group

	private final Command[] Device_cmd = new Command[] {	// Map device command keywords to their execution functions
		new Command(BKW_DEVICE_LANGUAGE)        { public boolean run() { return executeDEVICE(Locale.getDefault().getDisplayLanguage()); } },
		new Command(BKW_DEVICE_LOCALE)          { public boolean run() { return executeDEVICE(Locale.getDefault().toString()); } },
	};

	// **************** DIALOG Group

	private final Command[] Dialog_cmd = new Command[] {	// Map dialog command keywords to their execution functions
		new Command(BKW_DIALOG_MESSAGE)         { public boolean run() { return executeDIALOG_MESSAGE(); } },
		new Command(BKW_DIALOG_SELECT)          { public boolean run() { return executeDIALOG_SELECT(); } },
	};

	// **************** KB Group

	private final Command[] KB_cmd = new Command[] {	// Map KB command keywords to their execution functions
		new Command(BKW_KB_HIDE)                { public boolean run() { return executeKB_HIDE(); } },
		new Command(BKW_KB_RESUME)              { public boolean run() { return executeKB_RESUME(); } },
		new Command(BKW_KB_SHOWING)             { public boolean run() { return executeKB_SHOWING(); } },
		new Command(BKW_KB_SHOW)                { public boolean run() { return executeKB_SHOW(); } },
		new Command(BKW_KB_TOGGLE)              { public boolean run() { return executeKB_TOGGLE(); } },
	};

	// **************** SQL Group - SQLite database operations

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

	// **************** GR Group - graphics mode commands

	private final Command[] GR_cmd = new Command[] {	// Map GR command keywords to their execution functions
		new Command(BKW_GR_RENDER)                  { public boolean run() { return execute_gr_render(); } },
		new Command(BKW_GR_MODIFY)                  { public boolean run() { return execute_gr_modify(); } },
		new Command(BKW_GR_MOVE)                    { public boolean run() { return execute_gr_move(); } },
		new Command(BKW_GR_BOUNDED_TOUCH2)          { public boolean run() { return execute_gr_bound_touch(1); } },
		new Command(BKW_GR_BOUNDED_TOUCH)           { public boolean run() { return execute_gr_bound_touch(0); } },
		new Command(BKW_GR_TOUCH2)                  { public boolean run() { return execute_gr_touch(1); } },
		new Command(BKW_GR_TOUCH)                   { public boolean run() { return execute_gr_touch(0); } },

		new Command(BKW_GR_BITMAP_GROUP, CID_GROUP) { public boolean run() { return executeGR_BITMAP(); } },
		new Command(BKW_GR_CAMERA_GROUP, CID_GROUP) { public boolean run() { return executeGR_CAMERA(); } },
		new Command(BKW_GR_GET_GROUP, CID_GROUP)    { public boolean run() { return executeGR_GET(); } },
		new Command(BKW_GR_GROUP_GROUP, CID_GROUP)  { public boolean run() { return executeGR_GROUP(); } },
		new Command(BKW_GR_PAINT_GROUP, CID_GROUP)  { public boolean run() { return executeGR_PAINT(); } },
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
		new Command(BKW_GR_GROUP_CMD)               { public boolean run() { return execute_gr_group_objs(); } },
		new Command(BKW_GR_HIDE)                    { public boolean run() { return execute_gr_show(GR.VISIBLE.HIDE); } },
		new Command(BKW_GR_LINE)                    { public boolean run() { return execute_gr_line(); } },
		new Command(BKW_GR_ONGRTOUCH_RESUME)        { public boolean run() { return execute_gr_touch_resume(); } },
		new Command(BKW_GR_OPEN, CID_OPEN)          { public boolean run() { return execute_gr_open(); } },
		new Command(BKW_GR_ORIENTATION)             { public boolean run() { return execute_gr_orientation(); } },
		new Command(BKW_GR_OVAL)                    { public boolean run() { return execute_gr_oval(); } },
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
		new Command(BKW_GR_SHOW_TOGGLE)             { public boolean run() { return execute_gr_show(GR.VISIBLE.TOGGLE); } },
		new Command(BKW_GR_SHOW)                    { public boolean run() { return execute_gr_show(GR.VISIBLE.SHOW); } },
		new Command(BKW_GR_STATUSBAR_SHOW)          { public boolean run() { return execute_statusbar_show(); } },
		new Command(BKW_GR_STATUSBAR)               { public boolean run() { return execute_gr_statusbar(); } },
	};

	private final Command[] GrBitmap_cmd = new Command[] {	// Map GR.bitmap command keywords to their execution functions
		new Command(BKW_GR_BITMAP_CREATE)           { public boolean run() { return execute_gr_bitmap_create(); } },
		new Command(BKW_GR_BITMAP_CROP)             { public boolean run() { return execute_gr_bitmap_crop(); } },
		new Command(BKW_GR_BITMAP_DELETE)           { public boolean run() { return execute_gr_bitmap_delete(); } },
		new Command(BKW_GR_BITMAP_DRAWINTO_START)   { public boolean run() { return execute_gr_bitmap_drawinto_start(); } },
		new Command(BKW_GR_BITMAP_DRAWINTO_END)     { public boolean run() { return execute_gr_bitmap_drawinto_end(); } },
		new Command(BKW_GR_BITMAP_DRAW)             { public boolean run() { return execute_gr_bitmap_draw(); } },
		new Command(BKW_GR_BITMAP_FILL)             { public boolean run() { return execute_gr_bitmap_fill(); } },
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

	private final Command[] GrGroup_cmd = new Command[] {	// Map GR.group command keywords to their execution functions
		new Command(BKW_GR_GROUP_LIST)              { public boolean run() { return execute_gr_group_list(); } },
		new Command(BKW_GR_GETDL)                   { public boolean run() { return execute_gr_group_getdl(); } },
		new Command(BKW_GR_NEWDL)                   { public boolean run() { return execute_gr_group_newdl(); } },
	};

	private final Command[] GrPaint_cmd = new Command[] {	// Map GR.paint command keywords to their execution functions
		new Command(BKW_GR_PAINT_COPY)              { public boolean run() { return execute_gr_paint_copy(); } },
		new Command(BKW_GR_PAINT_GET)               { public boolean run() { return execute_gr_paint_get(); } },
		new Command(BKW_GR_PAINT_RESET)             { public boolean run() { return execute_gr_paint_reset(); } },
	};

	private final Command[] GrText_cmd = new Command[] {	// Map GR.text command keywords to their execution functions
		new Command(BKW_GR_TEXT_ALIGN)              { public boolean run() { return execute_gr_text_align(); } },
		new Command(BKW_GR_TEXT_BOLD)               { public boolean run() { return execute_gr_text_bold(); } },
		new Command(BKW_GR_TEXT_DRAW)               { public boolean run() { return execute_gr_text_draw(); } },
		new Command(BKW_GR_TEXT_HEIGHT)             { public boolean run() { return execute_gr_text_height(); } },
		new Command(BKW_GR_TEXT_SETFONT)            { public boolean run() { return execute_gr_text_setfont(); } },
		new Command(BKW_GR_TEXT_SIZE)               { public boolean run() { return execute_gr_text_size(); } },
		new Command(BKW_GR_TEXT_SKEW)               { public boolean run() { return execute_gr_text_skew(); } },
		new Command(BKW_GR_TEXT_STRIKE)             { public boolean run() { return execute_gr_text_strike(); } },
		new Command(BKW_GR_TEXT_TYPEFACE)           { public boolean run() { return execute_gr_text_typeface(); } },
		new Command(BKW_GR_TEXT_UNDERLINE)          { public boolean run() { return execute_gr_text_underline(); } },
		new Command(BKW_GR_TEXT_WIDTH)              { public boolean run() { return execute_gr_text_width(); } },
	};

	// **************** AUDIO Group

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

	// **************** SENSORS Group

	private final Command[] sensors_cmd = new Command[] {	// Map sensor command keywords to their execution functions
		new Command(BKW_SENSORS_LIST)           { public boolean run() { return execute_sensors_list(); } },
		new Command(BKW_SENSORS_OPEN)           { public boolean run() { return execute_sensors_open(); } },
		new Command(BKW_SENSORS_READ)           { public boolean run() { return execute_sensors_read(); } },
		new Command(BKW_SENSORS_CLOSE)          { public boolean run() { return execute_sensors_close(); } },
		new Command(BKW_SENSORS_ROTATE)         { public boolean run() { return execute_sensors_rotate(); } },
	};

	// **************** GPS Group

	private final Command[] GPS_cmd = new Command[] {	// Map GPS command keywords to their execution functions
		new Command(BKW_GPS_ALTITUDE)           { public boolean run() { return execute_gps_num(GpsData.ALTITUDE); } },
		new Command(BKW_GPS_LATITUDE)           { public boolean run() { return execute_gps_num(GpsData.LATITUDE); } },
		new Command(BKW_GPS_LONGITUDE)          { public boolean run() { return execute_gps_num(GpsData.LONGITUDE); } },
		new Command(BKW_GPS_BEARING)            { public boolean run() { return execute_gps_num(GpsData.BEARING); } },
		new Command(BKW_GPS_ACCURACY)           { public boolean run() { return execute_gps_num(GpsData.ACCURACY); } },
		new Command(BKW_GPS_SPEED)              { public boolean run() { return execute_gps_num(GpsData.SPEED); } },
		new Command(BKW_GPS_PROVIDER)           { public boolean run() { return execute_gps_string(GpsData.PROVIDER); } },
		new Command(BKW_GPS_SATELLITES)         { public boolean run() { return execute_gps_satellites(); } },
		new Command(BKW_GPS_TIME)               { public boolean run() { return execute_gps_num(GpsData.TIME); } },
		new Command(BKW_GPS_LOCATION)           { public boolean run() { return execute_gps_location(); } },
		new Command(BKW_GPS_STATUS)             { public boolean run() { return execute_gps_status(); } },
		new Command(BKW_GPS_OPEN, CID_OPEN)     { public boolean run() { return execute_gps_open(); } },
		new Command(BKW_GPS_CLOSE)              { public boolean run() { return execute_gps_close(); } },
	};

	// **************** ARRAY Group

	private final Command[] array_cmd = new Command[] {	// Map array command keywords to their execution functions
		new Command(BKW_ARRAY_DELETE)           { public boolean run() { return executeUNDIM(); } },
		new Command(BKW_ARRAY_DIMS)             { public boolean run() { return execute_array_dims(); } },
		new Command(BKW_ARRAY_FILL)             { public boolean run() { return execute_array_fill(); } },
		new Command(BKW_ARRAY_LENGTH)           { public boolean run() { return execute_array_length(); } },
		new Command(BKW_ARRAY_LOAD)             { public boolean run() { return execute_array_load(); } },
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

	// **************** LIST Group

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
		new Command(BKW_LIST_TOARRAY)           { public boolean run() { return execute_LIST_TOARRAY(); } },
		new Command(BKW_LIST_SEARCH)            { public boolean run() { return execute_LIST_SEARCH(); } },
	};

	// **************** BUNDLE Group

	private final Command[] bundle_cmd = new Command[] {// Map bundle command keywords to their execution functions
		new Command(BKW_BUNDLE_CREATE)          { public boolean run() { return execute_BUNDLE_CREATE(); } },
		new Command(BKW_BUNDLE_PUT)             { public boolean run() { return execute_BUNDLE_PUT(); } },
		new Command(BKW_BUNDLE_GET)             { public boolean run() { return execute_BUNDLE_GET(); } },
		new Command(BKW_BUNDLE_NEXT)            { public boolean run() { return execute_BUNDLE_NEXT(); } },
		new Command(BKW_BUNDLE_TYPE)            { public boolean run() { return execute_BUNDLE_TYPE(); } },
		new Command(BKW_BUNDLE_KEYS)            { public boolean run() { return execute_BUNDLE_KEYSET(); } },
		new Command(BKW_BUNDLE_COPY)            { public boolean run() { return execute_BUNDLE_COPY(); } },
		new Command(BKW_BUNDLE_CLEAR)           { public boolean run() { return execute_BUNDLE_CLEAR(); } },
		new Command(BKW_BUNDLE_CONTAIN)         { public boolean run() { return execute_BUNDLE_CONTAIN(); } },
		new Command(BKW_BUNDLE_REMOVE)          { public boolean run() { return execute_BUNDLE_REMOVE(); } },
	};

	// **************** STACK Group

	private final Command[] stack_cmd = new Command[] {	// Map stack command keywords to their execution functions
		new Command(BKW_STACK_CREATE)           { public boolean run() { return execute_STACK_CREATE(); } },
		new Command(BKW_STACK_PUSH)             { public boolean run() { return execute_STACK_PUSH(); } },
		new Command(BKW_STACK_POP)              { public boolean run() { return execute_STACK_POP(); } },
		new Command(BKW_STACK_PEEK)             { public boolean run() { return execute_STACK_PEEK(); } },
		new Command(BKW_STACK_TYPE)             { public boolean run() { return execute_STACK_TYPE(); } },
		new Command(BKW_STACK_ISEMPTY)          { public boolean run() { return execute_STACK_ISEMPTY(); } },
		new Command(BKW_STACK_CLEAR)            { public boolean run() { return execute_STACK_CLEAR(); } },
	};

	// **************** SOCKET Group

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

	// **************** DEBUG Group and ECHO Group

	private final Command[] debug_cmd = new Command[] {	// Map debug command keywords to their execution functions
		new Command(BKW_ON)                     { public boolean run() { return executeDEBUG_TOGGLE(ON); } },
		new Command(BKW_OFF)                    { public boolean run() { return executeDEBUG_TOGGLE(OFF); } },
		new Command(BKW_PRINT)                  { public boolean run() { return executeDEBUG_PRINT(); } },
		new Command(BKW_PRINT_SHORTCUT)         { public boolean run() { return executeDEBUG_PRINT(); } },
		new Command(BKW_DEBUG_ECHO_ON)          { public boolean run() { return executeECHO_TOGGLE(ON); } },
		new Command(BKW_DEBUG_ECHO_OFF)         { public boolean run() { return executeECHO_TOGGLE(OFF); } },
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
		new Command(BKW_DEBUG_COMMANDS)         { public boolean run() { return executeDEBUG_COMMANDS(); } },
		new Command(BKW_DEBUG_STATS)            { public boolean run() { return executeDEBUG_STATS(); } },
	};

	// Legacy: can use DEBUG.ECHO or just ECHO
	private final Command[] echo_cmd = new Command[] {	// Map echo command keywords to their execution functions
		new Command(BKW_ON)                     { public boolean run() { return executeECHO_TOGGLE(ON); } },
		new Command(BKW_OFF)                    { public boolean run() { return executeECHO_TOGGLE(OFF); } },
	};

	// **************** TTS Group - text-to-speech

	private final Command[] tts_cmd = new Command[] {	// Map TTS command keywords to their execution functions
		new Command(BKW_TTS_INIT)               { public boolean run() { return executeTTS_INIT(); } },
		new Command(BKW_TTS_SPEAK_TOFILE)       { public boolean run() { return executeTTS_SPEAK_TOFILE(); } },
		new Command(BKW_TTS_SPEAK)              { public boolean run() { return executeTTS_SPEAK(); } },
		new Command(BKW_TTS_STOP)               { public boolean run() { return executeTTS_STOP(); } }
	};

	// **************** FTP Group

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

	// **************** BT Group - Bluetooth channel operations

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

	// **************** SU and SYSTEM Groups - superuser and system commands

	private final Command[] SU_cmd = new Command[] {	// Map SU/System command keywords to their execution functions
		new Command(BKW_SU_OPEN, CID_OPEN)      { public boolean run() { return execute_SU_open(); } },
		new Command(BKW_SU_WRITE)               { public boolean run() { return execute_SU_write(); } },
		new Command(BKW_SU_READ_READY)          { public boolean run() { return execute_SU_read_ready(); } },
		new Command(BKW_SU_READ_LINE)           { public boolean run() { return execute_SU_read_line(); } },
		new Command(BKW_SU_CLOSE)               { public boolean run() { return execute_SU_close(); } }
	};

	// **************** SP Group - soundpool

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

	// **************** RINGER Group

	private final Command[] ringer_cmd = new Command[] {	// Map ringer command keywords to their execution functions
		new Command(BKW_RINGER_GET_MODE)        { public boolean run() { return executeRINGER_GET_MODE(); } },
		new Command(BKW_RINGER_SET_MODE)        { public boolean run() { return executeRINGER_SET_MODE(); } },
		new Command(BKW_RINGER_GET_VOLUME)      { public boolean run() { return executeRINGER_GET_VOLUME(); } },
		new Command(BKW_RINGER_SET_VOLUME)      { public boolean run() { return executeRINGER_SET_VOLUME(); } },
	};

	// **************** HTML Group

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

	// **************** SMS Group - text messages

	private final Command[] sms_cmd = new Command[] {	// Map SMS command keywords to their execution functions
		new Command(BKW_SMS_RCV_INIT)           { public boolean run() { return executeSMS_RCV_INIT(); } },
		new Command(BKW_SMS_RCV_NEXT)           { public boolean run() { return executeSMS_RCV_NEXT(); } },
		new Command(BKW_SMS_SEND)               { public boolean run() { return executeSMS_SEND(); } }
	};

	// **************** TIMER Group

	private final Command[] Timer_cmd = new Command[] {	// Map Timer command keywords to their execution functions
		new Command(BKW_TIMER_SET)              { public boolean run() { return executeTIMER_SET(); } },
		new Command(BKW_TIMER_CLEAR)            { public boolean run() { return executeTIMER_CLEAR(); } },
		new Command(BKW_TIMER_RESUME)           { public boolean run() { return executeTIMER_RESUME(); } }
	};

	// **************** TIMEZONE Group

	private final Command[] TimeZone_cmd = new Command[] {	// Map TimeZone command keywords to their execution functions
		new Command(BKW_TIMEZONE_SET)           { public boolean run() { return executeTIMEZONE_SET(); } },
		new Command(BKW_TIMEZONE_GET)           { public boolean run() { return executeTIMEZONE_GET(); } },
		new Command(BKW_TIMEZONE_LIST)          { public boolean run() { return executeTIMEZONE_LIST(); } }
	};

	// **************** PHONE Group

	private final Command[] phone_cmd = new Command[] {	// Map phone command keywords to their execution functions
		new Command(BKW_PHONE_CALL)             { public boolean run() { return executePHONE_DIAL(Intent.ACTION_CALL); } },
		new Command(BKW_PHONE_DIAL)             { public boolean run() { return executePHONE_DIAL(Intent.ACTION_DIAL); } },
		new Command(BKW_PHONE_RCV_INIT)         { public boolean run() { return executePHONE_RCV_INIT(); } },
		new Command(BKW_PHONE_RCV_NEXT)         { public boolean run() { return executePHONE_RCV_NEXT(); } },
		new Command(BKW_PHONE_INFO)             { public boolean run() { return executePHONE_INFO(); } }
	};

	// **************** VOLKEYS Group

	private final Command[] VolKeys_cmd = new Command[] {	// Map VolKeys command keywords to their execution functions
		new Command(BKW_ON)                 { public boolean run() { return executeVOLKEYS_TOGGLE(ON); } },
		new Command(BKW_OFF)                { public boolean run() { return executeVOLKEYS_TOGGLE(OFF); } },
	};

	// **************** APP Group - activity manager commands

	private final Command[] app_cmd = new Command[] {	// Map app command keywords to their execution functions
		new Command(BKW_APP_BROADCAST)          { public boolean run() { return executeAPP_BROADCAST(); } },
		new Command(BKW_APP_START)              { public boolean run() { return executeAPP_START(); } },
	};

	//*********************************************************************************************
	// Methods used by execution functions of group commands

	private Command findSubcommand(Command[] commands, String type) {
		Command c = ExecutingLineBuffer.findSubCommand(commands, LineIndex);
		if (c == null) { RunTimeError("Unknown " + type + " command"); }	// no keyword found
		LineIndex += ExecutingLineBuffer.subOffset();
		return c;
	}

	private boolean executeSubcommand(Command[] commands, String type) {
		Command c = findSubcommand(commands, type);
		if (c == null) return false;

		ExecutingLineBuffer.promoteSubCommand();			// replace the group command with the subcommand
		return c.run();
	}

	// Very special case: <group>.<subgroup>.<subcommand> where the <group> needs a validity check.
	// For example, GR.TEXT.SIZE: statementExecuter() must run executeGR() every time.
	private boolean executeSubgroupCommand(Command[] commands, String type) {
		int groupOffset = ExecutingLineBuffer.subOffset();	// get offset of <subgroup> keyword
		ExecutingLineBuffer.subcmd(null);					// force findSubcommand to search
		Command c = findSubcommand(commands, type);			// replace subcmd field with <subcommand>
		LineIndex -= groupOffset;							// got counted twice
		return (c != null) && c.run();
	}

	//*********************************************************************************************
	// The methods starting here are the core code for running a Basic program

	// Variable names consist of letters, digits, and these non-alphanumeric characters.
	private final static String varChars = "_@#";

	// Look for a BASIC! word: [_@#\l]?[_@#\l\d]*
	private String getWord(String line, int start, String possibleKeyword) {
		int max = line.length();
		if (start >= max || start < 0) { return ""; }
		boolean isPossibleKeyword = (possibleKeyword.length() != 0);

		int li = start;
		char c = line.charAt(li);
		if ((c >= 'a' && c <= 'z') || (varChars.indexOf(c) >= 0)) {	// if first character matches
			do {														// there's a word
				if (++li >= max) break;									// done if no more characters

				if (isPossibleKeyword &&								// caller wants to stop at keyword
					line.startsWith(possibleKeyword, li)) { break; }	// THEN, TO, or STEP

				c = line.charAt(li);									// get next character
			}															// and check it, stop if not valid
			while ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (varChars.indexOf(c) >= 0));
		}
		return line.substring(start, li);
	}

	private void PrintShow(String... strs) {				// write the console
		synchronized (mConsoleBuffer) {
			if (strs != null) {
				for (String str : strs) {
					mConsoleBuffer.add(str);
				}
			}
		}
	}

	private void SyntaxError() {						// Called to output Syntax Error Message
		if (!SyntaxError) {								// If a previous syntax error message has
			RunTimeError("Syntax Error");				// not been displayed then display one now
			SyntaxError = true;							// and set the flag so we don't do it again.
		}

		if (GRopen) {
			// If graphics is opened then the user will not be able to see error messages.
			// Provide a haptic notice.
			lv.performHapticFeedback(2, 1);
			try { Thread.sleep(300); } catch(InterruptedException e) {}
			lv.performHapticFeedback(2, 1);
			try { Thread.sleep(300); } catch(InterruptedException e) {}
			lv.performHapticFeedback(2, 1);
		}

	}

	private boolean RunTimeError(String... msgs) {
		if (mOnErrorInt == null) {						// don't display anything if error is being trapped
			String text = ExecutingLineBuffer.text();
			if (text.endsWith("\n")) { text = chomp(text); }
			PrintShow(msgs[0], text);					// display error message and offending line

			for (int i = 1; i < msgs.length; ++i) {		// display any supplemental text
				PrintShow(msgs[i]);
			}
		}
		SyntaxError = true;
		Editor.SyntaxErrorDisplacement = ExecutingLineIndex + 1;

		writeErrorMsg(msgs[0]);
		Log.d(LOGTAG, "RunTimeError: " + mErrorMsg);
		return false;									// always return false as convenience for caller
	}

	private boolean RunTimeError(Throwable e) {
		return RunTimeError("Error:", e);
	}

	private boolean RunTimeError(String prefix, Throwable e) {
		String msg = (e == null) ? null : e.getMessage();
		return RunTimeError(prefix + " " + ((msg == null) ? "?" : msg));
	}

	private void writeErrorMsg(String msg) {		// Write errorMsg, do NOT set SyntaxError
		mErrorMsg = msg + "\nLine: " + ExecutingLineBuffer.text();
	}

	private void writeErrorMsg(String prefix, Throwable e) {
		String msg = (e == null) ? null : e.getMessage();
		writeErrorMsg(prefix + " " + ((msg == null) ? "?" : msg));
	}

	private void writeErrorMsg(Exception e) {
		writeErrorMsg("Error:", e);
	}

	private void clearErrorMsg() {					// Clear the global error message
		mErrorMsg = NO_ERROR;
	}

	// ************************* start of getVar() and its derivatives ****************************

	private static final boolean TYPE_NUMERIC = true;	// true: type is numeric
	private static final boolean TYPE_STRING  = false;	// false: type is NOT numeric
	private static final boolean USER_FN_OK = true;		// flag used as parseVar() argument, when false
														// user-defined function names are not recognized as valid symbols

	private static final String EXPECT_ARRAY_VAR = "Array variable expected";
	private static final String EXPECT_ARRAY_EXISTS = "Array must be created before using";
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
	// with special cases directed by these global flags:
	//     doingDim, unDiming, SkipArrayValues, DoingDef
	// This implementation behaves as the original did when all flags were set false.
	// There are now dedicated functions for some of the special cases.
	// All other cases must be built up from the primitives found below.
	//
	private boolean getVar() {							// Get variable name if there is one.
		int LI = LineIndex;
		mVal = getVarValue(getVarAndType());			// If there is one, find it in the symbol table.
														// If not found in table, create new variable.
		if (mVal != null) return true;					// found: value is in mVal
		LineIndex = LI;									// not found: back up LineIndex
		return false;
	}

	private boolean getNVar() {							// get var and assure that it is numeric
		int LI = LineIndex;
		mVal = getVarValue(getVarAndType(TYPE_NUMERIC));
		if (mVal != null) return true;					// found: value is in mVal
		LineIndex = LI;									// not found: back up LineIndex
		return false;
	}

	private boolean getSVar() {							// get var and assure that it is not numeric
		int LI = LineIndex;
		mVal = getVarValue(getVarAndType(TYPE_STRING));
		if (mVal != null) return true;					// found: value is in mVal
		LineIndex = LI;									// not found: back up LineIndex
		return false;
	}

	private boolean getSVars(ArrayList<Var> vars) {		// get string scalar(s) or writeable array variable from command line
		boolean isArray;
		do {
			Var var = getVarAndType(TYPE_STRING);		// string var, may be scalar or array
			if (var == null)			return false;
			isArray = var.isArray();
			if (isArray) {								// note: new array is not added to symbol table yet
				if (!validArrayVarForWrite(var, TYPE_STRING)) return false;
			} else {
				if (getVarValue(var)					// create a new variable in symbol table if necessary
							== null)	return false;	// for completeness: can't happen because var is not array
			}
			vars.add(var);								// add to caller's Var list
		} while (!isArray && isNext(','));				// continue until no more parameters unless one was an array
		return true;
	}

	// Get a Var for creating an array: must be array name with no index(es).
	private Var getArrayVarForWrite() {					// returns the Var, null if error or no var
		Var var = getVarAndType();						// either string or numeric type is ok
		if (validArrayVarForWrite(var)) { return var; }			// no error: return Var
																// error: return null
		if (var != null) { LineIndex -= var.name().length(); }	// if LineIndex moved, back it up
		return null;
	}

	private boolean validArrayVarForWrite(Var var) {
		if ((var == null) || !var.isArray())	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (!isNext(']'))						{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
		return true;									// no error
	}

	// Get a Var for creating an array: must be array name of correct type with no index(es).
	private Var getArrayVarForWrite(boolean type) {		// returns the Var, null if error or no var
		Var var = getVarAndType();						// either string or numeric type is ok
		if (validArrayVarForWrite(var, type)) { return var; }	// no error: return Var
																// error: return null
		if (var != null) { LineIndex -= var.name().length(); }	// if LineIndex moved, back it up
		return null;
	}

	private boolean validArrayVarForWrite(Var var, boolean type) {
		if ((var == null) || !var.isArray())	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (type != var.isNumeric())			{ return RunTimeError(type ? EXPECT_NUM_ARRAY : EXPECT_STRING_ARRAY); }
		if (!isNext(']'))						{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
		return true;									// no error
	}

	// Get a Var for using an array or array segment: must be name of existing array
	private Var getExistingArrayVar() {					// returns the Var, null if error or no var
		Var var = getVarAndType();
		if (validExistingArrayVar(var)) { return var; }			// no error: return Var
																// error: return null
		if (var != null) { LineIndex -= var.name().length(); }	// if LineIndex moved, back it up
		return null;
	}

	private boolean validExistingArrayVar(Var var) {
		if ((var == null) || !var.isArray())	{ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (var.isNew())						{ return RunTimeError(EXPECT_ARRAY_EXISTS); }
		return true;									// no error
	}

	private Var getNewFNVar() {							// get var and assure that it is a new function name
														// returns the name, does NOT create a variable
		Var var = parseVar(USER_FN_OK);					// Get function name if there is one.
		if (var != null) {
			if (var.isFunction()) {						// If there is one...
				var = searchVar(var);					// ... look for it in the symbol table.
				if (var.isNew()) {
					return var;							// return new Var: SUCCESSFUL EXIT
				} else {
					RunTimeError(EXPECT_NEW_FN_NAME);	// not new
				}
			}
			LineIndex -= var.name().length();			// name not valid, back up LineIndex
		}
		return null;									// ERROR EXIT
	}

	// ************************* top half of getVar() *************************

	// Result is a Var, or null if no variable name found on the line.
	private Var getVarAndType() {
		Var var = parseVar(!USER_FN_OK);				// Get variable name if there is one.
		if (var != null) {								// If there is one...
			var = searchVar(var);						// ... find it in the symbol table.
		}
		return var;										// note: LineIndex does not change if var is null
	}

	// Result is a Var, or null if no variable name found on the line, or if variable type is wrong.
	private Var getVarAndType(boolean needNumeric) {	// specify TYPE_NUMERIC or TYPE_STRING
		Var var = parseVar(!USER_FN_OK);				// get variable name if there is one
		if (var != null) {								// if found, parseVar changed LineIndex
			if (needNumeric == var.isNumeric()) {		// if type matches expected type
				return searchVar(var);					// look up the variable name
			}											// else type mismatch, return no var
			LineIndex -= var.name().length();			// back up LineIndex
		}
		return null;									// no variable name found, LineIndex does not change
	}

	// Gets the variable name and type. Returns them in a Var object.
	// Name includes $ for strings, [ for arrays, ( for functions.
	// If arg is false, user-defined function names are not valid variable names.
	// If no valid variable name, returns null and does not advance LineIndex.
	//
	// Note: returns one of the "working Var" pool. Don't store this reference
	// anywhere. Copy the Var instead.
	private Var parseVar(boolean isUserFnAllowed) {
		String text = ExecutingLineBuffer.text();
		int LI = LineIndex;
		int max = text.length();
		Var var = null;

		// PosibleKeyWord is for the special cases where a var could be followed by keyword THEN, TO or STEP.
		String name = getWord(text, LI, PossibleKeyWord);		// isolate the var characters
		LI += name.length();
		if (LI > LineIndex) {									// no var if length is 0
			char c = text.charAt(LI);
			boolean isNumeric = (c != '$');						// Is this a string var?
			if (!isNumeric) {
				name += c; ++LI;
				c = text.charAt(LI);
			}
			if (c == '[') {										// Is this an array?
				name += c; ++LI;
				var = mArrayVar.reNew(name, isNumeric);
			} else if (c == '(') {								// Is this a function?
				if (!isUserFnAllowed) { return null; }			// Do not write LineIndex
				name += c; ++LI;
				var = mFunctionVar.reNew(name, isNumeric);
			} else {
				var = mScalarVar.reNew(name, isNumeric);
			}
		}
		if (LI >= max) { LineIndex = max; return null; }		// can't happen: line ends with "\n"
		LineIndex = LI;
		VarIsInt = false;										// Never an integer
		return var;												// null if no variable name found
	}

	// Always returns a Var, never null. Can use this Var anywhere;
	// if it was one of the "working Var" pool from parseVar() it gets copied.
	private Var searchVar(Var var) {				// search for a variable by name
// Skip global table for now, there's nothing in it.
		Var v = null;
//		Var v = searchVar(mGlobalSymbolTable, var);	// first search global symbols
//		if (v == null) {
			if (interruptResume < 0) {				// normal operation, not in interrupt
				v = searchVar(mSymbolTable, var);	// search local symbols
			} else {										// in interrupt
				for (Var.Table symbols : mSymbolTables) {	// search all tables
					v = searchVar(symbols, var);
					if (v != null) { break; }
				}
			}
			if (v != null)		{ return v; }
//		}
		return var.copy();							// not in lists of variable names, return new Var
	}

	// Utility for searchVar(Var), searches one table.
	private Var searchVar(Var.Table symbols, Var var) {	// search a table for a variable by name
		if (symbols == null)	{ return null; }	// no table
		int j = Collections.binarySearch(symbols.mVarNames, var.name());
		if (j < 0)				{ return null; }	// name not found

		var = symbols.mVars.get(j);
		if (var.isArray()) {
			// If variable is array invalidated by UNDIM,
			// delete it so a new one with the same name can be created.
			Var.ArrayDef array = var.arrayDef();
			if ((array != null) && !array.valid()) {
				symbols.remove(j);
				var.reNew();
			}
		}
		return var;
	}

	// ************************* bottom half of getVar() **********************

	// Do NOT call before calling parseVar() and searchVar(Var).
	// can automatically create scalar but not array.
	// Error if isArray and isNew or if GetArrayValue returns null.
	private Var.Val getVarValue(Var var) {			// bottom half of getVar()
		if (var == null) return null;				// no var to get
		Var.Val val = null;
		if (var.isArray()) {
			Var.ArrayVar aVar = (Var.ArrayVar)var;
			if (var.isNew()) {						// new array: error
				RunTimeError(EXPECT_ARRAY_EXISTS);	// set error and return null
			} else {								// existing array
				val = GetArrayValue(aVar);			// get a value based upon user's index values
			}
		} else {
			if (var.isNew()) {						// new scalar: put an empty Val in the Var
				var.newVal();						// make a new Val attached to the Var
				insertNewVar(var);					// and put the Var in the symbol table;
			}
			val = var.val();						// get the Val from the Var
		}
		return val;
	}

	// Get insertion point (-index - 1) so name list will still be in alphabetical order.
	private void insertNewVar(Var var) {			// make a new var table entry
		String name = var.name();
		// Throws exception if var already exists. If necessary, avoid by testing var.isNew() first.
		int index = newVarIndex(mSymbolTable.mVarNames, name);
		var.notNew();
		mSymbolTable.add(index, name, var);			// create new intry in symbol table
	}

	// ************************************* end of getVar() **************************************

	// ********************************** The Expression Parsers **********************************

	private boolean getNumber() {						// Get a number if there is one
		char c = 0;
		String text = ExecutingLineBuffer.text();
		int max = text.length();
		int i = LineIndex;
		while (i < max) {								// Must start with one or more digits
			c = text.charAt(i);
			if (c > '9' || c < '0') { break; }			// If not a digit, done with whole part
			++i;
		}
		if (i == LineIndex) { return false; }			// No digits, not a number

		if (c == '.') {									// May have a decimal point
			while (++i < max) {							// Followed by more digits
				c = text.charAt(i);
				if (c > '9' || c < '0') { break; }		// If not a digit, done with fractional part
			}
		}
		if (c == 'e' || c == 'E') {						// Is there an exponent
			if (++i < max) {
				c = text.charAt(i);
				if (c == '+' || c == '-') { ++i; }		// Is there a sign on the exponent
			}
			while (i < max) {							// Get the exponent
				c = text.charAt(i);
				if (c > '9' || c < '0') { break; }		// If not a digit, done with exponent
				++i;
			}
		}
		String num = text.substring(LineIndex, i);		// isolate the numeric characters
		LineIndex = i;
		double d = 0.0;
		try { d = Double.parseDouble(num); }			// have java parse it into a double
		catch (Exception e) { return RunTimeError(e); }	// can't happen if above parsing is correct

		GetNumberValue = d;								// Report the value
		return true;									// Say we found a number
	}

	private boolean GetStringConstant() {				// Get a string constant if there is one
		String text = ExecutingLineBuffer.text();
		int max = text.length();
		if (LineIndex >= max || LineIndex < 0) { return false; }

		int i = LineIndex;
		StringConstant = "";
		char c = text.charAt(i);
		if (c != '"')     { return false; }				// first char not "", not String Constant
		while (true) {									// Get the rest of the String
			++i;										// copy character until " or EOL
			if (i >= max) { return false; }
			c = text.charAt(i);
			if (c == '"') { break; }					// if " we're done

			if (c == '\r') {			// AddProgramLine hides embedded newline as carriage return
				c = '\n';
			} else if (c == '\\') {		// AddProgramLine allows only quote or backslash after backslash
				c = text.charAt(++i);
			}
			StringConstant += c;						// add to String Constant
		}

		if (i< max-1) { ++i; }							// do not let index be >= line length
		LineIndex = i;
		return true;									// Say we have a string constant
	}

	private boolean evalToPossibleKeyword(String keyword) {	// use with midline keywords THEN, TO, STEP
		// Evaluate a numeric expression, terminated either by EOL or by the given possible keyword.
		// Expression value is left in EvalNumericExpressionValue; return true is expression is valid.
		PossibleKeyWord = keyword;					// tell parseVar to expect the keyword
		boolean ok = evalNumericExpression();
		PossibleKeyWord = "";						// restore global before return
		return ok;
	}

	private boolean evalNumericExpression() {			// Evaluate a numeric expression

		if (LineIndex >= ExecutingLineBuffer.length()) return false;
		char c = ExecutingLineBuffer.text().charAt(LineIndex);
		if (c == '\n' || c == ')') return false;			// If eol or starts with ')', there is not an expression

		Stack<Double> ValueStack = new Stack<Double>();		// Each call to eval gets its own stack
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

															// The recursive part of evalNumericExpression
	private boolean ENE(Stack<Integer> theOpStack, Stack<Double> theValueStack) {
		double incdec = 0.0;									// for recording pre-inc/dec
		char c = ExecutingLineBuffer.text().charAt(LineIndex);	// First character
		if (c == '+') {											// Check for unary operators or pre-inc/dec
			++LineIndex;										// move to the next character
			if (isNext('+')) { incdec = 1.0; }					// remember to pre-increment
			else { theOpStack.push(UPLUS); }					// save the operator
		}
		else if(c == '-') {
			++LineIndex;
			if (isNext('-')) { incdec = -1.0; }					// remember to post-decrement
			else { theOpStack.push(UMINUS); }					// save the operator
		}
		else if (c == '!') {
			theOpStack.push(NOT);
			++LineIndex;
		}

		int holdLI = LineIndex;
		Var var = parseVar(USER_FN_OK);							// duplicate getVarAndType() except USER_FN_OK
		if (var != null) {										// is variable or function name
			if (!var.isFunction()) {
				var = searchVar(var);							// finish getVarAndType()
				if (var.isNumeric()) {
					mVal = getVarValue(var);					// finish getNVar()
					if (mVal != null) {							// we have a numeric variable!
						Var.Val val = mVal;
						double value = val.nval();
						if (incdec != 0) {
							value += incdec;					// pre-inc or dec
							val.val(value);
						}
						if      (ExecutingLineBuffer.startsWith(OP_INC, LineIndex)) {
							val.val(value + 1);					// post-increment
							LineIndex += 2;
						}
						else if (ExecutingLineBuffer.startsWith(OP_DEC, LineIndex)) {
							val.val(value - 1);					// post-decrement
							LineIndex += 2;
						}
						theValueStack.push(value);					// PUSH THE VALUE of the nvar
					} else { return false; }					// nvar, but null val: runtime error getting array val
				} else {										// var but not numeric
					LineIndex = holdLI;							// not nvar, back up and try something else
				}
			} else {											// function name
				if (incdec != 0) { SyntaxError(); return false; } // pre-inc/dec applies only to numeric variables
				Command cmd;
				String name = var.name();
				Var.FnDef def = mFunctionTable.get(name);
				if ((def != null) && def.type().isNumeric()) {	// like isUserFunction(true, TYPE_NUMERIC)
					if (!doUserFunction(def)) { SyntaxError(); return false; }
					theValueStack.push(EvalNumericExpressionValue);	// PUSH THE VALUE (result of the function)
				} else if ((cmd = MF_map.get(name)) != null) {	// try Math Function
					if (!doMathFunction(cmd)) { SyntaxError(); return false; }
					theValueStack.push(EvalNumericExpressionValue);	// PUSH THE VALUE (result of the function)
				} else {
					LineIndex = holdLI;							// not valid num function, back up and try something else
				}
			}
		}														// else var is null, try something else

		if (LineIndex == holdLI) {								// no valid Var found, here's where we try stomething else
			if (incdec != 0) { SyntaxError(); return false; }	// pre-inc/dec applies only to numeric variables
			else if (getNumber()) {								// is it a number?
				theValueStack.push(GetNumberValue);				// PUSH THE VALUE (the number)
			} else if (evalStringExpression()) {				// try String Logical Expression
				if (!SEisLE) return false;						// if was not a logical string expression, fail
				theValueStack.push(EvalNumericExpressionValue);
			} else if (isNext('(')) {							// handle possible (
				String holdPKW = PossibleKeyWord;
				PossibleKeyWord = "";
				boolean ok = evalNumericExpression() && isNext(')'); // eval expression inside the parens
				PossibleKeyWord = holdPKW;						// restore global before possible return
				if (!ok) { return false; }
				theValueStack.push(EvalNumericExpressionValue);	// no errors, push expression value
			}
			else { return false; }								// nothing left, fail
		}

		if (LineIndex >= ExecutingLineBuffer.length()) { return false; }
		c = ExecutingLineBuffer.text().charAt(LineIndex);

		if (!PossibleKeyWord.equals("")) {
			if (ExecutingLineBuffer.startsWith(PossibleKeyWord, LineIndex)) {
				return handleOp(EOL, theOpStack, theValueStack);
			}
		}

		if (",:;]".indexOf(c) >= 0) {						// treat any of these characters as an eol
			return handleOp(EOL, theOpStack, theValueStack);
		}

		if (!getOp()) { return false; }						// If operator does not follow, then fail

		switch (OperatorValue) {							// Handle special case operators
															// (This is probably redundant given the above)
		case EOL:
			if (!handleOp(EOL,  theOpStack, theValueStack)) { return false; }
			--LineIndex;
			return true;
		case RPRN:
			if (!handleOp(RPRN,  theOpStack, theValueStack)) { return false; }
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
			if (!handleOp(OperatorValue,  theOpStack, theValueStack)) { // Handles non special case ops
				 return false;
			}
		}

		return ENE(theOpStack, theValueStack);				// Recursively call ENE for rest of expression
	}

	private boolean getOp() {								// Get an expression operator if there is one

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

	private boolean handleOp(int op, Stack<Integer> theOpStack, Stack<Double> theValueStack) {	// handle an expression operator

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

		while (ComesOffPrecedence[theOpStack.peek()] >= GoesOnPrecedence[op]) {

				if (theValueStack.empty()) return false;	// Avoid a crash
				ExecOp = theOpStack.pop();

											// Execute the popped operator
											// In general values are popped from the stack and then
											// operated on by the operator
											// the result is then pushed onto the value stack
				switch (ExecOp) {

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
					d1 = d2;
					break;
				default:
				}
//			if (op == RPRN) break;

		} // End of while pop stack operations

		theOpStack.push(op);          // Push the current operator
		return true;
	}

	private boolean evalStringExpression() {					// Evaluate a string expression

		int max = ExecutingLineBuffer.length();
		if (LineIndex >= max) { return false; }

		char c = ExecutingLineBuffer.text().charAt(LineIndex);
		if (c == '\n' || c == ')') { return false; }			// If eol or starts with ')', there is not an expression

		SEisLE = false;											// Assume not Logical Expression

		if (!ESE()) { return false; }							// Get the next element (constant, var, function, etc)
		String Temp1 = StringConstant;
		StringBuilder sb = null;
		while (isNext('+')) {									// Another piece to concatenate?
			if (!ESE()) { return false; }						// Get the next element (constant, var, function, etc)
			if (sb == null) { sb = new StringBuilder(Temp1); }
			sb.append(StringConstant);							// save the resulting string
		}
		if (sb != null) {
			StringConstant = Temp1 = sb.toString();
			sb = null;
		}

		EvalNumericExpressionValue = 0.0;						// Set Logical Compare Result to false
		if (LineIndex >= max) { return false; }
		c = ExecutingLineBuffer.text().charAt(LineIndex);
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
				if (sb == null) { sb = new StringBuilder(Temp2); }
				sb.append(StringConstant);						// build up the right side string
			} else {											// what follows is not a string expression
				LineIndex = SaveLineIndex;						// assume the + operation is numeric
				break;
			}
		}
		if (sb != null) { StringConstant = Temp2 = sb.toString(); }

		if ((Temp1 == null) || (Temp2 == null)) { return false; }
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

	private boolean ESE() {										// Get a String expression element

		if (GetStringConstant())		return true;			// Try String Constant

		int LI = LineIndex;

		if (isNext('(')) {										// Try parenthesized string expression
			if (getStringArg() && isNext(')')) return true;		// logical expresson not allowed here
			LineIndex = LI;
			return false;
		}

		Var var = parseVar(USER_FN_OK);							// duplicate getVarAndType() except USER_FN_OK
		if (var == null)				return false;			// no variable or function name

		if (var.isFunction()) {									// name ends with '('; it's a function
			if (var.isString()) {								// could be a user-defined string-type function
				Var.FnDef fn = mFunctionTable.get(var.name());	// search like isUserFunction()
				if (fn != null) {
					boolean ok = doUserFunction(fn);			// execute the user function
					if (!ok) { LineIndex = LI; SyntaxError(); }
					return ok;
				}
			}

			Command cmd = SF_map.get(var.name());				// try built-in string function
			if (cmd != null) {
				boolean ok = cmd.run();							// execute the built-in functon
				if (!ok) { LineIndex = LI; SyntaxError(); }
				return ok;
			}
		} else													// array or scalar
		if (var.isString()) {									// is it a string type?
			var = searchVar(var);
			Var.Val val = getVarValue(var);						// bottom half of getVar()
			if (val != null) {
				StringConstant = val.sval();
				return true;
			}
		}

		LineIndex = LI;
		return false;
	}

	// ******************************* Statement Parsing Utilities ********************************

	private boolean isEOL() {
		return (LineIndex >= ExecutingLineBuffer.length()) ||
				(ExecutingLineBuffer.text().charAt(LineIndex) == '\n');
	}

	private boolean checkEOL() {
		if (isEOL()) return true;
		String ec = ExecutingLineBuffer.text().substring(LineIndex);
		RunTimeError("Extraneous characters in line: " + ec);
		return false;
	}

	private boolean isNext(char c) {		// Check the current character
		if ((LineIndex < ExecutingLineBuffer.length()) &&	// if it is as expected...
			(ExecutingLineBuffer.text().charAt(LineIndex) == c)) {
			++LineIndex;						// ... increment the character pointer
			return true;
		}
		return false;							// else don't increment LineIndex
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

	// Get optional arguments, where all are variables, not expressions.
	// types: 1 numeric, 2 string, 3 either
	// Type 3 coming in is overwritten to type of variable found on command line.
	private boolean getOptVars(byte[] types, Var.Val[] vals) {

		if (isEOL()) return true;							// no arguments
		int nArgs = types.length;
		if (nArgs != vals.length) return false;				// array lengths must match

		boolean isComma = true;
		for (int arg = 0; arg < nArgs; ++arg) {
			if (isComma) {
				isComma = isNext(',');
				if (!isComma) {
					if (!getVar()) return false;
					Var.Val val = mVal;
					byte vType = (byte)(val.isNumeric() ? 1 : 2);
					if ((vType & types[arg]) == 0) return false; // type mismatch
					types[arg] = vType;
					vals[arg] = val;
					isComma = isNext(',');
				}
			}
		}
		return (!isComma && checkEOL());
	} // getOptVars

	// Get optional arguments, where all are expressions, not variables.
	// types: 1 numeric, 2 string, 3 either
	// Type 3 coming in is overwritten to type of expression found on command line.
	private boolean getOptExprs(byte[] type, Double[] nVal, String[] sVal) {

		if (isEOL()) return true;							// no arguments
		int nArgs = type.length;
		if (nArgs != nVal.length) return false;				// array lengths must match
		if (nArgs != sVal.length) return false;				// array lengths must match

		boolean isComma = true;
		for (int arg = 0; arg < nArgs; ++arg) {
			int typ = type[arg];
			if (isComma) {
				isComma = isNext(',');
				if (!isComma) {
					if (typ != 2) {							// try numeric expression
						if (!evalNumericExpression()) {
							if (typ == 1) return false;		// required numeric
							typ = 2;						// actual type is string
						} else {
							if (typ == 3) { typ = 1; }		// actual type is numeric
							nVal[arg] = EvalNumericExpressionValue;
						}
					}
					if (typ == 2) {							// try string expression
						if (!getStringArg()) return false;
						sVal[arg] = StringConstant;
					}
					isComma = isNext(',');
				}
			}
		}
		return (!isComma && checkEOL());
	} // getOptExprs(byte[], Double[], Sring[])

	// Like getOptExprs, but limited to integer arguments.
	private boolean getOptExprs(int[] iVal) {
		if (isEOL()) return true;							// no arguments
		int nArgs = iVal.length;
		boolean isComma = true;
		for (int arg = 0; arg < nArgs; ++arg) {
			if (isComma) {
				isComma = isNext(',');
				if (!isComma) {
					if (!evalNumericExpression()) return false;
					iVal[arg] = EvalNumericExpressionValue.intValue();
					isComma = isNext(',');
				}
			}
		}
		return (!isComma && checkEOL());
	} // getOptExprs(int[])

	// Like getOptExprs, but limited to String arguments.
	private boolean getOptExprs(String[] sVal) {
		if (isEOL()) return true;							// no arguments
		int nArgs = sVal.length;
		boolean isComma = true;
		for (int arg = 0; arg < nArgs; ++arg) {
			if (isComma) {
				isComma = isNext(',');
				if (!isComma) {
					if (!getStringArg()) return false;
					sVal[arg] = StringConstant;
					isComma = isNext(',');
				}
			}
		}
		return (!isComma && checkEOL());
	} // getOptExprs(String[])

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

	private Var.Val[] getArgs4NVar() {								// get four numeric var arguments
		if (!getNVar())		{ return null; }
		Var.Val v1 = mVal;
		if (!isNext(','))	{ return null; }
		if (!getNVar())		{ return null; }
		Var.Val v2 = mVal;
		if (!isNext(','))	{ return null; }
		if (!getNVar())		{ return null; }
		Var.Val v3 = mVal;
		if (!isNext(','))	{ return null; }
		if (!getNVar())		{ return null; }
		Var.Val v4 = mVal;
		Var.Val[] args = {v1, v2, v3, v4};
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

	// ************************************** Math Functions **************************************

	private boolean doMathFunction(Command cmd) {
		// If the function exists, run it, and verify that the closing ')' is present.
		// Function value is returned in EvalNumericExpressionValue.
		return (cmd != null) && cmd.run() && isNext(')');
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
		double d1 = EvalNumericExpressionValue;
		if (d1 < 0) {
			return RunTimeError("SQR parameter must be >= 0");
		}
		EvalNumericExpressionValue = Math.sqrt(d1);
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
		if (d1 < -1 || d1 > 1) {
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
		if (!getStringArg()) return false;				// Get and check the string expression
		double d1 = StringConstant.length();			// then get its length
		EvalNumericExpressionValue = d1;
		return true;
	}

	private boolean executeMF_RANDOMIZE() {
		if (isNext(')')) {								// no parameter
			--LineIndex;								// unget the ')'
			randomizer = new Random();					// same as seed == 0
		} else {
			if (!evalNumericExpression()) return false;
			long seed = EvalNumericExpressionValue.longValue();
			randomizer = (seed == 0L) ? new Random() : new Random(seed);
		}
		EvalNumericExpressionValue = 0.0;
		return true;
	}

	private boolean executeMF_RND() {
		if (randomizer == null) { randomizer = new Random(); }
		EvalNumericExpressionValue = randomizer.nextDouble();
		return true;
	}

	private boolean executeMF_BACKGROUND() {
		boolean background = RunPaused && !GRFront && !mWebFront;
		EvalNumericExpressionValue = background ? 1.0 : 0.0;
		return true;
	}

	private boolean executeMF_VAL() {					// VAL(s$
		if (!getStringArg()) return false;				// Get and check the string expression
		StringConstant = StringConstant.trim();
		if (StringConstant.length() == 0) {
			return RunTimeError("VAL of empty string is not valid");
		}
		try {
			double d1 = Double.parseDouble(StringConstant);	// have java parse it into a double
			EvalNumericExpressionValue = d1;
		} catch (NumberFormatException e) {
			return RunTimeError("Not a valid number: " + StringConstant);
		}
		return true;
	}

	private boolean executeMF_IS_NUMBER() {				// IS_NUMBER(s$
		if (!getStringArg()) return false;				// Get and check the string expression
		StringConstant = StringConstant.trim();
		double isNum = 0.0;								// default: not a valid numeric string
		if (StringConstant.length() > 0) {				// not valid if length 0
			try {
				Double.parseDouble(StringConstant);		// have java parse it into a double
				isNum = 1.0;							// return 1: it's a valid numeric string
			} catch (NumberFormatException e) { }		// return 0: not a valid numeric string
		}
		EvalNumericExpressionValue = isNum;
		return true;
	}

	private int getIndexArg(int max) {					// return 1-based index, 0 if error, default 1
		int index = 1;
		if (isNext(',')) {
			if (!evalNumericExpression()) return 0;
			index = EvalNumericExpressionValue.intValue();
			if (max != 0) {								// if empty string, index is irrelevant
				if ((index < 1) || (index > max)) {
					RunTimeError("Index (" + index + ") out of range");
					return 0;
				}
			}
		}
		return index;
	}

	private boolean executeMF_ASCII() {
		if (!getStringArg()) return false;				// Get and check the string expression
		String arg = StringConstant;
		int len = arg.length();
		int index = getIndexArg(len);					// get 1-based string index, 0 if error
		if (--index < 0) return false;					// convert to 0-based index

		EvalNumericExpressionIntValue = (len == 0) ? 256L : (arg.charAt(index) & 0x00FF);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_UCODE() {
		if (!getStringArg()) return false;				// Get and check the string expression
		String arg = StringConstant;
		int len = arg.length();
		int index = getIndexArg(len);					// get 1-based string index, 0 if error
		if (--index < 0) return false;					// convert to 0-based index

		EvalNumericExpressionIntValue = (len == 0) ? 0x10000L : arg.charAt(index);
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
		return true;
	}

	private boolean executeMF_MOD() {					// MOD( d1,d2
		double[] args = getArgsDD();
		if (args == null) return false;
		if (args[1] == 0.0) { return RunTimeError("DIVIDE BY ZERO AT:"); }
		EvalNumericExpressionValue = (args[0] % args[1]);
		return true;
	}

	private boolean executeMF_BNOT() {
		if (!evalNumericExpression()) return false;
		long arg = EvalNumericExpressionValue.longValue();
		EvalNumericExpressionIntValue = ~arg;
		EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
		VarIsInt = true;
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

	private boolean executeMF_IS_IN() {
		String[] args = getArgsSS();
		if (args == null) return false;
		String searchFor = args[0];
		String searchIn = args[1];

		int start = 1;
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			start = EvalNumericExpressionValue.intValue();
		}

		double k;											// result: index of matched substring
		if (start < 0) {									// do reverse search
			start += searchIn.length();						// zero-based index of start character
			k = searchIn.lastIndexOf(searchFor, start);
		} else {											// do forward search
			--start;										// zero-based index of start character
			k = searchIn.indexOf(searchFor, start);
		}
		EvalNumericExpressionValue = (k < 0) ? 0 : ++k;		// convert to one-based index
		return true;
	}

	private boolean executeMF_STARTS_WITH() {
		String[] args = getArgsSS();
		if (args == null) return false;
		String searchFor = args[0];
		String searchIn = args[1];
		int start = 1;

		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			start = EvalNumericExpressionValue.intValue();
			if (start < 1) {
				return RunTimeError("Start value must be >= 1");
			}
		}

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
		if (ExecutingLineBuffer.text().charAt(LineIndex)== ')') {	// If no args, use current time
			EvalNumericExpressionIntValue = System.currentTimeMillis();
		} else {													// Otherwise, get user-supplied time
			Time time = theTimeZone.equals("") ? new Time() : new Time(theTimeZone);
			if (!parseTimeArgs(time)) return false;
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
		if (!getStringArg()) return false;				// Get and check the string expression
		try {
			EvalNumericExpressionIntValue = new BigInteger(StringConstant, base).longValue();
			EvalNumericExpressionValue = EvalNumericExpressionIntValue.doubleValue();
			VarIsInt = true;
		} catch (NumberFormatException e) {
			return RunTimeError("Not a valid number: " + StringConstant);
		}
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
		if (!getStringArg())			return false;
		String str = StringConstant;
		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'

		int length = str.length();
		if (length > 0) {
			int count = EvalNumericExpressionValue.intValue();
			if (count < 0) {
				count += length;
				str = (count <= 0) ? "" : str.substring(0, count);
			} else if (count < length) {
				str = str.substring(0, count);
			}
		}
		StringConstant = str;
		return true;
	}

	private boolean executeSF_RIGHT() {													// RIGHT$
		if (!getStringArg())			return false;
		if (!isNext(','))				return false;
		String str = StringConstant;
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'

		int length = str.length();
		if (length > 0) {
			int count = EvalNumericExpressionValue.intValue();
			if (count < 0) {
				str = (-count >= length) ? "" : str.substring(-count);
			} else if (count < length) {
				str = str.substring(length - count);
			}
		}
		StringConstant = str;
		return true;
	}

	private boolean executeSF_MID() {													// MID$
		if (!getStringArg())			return false;	// Get the string
		String str = StringConstant;
		int length = str.length();

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;	// Get the start index
		int start = EvalNumericExpressionValue.intValue();
		int count;

		if (isNext(',')) {								// If there is a count, get it
			if (!evalNumericExpression()) return false;
			count = EvalNumericExpressionValue.intValue();
		} else {
			count = length;								// Default count is whole string
		}
		if (!isNext(')'))				return false;	// Function must end with ')'

		if (length > 0) {
			if (--start < 0) { start = 0; }				// change 1-based index to 0-based
			else if (start >= length) { start = length; }
			int end;									// 0-based end index
			if (count > 0) {
				end = start + count;
				if (end > length) { end = length; }
			} else {
				end = start + 1;
				if (end > length) { end = length; }
				start = end + count;
				if (start < 0) { start = 0; }
			}
			if ((count == 0) || (start >= length)) { str = ""; }
			else if ((start > 0) || (end < length)) { str = str.substring(start, end); }
		}
		StringConstant = str;
		return true;
	}

	private String ltrim(String str, String trim) {			// remove
		if ((str == null) || str.equals("")) return "";
		if ((trim == null) || trim.equals("")) return str;
		if (!trim.startsWith("^")) { trim = "^" + trim; }
		return str.replaceFirst(trim, "");
	}

	private String rtrim(String str, String trim) {
		if ((str == null) || str.equals("")) return "";
		if ((trim == null) || trim.equals("")) return str;
		if (!trim.endsWith("$")) { trim += "$"; }
		return str.replaceFirst(trim, "");
	}

	private boolean executeSF_TRIM(int what) {											// TRIM$
														// use TLEFT, TRIGHT, or TLEFT|TRIGHT for what arg
		if (!getStringArg())			return false;
		String str = StringConstant;
		String trim = "\\s+";							// default: trim whitespace
		if (isNext(',')) {
			if (!getStringArg())		return false;
			trim = StringConstant;
		}
		if (!isNext(')'))				return false;	// Function must end with ')'

		if (trim != "") {
			if ((what & TLEFT) != 0)  { str = ltrim(str, trim); }
			if ((what & TRIGHT) != 0) { str = rtrim(str, trim); }
		}
		StringConstant = str;
		return true;
	}

	private boolean executeSF_WORD() {													// WORD$
		if (!getStringArg())			return false;	// string to split
		String SearchString = StringConstant;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;	// which word to return
		int wordIndex = EvalNumericExpressionValue.intValue();

		String r[] = doSplit(SearchString, 0);			// get regex arg, if any, and split the string.
		if (!isNext(')'))				return false;	// Function must end with ')'

		int length = r.length;							// get the number of strings generated
		if (length == 0)				return false;	// error in doSplit()

		wordIndex--;									// convert to 0-based index
		for (int i = 0; (i < length) && (r[i].length() == 0); ++i) {
			wordIndex++;								// special case: string started with delimiter(s)
		}
		StringConstant = ((wordIndex < 0) || (wordIndex >= length)) ? "" : r[wordIndex];
		return true;
	}

	private boolean executeSF_STR() {													// STR$
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		StringConstant = String.valueOf(EvalNumericExpressionValue);
		return true;
	}

	private boolean executeSF_UPPER() {													// UPPER$
		if (!getStringArg())			return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		StringConstant = StringConstant.toUpperCase(Locale.getDefault());
		return true;
	}

	private boolean executeSF_LOWER() {													// LOWER$
		if (!getStringArg())			return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		StringConstant = StringConstant.toLowerCase(Locale.getDefault());
		return true;
	}

	private boolean executeSF_ENCODE(boolean mode) {									// DECODE$ and ENCODE$
		int encoding;
		String charset = "UTF-8";						// default charset
		boolean isText = STR_TEXT;		// STR_TEXT: assume input is text String
										// STR_BINARY: assume input is binary "buffer string" (ISO-8859-1)
		clearErrorMsg();

		// If one of the special encodings (Base64, URL, or encryption),
		//   first arg is the type, (optional) second is charset (password if encryption)
		//   third (required) is the string to encode/decode.
		// Otherwise plain string conversion:
		//   first arg is charset, second is string to convert.
		if (!getStringArg())			return false;	// get the type or charset
		String type = StringConstant;
		if (!isNext(','))				return false;
		String typeLC = type.toLowerCase(Locale.getDefault());
		if		(typeLC.equals("base64"))		{ encoding = ENCODING_BASE64; isText = STR_BINARY; }
		else if	(typeLC.equals("url"))			{ encoding = ENCODING_URL; }
		else if	(typeLC.equals("encrypt"))		{ encoding = ENCODING_ENCRYPT; }
		else if	(typeLC.equals("decrypt"))		{ encoding = ENCODING_ENCRYPT; }
		else if	(typeLC.equals("encrypt_raw"))	{ encoding = ENCODING_ENCRYPT; isText = STR_BINARY; }
		else if	(typeLC.equals("decrypt_raw"))	{ encoding = ENCODING_ENCRYPT; isText = STR_BINARY; }
		else {
			// Plain string conversion. First arg is charset, second is string to convert.
			encoding = ENCODING_OTHER;
			charset = type;
		}

		String src;
		switch (encoding) {
			case ENCODING_ENCRYPT:
				charset = "";							// default password is empty string
				// Fall through to get password, if present.
			case ENCODING_BASE64:
			case ENCODING_URL:
				if (!isNext(',')) {
					if (!getStringArg()) return false;	// get the optional charset (password if ENCRYPT)
					charset = StringConstant;
					if (!isNext(','))	return false;
				}
				// Fall through to get the string.
			default:
				if (!getStringArg())	return false;	// Get the required source string
				src = StringConstant;
				if (!isNext(')'))		return false;	// Function must end with ')'
		}

		String dest = null;
		try {
			switch (encoding) {
				case ENCODING_ENCRYPT:
					dest = (mode == ENCODE) ? encrypt(src, charset, isText)
											: decrypt(src, charset, isText);
					break;
				case ENCODING_BASE64:
					dest = (mode == ENCODE) ? encodeBase64(src, charset)
											: decodeBase64(src, charset);
					break;
				case ENCODING_URL:
					dest = (mode == ENCODE) ? URLEncoder.encode(src, charset)
											: URLDecoder.decode(src, charset);
					break;
				default:
					dest = (mode == ENCODE) ? encode(src, charset)
											: decode(src, charset);
					break;
			}
		} catch (Exception e) {
			dest = "";
			if (encoding == ENCODING_ENCRYPT) {
				encryptionException(mode, e);
			} else {
				// UnsupportedEncodingException, IllegalCharsetNameException, or UnsupportedCharsetException
				writeErrorMsg(charset + " is not a valid encoding on this device.");
			}
		}
		if (dest == null)				return false;
		StringConstant = dest;
		return true;
	}

	private void encryptionException(boolean mode, Exception e) {
		writeErrorMsg((mode == ENCODE)
			? "Encryption error (" + e.getMessage() + ")"
			: "Decryption error (" + e.getMessage() + "). Check password.");
	}

	/**
	 * Encrypt a String and write the result to another String.
	 * EITHER encrypt a String (usually UTF-16 but decode as UTF-8) and re-encode to Base64
	 * OR encrypt byte data in an ISO-8859-1 encoded String and re-encode to ISO-8859-1.
	 *
	 * @param src The String to encode
	 * @param pw The encryption password
	 * @param isText A boolean: use STR_TEXT (true) for Unicode or STR_BINARY (false) for buffer (or ASCII)
	 * @return The encrypted and re-encoded String
	 * @throws Exception if decoding, encrypting, or re-encoding fails
	 */
	@TargetApi(Build.VERSION_CODES.FROYO)
	private String encrypt(String src, String pw, boolean isText) throws Exception {
		Cipher ecipher = new Basic.Encryption(Cipher.ENCRYPT_MODE, pw).cipher();
		byte[] in = src.getBytes(isText ? "UTF-8" : "ISO-8859-1");	// extract bytes from encoded string
		byte[] enc = ecipher.doFinal(in);							// encrypt

		// To support FroYo, there is a copy of Android's Base64.java in our build files.
		String dest = isText
			? Base64.encodeToString(enc, Base64.NO_WRAP).trim()		// encode text bytes to Base64...
			: new String(enc, "ISO-8859-1");						// ... or binary bytes to ISO-8859-1
		return dest;
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private String decrypt(String src, String pw, boolean isText) throws Exception {
		Cipher dcipher = new Basic.Encryption(Cipher.DECRYPT_MODE, pw).cipher();
		// To support FroYo, there is a copy of Android's Base64.java in our build files.
		byte[] dec = (isText)
			? Base64.decode(src, Base64.DEFAULT)					// decode Base64...
			: src.getBytes("ISO-8859-1");							// ... or ISO-8859-1 to get bytes
		byte[] dest = dcipher.doFinal(dec);							// decrypt
		return new String(dest, (isText) ? "UTF-8" : "ISO-8859-1");	// encode bytes into string
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private String encodeBase64(String src, String charset) throws UnsupportedEncodingException {
		byte[] enc = src.getBytes(charset);
		// To support FroYo, there is a copy of Android's Base64.java in our build files.
		return Base64.encodeToString(enc, Base64.NO_WRAP);
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private String decodeBase64(String src, String charset) throws UnsupportedEncodingException {
		// To support FroYo, there is a copy of Android's Base64.java in our build files.
		byte[] dec = Base64.decode(src, Base64.DEFAULT);
		return new String(dec, charset);
	}

	private String encode(String src, String charset) throws UnsupportedEncodingException {
		byte[] enc = src.getBytes(charset);
		return new String(enc, "ISO-8859-1");
	}

	private String decode(String src, String charset) throws UnsupportedEncodingException {
		byte[] dec = src.getBytes("ISO-8859-1");		// any char > 00FF maps to byte 3F
		return new String(dec, charset);
	}

	private boolean executeSF_FORMAT() {												// FORMAT$
		if (!getStringArg())			return false;	// get the pattern string
		String str = StringConstant;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;	// Get the number to format
		if (!isNext(')'))				return false;	// Function must end with ')'

		return Format(str, EvalNumericExpressionValue);	// and then do the format
	}

	private boolean executeSF_USING() {
		Locale locale;
		if (isNext(',')) { StringConstant = ""; }		// force default Locale
		else {
			if (!getStringArg())		return false;	// get user-specified Locale
			if (!isNext(','))			return false;
		}
		locale = parseLocale(StringConstant);
		if (locale == null) { return RunTimeError("Unknown locale " + StringConstant); }

		if (!getStringArg())			return false;	// get format string
		String fmt = StringConstant;

		Object[] args = getUsingArgs();					// get data to format
		if (args == null)				return false;	// error getting args
		if (!isNext(')'))				return false;	// Function must end with ')'

		try {
			StringConstant = String.format(locale, fmt, args);
		} catch (Exception e) {
			return RunTimeError("Cannot complete FPRINT\n", e);
		}

		return true;
	}

	private boolean executeSF_CHR() {													// CHR$
		StringBuilder sb = new StringBuilder();
		do {
			if (!evalNumericExpression()) return false;
			sb.append((char)EvalNumericExpressionValue.intValue());
		} while (isNext(','));
		if (!isNext(')'))				return false;	// Function must end with ')'
		StringConstant = sb.toString();
		return true;
	}

	private boolean executeSF_VERSION() {												// VERSION$
		if (!isNext(')'))				return false;	// Function must end with ')'
		StringConstant = getString(R.string.version);
		return true;
	}

	private boolean executeSF_GETERROR() {												// GETERROR$
		if (!isNext(')'))				return false;	// Function must end with ')'
		StringConstant = (mErrorMsg != null) ? mErrorMsg : "unknown";
		return true;
	}

	private boolean executeSF_REPLACE() {												// REPLACE$
		if (!getStringArg())			return false;
		String target = StringConstant;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;
		String argument = StringConstant;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;
		String replacment = StringConstant;
		if (!isNext(')'))				return false;	// Function must end with ')'

		if (argument == null || replacment == null) {
			return RunTimeError("Invalid string");
		}

		StringConstant = target.replace(argument, replacment);
		return true;
	}

	private boolean executeSF_INT() {													// INT$
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		long val = EvalNumericExpressionValue.longValue();
		StringConstant = Long.toString(val);
		return true;
	}

	private boolean executeSF_HEX() {													// HEX$
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		long val = EvalNumericExpressionValue.longValue();
		StringConstant = Long.toHexString(val);
		return true;
	}

	private boolean executeSF_OCT() {													// OCT$
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		long val = EvalNumericExpressionValue.longValue();
		StringConstant = Long.toOctalString(val);
		return true;
	}

	private boolean executeSF_BIN() {													// BIN$
		if (!evalNumericExpression())	return false;
		if (!isNext(')'))				return false;	// Function must end with ')'
		long val = EvalNumericExpressionValue.longValue();
		StringConstant = Long.toBinaryString(val);
		return true;
	}

	private boolean parseTimeArgs(Time time) {						// Convert time parameters to Time object fields
		int year, month, day, hour, minute, second;					// Requires six parameters,
		if (!getArgAsNum())					return false;			// either numeric or string containing a number
		year = EvalNumericExpressionValue.intValue();				// Year$
		if (!isNext(',') || !getArgAsNum())	return false;
		month = EvalNumericExpressionValue.intValue() - 1;			// Month$ (convert to 0-index)
		if (!isNext(',') || !getArgAsNum())	return false;
		day = EvalNumericExpressionValue.intValue();				// Day$
		if (!isNext(',') || !getArgAsNum())	return false;
		hour = EvalNumericExpressionValue.intValue();				// Hour$
		if (!isNext(',') || !getArgAsNum())	return false;
		minute = EvalNumericExpressionValue.intValue();				// Minute$
		if (!isNext(',') || !getArgAsNum())	return false;
		second = EvalNumericExpressionValue.intValue();				// Second$
		time.set(second, minute, hour, day, month, year);
		return true;
	}

	private Locale parseLocale(String localeStr) {
		if ((localeStr == null) || (localeStr.length() == 0)) { return Locale.getDefault(); }
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
			} else
			if (getStringArg()) {
				args.add(StringConstant);			// field is string
			}
			if (SyntaxError) return null;
		}
		return args.toArray();
	}

	private boolean Format(String Fstring, double Fvalue) {			// Format a number for output
																			// Do the heart of the FORMAT$ function
		BigDecimal B = BigDecimal.valueOf(0.0);
		try { B = BigDecimal.valueOf(Math.abs(Fvalue)); }
		catch (Exception e) { return RunTimeError(e); }
		String Vstring = B.toPlainString();									// and convert the big decimal to a string

		String FWstring = "";		// Will hold the whole number part of the pattern (format) string
		String FDstring = "";		// Will hold the decimal part of the pattern string
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
		for (i = 0; i < Fstring.length(); ++i) {
			if (Fstring.charAt(i)== '.') {
				if (FhasDecimal)			return false;					// if more than one decimal, error
				FhasDecimal = true;
				FdecimalIndex = i;
			}
		}
		if (!FhasDecimal) {													// If no decimal in pattern
			FdecimalIndex = i + 1;											// set decimal index past end of pattern
		} else {															// else move index past the decimal
			++FdecimalIndex;
		}
																			// Split the pattern string
		FWstring = Fstring.substring(0, FdecimalIndex-1);									// FW is whole number part (includes decimal)
		if (FhasDecimal) {FDstring = Fstring.substring(FdecimalIndex, Fstring.length());}	// FD is decimal string

		for (i = 0; i < FDstring.length(); ++i) {							// insure FD only has # chars
			if (FDstring.charAt(i) != '#')	return false;
		}

		for (i=0; i< Vstring.length(); ++i) {								// Scan the number string for its decimal index
			if (Vstring.charAt(i)== '.') {
				if (VhasDecimal)			return false;					// more than one decimal should never happen
				VhasDecimal = true;
				VdecimalIndex = i;											// Set the value decimal index
			}
		}
		if (!VhasDecimal) {													// If value has not decimal (should never happen)
			VdecimalIndex = i + 1;
		} else {
			++VdecimalIndex;												// move the decimal index past the decimal
		}
																			// Split the value string
		VWstring = Vstring.substring(0, VdecimalIndex-1);										// VW is whole number part (includes decimal)
		if (VhasDecimal) { VDstring = Vstring.substring(VdecimalIndex, Vstring.length()); }		// VD is the decimal part

																			// Build the decimal part of the output string
		Temp = "";
		if (FDstring.length() > 0) { Temp = "."; }							// If any pattern for decimal, output the decimal

		for (i = 0; i < FDstring.length(); ++i) {							// Copy Decimal digits to output as long as there
			if (FDstring.charAt(i) != '#')	return false;					// are pattern # chars. If run out of digits before
			if (i < VDstring.length()) {									// pattern digits, output 0 characters
				Temp += VDstring.charAt(i);
			} else {
				Temp += "0";
			}
		}
		VDstring = Temp;													// Save the Decimal Value output string

		Temp = "";
		int FWI = FWstring.length();
		int VWI = VWstring.length();
																			// Now work on the floating pattern

		String FloatChar = " ";												// Initialize float charcter to none
		if ((FWI > 0) &&													// If there is a float character
			(FWstring.charAt(0) != '#') &&
			(FWstring.charAt(0) != '%')) {
			FloatChar = Character.toString(FWstring.charAt(0));				// then move it to FloatChar
			FWstring = FWstring.substring(1,FWI);
			--FWI;
		}

		String Header = "";													// Initialize Header to empty
		Header += FloatChar;												// add the float char to the header
		Header += (Fvalue < 0) ? "-" : " ";									// if value < 0 output minus sign else space
																			// note: reverse order will be unreversed later

																	// Now work on the whole number par
		StringConstant = "";
		if ((FWI == 0) && (VWI > 0)) {								// No Whole format characters and Whole Value characters remain
			if (VWstring.charAt(0) != '0') {						// and the Whole character is not '0'
				StringConstant = "*" + VDstring;					// then show error
			} else {												// No whole number format chars and not whole number digits
				StringConstant += (Fvalue < 0) ? "-" : " ";			// If the decimal digits are < 0 output minus else blank
				StringConstant += FloatChar + VDstring;				// Build result for decimal digits with no whole number
			}
			return true;											// done
		}

																	// We have whole format chars and whole value digits
		--FWI;
		--VWI;
		boolean blanks = true;
		while (FWI >= 0) {															// While there are format characters
			c = FWstring.charAt(FWI);												// get the format character
			switch (c) {

			case '#':																// format charcter = #
				blanks = true;
				if (VWI >= 0) {
					if ((VWI == 0) && (VWstring.charAt(VWI) == '0')) { Temp += " ";}// if there are no more digits, output space
					else { Temp += Character.toString(VWstring.charAt(VWI)); }		// else output the digit
					--VWI;															// go to the next digit
				} else if (VWI == -1) {												// No digits left, if we just ran out, output the header
					Temp += Header + " ";											// output the header
					--VWI;
				} else {
					Temp += " ";													// output space
				}
				break;

			case '%':																// format charcter = %
				blanks = false;
				if (VWI >= 0) {
					Temp += Character.toString(VWstring.charAt(VWI));				// if more digits, output it
					--VWI;
				} else {
					Temp += "0";													// else output 0
				}
				break;

			default:																// format character not # or %
				if (blanks) {														// if doing blanks
					if      (VWI >= 0)	{ Temp += c; }								// if more digits, output char
					else if (VWI == -1)	{ Temp += Header + " "; --VWI; }			// if just now ran out, output header and blank
					else				{ Temp += " "; }							// else just a blank
				} else {
					Temp += c;														// not blanks, output the char
				}
			}
			--FWI;
		}

		if (VWI == -1) { Temp += Header; }							// add the header to the end of the whole number

		Temp = new StringBuilder(Temp).reverse().toString();		// now reverse the whole thing

		StringConstant = Temp + VDstring;
		if (VWI >= 0) {												// If value decimal digits remain
			StringConstant = "**" + StringConstant;					// show the error in place of the header
		}

		return true;												// and we are done
	}

	// *********************************** end String Functions ***********************************

	// ************************************* array utilities **************************************

	private boolean BuildBasicArray(Var var, int[] DimList) {		// build an arbitrary array with initialized data
		Var.ArrayVar arrayVar = (Var.ArrayVar)var;
		Var.ArrayDef array;
		try { array = Var.ArrayDef.create(var.isNumeric(), DimList); }// create a new array defintion
		catch (InvalidParameterException ex) { return RunTimeError("DIMs must be >= 1 at"); }
		array.createArray();										// add an empty array of the proper size and type

		arrayVar.arrayDef(array);									// add the definition to the variable
		if (var.isNew()) { insertNewVar(arrayVar); }				// put the variable in the symbol table if not already there
		return true;
	}

	private boolean BuildBasicArray(Var var, ArrayList<Integer> DimList) { // like previous method
		int[] dims = new int[DimList.size()];						// but convert ArrayList<Integer> to int[]
		int idx = 0;
		for (int dim : DimList) { dims[idx++] = dim; }
		return BuildBasicArray(var, dims);
	}

	private boolean BuildBasicArray(Var var, int length) {			// build a one-dimensional array with initialized data
		int[] dimValues = new int[] { length };						// list of dimensions has only one element
		return (BuildBasicArray(var, dimValues));					// go build an array of the proper size
	}

	private boolean ListToBasicNumericArray(Var var, List<Double> Values, int length) {
		if (!BuildBasicArray(var, length)) return false;			// go build an array of the proper size and type
		double[] array = var.arrayDef().getNumArray();				// Caution! Raw array access!
		int i = 0;
		for (double d : Values) { array[i++] = d; }					// stuff the array
		return true;
	}

	private boolean ListToBasicStringArray(Var var, List<String> Values, int length) {
		if (!BuildBasicArray(var, length)) return false;			// go build an array of the proper size and type
		String[] array = var.arrayDef().getStrArray();				// Caution! Raw array access!
		int i = 0;
		for (String s : Values) { array[i++] = s; }					// stuff the array
		return true;
	}

	private Var.Val GetArrayValue(Var.ArrayVar var) {	// index an array, return the value in a Val
		ArrayList<Integer> indexList = new ArrayList<Integer>();
		if (!isNext(']')) {								// parse out the index values for this call
			do {
				if (!evalNumericExpression()) return null;
				indexList.add(EvalNumericExpressionValue.intValue());
			} while (isNext(','));
			if (!isNext(']')) return null;
		}
		try { return var.val(indexList); }
		catch (InvalidParameterException ex) {
			RunTimeError(ex.getMessage());
			return null;
		}
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

	private boolean getArraySegment(Var.ArrayDef array, Integer[] pair) { // normalize array segment
								// pair in is 1-based [start, length], out is 1-based [base, length]
		if (array == null) { return RunTimeError("Array does not exist"); }
		int length = array.length();								// get the array length
		int base = 0;												// array always starts at index 0
		int max = length;

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

	// *********************************** end array utilities ************************************

	// ************************************* Command Methods **************************************

	private boolean executeImplicitCommand() {			// no keyword at beginning of line
		int LI = LineIndex;
		Var var = parseVar(USER_FN_OK);						// look for a variable or function name
		if (var == null) return false;

		LineIndex = LI;										// rewind the LineIndex for re-parse
		Command cmd = var.isFunction() ? CMD_CALL : CMD_IMPL_LET; // implicit CALL or LET (no preinc/dec)
		ExecutingLineBuffer.cmd(cmd, 0);					// remember for next time the line is executed
		return cmd.run();
	}

	private double incdec() {
		double result = 0.0;
		if      (ExecutingLineBuffer.startsWith(OP_INC, LineIndex)) { LineIndex += 2; result = 1.0; }
		else if (ExecutingLineBuffer.startsWith(OP_DEC, LineIndex)) { LineIndex += 2; result = -1.0; }
		return result;
	}

	private boolean executeLET() {						// Execute LET (an assignment statement)
		/*
		 * This is the entry point for use when the command keyword "LET" is present.
		 */
		double preVal = incdec();							// check for pre-increment/decrement
		// Remember the result so, if the line is executed again,
		// it will go directly to the executeLET(double) with the right pre-inc/dec val.
		Command cmd = (preVal == 0) ? CMD_IMPL_LET : ((preVal > 0.0) ? CMD_PREINC : CMD_PREDEC);
		int length = CMD_LET.name.length() + cmd.name.length();
		ExecutingLineBuffer.cmd(cmd, length);
		return executeLET(preVal);
	}

	private boolean executeLET(double nval) {			// Execute LET (an assignment statement)
		/*
		 * This is the execution function for commands BKW_LET, BKW_PREDEC, and BKW_PREINC,
		 * and it is also called from executeImplicitCommand() for implied LET commands.
		 * Pre-decrement and pre-increment with no LET are treated as explicit
		 * BKW_PREDEC and BKW_PREINC commands.
		 */
		boolean islval = (nval == 0.0);						// assignable only if no inc/dec

		if (!getVar())					return false;		// get or create the variable to assign a value to
		Var.Val val = mVal;									// variable's value

		boolean varIsNumeric = val.isNumeric();
		if (varIsNumeric) {
			double postincdec = incdec();					// check for post-increment/decrement
			if (postincdec != 0) { nval += postincdec; islval = false; }
		} else if (!islval)				return false;		// can't inc/dec a string

		if (isEOL()) {										// no more to parse, may have created variable
			if (varIsNumeric && (nval != 0.0)) {			// pre/post inc/dec of numeric variable
				val.addval(nval);
				return true;
			}
			return false;									// else (e.g., bare variable name) syntax error
		}
		else if (!islval)				return false;		// can't be label or assignment if already inc/dec
		else if (isNext(':')) {
			ExecutingLineBuffer.cmd(CMD_LABEL, LineIndex);	// it's a label
			return checkEOL();								// must end line
		}

		// Implementation note: this should probably be put in a Java enum type. (TODO)
		int op = ASSIGN;
		String sval = "";
		if (!isNext('=')) {									// require some kind of assignment operator
			// NOTE: The lvalue before assignment is the ORIGINAL value of the variable,
			// not the value as modified by and '++' or '--' in the rvalue expression.
			if (varIsNumeric) { nval += val.nval(); }
			else              { sval  = val.sval(); }
			if (ExecutingLineBuffer.startsWith("+=", LineIndex))			{ op = PLUS; }
			else if (varIsNumeric) {
				if (ExecutingLineBuffer.startsWith("-=", LineIndex))		{ op = MINUS; }
				else if (ExecutingLineBuffer.startsWith("*=", LineIndex))	{ op = MUL; }
				else if (ExecutingLineBuffer.startsWith("/=", LineIndex))	{ op = DIV; }
				else if (ExecutingLineBuffer.startsWith("^=", LineIndex))	{ op = EXP; }
				else if (ExecutingLineBuffer.startsWith("&=", LineIndex))	{ op = AND; }
				else if (ExecutingLineBuffer.startsWith("|=", LineIndex))	{ op = OR; }
				else					return false;
			} else						return false;
			LineIndex += 2;
		}

		if (varIsNumeric) {									// if variable is numeric then
			if (!evalNumericExpression()) return false;		// evaluate following numeric expression
			if (op != ASSIGN) {
				double rval = EvalNumericExpressionValue;
				switch (op) {								// apply the required operation
					case PLUS:  nval += rval; break;
					case MINUS: nval -= rval; break;
					case MUL:   nval *= rval; break;
					case DIV:   nval /= rval; break;
					case EXP:   nval = Math.pow(nval, rval); break;
					case AND:   nval = ((nval != 0) && (rval != 0)) ? 1.0 : 0.0;
					case OR:    nval = ((nval != 0) || (rval != 0)) ? 1.0 : 0.0;
				}
				EvalNumericExpressionValue = nval;
			}
			val.val(EvalNumericExpressionValue);			// assign result to the numeric var
		} else {											// var is string
			if (!getStringArg())		return false;		// evaluate the string expression
			if (op == PLUS) { StringConstant = sval + StringConstant; }
			val.val(StringConstant);						// assign result to the string var
		}
		return checkEOL();
	} // executeLET

	private boolean executeDIM() {
		do {									// Multiple Arrays can be DIMed in one DIM statement separated by commas
			Var var = getVarAndType();									// get the array variable name
			if ((var == null) || !var.isArray()){ return RunTimeError(EXPECT_ARRAY_VAR); }
			if (isNext(']'))					return false;			// must have dimension(s)

			ArrayList<Integer> dimValues = new ArrayList<Integer>();	// a list to hold the array dimension values
			do {														// get each index value
				if (!evalNumericExpression())	return false;
				dimValues.add(EvalNumericExpressionValue.intValue());	// and add it to the list
			} while (isNext(','));
			if (!isNext(']'))					return false;			// must have closing bracket

			if (!BuildBasicArray(var, dimValues)) return false;			// no error, build the array

		} while (isNext(','));											// continue while there are arrays to be DIMed
		return checkEOL();												// then done
	}

	private boolean executeUNDIM() {
		mVal = null;	// clear mVal just in case it's pointing at a big array that's about to be UNDIMed
		do {															// Multiple arrays can be UNDIMed in one statement
			Var var = getVarAndType();
			if ((var == null) || !var.isArray()){ return RunTimeError(EXPECT_ARRAY_VAR); }
			if (!isNext(']'))					{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
			if (!var.isNew()) {											// if DIMed, UNDIM it
				var.arrayDef().invalidate();	// mark the array invalid in case any other variable is looking at it
				searchVar(var);					// delete the variable so it can't be used to access this array any more
			}
		} while (isNext(','));											// continue while there are arrays to be UNDIMed

		return checkEOL();
	}

	private boolean executePRINT() {
		if (!buildPrintLine(PrintLine, "")) return false;	// build up the print line in StringConstant
		if (!PrintLineReady) {							// flag set by buildPrintLine
			PrintLine = StringConstant;					// not ready to print; hold line
			return true;								// and wait for next Print command
		}
		PrintLine = "";									// clear the accumulated print line
		PrintShow(StringConstant);						// output the line
		return true;
	}

	// Convert the fields of a print command into a String for printing.
	// The line param can hold an existing String to add the new String to.
	// If the line ends with a semicolon set PrintLineReady false,
	// else add the newline param to the String and set PrintLineReady true.
	// If the String is valid, put it in StringConstant and return true,
	// else return false to signal a syntax error.
	private boolean buildPrintLine(String text, String newline) {
		StringBuilder printLine = new StringBuilder((text == null) ? "" : text);
		char sep = ' ';
		do {										// do each field in the print statement
			char c = ExecutingLineBuffer.text().charAt(LineIndex);
			if (c == '\n') {
				break;								// done processing the line
			}
													// not EOL: expression is required
			if (evalNumericExpression()) {
				printLine.append(EvalNumericExpressionValue);	// convert to string
			} else
			if (!SyntaxError && evalStringExpression()) {
				printLine.append(StringConstant);				// field is string
			} else {
				if (!SyntaxError) checkEOL();		// report junk at EOL unless prior error
				return false;
			}
			if (SyntaxError) { return false; }

			sep = ExecutingLineBuffer.text().charAt(LineIndex);	// get possible separator
			if (sep == ',') {						// if the separator is a comma
				printLine.append(sep).append(' ');	// add comma + blank to the line
				++LineIndex;
			} else if (sep == ';') {				// if separator is semicolon
				++LineIndex;						// don't add anything to output
			}
		} while (true);								// Exit loop happens internal to loop

		PrintLineReady = (sep != ';');
		if (PrintLineReady) {						// if not ended in semicolon
			printLine.append(newline);				// add newline character(s)
		}
		StringConstant = printLine.toString();
		return true;
	}

	private boolean executeEND() {

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

		Stop = true;								// ALWAYS stop
		return ok;
	}

	private boolean getLabelLineNum() {
		int li = LineIndex;
		String label = getWord(ExecutingLineBuffer.text(), LineIndex, "");	// get label name
		int len = label.length();
		LineIndex += len;												// move LineIndex for isEOL()
		if (isEOL()) {					// If EOL, this is a simple "GOTO/GOSUB label" statement.
			if (len == 0) return false;									// no label and no expression: error
		} else {						// Otherwise it is a "GOTO/GOSUB index, label_list..." statement.
			int comma = ExecutingLineBuffer.text().indexOf(',', LineIndex);
			if (comma < 0) return false;	// if no comma, there is no expression list, so syntax error

			LineIndex = li;
			if (!evalNumericExpression() || !isNext(',')) return false;
			int index = (int)(EvalNumericExpressionValue + 0.5);		// round index

			ArrayList<String> labels = new ArrayList<String>();			// build label list
			do {
				if (isEOL()) return false;								// don't end with comma
				label = getWord(ExecutingLineBuffer.text(), LineIndex, "");
				labels.add(label);
				LineIndex += label.length();
			} while(isNext(','));
			if (!isEOL()) return false;

			if ((index < 1) || (index > labels.size())) return true;	// no target, fall through
			label = labels.get(index - 1);
			len = label.length();
			if (len == 0) return true;									// no target, fall through
		}
		Integer lineRef = Labels.get(label);
		if (lineRef == null) return RunTimeError("Undefined Label \"" + label + "\" at:");
		ExecutingLineIndex = lineRef.intValue();
		return true;
	}

	private boolean executeGOTO() {
		// Look for severe block boundary crossing abuse: GOTO out of IF or FOR.
		int maxStack = 50000;						// 50,000 should be enough

		if ((IfElseStack.size() > maxStack) || (ForNextStack.size() > maxStack)) {
			return RunTimeError("Stack overflow. See manual about use of GOTO.");
		}

		return getLabelLineNum();
	}

	private boolean executeGOSUB() {
		int returnAddress = ExecutingLineIndex;
		if (!getLabelLineNum())			return false;
		if (ExecutingLineIndex != returnAddress) {
			GosubStack.push(returnAddress);			// found a valid gosub address, expect a return
		}
		return true;
	}

	private boolean executeRETURN() {
		if (!checkEOL())				return false;
		if (GosubStack.empty()) {
			return RunTimeError("RETURN without GOSUB");
		}
		ExecutingLineIndex = GosubStack.pop();
		return true;
	}

	private boolean executeIF() {

		if (!IfElseStack.empty()) {											// if inside of an IF block
			Integer q = IfElseStack.peek();
			if ((q != IEexec) && (q != IEinterrupt)) {						// and not executing
				int index = ExecutingLineBuffer.text().indexOf("then");
				if (index < 0) {											// if there is no THEN
					IfElseStack.push(IEskip2);								// need to skip to this if's end
				} else {
					LineIndex = index + 4;									// skip over the THEN
					if (isNext('\n')) {										// if not single line if
						IfElseStack.push(IEskip2);							// need to skip to this if's end
					}														// else is single line, no skip needed
				}
				return true;
			}
		}
		// Execute this IF

		String kw = "then";
		if (!evalToPossibleKeyword(kw)) { return false; }		// tell getVar that THEN is expected
		boolean condition = (EvalNumericExpressionValue != 0);

		if (ExecutingLineBuffer.text().charAt(LineIndex) != '\n') {
			if (!ExecutingLineBuffer.startsWith(kw, LineIndex)) { checkEOL(); return false; }
			LineIndex += 4;

			if (!isNext('\n')) { return SingleLineIf(condition); }			// assume single-line IF

			// at this point: "IF condition THEN\n" and LineIndex is after '\n'
		}

		IfElseStack.push((condition) ? IEexec : IEskip1);

		return true;
	}

	private boolean SingleLineIf(boolean condition) {
		int index = ExecutingLineBuffer.text().lastIndexOf("else");

		// At present, can't use the same ProgramLine when calling StatementExecuter recursively.
		// We create a new, temporary ProgramLine, with its mCommand null, every time.
		if (condition) {
			String text = ExecutingLineBuffer.text();
			if (index > 0) {
				text = text.substring(0, index) + "\n";		// clip off the "else" clause
			}
			ExecutingLineBuffer = new ProgramLine(text);
			return StatementExecuter();
		}

		if (index > 0) {
			LineIndex = index + 4;
			ExecutingLineBuffer = new ProgramLine(ExecutingLineBuffer.text());
			return StatementExecuter();
		}
		return true;
	}

	private boolean executeELSE() {

		if (IfElseStack.empty()) {			// prior IF or ELSEIF should have put something on the stack
			RunTimeError("Misplaced ELSE");
			return false;
		}
		if (!checkEOL() ) return false;

		Integer q = IfElseStack.pop();
		IfElseStack.push((q == IEexec) ? IEskip2 : IEexec);

		return true;
	}

	private boolean executeELSEIF() {

		if (IfElseStack.empty()) {			// prior IF or ELSEIF should have put something on the stack
			RunTimeError("Misplaced ELSEIF");
			return false;
		}

		Integer q = IfElseStack.pop();

		if (q == IEexec) {
			IfElseStack.push(IEskip2);
		} else {
			String kw = "then";
			if (!evalToPossibleKeyword(kw)) { return false; }	// tell getVar that THEN is expected
			boolean condition = (EvalNumericExpressionValue != 0);

			if (ExecutingLineBuffer.startsWith(kw, LineIndex)) { LineIndex += 4; }
			if (!checkEOL()) { return false; }

			IfElseStack.push((condition) ? IEexec : IEskip1);
		}
		return true;
	}

	private boolean executeENDIF() {

		if (IfElseStack.empty()) {			// Something must be on the stack
			return RunTimeError("Misplaced ENDIF");
		}
		if (!checkEOL()) return false;
		IfElseStack.pop();					// but we do not care what it is
		return true;

	}

	private boolean skipTo(Command target, Command nest, String errMsg) {
		int lineNum;
		int limit = Basic.lines.size();
		for (lineNum = ExecutingLineIndex + 1; lineNum < limit; ++lineNum) {
			ProgramLine line = Basic.lines.get(lineNum);
			Command c = line.cmd();
			if (c == null) {
				if (line.startsWith(target.name)) {
					line.cmd(target);						// found the target
					c = target;
				} else if (line.startsWith(nest.name)) {
					line.cmd(nest);							// found nested block of same type
					c = nest;
				} else continue;							// next line
			}
			if (c == target) {
				ExecutingLineIndex = lineNum;				// found the target
				ExecutingLineBuffer = line;
				LineIndex = line.offset();
				return true;
			}
			if (c == nest) {
				ExecutingLineIndex = lineNum;				// found nested block of same type
				if (!skipTo(target, nest, errMsg)) return false;	// recursively seek its end
				lineNum = ExecutingLineIndex;
			}
		}
		return RunTimeError(errMsg);						// end of program, target not found
	}

	private boolean executeFOR() {

		int fline = ExecutingLineIndex;						// Loop return location
		if (!getNVar()) return false;
		Var.Val indexVal = mVal;							// For Var

		if (!isNext('=')) return false;						// For Var =

		String kw = "to";
		if (!evalToPossibleKeyword(kw)) return false;		// tell getVar that TO is expected
		double fstart = EvalNumericExpressionValue;

		if (!ExecutingLineBuffer.startsWith(kw, LineIndex)) return false;
		LineIndex += 2;

		kw = "step";
		if (!evalToPossibleKeyword(kw)) { return false; }	// For Var = <exp> to <exp>
		double flimit = EvalNumericExpressionValue;

		double fstep = 1.0;									// For Var = <exp> to <exp> <default step>
		if (ExecutingLineBuffer.startsWith(kw, LineIndex)) {
			LineIndex += 4;
			if (!evalNumericExpression()) { return false; }	// For Var = <exp> to <exp> step <exp>
			fstep = EvalNumericExpressionValue;
		}

		if (!checkEOL()) return false;

		indexVal.val(fstart);								// assign start <exp> to Var
		ForNext desc =										// An object to hold values for stack
				new ForNext(fline, indexVal, fstep, flimit);

		if (fstep > 0) {									// Test the initial condition
			if (fstart > flimit) { return SkipToNext(); }	// If exceeds limit then skip to NEXT
		} else {
			if (fstart < flimit) { return SkipToNext(); }
		}
		ForNextStack.push(desc);

		return true;
	}

	private boolean SkipToNext() {
		return skipTo(CMD_NEXT, CMD_FOR, "FOR without NEXT");
	}

	private boolean executeF_N_CONTINUE() {
		if (ForNextStack.empty()) {								// If the stack is empty
			return RunTimeError("No For Loop Active");			// then we have a misplaced CONTINUE
		}
		if (!checkEOL()) return false;

		if (!SkipToNext()) return false;
		doNext();
		return true;
	}

	private boolean executeF_N_BREAK() {
		if (ForNextStack.empty()) {								// If the stack is empty
			return RunTimeError("No For Loop Active");			// then we have a misplaced BREAK
		}
		if (!checkEOL()) return false;

		if (!SkipToNext()) return false;
		ForNextStack.pop();
		return true;
	}

	private boolean executeNEXT() {
		if (ForNextStack.empty()) {								// If the stack is empty
			return RunTimeError("NEXT without FOR");			// then we have a misplaced NEXT
		}
		return doNext();
	}

	private boolean doNext() {
		ForNext desc = ForNextStack.peek();						// peek at the descriptor
		if (desc.doStep()) {									// update the loop index
			ForNextStack.pop();									// done: pop the stack
		} else {
			ExecutingLineIndex = desc.line();					// not done: repeat the loop
		}
		return true;
	}

	private boolean executeWHILE() {
		int LI = LineIndex;
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		if (EvalNumericExpressionValue != 0.0) {				// true: push line number and index onto while stack
			WhileStack.push(new WhileRepeat(ExecutingLineIndex, LI));
			return true;
		}
		return SkipToRepeat();									// false: find the REPEAT for the WHILE
	}

	private boolean SkipToRepeat() {
		return skipTo(CMD_REPEAT, CMD_WHILE, "WHILE without REPEAT");
	}

	private boolean executeW_R_CONTINUE() {
		if (WhileStack.empty()) {								// If the stack is empty
			return RunTimeError("No While Loop Active");		// then we have a misplaced CONTINUE
		}
		if (!checkEOL()) return false;

		int saveLine = ExecutingLineIndex;						// save current line number
		if (!doRepeat()) return false;							// re-execute the WHILE statement
		if (EvalNumericExpressionValue == 0.0) {
			ExecutingLineIndex = saveLine;						// false: skip to end of loop
			if (!SkipToRepeat()) return false;
			WhileStack.pop();									// and pop the stack
		}
		return true;
	}

	private boolean executeW_R_BREAK() {
		if (WhileStack.empty()) {								// If the stack is empty
			return RunTimeError("No While Loop Active");		// then we have a misplaced BREAK
		}
		if (!checkEOL()) return false;

		if (!SkipToRepeat()) return false;
		WhileStack.pop();
		return true;
	}

	private boolean executeREPEAT() {
		if (WhileStack.empty()) {								// If the stack is empty
			return RunTimeError("REPEAT without WHILE");		// then we have a misplaced REPEAT
		}
		if (!checkEOL()) return false;

		int repeatLine = ExecutingLineIndex;					// save current line number
		if (!doRepeat()) return false;							// re-execute the WHILE statement
		if (EvalNumericExpressionValue == 0.0) {
			WhileStack.pop();									// false: pop the stack
			ExecutingLineIndex = repeatLine;					// and exit loop
		}														// else re-execute loop
		return true;

	}

	private boolean doRepeat() {
		WhileRepeat line = WhileStack.peek();					// re-execute the WHILE statement
		ExecutingLineIndex = line.line();
		ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
		LineIndex = line.offset();
		return (evalNumericExpression());
	}

	private boolean executeDO() {
		if (!checkEOL()) return false;
		DoStack.push(ExecutingLineIndex);						// push line number onto DO stack.
		return true;
	}

	private boolean SkipToUntil() {
		return skipTo(CMD_UNTIL, CMD_DO, "DO without UNTIL");
	}

	private boolean executeD_U_CONTINUE() {
		if (DoStack.empty()) {									// If the stack is empty
			return RunTimeError("No DO loop active");			// then we have a misplaced CONTINUE
		}
		if (!checkEOL()) return false;

		if (!SkipToUntil()) return false;
		return doUntil();
	}

	private boolean executeD_U_BREAK() {
		if (DoStack.empty()) {									// If the stack is empty
			return RunTimeError("No DO loop active");			// then we have a misplaced BREAK
		}
		if (!checkEOL()) return false;

		if (!SkipToUntil()) return false;
		DoStack.pop();
		return true;
	}

	private boolean executeUNTIL() {
		if (DoStack.empty()) {									// If the stack is empty
			return RunTimeError("UNTIL without DO");			// then we have a misplaced UNTIL
		}
		return doUntil();
	}

	private boolean doUntil() {
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		if (EvalNumericExpressionValue == 0) {
			ExecutingLineIndex = DoStack.peek();				// false: go back to the DO line number
		} else {
			DoStack.pop();										// true: pop the stack
		}
		return true;
	}

	private boolean executeINPUT() {

		String prompt = "";
		boolean isComma = isNext(',');						// 1st comma
		if (!isComma) {
			if (!getStringArg())		return false;		// get user prompt / Dialog title
			prompt = StringConstant;
			isComma = isNext(',');							// 1st comma
		}
		if (!isComma || !getVar())		return false;		// 1st comma, variable for return value
		Var.Val varVal = mVal;
		boolean isNumeric = varVal.isNumeric();

		String inputDefault = "";
		Var.Val canceledVal = null;
		isComma = isNext(',');
		if (isComma) {										// 2nd comma, get input default
			isComma = isNext(',');							// 3rd comma
			if (!isComma) {
				if (isNumeric) {
					if (!evalNumericExpression()) return false;
					inputDefault = String.valueOf(EvalNumericExpressionValue);
					if (inputDefault.endsWith(".0")) {
						inputDefault = inputDefault.replace(".0", "");
					}
				} else {
					if (!getStringArg()) return false;
					inputDefault = StringConstant;
				}
				isComma = isNext(',');						// 3rd comma
			}
		}
		if (isComma) {										// 3rd comma
			if (!getNVar())				return false;
			canceledVal = mVal;
		}
		if (!checkEOL())				return false;

		DialogArgs args = new DialogArgs();
		args.mTitle = prompt;
		args.mInputDefault = inputDefault;
		args.mButton1 = "Ok";
		args.mReturnVal = varVal;
		mWaitForLock = true;
		mInputCancelled = false;
		sendMessage(MESSAGE_INPUT_DIALOG, args);			// signal UI to start the dialog

		waitForLOCK();										// wait for the user to exit the Dialog

		if (canceledVal != null) {							// use cancel var to report if canceled
			canceledVal.val(mInputCancelled ? 1.0 : 0.0);
		}
		if (mInputCancelled) {
			if (isNumeric) {								// if canceled, listener did not set value
				varVal.val(0.0);
			} else {
				varVal.val("");
			}

			if (canceledVal == null) {						// no cancel var, report cancel as error
				String err = "Input dialog cancelled";
				if (mOnErrorInt != null) {					// allow program to trap cancel as error
					mErrorMsg = err;
					ExecutingLineIndex = mOnErrorInt.line();
				} else {									// tell user we are stopping
					PrintShow(err, "Execution halted");
					Stop = true;							// and stop executing
				}
			}
		}
		return true;
	}

	// ************************************** Dialog Commands *************************************

	private boolean executeDIALOG() {						// Get Dialog command keyword if it is there
		return executeSubcommand(Dialog_cmd, "Dialog");
	}

	private boolean executeDIALOG_MESSAGE() {				// Show a Dialog with title, message, and 0 - 3 buttons
		String title = null;
		String msg = null;
		String[] btn = new String[3];

		if (!isNext(',')) {									// 1st comma
			if (!getStringArg())		return false;		// get Dialog title
			title = StringConstant;
			if (!isNext(','))			return false;		// 1st comma
		}
		if (!isNext(',')) {									// 2nd comma
			if (!getStringArg())		return false;		// get Dialog message
			msg = StringConstant;
			if (!isNext(','))			return false;		// 2nd comma
		}
		if (!getNVar())					return false;		// variable for returned button number
		Var.Val val = mVal;

		for (int i = 0; (i < 3) && isNext(','); ++i) {
			if (!getStringArg())		return false;
			btn[i] = StringConstant.trim();
			if (btn[i].length() == 0)	return false;		// do not allow empty string as button label
		}
		if (!checkEOL())				return false;

		DialogArgs args = new DialogArgs();
		args.mTitle = title;
		args.mMessage = msg;
		args.mButton1 = btn[0];
		args.mButton2 = btn[1];
		args.mButton3 = btn[2];

		mWaitForLock = true;
		sendMessage(MESSAGE_ALERT_DIALOG, args);			// signal UI to start the dialog

		waitForLOCK();										// wait for the user to exit the Dialog

		val.val(mAlertItemID);
		return true;
	}

	private boolean executeDIALOG_SELECT() {				// Show a Dialog with a selection list
		DialogArgs args = new DialogArgs();
		ArrayList<String> selectList = new ArrayList<String>();
		if (!parseSelect(args, selectList)) return false;

		String[] array = new String[selectList.size()];
		selectList.toArray(array);
		args.mList = array;

		mWaitForLock = true;
		sendMessage(MESSAGE_ALERT_DIALOG, args);			// signal UI to start the dialog

		waitForLOCK();										// wait for the user to exit the Dialog

		args.mReturnVal.val(mAlertItemID);
		return true;
	}

	// ************************************** Read Commands ***************************************

	private boolean executeREAD() {								// Get READ command keyword if it is there
		return executeSubcommand(read_cmd, "Read");				// and execute the command
	}

	// Parse and bundle the data list of a READ.DATA statement
	// Called from PreScan() and NOT from statementExecuter().
	private boolean executeREAD_DATA() {
		do {													// Sweep up the data values
			Var.Val val;
			if (GetStringConstant()) {							// If it is a string
				val = new Var.StrVal(StringConstant);			// create a Val object for it
			}
			else {												// Should be a number
				double signum = 1.0;							// Assume positive
				if (isNext('-')) { signum = -1.0; }				// Catch minus sign
				else if (isNext('+')) { ; }						// If not negative, eat optional '+'

				if (getNumber()) {								// If it is a number
					val = new Var.NumVal(signum * GetNumberValue); // create a Val object for it
				}
				else {											// else is a run time error
					return RunTimeError("Invalid Data Value");
				}
			}
			readData.add(val);									// add the bundle to the list
		} while (isNext(','));									// and do again if more data
		return true;
	}

	private boolean executeREAD_NEXT() {
		do {
			if (readNext >= readData.size()) {					// Insure there is more data to read
				return RunTimeError("No more data to read");
			}

			Var.Val readval = readData.get(readNext);			// get the data object
			++readNext;											// and increment to next object

			if (!getVar()) return false;						// get the variable
			Var.Val val = mVal;
			if (val.isNumeric()) {								// if var is numeric
				val.val(readval.nval());						// copy the numeric value to the variable
			} else {											// else var is string
				val.val(readval.sval());						// copy the string value to the variable
			}
		} while (isNext(','));									// loop while there are variables

		return checkEOL();
	}

	private boolean executeREAD_FROM() {
		if (!evalNumericExpression()) return false;
		int newIndex = EvalNumericExpressionValue.intValue() - 1;
		if (newIndex < 0 || newIndex >= readData.size()) {
			return RunTimeError("Index out of range");
		}
		readNext = newIndex;
		return checkEOL();
	}

	// ********************************** User-Defined Functions **********************************

	private boolean executeFN() {									// Get User-defined Function (FN) command keyword if it is there
		return executeSubcommand(fn_cmd, "FN");
	}

	private boolean  executeFN_DEF() {								// Define User-defined Function

		Var fnVar = getNewFNVar();									// get the function name and type
		if (fnVar == null)				return false;

		// Make a list of the parameters to put in the FunctionDefinition.
		ArrayList<Var.FunctionParameter> fParms = new ArrayList<Var.FunctionParameter>();
		if (!isNext(')')) {
			Set<String> uParms = new HashSet<String>();				// Keep a set of unique parameter names
			do {													// Get each of the parameter names
				Var var = parseVar(!USER_FN_OK);					// without adding any new vars to the symbol table
				if (var == null)		return false;
				if (var.isArray() && !isNext(']')) {				// Process array
					return RunTimeError(EXPECT_ARRAY_NO_INDEX);		// Array must not have any indices
				}
				var = var.copy();									// don't use Var from ParseVar directly
				if (!var.isArray()) { var.newVal(); }				// make sure Val of ScalarVar is not null

				fParms.add(new Var.FunctionParameter(var));
				uParms.add(var.name());
			} while (isNext(','));
			if ( !(isNext(')') && checkEOL()) ) return false;
			// If there are duplicate parameter names, the list of unique names will be
			// shorter than the list of parameters. (Bug fix provided by Nicolas Mougin.)
			if (uParms.size() < fParms.size()) { return RunTimeError("Duplicate parameter names at:"); }
		}

		Var.FnDef fnDef = new Var.FnDef(ExecutingLineIndex, fnVar, fParms);
		fnVar.fnDef(fnDef);											// add the definition to the Var
		if (mFunctionTable.put(fnVar.name(), fnDef) != null) {		// and the global Function Table
			return RunTimeError("Duplicate function name at:");
		}
		insertNewVar(fnVar);										// put the Var in the symbol table

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

	private boolean executeFN_RTN() {
		if (FunctionStack.empty()) {						// Insure RTN actually called from executing function
			return RunTimeError("misplaced fn.rtn");
		}

		CallStackFrame frame = FunctionStack.peek();		// Look at the top frame of the stack
		if (frame.fnDef().type().isNumeric()) {				// to determine if function is string or numeric
			if (!evalNumericExpression()) return false;
		} else {
			if (!evalStringExpression()) return false;
		}
		if (!checkEOL())				return false;

		return endUserFunction();
	}

	private boolean executeFN_END() {
		if (FunctionStack.empty()) {						// Insure END actually called from executing function
			return RunTimeError("misplaced fn.end");
		}

		CallStackFrame frame = FunctionStack.peek();		// Look at the top frame of the stack
		if (frame.fnDef().type().isNumeric()) {				// to determine if function is string or numeric
			EvalNumericExpressionValue = 0.0;				// Set default value
		} else {
			StringConstant = "";							// Set default value
		}
		if (!checkEOL())				return false;

		return endUserFunction();
	}

	private boolean endUserFunction() {
		FunctionStack.pop().restore();						// Function execution done. Restore stuff.
		mSymbolTables.pop();								// discard the function's symbol table
		mSymbolTable = mSymbolTables.peek();				// restore the caller's symbol table
		fnRTN = true;										// Signal RunLoop() to return
		return true;
	}

	private boolean executeCALL() {
		Var.FnDef fnDef = isUserFunction(false, false);					// don't check type
		return ((fnDef != null) && doUserFunction(fnDef) && checkEOL());
	}

	private Var.FnDef isUserFunction(boolean checkType, boolean isNumeric) { // if first arg is false, second is ignored

		if (mFunctionTable.isEmpty())	return null;					// if function table empty, return fail

		int LI = LineIndex;
		Var var = parseVar(USER_FN_OK);									// duplicate getVarAndType() except USER_FN_OK
		if ((var != null) && var.isFunction()) {						// found a variable and it is a function name
			Var.FnDef fnDef = mFunctionTable.get(var.name());
			if (fnDef != null) {										// function must be defined already
				if (!checkType || (isNumeric == fnDef.type().isNumeric())) {// type matches or match not needed
					return fnDef;										// success: return function definition
				}
			}
		}
		LineIndex = LI;													// no match: rewind LineIndex
		return null;													// and report fail
	}

	private boolean doUserFunction(Var.FnDef fnDef) {					// execute function named in fnDef

		CallStackFrame frame = new CallStackFrame();
		frame.store(fnDef);												// build a stack frame

		ArrayList<Var.FunctionParameter> parms = fnDef.parms();			// parameters from FN.DEF
		int nParams = fnDef.nParms();									// number of parameters
		int nArgs = 0;													// number of arguments, also parameter index
		if (nParams != 0) {												// for each parameter
			do {
				if (nArgs >= nParams) {									// ensure no more parms than defined
					return RunTimeError("Calling parameter count exceeds defined parameter count");
				}
				Var.FunctionParameter parm = parms.get(nArgs++);		// get parm, count the arg
				Var var = parm.var();

				boolean byReference = isNext('&');						// optional for scalars, ignored for arrays
				parm.byReference(byReference);
				boolean typeIsNumeric = var.isNumeric();
				if (var.isArray()) {									// if this parm is an array
					Var callVar = getExistingArrayVar();				// get caller's array name var
					if (callVar == null)	return false;
					var.arrayDef(callVar.arrayDef());					// get a reference to the caller's ArrayDef
					if (!isNext(']')) {									// must be no indices
						return RunTimeError(EXPECT_ARRAY_NO_INDEX);
					} else  if (typeIsNumeric != callVar.isNumeric()) {	// insure type (string or number) match
						return RunTimeError("Array parameter type mismatch at:");
					}
				} // end array
				else if (byReference) {									// if this parm is var passed by reference
					Var callVar = getVarAndType();
					if (callVar == null)	return false;				// then must be var not expression
					if (typeIsNumeric != callVar.isNumeric()) {			// insure type (string or number) match
						return RunTimeError("Parameter type mismatch at:");
					}
					Var.Val callVal = getVarValue(callVar);				// bottom half of getVar()
					if (callVal == null)	return false;
					var.val(callVal);									// get a reference to the caller's Val
				} // end by-reference
				else {
					var = var.copy();									// don't use the same Var for all calls
					parm.var(var);
					Var.Val val;										// don't use the same Val, either
					if (!typeIsNumeric) {								// if parm is string
						if (!evalStringExpression()) {					// get the string value
							return RunTimeError("Parameter type mismatch at:");
						} else {
							val = new Var.StrVal(StringConstant);
						}
					} else {
						if (!evalNumericExpression()) {					// if parm is number get the numeric value
							return RunTimeError("Parameter type mismatch at:");
						} else {
							val = new Var.NumVal(EvalNumericExpressionValue);
						}
					}
					var.val(val);										// put the value in parm's var
				} // end scalar passed by value
			} while (isNext(','));										// keep going while caller parms exist
			// Loop may have created variables in caller's name space. No more will be created.
		} // end if

		if (nArgs != nParams) { return RunTimeError("Too few calling parameters at:"); }
		if (!isNext(')')) { return false; }					// every function must have a closing right parenthesis
		frame.storeLI();									// passed ')', now save the line buffer index 

		FunctionStack.push(frame);							// push the stack frame
		mSymbolTable = new Var.Table();						// start a new local symbol table
		mSymbolTables.push(mSymbolTable);					// and push it on the stack of tables

		// Start the new symbol table with the function parameter names.
		for (Var.FunctionParameter parm : parms) {
			insertNewVar(parm.var());
		}

		ExecutingLineIndex = fnDef.line() + 1;				// set to execute first line after fn.def statement

		fnRTN = false;										// will be set true by fn.rtn

		// The function body is executed in a recursive call to RunLoop().
		// FN.RTN or FN.END will signal RunLoop to exit, returning pass/fail state of the function here.
		return RunLoop();
		// Note that the part of RunLoop after StatementExecuter runs twice,
		// once after FN.RTN/END and again now, when this method exits.
	} // doUserFunction

	// ************************************ Switch Statements *************************************

	private boolean executeSW() {								// Get Switch (SW) command keyword if it is there
		return executeSubcommand(sw_cmd, "SW");
	}

	private SwitchDef scanSwitch() {
		// If this switch has already been scanned, use the stored SwitchDef.
		SwitchDef sw = (SwitchDef)(ExecutingLineBuffer.mExtras);
		if (sw != null) { return sw; }

		// Scan until "sw.end" or end of program. Remember CASE, DEFAULT, and END lines.
		ExecutingLineBuffer.mExtras = sw = new SwitchDef();
		while (++ExecutingLineIndex < Basic.lines.size()) {
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
			if (!ExecutingLineBuffer.startsWith("sw.")) { continue; } // not a switch command

			LineIndex = 3;									// length of "sw."
			if (ExecutingLineBuffer.cmd() == null) {
				ExecutingLineBuffer.cmd(CMD_SW_GROUP);		// same as result of searchCommands()
			}
			// cmd may be CMD_SW_GROUP if parsed while skipping THEN or ELSE clause of IF
			if (ExecutingLineBuffer.cmd() == CMD_SW_GROUP) {// find out which sw command
				Command c = findSubcommand(sw_cmd, "SW");	// body of executeSubcommand(sw_cmd, "SW")
				if (c == null)			return null;		// ...
				ExecutingLineBuffer.promoteSubCommand();	// ... to here
			}

			Command cmd = ExecutingLineBuffer.cmd();
			if (cmd == CMD_SW_CASE) {
				sw.mCases.add(ExecutingLineIndex);
			} else
			if (cmd == CMD_SW_DEFAULT) {
				if (sw.mDefault != -1) { RunTimeError("Extra sw.default at:"); return null; }
				sw.mDefault = ExecutingLineIndex;
				LineIndex += ExecutingLineBuffer.offset();
				if (!checkEOL())		return null;
			} else
			if (cmd == CMD_SW_BREAK) {
				ExecutingLineBuffer.mExtras = sw;			// so BREAK can find END
				LineIndex += ExecutingLineBuffer.offset();
				if (!checkEOL())		return null;
			} else
			if (cmd == CMD_SW_END) {
				sw.mEnd = ExecutingLineIndex;
				LineIndex += ExecutingLineBuffer.offset();
				if (!checkEOL())		return null;
				return sw;									// SUCCESSFUL EXIT
			} else
			if (cmd == CMD_SW_BEGIN) {						// nested switch
				if (scanSwitch() == null) return null;
			} else						return null;		// ???
		}
		RunTimeError("No sw.end after sw.begin");
		return null;
	} // scanSwitch

	private boolean executeSW_BEGIN() {
		boolean isNumeric;
		double nexp = 0;
		String sexp = "";
		if (evalNumericExpression()) {
			isNumeric = true;
			nexp = EvalNumericExpressionValue;
		} else if (evalStringExpression()) {
			isNumeric = false;
			sexp = StringConstant;
		} else							return false;
		if (!checkEOL())				return false;

		// Scan until "sw.end" or end of program.
		// Store CASE, DEFAULT, and END lines.
		SwitchDef sw = scanSwitch();
		if (sw == null)					return false;

		// If any CASE lines were found, test their conditions.
		// Start execution at the first one that matches the BEGIN expression.
		for (int line : sw.mCases) {
			ExecutingLineIndex = line;
			ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
			LineIndex = ExecutingLineBuffer.offset();
			String text = ExecutingLineBuffer.text();

			boolean found = false;
			char c = text.charAt(LineIndex);
			if ((c == '<') || (c == '>')) {					// SW.CASE <2, SW.CASE >="a", SW.CASE <>"abc" ...
				// Create a new, temporary ProgramLine to test the condition:
				String test = '(' + ((isNumeric) ? String.valueOf(nexp) : quote(sexp))
									+ text.substring(LineIndex, text.length() - 1) + ")\n";
				ExecutingLineBuffer = new ProgramLine(test);
				LineIndex = 0;
				boolean validCondition = evalNumericExpression() && checkEOL();

				// Now that it's done we can revert back to the real ProgramLine and interpret the result:
				ExecutingLineBuffer = Basic.lines.get(ExecutingLineIndex);
				if (!validCondition)				return false;

				LineIndex = text.length(); 					// Condition was valid -> skip it and go to EOL
				found = (0.0 != EvalNumericExpressionValue);// found if expression was true
			} else if (isNumeric) {							// SW.CASE 0,1,2,...
				do {
					if (!evalNumericExpression())	return false;
					if (nexp == EvalNumericExpressionValue) { found = true; break; }
				} while (isNext(','));
			} else {										// SW.CASE "a","b","c",...
				do {
					if (!evalStringExpression())	return false;
					if (sexp.equals(StringConstant)) { found = true; break; }
				} while (isNext(','));
			}
			if (found) {									// good one -> ignore any remaining values
				if (!isEOL() && !isNext(','))		return false;	// must be followed by comma or EOL
				return true;
			}
		} // for each case
		// No matching case found.
		ExecutingLineIndex = (sw.mDefault != -1)			// is there DEFAULT?
							? sw.mDefault					// yes: jump to DEFAULT
							: sw.mEnd;						// no: jump to END
		return true;
	} // executeSW_BEGIN

	private boolean executeSW_CASE() {
		return true;
	}

	private boolean executeSW_BREAK() {
		if (!checkEOL())				return false;
		// Normally this SW.Break was scanned by SW.Begin. In a single-line IF,
		// the scan missed it, so we have to scan again from here.
		SwitchDef sw = scanSwitch();						// get stored SwitchDef ir scan for SW.End
		if (sw == null)					return false;
		ExecutingLineIndex = sw.mEnd;						// jump to END
		return true;
	}

	private boolean executeSW_DEFAULT() {
		return true;
	}

	private boolean executeSW_END() {
		return true;
	}

	// ****************************  End of core Basic Methods *****************************

	// ******************************** Data I/O Operations ********************************

	private boolean checkReadFile(int fileNumber) {							// Validate input file for read commands
		if (FileTable.size() == 0)					{ RunTimeError("No files opened"); }
		else if (fileNumber < 0)					{ RunTimeError("Read file did not exist"); }
		else if (fileNumber >= FileTable.size())	{ RunTimeError("Invalid File Number at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkFile(int fileNumber) {								// Validate input file number for read or write commands
		if (FileTable.size() == 0)					{ RunTimeError("No files opened"); }
		else if (fileNumber >= FileTable.size() || fileNumber < 0)
													{ RunTimeError("Invalid File Number at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkFileType(FileInfo fInfo, FileType fType) {			// Type TEXT, BYTE, or ZIP as requested?
		if (fInfo.type() == fType)		return true;
		String exp = fType.toString();
		String act = fInfo.type().toString();
		return RunTimeError("File opened with " + act + ".open, expected " + exp + ".open" );
	}

	private boolean checkReadAttributes(FileInfo fInfo, FileType fType) {
											// Validate common FileInfo items for commands that read text files
		if (!checkFileType(fInfo, fType)) return false;							// Type TEXT or BYTE as requested?
		else if (fInfo.isClosed())					{ RunTimeError("File is closed"); }
		else if (fInfo.mode() != FMR)				{ RunTimeError("File not opened for read at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	private boolean checkWriteAttributes(FileInfo fInfo, FileType fType) {
											// Validate common FileInfo items for commands that write text files
		if (!checkFileType(fInfo, fType)) return false;							// Type TEXT or BYTE as requested?
		else if (fInfo.isClosed())					{ RunTimeError("File is closed"); }
		else if (fInfo.mode() != FMW)				{ RunTimeError("File not opened for write at"); }
		return !SyntaxError;				// SyntaxError is true if RunTimeError was called
	}

	// ************************************* Text Stream I/O **************************************

	private boolean executeTEXT() {									// Get Text command keyword if it is there
		Command c = findSubcommand(text_cmd, "Text");
		if (c == null)						return false;
		if (c.id == CID_EX) {										// can't do common processing
			return c.run();											// just run it
		}

		if (!evalNumericExpression())		return false;			// first parm is the file number expression
		int fileNumber = EvalNumericExpressionValue.intValue();
		if (c.id == CID_READ) {
			if (!checkReadFile(fileNumber))	return false;			// check runtime errors
		} else if (c.id == CID_WRITE) {
			if (!checkFile(fileNumber))		return false;			// check runtime errors
		}
		return c.run(fileNumber);
	}

	private boolean executeTEXT_OPEN() {							// Open a file
		boolean append = false;										// Assume not append
		int FileMode = 0;											// Default to FMR
		clearErrorMsg();
		switch (ExecutingLineBuffer.text().charAt(LineIndex)) {		// First parm is a, w or r
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
		if (!isNext(','))				return false;
		if (!getNVar())					return false;				// Next parameter is the file number variable
		Var.Val val = mVal;
		double fileNumber = FileTable.size();

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// Final parameter is the filename
		String fileName = StringConstant;
		if (!checkEOL())				return false;

		File file = new File(Basic.getDataPath(fileName));

		if (FileMode == FMR) {										// Read was selected
			TextReaderInfo fInfo = new TextReaderInfo(FileMode);	// Prepare the FileTable object
			BufferedReader buf = null;
			int flen = (int)Math.min(file.length(), Integer.MAX_VALUE);
			try {
				buf = Basic.getBufferedReader(Basic.DATA_DIR, fileName, Basic.Encryption.NO_DECRYPTION);
				if (buf == null) {
					writeErrorMsg(fileName + " not found");
				} else if (buf.markSupported()) {
					buf.mark(flen);
					fInfo.mark(1, flen);
				}
			}
			catch (Exception e) { writeErrorMsg(e); }
			if (buf != null) {
				fInfo.mTextReader = buf;							// store reader in fInfo
			} else {
				fileNumber = -1;									// change file index to report file does not exist
			}
			FileTable.add(fInfo);
		}

		else if (FileMode == FMW) {									// Write Selected
			TextWriterInfo fInfo = new TextWriterInfo(FileMode);	// Prepare the FileTable object
			// fInfo.mPosition is 1: absolute if 'w' and relative to old EOF if 'a'.
			FileWriter writer = null;
			if (!append || !file.exists()) {						// if not appending overwrite existing file
				try { file.createNewFile(); }						// if no file create a new one
				catch (IOException e) { writeErrorMsg(e); }
			}
			if (file.exists() && file.canWrite()) {
				try { writer = new FileWriter(file, append); }		// open the filewriter for the SD Card
				catch (IOException e) { writeErrorMsg(e); }
			}
			if (writer != null) {
				fInfo.mTextWriter = writer;							// store writer in fInfo
			} else {
				fileNumber = -1;									// change file index to report file does not exist
			}
			FileTable.add(fInfo);
		}
		val.val(fileNumber);										// return the file index
		return true;												// Done
	}

	private boolean executeTEXT_READLN(int fileNumber) {			// first parameter: file table index
		List<Var.Val> valList = new ArrayList<Var.Val>();
		while (isNext(',')) {										// list of zero or more
			if (!getSVar())				return false;				// numeric variable
			valList.add(mVal);										// to hold the data
		}
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkReadAttributes(fInfo, FileType.FILE_TEXT)) return false;

		int expect = valList.size();
		int actual = 0;
		if (!fInfo.isEOF()) {										// if already eof don't read
			BufferedReader buf = ((TextReaderInfo)fInfo).mTextReader;
			String[] values = new String[expect];
			try {
				for (; actual < expect; ++actual) {
					values[actual] = buf.readLine();				// read a line
					if (values[actual] == null) {					// hit eof
						fInfo.eof(true);							// mark fInfo
						break;
					}
				}
			}
			catch (IOException e) { return RunTimeError("I/O error at:"); }

			for (int i = 0; i < actual; ++i) {
				valList.get(i).val(values[i]);						// give the data to the user
			}
			fInfo.incPosition(actual);								// update position in Bundle
		}
		for (int i = actual; i < expect; ++i) {						// if any array space is left
			valList.get(i).val("EOF");								// fill it with EOF markers
		}
		return true;
	}

	private boolean executeTEXT_WRITELN(int fileNumber) {			// first parameter: file table index
		if (!isNext(','))				return false;				// set up to parse the stuff to print

		if (!buildPrintLine(textPrintLine, "\r\n")) return false;	// build up the text line in StringConstant
		if (!PrintLineReady) {										// flag set by buildPrintLine
			textPrintLine = StringConstant;							// not ready to print; hold line
			return true;											// and wait for next Text.Writeln command
		}
		textPrintLine = "";											// clear the accumulated text print line

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkWriteAttributes(fInfo, FileType.FILE_TEXT)) return false;

		FileWriter writer =  ((TextWriterInfo)fInfo).mTextWriter;
		try { writer.write(StringConstant); }
		catch (IOException e) { return RunTimeError("I/O error at"); }

		fInfo.incPosition();										// update position in fInfo
		return true;
	}

	private boolean executeTEXT_INPUT() {
		if (!getSVar())					return false;
		Var.Val val = mVal;												// variable to hold the data

		TextInputString = "";
		String title = null;
		if (isNext(',')) {											// Check for optional parameter(s)
			boolean isComma = isNext(',');							// Look for second comma, two commas together
																	// mean initial text is skipped, use empty string
			if (!isComma) {
				if (!getStringArg())	return false;				// One comma so far; get initial input text
				TextInputString = StringConstant;
				isComma = isNext(',');								// Look again for second comma
			}
			if (isComma) {
				if (!getStringArg())	return false;				// Second comma; get title
				title = StringConstant;
			}
		}
		if (!checkEOL())				return false;

		Intent intent = new Intent(Run.this, TextInput.class);
		if (title != null) { intent.putExtra("title", title); }
		mWaitForLock = true;
		startActivityForResult(intent, BASIC_GENERAL_INTENT);

		waitForLOCK();												// Wait for signal from TextInput.java thread

		val.val(TextInputString);
		return true;
	}

	private boolean executeTEXT_POSITION_SET(int fileNumber) {		// first parameter: file table index
		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// second parm is the position expression
		long pto = EvalNumericExpressionValue.longValue();
		if (pto < 1) {
			return RunTimeError("Set position must be >= 1");
		}
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkReadAttributes(fInfo, FileType.FILE_TEXT)) return false;

		long pnow = fInfo.position();
		boolean eof = fInfo.isEOF();
		BufferedReader buf = ((TextReaderInfo)fInfo).mTextReader;

		if (pto < pnow) {
			try { buf.reset(); }									// back to mark, exception if mark invalid
			catch (IOException e) { return RunTimeError(e); }
			eof = false;
			long pmark = fInfo.mark();
			pnow = (pmark > 0) ? pmark : 1;							// pmark should not be 0
		}
		String data = null;
		while ((pnow < pto) && !eof) {
			try { data = buf.readLine(); }							// read a line
			catch (Exception e) { return RunTimeError(e); }
			if (data == null) {
				eof = true;											// hit eof, record in fInfo
			} else {
				++pnow;												// not eof, update position in fInfo
			}
		}
		fInfo.position(pnow);										// update fInfo
		fInfo.eof(eof);
		return true;
	}

	private boolean executeTGET() {
		if (!getSVar())					return false;
		Var.Val val = mVal;								// variable to hold the data

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;
		TextInputString = StringConstant;
		String Prompt = StringConstant;

		String title = null;
		if (isNext(',')) {
			if (!getStringArg())		return false;
			title = StringConstant;
		}
		if (!checkEOL())				return false;

		checkpointMessage();							// allow any pending Console activity to complete
		while (mMessagePending) { Thread.yield(); }		// wait for checkpointMessage semaphore to clear

		Intent intent = new Intent(Run.this, TGet.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		if (title != null) { intent.putExtra(TGet.TITLE, title); }
		synchronized (mConsoleBuffer) {
			intent.putStringArrayListExtra(TGet.CONSOLE_TEXT, mOutput);
		}
		TGet.mMenuStop = false;
		mWaitForLock = true;
		startActivityForResult(intent, BASIC_GENERAL_INTENT);

		waitForLOCK();									// wait for signal from TGet.java thread

		if (TGet.mMenuStop) {							// user selected Stop from TGet menu
														// code copied from onOptionsItemSelected
			PrintShow("Stopped by user.");				// tell user
			Stop = true;								// signal main loop to stop
			disableInterrupt(Interrupt.BACK_KEY_BIT);	// menu-selected stop is not trappable
		} else {
			PrintShow(Prompt + TextInputString);
			val.val(TextInputString);
		}
		return true;
	}

	// ************************************* Byte Stream I/O **************************************

	private boolean executeBYTE() {							// Get Byte command keyword if it is there
		Command c = findSubcommand(byte_cmd, "Byte");
		if (c == null)						return false;
		if (c.id == CID_EX) {										// can't do common processing
			return c.run();											// just run it
		}

		if (!evalNumericExpression())		return false;			// first parm is the file number expression
		int fileNumber = EvalNumericExpressionValue.intValue();
		if (c.id == CID_READ) {
			if (!checkReadFile(fileNumber))	return false;			// check runtime errors
		} else if (c.id == CID_WRITE) {
			if (!checkFile(fileNumber))		return false;			// check runtime errors
		}
		return c.run(fileNumber);
	}

	private boolean executeBYTE_OPEN() {							// Open a file
		boolean append = false;										// Assume not append
		int FileMode = 0;											// Default to FMR
		clearErrorMsg();
		switch (ExecutingLineBuffer.text().charAt(LineIndex)) {		// First parm is a, w or r
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
		if (!isNext(','))				return false;
		if (!getNVar())					return false;				// Next parameter is the file number variable
		Var.Val val = mVal;
		double fileNumber = FileTable.size();

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// Final parameter is the filename
		String fileName = StringConstant;
		if (!checkEOL())				return false;

		if (FileMode == FMR) {										// Read was selected
			ByteReaderInfo fInfo = new ByteReaderInfo(FileMode);	// Prepare the FileTable object
			BufferedInputStream buf = null;
			if (fileName.startsWith("http")) {
				try {
					URL url = new URL(fileName);
					URLConnection connection = url.openConnection();
					buf = new BufferedInputStream(connection.getInputStream());
				} catch (Exception e) {
					writeErrorMsg(e);
				}
			} else {
				File file = new File(Basic.getDataPath(fileName));
				int flen = (int)Math.min(file.length(), Integer.MAX_VALUE);
				try {
					buf = Basic.getBufferedInputStream(Basic.DATA_DIR, fileName);
					if (buf == null) {
						writeErrorMsg(file + " not found");
					} else if (buf.markSupported()) {
						buf.mark(flen);
						fInfo.mark(1, flen);
					}
				}
				catch (Exception e) { writeErrorMsg(e); }
			}
			if (buf != null) {
				fInfo.mByteReader = buf;							// store reader in fInfo
			} else {
				fileNumber = -1;									// change file index to report file does not exist
			}
			FileTable.add(fInfo);
		}

		else if (FileMode == FMW) {									// Write Selected
			ByteWriterInfo fInfo = new ByteWriterInfo(FileMode);	// Prepare the FileTable object
			// fInfo.mPosition is 1: absolute if 'w' and relative to old EOF if 'a'.
			FileOutputStream fos = null;
			File file = new File(Basic.getDataPath(fileName));
			if (append && file.exists()) {
				fInfo.position(file.length() + 1);
			} else {												// if not appending overwrite existing file
				try { file.createNewFile(); }						// if no file create a new one
				catch (IOException e) { writeErrorMsg(e); }
			}
			if (file.exists() && file.canWrite()) {
				try { fos = new FileOutputStream(file.getAbsolutePath(), append); }
				catch (Exception e) { writeErrorMsg(e); }
			}
			if (fos != null) {
				fInfo.mByteWriter = fos;							// store writer in fInfo
			} else {
				fileNumber = -1;									// change file index to report file does not exist
			}
			FileTable.add(fInfo);
		}
		val.val(fileNumber);										// return the file index
		return true;												// Done
	}

	private boolean executeBYTE_COPY(int srcFileNumber) {			// first parameter: source file table index
		if (!isNext(','))				return false;
		if (!evalStringExpression())	return false;				// second parm is the destination file name
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(srcFileNumber);				// source file info
		if (!checkReadAttributes(fInfo, FileType.FILE_BYTE)) return false;
		if (fInfo.isEOF()) { return RunTimeError("Attempt to read beyond the EOF at:"); }

		BufferedInputStream bis = ((ByteReaderInfo)fInfo).mByteReader;

		String theFileName = StringConstant;
		File file = new File(Basic.getDataPath(theFileName));

		try {
			file.createNewFile();
		} catch (IOException e) {
			return RunTimeError(theFileName + " I/O Error");
		}
		if (!file.exists() || !file.canWrite()) {
			return RunTimeError("Problem opening " + theFileName);
		}
		return copyFile(bis, file, fInfo);
	}

	// Bottom half of Byte.Copy: copy input stream to output file. Close the streams.
	// For shared use, FileInfo may be null.
	private boolean copyFile(BufferedInputStream bis, File outFile, FileInfo fInfo) {
		if ((bis == null) || (outFile == null)) return false;
		long p = (fInfo != null) ? fInfo.position() : 0;

		IOException ex = null;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(outFile), 8192);
			byte[] buffer = new byte[1024];
			int len = 0;

			while((len = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, len);
				p += len;
			}
		} catch (IOException e) {
			ex = e;
			return RunTimeError("Exception: " + e);
		} finally {
			ex = FileInfo.closeStream(bis, ex);
			ex = FileInfo.closeStream(bos, FileInfo.flushStream(bos, ex));
			if (ex != null) { return !SyntaxError && RunTimeError(ex); }
		}
		if (fInfo != null) {							// update fInfo, if there is one
			fInfo.position(p);
			fInfo.eof(true);
		}
		return true;
	}

	private FileInfo getByteReadParams(int fileNumber, List<Var.Val> valList) {
																// return FileInfo or null if error
																// return list of variables in varList
		while (isNext(',')) {										// list of zero or more
			if (!getNVar())				return null;				// numeric variable
			valList.add(mVal);										// to hold the data
		}
		if (!checkEOL())				return null;

		FileInfo fInfo = FileTable.get(fileNumber);
		return (checkReadAttributes(fInfo, FileType.FILE_BYTE)) ? fInfo : null;
	}

	private FileInfo getByteWriteParams(int fileNumber, List<Double> nvalList, List<String> svalList) {
																// return FileInfo or null if error
																// return list of numeric values in nvalList
																// or single string value in svalList
		boolean isComma = isNext(',');
		if (isComma) {
			if (evalNumericExpression()) {							// second param may be numeric expression
				nvalList.add(EvalNumericExpressionValue);
				while (isComma = isNext(',')) {
					if (!evalNumericExpression()) break;			// list of numeric expressions
					nvalList.add(EvalNumericExpressionValue);
				}
			}
			if (isComma) {											// last param may be string expression
				if (!getStringArg())	return null;				// note: potential double-evaluation
				if (svalList != null) { svalList.add(StringConstant); }
			}
		}
		if (!checkEOL())				return null;

		FileInfo fInfo = FileTable.get(fileNumber);
		return (checkWriteAttributes(fInfo, FileType.FILE_BYTE)) ? fInfo : null;
	}

	private boolean executeBYTE_READ_BYTE(int fileNumber) {			// first parameter: file table index
		List<Var.Val> valList = new ArrayList<Var.Val>();
		FileInfo fInfo = getByteReadParams(fileNumber, valList);	// zero or more variables for data bytes
		if (fInfo == null)				return false;

		int expect = valList.size();
		int actual = 0;
		if (!fInfo.isEOF()) {										// if already eof don't read
			DataInputStream dis = ((ByteReaderInfo)fInfo).getDIS();
			byte[] values = new byte[expect];
			try { actual = dis.read(values); }						// read the byte(s)
			catch (IOException e) { return RunTimeError(e); }
			if (actual < 0) { actual = 0; }							// -1 if started at EOF
			if (actual < expect) { fInfo.eof(true); }				// hit eof, mark fInfo

			for (int i = 0; i < actual; ++i) {
				int datum = values[i] & 0x00FF;						// prevent sign-extension
				valList.get(i).val(datum);							// give the data to the user
			}
			fInfo.incPosition(actual);								// update position in fInfo
		}
		for (int i = actual; i < expect; ++i) {
			valList.get(i).val(-1);									// eof markers if needed
		}
		return true;
	}

	private boolean executeBYTE_READ_NUMBER(int fileNumber) {		// first parameter: file table index)
		List<Var.Val> valList = new ArrayList<Var.Val>();
		FileInfo fInfo = getByteReadParams(fileNumber, valList);
		if (fInfo == null)				return false;

		int expect = valList.size();
		int actual = 0;
		if (!fInfo.isEOF()) {										// if already eof don't read
			DataInputStream dis = ((ByteReaderInfo)fInfo).getDIS();
			double[] values = new double[expect];
			try {
				for (; actual < expect; ++actual) {
					values[actual] = dis.readDouble();				// read a double
				}
			}
			catch (EOFException eof) { fInfo.eof(true); }			// hit eof, mark fInfo
			catch (IOException e) { return RunTimeError("I/O error at:"); }

			for (int i = 0; i < actual; ++i) {
				valList.get(i).val(values[i]);						// give the data to the user
			}
			fInfo.incPosition(actual * 8);							// update position in Bundle
		}
		for (int i = actual; i < expect; ++i) {						// if any array space is left
			valList.get(i).val(-1);									// fill it with EOF markers
		}
		return true;
	}

	private boolean executeBYTE_READ_BUFFER(int fileNumber) {		// first parameter: file table index
		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// second parm is the byte count
		int expect = EvalNumericExpressionValue.intValue();

		if (!isNext(','))				return false;				// third parm is the return buffer string variable
		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkReadAttributes(fInfo, FileType.FILE_BYTE)) return false;

		String buff = "";
		if (!fInfo.isEOF()) {										// if already eof don't read
			DataInputStream dis = ((ByteReaderInfo)fInfo).getDIS();
			byte[] values = new byte[expect];
			int actual = 0;
			try { actual = dis.read(values); }						// read the bytes
			catch (IOException e) { return RunTimeError(e); }
			if (actual < 0) { actual = 0; }							// -1 if started at EOF
			if (actual > 0) {
				try { buff = new String(values, "ISO-8859-1"); }	// convert bytes to string for user
				catch (UnsupportedEncodingException ex) {
					return RunTimeError(ex);						// can't happen: "ISO-8859-1" is supported
				}
				if (actual < expect) {
					buff = buff.substring(0, actual);
					fInfo.eof(true);								// hit eof, mark fInfo
				}
				fInfo.incPosition(actual);							// update position in fInfo
			}
		}
		val.val(buff);												// give the data to the user
		return true;
	}

	private boolean executeBYTE_WRITE_BYTE(int fileNumber) {		// first parameter: file table index
		List<Double> nvalList = new ArrayList<Double>();
		List<String> svalList = new ArrayList<String>();
		FileInfo fInfo = getByteWriteParams(fileNumber, nvalList, svalList); // expect zero or more nval and zero or one sval
		if (fInfo == null)				return false;
		if (svalList.size() > 1)		return false;
		if ((nvalList.size() == 0) &&
			(svalList.size() == 0))		return true;				// nothing to do
		return writeBytes(fInfo, nvalList, svalList);
	}

	private boolean executeBYTE_WRITE_NUMBER(int fileNumber) {		// first parameter: file table index
		List<Double> nvalList = new ArrayList<Double>();
		List<String> svalList = new ArrayList<String>();
		FileInfo fInfo = getByteWriteParams(fileNumber, nvalList, svalList); // expect zero or more nval and no sval
		if (fInfo == null)				return false;
		if (svalList.size() != 0)		return false;
		if (nvalList.size() == 0)		return true;				// nothing to do

		DataOutputStream dos = ((ByteWriterInfo)fInfo).getDOS();
		for (Double val : nvalList) {
			try { dos.writeDouble(val); }							// write the number
			catch (IOException e) { return RunTimeError("I/O error at"); }
		}
		fInfo.incPosition(8 * nvalList.size());						// size of numbers written by dos.writeDouble
		return true;
	}

	private boolean executeBYTE_WRITE_BUFFER(int fileNumber) {		// first parameter: file table index
		List<String> svalList = new ArrayList<String>();
		FileInfo fInfo = getByteWriteParams(fileNumber, null, svalList); // expect no nvals and a single sval
		if (fInfo == null)				return false;
		if (svalList.size() == 0)		return true;				// nothing to do
		if (svalList.size() != 1)		return false;

		return writeBytes(fInfo, null, svalList);
	}

	private boolean writeBytes(FileInfo fInfo, List<Double> nvalList, List<String> svalList) {
		FileOutputStream fos = ((ByteWriterInfo)fInfo).mByteWriter;
		if ((nvalList != null) && (nvalList.size() != 0)) {
			for (Double val : nvalList) {							// write the numeric byte(s)
				try { fos.write(val.byteValue()); }
				catch (IOException e) {	return RunTimeError("I/O error at"); }
			}
			fInfo.incPosition(nvalList.size());
		}
		if ((svalList != null) && (svalList.size() != 0)) {
			for (String s : svalList) {								// write the bytes from the string(s)
				int len = s.length();
				for (int k = 0; k < len; ++k) {
					try { fos.write((byte)s.charAt(k)); }
					catch (IOException e) {	return RunTimeError("I/O error at"); }
				}
				fInfo.incPosition(len);
			}
		}
		return true;
	}

	private boolean executeBYTE_TRUNCATE(int fileNumber) {	// truncate a file opened for write
																	// first parameter: file table index
		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// second parm is the truncation length
		long length = EvalNumericExpressionValue.longValue();
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkWriteAttributes(fInfo, FileType.FILE_BYTE)) return false;

		try { ((ByteWriterInfo)fInfo).truncateFile(length); }
		catch (IOException ex) { return RunTimeError(ex); }
		return true;
	}

	private boolean executeBYTE_POSITION_SET(int fileNumber) {		// first parameter: file table index
		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// second parm is the position expression
		long pto = EvalNumericExpressionValue.longValue();
		if (pto < 1) {
			return RunTimeError("Set position must be >= 1");
		}
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkReadAttributes(fInfo, FileType.FILE_BYTE)) return false;

		BufferedInputStream bis = ((ByteReaderInfo)fInfo).mByteReader;
		long pnow = fInfo.position();
		boolean eof = fInfo.isEOF();

		if (pto < pnow) {
			try { bis.reset(); }							// back to mark, exception if mark invalid
			catch (IOException e) { return RunTimeError(e); }
			eof = false;
			long pmark = fInfo.mark();
			pnow = (pmark > 0) ? pmark : 1;					// pmark should not be 0
		}
		if ((pnow != pto) && !eof) {
			long skip = pto - pnow - 1;						// Skip plus single read will get to target position
			long skipped = 0;
			int data = -1;
			do {
				try {
					int avail = bis.available();			// Don't skip past eof
					skipped += bis.skip(Math.min(skip - skipped, avail));
					data = bis.read();						// Read to check eof
				} catch (Exception e) {
					return RunTimeError(e);
				}
				if (data >= 0) { ++skipped; }				// If byte was read, count it
				else { eof = true; break; }					// otherwise mark eof in Bundle
			} while (skipped < skip);
			pnow += skipped;								// Count bytes skipped
		}
		fInfo.position(pnow);								// update fInfo
		fInfo.eof(eof);
		return true;
	}

	// ************************************* Zip Stream I/O **************************************

	private boolean executeZIP() {							// Get Zip command keyword if it is there
		Command c = findSubcommand(zip_cmd, "Zip");
		if (c == null)						return false;
		if (c.id == CID_EX) {										// can't do common processing
			return c.run();											// just run it
		}

		if (!evalNumericExpression())		return false;			// first parm is the file number expression
		int fileNumber = EvalNumericExpressionValue.intValue();
		if (c.id == CID_READ) {
			if (!checkReadFile(fileNumber))	return false;			// check runtime errors
		} else if (c.id == CID_WRITE) {
			if (!checkFile(fileNumber))		return false;			// check runtime errors
		}
		return c.run(fileNumber);
	}

	private boolean executeZIP_DIR() {
		if (!getStringArg())			return false;		// get the path
		String fileName = StringConstant;

		if (!isNext(','))				return false;			// parse the ,D$[]
		Var var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
		if (var == null)				return false;			// must name a new string array variable
		String dirMark = "(d)";
		if (isNext(',')) {										// optional directory marker
			if (!getStringArg())		return false;
			dirMark = StringConstant;
		}
		if (!checkEOL())				return false;

		ArrayList <String> files = new ArrayList<String>();
		ArrayList <String> dirs = new ArrayList<String>();

		File file = new File(Basic.getDataPath(fileName));
		ZipFile reader = null;

		try {
			try {												// Open zip file for reading
				reader = new ZipFile(file);
				if (reader == null) { return RunTimeError(fileName + " not found"); }
			}
			catch (Exception e) { return RunTimeError(e); }

			// Get an enumeration of its entries
			Enumeration<? extends ZipEntry> e = reader.entries();

			// Go through each one of them
			while (e.hasMoreElements()) {
				ZipEntry entry = e.nextElement();
				String s = entry.getName();						// get the name of the entry
				if (entry.isDirectory()) {
					dirs.add(s.substring(0, s.length()-1) + dirMark);	// mark it and add it to directories list
				} else {
					files.add(s);								// else add name without the directory mark
				}
			}
			Collections.sort(dirs);								// Sort the directory list
			Collections.sort(files);							// Sort the file list
			dirs.addAll(files);									// copy the file list to end of dir list
		}
		finally {
			try { if (reader != null) { reader.close(); } }
			catch (IOException e) { return RunTimeError("I/O error at:"); }
		}

		int length = dirs.size();								// number of directories and files in list
		if (length == 0) {										// make at least one element if dir is empty
			dirs.add(" ");
			length = 1;
		}
		return ListToBasicStringArray(var, dirs, length);		// Copy the list to a BASIC! array
	}

	private boolean executeZIP_OPEN() {								// Open a file
		int FileMode = 0;											// Default to FMR
		clearErrorMsg();
		switch (ExecutingLineBuffer.text().charAt(LineIndex)) {		// First parm is a, w or r
		case 'w': case 'a':					// write
			FileMode = FMW;
			++LineIndex;
			break;
		case 'r':							// read
			FileMode = FMR;
			++LineIndex;
		}
		if (!isNext(','))				return false;
		if (!getNVar())					return false;				// Next parameter is the file number variable
		Var.Val val = mVal;
		double fileNumber = FileTable.size();

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// Final parameter is the filename
		String fileName = StringConstant;
		if (!checkEOL())				return false;

		File file = new File(Basic.getDataPath(fileName));

		if (FileMode == FMR) {										// Read was selected
			ZipReaderInfo fInfo = new ZipReaderInfo(FileMode);		// Prepare the FileTable object
			ZipFile zip = null;
			try {
				zip = new ZipFile(file);
				if (zip == null) {
					writeErrorMsg(fileName + " not found");
				}
			}
			catch (Exception e) { writeErrorMsg(e); }
			if (zip != null) {
				fInfo.mZipReader = zip;								// store reader in fInfo
			} else {
				fileNumber = -1;									// change file index to report file does not exist
			}
			FileTable.add(fInfo);
		}

		else if (FileMode == FMW) {									// Write Selected
			ZipWriterInfo fInfo = new ZipWriterInfo(FileMode);		// Prepare the FileTable object
			ZipOutputStream zos = null;
			if (!file.exists()) {
				try { file.createNewFile(); }						// if no file create a new one
				catch (IOException e) { writeErrorMsg(e); }
			}
			if (file.exists() && file.canWrite()) {					// open the zip writer for the SD Card
				try { zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(file))); }
				catch (IOException e) { writeErrorMsg(e); }
			}
			if (zos != null) {
				fInfo.mZipWriter = zos;								// store writer in fInfo
			} else {
				fileNumber = -1;									// change file index to report file does not exist
			}
			FileTable.add(fInfo);
		}

		val.val(fileNumber);										// return the file index
		return true;												// Done
	}

	private boolean executeZIP_READ(int fileNumber) {				// first parameter: file table index
		if (!isNext(','))				return false;				// set up to parse the stuff to print
		if (!getSVar())					return false;				// need string variable
		Var.Val val = mVal;											// variable to hold the data
		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// get the entry path
		String fileName = StringConstant;
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkReadAttributes(fInfo, FileType.FILE_ZIP)) return false;

		ZipFile zip = ((ZipReaderInfo)fInfo).mZipReader;

		try {
			ZipEntry entry = zip.getEntry(fileName);
			if (entry == null) {									// entry not found in the zip: return "EOF"
				val.val("EOF");
			} else if (entry.isDirectory()) {
				return RunTimeError("Zip entry must be a file:");
			} else {
				InputStream zis = zip.getInputStream(entry);
				BufferedInputStream is = new BufferedInputStream(zis);
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				BufferedOutputStream os = new BufferedOutputStream(buffer);
				byte[] data = new byte[1024];
				int read = 0;
				while ((read = is.read(data)) != -1) {
					os.write(data, 0, read);
				}
				os.flush();
				val.val(buffer.toString("ISO-8859-1"));				// convert bytes to string for user
			}
		}
		catch (IOException e) { return RunTimeError("I/O error at:"); }

		return true;
	}

	private boolean executeZIP_WRITE(int fileNumber) {				// first parameter: file table index
		if (!isNext(','))				return false;				// set up to parse the stuff to print
		if (!getStringArg())			return false;
		String buffer = StringConstant;								// variable to hold the data
		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// get the entry path
		String fileName = StringConstant;
		if (!checkEOL())				return false;

		FileInfo fInfo = FileTable.get(fileNumber);
		if (!checkWriteAttributes(fInfo, FileType.FILE_ZIP)) return false;

		ZipOutputStream zos =  ((ZipWriterInfo)fInfo).mZipWriter;	// get the writer to the zip

		// create an input stream from the string buffer passed as parameter
		ByteArrayInputStream is = null;
		try {
			is = new ByteArrayInputStream( buffer.getBytes("ISO-8859-1") );
		}
		catch (UnsupportedEncodingException ex) {
			return RunTimeError(ex);								// can't happen: "ISO-8859-1" is supported
		}

		try {
			byte[] data = new byte[1024];
			int read = 0;
			ZipEntry entry = new ZipEntry(fileName);
			zos.putNextEntry(entry);								// create entry in the zip
			while ((read = is.read(data, 0, 1024)) != -1) {
				zos.write(data, 0, read);							// write data to it
			}
		}
		catch (IOException e) { return RunTimeError("I/O error at:"); }
		finally {
			try { is.close(); }										// close input stream
			catch (IOException e) { return RunTimeError("I/O error at:"); }
		}

		return true;
	}

	// ************************ Text / Byte / Zip Stream: Common Operations ***********************

	private boolean executeCLOSE(FileType fType) {
		if (FileTable.size() == 0)			return true;

		if (!evalNumericExpression())		return false;			// First parm is the file number expression
		int fileNumber = EvalNumericExpressionValue.intValue();
		if (!checkFile(fileNumber))			return false;			// Check runtime errors
		if (!checkEOL())					return false;

		FileInfo fInfo = FileTable.get(fileNumber);					// Get the file info
		if (!checkFileType(fInfo, fType))	return false;			// Type TEXT, BYTE, or ZIP, as requested?

		if (fInfo.isClosed())				return true;			// Already closed

		IOException e = fInfo.close(fInfo.flush(null));				// flush is no-op on read types
		if (e != null) { return RunTimeError(e); }
		return true;
	}

	private boolean executeEOF(int fileNumber, FileType fType) {
		if (!isNext(',') || !getNVar())		return false;			// Second parm is the logical (numeric) variable
		Var.Val val = mVal;											// to hold the return value
		if (!checkEOL())					return false;

		FileInfo fInfo = FileTable.get(fileNumber);					// Get the file info
		if (!checkFileType(fInfo, fType))	return false;			// Type TEXT or BYTE as requested?

		boolean eof = fInfo.isClosed() || fInfo.isEOF();			// if closed or eof return true, else false
		val.val(eof ? 1.0 : 0.0);									// return boolean as numeric
		return true;
	}

	private boolean executePOSITION_GET(int fileNumber, FileType fType) {
		if (!isNext(','))					return false;			// Second parm is the position var
		if (!getNVar())						return false;
		Var.Val val = mVal;
		if (!checkEOL())					return false;

		FileInfo fInfo = FileTable.get(fileNumber);					// Get the file info
		if (!checkFileType(fInfo, fType))	return false;			// Type TEXT or BYTE as requested?

		val.val(fInfo.position());
		return true;
	}

	private boolean executePOSITION_MARK(FileType fType) {
		int fileNumber;
		int markLimit = -1;
		boolean isComma = isNext(',');
		if (!isComma && !isEOL()) {									// there is a file pointer arg
			if (!evalNumericExpression())	return false;
			fileNumber = EvalNumericExpressionValue.intValue();
			isComma = isNext(',');
		} else {
			fileNumber = FileTable.size() - 1;						// default if no file pointer arg
		}
		if (!checkReadFile(fileNumber))		return false;			// check runtime errors

		if (isComma) {												// second parm is the mark limit
			if (!evalNumericExpression())	return false;
			markLimit = EvalNumericExpressionValue.intValue();
			if (markLimit < 0) { markLimit = 0; }
		}
		if (!checkEOL())					return false;

		FileInfo fInfo = FileTable.get(fileNumber);					// Get the file info
		if (!checkReadAttributes(fInfo, fType)) return false;		// Check runtime errors

		if (markLimit < 0) {
			markLimit = fInfo.markLimit();							// retrieve mark limit from fInfo
		}

		switch (fType) {
			case FILE_TEXT:
				BufferedReader buf = ((TextReaderInfo)fInfo).mTextReader;
				try { buf.mark(markLimit); }						// set the mark
				catch (IOException e) { return RunTimeError("I/O error at:"); }
				break;
			case FILE_BYTE:
				BufferedInputStream bis = ((ByteReaderInfo)fInfo).mByteReader;
				bis.mark(markLimit);								// set the mark
				break;
		}
		fInfo.markCurrentPosition(markLimit);						// update the fInfo
		return true;
	}

	// ************************************* File Operations **************************************

	private boolean checkSDCARD(char mount) {				// mount is 'w' for writable,
															// 'r' for either readable or writable
		return Basic.checkSDCARD(mount) ? true : RunTimeError("SDCARD not available.");
	}

	private boolean executeMKDIR() {
		if (!getStringArg())			return false;		// get the path
		String path = StringConstant;
		if (!checkEOL())				return false;

		File file = new File(Basic.getDataPath(path));
		file.mkdirs();
		if (!file.exists()) {								// did we get a dir?
			return RunTimeError(path + " mkdir failed");
		}
		return true;
	}

	private boolean executeRENAME() {
		if (!getStringArg())			return false;		// get the old file name
		String Old = StringConstant;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;		// get the new file name
		String New = StringConstant;

		if (!checkEOL())				return false;
		if (!checkSDCARD('w'))			return false;

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

	private boolean executeDELETE() {

		if (!getNVar())					return false;		// get the var to put the result into
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;		// get the file name
		String fileName = StringConstant;

		if (!checkEOL())				return false;
		if (!checkSDCARD('w'))			return false;

		File file = new File(Basic.getDataPath(fileName));
		double result = file.delete() ? 1 : 0;				// try to delete it
		val.val(result);
		return true;
	}

	private boolean executeFILE() {							// Get File command keyword if it is there
		return executeSubcommand(file_cmd, "File");
	}

	private boolean executeFILE_EXISTS() {
		if (!getNVar())					return false;		// get the var to put the result into
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;		// get the file name
		String fileName = StringConstant;

		if (!checkEOL())				return false;
		if (!checkSDCARD('r'))			return false;

		double exists = 0.0;								// "false"
		if (!fileName.equals("")) {							// empty file name would report parent dir exists; catch it and report false
			File file = new File(Basic.getDataPath(fileName));
			if (file.exists()) exists = 1.0;				// if file exists, report "true"
		}
		val.val(exists);
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
			finally { FileInfo.closeStream(inputStream, null); }
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
			finally { FileInfo.closeStream(inputStream, null); }
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
		finally { FileInfo.closeStream(bis, null); }

		return ((size == AssetFileDescriptor.UNKNOWN_LENGTH) ? -1 : size);
	}

	private boolean executeFILE_SIZE() {
		if (!getNVar())					return false;		// get the var to put the size value into
		Var.Val val = mVal;
		long size = -1;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;		// get the file name
		String fileName = StringConstant;

		if (!checkEOL())				return false;
		if (!checkSDCARD('r'))			return false;

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
		val.val(size);										// Put the file size into the var
		return true;
	}

	private boolean executeFILE_TYPE() {
		if (!getSVar())					return false;		// get the var to put the type info into
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;		// get the file name
		String fileName = StringConstant;

		if (!checkEOL())				return false;
		if (!checkSDCARD('r'))			return false;

		String type = "x";									// assume does not exist
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
		val.val(type);											// put the file type into the var
		return true;
	}

	private boolean executeFILE_ROOT() {
		if (!getSVar())					return false;
		Var.Val val = mVal;

		if (!checkEOL())				return false;
		if (!checkSDCARD('r'))			return false;

		val.val(Basic.getDataPath(null));						// return canonical path to default data directory
		return true;
	}

	private boolean executeDIR() {
		if (!getStringArg())			return false;			// get the path
		String filePath = StringConstant;

		if (!isNext(','))				return false;			// parse the ,D$[]
		Var var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
		if (var == null)				return false;			// must name a new string array variable
		String dirMark = "(d)";
		if (isNext(',')) {										// optional directory marker
			if (!getStringArg())		return false;
			dirMark = StringConstant;
		}
		if (!checkEOL())				return false;			// line must end with ']'

		File lbDir = new File(Basic.getDataPath(filePath));
		if (!lbDir.exists()) {									// error if directory does not exist
			return RunTimeError(filePath + " is invalid path");
		}

		ArrayList <String> files = new ArrayList<String>();
		ArrayList <String> dirs = new ArrayList<String>();

		String FL[] = lbDir.list();								// get the list of files in the dir
		if (FL == null) {										// if not a dir
			dirs.add(" ");										// make list with one element
		} else {
											// Go through the file list and mark directory entries with dirMark
			String absPath = lbDir.getAbsolutePath() + '/';
			for (String s : FL) {
				File test = new File(absPath + s);
				if (test.isDirectory()) {						// If file is a directory
					dirs.add(s + dirMark);						// mark it and add it to display list
				} else {
					files.add(s);								// else add name without the directory mark
				}
			}
			Collections.sort(dirs);								// Sort the directory list
			Collections.sort(files);							// Sort the file list
			dirs.addAll(files);									// copy the file list to end of dir list
		}
		int length = dirs.size();								// number of directories and files in list
		if (length == 0) { length = 1; }						// make at least one element if dir is empty
																// it will be an empty string
		return ListToBasicStringArray(var, dirs, length);		// Copy the list to a BASIC! array
	}

	private boolean executeGRABFILE() {
		clearErrorMsg();
		if (!getSVar())					return false;				// First parm is string var
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// Second parm is the filename
		String theFileName = StringConstant;

		boolean textFlag = false;									// Default: assume ASCII/binary
		if (isNext(',')) {											// Optional third parm
			if (!evalNumericExpression()) return false;
			textFlag = (EvalNumericExpressionValue != 0.0);			// is the text flag: unicode if non-zero
		}
		if (!checkEOL())				return false;
		if (!checkSDCARD('r'))			return false;

		BufferedInputStream bis = null;
		String result = "";
		IOException ioex = null;
		Exception ex = null;

		try {
			bis = Basic.getBufferedInputStream(Basic.DATA_DIR, theFileName);
			if (bis != null) { result = grabStream(bis, textFlag); }
			else             { writeErrorMsg("File not found"); }
		}
		catch (FileNotFoundException fnfe) {
			writeErrorMsg("File not found");
		}
		catch (IOException ie) { ioex = ie; }
		catch (NotFoundException nfe) {
			writeErrorMsg("Resource not found");
		}
		catch (Exception e) { ex = e; }
		finally {
			ioex = FileInfo.closeStream(bis, ioex);
			if (ioex != null) { return RunTimeError(ioex); }		// Report first exception, if any, and if no previous RTE set
			if (ex != null) { return RunTimeError(ex); }
		}
		val.val(result);
		return true;
	}

	private boolean executeGRABURL() {
		if (!getSVar())					return false;				// First parm is string var
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// Second parm is the url

		int timeoutMillis = 0;										// Default: assume infinite timeout
		if (isNext(',')) {											// Optional third parm
			if (!evalNumericExpression()) return false;
			timeoutMillis = EvalNumericExpressionValue.intValue();	// is the timeout: infinite if 0
			if (timeoutMillis < 0) { timeoutMillis *= -1; }			// negative value would throw an exception
		}
		if (!checkEOL())				return false;

		BufferedInputStream bis = null;
		String result = "";

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
			if (bis != null) { result = grabStream(bis, true); }	// Read as encoded text stream, not byte stream
			else             { writeErrorMsg("Problem opening or reading URL"); }
// Alternate implementation: uncomment this catch block to handle Timeout explicitly.
//		} catch (SocketTimeoutException ste) {						// Connect or Read timeout
//			result = "";											// Empty result, finally will close stream but will not return
		} catch (Exception e) {										// Report exception in run time error
			writeErrorMsg(e);
		} finally {
			IOException ex = FileInfo.closeStream(bis, null);		// Close stream if not already closed
			if (ex != null) { return RunTimeError(ex); }			// Report first exception, if any, and if no previous RTE set
		}
		val.val(result);
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
			return new String(byteArray.toByteArray());			// Text: keep full two-byte encoding, assumes input is UTF-8
		} else {
			return new String(byteArray.toByteArray(), "ISO-8859-1");// ASCII or binary: force upper byte 0
		}
	}

	// ************************************** Time and TimeZone commands **************************

	private boolean executeTIME() {								// Get the date and time
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
			if (getSVar()) {									// Commas hold places for up to six svars.
				mVal.val(theTime[i]);							// If svar, use it; if nothing, skip to next comma.
			}
		} while ((++i < 6) && isNext(','));						// Anything else will get caught by checkEOL
		if (isNext(',') && getNVar()) {							// Another comma holds a place for an optional nvar
			double weekDay = time.weekDay + 1;					// for day of week: 1 is Sunday
			mVal.val(weekDay);
		}
		if (isNext(',') && getNVar()) {							// Another comma holds a place for an optional nvar
																// For Daylight Saving Time flag
			mVal.val(Math.signum(time.isDst));					// 1 yes, 0 no, -1 unknown
		}
		return checkEOL();
	}

	private boolean executeTIMEZONE() {							// Get TimeZone command keyword if it is there
		return executeSubcommand(TimeZone_cmd, "TimeZone");
	}

	private boolean executeTIMEZONE_SET() {						// Set a global Time Zone string for TIME and TIME(
		String zone = Time.getCurrentTimezone();				// default to local time zone
		if (getStringArg()) {
			TimeZone tz = TimeZone.getTimeZone(StringConstant);	// if arg, use it as TimeZone ID
			zone = tz.getID();									// read back ID, "GMT" if user-string invalid
		}
		if (!checkEOL())				return false;
		theTimeZone = zone;
		return true;
	}

	private boolean executeTIMEZONE_GET() {						// Get the time zone setting
		if (!(getSVar() && checkEOL()))	return false;
		String zone = theTimeZone;
		if (zone.equals("")) {
			zone = Time.getCurrentTimezone();					// If user never set a time zone, use local
		}
		mVal.val(zone);
		return true;
	}

	private boolean executeTIMEZONE_LIST() {					// Get a list of all valid time zone strings
		int listIndex = getListArg(Var.Type.STR);				// get a reusable List pointer - may create new list
		if (listIndex < 0)				return false;			// failed to get or create a list
		if (!checkEOL())				return false;

		ArrayList<String> theList = new ArrayList<String>();
		theLists.set(listIndex, theList);
		for (String zone : TimeZone.getAvailableIDs()) {		// get all the zones the system knows
			theList.add(zone);									// put them in the list
		}
		return true;
	}

	// ************************************** Miscellaneous Non-core commands **************************

	private boolean executeBACK_RESUME() {
		return doResume("Back key not hit");
	}

	private boolean executeKB_RESUME() {
		return doResume("No keyboard change");
	}

	private boolean executeLOWMEM_RESUME() {
		return doResume("No Low Memory signal");
	}

	private boolean executeMENUKEY_RESUME() {
		return doResume("Menu key not hit");
	}

	private boolean executePAUSE() {
		if (!evalNumericExpression()) return false;							// Get pause duration value
		if (!checkEOL()) return false;
		long dur = EvalNumericExpressionValue.longValue();
		if (dur < 1) {
			return RunTimeError("Pause must be greater than zero");
		}

		try { Thread.sleep(dur); } catch (InterruptedException e) {}
		return true;
	}

	private boolean executeBROWSE() {

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
		Var.Val val = mVal;
		if (!checkEOL()) return false;
		val.val(InChar.isEmpty() ? "@" : InChar.remove(0));
		return true;
	}

	private boolean executeKEY_RESUME() {
		return doResume("No Current Key Interrupt");
	}

	private boolean executePOPUP() {
		if (!getStringArg()) return false;				// get the message
		String msg = StringConstant;
		int[] args = { 0, 0, 0 };						// default x, y, duration
		if (isNext(',')) {								// any optional args?
			if (!getOptExprs(args)) return false;		// get the optional args
			args[2] = (args[2] == 0) ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;	// convert boolean to int
		}
		if (!checkEOL()) return false;

		Message m = mHandler.obtainMessage(MESSAGE_TOAST, args[0], args[1], msg);
		// If duration is Long, send it in a Bundle, otherwise let message default to SHORT.
		if (args[2] == Toast.LENGTH_LONG) {
			Bundle b = new Bundle();
			b.putInt("dur", args[2]);
			m.setData(b);
		}
		m.sendToTarget();								// tell the UI thread to pop the toast
		return true;
	}

	public boolean executeCLS() {						// Clear Screen
		if (!checkEOL()) return false;
		sendMessage(MESSAGE_CLEAR_CONSOLE);				// tell the UI thread to clear the Console
		return true;
	}

	private boolean parseSelect(DialogArgs args, ArrayList<String> selectList) {	// get SELECT parameters from program line

		if (!getNVar()) return false;								// get the var to put the key value into
		Var.Val returnVal = mVal;
		if (!isNext(',')) return false;

		int saveLineIndex = LineIndex;
		Var var = getVarAndType();
		if ((var != null) && var.isArray()) {
			if (!isNext(']'))	{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); } // Array must not have any indices
			if (var.isNumeric()){ return RunTimeError(EXPECT_STRING_ARRAY); }
			if (var.isNew())	{ return RunTimeError(EXPECT_ARRAY_EXISTS); }

			Var.ArrayDef array = var.arrayDef();					// get the array
			int length = array.length();							// get the array length
			for (int i = 0; i < length; ++i) {						// copy the array values into the list
				selectList.add(array.sval(i));						// Caution! No range checking!
			}
		} else {
			LineIndex = saveLineIndex;
			if (!evalNumericExpression()) return false;

			int listIndex = EvalNumericExpressionValue.intValue();
			if ((listIndex < 1) || (listIndex >= theLists.size())) {
				return RunTimeError("Invalid List Pointer");
			}
			if (theListsType.get(listIndex) != Var.Type.STR) {
				return RunTimeError("Not a string list");
			}
			selectList.addAll(theLists.get(listIndex));
		}

		String title = null;										// set defaults
		String msg = null;
		Var.Val isLongClickVal = null;

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
				isLongClickVal = mVal;
			}
		}
		if (!checkEOL()) return false;

		args.mTitle = title;
		args.mMessage = msg;
		args.mReturnVal = returnVal;
		args.mLongClickVal = isLongClickVal;
		return true;
	}

	private boolean executeSELECT() {
		DialogArgs args = new DialogArgs();
		ArrayList<String> selectList = new ArrayList<String>();
		if (!parseSelect(args, selectList)) return false;

		String title = args.mTitle;									// default null
		String msg = args.mMessage;									// default null

		SelectedItem = 0;											// intialize return values
		SelectLongClick = false;
		mWaitForLock = true;

		Intent intent = new Intent(Run.this, Select.class);
		if (title != null) { intent.putExtra(Select.EXTRA_TITLE, title); }
		if (msg != null)   { intent.putExtra(Select.EXTRA_MSG, msg); }
		intent.putStringArrayListExtra(Select.EXTRA_LIST, selectList);
		startActivityForResult(intent, BASIC_GENERAL_INTENT);

		waitForLOCK();												// Wait for signal from Selected.java thread

		Var.Val val = args.mReturnVal;
		if (val != null) { val.val(SelectedItem); }					// Set the return value

		val = args.mLongClickVal;
		if (val != null) { val.val(SelectLongClick ? 1 : 0); }		// Set the LongClick return value

		return true;
	}

	private boolean executeSPLIT(int limit) {

		Var var = getArrayVarForWrite(TYPE_STRING);					// get the result array variable
		if (var == null)				return false;				// must name a new string array variable

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;				// Get the string to split
		String SearchString = StringConstant;

		String r[] = doSplit(SearchString, limit);					// Get regex arg, if any, and split the string.
		if (!checkEOL())				return false;

		int length = r.length;										// Get the number of strings generated
		if (length == 0)				return false;				// error in doSplit()

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

	private boolean executeJOIN(boolean keepAll) {					// opposite of SPLIT
																	// if keepAll is true, keep empty array elements
		Var var = getExistingArrayVar();							// get the array whose elements will be joined
		if (var == null)				return false;
		if (var.isNumeric()) { return RunTimeError(EXPECT_STRING_ARRAY); }

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// Get values inside [], if any

		if (!isNext(','))				return false;
		if (!getSVar())					return false;				// get the string variable to hold the result
		Var.Val val = mVal;

		String args[] = { "", "" };									// optional separator and wrapper
		if (isNext(',')) {											// any optional args?
			if (!getOptExprs(args))		return false;				// get the optional args
		}
		if (!checkEOL())				return false;

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();
		String[] array = arrayDef.getStrArray();					// raw array access

		String sep = args[0];
		String wrap = args[1];
		StringBuilder result = new StringBuilder(wrap);				// start with wrapper
		boolean doSep = false;
		for (int i = 0; i < length; ++i) {
			String s = array[base + i];								// get each array element
			if ((s.length() == 0) && !keepAll) continue;			// skip empty strings if told to do so

			if (doSep) { result.append(sep); }						// separator before all but first substring
			result.append(s);										// append substring
			doSep = true;
		}
		result.append(wrap);										// end with wrapper
		val.val(result.toString());

		return true;
	}

	// ************************************ Keyboard Commands *************************************

	private boolean executeKB() {								// Get kb command keyword if it is there
		return executeSubcommand(KB_cmd, "KB");					// and execute the command
	}

	private KeyboardManager getKeyboardManager() {
		KeyboardManager kb = GRFront ? GR.drawView.mKB : lv.mKB;
		if (kb == null) { RunTimeError("No keyboard manager"); }
		return kb;
	}

	private boolean executeKB_TOGGLE() {
		if (!checkEOL())				return false;
		KeyboardManager kb = getKeyboardManager();
		return (kb != null) && (kb.showing() ? kbHide(kb) : kbShow(kb));
	}

	private boolean executeKB_HIDE() {
		if (!checkEOL())				return false;
		return kbHide(getKeyboardManager());
	}

	private boolean kbHide(KeyboardManager kb) {
		if (kb == null)					return false;
		kb.hide();
		return true;									// even if kb.hide() return false
	}

	private boolean executeKB_SHOW() {
		if (!checkEOL())				return false;
		return kbShow(getKeyboardManager());
	}

	private boolean kbShow(KeyboardManager kb) {
		if (kb == null)					return false;
		if (!kb.show()) {
			checkpointMessage();						// allow any pending Console activity to complete
			while (mMessagePending) { Thread.yield(); }	// wait for checkpointMessage semaphore to clear

			if (mConsole.getCount() == 0) {
				return RunTimeError("You must PRINT before you can show the soft keyboard");
			}
		}
		return true;
	}

	private boolean executeKB_SHOWING() {
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		KeyboardManager kb = getKeyboardManager();
		if (kb == null)					return false;
		val.val(kb.showing() ? 1.0 : 0.0);
		return true;
	}

	// ********************************************************************************************

	private boolean executeWAKELOCK() {
		if (!evalNumericExpression())	return false;				// Get setting
		int code = EvalNumericExpressionValue.intValue();
		int flags = 0;												// optional flags, default none
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;				// get flags
			int rawFlags = EvalNumericExpressionValue.intValue() & 0x03;	// ignore undefined values
			if ((rawFlags & 1) == 1) { flags |= PowerManager.ACQUIRE_CAUSES_WAKEUP; }
			if ((rawFlags & 2) == 2) { flags |= PowerManager.ON_AFTER_RELEASE; }
		}
		if (!checkEOL())				return false;

		if (theWakeLock != null) {
			theWakeLock.release();
			theWakeLock = null;
		}

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		String tag = "BASIC!";
		switch (code) {
			case partial:
				if (flags != 0) { flags = 0; }						// can't use flags with partial
				theWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, tag);
				theWakeLock.acquire();
				break;
			case dim:
				theWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK | flags, tag);
				theWakeLock.acquire();
				break;
			case bright:
				theWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | flags, tag);
				theWakeLock.acquire();
				break;
			case full:
				theWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | flags, tag);
				theWakeLock.acquire();
				break;
			case release:
				break;
			default:
				return RunTimeError("WakeLock code not 1 - 5");
		}

		return true;
	}

	private boolean executeWIFI_INFO() {
		if (isEOL())					return true;		// user asked for no data

		// First three return variables are strings. The IP address may be either string or numeric.
		// The last variable is numeric.
		byte[] types = { 2, 2, 2, 3, 1 };					// type of each variable
		int nArgs = types.length;
		Var.Val[] args = new Var.Val[nArgs];				// Val object for each variable
		WifiInfo wi = null;
		if (!getOptVars(types, args))	return false;

		try {
			WifiManager wm = (WifiManager)getSystemService(Context.WIFI_SERVICE);
			wi = wm.getConnectionInfo();
		} catch (Exception e) {
			Log.d(LOGTAG, e.toString());
			return RunTimeError("Cannot get WifiInfo:", e);
		}

		int arg = 0;
		if (args[arg] != null)   { args[arg].val(wi.getSSID()); }
		if (args[++arg] != null) { args[arg].val(wi.getBSSID()); }
		if (args[++arg] != null) { args[arg].val(wi.getMacAddress()); }
		if (args[++arg] != null) {
			Var.Val val = args[arg];						// IP address variable
			int ip = wi.getIpAddress();
			if (types[arg] == 1) {							// IP address variable is numeric
				val.val(ip);
			} else {										// convert to string
				String ipString = "";
				byte[] ipbytes = { (byte)(ip), (byte)(ip>>>8), (byte)(ip>>>16), (byte)(ip>>>24) };
				try { ipString = InetAddress.getByAddress(ipbytes).getHostAddress(); }
				catch (Exception e) { /* can't happen */ }
				val.val(ipString);
			}
		}
		if (args[++arg] != null) { args[arg].val(wi.getLinkSpeed()); }
		return (++arg == nArgs);							// sanity-check arg count
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	private boolean executeWIFILOCK() {
		if (!evalNumericExpression())	return false;				// Get setting
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

	private boolean executeTONE() {

		double duration = 1;							// seconds
		double freqOfTone = 1000;						// hz
		int sampleRate = 8000;							// a number

		if (!evalNumericExpression())	return false;	// Get frequency
		freqOfTone = EvalNumericExpressionValue;

		if (!isNext(','))				return false;

		if (!evalNumericExpression())	return false;	// Get duration
		duration= EvalNumericExpressionValue / 1000;

		double dnumSamples = duration * sampleRate;
		dnumSamples = Math.ceil(dnumSamples);
		int numSamples = (int) dnumSamples;
		double sample[] = new double[numSamples];
		ByteBuffer generatedSnd = null;
		try { generatedSnd = ByteBuffer.allocate(2 * numSamples); }
		catch (OutOfMemoryError oom) { return RunTimeError(oom); }

		generatedSnd.order(ByteOrder.LITTLE_ENDIAN);
		ShortBuffer shortView = generatedSnd.asShortBuffer();

		boolean flagMinBuff = true;						// Optionally skip checking min buffer size
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			if (EvalNumericExpressionValue == 0) { flagMinBuff = false; }
		}

		if (flagMinBuff) {
			int minBuffer = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO,
							AudioFormat.ENCODING_PCM_16BIT);
			if (2 * numSamples < minBuffer) {
				double minDuration = Math.ceil(1000 * (double)minBuffer/(2 * (double)sampleRate));
				return RunTimeError("Minimum tone duration for this device: " + (int) minDuration + " milliseconds");
			}
		}

		if (!checkEOL())				return false;	// No more parameters expected

		for (int i = 0; i < numSamples; ++i) {			// Fill the sample array
			sample[i] = Math.sin(freqOfTone * 2 * Math.PI * i / (sampleRate));
		}

		// convert to 16 bit pcm sound array
		// assumes the sample buffer is normalised.
		int i = 0;

		int ramp = numSamples / 20 ;					// Amplitude ramp as a percent of sample count

		for (i = 0; i< ramp; ++i) {						// Ramp amplitude up to max (to avoid clicks)
			short val = (short) (sample[i] * 32767 * i/ramp);
			shortView.put(val);
		}

		for ( ; i< numSamples - ramp; ++i) {			// Max amplitude for most of the samples
			short val = (short) (sample[i] * 32767);	// scale to maximum amplitude
			shortView.put(val);
		}

		for ( ; i< numSamples; ++i) {					// Ramp amplitude down to 0
			short val = (short) (sample[i] * 32767 * (numSamples-i)/ramp);
			shortView.put(val);
		}

		AudioTrack audioTrack = null;					// Get audio track
		try {
			audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
										sampleRate, AudioFormat.CHANNEL_OUT_MONO,
										AudioFormat.ENCODING_PCM_16BIT, numSamples*2,
										AudioTrack.MODE_STATIC);
			audioTrack.write(generatedSnd.array(), 0, numSamples*2);	// Load the track
			audioTrack.play();											// Play the track
		}
		catch (Exception e) { return RunTimeError(e); }

		int x = 0;
		do {											// Monitor playback to find when done
			x = (audioTrack != null) ? audioTrack.getPlaybackHeadPosition() : numSamples;
		} while (x < numSamples);

		if (audioTrack != null) audioTrack.release();	// Track play done. Release track.

		audioTrack = null;								// Release storage
		shortView = null;
		generatedSnd = null;
		sample = null;

		return true;
	}

	private boolean executeVIBRATE() {

		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;
		if (!var.isNumeric()) { return RunTimeError(EXPECT_NUM_ARRAY); } // Insure that it is a numeric array

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// get values inside [], if any

		if (!isNext(','))				return false;				// get the repeat value
		if(!evalNumericExpression())	return false;
		int repeat = EvalNumericExpressionValue.intValue();
		if (!checkEOL())				return false;

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();
		double[] array = arrayDef.getNumArray();					// raw array access

		long Pattern[] = new long[length];							// pattern array
		for (int i = 0; i < length; ++i) {							// copy user array into pattern
			Pattern[i] = (long)array[base + i];
		}

		try {
			if (myVib == null) {									// if no vibrator, go get it
				myVib = (Vibrator)Run.this.getSystemService(VIBRATOR_SERVICE);
			}
			if (repeat > 0) myVib.cancel();
			else myVib.vibrate(Pattern, repeat);					// do the vibrate
		} catch (SecurityException ex) {
			Log.d(LOGTAG, "SecurityException on VIBRATE: do you have android.permission.VIBRATE in your Manifest?");
			myVib = null;
			return RunTimeError("Your app is not permitted to use VIBRATE.");
		}
		return true;
	}

	private void cancelVibrator() {
		if (myVib != null) {
			myVib.cancel();
			myVib = null;
		}
	}

	private boolean executeDEVICE() {					// DEVICE or DEVICE.xxx
		if (isNext('.')) {
			return executeSubcommand(Device_cmd, "Device");
		}
		return executeDEVICE_all();
	}

	private boolean executeDEVICE(String str) {			// write the string argument into a variable
		if (!getSVar() || !checkEOL())	return false;	// variable to hold the returned Brand string
		Var.Val val = mVal;
		val.val((str != null) ?  str : "");
		return true;
	}

	private boolean executeDEVICE_all() {
		if (!getVar())					return false;	// variable to hold the returned bundle pointer or string value
		Var.Val val = mVal;

		Locale loc = Locale.getDefault();
		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String failMsg = "Not available";
		String[] keys = {
			"Brand", "Model", "Device", "Product", "OS",
			"Language", "Locale",
			"PhoneType", "PhoneNumber", "DeviceID",
			"SIM SN", "SIM MCC/MNC", "SIM Provider"
		};
		String[] vals = {
			Build.BRAND, Build.MODEL, Build.DEVICE, Build.PRODUCT, Build.VERSION.RELEASE,
			loc.getDisplayLanguage(), loc.toString(),
			getPhoneType(tm), getPhoneNumber(tm, failMsg),
			getDeviceID(tm, failMsg), getSimSN(tm, failMsg),
			getSimOperator(tm, failMsg), getSimOpName(tm, failMsg)
		};

		int len = keys.length;
		int i = 0;
		if (val.isNumeric()) {										// bundle format
			Bundle b = new Bundle();
			int bundleIndex = theBundles.size();
			theBundles.add(b);
			for (; i < len; ++i) {
				b.putString(keys[i], vals[i]);
			}
			val.val(bundleIndex);
		} else {													// string format
			StringBuilder s = new StringBuilder();
			while (true) {
				s.append(keys[i]).append(" = ").append(vals[i]);
				if (++i == len) break;
				s.append('\n');
			}
			val.val(s.toString());
		}
		return true;
	}

	// ************************************* Screen Commands **************************************

	private boolean executeSCREEN() {							// Get SCREEN command keyword if it is there
		return executeSubcommand(screen_cmd, "Screen");			// and execute the command
	}

	private boolean executeSCREEN_ROTATION() {
		if (!getNVar())					return false;				// get var for rotation
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
		val.val(getScreenRotation(display));
		return true;
	}

	private boolean executeSCREEN_SIZE() {
		if (isEOL()) return true;									// no arguments

		Var sizeVar = null;											// return array for size
		Var realVar = null;											// return array for realsize
		Var.Val densVal = null;										// return variable for density

		boolean isComma = isNext(',');
		if (!isComma) {
			sizeVar = getArrayVarForWrite(TYPE_NUMERIC);			// get array for size
			if (sizeVar == null)		return false;				// must name a numeric array variable
			isComma = isNext(',');
		}
		if (isComma) {
			isComma = isNext(',');
			if (!isComma) {
				realVar = getArrayVarForWrite(TYPE_NUMERIC);		// get array for realsize
				if (realVar == null)	return false;				// must name a numeric array variable
				isComma = isNext(',');
			}
		}
		if (isComma) {
			isComma = isNext(',');
			if (!isComma) {
				if (!getNVar())			return false;					// get var for density
				densVal = mVal;
			}
		}
		if (!checkEOL())				return false;			// have all parameters

		Display display = ((Activity)getContext()).getWindowManager().getDefaultDisplay();
		if ((sizeVar != null) || (realVar != null)) {
			if (!getScreenSize(display, sizeVar, realVar)) return false;
		}
		if (densVal != null) {
			densVal.val(getScreenDensity(display));
		}
		return true;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public int getScreenRotation(Display display) {
		return (Build.VERSION.SDK_INT < 8) ? display.getOrientation() : display.getRotation();
	}

	public int getScreenDensity(Display display) {
		DisplayMetrics dm = new DisplayMetrics();
		display.getMetrics(dm);
		return dm.densityDpi;
	}

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public boolean getScreenSize(Display display, Var size, Var realsize) {
		Point pSize = new Point();
		if (Build.VERSION.SDK_INT < 13) {
			pSize.set(display.getWidth(), display.getHeight());
		} else {
			display.getSize(pSize);										// "application" screen size
		}
		if (size != null) {
			if (!BuildBasicArray(size, 2))		return false;			// build the array
			double[] array = size.arrayDef().getNumArray();				// raw array access
			array[0] = pSize.x; array[1] = pSize.y;						// stuff the array
		}
		if (realsize != null) {
			if (Build.VERSION.SDK_INT >= 17) {
				display.getRealSize(pSize);								// "real" screen size
			}
			if (!BuildBasicArray(realsize, 2))	return false;			// build the array
			double[] array = realsize.arrayDef().getNumArray();			// raw array access
			array[0] = pSize.x; array[1] = pSize.y;						// stuff the array
		}
		return true;
	}

	private boolean executeHTTP_POST() {
		if (!getStringArg())			return false;
		String url = StringConstant;
		if (!isNext(','))				return false;

		if (!evalNumericExpression())	return false;
		int theListIndex = EvalNumericExpressionValue.intValue();
		if (theListIndex < 1 || theListIndex >= theLists.size()) {
			return RunTimeError("Invalid list pointer");
		}
		if (theListsType.get(theListIndex) != Var.Type.STR) {
			return RunTimeError("List must be of string type.");
		}

		List<String> thisList = theLists.get(theListIndex);
		int r = thisList.size() % 2;
		if (r != 0) {
			return RunTimeError("List must have even number of elements");
		}

		if (!isNext(','))				return false;
		if (!getSVar())					return false;
		Var.Val val = mVal;											// variable to hold Result
		if (!checkEOL())				return false;

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		for (int i = 0; i < thisList.size(); ++i) {
			nameValuePairs.add(new BasicNameValuePair(thisList.get(i), thisList.get(++i)));
		}

		String Result = "";
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
//			HttpResponse response = client.execute(post);

			ResponseHandler<String> responseHandler=new BasicResponseHandler();
			Result = client.execute(post, responseHandler);

		} catch (Exception e) {
			return RunTimeError("!", e);
		}

		val.val(Result);
		return true;
	}

	// ************************************************ SQL Package ***************************************

	private boolean executeSQL() {									// Get SQL command keyword if it is there
		return executeSubcommand(SQL_cmd, "SQL");
	}

	private boolean getDbPtrArg() {									// first arg of command is DB Pointer Variable
																	// "return value" is global theValueIndex
		String errStr = "Database not opened at:";
		if (DataBases.isEmpty()) {
			return RunTimeError(errStr);
		}
		if (!getNVar())					return false;				// variable that holds the DB table pointer
		int i = (int)mVal.nval();
		if (i == 0) {												// if pointer is zero
			return RunTimeError(errStr);							// DB has been closed
		}
		if (i < 0 || i > DataBases.size()) {
			return RunTimeError("Invalid Database Pointer at:");
		}
		return true;
	}

	private boolean getVarAndDbPtrArgs(Var.Val[] args) {			// first arg of command is a numeric variable
																	// and second is a DB table pointer expression
		String errStr = "Database not opened at:";
		if (DataBases.isEmpty()) {
			return RunTimeError(errStr);
		}
		if (!getNVar())					return false;				// user's nvar
		args[0] = mVal;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// expression that specifies the DB table pointer
		int db = EvalNumericExpressionValue.intValue();
		args[1] = new Var.NumVal(db);								// create a temporary Val to hold the value

		if (db == 0) {												// if pointer is zero
			return RunTimeError(errStr);							// DB has been closed
		}
		if (db < 0 || db > DataBases.size()) {
			return RunTimeError("Invalid Database Pointer at:");
		}
		return true;
	}

	private boolean getVarAndCursorPtrArgs(Var.Val[] args) {		// first arg of command is a numeric variable
																	// and second is a DB cursor pointer
		if (Cursors.isEmpty()) {									// Make sure there is at least one cursor
			return RunTimeError("Cursor not available at:");
		}
		if (!getNVar())					return false;				// user's nvar
		args[0] = mVal;

		if (!isNext(','))				return false;
		if (!getNVar())					return false;				// the DB cursor pointer
		args[1] = mVal;

		int i = (int)mVal.nval();
		if (i == 0) {												// If pointer is zero
			return RunTimeError("Cursor done at:");					// then cursor is used up
		}
		if (i < 0 || i > Cursors.size()) {
			return RunTimeError("Invalid Cursor Pointer at:");
		}
		return true;
	}

	private boolean getColumnValuePairs(ContentValues values) {		// Get column/value pairs from user command
		if (!isNext(','))				return false;
		do {
			if (!getStringArg())		return false;				// Column
			String Column = StringConstant;
			if (!isNext(',') || !getStringArg()) return false;		// Value
			String Value = StringConstant;
			values.put(Column, Value);								// Store the pair
		} while (isNext(','));
		return true;
	}

	private boolean execute_sql_open() {

		if (!getNVar())					return false;			// DB Pointer Variable
		Var.Val val = mVal;										// for the DB table pointer

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Get Data Base Name
		String DBname = StringConstant;
		if (!checkEOL())				return false;

		String fn;
		if (DBname.startsWith(":")) {
			fn = DBname;
		} else {
			if (!checkSDCARD('w'))		return false;
			fn = Basic.getDataBasePath(DBname);
		}

		SQLiteDatabase db;
		try {													// Do the open or create
			db = SQLiteDatabase.openOrCreateDatabase(new File(fn), null );
		} catch  (Exception e) {
			return RunTimeError("SQL Exception:", e);
		}

		// The newly opened data base is added to the DataBases list.
		// The list index of the new data base is added returned to the user

		val.val(DataBases.size() + 1);
		DataBases.add(db);
		return true;
	}

	private boolean execute_sql_close() {

		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		int i = (int)val.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base
		try { db.close(); }										// Try closing it
		catch (Exception e) { return RunTimeError("SQL Exception:", e); }

		val.val(0.0);											// Set the pointer to 0 to indicate closed.
		return true;
	}

	private boolean execute_sql_insert() {

		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		int i = (int)mVal.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Table Name
		String TableName = StringConstant;

		ContentValues values = new ContentValues();
		if (!getColumnValuePairs(values)) return false;			// Get column/value pairs from user command

		if (!checkEOL())				return false;

		try { db.insertOrThrow(TableName, null, values); }		// Now insert the pairs into the named table
		catch (Exception e) { return RunTimeError("SQL Exception:", e); }

		return true;
	}

	private boolean execute_sql_query() {

		Var.Val[] args = new Var.Val[2];						// get the first two args:
		if (!getVarAndDbPtrArgs(args))	return false;
		Var.Val cursorVar = args[0];							// Query Cursor Variable
		int dbPtr = (int)args[1].nval();						// one-based DB table pointer
		SQLiteDatabase db = DataBases.get(dbPtr - 1);			// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Table Name
		String TableName = StringConstant;

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// String of comma separated columns to get
		String RawColumns = StringConstant;

																// Must convert string to an array of columns
		ArrayList<String> cList = new ArrayList<String>();		// start by creating an ArrayList for column names

		String cTemp = "";										// Parse the Raw Columns
		for (int j = 0; j < RawColumns.length(); ++j) {
			char t = RawColumns.charAt(j);
			if (t != ',') {										// while tossing out blanks
				if ( t != ' ') { cTemp += t;}					// add characters to the column name
			} else {
				cList.add(cTemp);								// comma terminates a column name, add name to array list
				cTemp = "";										// and start a new column name
			}
		}
		cList.add(cTemp);										// add last column to the list

		String []Q_Columns = new String[cList.size()];			// Finally, convert the array list
		cList.toArray(Q_Columns);								// to a String Array.

		String Where = "";										// if no Where given, set empty
		String Order = "";										// if no Order given, set empty

		if (isNext(',')) {										// if no comma, then no optional Where
			if (!getStringArg())		return false;			// Where Value
			Where = StringConstant;

			if (isNext(',')) {									// if no comma, then no optional Order
				if (!getStringArg())	return false;			// Order Value
				Order = StringConstant;
			}
		}
		if (!checkEOL())				return false;

		Cursor cursor;
		try {													// Do the query and get the cursor
			cursor = db.query(TableName, Q_Columns, Where, null, null, null, Order);
		} catch (Exception e) {
			return RunTimeError("SQL Exception:", e);
		}

		cursorVar.val(Cursors.size() + 1);						// save the Cursor index into the var
		Cursors.add(cursor);									// and save the cursor.

		return true;
	}

	// Extended or unknown number of columns by GitHub poster @JasonFruit. See Issue #141.
	private boolean execute_sql_next() {						// get data from next row of cursor
		Var.Val[] args = new Var.Val[2];						// get the first two args:
		if (!getVarAndCursorPtrArgs(args)) return false;
		Var.Val doneVar = args[0];								// Done Flag variable
		Var.Val cursorVar = args[1];							// DB Cursor pointer variable

		ArrayList<Var> vars = new ArrayList<Var>();
		if (isNext(',')) {										// if comma, expect vars to receive column data
			if (!getSVars(vars))		return false;			// get list of string vars to hold column(s), last may be array
		}
		int nVars = vars.size();
		Var colArrayVar = (nVars != 0) ? vars.get(nVars - 1) : null;		// last var MAY be array
		boolean isArray = (colArrayVar != null) && colArrayVar.isArray();	// is it?

		Var.Val nColVal = null;									// for optional column count
		if (isArray) {
			--nVars;											// count of non-array vars
			if (isNext(',')) {
				if (!getNVar())			return false;			// get optional column count var
				nColVal = mVal;
			}
		}
		if (!checkEOL())				return false;

		doneVar.val(0.0);										// set Not Done
		int i = (int)cursorVar.nval();							// get the cursor pointer
		Cursor cursor = Cursors.get(i - 1);						// get the cursor

		if (cursor.moveToNext()) {								// if there is another row, go there
			int nCols = cursor.getColumnCount();				// num columns of data available
			if (nColVal != null) { nColVal.val(nCols); }		// if requested, return total number of columns

			if (nCols < nVars)						{ nVars = nCols; } // don't try to store more columns than we have 
			else if (!isArray && (nVars < nCols))	{ nCols = nVars; } // don't read more columns than we can return

			ArrayList<String> colData = new ArrayList<String>(nCols);
			for (int index = 0; index < nCols; ++index) {
				String result;
				try { result = cursor.getString(index); }		// get the result
				catch (Exception e) { return RunTimeError("SQL Exception:", e); }
				colData.add((result != null) ? result : "");
			}

			for (int index = 0; index < nVars; ++index) {		// write column data to scalar vars
				vars.get(index).val().val(colData.remove(0));
			}
			if (isArray) {										// write rest of column data to array var
				if (colData.size() == 0) { colData.add(""); }	// don't allow empty array
				if (!ListToBasicStringArray(colArrayVar, colData, colData.size())) return false;
			}
		} else {												// no next row, cursor is used up
			cursor.close();
			doneVar.val(1.0);
			cursorVar.val(0.0);
		}
		return true;
	}

	private boolean execute_sql_query_length() {				// Report the number of rows in a query result
		Var.Val[] args = new Var.Val[2];						// Get the first two args:
		if (!getVarAndCursorPtrArgs(args)) return false;
		Var.Val resultVar = args[0];							// variable for number of rows
		Var.Val cursorVar = args[1];							// DB Cursor pointer variable
		if (!checkEOL())				return false;

		int i = (int)cursorVar.nval();							// get the cursor pointer
		Cursor cursor = Cursors.get(i - 1);						// get the cursor
		double nRows = cursor.getCount();
		resultVar.val(nRows);									// return number of rows to user
		return true;
	}

	private boolean execute_sql_query_position() {				// Report current position in query results
		Var.Val[] args = new Var.Val[2];						// Get the first two args:
		if (!getVarAndCursorPtrArgs(args)) return false;
		Var.Val resultVar = args[0];							// variable for position
		Var.Val cursorVar = args[1];							// DB Cursor pointer variable
		if (!checkEOL())				return false;

		int i = (int)cursorVar.nval();							// get the cursor pointer
		Cursor cursor = Cursors.get(i - 1);						// get the cursor
		double position = cursor.getPosition();
		resultVar.val(position + 1);							// return position to user, 1-based
		return true;
	}

	private boolean execute_sql_delete() {

		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		int i = (int)mVal.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Table Name
		String TableName = StringConstant;

		Var.Val resultVar = null;
		String Where = null;									// if no Where given, set null
		if (isNext(',')) {										// if no comma, then no optional Where
			if (!getStringArg())		return false;			// Where Value
			Where = StringConstant;
			if (isNext(',')) {									// if there's a where
				if (!getNVar())			return false;			// there can be a return value
				resultVar = mVal;
			}
		}
		if (!checkEOL())				return false;

		int count = 0;
		try {
			count = db.delete(TableName, Where, null);			// do the deletes
		} catch (Exception e) {
			return RunTimeError("SQL Exception:", e);
		}

		if (resultVar != null) {
			resultVar.val(count);								// return the number of rows deleted
		}
		return true;
	}

	private boolean execute_sql_update() {

		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		int i = (int)mVal.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Table Name
		String TableName = StringConstant;

		ContentValues values = new ContentValues();
		if (!getColumnValuePairs(values)) return false;			// Get column/value pairs from user command

		String Where = null;									// Where is optional
		if (isNext(':')) {										// Colon indicates Where follows
			if (!getStringArg())		return false;			// Where Value
			Where = StringConstant;
		}
		if (!checkEOL())				return false;

		try { db.update(TableName, values, Where, null); }
		catch (Exception e) { return RunTimeError("SQL Exception:", e); }
		return true;
	}

	private boolean execute_sql_exec() {
		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		int i = (int)mVal.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Command string
		String CommandString = StringConstant;
		if (!checkEOL())				return false;

		try { db.execSQL(CommandString); }
		catch (Exception e) { return RunTimeError("SQL Exception:", e); }
		return true;
	}

	private boolean execute_sql_raw_query() {

		Var.Val[] args = new Var.Val[2];						// get the first two args:
		if (!getVarAndDbPtrArgs(args))	return false;
		Var.Val cursorVar = args[0];							// Query Cursor Variable
		int dbPtr = (int)args[1].nval();						// one-based DB table pointer
		SQLiteDatabase db = DataBases.get(dbPtr - 1);			// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// query string
		String QueryString = StringConstant;
		if (!checkEOL())				return false;

		Cursor cursor;
		try {													// Do the query and get the cursor
			cursor = db.rawQuery(QueryString, null);
		} catch (Exception e) {
			return RunTimeError("SQL Exception:", e);
		}

		cursorVar.val(Cursors.size() + 1);						// Save the Cursor index into the var
		Cursors.add(cursor);									// and save the cursor.
		return true;
	}

	private boolean execute_sql_drop_table() {

		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		int i = (int)mVal.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Table Name
		String TableName = StringConstant;
		if (!checkEOL())				return false;

		String CommandString = "DROP TABLE IF EXISTS " + TableName;

		try { db.execSQL(CommandString); }
		catch (Exception e) { return RunTimeError("SQL Exception:", e); }
		return true;
	}

	private boolean execute_sql_new_table() {

		if (!getDbPtrArg())				return false;			// get variable for the DB table pointer
		int i = (int)mVal.nval();								// get the pointer
		SQLiteDatabase db = DataBases.get(i - 1);				// get the data base

		if (!isNext(','))				return false;
		if (!getStringArg())			return false;			// Table Name
		String TableName = StringConstant;

		if (!isNext(','))				return false;
		ArrayList<String> Columns = new ArrayList<String>();
		do {
			if (!getStringArg())		return false;			// Columns
			Columns.add(StringConstant);
		} while (isNext(','));
		if (!checkEOL())				return false;

		String columns = "";
		int cc = Columns.size();
		for (int j = 0; j < cc; ++j) {
			columns += Columns.get(j) + " TEXT";
			if (j != cc - 1) { columns += " , "; }
		}

		String CommandString = StringConstant;
		CommandString = "CREATE TABLE " + TableName + "( "
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ columns + " )";

		try { db.execSQL(CommandString); }
		catch (Exception e) { return RunTimeError("SQL Exception:", e); }
		return true;
	}

	// ************************************  Graphics Package ***********************************

	private boolean executeGR() {
		Command c = findSubcommand(GR_cmd, "GR");
		if (c == null) return false;

		if (!GRopen && (c.id != CID_OPEN)) {
			return RunTimeError("Graphics not opened at:");
		}
		return c.run();
	}

	private boolean executeGR_BITMAP() {				// GR group, BITMAP subgroup
		return executeSubgroupCommand(GrBitmap_cmd, "Gr.Bitmap");
	}

	private boolean executeGR_CAMERA() {				// GR group, CAMERA subgroup
		return executeSubgroupCommand(GrCamera_cmd, "Gr.Camera");
	}

	private boolean executeGR_GET() {					// GR group, GET subgroup
		return executeSubgroupCommand(GrGet_cmd, "Gr.Get");
	}

	private boolean executeGR_GROUP() {					// GR group, GROUP subgroup
		return executeSubgroupCommand(GrGroup_cmd, "Gr.Group");
	}

	private boolean executeGR_PAINT() {					// GR group, PAINT subgroup
		return executeSubgroupCommand(GrPaint_cmd, "Gr.Paint");
	}

	private boolean executeGR_TEXT() {					// GR group, TEXT subgroup
		return executeSubgroupCommand(GrText_cmd, "Gr.Text");
	}

	private void DisplayListAdd(GR.BDraw b) {
		b.common((PaintList.size() - 1), 256);			// paint and alpha for this object

		if (drawintoCanvas != null) {
			GR.drawView.doDraw(drawintoCanvas, b);
			return;
		}

		synchronized (DisplayList) {
			RealDisplayList.add(DisplayList.size());
			DisplayList.add(b);
		}
	}

	private boolean execute_gr_bitmap_drawinto_start() {
		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0)				return false;
		if (!checkEOL())				return false;

		Bitmap bitmap = BitmapList.get(bitmapPtr);					// get the bitmap
		if (bitmap == null) {
			return RunTimeError("Bitmap was deleted");
		}
		if (!bitmap.isMutable()) {
			return RunTimeError("Bitmaps loaded from files not changeable.");
		}
		if (bitmap.isRecycled()) {
			return RunTimeError("Bitmap was recycled");
		}
		drawintoCanvas = new Canvas(bitmap);
		return true;
	}

	private boolean execute_gr_bitmap_drawinto_end() {
		drawintoCanvas = null;
		return true;
	}

	private Paint newPaint(Paint fromPaint) {						// Android bug workaround?
		Typeface tf = fromPaint.getTypeface();
		Paint rPaint = new Paint(fromPaint);						// creates a new Paint
		rPaint.setTypeface(tf);										// while preserving the type face
		return rPaint;
	}

	private Paint initPaint() {
		return initPaint(255, 0, 0, 0);								// new opaque black Paint with default settings
	}

	private Paint initPaint(int a, int r, int g, int b) {
		Paint paint = new Paint();
		paint.setARGB(a, r, g, b);									// set the colors, etc
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setStrokeWidth(0.0f);
//		int f = paint.getFlags();
//		paint.setFlags(f | Paint.FILTER_BITMAP_FLAG);
		return paint;
	}

	private void DisplayListClear(GR.Type type) {
		Log.d(LOGTAG, "DisplayListClear");
		synchronized (DisplayList) {
			DisplayList.clear();									// Clear the Display List
			DisplayList.trimToSize();

			RealDisplayList.clear();
			RealDisplayList.trimToSize();

			PaintListClear();										// clear and initialize the PaintList

			GR.BDraw b = new GR.BDraw(type);						// Start Display List
			DisplayListAdd(b);										// with specified first entry
		}
	}

	private void PaintListClear() {
		// Preserve user's current and working Paints if they exist.
		int nPaints = PaintList.size();
		Paint workingPaint = (nPaints == 0) ? initPaint() : PaintList.get(0); 
		Paint currentPaint = (nPaints == 0) ? initPaint() : PaintList.get(nPaints - 1);

		PaintList.clear();
		PaintList.trimToSize();

		PaintList.add(workingPaint);							// restore user's 
		PaintList.add(currentPaint);							// add user's current Paint at entry 1
	}

	private void BitmapListClear() {
		if (BitmapList != null) {
			for (int i = 0; i < BitmapList.size(); ++i) {
				Bitmap bitmap = BitmapList.get(i);
				if (bitmap != null) {
					bitmap.recycle();
					BitmapList.set(i, null);
				}
			}
			BitmapList.clear();
		}
	}

	private boolean execute_gr_getdl() {

		Var var = getArrayVarForWrite(TYPE_NUMERIC);				// get the result array variable
		if (var == null)				return false;				// must name a numeric array variable

		boolean keepHiddenObjects = false;
		if (isNext(',')) {											// Optional "hidden" flag
			if (!evalNumericExpression()) return false;
			keepHiddenObjects = (EvalNumericExpressionValue != 0.0);
		}
		if (!checkEOL())				return false;				// line must end with ']'

		double[] list = new double[RealDisplayList.size() + 1];
		int count = 0;
		for (Integer Idx : RealDisplayList) {						// For each object index
			int idx = (Idx == null) ? 0 : Idx.intValue();
			boolean include = ((idx != 0) &&						// If not null or index of null object...
				(keepHiddenObjects ||								// ... and either keeping all objects...
					DisplayList.get(idx).isVisible()));				// ... or object is not hidden...
			if (include) { list[count++] = idx; }					// ... then put index in the new list
		}
		if (count == 0) { count = 1; }								// if no objects, make a list with
																	// one entry that indexes the null object

		if (!BuildBasicArray(var, count)) return false;				// build the array
		double[] array = var.arrayDef().getNumArray();				// raw array access
		for (int i = 0; i < count; ++i) {							// stuff the array
			array[i] = list[i];										// count may be < list.length
		}
		return true;
	}

	private boolean execute_gr_newdl() {

		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;
		if (!var.isNumeric()) { return RunTimeError(EXPECT_NUM_ARRAY); } // insure that it is a numeric array

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// get values inside [], if any
		if (!checkEOL())				return false;				// line must end with ']'

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();
		double[] array = arrayDef.getNumArray();					// raw array access

		RealDisplayList.clear();
		RealDisplayList.add(0);										// first entry points to null object

		synchronized (DisplayList) {
			for (int i = 0; i < length; ++i) {						// copy the object pointers
				int id = (int)array[base + i];
				if (id < 0 || id >= DisplayList.size()) {
					return RunTimeError("Invalid Object Number");
				}
				RealDisplayList.add(id);
			}
		}
		return true;
	}

	private boolean execute_gr_open() {
		if (GRopen) {
			return RunTimeError("Graphics already opened");
		}

		int[] args = { 255, 255, 255, 255, 0, 0 };	// default to opaque white, no status bar, landscape
		if (!getOptExprs(args)) return false;

		int a = args[0];
		int r = args[1];
		int g = args[2];
		int b = args[3];
		int showStatusBar = args[4];
		int orientation = args[5];

		int backgroundColor =	a * 0x1000000 +						// Set the appropriate bytes
								r * 0x10000 +
								g * 0x100 +
								b;
		mShowStatusBar = (showStatusBar != 0);						// record choice for GR.StatusBar command

		synchronized (DisplayList) {
			drawintoCanvas = null;
			DisplayListClear(GR.Type.Open);
			BitmapListClear();
			BitmapList.add(null);									// Set Zero entry as null

			Paint paint = initPaint(a, r, g, b);					// Create a new Paint object, default except color
			PaintList.add(paint);									// Add to the Paint List as element 2
		}

		mGrIntent = new Intent(Run.this, GR.class);					// Set up parameters for the Graphics Activity
		mGrIntent.putExtra(GR.EXTRA_SHOW_STATUSBAR, showStatusBar);
		mGrIntent.putExtra(GR.EXTRA_ORIENTATION, orientation);
		mGrIntent.putExtra(GR.EXTRA_BACKGROUND_COLOR, backgroundColor);

		GR.Running = false;											// Set up the signals
		GR.waitForLock = true;
		startActivityForResult(mGrIntent, BASIC_GENERAL_INTENT);	// Start the Graphics Activity

		waitForGrLOCK();											// Do not continue until GR signals it is running

		GRopen = true;												// Set some more signals
		initTouchVars();
		GR.doSTT = false;
		CameraNumber = -1;
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		return true;
	}

	private void initTouchVars() {
		for (int i = 0; i < NewTouch.length; ++i) {
			TouchX[i] = TouchY[i] = -1;
			NewTouch[i] = false;
		}
	}

	private boolean execute_gr_paint_get() {						// get the index of the current (latest) Paint
		if (!getNVar()) return false;
		if (!checkEOL()) return false;
		mVal.val(PaintList.size() - 1);
		return true;
	}

	private boolean execute_gr_paint_copy() {						// copy a Paint or make a new one
		int[] args = { -1, -1 };									// default: src, dst both current Paint
		if (!getOptExprs(args))			return false;

		int current = PaintList.size() - 1;
		int src = args[0];
		int dst = args[1];

		if (src != -1) {
			if (!checkPaintIndex(src))	return false;				// ERROR: out of range
		} else {
			src = current;
		}
		Paint srcPaint = newPaint(PaintList.get(src));				// make a copy

		if (dst != -1) {
			if (!checkPaintIndex(dst))	return false;				// ERROR: out of range
			PaintList.set(dst, srcPaint);							// replace destination Paint with the copy
		} else {
			PaintList.add(srcPaint);								// add the copy to the Paint list
		}
		return true;
	}

	private boolean execute_gr_paint_reset() {
		int[] args = { -1 };										// default: reset current Paint;
		if (!getOptExprs(args))			return false;

		Paint paint = initPaint();									// create a new default Paint
		int index = args[0];
		if (index == -1) {
			PaintList.add(paint);									// add the new Paint to the List
		} else {
			if (!checkPaintIndex(index)) return false;
			PaintList.set(index, paint);							// replace the existing Paint
		}
		return true;
	}

	private void GRstop() {											// stop GR, but don't wait for lock
		if (GR.drawView != null) {									// create a new display list
			DisplayListClear(GR.Type.Close);						// that commands GR.java to close
			GR.drawView.postInvalidate();							// start the draw so the command will get executed
		}
		GRopen = false;
	}

	private boolean execute_gr_close() {
		if (!checkEOL()) return false;

		if (GR.drawView != null) {									// create a new display list
			DisplayListClear(GR.Type.Close);						// that commands GR.java to close
			GR.waitForLock = true;
			GR.drawView.postInvalidate();							// start the draw so the command will get executed
			waitForGrLOCK();
		}
		GRopen = false;

		return true;
	}

	private boolean execute_gr_render() {
		if (!checkEOL()) return false;

		if (GR.drawView == null) {									// make sure drawView has not gone null
			Stop = true;
			return false;
		}

		synchronized (DisplayList) {
			GR.waitForLock = true;
		}
		GR.NullBitMap = false;
		GR.drawView.postInvalidate();								// Start GR drawing.
		waitForGrLOCK();

		if (GR.NullBitMap) {
			GR.NullBitMap = false;
			return RunTimeError("Display List had deleted bitmap.");
		}
		return true;
	}

	private boolean checkPaintIndex(int index) {
		return ((index >= 0) && (index < PaintList.size())) ? true :
				RunTimeError("Paint Number out of range");
	}

	private Paint getWorkingPaint(int index) {					// get specified Paint
																// if none specified, clone current Paint as new current Paint
		Paint paint;
		if (index != -1) {											// get an existing Paint
			if (!checkPaintIndex(index)) return null;				// ERROR: out of range
			paint = PaintList.get(index);
		} else {
			paint = newPaint(PaintList.get(PaintList.size() - 1));	// get a copy of the latest Paint
			PaintList.add(paint);									// add the copy to the Paint list
		}
		return paint;
	}

	private boolean execute_gr_color() {
		int[] args = { -1, -1, -1, -1, -1, -1 };					// default to current color and style, new Paint
		if (!getOptExprs(args))			return false;

		int paintIdx = args[5];
		Paint paint = getWorkingPaint(paintIdx);
		if (paint == null)				return false;				// index out of range

		int color = paint.getColor();
		int a = (args[0] != -1) ? args[0] : 255 & (color >>> 24);
		int r = (args[1] != -1) ? args[1] : 255 & (color >>> 16);
		int g = (args[2] != -1) ? args[2] : 255 & (color >>> 8);
		int b = (args[3] != -1) ? args[3] : 255 & (color);
		paint.setARGB(a, r, g, b);									// set the colors

		int style = args[4];
		if      (style == 0)  { paint.setStyle(Paint.Style.STROKE); }
		else if (style == 1)  { paint.setStyle(Paint.Style.FILL); }
		else if (style != -1) { paint.setStyle(Paint.Style.FILL_AND_STROKE); }

		return true;
	}

	private boolean execute_gr_antialias() {
		int[] args = { -1, -1 };									// default to toggle and new Paint
		if (!getOptExprs(args)) return false;

		int select = args[0];
		int paintIdx = args[1];

		Paint paint = getWorkingPaint(paintIdx);
		if (paint == null)				return false;				// index out of range

		// -1 toggles antialias, 0 clears it, anything else sets it.
		boolean flag = (select == -1) ? !paint.isAntiAlias() : (select != 0);
		paint.setAntiAlias(flag);

		return true;
	}

	private boolean execute_gr_stroke_width() {
		byte[] type = { 1, 1 };										// two optional numeric arguments
		Double[] args = { null, null };
		String[] dummy = { null, null };							// not used, no String arguments
		if (!getOptExprs(type, args, dummy)) return false;

		int paintIdx = (args[1] != null) ? args[1].intValue() : -1;	// default to current Paint
		Paint paint = getWorkingPaint(paintIdx);					// null if invalid index

		if (args[0] != null) {										// if width provided
			float width = args[0].floatValue();						// get width
			if (width < 0.0f) { return RunTimeError("Width must be >= 0"); }
			if (paint != null) paint.setStrokeWidth(width);
		}
		if (paint == null)				return false;				// error reporting is in parameter order

		return true;
	}

	// Common processing for the beginning of a command that creates a graphical object.
	// Expect a numeric variable on the command line. Leave its Val object in mVal. TODO: ELIMINATE mVal
	private GR.BDraw createGrObj_start(GR.Type type) {
		if (!getNVar()) return null;
		GR.BDraw b = new GR.BDraw(type);
		return b;
	}

	// Common processing for the end of a command that creates a graphical object.
	private boolean createGrObj_finish(GR.BDraw b, Var.Val val) {
		if (!checkEOL()) return false;
		synchronized (DisplayList) {
			val.val(DisplayList.size());							// save the object index into the var
			DisplayListAdd(b);										// add the object to the Display List
		}
		return true;
	}

	// Common processing for the end of a command that creates a bitmap.
	private boolean createBitmap_finish(Bitmap bitmap, Var.Val val, String errMsg) {
		int value;
		if (bitmap != null) {
			value = BitmapList.size();								// save the bitmap index
			BitmapList.add(bitmap);									// add the new bitmap to the bitmap list
		} else {
			value = -1;
			writeErrorMsg((errMsg != null) ? errMsg : "Failed to create bitmap");
		}
		if (val != null) { val.val(value); }						// give the bitmap pointer to the user
		return true;
	}

	private boolean execute_gr_point() {
		GR.BDraw b = createGrObj_start(GR.Type.Point);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] xy = getArgsII();
		if (xy == null) return false;
		b.xy(xy);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_line() {
		GR.BDraw b = createGrObj_start(GR.Type.Line);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] x2y2 = getArgs4I();									// in x1, y1, x2, y2 order
		if (x2y2 == null) return false;
		b.ltrb(x2y2);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_rect() {
		GR.BDraw b = createGrObj_start(GR.Type.Rect);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] ltrb = getArgs4I();
		if (ltrb == null) return false;
		b.ltrb(ltrb);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_arc() {
		GR.BDraw b = createGrObj_start(GR.Type.Arc);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] ltrb = getArgs4I();
		if (ltrb == null) return false;

		if (!isNext(',')) return false;
		double[] angles = getArgsDD();
		if (angles == null) return false;

		if (!isNext(',') || !evalNumericExpression()) return false;
		int fillMode = EvalNumericExpressionValue.intValue();

		b.arc(ltrb, (float)angles[0], (float)angles[1], fillMode);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_circle() {
		GR.BDraw b = createGrObj_start(GR.Type.Circle);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] xy = getArgsII();
		if (xy == null) return false;
		if (!isNext(',') || !evalNumericExpression()) return false;
		int radius = EvalNumericExpressionValue.intValue();

		b.circle(xy, radius);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_oval() {
		GR.BDraw b = createGrObj_start(GR.Type.Oval);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] ltrb = getArgs4I();
		if (ltrb == null) return false;
		b.ltrb(ltrb);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private double gr_collide(int obj1Num, int obj2Num) {

		double fail = -1;
		double xfalse = 0;
		double xtrue = 1;

		if (obj1Num < 0 || obj1Num >= DisplayList.size()) {
			RunTimeError("Object 1 Number out of range");
			return fail;
		}
		GR.BDraw obj1 = DisplayList.get(obj1Num);					// Get the first object
		if (obj1.isHidden()) return xfalse;							// If hidden then no collide

		if (obj2Num < 0 || obj2Num >= DisplayList.size()) {
			RunTimeError("Object 2 Number out of range");
			return fail;
		}
		GR.BDraw obj2 = DisplayList.get(obj2Num);					// Get the second object
		if (obj2.isHidden()) return xfalse;							// If hidden then no collide

		Rect r1 = gr_getBounds(obj1);
		if (r1 == null) return fail;
		Rect r2 = gr_getBounds(obj2);
		if (r2 == null) return fail;

		// Note: Rect.intersects(Rect, Rect) does not consider right edge to be in the Rect
		//       so we can't use it.
		return ((r1.bottom < r2.top) ||								// Test for collision
				(r2.bottom < r1.top) ||
				(r1.right < r2.left) ||
				(r2.right < r1.left)) ? xfalse : xtrue;
	}

	@SuppressLint("RtlHardcoded")	// suppress false warning on Paint.Align.LEFT and RIGHT
	private Rect gr_getBounds(GR.BDraw b) {							// get the bounding box of a graphical object
		int left, top, right, bottom;
		Rect bounds = null;

		GR.Type type = b.type();
		switch (type) {
			case Circle:
				int cx = b.x();
				int cy = b.y();
				int cr = b.radius();
				bounds = new Rect(cx - cr, cy - cr, cx + cr, cy + cr);
				break;

			case Arc:
			case Oval:
			case Point:
			case Rect:
				left = b.left();
				top = b.top();
				right = b.right();
				bottom = b.bottom();
				bounds = new Rect(left, top, right, bottom);
				if (bounds.width() < 0) {
					bounds.set(right, top, left, bottom);
				}
				if (bounds.height() < 0) {
					bounds.set(left, bottom, right, top);
				}
				break;

			case Bitmap:
				left = b.left();
				top = b.top();
				Bitmap theBitmap = BitmapList.get(b.bitmap());
				right = left + theBitmap.getWidth();
				bottom = top + theBitmap.getHeight();
				bounds = new Rect(left, top, right, bottom);
				break;

			case Text:
				bounds = new Rect();
				Paint paint = PaintList.get(b.paint());
				String text = b.text();
				paint.getTextBounds(text, 0, text.length(), bounds);	// returns the minimum bounding box
																		// from implied origin (0,0)
				int tx = b.x();
				int ty = b.y();
				float tw = paint.measureText(text);						// width used for alignment
																		// generally more than theRect.width()
				Paint.Align align = paint.getTextAlign();
				switch (align) {										// adjust origin for alignment
					case LEFT:   bounds.offset(tx,               ty); break;
					case CENTER: bounds.offset((int)(tx - tw/2), ty); break;
					case RIGHT:  bounds.offset((int)(tx - tw),   ty); break;
				}
				break;

			default:
				break;
		}

		return bounds;
	}

	private boolean execute_gr_cls() {
		if (!checkEOL()) return false;

		DisplayListClear(GR.Type.Null);
		return true;
	}

	private int getBitmapArg() {									// get the bitmap number
		return getBitmapArg("Invalid Bitmap Pointer");				// with the default error message
	}

	private int getBitmapArg(String errMsg) {						// get and validate the bitmap number
		if (!evalNumericExpression()) return -1;
		int bitmapPtr = EvalNumericExpressionValue.intValue();
		if (bitmapPtr < 1 | bitmapPtr >= BitmapList.size()) {
			RunTimeError(errMsg);
			bitmapPtr = -1;
		}
		return bitmapPtr;
	}

	private int getObjectNumber() {									// get the Graphics Object Number
		return getObjectNumber("Object out of range");				// with the default error message
	}

	private int getObjectNumber(String errMsg) {					// get Object Number, set RTE if out of range
		int obj = getGraphicsObjectNumber();
		if (obj == -1) { RunTimeError(errMsg); }					// forced to -1 if out of range
		return obj;
	}

	private int getGraphicsObjectNumber() {							// get and validate the Graphics Object Number
		if (!evalNumericExpression()) return -1;
		int obj = EvalNumericExpressionValue.intValue();
		if (obj < 0 || obj >= DisplayList.size()) {					// if invalid Object Number
			obj = -1;												// force to standard error value (-1)
		}
		return obj;
	}

	private boolean execute_gr_group_objs() {						// create a Group from the object numbers on the command line
		GR.BDraw b = createGrObj_start(GR.Type.Group);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		ArrayList<Double> list = new ArrayList<Double>();			// get object numbers, put them in a list
		boolean isComma = isNext(',');
		while (isComma) {
			double lObj = getObjectNumber();
			if (lObj < 0.0) return false;
			list.add(lObj);
			isComma = isNext(',');
		}
		if (!checkEOL()) return false;

		int listIndex = theLists.size();							// store as a new numeric list
		theLists.add(list);
		theListsType.add(Var.Type.NUM);

		b.list(listIndex, list);									// attach the list to the Group Object
		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_group_list() {						// create a group from a list
		GR.BDraw b = createGrObj_start(GR.Type.Group);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int listIndex = getListArg(Var.Type.NUM);					// reuse old list or create new one
		if (listIndex < 0) return false;
		if (!checkEOL()) return false;

		ArrayList<Double> list = theLists.get(listIndex);			// retrieve the list
		b.list(listIndex, list);
		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_group_getdl() {						// create a group from the current Display List
		GR.BDraw b = createGrObj_start(GR.Type.Group);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		boolean keepHiddenObjects = false;
		if (isNext(',')) {											// Optional "hidden" flag
			if (!evalNumericExpression()) { return false; }
			keepHiddenObjects = (EvalNumericExpressionValue != 0.0);
		}
		if (!checkEOL()) { return false; }							// line must end with ']'

		ArrayList<Double> list = new ArrayList<Double>();			// copy Display List to a list
		for (Integer Idx : RealDisplayList) {						// for each object index
			int idx = (Idx == null) ? 0 : Idx.intValue();
			boolean include = ((idx != 0) &&						// if not null or index of null object...
				(keepHiddenObjects ||								// ... and either keeping all objects...
					DisplayList.get(idx).isVisible()));				// ... or object is not hidden...
			if (include) { list.add((double)idx); }					// ... then put index in the new list
		}

		int listIndex = theLists.size();							// store as a new numeric list
		theLists.add(list);
		theListsType.add(Var.Type.NUM);

		b.list(listIndex, list);
		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_group_newdl() {						// set a new display list from a group
		int obj = getObjectNumber();								// get the Group Object number
		if (obj < 0) return false;
		if (!checkEOL()) return false;

		synchronized (DisplayList) {
			GR.BDraw b = DisplayList.get(obj);						// get Group Object
			if (b.type() != GR.Type.Group) { return false; }		// make sure it's a group
			ArrayList<Double> list = b.list();						// retrive the list

			RealDisplayList.clear();
			RealDisplayList.add(0);									// first entry points to null object
			for (Double id : list) {								// copy the group list to the Display List
				RealDisplayList.add(id.intValue());
			}
		}
		return true;
	}

	private boolean execute_gr_show(GR.VISIBLE show) {
		int obj = getObjectNumber();								// get the Graphics Object number
		if (obj < 0) return false;
		if (!checkEOL()) return false;

		GR.BDraw b = DisplayList.get(obj);							// get Graphics Object
		if (b.type() != GR.Type.Group) {							// if it is not a Group
			b.show(show);											// hide or show it
			return true;
		}

		ArrayList<Double> list = b.list();							// Group: get the list of objects to change
		if (list == null) return true;								// nothing to do

		int dlSize = DisplayList.size();
		for (Double d : list) {
			obj = d.intValue();										// get each index from the list
			if ((obj < 0) || (obj >= dlSize)) {
				return RunTimeError("Object out of range");
			}
			b = DisplayList.get(obj);								// get each Graphics Object to change
			b.show(show);											// show or hide it
		}
		return true;
	}

	private boolean execute_gr_move() {
		int obj = getObjectNumber();								// get the Graphics Object number
		if (obj < 0) return false;
		int[] dxdy = { 0, 0 };										// default: deltas both zero
		if (isNext(',') ? !getOptExprs(dxdy)						// get the deltas if there are any
						: !checkEOL()) return false;

		GR.BDraw b = DisplayList.get(obj);							// get Graphics Object
		if (b.type() != GR.Type.Group) {							// if it is not a Group
			b.move(dxdy);											// move it
			return true;
		}

		ArrayList<Double> list = b.list();							// Group: get the list of objects to change
		if (list == null) return true;								// nothing to do

		int dlSize = DisplayList.size();
		for (Double d : list) {
			obj = d.intValue();										// get each index from the list
			if ((obj < 0) || (obj >= dlSize)) {
				return RunTimeError("Object out of range");
			}
			b = DisplayList.get(obj);								// get each Graphics Object to change
			b.move(dxdy);											// move it
		}
		return true;
	}

	private boolean execute_gr_get_position() {
		int obj = getObjectNumber();
		if (obj < 0) return false;
		if (!isNext(',') || !getNVar()) return false;
		Var.Val xVar = mVal;
		if (!isNext(',') || !getNVar()) return false;
		Var.Val yVar = mVal;
		if (!checkEOL()) return false;

		GR.BDraw b = DisplayList.get(obj);							// get the Graphics Object
		xVar.val(b.x());
		yVar.val(b.y());
		return true;
	}

	private boolean execute_gr_get_value() {
		int obj = getObjectNumber();
		if (obj < 0) return false;
		GR.BDraw b = DisplayList.get(obj);							// get the Graphics Object
		while (isNext(',')) {										// collect tag/var pairs
			if (!getStringArg()) return false;						// get each parameter string
			String parm = StringConstant;
			if (!b.type().hasParameter(parm)) {
				return RunTimeError("Object does not contain " + parm);
			}
			if (!isNext(',') || !getVar()) return false;			// var for value
			Var.Val val = mVal;
			boolean varIsNumeric = val.isNumeric();
			if (varIsNumeric == parm.equals("text")) {					// error if numeric var and "text" tag
				return RunTimeError("Wrong var type for tag: " + parm);	// or string var and not "text" tag
			}
			if (varIsNumeric) {
				double value = b.getValue(parm);
				val.val(value);
			} else {
				String theText = b.text();
				val.val(theText);
			}
		}
		return checkEOL();
	}

	private boolean execute_gr_get_type() {
		Var.Val val = null;
		int obj = getGraphicsObjectNumber();						// forced to -1 if out of range
		if (obj == -1) { writeErrorMsg("Not a graphical object"); }	// message for GetError$() function
		else           { clearErrorMsg(); }
		if (isNext(',')) {
			if (!getSVar())				return false;				// var for type string
			val = mVal;
		}
		if (!checkEOL())				return false;

		if (val != null) {											// if there is a return variable
			String type;											// value to return
			if (obj != -1) {										// if there is a valid object number
				GR.BDraw b = DisplayList.get(obj);					// get the Graphics Object
				type = b.type().type();								// get its type
			} else { type = ""; }									// else no type (invalid object number)
			val.val(type);
		}
		return true;
	}

	private boolean execute_gr_get_params() {
		int obj = getObjectNumber();
		if (obj < 0)					return false;
		GR.BDraw b = DisplayList.get(obj);							// get the Graphics Object

		if (!isNext(','))				return false;
		Var var = getArrayVarForWrite(TYPE_STRING);					// get the result array variable
		if (var == null)				return false;				// must name a new string array variable
		if (!checkEOL())				return false;				// line must end with ']'

		String[] params = b.type().parameters();
		int length = params.length;

		/* Puts the list of keys into a new array */
		return ListToBasicStringArray(var, Arrays.asList(params), length);
	}

	private boolean execute_gr_touch(int p) {
		if (!getNVar()) return false;								// boolean variable
		Var.Val flagVar = mVal;
		if (!isNext(',')) return false;

		if (!getNVar()) return false;								// x variable
		Var.Val xVar = mVal;
		if (!isNext(',')) return false;

		if (!getNVar()) return false;								// y variable
		Var.Val yVar = mVal;
		if (!checkEOL()) return false;

		flagVar.val(NewTouch[p] ? 1.0 : 0.0);						// return touched flag as numerical value
		if (TouchX[p] != -1) {										// if ever touched
			xVar.val(TouchX[p]);									// then report the last touch
			yVar.val(TouchY[p]);
		}
		return true;
	}

	private boolean execute_gr_bound_touch(int p) {
		if (!getNVar()) return false;								// boolean variable
		Var.Val flagVar = mVal;
		if (!isNext(',')) return false;

		int[] bounds = getArgs4I();									// [left, top, right, bottom]
		if (bounds == null) return false;							// error getting values
		if (!checkEOL()) return false;

		int left   = bounds[0];
		int top    = bounds[1];
		int right  = bounds[2];
		int bottom = bounds[3];
		if (right < left) {
			left = bounds[2];
			right = bounds[0];
		}
		if (bottom < top) {
			top = bounds[3];
			bottom = bounds[1];
		}

		boolean flag = false;
		if (NewTouch[p]) {											// if currently being touched
			flag = (TouchX[p] >= left && TouchX[p] <= right &&		// true iff touch was in bounding rect
					TouchY[p] >= top  && TouchY[p] <= bottom);
		}
		flagVar.val(flag ? 1.0 : 0.0);								// return flag as numerical value
		return true;
	}

	private boolean execute_gr_text_draw() {
		GR.BDraw b = createGrObj_start(GR.Type.Text);				// create Graphic Object and get variable
		if (b == null) return false;
		Var.Val saveVal = mVal;

		if (!isNext(',')) return false;
		int[] xy = getArgsII();
		if (xy == null) return false;
		if (!isNext(',') || !getStringArg()) return false;

		b.xy(xy);
		b.text(StringConstant);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_text_align() {
		int[] args = { -1, -1 };									// default to unchanged alignment and new Paint
		if (!getOptExprs(args))			return false;

		int alignCode = args[0];
		int paintIdx = args[1];

		Paint.Align align = null;									// validate and convert alignment parameter
		if      (alignCode == 1)  { align = Paint.Align.LEFT; }
		else if (alignCode == 2)  { align = Paint.Align.CENTER; }
		else if (alignCode == 3)  { align = Paint.Align.RIGHT; }
		else if (alignCode != -1) { return RunTimeError( "Align value not 1, 2 or 3 at "); }

		Paint paint = getWorkingPaint(paintIdx);					// validate Paint index parameter and get Paint
		if (paint == null)				return false;				// index out of range

		if (align != null) { paint.setTextAlign(align); }
		return true;
	}

	private boolean execute_gr_text_size() {
		byte[] type = { 1, 1 };										// two optional numeric arguments
		Double[] args = { null, null };
		String[] dummy = { null, null };							// not used, no String arguments
		if (!getOptExprs(type, args, dummy)) return false;

		int paintIdx = (args[1] != null) ? args[1].intValue() : -1;	// default to current Paint
		Paint paint = getWorkingPaint(paintIdx);					// null if invalid index

		if (args[0] != null) {										// if size provided
			float size = args[0].floatValue();						// get size
			if (size <= 0.0f) { return RunTimeError( "Size must be > 0"); }
			if (paint != null) paint.setTextSize(size);
		}
		if (paint == null)				return false;				// error reporting is in parameter order
		return true;

	}

	private boolean execute_gr_text_underline() {
		int[] args = { -1, -1 };									// default to toggle and new Paint
		if (!getOptExprs(args)) return false;

		int select = args[0];
		int paintIdx = args[1];

		Paint paint = getWorkingPaint(paintIdx);
		if (paint == null)				return false;				// index out of range

		// -1 toggles underlining, 0 clears it, anything else sets it.
		boolean flag = (select == -1) ? !paint.isUnderlineText() : (select != 0);
		paint.setUnderlineText(flag);

		return true;
	}

	private boolean execute_gr_text_skew() {
		byte[] type = { 1, 1 };										// two optional numeric arguments
		Double[] args = { null, null };
		String[] dummy = { null, null };							// not used, no String arguments
		if (!getOptExprs(type, args, dummy)) return false;

		int paintIdx = (args[1] != null) ? args[1].intValue() : -1;	// default to current Paint
		Paint paint = getWorkingPaint(paintIdx);					// null if invalid index
		if (paint == null)				return false;

		if (args[0] != null) {										// if skew provided
			float skew = args[0].floatValue();						// get skew
			paint.setTextSkewX(skew);
		}

		return true;
	}

	private boolean execute_gr_text_bold() {
		int[] args = { -1, -1 };									// default to toggle and new Paint
		if (!getOptExprs(args)) return false;

		int select = args[0];
		int paintIdx = args[1];

		Paint paint = getWorkingPaint(paintIdx);
		if (paint == null)				return false;				// index out of range

		// -1 toggles bold, 0 clears it, anything else sets it.
		boolean flag = (select == -1) ? !paint.isFakeBoldText() : (select != 0);
		paint.setFakeBoldText(flag);

		return true;
	}

	private boolean execute_gr_text_strike() {
		int[] args = { -1, -1 };									// default to toggle and new Paint
		if (!getOptExprs(args)) return false;

		int select = args[0];
		int paintIdx = args[1];

		Paint paint = getWorkingPaint(paintIdx);
		if (paint == null)				return false;				// index out of range

		// -1 toggles strike, 0 clears it, anything else sets it.
		boolean flag = (select == -1) ? !paint.isStrikeThruText() : (select != 0);
		paint.setStrikeThruText(flag);

		return true;
	}

	private GR.BDraw getTextObject(int dlIndex) {
		if (dlIndex < 0 || dlIndex >= DisplayList.size()) {
			RunTimeError("Object Number out of range");
			return null;
		}
		GR.BDraw b = DisplayList.get(dlIndex);
		if (b.type() != GR.Type.Text) {
			RunTimeError("Not a text object");
			return null;
		}
		return b;
	}

	private boolean execute_gr_get_textbounds() {
		Paint paint;
		String text;
		if (evalNumericExpression()) {								// if argument is an object number
			int index = EvalNumericExpressionValue.intValue();
			GR.BDraw b = getTextObject(index);
			if (b == null) return false;
			paint = PaintList.get(b.paint());						// use the text object's paint
			text = b.text();										// get text from the text object
		} else {
			if (SyntaxError) return false;
			if (!getStringArg()) return false;
			text = StringConstant;									// argument is the text to measure
			paint = PaintList.get(PaintList.size() - 1);			// use current Paint
		}

		if (!isNext(',')) return false;
		Var.Val[] val = getArgs4NVar();								// [left, top, right, bottom]
		if (val == null) return false;								// error getting variables
		if (!checkEOL()) return false;

		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);

		val[0].val(bounds.left);
		val[1].val(bounds.top);
		val[2].val(bounds.right);
		val[3].val(bounds.bottom);

		return true;
	}

	private boolean execute_gr_text_height() {
		if (isEOL()) return true;									// user asked for no data

		// Three optional numeric variables for height, up, and down values
		byte[] types = { 1, 1, 1 };									// type of each variable
		int nArgs = types.length;
		Var.Val[] args = new Var.Val[nArgs];						// Val object for each variable
		if (!getOptVars(types, args))	return false;

		Paint currentPaint = PaintList.get(PaintList.size() - 1);
		Paint.FontMetrics fm = currentPaint.getFontMetrics();
		float height = currentPaint.getTextSize();
		float ascent = fm.ascent;
		float descent = fm.descent;

		float[] vals = { height, ascent, descent };
		for (int arg = 0; arg < nArgs; ++arg) {
			Var.Val argval = args[arg];								// Val object to return each value
			if (argval != null) { argval.val(vals[arg]); }
		}
		return true;
	}

	private boolean execute_gr_text_width() {
		if (!getNVar()) return false;								// width return variable
		Var.Val val = mVal;
		if (!isNext(',')) return false;

		Paint paint;
		String text;
		if (evalNumericExpression()) {								// if argument is an object number
			int index = EvalNumericExpressionValue.intValue();
			GR.BDraw b = getTextObject(index);
			if (b == null) return false;
			paint = PaintList.get(b.paint());						// use the text object's paint
			text = b.text();										// get text from the text object
		} else {
			if (SyntaxError) return false;
			if (!getStringArg()) return false;
			text = StringConstant;									// argument is the text to measure
			paint = PaintList.get(PaintList.size() - 1);			// use current Paint
		}
		if (!checkEOL()) return false;

		double w = paint.measureText(text);							// get the string's width
		val.val(w);													// save the width into the var

		return true;
	}

	private boolean execute_gr_bitmap_load() {
		clearErrorMsg();
		if (!getNVar()) return false;								// bitmap pointer variable
		Var.Val val = mVal;
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;							// get the file path
		if (!checkEOL()) return false;

		String errMsg = null;
		Bitmap bitmap = null;

		String fileName = StringConstant;							// the filename as given by the user
		BufferedInputStream bis = null;								// establish an input stream
		try { bis = Basic.getBufferedInputStream(Basic.DATA_DIR, fileName); }
		catch (Exception e) { errMsg = e.getMessage(); }
		if (bis == null) {
			if (errMsg == null) { errMsg = "No bitmap found"; }		// can this happen?
		} else {
			try { bitmap = BitmapFactory.decodeStream(bis); }		// create bitmap from the input stream
			catch (OutOfMemoryError oom) { errMsg = oom.getMessage(); }
			finally {
				try { bis.close(); }
				catch (IOException e) { return RunTimeError(e); }
			}
		}
		return createBitmap_finish(bitmap, val, errMsg);			// store the bitmap and return its index
	}

	private boolean execute_gr_bitmap_delete() {
		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0) return false;
		if (!checkEOL()) return false;

		Bitmap bitmap = BitmapList.get(bitmapPtr);					// get the bitmap
		if (bitmap != null) {
			bitmap.recycle();
		}
		BitmapList.set(bitmapPtr, null);
		return true;
	}

	private boolean execute_gr_bitmap_scale() {
		clearErrorMsg();
		if (!getNVar()) return false;								// destination bitmap pointer variable
		Var.Val val = mVal;
		if (!isNext(',')) return false;

		int bitmapPtr = getBitmapArg();								// get source bitmap number
		if (bitmapPtr < 0) return false;
		Bitmap srcBitmap = BitmapList.get(bitmapPtr);				// get the bitmap
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// get width
		int Width = EvalNumericExpressionValue.intValue();
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// get height
		int Height = EvalNumericExpressionValue.intValue();

		boolean parm = true;
		if (isNext(',')) {											// optional scale parameter
			if (!evalNumericExpression()) return false;
			if (EvalNumericExpressionValue == 0.0) parm = false;
		}
		if (!checkEOL()) return false;

		if (srcBitmap == null) {
			return RunTimeError("Bitmap was deleted");
		}
		if (Width == 0 || Height == 0) {
			return RunTimeError("Width and Height must not be zero");
		}

		String errMsg = null;
		Bitmap bitmap = null;
		try { bitmap = Bitmap.createScaledBitmap(srcBitmap, Width, Height, parm); }
		catch (OutOfMemoryError oom) { errMsg = oom.getMessage(); }

		if (bitmap == srcBitmap) {
			// Scale 1:1 does not create a new bitmap. Make a copy.
			bitmap = srcBitmap.copy(srcBitmap.getConfig(), false);
		}
		return createBitmap_finish(bitmap, val, errMsg);			// store the bitmap and return its index
	}

	private boolean execute_gr_bitmap_size() {
		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0) return false;
		Bitmap srcBitmap = BitmapList.get(bitmapPtr);				// access the bitmap
		if (srcBitmap == null) { return RunTimeError("Bitmap was deleted");	}

		if (!isNext(',')) return false;
		if (!getNVar()) return false;								// get the width variable
		Var.Val wVar = mVal;

		if (!isNext(',')) return false;
		if (!getNVar()) return false;								// get the height variable
		Var.Val hVar = mVal;
		if (!checkEOL()) return false;

		int w = srcBitmap.getWidth();								// get the image width
		int h = srcBitmap.getHeight();								// get the image height

		wVar.val(w);												// set the width value
		hVar.val(h);												// set the height value
		return true;
	}

	private boolean execute_gr_bitmap_crop() {
		clearErrorMsg();
		if (!getNVar()) return false;								// dest Graphic Object variable
		Var.Val val = mVal;
		if (!isNext(',')) return false;

		int bitmapPtr = getBitmapArg("Invalid Source Bitmap Pointer");	// get source bitmap number
		if (bitmapPtr < 0) return false;
		Bitmap srcBitmap = BitmapList.get(bitmapPtr);				// get source bitmap
		if (!isNext(',')) return false;

		int[] bounds = getArgs4I();									// [x, y, width, height]
		if (bounds == null) return false;							// error getting values
		if (!checkEOL()) return false;

		if (srcBitmap == null) {
			return RunTimeError("Bitmap was deleted");
		}

		String errMsg = null;
		Bitmap bitmap = null;
		try { bitmap = Bitmap.createBitmap(srcBitmap, bounds[0], bounds[1], bounds[2], bounds[3]); }
		catch (OutOfMemoryError oom) { errMsg = oom.getMessage(); }

		if (bitmap == srcBitmap) {
			// "Crop" to full image does not create a new bitmap. Make a copy.
			bitmap = srcBitmap.copy(srcBitmap.getConfig(), false);
		}
		return createBitmap_finish(bitmap, val, errMsg);			// store the bitmap and return its index
	}

	private void fillscan(int[] arrPixels, int color, int sx, int ex, int y, int width, Stack<Point> q) {
		if ((arrPixels == null) || (q == null)) return;

		// Scan part of a line for points of the target color,
		// pushing one new seed for each segment of connected points found.
		boolean open = false;										// if true, have seeded a segment
		int x0 = width * y;											// first point of line
		for (int x = sx; x <= ex; ++x) {
			boolean match = arrPixels[x0 + x] == color;
			if (!open && match) {
				q.push(new Point (x, y));							// start a new segment
				open = true;
			} else if (open && !match) {
				open = false;										// close this segment
			}
		}
	}

	// 2015-05-31 Algorithm by George Matei, implemented by Chaand.
	private boolean execute_gr_bitmap_fill() {					// floodfill a region of a bitmap
		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0)				return false;
		if (!isNext(','))				return false;
		int[] xy = getArgsII();										// x, y coordinates of point in region to color
		if (xy == null)					return false;
		if (!checkEOL())				return false;

		Bitmap bmp = BitmapList.get(bitmapPtr);						// get the bitmap
		if (!checkBitmapPoint(bmp, xy))	return false;				// is point in bitmap?

		int targetColor = bmp.getPixel(xy[0], xy[1]);				// get the original color of the bitmap pixel
		Paint currentPaint = PaintList.get(PaintList.size() - 1);
		int replacementColor = currentPaint.getColor();				// get the color to change to
		if (targetColor == replacementColor) return true;			// nothing to do

		int width = bmp.getWidth();									// get the bitmap dimensions
		int xmax = width - 1;
		int height = bmp.getHeight();
		int ymax = height - 1;
		int[] arrPixels = new int[width * height];					// array for the bitmap pixels [TODO: too much memory?]

		bmp.getPixels(arrPixels, 0, width, 0, 0, width, height);	// get the bitmap pixels

		Stack<Point> q = new Stack<Point>();

		q.push(new Point(xy[0], xy[1]));							// first seed
		while (!q.empty()) {
			Point p = q.pop();
			int y = p.y;
			int x0 = width * p.y;									// index of point (0, y)

			// Change all connected points of the old color to the new color
			int sx = p.x;											// start point
			int ex = sx;											// end point
			arrPixels[x0 + sx] = replacementColor;
			while ((sx > 0) && (arrPixels[x0 + sx - 1] == targetColor)) {
				arrPixels[x0 + --sx] = replacementColor;
			}
			while ((ex < xmax) && (arrPixels[x0 + ex + 1] == targetColor)) {
				arrPixels[x0 + ++ex] = replacementColor;
			}
			bmp.setPixels(arrPixels, x0 + sx, width, sx, y, ex - sx + 1, 1);

			if (y > 0) {											// if there is a line above
				fillscan(arrPixels, targetColor, sx, ex, y - 1, width, q);	// push a point for each segment of matching color
			}
			if (y < ymax) {											// if there is a line below
				fillscan(arrPixels, targetColor, sx, ex, y + 1, width, q);	// push a point for each segment of matching color
			}
		}
		return true;
	}

	private boolean execute_gr_bitmap_draw() {
		GR.BDraw b = createGrObj_start(GR.Type.Bitmap);				// create Graphic Object and get variable
		if (b == null)					return false;
		Var.Val saveVal = mVal;
		if (!isNext(','))				return false;

		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0) return false;
		if (BitmapList.get(bitmapPtr) == null) {					// check the bitmap
			return RunTimeError("Bitmap was deleted");
		}
		b.bitmap(bitmapPtr);										// store the bitmap number

		if (!isNext(',')) return false;
		int[] xy = getArgsII();
		if (xy == null) return false;
		b.xy(xy);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_bitmap_create() {
		clearErrorMsg();
		if (!getNVar()) return false;								// get bitmap pointer variable
		Var.Val val = mVal;
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// get the width
		int width = EvalNumericExpressionValue.intValue();
		if (width <= 0) {
			return RunTimeError("Width must be >= 0");
		}
		if (!isNext(',')) return false;

		if (!evalNumericExpression()) return false;					// get the height
		int height = EvalNumericExpressionValue.intValue();
		if (height <= 0) {
			return RunTimeError("Height must be >= 0");
		}
		if (!checkEOL()) return false;

		String errMsg = null;
		Bitmap bitmap = null;
		try { bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); }
		catch (OutOfMemoryError oom) { errMsg = oom.getMessage(); }

		return createBitmap_finish(bitmap, val, errMsg);			// store the bitmap and return its index
	}

	private boolean execute_gr_rotate_start() {
		GR.BDraw b = new GR.BDraw(GR.Type.RotateStart);				// create a new object of type Rotate Start

		if (!evalNumericExpression()) return false;					// get angle
		b.angle(EvalNumericExpressionValue.floatValue());

		if (!isNext(',')) return false;
		int[] xy = getArgsII();
		if (xy == null) return false;
		b.xy(xy);

		Var.Val val = null;
		if (isNext(',')) {											// optional graphic object pointer variable
			if (!getNVar()) return false;
			val = mVal;
		}
		if (!checkEOL()) return false;

		if (val != null) { val.val(DisplayList.size()); }			// save the object number into the var
		DisplayListAdd(b);											// put the new object into the display list
		return true;
	}

	private boolean execute_gr_rotate_end() {
		GR.BDraw b = new GR.BDraw(GR.Type.RotateEnd);				// create a new object of type Rotate End

		Var.Val val = null;
		if (!isEOL()) {
			if (!getNVar()) return false;
			val = mVal;
			if (!checkEOL()) return false;
		}

		if (val != null) { val.val(DisplayList.size()); }			// save the object number into the var
		DisplayListAdd(b);											// add the object to the display list
		return true;
	}

	private boolean execute_gr_modify() {
		int obj = getObjectNumber("Object Number out of range");
		if (obj < 0) return false;
		GR.BDraw b = DisplayList.get(obj);							// get the object to change
		GR.Type type = b.type();

		while (isNext(',')) {
			if (!getStringArg()) return false;						// get the parameter string
			if (!isNext(',')) return false;
			String parm = StringConstant;

			String sVal = "";
			int iVal = 0;
			float fVal = 0.0f;
			if (parm.equals("text")) {
				if (!getStringArg()) return false;					// get the parameter string
				sVal = StringConstant;
			} else {
				if (!evalNumericExpression()) return false;			// get parameter value
				fVal = EvalNumericExpressionValue.floatValue();
				iVal = EvalNumericExpressionValue.intValue();
			}

			// For now, these list validations must be done here and not in BDraw.modify()
			if (parm.equals("paint")) {
				if ((iVal < 1) || (iVal >= PaintList.size())) {
					return RunTimeError ("Invalid Paint object number");
				}
				if (type == GR.Type.Group) {
					// Experiment: modify group Paint means modify Paint of all group objects.
					// TODO: This is very clumsy. Fix it.
					int dlSize = DisplayList.size();
					for (Double d : b.list()) {
						obj = d.intValue();							// get each index from the list
						if ((obj < 0) || (obj >= dlSize)) {
							return RunTimeError("Object out of range");
						}
						GR.BDraw toMod = DisplayList.get(obj);		// get each Graphics Object to change
						toMod.paint(iVal);							// modify it
					}												// note: the group's Paint gets changed, too
				}
				b.paint(iVal);
				continue;											// next parameter
			}
			switch (type) {
				case Bitmap:
					if (parm.equals("bitmap")) {
						if ((iVal < 0) | (iVal >= BitmapList.size())) {
							return RunTimeError("Bitmap pointer out of range");
						}
						b.bitmap(iVal);
						continue;									// next parameter
					}
					break;
				case Group:
					// Experiment: modify group alpha means modify alpha of all group objects.
					// TODO: This is very clumsy. Fix it.
					if (parm.equals("alpha")) {
						int dlSize = DisplayList.size();
						for (Double d : b.list()) {
							obj = d.intValue();						// get each index from the list
							if ((obj < 0) || (obj >= dlSize)) {
								return RunTimeError("Object out of range");
							}
							GR.BDraw toMod = DisplayList.get(obj);	// get each Graphics Object to change
							toMod.alpha(iVal);						// modify it
						}
						b.alpha(iVal);
						continue;									// next parameter
					}
					/* FALL THROUGH to handle "list" */
				case Poly:
					if (parm.equals("list")) {
						// For Now, list parm must be done here. Validate and attach list to poly object.
						if (!setPolyList(b, iVal))	return false;
						continue;									// next parameter
					}
				default:
					break;
			}
			if (!b.modify(parm, iVal, fVal, sVal)) {
				return RunTimeError(b.errorMsg());
			}
		}
		return checkEOL();
	}

	private boolean execute_gr_orientation() {
		if (!evalNumericExpression()) return false;					// get the mode (landscape or portrait)
		if (!checkEOL()) return false;

		int mode = EvalNumericExpressionValue.intValue();
//		Log.d(LOGTAG, "GR.Orientation " + mode);
		GR.drawView.setOrientation(mode);
		return true;
	}

	private boolean execute_gr_screen() {
		if (!getNVar()) return false;								// width variable
		Var.Val wVar = mVal;

		if (!isNext(',')) return false;
		if (!getNVar()) return false;								// height variable
		Var.Val hVar = mVal;

		Var.Val dVar = null;
		if (isNext(',')) {
			if (!getNVar()) return false;							// optional density variable
			dVar = mVal;
		}
		if (!checkEOL()) return false;

		Point size = new Point();
		int densityDpi = GR.drawView.getWindowMetrics(size);

		wVar.val(size.x);
		hVar.val(size.y);
		if (dVar != null) { dVar.val(densityDpi); }
		return true;
	}

	private boolean execute_gr_statusbar() {
		Var.Val heightVar = null;
		Var.Val showingVar = null;
		boolean isComma = isNext(',');
		if (!isComma) {
			if (!getNVar()) return false;							// height variable
			heightVar = mVal;
			isComma = isNext(',');
		}
		if (isComma) {
			if (!getNVar()) return false;							// showing variable
			showingVar = mVal;
		}
		if (!checkEOL()) return false;

		if (heightVar != null) {
			double height = 0.0;
			Resources res = getResources();
			int resID = res.getIdentifier("status_bar_height", "dimen", "android");
			if (resID > 0) {
				height = res.getDimensionPixelSize(resID);
			}
			heightVar.val(height);
		}
		if (showingVar != null) {
			showingVar.val(mShowStatusBar ? 1.0 : 0.0);
		}
		return true;
	}

	private boolean execute_gr_front() {
		if (!evalNumericExpression()) return false;					// get flag
		if (!checkEOL()) return false;

		if (EvalNumericExpressionValue == 0) {
			mConsoleIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
			startActivity(mConsoleIntent);
		} else {
			mGrIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mGrIntent);
		}
		try { Thread.sleep(100); } catch(InterruptedException e) {}
		return true;
	}

	private boolean execute_gr_set_pixels() {
		GR.BDraw b = createGrObj_start(GR.Type.SetPixels);			// create Graphic Object and get variable
		if (b == null)					return false;
		Var.Val saveVal = mVal;

		if (!isNext(','))				return false;
		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;
		if (!var.isNumeric()) { return RunTimeError(EXPECT_NUM_ARRAY); }

		Integer[] pair = new Integer[2];
		if (!getIndexPair(pair))		return false;				// get values inside [], if any

		int[] xy = { 0, 0 };
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			xy[0] = EvalNumericExpressionValue.intValue();
			if (!isNext(','))			return false;
			if (!evalNumericExpression()) return false;
			xy[1] = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL())				return false;

		Var.ArrayDef array = var.arrayDef();
		if (!getArraySegment(array, pair)) return false;			// get segment base and length
		int base = pair[0].intValue();
		int length = pair[1].intValue();
		if ((length % 2) != 0) {
			return RunTimeError("Not an even number of elements in pixel array");
		}

		b.array(array, base, length);
		b.xy(xy);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	private boolean execute_gr_get_bmpixel() {
		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0) return false;
		if (!isNext(',')) { return false; }

		Bitmap SourceBitmap = BitmapList.get(bitmapPtr);			// get the bitmap
		return getTheBMpixel(SourceBitmap);
	}

	private boolean execute_gr_get_pixel() {
		Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
		boolean retval = (b != null)
						? getTheBMpixel(b)							// get the requested pixel
						: RunTimeError("Could not capture screen bitmap. Sorry.");
		b = null;
		GR.drawView.destroyDrawingCache();							// clean up DrawingCache
		return retval;
	}

	// After it's done with the Bitmap, caller should call destroyDrawingCache()
	private Bitmap getTheBitmap() {
		synchronized (GR.drawView) {
			GR.drawView.setDrawingCacheEnabled(true);
			GR.drawView.buildDrawingCache();						// Build the cache
			return GR.drawView.getDrawingCache();					// get the bitmap
		}
	}

	private boolean getTheBMpixel(Bitmap b) {
		int[] xy = getArgsII();										// get x and y
		if (xy == null)					return false;				// error getting coordinate values
		if (!isNext(','))				return false;
		Var.Val[] argb = getArgs4NVar();							// [a, r, g, b]
		if (argb == null)				return false;				// error getting variables
		if (!checkEOL())				return false;				// end of syntax checks

		if (!checkBitmapPoint(b, xy))	return false;				// is point in bitmap?

		int pixel = b.getPixel(xy[0], xy[1]);						// get the pixel from the bitmap

		argb[0].val(Color.alpha(pixel));							// get the components of the pixel
		argb[1].val(Color.red(pixel));
		argb[2].val(Color.green(pixel));
		argb[3].val(Color.blue(pixel));

		return true;
	}

	private boolean checkBitmapPoint(Bitmap b, int xy[]) {
		if (b == null)	{ return RunTimeError("Bitmap was deleted"); }
		if (xy == null)	{ return false; }
		int x = xy[0];
		int y = xy[1];
		if ((x < 0) || (x >= b.getWidth()) || (y < 0) || (y >= b.getHeight())) {
			return RunTimeError("x or y exceeds size of bitmap");
		}
		return true;									// point is in bitmap
	}

	private boolean writeBitmapToFile(Bitmap b, String fn, int quality) {
		CompressFormat format = CompressFormat.PNG;					// assume png
		String tFN = fn.toUpperCase(Locale.getDefault());			// temp convert fn to upper case
		if (tFN.endsWith(".JPG")) format = CompressFormat.JPEG;		// test jpg
		else if (!tFN.endsWith(".PNG")) fn += ".png";				// test png

		File file = new File(Basic.getDataPath(fn));				// build full path
		FileOutputStream ostream = null;

		try {														// write the file
			file.createNewFile();
			ostream = new FileOutputStream(file);

			b.compress(format, quality, ostream);					// write png or jpg
			ostream.close();
		} catch (Exception e) {
			FileInfo.closeStream(ostream, null);
			return RunTimeError(e);
		}
		return true;
	}

	private boolean execute_gr_save() {

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

		Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
		boolean retval = (b != null)
						? writeBitmapToFile(b, fn, quality)
						: RunTimeError("Problem creating bitmap");
		b = null;
		GR.drawView.destroyDrawingCache();							// clean up DrawingCache
		return retval;
	}

	private boolean execute_screen_to_bitmap() {
		clearErrorMsg();
		if (!getNVar()) return false;
		Var.Val val = mVal;
		if (!checkEOL()) return false;

		String errMsg = null;
		Bitmap b = getTheBitmap();									// get the DrawingCache bitmap
		if (b == null) {
			errMsg = "Could not capture screen bitmap. Sorry.";
		} else {
			try { b = b.copy(Bitmap.Config.ARGB_8888, true); }		// copy the bitmap from the DrawingCache
			catch (Exception e) { errMsg = e.getMessage(); }
			catch (OutOfMemoryError oom) { errMsg = oom.getMessage(); }
		}
		createBitmap_finish(b, val, errMsg);						// store the bitmap and return its index
		GR.drawView.destroyDrawingCache();							// clean up DrawingCache
		return true;
	}

	private boolean execute_bitmap_save() {
		int bitmapPtr = getBitmapArg();								// get the bitmap number
		if (bitmapPtr < 0) return false;
		Bitmap SrcBitMap = BitmapList.get(bitmapPtr);				// get the bitmap
		if (!isNext(',')) return false;

		if (!getStringArg()) return false;							// get the filename
		String fn = StringConstant;

		int quality = 50;											// set default jpeg quality
		if (isNext(',')) {											// if there is an optional quality parm
			if (!evalNumericExpression()) return false;				// evaluate it
			quality = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL()) return false;

		if (SrcBitMap == null) { return RunTimeError("Bitmap was deleted"); }
		if (quality < 0 || quality > 100) {
			return RunTimeError("Quality must be between 0 and 100");
		}
		return writeBitmapToFile(SrcBitMap, fn, quality);
	}

	private boolean execute_gr_scale() {

		if (!evalNumericExpression())	return false;				// get x
		double x = EvalNumericExpressionValue;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// get y
		double y = EvalNumericExpressionValue;
		if (!checkEOL())				return false;

		GR.scaleX = (float) x;
		GR.scaleY = (float) y;

		return true;
	}

	private boolean execute_gr_clip() {
		GR.BDraw b = createGrObj_start(GR.Type.Clip);				// create Graphic Object and get variable
		if (b == null)					return false;
		Var.Val saveVal = mVal;

		if (!isNext(','))				return false;
		int[] ltrb = getArgs4I();
		if (ltrb == null)				return false;
		b.ltrb(ltrb);

		int RegionOp = 0;
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			RegionOp = EvalNumericExpressionValue.intValue();
			if (RegionOp < 0 || RegionOp > 5) {
				return RunTimeError("Region Operator not 0 to 5");
			}
		}
		b.clipOp(RegionOp);
		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	// Validate list and attach it to a Poly-type BDraw object.
	// Do it here, not in GR, for access to RunTimeError().
	private boolean setPolyList(GR.BDraw poly, int listIndex) {
		if ((listIndex < 1) || (listIndex >= theLists.size())) {
			return RunTimeError("Invalid list pointer");
		}
		if (theListsType.get(listIndex) != Var.Type.NUM) {
			return RunTimeError("List must be numeric");
		}
		ArrayList<Double> list = theLists.get(listIndex);
		int size = list.size();
		if (size < 4) {
			return RunTimeError("List must have at least two points");
		}
		if ((size % 2) != 0) {
			return RunTimeError("List must have even number of elements");
		}
		poly.list(listIndex, list);
		return true;
	}

	private boolean execute_gr_poly() {
		GR.BDraw b = createGrObj_start(GR.Type.Poly);				// create Graphic Object and get variable
		if (b == null)					return false;
		Var.Val saveVal = mVal;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;				// list pointer
		int theListIndex = EvalNumericExpressionValue.intValue();
		if (!setPolyList(b, theListIndex))	return false;			// validate and attach list to poly object

		int[] xy = { 0, 0 };
		if (isNext(',')) {
			if (!evalNumericExpression())	return false;
			xy[0] = EvalNumericExpressionValue.intValue();
			if (!isNext(','))				return false;
			if (!evalNumericExpression())	return false;
			xy[1] = EvalNumericExpressionValue.intValue();
		}
		b.xy(xy);

		return createGrObj_finish(b, saveVal);						// store the object and return its index
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private int getNumberOfCameras() {

		int cameraCount;
		int level = Build.VERSION.SDK_INT;
		if (level < Build.VERSION_CODES.GINGERBREAD) {				// if SDK < 9 there can be only one camera
			Camera tCamera = Camera.open();							// Check to see if there is any camera at all
			cameraCount = (tCamera == null) ? 0 : 1;
			tCamera.release();
		} else {
			cameraCount = Camera.getNumberOfCameras();				// May be more than one camera
		}
		return cameraCount;
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private boolean execute_gr_camera_select() {

		if (NumberOfCameras < 0) { NumberOfCameras = getNumberOfCameras(); }
		if (NumberOfCameras == 0) { return RunTimeError("This device does not have a camera."); }

		int level = Build.VERSION.SDK_INT;
		if (level < Build.VERSION_CODES.GINGERBREAD) {				// if SDK < 9 there can be only one camera
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

	private boolean execute_camera_shoot(int pictureMode) {

		if (NumberOfCameras < 0) { NumberOfCameras = getNumberOfCameras(); }
		if (NumberOfCameras == 0) { return RunTimeError("This device does not have a camera."); }

		int flashMode = 0;
		int focusMode = 0;
		if (!getNVar()) return false;
		Var.Val val = mVal;

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
		Intent cameraIntent = new Intent(Run.this, CameraView.class);	// Start the Camera
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

		double bitmapIndex = 0.0;
		if (CameraBitmap != null) {
			bitmapIndex = BitmapList.size();
			BitmapList.add(CameraBitmap);
		}
		val.val(bitmapIndex);											// Save the GR Object index into the var

		CameraBitmap = null;
		return true;
	}

	private boolean execute_statusbar_show() {
		String[] msg = {
			"This command deprecated.",							// First line is base of errorMsg
			"To show status bar, use:",
			"gr.open alpha, red, green, blue, 1"
		};
		return RunTimeError(msg);
	}

	private boolean execute_brightness() {
		if (!evalNumericExpression()) return false;
		if (!checkEOL()) return false;

		float value = EvalNumericExpressionValue.floatValue();
		if (value < 0.01f) { value = 0.01f; }
		if (value > 1.0f)  { value = 1.0f; }
		GR.Brightness = value;
		return true;
	}

	private boolean execute_gr_text_typeface() {
		int[] args = { 1, 1, -1 };									// default typeface and style, new Paint
		if (!getOptExprs(args)) return false;

		int face = args[0];
		int style = args[1];
		int paintIdx = args[2];

		Typeface tf;												// interpret typeface
		switch (face) {
			case 1: tf = Typeface.DEFAULT;    break;
			case 2: tf = Typeface.MONOSPACE;  break;
			case 3: tf = Typeface.SANS_SERIF; break;
			case 4: tf = Typeface.SERIF;      break;
			default: return RunTimeError("Typeface must be 1, 2, 3 or 4");
		}
		switch (style) {											// interpret style
			case 1: style = Typeface.NORMAL;      break;
			case 2: style = Typeface.BOLD;        break;
			case 3: style = Typeface.ITALIC;      break;
			case 4: style = Typeface.BOLD_ITALIC; break;
			default: return RunTimeError("Style must be 1, 2, 3 or 4");
		}

		Paint paint = getWorkingPaint(paintIdx);
		if (paint == null)				return false;				// index out of range

		// Done with error messages.
		tf = Typeface.create(tf, style);
		paint.setTypeface(tf);										// put the typeface into Paint

		return true;
	}

	private boolean execute_gr_touch_resume() {
		return doResume("No onTouch Interrupt");
	}

	private Typeface getTypeface(String fileName) {
		Typeface tf = null;
		File file = new File(Basic.getDataPath(fileName));
		if (file.exists()) {
			tf = Typeface.createFromFile(file);						// Create a new Typeface
		} else {													// the file does not exist
			if (Basic.isAPK) {										// we are in APK
				int resID = Basic.getRawResourceID(fileName);		// try to load the file from a raw resource
				if (resID != 0) {
					InputStream is = getResources().openRawResource(resID);
					String outPath = getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";
					File outFile = new File(outPath);
					if (copyFile(new BufferedInputStream(is), outFile, null)) {
						tf = Typeface.createFromFile(outPath);
						outFile.delete();							// clean up
					}
				} else {											// try to load the file from assets
					String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, fileName);
					tf = Typeface.createFromAsset(getAssets(), assetPath);
				}
			}
		}
		return tf;
	}

	private boolean execute_gr_text_setfont() {
		int fontPtr = 0;
		String familyName = null;									// default if no font arg
		int style = Typeface.NORMAL;								// default if no style arg
		int paintIdx = -1;											// default to current Paint

		boolean isComma = isNext(',');
		if (!isComma && !isEOL()) {									// there is a font arg
			int saveLI = LineIndex;
			fontPtr = getFontArg();									// get the font number
			if (fontPtr == -1)			return false;				// invalid font pointer
			if (fontPtr == -2) {									// not a numeric argument
				LineIndex = saveLI;
				if (!getStringArg())	return false;				// get the font family name
				familyName = StringConstant.trim();
			}
			isComma = isNext(',');
		}
		if (isComma) {
			if (!getStringArg())		return false;				// get the optional style
			String str = StringConstant.trim().toLowerCase(Locale.US);
			if      (str.equals("b")  || str.equals("bold"))        { style = Typeface.BOLD; }
			else if (str.equals("i")  || str.equals("italic"))      { style = Typeface.ITALIC; }
			else if (str.equals("bi") || str.equals("bold_italic")) { style = Typeface.BOLD_ITALIC; }
			isComma = isNext(',');
		}
		if (isComma) {
			if (!evalNumericExpression()) return false;
			paintIdx = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL())				return false;

		Typeface tf = null;
		if (fontPtr > 0) {
			tf = FontList.get(fontPtr);								// get specified custom font
			if (tf == null) { return RunTimeError("Font was deleted"); }
		} else if (fontPtr == 0) {									// no font arg
			for (int fp = FontList.size() - 1; (fp > 0) && (tf == null); --fp) {
				tf = FontList.get(fp);								// get last font loaded and not deleted
			}														// if no such font, will set default family
		}
		if (tf == null) {								// font family arg, or no font arg and no fonts loaded
			tf = Typeface.create(familyName, style);				// get the system font for this family name
		}															// null family name sets system default

		Paint paint = getWorkingPaint(paintIdx);					// null if invalid index
		if (paint == null)				return false;

		paint.setTypeface(tf);

		return true;
	}

	// ****************************************** Audio *******************************************

	private boolean executeAUDIO() {								// Get Audio command keyword if it is there
		return executeSubcommand(audio_cmd, "Audio");				// and execute the command
	}

	private MediaPlayer getMP(String fileName) {
		String errMsg = null;
		MediaPlayer mp = null;
		Context context = getApplicationContext();
		File file = new File(Basic.getDataPath(fileName));
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);								// Create Uri for the file
			if (uri != null) {
				mp = MediaPlayer.create(context, uri);		// Create a new Media Player
			}
		} else {														// the file does not exist
			if (Basic.isAPK) {											// we are in APK
				int resID = Basic.getRawResourceID(fileName);			// try to load the file from a raw resource
				if (resID != 0) {
					mp = MediaPlayer.create(context, resID);
				} else {												// try to load the file from assets
					AssetFileDescriptor afd = null;
					try {
						String assetPath = Basic.getAppFilePath(Basic.DATA_DIR, fileName);
						afd = getAssets().openFd(assetPath);
						mp = new MediaPlayer();
						mp.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
						mp.prepare();
					} catch (IOException e) {
						errMsg = e.getMessage();
						if (mp != null) {
							mp.release();
							mp = null;
						}
					} finally {
						if (afd != null) { try { afd.close(); } catch (IOException e) { } }
					}
				}
			} else { errMsg = "Audio file not found"; }
		}
		if (mp == null) {
			writeErrorMsg((errMsg != null) ? errMsg : "Unable to load audio file");
		}
		return mp;
	}

	private boolean execute_audio_load() {
		/* If there is already an audio running,
		 * then stop it and
		 * release its resources.
		 */

/*		if (theMP != null) {
			try {theMP.stop();} catch (IllegalStateException e) {}
			theMP.release();
			theMP = null;
		} */
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		AudioManager audioSM = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		clearErrorMsg();

		if (!getNVar())					return false;		// get the Player Number variable
		Var.Val val = mVal;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;		// get the file path
		if (!checkEOL())				return false;

		String fileName = StringConstant;					// the filename as given by the user
		MediaPlayer aMP = getMP(fileName);

		if (aMP == null) {
			val.val(0);										// indicate error with 0 in Player Number var
		} else {
			aMP.setAudioStreamType(AudioManager.STREAM_MUSIC);
			setVolumeControlStream(AudioManager.STREAM_MUSIC);

			val.val(theMPList.size());						// indicate success with list index in Player Number var
			theMPList.add(aMP);
			theMPNameList.add(fileName);
		}
		return true;
	}

	private boolean checkAudioFileTableIndex(int index) {
		if ((index <= 0) || (index >= theMPList.size())) {
			return RunTimeError("Invalid Audio File Table index");
		}
		return true;
	}

	private boolean execute_audio_release() {

		if (!evalNumericExpression())	return false;
		int index = EvalNumericExpressionValue.intValue();
		if (!checkAudioFileTableIndex(index)) return false;
		if (!checkEOL())				return false;

		MediaPlayer aMP = theMPList.get(index);
		if (aMP == null)				return true;

		if (theMP == aMP) { return RunTimeError("Must stop player before releasing"); }

		aMP.release();
		theMPList.set(index, null);

		return true;
	}

	private boolean execute_audio_play() {
		clearErrorMsg();
		if (!evalNumericExpression())	return false;
		int index = EvalNumericExpressionValue.intValue();
		if (!checkAudioFileTableIndex(index)) return false;
		if (!checkEOL())				return false;

		MediaPlayer aMP = theMPList.get(index);
		if (aMP == null)	{ return RunTimeError("Audio not loaded at:"); }
		if (theMP != null)	{ return RunTimeError("Stop Current Audio Before Starting New Audio"); }

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
//		Log.v(LOGTAG, "play " + aMP);

		try { aMP.prepare(); } catch (Exception e) { }
		aMP.start();

		if (!aMP.isPlaying()) {								// Somehow lost the player. Make a new one.
			aMP.release();
			aMP = getMP(theMPNameList.get(index));
			if (aMP == null) {
				RunTimeError("Media player synchronous problem.");
				return true;								// TODO: Is this correct?
			}
			theMPList.set(index, aMP);
			aMP.start();
		}

		aMP.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				PlayIsDone = true;
			}
		});
//		Log.v(LOGTAG, "is playing " + theMP.isPlaying());
		PlayIsDone = false;
		theMP = aMP;

		return true;
	}

	private boolean execute_audio_isdone() {
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		if (theMP == null) { PlayIsDone = true; }
		val.val((PlayIsDone) ? 1 : 0);

		return true;
	}

	private boolean execute_audio_loop() {
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!checkEOL())				return false;

		theMP.setLooping(true);
		return true;
	}

	private boolean execute_audio_stop() {
		if (!checkEOL())				return false;
		if (theMP == null)				return true;				// if theMP is null, Media player has stopped

//		MediaPlayer.setOnSeekCompleteListener(mSeekListener);
//		try {Thread.sleep(1000);}catch(InterruptedException e) {}
		try { theMP.stop(); }
		catch (Exception e) { return RunTimeError(e); }
		finally { theMP = null; }									// Signal MP stopped

		return true;
	}

	private boolean execute_audio_pause() {
		if (!checkEOL())				return false;
		if (theMP == null)				return true;				// if theMP is null, Media player has stopped

		try { theMP.pause(); }
		catch (Exception e) { return RunTimeError(e); }
		finally { theMP = null; }									// Signal MP stopped

		return true;
	}

	private boolean execute_audio_volume() {
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!evalNumericExpression())	return false;
		float left = EvalNumericExpressionValue.floatValue();

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;
		float right = EvalNumericExpressionValue.floatValue();
		if (!checkEOL())				return false;

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		theMP.setVolume(left, right);

		return true;
	}

	private boolean execute_audio_pcurrent() {
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		val.val(theMP.getCurrentPosition());
		return true;
	}

	private boolean execute_audio_pseek() {
		if (theMP == null) {
			return RunTimeError("Audio not playing at:");
		}
		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;

		int pos = EvalNumericExpressionValue.intValue();
		theMP.seekTo(pos);
		return true;
	}

	private boolean execute_audio_length() {
		if (!getNVar())					return false;				// Get the Player Number Var
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;
		int index = EvalNumericExpressionValue.intValue();
		if (!checkAudioFileTableIndex(index)) return false;
		if (!checkEOL())				return false;

		MediaPlayer aMP = theMPList.get(index);
		if (aMP == null) {
			return RunTimeError("Audio not loaded at:");
		}

		val.val(aMP.getDuration());
		return true;
	}

	private boolean execute_audio_record_start() {
		if (!getStringArg())			return false;
		String recordFileName = Basic.getDataPath(StringConstant);

		int source = 0;									// Get optional source
		if (isNext(',')) {
			if (!evalNumericExpression()) return false;
			source = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL())				return false;

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

		} catch (Exception e) { return RunTimeError("Audio record error: " + e); }

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
		} catch (Exception e) {}
		mRecorder = null;

		return true;
	}

	// ************************************* Sensors Package **************************************

	private boolean executeSENSORS() {							// Get Sensor command keyword if it is there
		return executeSubcommand(sensors_cmd, "Sensors");		// and execute the command
	}

	private boolean execute_sensors_list() {
		Var var = getArrayVarForWrite(TYPE_STRING);				// get the result array variable
		if (var == null)				return false;			// must name a string array variable
		if (!checkEOL())				return false;			// line must end with ']'

		if (theSensors == null) {
			theSensors = new SensorActivity(Run.this);
		}
		ArrayList<String> census = theSensors.takeCensus();
		int nSensors = census.size();
		if (nSensors == 0) {
			return RunTimeError("This device reports no Sensors");
		}

		/* Puts the list of sensors into a new array */
		return ListToBasicStringArray(var, census, nSensors);
	}

	private boolean execute_sensors_open() {
		if (theSensors == null) {
			theSensors = new SensorActivity(Run.this);
		}

		if (isEOL())					return false;
		boolean toOpen = false;									// anything to open?
		do {													// get the user's list of sensors to open
			if (!evalNumericExpression()) return false;			// get the sensor number
			int s = EvalNumericExpressionValue.intValue();
			int d = SensorManager.SENSOR_DELAY_NORMAL;
			if (isNext(':')) {
				if (!evalNumericExpression()) return false;		// get the sensor delay value
				d = EvalNumericExpressionValue.intValue();
			}
			toOpen |= theSensors.markForOpen(s, d);				// record selection
		} while (isNext(','));
		if (!checkEOL())				return false;

		if (toOpen) {											// if any valid selections
			theSensors.doOpen();								// open selected sensors
		}
		return true;
	}

	private boolean execute_sensors_read() {

		if (theSensors == null) {
			return RunTimeError("Sensors not opened at:");
		}
		if (!evalNumericExpression()) return false;				// Get Sensor Type
		int type = EvalNumericExpressionValue.intValue();

		if (type < 0 || type > SensorActivity.MaxSensors) {
			return RunTimeError("Sensor type not 0 to " + SensorActivity.MaxSensors);
		}

		Var.Val[] val = new Var.Val[4];							// 4 because that's what SensorActivity returns
		for (int i = 1; i < 4; ++i) {
			if (!isNext(','))			return false;
			if (!getNVar())				return false;			// Sensor Variable
			val[i] = mVal;
		}
		if (!checkEOL())				return false;

		double[] SensorValues = theSensors.getValues(type);
		for (int i = 1; i < 4; ++i) {
			val[i].val(SensorValues[i]);
		}
		return true;
	}

	private boolean execute_sensors_rotate() {

		/* This is a test.
		 * It has failed so far
		 * This command has not been exposed to the users.
		 * Someday....?
		 */

		float mOrientation[] = new float[3];
		double SensorValues[];

		SensorValues = theSensors.getValues(1);
		float g[] = new float[3];
		g[0] = (float)SensorValues[1];
		g[1] = (float)SensorValues[2];
		g[2] = (float)SensorValues[3];

		SensorValues = theSensors.getValues(1);
		float m[] = new float[3];
		m[0] = (float)SensorValues[1];
		m[1] = (float)SensorValues[2];
		m[2] = (float)SensorValues[3];

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
		mVal.val(mOrientation[0]);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;
		mVal.val(mOrientation[1]);
		if (!isNext(',')) return false;

		if (!getNVar()) return false;
		mVal.val(mOrientation[2]);
		if (!checkEOL()) return false;

		return true;
	}

	private boolean execute_sensors_close() {
		if (!checkEOL()) return false;
		if (theSensors != null) {
			theSensors.stop();
			theSensors = null;									// Tell everyone that Sensors are closed
		}
		return true;
	}

	// *************************************** GPS Package ****************************************

	private boolean executeGPS() {
		Command c = findSubcommand(GPS_cmd, "GPS");
		if (c == null) return false;

		if ((theGPS == null) && (c.id != CID_OPEN)) {
			return RunTimeError("GPS not opened at:");
		}
		return c.run();
	}

	public boolean execute_gps_open() {
		Var.Val statusVar = null;
		double errorCode = 1.0;
		long minTime = 0l;
		float minDistance = 0.0f;
		clearErrorMsg();

		boolean isComma = isNext(',');
		if (!isComma && !isEOL()) {							// there is a status var
			if (!getNVar())				return false;
			statusVar = mVal;
			isComma = isNext(',');
		}
		if (isComma) {
			isComma = isNext(',');
			if (!isComma) {
				if (!evalNumericExpression()) return false;	// get the minTime arg
				minTime = EvalNumericExpressionValue.longValue();
				if (minTime < 0) { return RunTimeError("Time less than zero"); }
				isComma = isNext(',');
			}
		}
		if (isComma) {
			if (!evalNumericExpression()) return false;		// get the minDistance arg
			minDistance = EvalNumericExpressionValue.longValue();
			if (minDistance < 0) { return RunTimeError("Distance less than zero"); }
		}
		if (!checkEOL())				return false;
		if (theGPS != null)				return true;		// already opened

		try {
			theGPS = new GPS(Run.this, minTime, minDistance);	// start GPS
		} catch (Exception e) {
			writeErrorMsg(e);
			errorCode = 0.0;
		}
		if (statusVar != null) { statusVar.val(errorCode); }
		return true;
	} // execute_gps_open

	public boolean execute_gps_close() {
		if (!checkEOL())				return false;
		if (theGPS != null) {
			Log.d(LOGTAG, "Stopping GPS on command");
			theGPS.stop();									// close GPS
			theGPS = null;
		}
		return true;
	}

	private boolean execute_gps_num(GPS.GpsData type) {
		if (!getNVar())					return false;		// Variable for returned value
		if (!checkEOL())				return false;
		double value = theGPS.getNumericValue(type);
		mVal.val(value);									// Set value into variable
		return true;
	}

	private boolean execute_gps_string(GPS.GpsData type) {
		if (!getSVar())					return false;
		if (!checkEOL())				return false;
		String value = theGPS.getStringValue(type);
		mVal.val((value == null) ? "" : value);
		return true;
	}

	private boolean execute_gps_satellites() {
		if (isEOL())					return true;		// user asked for no data

		Var.Val satCountVar = null;
		ArrayList<Object> sats = null;						// list of satellite bundles

		boolean isComma = isNext(',');
		if (!isComma) {
			if (!getNVar())				return false;		// variable for returned satellite count value
			satCountVar = mVal;
			isComma = isNext(',');
		}
		if (isComma) {
			int listIndex = getListArg(Var.Type.NUM);		// reuse old list or create new one
			if (listIndex < 0)			return false;
			sats = theLists.get(listIndex);
		}
		if (!checkEOL())				return false;

		if (satCountVar != null) {
			double count = theGPS.getNumericValue(GpsData.SATELLITES);
			satCountVar.val(count);							// set satellite count into variable
		}
		return updateGPSSatelliteList(sats);				// update list if user requested it
	} // execute_gps_satellites

	private boolean execute_gps_location() {
		if (isEOL())					return true;		// user asked for no data

		// returns time, provider, satellites, accuracy, latitude, longitude, altitude, bearing, and speed
		GpsData[] data = {
			GpsData.TIME, GpsData.PROVIDER, GpsData.SATELLITES, GpsData.ACCURACY,
			GpsData.LATITUDE, GpsData.LONGITUDE, GpsData.ALTITUDE,
			GpsData.BEARING, GpsData.SPEED
		};
		int nArgs = data.length;
		// PROVIDER is string, the rest are numeric
		byte[] types = { 1, 2, 1, 1, 1, 1, 1, 1, 1 };		// type of each variable
		Var.Val[] args = new Var.Val[nArgs];				// Val object for each variable

		if (!getOptVars(types, args))	return false;

		for (int arg = 0; arg < nArgs; ++arg) {
			Var.Val val = args[arg];
			if (val != null) {
				if (types[arg] == 1) {						// if numeric type
					val.val(theGPS.getNumericValue(data[arg]));
				} else {									// else string type
					val.val(theGPS.getStringValue(data[arg]));
				}
			}
		}
		return true;
	} // execute_gps_location

	private boolean execute_gps_status() {
		if (isEOL())					return true;		// user asked for no data

		// returns status, count of satellites in fix, count of satellites in view,
		// and list of satellite bundles
		Var.Val statusVar = null;
		boolean statusIsNumeric = true;
		Var.Val inFixVar = null;
		Var.Val inViewVar = null;
		ArrayList<Object> sats = null;						// list of satellite bundles

		boolean isComma = isNext(',');
		if (!isComma) {
			if (!getVar())				return false;		// status variable
			statusVar = mVal;
			statusIsNumeric = statusVar.isNumeric();		// may be either numeric or string
			isComma = isNext(',');
		}
		if (isComma) {
			isComma = isNext(',');
			if (!isComma) {
				if (!getNVar())			return false;		// inFix variable, numeric
				inFixVar = mVal;
				isComma = isNext(',');
			}
		}
		if (isComma) {
			isComma = isNext(',');
			if (!isComma) {
				if (!getNVar())			return false;		// inView variable, numeric
				inViewVar = mVal;
				isComma = isNext(',');
			}
		}
		if (isComma) {
			int listIndex = getListArg(Var.Type.NUM);		// reuse old list or create new one
			if (listIndex < 0)			return false;
			sats = theLists.get(listIndex);
		}
		if (!checkEOL())				return false;

		if (statusVar != null) {
			if (statusIsNumeric) {							// if type is numeric
				statusVar.val(theGPS.getNumericValue(GpsData.STATUS));
			} else {										// else type is string
				statusVar.val(theGPS.getStringValue(GpsData.STATUS));
			}
		}
		if (inFixVar != null) {
			inFixVar.val(theGPS.getNumericValue(GpsData.SATS_IN_FIX));
		}
		if (inViewVar != null) {
			inViewVar.val(theGPS.getNumericValue(GpsData.SATS_IN_VIEW));
		}
		return updateGPSSatelliteList(sats);				// update list if user requested it
	} // execute_gps_status

	private boolean updateGPSSatelliteList(ArrayList<Object> satList) {
		if (satList == null)			return true;		// No list, nothing to update

		// satList is a List of zero or more Bundle pointers (indices into theBundles).
		// Any Bundles present must contain satellite data.
		// We clear the stale data, but keep a history list of bundle pointers,
		// indexed by satellite PRN. When get new satellite bundles from GPS,
		// we match them by PRN to existing bundles, putting the new bundle in theBundles
		// where the old bundle was. New satellites add new bundles to theBundles.
		// Anything left in the history list is copied to the end of the user list.
		// That way we don't create a new bundle next time the same satellite reappears.

		// This is pretty convoluted, but it's all about re-using the satellite bundles,
		// or more accurately, reusing slots in theBundles list.
		// We use the user's list to keep pointers to every satellite ever seen.

		HashMap<Double, Double> oldList = validateSatelliteList(satList);
		if (oldList == null)			return false;		// list not valid
		satList.clear();									// erase the user's list

		getSatelliteBundles(oldList, satList);				// get latest satellite data from GPS
															// builds satList, removes items from oldList
		for (Double prn : oldList.keySet()) {				// if any satellite remain in the history list
			satList.add(oldList.get(prn));					// copy each index to the end of the user's list
		}
		return true;
	} // updateGPSSatelliteList

	// Validate user's satellite list: it must contain valid satellite bundles.
	// Build a history list that maps each satellite PRN to a bundle pointer (theBundles index).
	// Clear the user's list and return the history list. If error, return null.
	private HashMap<Double, Double> validateSatelliteList(ArrayList<Object> satList) {
		if (satList == null)			return null;
		if (!satList.isEmpty() && !(satList.get(0) instanceof Double)) {
			RunTimeError("Invalid Satellite List");
			return null;
		}
		HashMap<Double, Double> history = new HashMap<Double, Double>();
		for (Object o : satList) {							// for each satellite in the user's list
			Double sat = (Double)o;							// index of BASIC! bundle for one satellite
			int bSatIdx = sat.intValue();					// same thing as int
			if ((bSatIdx > 0) && (bSatIdx < theBundles.size())) {
				Bundle bSat = theBundles.get(bSatIdx);		// get the satellite bundle
				if (bSat.containsKey(GPS.KEY_PRN)) {
					Double prn = (Double)bSat.get(GPS.KEY_PRN);	// get the satellite's PRN
					history.put(prn, sat);						// map PRN to bundle index
					bSat.clear();								// clear stale data
					bSat.putDouble(GPS.KEY_PRN, prn);			// put the PRN back in
					continue;								// all conditions met; next satellite
				} // else no PRN
			} // else bad bundle index
			RunTimeError("Invalid Satellite Bundle");		// one of the conditions was not met
			history = null;
		}
		return history;
	} // validateSatelliteList

	// Get the GPS satellite data bundles.
	// Try to match them up by PRN to the pool of existing satellite bundles.
	// If match found, put the new bundle in theBundles at the old index and
	// remove the history item. If no bundle exists for a PRN, create a new one.
	// This is intended to allow a user to poll GPS without proliferating bundles!
	private void getSatelliteBundles(HashMap<Double, Double> pool, List<Object> list) {
		HashMap<Double, Bundle> satMap = theGPS.getSatellites();// map of new bundles of satellite data in Java format
		for (Double prn : satMap.keySet()) {
			Bundle jSat = satMap.get(prn);					// get each satellite's bundle
			Double sat = pool.get(prn);						// look for a corresponding bundle index
			if (sat != null) {								// existing satellite bundle
				int bSatIdx = sat.intValue();
				theBundles.set(bSatIdx, jSat);				// put the new bundle at the known index
			} else {										// no BASIC! bundle, create a new one
				int bSatIdx = theBundles.size();
				theBundles.add(jSat);
				sat = (double)bSatIdx;
			}
			list.add(sat);									// put the bundle index into the list
			pool.remove(prn);								// remove it from the history list
		}
	} // getSatelliteBundles

	// ************************************* Array Package ****************************************

	private boolean executeARRAY() {								// Get array command keyword if it is there
		return executeSubcommand(array_cmd, "Array");				// and execute the command
	}

	private boolean execute_array_dims() {							// get the dimensions of an array and put them in another array
		Var srcvar = getExistingArrayVar();							// get the source array variable (required)
		if (srcvar == null)				return false;
		if (!isNext(']'))				{ return RunTimeError(EXPECT_ARRAY_NO_INDEX); }
		if (isEOL())					return true;				// user supplied no desination variable(s)

		Var dimsVar = null;											// variable for array of dimensions
		Var.Val lenVal = null;										// variable for number of dimensions
		boolean isComma = isNext(',');
		if (isComma) {
			isComma = isNext(',');
			if (!isComma) {
				dimsVar = getArrayVarForWrite(TYPE_NUMERIC);		// get the array variable for dimensions
				isComma = isNext(',');
			}
		}
		if (isComma) {
			if (!getNVar())				return false;				// get the variable for number of dimensions
			lenVal = mVal;
		}
		if (!checkEOL())				return false;

		int[] dimList = ((Var.ArrayVar)srcvar).arrayDef().dimList();
		int size = dimList.length;
		if (dimsVar != null) {
			ArrayList<Double> dims = new ArrayList<Double>(size);
			for (int i = 0; i < size; ++i) { dims.add(Double.valueOf(dimList[i])); }
			if (!ListToBasicNumericArray(dimsVar, dims, size)) return false; // copy the list to a BASIC! array
		}
		if (lenVal != null) {
			lenVal.val(size);
		}
		return true;
	}

	private boolean execute_array_length() {

		if (!getNVar())					return false;				// Length Variable
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// Get values inside [], if any
		if (!checkEOL())				return false;				// line must end with ']'

		if (!getArraySegment(var.arrayDef(), p)) return false;		// get segment base and length
		int length = p[1].intValue();

		val.val(length);											// set the length into the var value

		return true;
	}

	private boolean execute_array_load() {
		Var var = getArrayVarForWrite();							// get the result array variable
		if (var == null)				return false;				// must name a new array variable
		if (!isNext(','))				return false;				// Comma before the list

		if (var.isNumeric()) {										// If the array is numeric
			ArrayList<Double> Values = new ArrayList<Double>();		// Create a list for the numeric values
			if (!LoadNumericList(Values)) return false;				// load numeric list
			if (!checkEOL())			return false;
			return ListToBasicNumericArray(var, Values, Values.size()); // Copy the list to a BASIC! array
		} else {
			ArrayList<String> Values = new ArrayList<String>();		// Create a list for the numeric values
			if (!LoadStringList(Values)) return false;				// load string list
			if (!checkEOL())			return false;
			return ListToBasicStringArray(var, Values, Values.size()); // Copy the list to a BASIC! array
		}
	}

	private boolean LoadNumericList(ArrayList <Double> Values) {
		while (true) {											// loop through the list
			if (!evalNumericExpression()) return false;			// values may be expressions
			Values.add(EvalNumericExpressionValue);

			String text = ExecutingLineBuffer.text();
			if (LineIndex >= text.length()) return false;
			char c = text.charAt(LineIndex);					// get the next character
			if (c == ',') {										// if it is a comma
				++LineIndex;									// skip it and continue looping
			} else break;										// else no more values in the list
		}
		return true;
	}

	private boolean LoadStringList(ArrayList <String> Values) {
		while (true) {											// loop through the list
			if (!getStringArg()) return false;					// values may be expressions
			Values.add(StringConstant);

			String text = ExecutingLineBuffer.text();
			if (LineIndex >= text.length()) return false;
			char c = text.charAt(LineIndex);					// get the next character
			if (c == ',') {										// if it is a comma
				++LineIndex;									// skip it and continue looping
			} else break;										// else no more values in the list
		}
		return true;
	}

	private boolean execute_array_collection(ArrayOrderOps op) {

		// This method implements several array commands

		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// Get values inside [], if any
		if (!checkEOL())				return false;				// line must end with ']'

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		if (var.isNumeric()) {										// Numeric Array
			double[] array = arrayDef.getNumArray();				// raw array access
			ArrayList<Double> Values = new ArrayList<Double>();		// Create a list to copy array values into

			for (int i = 0; i < length; ++i) {						// Copy the array values into that list
				Values.add(array[base + i]);
			}
			switch (op) {											// Execute the command specific procedure
				case DoReverse:		Collections.reverse(Values);	break;
				case DoSort:		Collections.sort(Values);		break;
				case DoShuffle:		Collections.shuffle(Values);	break;
			}
			for (int i = 0; i < length; ++i) {						// Copy the results back to the array
				array[base + i] = Values.get(i);
			}

		} else {													// Do the same stuff for a string array
			String[] array = arrayDef.getStrArray();				// raw array access
			ArrayList<String> Values = new ArrayList<String>();
			for (int i = 0; i < length; ++i) {
				Values.add(array[base + i]);
			}
			switch (op) {											// Execute the command specific procedure
				case DoReverse:		Collections.reverse(Values);	break;
				case DoSort:		Collections.sort(Values);		break;
				case DoShuffle:		Collections.shuffle(Values);	break;
			}
			for (int i = 0; i < length; ++i) {
				array[base + i] = Values.get(i);
			}
		}

		return true;
	}

	private boolean execute_array_sum(ArrayMathOps op) {

		if (!getNVar())					return false;				// get the value return variable
		Var.Val val = mVal;

		if (!isNext(','))				return false;
		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;
		if (!var.isNumeric()) { return RunTimeError(EXPECT_NUM_ARRAY); }

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// get values inside [], if any
		if (!checkEOL())				return false;				// line must end with ']'

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();
		double[] array = arrayDef.getNumArray();					// raw array access

		double Sum = array[base];
		double Min = Sum;
		double Max = Min;

		for (int i = 1; i < length; ++i) {							// loop through the array values
			double d = array[base + i];								// pick up the element's value
			Sum += d;												// build the Sum
			if (d < Min) { Min = d; }								// find the minimum value
			if (d > Max) { Max = d; }								// and the maxium value
		}
		double Average = Sum / length;								// calculate the average

		switch (op) {												// set the return value according to the command
			case DoAverage:	val.val(Average);	break;
			case DoSum:		val.val(Sum);		break;
			case DoMax:		val.val(Max);		break;
			case DoMin:		val.val(Min);		break;
			case DoVariance:
			case DoStdDev:
				double T = 0;
				double W = 0;
				for (int i = 0; i < length; ++i) {
					double d = array[base + i];						// pick up the element's value
					W = d - Average;
					T = T + W*W;
				}
				double variance = T/(length-1);
				if (op == ArrayMathOps.DoVariance) {
					val.val(variance);
				} else {											// DoStdDev
					val.val(Math.sqrt(variance));
				}
				break;
		}

		return true;
	}

	private boolean execute_array_fill() {
		Var dstVar = getExistingArrayVar();							// get the array variable
		if (dstVar == null)				return false;
		boolean isNumeric = dstVar.isNumeric();

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// get values inside [], if any

		Var.ArrayDef arrayDef = dstVar.arrayDef();
		if (!getArraySegment(arrayDef, p))	return false;			// get array segment base and length
		int start = p[0].intValue();
		int length = p[1].intValue();

		if (!isNext(','))				return false;				// move to the value

		if (!isNumeric) {											// string type array
			if (!getStringArg())		return RunTimeError("Array and fill must be same type");
			String fill = StringConstant;							// get the string to put in array
			if (!checkEOL())			return false;

			String[] array = dstVar.arrayDef().getStrArray();		// Caution! Raw array access!
			Arrays.fill(array, start, start + length, fill);		// fill the array
		} else {													// numeric type array
			if (!evalNumericExpression()) return RunTimeError("Array and fill must be same type");
			double fill = EvalNumericExpressionValue;				// get the value to put in array
			if (!checkEOL())			return false;

			double[] array = dstVar.arrayDef().getNumArray();		// Caution! Raw array access!
			Arrays.fill(array, start, start + length, fill);		// fill the array
		}
		return true;
	}

	private boolean execute_array_copy() {
															// **** Source Array ****

		Var var = getVarAndType();
		if (var == null)						return false;		// get the array variable
		if (!var.isArray())						return RunTimeError("Source not array");
		if (var.isNew())						return RunTimeError("Source array does not exist");
		boolean srcNumeric = var.isNumeric();

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))					return false;		// get values inside [], if any

		Var.ArrayDef srcArray = var.arrayDef();
		if (!getArraySegment(srcArray, p))		return false;		// get source segment base and length
		int srcBase = p[0].intValue();
		int srcLength = p[1].intValue();

		if (!isNext(','))						return false;
															// *** Destination Array ***

		var = getVarAndType();										// get the source array variable
		if (var == null)						return false;
		if (!var.isArray())						return RunTimeError("Destination not array");
		if (srcNumeric != var.isNumeric())		return RunTimeError("Arrays not of same type");
		boolean dstIsNew = var.isNew();
		Var dstVar = var;
		Var.ArrayDef dstArray;

		int extras = 0;												// get the extras parameter
		if (!isNext(']')) {
			if (!evalNumericExpression())		return false;
			if (!isNext(']'))					return false;
			extras = EvalNumericExpressionValue.intValue();
		}
		if (!checkEOL())						return false;

		int dstStart = 0;
		int totalLength = 0;
		if (dstIsNew) {												// copy to new array, optional extras arg adds element(s)

			if (extras == 0) {										// check for cases that would create empty array
				if (srcLength < 1)
					return RunTimeError("Source array [Start,Length] must specify at least one element");
			}
			totalLength = srcLength + Math.abs(extras);				// go build a new array of the proper size and type
			if (!BuildBasicArray(dstVar, totalLength)) return false;
			dstArray = dstVar.arrayDef();							// get the destination array
			if (extras < 0) { dstStart -= extras; }					// if negative extras, add absolute value to start index

		} else {													// copy over old array, optional extras arg is start index
			if (srcLength < 1)					return true;		// nothing to do

			dstArray = dstVar.arrayDef();							// get the destination array
			int dstLength = dstArray.length();						// get the destination array length
			if (extras > dstLength)				return true;		// dest start is past end of array, nothing to do

			if (--extras < 0) { extras = 0; }						// convert to 0-based index, ignore if less than 1
			if (extras + srcLength > dstLength) {					// start index + length to copy > space available
				srcLength = dstLength - extras;						// limit copy
			} else {
				dstLength = extras + srcLength;
			}
			dstStart = extras;
			totalLength = dstLength - extras;
		}

		if (srcNumeric) {											// do numeric array
			double[] sarray = srcArray.getNumArray();				// raw array access
			double[] darray = dstArray.getNumArray();				// raw array access
			for (int i = 0; i < srcLength; ++i) {					// copy the source array values
				darray[dstStart++] = sarray[srcBase++];
			}
		} else {													// do String array
			String[] sarray = srcArray.getStrArray();				// raw array access
			String[] darray = dstArray.getStrArray();				// raw array access
			for (int i = 0; i < srcLength; ++i) {					// copy the source array values
				darray[dstStart++] = sarray[srcBase++];
			}
		}
		return true;
	}

	private boolean execute_array_search() {
		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// get values inside [], if any

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		if (!isNext(','))				return false;				// move to the value

		Var.Val val = null;
		int start = 0;
		int found = -1;

		if (!var.isNumeric()) {										// String type array
			if (!getStringArg())		return false;				// get the string to search for
			String sfind = StringConstant;

			if (!isNext(','))			return false;				// move to the result var
			if (!getNVar())				return false;
			val = mVal;

			if (isNext(',')) {										// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL())			return false;

			String[] array = arrayDef.getStrArray();				// raw array access
			for (int i = start; i < length; ++i) {					// Search the list for a match
				if (sfind.equals(array[base + i])) {
					found = i;
					break;
				}
			}
		} else {													// Numeric type array
			if (!evalNumericExpression()) return false;				// Get the value to search for
			double nfind = EvalNumericExpressionValue;

			if (!isNext(','))			return false;				// move to the result var
			if (!getNVar())				return false;
			val = mVal;

			if (isNext(',')) {										// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL())			return false;

			double[] array = arrayDef.getNumArray();				// raw array access
			for (int i = start; i < length; ++i) {					// Search the list for a match
				if (nfind == array[base + i]) {
					found = i;
					break;
				}
			}
		}

		val.val(++found);											// return found as 1-based index
		return true;
	}

	// *************************************** List Package ***************************************

	private boolean executeLIST() {								// Get list command keyword if it is there
		return executeSubcommand(list_cmd, "List");				// and execute the command
	}

	private boolean execute_LIST_NEW() {
		char c = ExecutingLineBuffer.text().charAt(LineIndex);	// Get the type, s or n
		++LineIndex;

		Var.Type type = Var.Type.typeOf(c);
		if (type == Var.Type.NOVAR)		return false;			// Unknown type, don't create anything

		if (!isNext(','))				return false;
		if (!getNVar())					return false;			// List pointer variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		int theIndex = createNewList(type, val);				// Try to create list, -1 if fail
		return (theIndex >= 0);									// true if create succeeded
	}

	private int createNewList(Var.Type type, Var.Val val) {		// Put a new ArrayList in global theLists
																// Put its type in global theListsType
																// Write its index to user variable var
		int listIndex = theLists.size();
		switch (type) {
			case NUM:	theLists.add(new ArrayList<Double>());	break;	// Create a numeric list
			case STR:	theLists.add(new ArrayList<String>());	break;	// Create a string list
			default:	return -1;										// Unknown type, don't create anything
		}
		theListsType.add(type);									// Add the type
		val.val(listIndex);										// tell the user where it is
		return listIndex;
	}

	private int getListArg() {									// Get the List pointer
		return getListArg("Invalid List Pointer");
	}

	private int getListArg(String errorMsg) {					// Get the List pointer
		if (evalNumericExpression()) {
			int listIndex = EvalNumericExpressionValue.intValue();
			if ((listIndex > 0) && (listIndex < theLists.size())) {
				return listIndex;
			}
			RunTimeError(errorMsg);
		}
		return -1;
	}

	private int getListArg(Var.Type type) {
		// Type-restricted auto-create: if the command argument is a valid list pointer (index)
		// of the correct type, return the list pointer for re-use. If it is not (i.e., it is
		// out of range or the wrong type), and the command line argument is a simple numeric
		// variable, create a new list and return its index.
		// Otherwise post a RunTimeError and return -1.
		// NOTE: if this method auto-creates a list, it writes the index to the user's variable.
		int startLI = LineIndex;
		if (evalNumericExpression()) {
			int listIndex = EvalNumericExpressionValue.intValue();
			if ((listIndex > 0) && (listIndex < theLists.size()) &&		// in range
				(theListsType.get(listIndex) == type)) {				// type ok
				return listIndex;							// expression is valid string List pointer
			}
			int endLI = LineIndex;
			LineIndex = startLI;
			if (getNVar() && (LineIndex == endLI)) {		// if NVar is entire expression
				Var.Val val = mVal;
				return createNewList(type, val);			// try to create list, -1 if fail
			}												// create writes index to user variable
			LineIndex = endLI;
			RunTimeError("Invalid " + type.toString() + " List Pointer");
		}
		return -1;
	}

	private boolean execute_LIST_ADDARRAY() {
		int listIndex = getListArg();								// get the list pointer
		if (listIndex < 0)				return false;

		if (!isNext(','))				return false;
		Var var = getExistingArrayVar();							// get the array variable
		if (var == null)				return false;

		Integer[] p = new Integer[2];
		if (!getIndexPair(p))			return false;				// get values inside [], if any
		if (!checkEOL())				return false;

		boolean isListNumeric = (theListsType.get(listIndex) == Var.Type.NUM);
		if (isListNumeric != var.isNumeric()) { return RunTimeError("Type mismatch"); }

		Var.ArrayDef arrayDef = var.arrayDef();
		if (!getArraySegment(arrayDef, p)) return false;			// get segment base and length
		int base = p[0].intValue();
		int length = p[1].intValue();

		if (isListNumeric) {
			ArrayList<Double> destList = theLists.get(listIndex);	// copy array to list
			double[] array = arrayDef.getNumArray();				// raw array access
			for (int i = 0; i < length; ++i ) {
				destList.add(array[base + i]);						// Caution! No range checking!
			}
		} else {
			ArrayList<String> destList = theLists.get(listIndex);	// copy array to list
			String[] array = arrayDef.getStrArray();				// raw array access
			for (int i = 0; i < length; ++i ) {
				destList.add(array[base + i]);						// Caution! No range checking!
			}
		}
		return true;
	}

	private boolean execute_LIST_ADDLIST() {
		int destListIndex = getListArg("Invalid Destination List Pointer");	// Get the destination list pointer
		if (destListIndex < 0)			return false;
		if (!isNext(','))				return false;

		int sourceListIndex = getListArg("Invalid Source List Pointer");	// Get the source list pointer
		if (sourceListIndex < 0)		return false;
		if (!checkEOL())				return false;

		Var.Type destType = theListsType.get(destListIndex);
		Var.Type sourceType = theListsType.get(sourceListIndex);
		if (destType != sourceType) { return RunTimeError("Type mismatch"); }

		theLists.get(destListIndex).addAll(theLists.get(sourceListIndex));
		return true;
	}

	private boolean execute_LIST_SEARCH() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;				// move to the value

		Var.Val val = null;
		int start = 0;
		int found = -1;

		Var.Type type;
		type = theListsType.get(listIndex).isNS();					// ensure either numeric or sring

		if (type == Var.Type.STR) {									// String type list
			ArrayList<String> SValues = theLists.get(listIndex);	// Get the string list
			if (!getStringArg())		return false;				// values may be expressions
			String sfind = StringConstant;

			if (!isNext(','))			return false;				// move to the result var
			if (!getNVar())				return false;
			val = mVal;

			if (isNext(',')) {										// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL())			return false;

			for (int i = start; i < SValues.size(); ++i) {			// search the list for a match
				if (sfind.equals(SValues.get(i))) {
					found = i;
					break;
				}
			}
		} else {													// Numeric type list
			ArrayList<Double> NValues = theLists.get(listIndex);	// Get the numeric list
			if (!evalNumericExpression()) return false;				// values may be expressions
			double nfind = EvalNumericExpressionValue;

			if (!isNext(','))			return false;				// move to the result var
			if (!getNVar())				return false;
			val = mVal;

			if (isNext(',')) {										// move to the start index
				if (!evalNumericExpression()) return false;
				start = EvalNumericExpressionValue.intValue();
				if (--start < 0) { start = 0; }						// convert to zero-based index
			}
			if (!checkEOL())			return false;

			for (int i = start; i < NValues.size(); ++i) {			// search the list for a match
				if (nfind == (NValues.get(i))) {
					found = i;
					break;
				}
			}
		}

		val.val(++found);											// return found as 1-based index
		return true;
	}

	private boolean execute_LIST_ADD() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;				// move to the result value

		Var.Type type;
		type = theListsType.get(listIndex).isNS();					// ensure either numeric or sring

		if (type == Var.Type.NUM) {
			ArrayList<Double> Values = theLists.get(listIndex);		// Get the numeric list
			if (!LoadNumericList(Values)) return false;				// load numeric list
		} else {
			ArrayList<String> Values = theLists.get(listIndex);		// Get the string list
			if (!LoadStringList(Values)) return false;				// load string list
		}
		return checkEOL();
	}

	private boolean execute_LIST_SET() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;

		if (!evalNumericExpression())	return false;				// Get the index to get
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(','))				return false;

		Var.Type type;
		type = theListsType.get(listIndex).isNS();					// ensure either numeric or sring

		if (type == Var.Type.STR) {									// String type list
			if (!evalStringExpression()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL())			return false;
			ArrayList<String> thisList = theLists.get(listIndex);	// Get the string list
			if (getIndex < 0 || getIndex >= thisList.size()) {
				return RunTimeError("Index out of bounds");
			}
			thisList.set(getIndex, StringConstant);
		} else {													// Numeric type list
			if (!evalNumericExpression()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL())			return false;
			ArrayList<Double> thisList = theLists.get(listIndex);// Get the numeric list
			if (getIndex < 0 || getIndex >= thisList.size()) {
				return RunTimeError("Index out of bounds");
			}
			thisList.set(getIndex, EvalNumericExpressionValue);
		}

		return true;
	}

	private boolean execute_LIST_GET() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;

		if (!evalNumericExpression())	return false;				// Get the index to get
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(','))				return false;
		if (!getVar())					return false;				// Get the return value variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		Var.Type listType;											// Get this list's type
		listType = theListsType.get(listIndex).isNS();				// ensure either numeric or sring

		boolean isListNumeric = listType.isNumeric();
		if (isListNumeric != val.isNumeric()) { return RunTimeError("Type mismatch"); }

		if (!isListNumeric) {										// String type list
			ArrayList<String> thisStringList = theLists.get(listIndex);	// Get the string list
			if (getIndex < 0 || getIndex >= thisStringList.size()) {
				return RunTimeError("Index out of bounds");
			}
			String thisString = thisStringList.get(getIndex);		// Get the requested string
			val.val(thisString);
		} else {													// Numeric type list
			ArrayList<Double> thisNumericList = theLists.get(listIndex);// Get the numeric list
			if (getIndex < 0 || getIndex >= thisNumericList.size()) {
				return RunTimeError("Index out of bounds");
			}
			Double thisNumber = thisNumericList.get(getIndex);		// Get the requested number
			val.val(thisNumber);
		}

		return true;
	}

	private boolean execute_LIST_GETTYPE() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;

		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		val.val(theListsType.get(listIndex).typeNS());
		return true;
	}

	private boolean execute_LIST_CLEAR() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!checkEOL())				return false;

		ArrayList<?> list = theLists.get(listIndex);
		list.clear();
		list.trimToSize();
		return true;
	}

	private boolean execute_LIST_REMOVE() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;

		if (!evalNumericExpression())	return false;				// Get the index to remove
		if (!checkEOL())				return false;

		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		ArrayList theList = theLists.get(listIndex);				// Get the  list
		if (getIndex < 0 || getIndex >= theList.size()) {
			return RunTimeError("Index out of bounds");
		}
		theList.remove(getIndex);
		return true;
	}

	private boolean execute_LIST_INSERT() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;

		if (!evalNumericExpression())	return false;				// Get the index insert at
		int getIndex = EvalNumericExpressionValue.intValue();
		--getIndex;													// Ones based for Basic user

		if (!isNext(','))				return false;

		Var.Type listType;											// Get this list's type
		listType = theListsType.get(listIndex).isNS();				// ensure either numeric or sring

		if (listType == Var.Type.STR) {								// String type list
			if (!getStringArg()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL())			return false;

			ArrayList<String> thisStringList = theLists.get(listIndex);	// Get the string list
			if (getIndex < 0 || getIndex > thisStringList.size()) {		// if index == size element goes at end of list
				return RunTimeError("Index out of bounds");
			}
			thisStringList.add(getIndex, StringConstant);
		} else {													// Numeric type list
			if (!evalNumericExpression()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL())			return false;

			ArrayList<Double> thisNumericList = theLists.get(listIndex);// Get the numeric list
			if (getIndex < 0 || getIndex > thisNumericList.size()) {	// if index == size element goes at end of list
				return RunTimeError("Index out of bounds");
			}
			thisNumericList.add(getIndex, EvalNumericExpressionValue);
		}

		return true;
	}

	private boolean execute_LIST_SIZE() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;				// move to the return var

		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		int size = theLists.get(listIndex).size();
		val.val(size);

		return true;
	}

	private boolean execute_LIST_TOARRAY() {
		int listIndex = getListArg();								// Get the list pointer
		if (listIndex < 0)				return false;
		if (!isNext(','))				return false;				// move to the array var

		Var var = getArrayVarForWrite();							// get the result array variable
		if (var == null)				return false;				// must name a new array variable
		if (!checkEOL())				return false;				// line must end with ']'

		Var.Type listType;											// Get this list's type
		listType = theListsType.get(listIndex).isNS();				// ensure either numeric or sring

		boolean isListNumeric = listType.isNumeric();
		if (isListNumeric != var.isNumeric()) { return RunTimeError("Type mismatch"); }

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
		return executeSubcommand(bundle_cmd, "Bundle");			// and execute the command
	}

	private boolean execute_BUNDLE_CREATE() {
		if (!getNVar() || !checkEOL()) return false;			// get the Bundle pointer variable
		Var.Val val = mVal;
		createBundle(val);
		return true;
	}

	private int createBundle(Var.Val val) {						// create a new bundle and put it on the list
		int bundleIndex = theBundles.size();
		theBundles.add(new Bundle());
		val.val(bundleIndex);
		return bundleIndex;
	}

	private int getBundleArg() {								// Get the Bundle pointer
		// Auto-create: if the command argument is a valid bundle pointer (index),
		// return the bundle pointer for re-use. If it is not, and the command line argument
		// is a simple numeric variable, create a new bundle and return its index.
		// Otherwise post a RunTimeError and return -1.
		// NOTE: if this method auto-creates a bundle, it writes the index to the user's variable.
		int startLI = LineIndex;
		if (evalNumericExpression()) {
			int bundleIndex = EvalNumericExpressionValue.intValue();
			if ((bundleIndex > 0) && (bundleIndex < theBundles.size())) {
				return bundleIndex;								// expression is valid Bundle pointer
			}
			int endLI = LineIndex;
			LineIndex = startLI;
			if (getNVar() && (LineIndex == endLI)) {			// if NVar is entire expression
				Var.Val val = mVal;
				return createBundle(val);						// create a new Bundle
			}
			LineIndex = endLI;
			RunTimeError("Invalid Bundle Pointer");
		}
		return -1;
	}

	private boolean execute_BUNDLE_PUT() {
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex < 0)			return false;

		if (!isNext(','))				return false;					// move to the tag
		if (!getStringArg())			return false;
		String tag = StringConstant;

		if (!isNext(','))				return false;					// move to the value

		Bundle b = theBundles.get(bundleIndex);

		int LI = LineIndex;
		if (evalNumericExpression()) {
			if (!checkEOL())			return false;
			b.putDouble(tag, EvalNumericExpressionValue);
		} else {
			LineIndex = LI;
			if (!getStringArg())		return false;
			if (!checkEOL())			return false;
			b.putString(tag, StringConstant);
		}
		return true;
	}

	private boolean execute_BUNDLE_GET() {
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex < 0)			return false;

		if (!isNext(','))				return false;					// move to the tag
		if (!getStringArg())			return false;
		String tag = StringConstant;

		if (!isNext(','))				return false;					// move to the value variable
		if (!getVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;
		boolean varIsNumeric = val.isNumeric();

		Bundle b = theBundles.get(bundleIndex);
		if (!b.containsKey(tag)) {
			return RunTimeError(tag + " not in bundle");
		}

		Object o = b.get(tag);
		if (o instanceof Double) {
			if (!varIsNumeric) { return RunTimeError(tag + " is not a string"); }
			val.val(((Double)o).doubleValue());
		} else {
			if (varIsNumeric) { return RunTimeError(tag + " is not numeric"); }
			val.val((String)o);
		}
		return true;
	}

	private boolean execute_BUNDLE_NEXT() {
		return false;
	}

	private boolean execute_BUNDLE_TYPE() {
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex < 0)			return false;

		if (!isNext(','))				return false;					// move to the tag
		if (!getStringArg())			return false;
		String tag = StringConstant;

		if (!isNext(','))				return false;					// move to the result var
		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		Bundle b = theBundles.get(bundleIndex);
		if (!b.containsKey(tag)) { return RunTimeError(tag + " not in bundle"); }

		Var.Type type = (b.get(tag) instanceof Double) ? Var.Type.NUM : Var.Type.STR;
		val.val(type.typeNS());
		return true;
	}

	private boolean execute_BUNDLE_KEYSET() {
		int bundleIndex = getBundleArg();					// get the Bundle pointer
		if (bundleIndex < 0)			return false;

		if (!isNext(','))				return false;		// move to the list var
		int listIndex = getListArg(Var.Type.STR);			// get a reusable List pointer - may create new list
		if (listIndex < 0)				return false;		// failed to get or create a list
		if (!checkEOL())				return false;

		Bundle b = theBundles.get(bundleIndex);
		ArrayList<String> theStringList = new ArrayList<String>(b.keySet());
		theLists.set(listIndex, theStringList);				// put the new list on the list of lists

		return true;
	}

	private boolean execute_BUNDLE_COPY() {
		return false;
	}

	private boolean execute_BUNDLE_CLEAR() {
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex < 0)			return false;
		if (!checkEOL())				return false;

		Bundle b = theBundles.get(bundleIndex);
		b.clear();
		return true;
	}

	private boolean execute_BUNDLE_CONTAIN() {
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex < 0)			return false;

		if (!isNext(','))				return false;					// move to the tag
		if (!getStringArg())			return false;
		String tag = StringConstant;

		if (!isNext(','))				return false;					// move to the result var
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		Bundle b = theBundles.get(bundleIndex);
		val.val((b.containsKey(tag)) ? 1.0 : 0.0);

		return true;
	}

	private boolean execute_BUNDLE_REMOVE() {
		int bundleIndex = getBundleArg();								// Get the Bundle pointer
		if (bundleIndex < 0)			return false;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;					// Get the key to remove
		if (!checkEOL())				return false;
		String key = StringConstant;

		Bundle theBundle = theBundles.get(bundleIndex);					// Get the  bundle
		theBundle.remove(key);											// Remove the requested key
		return true;
	}

	// ************************************** Stack Package ***************************************

	private boolean executeSTACK() {							// Get stack command keyword if it is there
		return executeSubcommand(stack_cmd, "Stack");			// and execute the command
	}

	private boolean execute_STACK_CREATE() {
		char c = ExecutingLineBuffer.text().charAt(LineIndex);	// Get the type, s or n
		++LineIndex;

		Var.Type type = Var.Type.typeOf(c);
		if (type == Var.Type.NOVAR)		return false;			// Unknown type, don't create anything

		if (!isNext(','))				return false;
		if (!getNVar())					return false;			// stack pointer variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		Stack theStack = new Stack();
		int theIndex = theStacks.size();
		theStacks.add(theStack);

		theStacksType.add(type);								// add the type
		val.val(theIndex);										// return the stack pointer
		return true;
	}

	private int getStackIndexArg() {							// get the Stack pointer
		if (evalNumericExpression()) {
			int stackIndex = EvalNumericExpressionValue.intValue();
			if ((stackIndex > 0) && (stackIndex < theStacks.size())) {
				return stackIndex;
			}
			RunTimeError("Invalid Stack Pointer");
		}
		return -1;
	}

	private boolean execute_STACK_PUSH() {
		int stackIndex = getStackIndexArg();					// get the Stack pointer
		if (stackIndex < 0)				return false;
		if (!isNext(','))				return false;			// move to the value

		Stack thisStack = theStacks.get(stackIndex);			// get the stack

		Var.Type type;
		type = theStacksType.get(stackIndex).isNS();			// ensure either numeric or sring

		if (type == Var.Type.STR) {								// string stack
			if (!getStringArg()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL())			return false;
			thisStack.push(StringConstant);						// Add the string to the stack
		} else {												// numeric stack
			if (!evalNumericExpression()) {
				return RunTimeError("Type mismatch");
			}
			if (!checkEOL())			return false;
			thisStack.push(EvalNumericExpressionValue);			// Add the value to the stack
		}
		return true;
	}

	private boolean execute_STACK_POP() {
		int stackIndex = getStackIndexArg();					// Get the Stack pointer
		if (stackIndex < 0)				return false;
		if (!isNext(','))				return false;			// move to the value

		Stack thisStack = theStacks.get(stackIndex);			// Get the Stack
		if (thisStack.isEmpty()) {
			return RunTimeError("Stack is empty");
		}

		Var.Type stackType;
		stackType = theStacksType.get(stackIndex).isNS();		// ensure either numeric or sring
		boolean isStackNumeric = stackType.isNumeric();

		if (!getVar())					return false;
		Var.Val val = mVal;
		if (isStackNumeric != val.isNumeric()) { return RunTimeError("Type mismatch"); }
		if (!checkEOL())				return false;

		if (!isStackNumeric) {									// string stack
			String thisString = (String) thisStack.pop();
			val.val(thisString);
		} else {												// numeric stack
			double thisNumber = ((Double)thisStack.pop()).doubleValue();
			val.val(thisNumber);
		}
		return true;
	}

	private boolean execute_STACK_PEEK() {
		int stackIndex = getStackIndexArg();					// Get the Stack pointer
		if (stackIndex < 0)				return false;
		if (!isNext(','))				return false;			// move to the value

		Stack thisStack = theStacks.get(stackIndex);			// Get the Stack
		if (thisStack.isEmpty()) {
			return RunTimeError("Stack is empty");
		}

		Var.Type stackType;
		stackType = theStacksType.get(stackIndex).isNS();		// ensure either numeric or sring
		boolean isStackNumeric = stackType.isNumeric();

		if (!getVar())					return false;
		Var.Val val = mVal;
		if (isStackNumeric != val.isNumeric()) { return RunTimeError("Type mismatch"); }
		if (!checkEOL())				return false;

		if (!isStackNumeric) {									// string stack
			String thisString = (String) thisStack.peek();
			val.val(thisString);
		} else {												// numeric stack
			double thisNumber = ((Double)thisStack.peek()).doubleValue();
			val.val(thisNumber);
		}
		return true;
	}

	private boolean execute_STACK_TYPE() {
		int stackIndex = getStackIndexArg();					// Get the Stack pointer
		if (stackIndex < 0)				return false;
		if (!isNext(','))				return false;			// move to the value
		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		val.val(theStacksType.get(stackIndex).typeNS());
		return true;
	}

	private boolean execute_STACK_ISEMPTY() {
		int stackIndex = getStackIndexArg();					// Get the Stack pointer
		if (stackIndex < 0)				return false;
		if (!isNext(','))				return false;
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		Stack thisStack = theStacks.get(stackIndex);			// Get the Stack
		val.val(thisStack.isEmpty() ? 1 : 0);

		return true;
	}

	private boolean execute_STACK_CLEAR() {
		int stackIndex = getStackIndexArg();					// Get the Stack pointer
		if (stackIndex < 0)				return false;
		if (!checkEOL())				return false;

		Stack thisStack = theStacks.get(stackIndex);			// Get the Stack
		while (!thisStack.isEmpty()) { thisStack.pop(); }

		return true;
	}

	// ************************************ Clipboard Commands ************************************

	/*
	// This code does not work on devices with API level < 11

	private boolean executeCLIPBOARD_GET() {
		if (!getSVar()) return false;						// get the var to put the clip into
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		String data = "";
		if (clipboard.hasPrimaryClip()) {					// If clip board has text
			CharSequence data1 = clipboard.getText();		// Get the clip
			data = data1.toString();
			if (data == null) data = "";
		} else data ="";									// If no clip, set data to null
		StringVarValues.set(theValueIndex, data);			// Return the result to user
			if (!checkEOL()) return false;
		return true;
	}

	private boolean executeCLIPBOARD_PUT() {
		int v = Build.VERSION.SDK_INT);
		if (!getStringArg()) return false;					// Get the string to put into the clipboard
		ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		CharSequence cs = StringConstant;
		ClipData clip = ClipData.newPlainText("simple text",cs);
		clipboard.setPrimaryClip(clip);
		if (!checkEOL()) return false;
		return true;
	}
	*/

	private boolean executeCLIPBOARD_GET() {
		if (!getSVar())					return false;		// get the var to put the clip into
		Var.Val val = mVal;
		if (!checkEOL())				return false;
		String data;
		if (clipboard.hasText()) {							// If clip board has text
			data = clipboard.getText().toString();			// Get the clip
		} else { data =""; }								// If no clip, set data to null
		val.val(data);										// Return the result to user
		return true;
	}

	private boolean executeCLIPBOARD_PUT() {
		if (!getStringArg()) return false;					// Get the string to put into the clipboard
		if (!checkEOL()) return false;
		clipboard.setText(StringConstant);					// Put the user expression into the clipboard
		return true;
	}

	// *********************************** Encryption Commands ************************************

	private boolean executeENCRYPT(boolean mode) {
		clearErrorMsg();
		String pw = "";										// default password
		if (!isNext(',')) {
			if (!getStringArg())		return false;		// get the (optional) password arg
			pw = StringConstant;
			if (!isNext(','))			return false;
		}
		if (!getStringArg())			return false;		// get the (required) source string
		String src = StringConstant;
		if (!isNext(','))				return false;

		if (!getSVar())					return false;		// get the destination string variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		String dest = null;
		try {
			dest = (mode == ENCODE) ? encrypt(src, pw, STR_TEXT) : decrypt(src, pw, true);
			if (dest == null)			return false;
		} catch (Exception e) {
			dest = "";
			encryptionException(mode, e);
		}
		val.val(dest);										// Put the encrypted string into the user variable
		return true;
	}

	// ************************************* Socket Commands **************************************

	private boolean executeSOCKET() {								// Get Socket command keyword if it is there
		return executeSubcommand(Socket_cmd, "Socket");
	}

	private boolean executeSocketServer() {							// Get Socket Server command keyword if it is there
		return executeSubcommand(SocketServer_cmd, "Socket.Server");
	}

	private boolean executeSocketClient() {							// Get Socket Client command keyword if it is there
		return executeSubcommand(SocketClient_cmd, "Socket.Client");
	}

	private boolean isServerSocketConnected() {
		return isServerSocketConnected("No current connection");
	}

	private boolean isServerSocketConnected(String msgNullSocket) {
		if (theServerSocket == null) {
			return RunTimeError(msgNullSocket);
		}
		if (!theServerSocket.isConnected()) {
			return RunTimeError("Server Connection Disrupted");
		}
		return true;
	}

	private boolean isClientSocketConnected() {
		if (theClientSocket == null) {
			return RunTimeError("Client Socket Not Opened");
		}
		if (!theClientSocket.isConnected()) {
			return RunTimeError("Client Connection Disrupted");
		}
		return true;
	}

	private boolean executeSERVER_CREATE() {
		if (!evalNumericExpression())	return false;				// Get the List pointer
		if (!checkEOL())				return false;

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
			return RunTimeError("Server not created");
		}
		boolean block = true;			// Default if no "wait" parameter is to block. This preserves
										// behavior from before v01.73, when the parameter was added.
		if (evalNumericExpression()) {									// Optional "wait" parameter
			block = (EvalNumericExpressionValue != 0.0);
		}
		if (!checkEOL())				return false;
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

	private boolean executeCLIENT_CONNECT() {
		if (!getStringArg())			return false;					// get the server address
		String SocketClientsServerAdr = StringConstant;

		if (!isNext(','))				return false;					// move to the port number
		if (!evalNumericExpression())	return false;					// get the port number
		int SocketClientsServerPort = EvalNumericExpressionValue.intValue();

		boolean block = true;											// Default if no "wait" parameter is to block.
		if (isNext(',')) {
			if (evalNumericExpression()) {								// Optional "wait" parameter
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

	private boolean executeSERVER_STATUS() {

		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		double status = (newSS == null)                         ? STATE_NOT_ENABLED
					  : (serverSocketState == STATE_LISTENING)  ? STATE_LISTENING
					  : (theServerSocket == null)               ? STATE_NONE
					  : theServerSocket.isConnected()           ? STATE_CONNECTED
					  :                                           STATE_NONE;
		val.val(status);
		return true;
	}

	private boolean executeCLIENT_STATUS() {

		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		double status = (clientSocketState == STATE_CONNECTING) ? STATE_CONNECTING
					  : (theClientSocket == null)               ? STATE_NONE
					  : theClientSocket.isConnected()           ? STATE_CONNECTED
					  :                                           STATE_NONE;
		val.val(status);
		return true;
	}

	private boolean executeSERVER_CLIENT_IP() {
		if (!isServerSocketConnected("Server not connected to a client")) return false;
		return socketIP(theServerSocket);
	}

	private boolean executeCLIENT_SERVER_IP() {
		if (!isClientSocketConnected()) return false;
		return socketIP(theClientSocket);
	}

	private boolean socketIP(Socket socket) {

		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		InetAddress ia = socket.getInetAddress();
		val.val(ia.toString());
		return true;
	}

	private boolean executeSERVER_READ_READY() {
		if (!isServerSocketConnected("No current client accepted")) return false;
		return socketReadReady(ServerBufferedReader);
	}

	private boolean executeCLIENT_READ_READY() {
		if (!isClientSocketConnected()) return false;
		return socketReadReady(ClientBufferedReader);
	}

	private boolean socketReadReady(BufferedReader reader) {

		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		double ready = 0 ;
		try {
			if (reader.ready()) { ready = 1; }
		} catch (IOException e) {
			return RunTimeError(e);
		}
		val.val(ready);
		return true;
	}

	private boolean executeSERVER_READ_LINE() {
		if (!isServerSocketConnected()) return false;
		return socketReadLine(ServerBufferedReader);
	}

	private boolean executeCLIENT_READ_LINE() {
		if (!isClientSocketConnected()) return false;
		return socketReadLine(ClientBufferedReader);
	}

	private boolean socketReadLine(BufferedReader reader) {

		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		String line = null;
		try {
			line = reader.readLine();
		} catch (Exception e) {
			return RunTimeError(e);
		}

		if (line == null) {
			line = "NULL";
		}

		val.val(line);
		return true;
	}

	private boolean executeSERVER_WRITE_LINE() {
		if (!isServerSocketConnected()) return false;
		return socketWrite(theServerSocket, ServerPrintWriter, false);
	}

	private boolean executeCLIENT_WRITE_LINE() {
		if (!isClientSocketConnected()) return false;
		return socketWrite(theClientSocket, ClientPrintWriter, false);
	}

	private boolean executeSERVER_WRITE_BYTES() {
		if (!isServerSocketConnected()) return false;
		return socketWrite(theServerSocket, ServerPrintWriter, true);
	}

	private boolean executeCLIENT_WRITE_BYTES() {
		if (!isClientSocketConnected()) return false;
		return socketWrite(theClientSocket, ClientPrintWriter, true);
	}

	private boolean socketWrite(Socket socket, PrintWriter writer, boolean byteMode) {

		if (!getStringArg()) return false;
		if (!checkEOL()) return false;

		String err = null;
		if (byteMode) {
			OutputStream os = null;
			try {
				os = socket.getOutputStream();
				for (int k=0; k<StringConstant.length(); ++k) {
					byte bb = (byte)StringConstant.charAt(k);
					os.write(bb);
				}
			} catch (Exception e) {
				err = "Error: " + e;
			} finally {
				IOException ex = FileInfo.closeStream(os, FileInfo.flushStream(os, null));
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
			return RunTimeError(err);
		}
		return true;
	}

	private boolean executeSERVER_PUTFILE() {
		if (!isServerSocketConnected()) return false;
		return socketPutFile(theServerSocket);
	}

	private boolean executeCLIENT_PUTFILE() {
		if (!isClientSocketConnected()) return false;
		return socketPutFile(theClientSocket);
	}

	private boolean executeSERVER_GETFILE() {
		if (!isServerSocketConnected()) return false;
		return socketGetFile(theServerSocket);
	}

	private boolean executeCLIENT_GETFILE() {
		if (!isClientSocketConnected()) return false;
		return socketGetFile(theClientSocket);
	}

	private boolean socketPutFile(Socket socket) {

		if (!evalNumericExpression())	return false;						// Parm is the file number variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		int fileNumber = (int)val.nval();
		if (!checkReadFile(fileNumber))	return false;						// Check runtime errors

		FileInfo fInfo = FileTable.get(fileNumber);							// Get the file info
		if (!checkReadAttributes(fInfo, FileType.FILE_BYTE)) return false;	// Check runtime errors

		if (fInfo.isEOF()) { return RunTimeError("Attempt to read beyond the EOF at:"); }

		BufferedInputStream bis = ((ByteReaderInfo)fInfo).mByteReader;
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
		} finally {
			fInfo.eof(true);												// update fInfo
			fInfo.close(null);	// file is already closed, this cleans up the fInfo
		}
		return true;														// Success
	}

	private boolean socketGetFile(Socket socket) {

		if (!evalNumericExpression())	return false;						// Parm is the file number variable
		if (!checkEOL())				return false;

		int fileNumber = EvalNumericExpressionValue.intValue();
		if (!checkFile(fileNumber))		return false;						// Check runtime errors

		FileInfo fInfo = FileTable.get(fileNumber);							// Get the file info
		if (!checkWriteAttributes(fInfo, FileType.FILE_BYTE)) return false;	// Check runtime errors

		DataOutputStream dos = ((ByteWriterInfo)fInfo).getDOS();
		if (dos == null) { return RunTimeError("Error writing file"); }
		int bufferSize = 1024*512;
		try {
			InputStream is = socket.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			streamCopy(bis, dos, bufferSize, 0L);							// Copy from socket to file and close streams
		} catch (Exception e) {
			return RunTimeError(e);
		} finally {
			fInfo.eof(true);												// update fInfo
			fInfo.close(null);	// file is already closed, this cleans up the fInfo
		}
		return true;														// Success
	}

	private boolean streamCopy(BufferedInputStream bis, DataOutputStream dos, int bufferSize,
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
						timeout = (te - ts > 16000);				// terminate transmission
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
			return false;		// doesn't return, but overwrites return value
		} finally {
			ex = FileInfo.closeStream(dos, ex);						// close the streams
			ex = FileInfo.closeStream(bis, ex);
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
		if (!checkEOL())				return false;
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
		if (!checkEOL())				return false;
		if (theClientSocket == null)	return true;

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

	private boolean executeMYIP() {
		Var var = getVarAndType(TYPE_STRING);					// string scalar or array to hold IP address(es)
		Var.Val val = null;										// val in case var is scalar
		Var.Val nVal = null;									// optional address count in case var is array
		if (var == null)				return false;
		if (var.isArray()) {
			if (!validArrayVarForWrite(var, TYPE_STRING)) return false;
			if (isNext(',')) {
				if (!getNVar())			return false;
				nVal = mVal;
			}
		} else {
			mVal = getVarValue(var);							// create a new variable if necessary
			val = mVal;											// can't be null because var is not array
		}
		if (!checkEOL())				return false;

		ArrayList<String> IP = new ArrayList<String>();
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!(inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress())) {
						IP.add(inetAddress.getHostAddress().toString());
					}
				}
			}
		} catch (Exception e) {
			return RunTimeError(e);
		}
		int nIP = IP.size();
		if (nIP == 0) { IP.add(""); }							// prevent zero-length array
		if (var.isArray()) {									// return arg is array
			if (!ListToBasicStringArray(var, IP, IP.size())) return false;
			if (nVal != null) { nVal.val(nIP); }				// actual IP count, not array size
		} else {												// return arg is scalar
			val.val(IP.get(0));									// return first IP address (may be "")
		}
		return true;
	}

	//****************************************** TTS *******************************************

	private boolean executeTTS() {								// Get TTS command keyword if it is there
		return executeSubcommand(tts_cmd, "TTS");
	}

	private boolean executeTTS_INIT() {
		if (!checkEOL())				return false;
		if (theTTS != null)				return true;			// done if already opened

		theTTS = new TextToSpeechActivity(Run.this);			// blocks until initialized
		if (theTTS == null)				return false;

		switch (theTTS.mStatus) {
		case TextToSpeech.SUCCESS:            break;
		case TextToSpeech.LANG_MISSING_DATA:  return RunTimeError("Language Data Missing");
		case TextToSpeech.LANG_NOT_SUPPORTED: return RunTimeError("Language Not Supported");
		default:                              return RunTimeError("TTS Init Failed. Code = " + theTTS.mStatus);
		}

		return true;
	}

	private boolean executeTTS_SPEAK() {
		if (theTTS == null) {
			return RunTimeError("Text to speech not initialized");
		}
		if (!evalStringExpression())	return false;
		String speech = StringConstant;
		boolean block = true;					// Default if no "wait" parameter is to block. This preserves
		if (isNext(',')) {						// behavior from before v01.76, when the parameter was added.
			if (!evalNumericExpression()) return false;			// optional "wait" flag
			block = (EvalNumericExpressionValue != 0.0);		// block if non-zero
		}
		if (!checkEOL())				return false;

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));

		theTTS.speak(speech, params, block);
		return true;
	}

	private boolean executeTTS_SPEAK_TOFILE() {
		if (theTTS == null) {
			return RunTimeError("Text to speech not initialized");
		}
		if (!evalStringExpression())	return false;
		String speech = StringConstant;
		String theFileName;
		if (isNext(',')) {										// optional file name parameter
			if (!getStringArg())		return false;
			theFileName = StringConstant;
		} else { theFileName = "tts.wav"; }						// default file name
		if (!checkEOL())				return false;

		HashMap<String, String> params = new HashMap<String, String>();
		theFileName = Basic.getDataPath(theFileName);
		theTTS.speakToFile(speech, params, theFileName);		// always blocks
		return true;
	}

	private boolean executeTTS_STOP() {
		return checkEOL() && ttsWaitForDone() && ttsStop();
	}

	private boolean ttsWaitForDone() {							// wait for any outstanding speaking to finish
		if (theTTS != null) {									// because cleanup() can kill theTTS while we're not looking
			theTTS.waitForDone();
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

	private boolean executeFTP() {									// Get FTP command keyword if it is there
		return executeSubcommand(ftp_cmd, "FTP");					// and execute the command
	}

	private boolean executeFTP_OPEN() {
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

	public boolean ftpConnect(String host, String username, String password, int port) {
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

	public String ftpGetCurrentWorkingDirectory() {
		try {
			String workingDir = mFTPClient.printWorkingDirectory();
			return workingDir;
		} catch(Exception e) {
			RunTimeError(e);
		}
		return null;
	}

	private boolean executeFTP_CLOSE() {
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

	private boolean executeFTP_DIR() {
		if (FTPdir == null) { return RunTimeError("FTP not opened"); }

		if (!getNVar())					return false;				// get the list variable
		Var.Val val = mVal;

		String dirMark = "(d)";
		if (isNext(',')) {											// optional directory marker
			if (!getStringArg())		return false;
			dirMark = StringConstant;
		}
		if (!checkEOL())				return false;

		ArrayList<String> theStringList = new ArrayList <String>();	// create a new user list
		int theIndex = theLists.size();
		theLists.add(theStringList);

		theListsType.add(Var.Type.STR);								// add the type
		val.val(theIndex);											// return the list pointer

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

	private boolean executeFTP_CD() {
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg())		return false;				// new directory name
			if (!checkEOL())			return false;

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

	private boolean executeFTP_GET() {
			if (FTPdir == null) { return RunTimeError("FTP not opened"); }

			if (!getStringArg())		return false;					// Source file name
			String srcFile = StringConstant;

			if (!isNext(',')) return false;
			if (!getStringArg()) return false;							// Destination file name
			String destFile = StringConstant;
			if (!checkEOL()) return false;

			destFile = Basic.getDataPath(destFile);

			return ftpDownload(srcFile, destFile);
	}

	public boolean ftpDownload(String srcFilePath, String desFilePath) {
			FileOutputStream desFileStream = null;
			boolean status = false;
			try {
				desFileStream = new FileOutputStream(desFilePath);
				status = mFTPClient.retrieveFile(srcFilePath, desFileStream);
				desFileStream.close();
			} catch (Exception e) {
				FileInfo.closeStream(desFileStream, null);
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("Download error"); }
			return status;
	}

	private boolean executeFTP_PUT() {
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

	public boolean ftpUpload(String srcFilePath, String desFilePath) {
			FileInputStream srcFileStream = null;
			boolean status = false;
			try {
				srcFileStream = new FileInputStream(srcFilePath);
				status = mFTPClient.storeFile(desFilePath, srcFileStream);
				srcFileStream.close();
			} catch (Exception e) {
				FileInfo.closeStream(srcFileStream, null);
				return RunTimeError(e);
			}
			if (!status) { RunTimeError("Upload problem"); }
			return status;
	}

	public boolean executeFTP_CMD() {
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
			PrintShow(response);
			return true;
	}

	private boolean executeFTP_DELETE() {
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

	private boolean executeFTP_RMDIR() {
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

	private boolean executeFTP_MKDIR() {
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

	private boolean executeFTP_RENAME() {
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
		Command c = findSubcommand(bt_cmd, "BT");
		if (c == null) return false;

		if ((mChatService == null) && (c.id != CID_OPEN) && (c.id != CID_STATUS)) {
			return RunTimeError("Bluetooth not opened");
		}
		return c.run();
	}

	private synchronized boolean execute_BT_status() {
		if (isEOL())					return true;		// user asked for no data

		// status variable may be either numeric or string; the name and address are strings
		byte[] types = { 3, 2, 2 };							// type of each variable
		int nArgs = types.length;
		Var.Val[] args = new Var.Val[nArgs];				// Val object for each variable
		if (!getOptVars(types, args))	return false;

		int arg = 0;
		Var.Val val = args[arg];
		if (val != null) {
			int state = (mBluetoothAdapter == null) ? STATE_NOT_ENABLED :
						(mChatService == null)      ? STATE_NONE        : bt_state;
			if (types[arg] == 1) {							// status variable is numeric
				val.val(state);
			} else {
				String st = "";								// string representation of state
				switch (state) {
					case STATE_NOT_ENABLED:	st = "Not enabled";	break;
					case STATE_NONE:		st = "Idle";		break;
					case STATE_LISTENING:	st = "Listening";	break;
					case STATE_CONNECTING:	st = "Connecting";	break;
					case STATE_CONNECTED:	st = "Connected";	break;
					case STATE_READING:		st = "Reading";		break;
					case STATE_WRITING:		st = "Writing";		break;
					default:									break;
				}
				val.val(st);
			}
		}
		val = args[++arg];
		if (val != null) {
			String name = (mBluetoothAdapter == null) ? "" : mBluetoothAdapter.getName();
			val.val(name);
		}
		val = args[++arg];
		if (val != null) {
			String address = (mBluetoothAdapter == null) ? "" : mBluetoothAdapter.getAddress();
			val.val(address);
		}
		return (++arg == nArgs);							// sanity-check arg count
	} // execute_BT_status

	private boolean execute_BT_open() {

		if (mBluetoothAdapter == null) {
			return RunTimeError("Bluetooth is not available");
		}

		bt_Secure = true;												// this flag will be used when starting
		if (evalNumericExpression()) {									// the accept thread in BlueTootChatService
			if (EvalNumericExpressionValue == 0) { bt_Secure = false; }
		}
		if (!checkEOL())				return false;

		bt_enabled = mBluetoothAdapter.isEnabled() ? 1 : 0;				// Is BT enabled?
		if (bt_enabled == 0) {
			bt_state = STATE_NOT_ENABLED;								// Enable BT
			if (GRopen) {
				GR.doEnableBT = true;
				GR.drawView.postInvalidate();							// Start GR drawing.
			} else {
				Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
			}
			while (bt_enabled == 0)										// Wait until enabled
				Thread.yield();

			if (bt_enabled == -1) {										// Enable failed
				return RunTimeError("Bluetooth Not Enabled");
			}
		}

		synchronized (BT_Read_Buffer) {
			bt_state = STATE_NONE;
			btConnectDevice = null;
			mOutStringBuffer = new StringBuffer("");
			BT_Read_Buffer.clear();

			mChatService = new BluetoothChatService(Run.this, mHandler);	// Starts the accept thread
			mChatService.start(bt_Secure);
		}
		return true;
	}

	private boolean execute_BT_close() {
		if (mChatService != null) mChatService.stop();
		return checkEOL();
	}

	public void connectDevice(Intent data, boolean secure) {

		String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
		btConnectDevice = null;
		try {
			btConnectDevice = mBluetoothAdapter.getRemoteDevice(address);
			if ( btConnectDevice != null) mChatService.connect(btConnectDevice, secure);
		}
		catch (Exception e) {
			RunTimeError("Connect error: " + e);
		}
	}

	private boolean execute_BT_connect() {
		bt_Secure = true;
		if (evalNumericExpression()) {
			if (EvalNumericExpressionValue == 0) { bt_Secure = false; }
		}
		if (!checkEOL())				return false;

		if (GRopen) {
			GR.startConnectBT = true;
			GR.drawView.postInvalidate();					// Start GR drawing.
		} else {
			Intent serverIntent = null;
			try { serverIntent = new Intent(Run.this, DeviceListActivity.class); }
			catch (Exception ex) { return RunTimeError("Error selecting device"); }

			int requestCode = (bt_Secure) ? REQUEST_CONNECT_DEVICE_SECURE : REQUEST_CONNECT_DEVICE_INSECURE;
			startActivityForResult(serverIntent, requestCode);
		}
		return true;
	}

	private boolean execute_BT_disconnect() {
		if (!checkEOL())				return false;
		mChatService.disconnect();
		return true;
	}

	private boolean execute_BT_reconnect() {
		if (!checkEOL())				return false;
		if (btConnectDevice == null) {
			return RunTimeError("Not previously connected");
		}
		mChatService.connect(btConnectDevice, bt_Secure);
		return true;
	}

	private boolean execute_BT_listen() {
		if (!checkEOL())				return false;
//		mChatService.start();
		return true;
	}

	private boolean execute_BT_device_name() {
		if (bt_state != STATE_CONNECTED) {
			return RunTimeError("Bluetooth not connected");
		}

		if (!getSVar() || !checkEOL())	return false;
		mVal.val(mConnectedDeviceName);
		return true;
	}

	private boolean execute_BT_write() {
		if (bt_state != STATE_CONNECTED) {
			RunTimeError("Bluetooth not connected");
			return true;								// Deliberately not making error fatal
		}

		if (!buildPrintLine("", "\n"))	return false;	// build up the text line in StringConstant

		// Check that there's actually something to send
		if (StringConstant.length() > 0) {
			// Get the message bytes and tell the BluetoothChatService to write
			byte[] send = new byte[StringConstant.length()];
			for (int k=0; k<StringConstant.length(); ++k) {
				send[k] = (byte)StringConstant.charAt(k);
			}

			mChatService.write(send);
		}
		return true;
	}

	private boolean execute_BT_read_ready() {
		if (!getNVar() || !checkEOL())	return false;
		int count = 0;
		if (bt_state == STATE_CONNECTED) {
			synchronized (BT_Read_Buffer) {
				count = BT_Read_Buffer.size();
			}
		}
		mVal.val(count);
		return true;
	}

	private boolean execute_BT_readReady_Resume() {
		return doResume("No Bluetooth Read Ready Interrupt");
	}

	private boolean execute_BT_read_bytes() {

		if (bt_state != STATE_CONNECTED) {
			return RunTimeError("Bluetooth not connected");
		}

		String msg = "";
		if (bt_state == STATE_CONNECTED) {
			synchronized (BT_Read_Buffer) {
				int index = BT_Read_Buffer.size();
				if (index > 0) {
					msg = BT_Read_Buffer.get(0);
					BT_Read_Buffer.remove(0);
				}
			}
		}

		if (!getSVar() || !checkEOL())	return false;
		mVal.val(msg);
		return true;
	}

	private boolean execute_BT_set_uuid() {
		if (!evalStringExpression() || !checkEOL()) return false;
		UUID MY_UUID_SECURE = UUID.fromString(StringConstant);
		UUID MY_UUID_INSECURE = UUID.fromString(StringConstant);
		return true;
	}

	// *********************************** Superuser and System ***********************************

	private boolean executeSU(boolean isSU) {	// SU command (isSU true) or system comand (isSU false)
		Command c = findSubcommand(SU_cmd, (isSU ? "SU" : "System"));
		if (c == null) return false;

		if (SUprocess == null) {
			if (c.id == CID_OPEN) { mIsSU = isSU; }
			else return RunTimeError((isSU ? "Superuser" : "System shell") + " not opened");
		}
		return c.run();									// run the function and report back
	}

	private boolean execute_SU_open() {
		if (!checkEOL())				return false;
		if (SUprocess != null)			return true;
		SU_ReadBuffer = new ArrayList<String>();				// Initialize buffer

		File dir = null;
		if (!mIsSU) {											// System.open: make sure AppPath exists
			dir = new File(Basic.getFilePath());
			if (!dir.exists() && !dir.mkdirs()) {
				return RunTimeError("Cannot create working directory " + dir);
			}
		}
		try {
			SUprocess = (mIsSU)	? Runtime.getRuntime().exec("su")				// Request Superuser
								: Runtime.getRuntime().exec("sh", null, dir);	// Open ordinary shell
			SUoutputStream = new DataOutputStream(SUprocess.getOutputStream());	// Open the output stream
			SUinputStream = new BufferedReader(									// Open the input stream
								new InputStreamReader(SUprocess.getInputStream()));
		} catch (Exception e) {
			return RunTimeError((mIsSU ? "SU" : "System") + " Exception: " + e);
		}
		theSUReader = new SUReader(SUinputStream, SU_ReadBuffer);
		theSUReader.start();

		return true;
	}

	private boolean execute_SU_write() {
		if (!evalStringExpression())	return false;
		if (!checkEOL())				return false;

		String command = StringConstant;
		try {
			SUoutputStream.writeBytes(command + "\n");	// write the command followed by new line character
			SUoutputStream.flush();
		}
		catch (Exception e) {
			return RunTimeError((mIsSU ? "SU" : "System") + " Exception:", e);
		}
		return true;
	}

	private boolean execute_SU_read_ready() {
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		int bfrSize = 0;
		for (int pass = 0; pass < 3; ++pass) {					// try more than once so tight loops in user programs work better
			synchronized (SU_ReadBuffer) {
				bfrSize = SU_ReadBuffer.size();
			}
			if (bfrSize != 0) break;							// data available
			Thread.yield();										// give the SUreader another chance to read
		}
		val.val(bfrSize);										// return buffer size
		return true;
	}

	private boolean execute_SU_read_line() {
		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		boolean available;
		String msg = "";
		synchronized (SU_ReadBuffer) {
			available = (SU_ReadBuffer.size() > 0);
			if (available) {
				msg = SU_ReadBuffer.remove(0);					// read and remove the first item in the buffer
			}
		}
		if (available) { Thread.yield(); }						// give the SUreader a chance to read again

		val.val(msg);
		return true;
	}

	private boolean execute_SU_close() {
		theSUReader.stop();
		SUprocess.destroy();

		SUoutputStream = null;
		SUinputStream = null;
		SU_ReadBuffer = null;
		theSUReader = null;
		SUprocess = null;

		return true;
	}

	// *************************************** FONT Commands **************************************

	private boolean executeFONT() {							// Get Console command keyword if it is there
		return executeSubcommand(font_cmd, "Font");
	}

	private boolean executeFONT_LOAD() {
		clearErrorMsg();
		if (!getNVar())					return false;				// font pointer variable
		Var.Val val = mVal;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;				// get the file path
		String fileName = StringConstant;							// the filename as given by the user
		if (!checkEOL())				return false;

		int fontIdx;
		Typeface aFont = getTypeface(fileName);
		if (aFont == null) {
			writeErrorMsg("Font file not found: " + fileName);
			fontIdx = -1;
		} else {
			fontIdx = FontList.size();
			FontList.add(aFont);
		}
		val.val(fontIdx);
		return true;
	}

	private int getFontArg() {										// get the font number
		return getFontArg("Invalid Font Pointer");					// with the default error message
	}

	private int getFontArg(String errMsg) {							// get and validate the font number
		if (!evalNumericExpression()) { return -2; }				// or return -2 if argument is not numeric
		int fontPtr = EvalNumericExpressionValue.intValue();
		if (fontPtr < 1 | fontPtr >= FontList.size()) {
			RunTimeError(errMsg);
			fontPtr = -1;
		}
		return fontPtr;
	}

	private boolean executeFONT_DELETE() {
		Typeface font = null;
		int fp;														// index into FontList
		if (isEOL()) {												// no font number arg
			for (fp = FontList.size() - 1; (fp > 0) && (font == null); --fp) {
				font = FontList.get(fp);							// find last font loaded and not deleted
			}
			if (fp < 1)					return true;				// nothing to do
		} else {
			fp = getFontArg();										// get the font number arg
			if (fp < 0)					return false;
			if (!checkEOL())			return false;
			font = FontList.get(fp);								// get the font
		}
		if (font != null) {
			FontList.set(fp, null);
		}
		return true;
	}

	private boolean executeFONT_CLEAR() {							// command to clear the font list
		if (!checkEOL())				return false;
		clearFontList();
		return true;
	}

	private void clearFontList() {
		FontList.clear();											// clear the font list
		FontList.add(null);											// add a dummy element 0
	}

	// ************************************* CONSOLE Commands *************************************

	private boolean executeCONSOLE() {							// Get Console command keyword if it is there
		return executeSubcommand(Console_cmd, "Console");
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

	private boolean executeCONSOLE_LINE_COUNT() {
		if (!getNVar())					return false;			// variable to hold the number of lines
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		checkpointMessage();									// allow any pending Console activity to complete
		while (mMessagePending) { Thread.yield(); }				// wait for checkpointMessage semaphore to clear

		val.val(mConsole.getCount());							// number of lines written to console
		return true;
	}

	private boolean executeCONSOLE_LINE_TEXT() {
		if (!evalNumericExpression()) return false;				// line number to read
		int lineNum = EvalNumericExpressionValue.intValue();
		if (!isNext(',') || !getSVar() || !checkEOL()) return false; // variable for line content
		Var.Val val = mVal;

		if (--lineNum < 0) {									// convert from 1-based user index to 0-based Java index
			return RunTimeError("Line number must be >= 1");
		}
		int max = mConsole.getCount();							// number of lines written to console
		String lineText = (lineNum < max) ? mConsole.getItem(lineNum) : "";
		val.val(lineText);
		return true;
	}

	private boolean executeCONSOLE_LINE_TOUCHED() {
		if (!getNVar())					return false;			// variable for last line number touched
		Var.Val lineVar = mVal;
		Var.Val longTouchVar = null;
		if (isNext(',')) {
			if (!getNVar())				return false;			// optional variable indicating short or long touch
			longTouchVar = mVal;
		}
		if (!checkEOL())				return false;

		lineVar.val(TouchedConsoleLine);
		if (longTouchVar != null) {
			longTouchVar.val(ConsoleLongTouch ? 1 : 0);
		}
		return true;
	}

	private boolean executeCONSOLETOUCH_RESUME() {
		return doResume("Console not touched");
	}

	private boolean executeCONSOLE_DUMP() {

		if (!getStringArg() || !checkEOL()) return false;		// Only parameter is the filename
		String theFileName = StringConstant;

		checkpointMessage();									// allow any pending Console activity to complete
		while (mMessagePending) { Thread.yield(); }				// wait for checkpointMessage semaphore to clear

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
			synchronized (mConsoleBuffer) {
				for (String line : mOutput) {
					writer.write(line + "\n");
				}
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			return RunTimeError(e);
		}
		// Log.d(LOGTAG, "executeCONSOLE_DUMP: file " + theFileName + " written");

		return true;
	}

	private boolean executeCONSOLE_FRONT() {
		mConsoleIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
		startActivity(mConsoleIntent);

		return true;
	}

	private boolean executeCONSOLE_LINE_NEW() {
		PrintShow("");
		return true;
	}

	private boolean executeCONSOLE_LINE_CHAR() {
		if (!evalStringExpression())	return false;
		char c = StringConstant.charAt(0);
		sendMessage(MESSAGE_CONSOLE_LINE_CHAR, (int)c, 0);
		return true;
	}

	// ************************************* Ringer Commands **************************************

	private boolean executeRINGER() {							// Get RINGER command keyword if it is there
		return executeSubcommand(ringer_cmd, "Ringer");			// and execute the command
	}

	private boolean executeRINGER_GET_MODE() {

		if (!getNVar())					return false;			// Mode return variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int mode = am.getRingerMode();
		switch (mode) {											// convert from Android to internal values
			case AudioManager.RINGER_MODE_SILENT:  mode = RINGER_SILENT;  break;
			case AudioManager.RINGER_MODE_VIBRATE: mode = RINGER_VIBRATE; break;
			case AudioManager.RINGER_MODE_NORMAL:  mode = RINGER_NORMAL;  break;
			default:                               mode = RINGER_UNKNOWN; break;
		}

		val.val(mode);
		return true;
	}

	private boolean executeRINGER_SET_MODE() {

		if (!evalNumericExpression())	return false;			// Mode value
		int mode = EvalNumericExpressionValue.intValue();
		if (!checkEOL())				return false;

		switch (mode) {											// convert from internal to Android values
			case RINGER_SILENT:  mode = AudioManager.RINGER_MODE_SILENT;  break;
			case RINGER_VIBRATE: mode = AudioManager.RINGER_MODE_VIBRATE; break;
			case RINGER_NORMAL:  mode = AudioManager.RINGER_MODE_NORMAL;  break;
			default:					return true;			// bad value: don't change anything
		}
		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		am.setRingerMode(mode);
		return true;
	}

	private boolean executeRINGER_GET_VOLUME() {

		if (!getNVar())					return false;			// volume return variable
		Var.Val volVar = mVal;
		Var.Val maxVar = null;
		if (isNext(',')) {
			if (!getNVar())				return false;			// optional max volume return variable
			maxVar = mVal;
		}
		if (!checkEOL())				return false;

		AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		int max = am.getStreamMaxVolume(AudioManager.STREAM_RING);
		int vol = am.getStreamVolume(AudioManager.STREAM_RING);

		volVar.val(vol);
		if (maxVar != null) { maxVar.val(max); }
		return true;
	}

	private boolean executeRINGER_SET_VOLUME() {

		if (!evalNumericExpression())	return false;			// volume value
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
		Command c = findSubcommand(sp_cmd, "Soundpool");
		if (c == null) return false;

		if ((theSoundPool == null) && (c.id != CID_OPEN)) {
			return RunTimeError("SoundPool not opened");
		}
		return c.run();
	}

	private boolean execute_SP_open() {

		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;
		int SP_max = EvalNumericExpressionValue.intValue();
		if (SP_max <= 0) {
			return RunTimeError("Stream count must be > 0");
		}
//		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		theSoundPool = new SoundPool(SP_max,  AudioManager.STREAM_MUSIC, 0);

		return true;
	}

	private boolean execute_SP_load() {
		if (!getNVar())					return false;
		Var.Val val = mVal;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;					// Get the file path
		if (!checkEOL())				return false;
		String fileName = StringConstant;								// The filename as given by the user
		String fn = Basic.getDataPath(fileName);

		int SoundID = theSoundPool.load(fn, 1);
		if (SoundID == 0) {												// if file does not exist
			if (Basic.isAPK) {											// and this is a user APK
				int resID = Basic.getRawResourceID(fileName);			// try to load the file from a raw resource
				if (resID != 0) {
					SoundID = theSoundPool.load(getApplicationContext(), resID, 1);
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
			}
		}
		val.val(SoundID);
		return true;
	}

	private boolean execute_SP_play() {
		if (isEOL())					return true;				// no parameters

		Var.Val streamVar = null;
		boolean isComma = isNext(',');
		if (!isComma) {
			if (!getNVar())				return false;				// stream return variable
			streamVar = mVal;
			isComma = isNext(',');
		}

		// soundID, rightVol, leftVol, priority, loop, rate
		boolean[] isInt = { true , false, false, true , true , false };// true = int, false = float
		float[]  fltVal = { 0.0f , 0.5f , 0.5f , 0.0f , 0.0f , 1.0f  };
		int[]    intVal = { 0    , 0    , 0    , 0    , 0    , 0     };
		int nArgs = isInt.length;
		for (int arg = 0; arg < nArgs; ++arg) {
			if (isComma) {
				isComma = isNext(',');
				if (!isComma) {
					if (!evalNumericExpression()) return false;
					if (isInt[arg]) {
						int value = EvalNumericExpressionValue.intValue();
						intVal[arg] = value;
					} else {
						float value = EvalNumericExpressionValue.floatValue();
						fltVal[arg] = value;
					}
					isComma = isNext(',');
				}
			}
		}
		if (isComma || !checkEOL()) return false;

		int soundID = intVal[0];
		float rightVolume = fltVal[1];
		if (rightVolume < 0 || rightVolume >= 1.0) {
			return RunTimeError("Right volume out of range");
		}
		float leftVolume = fltVal[2];
		if (leftVolume < 0 || leftVolume >= 1.0) {
			return RunTimeError("Left volume out of range");
		}
		int priority = intVal[3];
		if (priority < 0 ) {
			return RunTimeError("Priority less than zero");
		}
		int loop = intVal[4];
		float rate = fltVal[5];

		int streamID = theSoundPool.play(soundID, leftVolume, rightVolume, priority, loop, rate);
		if (streamVar!= null) { streamVar.val(streamID); }
		return true;
	}

	private boolean execute_SP_stop() {
		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;
		int streamID = EvalNumericExpressionValue.intValue();
		theSoundPool.stop(streamID);

		return true;
	}

	private boolean execute_SP_unload() {
		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;
		int soundID = EvalNumericExpressionValue.intValue();
		theSoundPool.unload(soundID);

		return true;
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private boolean execute_SP_pause() {
		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;

		int streamID = EvalNumericExpressionValue.intValue();
		if (streamID == 0 ) theSoundPool.autoPause();
		else theSoundPool.pause(streamID);

		return true;
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private boolean execute_SP_resume() {
		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;

		int streamID = EvalNumericExpressionValue.intValue();
		if (streamID == 0 ) theSoundPool.autoResume();
		else theSoundPool.resume(streamID);

		return true;
	}

	private boolean execute_SP_release() {
		if (!checkEOL())				return false;
		theSoundPool.release();
		theSoundPool = null;
		return true;
	}

	private boolean execute_SP_setvolume() {

		if (!evalNumericExpression())	return false;
		int streamID = EvalNumericExpressionValue.intValue();

		if (!isNext(','))				return false;				// Left Volume
		if (!evalNumericExpression())	return false;
		float leftVolume = EvalNumericExpressionValue.floatValue();

		if (!isNext(','))				return false;				// Right Volume
		if (!evalNumericExpression())	return false;
		float rightVolume = EvalNumericExpressionValue.floatValue();
		if (!checkEOL())				return false;

		if (leftVolume < 0 || leftVolume >= 1.0 ) {
			return RunTimeError("Left volume out of range");
		}

		if (rightVolume < 0 || rightVolume >= 1.0) {
			return RunTimeError("Right volume out of range");
		}

		theSoundPool.setVolume(streamID, leftVolume, rightVolume);

		return true;
	}

	private boolean execute_SP_setpriority() {
		if (!evalNumericExpression())	return false;
		int streamID = EvalNumericExpressionValue.intValue();

		if (!isNext(','))				return false;				// Priority
		if (!evalNumericExpression())	return false;
		int priority = EvalNumericExpressionValue.intValue();
		if (!checkEOL())				return false;

		if (priority < 0) {
			return RunTimeError("Priority less than zero");
		}

		theSoundPool.setPriority(streamID, priority);

		return true;
	}

	private boolean execute_SP_setloop() {
		if (!evalNumericExpression())	return false;
		int streamID = EvalNumericExpressionValue.intValue();

		if (!isNext(','))				return false;				// Loop value
		if (!evalNumericExpression())	return false;
		int loop = EvalNumericExpressionValue.intValue();
		if (!checkEOL())				return false;

		theSoundPool.setLoop(streamID, loop);

		return true;
	}

	private boolean execute_SP_setrate() {
		if (!evalNumericExpression())	return false;
		int streamID = EvalNumericExpressionValue.intValue();

		if (!isNext(','))				return false;				// Rate
		if (!evalNumericExpression())	return false;
		float rate = EvalNumericExpressionValue.floatValue();
		if (!checkEOL())				return false;

		theSoundPool.setRate(streamID, rate);
		return true;
	}

	// ***************************************** Headset ******************************************

	private boolean executeHEADSET() {

		if (!getNVar())					return false;
		Var.Val stateVar = mVal;
		if (!isNext(','))				return false;

		if (!getSVar())					return false;
		Var.Val nameVar = mVal;
		if (!isNext(','))				return false;

		if (!getNVar())					return false;
		Var.Val micVar = mVal;
		if (!checkEOL())				return false;

		stateVar.val(headsetState);
		nameVar.val(headsetName);
		micVar.val(headsetMic);

		return true;
	}

	// ******************************************* SMS ********************************************

	private boolean executeSMS() {								// Get SMS command keyword if it is there
		return executeSubcommand(sms_cmd, "SMS");				// and execute the command
	}

	private boolean executeSMS_SEND() {

		if (!getStringArg())			return false;
		String number = StringConstant;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;
		String msg = StringConstant;
		if (!checkEOL())				return false;

		SmsManager sm = android.telephony.SmsManager.getDefault();
		try {
			sm.sendTextMessage(number, null, msg, null, null);
		} catch (Exception e) {
			return RunTimeError(e);
		}

		return true;
	}

	private boolean executeSMS_RCV_INIT() {
		if (!checkEOL())				return false;

		registerReceiver(receiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
		smsRcvBuffer = new ArrayList <String>();
		return true;
	}

	private boolean executeSMS_RCV_NEXT() {
		if (smsRcvBuffer == null) {
			return RunTimeError("SMS.RCV.INIT not executed)");
		}

		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		if (smsRcvBuffer.size() != 0) {
			val.val(smsRcvBuffer.get(0));
			smsRcvBuffer.remove(0);
		} else {
			val.val("@");
		}
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

	// *********************************** Phone Calls and Info ***********************************

	private boolean executePHONE() {							// Get phone command keyword if it is there
		return executeSubcommand(phone_cmd, "Phone");			// and execute the command
	}

	private boolean executePHONE_DIAL(String action) {			// Dial or call a phone number
		if (!getStringArg())			return false;
		if (!checkEOL())				return false;
		String number = "tel:" + StringConstant;

		String encodedHash = Uri.encode("#");
		number = number.replace("#", encodedHash);

		Intent callIntent = new Intent(action);
		callIntent.setData(Uri.parse(number));
		// this will make such that when user returns to your app, your app is displayed, instead of the phone app.
		callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		try { startActivityForResult(callIntent, BASIC_GENERAL_INTENT); }
		catch (Exception e) { return RunTimeError(e); }

		return true;
	}

	private boolean executePHONE_RCV_INIT() {
		if (!checkEOL())				return false;

		if (phoneRcvInited)				return true;
		phoneRcvInited = true;

		mTM = (TelephonyManager)Run.this.getSystemService(Context.TELEPHONY_SERVICE);
		mTM.listen(PSL, PhoneStateListener.LISTEN_CALL_STATE
						+ PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

		return true;
	}

	PhoneStateListener PSL = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state,  String incomingNumber) {
			phoneState = state;
			if (phoneState == TelephonyManager.CALL_STATE_RINGING) {
				phoneNumber = incomingNumber;
			}
		}

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			mSignalStrength = signalStrength;
		}
	};

	private boolean executePHONE_RCV_NEXT() {
		if (!phoneRcvInited) {
			return RunTimeError("phone.rcv.init not executed");
		}

		if (!getNVar())					return false;
		Var.Val stateVar = mVal;
		if (!isNext(','))				return false;

		if (!getSVar())					return false;
		Var.Val numberVar = mVal;
		if (!checkEOL())				return false;

		int callState = mTM.getCallState();
		if (callState == TelephonyManager.CALL_STATE_IDLE) { phoneNumber = ""; }

		stateVar.val(callState);
		numberVar.val(phoneNumber);

		return true;
	}

	private boolean executeMYPHONENUMBER() {

		if (!getSVar())					return false;
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		String pn = getPhoneNumber(null, "Get phone number failed.");
		val.val(pn);

		return true;		// Leave theValueIndex intact for executeDEVICE
	}

	private boolean executePHONE_INFO() {		// This is dynamic info. Some static
												// phone info is available from executeDEVICE().
		int bundleIndex = getBundleArg();						// get the Bundle pointer
		if (bundleIndex < 0)			return false;
		if (!checkEOL())				return false;

		Bundle b = theBundles.get(bundleIndex);

		TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		String phoneType = getPhoneType(tm);
		String networkType = getNetworkType(tm);
		b.putString("PhoneType", phoneType);
		b.putString("NetworkType", networkType);

		CellLocation loc = (tm != null) ? tm.getCellLocation() : null;
		if (loc == null) {
			// no CellLocation available - not on a network?
		} else if (phoneType.equals("GSM")) {
			double cid = ((GsmCellLocation)loc).getCid();
			double lac = ((GsmCellLocation)loc).getLac();
			b.putDouble("CID", cid);
			b.putDouble("LAC", lac);

			String mcc_mnc = tm.getNetworkOperator();
			String operator = tm.getNetworkOperatorName();
			b.putString("MCC/MNC", mcc_mnc);
			b.putString("Operator", operator);
		} else if (phoneType.equals("CDMA")) {
			double baseID = ((CdmaCellLocation)loc).getBaseStationId();
			double networkID = ((CdmaCellLocation)loc).getNetworkId();
			double systemID = ((CdmaCellLocation)loc).getSystemId();
			b.putDouble("BaseID", baseID);
			b.putDouble("NetworkID", networkID);
			b.putDouble("SystemID", systemID);
		}
		if (mSignalStrength != null) {
			// The PhoneStateListener is listening and caught a signal strength change.
			// Try to use reflection to find "@hide" methods available in some API levels.
			try {
				Class<?> c = Class.forName("android.telephony.SignalStrength");
				java.lang.reflect.Method m = c.getMethod("getLevel");
				Integer level = (Integer)m.invoke(mSignalStrength, (Object[])null);
				b.putDouble("SignalLevel", level.doubleValue());
			} catch (NoSuchMethodException nsm) {				// fall back on less flexible methods
				if (phoneType.equals("GSM")) {
					b.putDouble("GsmSignal", (double)mSignalStrength.getGsmSignalStrength());
				} else if (phoneType.equals("CDMA")) {
					b.putDouble("CdmaDbm", (double)mSignalStrength.getCdmaDbm());
				}
			} catch (Exception e) {}
			try {
				Class<?> c = Class.forName("android.telephony.SignalStrength");
				java.lang.reflect.Method m = c.getMethod("getAsuLevel");
				Integer level = (Integer)m.invoke(mSignalStrength, (Object[])null);
				b.putDouble("SignalASU", level.doubleValue());
			} catch (Exception e) {}
		}
		return true;
	}

	private String getPhoneNumber(TelephonyManager tm, String failMsg) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		String pn = (tm != null) ? tm.getLine1Number() : null;
		return (pn != null) ? pn : failMsg;
	}

	private String getDeviceID(TelephonyManager tm, String failMsg) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		String id = (tm != null) ? tm.getDeviceId() : null;
		return (id != null) ? id : failMsg;
	}

	private String getPhoneType(TelephonyManager tm) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		int typecode = (tm != null) ? tm.getPhoneType() : TelephonyManager.PHONE_TYPE_NONE;
		String type;
		switch (typecode) {
			default:
			case TelephonyManager.PHONE_TYPE_NONE:	type = "None";	break;
			case TelephonyManager.PHONE_TYPE_GSM:	type = "GSM";	break;
			case TelephonyManager.PHONE_TYPE_CDMA:	type = "CDMA";	break;
			case TelephonyManager.PHONE_TYPE_SIP:	type = "SIP";	break;	// API level >= 11
		}
		return type;
	}

	private String getNetworkType(TelephonyManager tm) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		int typecode = (tm != null) ? tm.getNetworkType() : TelephonyManager.NETWORK_TYPE_UNKNOWN;
		String type;
		switch (typecode) {
			default:
			case TelephonyManager.NETWORK_TYPE_UNKNOWN:	type = "Unknown";	break;
			case TelephonyManager.NETWORK_TYPE_GPRS:	type = "GPRS";		break;
			case TelephonyManager.NETWORK_TYPE_EDGE:	type = "EDGE";		break;
			case TelephonyManager.NETWORK_TYPE_UMTS:	type = "UMTS";		break;
			case TelephonyManager.NETWORK_TYPE_CDMA:	type = "CDMA";		break;
			case TelephonyManager.NETWORK_TYPE_EVDO_0:	type = "EVDOrev0";	break;
			case TelephonyManager.NETWORK_TYPE_EVDO_A:	type = "EVDOrevA";	break;
			case TelephonyManager.NETWORK_TYPE_1xRTT:	type = "1xRTT";		break;
			case TelephonyManager.NETWORK_TYPE_HSDPA:	type = "HSDPA";		break;
			case TelephonyManager.NETWORK_TYPE_HSUPA:	type = "HSUPA";		break;
			case TelephonyManager.NETWORK_TYPE_HSPA:	type = "HSPA" ;		break;
			case TelephonyManager.NETWORK_TYPE_IDEN:	type = "iDen";		break;	// API level >= 8
			case TelephonyManager.NETWORK_TYPE_EVDO_B:	type = "EVDOrevB";	break;	// API level >= 9
			case TelephonyManager.NETWORK_TYPE_LTE:		type = "LTE";		break;	// API level >= 11
			case TelephonyManager.NETWORK_TYPE_EHRPD:	type = "EHRPD";		break;	// API level >= 11
			case TelephonyManager.NETWORK_TYPE_HSPAP:	type = "HSPAP+";	break;	// API level >= 13
		}
		return type;
	}

	private int getSimState(TelephonyManager tm) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		return (tm != null) ? tm.getSimState() : TelephonyManager.SIM_STATE_UNKNOWN;
	}

	private String getSimSN(TelephonyManager tm, String failMsg) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		String sn = failMsg;
		if (tm != null) {
			int state = getSimState(tm);
			if (state == TelephonyManager.SIM_STATE_ABSENT) { sn = "No SIM"; }
			else if (state == TelephonyManager.SIM_STATE_READY) {
				String realSN = tm.getSimSerialNumber();
				if (realSN != null) { sn = realSN; }
			}
		}
		return sn;
	}

	private String getSimOperator(TelephonyManager tm, String failMsg) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		return (getSimState(tm) == TelephonyManager.SIM_STATE_READY) ? tm.getSimOperator() : failMsg;
	}

	private String getSimOpName(TelephonyManager tm, String failMsg) {
		if (tm == null) { tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); }
		return (getSimState(tm) == TelephonyManager.SIM_STATE_READY) ? tm.getSimOperatorName() : failMsg;
	}

	// ****************************************** EMAIL *******************************************

	private boolean executeEMAIL_SEND() {

		if (!getStringArg())			return false;
		String recipiant = "mailto:" + StringConstant;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;
		String subject = StringConstant;
		if (!isNext(','))				return false;

		if (!getStringArg())			return false;
		String body = StringConstant;
		if (!checkEOL())				return false;

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

	// ***************************************** VOLKEYS ******************************************

	private boolean executeVOLKEYS() {						// Get VOLKEYS command keyword if it is there
		return executeSubcommand(VolKeys_cmd, "VolKeys");	// and execute the command
	}

	private boolean executeVOLKEYS_TOGGLE(boolean on) {		// Enable or disable passing certain keys to system:
															// VOL up/down/mute, MUTE (mic), HEADSET hook
		if (!checkEOL())				return false;
		mBlockVolKeys = !on;					// OFF means block keys, ON means let keys through to the system.
		return true;
	}

	// ******************************************* HTML *******************************************

	private boolean executeHTML() {

		if (htmlOpening) {
			while (Web.aWebView == null) Thread.yield();
			htmlOpening = false;
		}

		Command c = findSubcommand(html_cmd, "HTML");
		if (c == null)					return false;

		if ((htmlIntent == null) || (Web.aWebView == null)) {
			if (c.id == CID_CLOSE)		return true;	// Allow close if already closed
			if ((c.id != CID_OPEN) &&					// Allow open and get.datalink if not opened
					(c.id != CID_DATALINK)) {
				return RunTimeError("html not opened");
			}
		}
		return c.run();
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
		if (!checkEOL())				return false;

		htmlIntent = new Intent(Run.this, Web.class);		// Intent variable used to tell if opened
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

		if (!getSVar())					return false;		// The string return variable
		Var.Val val = mVal;
		if (!checkEOL())				return false;

		String data = "";
		if (htmlData_Buffer != null) {
			synchronized (htmlData_Buffer) {
				if (htmlData_Buffer.size() > 0) {				// If the buffer is not empty
					data = htmlData_Buffer.remove(0);			// get the oldest entry and remove it from the buffer
				}
			}
		}
		val.val(data);										// return the data to the user

		if (Web.aWebView == null)		return true;		// if already closed, return now
															// else check to see if we should close
//		if (data.startsWith("FOR:")) return execute_html_close();	// if Form, close the html
		if (data.startsWith("ERR:")) return execute_html_close();	// if error, close the html

		return true;
	}

	private void addDataLink(String data) {
		if (htmlData_Buffer != null) {
			synchronized (htmlData_Buffer) {
				htmlData_Buffer.add(data);
			}
		}
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
		if (!checkEOL())				return false;
		if (Web.aWebView != null) Web.aWebView.webClose();	// if it is open
		while (Web.aWebView != null) Thread.yield();		// wait for the close signal
		htmlIntent = null;									// indicate not open
		return true;
	}

	private boolean execute_html_post() {
		if (!getStringArg())			return false;
		String url = StringConstant;

		if (!isNext(','))				return false;
		if (!evalNumericExpression())	return false;
		if (!checkEOL())				return false;

		int theListIndex = EvalNumericExpressionValue.intValue();
		if (theListIndex < 1 || theListIndex >= theLists.size()) {
			return RunTimeError("Invalid list pointer");
		}
		if (theListsType.get(theListIndex) != Var.Type.STR) {
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

	// *************************************** Run Command ****************************************

	private boolean executeRUN() {

		if (!getStringArg())			return false;						// get program filename
		String fileName = StringConstant;

		String data = "";
		if (isNext(',')) {													// optional
			if (!getStringArg())		return false;						// parameter to pass to program
			data = StringConstant;
		}
		if (!checkEOL())				return false;

		String path = Basic.getFilePath(Basic.SOURCE_DIR, fileName);
		boolean exists = false;
		if (!Basic.isAPK) { exists = new File(path).exists(); }				// standard BASIC can only RUN a file
		else if (Basic.getRawResourceID(fileName) != 0) { exists = true; }	// APK can run resource
		else {																// or asset
			String assetPath = Basic.getAppFilePath(Basic.SOURCE_DIR, fileName);
			if (getAssetType(assetPath) == "f") { exists = true; }			// it is a valid asset file
		}
		if (!exists) {														// error if the program does not exist
			return RunTimeError(fileName + " not found");
		}

		// This AutoRun will be used later to load the new program
		// and to get an Intent to create a new Interpreter to run it.
		Context context = getApplicationContext();
		mAutoRun = new AutoRun(context, fileName, true, data);				// use file name without path
		Exit = true;														// do not allow interrupt processing
		return true;
	}

	// ********************************** Empty Program Command ***********************************

	private boolean executeEMPTY_PROGRAM() {
		PrintShow("Nothing to execute.");
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
		PendingIntent contentIntent = PendingIntent.getActivity(Run.this, REQUEST_CODE,
													new Intent(Run.this, HandleNotify.class), 0);

		notification.setLatestEventInfo(Run.this, title, subtitle, contentIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL;

		manager.notify(NOTIFICATION_ID, notification);

		if (wait) {
			while (!Notified) Thread.yield();
		}
		return true;
	}

	// *************************************** Swap Command ***************************************

	private boolean executeSWAP() {

		if (!getVar())					return false;
		Var.Val aVar = mVal;
		boolean aIsNumeric = aVar.isNumeric();
		if (!isNext(','))				return false;

		if (!getVar())					return false;
		Var.Val bVar = mVal;
		if (aIsNumeric != bVar.isNumeric()) { return RunTimeError("Type mismatch"); }
		if (!checkEOL())				return false;

		if (aIsNumeric) {
			double aValue = aVar.nval();
			double bValue = bVar.nval();
			aVar.val(bValue);
			bVar.val(aValue);
		} else {
			String aValue = aVar.sval();
			String bValue = bVar.sval();
			aVar.val(bValue);
			bVar.val(aValue);
		}
		return true;
	}

	// ********************************* Speech-to-Text Commands **********************************

	private boolean executeSTT_LISTEN() {
		if (isEOL()) {
			sttPrompt = sttDefaultPrompt;
		} else {
			if (!getStringArg())		return false;
			sttPrompt = StringConstant;
			if (!checkEOL())			return false;
		}

		PackageManager pm = getPackageManager();
		List<ResolveInfo> activities = pm.queryIntentActivities(
				new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		if (activities.size() == 0) {
			return RunTimeError("Recognizer not present");
		}

		sttListening = true;
		sttDone = false;
		if (GRopen) {
			GR.doSTT = true;
		} else {
			Intent intent = Run.buildVoiceRecognitionIntent();
			startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
		}
		return true;
	}

	private boolean executeSTT_RESULTS() {
		if (!sttListening) {
			return RunTimeError("STT_LISTEN not executed.");
		}
		int listIndex = getListArg(Var.Type.STR);		// reuse old list or create new one
		if (listIndex < 0)				return false;
		if (!checkEOL())				return false;

		while (!sttDone) Thread.yield();
		sttListening = false;

		if (sttResults == null) {
			sttResults = new ArrayList <String>();
			sttResults.add("Recognition Cancelled");
		}
		theLists.set(listIndex, sttResults);			// give the list of results to the user
		return true;
	}

	// ************************************** Timer Commands **************************************

	private boolean executeTIMER() {								// Get Timer command keyword if it is there
		return executeSubcommand(Timer_cmd, "Timer");
	}

	private boolean executeTIMER_SET() {
		if (theTimer != null) {
			return RunTimeError("Previous Timer Not Cleared");
		}

		if (!mIntA.containsKey(Interrupt.TIMER_BIT)) {
			return RunTimeError("No OnTimer: Label");
		}

		if (!evalNumericExpression())	return false;
		long interval = EvalNumericExpressionValue.longValue();
		if (interval < 1) { interval = 1; }				// Disallow negative or zero
		if (!checkEOL())				return false;

		TimerTask tt = new TimerTask() {
			public void run() {
				triggerInterrupt(Interrupt.TIMER_BIT);
			}
		};

		theTimer = new Timer();
		theTimer.scheduleAtFixedRate(tt, interval, interval);

		return true;
	}

	private boolean executeTIMER_CLEAR() {
		if (!checkEOL())				return false;
		cancelTimer();
		return true;
	}

	private void cancelTimer() {
		if (theTimer != null) {
			theTimer.cancel();
			theTimer = null;
		}
	}

	private boolean executeTIMER_RESUME() {
		return doResume("No timer interrupt to resume");
	}

	// *************************************** Home Command ***************************************

	private boolean executeHOME() {
		if (!checkEOL())				return false;

		moveTaskToBack(true);
		return true;
	}

	private boolean executeBACKGROUND_RESUME() {
		return doResume("No background state change");
	}

	// ****************************** Android Activity Manager (AM) *******************************

	private boolean executeAPP() {								// Get APPlication command keyword if it is there
		return executeSubcommand(app_cmd, "APP");
	}

	private Intent buildIntentForAPP() {
		// Six optional string expressions and two optional numeric expressions:
		// the action, data, package, component, type, categories, bundle pointer, flags
		byte[] type = { 2, 2, 2, 2, 2, 2, 1, 1 };
		Double[] nVal = { null, null, null, null, null, null, null, null  };
		String[] sVal = { null, null, null, null, null, null, null, null };

		if (!getOptExprs(type, nVal, sVal)) return null;
		String action = sVal[0];
		String data   = sVal[1];
		String pkg    = sVal[2];
		String comp   = sVal[3];
		String mime   = sVal[4];
		String cats   = sVal[5];
		Double bIdx   = nVal[6];
		Double flags  = nVal[7];

		Intent intent = new Intent();							// corresponding adb "am start" parameters
		if (action != null) { intent.setAction(action); }		// am -a
		if (data != null) {										// am -d and -t
			Uri dataUri = Uri.parse(data);
			if (mime == null) { intent.setData(dataUri); }			// data, no MIME type
			else              { intent.setDataAndType(dataUri, mime); } // data and MIME type
		}
		else if (mime != null) { intent.setType(mime); }			// MIME type, no data
		if (comp != null) {											// component name for am -n
			if (pkg != null) {
				intent.setClassName(pkg, comp);						// package name given
			} else {
				intent.setClassName(Run.this, comp);				// no package given
			}
		}
		if (cats != null) {										// am -c (multiple allowed)
			String[] catArray = sVal[5].split("\\s+,\\s+", 0);		// comma-separated list of categories
			for (String cat : catArray) { intent.addCategory(cat); }
		}
		if (bIdx != null) {										// am -e (limited subset)
			int bundleIndex = bIdx.intValue();						// index of a bundle of extras
			if ((bundleIndex <= 0) || (bundleIndex >= theBundles.size())) {
				return null;										// expression is not valid Bundle pointer
			}
			intent.putExtras(theBundles.get(bundleIndex));
		}
		if (flags != null) { intent.addFlags(flags.intValue()); } // am -f
		return intent;
	}

	private boolean executeAPP_BROADCAST() {					// Broadcast an Intent to application(s)
		if (isEOL())					return true;			// nothing to do

		Intent intent = buildIntentForAPP();
		if (intent != null) {
			try { Run.this.sendBroadcast(intent); }
			catch (Exception e) { return RunTimeError(e); }
			return true;
		}
		return false;
	}

	private boolean executeAPP_START() {						// Start an Application's Activity via Intent
		if (isEOL())					return true;			// nothing to do

		Intent intent = buildIntentForAPP();
		if (intent != null) {
			try { Run.this.startActivity(intent); }
			catch (Exception e) { return RunTimeError(e); }
			return true;
		}
		return false;
	}

	// ************************************** Debug Commands **************************************

	private boolean executeDEBUG() {							// Get debug command keyword if it is there
		return executeSubcommand(debug_cmd, "Debug");			// and execute the command
	}

	private boolean executeECHO() {								// Legacy: can use DEBUG.ECHO or just ECHO
		return executeSubcommand(echo_cmd, "Echo");
	}

	private boolean executeDEBUG_TOGGLE(boolean on) {			// parameter is ON or OFF
		if (!checkEOL())				return false;
		Debug = on;
		if (!Debug) { Echo = OFF; }
		return true;
	}

	private boolean executeDEBUG_PRINT() {
		if (Debug) executePRINT();
		return true;
	}

	private boolean executeECHO_TOGGLE(boolean on) {			// parameter is ON or OFF
		if (!checkEOL())				return false;
		Echo = Debug & on;
		return true;
	}

	private boolean executeDEBUG_COMMANDS() {
		if (!Debug)						return true;
		if (isEOL())					return true;		// user asked for no data

		int listIndex = -1;
		ArrayList<String> list = null;
		if (!isNext(',')) {
			listIndex = getListArg(Var.Type.STR);			// get a reusable List pointer - may create new list
			if (listIndex < 0)			return false;		// failed to get or create a list
			isNext(',');									// consume comma, if there is one
		}

		// Three optional numeric variables for counts of math functions, string functions, and command keywords.
		byte[] types = { 1, 1, 1 };							// type of each variable
		int nArgs = types.length;
		Var.Val[] args = new Var.Val[nArgs];				// Val object for each variable
		if (!getOptVars(types, args))	return false;
		Var.Val countVar = args[nArgs - 1];					// keyword count var is last in array

		int kwCount = 0;
		if (listIndex >= 0) {
			list = new ArrayList<String>();					// the list of commands
			theLists.set(listIndex, list);					// put new list in theLists
		}
		if ((listIndex >= 0) || (countVar != null)) {		// if need list or command keyword count
			HashMap<String, String[]> groups = getKeywordLists();	// command groups

			if (list != null) {
				for (String kw : MathFunctions)   { list.add(kw + ')'); }
				for (String kw : StringFunctions) { list.add(kw + ')'); }
			}
			for (String kw : BasicKeyWords) {				// build list and/or count keywords
				if (!kw.endsWith(".")) {
					if (list != null) { list.add(kw); }
					++kwCount;
				} else {
					String[] group = groups.get(kw);
					for (String sub : group) {
						if (list != null) { list.add(kw + sub); }
						++kwCount;
					}
				}
			}
		}
		int[] vals = { MathFunctions.length, StringFunctions.length, kwCount };
		for (int arg = 0; arg < nArgs; ++arg) {
			Var.Val argval = args[arg];
			if (argval != null) { argval.val(vals[arg]); }
		}
		return true;
	} // executeDEBUG_COMMANDS

	private String plusBase(int val) {
		return (val == 0) ? "0" : "1 + " + (val - 1);
	}

	private String varStats(Var.Table symbols) {
		ArrayList<Var> vars = symbols.mVars;
		int scalars = 0;
		int arrays = 0;
		for (Var v : vars) {
			if (v instanceof Var.ScalarVar)	{ ++scalars; }
			else if (v.isArray())			{ ++arrays; }
		}
		return	"\n# variables: " + vars.size() +
				"\n  scalars  : " + scalars +
				"\n  arrays   : " + arrays;
	}

	private boolean executeDEBUG_STATS() {
		if (Debug) {
			ActivityManager actvityManager = (ActivityManager)Run.this.getSystemService(ACTIVITY_SERVICE);
			PrintShow(
				"Mem class  : " + actvityManager.getMemoryClass(),
				"Local scope"   + varStats(mSymbolTable),
				"Global scope"  + varStats(mGlobalSymbolTable),
				"# labels   : " + Labels.size(),
				"  lists    : " + plusBase(theLists.size()),	// item 0 always present but not accessible
				"  stacks   : " + plusBase(theStacks.size()),	// item 0 always present but not accessible
				"  bundles  : " + plusBase(theBundles.size()),	// item 0 always present but not accessible
				"  functions: " + mFunctionTable.size(),
				"    nested : " + FunctionStack.size(),
				"  fonts    : " + plusBase(FontList.size()),	// item 0 always present but not accessible
				"  paints   : " + plusBase(PaintList.size()),	// item 0 present after GR.Open but not accessible
				"  bitmaps  : " + plusBase(BitmapList.size()),	// item 0 present after GR.Open but not accessible
				"DL size    : " + plusBase(DisplayList.size()),	// item 0 present after GR.Open but not accessible
				"RealDL size: " + plusBase(RealDisplayList.size()) // item 0 present after GR.Open but not accessible
			);
			int c;
			c = InChar.size();			if (c != 0) { PrintShow("InKey count: " + c); }
			c = IfElseStack.size();		if (c != 0) { PrintShow("IF stack   : " + c); }
			c = ForNextStack.size();	if (c != 0) { PrintShow("FOR stack  : " + c); }
			c = WhileStack.size();		if (c != 0) { PrintShow("WHILE stack: " + c); }
			c = DoStack.size();			if (c != 0) { PrintShow("DO stack   : " + c); }
			c = GosubStack.size();		if (c != 0) { PrintShow("GOSUB stack: " + c); }
			c = FunctionStack.size();	if (c != 0) { PrintShow("Call stack : " + c); }
		}
		return true;
	}

	private boolean executeDUMP_SCALARS() {
		if (!Debug)						return true;
		if (!checkEOL())				return false;

		ArrayList<String> lines = dbDoScalars("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_ARRAY() {
		if (!Debug)						return true;

		Var var = getVarAndType();
		if ((var == null) || !var.isArray()){ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (var.isNew())					{ return RunTimeError(EXPECT_ARRAY_EXISTS); }
		// No checkEOL: ignore anything after the '['

		WatchedArray = var;
		ArrayList<String> lines = dbDoArray("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_LIST() {
		if (!Debug)						return true;

		int listIndex = getListArg();							// get the list pointer
		if (listIndex < 0)				return false;
		if (!checkEOL())				return false;

		WatchedList = listIndex;
		ArrayList<String> lines = dbDoList("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_STACK() {
		if (!Debug)						return true;

		int stackIndex = getStackIndexArg();					// get the stack pointer
		if (stackIndex < 0)				return false;
		if (!checkEOL())				return false;

		WatchedStack = stackIndex;
		ArrayList<String> lines = dbDoStack("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	private boolean executeDUMP_BUNDLE() {
		if (!Debug)						return true;

		int bundleIndex = getBundleArg();						// get the Bundle pointer
		if (bundleIndex < 0)			return false;
		if (!checkEOL())				return false;

		WatchedBundle = bundleIndex;
		ArrayList<String> lines = dbDoBundle("");
		for (String line : lines) {
			if (line != null) { PrintShow(line); }
		}
		PrintShow("....");
		return true;
	}

	//=====================DEBUGGER DIALOG STUFF========================

	private boolean executeDEBUG_WATCH_CLEAR() {
		if(!Debug)						return true;
		if (!checkEOL())				return false;

		WatchedVars.clear();
		return (WatchedVars.isEmpty());
	}

	private boolean executeDEBUG_WATCH() {				// separate the names and store them
		if (!Debug)						return true;

		String text = ExecutingLineBuffer.text();
		int max = text.length() - 1;
		int ni = LineIndex;								// start of name string
		do {
			int i = text.indexOf(',', ni);
			if (i < 0) { i = max; }
			Var var = getVarAndType();
			boolean add = (var != null);
			for (int j = 0; j < WatchedVars.size(); ++j) {
				if (WatchedVars.get(j) == var) { add = false; }
			}
			if (add) {
				WatchedVars.add(var);
			}
			LineIndex = ni = i + 1;
		} while (ni < max);
		return true;
	}

	private boolean executeDEBUG_SHOW_SCALARS() {
		DialogSelector(1);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_ARRAY() {
		if (!Debug)						return true;

		Var var = getVarAndType();
		if ((var == null) || !var.isArray()){ return RunTimeError(EXPECT_ARRAY_VAR); }
		if (var.isNew())					{ return RunTimeError(EXPECT_ARRAY_EXISTS); }

		WatchedArray = var;
		DialogSelector(2);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_LIST() {
		if (!Debug)						return true;

		int listIndex = getListArg();							// get the list pointer
		if (listIndex < 0)				return false;

		WatchedList = listIndex;
		DialogSelector(3);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_STACK() {
		if (!Debug)						return true;

		int stackIndex = getStackIndexArg();					// get the stack pointer
		if (stackIndex < 0)				return false;

		WatchedStack = stackIndex;
		DialogSelector(4);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_BUNDLE() {
		if (!Debug)						return true;

		int bundleIndex = getBundleArg();						// get the Bundle pointer
		if (bundleIndex < 0)			return false;

		WatchedBundle = bundleIndex;
		DialogSelector(5);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_WATCH() {
		if (!Debug)						return true;
		DialogSelector(6);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_CONSOLE() {
		if (!Debug)						return true;
		DialogSelector(7);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW_PROGRAM() {
		if(!Debug)						return true;
		DialogSelector(8);
		executeDEBUG_SHOW();
		return true;
	}

	private boolean executeDEBUG_SHOW() {				// trigger do debug dialog
		if (!Debug)						return true;
		WaitForDebugResume = true;						// make RunLoop() check debug flags
		DebuggerStep = true;							// make RunLoop() start the debug dialog after this command
		return true;
	}

	private ArrayList<String> dbDoWatch(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Watching:");

		if (!WatchedVars.isEmpty()) {
			int watchcount = WatchedVars.size();
			for (int j = 0; j < watchcount; ++j) {
				Var wv = WatchedVars.get(j);
				String line = dbDoOneScalar(wv, prefix);
				if (line != null) { msg.add(line); }
			}
		} else { msg.add("\n" + "Undefined."); }
		return msg;
	}

	private ArrayList<String> dbDoFunc() {
		ArrayList<String> msg = new ArrayList<String>();
		String msgs = "";
		if (!FunctionStack.isEmpty()) {
			Stack<CallStackFrame> tempStack = (Stack<CallStackFrame>)FunctionStack.clone();
			do {
				msgs = tempStack.pop().name() + msgs;
			} while (!tempStack.isEmpty());
		} else { msgs += "MainProgram"; }
		msg.add("In Function: " + msgs);
		return msg;
	}

	private ArrayList<String> dbDoScalars(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Scalar Dump");
		msg.add("  Local");
		for (Var var : mSymbolTable.mVars) {
			String line = dbDoOneScalar(var, prefix);
			if (line != null) { msg.add(line); }
		}
		msg.add("  Global");
		for (Var var : mGlobalSymbolTable.mVars) {
			String line = dbDoOneScalar(var, prefix);
			if (line != null) { msg.add(line); }
		}
		return msg;
	}

	private String dbDoOneScalar(Var var, String prefix) {
		if (var == null) { return (prefix + "Warning: null variable"); }
		String name = var.name();
		if (name.length() == 0) { return (prefix + "Warning: zero-length variable name"); }

		boolean isScalar = !var.isArray() && !var.isFunction();
		if (isScalar) {
			Var.Val val = var.val();
			boolean isString = var.isString();
			String line = prefix + name;
			line += " = " + (isString ? quote(val.sval()) : val.nval());
			return line;
		}
		return null;
	}

	private ArrayList<String> dbDoArray(String prefix) {
		ArrayList<String> msg = new ArrayList<String>();
		msg.add("Dumping Array " + WatchedArray.name() + "]");

		Var.ArrayDef array = WatchedArray.arrayDef();			// get the array
		if (array == null) {
			msg.add(prefix + "Warning: null array table entry");
		} else {
			int length = array.length();						// get the array length
			// ArrayList<Integer> dims = array.dimList();
			// ArrayList<Integer> sizes = array.arraySizes();
			// msg.add("dims: " + dims.toString());
			// msg.add("sizes: " + sizes.toString());
			boolean isString = WatchedArray.isString();
			for (int i = 0; i < length; ++i) {
				msg.add(prefix +
						(isString ? quote(array.sval(i)) : array.nval(i)));
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

		ArrayList list = theLists.get(WatchedList); // get the list
		if (list == null) {
			msg.add(prefix + "Warning: null list variable");
			return msg;
		}

		int length = list.size();
		if (length == 0) {
			msg.add(prefix + "Empty List");
		} else {
			boolean isString = (theListsType.get(WatchedList) == Var.Type.STR);
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
			boolean isString = (theStacksType.get(WatchedStack) == Var.Type.STR);
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
			Object o = b.get(s);
			boolean isNumeric = o instanceof Double;
			msg.add(prefix + s + ": "
					+ (isNumeric ? (Double) o : quote((String) o)));
		}
		return msg;
	}

} // End of Interpreter

} // End of Run
