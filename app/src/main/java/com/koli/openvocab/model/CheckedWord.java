package com.koli.openvocab.model;

import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class CheckedWord {

    private final Word word;
    private boolean checked;


    public CheckedWord(Word word) {
        this(word, false);
    }

    public CheckedWord(Word word, boolean checked) {
        this.word = word;
        this.checked = checked;
    }

    public Word getWord() {
        return word;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public WordEntity toWordEntity() {
        return new WordEntity(word.getId(), word.getQuery(), word.getResult(), word.getQueryLocale().toLanguageTag(), word.getResultLocale().toLanguageTag());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckedWord that = (CheckedWord) o;
        return checked == that.checked && word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, checked);
    }
}