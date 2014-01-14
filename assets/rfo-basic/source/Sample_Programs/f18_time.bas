! TIME Command
!
! TIME Year$, Month$, Day$, Hour$, Minute$, Second$
!
! All the parameters are returned by the TIME command.
!
! Year$: The four digit year
! Month$: The two digit month (1 to 12)
! Day$: The two digit day of the month (1 to 31)
! Hour$: The two digit hour (0 to 23)
! Minute$: The two digit minute (0 to 59)
! Second$: The two digit second (0 to 59)
! WeekDay: The number of the day of the week (1 to 7)
! isDST: True (1) if the day is in Daylight Saving Time
!

DIM m$[12]
m$[1] = "January"
m$[2] = "February"
m$[3] = "March"
m$[4] = "April"
m$[5] = "May"
m$[6] = "June"
m$[7] = "July"
m$[8] = "August"
m$[9] = "September"
m$[10] = "October"
m$[11] = "November"
m$[12] = "December"

ARRAY.LOAD d$[], "Sunday","Monday","Tuesday"~
"Wednesday","Thursday","Friday","Saturday"

TIME Year$, Month$, Day$, Hour$, Minute$, Second$, WeekDay, isDST
TIMEZONE.GET tz$

PRINT "Today is: "; d$[WeekDay]
PRINT m$[VAL(Month$)]; " "; Day$; ", "; Year$
PRINT ""

AMPM$ = " AM"
Hour = VAL(Hour$)
IF Hour = 0 THEN
 Hour = 12
ELSEIF Hour >= 12 THEN
 AMPM$ = " PM"
 IF Hour > 12 THEN Hour = Hour - 12
ENDIF

PRINT "The Time Is:"
PRINT FORMAT$("##",Hour); ":"; Minute$; AMPM$
IF isDST
 PRINT "Daylight Saving";
ELSE
 PRINT "Standard Time";
ENDIF
PRINT " in "; tz$
END "Have a nice day!"
