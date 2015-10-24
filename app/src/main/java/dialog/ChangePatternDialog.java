package dialog;

import android.content.Context;
import android.view.Window;

import com.ihm15.project.phonetection.R;

public class ChangePatternDialog extends AbstractPatternDialog {
    public ChangePatternDialog(Context context){
        super(context, context.getString(R.string.new_pattern_choice),
                context.getString(R.string.clear_button),
                context.getString(R.string.validate_button));
    }

    @Override
    protected void validateButtonClicked() {
        switch (state){
            case IDLE:
                //INTERDIT
                break;
            case UPDATING_PATTERN:
                //INTERDIT
                break;
            case PATTERN_COMPLETE:
                ConfirmPatternDialog cpd = new ConfirmPatternDialog(getContext(), pattern);
                this.dismiss();
                cpd.show();
                cpd.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_apps_indigo_36px);

        }
    }
}
