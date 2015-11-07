package service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ihm15.project.phonetection.Data;

import managers.AlertManager;
import receiver.CableBroadcastReceiver;

public class CableService extends Service {

    private String TAG = "BroadcastReceiver";
    private boolean mReceiverRegistered = false;
    private AlertManager alertManager;
    private CableBroadcastReceiver broadcastService;

    @Override
    public void onCreate() {
        super.onCreate();
        alertManager = new AlertManager();
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
/*
    private final BroadcastReceiver cableReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                Log.e(TAG, "Je suis connecté ! ");
                Toast.makeText(getApplicationContext(),"Je suis connecté !",Toast.LENGTH_SHORT).show();
            }else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
                Log.e(TAG,"Je suis déconnecté !");
                Toast.makeText(getApplicationContext(),"Je suis deconnecté ! ", Toast.LENGTH_SHORT).show();
                if (Data.isCableModeActivate()) {
                    alertManager.startAlarm();
                }
            }

        }
    };*/

/*
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Received system broadcast");*/
        /*if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {
            alarme.cancelTimer();
            Library.CONNECTED = true;
        } else if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {
            if (Library.CABLE_MODE) {
                alarme.activeWarning(context);
                Library.WARNING_BY = 2;
            }
            Library.CONNECTED = false;
        }*/
        //Intent intentStartActivity = new Intent(context, CardViewActivity.class);
        //intentStartActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(intentStartActivity);
    //}

}
