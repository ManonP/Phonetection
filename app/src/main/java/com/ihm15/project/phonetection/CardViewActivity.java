package com.ihm15.project.phonetection;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Manon on 17/10/2015.
 */
public class CardViewActivity extends AppCompatActivity implements SensorEventListener{

    public static Boolean DETECTION_MODE;
    public static Boolean CABLE_MODE;
    public static Boolean SIM_MODE;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "CardViewActivity";

    private MediaPlayer player;

    float motion = (float)2; //Constante pour définir l'intensité du mouvmeent
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

        DETECTION_MODE = false;
        CABLE_MODE = false;
        SIM_MODE = false;

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new MyRecyclerViewAdapter
                .MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.i(LOG_TAG, "Clicked on Item " + position);
                switch (position) {
                    case 0 :
                        if (DETECTION_MODE) {
                            DETECTION_MODE = false;
                            setDataMoveDisable();
                            Toast.makeText(getApplicationContext(),"Le mode détection de mouvement est maintenant désactivé", Toast.LENGTH_SHORT).show();
                        } else {
                            DETECTION_MODE = true;
                            setDataMoveEnable();
                            Toast.makeText(getApplicationContext(),"Le mode détection de mouvement est maintenant activé", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case 1 :
                        if (CABLE_MODE) {
                            CABLE_MODE = false;
                            setDataCableDisable();
                            Toast.makeText(getApplicationContext(), "Le mode cable est maintenant désactivé", Toast.LENGTH_SHORT).show();
                        } else {
                            CABLE_MODE = true;
                            setDataCableEnable();
                            Toast.makeText(getApplicationContext(), "Le mode cable est maintenant activé", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case 2 :
                        if (SIM_MODE) {
                            SIM_MODE = false;
                            setDataCableDisable();
                            Toast.makeText(getApplicationContext(), "Le mode SIM est maintenant désactivé", Toast.LENGTH_SHORT).show();
                        } else {
                            SIM_MODE = true;
                            setDataCableEnable();
                            Toast.makeText(getApplicationContext(), "Le mode SIM est maintenant activé", Toast.LENGTH_SHORT).show();
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
        if (DETECTION_MODE) {
            results.add(0,setDataMoveEnable());
        } else {
            results.add(0,setDataMoveDisable());
        }

        if (CABLE_MODE) {
            results.add(1, setDataCableEnable());
        } else {
            results.add(1, setDataCableDisable());
        }

        if (SIM_MODE){
            results.add(2, setDataSimEnable());
        } else {
            results.add(2, setDataSimDisable());
        }



       /* for (int index = 0; index < 20; index ++) {
            DataObject obj = new DataObject("Some Primary Text " + index,
                    "Secondary " + index);
            results.add(index,obj);
        }*/
        return results;
    }

    private DataObject setDataMoveDisable(){
        DataObject obj = new DataObject("Déplacement de l'appareil",
                "Désactivé", R.drawable.move150);
        return obj;
    }

    private DataObject setDataMoveEnable(){
        DataObject obj = new DataObject("Déplacement de l'appareil",
                "Activé", R.drawable.move150);
        return obj;
    }

    private DataObject setDataCableDisable() {
        DataObject obj = new DataObject("Appareil débranché",
                "Désactivé", R.drawable.cable150);
        return obj;
    }

    private DataObject setDataCableEnable() {
        DataObject obj = new DataObject("Appareil débranché",
                "Activé", R.drawable.cable150);
        return obj;
    }

    private DataObject setDataSimDisable(){
        DataObject obj = new DataObject("Changement de SIM",
                "Désactivé", R.drawable.sim150);
        return obj;
    }

    private DataObject setDataSimEnable(){
        DataObject obj = new DataObject("Changement de SIM",
                "Activé", R.drawable.sim150);
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
                if (isInMotion(event) && DETECTION_MODE) {
                    Log.e(LOG_TAG, "Vous avez bougé !");
                    player = MediaPlayer.create(getBaseContext(),R.raw.pompier);
                    player.start();

                    //Toast.makeText(getApplicationContext(), "Vous avez bougé !", Toast.LENGTH_LONG).show();
                };
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
