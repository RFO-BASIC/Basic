!!

******  READ!  *******

See f32_tcp-ip_socket.bas
introduction for details
about setting up a server
and a client.

This program must be run
on two different Android
devices.

In this program, it is
a server that takes a picture
under command from a client. It
is a client that retrieves the
image and displays it.

The viewer saves the received
image in 
/sdcard/rfo-basic/data/rimage.jpg
and displays the image

!!

ARRAY.LOAD type$[], "Remote Camera", "Remote Viewer"
msg$ = "Select Remote Type"
SELECT type, type$[], msg$
IF type = 0
 "Thanks for playing"
 END
ELSEIF type = 2
 GOTO xdoViewer
ENDIF

!***********  Camera Server Server Demo  **************

ServerErrorCount = 0
ServerMaxError = 100
RecoveryState = -1

INPUT "Enter the port number", port, 1080

SOCKET.MYIP ip$
PRINT "LAN IP: " + ip$
GRABURL ip$, "http://automation.whatismyip.com/n09230945.asp"
PRINT "WAN IP: " + ip$

! Create the server on the specified port
SOCKET.SERVER.CREATE port
RecoveryState = 0

newConnection:

! Connect to the next Client
! and print the Client IP
PRINT ""
PRINT "Waiting for viewer client connect"
SOCKET.SERVER.CONNECT
SOCKET.SERVER.CLIENT.IP ip$
PRINT "Connected to ";ip$
RecoverState = 0

! Connected to a Client
! Wait for Client to send a message
! or time out after 10 seconds

maxclock = CLOCK() + 60000
DO
 SOCKET.SERVER.READ.READY flag
 IF CLOCK() > maxclock
  PRINT "Read time out"
  GOTO OnError
 ENDIF
UNTIL flag

! Message received. Read it.

SOCKET.SERVER.READ.LINE msg$
PRINT "Viewer command = ";msg$

! The message is a string with
! single digit

! Open Graphics
GR.OPEN 255, 0, 0, 0 % Black Background

SW.BEGIN msg$

 SW.CASE "0" % 0 = Quit
  GR.CLOSE
  PRINT "Viewer says to shutdown"
  PRINT "Disconnecting"
  SOCKET.SERVER.DISCONNECT
  PRINT "Closing socket"
  SOCKET.SERVER.CLOSE
  PRINT "Done"
  END
  SW.BREAK


 SW.CASE "1" % Automatic mode auto flash
  GR.CAMERA.AUTOSHOOT bm_ptr, 0
  SW.BREAK

 SW.CASE "2" % Automatic mode without flash
  GR.CAMERA.AUTOSHOOT bm_ptr, 2
  SW.BREAK

 SW.CASE "3" % Automatic mode with flash
  GR.CAMERA.AUTOSHOOT bm_ptr, 1
  SW.BREAK

 SW.DEFAULT
  PRINT "Do some more debugging"
  END

SW.END

! We do not need the bitmap,
! so delete it.

GR.BITMAP.DELETE bm_ptr

! Close graphics

GR.CLOSE

! The autoshoot mode places an
! image file, image.png in the
! /sdcard/rfo-basic/data/
! directory
!
! We will send that image to
! the client

BYTE.OPEN R, fr, "image.png"
PRINT "Sending image"
SOCKET.SERVER.WRITE.FILE fr
PRINT "Image sent"

! Finished this capture.
! Disconnect from Client

SOCKET.SERVER.DISCONNECT
PRINT "Disconnected from viewer"

! Loop to get the next Client

GOTO newConnection

! Server Error Recovery Code

ONERROR:

! If not in server mode, quit
IF type <> 1
 PRINT "Client error. Terminating"
 END
ENDIF

IF RecoverState = -1
 PRINT "Device not connected to LAN or WAN"
 END
ENDIF

! Limit the number of server errors
! to avoid endless looping


