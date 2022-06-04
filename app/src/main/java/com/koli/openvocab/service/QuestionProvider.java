package com.koli.openvocab.service;

import android.content.Context;

import com.koli.openvocab.model.Question;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.entity.WordEntity;
import com.koli.openvocab.settings.QuizSettings;
import com.koli.openvocab.settings.SettingsProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class QuestionProvider {

    private final Logger log = Logger.getLogger(QuestionProvider.class.getName());

    private final List<WordEntity> words;
    private final SettingsProvider settings;
    private final Random random;

    private Question lastQuestion;

    public QuestionProvider(Context context, List<WordEntity> words) {
        this.random = new Random();
        this.settings = new SettingsProvider(context);
        this.words = words;
    }

    public Question next() {
        final List<Word> choices = createDistinctChoices();
        this.lastQuestion = new Question(chooseQuestion(choices), choices);
        return lastQuestion;
    }

    private List<Word> createDistinctChoices() {
        return Stream.generate(this::getRandomWord)
            .distinct()
            .limit(getPossibleChoiceCount())
            .map(wordEntity -> new Word(wordEntity.getId(), wordEntity.getQuery(), wordEntity.getResult()))
            .collect(Collectors.toList());
    }

    private WordEntity getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    private int getPossibleChoiceCount() {
        return Math.min(words.size(), settings.readInt(QuizSettings.CHOICE_COUNT));
    }

    private Word chooseQuestion(List<Word> choices) {
        List<Word> mutableChoices = new ArrayList<>(choices);
        Collections.shuffle(mutableChoices);
        return mutableChoices.stream()
            .filter(this::isNotSameAsLast)
            .findAny().orElseThrow(() -> new RuntimeException("Generated question has no data. Do you have a single word in a dictionary?"));
    }

    private boolean isNotSameAsLast(Word word) {
        return lastQuestion == null || !word.getQuery().equals(lastQuestion.getWord().getQuery());
    }

    public List<WordEntity> getWords() {
        return words;
    }
}