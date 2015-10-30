package preferences;

import android.content.Context;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;

import dialogs.ChangePatternDialog;

public class PatternPreference extends Preference{
    public PatternPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onClick() {
        ChangePatternDialog cpd = new ChangePatternDialog(getContext());
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_pattern");
    }
}