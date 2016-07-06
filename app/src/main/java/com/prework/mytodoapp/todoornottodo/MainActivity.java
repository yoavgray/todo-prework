package com.prework.mytodoapp.todoornottodo;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_EDIT_TASK = 0;
    private static final int REQUEST_CODE_CHOOSE_TIME = 1;
    List<ListItem> items;
    List<String> itemsFromFile;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    TextView tvCap, tvDateTime;
    EditText etText;
    Spinner s;
    int year, month, day, hour, minute;

    final static int TEXT_START = 15;
    final static int TEXT_CHECK_OFFSET = 3;
    final static int IS_CHECKED_START = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        setupViews();
        setupListViewListener();

    }

    //save data before leaving app
    @Override
    protected void onStop() {
        super.onStop();
        writeItems();
    }

    private void setupViews() {
        itemsAdapter = new TodoItemAdapter(this, R.layout.my_list_item, items);

        tvDateTime = (TextView)findViewById(R.id.tvDateTime);
        tvDateTime.setText("Please set time");
        tvCap = (TextView)findViewById(R.id.tvCap);
        tvCap.setText("* Long click on a task to remove it from list");
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).isChecked()) {
                    Toast.makeText(getBaseContext(), "Uncheck 'Complete' checkbox to edit task", Toast.LENGTH_LONG).show();
                } else {
                    // Create and show the dialog.
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    String text = items.get(position).getText();
                    DialogFragment newFragment = EditTaskFragment.newInstance(text, position);
                    newFragment.show(getSupportFragmentManager(), "Dialog");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_TIME:
                if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getBaseContext(), "Time and date were not set", Toast.LENGTH_SHORT).show();
                } else {
                    year = data.getIntExtra("year", 2016);
                    month = data.getIntExtra("month", 7);
                    day = data.getIntExtra("day", 4);
                    hour = data.getIntExtra("hour", 7);
                    minute = data.getIntExtra("minute", 30);
                    String timeDisplay = month + "/" + day + "/" + year + "  " + hour + ":" + minute;
                    tvDateTime.setText(timeDisplay);
                }
                break;
        }
    }

    public void onAddItem(View v) {
        etText = (EditText)findViewById(R.id.etText);

        if (etText.getText().toString().equals("")) {
            Toast.makeText(this, "Task is empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (tvDateTime.getText().subSequence(0,1).equals("P")) {
            Toast.makeText(this, "Time and date are not set!", Toast.LENGTH_SHORT).show();
            return;
        }

        String itemText = etText.getText().toString();
        //clean text field for the next task to be added
        etText.setText("");
        tvDateTime.setText(R.string.please_set_time);
        itemsAdapter.add(new ListItem(itemText, false, year, month, day, hour, minute, 0));
        //make sure to keep file updated
        writeItems();
    }

    //read list from saved file
    private void readItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            itemsFromFile = new ArrayList<>(FileUtils.readLines(todoFile));
            items = new ArrayList<ListItem>();
            //to save a list with a text value and a boolean value, I had to parse
            //the text that looks like "ListItem{text='foo', isChecked=false}"
            for (int i=0; i<itemsFromFile.size(); i++) {
                String strFromFile = itemsFromFile.get(i);
                JSONObject jo = new JSONObject(strFromFile);

                String text = jo.getString("text");
                boolean isChecked = jo.getBoolean("isChecked");
                int year = jo.getInt("year");
                int month = jo.getInt("month");
                int day = jo.getInt("day");
                int hour = jo.getInt("hour");
                int minute = jo.getInt("minute");
                int priority = jo.getInt("priority");
                items.add(new ListItem(text, isChecked, year, month, day, hour, minute, priority));
            }
        } catch (IOException e) {
            items = new ArrayList<ListItem>();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //write list to a txt file
    private void writeItems() {
        File filesDir = getFilesDir();
        File todoFile = new File(filesDir, "todo.txt");
        try {
            FileUtils.writeLines(todoFile, items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //set this new function just to have a public access to writeItems()
    //through clicking on the checkbox, to save parameters
    public void notifyChange(View v) {
        itemsAdapter.notifyDataSetChanged();
        writeItems();
    }

    //just trying a different method - calling a function from onClick method in
    //button XML attributes
    public void inflateChooseTimeDialog(View v) {
        Intent i = new Intent(this, SetTimeActivity.class);
        startActivityForResult(i, REQUEST_CODE_CHOOSE_TIME);
    }

    //extracting values from EditTaskFragment dialog
    public void onUserSelectValue(String text, int position) {
        if (text != null) {
            //extracting changed task and position in the items ArrayList
            items.get(position).setText(text);
            itemsAdapter.notifyDataSetChanged();
            //make sure to keep the file updated
            writeItems();
        } else {
            Toast.makeText(getBaseContext(), "Task was not changed", Toast.LENGTH_SHORT).show();
        }
    }
}
