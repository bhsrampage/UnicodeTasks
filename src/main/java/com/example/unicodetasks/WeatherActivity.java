package com.example.unicodetasks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import static android.widget.Toast.makeText;

public class WeatherActivity extends AppCompatActivity {

    EditText editText;
    TextView resultTextView;

    public class DownloadTask extends AsyncTask<String , Void , String>
    {

        @Override
        protected String doInBackground(String... urls) {
            URL url ;
            String result= "";
            HttpURLConnection urlConnection = null;
            try{
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while(data!=-1){
                    char current = (char) data;
                    result+=current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e){
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), "Cannot get weather info :(", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);
                String message ="";
                for (int i=0; i<arr.length();i++){
                    JSONObject jsonpart = arr.getJSONObject(i);
                    String main = jsonpart.getString("main");
                    String description = jsonpart.getString("description");
                    if(!main.equals("") && !description.equals("")){
                        message += main +":"+ description + "\r\n";
                    }
                }
                if(!message.equals("")){
                    resultTextView.setText(message);
                }
                else{

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), "Cannot get weather info :(", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void getweather(View view) throws UnsupportedEncodingException {

        DownloadTask task = new DownloadTask();
        String result = null;
        String encodedCityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
        try {
            result = task.execute("https://openweathermap.org/data/2.5/weather?q="+  encodedCityName +"&appid=439d4b804bc8187953eb36d2a8c26a02").get();
        } catch (Exception e) {
            e.printStackTrace();
            makeText(getApplicationContext(),"Could not get weather info :(", Toast.LENGTH_LONG).show();
        }
        InputMethodManager mgr =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        editText = findViewById(R.id.editText);
        resultTextView = findViewById(R.id.resultTextview);

    }
}