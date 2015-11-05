package dialogs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(getContext().getString(R.string.pref_key_pin), pin);
        editor.commit();
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
                    enableNegativeButton();
                    enablePinPad();
                    disableClearButton();
                    showWrongPinDialog();
                    dismiss();
                } else {
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
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
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_check_circle_black_36dp,
                context.getString(R.string.pin_saved_dialog), null,
                context.getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) context).getSupportFragmentManager(), "pin_change_success");
    }

    protected void showWrongPinDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_error_white_36px,
                context.getString(R.string.different_pin_dialog),
                context.getString(R.string.different_pin_dialog_message),
                context.getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) context).getSupportFragmentManager(), "pin_change_failure");
    }
}
