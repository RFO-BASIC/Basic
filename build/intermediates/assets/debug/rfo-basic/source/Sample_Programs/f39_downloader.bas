! f39_downloader.bas
!
! Downloads shared BASIC! programs
! from the BASIC! shared program ftp site.
! Run the program and select the BASIC!
! program that you want to down load.
!
PRINT "Connecting to ftp server..."

! Set the default path

path$="ftp.laughton.com"

FTP.OPEN path$, 21, "basic", "basic"

! Clear the default path

path$="/"

loop: % Main loop

FTP.DIR prelist

! Add the quit option and
! Create the list (not
! prelist)

LIST.ADD prelist, "Quit!"
LIST.CREATE S, list
LIST.ADD list, ".."
LIST.SIZE prelist, s

! Copy the prelist to the
! List without ".ftpquota"
! and ".haccess" if they
! Exist

a=2
IF path$="/" then
s=s-2
a=4
End IF
s=s-2

FOR i=1 TO s
 LIST.GET prelist, i+a, toadd$
 LIST.ADD list, toadd$
NEXT i

CLS % Clear the screen 

SELECT s, list, path$
! Use the path as message

! See if user quits

IF s=0 THEN END

! Get the file/folder name

LIST.GET list, s, choose$

! See if user quits

IF choose$="Quit!" THEN END

! Check if user clicks ".."
! If it does, go back

IF choose$=".." THEN
 IF path$="/" THEN
  PRINT "Can't go back any more"
  GOTO skip
 END IF
 SPLIT result$[], path$, "/"
 ARRAY.LENGTH l, result$[]
 PRINT "Going back..."
 l=l-1
 path$=""
 FOR i=1 TO l
  path$=path$+result$[i]+"/"
 NEXT i
 FTP.CD path$
 GOTO skip
END IF

! Check if user clicked
! a folder

IF IS_IN("(d)", choose$, 1)>0 THEN
 a=LEN(choose$)
 a=a-3
 path$=path$+MID$(choose$, 1, a)+"/"
 FTP.CD path$
 PRINT "Loading directory ";"laughton.com/basic/programs/";path$;"..."
 GOTO SKIP
END IF

! If the program reaches here
! Means than the user clicked
! A file

! Guess the destination of
! The download directory

IF IS_IN(".bas", choose$, 1) THEN
 default$="rfo-basic/source/"
ELSEIF IS_IN(".db", choose$, 1)
 default$="rfo-basic/databases/"
ELSE
 default$="rfo-basic/data/"
END IF

! Ask the user the
! Destination path

INPUT "Destination path", dest$, default$

dest$="../../"+dest$

! Download the file

PRINT "Downloading file..."
POPUP "Downloading file...", 0, 0, 1
FTP.GET path$+choose$, dest$+choose$

! Tell user that the file has
! Finished downloading and
! Loop again

PRINT "Download complete!"
PAUSE 1000 % Pause for a little

skip:

ARRAY.DELETE result$[]
LIST.CLEAR prelist
LIST.CLEAR list

GOTO loop % Loop again
