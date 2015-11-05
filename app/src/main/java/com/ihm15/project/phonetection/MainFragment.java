package com.ihm15.project.phonetection;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
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
public class MainFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Alarme alarme = new Alarme();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_card_view, container, false);

        getActivity().startService(new Intent(getActivity(), MotionReceiver.class));

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
                            Toast.makeText(getActivity(), getString(R.string.charger_detection_mode) + " is desactived", Toast.LENGTH_SHORT).show();
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

}
