!!

Demonstrates the Select Command

!!

! Load an array with the names of the months

ARRAY.LOAD months$[], "January", "February"~
"March", "April", "May", "June", "July", "August"~
"September", "October", "November", "December"

DIALOG.MESSAGE ,"Use SELECT or DIALOG.SELECT?", b, "DIALOG.SELECT", "SELECT"

! Set the Popup Message

msg$ ="Select the month of your birth."

! Shows the list and waits for the user
! to make the selection.

IF b = 2  % user chose SELECT
SELECT month, months$[], msg$
ELSE      % chose DIALOG or did not choose
DIALOG.SELECT month, months$[], msg$
ENDIF

! if the selection was zero, the user hit the
! BACK key

IF month = 0
 PRINT "You are unborn"
ELSE
 PRINT "You were born in ";
 PRINT months$[month]
ENDIF

