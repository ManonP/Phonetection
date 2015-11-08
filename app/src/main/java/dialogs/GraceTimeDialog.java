package dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

/**
 * Created by Dimitri on 08/11/2015.
 */
public class GraceTimeDialog extends DialogFragment implements View.OnClickListener,
        DialogInterface.OnClickListener{

    private Button positiveButton;
    private Button negativeButton;

    private RadioButton rb5;
    private RadioButton rb10;
    private RadioButton rb15;
    private RadioButton rb30;

    private int valueToSave;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.println(Log.DEBUG, "", "POTATOES");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Data.getInstance(getContext());
        valueToSave = Data.getGraceTime();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme_Dialog_Alert));
        builder.setView(inflater.inflate(R.layout.grace_time_layout, null));
        builder.setIcon(R.drawable.ic_timer_black_36dp);
        builder.setTitle(getContext().getString(R.string.grace_time_setting));
        builder.setPositiveButton(getContext().getString(R.string.ok_button), this);
        builder.setNegativeButton(getContext().getString(R.string.cancel_button), this);
        AlertDialog al = builder.create();
        al.show();

        positiveButton = al.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = al.getButton(AlertDialog.BUTTON_NEGATIVE);

        setDialogButtonTextColor();

        rb5 = (RadioButton) al.findViewById(R.id.grace_time_5_seconds);
        rb10 = (RadioButton) al.findViewById(R.id.grace_time_10_seconds);
        rb15 = (RadioButton) al.findViewById(R.id.grace_time_15_seconds);
        rb30 = (RadioButton) al.findViewById(R.id.grace_time_30_seconds);

        rb5.setOnClickListener(this);
        rb10.setOnClickListener(this);
        rb15.setOnClickListener(this);
        rb30.setOnClickListener(this);

        initSelectedButton();

        al.hide();

        return al;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.grace_time_5_seconds:
                valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_5);
                break;
            case R.id.grace_time_10_seconds:
                valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_10);
                break;
            case R.id.grace_time_15_seconds:
                valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_15);
                break;
            case R.id.grace_time_30_seconds:
                valueToSave = getContext().getResources().getInteger(R.integer.pref_grace_time_30);
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

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    protected void positiveButtonClicked(){
        Data.setGraceTime(valueToSave);
        dismiss();
    }

    protected void negativeButtonClicked(){
        dismiss();
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    public void initSelectedButton(){
        Data.getInstance(getContext());
        int gt = Data.getGraceTime();
        if (gt == getContext().getResources().getInteger(R.integer.pref_grace_time_5))
            rb5.setChecked(true);
        else if (gt == getContext().getResources().getInteger(R.integer.pref_grace_time_10))
            rb10.setChecked(true);
        else if (gt == getContext().getResources().getInteger(R.integer.pref_grace_time_15))
            rb15.setChecked(true);
        else rb30.setChecked(true);
    }


    protected void setDialogButtonTextColor (){
        positiveButton.setTextColor(getContext().getResources().getColor(R.color.accent));
        negativeButton.setTextColor(getContext().getResources().getColor(R.color.accent));
    }
}
