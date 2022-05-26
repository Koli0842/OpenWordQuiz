package com.koli.openvocab.persistence.sql.view;

import androidx.room.DatabaseView;

import java.util.UUID;

@DatabaseView("SELECT dictionaryEntity.id AS dictionaryId," +
    "dictionaryEntity.name AS dictionaryName," +
    "SUM(WordStatEntity.answeredCount) AS answeredCount," +
    "SUM(WordStatEntity.correctCount) AS correctCount " +

    "FROM dictionaryentity " +
    "INNER JOIN dictionarywordmapping AS dictionariesWithWord ON dictionaryEntity.id = dictionariesWithWord.dictionaryId " +
    "LEFT JOIN WordStatEntity ON dictionariesWithWord.wordId = WordStatEntity.wordId " +
    "GROUP BY dictionaryEntity.id")
public class DictionaryWordStatView {

    private final UUID dictionaryId;
    private final String dictionaryName;
    private final long answeredCount;
    private final long correctCount;

    public DictionaryWordStatView(UUID dictionaryId, String dictionaryName, long answeredCount, long correctCount) {
        this.dictionaryId = dictionaryId;
        this.dictionaryName = dictionaryName;
        this.answeredCount = answeredCount;
        this.correctCount = correctCount;
    }

    public UUID getDictionaryId() {
        return dictionaryId;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public long getAnsweredCount() {
        return answeredCount;
    }

    public long getCorrectCount() {
        return correctCount;
    }
}
