package com.koli.openvocab.persistence.sql.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.koli.openvocab.persistence.sql.view.DictionaryWordStatView;

import java.util.List;

@Dao
public interface DictionaryWordStatViewDao {

    @Query("SELECT * FROM dictionarywordstatview")
    List<DictionaryWordStatView> findAll();

}
