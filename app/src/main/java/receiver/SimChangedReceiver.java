package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SimChangedReceiver extends BroadcastReceiver {
    public SimChangedReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SIM_STATE_CHANGED")) {
            Log.d("SimChangedReceiver", "--> SIM state changed <--");
        }
    }
}
