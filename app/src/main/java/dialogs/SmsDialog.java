package dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

/**
 * Created by Dimitri on 08/11/2015.
 */
public class SmsDialog extends DialogFragment implements TextWatcher,
        DialogInterface.OnClickListener{

    private Button positiveButton;
    private Button negativeButton;

    private EditText etSms;

    private enum States{
        IDLE,
        TEXT_TAPED
    }
    private States state;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.println(Log.DEBUG, "", "POTATOES");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Data.getInstance(getContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme_Dialog_Alert));
        builder.setView(inflater.inflate(R.layout.alarm_control_sms_layout, null));
        builder.setIcon(R.drawable.ic_textsms_black_36dp);
        builder.setTitle(getContext().getString(R.string.sms_setting));
        builder.setPositiveButton(getContext().getString(R.string.ok_button), this);
        builder.setNegativeButton(getContext().getString(R.string.cancel_button), this);
        AlertDialog al = builder.create();
        al.show();

        positiveButton = al.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = al.getButton(AlertDialog.BUTTON_NEGATIVE);

        setDialogButtonTextColor();


        etSms = (EditText) al.findViewById(R.id.sms_edit_text);
        etSms.addTextChangedListener(this);

        state = States.IDLE;

        disablePositiveButton();

        al.hide();

        return al;
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        keyPressed();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    protected void positiveButtonClicked(){
        Log.println(Log.DEBUG, "", "DEBUG : " + etSms.getText().toString());
        Data.setSms(etSms.getText().toString());
        dismiss();
    }

    protected void negativeButtonClicked(){
        dismiss();
    }

    protected void keyPressed(){
        switch(state){
            case IDLE:
                if (etSms.getText().toString().length() > 0){
                    state = States.TEXT_TAPED;

                    enablePositiveButton();
                } else {
                    state = States.IDLE;

                    disablePositiveButton();
                }
                break;
            case TEXT_TAPED:
                if (etSms.getText().toString().length() > 0){
                    state = States.TEXT_TAPED;

                    enablePositiveButton();
                } else {
                    state = States.IDLE;

                    disablePositiveButton();
                }
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void setDialogButtonTextColor (){
        positiveButton.setTextColor(getContext().getResources().getColor(R.color.accent));
        negativeButton.setTextColor(getContext().getResources().getColor(R.color.accent));
    }

    private void enablePositiveButton(){
        positiveButton.setEnabled(true);
    }

    private void disablePositiveButton(){
        positiveButton.setEnabled(false);
    }
}
