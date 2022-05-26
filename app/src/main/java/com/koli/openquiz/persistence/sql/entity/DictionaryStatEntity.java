package com.koli.openquiz.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity(foreignKeys = @ForeignKey(entity = DictionaryEntity.class, parentColumns = "id", childColumns = "dictionaryId", onDelete = ForeignKey.CASCADE))
public class DictionaryStatEntity {

    @NonNull
    @PrimaryKey
    private final UUID dictionaryId;
    private final long highestStreak;

    public DictionaryStatEntity(@NonNull UUID dictionaryId, long highestStreak) {
        this.dictionaryId = dictionaryId;
        this.highestStreak = highestStreak;
    }

    @NonNull
    public UUID getDictionaryId() {
        return dictionaryId;
    }

    public long getHighestStreak() {
        return highestStreak;
    }

}
