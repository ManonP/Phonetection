package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ihm15.project.phonetection.Data;

import managers.AlertManager;

/**
 * Created by Manon on 07/11/2015.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();
    private AlertManager alertManager;

    public SmsBroadcastReceiver(){
        alertManager = new AlertManager();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Data.getInstance(context);
        Bundle extras = intent.getExtras();

        String strMessage = Data.getSms();
        String strMsgBody = "";

     /*   if (extras != null ){
            Object[] smsextras = (Object[]) extras.get("pdus");
            for (int i= 0; i< smsextras.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                strMsgBody = smsMessage.getMessageBody().toString();
                Log.e(TAG,strMsgBody);
                Log.e(TAG + "data", strMessage);
            }
        }

        if (Data.isSimModeActivate() && strMsgBody.equals(strMessage)) {
            alertManager.startAlarm();
            Toast.makeText(context,"Message recu : " + strMsgBody, Toast.LENGTH_SHORT).show();
        }
*/
    }
}
