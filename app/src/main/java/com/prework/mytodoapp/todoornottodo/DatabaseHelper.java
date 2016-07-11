package com.prework.mytodoapp.todoornottodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoav on 08/07/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION        = 1;
    public static final String DATABASE_NAME        = "todo_list.db";
    public static final String TABLE_NAME           = "todo_table";
    public static final String COLUMN_ID            = "_id";
    public static final String COLUMN_PRIORITY      = "priority";
    public static final String COLUMN_TASK          = "task";
    public static final String COLUMN_IS_CHECKED    = "is_checked";
    public static final String COLUMN_TIME          = "time";
    public static final String COLUMN_DATE          = "date";

    // Database creation sql statement
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + " (" + COLUMN_ID + INT_TYPE + " PRIMARY KEY" + COMMA_SEP
            + COLUMN_PRIORITY + INT_TYPE + COMMA_SEP
            + COLUMN_TASK + TEXT_TYPE + COMMA_SEP
            + COLUMN_IS_CHECKED + INT_TYPE + COMMA_SEP
            + COLUMN_TIME + TEXT_TYPE + COMMA_SEP
            + COLUMN_DATE + TEXT_TYPE + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
