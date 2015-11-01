package preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ihm15.project.phonetection.R;

import dialogs.ChangePatternDialog;
import dialogs.EnterPatternDialog;
import events.UnlockObject;
import events.WrongUnlockObject;

public class PatternPreference extends Preference implements UnlockObject.UnlockedEventListener,
        WrongUnlockObject.WrongUnlockedEventListener, SharedPreferences.OnSharedPreferenceChangeListener{
    public PatternPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int pattern;

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        int defaultValue = getContext().getResources().getInteger(R.integer.pref_pattern_default);
        pattern = sp.getInt(getContext().getString(R.string.pref_key_pattern), defaultValue);
        sp.registerOnSharedPreferenceChangeListener(this);

        if (!isPreferenceSettedBefore()){
            setSummary(getContext().getString(R.string.pattern_not_set));
        } else {
            setSummary(null);
        }
        return view;
    }

    @Override
    protected void onClick() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        int defaultValue = getContext().getResources().getInteger(R.integer.pref_pattern_default);
        pattern = sp.getInt(getContext().getString(R.string.pref_key_pattern), defaultValue);

        Log.println(Log.DEBUG, "", "PATTERN: " + pattern + ", DEFAULT VALUE: " + defaultValue + " ["
                + isPreferenceSettedBefore() + "]");
        if (isPreferenceSettedBefore()){
            EnterPatternDialog epd = new EnterPatternDialog(getContext(), getContext().getString(R.string.cancel_button));
            epd.addUnlockedEventListener(this);
            epd.addWrongUnlockedEventListener(this);
            epd.setRightPattern(pattern);
            epd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "enter_patternn_dialog");

        } else {
            ChangePatternDialog cpd = new ChangePatternDialog(getContext());
            cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_pattern");
        }
    }
    private boolean isPreferenceSettedBefore(){
        int defaultValue = getContext().getResources().getInteger(R.integer.pref_pattern_default);
        return pattern != defaultValue;
    }

    @Override
    public void onUnlocked(UnlockObject.UnlockedEvent ue) {
        ChangePatternDialog cpd = new ChangePatternDialog(getContext());
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_pattern");
    }

    @Override
    public void onWrongUnlocked(WrongUnlockObject.WrongUnlockedEvent ue) {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        EnterPatternDialog epd = new EnterPatternDialog(getContext(), getContext().getString(R.string.cancel_button));
        epd.addUnlockedEventListener(this);
        epd.addWrongUnlockedEventListener(this);
        epd.setRightPattern(pattern);
        epd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "enter_pattern_dialog");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getContext().getString(R.string.pref_key_pattern))){
            setSummary(null);
        }
    }
}