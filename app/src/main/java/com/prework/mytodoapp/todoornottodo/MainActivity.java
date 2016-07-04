package com.prework.mytodoapp.todoornottodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<TodoItem> items;
    TodoItemAdapter itemsAdapter;
    ListView lvItems;
    EditText etHeader, etNewItem;
    TextView header;
    CheckBox cbComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        items = new ArrayList<>();
        itemsAdapter = new TodoItemAdapter(this, R.layout.my_list_item, items);
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);
        /*
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("setOnItemClickListener", parent.toString() + " " + view.getTag().toString() + " " + position + " " + id);
            }
        });
        */
    }

    public void onAddItem(View v) {
        etHeader = (EditText)findViewById(R.id.etHeader);
        etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemHeader = etHeader.getText().toString();
        String itemText = etNewItem.getText().toString();

        itemsAdapter.add(new TodoItem(itemHeader, false, itemText));
    }

}
