package com.example.joan.wp2;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;

import ch.skywatch.windoo.api.JDCWindooEvent;
import ch.skywatch.windoo.api.JDCWindooManager;
import ch.skywatch.windoo.api.JDCWindooMeasurement;

/**
 * Created by joan.sansa.melsion on 18/04/2018.
 * https://gist.github.com/jdc-electronic/4eb9b585bf27e5a222da
 */

public class WindooSensorClass implements Observer {
    private JDCWindooManager jdcWindooManager;
    private JDCWindooMeasurement measurement;
    private MainActivity activity;
    private Double pressureGlobal;

    public WindooSensorClass(MainActivity activity){
        this.activity=activity;
        this.jdcWindooManager = JDCWindooManager.getInstance();
    }

    public void start(){
        jdcWindooManager.addObserver(this);
        jdcWindooManager.enable(activity);
    }

    public void stop(){
        jdcWindooManager.disable(activity);
        jdcWindooManager.deleteObserver(this);
    }

    @Override
    public void update(Observable observable, final Object object) {
        activity.runOnUiThread(new Runnable() {
            public void run() {
                JDCWindooEvent event = (JDCWindooEvent) object;
                if (event.getType() == JDCWindooEvent.JDCWindooAvailable) {
                    Log.d("WindooSensorClass", "Windoo available");
                } else if (event.getType() == JDCWindooEvent.JDCWindooNewPressureValue) {
                    double pressureValue= (double)event.getData();
                    Log.d("WindooSensorClass", "Pressure received : " + pressureValue);

                    //Two ways to get the value:
                    //activity.updatePressureUI(0,0,pressureValue);

                    measurement = jdcWindooManager.getLive();
                    pressureGlobal = measurement.getPressure();
                    activity.updatePressureUI(0,0,pressureGlobal);
                }

            }
        });
    }
}
