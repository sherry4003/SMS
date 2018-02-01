package com.example.drago.schedule_sms;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ScheduleSetActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    String [] items;
    ArrayList<String> listItems;
    ArrayAdapter <String> adapter;
    ListView listView;
    Button selectDatebtn, selectTimebtn;
    EditText searchtxt, messagetxt;

    String number, name, message, time, date;
    String finalNumber, finalName, finalMessage;
    int finalHour, finalMinute, finalDay, finalMonth, finalYear;

    private static  final String TAG = "ScheduleSetActivity";
    private EditText datetxt, timetxt;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TimePickerDialog.OnTimeSetListener timeSetListener;
    private static final int REQUEST_CODE = 1;

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_set);
        //    listView = (ListView) findViewById(R.id.listview);
        database = new SQLiteDB(this).getWritableDatabase();


        //defined button, textview, EditText
        selectDatebtn = (Button) findViewById(R.id.selectDatebtn);
        selectTimebtn = (Button) findViewById(R.id.selectTimebtn);
        datetxt = (EditText) findViewById(R.id.datetxt);
        timetxt = (EditText) findViewById(R.id.timetxt);
        searchtxt = (EditText) findViewById(R.id.searchtxt);
        messagetxt = (EditText) findViewById(R.id.mesgEditText);
        //saveToDB("name", "date", "time", "message", "2017", "12", "17", "9", "10", "+15712349156");
        //saveToDB("name", "date", "time", "message", 2017, 12, 17, 9, 10, "+15712349156");
        //setup the Date select listener
        selectDatebtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ScheduleSetActivity.this,
                        android.R.style.Theme_Holo_Dialog_MinWidth,
                        dateSetListener,year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+ 1; //;D
                Log.d(TAG, "DateSet: mm/dd/yy: "+ month +"/"+ dayOfMonth + "/"+year);
                finalMonth = month;//might need to change this
                finalDay =dayOfMonth;
                finalYear = year;
                String date =  month +"/"+ dayOfMonth + "/"+year;
                datetxt.setText(date);
            }
        };
    }

    // Add contact button, access to user contact list
    public void AddContactButtonClicked (View view){
        //asscess teh contact list
        Uri uri = Uri.parse("content://contacts");
        Intent intent = new Intent(Intent.ACTION_PICK, uri);
        intent.setType(Phone.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_CODE);
    }

    //set time picker button
    public void SelecttTimeButtonClicked (View view){
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(
                ScheduleSetActivity.this,
                new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timetxt.setText(hourOfDay+": "+minute);
                        finalHour = hourOfDay;
                        finalMinute = minute;
                    }
                }, hour, minute,false);
        timePickerDialog.show();
    }

    // cancel activity  button , go back to the main menu
    public void Cancelbuttonclicked (View view){
        Intent backintent = new Intent (ScheduleSetActivity.this, MenuActivity.class);
        startActivity(backintent);
    }

    //intend back result after access user's contact list
    protected void onActivityResult(int requestCode, int resultCode, Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Uri uri = i.getData();
                String[] contactData = { Phone.NUMBER, Phone.DISPLAY_NAME };

                Cursor cursor = getContentResolver().query(uri, contactData,
                        null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(Phone.NUMBER);
                int nameColumIndex = cursor.getColumnIndex(Phone.DISPLAY_NAME);
                number = cursor.getString(numberColumnIndex);
                name = cursor.getString(nameColumIndex);
                searchtxt.setText(name);
            }
        }
    }

    public void SetButtonClicked (View view){

        //get infor from user input
        time = timetxt.getText().toString();
        date = datetxt.getText().toString();
        message = messagetxt.getText().toString();

        Intent scheduleIntent = new Intent (ScheduleSetActivity.this, HistoryViewActivity.class);
        //pass datas to HistoryViewActivity class
        //scheduleIntent.putExtra("name", name);
        //scheduleIntent.putExtra("number", number);
        //scheduleIntent.putExtra("message", message);
        //scheduleIntent.putExtra("time", time);
        //scheduleIntent.putExtra("date", date);

        scheduleIntent.putExtra("name", name); //1
        scheduleIntent.putExtra("number", number); //2
        scheduleIntent.putExtra("message", message); //3
        scheduleIntent.putExtra("hour", finalHour); //4
        scheduleIntent.putExtra("minute", finalMinute); //5
        scheduleIntent.putExtra("month", finalMonth); //6
        scheduleIntent.putExtra("day", finalDay); //7
        scheduleIntent.putExtra("year", finalYear); //8

        scheduleIntent.putExtra("time", time);
        scheduleIntent.putExtra("date", date);
        saveToDB(name, date, time, message, ""+finalYear, ""+finalMonth, ""+finalDay, ""+finalHour, ""+finalMinute, number);
        sendSMSMessage();
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);

        startActivity(scheduleIntent);
    }
    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }
    private void saveToDB(String name, String date, String time, String message, String year, String month, String day, String hour, String min, String number) {
        SQLiteDatabase database = new SQLiteDB(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SQLiteDB.PERSON_COLUMN_NAME, name);
        values.put(SQLiteDB.PERSON_COLUMN_DATE, date);
        values.put(SQLiteDB.PERSON_COLUMN_TIME, time);
        values.put(SQLiteDB.PERSON_COLUMN_MESSAGE, message);
        values.put(SQLiteDB.PERSON_COLUMN_NUMBER, number);
        values.put(SQLiteDB.PERSON_COLUMN_YEAR, year);
        values.put(SQLiteDB.PERSON_COLUMN_MONTH, month);
        values.put(SQLiteDB.PERSON_COLUMN_DAY, day);
        values.put(SQLiteDB.PERSON_COLUMN_HOUR, hour);
        values.put(SQLiteDB.PERSON_COLUMN_MIN, min);

        long newRowId = database.insert(SQLiteDB.PERSON_TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }
    public void remove(String[] names){
        database = new SQLiteDB(this).getWritableDatabase();
        database.delete(SQLiteDB.PERSON_TABLE_NAME, "name=?", names);
    }

}