package managers;

import android.app.Activity;
import android.app.Fragment;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ihm15.project.phonetection.R;

import java.util.Timer;
import java.util.TimerTask;

import dialogs.EnterImageDialog;
import dialogs.EnterPatternDialog;
import dialogs.EnterPinDialog;
import events.UnlockObject;
import events.WrongUnlockObject;

public class AlertManager extends Fragment implements UnlockObject.UnlockedEventListener,
        WrongUnlockObject.WrongUnlockedEventListener{

    private EnterPinDialog enterPinDialog;
    private EnterPatternDialog enterPatternDialog;
    private EnterImageDialog enterImageDialog;

    int alarmVolume;
    boolean lightAlarm;
    int graceTime;
    String securityLevel;
    String pin;
    int pattern;
    String image;

    Timer graceTimeTimer;

    public enum States{
        IDLE,
        ON_PIN_UNLOCK,
        ON_PATTERN_UNLOCK,
        ON_IMAGE_UNLOCK,
        UNLOCKED
    }

    protected States state;
    protected int mistakes;
    private boolean alarmOn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.test_layout, container, false);

        Button b = (Button) v.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlarmStart();
            }
        });
        init();

        KeyguardManager keyguardManager = (KeyguardManager) getActivity().getSystemService(Activity.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock lock = keyguardManager.newKeyguardLock(Activity.KEYGUARD_SERVICE);
        lock.disableKeyguard();

        return v;
    }

    @Override
    public void onUnlocked(UnlockObject.UnlockedEvent ue) {
        switch(ue.getType()){
            case UnlockObject.UnlockedEvent.PIN_UNLOCK:
                PinUnlocked();
                break;
            case UnlockObject.UnlockedEvent.PATTERN_UNLOCK:
                PatternUnlocked();
                break;
            case UnlockObject.UnlockedEvent.IMAGE_UNLOCK:
                ImageUnlocked();
        }
    }

    @Override
    public void onWrongUnlocked(WrongUnlockObject.WrongUnlockedEvent wue) {
        switch(wue.getType()){
            case WrongUnlockObject.WrongUnlockedEvent.PIN_WRONG_UNLOCK:
                PinWrongUnlocked();
                break;
            case WrongUnlockObject.WrongUnlockedEvent.PATTERN_WRONG_UNLOCK:
                PatternWrongUnlocked();
                break;
            case WrongUnlockObject.WrongUnlockedEvent.IMAGE_WRONG_UNLOCK:
                ImageWrongUnlocked();
        }
    }

    //SEEHEIM-NOYAU FONCTIONEL//////////////////////////////////////////////////////////////////////
    private void loadPreferences(){
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity());
        loadAlarmVolume(sharedPreferences);
        loadLightAlarm(sharedPreferences);
        loadGraceTime(sharedPreferences);
        loadSecurityLevel(sharedPreferences);
        loadPin(sharedPreferences);
        loadPattern(sharedPreferences);
        loadImage(sharedPreferences);
    }

    private void loadAlarmVolume(SharedPreferences sharedPreferences){
        alarmVolume = sharedPreferences.getInt(
                getActivity().getString(R.string.pref_key_alarm_volume),
                getActivity().getResources().getInteger(R.integer.pref_alarm_volume_default));
    }

    private void loadLightAlarm(SharedPreferences sharedPreferences){
        /*lightAlarm = sharedPreferences.getBoolean(
                getActivity().getBaseContext().getString(R.string.pref_key_pattern),
                getActivity().getResources().getBoolean(R.bool.pref_light_alarm_default));*/
        lightAlarm = true;
    }

    private void loadGraceTime(SharedPreferences sharedPreferences){
        graceTime = sharedPreferences.getInt(
                getActivity().getString(R.string.pref_key_grace_time),
                getActivity().getResources().getInteger(R.integer.pref_grace_time_default));
    }

    private void loadSecurityLevel(SharedPreferences sharedPreferences){
        securityLevel = sharedPreferences.getString(
                getActivity().getString(R.string.pref_key_security_level),
                getActivity().getString(R.string.pref_security_level_default));
    }

    private void loadPin(SharedPreferences sharedPreferences){
        pin = sharedPreferences.getString(
                getActivity().getString(R.string.pref_key_pin),
                getActivity().getString(R.string.pref_pin_default));
    }

    private void loadPattern(SharedPreferences sharedPreferences){
        pattern = sharedPreferences.getInt(
                getActivity().getString(R.string.pref_key_pattern),
                getActivity().getResources().getInteger(R.integer.pref_pattern_default));
    }
    private void loadImage(SharedPreferences sharedPreferences){
        image = sharedPreferences.getString(
                getActivity().getString(R.string.pref_key_image),
                getActivity().getString(R.string.pref_image_default));
        Log.println(Log.DEBUG, "", "LOAD IMAGE: " + image);
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    private void init(){
        state = States.IDLE;
        mistakes = 0;
        alarmOn = false;

        loadPreferences();
    }

    public void AlarmStart(){
        Log.println(Log.DEBUG, "", "ALARM START");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "AL: IDLE");
                state = States.ON_PIN_UNLOCK;
                mistakes = 0;

                loadPreferences();
                enterPinDialog = new EnterPinDialog(getActivity(), null);
                enterPinDialog.addUnlockedEventListener(this);
                enterPinDialog.addWrongUnlockedEventListener(this);
                enterPinDialog.setRightPin(pin);
                enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pin_dialog");

                startGraceTimeTimer();

                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "AL: ON_PIN_UNLOCK");
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "AL: ON_PATTERN_UNLOCK");
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "AL: ON_IMAGE_UNLOCK");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "AL: UNLOCKED");
                break;
        }
    }

    public void PinUnlocked(){
        Log.println(Log.DEBUG, "", "PIN_UNLOCKED");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "PU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "PU: ON_PIN_UNLOCK");
                if(securityLevel.equals(getActivity().getResources().
                        getString(R.string.pref_security_level_low))){
                    state = States.UNLOCKED;
                    mistakes = 0;
                    alarmOn = false;

                    enterPinDialog.lightAlarmOff();
                    stopAlarm();
                    stopGraceTimeTimer();
                    //TODO revenir au fragment principal
                } else {
                    state = States.ON_PATTERN_UNLOCK;

                    enterPinDialog.lightAlarmOff();
                    enterPatternDialog = new EnterPatternDialog(getActivity(), null);
                    enterPatternDialog.addUnlockedEventListener(this);
                    enterPatternDialog.addWrongUnlockedEventListener(this);
                    enterPatternDialog.setRightPattern(pattern);
                    enterPatternDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pattern_dialog");
                    if (lightAlarm && alarmOn) enterPatternDialog.lightAlarmOn();
                }
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "PU: ON_PATTERN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "PU: ON_IMAGE_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "PU: UNLOCKED");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin unlocked error: state == UNLOCKED -> FORBIDDEN");
                break;
        }
    }

    public void PinWrongUnlocked(){
        Log.println(Log.DEBUG, "", "PIN_WRONG_UNLOCKED");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "PWU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "PWU: ON_PIN_UNLOCK");
                if (mistakes == 2){
                    Log.println(Log.DEBUG, "", "WRONG PIN WITH ALARM");

                    state = States.ON_PIN_UNLOCK;
                    alarmOn = true;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterPinDialog = new EnterPinDialog(getActivity(), null);
                    enterPinDialog.addUnlockedEventListener(this);
                    enterPinDialog.addWrongUnlockedEventListener(this);
                    enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pin_dialog");
                    enterPinDialog.setRightPin(pin);
                    startAlarm();
                    stopGraceTimeTimer();
                    if (lightAlarm) enterPinDialog.lightAlarmOn();
                } else {
                    Log.println(Log.DEBUG, "", "WRONG PIN NO ALARM");
                    state = States.ON_PIN_UNLOCK;
                    mistakes++;


                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterPinDialog = new EnterPinDialog(getActivity(), null);
                    enterPinDialog.addUnlockedEventListener(this);
                    enterPinDialog.addWrongUnlockedEventListener(this);
                    enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_pin_dialog");
                    enterPinDialog.setRightPin(pin);
                    if (lightAlarm && alarmOn) enterPinDialog.lightAlarmOn();

                }
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "PWU: ON_PATTERN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "PWU: ON_IMAGE_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "PWU: UNLOCKED");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == UNLOCKED -> FORBIDDEN");
                break;
        }
    }

    public void PatternUnlocked(){
        Log.println(Log.DEBUG, "", "PATTERN_UNLOCKED");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "PATU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "PATU: ON_PIN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "PATU: ON_PATTERN_UNLOCK");
                if(securityLevel.equals(getActivity().getResources().
                        getString(R.string.pref_security_level_medium))){
                    state = States.UNLOCKED;
                    mistakes = 0;
                    alarmOn = false;

                    enterPatternDialog.lightAlarmOff();
                    stopAlarm();
                    stopGraceTimeTimer();
                    //TODO revenir au fragment principal
                } else {
                    state = States.ON_IMAGE_UNLOCK;

                    enterPatternDialog.lightAlarmOff();
                    enterImageDialog = new EnterImageDialog(getActivity(), null);
                    enterImageDialog.addUnlockedEventListener(this);
                    enterImageDialog.addWrongUnlockedEventListener(this);
                    enterImageDialog.setRightImage(image);
                    enterImageDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_image_dialog");
                    if (lightAlarm && alarmOn) enterImageDialog.lightAlarmOn();
                }
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "PATU: ON_IMAGE_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "PATU: UNLOCKED");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern unlocked error: state == UNLOCKED -> FORBIDDEN");
                break;
        }
    }

    public void PatternWrongUnlocked(){
        Log.println(Log.DEBUG, "", "PATTERN_WRONG_UNLOCKED");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "PATWU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pattern wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "PATWU: ON_PIN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pin wrong unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "PATWU: ON_PATTERN_UNLOCK");
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
                    enterPatternDialog.setRightPattern(pattern);
                    startAlarm();
                    stopGraceTimeTimer();
                    if (lightAlarm) enterPatternDialog.lightAlarmOn();
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
                    enterPatternDialog.setRightPattern(pattern);
                    if (lightAlarm && alarmOn) enterPatternDialog.lightAlarmOn();

                }
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "PATWU: ON_IMAGE_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pattern wrong unlocked error: state == ON_IMAGE_UNLOCK -> FORBIDDEN");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "PATWU: UNLOCKED");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Pattern wrong unlocked error: state == UNLOCKED -> FORBIDDEN");
                break;
        }
    }

    public void ImageUnlocked(){
        Log.println(Log.DEBUG, "", "IMAGE_UNLOCKED");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "IU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "IU: ON_PIN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "IU: ON_PATTERN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "IU: ON_IMAGE_UNLOCK");
                state = States.UNLOCKED;
                mistakes = 0;
                alarmOn = false;

                enterImageDialog.lightAlarmOff();
                stopAlarm();
                stopGraceTimeTimer();
                //TODO revenir au fragment principal
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "IU: UNLOCKED");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image unlocked error: state == UNLOCKED -> FORBIDDEN");
                break;
        }
    }

    public void ImageWrongUnlocked(){
        Log.println(Log.DEBUG, "", "IMAGE_WRONG_UNLOCKED");
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "IWU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Image wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "IWU: ON_PIN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Image wrong unlocked error: state == ON_PIN_UNLOCK -> FORBIDDEN");
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "IWU: ON_IMAGE_UNLOCK");
                if (mistakes == 2){
                    Log.println(Log.DEBUG, "", "WRONG IMAGE WITH ALARM");

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
                    enterImageDialog.setRightImage(image);
                    startAlarm();
                    stopGraceTimeTimer();
                    if (lightAlarm) enterImageDialog.lightAlarmOn();
                } else {
                    Log.println(Log.DEBUG, "", "WRONG IMAGE NO ALARM");
                    state = States.ON_IMAGE_UNLOCK;
                    mistakes++;

                    Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 100 milliseconds
                    v.vibrate(100);
                    enterImageDialog = new EnterImageDialog(getActivity(), null);
                    enterImageDialog.addUnlockedEventListener(this);
                    enterImageDialog.addWrongUnlockedEventListener(this);
                    enterImageDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(), "enter_image_dialog");
                    enterImageDialog.setRightImage(image);
                    if (lightAlarm && alarmOn) enterImageDialog.lightAlarmOn();

                }
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "IWU: ON_PATTERN_UNLOCK");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Image wrong unlocked error: state == ON_PATTERN_UNLOCK -> FORBIDDEN");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "IWU: UNLOCKED");
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Image wrong unlocked error: state == UNLOCKED -> FORBIDDEN");
                break;
        }
    }

    public void graceTimeTimerTicked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.DEBUG, "", "Grace time timer ticked error: state == IDLE");
                break;
            case ON_PIN_UNLOCK:
                if (alarmOn) {
                    state = States.ON_PIN_UNLOCK;

                    stopGraceTimeTimer();
                } else {
                    state = States.ON_PIN_UNLOCK;
                    alarmOn = true;

                    if(lightAlarm) enterPinDialog.lightAlarmOn();
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

                    if(lightAlarm) enterPatternDialog.lightAlarmOn();
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

                    if(lightAlarm) enterImageDialog.lightAlarmOn();
                    startAlarm();
                    stopGraceTimeTimer();
                }
                break;
            case UNLOCKED:
                //FORBIDDEN
                Log.println(Log.DEBUG, "", "Grace time timer ticked error: state == UNLOCKED");
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    public void startAlarm(){
        Log.println(Log.DEBUG, "", "ALARM START");
    }

    public void stopAlarm(){
        Log.println(Log.DEBUG, "", "ALARM STOP");
    }

    public void startGraceTimeTimer(){
        Log.println(Log.DEBUG, "", "GRACE TIMER START");
        graceTimeTimer = new Timer();
        graceTimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                graceTimeTimerTicked();
            }
        }, graceTime);
    }

    public void stopGraceTimeTimer(){
        Log.println(Log.DEBUG, "", "GRACE TIMER STOP");
        if (graceTimeTimer != null) graceTimeTimer.cancel();
    }

    public void disableTriggeringMode(){

    }


}
