package com.koli.openquiz.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import java.util.UUID;

@Entity(primaryKeys = {"dictionaryId", "wordId"})
public class DictionaryWordMapping {

    @NonNull
    private final UUID dictionaryId;
    @NonNull
    private final UUID wordId;

    public DictionaryWordMapping(@NonNull UUID dictionaryId, @NonNull UUID wordId) {
        this.dictionaryId = dictionaryId;
        this.wordId = wordId;
    }

    @NonNull
    public UUID getDictionaryId() {
        return dictionaryId;
    }

    @NonNull
    public UUID getWordId() {
        return wordId;
    }
}