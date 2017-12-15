#include <SoftwareSerial.h>

// SoftwareSerial(RX, TX)
SoftwareSerial BTSerial(2, 3); 
// 데이터를 수신 받을 버퍼
byte buffer[1024];
// 버퍼에 데이타를 저장할 때 기록할 위치
int bufferPosition; 

void setup() {
  BTSerial.begin(9600); 
  Serial.begin(9600); 
  // 버퍼 위치 초기화
  bufferPosition = 0; 
}

void loop() {
  // 블루투스로 데이터 수신
  if (BTSerial.available()) { 
    // 수신 받은 데이터 저장
    byte data = BTSerial.read(); 
    // 수신된 데이터 시리얼 모니터로 출력
    Serial.write(data); 
    // 수신 받은 데이터를 버퍼에 저장
    buffer[bufferPosition++] = data; 
    
    // 문자열 종료 표시
    if (data == '\n') { 
      buffer[bufferPosition] = '\0';
      
      // 스마트폰으로 문자열 전송
      BTSerial.write(buffer, bufferPosition);
      bufferPosition = 0;
    }  
  }
}
