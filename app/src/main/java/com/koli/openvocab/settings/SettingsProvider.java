package com.koli.openvocab.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SettingsProvider {

    private final Context context;
    private final SharedPreferences preferences;

    public SettingsProvider(Context context) {
        this.context = context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public <T> T read(SettingKey setting, Class<T> clazz) {
        return clazz.cast(preferences.getAll().get(setting.preferenceKey()));
    }

    public int readInt(SettingKey setting) {
        return Integer.parseInt(readString(setting));
    }

    public boolean readBoolean(SettingKey setting) {
        return preferences.getBoolean(setting.preferenceKey(),
            context.getResources().getBoolean(setting.defaultKey()));
    }

    private String readString(SettingKey setting) {
        return preferences.getString(setting.preferenceKey(),
            context.getResources().getString(setting.defaultKey()));
    }

}
