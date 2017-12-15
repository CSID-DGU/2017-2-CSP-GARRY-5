package org.androidtown.sw_pj;

import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.androidtown.sw_pj.decorators.HC06_EventDecorator;
import org.androidtown.sw_pj.decorators.HC06_OneDayDecorator;
import org.androidtown.sw_pj.decorators.HC06_SaturdayDecorator;
import org.androidtown.sw_pj.decorators.HC06_SundayDecorator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;

public class HC06_CheckActivity extends AppCompatActivity {

    String time,kcal,menu;
    private final HC06_OneDayDecorator oneDayDecorator = new HC06_OneDayDecorator();
    Cursor cursor;
    MaterialCalendarView materialCalendarView;

    //대소변 횟수 출력 관련
    TextView pee;
    TextView shit;

    int count_pee;
    int count_shit;

    String key_pee;
    String key_shit;

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
                new HC06_SundayDecorator(),
                new HC06_SaturdayDecorator(),
                oneDayDecorator);

        String[] result = {"2017,03,18","2017,04,18","2017,05,18","2017,06,18"};

        new ApiSimulator(result).executeOnExecutor(Executors.newSingleThreadExecutor());


        //대소변 값 출력 관련
        pee = (TextView) findViewById(R.id.day_pee);
        shit = (TextView) findViewById(R.id.day_shit);


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

                count_pee = ((HC06_MainActivity)HC06_MainActivity.mContext).loadPreference2(key_pee);
                count_shit = ((HC06_MainActivity)HC06_MainActivity.mContext).loadPreference2(key_shit);

                pee.setText("소변 횟수 : "+ count_pee);
                shit.setText("대변 횟수 : "+ count_shit);
                //여기까지

                String shot_Day = Year + "/" + Month + "/" + Day;

                Log.i("shot_Day test", shot_Day + "");
                materialCalendarView.clearSelection();

                Toast.makeText(getApplicationContext(), shot_Day , Toast.LENGTH_SHORT).show();
            }
        });
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

            materialCalendarView.addDecorator(new HC06_EventDecorator(Color.GREEN, calendarDays,HC06_CheckActivity.this));
        }
    }
}