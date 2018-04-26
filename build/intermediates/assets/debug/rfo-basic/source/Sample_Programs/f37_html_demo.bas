!!
This program demonstrates some
of the possibilities
when using the BASIC! html commands.
!!

! html must be opened before doing
! any html command
HTML.OPEN

! Load the file, htmldemo1.html

HTML.LOAD.URL "htmldemo1.html"

!The user now sees the html

! We can now monitor the user
! actions

xnextUserAction:

! loop until data$ is not ""

DO
 HTML.GET.DATALINK data$
UNTIL data$ <> ""

! The first four characters of data$
! identify the type of data returned.
! Extract those three characters to
! use in a switch statement

type$ = LEFT$(data$, 4)

! Trim the first four characters from
! data$

data$ = MID$(data$,5)

! Act on the data type
! Shown are all the current data types

SW.BEGIN type$

 ! Back Key hit.
 ! if we can go back then do it
 SW.CASE "BAK:"
  PRINT "BACK key: " + data$
  IF data$ = "1" THEN HTML.GO.BACK ELSE END
  SW.BREAK

 ! A hyperlink was clicked on
 SW.CASE "LNK:"
  PRINT "Hyperlink selected: "+ data$
  IF RIGHT$(data$,7)<>"sign_in" THEN HTML.LOAD.URL data$
  SW.BREAK

 ! An error occured
 SW.CASE "ERR:"
  PRINT "Error: " + data$
  SW.BREAK

 ! User data returned
 SW.CASE "DAT:"
  PRINT "User data: " + data$
  
  ! Check for Exit
  IF data$ = "Exit"
   PRINT "User ended demo."
   HTML.CLOSE
   END
  ENDIF
  
  ! Check for Speech to text
  IF data$ = "STT"
   STT.RESULTS theList
   ! Get the first (best) result
   LIST.GET theList, 1, theText$
   ! Insert the text into the HTML
   theText$ = "You said: " + theText$
   insert$ = "javascript:text(\""+theText$+"\")"
   PRINT insert$
   HTML.LOAD.URL insert$
  ENDIF
   
  SW.BREAK

 ! Form data returned.
 ! Note: Form data returning
 ! always exits the html.

 SW.CASE "FOR:"
  PRINT "Form data: "+data$
  END
  SW.BREAK

 ! download requested
 ! extract the filename
 ! tell user download starting
 ! download it

 SW.CASE "DNL:"
  PRINT "Download: " + data$
  ARRAY.DELETE p$[]
  SPLIT p$[], data$, "/"
  ARRAY.LENGTH l, p$[]
  fn$ = p$[l]
  HTML.LOAD.STRING "<html> Starting download of " + fn$ + "</html>"
  BYTE.OPEN r,f,data$
  PAUSE 2000
  HTML.GO.BACK
  BYTE.COPY f,fn$
  BYTE.CLOSE f
  SW.BREAK


 SW.DEFAULT
  PRINT "Unexpected data type:", type$ + data$
  END

SW.END

GOTO xnextUserAction

