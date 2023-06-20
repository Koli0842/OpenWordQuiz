package com.koli.openvocab.convert;

import androidx.annotation.NonNull;

import com.koli.openvocab.model.json.DictionaryJson;
import com.koli.openvocab.model.json.WordJson;
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;
import com.koli.openvocab.persistence.sql.entity.DictionaryWithWords;
import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class DictionaryJsonConverter {

    @NonNull
    public DictionaryWithWords toDatabaseEntity(@NonNull DictionaryJson dictionary) {
        List<WordEntity> wordEntities = dictionary.getDictionary().stream().map(this::toWordEntity).collect(Collectors.toList());
        return new DictionaryWithWords(toDictionaryEntity(dictionary), wordEntities);
    }

    @NonNull
    public DictionaryJson fromDatabaseEntity(@NonNull DictionaryWithWords dictionary) {
        List<WordJson> wordJsons = dictionary.getWords().stream().map(this::fromWordEntity).collect(Collectors.toList());
        DictionaryJson dictionaryJson = fromDictionaryEntity(dictionary.getDictionary());
        dictionaryJson.setDictionary(wordJsons);
        return dictionaryJson;
    }

    private DictionaryEntity toDictionaryEntity(DictionaryJson dictionary) {
        return new DictionaryEntity(UUID.randomUUID(), dictionary.getName(), null);
    }

    public DictionaryJson fromDictionaryEntity(DictionaryEntity dictionary) {
        DictionaryJson dictionaryJson = new DictionaryJson();
        dictionaryJson.setName(dictionary.getName());
        dictionaryJson.setVersion(1);
        return dictionaryJson;
    }

    private WordEntity toWordEntity(WordJson word) {
        return new WordEntity(UUID.randomUUID(), word.getQuery(), word.getResult(), word.getQueryLocale(), word.getResultLocale());
    }

    private WordJson fromWordEntity(WordEntity word) {
        return new WordJson(word.getQuery(), word.getResult(), word.getQueryLocale(), word.getResultLocale());
    }

}
