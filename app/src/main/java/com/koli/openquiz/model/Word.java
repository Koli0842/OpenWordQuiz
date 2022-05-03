package com.koli.openquiz.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Word {

    public static final String QUERY_KEY = "Query";
    public static final String RESULT_KEY = "Result";

    private String query;
    private String result;

    public Word(final String query, final String result) {
        this.query = query;
        this.result = result;
    }

    public static Word createWord(final JSONObject jsonObject) throws JSONException {
        final String query = jsonObject.getString(QUERY_KEY);
        final String result = jsonObject.getString(RESULT_KEY);
        return new Word(query, result);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(QUERY_KEY, query);
            jsonObject.put(RESULT_KEY, result);
        } catch (JSONException e) {
            jsonObject = null;
        }
        return jsonObject;
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
        if (!(o instanceof Word))
            return false;

        Word word = (Word) o;

        return word.query.equals(query) || word.result.equals(result);
    }

    @Override
    public int hashCode() {
        int result1 = query.hashCode();
        result1 = 31 * result1 + result.hashCode();
        return result1;
    }

}