package com.koli.openquiz.view.dictionary;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;

import com.koli.openquiz.R;
import com.koli.openquiz.service.QuestionProvider;
import com.koli.openquiz.view.adapter.WordListAdapter;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        String filename = getIntent().getStringExtra("DICTIONARY");
        QuestionProvider questionProvider = new QuestionProvider(this, filename);

        ListView listView = findViewById(R.id.word_list);
        WordListAdapter listAdapter = new WordListAdapter(this, questionProvider.getDictionary());

        listView.setAdapter(listAdapter);
    }
}