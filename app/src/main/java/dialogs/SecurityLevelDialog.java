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
public class SecurityLevelDialog extends DialogFragment implements View.OnClickListener,
        DialogInterface.OnClickListener{

    private Button positiveButton;
    private Button negativeButton;

    private RadioButton rbl;
    private RadioButton rbm;
    private RadioButton rbh;

    private String valueToSave;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.println(Log.DEBUG, "", "POTATOES");

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Data.getInstance(getContext());
        valueToSave = Data.getSecurityLevel();

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getContext(), R.style.AppTheme_Dialog_Alert));
        builder.setView(inflater.inflate(R.layout.security_level_layout, null));
        builder.setIcon(R.drawable.ic_security_black_36dp);
        builder.setTitle(getContext().getString(R.string.security_level_setting));
        builder.setPositiveButton(getContext().getString(R.string.ok_button), this);
        builder.setNegativeButton(getContext().getString(R.string.cancel_button), this);
        AlertDialog al = builder.create();
        al.show();

        positiveButton = al.getButton(AlertDialog.BUTTON_POSITIVE);
        negativeButton = al.getButton(AlertDialog.BUTTON_NEGATIVE);

        setDialogButtonTextColor();

        rbl = (RadioButton) al.findViewById(R.id.security_level_low);
        rbm = (RadioButton) al.findViewById(R.id.security_level_medium);
        rbh = (RadioButton) al.findViewById(R.id.security_level_high);


        rbl.setOnClickListener(this);
        rbm.setOnClickListener(this);
        rbh.setOnClickListener(this);

        initSelectedButton();

        al.hide();

        return al;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.security_level_low:
                valueToSave = getContext().getString(R.string.pref_security_level_low);
                break;
            case R.id.security_level_medium:
                valueToSave = getContext().getString(R.string.pref_security_level_medium);
                break;
            case R.id.security_level_high:
                valueToSave = getContext().getString(R.string.pref_security_level_high);
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
        Data.setSecurityLevel(valueToSave);
        dismiss();
    }

    protected void negativeButtonClicked(){
        dismiss();
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    public void initSelectedButton(){
        Data.getInstance(getContext());
        String sl = Data.getSecurityLevel();
        if (sl == getContext().getString(R.string.pref_security_level_low))
            rbl.setChecked(true);
        else if (sl == getContext().getString(R.string.pref_security_level_medium))
            rbm.setChecked(true);
        else rbh.setChecked(true);
    }


    protected void setDialogButtonTextColor (){
        positiveButton.setTextColor(getContext().getResources().getColor(R.color.accent));
        negativeButton.setTextColor(getContext().getResources().getColor(R.color.accent));
    }
}
