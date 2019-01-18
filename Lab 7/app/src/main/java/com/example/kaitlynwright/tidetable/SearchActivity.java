package com.example.kaitlynwright.tidetable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity
        implements OnClickListener, OnEditorActionListener {
    private String location;
    private String date;
    TideItems items;

    //widgets
    private EditText locationEdit;
    private EditText dateEdit;
    private Button showTides;

    private SharedPreferences savedValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //get widget references
        locationEdit = (EditText) findViewById(R.id.locationEdit);
        dateEdit = (EditText) findViewById(R.id.dateEdit);
        showTides = (Button) findViewById(R.id.ShowTides);

        locationEdit.setOnEditorActionListener(this);
        dateEdit.setOnEditorActionListener(this);
        showTides.setOnClickListener(this);

        savedValues = this.getSharedPreferences("savedValues", Context.MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putString("location", location);
        editor.putString("date", date);

        editor.apply();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        // get preferences
        //location = savedValues.getString("location", "Coos Bay");
        //date = savedValues.getString("date", "2018/01/01");
    }

    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            SharedPreferences.Editor editor = savedValues.edit();

            switch (v.getId()) {
                case R.id.locationEdit:
                    location = locationEdit.getText().toString();
                    editor.putString("location", location);
                    editor.apply();
                    break;
                case R.id.dateEdit:
                    date = dateEdit.getText().toString();
                    editor.putString("date", date);
                    editor.apply();
                    break;
            }

            InputMethodManager imm = (InputMethodManager) v.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm == null) throw new AssertionError();
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ShowTides:
                if(!date.substring(0, 4).equals("2018")) {
                    String toast = String.format("Only 2018 dates available.\nYear entered: %s",
                            date.substring(0, 4));
                    Toast.makeText(this, toast,
                            Toast.LENGTH_LONG).show();
                } else if(!location.equals("Coos Bay") &&
                        !location.equals("Depoe Bay") &&
                        !location.equals("Cape Disappointment")) {
                    String toast = String.format("Only listed locations available." +
                                    "\nLocation entered: %s",
                                    location);
                    Toast.makeText(this, toast,
                            Toast.LENGTH_LONG).show();
                } else {
                    startActivity(new Intent(SearchActivity.this,
                            TideActivity.class));
                }
                break;
        }
    }

}
