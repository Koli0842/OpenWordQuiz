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
    private final UUID id;
    private final String query;
    private final String result;

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
