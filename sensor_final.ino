#include <SoftwareSerial.h>
#include <DHT.h>    //온습도 라이브러리 불러옴
SoftwareSerial BTSerial(0, 1);

//sleep 모드 라이브러리
#include <avr/sleep.h>
#include <avr/power.h>
#include <avr/wdt.h>

#define DHTPIN A2 //온습도 핀 설정
#define DHTTYPE DHT22 //DHT22센서 종류 설정
DHT dht(DHTPIN, DHTTYPE);

int gassensor = A3;
int count, count2 = 0;           //카운트 값
float ddong;
float humi, humi2, temp, temp2 = 0; //오줌값
float humi_sum, humi_sum2 = 0; //습도 썸값
float humi_average = 0; //습도 평균값
float temp_sum, temp_sum2 = 0 ; //온도 썸값
float temp_average = 0 ; //온도 평균값
float humi_average2 = 0;
float temp_average2 = 0 ; //온도 평균값

unsigned long pt = 0;
unsigned long pt2 = 0;
unsigned long pt3 = 0;
unsigned long pt4 = 0;
unsigned long pt5 = 0;
unsigned long pt6 = 0;

unsigned long ct = 0;
unsigned long ct2 = 0;
unsigned long ct3 = 0;
unsigned long ct4 = 0;
unsigned long ct5 = 0;
unsigned long ct6 = 0;

//sleep모드 관련(대소변 감지후)
#define WATCH_DOG_COUNT 68
int wake_count = 0;
ISR(WDT_vect) {}

void sleep()
{
  set_sleep_mode(SLEEP_MODE_PWR_DOWN);
  power_adc_disable();
  sleep_mode();
  //CPU SLEEP
  //WHEN WAKE
  sleep_disable();
  power_all_enable();
}

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  BTSerial.begin(115200);
  pinMode(3,OUTPUT);
  digitalWrite(3,HIGH);// voc off
  delay(1000);
  dht.begin();
  pt = millis();
  pt2 = millis();
  pt3 = millis();
  pt4 = millis();
  pt5 = millis();
  pt6 = millis();
}

void loop() {

  // put your main code here, to run repeatedly:
  ct = millis();
  ct2 = millis();
  ct3 = millis();
  ct4 = millis();
  ct5 = millis();
  ct6 = millis();

  //시작 후 3분간 센서값 20번 측정
  if ((ct - pt) <= 180000) {
    //예열1분
    if ((ct5 - pt5) >= 60000) {
      if (count < 20) {               
        humi = dht.readHumidity();
        temp = dht.readTemperature();
        humi_sum = humi_sum + humi;
        temp_sum = temp_sum + temp;
        count++;
        delay(4000);
      }
    }
  }

  //20번 측정한 센서값들의 평균 도출
  if ((count >= 20) && (count < 21)) {
    humi_average = humi_sum / 20 ;
    temp_average = temp_sum / 20 ;
    humi_sum = 0;
    temp_sum = 0;
    count = 21;     
    delay(1000);
  }


  //30분 간격으로 재측정
  if ((ct2 - pt2) > 1800000) {
    if (count2 < 20) {
      humi2 = dht.readHumidity();
      temp2 = dht.readTemperature();
      humi_sum2 = humi_sum2 + humi2;
      temp_sum2 = temp_sum2 + temp2;
      count2++;

      //재측정 하는 도중 대소변의 변화가 감지될 수 있기 때문에
      if ( (humi2 / humi_average) >= 1.20) {

      //온도를 경우에 따라 나눕니다.
      if ((temp_average <= 22.0) && (temp2 / temp_average) >= 1.15) {
        detect_ddong(); //실내온도가 22도 이하이면 변화량이 15%

      } else if ((22.0 <= temp_average) && (temp_average <= 26.0) && (temp2 / temp_average) >= 1.08) {
        detect_ddong(); //실내온도가 22도에서 26도 사이이면 변화량이 8%

      } else if ((temp_average >= 26.0) && (temp2 / temp_average) >= 1.03) {
        detect_ddong(); //실내온도가 26도 이상이면 변화량이 3%

      }

    }
      delay(4000);
    }
  }

  //30분 간격 재측정한 평균값 도출
  if (count2 == 20) {
    humi_average = humi_sum2 / 20 ;
    temp_average = temp_sum2 / 20 ;
    humi_sum2 = 0;
    temp_sum2 = 0;
    count2 = 0;
    pt2 = ct2;
    pt3 = ct3;
  }

  //5분 간격으로 측정 후 평균값과 비교
  if ( ((ct3 - pt3) > 300000)) {
    humi = dht.readHumidity();
    temp = dht.readTemperature();


    // 소변 감지(일단 습도를 먼저 측정합니다.)
    if ( (humi / humi_average) >= 1.20) {

      //온도를 경우에 따라 나눕니다.
      if ((temp_average <= 22.0) && (temp / temp_average) >= 1.15) {
        detect_ddong(); //실내온도가 22도 이하이면 변화량이 15%

      } else if ((22.0 <= temp_average) && (temp_average <= 26.0) && (temp / temp_average) >= 1.08) {
        detect_ddong(); //실내온도가 22도에서 26도 사이이면 변화량이 8%

      } else if ((temp_average >= 26.0) && (temp / temp_average) >= 1.03) {
        detect_ddong(); //실내온도가 26도 이상이면 변화량이 3%

      }

    }

}

if (BTSerial.available()) {
  Serial.write(BTSerial.read());
}
// Serial –> Data –> BT
if (Serial.available()) {
  BTSerial.write(Serial.read());
}
delay(1000);
}


void detect_ddong() {
  digitalWrite(3,LOW);   //voc켜고
  delay(60000);          //1분 예열
  ddong = analogRead(A3);

  if (ddong >= 100) {
    //대변감지
    BTSerial.print("1");
    digitalWrite(3,HIGH); //voc끄고
    delay(1000);
    

    //sleep 모드로 가세요
    while (1) {
      wake_count++;
      if (wake_count >= WATCH_DOG_COUNT)
      {
        wake_count = 0;
        break;
      }

      sleep();
    }
  } else if (ddong < 100) {
    //대변 감지 안되면 소변으로만 감지
    BTSerial.print("0");
    digitalWrite(3,HIGH); //voc끄고
    delay(1000);

    //sleep 모드로 가세요
    while (1) {
      wake_count++;
      if (wake_count >= WATCH_DOG_COUNT)
      {
        wake_count = 0;
        break;
      }
      sleep();
    }
  }
}
