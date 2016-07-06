package com.prework.mytodoapp.todoornottodo;

import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Comparator;

public class ListItem {

    private String text;
    private boolean isChecked;
    private int year, month, day, hour, minute, priority;

    public ListItem(String text, boolean isChecked, int year, int month, int day, int hour, int minute, int priority) {
        this.text = text;
        this.isChecked = isChecked;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.priority = priority;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        try {
            j.put("text",text);
            j.put("isChecked",isChecked);
            j.put("year",year);
            j.put("month",month);
            j.put("day",day);
            j.put("hour",hour);
            j.put("minute",minute);
            j.put("priority",priority);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j.toString();
    }
}