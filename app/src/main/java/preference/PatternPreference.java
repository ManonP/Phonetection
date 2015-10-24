package preference;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.Window;

import com.ihm15.project.phonetection.R;

import dialog.ChangePatternDialog;
import dialog.ChangePinDialog;

public class PatternPreference extends Preference {
    public PatternPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        ChangePatternDialog cpd = new ChangePatternDialog(getContext());
        cpd.show();
        cpd.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.ic_apps_indigo_36px);
    }
}