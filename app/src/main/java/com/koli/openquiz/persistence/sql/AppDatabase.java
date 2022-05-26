package com.koli.openquiz.persistence.sql;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.koli.openquiz.persistence.sql.dao.DictionaryDao;
import com.koli.openquiz.persistence.sql.dao.DictionaryWordStatViewDao;
import com.koli.openquiz.persistence.sql.dao.DictionaryStatsDao;
import com.koli.openquiz.persistence.sql.dao.DictionaryWordMappingDao;
import com.koli.openquiz.persistence.sql.dao.WordStatsDao;
import com.koli.openquiz.persistence.sql.dao.WordDao;
import com.koli.openquiz.persistence.sql.entity.DictionaryEntity;
import com.koli.openquiz.persistence.sql.entity.DictionaryStatEntity;
import com.koli.openquiz.persistence.sql.entity.DictionaryWordMapping;
import com.koli.openquiz.persistence.sql.entity.WordStatEntity;
import com.koli.openquiz.persistence.sql.entity.WordEntity;
import com.koli.openquiz.persistence.sql.repository.DictionaryWithWordsRepository;
import com.koli.openquiz.persistence.sql.view.DictionaryWordStatView;

@Database(entities = {WordEntity.class, DictionaryEntity.class, DictionaryWordMapping.class, WordStatEntity.class, DictionaryStatEntity.class}, views = {DictionaryWordStatView.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase database;

    public abstract WordDao wordDao();
    public abstract DictionaryDao dictionaryDao();
    public abstract DictionaryWordMappingDao dictionaryWithWordsDao();
    public abstract WordStatsDao scoreDao();
    public abstract DictionaryWordStatViewDao dictionaryWordStatViewDao();
    public abstract DictionaryStatsDao dictionaryStatsDao();

    public DictionaryWithWordsRepository dictionaryWithWordsRepository() {
        return new DictionaryWithWordsRepository(dictionaryDao(), wordDao(), dictionaryWithWordsDao());
    }

    public synchronized static AppDatabase getInstance(Context context) {
        if(database == null) {
            database = Room.databaseBuilder(context, AppDatabase.class, "barista-db").allowMainThreadQueries().build();
        }
        return database;
    }

}
