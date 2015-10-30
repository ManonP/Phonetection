package dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.ihm15.project.phonetection.R;

import java.util.Timer;
import java.util.TimerTask;

import events.UnlockObject;
import events.WrongUnlockObject;
import interfaces.LightAlarmInterface;


public class EnterPinDialog extends AbstractPinDialog implements LightAlarmInterface{

    private UnlockObject unlockObject;
    private WrongUnlockObject wrongUnlockObject;

    int rightPin;

    /*private Timer orangeTimer;

    private enum TimerStates{
        ORANGE_ON,
        ORANGE_OFF
    }

    private TimerStates timerState;
*/
    boolean hasCancelButton;

    public EnterPinDialog(Context context, String cancelButtonText){
        super(context, context.getString(R.string.enter_pin_dialog),
                context.getString(R.string.validate_button),
                cancelButtonText);

        hasCancelButton = (cancelButtonText != null);

        unlockObject = new UnlockObject(this, UnlockObject.UnlockedEvent.PIN_UNLOCK);
        wrongUnlockObject = new WrongUnlockObject(this, WrongUnlockObject.WrongUnlockedEvent.PIN_WRONG_UNLOCK);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.show();
        if (!hasCancelButton) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        dialog.hide();
        return dialog;
    }

    public void setRightPin(int rightPin){
        this.rightPin = rightPin;
    }

    public void addUnlockedEventListener(UnlockObject.UnlockedEventListener uel){
        unlockObject.addUnlockedEventListener(uel);
    }

    public void removeUnlockedEventListener(UnlockObject.UnlockedEventListener uel){
        unlockObject.removeUnlockedEventListener(uel);
    }

    public void addWrongUnlockedEventListener(WrongUnlockObject.WrongUnlockedEventListener wuel){
        wrongUnlockObject.addWrongUnlockedListener(wuel);
    }

    public void removeWrongUnlockedEventListener(WrongUnlockObject.WrongUnlockedEventListener wuel){
        wrongUnlockObject.removeWrongUnlockedListener(wuel);
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
            case UPDATING_PIN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PIN -> FORBIDDEN");
                break;
            case PIN_COMPLETE:
                Log.println(Log.DEBUG, "", "PIN_COMPLETE: "+ pin + ", " + rightPin);
                if (pin.equals(Integer.toString(rightPin))){
                    state = States.IDLE;
                    pin = "";

                    setPinText();
                    disablePositiveButton();
                    enableNegativeButton();
                    disableClearButton();
                    enablePinPad();
                    unlockObject.fireUnlockedEvent();
                } else {
                    state = States.IDLE;
                    pin = "";

                    setPinText();
                    disablePositiveButton();
                    enableNegativeButton();
                    disableClearButton();
                    enablePinPad();
                    wrongUnlockObject.fireWrongUnlockedEvent();
                }
        }
        /*switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case UPDATING_PIN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PIN -> FORBIDDEN");
                break;
            case PIN_COMPLETE:
                Log.println(Log.DEBUG, "", "PIN_COMPLETE: "+ pin + ", " + rightPin);
                if (pin.equals(Integer.toString(rightPin))) {
                    Log.println(Log.DEBUG, "", "OK");
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
                    enablePinPad();
                    disableClearButton();
                    stopAllTimer();
                    unlockObject.fireUnlockedEvent();
                } else {
                    Log.println(Log.DEBUG, "", "KO");
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
                    enablePinPad();
                    disableClearButton();
                    stopAllTimer();
                    wrongUnlockObject.fireWrongUnlockedEvent();
                }
        }*/
    }

    /*public void initTimerStateMachine(){
        timerState = TimerStates.ORANGE_ON;
        setOrangeBackground();
    }

    public void orangeOnTimerRun(){
        switch (timerState){
            case ORANGE_ON:
                timerState = TimerStates.ORANGE_OFF;

                removeOrangeBackground();

                stopOrangeOnTimer();
                startOrangeOffTimer();
                break;
            case ORANGE_OFF:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Orange on timer run error: timerState == ORANGE_OFF -> FORBIDDEN");
                break;
        }
    }

    public void orangeOffTimerRun(){
        switch (timerState){
            case ORANGE_ON:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Orange off timer run error: timerState == ORANGE_ON -> FORBIDDEN");
                break;
            case ORANGE_OFF:
                timerState = TimerStates.ORANGE_ON;

                setOrangeBackground();

                stopOrangeOffTimer();
                startOrangeOnTimer();
                break;
        }
    }*/

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    public void startOrangeOnTimer(){
        /*orangeTimer = new Timer();
        orangeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        orangeOnTimerRun();
                    }
                });
            }
        }, 100);*/
    }

    public void stopOrangeOnTimer(){
        /*if (orangeTimer != null) orangeTimer.cancel();*/
    }

    public void startOrangeOffTimer(){
        /*orangeTimer = new Timer();
        orangeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                orangeOffTimerRun();
            }
        }, 100);*/
    }

    public void stopOrangeOffTimer(){
        /*if (orangeTimer != null) orangeTimer.cancel();*/
    }

    public void stopAllTimer(){
        /*if (orangeTimer != null) orangeTimer.cancel();*/
    }

    public void setOrangeBackground(){
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = al.getWindow().getDecorView();
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.orange));
            }
        });*/

    }

    public void removeOrangeBackground(){
        /*getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View view = al.getWindow().getDecorView();
                view.setBackgroundColor(getActivity().getResources().getColor(R.color.primary_light_text));
            }
        });*/
    }

    @Override
    public void lightAlarmOn() {
        /*initTimerStateMachine();
        startOrangeOnTimer();*/
    }

    @Override
    public void lightAlarmOff() {
        /*stopAllTimer();*/
    }

}
