#include <SoftwareSerial.h>
#include <DHT.h>    //온습도 라이브러리 불러옴

#include <avr/sleep.h>
#include <avr/power.h>
#include <avr/wdt.h>
SoftwareSerial BTSerial(0, 1);
#define DHTPIN A2 //온습도 핀 설정
#define DHTTYPE DHT22 //DHT22센서 종류 설정
DHT dht(DHTPIN, DHTTYPE);
int gassensor = A3;

//delayMicrosenonds();
#include <util/delay_basic.h>
#define DELAY 100   // 지연 시간: 단위 us
unsigned long p_t, c_t;

 



int count, count2 = 0;           //카운트 값
float ddong, ddong2; //똥값
float ddong_sum, ddong_sum2 = 0; //가스 썸값
float ddong_average = 0; //가스 평균값
float humi, humi2, temp, temp2 = 0; //오줌값
float humi_sum, humi_sum2 = 0; //습도 썸값
float humi_average = 0; //습도 평균값
float temp_sum, temp_sum2 = 0 ; //온도 썸값
float temp_average = 0 ; //온도 평균값

//sleep모드 확인용
float humi_t, temp_t, ddong_t = 0;

unsigned long pt = 0;
unsigned long pt2 = 0;
unsigned long pt3 = 0;
unsigned long pt4 = 0;
unsigned long pt5 = 0;
unsigned long ct = 0;
unsigned long ct2 = 0;
unsigned long ct3 = 0;
unsigned long ct4 = 0;
unsigned long ct5 = 0;

//
unsigned long pt6 = 0;
unsigned long ct6 = 0;


//알람한번만 울리게 하기
int stop;
unsigned long pt7 = 0;
unsigned long ct7 = 0;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
  BTSerial.begin(115200);
  dht.begin();
  Serial.println("Hello World");
  pt = millis();
  pt2 = millis();
  pt3 = millis();
  pt4 = millis();
  pt5 = millis();
  pt6 = millis();
  //알람 한 번 울리기용
  pt7 = millis();
  stop = 0;

  //delaymicrosenonds
   //noInterrupts();
   p_t = micros();
}

void loop() {


  
  // put your main code here, to run repeatedly:
  ct = millis();
  ct2 = millis();
  ct3 = millis();
  ct4 = millis();
  ct5 = millis();
  ct6 = millis();
  //알람 한 번 울리기용
  ct7 = millis();


  c_t = micros();
  //interrupts();

  //sleep모드 확인용
  ddong_t = analogRead(gassensor);
  humi_t = dht.readHumidity();
  temp_t = dht.readTemperature();
  Serial.println(ddong_t);
  Serial.println(humi_t);
  Serial.println(temp_t);

  //delayMicroseconds
  Serial.print("p_t");
  Serial.println(p_t);
  Serial.print("c_t");
  Serial.println(c_t);


  //시작 후 3분간 센서값 20번 측정
  if ((ct - pt) <= 120000) {

    if ((ct5 - pt5) >= 60000) {

      if (count < 11) {

        ddong = analogRead(gassensor);
        humi = dht.readHumidity();
        temp = dht.readTemperature();
        ddong_sum = ddong_sum + ddong;
        humi_sum = humi_sum + humi;
        temp_sum = temp_sum + temp;

        count++;

        delay(4000);
      }
    }
  }

  //20번 측정한 센서값들의 평균 도출
  if (count >= 10) {
    ddong_average = ddong_sum / 11;
    humi_average = humi_sum / 11 ;
    temp_average = temp_sum / 11 ;


    ddong_sum = 0;
    humi_sum = 0;
    temp_sum = 0;
    count = 0;
  }


  //30분 간격으로 재측정
  if ((ct2 - pt2) > 1800000) {
    if (count2 < 10) {
      ddong2 = analogRead(gassensor);
      humi2 = dht.readHumidity();
      temp2 = dht.readTemperature();
      ddong_sum2 = ddong_sum2 + ddong2;
      humi_sum2 = humi_sum2 + humi2;
      temp_sum2 = temp_sum2 + temp2;
      count2++;

      delay(4000);
    }
  }

  //30분 간격 재측정한 평균값 도출
  if (count2 == 10) {
    ddong_average = ddong_sum2 / 10;
    humi_average = humi_sum2 / 10 ;
    temp_average = temp_sum2 / 10 ;

    ddong_sum2 = 0;
    humi_sum2 = 0;
    temp_sum2 = 0;
    count2 = 0;
    pt2 = ct2;
  }

  //ct-pt는 몇초마다 작동할 것인가 설정
  //average는 10번 받은 평균값, a는 센서에서 측정된 값
  //a/average>2 라는 소리는 측정된 값이 평균값보다 2배 컸을때를 의미
  //2가지 조건을 만족하면  똥 쌈의 의미


     Serial.print("ct : ");
    Serial.println(ct7);
    
  if (((c_t - p_t) >= 100000)) {   
    power_all_enable();
    sleep_disable();
    Serial.println("잘잤당");
  }



  if ((ct6 - pt6) >= 180000) {
    //5분 간격으로 측정 후 평균값과 비교
    if ( ((ct3 - pt3) > 15000)) {
      humi = dht.readHumidity();
      temp = dht.readTemperature();
      pt3 = ct3;





      // 소변 감지(일단 습도를 먼저 측정합니다.)
      if ( (humi / humi_average) >= 1.09) {

        //온도를 경우에 따라 나눕니다.
        if ((temp_average <= 24.0) && (temp / temp_average) >= 1.15) {
          detect_ddong(); //실내온도가 24도 이하이면 변화량이 21%

        } else if ((24.0 <= temp_average) && (temp_average <= 29.0) && (temp / temp_average) >= 1.10) {
          detect_ddong(); //실내온도가 24도에서 29도 사이이면 변화량이 14%

        } else if ((temp_average >= 29.0) && (temp / temp_average) >= 1.05) {
          detect_ddong(); //실내온도가 29도 이상이면 변화량이 7%

        }
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
  ddong = analogRead(A3);

  if (((ddong / (ddong_average)) >= 5)) {
    //대변감지
    Serial.println("1");
    delay(1500);
    sleep();

  } else if (((ddong / (ddong_average)) < 5)) {
    //대변 감지 안되면 소변으로만 감지
    Serial.println("0");
    delay(1500);
    sleep();

  }
}

void sleep() {
  Serial.println(p_t);
  p_t=c_t;
  Serial.println(c_t);
  set_sleep_mode(SLEEP_MODE_STANDBY);
  sleep_enable();
  sleep_mode();
}
