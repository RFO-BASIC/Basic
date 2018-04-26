! This program is designed to demonstrate
! the gr.rotate , gr.modify, audio and
! popup commands in the context of a
! graphics animation
!
! Open the Graphics screen with a background
! color of White, force landscape mode and
! get the size of the screen.

GR.OPEN 255, 255, 255, 255
GR.ORIENTATION 0 % force landscape mode
GR.SCREEN w,h

! Set up to draw objects with the color
! as filled Red

GR.COLOR 255, 255, 0, 0, 1

! Draw a some rotated text

GR.TEXT.ALIGN 1      % align left
GR.TEXT.SIZE h/17
text_up_x = w/5	 % Up moving text start x
text_down_x = w/5   % Down moving text start x

GR.ROTATE.START -45, text_up_x, 180
GR.TEXT.DRAW p_up_text, text_up_x, 180, "Cartman loves BASIC!"
GR.ROTATE.END % rotate.end signals end of this rotate
GR.RENDER  % Render what we have done so far

! Draw and rotate a bitmap object (Cartman.png)

! First draw a box for Cartman to land on.
! The rectangle will be filled due to the
! fill parameter of the gr.color command
! issued earlier.
bleft = w/4
btop = h-55
GR.RECT q, bleft, btop, bleft+68, h

! Load an image as a bitmap positioned
! on the box

! Start by loading the image bitmap
! from the Cartman.png from the directory
! "/sdcard/rfo-basic/data/"

GR.BITMAP.LOAD p_bm_obj, "Cartman.png"
! Cartman is 48 x 48 Pixels

DIM p_bitmap[10] % The animation will have ten steps

! Draw Cartman on the box
! the box is 68 pixels wide and
! 55 pixels hight

index = 1   % This is the Cartman on box index
GR.BITMAP.DRAW p_bitmap[index], p_bm_obj, bleft + 10, btop - 48
GR.RENDER                % Render it now
PAUSE 500                % Pause for 1/2 second
GR.HIDE p_bitmap[index]  % and then hide it

index = 2  % Index 2 is start of jumped, rotating Cartman

! Rotate Cartman through 9 positions

FOR angle = 0 TO 360 STEP 45  % For each angle

 ! draw one rotation of Cartman
 GR.ROTATE.START angle, bleft + 10 + 24, btop-124
 GR.BITMAP.DRAW p_bitmap[index], p_bm_obj,bleft + 10, btop - 148
 GR.ROTATE.END
 GR.RENDER            % Render the rotated object

 GR.HIDE p_bitmap[index]

 index = index + 1

NEXT angle  % step through all 9 postions

GR.SHOW p_bitmap[1] % Leave Cartman standing on box

AUDIO.LOAD whee, "whee.mp3"
AUDIO.LOAD boing, "boing.mp3"

! All objects are now drawn
! Now animate them

loop:   % Start of animation loop

FOR index = 1 TO 10 % 10 animation STEPs

 GR.SHOW p_bitmap[index] % Show Cartman

 ! If Cartman is about jump, start the whee sound
 IF index = 2 THEN AUDIO.PLAY whee

 ! Move the up text up by 10 pixels, If text
 ! goes off screen top, move to bottom

 GR.MODIFY p_up_text, "x", text_up_x
 text_up_x = text_up_x + w/20
 IF text_up_x > w/2 THEN text_up_x = -w/2
!!
! Move the down text down by 10 pixels. If
!  text goes off screen bottom move to top

   gr.modify p_down_text, "x", text_down_x
      text_down_x = text_down_x - 10
	  if text_down_x < 10 then text_down_x = 750
!! 
 GR.RENDER % Render this animation step

 ! If Cartman just landed on the box, play
 ! boing sound and pause for 1/2 second

 IF index = 1 THEN
  AUDIO.STOP
  AUDIO.PLAY boing
  PAUSE 300
  AUDIO.STOP
 ENDIF

 ! End of one animation step
 ! hide Cartman
 PAUSE 100 % To smooth animation
 GR.HIDE p_bitmap[index]

NEXT index    % do next animation step

% 10 animation steps done

AUDIO.STOP    % stop Cartman's whee sound
GOTO loop  % do the animations forever


ONERROR:
END
