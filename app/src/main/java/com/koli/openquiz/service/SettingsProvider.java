package com.koli.openquiz.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.koli.openquiz.R;
import com.koli.openquiz.model.QuizSettings;

public class SettingsProvider {

    private Context context;
    private SharedPreferences preferences;

    public SettingsProvider(Context context) {
        this.context = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public int read(String key) {
        return Integer.parseInt(preferences.getString(key, getDefault(key)));
    }

    private String getDefault(String key) {
        switch(key) {
            case QuizSettings.CHOICE_COUNT: return context.getResources().getString(R.string.default_choice_count);
            case QuizSettings.WAIT_TIME: return context.getResources().getString(R.string.default_wait_time);
        }
        throw new RuntimeException();
    }
}
