package com.koli.openquiz.persistence.sql.entity;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class DictionaryWithWords {

    @Embedded
    private final DictionaryEntity dictionary;

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = @Junction(DictionaryWordMapping.class))
    private final List<WordEntity> words;

    public DictionaryWithWords(DictionaryEntity dictionary, List<WordEntity> words) {
        this.dictionary = dictionary;
        this.words = words;
    }

    public DictionaryEntity getDictionary() {
        return dictionary;
    }

    public List<WordEntity> getWords() {
        return words;
    }
}
