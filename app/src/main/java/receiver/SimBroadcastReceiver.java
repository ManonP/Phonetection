package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ihm15.project.phonetection.Data;

import managers.AlertManager;

public class SimBroadcastReceiver extends BroadcastReceiver {
    private AlertManager alertManager;

    public SimBroadcastReceiver() {
        alertManager = new AlertManager();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Data.getInstance(context);
        if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            Log.d("SimBroadcastReceiver", "--> SIM state changed <--");
            alertManager.startAlarm();
        }
    }
}
