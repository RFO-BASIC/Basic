!!

******  READ!  *******

Sample program demonstrating
TCP/IP sockets. The program 
has two parts: a server side
and a client side. Both sides
need to be running on different
Android devices.

The Server will wait for a Client
to connect to it. When the
connection is made, the Server
will wait for a message from
the Client. Once the message
has been recieved, the Server
will send a message back
to the Client.

Some devices/networks do not
allow smart phone servers onto
their data network.

In that case, the server must be
inside a LAN.

LAN = Local Area Network (WiFi)
WAN = Wide Area Network (Internet)

If the Client is on the same LAN
as the Server, the Client can 
connect directly to the Server
using the Server's LAN IP.

If the Client is outside of the
Server LAN then the Client must
must connect using the Server's
WAN IP.

It will be necessary to program
the Server's LAN router to pass
a specific port through from the
WAN to Server's LAN IP

!!

ARRAY.LOAD type$[], "Server", "Client"
msg$ = "Select TCP/IP socket type"
SELECT type, type$[], msg$
IF type = 0
 "Thanks for playing"
 END
ELSEIF type = 2
 GOTO doClient
ENDIF

!***********  Server Demo  **************

INPUT "Enter the port number", port, 1080

SOCKET.MYIP ip$
PRINT "LAN IP: " + ip$
GRABURL ip$, "http://icanhazip.com"
PRINT "WAN IP: " + ip$

! Create the server on the specified port
SOCKET.SERVER.CREATE port

newConnection:

! Connect to the next Client
! and print the Client IP
PRINT "Waiting for client connect"
SOCKET.SERVER.CONNECT 0
DO
 SOCKET.SERVER.STATUS st
UNTIL st = 3

SOCKET.SERVER.CLIENT.IP ip$
PRINT "Connected to ";ip$

! Connected to a Client
! Wait for Client to send a message
! or time out after 10 seconds

maxclock = CLOCK() + 10000
DO
 SOCKET.SERVER.READ.READY flag
 IF CLOCK() > maxclock
  PRINT "Read time out"
  END
 ENDIF
UNTIL flag

! Message received. Read it.
! Print it

SOCKET.SERVER.READ.LINE line$
PRINT line$

! Send a message back to client

SOCKET.SERVER.WRITE.LINE "Server to client message"

! Finished this protocol
! Disconnect from Client

SOCKET.SERVER.DISCONNECT
PRINT "Disconnected from client"

! Loop to get the next Client

GOTO newConnection

! ****************** Client Demo ****************

DOClient:

 INPUT "Enter the connect-to IP", ip$
 INPUT "Enter the port number", port, 1080

 ! Connect to the specified IP on the
 ! specified Port

 clientAgain:

 SOCKET.CLIENT.CONNECT ip$, port
 PRINT "Connected"

 ! When the connection is established,
 ! send the server a message

 SOCKET.CLIENT.WRITE.LINE "Client to server message"

 ! and then wait for Server to respond
 ! or time out after 10 seconds

 maxclock = CLOCK() + 10000
 DO
  SOCKET.CLIENT.READ.READY flag
  IF CLOCK() > maxclock
   PRINT "Read time out"
   END
  ENDIF
 UNTIL flag

 ! Server has sent message.
 ! Read it. Print it.

 SOCKET.CLIENT.READ.LINE line$
 PRINT line$

 ! Close the client

 SOCKET.CLIENT.CLOSE
 PRINT "Disconnected from server"

 INPUT "Connect again? Y or N", again$, "Y"
 IF again$ = "Y" THEN GOTO clientAgain

 END
