package com.koli.openquiz.service;

import android.content.Context;

import com.koli.openquiz.model.Dictionary;
import com.koli.openquiz.model.Question;
import com.koli.openquiz.settings.QuizSettings;
import com.koli.openquiz.model.Word;
import com.koli.openquiz.model.exception.InvalidDictionaryException;
import com.koli.openquiz.persistence.DictionaryStore;
import com.koli.openquiz.settings.SettingsProvider;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class QuestionProvider {

    private final Logger log = Logger.getLogger(QuestionProvider.class.getName());

    private final Dictionary dictionary;
    private final SettingsProvider settings;
    private final Random random;

    public QuestionProvider(Context context, String filename) {
        this.random = new Random();
        this.settings = new SettingsProvider(context);
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<Dictionary> adapter = moshi.adapter(Dictionary.class);

        final DictionaryStore service = new DictionaryStore(context);

        try {
            String fileContent = service.read(filename);
            this.dictionary = adapter.lenient().fromJson(fileContent);
            log.info(String.format("Initialized with dictionary: %s", dictionary));
        } catch (IOException e) {
            throw new InvalidDictionaryException(e);
        }
    }

    public Question next() {
        final List<Word> choices = createDistinctChoices();
        return new Question(getRandomWord(choices), choices);
    }

    private List<Word> createDistinctChoices() {
        final List<Word> choices = new ArrayList<>();

        while (choices.size() < getPossibleChoiceCount()) {
            final Word word = dictionary.getRandomWord();
            if (!choices.contains(word)) {
                choices.add(word);
            }
        }
        return choices;
    }

    private int getPossibleChoiceCount() {
        return Math.min(dictionary.size(), settings.readInt(QuizSettings.CHOICE_COUNT));
    }

    private Word getRandomWord(List<Word> choices) {
        return choices.get(random.nextInt(choices.size()));
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

}