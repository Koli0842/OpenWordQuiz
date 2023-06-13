package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Update;

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WordEntity word);

    @Update
    void update(WordEntity word);

    @Insert
    void insert(List<WordEntity> words);

    @Delete
    void delete(WordEntity word);

}
