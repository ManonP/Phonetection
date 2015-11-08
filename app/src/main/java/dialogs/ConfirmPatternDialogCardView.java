package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

import events.LockSetObject;
import events.WrongLockSetObject;

/**
 * Created by Dimitri on 07/11/2015.
 */
public class ConfirmPatternDialogCardView extends AbstractPatternDialog {

    private int newPattern;
    private LockSetObject lockSetObject;
    private WrongLockSetObject wrongLockSetObject;

    private LockSetObject.LockSetEventListener lsel;
    private WrongLockSetObject.WrongLockSetEventListener wlsel;
    private int type;

    public ConfirmPatternDialogCardView(Context context, int newPattern,
                                        LockSetObject.LockSetEventListener lsel,
                                        WrongLockSetObject.WrongLockSetEventListener wlsel,
                                        int type) {
        super(context, context.getString(R.string.new_pattern_confirm), context.getString(R.string.confirm_button),
                context.getString(R.string.cancel_button));
        this.newPattern = newPattern;

        this.lsel = lsel;
        this.wlsel = wlsel;
        this.type = type;
        lockSetObject = new LockSetObject(this, type);
        lockSetObject.addLockSetEventListener(lsel);
        wrongLockSetObject = new WrongLockSetObject(this, type);
        wrongLockSetObject.addWrongLockSetEventListener(wlsel);

    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////
    protected void savePattern() {
        Data.getInstance(context);
        Data.setPattern(pattern);
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////
    @Override
    protected void positiveButtonClicked() {
        switch (state) {
            case IDLE:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == IDLE -> FORBIDDEN");
                break;
            case UPDATING_PATTERN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PIN -> FORBIDDEN");
                break;
            case PATTERN_COMPLETE:
                if (pattern != newPattern) {
                    showWrongPatternDialog();
                    dismiss();
                    wrongLockSetObject.fireWrongLockSetEvent();
                } else {
                    String sl = Data.getSecurityLevel();
                    String msl = context.getString(R.string.pref_security_level_medium);
                    String hsl = context.getString(R.string.pref_security_level_high);
                    String i = Data.getImage();
                    String di = context.getString(R.string.pref_image_default);
                    if (sl.equals(msl)) {
                        savePattern();
                        showSuccessDialog();
                        dismiss();
                        lockSetObject.fireLockSetEvent();
                    } else if ((sl.equals(hsl)) && i.equals(di)) {
                        savePattern();
                        showChangeImageDialogCardView();
                        dismiss();
                    }
                }
                break;
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void showSuccessDialog() {
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_check_circle_black_36dp,
                context.getString(R.string.pattern_saved_dialog), null,
                context.getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) context).getSupportFragmentManager(), "pattern_change_success");
    }

    protected void showWrongPatternDialog() {
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_error_white_36px,
                context.getString(R.string.different_pattern_dialog),
                context.getString(R.string.different_pattern_dialog_message),
                context.getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) context).getSupportFragmentManager(), "pattern_change_failure");
    }

    protected void showChangeImageDialogCardView() {
        ChangeImageDialogCardView cid = new ChangeImageDialogCardView(context, lsel, wlsel, type);
        cid.show(((FragmentActivity) context).getSupportFragmentManager(), "change_image");
    }
}
