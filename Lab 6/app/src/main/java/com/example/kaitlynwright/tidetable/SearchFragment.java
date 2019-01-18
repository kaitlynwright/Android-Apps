package com.example.kaitlynwright.tidetable;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView.OnEditorActionListener;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchFragment extends Fragment
        implements OnClickListener, OnEditorActionListener {
    private String location;
    private String date;
    TideItems items;

    private SearchActivity activity = (SearchActivity) getActivity();

    //widgets
    private EditText locationEdit;
    private EditText dateEdit;
    private Button showTides;

    private SharedPreferences savedValues;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //activity.setContentView(R.layout.home_page);

        savedValues = getActivity().getSharedPreferences("savedValues", Context.MODE_PRIVATE);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment,
                container, false);

        //get widget references
        locationEdit = (EditText) view.findViewById(R.id.locationEdit);
        dateEdit = (EditText) view.findViewById(R.id.dateEdit);
        showTides = (Button) view.findViewById(R.id.ShowTides);

        locationEdit.setOnEditorActionListener(this);
        dateEdit.setOnEditorActionListener(this);
        showTides.setOnClickListener(this);

        return view;
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
        location = savedValues.getString("location", "Coos Bay");
        date = savedValues.getString("date", "2018/01/01");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // get references for host activity
        activity = (SearchActivity) getActivity();
    }


    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            switch (v.getId()) {
                case R.id.locationEdit:
                    location = locationEdit.getText().toString();
                    break;
                case R.id.dateEdit:
                    date = dateEdit.getText().toString();
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
                startActivity(new Intent(getActivity(), TideActivity.class));
                break;
        }
    }
}
