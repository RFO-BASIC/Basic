!!
This program demonstrates using
Bluetooth as both an application
that listens for a connection
or makes a connection to a listener.

Before running this program use
the Android "Settings" application
to enable Bluetooth and to pair
with the device(s) that you will
talk to.

This program will send and receive
data bytes to and from a connected
device.

You can use this program to 
(among many other things) set
up a chat between two Android
devices.

Note: The UUID used by default is
the standard serial port UUID. This
can be changed. Read the manual.

!!

! Begin by opening Bluetooth
! If Bluetooth is not enabled
! the program will stop here.

BT.OPEN

! When BT is opened, the program
! will start listening for another
! device to connect. At this time
! the user can continue to wait
! for a connection are can attempt
! to connect to another device
! that is waiting for a connection

! Ask user what to do

ARRAY.LOAD type$[], "Connect to listener", "Continue to listen for connection", "Quit"
title$ = "Select operation mode"

! Create the menu that will be loaded
! when the screen is touched.

ARRAY.LOAD menu$[], "Send", "Disconnect","Quit"

new_connection:
xdomenu =0

SELECT type, type$[], title$

! If the user pressed the back
! key or selected quit then quit
! otherwise try to connect to
! a listener

IF (type = 0) | (type =3)
 PRINT "Thanks for playing"
 BT.CLOSE
 END
ELSEIF type = 1
 BT.CONNECT
ENDIF

! Read status until
! a connection is made

ln = 0
DO
 BT.STATUS s
 IF s = 1
  ln = ln + 1
  PRINT "Listening", ln
 ELSEIF s =2
  PRINT "Connecting"
 ELSEIF s = 3
  PRINT "Connected"
  PRINT "Touch any text line to send, disconnect or quit."
 ENDIF
 PAUSE 1000

UNTIL s = 3

! When a connection is made
! get the name of the connected
! device

BT.DEVICE.NAME device$

! *** Read/Write Loop ****

RW_Loop:

DO

 ! If the screen is touched, the interrupt
 ! code will change xdoMemu to 1 (true)
 ! In that case, show the menu

 IF xdoMenu
  xdoMenu =0
  SELECT menu,  menu$[], "Do what?"
  IF menu = 1 THEN GOSUB xdoSend
  IF menu = 2 THEN BT.DISCONNECT
  IF menu = 3
   PRINT "Thanks for playing"
   BT.CLOSE
   END
  ENDIF

 ENDIF


 ! Read status to insure
 ! that we remain connected.
 ! If disconnected, program
 ! reverts to listen mode.
 ! In that case, ask user
 ! what to do.

 BT.STATUS s
 IF s<> 3
  PRINT "Connection lost"
  GOTO new_connection
 ENDIF

 ! Read messages until
 ! the message queue is
 ! empty

 DO
  BT.READ.READY rr
  IF rr
   BT.READ.BYTES rmsg$
   PRINT device$;": ";rmsg$
  ENDIF
 UNTIL rr = 0

UNTIL 0

! Get and send message
! to the connected device

xdoSend:

INPUT "Text to send", wmsg$
BT.WRITE wmsg$
PRINT "Me: "; wmsg$

RETURN

! When Console is touched
! set xdoMenu to true

onConsoleTouch:
xdoMenu = 1
ConsoleTouch.Resume

onError:
END
