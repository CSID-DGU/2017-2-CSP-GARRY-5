package org.androidtown.sw_pj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class HC06_Intro extends Activity {
    Handler handler = new Handler();
    Runnable r = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), HC06_MainActivity.class);
            startActivity(intent);
            finish();
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    protected void onResume() {
        super.onResume();
        //다시 화면에 들어왔을 때 예약 걸어주기
        handler.postDelayed(r,1000);
    }

    protected void onPause() {
        super.onPause();
        //화면을 벗어나면, handler에 예약해놓은 작업을 취소하자
        handler.removeCallbacks(r);
    }
}
