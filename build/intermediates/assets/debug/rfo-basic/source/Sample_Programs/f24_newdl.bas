!!
This program demonstrates the use 
of the gr.NewDL to change the display list
and thus change the Z order
of objects.

Coincidentally, it demonstrates
the some of BASIC!s array manipulation
capabilities
!!

GR.OPEN 255,255,255,255
GR.ORIENTATION 0
GR.SCREEN width, height
count = 8
delay = 3000

s =( width/(count))/4
center = height/2
@top = center - 5*s

DIM objs[count]

!!
Draw count rectangles
4*s high
4*s wide with a 1*s overlap

Each rectangle will have
a random color

The first rect starts at 
3*s
!!

left = 3*s

FOR i = 1 TO count
 rr = RND() * 255
 gg = RND() * 255
 bb = RND() * 255
 GR.COLOR 240, rr, gg, bb, 1
 GR.RECT objs[i], left, @top, left + 4*s, @top + 4*s
 left = left + 3*s
 @top = @top + s
NEXT i

! Display loop

DO
 !draw in original order

 GR.RENDER
 PAUSE delay

 ! draw in reverse order

 ARRAY.REVERSE objs[]
 GR.NEWDL objs[]
 GR.RENDER
 PAUSE delay

 ! Do four cycles with
 ! random shuffling

 FOR j = 1 TO 4
  ARRAY.SHUFFLE objs[]
  GR.NEWDL objs[]
  GR.RENDER
  PAUSE delay
 NEXT j

 ! Restore to the original order

 ARRAY.SORT objs[]
 GR.NEWDL objs[]

UNTIL 0

