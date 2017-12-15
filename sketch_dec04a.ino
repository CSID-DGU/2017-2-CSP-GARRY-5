#include <DHT11.h> 
#include <SoftwareSerial.h>
int pin = A2;         
int sensor = A3;
DHT11 dht11(pin);
SoftwareSerial BTSerial(2, 3);
float temp, humi = 0;
float ddong = 0;
void setup()
{
  Serial.begin(115200); 
}
void loop()
{
  Serial.println("A");
  ddong = analogRead(sensor);
  /*dht11.read(humi, temp);
  /*Serial.print("temperature:");
  Serial.println(temp);
  Serial.print(" humidity:");
  Serial.println(humi);*/
  Serial.print("ddong: ");
  Serial.println(ddong);
  Serial.print("B");
  delay(3000);                    
}


