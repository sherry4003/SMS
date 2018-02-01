package com.example.drago.schedule_sms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class AutoSetActivity extends AppCompatActivity {
    Button startTimebtn, endTimebtn;
    TextView startTimetxt, endtTimetxt;
    EditText mesgEditText;
    CheckBox mon, tues, wed, thur, fri, sat, sun;
    ToggleButton onOffToggle;
    String smsMessage, startTime, endTime;
    SharedPreferences sharedpreferences, sharedpref;
    PendingIntent pendingIntent;
    AlarmManager alarmManager;
    Calendar cal;
    int sHour, sMinute;
    int eHour, eMinute;
    boolean toggle;
    Calendar start, end ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_set);

        //defind button
        startTimebtn = (Button) findViewById(R.id.startTimebtn);
        endTimebtn = (Button) findViewById(R.id.endTimebtn);
        onOffToggle = (ToggleButton) findViewById(R.id.toggleButton);

        //define starttime and end time text field
        startTimetxt = (EditText) findViewById(R.id.startTimetxt);
        endtTimetxt = (EditText) findViewById(R.id.endtTimetxt);
        //define edit text message feild
        mesgEditText = (EditText) findViewById(R.id.mesgEditText);
        //define checkboxes for monday - sunday
        mon = (CheckBox) findViewById(R.id.mon);
        tues = (CheckBox) findViewById(R.id.tues);
        wed = (CheckBox) findViewById(R.id.wed);
        thur = (CheckBox) findViewById(R.id.thur);
        fri = (CheckBox) findViewById(R.id.fri);
        sat = (CheckBox) findViewById(R.id.sun);
        sun = (CheckBox) findViewById(R.id.sun);


    }

    //toggle butoon onClick listener
    // conttol the set auto is on or off
    public void onToggleClicked(View view) {
        sharedpreferences  = getApplicationContext().getSharedPreferences("toggle", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        try {
            if (validation()) {
                if (((ToggleButton) view).isChecked()) {
                    toggle = true;
                    setAlarm();
                    Toast.makeText(getApplicationContext(), "on", Toast.LENGTH_LONG).show();
                    editor.putBoolean("onOff", toggle);
                } else {
                    toggle = false;
                    //  setAlarm();
                    Toast.makeText(getApplicationContext(), "off.", Toast.LENGTH_LONG).show();
                    editor.putBoolean("onOff", toggle);
                }

                editor.commit();
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Fill out all field before turn it on", Toast.LENGTH_LONG).show();

        }

    }


    //Select Start time onclick listener
    public void StartTimeButtonClicked(View view) {
        cal = Calendar.getInstance();
        sHour = cal.get(Calendar.HOUR);
        sMinute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                AutoSetActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        sharedpref = getApplicationContext().getSharedPreferences("savePref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpref.edit();

                        start = Calendar.getInstance();
                        start.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        start.set(Calendar.MINUTE, minute);
                        editor.putInt("startHour", hourOfDay);
                        editor.putInt("startMin", minute);

                        editor.commit();

                        String minute_str = "";
                        String hour_str = "";
                        if (minute < 10) {
                            minute_str = "0" + minute;
                        } else {
                            minute_str = minute+"";
                        }
                        if (hourOfDay < 10) {
                            hour_str = "0" + hourOfDay;
                        } else {
                            hour_str = "" + hourOfDay;
                        }

                        startTimetxt.setText(hour_str + ": " + minute_str);
                    }
                }, sHour, sMinute, false);
        timePickerDialog.show();

    }

    //Select End Time onlick listner
    public void EndTimeButtonClicked(View view) {

        cal = Calendar.getInstance();
        eHour = cal.get(Calendar.HOUR);
        eMinute = cal.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                AutoSetActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        end = Calendar.getInstance();
                        end.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        end.set(Calendar.MINUTE, minute);

                        String minute_str = "";
                        String hour_str = "";
                        if (minute < 10) {
                            minute_str = "0" + minute;
                        } else {
                            minute_str = minute+"";
                        }
                        if (hourOfDay < 10) {
                            hour_str = "0" + hourOfDay;
                        } else {
                            hour_str = "" + hourOfDay;
                        }

                        endtTimetxt.setText(hour_str + ": " + minute_str);
                    }
                }, eHour, eMinute, false);
        timePickerDialog.show();
    }

    private void setAlarm(){

        sharedpref = this.getSharedPreferences("sharedpref",MODE_PRIVATE);
        int h = 0,m =0;
        sharedpref.getInt("startHour", h);
        sharedpref.getInt("startMin", m);

        //user alarm amanger to active auto reply
        Intent intent = new Intent(AutoSetActivity.this, SmsReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, intent, 0);
        alarmManager = (AlarmManager)this.getApplicationContext().getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), pendingIntent);
    }

    //cancel the auto reply helper method
    private void cancelAlarm (PendingIntent pendingIntent){
        alarmManager.cancel(pendingIntent);
        Toast.makeText(AutoSetActivity.this, "Auto reply off ", Toast.LENGTH_SHORT).show();

    }

    //validation of user input
    private boolean validation() {
        smsMessage = mesgEditText.getText().toString();
        startTime = startTimetxt.getText().toString();
        endTime = endtTimetxt.getText().toString();

        if (smsMessage.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            showDialog();
            return false;
        }

        if (mon.isChecked() || tues.isChecked() || wed.isChecked() || thur.isChecked() || fri.isChecked() || sat.isChecked() || sun.isChecked()) {
            return true;
        }

        return false;
    }

    // set  button listener to save all the data user input
    public void SetAutobuttonclicked(View view) {
        if (validation()){
            //use share preference to store data
            sharedpreferences  = getApplicationContext().getSharedPreferences("saveSMS", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            //get editsms from user input
            smsMessage = mesgEditText.getText().toString();
            editor.putString("smsMessage", smsMessage);
            editor.commit();
            Toast.makeText(AutoSetActivity.this, "Auto Response has been set", Toast.LENGTH_SHORT).show();
        }

    }

    //show the message error
    public void showDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(AutoSetActivity.this).create();
        alertDialog.setTitle("Missing field");
        alertDialog.setMessage("Please enter all fields");
        // Alert dialog button
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Alert dialog action goes here
                        dialog.dismiss();// use dismiss to cancel alert dialog
                    }
                });
        alertDialog.show();
    }

    //cancel button listener
    public void Cancelbuttonclicked (View view){
        //cancel alarm may go here
        Intent backintent = new Intent (AutoSetActivity.this, MenuActivity.class);
        startActivity(backintent);
    }
}
