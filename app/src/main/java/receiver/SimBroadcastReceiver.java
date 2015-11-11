package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ihm15.project.phonetection.Data;

public class SimBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Data.getInstance(context);
        if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            Log.d("SimBroadcastReceiver", "--> SIM state changed <--");
            if (Data.isSimModeActivate()) {
                Intent i = new Intent();
                i.setClassName("com.ihm15.project.phonetection",
                        "com.ihm15.project.phonetection.CardViewActivity");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(Data.EXTRA_WITH_ALARM,true);
                i.putExtra(Data.EXTRA_MODE, Data.SIM_MODE);
                Data.setSimMode(false);
                context.startActivity(i);
            }
        }
    }
}
