package com.prework.mytodoapp.todoornottodo.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.DatePicker;

import com.prework.mytodoapp.todoornottodo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseDateActivity extends AppCompatActivity {
    @BindView(R.id.datePicker) DatePicker dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_date_picker);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btSaveDate)
    protected void saveDate() {
        Intent i = getIntent();
        int year = dp.getYear();
        int month = dp.getMonth();
        int day = dp.getDayOfMonth();
        i.putExtra("year", year);
        i.putExtra("month", month + 1);
        i.putExtra("day", day);
        setResult(RESULT_OK, i);
        finish();
    }
}
