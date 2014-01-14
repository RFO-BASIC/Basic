!!
This command down loads the
BASIC! manual, De Re BASIC!

The manual is in PDF format
and can be read by any of the
PDF readers available on the
Android Market.

Most Android products come
with a PDF reader application. Thus
this program will attempt to open
the manual for reading.

The manual will be down loaded
to the SDCARD in the directory
"/sdcard/rfo-basic/data/De_Re_BASIC!.pdf"

This download will take up
to a minute. Be patient.

!!
BYTE.OPEN r, fn, "http://laughton.com/basic/help/De_Re_BASIC!.pdf"
POPUP "Be patient. Wait for 'Download Done' Message", 0, 0, 1
BYTE.COPY fn, "De_Re_BASIC!.pdf"
PRINT "Download Done."
PRINT "Opening the manual in your PDF reader."
BROWSE "file:///sdcard/rfo-basic/data/De_Re_BASIC!.pdf"
END
