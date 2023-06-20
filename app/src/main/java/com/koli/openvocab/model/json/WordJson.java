package com.koli.openvocab.model.json;

import java.util.Objects;

public class WordJson {

    private final String query;
    private final String result;
    private final String queryLocale;
    private final String resultLocale;

    public WordJson(String query, String result, String queryLocale, String resultLocale) {
        this.query = query;
        this.result = result;
        this.queryLocale = queryLocale;
        this.resultLocale = resultLocale;
    }

    public String getQuery() {
        return query;
    }

    public String getResult() {
        return result;
    }

    public String getQueryLocale() {
        return queryLocale;
    }

    public String getResultLocale() {
        return resultLocale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WordJson wordJson = (WordJson) o;
        return query.equals(wordJson.query) && result.equals(wordJson.result) && queryLocale.equals(wordJson.queryLocale) && resultLocale.equals(wordJson.resultLocale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, result, queryLocale, resultLocale);
    }
}