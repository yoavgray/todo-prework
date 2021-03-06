package com.prework.mytodoapp.todoornottodo.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.prework.mytodoapp.todoornottodo.adapters.ListItem;

import java.util.List;

public class ListItemDataSource {
    private static final int UPDATE_PRIORITY = 0;
    private static final int UPDATE_TASK = 1;
    private static final int UPDATE_IS_CHECKED = 2;
    private static final int UPDATE_TIME_AND_DATE = 3;

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private String[] allColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_PRIORITY, DatabaseHelper.COLUMN_TASK,
            DatabaseHelper.COLUMN_IS_CHECKED, DatabaseHelper.COLUMN_TIME,
            DatabaseHelper.COLUMN_DATE };

    public ListItemDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean isOpen() {
        return database.isOpen();
    }

    public boolean addListItem(int taskId, int priority, String task, boolean isChecked, String time, String date) {
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_ID, taskId);
        contentValues.put(DatabaseHelper.COLUMN_PRIORITY, priority);
        contentValues.put(DatabaseHelper.COLUMN_TASK, task);
        contentValues.put(DatabaseHelper.COLUMN_IS_CHECKED, isChecked ? 1 : 0);
        contentValues.put(DatabaseHelper.COLUMN_TIME, time);
        contentValues.put(DatabaseHelper.COLUMN_DATE, date);
        long result = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public void deleteListItem(ListItem listItem) {
        //If for any reason, the db closed
        if (!this.isOpen()) {
            this.open();
        }
        int id = listItem.getId();
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    //Update an existing db item by a different changing parameter
    public int updateListItem(int changeParam, int id, int priority, String task, boolean isChecked, String time, String date) {
        //If for any reason, the db closed
        if (!this.isOpen()) {
            this.open();
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        switch (changeParam) {
            case UPDATE_PRIORITY:
                contentValues.put(DatabaseHelper.COLUMN_PRIORITY, priority);
                break;
            case UPDATE_TASK:
                contentValues.put(DatabaseHelper.COLUMN_TASK, task);
                break;
            case UPDATE_IS_CHECKED:
                contentValues.put(DatabaseHelper.COLUMN_IS_CHECKED, isChecked);
                break;
            case UPDATE_TIME_AND_DATE:
                contentValues.put(DatabaseHelper.COLUMN_TIME, time);
                contentValues.put(DatabaseHelper.COLUMN_DATE, date);
                break;
        }

        return db.update(DatabaseHelper.TABLE_NAME, contentValues,DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    //I added a List as an argument because it was a mistake to instantiate
    //it here.
    public List<ListItem> getAllItems(List<ListItem> items) {

        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ListItem listItem = cursorToItem(cursor);
            items.add(listItem);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return items;
    }

    private ListItem cursorToItem(Cursor cursor) {
        int id = cursor.getInt(0);
        int priority = cursor.getInt(1);
        String text = cursor.getString(2);
        boolean isChecked = (cursor.getInt(3) != 0);
        String time = cursor.getString(4);
        String date = cursor.getString(5);
        return new ListItem(id, priority, text, isChecked, time, date);
    }
}
