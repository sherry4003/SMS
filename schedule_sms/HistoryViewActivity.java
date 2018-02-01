package com.example.drago.schedule_sms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private ListView mylistView;
    private ContactListAdapter myAdapter;
    private List<Contact> contactSMSList;

    static String finalMessage;
    static String finalPhoneNumber;
    AlarmManager manager;
    PendingIntent pendingIntent;
    Intent myIntent;

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private Context context;


    ArrayList<PendingIntent> intentArray;
    ArrayList<AlarmManager> alarmArray;
    static int modI;
    int modModI; //broadcast

    String name, sms, date, time;
    Contact contact;
    SQLiteDatabase database;
    static HistoryViewActivity hActivity = null;
    public HistoryViewActivity(){
        //database = new SQLiteDB(this).getReadableDatabase();
    }
    public static HistoryViewActivity getInstance(){
        return hActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);
        hActivity = this;

        //time to add my part
        mylistView = (ListView) findViewById(R.id.mylist);
        contactSMSList = new ArrayList<>();
        intentArray = new ArrayList<PendingIntent>(); //arraylist of pendingIntents
        alarmArray = new ArrayList<AlarmManager>(); //arraylist of Managers

        readFromDB();

        modI = 0;
        modModI = 0;


        this.context = this;
        Intent alarm = new Intent(this.context, AlarmTextToastReciever.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(this.context, 0, alarm, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 60000, pendingIntent);
        }

        //set adapter
        myAdapter  = new ContactListAdapter(getApplicationContext(), contactSMSList);
        mylistView.setAdapter(myAdapter);

        mylistView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"clicked "+ view.getTag(), Toast.LENGTH_SHORT).show();
            }
        });

        mylistView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                                  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                                                      Toast.makeText(getApplicationContext(), "Removing " + view.getTag(), Toast.LENGTH_SHORT).show();
                                                      //I removed removing for now
                                                      //modI--;
                                                      //alarmArray.get((Integer) view.getTag()).cancel(intentArray.get((Integer) view.getTag()));
                                                      //alarmArray.remove(view.getTag());
                                                      //intentArray.remove(view.getTag());

                                                      contactSMSList.remove(position);
                                                      myAdapter.notifyDataSetChanged();

                                                      return true;
                                                  }
                                              }
        );

    }
    public List<Contact> readFromDB() {
        database = new SQLiteDB(this).getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + SQLiteDB.PERSON_TABLE_NAME, null);
        Contact contact;
        if (cursor.moveToFirst()) {
            do{
                String name = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_NAME));
                String date = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_DATE));
                String time = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_TIME));
                String sms = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_MESSAGE));
                String number = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_NUMBER));
                String year = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_YEAR));
                String month = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_MONTH));
                String day = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_DAY));
                String hour = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_HOUR));
                String min = cursor.getString(cursor.getColumnIndex(SQLiteDB.PERSON_COLUMN_MIN));

                contact = new Contact(name, sms, date, time);
                contact.setPhoneNum(number);
                contact.setDate(date);
                contact.setTime(time);
                contact.setName(name);
                contact.setMessage(sms);
                contact.setDetails(Integer.parseInt(min), Integer.parseInt(hour), Integer.parseInt(day),
                        Integer.parseInt(month), Integer.parseInt(year), number);
                contactSMSList.add(contact);
            }while(cursor.moveToNext());
        }
        Log.d("TAG", "The total cursor count is " + cursor.getCount());
        mylistView.setAdapter(new ContactListAdapter(getApplicationContext(), contactSMSList));
        cursor.close();
        return contactSMSList;
    }
    public List<Contact> getContactSMSList(){
        return contactSMSList;
    }
    public void remove(String[] names){
        database = new SQLiteDB(this).getWritableDatabase();
        database.delete(SQLiteDB.PERSON_TABLE_NAME, "number=?", names);
    }
}