IF RecoverState = 0
 ServerErrorCount = ServerErrorCount + 1
 PRINT "Server Error. Recovering " + STR$(ServerErrorCount)
 IF ServerErrorCount > ServerMaxError
  PRINT "Exceeded Max Server Error Count. Terminating"
  END
 ENDIF

 ! Try to disconnect and close
 ! Server. If one fails, do
 ! not try it again until
 ! and Server is created

 IF RecoverState = 0
  PRINT "Attempting to disconnect"
  RecoverState = 1
  SOCKET.SERVER.DISCONNECT
  PRINT "Disconnected"
  GOTO newConnection
 ELSEIF
  PRINT "Attempting to close Server"
  RecoverState =2
  SOCKET.SERVER.CLOSE
  PRINT "Closed"
 ENDIF

 ! Try to crate and new server.

 PRINT "Attempting to create new Server"
 SOCKET.SERVER.CREATE port
 RecoveryState = 0
 GOTO newConnection

 ! ****************** Client Demo ****************

 xDOViewer:

  ! Load an array with the
  ! Select Options
  ARRAY.LOAD method$[], "Quit" ~
  "Capture with auto flash", "Capture without flash", "Capture with flash"~
  "Shut down remote camera"

  ! Set the Popup Message
  msg$ ="Connected. Select A Capture Mode"

  INPUT "Enter the connect-to IP", ip$
  INPUT "Enter the port number", port, 1080


  viewerAgain:

  ! Connect to the specified IP on the
  ! specified Port

  SOCKET.CLIENT.CONNECT ip$, port
  PRINT ""
  PRINT "Connected to Camera Server"


  again:

  ! Ask the user what to do
  SELECT mode, method$[], msg$

  ! A choice has been made
  ! Act on that choice
  SW.BEGIN mode

   SW.CASE 0 % Back Key
   SW.CASE 1 % Quit
    PRINT "Done taking pictures"
    END
    SW.BREAK

   SW.CASE 2 % Capture with auto flash
    SOCKET.CLIENT.WRITE.LINE "1"
    SW.BREAK

   SW.CASE 3 % Automatic mode without flash
    SOCKET.CLIENT.WRITE.LINE "2"
    SW.BREAK

   SW.CASE 4 % Automatic mode with flash
    SOCKET.CLIENT.WRITE.LINE "3"
    SW.BREAK

   SW.CASE 5 % Shut down camera
    SOCKET.CLIENT.WRITE.LINE "0"
    PRINT "Done taking pictures"
    SOCKET.CLIENT.CLOSE
    END
    SW.BREAK

  SW.END

  ! and then wait for Server to respond
  ! or time out after 30 seconds

  PRINT "Waiting for image"
  maxclock = CLOCK() + 30000
  DO
   SOCKET.CLIENT.READ.READY flag
   IF CLOCK() > maxclock
    PRINT "Read time out"
    END
   ENDIF
  UNTIL flag

  ! Server has sent the image file.
  ! Read it. Print it.

  BYTE.OPEN W, fw, "rimage.jpg"
  SOCKET.CLIENT.READ.FILE fw
  BYTE.CLOSE fw
  PRINT "Image received"

  ! The image is now in the file
  ! "rimage.jpg"

  ! Close the client

  SOCKET.CLIENT.CLOSE
  PRINT "Disconnected from server"

  ! Open Graphics
  GR.OPEN 255, 0, 0, 0 % Black Background

  !Reset orientation to Landscape
  GR.ORIENTATION 0 % Force Landscape

  ! Load the file into a bitmap

  GR.BITMAP.LOAD bm_ptr, "rimage.jpg"

  ! Scale the bitmap to fit the
  ! screen with the proper
  ! aspect ratio

  GR.BITMAP.SIZE bm_ptr, bw,bh
  IF bh > bw
   GR.ORIENTATION 1
   GR.SCREEN sw, sh
   ar = bw/bh
   GR.BITMAP.SCALE scaled_bm, bm_ptr, sw, sh*ar
  ELSE
   GR.SCREEN sw, sh
   ar = bh/bw
   GR.BITMAP.SCALE scaled_bm, bm_ptr, sw*ar, sh
  ENDIF

  ! Draw the scaled image bitmap
  GR.BITMAP.DRAW obj_ptr, scaled_bm, 0, 0

  ! Add some text

  GR.TEXT.SIZE sw/25
  GR.TEXT.ALIGN 1
  xleft = sw/12
  xtop = sh - 2*(sw/25)
  GR.COLOR 255, 255, 255, 255, 1
  GR.TEXT.DRAW P, xleft,  xtop, "Tap screen to take another picture."

  ! Now render the picture and text
  GR.RENDER

  ! Wait until touch and then untouch

  flag = 0
  DO
   GR.TOUCH flag, xx, yy
  UNTIL flag
  DO
   GR.TOUCH flag, xx, yy
  UNTIL !flag

  ! Delete the old bitmaps
  GR.BITMAP.DELETE bm_ptr
  GR.BITMAP.DELETE scaled_bm

  ! Close graphics
  GR.CLOSE

  ! Ready for the next capture
  GOTO viewerAgain

