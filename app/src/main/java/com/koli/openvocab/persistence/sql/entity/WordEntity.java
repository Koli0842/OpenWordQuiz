package com.koli.openvocab.persistence.sql.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Entity
public class WordEntity {

    @NonNull
    @PrimaryKey
    private UUID id;
    private String query;
    private String result;
    @ColumnInfo(defaultValue = "en-GB")
    private String queryLocale;
    @ColumnInfo(defaultValue = "hu-HU")
    private String resultLocale;


    public WordEntity(@NonNull UUID id, String query, String result, String queryLocale, String resultLocale) {
        this.id = id;
        this.query = query;
        this.result = result;
        this.queryLocale = queryLocale;
        this.resultLocale = resultLocale;
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

    public String getQueryLocale() {
        return queryLocale;
    }

    public void setQueryLocale(String queryLocale) {
        this.queryLocale = queryLocale;
    }

    public String getResultLocale() {
        return resultLocale;
    }

    public void setResultLocale(String resultLocale) {
        this.resultLocale = resultLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordEntity that = (WordEntity) o;
        return id.equals(that.id) && query.equals(that.query) && result.equals(that.result) && queryLocale.equals(that.queryLocale) && resultLocale.equals(that.resultLocale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, result, queryLocale, resultLocale);
    }
}
