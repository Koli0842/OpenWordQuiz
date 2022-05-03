package com.koli.openquiz.view.dictionary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        ListView listView = (ListView) findViewById(R.id.word_list);
        WordListAdapter listAdapter = new WordListAdapter(this, questionProvider.getDictionary());

        listView.setAdapter(listAdapter);
    }
}