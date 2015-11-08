package preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;

import dialogs.SmsDialog;

public class SmsPreference extends Preference implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public SmsPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        Data.getInstance(getContext());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.registerOnSharedPreferenceChangeListener(this);

        setSmsSummary();

        return view;
    }

    @Override
    protected void onClick() {
        SmsDialog cpd = new SmsDialog();
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "sms");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.println(Log.DEBUG, "", "BANANA :" + key);
        if (key.equals(getContext().getString(R.string.pref_key_sms))){
            setSmsSummary();
        }
    }


    public void setSmsSummary(){
        Data.getInstance(getContext());
        setSummary(Data.getSms());
    }
}