!!
This file contains executable examples
For - Next operations. Examine 
the code and run then run this program.
!!

PRINT "Example 1"
FOR i = 1 TO 3 STEP 1
 PRINT i
NEXT i
PRINT i * 10

! will print 1, 2, 3, 40

PRINT "Example 2"
FOR j = 1 TO 3
 PRINT j
NEXT j
PRINT j*10

! will also print 1, 2, 3, 40
!
! if the STEP is not present, STEP 1
! is assumed

PRINT "Example 3"
start = 1
stop = 4
FOR index = start TO stop STEP 2
 PRINT index
NEXT index
PRINT index*10

! will print 1, 3, 50
!
! The loop will repeat until the
! index > stop
!
! If the STEP value is negative
! the loop will repeat until the
! index < stop

PRINT "Example 4"
start = 3
stop = 1
FOR index = start TO stop STEP -2
 PRINT index
NEXT index
PRINT index*10

! will print 3, 1, -10
!
! FOR loops can be nested to any number of levels
!
PRINT "Example 4"
FOR i = 1 TO 2
 FOR j = 1 TO 2
  FOR k = 1 TO 2
   PRINT i, j, k
  NEXT k
 NEXT j
NEXT k

! The index variable name
! after NEXT is not required nor
! is it checked. It is essentially
! a comment to help you keep track
! your nesting.
!
! Some restrictions on variable names.
!
! FOR <var> = <exp1> TO <exp2> STEP <exp3>
!
! The variables used in <exp1> must not contain
! the character sequence TO.
!
! The variables used in <exp2> must not contain
! the character sequence STEP.
!
! If you do this, you will get unexpected results.

bottom = 2
top = 3
FOR index = botTOm TO top STEP 2
 PRINT index
NEXT

! But you can use parentheses to hide the keyword.

bottom = 2
top = 3
FOR index = (bottom) TO top STEP 2
 PRINT index
NEXT
