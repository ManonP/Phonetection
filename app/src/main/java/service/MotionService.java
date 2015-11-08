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

    private SensorManager sensorManager;
    private Sensor mAccelerometer;


    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sensorManager.registerListener(this,mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
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
                    Intent i = new Intent();
                    i.setClassName("com.ihm15.project.phonetection",
                            "com.ihm15.project.phonetection.CardViewActivity");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra(Data.EXTRA_WITH_ALARM, true);
                    i.putExtra(Data.EXTRA_MODE, Data.CHARGER_MODE);
                    Data.setMotionMode(false);
                    getBaseContext().startActivity(i);
                }
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

}
