String readString;
char c;
void setup()
{
 pinMode(13,OUTPUT);
 digitalWrite(13,LOW);
 Serial.begin(9600);
 Serial.println("OK then, you first, say something.....");
 Serial.println("Go on, type something and hit Send,");
 Serial.println("or just hit the Enter key,");
 Serial.println("then I will repeat it!");
 Serial.println("");
}
void loop() {
 while (Serial.available())
 {
   delay(3);
   c = Serial.read();
    if (c=='h'||c=='H'){
      digitalWrite(13,HIGH);
      Serial.println("You went HIGH");
    }
    if (c=='l'||c=='L'){
      digitalWrite(13,LOW);
      Serial.println("You went LOW");
    }
 }
}

