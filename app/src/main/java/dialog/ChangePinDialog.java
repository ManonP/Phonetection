package dialog;

import android.content.Context;
import android.view.Window;

import com.ihm15.project.phonetection.R;

public class ChangePinDialog extends AbstractPinDialog {

    public ChangePinDialog(Context context){
        super(context, context.getString(R.string.new_pin_choice),
                context.getString(R.string.clear_button),
                context.getString(R.string.validate_button));
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
                ConfirmPinDialog cpd = new ConfirmPinDialog(getContext(), pin);
                this.dismiss();
                cpd.show();
                cpd.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_dialpad_indigo_36px);
        }
    }
}
