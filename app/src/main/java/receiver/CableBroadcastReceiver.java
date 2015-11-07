package receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.ihm15.project.phonetection.Data;


import managers.AlertManager;

/**
 * Created by Manon on 07/11/2015.
 */
public class CableBroadcastReceiver extends BroadcastReceiver {

    private String LOG_TAG_BROADCAST_SERVICE = "CableBroadcastReceiver";
    private AlertManager alertManager;
    public CableBroadcastReceiver() {
        alertManager = new AlertManager();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Data.getInstance(context);
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            Log.e(LOG_TAG_BROADCAST_SERVICE, "Je suis connecté ! ");
            Toast.makeText(context, "Je suis connecté !", Toast.LENGTH_SHORT).show();
        }else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            Log.e(LOG_TAG_BROADCAST_SERVICE,"Je suis déconnecté !");
            Toast.makeText(context,"Je suis deconnecté ! ", Toast.LENGTH_SHORT).show();
            if (Data.isCableModeActivate()) {
                alertManager.startAlarm();
            }
        }
    }
}
