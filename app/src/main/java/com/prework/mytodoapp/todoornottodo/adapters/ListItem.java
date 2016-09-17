package com.prework.mytodoapp.todoornottodo.adapters;

import org.json.JSONException;
import org.json.JSONObject;

public class ListItem {
    private boolean isShown;
    private int id;
    private int priority;
    private String text;
    private boolean isChecked;
    private String time, date;

    public ListItem(int id, int priority, String text, boolean isChecked, String time, String date) {
        this.isShown = false;
        this.id = id;
        this.priority = priority;
        this.text = text;
        this.isChecked = isChecked;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    @Override
    public String toString() {
        JSONObject j = new JSONObject();
        try {
            j.put("id",id);
            j.put("priority",priority);
            j.put("text",text);
            j.put("isChecked",isChecked);
            j.put("time",time);
            j.put("date",date);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return j.toString();
    }
}