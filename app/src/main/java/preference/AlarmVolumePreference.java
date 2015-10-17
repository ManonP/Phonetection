package preference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.SeekBar;

import com.ihm15.project.phonetection.R;

public class AlarmVolumePreference extends DialogPreference implements
        DialogInterface.OnClickListener{

    SharedPreferences sharedPreferences;
    Integer initialValue;
    Integer valueToSave;

    SeekBar alarmeVolumeSeekBar;

    public AlarmVolumePreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDialogLayoutResource(R.layout.alarm_volume_layout);
        setNegativeButtonText(android.R.string.cancel);
        setDialogTitle(R.string.alarm_volume_setting);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initialValue = sharedPreferences.getInt(
                context.getString(R.string.pref_key_alarm_volume),
                Integer.parseInt(context.getString(R.string.pref_alarm_volume_default)));
        valueToSave = initialValue;
    }

    @Override
    protected void onBindDialogView(View view) {
        alarmeVolumeSeekBar = (SeekBar) view.findViewById(R.id.alarm_volume_seek_bar);

        alarmeVolumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //TODO declancher un son feedback
                valueToSave = seekBar.getProgress();

            }
        });

        super.onBindDialogView(view);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        initialValue = sharedPreferences.getInt(
                getContext().getString(R.string.pref_key_alarm_volume),
                Integer.parseInt(getContext().getString(R.string.pref_alarm_volume_default)));
        valueToSave = initialValue;

        setAlarmVolumeSeekBar(initialValue);
    }



    @Override
    public void onClick(DialogInterface dialogInterface, int buttonClickedType){
        if (buttonClickedType == DialogInterface.BUTTON_POSITIVE){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(getContext().getString(R.string.pref_key_alarm_volume), valueToSave);
            editor.commit();

        } else if (buttonClickedType == DialogInterface.BUTTON_NEGATIVE){
            setAlarmVolumeSeekBar(initialValue);
        }
    }


    private void setAlarmVolumeSeekBar(Integer preferenceValue){
        alarmeVolumeSeekBar.setProgress(preferenceValue);
    }


}

