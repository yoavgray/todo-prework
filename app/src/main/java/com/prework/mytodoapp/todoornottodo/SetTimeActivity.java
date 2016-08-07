package com.prework.mytodoapp.todoornottodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SetTimeActivity extends AppCompatActivity implements View.OnClickListener {
    final int CHOOSE_TIME_REQUEST_CODE = 0;
    final int CHOOSE_DATE_REQUEST_CODE = 1;

    EditText etTask;
    TextView tvTimeChosen, tvDateChosen;
    Button btOk, btCancel, btSetDate, btSetTime;
    Spinner prioritySpinner;
    int year, month, day, hour, minute, priority, id;
    boolean isTaskNew;
    Intent callingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_picker_dialog);

        callingIntent = getIntent();
        isTaskNew = callingIntent.getBooleanExtra("new", true);

        setupEditText(isTaskNew);
        setupTextViews(isTaskNew);
        setupSpinner(isTaskNew);
        setupButtons();
    }

    private void setupEditText(boolean isTaskNew) {
        etTask = (EditText) findViewById(R.id.etTask);

        if (!isTaskNew) {
            String text = callingIntent.getStringExtra("task");
            etTask.setText(text);
            etTask.setSelection(text.length());
        }

        etTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    etTask.setError(getText(R.string.required));
                    btOk.setEnabled(false);
                } else {
                    etTask.setError(null);
                    btOk.setEnabled(true);
                }
            }
        });
    }

    private void setupSpinner(boolean isTaskNew) {
        prioritySpinner = (Spinner) findViewById(R.id.priority_spinner_edit_task);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        prioritySpinner.setAdapter(adapter);
        if (!isTaskNew) {
            priority = callingIntent.getIntExtra("priority", 0);
            prioritySpinner.setSelection(priority);
        } else {
            prioritySpinner.setSelection(0);
        }
        prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priority = prioritySpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void setupButtons() {
        btOk = (Button) findViewById(R.id.buttonOkDialog);
        if (!etTask.getText().toString().equals("")) {
            btOk.setEnabled(true);
        }
        btCancel = (Button) findViewById(R.id.buttonCancelDialog);
        btOk.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btSetDate = (Button) findViewById(R.id.btSetDate);
        btSetDate.setOnClickListener(this);
        btSetTime = (Button) findViewById(R.id.btSetTime);
        btSetTime.setOnClickListener(this);
    }

    /**
     * Setup all the Views in this Activity
     * @param isTaskNew
     */
    private void setupTextViews(boolean isTaskNew) {
        tvDateChosen = (TextView) findViewById(R.id.tvDateChosen);
        tvTimeChosen = (TextView) findViewById(R.id.tvTimeChosen);

        if (!isTaskNew) {
            String[] dateSplitted = callingIntent.getStringExtra("date").split("/");
            month = Integer.valueOf(dateSplitted[0]);
            day = Integer.valueOf(dateSplitted[1]);
            year = Integer.valueOf(dateSplitted[2]);
            String[] timeSplitted = callingIntent.getStringExtra("time").split(":");
            hour = Integer.valueOf(timeSplitted[0]);
            minute = Integer.valueOf(timeSplitted[1]);
        } else {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        String timeChosen = hour + ":" + ((minute < 10) ? "0" + minute : minute);
        String dateChosen = ((month < 10) ? "0" + month : month) + "/"
                + ((day < 10) ? "0" + day : day) + "/" + year;
        tvDateChosen.setText(dateChosen);
        tvTimeChosen.setText(timeChosen);
    }

    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        Intent intent;

        switch (v.getId()) {
            case (R.id.btSetTime):
                intent = new Intent(this, ChooseTimeActivity.class);
                startActivityForResult(intent,CHOOSE_TIME_REQUEST_CODE);
                break;

            case (R.id.btSetDate):
                intent = new Intent(this, ChooseDateActivity.class);
                startActivityForResult(intent,CHOOSE_DATE_REQUEST_CODE);
                break;

            case (R.id.buttonOkDialog):
                //We're instantiating a Calendar instance to get the desired time in milliseconds
                Calendar timeChosen = Calendar.getInstance();
                timeChosen.set(Calendar.YEAR, year);
                timeChosen.set(Calendar.MONTH, month);
                timeChosen.set(Calendar.DAY_OF_MONTH, day);
                timeChosen.set(Calendar.HOUR_OF_DAY, hour);
                timeChosen.set(Calendar.MINUTE, minute);
                Calendar timeNow = GregorianCalendar.getInstance(); // creates a new calendar instance
                long timeInMs = timeChosen.getTimeInMillis();
                String task = etTask.getText().toString();
                //Checking that user did not choose past time
                if (timeChosen.compareTo(timeNow) >= 0) {
                    i.putExtra("day",       day);
                    i.putExtra("month",     month);
                    i.putExtra("year",      year);
                    i.putExtra("hour",      hour);
                    i.putExtra("minute",    minute);
                    i.putExtra("timeInMs",  timeInMs);
                    i.putExtra("task",      task);
                    i.putExtra("priority",  priority);
                    i.putExtra("new",       isTaskNew);
                    int id = callingIntent.getIntExtra("id", 0);
                    i.putExtra("id",        id);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case CHOOSE_TIME_REQUEST_CODE:
                    hour = data.getIntExtra("hour", 7);
                    minute = data.getIntExtra("minute", 30);
                    String timeChosen = hour + ":" + ((minute < 10) ? "0" + minute : minute);
                    tvTimeChosen.setText(timeChosen);
                    break;

                case CHOOSE_DATE_REQUEST_CODE:
                    year = data.getIntExtra("year", 2016);
                    month = data.getIntExtra("month", 7);
                    day = data.getIntExtra("day", 4);
                    String dateChosen = ((month < 10) ? "0" + month : month) + "/"
                            + ((day < 10) ? "0" + day : day) + "/" + year;
                    tvDateChosen.setText(dateChosen);
                    break;
            }
        }
    }
}
