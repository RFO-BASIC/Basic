! This example shows the different effects
! available for drawing graphical text.
!
! Open Graphics Screen with a White background.

GR.OPEN 255, 255, 255, 255
GR.ORIENTATION 0
GR.SCREEN w,h

xleft = w/4

! Draw a Black text alignment line

GR.COLOR 255,0,0,0,1
GR.LINE n, xleft,0, xleft,h

! Set the text color to Red with fill = false

GR.COLOR 255,255, 0, 0, 0

! Set the text size to 1/25th screen height

GR.TEXT.SIZE w/25

! Set the text align to Left = 1

GR.TEXT.ALIGN 1

GR.TEXT.DRAW P, xleft, h/3, "Left Aligned"

! Set the text align to Center = 2

GR.TEXT.ALIGN 2

GR.TEXT.DRAW P, xleft, h/2, "Center Aligned"

! Set the text align to Right = 3

GR.TEXT.ALIGN 3

GR.TEXT.DRAW P, xleft, 2*h/3, "Right Aligned"

! Demonstrate Text Sizes and Styles
! All examples will be aligned Left = 1
! and with Blue (0,0,255) text with fill = false

GR.TEXT.ALIGN 1
GR.COLOR 255, 0, 0, 255, 0

! Eight block of text will be drawn
! evenly spaced

block = h/8
xleft = w/2

GR.TEXT.SIZE block/4
GR.TEXT.DRAW P, xleft,  block/2, "Tiny text"

GR.TEXT.SIZE  block
GR.TEXT.DRAW blink_ptr, xleft, 2*block, "Bigger text"

! normal text size is block/2
hblock = block/2
bc = 3 % Block level

GR.TEXT.SIZE hblock
GR.TEXT.BOLD 1
GR.TEXT.DRAW BTP, xleft , bc*block + hblock, "Bold, unfilled text"
bc = bc +1

! Show Bold, filled text. Change the color
! fill parameter to true

GR.COLOR 255, 0, 0, 255, 1

GR.TEXT.DRAW P, xleft, bc*block + hblock, "Bold, filled text"
bc =bc + 1

! Show Underlinded text

GR.TEXT.BOLD 0
GR.TEXT.UNDERLINE 1
GR.TEXT.DRAW P, xleft, bc*block + hblock, "Underlined text"
bc =bc + 1

! Show strike through text

GR.TEXT.UNDERLINE 0
GR.TEXT.STRIKE 1
GR.TEXT.DRAW P, xleft, bc*block + hblock, "Strike through text"
bc =bc + 1

! Show skewed text

GR.TEXT.STRIKE 0
GR.TEXT.SKEW -0.25
GR.TEXT.DRAW P,xleft, bc*block + hblock, "Skewed text"

! Now render everything

GR.RENDER

! After a one second pause,
! start to blink the big text

PAUSE 1000

DO
 GR.HIDE blink_ptr
 GR.RENDER
 PAUSE 500
 GR.SHOW blink_ptr
 GR.RENDER
 PAUSE 750
UNTIL 0

! The program does not end until the
! BACK key is pressed. When BACK
! is pressed, there will be a graphics
! not open error. The onerror will
! it an end without an error.

ONERROR:
END
