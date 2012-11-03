! Demo TCP Client by getting
! current time from Time Server
! in Mt. View, CA

! Other time servers can be found at
! http://tf.nist.gov/tf-cgi/servers.cgi

SOCKET.CLIENT.CONNECT "207.200.81.113", 13

! Wait for first line (a blank line)
! or time out after 10 seconds

maxclock = CLOCK() + 10000
DO
 SOCKET.CLIENT.READ.READY flag
 IF CLOCK() > maxclock
  PRINT "Read time out"
  END
 ENDIF
UNTIL flag

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
