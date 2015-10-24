package preference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RadioButton;

import com.ihm15.project.phonetection.R;

public class GraceTimePreference extends DialogPreference implements View.OnClickListener,
        DialogInterface.OnClickListener{

    SharedPreferences sharedPreferences;
    String initialValue;
    String valueToSave;

    RadioButton seconds5RadioButton;
    RadioButton seconds10RadioButton;
    RadioButton seconds15RadioButton;
    RadioButton seconds30RadioButton;

    public GraceTimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.grace_time_layout);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(R.drawable.ic_query_builder_indigo_36px);
        setDialogTitle(R.string.grace_time_setting);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initialValue = sharedPreferences.getString(
                context.getString(R.string.pref_key_grace_time),
                context.getString(R.string.pref_grace_time_default));
        valueToSave = initialValue;

        setGraceTimeSummary(initialValue);
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        seconds5RadioButton = (RadioButton) view.findViewById(R.id.grace_time_5_seconds);
        seconds10RadioButton = (RadioButton) view.findViewById(R.id.grace_time_10_seconds);
        seconds15RadioButton = (RadioButton) view.findViewById(R.id.grace_time_15_seconds);
        seconds30RadioButton = (RadioButton) view.findViewById(R.id.grace_time_30_seconds);

        seconds5RadioButton.setOnClickListener(this);
        seconds10RadioButton.setOnClickListener(this);
        seconds15RadioButton.setOnClickListener(this);
        seconds30RadioButton.setOnClickListener(this);

        super.onBindDialogView(view);
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
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        initialValue = sharedPreferences.getString(
                getContext().getString(R.string.pref_key_grace_time),
                getContext().getString(R.string.pref_grace_time_default));
        valueToSave = initialValue;

        setGraceTimeSummary(initialValue);
        setCheckRadioButton(valueToSave);
    }



    @Override
    public void onClick(DialogInterface dialogInterface, int buttonClickedType){
        if (buttonClickedType == DialogInterface.BUTTON_POSITIVE){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getContext().getString(R.string.pref_key_grace_time), valueToSave);
            editor.commit();
            setGraceTimeSummary(valueToSave);
        } else if (buttonClickedType == DialogInterface.BUTTON_NEGATIVE){
            setCheckRadioButton(initialValue);
        }
    }

    private void graceTime5secondsRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_grace_time_5);
    }

    private void graceTime10secondsRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_grace_time_10);
    }

    private void graceTime15secondsRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_grace_time_15);
    }

    private void graceTime30secondsRadioButtonClicked(){
        valueToSave = getContext().getString(R.string.pref_grace_time_30);
    }

    private void setGraceTimeSummary(String preferenceValue){
        if (preferenceValue.equals(getContext().getString(R.string.pref_grace_time_5))) {
            setSummary(getContext().getString(R.string.grace_time_5_seconds));
        } else if (preferenceValue.equals(getContext().getString(R.string.pref_grace_time_10))) {
            setSummary(getContext().getString(R.string.grace_time_10_seconds));
        } else if (preferenceValue.equals(getContext().getString(R.string.pref_grace_time_15))) {
            setSummary(getContext().getString(R.string.grace_time_15_seconds));
        } else {
            setSummary(getContext().getString(R.string.grace_time_30_seconds));
        }
    }

    private void setCheckRadioButton(String preferenceValue){
        if (preferenceValue.equals(getContext().getString(R.string.pref_grace_time_5))) {
            seconds5RadioButton.setChecked(true);
        } else if (preferenceValue.equals(getContext().getString(R.string.pref_grace_time_10))) {
            seconds10RadioButton.setChecked(true);
        } else if (preferenceValue.equals(getContext().getString(R.string.pref_grace_time_15))) {
            seconds15RadioButton.setChecked(true);
        } else {
            seconds30RadioButton.setChecked(true);
        }
    }


}

