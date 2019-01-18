package com.example.kaitlynwright.clickcountappliation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.lang.String;
import java.lang.Integer;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ClickCountActivity extends AppCompatActivity implements OnClickListener {

    private Button AddOne;
    private Button Reset;
    private TextView Count;

    private Integer numCount = 0;

    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_count);

        //references to widgets
        AddOne = (Button) findViewById(R.id.AddOne);
        Reset = (Button) findViewById(R.id.Reset);
        Count = (TextView) findViewById(R.id.Count);

        //set up listeners
        AddOne.setOnClickListener(this);
        Reset.setOnClickListener(this);

        //get SharedPreferences object
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);

        Count.setText(String.valueOf(numCount));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.AddOne:
                numCount += 1;
                Count.setText(String.valueOf(numCount));
                break;
            case R.id.Reset:
                numCount = 0;
                Count.setText(String.valueOf(numCount));
                break;
        }
    }

    @Override
    public void onPause() {
        Editor editor = savedValues.edit();
        editor.putString("numCount", String.valueOf(numCount));
        editor.apply();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        //get instance variables
        numCount = Integer.parseInt(savedValues.getString("numCount", "0"));

        //set count on widget
        Count.setText(String.valueOf(numCount));
    }
}