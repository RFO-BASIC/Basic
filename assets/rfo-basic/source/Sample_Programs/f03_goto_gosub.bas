!!
This file contains executable examples
GoTo and GoSub operations. Examine 
the code  and run then run this program.
!!

! GoTo Example

PRINT "GoTo Example"

PRINT 1
GOTO skip
PRINT 1000
skip:
PRINT 2

! Prints 1, 2

! GOSUB Example

PRINT "GoSub Example"

PRINT 1
GOSUB subroutine
PRINT 4
PRINT 5
GOTO done
subroutine:
PRINT 2
PRINT 3
RETURN
DOne:
 PRINT "done"

 ! Prints 1, 2, 3, 4, 5, done




