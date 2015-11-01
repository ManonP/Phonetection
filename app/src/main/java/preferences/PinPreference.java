package preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ihm15.project.phonetection.R;

import dialogs.ChangePinDialog;
import dialogs.EnterPinDialog;
import events.UnlockObject;
import events.WrongUnlockObject;

public class PinPreference extends Preference implements UnlockObject.UnlockedEventListener,
        WrongUnlockObject.WrongUnlockedEventListener, SharedPreferences.OnSharedPreferenceChangeListener{
    public PinPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String pin;
    private MediaPlayer mp;

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultValue = getContext().getString(R.string.pref_pin_default);
        pin = sp.getString(getContext().getString(R.string.pref_key_pin), defaultValue);
        sp.registerOnSharedPreferenceChangeListener(this);
        mp = MediaPlayer.create(getContext(), R.raw.wrong);

        if (!isPreferenceSettedBefore()){
            setSummary(getContext().getString(R.string.pin_not_set));
        } else {
            setSummary(null);
        }
        return view;
    }

    @Override
    protected void onClick() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultValue = getContext().getString(R.string.pref_pin_default);
        pin = sp.getString(getContext().getString(R.string.pref_key_pin), defaultValue);
        Log.println(Log.DEBUG, "", "PIN: " + pin + ", DEFAULT VALUE: " + defaultValue + " ["
        + isPreferenceSettedBefore() + "]");
        if (isPreferenceSettedBefore()){
            EnterPinDialog epd = new EnterPinDialog(getContext(), getContext().getString(R.string.cancel_button));
            epd.addUnlockedEventListener(this);
            epd.addWrongUnlockedEventListener(this);
            epd.setRightPin(pin);
            epd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "enter_pin_dialog");

        } else {
            ChangePinDialog cpd = new ChangePinDialog(getContext());
            cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_pin");
        }
    }

    private boolean isPreferenceSettedBefore(){
        String defaultValue = getContext().getString(R.string.pref_pin_default);
        return !pin.equals(defaultValue);
    }

    @Override
    public void onUnlocked(UnlockObject.UnlockedEvent ue) {
        ChangePinDialog cpd = new ChangePinDialog(getContext());
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_pin");
    }

    @Override
    public void onWrongUnlocked(WrongUnlockObject.WrongUnlockedEvent ue) {
        mp.start();
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        EnterPinDialog epd = new EnterPinDialog(getContext(), getContext().getString(R.string.cancel_button));
        epd.addUnlockedEventListener(this);
        epd.addWrongUnlockedEventListener(this);
        epd.setRightPin(pin);
        epd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "enter_pin_dialog");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getContext().getString(R.string.pref_key_pin))){
            setSummary(null);
        }
    }
}
