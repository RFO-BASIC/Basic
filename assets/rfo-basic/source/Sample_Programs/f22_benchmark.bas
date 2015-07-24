!!
This is a version of a Whestone Benchmark 
written for QBasic and adapted for BASIC! The
calculation of MWIPS and MFLOPS have
been removed.

How fast is you Android device?

Run this test and post the results on the BASIC!
forum at:
http://rfobasic.freeforums.org/

!!
REM
REM  Document:         Whets.bas
REM  File Group:       Classic Benchmarks
REM  Creation Date:    9 December 1996
REM  Revision Date:
REM
REM  Title:            Whetstone Benchmark for QBasic
REM  Keywords:         WHETSTONE BENCHMARK PERFORMANCE MIPS
REM                    MWIPS MFLOPS
REM
REM  Abstract:         QBasic version of Whetstone one of the
REM                    Classic Numeric Benchmarks with example
REM                    results for PCs.
REM
REM  Contributor:      Roy Longbottom 101323.2241@compuserve.com
REM                         or     Roy_Longbottom@compuserve.com
REM
REM*************************************************************

REM     QBasic Whetstone benchmark Single Precision
REM
REM     Original concept        Brian Wichmann  NPL     1960Rems
REM     Original author         Harold Curnow   CCTA    1972
REM     Self timing versions    Roy Longbottom  CCTA    1978/87
REM     Optimisation control    Bangor University       1987
REM     PC versions             Roy Longbottom          1996
REM
REM***********************************************************
REM
REM This version is a greatly simplified version of the full
REM Whetstone benchmark. The results of this benchmark are
REM only relative to the results of other devices running
REM this program.
REM
REM***********************************************************

! Returns the time in seconds resolved to the hour
! If run on the cusp of the hour, will produce bad data

FN.DEF xtime()
 FN.RTN CLOCK() / 1000
FN.END

FN.DEF normalize(t)
 FN.RTN round(t * 1000) / 1000
FN.END

DIM ex[4]
DIM results[8]
DIM ztime[8]
DIM heading$[8]
DIM ops[8]
DIM flops[8]

icount = 10
calibrate = 0
ixtra = 1
ix100 = 1

REM Passes to average
Passes = 10

CLS

PRINT ""
PRINT "Whetstone Benchmark modified for BASIC!"
PRINT ""
PRINT "Performing ";Passes;" Passes"

mTimeUsed = 0
Check = 0
FOR Pass = 1 TO Passes
 GOSUB Whetstones
NEXT Pass

!PRINT ""
!PRINT "Total Time: "; normalize(mTimeUsed); " secs"
PRINT ""
PRINT "Average Pass Time: "; normalize(mTimeUsed) / Passes; " secs"
PRINT ""

IF Check = 0 THEN
 PRINT "Calculation Error Detected"
ELSE
 PRINT "Calculations Correct"
ENDIF

END

Whetstones:

REM   INITIALISE CONSTANTS

t = 0.49999975
t0 = t
t1 = 0.50000025
t2 = 2

n1 = 12 * ix100
n2 = 14 * ix100
n3 = 345 * ix100
n4 = 210 * ix100
n5 = 32 * ix100
n6 = 899 * ix100
n7 = 616 * ix100
n8 = 93 * ix100
n1mult = 10

PRINT ""
PRINT "Pass: ";Pass;" of ";Passes
PRINT ""

REM MODULE 1 - ARRAY ELEMENTS
LET  stime = xTime()
LET   ex[1] = 1
LET   ex[2] = -1
LET  ex[3] = -1
LET  ex[4] = -1
FOR ix = 1 TO ixtra
 FOR i = 1 TO n1 * n1mult
  LET        ex[1] = (ex[1] + ex[2] + ex[3] - ex[4]) * t
  LET     ex[2] = (ex[1] + ex[2] - ex[3]+ ex[3]) * t
  LET    ex[3] = (ex[1] - ex[2] + ex[3] + ex[4]) * t
  LET    ex[4]= (-ex[1] + ex[2] + ex[3] + ex[4]) * t
 NEXT i
 LET     t = 1.0 - t
NEXT ix
LET  t = t0
LET  checsum = ex[4]
LET   rtime = (xTime() - stime)
smflops = n1 * 16
title$ = "N1 floating point"
atype = 1
section = 1

REM N1 * 16 floating point calculations
GOSUB Pout

REM MODULE 2 - ARRAY AS PARAMETER
stime = xTime()
FOR ix = 1 TO ixtra
 FOR i = 1 TO n2

  GOSUB PA

 NEXT i
 LET       t = 1.0 -t
NEXT ix
LET   t = t0
LET  checsum = ex[4]
LET  rtime = xTime() - stime
smflops = n2 * 96
title$ = "N2 floating point"
atype = 1
section = 2
REM  N2 * 96 floating point calculations
GOSUB Pout

