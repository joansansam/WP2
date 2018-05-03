package com.example.joan.wp2;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.widget.Toast;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 * http://developer.samsung.com/technical-doc/view.do;jsessionid=1EAF2428DFF919537C6594B261B24A49?v=T000000127
 */

public class PressureSensorClass {
    private SensorManager sensorManager = null;
    private Context context;
    private MainActivity activity;
    private double pressureValue;
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
                pressureValue = (double)event.values[0];
                //androidHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressureValue);
                //activity.updatePressureUI(pressureValue, androidHeight,0);

                //To check measures, save them to a file (Try to not use this value, TOO MUCH OVERFLOW IN THE LOG FILE)
                //FileUtil.saveToFile(0,pressureValue,0);

                //Averaging and sending to UI and file
                //ToDo: quan es decideixi no fer servir la mitjana temporal, fer el métode averaging un return double i fer l'updateUI() i saveToFile() aquí (fora del métode)
                //temporalAveraging(pressureValue);
                averaging(pressureValue);
            }
        }
    };

    //This method averages between each X (25) input values
    private void averaging(double value){
        acum+=value;
        n++;
        if(n==25){ //For every 25 measurements, get the average
            average=acum/n;
            androidHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, average);

            //To check measures, save them to a file
            FileUtil.saveToFile(0,average,0);

            activity.updatePressureUI(average, androidHeight, 0,0);

            n=0;
            acum=0;
            average=0;
        }
    }

    private float acum=0;
    private void temporalAveraging(double value){
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
                    //To know how many measures per interval is retrieving the sensor
                    //Toast.makeText(activity.getApplicationContext(),"n= "+n,Toast.LENGTH_SHORT).show();
                    firstValue=true;
                    average=acum/n;
                    androidHeight = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, average);

                    //To check measures, save them to a file
                    FileUtil.saveToFile(0,average,0);

                    activity.updatePressureUI(average, androidHeight, 0,0);
                    average = 0;
                    n=0;
                    acum=0;
                }
            }.start();
        }
    }
}
