!!
This program demonstrates the
use of the camera interfaces 
by BASIC!

Note: The captured images are
not saved.
!!

! Load an array with the
! Select Options
ARRAY.LOAD method$[], "Quit" ~
"Use Device User Interface" ~
"Automatic with auto flash", "Automatic mode without flash", "Automatic mode with flash"~
"Manual with auto flash", "Manual mode without flash", "Manual mode with flash"

! Set the Popup Message
msg$ ="Select A Capture Mode"

! Open Graphic
GR.OPEN 255, 0, 0, 0 % Black Background

again:

!Reset orientation to Landscape

GR.ORIENTATION 0 % Force Landscape

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

 SW.CASE 2 % Use Device User Interface
  GR.CAMERA.SHOOT bm_ptr
  SW.BREAK

 SW.CASE 3 % Automatic mode auto flash
  GR.CAMERA.AUTOSHOOT bm_ptr, 0
  SW.BREAK

 SW.CASE 4 % Automatic mode without flash
  GR.CAMERA.AUTOSHOOT bm_ptr, 2
  SW.BREAK

 SW.CASE 5 % Automatic mode with flash
  GR.CAMERA.AUTOSHOOT bm_ptr, 1
  SW.BREAK

 SW.CASE 6 % Manual mode with auto flash
  GR.CAMERA.MANUALSHOOT bm_ptr, 0
  SW.BREAK

 SW.CASE 7 % Manual mode without flash
  GR.CAMERA.MANUALSHOOT bm_ptr, 2
  SW.BREAK

 SW.CASE 8 % Manual mode with flash
  GR.CAMERA.MANUALSHOOT bm_ptr, 1
  SW.BREAK

SW.END

! Test to insure that an
! image was captured
IF bm_ptr = 0
 PRINT "Image not captured"
 GOTO again
ENDIF

! If the picture is in
! in Portrait mode, switch
! to Portrait mode

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

!Clear the screen

GR.CLS
GR.RENDER

! Delete the old bitmaps
GR.BITMAP.DELETE bm_ptr
GR.BITMAP.DELETE scaled_bm

! Ready for the next capture
GOTO again
