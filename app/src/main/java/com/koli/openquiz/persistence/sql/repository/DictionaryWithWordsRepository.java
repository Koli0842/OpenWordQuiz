package com.koli.openquiz.persistence.sql.repository;

import com.koli.openquiz.persistence.sql.dao.DictionaryDao;
import com.koli.openquiz.persistence.sql.dao.DictionaryWordMappingDao;
import com.koli.openquiz.persistence.sql.dao.WordDao;
import com.koli.openquiz.persistence.sql.entity.DictionaryWithWords;
import com.koli.openquiz.persistence.sql.entity.DictionaryWordMapping;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DictionaryWithWordsRepository {

    private final DictionaryDao dictionaryDao;
    private final WordDao wordDao;
    private final DictionaryWordMappingDao dictionaryWordMappingDao;

    public DictionaryWithWordsRepository(DictionaryDao dictionaryDao, WordDao wordDao, DictionaryWordMappingDao dictionaryWordMappingDao) {
        this.dictionaryDao = dictionaryDao;
        this.wordDao = wordDao;
        this.dictionaryWordMappingDao = dictionaryWordMappingDao;
    }

    public void insert(DictionaryWithWords dictionaryWithWords) {
        wordDao.insert(dictionaryWithWords.getWords());
        dictionaryDao.insert(dictionaryWithWords.getDictionary());
        dictionaryWordMappingDao.insert(createMappings(dictionaryWithWords));
    }

    private List<DictionaryWordMapping> createMappings(DictionaryWithWords dictionaryWithWords) {
        UUID dictionaryId = dictionaryWithWords.getDictionary().getId();
        return dictionaryWithWords.getWords().stream()
            .map(word -> new DictionaryWordMapping(dictionaryId, word.getId()))
            .collect(Collectors.toList());
    }

}
