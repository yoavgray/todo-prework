package com.prework.mytodoapp.todoornottodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoav on 09/07/16.
 */
public class ListItemDataSource {
    private static final int UPDATE_PRIORITY = 0;
    private static final int UPDATE_TASK = 1;
    private static final int UPDATE_IS_CHECKED = 2;
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

    public boolean addListItem(long taskId, int priority, String task, boolean isChecked, String time, String date) {
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_ID, taskId);
        contentValues.put(DatabaseHelper.COLUMN_PRIORITY, priority);
        contentValues.put(DatabaseHelper.COLUMN_TASK, task);
        contentValues.put(DatabaseHelper.COLUMN_IS_CHECKED, isChecked ? 1 : 0);
        contentValues.put(DatabaseHelper.COLUMN_TIME, time);
        contentValues.put(DatabaseHelper.COLUMN_DATE, date);
        long result = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public void deleteListItem(ListItem listItem) {
        long id = listItem.getId();
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<ListItem> deleteCompleted() {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_IS_CHECKED
                + " = " + 1, null);
        return getAllItems();
    }

    public int updateListItem(int changeParam, long id, int priority, String task, boolean isChecked) {
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
        }

        return db.update(DatabaseHelper.TABLE_NAME, contentValues,DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public List<ListItem> getAllItems() {
        List<ListItem> items = new ArrayList<>();

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
