int LED_R = 5, LED_B = 6, LED_G = 9, SEL_B0 = 4, SEL_B1 = 7, SEL_B2 = 8;
int ACTIVE_RGB[18] = {200,25,0,200,25,0,200,25,0,200,25,0,200,25,0,200,25,0};
int DEACTIVE_RGB[18] = {0,75,220,0,75,220,0,75,220,0,75,220,0,75,220,0,75,220};
int AFLAG = 0;

void setup(){
  Serial.begin(9600);
  pinMode(LED_R, OUTPUT);
  pinMode(LED_B, OUTPUT);
  pinMode(LED_G, OUTPUT);
  pinMode(SEL_B0, OUTPUT);
  pinMode(SEL_B1, OUTPUT);
  pinMode(SEL_B2, OUTPUT);
}

void loop() {

  //do{

    AFLAG = AlarmOn();
    
  //}while(!AFLAG)

}

int AlarmOn(){
    for(int i = 0; i <= 5; i++){
        pinSelect(i);
    }
}

void pinSelect(int pin_num){

       digitalWrite(SEL_B0,(((pin_num&1)==1) ? HIGH : LOW));
       digitalWrite(SEL_B1,(((pin_num&2)==2) ? HIGH : LOW));
       digitalWrite(SEL_B2,(((pin_num&4)==4) ? HIGH : LOW));
       
       /*//Debugging Code
         Serial.print(digitalRead(SEL_B2));
         Serial.print(digitalRead(SEL_B1));
         Serial.print(digitalRead(SEL_B0));*/

         rgbSelect(pin_num*3);

}

void rgbSelect(int color_sel){
          
       analogWrite(LED_R,ACTIVE_RGB[color_sel]);
       analogWrite(LED_B,ACTIVE_RGB[color_sel+1]);
       analogWrite(LED_G,ACTIVE_RGB[color_sel+2]);

       Serial.print(ACTIVE_RGB[color_sel]);
       Serial.print("\t");
       Serial.print(ACTIVE_RGB[color_sel+1]);
       Serial.print("\t");

       Serial.println(ACTIVE_RGB[color_sel+2]);

}
