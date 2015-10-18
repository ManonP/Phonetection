package preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Window;

import com.ihm15.project.phonetection.R;

import dialog.ChangePinDialog;

public class PinPreference extends Preference {
    public PinPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        ChangePinDialog cpd = new ChangePinDialog(getContext());
        cpd.show();
        cpd.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_dialpad_indigo_36px);
    }
}
