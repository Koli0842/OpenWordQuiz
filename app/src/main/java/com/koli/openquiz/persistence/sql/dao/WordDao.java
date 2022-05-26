package com.koli.openquiz.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.koli.openquiz.persistence.sql.entity.WordEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface WordDao {

    @Query("SELECT * FROM wordentity INNER JOIN dictionarywordmapping AS dictionariesWithWord ON dictionariesWithWord.wordId == wordEntity.id WHERE dictionariesWithWord.dictionaryId = :dictionaryId")
    List<WordEntity> findAllInDictionary(UUID dictionaryId);

    @Insert
    void insert(WordEntity word);

    @Insert
    void insert(List<WordEntity> words);

}
