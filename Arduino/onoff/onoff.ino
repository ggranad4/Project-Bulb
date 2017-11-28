/* 
List of items
Bluetooth HC-05
Arduino Uno
LED
Resistor 220 Ohm
Breadboard

Instructions
Bluetooth    Arduino
  Rxd   --->  Txd
  Txd   --->  Rxd
  Ground   --->  Ground
  VCC   --->  5v

LED to Arduino 
  -Place LED into breadboard
  -Connect LED negative(short pin) with resistor
  -The other end of the resistor is connected to the negatiive side of the breadboard
  -Place a wire for the resistor to connect to the arduino ground
  -Connect LED positive (large pin) to the arduino pin 13
*/

String voice;
int LEDInput = 13; //The pin the LED is connected to

//Sets up the connection with these values
void setup() {
  pinMode(LEDInput, OUTPUT); //Sets pin 13 as the input and OUTPUT as the result
  Serial.begin(9600); 
} 

//When bluetooth is active and arduino receieves data to modify LED
void loop() {
  char data;
  if(Serial.available()) {
    data=Serial.read(); //Data received
    //LED ON
    if(data=="on"){
      digitalWrite(LEDInput,HIGH);  // OUTPUT with HIGH turns on LED
    }
    //LED OFF
    if(data=="off"){
      digitalWrite(LEDInput,LOW);  //OUTPUT with LOW turns off LED
    }
  }
}

