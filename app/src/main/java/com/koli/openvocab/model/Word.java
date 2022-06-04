package com.koli.openvocab.model;

import java.util.Objects;
import java.util.UUID;

public class Word {

    private final UUID id;
    private final String query;
    private final String result;

    public Word(UUID id, String query, String result) {
        this.id = id;
        this.query = query;
        this.result = result;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return Objects.equals(id, word.id) && Objects.equals(query, word.query) && Objects.equals(result, word.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, result);
    }
}