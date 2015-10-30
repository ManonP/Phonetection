package preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;

import com.ihm15.project.phonetection.R;

import dialogs.ChangePinDialog;

public class PinPreference extends Preference {
    public PinPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        ChangePinDialog cpd = new ChangePinDialog(getContext());
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_pin");
    }
}
