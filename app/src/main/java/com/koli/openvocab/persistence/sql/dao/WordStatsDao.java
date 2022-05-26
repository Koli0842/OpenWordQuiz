package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.koli.openvocab.persistence.sql.entity.WordStatEntity;

import java.util.UUID;

@Dao
public interface WordStatsDao {

    @Query("UPDATE WordStatEntity SET answeredCount = answeredCount + 1, correctCount = correctCount + 1 WHERE wordId = :wordId")
    void incrementCorrectAndAnswered(UUID wordId);

    @Query("UPDATE WordStatEntity SET answeredCount = answeredCount + 1 WHERE wordId = :wordId")
    void incrementAnswered(UUID wordId);

    @Query("SELECT * FROM WordStatEntity WHERE wordId = :wordId")
    WordStatEntity find(UUID wordId);

    @Insert
    void insert(WordStatEntity score);

    default void addCorrect(UUID wordId) {
        if (find(wordId) == null) {
           insert(new WordStatEntity(wordId, 1, 1));
        } else {
            incrementCorrectAndAnswered(wordId);
        }
    }

    default void addIncorrect(UUID wordId) {
        if (find(wordId) == null) {
            insert(new WordStatEntity(wordId, 1, 0));
        } else {
            incrementAnswered(wordId);
        }
    }

}
