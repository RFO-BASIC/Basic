! Demonstrates the GPS commands

GPS.OPEN
GR.OPEN 255, 255, 255, 255
GR.ORIENTATION 0
GR.SCREEN w, h

sp = h/7
pad = 0.25 * sp
x = 20

loop:

GR.TEXT.SIZE sp - 2*pad
GR.COLOR 255,0,0,0,1

y  = 0*sp + sp - pad
GPS.PROVIDER provider$
GR.TEXT.DRAW  p, x , y , "Provider: " +  provider$

y  = 1*sp + sp - pad
GPS.ACCURACY accuracy
GR.TEXT.DRAW p, x,y, "Accuracy: " + FORMAT$("##", accuracy)

y  = 2*sp + sp - pad
GPS.LATITUDE latitude
GR.TEXT.DRAW p, x,y, "Latitude: " + FORMAT$("##%.#####", latitude)

y  = 3*sp + sp - pad
GPS.LONGITUDE longitude
GR.TEXT.DRAW p, x, y, "Longitude: " + FORMAT$("##%.#####", longitude)

y  = 4*sp + sp - pad
GPS.ALTITUDE altitude
GR.TEXT.DRAW p, x, y, "Altitude: " + FORMAT$("#####", altitude)

y  = 5*sp + sp - pad
GPS.BEARING bearing
GR.TEXT.DRAW p, x, y, "Bearing: " + FORMAT$("##%.##", bearing)

y  = 6*sp + sp - pad
GPS.SPEED speed
GR.TEXT.DRAW p, x,y, "Speed: " + FORMAT$("##%.##", speed)

GR.RENDER
PAUSE 5000
GR.CLS

GOTO loop

ONERROR:
END
