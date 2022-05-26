package com.koli.openvocab.sync.domain;

import org.json.JSONException;
import org.json.JSONObject;

public class DictionaryInfo {
    private int id;
    private String name;
    private int version;

    public static DictionaryInfo createDictionary(final JSONObject jsonObject) throws JSONException {
        final DictionaryInfo dictionaryInfo = new DictionaryInfo();
        dictionaryInfo.setId(jsonObject.getInt("id"));
        dictionaryInfo.setName(jsonObject.getString("name"));
        dictionaryInfo.setVersion(jsonObject.getInt("version"));
        return dictionaryInfo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
