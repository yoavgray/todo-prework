package com.prework.mytodoapp.todoornottodo;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.CalendarContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Calendar;
import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<ListItem> {

    final static int REQUEST_CODE_CHOOSE_TIME = 1;
    final static int PRIORITY_LOW = 0;
    final static int PRIORITY_MEDIUM = 1;
    final static int PRIORITY_HIGH = 2;
    final static int PRIORITY_SUPER = 3;

    ListItemDataSource dataSource;
    Context context;
    int layoutResourceId;
    List<ListItem> data = null;

    public TodoItemAdapter(Context context, int layoutResourceId, List<ListItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public ListItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ItemHolder holder;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.tvPriority           = (TextView)row.findViewById(R.id.tvPriority);
            holder.tvText               = (TextView)row.findViewById(R.id.tvListView);
            holder.tvDate               = (TextView)row.findViewById(R.id.tvDayDue);
            holder.tvTime               = (TextView)row.findViewById(R.id.tvTimeDue);
            holder.cb                   = (CheckBox)row.findViewById(R.id.cbComplete);
            holder.fabEdit              = (FloatingActionButton)row.findViewById(R.id.fabEdit);
            holder.fabDelete            = (FloatingActionButton)row.findViewById(R.id.fabDelete);
            holder.fabExport            = (FloatingActionButton)row.findViewById(R.id.fabExport);
            holder.barrier              = row.findViewById(R.id.listItemVerticalBarrier);
            holder.bottomLinearLayout   = (LinearLayout)row.findViewById(R.id.LinearLayoutListItemBottom);

            row.setTag(holder);
        } else {
            holder = (ItemHolder)row.getTag();
        }

        final ListItem todoItem = data.get(position);
        holder.barrier.setVisibility(todoItem.isShown() ? View.VISIBLE : View.GONE);
        holder.bottomLinearLayout.setVisibility(todoItem.isShown() ? View.VISIBLE : View.GONE);
        holder.tvText.setText(todoItem.getText());
        String dateDisplay = todoItem.getDate();
        holder.tvDate.setText(dateDisplay);
        String timeDisplay = todoItem.getTime();
        holder.tvTime.setText(timeDisplay);
        holder.cb.setChecked(todoItem.isChecked());
        int priority = todoItem.getPriority();
        switch (priority) {
            case PRIORITY_LOW:
                row.setBackgroundColor(Color.parseColor("#FFCDD2"));
                holder.tvPriority.setText(R.string.priority_low);
                break;
            case PRIORITY_MEDIUM:
                holder.tvPriority.setText(R.string.priority_med);
                row.setBackgroundColor(Color.parseColor("#EF9A9A"));
                break;
            case PRIORITY_HIGH:
                holder.tvPriority.setText(R.string.priority_hi);
                row.setBackgroundColor(Color.parseColor("#E57373"));
                break;
            case PRIORITY_SUPER:
                holder.tvPriority.setText(R.string.priority_super);
                row.setBackgroundColor(Color.parseColor("#EF5350"));
                break;
        }

        if (todoItem.isChecked()) {
            holder.tvPriority.setEnabled(false);
            holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tvPriority.setEnabled(true);
            holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        //OnCheckedChanged creates a bug while scrolling list. Views lose their values (Checkboxes
        //are getting unchecked, etc)
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = todoItem.getId();
                boolean isChecked = ((CheckBox)v).isChecked();

                if (isChecked) {
                    holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

                int priority = todoItem.getPriority();
                LinearLayout ll = ((LinearLayout) v.getParent().getParent());
                switch (priority) {
                    case PRIORITY_LOW:
                        YoYo.with(Techniques.Swing)
                                .duration(700)
                                .playOn(ll.findViewById(R.id.tvListView));
                        break;
                    case PRIORITY_MEDIUM:
                        YoYo.with(Techniques.FlipInX)
                                .duration(700)
                                .playOn(ll.findViewById(R.id.tvListView));
                        break;
                    case PRIORITY_HIGH:
                        YoYo.with(Techniques.Bounce)
                                .duration(700)
                                .playOn(ll.findViewById(R.id.tvListView));
                        break;
                    case PRIORITY_SUPER:
                        YoYo.with(Techniques.Shake)
                                .duration(700)
                                .playOn(ll.findViewById(R.id.tvListView));
                        break;
                }

                if (dataSource == null) {
                    dataSource = new ListItemDataSource(context);
                    dataSource.open();
                }
                dataSource.updateListItem(2, id, 0, null, isChecked, "", "");
                dataSource.close();
                todoItem.setChecked(isChecked);
                holder.tvPriority.setEnabled(!isChecked);
            }
        });

        holder.fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isChecked = holder.cb.isChecked();
                if (isChecked) {
                    Toast.makeText(context, R.string.uncheck_task, Toast.LENGTH_LONG).show();
                } else {
                    Intent i = new Intent(context, SetTimeActivity.class);
                    i.putExtra("new", false);
                    i.putExtra("id", todoItem.getId());
                    i.putExtra("task", todoItem.getText());
                    i.putExtra("priority", todoItem.getPriority());
                    i.putExtra("date", todoItem.getDate());
                    i.putExtra("time", todoItem.getTime());
                    ((Activity) context).startActivityForResult(i, REQUEST_CODE_CHOOSE_TIME);
                }
            }
        });

        holder.fabDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListItem todoItem = data.get(position);
                AlertDialog.Builder adb = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
                adb.setTitle(R.string.dialog_delete_title);
                adb.setMessage(R.string.dialog_delete_message);
                adb.setIcon(android.R.drawable.ic_dialog_alert);
                adb.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent taskIntent = new Intent(context, TaskTimeReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, todoItem.getId(), taskIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        am.cancel(pendingIntent);
                        if (dataSource == null) {
                            dataSource = new ListItemDataSource(context);
                            dataSource.open();
                        }
                        dataSource.deleteListItem(todoItem);
                        data.remove(position);
                        notifyDataSetChanged();
                    } });

                adb.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    } });

                AlertDialog alertDialog = adb.create();
                alertDialog.show();
            }
        });

        holder.fabExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ListItem todoItem = data.get(position);
                String[] dateSplit = todoItem.getDate().split("/");
                String[] timeSplit = todoItem.getTime().split(":");
                Calendar time = Calendar.getInstance();
                time.set(Calendar.YEAR, Integer.parseInt(dateSplit[2]));
                //The months start from 0 so we need to adjust
                time.set(Calendar.MONTH, Integer.parseInt(dateSplit[0]) - 1);
                time.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[1]));
                time.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
                time.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
                long start = time.getTimeInMillis();
                Intent intent = new Intent(Intent.ACTION_INSERT)
                        .setData(CalendarContract.Events.CONTENT_URI)
                        .putExtra(CalendarContract.Events.TITLE, todoItem.getText())
                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, start);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        return row;
    }

    static class ItemHolder
    {
        TextView tvPriority, tvText, tvDate, tvTime;
        View barrier;
        LinearLayout bottomLinearLayout;
        FloatingActionButton fabEdit, fabDelete, fabExport;
        CheckBox cb;
    }
}
