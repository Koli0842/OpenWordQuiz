package com.koli.openquiz.model;

import com.squareup.moshi.Json;

public class Word {

    @Json(name = "Query")
    private String query;
    @Json(name = "Result")
    private String result;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o instanceof Word word)
            return word.query.equals(query) || word.result.equals(result);

        return false;
    }

    @Override
    public int hashCode() {
        int result1 = query.hashCode();
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

}