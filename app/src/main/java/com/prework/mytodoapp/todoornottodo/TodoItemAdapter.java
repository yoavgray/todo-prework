package com.prework.mytodoapp.todoornottodo;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

public class TodoItemAdapter extends ArrayAdapter<TodoItem> {

    Context context;
    int layoutResourceId;
    List<TodoItem> data = null;

    public TodoItemAdapter(Context context, int layoutResourceId, List<TodoItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final ItemHolder holder;

        if (row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ItemHolder();
            holder.tv = (TextView)row.findViewById(R.id.tvListView);
            holder.cb = (CheckBox) row.findViewById(R.id.cbComplete);

            row.setTag(holder);
        }
        else
        {
            holder = (ItemHolder)row.getTag();
        }

        Log.d("kaki", position + " " + row.toString() + " " + parent.toString());

        TodoItem todoItem = data.get(position);
        holder.tv.setText(todoItem.getHeader());
        holder.cb.setChecked(todoItem.isChecked());

        holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("kaki", "checkbox is " + isChecked);
                if (isChecked == true) {
                    holder.tv.setPaintFlags(holder.tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    holder.tv.setPaintFlags(holder.tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });

        if (todoItem.isChecked() == true) {
            holder.tv.setPaintFlags(holder.tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.tv.setPaintFlags(holder.tv.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        return row;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    static class ItemHolder
    {
        CheckBox cb;
        TextView tv;
    }
}
