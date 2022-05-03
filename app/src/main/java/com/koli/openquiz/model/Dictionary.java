package com.koli.openquiz.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Dictionary {

    private String name;
    private int version;
    private List<Word> content;

    public static Dictionary createDictionary(final JSONObject jsonObject) {
        try {
            final Dictionary dictionary = new Dictionary();
            dictionary.setName(jsonObject.getString("name"));
            dictionary.setVersion(jsonObject.getInt("version"));
            dictionary.setContent(parseContent(new JSONArray(jsonObject.getString("dictionary"))));
            return dictionary;
        } catch (JSONException e) {
            throw new RuntimeException();
        }
    }

    public static Dictionary createDictionary(final JSONArray jsonArray) throws JSONException {
        final Dictionary dictionary = new Dictionary();
        dictionary.setName("Unknown");
        dictionary.setVersion(1);
        dictionary.setContent(parseContent(jsonArray));
        return dictionary;
    }

    private static List<Word> parseContent(final JSONArray jsonArray) throws JSONException {
        final List<Word> content = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            final JSONObject jsonWordObject = jsonArray.getJSONObject(i);
            content.add(Word.createWord(jsonWordObject));
        }
        return content;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("version", version);
            jsonObject.put("dictionary", convertContent());
        } catch (JSONException e) {
            jsonObject = null;
        }
        return jsonObject;
    }

    private JSONArray convertContent() {
        JSONArray jsonArray = new JSONArray();
        for(Word word : content) {
            jsonArray.put(word.toJson());
        }
        return jsonArray;
    }

    public Word getRandomWord() {
        final Random random = new Random();
        return content.get(random.nextInt(content.size()));
    }

    public int size() {
        return content.size();
    }

    public Word get(final int index) {
        return content.get(index);
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<Word> getContent() {
        return content;
    }

    public void setContent(final List<Word> content) {
        this.content = content;
    }

}
