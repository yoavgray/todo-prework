package com.prework.mytodoapp.todoornottodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

public class ChooseDateActivity extends AppCompatActivity implements View.OnClickListener {
    DatePicker dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_picker_layout);

        dp = (DatePicker) findViewById(R.id.datePicker);

        Button buttonSaveDate = (Button) findViewById(R.id.btSaveDate);
        buttonSaveDate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Just to make sure it's nothing about the TimePicker
        if (view.getId() == R.id.btSaveDate) {
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
}
