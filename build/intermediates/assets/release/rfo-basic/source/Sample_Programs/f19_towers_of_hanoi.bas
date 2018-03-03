!!
Solves the Tower of Hanoi puzzle using functions
and recursion.

The puzzle has disks stacked on one of three pegs.
The object is to move all the disks from that peg
to another peg. The rule is that no disk may ever
be on top of a smaller disk

!!

!****  Hanoi Algorithm ****

FN.DEF hanoi(disk,dest,source,other, peg[],  parms[])

 ! the parms peg[], and parms[] are passed through
 ! for drawing and are not used the algoritim

 IF disk > 0
  n = hanoi(disk-1,other,source,dest, peg[], parms[])
  n = move(source, dest, peg[],  parms[])
  n = hanoi(disk-1,dest,other,source, peg[],  parms[])
  FN.RTN 0
 ENDIF
 FN.RTN 0
FN.END

!**** dleft ****
! Calculate the x coordinate of left side of a disk given
! the disk number and margin

FN.DEF dleft(disk, w, peg, margin)
 c = w/4 - disk * margin
 q = c/2
 p = peg*(w/4)
 FN.RTN p-q
FN.END

!**** dright ****
! Calculate the x coordinate of the right side of a disk
! given the disk number and margin

FN.DEF dright(disk, w, peg, margin)
 c = w/4 - disk * margin
 q = c/2
 p = peg*(w/4)
 FN.RTN p + q
FN.END

!**** dtop ****
! Calculate the y coordinate of the top of a disk
! given the disk level on the peg and the
! space between the disks

FN.DEF dtop(level, height, space)
 b = height - (level * 2 * space)
 t = b - space
 FN.RTN t
FN.END

!**** dbottom ****
! Calculate the y coordinate of the bottom  of a disk
! given the disk level on the peg and the
! space between the disks

FN.DEF dbottom(level, height, space)
 b = level * 2 * space
 FN.RTN height - b
FN.END

!**** move ****

! Move one disk
FN.DEF move( from, to, peg[], parms[])
 i = 1
 ! find the first non-empty level on the from peg

 WHILE peg[from,i] <> 0
  i = i +1
 REPEAT

 ! back up to the last non-empty peg
 i = i -1

 ! Get the disk number
 disk = peg[from,i]

 ! Remove the disk from the peg
 peg[from,i] = 0

 i = 1
 ! find the first non-empty level on the to peg

 WHILE peg[to, i] <>0
  i = i +1
 REPEAT

 ! put the disk in that first empty level
 peg[to, i] = disk

 ! now go draw the pegs and disks
 n = draw_it( peg[], parms[] )

 FN.RTN 0
FN.END

!**** drawit ****
! Draw the pegs and disks

FN.DEF draw_it( peg[] , parms[])

 GR.CLS

 ! Get the values out of parms[]
 h = parms[1]
 w = parms[2]
 space = parms[3]
 margin = parms[4]

 ! Color the peg red
 GR.COLOR 255,255,0,0,1

 ! Draw the pegs
 GR.LINE f, w/4, h, w/4, h/3
 GR.LINE f, w/2, h, w/2, h/3
 GR.LINE f, 3*w/4, h, 3*w/4, h/3

 ! Color the disks blue
 GR.COLOR 255,0,0,255,1

 ! For each peg
 FOR thePeg = 1 TO 3

  !Draw each disk current on the peg
  theLevel = 1
  WHILE peg[thePeg, theLevel] <> 0
   disk = peg[ thePeg, theLevel]

   ! get the coordinates of the disk
   ! at this peg and this level
   left = dleft(disk, w, thePeg, margin)
   right = dright(disk,w,thePeg,margin)
   xtop = dtop(theLevel, h, space)
   bottom = dbottom(theLevel, h, space)

   ! Draw one disk
   GR.RECT f, left, xtop, right, bottom
   theLevel = theLevel + 1

  REPEAT
 NEXT thePeg

 ! Draw everything
 GR.RENDER
 PAUSE 500

 FN.RTN 0
FN.END

!**** Main Program ****

! Number of disks
disk_count = 4

! Define the pegs and fill the first peg with the disks
! Disk 1 is the the largest disk

! Dimension array such there will always be a disk
! zero at the top of the peg

DIM peg[3,disk_count+1]

! Fill peg 1
FOR i = 1 TO disk_count
 peg[1,i] = i
NEXT i


GR.OPEN 255,255,255,255
GR.ORIENTATION 0
GR.SCREEN width, height

! Largest disk is 1/4 screen width minus
! margin. Smaller disks are reduced
! by margin

margin = 20

! space between disks and height
! of the disks

space = height/20

DIM parms[4]
parms[1] = height
parms[2] = width
parms[3] = space
parms[4] = margin

! Draw the initial pegs and disks
n = draw_it(peg[], parms[])

! Solve the puzzle
n =hanoi(disk_count, 3, 1, 2, peg[], parms[])

! Done

! Stay running to keep the
! graphics screen showing
DO
UNTIL 0

ONERROR:
END


