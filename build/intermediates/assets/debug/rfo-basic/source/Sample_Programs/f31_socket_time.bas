! Demo TCP Client by getting
! current time from a Time Server
! of the U.S. National Institute
! of Standards and Technology (NIST)

! DO NOT ATTEMPT TO ACCESS
! THIS SERVICE MORE OFTEN
! THAN ONCE EVERY 4 SECONDS!

! Other time servers can be found at
! http://tf.nist.gov/tf-cgi/servers.cgi

SOCKET.CLIENT.CONNECT "time.nist.gov", 13, 0

! Wait for connection
! or time out after 10 seconds

maxclock = CLOCK() + 10000
DO
 SOCKET.CLIENT.STATUS cs
 IF cs = 2         % still trying to connect
  IF CLOCK() > maxclock
   PRINT "Connect time out"
   die = 1
  ELSE
   PRINT "Connecting..."
   PAUSE 500
  ENDIF
 ENDIF
UNTIL cs <> 2 | die
IF cs <> 3
 PRINT "Could not connect"
 die = 1
ENDIF
IF die THEN END

! Wait for first line (a blank line)
! or time out after 10 seconds

maxclock = CLOCK() + 10000
DO
 SOCKET.CLIENT.READ.READY ready
 IF CLOCK() > maxclock
  PRINT "Read time out"
  die = 1
 ENDIF
UNTIL ready | die
IF die THEN END

! Read the blank line and print it

SOCKET.CLIENT.READ.LINE line$
PRINT line$

! Wait for the time data line
! Read it. Print it.

SOCKET.CLIENT.READ.LINE line$
PRINT line$

! Protocol done. Close Client

SOCKET.CLIENT.CLOSE

END
