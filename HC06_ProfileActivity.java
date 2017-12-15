package org.androidtown.sw_pj;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class HC06_ProfileActivity extends AppCompatActivity {

    private static String TAG = "phptest_ProfileActivity";

    private EditText mEditTextPhonenumber;
    private EditText mEditTextNickName;
    private EditText mEditTextName;
    private EditText mEditTextAge;
    private EditText mEditTextSex;
    private TextView mTextViewResult;
    private boolean validate = false;

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

                mEditTextPhonenumber.setText( "" + phonenumber );
                mEditTextNickName.setText( "" + nickname );
                mEditTextName.setText( "" + name );
                mEditTextAge.setText( "" + age );
                mEditTextSex.setText( "" + sex );
            }
        } );
    }


    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show( HC06_ProfileActivity.this,
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

            String serverURL = "http://211.178.141.144/insert.php";
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
}