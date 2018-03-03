!!
This file contains executable examples
of PRINT and FORMAT operations. 
Examine the code  and run then run 
this program. 
!!

! PRINT

PRINT "Comma seperated example"
a$ = "ABC"
b = 123
c$ = "DEF"
PRINT a$, b, c$, "GHI"

PRINT "Semicolon seperated example"
a1$ = "The quick "
a2$ = "brown fox "
PRINT a1$;a2$

!print "Semicolon ata end of line example"
a3$ = "jumped over "
PRINT a1$;a2$;
PRINT a3$

! FORMAT

PRINT "Format Example 1"
PRINT FORMAT$("%%%%%.###", 123.456)
PRINT FORMAT$("%%%%%.###", -123.456)
PRINT FORMAT$("$%%%%%.###", 123.456)
PRINT FORMAT$("$%%%%%.##", -123.456)
PRINT FORMAT$("$%%%%%", -123.456)
PRINT FORMAT$("#####.###", 123.456)
PRINT FORMAT$("#####.###", -123.456)
PRINT FORMAT$("$#####.###", 123.456)
PRINT FORMAT$("$#####.##", -123.456)
PRINT FORMAT$("$#####", -123.456)

PRINT "Format Example 2"
PRINT FORMAT$("$##,###,###,###", 123)
PRINT FORMAT$("$##,###,###,###", 1234)
PRINT FORMAT$("$##,###,###,###", 1234)
PRINT FORMAT$("$##,###,###,###", 1234567)
PRINT FORMAT$("$##,###,###,###", 12345678)


PRINT "Format Example 3"
PRINT FORMAT$("@ #/###/###/###", 1234567890)
PRINT FORMAT$("@ ###/###", -1234567890)

END

