package dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.ihm15.project.phonetection.R;


public class ConfirmPinDialog extends AbstractPinDialog {

    private String newPin;

    public ConfirmPinDialog(Context context,String  newPin) {
        super(context, context.getString(R.string.new_pin_confirm), context.getString(R.string.confirm_button),
                context.getString(R.string.cancel_button));
        this.newPin = newPin;
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////
    protected void savePin(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.edit().putInt(
                getContext().getString(R.string.pref_key_pin),
                Integer.parseInt(pin));
        sp.edit().commit();
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////
    @Override
    protected void positiveButtonClicked() {
        switch (state){
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case UPDATING_PIN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PIN -> FORBIDDEN");
                break;
            case PIN_COMPLETE:
                if(!pin.equals(newPin)){
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNeagtiveButton();
                    enablePinPad();
                    disableClearButton();
                    showWrongPinDialog();
                    dismiss();
                } else {
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNeagtiveButton();
                    enablePinPad();
                    disableClearButton();
                    savePin();
                    showSuccessDialog();
                    dismiss();
                }
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void showSuccessDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_done_indigo_36px,
                getContext().getString(R.string.pin_saved_dialog), null,
                getContext().getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pin_change_success");
    }

    protected void showWrongPinDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_error_outline_red_36px,
                getContext().getString(R.string.different_pin_dialog),
                getContext().getString(R.string.different_pin_dialog_message),
                getContext().getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pin_change_failure");
    }
}
