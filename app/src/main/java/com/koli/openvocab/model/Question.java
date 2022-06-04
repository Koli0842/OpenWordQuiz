package com.koli.openvocab.model;

import java.util.List;

public class Question {

    private final Word word;
    private final List<Word> choices;

    private boolean answered;
    private boolean correct;

    public Question(Word word, List<Word> choices) {
        this.word = word;
        this.choices = choices;
        this.answered = false;
    }

    public void answer(Word chosenWord) {
        answered = true;
        correct = word.getQuery().equalsIgnoreCase(chosenWord.getQuery()) || word.getResult().equalsIgnoreCase(chosenWord.getResult());
    }

    public Word getWord() {
        return word;
    }

    public List<Word> getChoices() {
        return choices;
    }

    public boolean isAnswered() {
        return answered;
    }

    public boolean isCorrect() {
        return correct;
    }
}
