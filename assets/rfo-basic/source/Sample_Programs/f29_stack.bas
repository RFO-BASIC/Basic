!!
This program demonstrates
the operation of Stacks
!!

! Create a numeric stack
STACK.create N, nStack

! Push some numbers onto
! the numeric stack

FOR i = 1 TO 5
 STACK.push nStack, i
 PRINT "Push "; i
NEXT i

! Pop numbers from
! stack until empty

STACK.isempty nStack, empty
WHILE !empty
 STACK.pop nStack, value
 PRINT "Pop, top value = "; value
 STACK.isempty nStack, empty
REPEAT

PRINT "Stack is empty"
PRINT " "
! Create a string stack
STACK.create S,sStack

! Push some strings onto
! the string stack

s$ = "Last"
GOSUB sPush
s$ = "In"
GOSUB sPush
s$ = "First"
GOSUB sPush
s$ = "Out"
GOSUB sPush

STACK.isempty sStack, empty
WHILE !empty
 STACK.pop sStack, value$
 PRINT "Pop, top value = "; value$
 STACK.isempty sStack, empty
REPEAT

PRINT "Stack is empty"
END

sPush:
PRINT "Push "; s$
STACK.push sStack, s$
RETURN
