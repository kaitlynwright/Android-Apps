package com.example.kaitlynwright.to_dolist;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tasks.sqlite";
    private static final int DATABASE_VERSION = 1;

    public static final String TASK_TABLE = "Tasks";
    public static final String LT_TASK_TABLE = "Long Term Tasks";
    public static final String TASK_NAME = "Name";
    public static final String DUE_DATE = "DueDate";
    public static final String DAY = "Day";
    public static final String MONTH = "Month";
    public static final String YEAR = "Year";
    public static final String NOTE = "Note";
    public static final String COMPLETE = "Complete";
    public static final String LONG_TERM = "LongTerm";

    private Context context = null;

    public SQLiteHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = c;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TASK_TABLE
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_NAME + " TEXT,"
                + DUE_DATE + " TEXT,"
                + DAY + " INTEGER,"
                + MONTH + " INTEGER,"
                + YEAR + " INTEGER,"
                + NOTE + " TEXT,"
                + COMPLETE + " TEXT,"
                + LONG_TERM + " INTEGER NOT NULL"
                + ")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // upgrade databse
    }

    // Delete task from database
    public void deleteTask(Cursor c) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = c.getLong(0);
        db.execSQL("DELETE FROM " + TASK_TABLE + " WHERE _id=" + id);

        db.close();
    }

    // Add task to database
    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TASK_NAME, task.getName());
        cv.put(DUE_DATE, task.getDueDate());
        cv.put(NOTE, task.getNote());

        cv.put(MONTH, String.valueOf(task.getDueDate().substring(0,1)));
        cv.put(DAY, String.valueOf(task.getDueDate().substring(3,4)));
        cv.put(YEAR, String.valueOf(task.getDueDate().substring(6)));

        if (task.getComplete()) {
            cv.put(COMPLETE, "Complete");
        } else {
            cv.put(COMPLETE, "Incomplete");
        }

        if (task.getLongTerm()) {
            cv.put(LONG_TERM, 1);
        } else {
            cv.put(LONG_TERM, 0);
        }

        db.insert(TASK_TABLE, null, cv);
        db.close();
    }

    // Retrieve all tasks, return cursor for adapter
    public Cursor fetchAllTasksCursor() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TASK_TABLE
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_NAME + " TEXT,"
                + DUE_DATE + " TEXT,"
                + DAY + " INTEGER,"
                + MONTH + " INTEGER,"
                + YEAR + " INTEGER,"
                + NOTE + " TEXT,"
                + COMPLETE + " TEXT,"
                + LONG_TERM + " INTEGER NOT NULL"
                + ")" );
        return db.query(TASK_TABLE,null, null, null,
                null, null, null);
    }


    // Retrieve all tasks from database, put in list
    public TaskItems fetchAllTasks() {
        TaskItems tasks = new TaskItems();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = fetchAllTasksCursor();

        c.moveToFirst();
        while(!c.isAfterLast()) {
            Task task = new Task();
            task.setName(c.getString(c.getColumnIndex(TASK_NAME)));
            task.setDueDate(c.getString(c.getColumnIndex(DUE_DATE)));
            task.setNote(c.getString(c.getColumnIndex(NOTE)));

            if (c.getString(c.getColumnIndex(COMPLETE)) == "Complete") {
                task.setComplete(true);
            } else task.setComplete(false);

            if (c.getInt(c.getColumnIndex(LONG_TERM)) == 1) {
                task.setLongTerm(true);
            } else task.setLongTerm(false);

            tasks.add(task);
            c.moveToNext();
        }
        db.close();
        c.close();
        return tasks;
    }

    // Preference Sorting
    public void sortBy(String condition) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS ordered_table");
        db.execSQL("CREATE TABLE ordered_table "
                + "( _id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + TASK_NAME + " TEXT,"
                + DUE_DATE + " TEXT,"
                + MONTH + " INTEGER,"
                + DAY + " INTEGER,"
                + YEAR + " INTEGER,"
                + NOTE + " TEXT,"
                + COMPLETE + " TEXT,"
                + LONG_TERM + " INTEGER NOT NULL"
                + ")");

        switch(condition) {
            case "Alphabetical":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                ", " + DUE_DATE + ", "+ MONTH + ", " + DAY + ", "
                                + YEAR + ", "+ NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                                ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                                ", "+ MONTH + ", " + DAY + ", "
                                + YEAR + ", " + NOTE + ", " + COMPLETE + ", "
                                + LONG_TERM + " FROM " + TASK_TABLE +
                                " ORDER BY " + TASK_NAME
                );
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;

            case "Ascending Date":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                        ", " + DUE_DATE + ", " + MONTH + ", " + DAY + ", "
                        + YEAR + ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                        ", " + MONTH + ", " + DAY + ", " + YEAR +
                        ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        " FROM " + TASK_TABLE + " ORDER BY " + YEAR + ", " + MONTH +
                        ", " + DAY
                );
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;

            case "Descending Date":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                        ", " + DUE_DATE + ", " + MONTH + ", " + DAY + ", "
                        + YEAR +", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                        ", " + DAY + ", " + MONTH + ", " + YEAR +
                        ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        " FROM " + TASK_TABLE + " ORDER BY " + YEAR + " DESC, " + MONTH +
                        " DESC, " + DAY +" DESC"
                );
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;

            case "Complete - First":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                        ", " + DUE_DATE + ", " + MONTH + ", " + DAY + ", "
                        + YEAR +", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                        ", " + DAY + ", " + MONTH + ", " + YEAR +
                        ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        " FROM " + TASK_TABLE + " ORDER BY " + COMPLETE);
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;

            case "Complete - Last":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                        ", " + DUE_DATE + ", " + MONTH + ", " + DAY + ", "
                        + YEAR +", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                        ", " + DAY + ", " + MONTH + ", " + YEAR +
                        ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        " FROM " + TASK_TABLE + " ORDER BY " + COMPLETE + " DESC");
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;

            case "Long Term - First":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                        ", " + DUE_DATE + ", " + MONTH + ", " + DAY + ", "
                        + YEAR +", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                        ", " + DAY + ", " + MONTH + ", " + YEAR +
                        ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        " FROM " + TASK_TABLE + " ORDER BY " + LONG_TERM + " DESC" );
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;

            case "Long Term - Last":
                db.execSQL("INSERT INTO ordered_table (" + TASK_NAME +
                        ", " + DUE_DATE + ", " + MONTH + ", " + DAY + ", "
                        + YEAR +", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        ") SELECT " + TASK_NAME +  ", " + DUE_DATE +
                        ", " + DAY + ", " + MONTH + ", " + YEAR +
                        ", " + NOTE + ", " + COMPLETE + ", " + LONG_TERM +
                        " FROM " + TASK_TABLE + " ORDER BY " + LONG_TERM);
                db.execSQL("DROP TABLE " + TASK_TABLE);
                db.execSQL("ALTER TABLE ordered_table RENAME TO " + TASK_TABLE);
                break;
        }
    }

    // Clear table
    public void clearTable(Cursor c) {
        SQLiteDatabase db = this.getWritableDatabase();
        c.moveToFirst();
        while ((!c.isAfterLast())) {
            this.deleteTask(c);
            c.moveToNext();
        }
        db.close();
    }


}