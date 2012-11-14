!!
variables and functions with a leading underscore  are no longer fully supported, as it is seen as a collect next line comment. but if you really want to use them then don't have a space before the underscore
!!
tv$ = ##$
FN.DEF x(a _ %comment here supported or
        ,b _this is a comment as well, and
        ,c _ you will have to manually indent
        ) % these lines

 % collect line does not work between quotes %trying to do so will cause an error.

 s$ = " this is a test line _with underscore _ to test formatting"

 PRINT s$
 ++a              % is: a = 1 + a
 a++              % is: a = a + 1
 a+=2             % is: a = a + 2
 PRINT _
       a
 a*=5             % is: a = a * 5
 PRINT a
 a/=2             % is: a = a / 2
 PRINT a
 a-=10+3-6*2      % is: a = a - 10+3-6*2
 PRINT a

 FN.RTN a+b+c
FN.END

PRINT x(1,2,3)

DO
 --c               % is: c = 1 - c
 PRINT c ;"  "; _
       i
 ++i
UNTIL i > 4

fun = 1
IF fun THEN
 --fun              % is: f = 1 - f
 PRINT f
 IF fun=0 THEN f--  % is: f = f - 1
ENDIF
PRINT f

p$ = "fun stuff with -=+=/=*=*=/=-=-=+="
PRINT p$


s$ = "t"+ _
"56"
PRINT s$

t = 1 _
+ 2

PRINT t

FOR i = 1 TO 4

NEXT


IF LEN(tv$)<3
 RUN "linecollecting_shorthandtest.bas",tv$+"1"
ENDIF
