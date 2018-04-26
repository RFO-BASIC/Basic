!!
This program can be used to
explore the file system on an
Android device. 

Any filename with a (d) after
it is a directory. Tap on that
entry to open that directory.

Tap on the top entry, "..", to
move up one directory level.

Press the BACK key to exit.

This program will list all the
way up to the file system root.

The initial directory shown is
"/sdcard/rfo-basic/data/"

!!

! Lists a directory. Gets and
! returns the user's selection

path$ = ""
loop:

! Get the listing from
! the path directory
! and sort it

ARRAY.DELETE d1$[]
FILE.DIR path$, d1$[]
ARRAY.LENGTH length, d1$[]

! Copy the file list
! adding the top ",,"
! entry

ARRAY.DELETE d2$[]
DIM d2$[length+1]
d2$[1] = ".."
FOR i = 1 TO length
 d2$[i + 1] = d1$[i]
NEXT i

! Present the list
! and get the user's
! choice

SELECT s, d2$[], ""
IF s = 0 THEN END

! If top entry, "..", not
! selected then append
! the selected directory
! name to the path

IF s>1
 n = IS_IN("(d)", d2$[s])
 IF n = 0
  GOTO loop
 ENDIF
 dname$ = LEFT$(d2$[s],n-1)
 path$=path$+dname$+"/"
 GOTO loop
ENDIF

! If s = 1 then must back
! one level

! if at start path then
! back up one level

IF path$ = ""
 path$ = "../"
 GOTO loop
ENDIF

! Not at start path
! split the path by
! the "/" chars

ARRAY.DELETE p$[]
SPLIT p$[], path$, "/"
ARRAY.LENGTH length, p$[]

! If the last entry is
! ".." then add "../"
! to back up one level

IF p$[length] = ".."
 path$ = path$ + "../"
 GOTO loop
ENDIF

! Last entry is not ".."
! so must delete the
! last directory from
! the path

! If only one entry
! then path is back
! to the base path

IF length = 1
 path$ = ""
 GOTO loop
ENDIF

! Reconstruct path without
! the last directory

path$ = ""
FOR i = 1 TO length - 1
 path$ = path$ + p$[i] + "/"
NEXT i

GOTO loop
