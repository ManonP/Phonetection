package com.ihm15.project.phonetection;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.CardViewAdapter;
import dialogs.ChangeImageDialogCardView;
import dialogs.ChangePatternDialogCardView;
import dialogs.ChangePinDialogCardView;
import dialogs.DissuasiveDialog;
import dialogs.EnterImageDialog;
import dialogs.EnterPatternDialog;
import dialogs.EnterPinDialog;
import events.DissuasiveDialogSkipObject;
import events.LockSetObject;
import events.UnlockObject;
import events.WrongLockSetObject;
import events.WrongUnlockObject;
import service.CableService;
import service.MotionService;
import service.SmsService;

public class MainFragment extends Fragment implements View.OnClickListener,
        DissuasiveDialogSkipObject.DissuasiveDialogSkippedEventListener,
        UnlockObject.UnlockedEventListener, WrongUnlockObject.WrongUnlockedEventListener,
        LockSetObject.LockSetEventListener, WrongLockSetObject.WrongLockSetEventListener{

    private CardViewAdapter cva;

    private EnterPinDialog enterPinDialog;
    private EnterPatternDialog enterPatternDialog;
    private EnterImageDialog enterImageDialog;

    private enum ModeStates{
        MODE_DEACTIVATED,
        MODE_ACTIVATED
    }

    private ModeStates motionState;
    private ModeStates chargerState;
    private ModeStates simState;
    private ModeStates smsState;
    private Context myContext;

    private Timer graceTimeTimer;
    public enum States{
        IDLE,
        ON_DIALOG_BEFORE_UNLOCK,
        ON_PIN_UNLOCK,
        ON_PATTERN_UNLOCK,
        ON_IMAGE_UNLOCK
    }

    protected States state;
    protected int mistakes;
    private boolean alarmOn;
    private List<Integer> modeWhichStartTheAlarm;

    private MediaPlayer alarmMediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_card_view, container, false);

        alarmMediaPlayer = MediaPlayer.create(getActivity(), R.raw.honk);
        alarmMediaPlayer.setLooping(true);

        getActivity().startService(new Intent(getActivity(), MotionService.class));
        getActivity().startService(new Intent(getActivity(), CableService.class));
        getActivity().startService(new Intent(getActivity(), SmsService.class));

        Data.getInstance(getActivity());

        RecyclerView mRecyclerView;
        RecyclerView.LayoutManager mLayoutManager;

        mRecyclerView = (RecyclerView) v.findViewById(R.id.mode_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        cva = new CardViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(cva);

        myContext = getActivity();

        init();
        initModes();


        if (((CardViewActivity) getActivity()).isWithAlarm()) alarmTrigger(((CardViewActivity) getActivity()).getMode());
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
    public void onDissuasiveDialogSkipped(DissuasiveDialogSkipObject.DissuasiveDialogSkippedEvent ddse) {
        DissuasiveDialogSkipped();
    }

    @Override
    public void onUnlocked(UnlockObject.UnlockedEvent ue) {
        switch(ue.getType()){
            case Data.PIN_UNLOCK:
                PinUnlocked();
                break;
            case Data.PATTERN_UNLOCK:
                PatternUnlocked();
                break;
            case Data.IMAGE_UNLOCK:
                ImageUnlocked();
        }
    }

    @Override
    public void onWrongUnlocked(WrongUnlockObject.WrongUnlockedEvent wue) {
        switch(wue.getType()){
            case Data.WRONG_PIN_UNLOCK:
                PinWrongUnlocked();
                break;
            case Data.WRONG_PATTERN_UNLOCK:
                PatternWrongUnlocked();
                break;
            case Data.WRONG_IMAGE_UNLOCK:
                ImageWrongUnlocked();
        }
    }

    @Override
    public void onLockSet(LockSetObject.LockSetEvent ue) {
        switch(ue.getType()){
            case Data.MOTION_MODE:
                motionLockSet();
                break;
            case Data.CHARGER_MODE:
                chargerLockSet();
                break;
            case Data.SIM_MODE:
                simLockSet();
                break;
            case Data.SMS_MODE:
                smsLockSet();
                break;
        }
    }

    @Override
    public void onWrongLockSet(WrongLockSetObject.WrongLockSetEvent ue) {
        switch(ue.getType()){
            case Data.MOTION_MODE:
                motionWrongLockSet();
                break;
            case Data.CHARGER_MODE:
                chargerWrongLockSet();
                break;
            case Data.SIM_MODE:
                simWrongLockSet();
                break;
            case Data.SMS_MODE:
                smsWrongLockSet();
                break;
        }
    }



    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////
    private void init(){
        state = States.IDLE;
        mistakes = 0;
        alarmOn = false;
        modeWhichStartTheAlarm = new ArrayList<>();
    }

    private void initModes(){
        if (Data.isMotionModeActivate()) {
            motionState = ModeStates.MODE_ACTIVATED;
            Log.d("", "DEBUG: MOTION STATE ACTIVATED");
        } else {
            motionState = ModeStates.MODE_DEACTIVATED;
            Log.d("", "DEBUG: MOTION STATE DEACTIVATED");
        }

        if (Data.isCableModeActivate()) {
            chargerState = ModeStates.MODE_ACTIVATED;
            Log.d("", "DEBUG: CHARGER STATE ACTIVATED");
        } else {
            chargerState = ModeStates.MODE_DEACTIVATED;
            Log.d("", "DEBUG: CHARGER STATE DEACTIVATED");
        }

        if (Data.isSimModeActivate()) {
            simState = ModeStates.MODE_ACTIVATED;
            Log.d("", "DEBUG: SIM STATE ACTIVATED");
        } else {
            simState = ModeStates.MODE_DEACTIVATED;
            Log.d("", "DEBUG: SIM STATE DEACTIVATED");
        }

        if (Data.isSmsModeActivate()){
            smsState = ModeStates.MODE_ACTIVATED;
            Log.d("", "DEBUG: SMS STATE ACTIVATED");
        } else {
            smsState = ModeStates.MODE_DEACTIVATED;
            Log.d("", "DEBUG: SMS STATE DEACTIVATED");
        }
    }



    private void motionButtonClicked(){
        switch (motionState){
            case MODE_ACTIVATED:
                Log.d("", "DEBUG: MOTION BUTTON CLICKED MODE ACTIVATED");
                motionState = ModeStates.MODE_DEACTIVATED;

                Data.setMotionMode(false);

                cva.motionButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                Log.d("", "DEBUG: MOTION BUTTON CLICKED MODE DEACTIVATED");
                String sl = Data.getSecurityLevel();
                String lsl = getActivity().getString(R.string.pref_security_level_low);
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                Log.d("", "    DEBUG: MOTION BUTTON CLICKED [SL] -> " + sl + ", " + lsl + ", " + msl + ", " + hsl);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                Log.d("", "    DEBUG: MOTION BUTTON CLICKED [PIN] -> " + pin + ", " + dPin);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                Log.d("", "    DEBUG: MOTION BUTTON CLICKED [PATTERN] -> " + pattern + ", " + dPattern);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                Log.d("", "    DEBUG: MOTION BUTTON CLICKED [IMAGE] -> " + i + ", " + di);
                if (pin.equals(dPin)){
                    Log.d("", "DEBUG: MOTION BUTTON CLICKED -> PIN = DEFAULT_PIN");
                    motionState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    Data.MOTION_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.motionButtonDeactivated();
                } else if ((pattern == dPattern) && (!sl.equals(lsl))){
                    Log.d("", "DEBUG: MOTION BUTTON CLICKED -> PATTERN = DEFAULT_PATTERN");
                    motionState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    Data.MOTION_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.motionButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    Log.d("", "DEBUG: MOTION BUTTON CLICKED -> IMAGE = DEFAULT_IMAGE");
                    motionState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    Data.MOTION_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.motionButtonDeactivated();
                } else {
                    Log.d("", "DEBUG: MOTION BUTTON CLICKED -> OTHER");
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
                Log.d("", "DEBUG: CHARGER BUTTON CLICKED MODE ACTIVATED");
                chargerState = ModeStates.MODE_DEACTIVATED;

                Data.setCableMode(false);

                cva.chargerButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                Log.d("", "DEBUG: CHARGER BUTTON CLICKED MODE DEACTIVATED");
                String sl = Data.getSecurityLevel();
                String lsl = getActivity().getString(R.string.pref_security_level_low);
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                Log.d("", "    DEBUG: CHARGER BUTTON CLICKED [SL] -> " + sl + ", " + lsl + ", " + msl + ", " + hsl);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                Log.d("", "    DEBUG: CHARGER BUTTON CLICKED [PIN] -> " + pin + ", " + dPin);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                Log.d("", "    DEBUG: CHARGER BUTTON CLICKED [PATTERN] -> " + pattern + ", " + dPattern);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                Log.d("", "    DEBUG: CHARGER BUTTON CLICKED [IMAGE] -> " + i + ", " + di);
                if (pin.equals(dPin)){
                    Log.d("", "DEBUG: CHARGER BUTTON CLICKED -> PIN = DEFAULT_PIN");
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    Data.CHARGER_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.chargerButtonDeactivated();
                } else if ((pattern == dPattern) && (!sl.equals(lsl))){
                    Log.d("", "DEBUG: CHARGER BUTTON CLICKED -> PATTERN = DEFAULT_PATTERN");
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    Data.CHARGER_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.chargerButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    Log.d("", "DEBUG: CHARGER BUTTON CLICKED -> IMAGE = DEFAULT_IMAGE");
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    Data.CHARGER_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.chargerButtonDeactivated();
                } else {
                    Log.d("", "DEBUG: CHARGER BUTTON CLICKED -> OTHER");
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
                Log.d("", "DEBUG: SIM BUTTON CLICKED MODE ACTIVATED");
                simState = ModeStates.MODE_DEACTIVATED;

                Data.setSimMode(false);

                cva.simButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                Log.d("", "DEBUG: SIM BUTTON CLICKED MODE DEACTIVATED");
                String sl = Data.getSecurityLevel();
                String lsl = getActivity().getString(R.string.pref_security_level_low);
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                Log.d("", "    DEBUG: SIM BUTTON CLICKED [SL] -> " + sl + ", " + lsl + ", " + msl + ", " + hsl);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                Log.d("", "    DEBUG: SIM BUTTON CLICKED [PIN] -> " + pin + ", " + dPin);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                Log.d("", "    DEBUG: SIM BUTTON CLICKED [PATTERN] -> " + pattern + ", " + dPattern);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                Log.d("", "    DEBUG: SIM BUTTON CLICKED [IMAGE] -> " + i + ", " + di);
                if (pin.equals(dPin)){
                    Log.d("", "DEBUG: SIM BUTTON CLICKED -> PIN = DEFAULT_PIN");
                    simState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    Data.SIM_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.simButtonDeactivated();
                } else if ((pattern == dPattern) && (!sl.equals(lsl))){
                    Log.d("", "DEBUG: SIM BUTTON CLICKED -> PATTERN = DEFAULT_PATTERN");
                    simState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    Data.SIM_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.simButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    Log.d("", "DEBUG: SIM BUTTON CLICKED -> IMAGE = DEFAULT_IMAGE");
                    simState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    Data.SIM_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.simButtonDeactivated();
                } else {
                    Log.d("", "DEBUG: SIM BUTTON CLICKED -> OTHER");
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
                Log.d("", "DEBUG: SMS BUTTON CLICKED MODE ACTIVATED");
                smsState = ModeStates.MODE_DEACTIVATED;

                Data.setSmsMode(false);

                cva.smsButtonDeactivated();
                break;
            case MODE_DEACTIVATED:
                Log.d("", "DEBUG: SMS BUTTON CLICKED MODE DEACTIVATED");
                String sl = Data.getSecurityLevel();
                String lsl = getActivity().getString(R.string.pref_security_level_low);
                String msl = getActivity().getString(R.string.pref_security_level_medium);
                String hsl = getActivity().getString(R.string.pref_security_level_high);
                Log.d("", "    DEBUG: SMS BUTTON CLICKED [SL] -> " + sl + ", " + lsl + ", " + msl + ", " + hsl);
                String pin = Data.getPin();
                String dPin = getActivity().getString(R.string.pref_pin_default);
                Log.d("", "    DEBUG: SMS BUTTON CLICKED [PIN] -> " + pin + ", " + dPin);
                int pattern = Data.getPattern();
                int dPattern = getActivity().getResources().getInteger(R.integer.pref_pattern_default);
                Log.d("", "    DEBUG: SMS BUTTON CLICKED [PATTERN] -> " + pattern + ", " + dPattern);
                String i = Data.getImage();
                String di = getActivity().getString(R.string.pref_image_default);
                Log.d("", "    DEBUG: SMS BUTTON CLICKED [IMAGE] -> " + i + ", " + di);
                if (pin.equals(dPin)){
                    Log.d("", "DEBUG: SMS BUTTON CLICKED -> PIN = DEFAULT_PIN");
                    smsState = ModeStates.MODE_DEACTIVATED;

                    ChangePinDialogCardView cid =
                            new ChangePinDialogCardView(getActivity(), this, this,
                                    Data.SMS_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pin");

                    cva.smsButtonDeactivated();
                } else if ((pattern == dPattern) && (!sl.equals(lsl))){
                    Log.d("", "DEBUG: SMS BUTTON CLICKED -> PATTERN = DEFAULT_PATTERN");
                    smsState = ModeStates.MODE_DEACTIVATED;

                    ChangePatternDialogCardView cid =
                            new ChangePatternDialogCardView(getActivity(), this, this,
                                    Data.SMS_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_pattern");

                    cva.smsButtonDeactivated();
                } else if ((i.equals(di)) && (sl.equals(hsl))){
                    Log.d("", "DEBUG: SMS BUTTON CLICKED -> PIN = DEFAULT_IMAGE");
                    smsState = ModeStates.MODE_DEACTIVATED;

                    ChangeImageDialogCardView cid =
                            new ChangeImageDialogCardView(getActivity(), this, this,
                                    Data.SMS_MODE);
                    cid.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "change_image");

                    cva.smsButtonDeactivated();
                } else {
                    Log.d("", "DEBUG: SMS BUTTON CLICKED -> OTHER");
                    smsState = ModeStates.MODE_ACTIVATED;

                    Data.setSmsMode(true);

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
                Log.d("", "DEBUG: MOTION LOCK SET MODE DEACTIVATED");
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
                Log.d("", "DEBUG: CHARGER LOCK SET MODE DEACTIVATED");
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
                Log.d("", "DEBUG: SIM LOCK SET MODE DEACTIVATED");
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
                Log.d("", "DEBUG: SMS LOCK SET MODE DEACTIVATED");
                smsState = ModeStates.MODE_ACTIVATED;

                Data.setSmsMode(true);

                cva.smsButtonActivated();
                break;
        }
    }

    public void motionWrongLockSet(){}

    public void chargerWrongLockSet(){}

    public void simWrongLockSet(){}

    public void smsWrongLockSet(){}

    public void alarmTrigger(int mode){
        switch (state){
            case IDLE:
                state = States.ON_DIALOG_BEFORE_UNLOCK;
                mistakes = 0;
                modeWhichStartTheAlarm.add(mode);

                DissuasiveDialog dissuasiveDialog = new DissuasiveDialog(
                        getActivity().getString(R.string.dissuasive_dialog),
                        getActivity().getString(R.string.dissuasive_dialog_message),
                        getActivity().getString(R.string.skip_button));
                dissuasiveDialog.addDissuasiveDialogSkipedEventListener(this);
                dissuasiveDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(),
                        "dissuasive_dialog");

                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                state = States.ON_DIALOG_BEFORE_UNLOCK;
                modeWhichStartTheAlarm.add(mode);
                break;
            case ON_PIN_UNLOCK:
                state = States.ON_PIN_UNLOCK;
                modeWhichStartTheAlarm.add(mode);
                break;
            case ON_PATTERN_UNLOCK:
                state = States.ON_PATTERN_UNLOCK;
                modeWhichStartTheAlarm.add(mode);
                break;
            case ON_IMAGE_UNLOCK:
                state = States.ON_IMAGE_UNLOCK;
                modeWhichStartTheAlarm.add(mode);
                break;
        }
    }

    public void DissuasiveDialogSkipped(){
        switch(state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Dissuasive dialog skipped error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                state = States.ON_PIN_UNLOCK;
                enterPinDialog = new EnterPinDialog(getActivity(), null);
                enterPinDialog.addUnlockedEventListener(this);
                enterPinDialog.addWrongUnlockedEventListener(this);
                enterPinDialog.setRightPin(Data.getPin());
                enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pin_dialog");

                startGraceTimeTimer();
                break;
            case ON_PIN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Dissuasive dialog skipped error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Dissuasive dialog skipped error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Dissuasive dialog skipped error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
        }
    }

    public void PinUnlocked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin unlocked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                if(Data.getSecurityLevel().equals(getActivity().getResources().
                        getString(R.string.pref_security_level_low))){
                    state = States.IDLE;
                    mistakes = 0;
                    alarmOn = false;

                    stopAlarm();
                    stopGraceTimeTimer();
                    disableModesWhichStartedTheAlarm();
                    modeWhichStartTheAlarm = new ArrayList<>();
                } else {
                    state = States.ON_PATTERN_UNLOCK;

                    enterPinDialog.lightAlarmOff();
                    enterPatternDialog = new EnterPatternDialog(getActivity(), null);
                    enterPatternDialog.addUnlockedEventListener(this);
                    enterPatternDialog.addWrongUnlockedEventListener(this);
                    enterPatternDialog.setRightPattern(Data.getPattern());
                    enterPatternDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pattern_dialog");
                    if (Data.isLightAlarmActivate() && alarmOn) enterPatternDialog.lightAlarmOn();
                }
                break;
            case ON_PATTERN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
        }
    }

    public void PinWrongUnlocked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                if (mistakes == 2){
                    state = States.ON_PIN_UNLOCK;
                    alarmOn = true;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(100);
                    enterPinDialog = new EnterPinDialog(getActivity(), null);
                    enterPinDialog.addUnlockedEventListener(this);
                    enterPinDialog.addWrongUnlockedEventListener(this);
                    enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pin_dialog");
                    enterPinDialog.setRightPin(Data.getPin());
                    startAlarm();
                    stopGraceTimeTimer();

                    if (Data.isLightAlarmActivate()) enterPinDialog.lightAlarmOn();
                } else {
                    state = States.ON_PIN_UNLOCK;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterPinDialog = new EnterPinDialog(getActivity(), null);
                    enterPinDialog.addUnlockedEventListener(this);
                    enterPinDialog.addWrongUnlockedEventListener(this);
                    enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pin_dialog");
                    enterPinDialog.setRightPin(Data.getPin());
                    if (Data.isLightAlarmActivate() && alarmOn) enterPinDialog.lightAlarmOn();
                }
                break;
            case ON_PATTERN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
        }
    }

    public void PatternUnlocked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pattern unlocked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                if(Data.getSecurityLevel().equals(getActivity().getResources().
                        getString(R.string.pref_security_level_medium))){
                    state = States.IDLE;
                    mistakes = 0;
                    alarmOn = false;

                    enterPatternDialog.lightAlarmOff();
                    stopAlarm();
                    stopGraceTimeTimer();
                    disableModesWhichStartedTheAlarm();
                    modeWhichStartTheAlarm = new ArrayList<>();
                } else {
                    state = States.ON_IMAGE_UNLOCK;

                    enterPatternDialog.lightAlarmOff();
                    enterImageDialog = new EnterImageDialog(getActivity(), null);
                    enterImageDialog.addUnlockedEventListener(this);
                    enterImageDialog.addWrongUnlockedEventListener(this);
                    enterImageDialog.setRightImage(Data.getImage());
                    enterImageDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_image_dialog");
                    if (Data.isLightAlarmActivate() && alarmOn) enterImageDialog.lightAlarmOn();
                }
                break;
            case ON_IMAGE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
        }
    }

    public void PatternWrongUnlocked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pattern wrong unlocked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                if (mistakes == 2){
                    Log.println(Log.DEBUG, "", "WRONG PATTERN WITH ALARM");

                    state = States.ON_PATTERN_UNLOCK;
                    alarmOn = true;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterPatternDialog = new EnterPatternDialog(getActivity(), null);
                    enterPatternDialog.addUnlockedEventListener(this);
                    enterPatternDialog.addWrongUnlockedEventListener(this);
                    enterPatternDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pattern_dialog");
                    enterPatternDialog.setRightPattern(Data.getPattern());
                    startAlarm();
                    stopGraceTimeTimer();
                    if (Data.isLightAlarmActivate()) enterPatternDialog.lightAlarmOn();
                } else {
                    Log.println(Log.DEBUG, "", "WRONG PATTERN NO ALARM");
                    state = States.ON_PATTERN_UNLOCK;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterPatternDialog = new EnterPatternDialog(getActivity(), null);
                    enterPatternDialog.addUnlockedEventListener(this);
                    enterPatternDialog.addWrongUnlockedEventListener(this);
                    enterPatternDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pattern_dialog");
                    enterPatternDialog.setRightPattern(Data.getPattern());
                    if (Data.isLightAlarmActivate() && alarmOn) enterPatternDialog.lightAlarmOn();
                }
                break;
            case ON_IMAGE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pattern wrong unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
        }
    }

    public void ImageUnlocked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Imagd unlocked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                state = States.IDLE;
                mistakes = 0;
                alarmOn = false;

                enterImageDialog.lightAlarmOff();
                stopAlarm();
                stopGraceTimeTimer();
                disableModesWhichStartedTheAlarm();
                modeWhichStartTheAlarm = new ArrayList<>();
                break;
        }
    }

    public void ImageWrongUnlocked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Image wrong unlocked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Image wrong unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Image wrong unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                if (mistakes == 2){
                    state = States.ON_IMAGE_UNLOCK;
                    alarmOn = true;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterImageDialog = new EnterImageDialog(getActivity(), null);
                    enterImageDialog.addUnlockedEventListener(this);
                    enterImageDialog.addWrongUnlockedEventListener(this);
                    enterImageDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_image_dialog");
                    enterImageDialog.setRightImage(Data.getImage());
                    startAlarm();
                    stopGraceTimeTimer();
                    if (Data.isLightAlarmActivate()) enterImageDialog.lightAlarmOn();
                } else {
                    state = States.ON_IMAGE_UNLOCK;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterImageDialog = new EnterImageDialog(getActivity(), null);
                    enterImageDialog.addUnlockedEventListener(this);
                    enterImageDialog.addWrongUnlockedEventListener(this);
                    enterImageDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_image_dialog");
                    enterImageDialog.setRightImage(Data.getImage());
                    if (Data.isLightAlarmActivate() && alarmOn) enterImageDialog.lightAlarmOn();

                }
                break;
        }
    }

    public void graceTimeTimerTicked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Grace time timer ticked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_DIALOG_BEFORE_UNLOCK:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Grace time timer ticked error: state == ON_DIALOG_BEFORE_UNLOCK -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                if (alarmOn) {
                    state = States.ON_PIN_UNLOCK;

                    stopGraceTimeTimer();
                } else {
                    state = States.ON_PIN_UNLOCK;
                    alarmOn = true;

                    if(Data.isLightAlarmActivate()) enterPinDialog.lightAlarmOn();
                    startAlarm();
                    stopGraceTimeTimer();
                }
                break;
            case ON_PATTERN_UNLOCK:
                if (alarmOn) {
                    state = States.ON_PATTERN_UNLOCK;

                    stopGraceTimeTimer();
                } else {
                    state = States.ON_PATTERN_UNLOCK;
                    alarmOn = true;

                    if(Data.isLightAlarmActivate()) enterPatternDialog.lightAlarmOn();
                    startAlarm();
                    stopGraceTimeTimer();
                }
                break;
            case ON_IMAGE_UNLOCK:
                if (alarmOn) {
                    state = States.ON_IMAGE_UNLOCK;

                    stopGraceTimeTimer();
                } else {
                    state = States.ON_IMAGE_UNLOCK;
                    alarmOn = true;

                    if(Data.isLightAlarmActivate()) enterImageDialog.lightAlarmOn();
                    startAlarm();
                    stopGraceTimeTimer();
                }
                break;
        }
    }



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

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    public void startAlarm() {
        Log.println(Log.DEBUG, "", "ALARM START");

        AudioManager mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_PLAY_SOUND);
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        alarmMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        alarmMediaPlayer.start();
    }

    public void stopAlarm(){
        Log.println(Log.DEBUG, "", "ALARM STOP");
        alarmMediaPlayer.stop();
    }

    public void startGraceTimeTimer(){
        Log.println(Log.DEBUG, "", "GRACE TIMER START");
        graceTimeTimer = new Timer();
        graceTimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                graceTimeTimerTicked();
            }
        }, Data.getGraceTime());
    }

    public void stopGraceTimeTimer(){
        Log.println(Log.DEBUG, "", "GRACE TIMER STOP");
        if (graceTimeTimer != null) graceTimeTimer.cancel();
    }

    public void disableModesWhichStartedTheAlarm(){
        for (Integer i : modeWhichStartTheAlarm){
            switch(i){
                case Data.MOTION_MODE:
                    motionState = ModeStates.MODE_DEACTIVATED;

                    Data.setMotionMode(false);

                    cva.motionButtonDeactivated();
                    break;
                case Data.CHARGER_MODE:
                    chargerState = ModeStates.MODE_DEACTIVATED;

                    Data.setCableMode(false);

                    cva.chargerButtonDeactivated();
                    break;
                case Data.SIM_MODE:
                    simState = ModeStates.MODE_DEACTIVATED;

                    Data.setSimMode(false);

                    cva.simButtonDeactivated();
                    break;
                case Data.SMS_MODE:
                    smsState = ModeStates.MODE_DEACTIVATED;

                    Data.setSmsMode(false);

                    cva.smsButtonDeactivated();
                    break;
            }
        }
    }
}
