package com.koli.openquiz.persistence.sql;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.koli.openquiz.persistence.sql.dao.DictionaryDao;
import com.koli.openquiz.persistence.sql.dao.DictionaryWordMappingDao;
import com.koli.openquiz.persistence.sql.dao.ScoreDao;
import com.koli.openquiz.persistence.sql.dao.WordDao;
import com.koli.openquiz.persistence.sql.entity.DictionaryEntity;
import com.koli.openquiz.persistence.sql.entity.DictionaryWordMapping;
import com.koli.openquiz.persistence.sql.entity.ScoreEntity;
import com.koli.openquiz.persistence.sql.entity.WordEntity;
import com.koli.openquiz.persistence.sql.repository.DictionaryWithWordsRepository;

@Database(entities = {WordEntity.class, DictionaryEntity.class, DictionaryWordMapping.class, ScoreEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase database;

    public abstract WordDao wordDao();
    public abstract DictionaryDao dictionaryDao();
    public abstract DictionaryWordMappingDao dictionaryWithWordsDao();
    public abstract ScoreDao scoreDao();

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
