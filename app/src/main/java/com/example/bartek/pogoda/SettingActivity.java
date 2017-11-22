package com.example.bartek.pogoda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SettingActivity extends AppCompatActivity {

    Button buttonSave;
    EditText editCity;
    Switch locSwitch;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String city;
    boolean checkLOC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("set", "new intent");
        setContentView(R.layout.activity_setting);

        context = getApplicationContext();

        buttonSave = (Button) findViewById(R.id.buttonsave);
        editCity = (EditText) findViewById(R.id.editCity);
        locSwitch = (Switch) findViewById(R.id.locswitch);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();

        city = sharedPreferences.getString("city", "Lodz");
        editCity.setHint(city);

        checkLOC = sharedPreferences.getBoolean("checkLOC", false);
        if(checkLOC == true){
            locSwitch.setChecked(true);
        }else {
            locSwitch.setChecked(false);
        }

        if(locSwitch.isChecked()){
            editCity.setEnabled(false);
        }


            buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!locSwitch.isChecked()){
                    if(editCity.getText().toString().trim().length()>0){
                        try {
                            city = URLEncoder.encode(editCity.getText() + "", "UTF-8");
                            editor.putString("city", city);
                            editor.apply();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Intent w = new Intent(context, MainActivity.class);
                        finish();
                        startActivity(w);
                    }else{
                        Toast.makeText(context, "You must enter the city", Toast.LENGTH_SHORT).show();

                        /*
                        Intent w = new Intent(context, MainActivity.class);
                        finish();
                        startActivity(w);
                        */
                    }
                }else{
                    Intent w = new Intent(context, MainActivity.class);
                    finish();
                    startActivity(w);
                }
            }
        });


        locSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(locSwitch.isChecked()){
                    editor.putBoolean("checkLOC", true);
                    editCity.setEnabled(false);
                    editor.apply();
                }else {
                    editor.putBoolean("checkLOC", false);
                    editCity.setEnabled(true);
                    editor.apply();
                }
            }
        });

    }


}
