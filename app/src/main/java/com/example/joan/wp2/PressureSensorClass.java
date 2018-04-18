package com.example.joan.wp2;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 * http://developer.samsung.com/technical-doc/view.do;jsessionid=1EAF2428DFF919537C6594B261B24A49?v=T000000127
 */

public class PressureSensorClass {
    private SensorManager sensorManager = null;
    private Context context;
    private MainActivity activity;

    public PressureSensorClass(MainActivity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }

    public void start(){
        //Get SensorManager instance
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        //Register listener
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE), SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void stop(){
        sensorManager.unregisterListener(sensorListener);
    }

    private SensorEventListener sensorListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // when accuracy changed, this method will be called.
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // when pressure value is changed, this method will be called.
            float pressureValue = 0.0f;
            float androidHeight = 0.0f;

            //Check sensor type.
            if(event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                pressureValue = event.values[0];
                androidHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureValue);
                activity.updatePressureUI(pressureValue, androidHeight,0);
            }
        }
    };
}
