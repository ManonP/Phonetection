package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

import events.LockSetObject;
import events.WrongLockSetObject;


public class ChangePinDialogCardView extends AbstractPinDialog {

    private int type;
    private LockSetObject.LockSetEventListener lsel;
    private WrongLockSetObject.WrongLockSetEventListener wlsel;

    public ChangePinDialogCardView(Context context, LockSetObject.LockSetEventListener lsel,
                                   WrongLockSetObject.WrongLockSetEventListener wlsel, int type) {
        super(context, context.getString(R.string.new_pin_choice), context.getString(R.string.validate_button),
                context.getString(R.string.cancel_button));
        this.lsel = lsel;
        this.wlsel = wlsel;
        this.type = type;

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
                dismiss();
                showConfirmPinDialog();
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void showConfirmPinDialog(){
        ConfirmPinDialogCardView cpd = new ConfirmPinDialogCardView(context, pin, lsel, wlsel, type);
        cpd.show(((FragmentActivity) context).getSupportFragmentManager(), "change_pin");
    }
}
