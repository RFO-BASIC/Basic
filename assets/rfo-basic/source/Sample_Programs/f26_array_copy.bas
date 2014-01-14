! This program demonstrates
! the array.copy command

! This function prints a numeric
! Array

FN.DEF array_print(a[],msg$)
 PRINT msg$
 ARRAY.LENGTH l,a[]
 FOR i = 1 TO l
  PRINT a[i]
 NEXT i
 FN.RTN 0
FN.END

ARRAY.LOAD n1[],1,2,3,4,5
n=array_print (n1[],"Original array")

ARRAY.COPY n1[],n2[]
n=array_print (n2[], "Straight copy")

ARRAY.COPY  n1[],n3[2]
n=array_print (n3[], "2 extras at end")

ARRAY.COPY n1[],n4[-2]
n=array_print (n4[], "2 extras at front")

ARRAY.COPY n1[2,3], n5[]
n=array_print (n5[], "trim to middle three")

ARRAY.COPY n1[1,2],n6[-1]
n=array_print (n6[], "trim to first 2 and add 1 at start")

! This function prints a String
! Array

FN.DEF array_print$(a$[],msg$)
 PRINT msg$
 ARRAY.LENGTH l,a$[]
 FOR i = 1 TO l
  IF a$[i] = "" THEN PRINT "(empty)" ELSE PRINT a$[i]
 NEXT i
 FN.RTN ""
FN.END

ARRAY.LOAD str$[],"a","b","c","d","e"
n$=array_print$ (str$[],"Original array")

ARRAY.COPY str$[],n2$[]
n$=array_print$ (n2$[], "Straight copy")

ARRAY.COPY  str$[],n3$[2]
n$=array_print$(n3$[], "2 extra at end")

ARRAY.COPY str$[],n4$[-2]
n$=array_print$(n4$[], "2 extra at front")

ARRAY.COPY str$[2,3], n5$[]
n$=array_print$(n5$[], "trim to middle three")

ARRAY.COPY str$[1,2],n6$[-1]
n$=array_print$ (n6$[], "trim to first 2 and add 1 at start")

END
