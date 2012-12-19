! An SQL Interface Example
!
! Ms O'Brien teaches Java at San Jose State.
! Her students are always asking what their
! overall grade average is. In order to
! quickly answer their question, she has
! created an SQL database with this
! information. She keeps the name (first,
! last) of the student, the current grade
! average, and the student's attendance
! percentage. In this example, she is
! creating the table for her Java 101
! class.
!
! First she created the database.

dbname$ = "grades.db"


SQL.OPEN DB_Ptr, dbname$

! Now that the database has been
! opened or created, she can use the returned
! DB_ptr to work on the database.
!
! The next thing she needs to do is create a
! table in this new database.

tbname$ = "java101"
c1$ = "first_name"
c2$ = "last_name"
c3$ = "grade_average"
c4$ = "percent_attendance"

! Before the table is created, it will be removed
! (Drop) the table from the database if it already
! exists. This will avoid duplicate rows in the
! table resulting from multiple executions of this
! program.

SQL.DROP_TABLE DB_Ptr, tbname$

SQL.NEW_TABLE DB_Ptr, tbname$, c1$, c2$, c3$, c4$

! The next thing she needs to do is initialize
! the database table with some students and their
! data.

fn$ = "Betty"
ln$ = "Ng"
ga$ = "96"
pa$ = "100"
GOSUB insert

fn$ = "Janice"
ln$ = "Chen"
ga$ = "88"
pa$ = "100"
GOSUB insert

fn$ = "Bill"
ln$ = "Wilkinson"
ga$ = "43"
pa$ = "28"
GOSUB insert

fn$ = "Jose"
ln$ = "Martinez"
ga$ = "72"
pa$ = "65"
GOSUB insert

fn$ = "Tamasin"
ln$ = "Washington"
ga$ = "91"
pa$ = "96"
GOSUB insert

fn$ = "Shania"
ln$ = "Lewis"
ga$ = "82"
pa$ = "100"
GOSUB insert

fn$ = "Pierre"
ln$ = "Thomas"
ga$ = "78"
pa$ = "65"
GOSUB insert

GOTO skip1

! INSERT subroutine

insert:
SQL.INSERT DB_Ptr, tbname$, c1$,fn$,  c2$,ln$, c3$,ga$, c4$,pa$
RETURN

! Now that she has built the database, she should
! look at the data base to insure everything
! is correct.
!
skip1:

title$ = "First look"
GOSUB show_all

GOTO skip2

! Subroutine to show all of the data base

show_all:
!
! First, submit a query that will return all
! rows of data
!
! Start by building a string with the list
! column names separated by commas. Note the
! the first column requested is the Row Index.
! SQLite automatically generates a unique index
! value for each row. That Index is named "_id"

Columns$ = "_id," + c2$  +"," + c1$ + "," +c3$ + "," + c4$

! Note that she is asking that the rows be returned
! with the columns in the order last_name, first_name
! instead of the order in which they are in the table.
!
! Query the data base. The query will return a cursor
! that can be used to step through each of the
! returned rows.
!

SQL.QUERY Cursor, DB_Ptr, tbname$,  Columns$

! fall through into the show_results subroutine

! show_query_results subroutine

show_query_results:

PRINT title$

! Step the cursor to each returned row and get
! the result values. The number of result variables
! should be the the same as the number of queried
! columns. The done variable returns false (0) until
! the last row is read.

xdone = 0
DO
 SQL.NEXT xdone,cursor,index$,v1$,v2$,v3$,v4$
 IF !xdone THEN PRINT  index$,v1$,v2$,v3$,v4$
UNTIL xdone
PRINT " "
RETURN

skip2:

! The first person who asks for their grade
! is, of course, Betty Ng. Ms. O'Brien formats
! a query to get just Betty's data.
!
! She will use the already formatted Columns$
! to return the Index and all the columns from
! Betty's data row.
!
! She creates a Where$ string that will select
! only Betty's Column

Where$ = "first_name = 'Betty' AND last_name = 'Ng' "
SQL.QUERY Cursor, DB_Ptr, tbname$,  Columns$, Where$

! Now go display the result

title$ = " Betty's grade"
GOSUB show_query_results

! When other students ask for their grade,
! Ms O'Brien will run a similar query with
! their name.
!
! Now Ms O'Brien would like a list of everyone
! who is making a grade of 90 or better.
!
! In this case, the Where$ string looks like this:

Where$ = "grade_average > '89' "
SQL.QUERY Cursor, DB_Ptr, tbname$,  Columns$, Where$

! Now go display the result

title$ = "People with grades of 90 or better."
GOSUB show_query_results

!
! Bill Wilkinson was doing so poorly in the class
! that he decided to drop the class. Ms O'Brien
! now needs to remove him from the database.
!

Where$ = "first_name = 'Bill' AND last_name = 'Wilkinson' "
SQL.DELETE DB_ptr, tbname$, Where$

! Now display the table without Bill

title$ = "Table without Bill Wilkinson"


! For this particular query, she wants the data
! returned in ascending order of the last name.

Order$ = "last_name ASC"
SQL.QUERY Cursor, DB_Ptr, tbname$,  Columns$, "", Order$
GOSUB show_query_results

! The Mid Term test results are in. Ms O'Brien
! needs to update the grades. She will start
! by changing Tamasin's grade. Her attendance
! record has not changed.

Where$ = "first_name = 'Tamasin' AND last_name = 'Washington' "
SQL.UPDATE DB_Ptr, tbname$, c3$, "94": Where$

! Now examine Tamasin's record to insure that
! it is correct.

SQL.QUERY Cursor, DB_Ptr, tbname$,  Columns$, Where$
title$="Tamasin's new grade"
GOSUB show_query_results

! Finally, Ms O'Brien would like to know the
! average grade for her class.
!
! She only needs to get the grade average column

Columns$ = "grade_average"
SQL.QUERY Cursor, DB_Ptr, tbname$,  Columns$

xdone = 0
count = 0
sum = 0

DO
 SQL.NEXT xdone,cursor,v$
 IF !xdone THEN
  count = count + 1
  sum = sum + VAL(v$)
 ENDIF
UNTIL xdone

PRINT "The class grade average is "; FORMAT$("###.##",sum/count)

!
! After we are done with an open database, we
! should close it.

SQL.CLOSE DB_Ptr

END

! In the real world, Ms O'Brien made several
! small BASIC! programs to do each  of the
! tasks of Inserting, Deleting, Updating, etc.
! She used the INPUT command to get the
