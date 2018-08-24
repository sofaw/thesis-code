#include "DHT.h"
#include "Servo.h"

// Add definition
#define DHTTYPE DHT22

const int basicServo1WritePin1 = 12;
const int humidityandTemperatureSensorRHT031ReadPin1 = 6;
const int tS2ReadPin1 = 3;
const int infrared850nmLED1WritePin1 = 2;
const int tS1ReadPin1 = 10;
const int infrared850nmLED2WritePin1 = 9;
const int basicServo2WritePin1 = 13;
const int red633nmLED1WritePin1 = 7;

int irLeft;
int irRight;
// Edit: int humPin = 6;
int humPin = humidityandTemperatureSensorRHT031ReadPin1;
// Edit: int ledPin = 8;
int ledPin = red633nmLED1WritePin1;
float humThresh = 25.0;

// Add definition
char CELSIUS = 0;

// Add decl.
DHT humiditySensor(humPin, DHTTYPE);

// Add decl.
Servo servoLeft;
Servo servoRight;

void setup() {
  Serial.begin(9600);

  humiditySensor.begin();

  low(26);
  low(27);
  drive_setRampStep(12);

  // Add servo setup
  servoLeft.attach(basicServo2WritePin1);
  servoRight.attach(basicServo1WritePin1);

  // Add pinMode setup
  pinMode(infrared850nmLED1WritePin1, OUTPUT);
  pinMode(infrared850nmLED2WritePin1, OUTPUT);
  pinMode(tS2ReadPin1, INPUT);
  pinMode(tS1ReadPin1, INPUT);
  pinMode(ledPin, OUTPUT);
}

void loop() {
  dht22_read(humPin);
  float humidity = dht22_getHumidity();
  // humidity = humidity / 10.0; Delete: Parallax libraries return in tenths
  //float temp = dht22_getTemp(CELSIUS) / 10.0;
  float temp = dht22_getTemp(CELSIUS);

  // Delete putchar
  myPrintf(humidity);
  myPrintf(temp);

  if(temp > humThresh) {
        high(ledPin);
      } else {
        low(ledPin);
      }



  // Replace w. generated variable
  freqout(infrared850nmLED1WritePin1, 1, 38000);
  irRight = input(tS2ReadPin1);

  // Replace w. generated variable
  freqout(infrared850nmLED2WritePin1, 1, 38000);
  irLeft = input(tS1ReadPin1);

  if(irRight == 1 && irLeft == 1) {
        drive_rampStep(128, 128);
      } else if(irLeft == 0 && irRight == 0)
        drive_rampStep(-128, -128);
      else if(irRight == 0)
        drive_rampStep(-128, 128);
      else if(irLeft == 0)
        drive_rampStep(128, -128);
}

// Replace
void myPrintf(float fVal) {
	Serial.println(fVal);
}

void dht22_read(int dht_pin) { // Change return type char -> void
	// Do nothing
}

float dht22_getHumidity() { // Change return type int -> float
	return humiditySensor.readHumidity();
}

void high(int pin) {
	digitalWrite(pin, HIGH);
}

void freqout(int pin, int msTime, int frequency) {
	tone(pin, frequency, msTime*8);
	delay(1);
}

int input(int pin) {
	int val = digitalRead(pin);
	delay(1);
	return val;
}

void drive_rampStep(int left, int right) {
	servoLeft.writeMicroseconds(1500 + left);
	servoRight.writeMicroseconds(1500 - right);
	delay(20);
}

float dht22_getTemp(char temp_units) { // Change return type int -> float
	return humiditySensor.readTemperature();
}

void low(int pin) {
	digitalWrite(pin, LOW);
}

void drive_setRampStep(int stepsize) {
	// Do nothing
}

