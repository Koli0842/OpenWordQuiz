package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;

import java.util.List;

@Dao
public interface DictionaryDao {

    @Query("SELECT * FROM dictionaryentity")
    List<DictionaryEntity> findAll();

    @Insert
    void insert(DictionaryEntity dictionary);

}
