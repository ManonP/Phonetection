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

import dialogs.GraceTimeDialog;

public class GraceTimePreference extends Preference implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    public GraceTimePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        View view = super.onCreateView(parent);
        Data.getInstance(getContext());
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        sp.registerOnSharedPreferenceChangeListener(this);

        setGraceTimeSummary();

        return view;
    }

    @Override
    protected void onClick() {
        GraceTimeDialog cpd = new GraceTimeDialog();
        cpd.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "grace_time");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.println(Log.DEBUG, "", "BANANA :" + key);
        if (key.equals(getContext().getString(R.string.pref_key_grace_time))){
            setGraceTimeSummary();
        }
    }


    public void setGraceTimeSummary(){
        Data.getInstance(getContext());
        int gt = Data.getGraceTime();
        if (gt == getContext().getResources().getInteger(R.integer.pref_grace_time_5))
            setSummary(getContext().getString(R.string.grace_time_5_seconds));
        else if (gt == getContext().getResources().getInteger(R.integer.pref_grace_time_10))
            setSummary(getContext().getString(R.string.grace_time_10_seconds));
        else if (gt == getContext().getResources().getInteger(R.integer.pref_grace_time_15))
            setSummary(getContext().getString(R.string.grace_time_15_seconds));
        else setSummary(getContext().getString(R.string.grace_time_30_seconds));
    }
}