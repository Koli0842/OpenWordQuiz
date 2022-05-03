package com.koli.openquiz.model;

import android.content.Context;

import com.koli.openquiz.service.ScoreStorageService;

public class Score {

    private static final String HIGHEST_STREAK = "HighestStreak";
    private static final String ANSWERED = "Answered";
    private static final String CORRECT = "Correct";

    private ScoreStorageService service;

    private int highestStreak;
    private int answered;
    private int correct;

    private int currentStreak = 0;

    public Score(Context context) {
        this.service = new ScoreStorageService(context);

        highestStreak = service.read(HIGHEST_STREAK);
        answered = service.read(ANSWERED);
        correct = service.read(CORRECT);
    }

    public void addCorrect() {
        correct++;
        answered++;
        service.store(CORRECT, correct);
        service.store(ANSWERED, answered);
        increaseStreak();
    }

    public void addIncorrect() {
        answered++;
        service.store(ANSWERED, answered);
        currentStreak = 0;
    }

    private void increaseStreak() {
        currentStreak++;
        if(currentStreak > highestStreak)
            updateHighestStreak();
    }

    private void updateHighestStreak() {
        highestStreak = currentStreak;
        service.store(HIGHEST_STREAK, highestStreak);
    }

    public int getHighestStreak() {
        return highestStreak;
    }

    public int getAnswered() {
        return answered;
    }

    public int getCorrect() {
        return correct;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public double getAccuracy() {
        return Math.round((double)correct / answered * 100.0);
    }
}