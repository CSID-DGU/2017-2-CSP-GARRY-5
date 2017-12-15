package org.androidtown.sw_pj;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HC06_BroadcastR extends BroadcastReceiver {
    String INTENT_ACTION = Intent.ACTION_BOOT_COMPLETED;
    Activity mActivity;
    //하루 지나면 초기화 관련 변수
    Calendar cal2 = Calendar.getInstance();
    private Handler mHandler;

    SimpleDateFormat Today_hour = new SimpleDateFormat("HH", Locale.KOREAN);
    SimpleDateFormat Today_minute = new SimpleDateFormat("mm", Locale.KOREAN);
    SimpleDateFormat Today_second = new SimpleDateFormat("ss", Locale.KOREAN);

    String today_hour = Today_hour.format(cal2.getTime());
    String today_minute = Today_minute.format(cal2.getTime());
    String today_second = Today_second.format(cal2.getTime());

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onReceive(Context context, Intent intent) {
        //알람 시간이 되었을때 onReceive를 호출함

        if(today_hour.equals("03") && today_minute.equals("13") && today_second.equals("00")) {
            // 메시지 얻어오기
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