package com.example.weatherappfortravelapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class WeatherMainActivity extends Activity {

    EditText cityName;
    TextView result;
    TextView forecast1;
    TextView forecast2;
    TextView forecast3;
    String name;
    String[] forecast = new String[4];
    String[] messages = new String[4];
    ArrayList<HashMap> weatherResults= new ArrayList <HashMap>();
    HashMap<String, String> storeResults;

    public void findWeather(View view) {


        // get the city name from application
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(), 0);

        // need try catch for url encode()
        try {
            // encode the city name to URL
            String encodedCityName = URLEncoder.encode(cityName.getText().toString(), "UTF-8");

            Tasks task = new Tasks();
            task.execute("http://api.openweathermap.org/data/2.5/forecast?q="+encodedCityName+"&appid=57eadf2727845b199642f33474c61ee1 ");


        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity_main);



        cityName = (EditText) findViewById(R.id.cityName);
        result = (TextView) findViewById(R.id.textView4);
        forecast1 = (TextView) findViewById(R.id.textView1);
        forecast2 = (TextView) findViewById(R.id.textView2);
        forecast3 = (TextView) findViewById(R.id.textView3);

    }

    // user created class
    // asynctask to do threads in background with speed
    public class Tasks extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();

// get the data
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();

                while (data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();

                }

                return result;

            } catch (Exception e) {

                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show();

            }

            return null;
        }
        // dobackground result is shown here
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String message = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray test1 = jsonObject.getJSONArray("list");
                boolean flag_start = false;
                for (int i = 0; i < test1.length(); i++) {
                    String city = jsonObject.getJSONObject("city").getString("name");
                    storeResults = new HashMap<String, String>();
                    JSONObject jsonobject = test1.getJSONObject(i);
                    name = jsonobject.getJSONObject("main").getString("temp");
                    name = name.substring(0, 3);
                    double temp = Double.parseDouble(name);
                    temp = (temp - 273.15) *(9/5) + 32;

                    String answer = (String) Double.toString(temp);
                    answer = answer.substring(0, 2);

                    JSONArray weatherArr = jsonobject.getJSONArray("weather");
                    String main = weatherArr.getJSONObject(0).getString("main");
                    String description = weatherArr.getJSONObject(0).getString("description");

                    String icon = weatherArr.getJSONObject(0).getString("icon");

                    String date = jsonobject.getString("dt_txt");

                    if(date.contains("12:00:00") || i==0){
                        storeResults.put("city", city);
                        storeResults.put("temp", answer);
                        storeResults.put("main", main);
                        storeResults.put("description", description);
                        storeResults.put("icon", icon);
                        storeResults.put("date", date);
                        weatherResults.add(storeResults);
                    }
                }


                if (message == "") {


                } else {

                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

                }


            } catch (JSONException e) {
                Log.e("WEATHER",e.toString());

//                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

            }
        }
    }

    public ArrayList<HashMap> fetchInfo(Double lat, Double lon)
    {

        try {
            // encode the city name to URL
//            String encodedCityName = URLEncoder.encode(cityName, "UTF-8");

            Tasks task = new Tasks();

            try {
//                String result = task.execute("http://api.openweathermap.org/data/2.5/forecast?q=" + encodedCityName + "&appid=57eadf2727845b199642f33474c61ee1 ").get();
                String result = task.execute("http://api.openweathermap.org/data/2.5/forecast?lat="+lat.toString()+"&lon="+lon.toString()+"&appid=57eadf2727845b199642f33474c61ee1").get();
                task.onPostExecute(result);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
        catch (Exception e) {

            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG);

        }

        return weatherResults;
    }



}








