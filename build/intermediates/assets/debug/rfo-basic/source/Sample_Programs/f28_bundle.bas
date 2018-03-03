!!
This program illustrates
the various Bundle commands
!!

! A function to print a
! any Bundle

FN.DEF bprint(bundle, msg$)
 PRINT msg$

 ! get the list of the keys
 ! in this bundle
 ! and the number of keys
 ! if the numbers of keys
 ! is zero then the bundle
 ! has no keys

 BUNDLE.KEYS bundle, list
 LIST.SIZE list, size
 IF size = 0
  PRINT "Empty bundle"
  PRINT " "
  FN.RTN 0
 ENDIF

 ! For each key,
 ! get the key type
 ! and then get key's
 ! value base upon
 ! the type

 FOR i = 1 TO size
  LIST.GET list, i, key$
  BUNDLE.TYPE bundle, key$, type$
  IF type$ = "S"
   BUNDLE.GET bundle, key$, value$
   PRINT key$, value$
  ELSE
   BUNDLE.GET bundle, key$, value
   PRINT key$, value
  ENDIF
 NEXT i

 PRINT " "
 FN.RTN 1
FN.END

! Create a bundle and print it

BUNDLE.CREATE b1
x = bprint(b1, "A new, empty bundle")

! put some keys and values

BUNDLE.PUT b1, "First_Name", "Frank"
BUNDLE.PUT b1, "Last_Name", "Smith"
BUNDLE.PUT b1, "Age", 44
BUNDLE.PUT b1, "Employee_Number", 163
x = bprint(b1, "B1 with keys and values")

! Change the value of the Age key

BUNDLE.PUT b1, "Age", 45
x = bprint(b1, "B1 with Age Value changed")

! Remove a key

BUNDLE.REMOVE b1, "Age"
x = bprint(b1, "B1 with Age removed")

END
