package com.koli.openquiz.view.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.koli.openquiz.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.settings_layout, new SettingsFragment()).commit();
        }
    }

}
