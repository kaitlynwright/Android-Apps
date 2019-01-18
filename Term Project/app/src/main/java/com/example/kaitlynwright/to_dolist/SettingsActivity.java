package com.example.kaitlynwright.to_dolist;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Window;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
