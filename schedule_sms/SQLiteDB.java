package com.example.drago.schedule_sms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by  on 12/17/2017.
 */

public class SQLiteDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 9;
    public static final String DATABASE_NAME = "text_message";
    public static final String PERSON_TABLE_NAME = "person";
    public static final String PERSON_COLUMN_ID = "_id";
    public static final String PERSON_COLUMN_NAME = "name";
    public static final String PERSON_COLUMN_DATE = "date";
    public static final String PERSON_COLUMN_TIME = "time";
    public static final String PERSON_COLUMN_MESSAGE= "message";
    public static final String PERSON_COLUMN_NUMBER = "number";
    public static final String PERSON_COLUMN_YEAR = "year";
    public static final String PERSON_COLUMN_MONTH = "month";
    public static final String PERSON_COLUMN_DAY = "day";
    public static final String PERSON_COLUMN_HOUR= "hour";
    public static final String PERSON_COLUMN_MIN = "min";



    public SQLiteDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + PERSON_TABLE_NAME + " (" +
                PERSON_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PERSON_COLUMN_NAME + " TEXT, " +
                PERSON_COLUMN_DATE + " TEXT, " +
                PERSON_COLUMN_TIME+ " TEXT, " +
                PERSON_COLUMN_MESSAGE + " TEXT," +
                PERSON_COLUMN_NUMBER + " TEXT," +
                PERSON_COLUMN_YEAR + " TEXT," +
                PERSON_COLUMN_MONTH + " TEXT," +
                PERSON_COLUMN_DAY + " TEXT," +
                PERSON_COLUMN_HOUR + " TEXT," +
                PERSON_COLUMN_MIN + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PERSON_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}