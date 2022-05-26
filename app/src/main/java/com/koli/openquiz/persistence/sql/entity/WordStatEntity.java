package com.koli.openquiz.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = WordEntity.class, parentColumns = "id", childColumns = "wordId", onDelete = ForeignKey.CASCADE))
public class WordStatEntity {

    @NonNull
    @PrimaryKey
    private final UUID wordId;
    private final long answeredCount;
    private final long correctCount;

    public WordStatEntity(@NonNull UUID wordId, long answeredCount, long correctCount) {
        this.wordId = wordId;
        this.answeredCount = answeredCount;
        this.correctCount = correctCount;
    }

    @NonNull
    public UUID getWordId() {
        return wordId;
    }

    public long getAnsweredCount() {
        return answeredCount;
    }

    public long getCorrectCount() {
        return correctCount;
    }
}
