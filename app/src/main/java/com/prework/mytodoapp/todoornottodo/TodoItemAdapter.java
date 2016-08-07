package com.prework.mytodoapp.todoornottodo;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<ListItem> {

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
            holder.tvPriority   = (TextView)row.findViewById(R.id.tvPriority);
            holder.tvText       = (TextView)row.findViewById(R.id.tvListView);
            holder.tvDate       = (TextView)row.findViewById(R.id.tvDayDue);
            holder.tvTime       = (TextView)row.findViewById(R.id.tvTimeDue);
            holder.cb           = (CheckBox)row.findViewById(R.id.cbComplete);

            row.setTag(holder);
        } else {
            holder = (ItemHolder)row.getTag();
        }

        final ListItem todoItem = data.get(position);
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
                    dataSource = new ListItemDataSource(getContext());
                    dataSource.open();
                }
                dataSource.updateListItem(2, id, 0, null, isChecked, "", "");
                dataSource.close();
                todoItem.setChecked(isChecked);
                holder.tvPriority.setEnabled(!isChecked);
            }
        });

        return row;
    }

    static class ItemHolder
    {
        TextView tvPriority, tvText, tvDate, tvTime;
        CheckBox cb;
    }
}
