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

import dialogs.ChangeImageDialog;
import dialogs.EnterImageDialog;
import events.UnlockObject;
import events.WrongUnlockObject;

public class ImagePreference extends Preference implements UnlockObject.UnlockedEventListener,
        WrongUnlockObject.WrongUnlockedEventListener, SharedPreferences.OnSharedPreferenceChangeListener{
    public ImagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String image;

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultValue = getContext().getString(R.string.pref_image_default);
        image = sp.getString(getContext().getString(R.string.pref_key_image), defaultValue);
        sp.registerOnSharedPreferenceChangeListener(this);

        if (!isPreferenceSettedBefore()){
            setSummary(getContext().getString(R.string.image_not_set));
        } else {
            setSummary(null);
        }
        return view;
    }

    @Override
    protected void onClick() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultValue = getContext().getString(R.string.pref_image_default);
        image = sp.getString(getContext().getString(R.string.pref_key_image), defaultValue);

        Log.println(Log.DEBUG, "", "IMAGE: " + image + ", DEFAULT VALUE: " + defaultValue + " ["
                + isPreferenceSettedBefore() + "]");
        if (isPreferenceSettedBefore()){
            EnterImageDialog epd = new EnterImageDialog(getContext(), getContext().getString(R.string.cancel_button));
            epd.addUnlockedEventListener(this);
            epd.addWrongUnlockedEventListener(this);
            epd.setRightImage(image);
            epd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "enter_image_dialog");

        } else {
            ChangeImageDialog cpd = new ChangeImageDialog(getContext());
            cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_image");
        }
    }
    private boolean isPreferenceSettedBefore(){
        String defaultValue = getContext().getString(R.string.pref_image_default);
        return !image.equals(defaultValue);
    }

    @Override
    public void onUnlocked(UnlockObject.UnlockedEvent ue) {
        ChangeImageDialog cpd = new ChangeImageDialog(getContext());
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "choose_image");
    }

    @Override
    public void onWrongUnlocked(WrongUnlockObject.WrongUnlockedEvent ue) {
        Vibrator v = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);
        EnterImageDialog epd = new EnterImageDialog(getContext(), getContext().getString(R.string.cancel_button));
        epd.addUnlockedEventListener(this);
        epd.addWrongUnlockedEventListener(this);
        epd.setRightImage(image);
        epd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "enter_image_dialog");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.println(Log.DEBUG, "", "BANANA :" + key);
        if (key.equals(getContext().getString(R.string.pref_key_image))){
            setSummary(null);
        }
    }
}