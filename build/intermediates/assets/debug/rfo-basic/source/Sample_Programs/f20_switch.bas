! This program demonstrates the
! SWITCH commands

PRINT "How many days in a given month?"
PRINT  " "
INPUT "Enter Month Name", aname$
name$ = LOWER$(aname$)
num_days = 0

SW.BEGIN name$
 SW.CASE "january"
 SW.CASE "march"
 SW.CASE "may"
 SW.CASE "july"
 SW.CASE "august"
 SW.CASE "october"
 SW.CASE "december"
  num_days = 31
  SW.BREAK

 SW.CASE "april"
 SW.CASE "june"
 SW.CASE "september"
 SW.CASE "november"
  num_days = 30
  SW.BREAK

 SW.CASE "february"

  TIME Year$, Month$, Day$, Hour$, Minute$, Second$
  year = VAL(Year$)

  IF ( (MOD(year,4) = 0) & (MOD(year,100) <> 0) ) |  (MOD(year,400) = 0) THEN
   num_days = 29
  ELSE
   num_days = 28
  ENDIF
  SW.BREAK

 SW.DEFAULT
  PRINT "Invalid month"

SW.END

IF num_days <>0
 PRINT "The number of days in " + aname$ + " is:" + FORMAT$("##",num_days)
ENDIF

END


