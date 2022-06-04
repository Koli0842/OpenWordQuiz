package com.koli.openvocab.model;

import java.util.List;

public class DictionaryImport {

    private String name;
    private int version;
    private List<WordImport> dictionary;

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

    public List<WordImport> getDictionary() {
        return dictionary;
    }

    public void setDictionary(final List<WordImport> dictionary) {
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
