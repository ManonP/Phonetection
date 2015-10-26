package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ihm15.project.phonetection.R;

public abstract class AbstractPinDialog extends DialogFragment implements View.OnClickListener,
        DialogInterface.OnClickListener {

    protected static final int PIN_0 = 0;
    protected static final int PIN_1 = 1;
    protected static final int PIN_2 = 2;
    protected static final int PIN_3 = 3;
    protected static final int PIN_4 = 4;
    protected static final int PIN_5 = 5;
    protected static final int PIN_6 = 6;
    protected static final int PIN_7 = 7;
    protected static final int PIN_8 = 8;
    protected static final int PIN_9 = 9;


    protected Context context;
    private String dialogTitleText;
    private String positiveButtonText;
    private String negativeButtonText;

    protected AlertDialog.Builder builder;
    protected AlertDialog al;

    protected Button positiveButton;
    protected Button negativeButton;
    protected Button []pinButtons;
    protected Button clearButton;

    protected TextView pinTextView;

    protected enum States {
        IDLE,
        UPDATING_PIN,
        PIN_COMPLETE
    }

    protected States state;
    protected String pin;


    public AbstractPinDialog(Context context, String dialogTitleText, String positiveButtonText,
                             String negativeButtonText){
        this.context = context;
        this.dialogTitleText = dialogTitleText;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;

        pin = null;
        pinButtons = new Button[10];

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder = new AlertDialog.Builder(context);
        builder.setView(inflater.inflate(R.layout.pin_layout, null));
        builder.setIcon(R.drawable.ic_dialpad_indigo_36px);
        builder.setTitle(dialogTitleText);
        builder.setPositiveButton(positiveButtonText, this);
        builder.setNegativeButton(negativeButtonText, this);
        al = builder.create();
        al.show();

        positiveButton = al.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = al.getButton(AlertDialog.BUTTON_NEGATIVE);

        setDialogButtonTextColor();


        pinButtons[0] = (Button) al.findViewById(R.id.pin_0_button);
        pinButtons[1] = (Button) al.findViewById(R.id.pin_1_button);
        pinButtons[2] = (Button) al.findViewById(R.id.pin_2_button);
        pinButtons[3] = (Button) al.findViewById(R.id.pin_3_button);
        pinButtons[4] = (Button) al.findViewById(R.id.pin_4_button);
        pinButtons[5] = (Button) al.findViewById(R.id.pin_5_button);
        pinButtons[6] = (Button) al.findViewById(R.id.pin_6_button);
        pinButtons[7] = (Button) al.findViewById(R.id.pin_7_button);
        pinButtons[8] = (Button) al.findViewById(R.id.pin_8_button);
        pinButtons[9] = (Button) al.findViewById(R.id.pin_9_button);

        clearButton = (Button) al.findViewById(R.id.pin_clear_button);

        pinTextView = (TextView) al.findViewById(R.id.pin_password);

        for (int i = 0; i < 10; i++){
            pinButtons[i].setOnClickListener(this);
        }

        clearButton.setOnClickListener(this);

        init();

        al.hide();

        return al;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.pin_0_button:
                pinPadClicked(PIN_0);
                break;
            case R.id.pin_1_button:
                pinPadClicked(PIN_1);
                break;
            case R.id.pin_2_button:
                pinPadClicked(PIN_2);
                break;
            case R.id.pin_3_button:
                pinPadClicked(PIN_3);
                break;
            case R.id.pin_4_button:
                pinPadClicked(PIN_4);
                break;
            case R.id.pin_5_button:
                pinPadClicked(PIN_5);
                break;
            case R.id.pin_6_button:
                pinPadClicked(PIN_6);
                break;
            case R.id.pin_7_button:
                pinPadClicked(PIN_7);
                break;
            case R.id.pin_8_button:
                pinPadClicked(PIN_8);
                break;
            case R.id.pin_9_button:
                pinPadClicked(PIN_9);
                break;
            case R.id.pin_clear_button:
                clearButtonClicked();
                break;
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                positiveButtonClicked();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                negativeButtonClicked();
                break;
        }
    }

    //SEEHIEM-DIALOGUE//////////////////////////////////////////////////////////////////////////////
    protected void init(){
        state = States.IDLE;
        pin = "";
        disablePositiveButton();
        enableNeagtiveButton();
        enablePinPad();
        disableClearButton();
    }

    protected abstract void positiveButtonClicked();

    protected void negativeButtonClicked(){
        switch (state) {
            case IDLE:
                state = States.IDLE;


                disablePositiveButton();
                enableNeagtiveButton();
                enablePinPad();
                disableClearButton();
                dismiss();
                break;
            case UPDATING_PIN:
                state = States.IDLE;

                disablePositiveButton();
                enableNeagtiveButton();
                enablePinPad();
                disableClearButton();
                dismiss();
                break;
            case PIN_COMPLETE:
                state = States.IDLE;

                disablePositiveButton();
                enableNeagtiveButton();
                enablePinPad();
                disableClearButton();
                dismiss();
                break;
        }
    }

    protected void pinPadClicked(int pinNb){
        switch (state){
            case IDLE:
                state = States.UPDATING_PIN;
                addPinNb(pinNb);

                enableNeagtiveButton();
                disablePositiveButton();
                enableClearButton();
                enablePinPad();
                setPinText();
                break;
            case UPDATING_PIN:
                if(pin.length() < 3){
                    state = States.UPDATING_PIN;
                    addPinNb(pinNb);

                    enableNeagtiveButton();
                    disablePositiveButton();
                    enableClearButton();
                    enablePinPad();
                    setPinText();
                } else {
                    state = States.PIN_COMPLETE;
                    addPinNb(pinNb);

                    enableNeagtiveButton();
                    enablePositiveButton();
                    enableClearButton();
                    disablePinPad();
                    setPinText();
                }
                break;
            case PIN_COMPLETE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "PIN pad clicked error: state == PIN_COMPLETE -> FORBIDDEN");
                break;
        }
    }

    protected void clearButtonClicked(){
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Clear button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case UPDATING_PIN:
                if (pin.length() > 1){
                    state = States.UPDATING_PIN;
                    removePinNb();

                    enableNeagtiveButton();
                    disablePositiveButton();
                    enableClearButton();
                    enablePinPad();
                    setPinText();
                } else {
                    state = States.IDLE;
                    removePinNb();

                    enableNeagtiveButton();
                    disablePositiveButton();
                    disableClearButton();
                    enablePinPad();
                    setPinText();
                }
                break;
            case PIN_COMPLETE:
                state = States.UPDATING_PIN;
                removePinNb();

                enableNeagtiveButton();
                disablePositiveButton();
                enableClearButton();
                enablePinPad();
                setPinText();
                break;
        }
    }

    protected void addPinNb(int pinNb){
        pin += pinNb;
    }

    protected void removePinNb(){
        pin = pin.substring(0,pin.length()-1);
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    protected void setDialogButtonTextColor (){
        positiveButton.setTextColor(getContext().getResources().getColor(R.color.accent));
        negativeButton.setTextColor(getContext().getResources().getColor(R.color.accent));
    }

    protected void enablePositiveButton(){ positiveButton.setEnabled(true); }

    protected void disablePositiveButton(){ positiveButton.setEnabled(false); }

    protected void enableNeagtiveButton(){ negativeButton.setEnabled(true); }

    protected void disableNegativeButton(){ negativeButton.setEnabled(false); }

    protected void enablePinPad(){
        for(int i = 0; i < 10; i++){
            pinButtons[i].setEnabled(true);
        }
    }

    protected void disablePinPad(){
        for(int i = 0; i < 10; i++){
            pinButtons[i].setEnabled(false);
        }
    }

    protected void enableClearButton(){ clearButton.setEnabled(true); }

    protected void disableClearButton(){ clearButton.setEnabled(false); }

    protected void setPinText(){
        pinTextView.setText(pin);
    }
}