package service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ihm15.project.phonetection.Data;

import managers.AlertManager;
import receiver.SimBroadcastReceiver;

/**
 * Created by Manon on 07/11/2015.
 */
public class SimService extends Service {

    private String TAG = "SimService";
    private AlertManager alertManager;
    private boolean mReceiverRegistred = false;
    private SimBroadcastReceiver simBroadcastReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        alertManager = new AlertManager();
        Data.getInstance(getApplicationContext());
        simBroadcastReceiver = new SimBroadcastReceiver();

        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction("android.intent.action.SIM_STATE_CHANGED");
        this.registerReceiver(simBroadcastReceiver, intentToReceiveFilter);
        mReceiverRegistred = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mReceiverRegistred) {
            unregisterReceiver(simBroadcastReceiver);
            mReceiverRegistred =false;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
