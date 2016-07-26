package com.prework.mytodoapp.todoornottodo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
            holder.spinner = (Spinner)row.findViewById(R.id.priority_spinner);
            holder.tvText = (TextView)row.findViewById(R.id.tvListView);
            holder.tvDate = (TextView)row.findViewById(R.id.tvDayDue);
            holder.tvTime = (TextView)row.findViewById(R.id.tvTimeDue);
            holder.cb = (CheckBox)row.findViewById(R.id.cbComplete);

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

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.priority_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        holder.spinner.setAdapter(adapter);
        holder.spinner.setSelection(todoItem.getPriority());
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                todoItem.setPriority(position);
                LinearLayout ll = (LinearLayout)parent.getParent();
                dataSource = new ListItemDataSource(getContext());
                dataSource.open();
                switch (position) {
                    case PRIORITY_LOW:
                        ll.setBackgroundColor(Color.parseColor("#FFC9C9"));
                        break;
                    case PRIORITY_MEDIUM:
                        ll.setBackgroundColor(Color.parseColor("#FD9B9B"));
                        break;
                    case PRIORITY_HIGH:
                        ll.setBackgroundColor(Color.parseColor("#FD5656"));
                        break;
                    case PRIORITY_SUPER:
                        ll.setBackgroundColor(Color.parseColor("#FF0000"));
                        break;

                }
                dataSource.updateListItem(0, todoItem.getId(), position, null, false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (todoItem.isChecked()) {
            holder.spinner.setEnabled(false);
            holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.spinner.setEnabled(true);
            holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        //OnCheckedChanged creates a bug while scrolling list. Views losetheir values (Checkboxes
        //are getting unchecked, etc)
        holder.cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long id = todoItem.getId();
                boolean isChecked = ((CheckBox)v).isChecked();

                if (isChecked) {
                    holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }

                dataSource.updateListItem(2, id, 0, null, isChecked);
                todoItem.setChecked(isChecked);
                holder.spinner.setEnabled(!isChecked);
            }
        });

        return row;
    }

    static class ItemHolder
    {
        Spinner spinner;
        TextView tvText, tvDate, tvTime;
        CheckBox cb;
    }
}
