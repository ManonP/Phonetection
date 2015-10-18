package dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Button;

import com.ihm15.project.phonetection.R;


public class ConfirmPinDialog extends AbstractPinDialog {

    private String newPin;

    public ConfirmPinDialog(Context context, String newPin){
        super(context, context.getString(R.string.new_pin_confirm),
                context.getString(R.string.clear_button),
                context.getString(R.string.confirm_button));
        this.newPin = newPin;
    }

    @Override
    protected void validateButtonClicked() {
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case UPDATING_PIN:
                //INTERDIT
                break;
            case PIN_COMPLETE:
                this.dismiss();
                if (!pin.equals(newPin)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setCancelable(false);
                    builder.setTitle(R.string.different_pin_dialog);
                    builder.setMessage(R.string.different_pin_dialog_message);
                    builder.setPositiveButton(R.string.ok_button, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog ad = builder.create();
                    ad.show();
                    Button button = ad.getButton(AlertDialog.BUTTON_POSITIVE);
                    button.setTextColor(getContext().getResources().getColor(R.color.accent));
                } else {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
                    sp.edit().putInt(
                            getContext().getString(R.string.pref_key_security_level),
                            Integer.parseInt(pin));
                }
        }
    }
}
