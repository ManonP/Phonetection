package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

import java.util.Timer;
import java.util.TimerTask;

import events.UnlockObject;
import events.WrongUnlockObject;
import interfaces.LightAlarmInterface;


public class EnterPatternDialog extends AbstractPatternDialog implements LightAlarmInterface {
    private UnlockObject unlockObject;
    private WrongUnlockObject wrongUnlockObject;

    int rightPattern;

    private Timer lightAlarmTimer;

    private enum TimerStates{
        FIRST_LIGHT,
        SECOND_LIGHT
    }
    private TimerStates timerState;

    boolean hasCancelButton;

    public EnterPatternDialog(Context context, String cancelButtonText){
        super(context, context.getString(R.string.draw_pattern_dialog),
                context.getString(R.string.validate_button),
                cancelButtonText);

        hasCancelButton = (cancelButtonText != null);

        unlockObject = new UnlockObject(this, Data.PATTERN_UNLOCK);
        wrongUnlockObject = new WrongUnlockObject(this, Data.WRONG_PATTERN_UNLOCK);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.println(Log.DEBUG, "", "ON_CREATE_DIALOG");

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.show();
        if (!hasCancelButton) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            setCancelable(false);
        }
        dialog.hide();
        return dialog;
    }

    public void setRightPattern(int rightPattern){
        this.rightPattern = rightPattern;
    }

    public void addUnlockedEventListener(UnlockObject.UnlockedEventListener uel){
        unlockObject.addUnlockedEventListener(uel);
    }

    public void removeUnlockedEventListener(UnlockObject.UnlockedEventListener uel){
        unlockObject.removeUnlockedEventListener(uel);
    }

    public void addWrongUnlockedEventListener(WrongUnlockObject.WrongUnlockedEventListener wuel){
        wrongUnlockObject.addWrongUnlockedEventListener(wuel);
    }

    public void removeWrongUnlockedEventListener(WrongUnlockObject.WrongUnlockedEventListener wuel){
        wrongUnlockObject.removeWrongUnlockedEventListener(wuel);
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    @Override
    protected void positiveButtonClicked() {
        switch (state) {
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case UPDATING_PATTERN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PATTERN -> FORBIDDEN");
                break;
            case PATTERN_COMPLETE:
                Log.println(Log.DEBUG, "", "PATTERN_COMPLETE: "+ pattern + ", " + rightPattern);
                if (pattern == rightPattern){
                    Log.println(Log.DEBUG, "", "PATTERN_CORRECT");
                    state = States.IDLE;
                    pattern = 0;

                    disablePositiveButton();
                    enableNegativeButton();
                    stopAllTimer();
                    unlockObject.fireUnlockedEvent();
                } else {
                    Log.println(Log.DEBUG, "", "PATTERN_INCORRECT");
                    state = States.IDLE;
                    pattern = 0;

                    disablePositiveButton();
                    enableNegativeButton();
                    stopAllTimer();
                    wrongUnlockObject.fireWrongUnlockedEvent();
                }
        }
    }

    public void initTimer(){
        timerState = TimerStates.FIRST_LIGHT;
        setFirstBackground();
    }

    public void firstLightTimerRun(){
        switch (timerState){
            case FIRST_LIGHT:
                timerState = TimerStates.SECOND_LIGHT;

                setSecondBackground();
                stopFirstLightTimer();
                startSecondLightTimer();
                break;
            case SECOND_LIGHT:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "First light timer run error: timerState == SECOND_LIGHT -> FORBIDDEN");
                break;
        }
    }

    public void secondLightTimerRun(){
        switch (timerState){
            case FIRST_LIGHT:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Second light timer run error: timerState == FIRST_LIGHT -> FORBIDDEN");
                break;
            case SECOND_LIGHT:_LIGHT:
            timerState = TimerStates.FIRST_LIGHT;

                setFirstBackground();
                stopSecondLightTimer();
                startFirstLightTimer();
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    public void startFirstLightTimer(){
        Log.println(Log.DEBUG, "","FIRST_LIGHT_TIMER");
        lightAlarmTimer = new Timer();
        lightAlarmTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                firstLightTimerRun();
            }
        }, 100);
    }

    public void stopFirstLightTimer(){
        if (lightAlarmTimer != null) lightAlarmTimer.cancel();
    }

    public void startSecondLightTimer(){
        lightAlarmTimer = new Timer();
        lightAlarmTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                secondLightTimerRun();
            }
        }, 100);
    }

    public void stopSecondLightTimer(){
        if (lightAlarmTimer != null) lightAlarmTimer.cancel();
    }

    public void stopAllTimer(){
        if (lightAlarmTimer != null) lightAlarmTimer.cancel();
    }

    public void setFirstBackground(){
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = al.getWindow().getDecorView();
                    view.setBackgroundColor(getActivity().getResources().getColor(R.color.red));
                }
            });
    }

    public void setSecondBackground(){
        if (getActivity() != null)
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    View view = al.getWindow().getDecorView();
                    view.setBackgroundColor(getActivity().getResources().getColor(R.color.blue));
                }
            });
    }

    @Override
    public void lightAlarmOn() {
        initTimer();
        firstLightTimerRun();
    }

    @Override
    public void lightAlarmOff() {
        stopAllTimer();
    }
}
