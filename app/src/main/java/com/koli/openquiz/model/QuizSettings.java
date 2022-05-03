package com.koli.openquiz.model;

import android.content.Context;

import com.koli.openquiz.service.SettingsProvider;

public class QuizSettings {

    public static final String WAIT_TIME = "wait_time";
    public static final String CHOICE_COUNT = "choice_count";

    private SettingsProvider service;

    private int waitTime;
    private int choiceCount;

    public QuizSettings(Context context) {
        this.service = new SettingsProvider(context);

        this.waitTime = service.read(WAIT_TIME);
        this.choiceCount = service.read(CHOICE_COUNT);
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getChoiceCount() {
        return choiceCount;
    }
}
