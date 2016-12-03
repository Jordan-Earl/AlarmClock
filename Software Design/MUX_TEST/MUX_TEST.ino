/* Copyright (c) Jordan Earl Williams
 * Date Started: 19NOV2016
 * Version 0.0.1
 * 
 * This code is to test the DEMUX theory of our LED matrix. What it does is runs the alarmOn fucntion 
 * which makes the device go into the alarm state. With that we can set each LED to a color using PWM
 * and DEMUX bit selection. Getting the RGB colors allows us to send it as a pwm to each cathode 
 * allowing maximum color spectrum for our clock.
 */
 
int LED_R = 5, LED_B = 6, LED_G = 9, SEL_B0 = 4, SEL_B1 = 7, SEL_B2 = 8; //Setting pin numbers for the RGB Cathodes and the selection pins for the DEMUX
int ACTIVE_RGB[18] = {0,255,0,20,25,100,145,185,120,255,255,255,200,25,0,200,25,0}; //This is to hold the colors for each LED when the alarm is on
int DEACTIVE_RGB[18] = {0,75,220,0,75,220,0,75,220,0,75,220,0,75,220,0,75,220}; //This is to hold the colors for each LED when the correct button has been depressed
int AFLAG = 0; //This will make the clock go into the alarm mode

void setup(){

  //Baud Rate could be increase?
  Serial.begin(115200);

  //Setting I/O Pins
  pinMode(LED_R, OUTPUT);
  pinMode(LED_B, OUTPUT);
  pinMode(LED_G, OUTPUT);
  pinMode(SEL_B0, OUTPUT);
  pinMode(SEL_B1, OUTPUT);
  pinMode(SEL_B2, OUTPUT);
}

void loop() {

  //do{

    AFLAG = alarmOn();
    
  //}while(!AFLAG)

}

//alarmON loops through the LEDs and displays their color by calling the pinSelect then rgbSelect
int alarmOn(){
    for(int i = 0; i <= 5; i++){
        pinSelect(i);
        rgbSelect(i);
    }
}

//pinSelect sets the select bits of the DEMUX to connect the anode to ground allowing the colors to shine
void pinSelect(int bit_sel){

       digitalWrite(SEL_B0,(((bit_sel&1)==1) ? HIGH : LOW));
       digitalWrite(SEL_B1,(((bit_sel&2)==2) ? HIGH : LOW));
       digitalWrite(SEL_B2,(((bit_sel&4)==4) ? HIGH : LOW));
       
       //Debugging Code::
         Serial.print(digitalRead(SEL_B2));
         Serial.print(digitalRead(SEL_B1));
         Serial.println(digitalRead(SEL_B0));

}


//rgbSelect sets the PWM for the proper RGB colors of the LEDs
void rgbSelect(int color_sel){
       color_sel *= 3;
       analogWrite(LED_R,ACTIVE_RGB[color_sel]);
       analogWrite(LED_B,ACTIVE_RGB[color_sel+1]);
       analogWrite(LED_G,ACTIVE_RGB[color_sel+2]);
       delay(250);
    //Debug Code::
       Serial.print(ACTIVE_RGB[color_sel]);
       Serial.print("\t");
       Serial.print(ACTIVE_RGB[color_sel+1]);
       Serial.print("\t");
       Serial.println(ACTIVE_RGB[color_sel+2]);

}
