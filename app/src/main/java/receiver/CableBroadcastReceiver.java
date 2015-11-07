package receiver;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.ihm15.project.phonetection.CardViewActivity;
import com.ihm15.project.phonetection.Data;
import com.ihm15.project.phonetection.R;


import managers.AlertManager;

/**
 * Created by Manon on 07/11/2015.
 */
public class CableBroadcastReceiver extends BroadcastReceiver {

    private String LOG_TAG_BROADCAST_SERVICE = "CableBroadcastReceiver";
    private AlertManager alertManager;

    public int ID_NOTIFICATION = 0;


    public CableBroadcastReceiver() {
        alertManager = new AlertManager();

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Data.getInstance(context);
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            Log.e(LOG_TAG_BROADCAST_SERVICE, "Je suis connecté ! ");
            Toast.makeText(context, "Je suis connecté !", Toast.LENGTH_SHORT).show();
            activeNotification(context);
        }else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            Log.e(LOG_TAG_BROADCAST_SERVICE,"Je suis déconnecté !");
            Toast.makeText(context,"Je suis deconnecté ! ", Toast.LENGTH_SHORT).show();
            if (Data.isCableModeActivate()) {
                alertManager.startAlarm();
            }
        }
    }

    private void activeNotification(Context context) {
        int icon = R.drawable.ic_phonelink_lock_white_36dp;
        CharSequence tickerText = "Activate the alarm";
        long when = System.currentTimeMillis();
        int currentapiVersion = Build.VERSION.SDK_INT;
        CharSequence text = "Remember to activate the alarm";
        CharSequence title = "Phonectection";
        long time = 30;
        Notification notification = new Notification(icon, text, time);
        Intent notificationIntent = new Intent(context,CardViewActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,0,notificationIntent,0);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        notification = builder.setContentIntent(contentIntent)
                .setSmallIcon(icon).setTicker(tickerText).setWhen(when)
                .setAutoCancel(false).setContentTitle(title)
                .setContentText(text).build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        manager.notify(ID_NOTIFICATION, notification);
    }
}
