package service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import receiver.SmsBroadcastReceiver;

/**
 * Created by Manon on 07/11/2015.
 */
public class SmsService extends Service {
    private SmsBroadcastReceiver mSmsReceiver;
    private IntentFilter mIntentFilter;


    @Override
    public void onCreate() {
        super.onCreate();

        mSmsReceiver = new SmsBroadcastReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(mSmsReceiver,mIntentFilter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mSmsReceiver);
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
