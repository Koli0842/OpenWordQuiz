package com.koli.openquiz.model;

import java.util.UUID;

public class StreakCounter {

    private final UUID dictionaryId;
    private int currentStreak = 0;

    public StreakCounter(UUID dictionaryId) {
        this.dictionaryId = dictionaryId;
    }

    public void increment() {
        currentStreak++;
    }

    public void reset() {
        currentStreak = 0;
    }

    public UUID getDictionaryId() {
        return dictionaryId;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public String asString() {
        return String.valueOf(currentStreak);
    }
}