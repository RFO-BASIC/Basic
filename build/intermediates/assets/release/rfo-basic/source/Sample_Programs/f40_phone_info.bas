!! phone_info.bas
This program demonstrates
DEVICE and PHONE.INFO
used to get telephone
network information.
!!
DEVICE db
BUNDLE.CONTAIN db, "PhoneType", response
IF response THEN BUNDLE.GET db, "PhoneType", type$
IF type$ = "" | type$ = "None"
  DIALOG.MESSAGE "Warning" ,"Your device is not a phone.\nDo you wish to continue?", reply, "YES", "NO"
  IF reply <> 1 THEN stop = 1
ENDIF
IF stop THEN END

DIALOG.MESSAGE ,"Do you want to try to get signal strength data?", reply, "YES", "NO"
IF reply = 1
  PHONE.RCV.INIT
  PAUSE 1000  % give signal strength listener a chance
ENDIF

GetInfo:
PHONE.INFO pib

BUNDLE.KEYS pib, keyList
LIST.SIZE keyList, nKeys

FOR i = 1 TO nKeys
  LIST.GET keyList, i, key$
  PRINT USING$(, "%-12s:  ", key$);
  BUNDLE.TYPE pib, key$, type$
  IF type$ = "N"
    BUNDLE.GET pib, key$, n
    PRINT n
  ELSE
    BUNDLE.GET pib, key$, s$
    print s$
  ENDIF
NEXT i

PRINT
PRINT "Tap any line to refresh"
PRINT "or the BACK key to quit"
PRINT

DO
PAUSE 500
UNTIL rpt | xit

IF xit THEN END
rpt = 0
GOTO GetInfo

OnConsoleTouch:
rpt = 1
ConsoleTouch.Resume

OnBackKey:
xit = 1
Back.Resume