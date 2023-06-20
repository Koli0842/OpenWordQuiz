package com.koli.openvocab.view.dictionary;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.view.adapter.WordListAdapter;

import java.util.Locale;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DictionaryActivity extends AppCompatActivity {

    private final Logger log = Logger.getLogger(DictionaryActivity.class.getName());
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        this.tts = new TextToSpeech(this, this::initTts);

        String dictionary = getIntent().getStringExtra("DICTIONARY");
        UUID dictionaryId = dictionary == null ? null : UUID.fromString(dictionary);
        WordDao wordDao = AppDatabase.getInstance(this).wordDao();

        RecyclerView listView = findViewById(R.id.word_list);
        WordListAdapter listAdapter = new WordListAdapter(view -> {
            tts.setLanguage(view.getEntity().getQueryLocale());
            tts.speak(view.getEntity().getQuery(), TextToSpeech.QUEUE_FLUSH, null, UUID.randomUUID().toString());
        }, (item, v) -> {});
        listAdapter.submitList(wordDao.findAllInDictionary(dictionaryId).stream().map(Word::fromEntity).collect(Collectors.toList()));

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