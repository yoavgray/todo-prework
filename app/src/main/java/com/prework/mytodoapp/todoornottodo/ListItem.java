package com.prework.mytodoapp.todoornottodo;

import android.widget.CheckBox;
import android.widget.TextView;

public class ListItem {

    private String text;
    private boolean isChecked;

    public ListItem(String text, boolean isChecked) {
        this.text = text;
        this.isChecked = isChecked;
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

    @Override
    public String toString() {
        return "ListItem{" +
                "text='" + text + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}