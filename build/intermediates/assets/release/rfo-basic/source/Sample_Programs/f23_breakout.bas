!!
This game of Breakout is intended
as an example of collision
detection in Graphics mode.

 ***** Instructions *****

The ball will only move while
touching the screen. Lift your
finger from the screen and the 
ball will stop. Touch the screen
again and the ball will move.

You can put some "English" on
the ball depending on the speed
and direction of the paddle strike

After the ball stops flashing at
game end, you can start a new
game. Just tap the screen.

The score is the number of bricks
remaining after breaking out. The
theoretical maximum score is 21.

You can change the speed of the
action by changing the "friction"
value just below this comment.
Larger friction values result
in slower action.

!!

friction = 10
GR.OPEN 255, 0,0,0
GR.ORIENTATION 0
GR.SCREEN sw, xsh
sh = xsh - xsh/10
space = 2
last_score = 0
high_score = 0
lives = 0

GOSUB instructions

! Game restarts here

restart:
english = 0
lives = lives + 1

! Draw bounding lines
! Bounding lines are filled rectangles

line_width = CEIL(sh/200)

! Make top line black
! It will not be seen
! but will report collisions

GR.COLOR 255, 0, 0, 0, 1
! Top Line
GR.RECT TL, 0, 0, sw, line_width

! Make what follows white

GR.COLOR 255, 255, 255, 255, 1

! Display the score information

GR.TEXT.SIZE xsh/10 - 6
GR.TEXT.ALIGN 1
GR.TEXT.DRAW text, 0, xsh - 8, " Lives:" + FORMAT$("%%",lives) + "  Last Score:" + FORMAT$("%%",last_score) + "  High Score:" + FORMAT$("%%",high_score)

! Left line
GR.RECT  LL, 0, 0, line_width, sh
! Right Line
GR.RECT RL, sw - 2*line_width, 0, sw, sh
! Bottom Line
GR.RECT  BL, 0, sh - line_width, sw, sh

! Draw Paddle

paddle_count = 4
paddle_height = sh/20
pb = sh - line_width - space
pt = pb - paddle_height
pl = line_width + space
pr = pl + sw/paddle_count - 2*line_width - 2*space
paddle_width = pr - pl
paddle_left_limit = pl
paddle_right_limit = sw - paddle_width - line_width - space

! Adjust paddle postion to center

pl = sw/2 - paddle_width/2
pr = pl + paddle_width
GR.RECT Paddle, pl, pt, pr, pb

! Draw the Ball

! If this is a game restart
! position the ball where
! the user touched the screen

ball_x = RND()*sw
ball_y = sh/2

! The _xx values are the
! ball movement increments

ball_xx = -15
ball_yy = 15

ball_radius = paddle_height
GR.CIRCLE Ball, ball_x, ball_y, ball_radius

! Draw the target bricks
! Each row of bricks will have
! different colors

T_count = 8
T_width = sw/T_count
T_width = T_width - 2*line_width + space
T_start_left = 2*line_width + space
T_height = Paddle_height
T_start_top = T_height

! Top Row

ttl = T_start_left
tt = T_start_top
tr = ttl + T_width
tb = tt + T_height
GR.COLOR 255,255,0,0,1
DIM T1[T_count]
FOR i = 1 TO T_count
 GR.RECT T1[i], ttl, tt, tr, tb
 ttl = tr + space
 tr = ttl + T_width
NEXT i

! Middle Row

tt = tt + T_height + space
tb = tt + T_height
ttl = T_start_left
tr = ttl + T_width
DIM t2[T_count]
GR.COLOR 255,0,255,0,1
FOR i = 1 TO T_count
 GR.RECT T2[i], ttl, tt, tr, tb
 ttl = tr + space
 tr = ttl + T_width
NEXT i

! Bottom Row

tt = tt + T_height + space
tb = tt + T_height
ttl = T_start_left
tr = ttl + T_width
DIM T3[T_count]
GR.COLOR 255,0,0,255,1
FOR i = 1 TO T_count
 GR.RECT T3[i], ttl, tt, tr, tb
 ttl = tr + space
 tr = ttl + T_width
NEXT i

! The lower limit is where
! we start looking for
! collisions with bricks

T_lower_limit = tb + T_height

! Finally, render the screen

GR.RENDER

! Some final variables to set

GR.COLOR 255,255,255,255,1
game_end = 0
hits = 0

! ***** Main Game Loop *****

