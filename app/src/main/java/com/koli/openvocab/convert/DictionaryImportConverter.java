package com.koli.openvocab.convert;

import androidx.annotation.NonNull;

import com.koli.openvocab.model.Dictionary;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;
import com.koli.openvocab.persistence.sql.entity.DictionaryWithWords;
import com.koli.openvocab.persistence.sql.entity.WordEntity;

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
