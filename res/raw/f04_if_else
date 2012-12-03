!!
This file contains executable examples
if - then - else operations. Examine 
the code  and run then run this program.
!!

! The code will print numbers
! 1 through 13 in the sequence
!
PRINT "Example 1"

IF 1<2 THEN PRINT 1 ELSE PRINT 1000

IF 1>2 THEN PRINT 2000 ELSE PRINT 2

IF 1 THEN
 PRINT 3
 PRINT 4
ELSE
 PRINT 3000
ENDIF
PRINT 5

!
! Note: IF no statement is going to follow
! the THEN on same line,
! the THEN is assumed and
! need not be present
!

IF 0
 PRINT 4000
 PRINT 4001
ELSE
 PRINT 6
ENDIF
PRINT 7

IF 1 | 0 THEN
 PRINT 8
 PRINT 9
ELSE
 PRINT 5000
 PRINT 5001
ENDIF
PRINT 10

IF 1 & 0 THEN
 PRINT 6000
 PRINT 6001
ELSE
 PRINT 11
 PRINT 12
ENDIF
PRINT 13

PRINT "Example 2"

IF 1 = 1 THEN
 PRINT 1
 IF 1 = 2 THEN
  PRINT 1000
 ELSE
  PRINT 2
 ENDIF
 PRINT 3
ENDIF

IF 3 <> 3 THEN
 PRINT 2000
 IF 1 = 2 THEN
  PRINT 1000
 ELSE
  PRINT 2000
 ENDIF
 PRINT 3000
ELSE
 PRINT 4
 PRINT 5
ENDIF


IF 1 & 1
 PRINT 6
 IF 1 THEN
  PRINT 7
  IF 2 THEN
   PRINT 8
   PRINT 9
  ELSE
   PRINT 9000
  ENDIF
  PRINT 10
 ENDIF
 PRINT 11
ENDIF

! ENDIF provides for a chain
! of embedded IF statements.

! Note that only if first
! matching ELSEIF is executed,

! Note that the THEN is optional

PRINT "Example 3"

x=1
IF x=3
 PRINT 3
ELSEIF x=2 THEN
 PRINT 2
ELSEIF x=1
 PRINT 1
ELSEIF x=4 THEN
 PRINT 4
ELSEIF x=3
 PRINT 3000
ENDIF

! ESLEIF may be followed
! by an ELSE; however
! no following ELSEIF
! will ever be executed.

x=1
IF x=3
 PRINT 3
ELSE IF x =4
 PRINT 4
ELSE
 PRINT 2
ELSEIF x=1
 PRINT 1
ENDIF