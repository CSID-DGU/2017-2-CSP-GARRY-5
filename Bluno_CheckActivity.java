package org.androidtown.smart_diaper;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.androidtown.smart_diaper.decorators.BlunoEventDecorator;
import org.androidtown.smart_diaper.decorators.BlunoOneDayDecorator;
import org.androidtown.smart_diaper.decorators.BlunoSaturdayDecorator;
import org.androidtown.smart_diaper.decorators.BlunoSundayDecorator;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class Bluno_CheckActivity extends AppCompatActivity {


    public Context mContext3=this;
    String time,kcal,menu;
    private final BlunoOneDayDecorator oneDayDecorator = new BlunoOneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;

    //Server DB add
    private static String TAG = "phptest_CheckActivity";

    //대소변 횟수 출력 관련
    TextView pee;
    TextView shit;

    Button button2;

    int count_pee;
    int count_shit;
    //int totalcount;

    String key_pee;
    String key_shit;

    String peecount2;
    String shitcount2;
    String totalcount2;


    String phonenum;
    //String phonenumber="phonenumber";

    //digestinsert

    // String phonenumber=((ProfileActivity)ProfileActivity.mContext).loadPreference( "phonenumber" );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        materialCalendarView = (MaterialCalendarView)findViewById(R.id.calendarView);

        materialCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2017, 0, 1)) // 달력의 시작
                //.setMaximumDate(CalendarDay.from(2030, 11, 31)) // 달력의 끝
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.addDecorators(
                new BlunoSundayDecorator(),
                new BlunoSaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2017,03,18","2017,04,18","2017,05,18","2017,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());


        //대소변 값 출력 관련
        pee = (TextView) findViewById(R.id.day_pee);
        shit = (TextView) findViewById(R.id.day_shit);
        //
        button2 =(Button)findViewById( R.id.button2 );

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                String Year = Integer.toString(date.getYear());
                String Month = Integer.toString(date.getMonth() + 1);
                int Day1 = date.getDay();
                String Day;

                if(Day1<10) {
                    Day = "0"+ Day1;
                } else {
                    Day = Integer.toString(Day1);
                }

                Log.i("Year test", Year + "");
                Log.i("Month test", Month + "");
                Log.i("Day test", Day + "");

                //대소변 횟수 비교 관련
                key_pee = Year+Month+Day+"소변";
                key_shit = Year+Month+Day+"대변";

                count_pee = ((Bluno_MainActivity)Bluno_MainActivity.mContext).loadPreference2(key_pee);
                count_shit = ((Bluno_MainActivity)Bluno_MainActivity.mContext).loadPreference2(key_shit);



                peecount2= Integer.toString( count_pee );
                shitcount2=Integer.toString( count_shit );
                totalcount2= Integer.toString( count_pee+count_shit );

                pee.setText("소변 횟수 : "+ count_pee);
                shit.setText("대변 횟수 : "+ count_shit);
                //여기까지


                String shot_Day = Year + "/" + Month + "/" + Day;

                //db date
                final String the_day= Year+Month+Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                Toast.makeText(getApplicationContext(), shot_Day , Toast.LENGTH_SHORT).show();



                //php 전송을 위한 버튼추가
                button2.setVisibility( View.VISIBLE );
                button2.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phonenumber = ((Bluno_ProfileActivity)Bluno_ProfileActivity.mContext2).loadPreference( "phonenumber" );
                        String date = the_day;
                        String peecount = peecount2;
                        String shitcount = shitcount2;
                        String totalcount = totalcount2;

                        //db에 입력하는 부분
                        InsertData2 task = new InsertData2();
                        task.execute( phonenumber,date,peecount,shitcount ,totalcount);


                    }
                } );



            }

        });
        mContext3 = this;
    }

    private class ApiSimulator extends AsyncTask<Void, Void, List<CalendarDay>> {

        String[] Time_Result;

        ApiSimulator(String[] Time_Result){
            this.Time_Result = Time_Result;
        }

        @Override
        protected List<CalendarDay> doInBackground(@NonNull Void... voids) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();

            /*특정날짜 달력에 점표시해주는곳*/
            /*월은 0이 1월 년,일은 그대로*/
            //string 문자열인 Time_Result 을 받아와서 ,를 기준으로짜르고 string을 int 로 변환
            for(int i = 0 ; i < Time_Result.length ; i ++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = Time_Result[i].split(",");
                int year = Integer.parseInt(time[0]);
                int month = Integer.parseInt(time[1]);
                int dayy = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayy);
            }



            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            materialCalendarView.addDecorator(new BlunoEventDecorator(Color.GREEN, calendarDays,Bluno_CheckActivity.this));
        }
    }


    //digestinsert에 필요한 삽입구문
    static class InsertData2 extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute( result );

            Log.d( "digestdatainput", "POST response  - " + result );

        }


        @Override
        protected String doInBackground(String... params) {

            String phonenumber = (String)params[0];
            String date = (String) params[1];
            String peecount = (String) params[2];
            String shitcount = (String) params[3];
            String totalcount = (String) params[4];

            String serverURL = "http://52.78.131.171/digestinsert.php";
            String postParameters = "phonenumber="+phonenumber+ "&date=" + date + "&peecount=" + peecount + "&shitcount=" + shitcount + "&totalcount=" + totalcount;


            try {

                URL url = new URL( serverURL );
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout( 5000 );
                httpURLConnection.setConnectTimeout( 5000 );
                httpURLConnection.setRequestMethod( "POST" );
                //httpURLConnection.setRequestProperty("content-type", "application/json");
                httpURLConnection.setDoInput( true );
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write( postParameters.getBytes( "UTF-8" ) );
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d( "digestinput", "POST response code - " + responseStatusCode );

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader( inputStream, "UTF-8" );
                BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append( line );
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d( "digestinsert", "InsertData: Error ", e );
                return new String( "Error: " + e.getMessage() );
            }

        }
    }

}