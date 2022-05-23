package com.koli.openquiz.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Insert;

import com.koli.openquiz.persistence.sql.entity.ScoreEntity;

@Dao
public interface ScoreDao {

    @Insert
    void insert(ScoreEntity score);

}
