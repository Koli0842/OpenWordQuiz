package com.koli.openvocab.settings;

import com.koli.openvocab.R;

public enum QuizSettings implements SettingKey {

    WAIT_TIME("wait_time", R.string.default_wait_time, String.class),
    CHOICE_COUNT("choice_count", R.string.default_choice_count, String.class);

    private final String key;
    private final int defaultKey;
    private final Class<?> clazz;

    QuizSettings(String key, int defaultKey, Class<?> clazz) {
        this.key = key;
        this.defaultKey = defaultKey;
        this.clazz = clazz;
    }

    @Override
    public String preferenceKey() {
        return key;
    }

    @Override
    public int defaultKey() {
        return defaultKey;
    }

    @Override
    public Class<?> getClazz() {
        return clazz;
    }
}