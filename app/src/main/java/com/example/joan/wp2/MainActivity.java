package com.example.joan.wp2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 * https://gist.github.com/jdc-electronic/4eb9b585bf27e5a222da
 */

public class MainActivity extends AppCompatActivity {
    private PressureSensorClass pressureSensorClass;
    private WindooSensorClass windooSensorClass;
    private TextView pressureFromPhoneTV, androidHeightTV, windooPressureTV;
    private Button windooButton, barometerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://stackoverflow.com/questions/28539717/android-startrecording-called-on-an-uninitialized-audiorecord-when-samplerate/28539778
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},123);
        }

        pressureFromPhoneTV = findViewById(R.id.pressure_from_phone_tv);
        androidHeightTV = findViewById(R.id.android_height_tv);
        windooPressureTV = findViewById(R.id.windoo_pressure_tv);

        windooButton = findViewById(R.id.windoo_btn);
        windooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windooSensorClass.start();
            }
        });
        barometerButton = findViewById(R.id.barometer_btn);
        barometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressureSensorClass.start();
            }
        });

        pressureSensorClass = new PressureSensorClass(this);
        windooSensorClass= new WindooSensorClass(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        pressureSensorClass.stop();
        windooSensorClass.stop();
    }

    public void updatePressureUI(float pressureValue, float androidHeight, double windooPressure){
        if(pressureValue == 0 && androidHeight == 0){
            windooPressureTV.setText(Double.toString(windooPressure));
        } else if(windooPressure == 0){
            pressureFromPhoneTV.setText(Float.toString(pressureValue));
            androidHeightTV.setText(Float.toString(androidHeight));
        }
    }
}
