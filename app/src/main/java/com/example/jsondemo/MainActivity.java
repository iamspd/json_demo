package com.example.jsondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static class LoadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(urls[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {

                    char current = (char) data;
                    result += current;

                    data = inputStreamReader.read();
                }

                return result;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                JSONObject apiObj = new JSONObject(result);
                String weather = apiObj.getString("weather");

                JSONArray weatherArray = new JSONArray(weather);

                for (int i = 0; i < weatherArray.length(); i++) {

                    JSONObject weatherObj = weatherArray.getJSONObject(i);

                    Log.i("API Data", weatherObj.getString("description"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoadJSONTask loadJSONTask = new LoadJSONTask();
        loadJSONTask
                .execute("https://api.openweathermap.org/data/2.5/weather?q=Toronto&appid=7b35086e80a579855a86a30689073378");

    }
}