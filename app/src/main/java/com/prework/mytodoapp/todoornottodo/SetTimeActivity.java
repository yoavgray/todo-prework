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
        boolean validTime = true;
        Intent i = getIntent();
        int day, month, year, hour, minute;
        day = dp.getDayOfMonth();
        month = dp.getMonth();
        year = dp.getYear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            minute = tp.getMinute();
            hour = tp.getHour();
        } else {
            minute = tp.getCurrentMinute();
            hour = tp.getCurrentHour();
        }
        switch (v.getId()) {
            case (R.id.buttonOkDialog):
                if (timeIsValid(year, month, day, hour, minute)) {
                    i.putExtra("day", day);
                    i.putExtra("month", month);
                    i.putExtra("year", year);
                    i.putExtra("hour", hour);
                    i.putExtra("minute", minute);
                    setResult(RESULT_OK, i);
                } else {
                    Toast.makeText(SetTimeActivity.this, "Past date/time is invalid", Toast.LENGTH_SHORT).show();
                    validTime = false;
                }
                break;

            case (R.id.buttonCancelDialog):
                setResult(RESULT_CANCELED, i);
                break;
        }

        if (validTime == true) {
            finish();
        }
    }

    private boolean timeIsValid(int chosenYear, int chosenMonth, int chosenDay, int chosenHour, int chosenMinute) {
        boolean retValue = true;
        int currentHour, currentMinute, currentDay, currentMonth, currentYear;
        long delayInMinutes;
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH);
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        currentHour = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        currentMinute = calendar.get(Calendar.MINUTE);     // gets month number, NOTE this is zero based!

        if (chosenYear > currentYear) {
              retValue = true;
        } else if (chosenYear == currentYear) {
            if (chosenMonth > currentMonth) {
                retValue = true;
            } else if (chosenMonth == currentMonth) {
                if (chosenDay > currentDay) {
                    retValue = true;
                } else if (chosenDay == currentDay) {
                    if (chosenHour > currentHour) {
                        retValue = true;
                    } else if (chosenHour == currentHour) {
                        if (chosenMinute >= currentMinute) {
                            retValue = true;
                        } else {
                            retValue = false;
                        }
                    } else {
                        retValue = false;
                    }
                } else {
                    retValue = false;
                }
            } else {
                retValue = false;
            }
        } else {
            retValue = false;
        }

        if (retValue) {
            delayInMinutes = (chosenMonth - currentMonth) * 43829 + (chosenDay - currentDay) * 1440 + ((chosenHour - currentHour) * 60) + (chosenMinute - currentMinute);
        }

        return retValue;
    }
}
