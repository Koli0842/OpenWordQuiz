package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.koli.openvocab.persistence.sql.entity.DictionaryWordMapping;

import java.util.List;

@Dao
public interface DictionaryWordMappingDao {

    @Insert
    void insert(DictionaryWordMapping... dictionaryWordMappings);

    @Insert
    void insert(List<DictionaryWordMapping> dictionaryWordMapping);

}
