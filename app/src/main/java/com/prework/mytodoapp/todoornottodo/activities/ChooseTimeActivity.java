package com.prework.mytodoapp.todoornottodo.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TimePicker;

import com.prework.mytodoapp.todoornottodo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseTimeActivity extends AppCompatActivity {
    @BindView(R.id.timePicker) TimePicker tp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_time_picker);
        ButterKnife.bind(this);

        tp.setIs24HourView(true);
    }

    @OnClick(R.id.btSaveTime)
    public void saveTime() {
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
