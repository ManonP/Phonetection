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

public class GraceTimePreference extends DialogPreference implements View.OnClickListener,
        DialogInterface.OnClickListener{

    private RadioButton seconds5RadioButton;
    private RadioButton seconds10RadioButton;
    private RadioButton seconds15RadioButton;
    private RadioButton seconds30RadioButton;

    int valueToSave;

    public GraceTimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogIcon(R.drawable.ic_timer_black_36dp);
        setDialogTitle(R.string.grace_time_setting);
        setDialogLayoutResource(R.layout.grace_time_layout);
        setPositiveButtonText(R.string.ok_button);
        setNegativeButtonText(R.string.cancel_button);

        Data.getInstance(context);

        setGraceTimeSummary(Data.getGraceTime());
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        seconds5RadioButton = (RadioButton) view.findViewById(R.id.grace_time_5_seconds);
        seconds10RadioButton = (RadioButton) view.findViewById(R.id.grace_time_10_seconds);
        seconds15RadioButton = (RadioButton) view.findViewById(R.id.grace_time_15_seconds);
        seconds30RadioButton = (RadioButton) view.findViewById(R.id.grace_time_30_seconds);

        seconds5RadioButton.setOnClickListener(this);
        seconds10RadioButton.setOnClickListener(this);
        seconds15RadioButton.setOnClickListener(this);
        seconds30RadioButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.grace_time_5_seconds:
                graceTime5secondsRadioButtonClicked();
                break;
            case R.id.grace_time_10_seconds:
                graceTime10secondsRadioButtonClicked();
                break;
            case R.id.grace_time_15_seconds:
                graceTime15secondsRadioButtonClicked();
                break;
            case R.id.grace_time_30_seconds:
                graceTime30secondsRadioButtonClicked();
                break;
        }
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        setDialogButtonTextColor();

        setSelectedButton(getSharedPreferences().getInt(getContext().getString(R.string.pref_key_grace_time),
                getContext().getResources().getInteger(R.integer.pref_grace_time_default)));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            persistInt(valueToSave);
            setGraceTimeSummary(valueToSave);
        }
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    private void graceTime5secondsRadioButtonClicked(){
        valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_5);
    }

    private void graceTime10secondsRadioButtonClicked(){
        valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_10);
    }

    private void graceTime15secondsRadioButtonClicked(){
        valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_15);
    }

    private void graceTime30secondsRadioButtonClicked(){
        valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_30);
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void setGraceTimeSummary(int value){
        if (value == getContext().getResources().getInteger(R.integer.pref_grace_time_5)) {
            setSummary(getContext().getString(R.string.grace_time_5_seconds));
        } else if (value == getContext().getResources().getInteger(R.integer.pref_grace_time_10)) {
            setSummary(getContext().getString(R.string.grace_time_10_seconds));
        } else if (value == getContext().getResources().getInteger(R.integer.pref_grace_time_15)) {
            setSummary(getContext().getString(R.string.grace_time_15_seconds));
        } else {
            setSummary(getContext().getString(R.string.grace_time_30_seconds));
        }
    }

    protected void setDialogButtonTextColor (){
        Button b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTextColor(getContext().getResources().getColor(R.color.accent));
        b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        b.setTextColor(getContext().getResources().getColor(R.color.accent));
    }

    protected void setSelectedButton (int value){
        if (value == getContext().getResources().getInteger(R.integer.pref_grace_time_5)) {
            seconds5RadioButton.setChecked(true);
        } else if (value == getContext().getResources().getInteger(R.integer.pref_grace_time_10)) {
            seconds10RadioButton.setChecked(true);
        } else if (value == getContext().getResources().getInteger(R.integer.pref_grace_time_15)) {
            seconds15RadioButton.setChecked(true);
        } else {
            seconds30RadioButton.setChecked(true);
        }
    }
}

