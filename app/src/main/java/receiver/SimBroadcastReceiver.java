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

        }
    }
}
