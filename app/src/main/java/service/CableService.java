package service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ihm15.project.phonetection.Data;

import receiver.CableBroadcastReceiver;

public class CableService extends Service {

    private String TAG = "BroadcastReceiver";
    private boolean mReceiverRegistered = false;
    private CableBroadcastReceiver broadcastService;

    @Override
    public void onCreate() {
        super.onCreate();
        Data.getInstance(getApplicationContext());
        broadcastService = new CableBroadcastReceiver();

        IntentFilter intentToReceiveFilter = new IntentFilter();
        intentToReceiveFilter.addAction(POWER_SERVICE);
        this.registerReceiver(broadcastService,intentToReceiveFilter);
        mReceiverRegistered = true;
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();
        if (mReceiverRegistered) {
            unregisterReceiver(broadcastService);
            mReceiverRegistered = false;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
