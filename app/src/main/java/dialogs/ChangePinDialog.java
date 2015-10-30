package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

public class ChangePinDialog extends AbstractPinDialog {

    public ChangePinDialog(Context context) {
        super(context, context.getString(R.string.new_pin_choice), context.getString(R.string.validate_button),
                context.getString(R.string.cancel_button));
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
                state = States.IDLE;

                disablePositiveButton();
                enableNegativeButton();
                enablePinPad();
                disableClearButton();
                dismiss();
                showConfirmPinDialog();
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void showConfirmPinDialog(){
        ConfirmPinDialog cpd = new ConfirmPinDialog(context, pin);
        cpd.show(((FragmentActivity) context).getSupportFragmentManager(), "change_pin");
    }
}
