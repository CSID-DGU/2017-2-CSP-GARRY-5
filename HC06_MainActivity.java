package org.androidtown.sw_pj;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class HC06_MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @RequiresApi(api = Build.VERSION_CODES.N)

    //함수 호출 관련
    public static Context mContext;

    //현재시간을 msec으로 구한다.
    long now = System.currentTimeMillis();
    //현재시간을 date 변수에 저장한다.
    Date date = new Date(now);
    //시간을 나타낼 포맷을 정한다(yyyy/MM/dd 같은 형태로 변형 가능)
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy년 MM월 dd일 EEEE", Locale.KOREAN);
    //nowDate 변수에 값을 저장한다.

    //오늘 대소변 횟수 관련
    SimpleDateFormat count_format = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);

    Calendar calendar = Calendar.getInstance();
    String formatDate = sdfNow.format(date);
    String count = count_format.format(date);
    TextView dateNow;

    /*BluetoothChat 관련*/
    //Debugging
    private static final String TAG = "MAIN";

    //Intent request_code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    //layout : 에 관련된 객체 정의
    private Button btn_Connect;

    private HC06_BluetoothService bluetoothService_obj = null;  //BluetoothService클래스에 접근하기 위한 객체이다.

    private final Handler mHandler = new Handler() {

        //핸들러의 기능을 수행할 클래스(handleMessage)
        @Override
        public void handleMessage(Message msg) {
            //BluetoothService로부터 메시지(msg)를 받는다.
            super.handleMessage(msg);

            switch(msg.what) {
                case MESSAGE_STATE_CHANGE :
                    if(D) Log.i(TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);

                    switch(msg.arg1) {

                        //블루투스 연결이 되었을 경우
                        case HC06_BluetoothService.STATE_CONNECTED :
                            Toast.makeText(getApplicationContext(), "센서 연결에 성공하였습니다!",Toast.LENGTH_LONG).show();
                            break;

                        //블루투스 연결이 실패했을 경우
                        case HC06_BluetoothService.STATE_FAIL :
                            Toast.makeText(getApplicationContext(), "센서 연결에 실패하였습니다...", Toast.LENGTH_LONG).show();
                            break;
                    }//msg_switch

                    break;

                case MESSAGE_READ :
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    strMsg = new String(readBuf, 0, msg.arg1);
                    //센서로 부터 읽은 값 ListView에 출력

                    //현재 시간 관련
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("HH : mm",Locale.KOREAN);
                    String time = df.format(cal.getTime());


                    //SharedPreference 관련
                    order=loadPreference2("sensor");
                    order++;
                    dataKey = Integer.toString(order);
                    savePreference("sensor",order);

                    if(strMsg.equals("0"))
                    {
                        strMsg = time +" 소변이 감지되었습니다.";
                        savePreference(dataKey, strMsg);
                        count_pee = loadPreference2(count+"소변");
                        count_pee++;
                        savePreference(count+"소변",count_pee);
                    }

                    else if(strMsg.equals("1")) {
                        strMsg = time +" 대변이 감지되었습니다.";
                        savePreference(dataKey, strMsg);
                        count_shit = loadPreference2(count+"대변");
                        count_shit++;
                        savePreference(count+"대변",count_shit);
                    }


                    //Alarm 메소드에 시간 정보 저장
                    new AlarmHATT(getApplicationContext()).Alarm(cal);

                    break;

                case DAY_RESET :
                    if(msg.arg1==0) {
                        reset();
                    }

                    break;
            }
        }
    };


    private static final boolean D = true;
    //Messages
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 0;

    //블루투스 데이터 수신관련
    String strMsg;

    ListView sensorData;
    ArrayAdapter <String> adapter;


    //SharedPreference 관련
    int order;
    String dataKey;


    //하루지나면 데이터 초기화 관련
    Calendar cal2;
    public static final int DAY_RESET=3;
    HC06_ResetThread restThread;
    private Handler thandler;

    void reset() {
        savePreference("sensor",0);
        adapter.clear();
    }

    //하루 대변, 소변 횟수 변수
    int count_pee;
    int count_shit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG,"onCreate");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //액션바 배경색 변경
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF339999));


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //현재날짜 출력하기
        dateNow = (TextView) findViewById(R.id.dateNow);
        dateNow.setText(formatDate);


        //BluetoothChat
        //버튼 리소스를 뷰로 전개
        View myButtonLayout = getLayoutInflater().inflate(R.layout.actionbar_button, null);
        //액션바의 인스턴스 생성
        ActionBar ab = getSupportActionBar();
        //액션바의 커스텀 영역에 버튼 뷰 추가
        ab.setCustomView(myButtonLayout);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME |
                ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_SHOW_CUSTOM);


        //버튼에 기능을 주기 위한 리스너 부착
        btn_Connect = (Button) findViewById(R.id.bluetooth_connect);
        btn_Connect.setOnClickListener(mClickListener);

        if(bluetoothService_obj == null) {
            bluetoothService_obj = new HC06_BluetoothService(this, mHandler);
        }


        //ListView에 센서값 출력 관련
        adapter = new ArrayAdapter<String> (this, R.layout.data_list);
        sensorData = (ListView) findViewById(R.id.ListView2);
        sensorData.setAdapter(adapter);


        //SharedPreferences 데이터 불러오기
        reset2();
        printData();

        //하루 지나면 데이터 초기화 관련 쓰레드 생성
        restThread = new HC06_ResetThread(this, mHandler);
        restThread.start();

        //함수 호출 관련
        mContext = this;

    }



    //알람관련 내부 클래스
    public class AlarmHATT {
        private Context context;
        public AlarmHATT(Context context) {
            this.context=context;
        }
        public void Alarm(Calendar calendar) {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(HC06_MainActivity.this, HC06_BroadcastD.class);

            PendingIntent sender = PendingIntent.getBroadcast(HC06_MainActivity.this, 0, intent, 0);

            Calendar cal = calendar;

            //알람이 발생할 정확한 시간을 지정
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.SECOND,5);
            am.set(AlarmManager.RTC, System.currentTimeMillis() + 500, sender);
        }
    }


    /*
    //하루 지나면 초기화 관련
    public class Reset {
        private Context context;
        public Reset(Context context) {
            this.context=context;
        }
        public void reset(Calendar calendar) {
            AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, BroadcastR.class);

            PendingIntent sender = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);

            Calendar cal = calendar;

            //알람이 발생할 정확한 시간을 지정
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.add(Calendar.SECOND,5);
            am.set(AlarmManager.RTC, System.currentTimeMillis(), sender);

        }
    }
    */

    @Override
    protected void onStop() {
        super.onStop();
        /*
            데이터 초기화 관련중 오늘 날짜 저장
        */
        SimpleDateFormat Today = new SimpleDateFormat("DD",Locale.KOREAN);
        String tod = Today.format(date);
        //오늘 날짜 캐시에 저장
        savePreference("today",tod);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //SharedPreferences 데이터 불러오기
    public void printData() {
        for(int i = 1; i <= loadPreference2("sensor"); i++) {
            String key = Integer.toString(i);
            String dataValue = loadPreference(key);
            adapter.insert(dataValue, 0);
        }
    }

    void reset2() {
        adapter.clear();
    }


    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.bluetooth_connect) {
            btn_Connect = (Button) findViewById(R.id.bluetooth_connect);
            btn_Connect.setOnClickListener(mClickListener);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    */


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_check) {
            HC06_MainActivity.this.startActivity(new Intent(HC06_MainActivity.this,HC06_CheckActivity.class));
        } else if (id == R.id.nav_profile) {
            HC06_MainActivity.this.startActivity(new Intent(HC06_MainActivity.this,HC06_ProfileActivity.class));
        }
        //else if (id == R.id.nav_alarm) {
        //MainActivity.this.startActivity(new Intent(MainActivity.this,SettingActivity.class));
        //}
        /* 나중에 안람관련 사용할 때
                <item
            android:id="@+id/nav_alarm"
            android:icon="@drawable/ic_menu_slideshow"
            android:title="알람 관리" />
            이거를 menu-> activity_main_drawer에 넣는다.
         */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*블루투스 접속에 따른 결과를 처리하는 메소드 이다.*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.d(TAG, "onActivityResult" + resultCode);
        // TODO Auto-generated method stub

        switch(requestCode)
        {

            case REQUEST_ENABLE_BT :
                //When the request to enable Bluetooth returns
                if(resultCode == Activity.RESULT_OK)  //블루투스가 Off일때 연결을 눌러 활성화 시켰을 경우
                {
                    bluetoothService_obj.scanDevice(); //기기검색을 요청하는 메소드 추가
                } else {
                    Log.d(TAG, "Bluetooth is not enable");
                }
                break;

            case REQUEST_CONNECT_DEVICE :
                if(resultCode == Activity.RESULT_OK)
                {
                    bluetoothService_obj.getDeviceInfo(data);
                }
                break;
        }
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //분기.
            switch ( v.getId() ){

                case R.id.bluetooth_connect :  //모든 블루투스의 활성화는 블루투스 서비스 객체를 통해 접근한다.

                    if(bluetoothService_obj.getDeviceState()) // 블루투스 기기의 지원여부가 true 일때
                    {
                        bluetoothService_obj.enableBluetooth();  //블루투스 활성화 시작.
                    }
                    else
                    {
                        finish();
                    }
                    break ;

                default: break ;

            }//switch
        }
    };

    //문자 저장
    private void savePreference(String key, String save) {
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, save);
        editor.commit();
    }

    //정수 저장
    private void savePreference(String key, int save) {
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, save);
        editor.commit();
    }

    //문자 반환
    String loadPreference(String key) {
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        //기존에 저장된거 없을때는 그냥 오늘날짜 반환
        SimpleDateFormat Today = new SimpleDateFormat("DD",Locale.KOREAN);
        String tod = Today.format(date);
        return pref.getString(key,tod);
    }


    //정수 반환
    public int loadPreference2(String key) {
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        return pref.getInt(key,0);
    }


    private void removePreferences(String key) {
        SharedPreferences pref = getSharedPreferences("pref",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove(key);
        editor.commit();
    }
}