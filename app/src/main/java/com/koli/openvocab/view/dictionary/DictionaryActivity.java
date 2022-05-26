package com.koli.openvocab.view.dictionary;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.service.QuestionProvider;
import com.koli.openvocab.view.adapter.WordListAdapter;

import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;

public class DictionaryActivity extends AppCompatActivity {

    private final Logger log = Logger.getLogger(DictionaryActivity.class.getName());
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.tts = new TextToSpeech(this, this::initTts);

        setContentView(R.layout.activity_dictionary);

        String filename = getIntent().getStringExtra("DICTIONARY");
        WordDao wordDao = AppDatabase.getInstance(this).wordDao();
        QuestionProvider questionProvider = new QuestionProvider(this, wordDao.findAllInDictionary(UUID.fromString(filename)));

        RecyclerView listView = findViewById(R.id.word_list);
        WordListAdapter listAdapter = new WordListAdapter(questionProvider.getWords(), view -> {
            log.info(view.toString());
            CharSequence text = view.getQuery().getText();
            log.info("Speaking " + text);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
        });

        listView.setAdapter(listAdapter);
    }

    private void initTts(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (tts.setLanguage(Locale.UK) == TextToSpeech.SUCCESS) {
                log.info("Initialized TTS");
            } else {
                log.info("Failed to set language");
            }
        } else {
            log.info("Failed to initialize TTS");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tts.shutdown();
    }
}