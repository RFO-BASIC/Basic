!!
This file contains executable examples
of File I/O operations. 
Examine the code  and run then run 
this program. 
!!
PRINT "******** Text  Read, Write Example"

TEXT.OPEN W, FN1, "testfile.txt"
TEXT.WRITELN FN1, "Test line 1"
TEXT.WRITELN FN1, "Test line 2"
TEXT.CLOSE FN1

TEXT.OPEN R, FN2, "testfile.txt"
DO
 TEXT.READLN FN2, a_line$
 PRINT a_line$
UNTIL a_line$ = "EOF"
TEXT.CLOSE FN2


! Byte file I/O

PRINT "******** Byte.copy example"
BYTE.OPEN R, rf, "Cartman.png"
BYTE.COPY rf, "Cartman2.png"
BYTE.CLOSE rf

PRINT "******** Show Cartman2 exists"
FILE.EXISTS b, "Cartman2.png"
IF b THEN PRINT "It does exist" ELSE PRINT "It does not exist"

PRINT "******** Sizes of the files"
FILE.SIZE s1, "Cartman.png"
FILE.SIZE s2, "Cartman2.png"
PRINT s1, s2


! MKDIR and DIR Example

PRINT "******** MKDIR and DIR Example"

PRINT "mkdir \"newdir\""
FILE.MKDIR "newdir"
FILE.DIR "", dir$[]
ARRAY.LENGTH length, dir$[]

FOR index = 1 TO length
 PRINT dir$[index]
NEXT index

! RENAME

PRINT "******** Rename Example"
PRINT "rename \"newdir\", \"olddir\""
FILE.RENAME "newdir", "olddir"
FILE.DIR "", dir2$[]
ARRAY.LENGTH length, dir2$[]

FOR index = 1 TO length
 PRINT dir2$[index]
NEXT index

PRINT "******** Root example"
FILE.ROOT r$
PRINT r$

END

