package preferences;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.ihm15.project.phonetection.R;

public class AlarmVolumePreference extends DialogPreference implements
        DialogInterface.OnClickListener {

    private SeekBar volumeSeekBar;

    public AlarmVolumePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogIcon(R.drawable.ic_volume_up_indigo_36px);
        setDialogTitle(context.getString(R.string.alarm_volume_setting));
        setDialogLayoutResource(R.layout.alarm_volume_layout);
        setPositiveButtonText(context.getString(R.string.ok_button));
        setNegativeButtonText(context.getString(R.string.cancel_button));
    }

    @Override
    protected void onBindDialogView(@NonNull View view) {
        super.onBindDialogView(view);
        volumeSeekBar = (SeekBar) view.findViewById(R.id.alarm_volume_seek_bar);
    }

    @Override
    protected void showDialog(Bundle state) {
        super.showDialog(state);
        setVolumeSeekBarProgress();
        setDialogButtonTextColor();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            persistInt(volumeSeekBar.getProgress());
        }
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////

    private int getAlarmVolumeValue(){
        return getSharedPreferences().getInt(getContext().getString(R.string.pref_key_alarm_volume),
                getContext().getResources().getInteger(R.integer.pref_alarm_volume_default));
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void setVolumeSeekBarProgress(){
        volumeSeekBar.setProgress(getAlarmVolumeValue());
    }

    protected void setDialogButtonTextColor (){
        Button b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);
        b.setTextColor(getContext().getResources().getColor(R.color.accent));
        b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_NEGATIVE);
        b.setTextColor(getContext().getResources().getColor(R.color.accent));
    }
}

