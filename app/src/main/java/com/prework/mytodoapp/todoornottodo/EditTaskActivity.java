package com.prework.mytodoapp.todoornottodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etEditTask;
    Button ok, cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        setupViews();
        Intent i = getIntent();
        String text = i.getStringExtra("text");
        etEditTask.setText(text);
        etEditTask.setSelection(etEditTask.getText().length());
    }

    @Override
    public void onClick(View v) {
        Intent i = getIntent();
        switch (v.getId()) {
            case R.id.buttonEditTaskCancel:
                i.putExtra("change","no");
                break;
            case R.id.buttonEditTaskOk:
                i.putExtra("change","yes");
                i.putExtra("text", etEditTask.getText().toString());
                break;
        }

        setResult(RESULT_OK, i);
        finish();
    }

    private void setupViews() {
        etEditTask = (EditText)findViewById(R.id.etEditTask);
        ok = (Button)findViewById(R.id.buttonEditTaskOk);
        cancel = (Button)findViewById(R.id.buttonEditTaskCancel);
        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }
}
