package com.example.joan.wp2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 * http://developer.samsung.com/technical-doc/view.do;jsessionid=1EAF2428DFF919537C6594B261B24A49?v=T000000127
 */

public class PressureSensorClass {
    private SensorManager sensorManager = null;
    private Context context;
    private MainActivity activity;
    private float pressureValue;
    private float androidHeight;
    private boolean firstValue;
    private int n;
    private float average;

    public PressureSensorClass(MainActivity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
        firstValue=true;
        n=0;
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

            //Check sensor type.
            if(event.sensor.getType() == Sensor.TYPE_PRESSURE) {
                pressureValue = event.values[0];
                //androidHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureValue);
                //activity.updatePressureUI(pressureValue, androidHeight,0);

                //Temporal averaging
                temporalAveraging(pressureValue);
            }
        }
    };

    //ToDo: this code can be improved
    private float acum=0;
    private void temporalAveraging(float value){
        acum+=value;
        n++;
        if(firstValue){
            firstValue=false;
            //Timer to average values within 5 seconds
            new CountDownTimer(5000,1000){
                @Override
                public void onTick(long millisUntilFinished) {}
                @Override
                public void onFinish() {
                    firstValue=true;
                    average=acum/n;
                    androidHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, average);
                    activity.updatePressureUI(average, androidHeight, 0);
                    average = 0;
                    n=0;
                    acum=0;
                }
            }.start();
        }
    }
}
