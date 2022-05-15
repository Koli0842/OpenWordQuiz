package com.koli.openquiz.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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

    private String readString(SettingKey setting) {
        return preferences.getString(setting.preferenceKey(),
            context.getResources().getString(setting.defaultKey()));
    }

}
