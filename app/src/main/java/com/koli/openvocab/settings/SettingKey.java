package com.koli.openvocab.settings;

public interface SettingKey {

    String preferenceKey();

    int defaultKey();

    Class<?> getClazz();
}
