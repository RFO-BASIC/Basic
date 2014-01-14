!!
The program implements a
Superuser terminal.

Your device must be rooted
to successfully run this
program.

!!

! Get Superuser permission
SU.OPEN

! Main loop
loop:

! get the command from the user
TGET r$, "cmd: "

! if cmd is "exit"
IF r$ = "exit"
 SU.CLOSE
 PRINT "Exited"
 END
ENDIF

! write the command
SU.WRITE r$

! Give system time to respond
PAUSE 500

! check for a response
SU.READ.READY ready

! if no input, do next command
IF !ready THEN GOTO loop

! read responses
DO
 SU.READ.LINE l$
 PRINT l$
 SU.READ.READY ready
UNTIL !ready

! Go for the next command
GOTO loop

