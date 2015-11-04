package com.ihm15.project.phonetection;

import android.app.Activity;
import android.app.Fragment;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Dimitri on 03/11/2015.
 */
public class MainFragment extends Fragment implements SensorEventListener {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_card_view, container, false);

        //Initialisation de la synthèse vocale
        Library.textToSpeak = new TextToSpeak(getActivity());

        Library.DETECTION_MODE = false;
        Library.CABLE_MODE = false;
        Library.SIM_MODE = false;

        mRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);

        //Gestionnaire de capteur
        SensorManager sm = (SensorManager) getActivity().getSystemService(Activity.SENSOR_SERVICE);

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
        return v;
    }

    @Override
    public void onResume() {
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
                            Toast.makeText(getActivity(), getString(R.string.motion_detection_mode) + " is desactived", Toast.LENGTH_SHORT).show();
                        } else {
                            Library.DETECTION_MODE = true;
                            setDataMoveEnable();
                            Toast.makeText(getActivity(), getString(R.string.motion_detection_mode) + " is actived", Toast.LENGTH_SHORT).show();

                        }
                        break;

                    case 1:
                        if (Library.CABLE_MODE) {
                            Library.CABLE_MODE = false;
                            setDataCableDisable();
                            Toast.makeText(getActivity(), getString(R.string.charger_detection_mode) +" is desactived", Toast.LENGTH_SHORT).show();
                        } else {
                            if (Library.CONNECTED) {
                                Library.CABLE_MODE = true;
                                setDataCableEnable();
                                Toast.makeText(getActivity(), getString(R.string.charger_detection_mode) + " is actived", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "You must connect your phone to activate this mode", Toast.LENGTH_SHORT).show();
                                setDataCableDisable();
                            }
                        }
                        break;
                    case 2:
                        if (Library.SIM_MODE) {
                            Library.SIM_MODE = false;
                            setDataCableDisable();
                            Toast.makeText(getActivity(), getString(R.string.sim_detection_mode) + " is desactived", Toast.LENGTH_SHORT).show();
                        } else {
                            Library.SIM_MODE = true;
                            setDataCableEnable();
                            Toast.makeText(getActivity(), getString(R.string.sim_detection_mode) + " is actived", Toast.LENGTH_SHORT).show();
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
        DataObject obj = new DataObject(getResources().getColor(R.color.motion_card),
                getString(R.string.motion_detection_mode),
                getString(R.string.motion_detection_mode_description),
                getString(R.string.activate_button), R.drawable.moving_mode);
        return obj;
    }

    private DataObject setDataMoveEnable(){
        DataObject obj = new DataObject(getResources().getColor(R.color.motion_card),
                getString(R.string.motion_detection_mode),
                getString(R.string.motion_detection_mode_description),
                getString(R.string.deactivate_button), R.drawable.moving_mode);
        return obj;
    }

    private DataObject setDataCableDisable() {
        DataObject obj = new DataObject(getResources().getColor(R.color.charger_card),
                getString(R.string.charger_detection_mode),
                getString(R.string.charger_detection_mode_description),
                getString(R.string.activate_button), R.drawable.charger_mode);
        return obj;
    }

    private DataObject setDataCableEnable() {
        DataObject obj = new DataObject(getResources().getColor(R.color.charger_card),
                getString(R.string.charger_detection_mode),
                getString(R.string.charger_detection_mode_description),
                getString(R.string.deactivate_button), R.drawable.charger_mode);
        return obj;
    }

    private DataObject setDataSimDisable(){
        DataObject obj = new DataObject(getResources().getColor(R.color.SIM_card),
                getString(R.string.sim_detection_mode),
                getString(R.string.sim_detection_mode_description),
                getString(R.string.activate_button), R.drawable.sim_mode);
        return obj;
    }

    private DataObject setDataSimEnable(){
        DataObject obj = new DataObject(getResources().getColor(R.color.SIM_card),
                getString(R.string.sim_detection_mode),
                getString(R.string.sim_detection_mode_description),
                getString(R.string.deactivate_button), R.drawable.sim_mode);
        return obj;
    }

    //Detecte quand le téléphone bouge
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                if (isInMotion(event) && Library.DETECTION_MODE) {
                    //Log.e(LOG_TAG, "Vous avez bougé !");
                    alarme.activeWarning(getActivity());
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
