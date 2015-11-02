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
       // Toast.makeText(context, "Je suis le onReceive ! ", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            //Toast.makeText(context, "Je suis connecté ! ", Toast.LENGTH_LONG).show();
            alarme.cancelTimer();
            Library.CONNECTED = true;
        } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            //Déclenchement de l'alarme si le mode est activé
            if (Library.CABLE_MODE) {
                alarme.activeWarningCable(context);
                Library.WARNING_BY = 2;
                //Library.CONNECTED = false;
            }
            //Toast.makeText(context, "Je suis déconnecté ! ", Toast.LENGTH_LONG).show();
            Library.CONNECTED = false;
        }


        //Intent intentStartActivity = new Intent(context, CardViewActivity.class);
        //intentStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intentStartActivity);
    }

}
