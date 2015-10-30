package managers;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import dialogs.EnterPinDialog;
import events.UnlockObject;
import events.WrongUnlockObject;

public class AlertManager extends Fragment implements UnlockObject.UnlockedEventListener,
        WrongUnlockObject.WrongUnlockedEventListener{

    private EnterPinDialog enterPinDialog;
    //private EnterPatternDialog enterPatternDialog = new EnterPatternDialog(getActivity().getBaseContext());
    //private EnterImageDialog enterImageDialog = new EnterImageDialog(getActivity().getBaseContext());

    int alarmVolume;
    boolean lightAlarm;
    int graceTime;
    String securityLevel;
    int pin;
    int pattern;
    String image;

    /*Timer graceTimeTimer;*/

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
                getActivity().getBaseContext().getString(R.string.pref_key_alarm_volume),
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
                getActivity().getBaseContext().getString(R.string.pref_key_grace_time),
                getActivity().getResources().getInteger(R.integer.pref_grace_time_default));
    }

    private void loadSecurityLevel(SharedPreferences sharedPreferences){
        securityLevel = sharedPreferences.getString(
                getActivity().getBaseContext().getString(R.string.pref_key_security_level),
                getActivity().getBaseContext().getString(R.string.pref_security_level_default));
    }

    private void loadPin(SharedPreferences sharedPreferences){
        pin = sharedPreferences.getInt(
                getActivity().getBaseContext().getString(R.string.pref_key_pin),
                getActivity().getResources().getInteger(R.integer.pref_pin_default));
    }

    private void loadPattern(SharedPreferences sharedPreferences){
        pattern = sharedPreferences.getInt(
                getActivity().getString(R.string.pref_key_pattern),
                getActivity().getResources().getInteger(R.integer.pref_pattern_default));
    }
    private void loadImage(SharedPreferences sharedPreferences){
        image = sharedPreferences.getString(
                getActivity().getBaseContext().getString(R.string.pref_key_image),
                getActivity().getBaseContext().getString(R.string.pref_image_default));
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
                startGraceTimeTimer();
                enterPinDialog = new EnterPinDialog(getActivity(), null);
                enterPinDialog.addUnlockedEventListener(this);
                enterPinDialog.addWrongUnlockedEventListener(this);
                enterPinDialog.setRightPin(pin);
                enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager(),
                        "enter_pin_dialog");
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
                }
                break;
            case ON_PATTERN_UNLOCK:
                Log.println(Log.DEBUG, "", "PU: ON_PATTERN_UNLOCK");
                break;
            case ON_IMAGE_UNLOCK:
                Log.println(Log.DEBUG, "", "PU: ON_IMAGE_UNLOCK");
                break;
            case UNLOCKED:
                Log.println(Log.DEBUG, "", "PU: UNLOCKED");
                break;
        }
    }

    public void PinWrongUnlocked(){
        switch (state){
            case IDLE:
                Log.println(Log.DEBUG, "", "PWU: IDLE");
                //FORBIDDEN
                Log.println(Log.ERROR, "", "Pin wrong unlocked error: state == IDLE -> FORBIDDEN");
                break;
            case ON_PIN_UNLOCK:
                Log.println(Log.DEBUG, "", "PWU: ON_PIN_UNLOCK");
                if (mistakes == 2){
                    state = States.ON_PIN_UNLOCK;
                    alarmOn = true;
                    mistakes++;

                    enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction(),
                            "enter_pin_dialog");
                    if (lightAlarm) enterPinDialog.lightAlarmOn();
                    startAlarm();
                    stopGraceTimeTimer();
                } else {
                    state = States.ON_PIN_UNLOCK;
                    mistakes++;

                    enterPinDialog.show(((FragmentActivity) getActivity()).getSupportFragmentManager().beginTransaction(),
                            "enter_pin_dialog");
                }
        }
    }

    public void PatternUnlocked(){

    }

    public void ImageUnlocked(){

    }

    public void PatternWrongUnlocked(){

    }

    public void ImageWrongUnlocked(){

    }

    public void graceTimeTimerTicked(){

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
        /*graceTimeTimer = new Timer();
        graceTimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                graceTimeTimerTicked();
            }
        }, graceTime);*/
    }

    public void stopGraceTimeTimer(){
        Log.println(Log.DEBUG, "", "GRACE TIMER STOP");
        /*if (graceTimeTimer != null) graceTimeTimer.cancel();*/
    }

    public void disableTriggeringMode(){

    }


}
