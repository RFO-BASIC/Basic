!! htmledit.bas
A program to edit html files.
Input the filename without 
the .html extension
!!

data$ = ""

! Get the filename
! and add the .html

INPUT "Enter filename", fn$
fn$=fn$+".html"

! test to see if the file exists
! if it does exist then
! open it, grab the contents
! and close the file

FILE.EXISTS fe, fn$
IF fe THEN GRABFILE data$, fn$

! use text.input to do
! the editing

TEXT.INPUT data1$, data$

! Now write the edited
! text to the file

TEXT.OPEN w,f,fn$
TEXT.WRITELN f,data1$
TEXT.CLOSE f

END

