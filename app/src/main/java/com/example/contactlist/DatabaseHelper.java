package com.example.contactlist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mycontacts.db";
    private static final int DATABASE_VERSION = 2;
    public static final String CONTACT_TABLE = "CONTACT_TABLE";
    public static final String COLUMN_CONTACT_ID = "COLUMN_CONTACTID";
    public static final String COLUMN_CONTACT_NAME = "COLUMN_CONTACT_NAME";
    public static final String COLUMN_CONTACT_ADDRESS = "COLUMN_CONTACT_ADDRESS";
    public static final String COLUMN_CONTACT_CITY= "COLUMN_CONTACT_CITY";
    public static final String COLUMN_CONTACT_STATE = "COLUMN_CONTACT_STATE";
    public static final String COLUMN_CONTACT_ZIPCODE = "COLUMN_CONTACT_ZIPCODE";
    public static final String COLUMN_CONTACT_PHONENUMBER = "COLUMN_CONTACT_PHONENUMBER";
    public static final String COLUMN_CONTACT_CELLNUMBER = "COLUMN_CONTACT_CELLNUMBER";
    public static final String COLUMN_CONTACT_EMAIL = "COLUMN_CONTACT_EMAIL";
    public static final String COLUMN_CONTACT_BIRTHDAY = "COLUMN_CONTACT_BIRTHDAY";
    public static final String COLUMN_CONTACT_CONTACTPHOTO = "COLUMN_CONTACT_CONTACTPHOTO";
    private static final String CREATE_TABLE = "CREATE TABLE "
            + CONTACT_TABLE +
            " (" + COLUMN_CONTACT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CONTACT_NAME + " TEXT, "
            + COLUMN_CONTACT_ADDRESS + " TEXT, "
            + COLUMN_CONTACT_CITY + " TEXT, "
            + COLUMN_CONTACT_STATE + " TEXT, "
            + COLUMN_CONTACT_ZIPCODE + " TEXT, "
            + COLUMN_CONTACT_PHONENUMBER + " TEXT, "
            + COLUMN_CONTACT_CELLNUMBER + " TEXT, "
            + COLUMN_CONTACT_EMAIL + " TEXT, "
            + COLUMN_CONTACT_BIRTHDAY + " TEXT, "
            + COLUMN_CONTACT_CONTACTPHOTO + " BLOB )";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        //Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + i + " To" + i1 + ", which will destroy all old data");
        //database.execSQL("DROP TABLE IF EXISTS "+ COLUMN_CONTACT_NAME);
        //onCreate(database);
        try{
            database.execSQL("ALTER TABLE "+CONTACT_TABLE+" ADD COLUMN "+COLUMN_CONTACT_CONTACTPHOTO+" BLOB");
        }catch (Exception e ){

        }
    }


}
