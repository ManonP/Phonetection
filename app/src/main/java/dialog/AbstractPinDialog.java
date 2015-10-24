package dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ihm15.project.phonetection.R;

public abstract class AbstractPinDialog extends Dialog implements View.OnClickListener {
    private String dialogTitleText;
    private String clearButtonText;
    private String validateButtonText;

    private Button clearButton;
    private Button validateButton;
    private Button cancelButton;

    private Button pad0Button;
    private Button pad1Button;
    private Button pad2Button;
    private Button pad3Button;
    private Button pad4Button;
    private Button pad5Button;
    private Button pad6Button;
    private Button pad7Button;
    private Button pad8Button;
    private Button pad9Button;
    protected String pin;

    private TextView pinTextView;

    protected enum States {
        IDLE,
        UPDATING_PIN,
        PIN_COMPLETE
    }
    protected States state;

    public AbstractPinDialog(Context context,
                             String dialogTitleText,
                             String clearButtonText,
                             String validateButtonText) {
        super(context);
        this.dialogTitleText = dialogTitleText;
        this.clearButtonText = clearButtonText;
        this.validateButtonText = validateButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.pin_layout);

        this.setTitle(dialogTitleText);


        pinTextView = (TextView) findViewById(R.id.pin_password);
        clearButton = (Button) findViewById(R.id.pin_clear_button);
        cancelButton = (Button) findViewById(R.id.pin_cancel_button);
        validateButton = (Button) findViewById(R.id.pin_validate_button);
        pad0Button = (Button) findViewById(R.id.pin_0_button);
        pad1Button = (Button) findViewById(R.id.pin_1_button);
        pad2Button = (Button) findViewById(R.id.pin_2_button);
        pad3Button = (Button) findViewById(R.id.pin_3_button);
        pad4Button = (Button) findViewById(R.id.pin_4_button);
        pad5Button = (Button) findViewById(R.id.pin_5_button);
        pad6Button = (Button) findViewById(R.id.pin_6_button);
        pad7Button = (Button) findViewById(R.id.pin_7_button);
        pad8Button = (Button) findViewById(R.id.pin_8_button);
        pad9Button = (Button) findViewById(R.id.pin_9_button);

        clearButton.setOnClickListener(this);
        clearButton.setText(clearButtonText);
        cancelButton.setOnClickListener(this);
        validateButton.setOnClickListener(this);
        validateButton.setText(validateButtonText);
        pad0Button.setOnClickListener(this);
        pad1Button.setOnClickListener(this);
        pad2Button.setOnClickListener(this);
        pad3Button.setOnClickListener(this);
        pad4Button.setOnClickListener(this);
        pad5Button.setOnClickListener(this);
        pad6Button.setOnClickListener(this);
        pad7Button.setOnClickListener(this);
        pad8Button.setOnClickListener(this);
        pad9Button.setOnClickListener(this);
        init();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.pin_0_button:
                padButtonClicked(getContext().getString(R.string.pin_0_button));
                break;
            case R.id.pin_1_button:
                padButtonClicked(getContext().getString(R.string.pin_1_button));
                break;
            case R.id.pin_2_button:
                padButtonClicked(getContext().getString(R.string.pin_2_button));
                break;
            case R.id.pin_3_button:
                padButtonClicked(getContext().getString(R.string.pin_3_button));
                break;
            case R.id.pin_4_button:
                padButtonClicked(getContext().getString(R.string.pin_4_button));
                break;
            case R.id.pin_5_button:
                padButtonClicked(getContext().getString(R.string.pin_5_button));
                break;
            case R.id.pin_6_button:
                padButtonClicked(getContext().getString(R.string.pin_6_button));
                break;
            case R.id.pin_7_button:
                padButtonClicked(getContext().getString(R.string.pin_7_button));
                break;
            case R.id.pin_8_button:
                padButtonClicked(getContext().getString(R.string.pin_8_button));
                break;
            case R.id.pin_9_button:
                padButtonClicked(getContext().getString(R.string.pin_9_button));
                break;
            case R.id.pin_clear_button:
                clearButtonClicked();
                break;
            case R.id.pin_cancel_button:
                cancelButtonClicked();
                break;
            case R.id.pin_validate_button:
                validateButtonClicked();
                break;
        }
    }

    //Dialogue
    private void init(){
        state = States.IDLE;
        pin = "";

        enablePad();
        disableClearButton();
        disableValidateButton();
    }

    protected abstract void validateButtonClicked();

    private void cancelButtonClicked(){
        switch (state){
            case IDLE:
                dismiss();
                break;
            case UPDATING_PIN:
                dismiss();
                break;
            case PIN_COMPLETE:
                dismiss();
                break;
        }
    }

    private void clearButtonClicked(){
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case UPDATING_PIN:
                if (pin.length() > 1){
                    state = States.UPDATING_PIN;

                    deleteLastPinNumber();

                    enablePad();
                    disableValidateButton();
                    enableClearButton();
                } else {
                    state = States.IDLE;

                    deleteLastPinNumber();

                    enablePad();
                    disableValidateButton();
                    disableClearButton();
                }
                break;
            case PIN_COMPLETE:
                state = States.UPDATING_PIN;

                deleteLastPinNumber();

                enablePad();
                disableValidateButton();
                enableClearButton();
                break;
        }
    }

    private void padButtonClicked(String pinNumber){
        switch (state){
            case IDLE:
                state = States.UPDATING_PIN;

                addPinNumber(pinNumber);

                enablePad();
                enableClearButton();
                disableValidateButton();
                break;
            case UPDATING_PIN:
                if (pin.length() < 3){
                    state = States.UPDATING_PIN;

                    addPinNumber(pinNumber);

                    enablePad();
                    enableClearButton();
                    disableValidateButton();
                } else {
                    state = States.PIN_COMPLETE;

                    addPinNumber(pinNumber);

                    disablePad();
                    enableClearButton();
                    enableValidateButton();
                }
                break;
            case PIN_COMPLETE:
                //INTERDIT
                break;
        }
    }

    //Presentation
    private void disableClearButton(){
        clearButton.setEnabled(false);
    }

    private void enableClearButton(){
        clearButton.setEnabled(true);
    }

    private void disableValidateButton(){
        validateButton.setEnabled(false);
    }

    private void enableValidateButton(){
        validateButton.setEnabled(true);
    }

    private void disablePad(){
        pad0Button.setEnabled(false);
        pad1Button.setEnabled(false);
        pad2Button.setEnabled(false);
        pad3Button.setEnabled(false);
        pad4Button.setEnabled(false);
        pad5Button.setEnabled(false);
        pad6Button.setEnabled(false);
        pad7Button.setEnabled(false);
        pad8Button.setEnabled(false);
        pad9Button.setEnabled(false);
    }

    private void enablePad(){
        pad0Button.setEnabled(true);
        pad1Button.setEnabled(true);
        pad2Button.setEnabled(true);
        pad3Button.setEnabled(true);
        pad4Button.setEnabled(true);
        pad5Button.setEnabled(true);
        pad6Button.setEnabled(true);
        pad7Button.setEnabled(true);
        pad8Button.setEnabled(true);
        pad9Button.setEnabled(true);
    }

    private void deleteLastPinNumber(){
        pin = pin.substring(0,pin.length()-1);
        pinTextView.setText(pin);
    }

    private void addPinNumber(String pinNumber){
        pin += pinNumber;
        pinTextView.setText(pin);
    }
}
