package com.example.oreoluwafaseru.db_helpers;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.oreoluwafaseru.models.CarOwner;

import java.util.ArrayList;

public class DBController extends SQLiteOpenHelper {
    private static final String LOGCAT = "DB";
    private static final String DB_NAME = "venten.db";
    public static final String TABLE_NAME = "owner_info";

    public static final String
            FNAME = "fname",
            EMAIL = "email",
            COUNTRY = "country",
            CMAKE = "car_make",
            COLOR = "color",
            YEAR = "year",
            GENDER = "gender",
            J_TITLE = "job_title",
            BIO = "bio";


    public DBController(Context applicationcontext) {
        super(applicationcontext, DB_NAME, null, 1);  // creating DATABASE
        Log.d(LOGCAT, "Created");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS "+
                TABLE_NAME +
                " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FNAME + " TEXT, "+
                EMAIL + " TEXT, "+
                COUNTRY + " TEXT collate nocase, "+
                CMAKE + " TEXT, "+
                COLOR + " TEXT collate nocase, "+
                YEAR + " TEXT, "+
                GENDER + " TEXT collate nocase, "+
                J_TITLE + " TEXT, "+
                BIO +" TEXT)";
        database.execSQL(query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old,
                          int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        database.execSQL(query);
        onCreate(database);
    }


}