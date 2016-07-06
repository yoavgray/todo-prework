package com.prework.mytodoapp.todoornottodo;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<ListItem> {

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
        holder.tvDate.setText(todoItem.getMonth() + "/" + todoItem.getDay() + "/" + todoItem.getYear());
        holder.tvTime.setText(todoItem.getHour() + ":" + todoItem.getMinute());
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

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    todoItem.setChecked(true);
                    holder.spinner.setEnabled(false);
                    holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    todoItem.setChecked(false);
                    holder.spinner.setEnabled(true);
                    holder.tvText.setPaintFlags(holder.tvText.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });

        return row;
    }

    static class ItemHolder
    {
        CheckBox cb;
        TextView tvText, tvDate, tvTime;
        Spinner spinner;
    }
}