DO

 GR.TOUCH touched, tx1, ty1
 WHILE touched & (game_end = 0)

  ! Move the paddle

  GR.TOUCH touched, tx2, ty2
  delta_tx = tx2 -tx1
  tx1 = tx2
  npl = pl + delta_tx
  IF (npl <= paddle_left_limit) | npl >= (paddle_right_limit) THEN npl = pl
  GR.MODIFY Paddle, "left", npl
  GR.MODIFY Paddle, "right", npl + paddle_width
  pl = npl

  ! Move the ball

  ball_x = ball_x + ball_xx + english
  IF ball_x > sw THEN ball_x = sw - ball_radius
  IF ball_x <  0 THEN ball_x = ball_radius
  ball_y = ball_y + ball_yy
  GR.HIDE Ball
  GR.CIRCLE Ball, ball_x, ball_y, ball_radius

  ! Check for collision with Paddle

  IF GR_COLLISION(Ball, Paddle)
   ball_yy = -ball_yy
   IF ABS(delta_tx) >10
    english = delta_tx/2
   ENDIF
  ENDIF

  ! Check for collisions with the
  ! boundry lines

  IF GR_COLLISION(Ball, LL)
   ball_xx = ABS(ball_xx)
   english = 0
  ENDIF

  IF GR_COLLISION(Ball, RL)
   ball_xx = - ABS(ball_xx)
   english = 0
  ENDIF

  IF GR_COLLISION(Ball, BL) THEN game_end = 1
  IF GR_COLLISION(Ball, TL) THEN game_end = 2

  ! Check for collisions with the bricks
  ! Checks will only be made if ball
  ! is past the threshold

  IF (game_end = 0) & (ball_yy < 0) & (ball_y < T_lower_limit)
   hit = 0

   ! Bottom row

   FOR i = 1 TO T_count
    IF GR_COLLISION(Ball, T3[i])
     GR.HIDE T3[i]
     ball_yy = -ball_yy
     i = T_count + 1
     hit = 1
     hits = hits + 1
     english = 0
    ENDIF
   NEXT i

   ! Middle Row
   ! Will only be checked if no hit
   ! on bottom row

   IF (hit = 0)
    FOR i = 1 TO T_count
     IF GR_COLLISION(Ball, T2[i])
      GR.HIDE T2[i]
      ball_yy = -ball_yy
      i = T_count + 1
      hit = 2
      hits = hits + 1
      english = 0
     ENDIF
    NEXT i
   ENDIF

   ! Top row
   ! Will only be checked if no hit
   ! on bottom two rows

   IF (hit =  0)
    FOR i = 1 TO T_count
     IF GR_COLLISION(Ball, T1[i])
      GR.HIDE T1[i]
      ball_yy = -ball_yy
      i = T_count + 1
      hit = 2
      hits = hits + 1
      english = 0
     ENDIF
    NEXT i
   ENDIF

  ENDIF

  ! Show where we are at and
  ! then pause by the friction
  ! amount.

  GR.RENDER
  PAUSE friction

 REPEAT
UNTIL game_end

! End of game action
! Keep score

IF game_end = 1 THEN hits = 24
last_score = 24 - hits
IF last_score > high_score THEN high_score = last_score

GR.HIDE text
GR.TEXT.DRAW z, 0, xsh - 8, " Lives:" + FORMAT$("%%",lives) + "  Last Score:" + FORMAT$("%%",last_score) + "  High Score:" + FORMAT$("%%",high_score)

! Flash the ball

FOR i = 1 TO 3
 GR.HIDE Ball
 GR.RENDER
 PAUSE 125
 GR.SHOW Ball
 GR.RENDER
 PAUSE 125
NEXT i

GR.TEXT.DRAW z, sw/12, sh/2, "Tap the screen to start new game."
GR.RENDER

! Wait for the new game
! touch and release signal
! The touch point will be
! the ball start location
! for the new game

DO
 GR.TOUCH touched,nx,ny
UNTIL touched
DO
 GR.TOUCH t,b,b
UNTIL !t

! Prep for new game

GR.CLS
UNDIM T1[]
UNDIM T2[]
UNDIM T3[]
GOTO restart

! Catch graphics not
! open error message
! and end

!onerror:
END

instructions:

DIM line$[20]
i = 1
line$[i]= "                *** INSTRUCTIONS *** "
i = i + 1
line$[i] = " "
i = i + 1
line$[i]= "The ball will move only while touching the screen."
i = i+1
line$[i] = " "
i = i + 1
line$[i]= "The speed and direction of the paddle strike affects the"
i = i+1
line$[i]= "movment of the ball."
i = i+1
line$[i] = " "
i = i + 1
line$[i]= "The game score is the number of bricks remaining after"
i = i+1
line$[i]= "escaping."
i = i + 1
line$[i] = " "
i = i + 1
line$[i]= "Start the game by tapping the screen now."
line_count = i

it_size = sh/15
it_space = sh/45
GR.COLOR 255, 255, 255, 255, 1
GR.TEXT.ALIGN 1
GR.TEXT.SIZE it_size
y = it_space + it_size
x = 0

FOR k = 1 TO line_count
 GR.TEXT.DRAW z, x , y,line$[k]
 y = y + it_space + it_size
NEXT i

GR.RENDER

DO
 GR.TOUCH touched,nx,ny
UNTIL touched
DO
 GR.TOUCH t,b,b
UNTIL !t

GR.CLS
RETURN

ONERROR:
END
