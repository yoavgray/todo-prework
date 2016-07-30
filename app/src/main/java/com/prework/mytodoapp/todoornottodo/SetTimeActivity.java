package com.prework.mytodoapp.todoornottodo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SetTimeActivity extends AppCompatActivity implements View.OnClickListener {
    TimePicker tp;
    DatePicker dp;
    Button ok, cancel;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_picker_dialog);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        tp = (TimePicker)findViewById(R.id.timePicker);
        assert tp != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp.setHour(hour);
            tp.setMinute(minute);
        } else {
            tp.setCurrentHour(hour);
            tp.setCurrentMinute(minute);
        }
        tp.setIs24HourView(true);

        dp = (DatePicker)findViewById(R.id.datePicker);
        ok = (Button)findViewById(R.id.buttonOkDialog);
        cancel = (Button)findViewById(R.id.buttonCancelDialog);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        long day, month, year, hour, minute;
        day = dp.getDayOfMonth();
        month = dp.getMonth();
        year = dp.getYear();
        //Have to do this to be safe
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            minute = tp.getMinute();
            hour = tp.getHour();
        } else {
            minute = tp.getCurrentMinute();
            hour = tp.getCurrentHour();
        }

        switch (v.getId()) {
            case (R.id.buttonOkDialog):
                //We're instantiating a Calendar instance to get the desired time in milliseconds
                Calendar timeChosen = Calendar.getInstance();
                timeChosen.set(Calendar.YEAR, (int)year);
                timeChosen.set(Calendar.MONTH, (int)month);
                timeChosen.set(Calendar.DAY_OF_MONTH, (int)day);
                timeChosen.set(Calendar.HOUR_OF_DAY, (int)hour);
                timeChosen.set(Calendar.MINUTE, (int)minute);
                Calendar timeNow = GregorianCalendar.getInstance(); // creates a new calendar instance
                long timeInMs = timeChosen.getTimeInMillis();
                //Checking that user did not choose past time
                if (timeChosen.compareTo(timeNow) >= 0) {
                    i.putExtra("day", (int)day);
                    i.putExtra("month", (int)month);
                    i.putExtra("year", (int)year);
                    i.putExtra("hour", (int)hour);
                    i.putExtra("minute", (int)minute);
                    i.putExtra("timeInMs", timeInMs);
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(SetTimeActivity.this, "Past date/time is invalid", Toast.LENGTH_SHORT).show();
                }
                break;

            case (R.id.buttonCancelDialog):
                setResult(RESULT_CANCELED, i);
                finish();
                break;
        }
    }
}
