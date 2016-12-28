#include <SoftwareSerial.h>

String readString;
char c;
SoftwareSerial mSerial(0, 1);

void setup()
{
 pinMode(13,OUTPUT);
 digitalWrite(13,LOW);
// Serial.println("OK then, you first, say something.....");
// Serial.println("Go on, type something and hit Send,");
// Serial.println("or just hit the Enter key,");
// Serial.println("then I will repeat it!");
// Serial.println("");
// 
 mSerial.begin(9600);
 delay(1000);
 mSerial.println("My serial start... ");
 mSerial.print("AT+BAUD4");
 waitForResponse();
}

void loop() {

while (mSerial.available()){
  c = mSerial.read();
  mSerial.println("reading..");
  mSerial.println(c);
  if (c=='h'||c=='H'){
      digitalWrite(13,HIGH);
      mSerial.println("You went HIGH");
    }
    
    if (c=='l'||c=='L'){
      digitalWrite(13,LOW);
      mSerial.println("You went LOW");
    }
  }
}

void waitForResponse(){
  delay(1000);
  while(mSerial.available()){
    mSerial.write(mSerial.read());
    }
   mSerial.write("\n");
  }
