package org.androidtown.sw_pj;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;


import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;


public class ProfileActivity extends AppCompatActivity {

    private static String TAG = "phptest_ProfileActivity";


    public static Context mContext2;

    private EditText mEditTextPhonenumber;
    private EditText mEditTextNickName;
    private EditText mEditTextName;
    private EditText mEditTextAge;
    private EditText mEditTextSex;
    private TextView mTextViewResult;
    private boolean validate = false;
    private AlertDialog dialog;

    String sfName = "profile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.profile_setting );


        mEditTextPhonenumber = (EditText) findViewById( R.id.inputphoneInputEditText );
        mEditTextNickName = (EditText) findViewById( R.id.nicknameInputEditText );
        mEditTextName = (EditText) findViewById( R.id.nameInputEditText );
        mEditTextAge = (EditText) findViewById( R.id.ageInputEditText );
        mEditTextSex = (EditText) findViewById( R.id.sexInputEditText );
        mTextViewResult = (TextView) findViewById( R.id.textView_main_result );

        /*
        final Button validateButton = (Button)findViewById( R.id.validateButton );
        validateButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonenumber2 = mEditTextPhonenumber.getText().toString();
                if(validate){
                    return;
                }
                else(mEditTextPhonenumber.equals(""))
                {
                    AlertDialog.Builder builder = new AlertDialog().Builder(ProfileActivity.this);
                    dialog = builder.setMessage("핸드폰 번호는 빈칸일 수 없습니다.")
                                .setPositiveButton( "확인" ,null)
                                .create();
                    dialog.show();
                    return;

                }

                }
            }
        } );

        */



        SharedPreferences sf = getSharedPreferences(sfName, 0);
        String str = sf.getString("phonenumber", ""); // 키값으로 꺼냄
        mEditTextPhonenumber.setText(str); // EditText에 반영함
        savePreference( "phonenumber",str );
        String str2 = sf.getString( "nickname","" );
        mEditTextNickName.setText( str2 );
        String str3 = sf.getString( "name","" );
        mEditTextName.setText( str3 );
        String str4 = sf.getString( "age","" );
        mEditTextAge.setText( str4 );
        String str5 = sf.getString( "sex","" );
        mEditTextSex.setText(str5);

        Button buttonInsert = (Button) findViewById( R.id.button_main_insert );
        buttonInsert.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phonenumber = mEditTextPhonenumber.getText().toString();
                String nickname = mEditTextNickName.getText().toString();
                String name = mEditTextName.getText().toString();
                String age = mEditTextAge.getText().toString();
                String sex = mEditTextSex.getText().toString();

                InsertData task = new InsertData();
                task.execute( phonenumber,nickname, name, age, sex );

            }
        } );

        mContext2 = this;
    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show( ProfileActivity.this,
                    "Please Wait", null, true, true );
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute( result );

            progressDialog.dismiss();
            mTextViewResult.setText( result );
            Log.d( TAG, "POST response  - " + result );
        }


        @Override
        protected String doInBackground(String... params) {

            String phonenumber = (String)params[0];
            String nickname = (String) params[1];
            String name = (String) params[2];
            String age = (String) params[3];
            String sex = (String) params[4];

            String serverURL = "http://52.78.131.171/insert.php";
            String postParameters = "phonenumber="+phonenumber+ "&nickname=" + nickname + "&name=" + name + "&age=" + age + "&sex=" + sex;


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
                Log.d( TAG, "POST response code - " + responseStatusCode );

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

                Log.d( TAG, "InsertData: Error ", e );

                return new String( "Error: " + e.getMessage() );
            }

        }
    }

    protected void onStop() {
        super.onStop();
        // Activity 가 종료되기 전에 저장한다
        // SharedPreferences 에 설정값(특별히 기억해야할 사용자 값)을 저장하기
        SharedPreferences sf = getSharedPreferences("profile", 0);
        SharedPreferences.Editor editor = sf.edit();//저장하려면 editor가 필요
        String str = mEditTextPhonenumber.getText().toString(); // 사용자가 입력한 값
        String str2 = mEditTextNickName.getText().toString();
        String str3 = mEditTextName.getText().toString();
        String str4 = mEditTextAge.getText().toString();
        String str5 = mEditTextSex.getText().toString();
        editor.putString("phonenumber", str); // 입력
        editor.putString( "nickname",str2 );
        editor.putString( "name",str3 );
        editor.putString( "age",str4 );
        editor.putString( "sex",str5 );
        editor.commit(); // 파일에 최종 반영함

    }
    /*
    String loadPreference(String key) {
        SharedPreferences sf = getSharedPreferences("profile",0);
        String str = mEditTextPhonenumber.getText().toString();
        return sf.getString(key,str);
    }
    */

    //문자 저장
    private void savePreference(String key, String save) {
        SharedPreferences pref = getSharedPreferences("profile",0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, save);
        editor.commit();
    }

    //핸드폰전용
    //문자 반환
    String loadPreference(String key) {
        SharedPreferences pref = getSharedPreferences("profile",0);
        return pref.getString(key,"000-0000-0000");
    }

}
