package com.koli.openquiz.settings;

public interface SettingKey {

    String preferenceKey();

    int defaultKey();

    Class<?> getClazz();
}
