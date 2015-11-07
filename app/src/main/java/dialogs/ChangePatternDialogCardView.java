package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

import events.LockSetObject;
import events.UnlockObject;
import events.WrongLockSetObject;
import events.WrongUnlockObject;

/**
 * Created by Dimitri on 07/11/2015.
 */
public class ChangePatternDialogCardView extends AbstractPatternDialog {

    private LockSetObject.LockSetEventListener lsel;
    private WrongLockSetObject.WrongLockSetEventListener wlsel;
    private int type;

    public ChangePatternDialogCardView(Context context, LockSetObject.LockSetEventListener lsel,
                                       WrongLockSetObject.WrongLockSetEventListener wlsel, int type) {
        super(context, context.getString(R.string.new_pattern_choice), context.getString(R.string.validate_button),
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
            case UPDATING_PATTERN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PIN -> FORBIDDEN");
                break;
            case PATTERN_COMPLETE:
                showConfirmPinDialog();
                dismiss();
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void showConfirmPinDialog(){
        ConfirmPatternDialogCardView cpd = new ConfirmPatternDialogCardView(context, pattern, lsel, wlsel, type);
        cpd.show(((FragmentActivity) context).getSupportFragmentManager(), "change_pattern");
    }
}
