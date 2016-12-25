package com.salmansiddiqui.reminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ReminderDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ReminderTasks.db";
    private static final int DATABASE_VERSION = 1;
    public static final String REMINDER_TABLE_NAME = "reminder";
    public static final String REMINDER_COLUMN_ID = "timeOfCreation"; // unix time of task creation
    public static final String REMINDER_COLUMN_TASK = "task";
    public static final String REMINDER_COLUMN_COMPLETE = "T"; // bool: T or F
    public static final String REMINDER_COLUMN_REMIND = "T"; // bool: T or F
    public static final String REMINDER_COLUMN_TIME = "timeOfReminder"; // unix time of task reminder

    public ReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + REMINDER_TABLE_NAME + "(" +
                REMINDER_COLUMN_ID + " TEXT, " +
                REMINDER_COLUMN_TASK + " TEXT, " +
                REMINDER_COLUMN_COMPLETE + " TEXT, " +
                REMINDER_COLUMN_REMIND + " TEXT, " +
                REMINDER_COLUMN_TIME + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public boolean insertReminder(String task, String complete, String remind, String timeOfReminder) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_COLUMN_ID, "strftime('%s','now')");
        contentValues.put(REMINDER_COLUMN_TASK, task);
        contentValues.put(REMINDER_COLUMN_COMPLETE, complete);
        contentValues.put(REMINDER_COLUMN_REMIND, remind);
        contentValues.put(REMINDER_COLUMN_TIME, timeOfReminder);
        db.insert(REMINDER_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateReminder(String id, String task, String complete, String remind,
                                  String timeOfReminder) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REMINDER_COLUMN_TASK, task);
        contentValues.put(REMINDER_COLUMN_COMPLETE, complete);
        contentValues.put(REMINDER_COLUMN_REMIND, remind);
        contentValues.put(REMINDER_COLUMN_TIME, timeOfReminder);
        db.update(REMINDER_TABLE_NAME, contentValues, REMINDER_COLUMN_ID + " = ? ",
                new String[] { id } );
        return true;
    }

    public Cursor getReminder(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + REMINDER_TABLE_NAME + " WHERE " +
                REMINDER_COLUMN_ID + "=?", new String[] { id } );
        return res;
    }

    public Cursor getAllReminders() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + REMINDER_TABLE_NAME, null );
        return res;
    }

    public Integer deleteReminder(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(REMINDER_TABLE_NAME, REMINDER_COLUMN_ID + " = ? ",
                new String[] { id });
    }
}
