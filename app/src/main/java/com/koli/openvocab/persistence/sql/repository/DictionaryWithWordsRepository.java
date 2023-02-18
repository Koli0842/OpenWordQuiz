package com.koli.openvocab.persistence.sql.repository;

import com.koli.openvocab.persistence.sql.dao.DictionaryDao;
import com.koli.openvocab.persistence.sql.dao.DictionaryWordMappingDao;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.persistence.sql.entity.DictionaryWithWords;
import com.koli.openvocab.persistence.sql.entity.DictionaryWordMapping;
import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.Collection;
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

    public void insertMainIfNotPresent() {
        if (dictionaryDao.findById(DictionaryDao.MAIN_ID) == null) {
            dictionaryDao.insertMain();
            dictionaryWordMappingDao.insert(createMainMappings(wordDao.findAll()));
        }
    }

    public void insert(DictionaryWithWords dictionaryWithWords) {
        wordDao.insert(dictionaryWithWords.getWords());
        dictionaryDao.insert(dictionaryWithWords.getDictionary());
        dictionaryWordMappingDao.insert(createMappings(dictionaryWithWords));
        dictionaryWordMappingDao.insert(createMainMappings(dictionaryWithWords.getWords()));
    }

    private List<DictionaryWordMapping> createMappings(DictionaryWithWords dictionaryWithWords) {
        UUID dictionaryId = dictionaryWithWords.getDictionary().getId();
        return dictionaryWithWords.getWords().stream()
            .map(word -> new DictionaryWordMapping(dictionaryId, word.getId()))
            .collect(Collectors.toList());
    }

    private List<DictionaryWordMapping> createMainMappings(Collection<WordEntity> words) {
        return words.stream()
            .map(word -> new DictionaryWordMapping(DictionaryDao.MAIN_ID, word.getId()))
            .collect(Collectors.toList());
    }

}
