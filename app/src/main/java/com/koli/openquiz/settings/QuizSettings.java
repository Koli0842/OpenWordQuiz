package com.koli.openquiz.settings;

import com.koli.openquiz.R;

public enum QuizSettings implements SettingKey {

    WAIT_TIME("wait_time", R.string.default_wait_time, Integer.class),
    CHOICE_COUNT("choice_count", R.string.default_choice_count, Integer.class);

    private final String key;
    private final int defaultKey;
    private final Class<Integer> clazz;

    QuizSettings(String key, int defaultKey, Class<Integer> clazz) {
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