package com.prework.mytodoapp.todoornottodo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

public class ChooseTimeActivity extends AppCompatActivity implements View.OnClickListener {
    TimePicker tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_picker_layout);

        tp = (TimePicker) findViewById(R.id.timePicker);
        tp.setIs24HourView(true);

        Button buttonSaveTime = (Button) findViewById(R.id.btSaveTime);
        buttonSaveTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //Just to make sure it's nothing about the TimePicker
        if (view.getId() == R.id.btSaveTime) {
            Intent i = getIntent();
            int hour, minute;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                hour = tp.getHour();
                minute = tp.getMinute();
            } else {
                hour = tp.getCurrentHour();
                minute = tp.getCurrentMinute();
            }
            i.putExtra("hour", hour);
            i.putExtra("minute", minute);
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
