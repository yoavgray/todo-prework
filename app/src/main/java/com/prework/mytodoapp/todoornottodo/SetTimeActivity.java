package com.prework.mytodoapp.todoornottodo;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class SetTimeActivity extends AppCompatActivity implements View.OnClickListener {
    TimePicker tp;
    DatePicker dp;
    Button ok, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_picker_dialog);

        tp = (TimePicker)findViewById(R.id.timePicker);
        dp = (DatePicker)findViewById(R.id.datePicker);
        ok = (Button)findViewById(R.id.buttonOkDialog);
        cancel = (Button)findViewById(R.id.buttonCancelDialog);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case (R.id.buttonOkDialog):
                i.putExtra("day", dp.getDayOfMonth());
                i.putExtra("month", dp.getMonth() + 1);
                i.putExtra("year", dp.getYear());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    i.putExtra("hour", tp.getHour());
                    i.putExtra("minute", tp.getMinute());
                } else {
                    i.putExtra("hour", tp.getCurrentHour());
                    i.putExtra("minute", tp.getCurrentMinute());
                }
                setResult(RESULT_OK, i);
                break;

            case (R.id.buttonCancelDialog):
                setResult(RESULT_CANCELED, i);
                break;
        }
        finish();
    }
}
