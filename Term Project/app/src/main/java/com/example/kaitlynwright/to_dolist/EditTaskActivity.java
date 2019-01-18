package com.example.kaitlynwright.to_dolist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.io.LineNumberInputStream;

public class EditTaskActivity extends AppCompatActivity
            implements View.OnClickListener, TextView.OnEditorActionListener {

        //widget variables
        private TextView title;
        private EditText nameEdit;
        private EditText dueDateEdit;
        private EditText noteEdit;
        private CheckBox completeBox;
        private CheckBox longTermBox;
        private Button submitButton;
        private Button cancelButton;

        // variables
        String name;
        String dueDate;
        String note;
        Boolean complete;
        Boolean longTerm;

        //preferences
        private SharedPreferences savedValues;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_ACTION_BAR);
            setContentView(R.layout.activity_new_task);

            //get widget references
            title = (TextView) findViewById(R.id.titleTextView);
            nameEdit = (EditText) findViewById(R.id.nameEdit);
            dueDateEdit = (EditText) findViewById(R.id.dueDateEdit);
            noteEdit = (EditText) findViewById(R.id.noteEdit);
            completeBox = (CheckBox) findViewById(R.id.completeBox);
            longTermBox = (CheckBox) findViewById(R.id.longTermBox);
            submitButton = (Button) findViewById(R.id.submitTaskButton);
            cancelButton = (Button) findViewById(R.id.cancelButton);

            // set listeners
            nameEdit.setOnEditorActionListener(this);
            dueDateEdit.setOnEditorActionListener(this);
            noteEdit.setOnEditorActionListener(this);
            submitButton.setOnClickListener(this);
            cancelButton.setOnClickListener(this);

            //preferences
            PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
            savedValues = PreferenceManager.getDefaultSharedPreferences(this);

            //set saved values for editing
            title.setText("Edit Task");

            name = savedValues.getString("name", " ");
            nameEdit.setText(name);

            dueDate = savedValues.getString("dueDate", " ");
            dueDateEdit.setText(dueDate);

            note = savedValues.getString("note", " ");
            noteEdit.setText(note);

            complete = savedValues.getBoolean("complete", false);
            completeBox.setChecked(complete);

            longTerm = savedValues.getBoolean("longTerm", false);
            longTermBox.setChecked(longTerm);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                switch(v.getId()) {
                    case R.id.nameEdit:
                        name = nameEdit.getText().toString();
                        break;
                    case R.id.dueDateEdit:
                        dueDate = dueDateEdit.getText().toString();
                        break;
                    case R.id.noteEdit:
                        note = noteEdit.getText().toString();
                        break;
                }

                InputMethodManager inputManager =
                        (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(
                        this.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return false;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch(v.getId()) {
                case R.id.submitTaskButton:
                    complete = completeBox.isChecked();
                    longTerm = longTermBox.isChecked();

                    intent.putExtra("name", name);
                    intent.putExtra("dueDate", dueDate);
                    intent.putExtra("note", note);
                    intent.putExtra("complete", complete);
                    intent.putExtra("longTerm", longTerm);
                    setResult(RESULT_OK, intent);

                    finish();
                    break;

                case R.id.cancelButton:
                    intent.putExtra("name", savedValues.getString("name", " "));
                    intent.putExtra("dueDate", savedValues.getString("dueDate", " "));
                    intent.putExtra("note", savedValues.getString("note", " "));
                    intent.putExtra("complete", savedValues.getBoolean("complete", false));
                    intent.putExtra("longTerm", savedValues.getBoolean("complete", false));
                    setResult(RESULT_OK, intent);

                    finish();
                    break;

                case R.id.completeBox:
                    complete = completeBox.isChecked();
                    break;

                case R.id.longTermBox:
                    longTerm = longTermBox.isChecked();
                    break;
            }
        }}
