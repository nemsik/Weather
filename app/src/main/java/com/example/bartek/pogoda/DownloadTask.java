package com.example.bartek.pogoda;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Bartek on 2017-04-11.
 */


public class DownloadTask extends AsyncTask<String, Void, String> {

    String result = "";
    URL url;
    HttpURLConnection URLConnection = null;
    DownloadIMG downIMG;

    @Override
    protected String doInBackground(String... urls) {


        try {

            url = new URL(urls[0]);
            URLConnection = (HttpURLConnection) url.openConnection();

            InputStream in = URLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {}
        return null;
    }



    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        String sky="";
        String icon="";

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherMain = new JSONObject(jsonObject.getString("main"));
            JSONObject weatherWind = new JSONObject(jsonObject.getString("wind"));
            JSONObject weatherSys = new JSONObject(jsonObject.getString("sys"));

            double temperature = Double.parseDouble(weatherMain.getString("temp"));
            double pressure = Double.parseDouble(weatherMain.getString("pressure"));
            double humidity = Double.parseDouble(weatherMain.getString("humidity"));
            double windSpeed = Double.parseDouble(weatherWind.getString("speed"));
            float windDeg = Float.parseFloat(weatherWind.getString("deg"));
            temperature = (temperature -272.15);

            String placeName = jsonObject.getString("name");
            String country = weatherSys.getString("country");


            JSONArray weatherArray = new JSONArray(jsonObject.getString("weather"));
            for (int i = 0; i < weatherArray.length(); i++) {
                JSONObject jsonobject = weatherArray.getJSONObject(i);
                icon = jsonobject.getString("icon");
                sky = jsonobject.getString("main");

            }

            downIMG = new DownloadIMG();
            downIMG.execute("http://www.openweathermap.org/img/w/"+icon+".png");

            String temp = String.valueOf(temperature);

            DecimalFormat precision = new DecimalFormat("0.00");

            MainActivity.temperature.setVisibility(TextView.VISIBLE);
            MainActivity.sky.setVisibility(TextView.VISIBLE);
            MainActivity.pressure.setVisibility(TextView.VISIBLE);
            MainActivity.humidity.setVisibility(TextView.VISIBLE);
            MainActivity.wind.setVisibility(TextView.VISIBLE);

            MainActivity.placename.setText(placeName+", "+country);
            MainActivity.temperature.setText(precision.format(temperature)+" *C");
            MainActivity.sky.setText("sky: "+sky);
            MainActivity.pressure.setText("pressure: "+precision.format(pressure)+" hPa");
            MainActivity.humidity.setText("humidity: "+precision.format(humidity)+" %");
            MainActivity.wind.setText("wind: " + windSpeed+" ms/s");
            MainActivity.windICO.setRotation(windDeg + 180);

            MainActivity.pb.setVisibility(ProgressBar.INVISIBLE);


        }catch (Exception e){
            Log.d("DOWNLOADTASK", "Error");

            MainActivity.placename.setText("No city found.\n" +
                    "Check spelling and internet connection.");
            MainActivity.temperature.setVisibility(TextView.INVISIBLE);
            MainActivity.sky.setVisibility(TextView.INVISIBLE);
            MainActivity.pressure.setVisibility(TextView.INVISIBLE);
            MainActivity.humidity.setVisibility(TextView.INVISIBLE);
            MainActivity.wind.setVisibility(TextView.INVISIBLE);
            MainActivity.windICO.setVisibility(ImageView.INVISIBLE);
        }
        finally {
            MainActivity.pb.setVisibility(ProgressBar.INVISIBLE);
        }


    }

}
