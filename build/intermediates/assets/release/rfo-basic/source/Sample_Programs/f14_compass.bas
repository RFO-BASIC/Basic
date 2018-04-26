! This is example program demonstrates the
! use of the "Orientation" sensor to make a
! simple Compass.

! Open the graphics screen with a
! White background and..
! Initialize the draw color to red

GR.OPEN 255,255,255,255
GR.ORIENTATION 0  % force landscape mode
GR.SCREEN w, h
GR.COLOR 255,255,0,0,1

cx = w/2
cy = h/2
r =cy - cy * 0.30

! Initialize the text parameters

GR.TEXT.SIZE h/24
GR.TEXT.ALIGN 2

! Draw the parameter value displays

z = TORADIANS(45)
x = COS(z) * r + cx
y = SIN(z) * r +cy
GR.TEXT.DRAW tn, x, y,"A000"    %Azimuth
z = TORADIANS(135)
x = COS(z) * r + cx
y = SIN(z) * r +cy
GR.TEXT.DRAW tp, x, y,"P000"   % Pitch
z = TORADIANS(225)
x = COS(z) * r + cx
y = SIN(z) * r +cy
GR.TEXT.DRAW tr, x, y, "R000"    %Roll

! Draw the compass labels

GR.TEXT.SIZE h/16
sp = 25
GR.TEXT.DRAW k, cx + r + sp, cy, "N"
GR.TEXT.DRAW k, cx, cy - r - sp, "W"
GR.TEXT.DRAW k, cx - r - sp ,cy, "S"
GR.TEXT.DRAW k, cx, cy + r  + sp, "E"
GR.RENDER   % Show what we have drawn so far

! Now pre-draw 360 degree lines. This will be
! hidden and then shown one at a time based
! upon the Azimuth values returned

DIM a[360]
FOR i = 1 TO 360
 GR.ROTATE.START i, cx, cy
 GR.LINE a[i], cx, cy, cx+r, cy
 GR.ROTATE.END
 GR.HIDE a[i]
 % It takes a while to draw these lines, so
 % give the show some progress
 IF i = 240 THEN POPUP "Stand by",120,240,0
NEXT i

! Now open the Orientation Sensor which
! is Type = 3

SENSORS.OPEN 3

! Set  my local magnetic declination from
! true North

declination = 14

! In an infinite loop,  read the sensor
! and show the compass line for
! the (adjusted) Azimuth

DO
 SENSORS.READ 3, az, pitch, roll
 az = ROUND(az)  % Round to he nearest whole number
 az=az + declination  % Add in the declination
 IF az = 0 THEN az = 360
 az1 = az  % Save the Aziumth

 % The reported Azimuth changes with the
 % orientation of the device. This code chunk
 % attempts to adjust for this.

 IF pitch < 0 THEN az = az + roll ELSE az = az - roll - 180
 IF az > 360 THEN az = 360 - az
 IF az < 1 THEN az = 360 + az

 GR.SHOW a[az] % Show the Azumth Line

 % Display the sensor's parameters

 az1$ = FORMAT$("A%%%", az1)
 pitch$ = FORMAT$("P%%%", pitch)
 roll$ = FORMAT$("R%%%", roll)
 GR.MODIFY tn, "text", az1$
 GR.MODIFY tp, "text", pitch$
 GR.MODIFY tr, "text", roll$

 % Show the changes made, pause
 % and then hide the Azimuth line

 GR.RENDER
 PAUSE 100
 GR.HIDE a[az]

UNTIL 0  % Loop forever

ONERROR:
END
