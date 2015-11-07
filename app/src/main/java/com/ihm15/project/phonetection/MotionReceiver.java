package com.ihm15.project.phonetection;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by Dimitri on 05/11/2015.
 */
public class MotionReceiver extends IntentService implements SensorEventListener {

    private static String LOG_TAG = "CardViewActivity";


    //Détection des mouvements
    float motion = (float)5; //Constante pour définir l'intensité du mouvement
    float x,y,z = 0;
    float lx,ly,lz = 0;
    float lastUpdate = -1;
    long curtime = -1;

    public MotionReceiver() {
        super("MotionReceiver");
        Log.e(LOG_TAG, "BANANA");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        /*switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (isInMotion(event) && Library.DETECTION_MODE) {
                    Log.e(LOG_TAG, "Vous avez bougé !");

                    Library.WARNING_BY = 1;
                    Library.DETECTION_MODE = false;
                } //else if (!isInMotion(event)) {
                  //  alarme.cancelTimer();
                //}
                break;
        }*/
    }

    private boolean isInMotion(SensorEvent event) {
        curtime = System.currentTimeMillis();
        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if ((curtime-lastUpdate)>100) {
            lx=x;
            ly=y;
            lz=z;
            lastUpdate= curtime;
        }

        return ((Math.abs(lx-x)>motion) || (Math.abs(ly-y)>motion) || (Math.abs(lz-z)>motion));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onHandleIntent(Intent intent) {
        //Gestionnaire de capteur
        SensorManager sm = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);

        //Enregistre un listener pour le gestionnaire de capteur
        boolean accelSupport = sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if (!accelSupport) {
            sm.unregisterListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }
        Log.e(LOG_TAG, "SPLIT");
    }
}
