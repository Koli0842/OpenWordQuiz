package com.koli.openquiz.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = WordEntity.class, parentColumns = "id", childColumns = "wordId", onDelete = ForeignKey.CASCADE))
public class ScoreEntity {

    @NonNull
    @PrimaryKey
    private final UUID wordId;
    private final long answeredTimes;
    private final long correctTimes;

    public ScoreEntity(@NonNull UUID wordId, long answeredTimes, long correctTimes) {
        this.wordId = wordId;
        this.answeredTimes = answeredTimes;
        this.correctTimes = correctTimes;
    }

    @NonNull
    public UUID getWordId() {
        return wordId;
    }

    public long getAnsweredTimes() {
        return answeredTimes;
    }

    public long getCorrectTimes() {
        return correctTimes;
    }
}
