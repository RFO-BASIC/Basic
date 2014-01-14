! This program is designed to demonstrate
! the gr.touch and gr.bitmap, commands in
! the context of a graphics application.
!
! Run the program and follow the instructions.
!
! Note: You can not exercise all the features
! of this program if you do not have a physical
! keyboard.

PRINT "Touch the screen. A small star will appear."
PRINT "Pressing UP makes the next star bigger."
PRINT "Pressing DOWN makes the next star smaller"
PRINT "Pressing GO toggles between the star and a galaxy bitmap."
PRINT ""
PRINT "When ready, Tap the screen."
PRINT "When done, Press the BACK key."

! Wait for the screen to be tapped
Tapped = 0
DO
UNTIL Tapped
GOTO Start

onConsoleTouch:
Tapped = 1
ConsoleTouch.Resume

Start:
! open the Graphics Screen with a
! white background

GR.OPEN 255, 0,0,0

! Load the bitmap object and set mode to dots

GR.BITMAP.LOAD BM_ptr, "Galaxy.png"
m = 1

! Set the color of the dot to filled Black

GR.COLOR 255,255,255,255,1

! Set the start radius to 1
r = 1

! Run an infinite loop testing for a touch
! and looking for key presses

DO

 inkey$ key$
 IF key$ = "up" THEN r = r + 1
 IF key$ = "down" THEN r = r - 1
 IF key$ = "go"
  IF m = 1 THEN m = 2	ELSE  m = 1
 ENDIF

 GR.TOUCH flag, x , y

 IF flag
  IF m = 1 THEN GR.CIRCLE n, x, y, r ELSE GR.BITMAP.DRAW n, BM_ptr , x, y
  GR.RENDER
 ENDIF

UNTIL 0

! When you exit the graphic screen
! with the back key, you will get
! run time error at gr.touch. This
! This because the graphics mode
! is turned off when the graphics
! screen is  exited. Hit the Back
! key one more time to get back
! to the Editor.

! The Onerror will catch the error
! and not print the graphics not
! opened error message
ONERROR:
END
