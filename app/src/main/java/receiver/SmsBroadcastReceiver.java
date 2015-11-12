package receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ihm15.project.phonetection.Data;

/**
 * Created by Manon on 07/11/2015.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Data.getInstance(context);
        Bundle extras = intent.getExtras();

        String strMessage = Data.getSms();
        String strMsgBody = "";

        if (extras != null ){
            Object[] smsextras = (Object[]) extras.get("pdus");
            for (int i= 0; i< smsextras.length; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) smsextras[i]);
                strMsgBody = smsMessage.getMessageBody().toString();
            }
        }
        if (Data.isSmsModeActivate() && strMsgBody.equals(strMessage)) {
            Intent i = new Intent();
            i.setClassName("com.ihm15.project.phonetection",
                    "com.ihm15.project.phonetection.CardViewActivity");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Data.EXTRA_WITH_ALARM, true);
            i.putExtra(Data.EXTRA_MODE, Data.SMS_MODE);
            Data.setSmsMode(false);
            context.startActivity(i);
        }

    }
}
