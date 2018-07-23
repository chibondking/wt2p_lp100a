# LP100A OSD Serial Interface

This is a Java application that allows for people to connect to a LP-100A device and display on screen forward power and SWR as seen on the actual LP-100A wattmeter. Current initial support will support single coupler and dual coupler features (dual coupler sampling is detected automatically).

If you're using the latest firmware (TBD) for your LP-100A, this OSD software will tell you which coupler (Radio 1 or Radio 2) is currently transmitting.

Colors

Power Meter color schemes:
 Dark blue for Low Power (0-500 watts)
 Dark Yellow for Medium Power (500-1000 watts)
 Dark Orage for High Power (1000-1500 watts)
 Bright Red for 1500-2000 watts)

SWR Meter color schemes:
Green: 1.0-1.49
Yellow: 1.50-1.99
Orange: 2.0 - 2.49
Red: 2.50 +

If the SWR is above 3.0, an additional bright red alert text will display to the right of the SWR bargraph.

Running
To run the LP-100A software, unzip the WT2P-LP100A.zip file to a location of your choice.

You can manually run this via the command line:

java -jar WT2P-LP100A.jar <COM_PORT> <LATEST_LP_100_FIRMWARE>

Where:
COM_PORT is the Com Port your LP-100A is running on (i.e. COM9 on Windows, /dev/ttyS0 on *NIX variants)

LATEST_LP_100_FIRMWARE is if you're running the latest LP-100 firmware (July/August 2018). You're probably not, so this should be set to false.

java -jar WT2P-LP100A.jar COM9 false

If all is successful, you should see the LP-100A software display on screen and the text "connected" appears in the lower left corner of the screen. Apply RF from your radio and you should see the OSD display Power and SWR from the LP-100A.

