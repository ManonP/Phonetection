package dialogs;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.ihm15.project.phonetection.R;

public class ChangePatternDialog extends AbstractPatternDialog {
    public ChangePatternDialog(Context context){
        super(context, context.getString(R.string.new_pattern_choice),
                context.getString(R.string.validate_button),
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
            case UPDATING_PATTERN:
                //FORBIDDEN
                Log.println(Log.ERROR, "",
                        "Positive button clicked error: state == UPDATING_PATTERN -> FORBIDDEN");
                break;
            case PATTERN_COMPLETE:
                state = States.IDLE;

                disablePositiveButton();
                enableNegativeButton();
                dismiss();
                showConfirmPatternDialog();

        }
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////

    private void showConfirmPatternDialog(){
        ConfirmPatternDialog cpd = new ConfirmPatternDialog(context, pattern);
        cpd.show(((FragmentActivity) context).getSupportFragmentManager(), "change_pattern");
    }
}
