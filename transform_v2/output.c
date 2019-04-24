  
const int basicServo1WritePin1 = 13;  
const int humidityandTemperatureSensorRHT031ReadPin1 = 6;  
const int tS2ReadPin1 = 10;  
const int infrared850nmLED1WritePin1 = 9;  
const int tS1ReadPin1 = 3;  
const int infrared850nmLED2WritePin1 = 2;  
const int basicServo2WritePin1 = 12;  
const int red633nmLED1WritePin1 = 7;  


void setup() {
  low(26);
  low(27);
  drive_setRampStep(12);
}

void loop() {
  dht22_read(humPin);
  float humidity = dht22_getHumidity();
  humidity = humidity / 10.0;
  float temp = dht22_getTemp(CELSIUS) / 10.0;
  if(temp > humThresh) {
        high(ledPin);
      } else {
        low(ledPin);
      }
  freqout(11, 1, 38000);
  irLeft = input(10);
  freqout(1, 1, 38000);
  irRight = input(2);
  if(irRight == 1 && irLeft == 1) {
        drive_rampStep(128, 128);
      } else if(irLeft == 0 && irRight == 0)
        drive_rampStep(-128, -128);
      else if(irRight == 0)
        drive_rampStep(-128, 128);
      else if(irLeft == 0)
        drive_rampStep(128, -128);
}

