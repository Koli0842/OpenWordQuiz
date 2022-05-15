package com.koli.openquiz.model;

import java.util.List;
import java.util.Random;

public class Dictionary {

    private String name;
    private int version;
    private List<Word> dictionary;

    public Word getRandomWord() {
        final Random random = new Random();
        return dictionary.get(random.nextInt(dictionary.size()));
    }

    public int size() {
        return dictionary.size();
    }

    public Word get(final int index) {
        return dictionary.get(index);
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
