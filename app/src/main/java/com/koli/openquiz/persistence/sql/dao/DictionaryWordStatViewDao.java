package com.koli.openquiz.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.koli.openquiz.persistence.sql.view.DictionaryWordStatView;

import java.util.List;

@Dao
public interface DictionaryWordStatViewDao {

    @Query("SELECT * FROM dictionarywordstatview")
    List<DictionaryWordStatView> findAll();

}
