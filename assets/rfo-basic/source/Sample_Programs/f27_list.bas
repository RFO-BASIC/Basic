!!
This program demonstrates
the use of the LIST commands
!!

! A function to print any
! list

FN.DEF lprint(list, msg$)
 PRINT msg$

 LIST.SIZE list, size
 IF size = 0
  PRINT "Empty list"
  PRINT " "
  FN.RTN 0
 ENDIF

 LIST.TYPE list,type$
 IF type$ = "N"
  FOR i = 1 TO size
   LIST.GET list, i, num
   PRINT num
  NEXT i
 ELSE
  FOR i = 1 TO size
   LIST.GET list, i, str$
   PRINT str$
  NEXT i
 ENDIF
 PRINT " "
 FN.RTN size
FN.END

LIST.CREATE N, num
x = lprint(num, "Newly created empty list")

! Load an array and
! add the array to
! empty list

ARRAY.LOAD n[], 1, 2, 3, 4
LIST.ADD.ARRAY num,n[]
x = lprint(num, "Array added to empty list")

! Add an element

LIST.ADD num, 5
x = lprint(num, "Element 5 added to list")

! Remove and element from the list

LIST.REMOVE num, 2
x = lprint(num, "Element 2 removed from list")

! Insert a new element 2

LIST.INSERT num, 2, 22
x = lprint(num, "New element inserted at 2")

! Replace an element

LIST.REPLACE num,2, 222
x = lprint(num, "Element 2 replaced")

! Add a list

LIST.ADD.LIST num, num
x = lprint(num, "List added to itself")

! List copied to new array

LIST.TOARRAY num, array[]
ARRAY.LENGTH length, array[]
PRINT "List copied to new array"
FOR i = 1 TO length
 PRINT array[i]
NEXT i
PRINT " "


! Clear the list

LIST.CLEAR num
x = lprint(num, "List cleared")

! All of the operations
! can be done with strings
!

ARRAY.LOAD s$[], "a", "b", "c"
LIST.CREATE S, str
LIST.ADD.ARRAY str, s$[]
x = lprint (str, "A string list")

END

