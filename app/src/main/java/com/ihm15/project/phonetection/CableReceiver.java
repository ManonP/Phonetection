package com.ihm15.project.phonetection;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class CableReceiver extends BroadcastReceiver {

    private String TAG = "BroadcastReceiver";
    private Alarme alarme = new Alarme();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received system broadcast");
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            alarme.cancelTimer();
            Library.CONNECTED = true;
        } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            if (Library.CABLE_MODE) {
                alarme.activeWarning(context);
                Library.WARNING_BY = 2;
            }
            Library.CONNECTED = false;
        }
        //Intent intentStartActivity = new Intent(context, CardViewActivity.class);
        //intentStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intentStartActivity);
    }

}
