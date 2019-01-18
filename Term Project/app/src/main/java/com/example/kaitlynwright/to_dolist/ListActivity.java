package com.example.kaitlynwright.to_dolist;

import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.ViewGroup;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;
import android.database.Cursor;


public class ListActivity extends AppCompatActivity
        implements OnItemClickListener, OnClickListener {

    //task list
    TaskItems tasks;

    //adapter
    SimpleCursorAdapter adapter;

    //Sqlite
    SQLiteHelper helper;

    // preferences
    private SharedPreferences savedValues;
    private String pref_taskOrg;

    //widgets
    private ListView items;
    private TextView listTitle;
    private Button addNewTask;
    private Button clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Initialize values
        helper = new SQLiteHelper(this);
        tasks = helper.fetchAllTasks();

        //Widgets
        listTitle = (TextView) findViewById(R.id.taskListTitle);
        addNewTask = (Button) findViewById(R.id.addNewTask);
        clearButton = (Button) findViewById(R.id.clearButton);
        items = (ListView) findViewById(R.id.taskList);

        // *** Adapter *** //
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item, helper.fetchAllTasksCursor(),
                new String[]{ helper.TASK_NAME, helper.DUE_DATE, helper.COMPLETE },
                new int[]{ R.id.title, R.id.info, R.id.complete }
        ) {

          @Override
          public View getView(int position, View convertView, ViewGroup parent)
          {
              View v = super.getView(position, convertView, parent);

              Button editButton = (Button) v.findViewById(R.id.editButton);
              Button deleteButton = (Button) v.findViewById(R.id.deleteButton);

              //Edit button on each task
              editButton.setTag(position);
              editButton.setOnClickListener(new OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      // get current task
                      Task currentTask = tasks.get(((Integer)view.getTag()).intValue());
                      SQLiteDatabase db = helper.getReadableDatabase();
                      Cursor c = (Cursor) adapter.getCursor();
                      c.moveToPosition(((Integer)view.getTag()).intValue());

                      //remove current version of task from lists
                      helper.deleteTask(c);
                      tasks.remove(currentTask);

                      // save current task info for editing
                      Editor editor = savedValues.edit();
                      editor.putString("name", currentTask.getName());
                      editor.putString("dueDate", currentTask.getDueDate());
                      editor.putBoolean("complete", currentTask.getComplete());
                      editor.putBoolean("longTerm", currentTask.getLongTerm());
                      editor.putString("note", currentTask.getNote());
                      editor.commit();

                      Intent i = new Intent(ListActivity.this, EditTaskActivity.class);
                      startActivityForResult(i, 1);
                  }
              });

              //Delete button on each task
              deleteButton.setTag(position);
              deleteButton.setOnClickListener(

               new OnClickListener() {
                  @Override
                  public void onClick(View view) {
                      Task currentTask = tasks.get(((Integer)view.getTag()).intValue());
                      Cursor c = (Cursor) adapter.getCursor();
                      c.moveToPosition(((Integer)view.getTag()).intValue());
                      tasks.remove(currentTask);
                      helper.deleteTask(c);
                      adapter.changeCursor(helper.fetchAllTasksCursor());
                      adapter.notifyDataSetChanged();

                      if (tasks.size() == 0) { listTitle.setText("No Current Tasks"); }
                  }
              });

              return v;
          }
        };
        // *** End Adapter *** //

        //Set listeners and adapters
        items.setAdapter(adapter);
        items.setOnItemClickListener(this);
        addNewTask.setOnClickListener(this);
        clearButton.setOnClickListener(this);

        //Set Title
        if(tasks.size() == 0) { listTitle.setText("No Current Tasks"); }
        else { listTitle.setText("Current Tasks"); }

        //Preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        savedValues = PreferenceManager.getDefaultSharedPreferences(this);
        pref_taskOrg = savedValues.getString("pref_taskOrg", "Alphabetical");

    }

    @Override
    public void onResume() {
        super.onResume();
        tasks = helper.fetchAllTasks();
        if(tasks.size() != 0) listTitle.setText("Current Tasks");

        //preferences
        pref_taskOrg = savedValues.getString("pref_taskOrg", "Alphabetical");

        //update database
        helper.sortBy(pref_taskOrg);
        adapter.changeCursor(helper.fetchAllTasksCursor());
    }

    // task click
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        String note;
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = (Cursor) adapter.getCursor();
        c.moveToPosition(position);
        note = c.getString(c.getColumnIndex(SQLiteHelper.NOTE));

        Toast.makeText(ListActivity.this, note, Toast.LENGTH_LONG).show();
        db.close();
    }

    //button click event handling
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addNewTask:
                Intent i = new Intent(ListActivity.this, NewTaskActivity.class);
                startActivityForResult(i, 1);
                break;
            case R.id.clearButton:
                Cursor c = (Cursor) adapter.getCursor();
                helper.clearTable(c);
                tasks.clear();
                listTitle.setText("No Current Tasks");
                adapter.changeCursor(helper.fetchAllTasksCursor());
                break;
        }
    }

    // getting values from new/edit task activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Task newTask = new Task();
                newTask.setName(data.getExtras().getString("name", "No Name"));
                newTask.setDueDate(data.getExtras().getString("dueDate", "No Date"));
                newTask.setNote(data.getExtras().getString("note", "No Note"));
                newTask.setComplete(data.getExtras().getBoolean("complete"));
                newTask.setLongTerm(data.getExtras().getBoolean("longTerm"));

                // update list & database
                tasks.add(newTask);
                helper.addTask(newTask);
                helper.sortBy(pref_taskOrg);
                adapter.changeCursor(helper.fetchAllTasksCursor());
                adapter.notifyDataSetChanged();
            }
        }
    }

    // Methods for Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.menu_about:
                Toast.makeText(this, "Created by Kaitlyn Wright", Toast.LENGTH_LONG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
