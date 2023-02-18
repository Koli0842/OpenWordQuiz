package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;

import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface WordDao {

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM wordentity INNER JOIN dictionarywordmapping AS dictionariesWithWord ON dictionariesWithWord.wordId == wordEntity.id WHERE dictionariesWithWord.dictionaryId = :dictionaryId")
    List<WordEntity> findAllInDictionary(UUID dictionaryId);

    @Query("SELECT * FROM wordentity")
    List<WordEntity> findAll();

    @Insert
    void insert(WordEntity word);

    @Insert
    void insert(List<WordEntity> words);

}
