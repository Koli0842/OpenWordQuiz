package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface DictionaryDao {

    UUID MAIN_ID = UUID.fromString("00000000-0000-0000-0000-000000000000");

    default void insertMain() {
        insert(new DictionaryEntity(MAIN_ID, "Everything", 0));
    }

    @Query("SELECT * FROM dictionaryentity WHERE id = :id")
    DictionaryEntity findById(UUID id);

    @Query("SELECT * FROM dictionaryentity ORDER BY ordinal")
    List<DictionaryEntity> findAll();

    @Insert
    void insert(DictionaryEntity dictionary);

}
