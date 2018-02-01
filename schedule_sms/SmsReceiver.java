package com.example.drago.schedule_sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Calendar;

public class SmsReceiver extends BroadcastReceiver {
    SharedPreferences sharedpreferences;
    SharedPreferences shared;

    String sms, smsMessage ;
    String senderNumber;
    boolean autoSentOn = true;
    boolean onOff;

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {

        sharedpreferences = context.getSharedPreferences("saveSMS", context.MODE_PRIVATE);
        smsMessage = sharedpreferences.getString("smsMessage", smsMessage);
       // Toast.makeText(context, "print sth: " + smsMessage, Toast.LENGTH_LONG).show();

        shared = context.getSharedPreferences("toggle", context.MODE_PRIVATE);
        onOff = shared.getBoolean("onOff", onOff);

        if (autoSentOn == onOff) {
                Bundle pudsBundle = intent.getExtras();
                if (pudsBundle != null) {
                    Object[] pdus = (Object[]) pudsBundle.get("pdus");
                    for (int i = 0; i < pdus.length; i++) {
                        SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sms = messages.getMessageBody();
                        //sender number received
                        senderNumber = messages.getOriginatingAddress();
                    }

                       //send out the response message after recieving incoming sms when is active
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(senderNumber, null, smsMessage, null, null);
                }
            } else {
                Toast.makeText(context, "auto send off", Toast.LENGTH_LONG).show();

            }

        }

}
