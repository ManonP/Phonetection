package com.ihm15.project.phonetection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Manon on 17/10/2015.
 */
public class CardViewActivity extends AppCompatActivity implements SensorEventListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    private Alarme alarme = new Alarme();

    //Détection des mouvements
    float motion = (float)5; //Constante pour définir l'intensité du mouvement
    float x,y,z = 0;
    float lx,ly,lz = 0;
    float lastUpdate = -1;
    long curtime = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        //Initialisation de la synthèse vocale
        Library.textToSpeak = new TextToSpeak(getBaseContext());

        Library.DETECTION_MODE = false;
        Library.CABLE_MODE = false;
        Library.SIM_MODE = false;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        //Gestionnaire de capteur
        SensorManager sm = (SensorManager)getSystemService(SENSOR_SERVICE);

        //Enregistre un listener pour le gestionnaire de capteur
        boolean accelSupport = sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
        if (!accelSupport) {
            sm.unregisterListener(this,sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        }

        /*NotificationCompat.Builder mBuilder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notification)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                switch (position) {
                    case 0:
                        if (Library.DETECTION_MODE) {
                            Library.DETECTION_MODE = false;
                            setDataMoveDisable();
                            Toast.makeText(getApplicationContext(), getString(R.string.motion_detection_mode) + " is desactived", Toast.LENGTH_SHORT).show();
                        } else {
                            Library.DETECTION_MODE = true;
                            setDataMoveEnable();
                            Toast.makeText(getApplicationContext(), getString(R.string.motion_detection_mode) + " is actived", Toast.LENGTH_SHORT).show();

                        }
                        break;

                    case 1:
                        if (Library.CABLE_MODE) {
                            Library.CABLE_MODE = false;
                            setDataCableDisable();
                            Toast.makeText(getApplicationContext(), getString(R.string.charger_detection_mode) +" is desactived", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Library.CONNECTED) {
                                Library.CABLE_MODE = true;
                                setDataCableEnable();
                                Toast.makeText(getApplicationContext(), getString(R.string.charger_detection_mode) + " is actived", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "You must connect your phone to activate this mode", Toast.LENGTH_SHORT).show();
                                setDataCableDisable();
                            }
                        }
                        break;
                    case 2:
                        if (Library.SIM_MODE) {
                            Library.SIM_MODE = false;
                            setDataCableDisable();
                            Toast.makeText(getApplicationContext(), getString(R.string.sim_detection_mode) + " is desactived", Toast.LENGTH_SHORT).show();
                        } else {
                            Library.SIM_MODE = true;
                            setDataCableEnable();
                            Toast.makeText(getApplicationContext(), getString(R.string.sim_detection_mode) + " is actived", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                changeCard();
            }
        });
    }

    private void changeCard() {
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
    }
    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();
        if (Library.DETECTION_MODE) {
            results.add(0,setDataMoveEnable());
        } else {
            results.add(0,setDataMoveDisable());
        }

        if (Library.CABLE_MODE) {
            results.add(1, setDataCableEnable());
        } else {
            results.add(1, setDataCableDisable());
        }

        if (Library.SIM_MODE){
            results.add(2, setDataSimEnable());
        } else {
            results.add(2, setDataSimDisable());
        }

        return results;
    }

    private DataObject setDataMoveDisable(){
        DataObject obj = new DataObject(getString(R.string.motion_detection_mode),
                getString(R.string.motion_detection_mode_description),
                "Desactivate", R.drawable.move150);
        return obj;
    }

    private DataObject setDataMoveEnable(){
        DataObject obj = new DataObject(getString(R.string.motion_detection_mode),
                getString(R.string.motion_detection_mode_description),
                "Activate", R.drawable.move150);
        return obj;
    }

    private DataObject setDataCableDisable() {
        DataObject obj = new DataObject(getString(R.string.charger_detection_mode),
                getString(R.string.charger_detection_mode_description),
                "Desactivate", R.drawable.cable150);
        return obj;
    }

    private DataObject setDataCableEnable() {
        DataObject obj = new DataObject(getString(R.string.charger_detection_mode),
                getString(R.string.charger_detection_mode_description),
                "Activate", R.drawable.cable150);
        return obj;
    }

    private DataObject setDataSimDisable(){
        DataObject obj = new DataObject(getString(R.string.sim_detection_mode),
                getString(R.string.sim_detection_mode_description),
                "Desactivate", R.drawable.sim150);
        return obj;
    }

    private DataObject setDataSimEnable(){
        DataObject obj = new DataObject(getString(R.string.sim_detection_mode),
                getString(R.string.sim_detection_mode_description),
                "Activate", R.drawable.sim150);
        return obj;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Detecte quand le téléphone bouge
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (isInMotion(event) && Library.DETECTION_MODE) {
                    //Log.e(LOG_TAG, "Vous avez bougé !");
                    alarme.activeWarning(getApplicationContext());
                    Library.WARNING_BY =1;
                    Library.DETECTION_MODE = false;
                } /*else if (!isInMotion(event)) {
                    alarme.cancelTimer();
                }*/
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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
