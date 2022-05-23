package com.koli.openquiz.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Transaction;

import com.koli.openquiz.persistence.sql.entity.DictionaryEntity;
import com.koli.openquiz.persistence.sql.entity.DictionaryWithWords;
import com.koli.openquiz.persistence.sql.entity.DictionaryWordMapping;

import java.util.List;

@Dao
public interface DictionaryWordMappingDao {

    @Insert
    void insert(DictionaryWordMapping... dictionaryWordMappings);

    @Insert
    void insert(List<DictionaryWordMapping> dictionaryWordMapping);

}
