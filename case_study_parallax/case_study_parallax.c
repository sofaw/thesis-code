/*
  Blank Simple Project.c
  http://learn.parallax.com/propeller-c-tutorials 
*/
#include "simpletools.h"                        // Library includes
#include "abdrive.h"
#include "dht22.h"

int irLeft, irRight;                            // IR variables

int humPin = 6;

int main()                                      // main function
{
  low(26);                                      // D/A0 & D/A1 to 0 V
  low(27);

  drive_setRampStep(12);                        // Max step 12 ticks/s every 20 ms

  while(1)
  {
    
    dht22_read(humPin);
    float humidity = dht22_getHumidity() / 10.0; // Humidity returned in tenths of a percent
    float temp = dht22_getTemp(CELSIUS) / 10.0; // Temp returned in tenths of degrees
    myPrintf(humidity);
    putchar('\n');
    myPrintf(temp);
    putchar('\n');
    
    freqout(11, 1, 38000);                      // Check left & right objects
    irLeft = input(10);

    freqout(1, 1, 38000);
    irRight = input(2);

    if(irRight == 1 && irLeft == 1)             // No obstacles?
      drive_rampStep(128, 128);                 // ...full speed ahead
    else if(irLeft == 0 && irRight == 0)        // Left & right obstacles?
      drive_rampStep(-128, -128);               // ...full speed reverse
    else if(irRight == 0)                       // Just right obstacle?
      drive_rampStep(-128, 128);                // ...rotate left
    else if(irLeft == 0)                        // Just left obstacle?
      drive_rampStep(128, -128);                // ...rotate right
  }
}

// https://stackoverflow.com/questions/23191203/convert-float-to-string-without-sprintf
void myPrintf(float fVal)
{
    char result[100];
    int dVal, dec, i;

    fVal += 0.005;   // added after a comment from Matt McNabb, see below.

    dVal = fVal;
    dec = (int)(fVal * 100) % 100;

    memset(result, 0, 100);
    result[0] = (dec % 10) + '0';
    result[1] = (dec / 10) + '0';
    result[2] = '.';

    i = 3;
    while (dVal > 0)
    {
        result[i] = (dVal % 10) + '0';
        dVal /= 10;
        i++;
    }

    for (i=strlen(result)-1; i>=0; i--)
        putc(result[i], stdout);
}