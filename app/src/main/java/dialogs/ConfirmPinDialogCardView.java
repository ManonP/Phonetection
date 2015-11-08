package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

import events.LockSetObject;
import events.WrongLockSetObject;


public class ConfirmPinDialogCardView extends AbstractPinDialog {

    private String newPin;
    private LockSetObject lockSetObject;
    private WrongLockSetObject wrongLockSetObject;

    private LockSetObject.LockSetEventListener lsel;
    private WrongLockSetObject.WrongLockSetEventListener wlsel;
    private int type;

    public ConfirmPinDialogCardView(Context context, String  newPin,
                                    LockSetObject.LockSetEventListener lsle,
                                    WrongLockSetObject.WrongLockSetEventListener wlsel, int type) {
        super(context, context.getString(R.string.new_pin_confirm), context.getString(R.string.confirm_button),
                context.getString(R.string.cancel_button));
        this.newPin = newPin;
        this.lsel = lsle;
        this.wlsel = wlsel;
        this.type = type;

        lockSetObject = new LockSetObject(this, type);
        lockSetObject.addLockSetEventListener(lsle);
        wrongLockSetObject = new WrongLockSetObject(this, type);
        wrongLockSetObject.addWrongLockSetEventListener(wlsel);
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////
    protected void savePin(){
        Data.getInstance(getContext());
        Data.setPin(pin);
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
                    showWrongPinDialog();
                    dismiss();
                    wrongLockSetObject.fireWrongLockSetEvent();
                } else {
                    String sl = Data.getSecurityLevel();
                    String lsl = context.getString(R.string.pref_security_level_low);
                    String msl = context.getString(R.string.pref_security_level_medium);
                    String hsl = context.getString(R.string.pref_security_level_high);
                    int p = Data.getPattern();
                    int dp = context.getResources().getInteger(R.integer.pref_pattern_default);
                    String i = Data.getImage();
                    String di = context.getString(R.string.pref_image_default);
                    if (sl.equals(lsl)){
                        savePin();
                        showSuccessDialog();
                        dismiss();
                        lockSetObject.fireLockSetEvent();
                    } else if ((sl.equals(msl) && p == dp)
                            ||((sl.equals(hsl)) && p == dp)){
                        savePin();
                        showChangePatternDialogCardView();
                        dismiss();
                    } else if ((sl.equals(hsl)) && i.equals(di)){
                        savePin();
                        showChangeImageDialogCardView();
                        dismiss();
                    }
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

    protected void showChangePatternDialogCardView(){
        ChangePatternDialogCardView cpd = new ChangePatternDialogCardView(context, lsel, wlsel, type);
        cpd.show(((FragmentActivity) context).getSupportFragmentManager(), "change_pattern");
    }

    protected void showChangeImageDialogCardView(){
        ChangeImageDialogCardView cid = new ChangeImageDialogCardView(context, lsel, wlsel, type);
        cid.show(((FragmentActivity) context).getSupportFragmentManager(), "change_image");
    }
}
