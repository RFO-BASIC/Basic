!!
This file contains executable examples
DO - UNTIL and WHILE - REPEAT operations. 
Examine the code  and run then run this program.
!!

! DO - UNTIL Example

! The following example will print
! 1, 2, 3

PRINT "Do Example"
index = 1
limit = 3
DO
 PRINT index
 index = index + 1
UNTIL index = limit
PRINT index

! The following example will print
! 4, 5, 6, 7

PRINT "While Example"
index = 4
limit = 6
WHILE index <= limit
 PRINT index
 index = index + 1
REPEAT
PRINT index

! Both loop types may be nested
! to any level
!
! The following example will print
! 1, 1, 1
! 1, 1, 2
! 1, 2, 1
! 1, 2, 2
! 2, 1, 1
! 2, 1, 2
! 2, 2, 1
! 2, 2, 2
! 30, 30, 30
!
PRINT "Nested DO and WHILE examples"

index_1 = 1
limit_1 = 3
DO
 index_2 = 1
 limit_2 = 3
 WHILE index_2 < limit_2
  index_3 = 1
  limit_3 = 3
  WHILE index_3 < limit_3
   PRINT index_1, index_2, index_3
   index_3 = index_3 + 1
  REPEAT
  index_2 = index_2 + 1
 REPEAT
 index_1 = index_1 + 1
UNTIL index_1 = limit_1
PRINT index_1*10, index_2*10, index_3*10

END
