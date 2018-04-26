REM Start of BASIC! Program
!
! Demo my_program for building APKs
!
! When this program is run in Standard Basic
! the audio file is loaded from rfo-basic/data/
!
! When run from an APK, the audio is loaded
! from assets/<your-app>/data/
!
AUDIO.LOAD aft, "meow.wav"
AUDIO.PLAY aft
PRINT "Kitty says hello world"
PRINT "Hit back to exit"
DO
UNTIL 0
ONBACKKEY:
EXIT
