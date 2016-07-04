package com.prework.mytodoapp.todoornottodo;

import android.widget.CheckBox;
import android.widget.TextView;

public class ListItem {

    private String header;
    private boolean isChecked;

    public ListItem(String header, boolean isChecked) {
        this.header = header;
        this.isChecked = isChecked;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setCheckBox(boolean check) {
        this.isChecked = check;
    }
}