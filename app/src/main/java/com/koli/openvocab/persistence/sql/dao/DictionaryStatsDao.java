package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.koli.openvocab.persistence.sql.entity.DictionaryStatEntity;

import java.util.List;
import java.util.UUID;

@Dao
public interface DictionaryStatsDao {

    @Query("SELECT * FROM dictionarystatentity")
    List<DictionaryStatEntity> findAll();

    @Query("SELECT * FROM dictionarystatentity WHERE dictionaryId = :dictionaryId")
    DictionaryStatEntity find(UUID dictionaryId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DictionaryStatEntity dictionaryStat);

    default void updateStreakWhenHigher(UUID dictionaryId, long streak) {
        DictionaryStatEntity dictionaryStatEntity = find(dictionaryId);
        if (dictionaryStatEntity == null || dictionaryStatEntity.getHighestStreak() < streak) {
            insert(new DictionaryStatEntity(dictionaryId, streak));
        }
    }

}
