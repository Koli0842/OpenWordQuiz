package com.koli.openvocab.model;

import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class Word {

    private final UUID id;
    private final String query;
    private final String result;
    private final Locale queryLocale;
    private final Locale resultLocale;

    public Word(UUID id, String query, String result, Locale queryLocale, Locale resultLocale) {
        this.id = id;
        this.query = query;
        this.result = result;
        this.queryLocale = queryLocale;
        this.resultLocale = resultLocale;
    }

    public static Word fromEntity(WordEntity wordEntity) {
        return new Word(wordEntity.getId(), wordEntity.getQuery(), wordEntity.getResult(), Locale.forLanguageTag(wordEntity.getQueryLocale()), Locale.forLanguageTag(wordEntity.getResultLocale()));
    }

    public WordEntity toEntity() {
        return new WordEntity(id, query, result, queryLocale.toLanguageTag(), resultLocale.toLanguageTag());
    }

    public UUID getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public String getResult() {
        return result;
    }

    public Locale getQueryLocale() {
        return queryLocale;
    }

    public Locale getResultLocale() {
        return resultLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return id.equals(word.id) && query.equals(word.query) && result.equals(word.result) && queryLocale.equals(word.queryLocale) && resultLocale.equals(word.resultLocale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, result, queryLocale, resultLocale);
    }
}