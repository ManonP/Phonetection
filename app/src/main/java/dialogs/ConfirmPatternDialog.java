package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

public class ConfirmPatternDialog extends AbstractPatternDialog {


    int newPattern;

    public ConfirmPatternDialog(Context context, int newPattern){
        super(context, context.getString(R.string.new_pattern_confirm),
                context.getString(R.string.confirm_button),
                context.getString(R.string.cancel_button));
        this.newPattern = newPattern;
    }

    //SEEHEIM-NOYAU FONCTIONNEL/////////////////////////////////////////////////////////////////////

    protected void savePattern() {
        Data.getInstance(getActivity());
        Data.setPattern(pattern);
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
                        "Positive button clicked error: state == UPDATING_PATTERN -> FORBIDDEN");
                break;
            case PATTERN_COMPLETE:
                if (pattern != newPattern){
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
                    showWrongPatternDialog();
                    dismiss();
                } else {
                    state = States.IDLE;

                    disablePositiveButton();
                    enableNegativeButton();
                    showSuccessDialog();
                    savePattern();
                    dismiss();
                }
        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    protected void showSuccessDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_check_circle_black_36dp,
                getContext().getString(R.string.pattern_saved_dialog), null,
                getContext().getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pin_change_success");
    }

    protected void showWrongPatternDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_error_white_36px,
                getContext().getString(R.string.different_pattern_dialog),
                getContext().getString(R.string.different_pattern_dialog_message),
                getContext().getString(R.string.ok_button), null, null);
        cmd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "pattern_change_failure");
    }
}
