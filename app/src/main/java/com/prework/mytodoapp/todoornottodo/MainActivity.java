package com.prework.mytodoapp.todoornottodo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE_TIME = 1;
    private static final String TASK_ID_FILE = "MyTaskIdFile";
    private static final int UPDATE_PRIORITY = 0;
    private static final int UPDATE_TASK = 1;
    private static final int UPDATE_IS_CHECKED = 2;
    private static final int UPDATE_TIME_AND_DATE = 3;
    private static int taskId;

    ListItemDataSource dataSource;
    List<ListItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences settings = getSharedPreferences(TASK_ID_FILE, 0);
        taskId = settings.getInt("taskId", 0);
        //Toast.makeText(this, "Read taskId from file. taskId = " + taskId, Toast.LENGTH_LONG).show();
        dataSource = new ListItemDataSource(this);
        dataSource.open();
        items = new ArrayList<>();
        dataSource.getAllItems(items);

        setupViews();
        setupListViewListener();
        ActionBar ab = getSupportActionBar();
        Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.logo_actionbar, null);
        assert ab != null;
        ab.setBackgroundDrawable(d);
        ab.setDisplayShowTitleEnabled(false);
        //Change the color of the status bar above API21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.RED);
        }

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            NetworkInfo.DetailedState ds = networkInfo.getDetailedState();
            String extraInfo = networkInfo.getExtraInfo();
            Toast.makeText(this,"Yay! Apparently, you're connected to " + extraInfo.substring(1,extraInfo.length()-1) + " network!",Toast.LENGTH_LONG).show();
        }

        //This is for when the activity is started from a 'task due' notification. We want to
        //update the list item and dismiss the notification
        Intent startingIntent = getIntent();
        if (startingIntent != null && startingIntent.getBooleanExtra("done", false)) {
            int doneTaskId = startingIntent.getIntExtra("id",0);
            for (ListItem listItem : items) {
                if (listItem.getId() == doneTaskId) {
                    listItem.setChecked(true);
                    dataSource.updateListItem(UPDATE_IS_CHECKED, doneTaskId, 0, null, true, "", "");
                    break;
                }
            }
            itemsAdapter.notifyDataSetChanged();
            //After we marked task as done, we would like to cancel the notification
            Intent cancel = new Intent("com.prework.cancel");
            cancel.putExtra("id", doneTaskId);
            sendBroadcast(cancel);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences settings = getSharedPreferences(TASK_ID_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("taskId", taskId);

        // Commit the edits!
        editor.apply();

        if (dataSource.isOpen()) {
            dataSource.close();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences settings = getSharedPreferences(TASK_ID_FILE, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("taskId", taskId);

        // Commit the edits!
        editor.apply();

        if (dataSource.isOpen()) {
            dataSource.close();
        }
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

            //Device should support hebrew letters for this to be shown
            case R.id.set_hebrew:
                conf.locale = new Locale("he".toLowerCase());
                res.updateConfiguration(conf, dm);
                finish();
                startActivity(intent);
                return true;

            case R.id.action_like_dislike:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                DialogFragment newFragment = LikeDislikeFragment.newInstance();
                newFragment.show(getSupportFragmentManager(), "Dialog");
                ft.commit();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupViews() {
        itemsAdapter = new TodoItemAdapter(this, R.layout.my_list_item, items);
        lvItems = (ListView)findViewById(R.id.lvItems);
        assert lvItems != null;
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
                        ListItem thisItem = items.get(position);
                        Intent taskIntent = new Intent(MainActivity.this, TaskTimeReceiver.class);
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, thisItem.getId(), taskIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        dataSource.deleteListItem(thisItem);
                        items.remove(position);
                        itemsAdapter.notifyDataSetChanged();
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
                items.get(position).setShown(!items.get(position).isShown());
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }

    //just trying a different method - calling a function from onClick method in
    //button XML attributes
    public void addTask(View v) {
        Intent i = new Intent(this, SetTimeActivity.class);
        i.putExtra("new", true);
        startActivityForResult(i, REQUEST_CODE_CHOOSE_TIME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_CHOOSE_TIME:
                if (resultCode == RESULT_CANCELED) {
                    Log.d("MainActivity","User canceled task addition");
                } else {
                    int year = data.getIntExtra("year", 2016);
                    int month = data.getIntExtra("month", 7);
                    int day = data.getIntExtra("day", 4);
                    int hour = data.getIntExtra("hour", 7);
                    int minute = data.getIntExtra("minute", 30);

                    String itemText = data.getStringExtra("task");
                    int priority = data.getIntExtra("priority", 0);
                    //clean text field for the next task to be added
                    String time = hour + ":" + ((minute < 10) ? "0" + minute : minute);
                    String date = ((month < 10) ? "0" + month : month) + "/"
                            + ((day < 10) ? "0" + day : day) + "/" + year;

                    Intent taskIntent = new Intent(this, TaskTimeReceiver.class);
                    boolean isTaskNew = data.getBooleanExtra("new", true);
                    if (isTaskNew) {
                        boolean result = dataSource.addListItem(taskId, priority, itemText, false, time, date);
                        if (!result) {
                            Toast.makeText(this, "Error: Task was not added! Please try again", Toast.LENGTH_LONG).show();
                        } else {
                            items.add(new ListItem(taskId, priority, itemText, false, time, date));
                            itemsAdapter.notifyDataSetChanged();
                            //Intent and data to start a broadcast and to eventually present
                            //data in the notification when task is due
                            taskIntent.putExtra("task", itemText);
                            taskIntent.putExtra("date", date);
                            taskIntent.putExtra("time", time);
                            taskIntent.putExtra("priority", priority);
                            taskIntent.putExtra("taskId",taskId);
                            pendingIntent = PendingIntent.getBroadcast(this, taskId, taskIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                            taskId++;
                            //Set alarm with the right offset
                            long timeInMs = data.getLongExtra("timeInMs", 1);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMs, pendingIntent);
                        }
                    } else {
                        int id = data.getIntExtra("id", 0);
                        dataSource.updateListItem(UPDATE_PRIORITY,      id, priority, "", false, "", "");
                        dataSource.updateListItem(UPDATE_TASK,          id, 0, itemText, false, "", "");
                        dataSource.updateListItem(UPDATE_TIME_AND_DATE, id, 0, "", false, time, date);
                        for (ListItem thisItem : items) {
                            if (thisItem.getId() == id) {
                                thisItem.setText(itemText);
                                thisItem.setPriority(priority);
                                thisItem.setDate(date);
                                thisItem.setTime(time);
                            }
                        }
                        itemsAdapter.notifyDataSetChanged();
                        taskIntent.putExtra("task", itemText);
                        taskIntent.putExtra("date", date);
                        taskIntent.putExtra("time", time);
                        taskIntent.putExtra("priority", priority);
                        taskIntent.putExtra("taskId", id);
                        pendingIntent = PendingIntent.getBroadcast(this, id, taskIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);
                        long timeInMs = data.getLongExtra("timeInMs", 1);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMs, pendingIntent);
                    }

                }
                break;
        }
    }

    //set this new function just to have a public access to writeItemsFile()
    //through clicking on the checkbox, to save parameters
    public void notifyChange(View v) {
        itemsAdapter.notifyDataSetChanged();
    }

    //extracting values from EditTaskFragment dialog
    public void onUserSelectValue(String text, int position) {
        if (text != null) {
            //extracting changed task and position in the items ArrayList
            ListItem thisItem = items.get(position);
            dataSource.updateListItem(UPDATE_TASK, thisItem.getId(), 0, text, false, "", "");
            thisItem.setText(text);
            itemsAdapter.notifyDataSetChanged();
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
