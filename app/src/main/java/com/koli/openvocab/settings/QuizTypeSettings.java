package com.koli.openvocab.settings;

import com.koli.openvocab.R;

public enum QuizTypeSettings implements SettingKey {

    TEXTUAL_ENABLED("quiz_textual_enabled", R.bool.default_quiz_textual_enabled, Boolean.class),
    VERBAL_ENABLED("quiz_verbal_enabled", R.bool.default_quiz_verbal_enabled, Boolean.class);

    private final String key;
    private final int defaultKey;
    private final Class<?> clazz;

    QuizTypeSettings(String key, int defaultKey, Class<?> clazz) {
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