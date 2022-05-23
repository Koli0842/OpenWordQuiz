package com.koli.openquiz.model;

import java.util.List;
import java.util.Random;

public class Dictionary {

    private String name;
    private int version;
    private List<Word> dictionary;

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

    public List<Word> getDictionary() {
        return dictionary;
    }

    public void setDictionary(final List<Word> dictionary) {
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
