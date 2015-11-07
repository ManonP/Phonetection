package com.ihm15.project.phonetection;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import adapters.CardViewAdapter;
import dialogs.ChangeImageDialogCardView;
import dialogs.ChangePatternDialogCardView;
import dialogs.ChangePinDialogCardView;
import events.LockSetObject;
import events.WrongLockSetObject;
import service.CableService;
import service.MotionService;

public class MainFragment extends Fragment implements View.OnClickListener,
        LockSetObject.LockSetEventListener, WrongLockSetObject.WrongLockSetEventListener{

    private ListView listView;
    private CardViewAdapter cva;

    private enum ModeStates{
        MODE_DEACTIVATED,
        MODE_ACTIVATED
    }

    private ModeStates motionState;
    private ModeStates chargerState;
    private ModeStates simState;
    private ModeStates smsState;
    private Context myContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_card_view, container, false);

        getActivity().startService(new Intent(getActivity(), MotionService.class));
        getActivity().startService(new Intent(getActivity(), CableService.class));
        //startService(new Intent(new Intent(this, MotionService.class)));

        Data.getInstance(getActivity());

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = (RecyclerView) v.findViewById(R.id.mode_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        cva = new CardViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(cva);

        myContext = getActivity().getBaseContext();
       // startBackgroundServiceCable();

        initModes();

        return v;
    }

    @Override
    public void onClick(View v) {
        switch((String) v.getTag()){
            case Data.MOTION_BUTTON_TAG:
                motionButtonClicked();
                break;
            case Data.CHARGER_BUTTON_TAG:
                chargerButtonClicked();
                break;
            case Data.SIM_BUTTON_TAG:
                simButtonClicked();
                break;
            case Data.SMS_BUTTON_TAG:
                smsButtonClicked();
                break;
        }
    }

    @Override
    public void onLockSet(LockSetObject.LockSetEvent ue) {
        switch(ue.getType()){
            case LockSetObject.LockSetEvent.MOTION_MODE:
                motionLockSet();
                break;
            case LockSetObject.LockSetEvent.CHARGER_MODE:
                chargerLockSet();
                break;
            case LockSetObject.LockSetEvent.SIM_MODE:
                simLockSet();
                break;
            case LockSetObject.LockSetEvent.SMS_MODE:
                smsLockSet();
                break;
        }
    }

    @Override
    public void onWrongLockSet(WrongLockSetObject.WrongLockSetEvent ue) {
        switch(ue.getType()){
            case LockSetObject.LockSetEvent.MOTION_MODE:
                motionWrongLockSet();
                break;
            case LockSetObject.LockSetEvent.CHARGER_MODE:
                chargerWrongLockSet();
                break;
            case LockSetObject.LockSetEvent.SIM_MODE:
                simWrongLockSet();
                break;
            case LockSetObject.LockSetEvent.SMS_MODE:
                smsWrongLockSet();
                break;
        }
    }


    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////
    private void initModes(){
        if (Data.isMotionModeActivate()) {
            motionState = ModeStates.MODE_ACTIVATED;
        } else {
            motionState = ModeStates.MODE_DEACTIVATED;
        }

        if (Data.isCableModeActivate()) {
            chargerState = ModeStates.MODE_ACTIVATED;
        } else {
            chargerState = ModeStates.MODE_DEACTIVATED;
        }

        if (Data.isSimModeActivate()) {
            simState = ModeStates.MODE_ACTIVATED;
        } else {
            simState = ModeStates.MODE_DEACTIVATED;
        }

        if (Data.isSmsModeActivate()){
            smsState = ModeStates.MODE_ACTIVATED;
        } else {
            smsState = ModeStates.MODE_DEACTIVATED;
        }


    }

    private void motionButtonClicked(){
        switch (motionState){
            case MODE_ACTIVATED:
                motionState = ModeStates.MODE_DEACTIVATED;

                Data.setMotionMode(false);

                cva.motionButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                String sl = Data.getSecurityLevel();
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                if (pin.equals(dPin)){
                    motionState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.MOTION_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.motionButtonDeactivated();
                } else if (((pattern == dPattern) && (sl.equals(msl)))
                        || ((pattern == dPattern) && (sl.equals(hsl)))){
                    motionState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.MOTION_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.motionButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    motionState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.MOTION_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.motionButtonDeactivated();
                } else {
                    motionState = ModeStates.MODE_ACTIVATED;

                    Data.setMotionMode(true);

                    cva.motionButtonActivated();
                }
                break;
        }
    }

    private void chargerButtonClicked(){
        switch (chargerState){
            case MODE_ACTIVATED:
                chargerState = ModeStates.MODE_DEACTIVATED;

                Data.setCableMode(false);

                cva.chargerButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                String sl = Data.getSecurityLevel();
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                if (pin.equals(dPin)){
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.CHARGER_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.chargerButtonDeactivated();
                } else if (((pattern == dPattern) && (sl.equals(msl)))
                        || ((pattern == dPattern) && (sl.equals(hsl)))){
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.CHARGER_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.chargerButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.CHARGER_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.chargerButtonDeactivated();
                } else {
                    chargerState = ModeStates.MODE_ACTIVATED;

                    Data.setCableMode(true);

                    cva.chargerButtonActivated();
                }
                break;
        }
    }

    private void simButtonClicked(){
        switch (simState){
            case MODE_ACTIVATED:
                simState = ModeStates.MODE_DEACTIVATED;

                Data.setSimMode(false);

                cva.simButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                String sl = Data.getSecurityLevel();
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                if (pin.equals(dPin)){
                    simState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.SIM_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.simButtonDeactivated();
                } else if (((pattern == dPattern) && (sl.equals(msl)))
                        || ((pattern == dPattern) && (sl.equals(hsl)))){
                    simState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.SIM_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.simButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    simState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.SIM_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.simButtonDeactivated();
                } else {
                    simState = ModeStates.MODE_ACTIVATED;

                    Data.setSimMode(true);

                    cva.simButtonActivated();
                }
                break;
        }
    }

    private void smsButtonClicked(){
        switch (smsState){
            case MODE_ACTIVATED:
                Log.d("", "BANANA_ACTIVATED");
                smsState = ModeStates.MODE_DEACTIVATED;

                Data.setSmsMode(false);

                cva.smsButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                String sl = Data.getSecurityLevel();
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                if (pin.equals(dPin)){
                    Log.d("", "BANANA: " + pin + ", " + dPin + ", " + pattern + ", " + dPattern + ", " + sl);
                    smsState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.SMS_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.smsButtonDeactivated();
                } else if (((pattern == dPattern) && (sl.equals(msl)))
                        || ((pattern == dPattern) && (sl.equals(hsl)))){
                    Log.d("", "PEACH: " + pin + ", " + dPin + ", " + pattern + ", " + dPattern + ", " + sl);
                    smsState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.SMS_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.smsButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    Log.d("", "STRAWBERRY: " + pin + ", " + dPin + ", " + pattern + ", " + dPattern + ", " + sl);
                    smsState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    LockSetObject.LockSetEvent.SMS_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.smsButtonDeactivated();
                } else {
                    Log.d("", "RASBERRY: " + pin + ", " + dPin + ", " + pattern + ", " + dPattern + ", " + sl);
                    smsState = ModeStates.MODE_ACTIVATED;

                    Data.setMotionMode(true);

                    cva.smsButtonActivated();
                }
                break;
        }
    }

    public void motionLockSet(){
        switch (motionState){
            case MODE_ACTIVATED:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Motion lock set error: motionState == MODE_ACTIVATED -> FORBIDDEN");
                break;
            case MODE_DEACTIVATED:
                motionState = ModeStates.MODE_ACTIVATED;

                Data.setMotionMode(true);

                cva.motionButtonActivated();
                break;
        }
    }

    public void chargerLockSet(){
        switch (chargerState){
            case MODE_ACTIVATED:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Charger lock set error: chargerState == MODE_ACTIVATED -> FORBIDDEN");
                break;
            case MODE_DEACTIVATED:
                chargerState = ModeStates.MODE_ACTIVATED;

                Data.setCableMode(true);

                cva.chargerButtonActivated();
                break;
        }
    }

    public void simLockSet(){
        switch (simState){
            case MODE_ACTIVATED:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Sim lock set error: simState == MODE_ACTIVATED -> FORBIDDEN");
                break;
            case MODE_DEACTIVATED:
                simState = ModeStates.MODE_ACTIVATED;

                Data.setSimMode(true);

                cva.simButtonActivated();
                break;
        }
    }

    public void smsLockSet(){
        switch (smsState){
            case MODE_ACTIVATED:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Sms lock set error: smsState == MODE_ACTIVATED -> FORBIDDEN");
                break;
            case MODE_DEACTIVATED:
                smsState = ModeStates.MODE_ACTIVATED;

                Data.setMotionMode(true);

                cva.smsButtonActivated();
                break;
        }
    }

    public void motionWrongLockSet(){}

    public void chargerWrongLockSet(){}

    public void simWrongLockSet(){}

    public void smsWrongLockSet(){}

    BroadcastReceiver cableBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("cable receiver", "ACTION_POWER_DISCONNECTED");
            Toast.makeText(myContext,"Power disconnected",Toast.LENGTH_SHORT).show();
           // myContext = context;
        }
    };

    void startBackgroundServiceCable() {
        //Enregistrement du BroadcastReceiver
        IntentFilter filter = new IntentFilter("android.intent.action.ACTION_POWER_DISCONNECTED");
        myContext.registerReceiver(cableBroadcastReceiver, filter);

        //Lancement du service
        Intent intent = new Intent(myContext, CableService.class);
        myContext.startService(intent);
    }

}
