package service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ihm15.project.phonetection.Data;

import managers.AlertManager;

/**
 * Created by Dimitri on 05/11/2015.
 */
public class MotionService extends Service implements SensorEventListener {

    private static String LOG_TAG = "CardViewActivity";


    //Détection des mouvements
    float motion = (float)5; //Constante pour définir l'intensité du mouvement
    float x,y,z = 0;
    float lx,ly,lz = 0;
    float lastUpdate = -1;
    long curtime = -1;
    AlertManager alertManager;

    private SensorManager sensorManager;
    private Sensor mAccelerometer;

    /*public MotionService() {
        super("MotionService");
        alertManager = new AlertManager();
        Log.e(LOG_TAG, "BANANA");
    }*/

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        alertManager = new AlertManager();
        Data.getInstance(getApplicationContext());

    }

    @Override
    public void onDestroy() {
        sensorManager.unregisterListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (isInMotion(event) && Data.isMotionModeActivate()) {
                    Log.e(LOG_TAG, "Vous avez bougé !");
                    alertManager.startAlarm();
                } //else if (!isInMotion(event)) {
                  //  alarme.cancelTimer();
                //}
                break;
        }
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

    /*@Override
    protected void onHandleIntent(Intent intent) {
        //Gestionnaire de capteur
        SensorManager sm = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);

        //Enregistre un listener pour le gestionnaire de capteur
        boolean accelSupport = sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if (!accelSupport) {
            sm.unregisterListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }
        Log.e(LOG_TAG, "SPLIT");
    }*/
}
