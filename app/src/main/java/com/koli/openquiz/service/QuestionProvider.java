package com.koli.openquiz.service;

import android.content.Context;

import com.koli.openquiz.model.Dictionary;
import com.koli.openquiz.model.Question;
import com.koli.openquiz.model.QuizSettings;
import com.koli.openquiz.model.Word;
import com.koli.openquiz.model.exception.InvalidDictionaryException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuestionProvider {

    private Dictionary dictionary;
    private QuizSettings settings;
    private Random random;

    public QuestionProvider(Context context, String filename) {
        this.random = new Random();
        this.settings = new QuizSettings(context);

        final DictionaryStorageService service = new DictionaryStorageService(context);
        try {
            final JSONObject jsonObject = new JSONObject(service.read(filename));
            this.dictionary = Dictionary.createDictionary(jsonObject);
        } catch (JSONException e) {
            throw new InvalidDictionaryException();
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
        return (dictionary.size() < settings.getChoiceCount()) ? dictionary.size() : settings.getChoiceCount();
    }

    private Word getRandomWord(List<Word> choices) {
        return choices.get(random.nextInt(choices.size()));
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

}