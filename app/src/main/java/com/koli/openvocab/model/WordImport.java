package com.koli.openvocab.model;

public class WordImport {

    private final String query;
    private final String result;

    public WordImport(String query, String result) {
        this.query = query;
        this.result = result;
    }

    public String getQuery() {
        return query;
    }

    public String getResult() {
        return result;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o instanceof WordImport word)
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