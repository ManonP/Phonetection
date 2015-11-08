package preferences;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

public class SecurityLevelPreference extends DialogPreference implements View.OnClickListener,
        DialogInterface.OnClickListener{

    String initialValue;
    String valueToSave;

    RadioButton lowRadioButton;
    RadioButton mediumRadioButton;
    RadioButton highRadioButton;

    public SecurityLevelPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.security_level_layout);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(R.drawable.ic_security_black_36dp);
        setDialogTitle(R.string.security_level_setting);


        initialValue = Data.getSecurityLevel();
        valueToSave = initialValue;

        setSecurityLevelSummary(initialValue);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        lowRadioButton = (RadioButton) view.findViewById(R.id.security_level_low);
        mediumRadioButton = (RadioButton) view.findViewById(R.id.security_level_medium);
        highRadioButton = (RadioButton) view.findViewById(R.id.security_level_high);

        lowRadioButton.setOnClickListener(this);
        mediumRadioButton.setOnClickListener(this);
        highRadioButton.setOnClickListener(this);

        super.onBindDialogView(view);
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.security_level_low:
                lowRadioButtonClicked();
                break;
            case R.id.security_level_medium:
                mediumRadioButtonClicked();
                break;
            case R.id.security_level_high:
                highRadioButtonClicked();
                break;
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        initialValue = Data.getSecurityLevel();
        valueToSave = initialValue;

        setSecurityLevelSummary(initialValue);
        setCheckRadioButton(valueToSave);

        Button b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTextColor(getContext().getResources().getColor(R.color.accent));
        b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        b.setTextColor(getContext().getResources().getColor(R.color.accent));
        
    }



    @Override
    public void onClick(DialogInterface dialogInterface, int buttonClickedType){
        if (buttonClickedType == DialogInterface.BUTTON_POSITIVE){
            Data.setSecurityLevel(valueToSave);
            setSecurityLevelSummary(valueToSave);
        } else if (buttonClickedType == DialogInterface.BUTTON_NEGATIVE){
            setCheckRadioButton(initialValue);
        }
    }

    private void lowRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_security_level_low);
    }

    private void mediumRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_security_level_medium);
    }

    private void highRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_security_level_high);
    }

    private void setSecurityLevelSummary(String preferenceValue){
        if (preferenceValue.equals(getContext().getString(R.string.pref_security_level_low))) {
            setSummary(getContext().getString(R.string.security_level_low));
        } else if (preferenceValue.equals(getContext().getString(R.string.pref_security_level_medium))) {
            setSummary(getContext().getString(R.string.security_level_medium));
        } else {
            setSummary(getContext().getString(R.string.security_level_high));
        }
    }

    private void setCheckRadioButton(String preferenceValue){
        if (preferenceValue.equals(getContext().getString(R.string.pref_security_level_low))) {
            lowRadioButton.setChecked(true);
        } else if (preferenceValue.equals(getContext().getString(R.string.pref_security_level_medium))) {
            mediumRadioButton.setChecked(true);
        } else {
            highRadioButton.setChecked(true);
        }
    }


}

