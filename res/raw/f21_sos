!!
This program demonstrates the vibrate
command by vibrating out SOS in
Morse Code
!!

! Set the variables

xdot = 200 % Length of a Morse Code "dot" in milliseconds
dash = 500 % Length of a Morse Code "dash" in milliseconds
sg = 200 %Length of Gap Between dots/dashes
mg = 500 %Length of Gap Between Letters
lg = 1000 % Length of Gap Between Words

! Load the Pattern[] Array with the SOS
! no initial pause
! dot, dot, dot
! dash, dash, dash
! dot, dot, dot

ARRAY.LOAD Pattern[], 1~
xdot, sg, xdot, sg, xdot, mg~
dash,sg,dash,sg,dash,mg~
xdot, sg, xdot, sg, xdot

VIBRATE Pattern[], -1
PAUSE 5500

END
