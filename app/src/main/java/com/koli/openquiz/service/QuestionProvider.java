package com.koli.openquiz.service;

import android.content.Context;

import com.koli.openquiz.model.Question;
import com.koli.openquiz.persistence.sql.entity.WordEntity;
import com.koli.openquiz.settings.QuizSettings;
import com.koli.openquiz.model.Word;
import com.koli.openquiz.settings.SettingsProvider;

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

    public QuestionProvider(Context context, List<WordEntity> words) {
        this.random = new Random();
        this.settings = new SettingsProvider(context);
        this.words = words;
    }

    public Question next() {
        final List<Word> choices = createDistinctChoices();
        return new Question(chooseQuestion(choices), choices);
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
        return choices.get(random.nextInt(choices.size()));
    }

    public List<WordEntity> getWords() {
        return words;
    }
}