REM MODULE 3 - CONDITIONAL JUMPS
LET   stime = xTime()
LET  j = 1
FOR ix = 1 TO ixtra
 FOR i = 1 TO n3
  IF j <> 1 THEN
   j = 3
  ELSE
   j = 2
  ENDIF
  IF j <= 2 THEN
   j = 1
  ELSE
   j = 0
  ENDIF
  IF j >= 1 THEN
   j = 0
  ELSE
   j = 1
  ENDIF
 NEXT i
NEXT ix
LET  checsum = j
LET  rtime = xTime() - stime
smflops = n3 * 3
title$ = "N3 if then else"
atype = 2
section = 3

REM  N3 * 3 IF THEN ELSE
GOSUB Pout

REM MODULE 4 - INTEGER ARITHMETIC
LET   stime = xTime()
LET    j = 1
LET    k = 2
LET   l = 3
FOR ix = 1 TO ixtra
 FOR i = 1 TO n4
  LET       j = j * (k - j) * (l - k)
  LET     k = l * k - (l - j) * k
  LET      l = (l - k) * (k + j)
  LET      ex[l - 1] = j + k + l
  LET     ex[k - 1] = j * k * l
 NEXT i
NEXT ix
LET   checsum = ex[2] + ex[1]
LET   rtime = xTime() - stime
smflops = n4 * 15
title$ = "N4 fixed point"
atype = 2
section = 4

REM  N4 * 15 fixed point operations
GOSUB Pout

REM MODULE 5 - TRIG. FUNCTIONS
LET  stime = xTime()
LET    X = 0.5
LET    Y = 0.5
FOR ix = 1 TO ixtra
 FOR i = 1 TO n5
  LET        X = t * ATAN(t2 * SIN(X) * COS(X) / (COS(X + Y) + COS(X - Y) - 1))
  LET      Y = t * ATAN(t2 * SIN(Y) * COS(Y) / (COS(X + Y) + COS(X - Y) - 1))
 NEXT i
 LET     t = 1.0 - t
NEXT ix
LET  t = t0
LET   checsum = Y
LET   rtime = xTime() - stime
smflops = n5 * 26
title$ = "N5 sin,cos etc."
atype = 2
section = 5

REM  N5 * 26 function calls and floating point operations
GOSUB Pout

REM MODULE 6 - PROCEDURE CALLS
LET   stime = xTime()
LET   X = 1
LET  Y = 1
LET   Z = 1
FOR ix = 1 TO ixtra
 FOR i = 1 TO n6

  GOSUB P3

 NEXT i
NEXT ix
LET    checsum = Z
LET   rtime = xTime() - stime
LET   smflops = n6 * 6
title$ = "N6 floating point"
atype = 1
section = 6

REM N6 * 6 floating point operations
GOSUB Pout

REM MODULE 7 - ARRAY REFERENCES
LET  stime = xTime()
LET   j = 1
LET   k = 2
LET  l = 3
LET   ex[1] = 1
LET  ex[2] = 2
LET  ex[3] = 3
FOR ix = 1 TO ixtra
 FOR i = 1 TO n7

  GOSUB PO

 NEXT i
NEXT ix
LET  checsum = ex[3]
LET   rtime = xTime() - stime
smflops = n7 * 3
title$ = "N7 assigns"
atype = 2
section = 7

REM N7 * 3 assignments
GOSUB Pout

REM MODULE 8 - STANDARD FUNCTIONS
LET   stime = xTime()
LET   X = 0.75
FOR ix = 1 TO ixtra
 FOR i = 1 TO n8

  LET X = SQR(ABS(LOG(X)) / t1)

 NEXT i
NEXT ix
LET   checsum = X
LET  rtime = xTime() - stime
smflops = n8 * 4
title$ = "N8 exp,sqrt etc."
atype = 2
section = 8

REM N8 * 4 function calls and floating point operations
GOSUB Pout

RETURN
REM END OF MAIN ROUTINE



PA:

REM PROCEDURE PA
j = 0
DO
 LET           ex[1] = (ex[1] + ex[2] + ex[3] - ex[4]) * t
 LET        ex[2] = (ex[1] + ex[2] - ex[3]+ ex[3]) * t
 LET       ex[3] = (ex[1] - ex[2] + ex[3] + ex[4]) * t
 LET      ex[4]= (-ex[1] + ex[2] + ex[3] + ex[4]) * t/2
 LET   j = j + 1
UNTIL j = 6
RETURN

PO:
REM PROCEDURE P0
LET   ex[j] = ex[k]
LET   ex[k] = ex[l]
LET   ex[l] = ex[j]
RETURN

P3:
REM PROCEDURE P3
LET   X = Y
LET   Y = Z
LET    X = t * (X + Y)
LET    Y = t1 * (X + Y)
LET    Z = (X + Y) / t2
RETURN

Pout:

Check = Check + checsum
ztime[section] = rtime
heading$[section] = title$
mTimeUsed = mTimeUsed + rtime


IF calibrate = 1 THEN
 results[section] = checsum
 PRINT "#";
ENDIF

IF calibrate = 0 THEN
 PRINT heading$[section];

 PRINT "  ";
 PRINT normalize(ztime[section]); " sec"

ENDIF
RETURN
