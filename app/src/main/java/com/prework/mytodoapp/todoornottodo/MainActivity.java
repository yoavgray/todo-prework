package com.prework.mytodoapp.todoornottodo;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE_TIME = 1;
    List<ListItem> items;
    List<String> itemsFromFile;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    TextView tvCap;
    EditText etText;
    int year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        setupViews();
        setupListViewListener();
        ActionBar ab = getSupportActionBar();
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.logo, null);
        ab.setBackgroundDrawable(d);
        ab.setDisplayShowTitleEnabled(false);
    }

    //save data before leaving app
    @Override
    protected void onStop() {
        super.onStop();
        writeItems();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Resources res = getResources();
        // Change locale settings in the app. I'm aware that this is not a good practice
        // and that language should be set by the locale of the device. Just wanted to experiment
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        Intent intent = getIntent();

        switch (item.getItemId()) {
            case R.id.set_english:
                conf.locale = new Locale("en".toLowerCase());
                res.updateConfiguration(conf, dm);
                finish();
                startActivity(intent);
                return true;

            case R.id.set_spanish:
                conf.locale = new Locale("es".toLowerCase());
                res.updateConfiguration(conf, dm);
                finish();
                startActivity(intent);
                return true;

            /* Device should support hebrew letters for this to be shown
            case R.id.set_hebrew:
                conf.locale = new Locale("he".toLowerCase());
                res.updateConfiguration(conf, dm);
                finish();
                startActivity(intent);
                return true;
            */

            case R.id.action_like_dislike:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                DialogFragment newFragment = LikeDislikeFragment.newInstance();
                newFragment.show(getSupportFragmentManager(), "Dialog");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {
        itemsAdapter = new TodoItemAdapter(this, R.layout.my_list_item, items);
        tvCap = (TextView)findViewById(R.id.tvCap);
        tvCap.setText(R.string.long_click);
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this, R.style.AlertDialogStyle);
                adb.setTitle(R.string.dialog_delete_title);
                adb.setMessage(R.string.dialog_delete_message);
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                    } });

                adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    } });

                AlertDialog alertDialog = adb.create();
                alertDialog.show();
                return true;
            }
        });

        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (items.get(position).isChecked()) {
                    Toast.makeText(getBaseContext(), R.string.uncheck_task, Toast.LENGTH_LONG).show();
                } else {
                    // Create and show the dialog.
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.addToBackStack(null);
                    String text = items.get(position).getText();
                    DialogFragment newFragment = EditTaskFragment.newInstance(text, position);
                    newFragment.show(getSupportFragmentManager(), "Dialog");
                    ft.commit();
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
                    etText.setText("");
                } else {
                    year = data.getIntExtra("year", 2016);
                    month = data.getIntExtra("month", 7);
                    day = data.getIntExtra("day", 4);
                    hour = data.getIntExtra("hour", 7);
                    minute = data.getIntExtra("minute", 30);

                    String itemText = etText.getText().toString();
                    //clean text field for the next task to be added
                    etText.setText("");
                    itemsAdapter.add(new ListItem(itemText, false, year, month, day, hour, minute, 0));
                    //make sure to keep file updated
                    writeItems();
                }
                break;
        }
    }

    public void onAddItem(View v) {
        etText = (EditText)findViewById(R.id.etText);

        if (etText.getText().toString().equals("")) {
            Toast.makeText(this, R.string.task_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        inflateChooseTimeDialog();
    }

    //just trying a different method - calling a function from onClick method in
    //button XML attributes
    public void inflateChooseTimeDialog() {
        Intent i = new Intent(this, SetTimeActivity.class);
        startActivityForResult(i, REQUEST_CODE_CHOOSE_TIME);
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

    public void onUserSendMail(String emailContent) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto","yoavgray@gmail.com", null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "To Do? Or Not To Do App");
        emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
