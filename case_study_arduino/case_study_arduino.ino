#include <Servo.h>                           // Include servo library
#include <Wire.h>
//#include "Adafruit_HTU21DF.h"
#include "DHT.h"

#define DHTPIN 6
#define DHTTYPE DHT22

Servo servoLeft;                             // Declare left and right servos
Servo servoRight;

DHT humiditySensor(DHTPIN, DHTTYPE);

void setup()                                 // Built-in initialization block
{
	Serial.begin(9600);
	Serial.println("Humidity/Temperature Readings:");

	humiditySensor.begin();

	pinMode(10, INPUT);
	pinMode(9, OUTPUT);    // Left IR LED & Receiver
	pinMode(3, INPUT);
	pinMode(2, OUTPUT);    // Right IR LED & Receiver
	pinMode(7, OUTPUT);    // LED

	tone(4, 3000, 1000);                       // Play tone for 1 second
	delay(1000);                               // Delay to finish tone

	servoLeft.attach(13);                      // Attach left signal to pin 13
	servoRight.attach(12);                     // Attach right signal to pin 12
}

void loop()                                  // Main loop auto-repeats
{
    printHumidityAndTemp();
    //indicateAboveTemp(25);
	indicateAboveHumidity(25.0);

	int irLeft = irDetect(9, 10, 38000);       // Check for object on left
	int irRight = irDetect(2, 3, 38000);       // Check for object on right

	if ((irLeft == 0) && (irRight == 0))        // If both sides detect
			{
		maneuver(-200, -200, 20);                // Backward 20 milliseconds
	} else if (irLeft == 0)                       // If only left side detects
			{
		maneuver(200, -200, 20);                 // Right for 20 ms
	} else if (irRight == 0)                      // If only right side detects
			{
		maneuver(-200, 200, 20);                 // Left for 20 ms
	} else                                       // Otherwise, no IR detects
	{
		maneuver(200, 200, 20);                  // Forward 20 ms
	}
}

int irDetect(int irLedPin, int irReceiverPin, long frequency) {
	tone(irLedPin, frequency, 8);              // IRLED 38 kHz for at least 1 ms
	delay(1);                                  // Wait 1 ms
	int ir = digitalRead(irReceiverPin);       // IR receiver -> ir variable
	delay(1);                                  // Down time before recheck
	return ir;                                 // Return 1 no detect, 0 detect
}

void maneuver(int speedLeft, int speedRight, int msTime) {
	// speedLeft, speedRight ranges: Backward  Linear  Stop  Linear   Forward
	//                               -200      -100......0......100       200
	servoLeft.writeMicroseconds(1500 + speedLeft);   // Set left servo speed
	servoRight.writeMicroseconds(1500 - speedRight); // Set right servo speed
	if (msTime == -1)                                   // if msTime = -1
			{
		servoLeft.detach();                            // Stop servo signals
		servoRight.detach();
	}
	delay(msTime);                                   // Delay for msTime
}

void printHumidityAndTemp() {
	float hum = humiditySensor.readHumidity();
	float temp = humiditySensor.readTemperature();

	Serial.print("Humidity: ");
	Serial.print(hum, 2);
	Serial.println("%");

	Serial.print("Temperature: ");
	Serial.print(temp, 2);
	Serial.println(" degrees");
}

void indicateAboveTemp(int thresh) {
	float temp = humiditySensor.readTemperature();
	if (temp > thresh) {
		digitalWrite(2, 0);
	}
}

void indicateAboveHumidity(float thresh) {
	float h = humiditySensor.readHumidity();
	if (h > thresh) {
		digitalWrite(7, 1);
	}
}
