!!

Demonstrates the Select Command

!!

! Load an array with the names of the months

ARRAY.LOAD months$[], "January", "February"~
"March", "April", "May", "July", "August"~
"September", "October", "November", "December"

! Set the Popup Message

msg$ ="Select the month of your birth."

! Shows the list and waits for the user
! to make the selection.

SELECT month, months$[], msg$

! if the selection was zero, the user hit the
! BACK key

IF month = 0
 PRINT "You are unborn"
ELSE
 PRINT "You were born in ";
 PRINT months$[month]
ENDIF

