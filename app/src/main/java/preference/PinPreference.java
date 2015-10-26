package preference;


import android.content.Context;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;

import dialog.ChangePinDialog;

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
