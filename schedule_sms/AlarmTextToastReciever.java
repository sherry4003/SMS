package com.example.drago.schedule_sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by drago on 12/14/2017.
 */

public class AlarmTextToastReciever extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
        Intent background = new Intent(context, BackgroundService.class);
        context.startService(background);
        HistoryViewActivity activity = HistoryViewActivity.getInstance();
        //String message = finalMessage;
        //String number = finalPhoneNumber;
        ///for loop that goes through all contacts, any contacts bellow time will be sent
        if (activity != null) {
            List<Contact> smsList = activity.getContactSMSList();
            Log.d("dfgh", "The size is " + smsList.size());
            Calendar currentCal = Calendar.getInstance();
            String message;
            String number;
            long currentTimeLong = currentCal.getTime().getTime();
            for (int i = 0; i < smsList.size(); i++) {
                int minute = smsList.get(i).getMinute();
                int hour = smsList.get(i).getHour();
                int day = smsList.get(i).getDay();
                int month = smsList.get(i).getMonth();
                int year = smsList.get(i).getYear();
                message = smsList.get(i).getMessage();
                number = smsList.get(i).getPhoneNum();//"5712349156";
//            Log.d("dfgh", "The first message is " + smsList.get(i).getMessage());
//            Log.d("dfgh", "The first message is " + smsList.get(i).getPhoneNum());
                Calendar contactTime = Calendar.getInstance();
                Log.d("dfgh", "The time is " + year);
                month = month - 1;
                contactTime.set(year, month, day); //year month day
                contactTime.set(Calendar.MINUTE, minute);
                contactTime.set(Calendar.HOUR_OF_DAY, hour);
                contactTime.set(Calendar.SECOND, 0);
                long contactTimeLong = contactTime.getTime().getTime();
                Toast.makeText(context, "Contact time " + contactTimeLong + "Current itme " + currentTimeLong, Toast.LENGTH_LONG).show();

                if ((contactTimeLong / 120000) <= (currentTimeLong / 120000)) {
                    //message = smsList.get(i).getMessage();
                    //number = smsList.get(i).getPhoneNum();//"5712349156";
                    Log.d("dfgh", "The number is " + number);
                    Log.d("dfgh", "The message is " + message);
                    Toast.makeText(context, "Your text message of " + message + " has been sent", Toast.LENGTH_LONG).show();
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, message, null, null);
                    String[] name = {number};
                    activity.remove(name);

                }
            }
        }
    }
}
