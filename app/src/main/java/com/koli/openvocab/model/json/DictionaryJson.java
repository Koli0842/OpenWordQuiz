package com.koli.openvocab.model.json;

import java.util.List;

public class DictionaryJson {

    private String name;
    private int version;
    private List<WordJson> dictionary;

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

    public List<WordJson> getDictionary() {
        return dictionary;
    }

    public void setDictionary(final List<WordJson> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
            "name='" + name + '\'' +
            ", version=" + version +
            ", dictionary=" + dictionary +
            '}';
    }
}
