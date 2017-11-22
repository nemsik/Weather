package com.example.bartek.pogoda;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements LocationListener, ActivityCompat.OnRequestPermissionsResultCallback {

    static TextView placename;
    static TextView temperature;
    static TextView humidity;
    static TextView pressure;
    static TextView sky;
    static ImageView icon;
    static TextView wind;
    static ImageView windICO;
    static ProgressBar pb;
    ImageView imageSetting;
    Switch switchLoc;
    Context context;
    DownloadTask task;
    Location location;

    SharedPreferences sharedpreferences;
    SharedPreferences.Editor editor;

    LocationManager locationManager;
    private static String appid = "&appid=fe46b0c4a994d0f3e1789275bd9bb8a7";

    String link = "http://api.openweathermap.org/data/2.5/weather?q=";
    String city;
    boolean checkSwitch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        placename = (TextView) findViewById(R.id.textView);
        temperature = (TextView) findViewById(R.id.textView2);
        humidity = (TextView) findViewById(R.id.humidity);
        pressure = (TextView) findViewById(R.id.pressure);
        sky = (TextView)  findViewById(R.id.sky);
        icon = (ImageView) findViewById(R.id.imageView);
        wind = (TextView) findViewById(R.id.wind);
        windICO = (ImageView) findViewById(R.id.windICO);
        pb = (ProgressBar) findViewById(R.id.progressBar);

        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedpreferences.edit();


        city = sharedpreferences.getString("city", "Lodz");
        Log.d("CITY", city);
        checkSwitch = sharedpreferences.getBoolean("checkLOC", false);

        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;


        if (permissionGranted) {
            try {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 100, this);

            }catch (Exception e){}
        }
        else ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);


        pb.setVisibility(ProgressBar.VISIBLE);
        icon.setVisibility(ImageView.INVISIBLE);
        Log.d("CHECKSWITCH", String.valueOf(checkSwitch));
        if(checkSwitch==false){
            task = new DownloadTask();
            task.execute(link + city + appid);
        }else
        {
            try {
                pb.setVisibility(ProgressBar.VISIBLE);
                icon.setVisibility(ImageView.INVISIBLE);
                pogoda(location.getLatitude(), location.getLongitude());

               // Toast.makeText(context, ""+location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            }catch (Exception e){
                Toast.makeText(context, "err", Toast.LENGTH_SHORT).show();
            }
            
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        try{
            if(checkSwitch == true){
                pogoda(location.getLatitude(), location.getLongitude());
            }
        }catch (Exception e){}
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    public void pogoda(double latitude, double longitude){
        task = new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?lat=" +String.valueOf(latitude)+ "&lon="+ String.valueOf(longitude) + "&appid=fe46b0c4a994d0f3e1789275bd9bb8a7");
    }

    public void SettingClick(View view) {
        Log.d("nowy", "intent");
        Intent setting = new Intent(context, SettingActivity.class);
        finish();
        startActivity(setting);
    }


}
