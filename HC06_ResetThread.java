package org.androidtown.sw_pj;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by jiwan_000 on 2017-11-17.
 */

public class HC06_ResetThread extends Thread {

    Calendar cal2;

    Activity mActivity;
    private Handler mHandler;

    SimpleDateFormat Today_hour;
    SimpleDateFormat Today_minute;
    SimpleDateFormat Today_second;

    String today_hour;
    String today_minute;
    String today_second;

    //하루 지난거 감지하기 위한

    SimpleDateFormat Tomorrow;

    String today;
    String tomorrow;

    /*
    public static final int RESET_DAY = 7;

    private final Handler rHandler = new Handler() {

        //핸들러의 기능을 수행할 클래스(handleMessage)
        @Override
        public void handleMessage(Message msg) {
            //BluetoothService로부터 메시지(msg)를 받는다.
            super.handleMessage(msg);

            switch (msg.what) {
                case RESET_DAY :
                    today = Integer.toString(msg.arg1).trim();
                    break;
            }
        }
    };
    */


    //BluetoothService : 생성자
    public HC06_ResetThread(Activity ac, Handler h) {
        mActivity = ac;
        mHandler = h;
    }


    public void run() {
        today = ((HC06_MainActivity)HC06_MainActivity.mContext).loadPreference("today");

        while(true) {
            cal2 = Calendar.getInstance();

            Today_hour = new SimpleDateFormat("HH", Locale.KOREAN);
            Today_minute = new SimpleDateFormat("mm", Locale.KOREAN);
            Today_second = new SimpleDateFormat("ss", Locale.KOREAN);

            today_hour = Today_hour.format(cal2.getTime());
            today_minute = Today_minute.format(cal2.getTime());
            today_second = Today_second.format(cal2.getTime());



            //현재시간을 msec으로 구한다.
            long now = System.currentTimeMillis();
            //현재시간을 date 변수에 저장한다.
            Date date = new Date(now);
            //시간을 나타낼 포맷을 정한다
            Tomorrow = new SimpleDateFormat("DD", Locale.KOREAN);

            String tomorrow = Tomorrow.format(date);

            if(!today.equals(tomorrow)) {
                Message msg = mHandler.obtainMessage();
                // 메시지 ID 설정
                msg.what = HC06_MainActivity.DAY_RESET;
                // 메시지 정보 설정 (int 형식)
                msg.arg1 = 0;
                mHandler.sendMessage(msg);

            } else {
                // 메시지 얻어오기
                Message msg = mHandler.obtainMessage();
                // 메시지 ID 설정
                msg.what = HC06_MainActivity.DAY_RESET;
                // 메시지 정보 설정 (int 형식)
                msg.arg1 = 1;
                mHandler.sendMessage(msg);
            }

        }
    }
}