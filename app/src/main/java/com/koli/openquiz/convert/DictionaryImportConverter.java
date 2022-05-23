package com.koli.openquiz.convert;

import androidx.annotation.NonNull;

import com.koli.openquiz.model.Dictionary;
import com.koli.openquiz.model.Word;
import com.koli.openquiz.persistence.sql.entity.DictionaryEntity;
import com.koli.openquiz.persistence.sql.entity.DictionaryWithWords;
import com.koli.openquiz.persistence.sql.entity.WordEntity;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DictionaryImportConverter {

    @NonNull
    public DictionaryWithWords toDatabaseEntity(@NonNull Dictionary dictionary) {
        List<WordEntity> wordEntities = dictionary.getDictionary().stream().map(this::toWordEntity).collect(Collectors.toList());
        return new DictionaryWithWords(toDictionaryEntity(dictionary), wordEntities);
    }

    private DictionaryEntity toDictionaryEntity(Dictionary dictionary) {
        return new DictionaryEntity(UUID.randomUUID(), dictionary.getName());
    }

    private WordEntity toWordEntity(Word word) {
        return new WordEntity(UUID.randomUUID(), word.getQuery(), word.getResult());
    }

}
