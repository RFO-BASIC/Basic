!!
This program demonstrates
the gr.poly command.

Several polygons with
different numbers of 
sizes will be drawn.
!!

! Set up the graphics
GR.OPEN 255, 255, 255, 255
GR.SET.STROKE 4
GR.ORIENTATION 0  % Force landscape

! Get the screen size
! of this device and then
! scale from 800 x 480
! to fit this screen

GR.SCREEN w, h
dw = 800
dh = 480
sw = w/dw
sh = h/dh
GR.SCALE sw,sh

! Draw a filled triangle
! and then move it and
! draw it is unfilled.

ARRAY.LOAD a[], 60, 20~
180, 70~
30, 180

LIST.CREATE n, List1
LIST.ADD.ARRAY List1, a[]

GR.COLOR 255, 255, 0, 0, 1
GR.POLY pt, List1
GR.COLOR 255, 255, 0, 0, 0
GR.POLY pt, List1, 200, 0

! Draw four sided, regular
! filled polygon and then
! move it and draw it as
! unfilled

ARRAY.LOAD b[], 500,20~
700, 20~
550, 100~
350, 100

LIST.CREATE n, List2
LIST.ADD.ARRAY List2, b[]
GR.COLOR 255, 255, 0, 0, 1
GR.POLY pt, List2
GR.COLOR 255, 255, 0, 0, 0
GR.POLYt pt, List2, 0, 110

! Draw a polygon whose
! lines cross

ARRAY.LOAD c[], 50, 300~
250, 300~
80, 400~
220, 400

LIST.CREATE n, List3
LIST.ADD.ARRAY List3, c[]
GR.COLOR 255, 255, 0, 255, 1
GR.POLY pt, List3

! Draw an irregular,
! six sided, unfilled
! polygon

ARRAY.LOAD d[], 550,260~
650, 260~
750, 320~
650, 420~
550, 420~
450, 320

LIST.CREATE n, List4
LIST.ADD.ARRAY List4, d[]
GR.COLOR 255, 0, 0, 255, 0
GR.POLY pt, List4

GR.RENDER


! Stay running to keep the
! graphics screen showing
DO
UNTIL 0

