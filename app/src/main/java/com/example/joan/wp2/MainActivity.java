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
 */

public class MainActivity extends AppCompatActivity {
    private PressureSensorClass pressureSensorClass;
    private WindooSensorClass windooSensorClass;
    private TextView pressureFromPhoneTV, androidHeightTV, windooPressureTV, windooPressureTV2;
    private Button windooButton, barometerButton;

    //--------------------------------------------------------------------------
    // LIFE CYCLE
    //--------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Check audio permissions in order to use Windoo jack sensor.
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO},123);
        }

        pressureFromPhoneTV = findViewById(R.id.pressure_from_phone_tv);
        androidHeightTV = findViewById(R.id.android_height_tv);
        windooPressureTV = findViewById(R.id.windoo_pressure_tv);
        windooPressureTV2 = findViewById(R.id.windoo_pressure_tv2);

        windooButton = findViewById(R.id.windoo_btn);
        windooButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Instantiate jdcWindooManager and start observing sensor changes
                if(windooSensorClass == null) {
                    windooSensorClass = new WindooSensorClass(MainActivity.this);
                    windooSensorClass.start();

                    //If any of the sensors have not been initialized, create the file to store the measurements
                    if(pressureSensorClass == null){
                        FileUtil.createFile(getApplicationContext());
                    }
                }
            }
        });
        barometerButton = findViewById(R.id.barometer_btn);
        barometerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Start smartphone sensor listener
                if(pressureSensorClass == null) {
                    pressureSensorClass = new PressureSensorClass(MainActivity.this);
                    pressureSensorClass.start();

                    //If any of the sensors have not been initialized, create the file to store the measurements
                    if(windooSensorClass == null){
                        FileUtil.createFile(getApplicationContext());
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //Liberate sensor listeners when closing app
        pressureSensorClass.stop();
        windooSensorClass.stop();
        pressureSensorClass=null;
        windooSensorClass=null;
        FileUtil.closeOutputStream();
    }

    //--------------------------------------------------------------------------
    // UI METHODS
    //--------------------------------------------------------------------------

    /**
     * Update TextView with new pressure values from the two sources
     * @param pressureValue
     * @param androidHeight
     * @param windooPressure
     */
    public void updatePressureUI(double pressureValue, float androidHeight, double windooPressure, double windooPressureLive){
        if(pressureValue == 0 && androidHeight == 0){
            windooPressureTV.setText(Double.toString(windooPressure));
            windooPressureTV2.setText(Double.toString(windooPressureLive));
        } else if(windooPressure == 0){
            pressureFromPhoneTV.setText(Double.toString(pressureValue));
            androidHeightTV.setText(Float.toString(androidHeight));
        }
    }
}
