package com.example.kaitlynwright.tidetable;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

class TideSQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "tides.sqlite";
    private static final int DB_VERSION = 1;

    /*// list table
    public static final String TIDE_LOCATIONS = "locations_list";
    public static final String TIDE_ID = "_id";
    public static final String LIST_NAME = "list_name";*/

    // tide table
    public static final String TIDE = "Tides";
    public static final String TIDE_LIST_ID = "list_id";
    public static final String TIDE_LOCATION = "location";

    public static final String DATE = "date";
    public static final String DAY = "day";
    public static final String TIME = "time";
    public static final String PREDFT = "pred_in_ft";
    public static final String PREDCM = "pred_in_cm";
    public static final String HIGHLOW = "highlow";

    private Context context = null;

    public TideSQLiteHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
        this.context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*db.execSQL("CREATE TABLE " + TIDE_LOCATIONS + " (" +
                TIDE_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LIST_NAME + " TEXT    NOT NULL UNIQUE);");*/

        db.execSQL("CREATE TABLE " + TIDE
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TIDE_LOCATION + " TEXT, "
                + DATE + " TEXT,"
                + DAY  + " TEXT,"
                + TIME + " TEXT,"
                + PREDFT + " TEXT,"
                + PREDCM + " TEXT,"
                + HIGHLOW + " TEXT"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion ) {
        // TO-DO
    }
}
