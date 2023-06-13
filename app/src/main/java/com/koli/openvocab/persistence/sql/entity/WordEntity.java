package com.koli.openvocab.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;

@Entity
public class WordEntity {

    @NonNull
    @PrimaryKey
    private UUID id;
    private String query;
    private String result;

    public WordEntity(@NonNull UUID id, String query, String result) {
        this.id = id;
        this.query = query;
        this.result = result;
    }

    @NonNull
    public UUID getId() {
        return id;
    }

    public String getQuery() {
        return query;
    }

    public String getResult() {
        return result;
    }

    public void setId(@NonNull UUID id) {
        this.id = id;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordEntity that = (WordEntity) o;
        return id.equals(that.id) && Objects.equals(query, that.query) && Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, result);
    }
}
