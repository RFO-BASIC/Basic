!!
This program demonstrates the Accelerometer Sensors.

It displays the magnitude and direction of the 
accelerometer data in the X and Y axis as Red 
and Green lines.

The magnitude and direction of the X and Y 
vectors are then added to generate the 
gravity vector blue line.

The blue line will always point to the center 
of the Earth. 

If you hold the device horizonatally the three 
displayed vectors will disappear.

!!

! Open the acclerometer sensor
SENSORS.OPEN 1

! Open Graphics
GR.OPEN 255,0,0,0
GR.ORIENTATION 0

! Get screen size paramters
GR.SCREEN w,h

! Calculate screen center x, y
cx = w/2
cy = h/2

! The main program loop
DO

 !Read the acclerometer
 SENSORS.READ 1,y,x,z

 !Amplify the magnitudes
 x = x* (cy/12)
 y = y*(cy/12)

 ! Draw the X Axis in Red
 GR.COLOR 255,255,0,0,1
 GR.LINE q, cx, cy, cx, cy+ y

 ! Daw the Y Axis in Green
 GR.COLOR 255,0,255,0,1
 GR.LINE r, cx, cy, cx+ x, cy

 ! Calculate the gravity vector
 m = SQR(x*x + y*y)
 IF m=0 THEN m = 0.0001
 angle = TODEGREES(ACOS(x/m))
 IF y < 0 THEN angle = - angle

 ! Draw the gravity vector
 GR.COLOR 255,0,0,255,1
 GR.ROTATE.START angle, cx, cy
 GR.LINE s, cx, cy, c x+m, cy
 GR.ROTATE.END

 ! Now show what we have drawn
 GR.RENDER

 ! Pause for a moment
 PAUSE 5

 ! Clear the screen and repeat
 GR.CLS

UNTIL 0

! If the user hits BACK, he will not see
! a Graphics not opened message
! because OnError will intercept

ONERROR:
END

