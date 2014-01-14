!! When you run this program, the Graphics
! Screen will be displayed. To exit back
! to the BASIC! text output screen, hit
! the Back key.
!
! Open graphics screen with
! background color of WHITE
!!
GR.OPEN 255, 255, 255, 255
GR.ORIENTATION 0  % Force landscape
GR.SCREEN w, h
whalf = w/2
hhalf = h/2
wqtr = w/4
hqtr = h/4
factor = 0.1

! Draw divider lines

!Set the draw color to black
a = 255
r = 0
g = 0
b = 0
fill = 0
GR.COLOR a,r,g,b,fill

x1 = 0
y1 = hhalf
x2 = w
y2 = hhalf

GR.LINE n, x1, y1, x2, y2

x1 = whalf
y1 = 0
x2 = whalf
y2 = h

GR.LINE n, x1, y1, x2, y2

! Draw unfilled red circle

r = 255
g = 0
b = 0
GR.COLOR a,r,g,b,fill

cx = wqtr
cy = hqtr
radius = hqtr - factor*hqtr

GR.CIRCLE nc, cx, cy, radius

! Label the circle

GR.COLOR 255,0,0,0,1
GR.TEXT.SIZE 30
GR.TEXT.ALIGN 2
GR.TEXT.DRAW pt, cx, cy , "Circle"

! Draw a filled red oval

fill = 1
GR.COLOR a,r,g,b,fill

left = whalf  + factor* (wqtr)
LET top = factor*(hqtr)
right = w - factor*(wqtr)
bottom = hhalf -factor*(hqtr)

GR.OVAL no, left, top, right, bottom

! Label the oval

GR.COLOR 255,0,0,0,1
GR.TEXT.DRAW pt, whalf + wqtr, hqtr, "Oval"

! Draw a blue filled rectangle

r = 0
b = 255
g = 0
GR.COLOR a,r,g,b,fill

left = factor*(wqtr)
LET top = factor*(hqtr) + hhalf
right = whalf - factor*(wqtr)
bottom = h - factor*(hqtr)

GR.RECT nr, left, top, right, bottom

! Label the rectangle

GR.COLOR 255,0,0,0,1
GR.TEXT.DRAW pt, wqtr, (3*hqtr ), "Rectangle"


! Draw a filled yellow Arc as a Pie Chart

r = 255
g = 255
b = 0
GR.COLOR a,r,g,b,fill

left = 3*wqtr - hqtr - factor*hqtr
LET top = 3*hqtr - hqtr + factor*hqtr
right = 3*wqtr + hqtr - factor*hqtr
bottom =3*hqtr + hqtr - factor*hqtr
sa = 0
ea = 240
cf = 1

GR.ARC na, left, top, right, bottom, sa,ea,cf

! Label the Pie Chart

GR.COLOR 255,0,0,0,1
GR.TEXT.DRAW pt, 3*wqtr, 3*hqtr, "Filled Arc"

! Now render everything
GR.RENDER


! Hide and show the objects
! Pause 2 seconds before starting
! Pause 950 milliseconds with each change

PAUSE 2000

t = 950

GR.HIDE nc
GR.RENDER
PAUSE t
GR.SHOW nc
GR.RENDER
PAUSE t
GR.HIDE no
GR.RENDER
PAUSE t
GR.SHOW no
GR.RENDER
PAUSE t
GR.HIDE nr
GR.RENDER
PAUSE t
GR.SHOW nr
GR.RENDER
PAUSE t
GR.HIDE na
GR.RENDER
PAUSE t
GR.SHOW na
GR.RENDER
PAUSE t

! Stay running to keep the
! graphics screen showing
DO
UNTIL 0

ONERROR:
END
