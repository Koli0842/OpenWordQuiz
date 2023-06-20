package com.koli.openvocab.view.settings.words;

import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koli.openvocab.R;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.persistence.sql.entity.WordEntity;
import com.koli.openvocab.view.adapter.WordListAdapter;

import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

public class ManageWordListFragment extends Fragment {

    private Context context;
    private WordDao wordDao;

    private RecyclerView wordList;
    private WordListAdapter wordListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Words");
        return inflater.inflate(R.layout.fragment_manage_words, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        wordList = view.findViewById(R.id.dictionary_list);
        wordList.setLayoutManager(new LinearLayoutManager(context));
        wordListAdapter = new WordListAdapter(v -> {
            SaveWordFragment saveWordFragment = new SaveWordFragment(this::saveWord, v.getEntity().toEntity());
            saveWordFragment.show(getParentFragmentManager(), "saveWordDialog");
        }, this::createListItemContextMenu);
        wordList.setAdapter(wordListAdapter);
        wordListAdapter.submitList(wordDao.findAll().stream().map(Word::fromEntity).collect(Collectors.toList()));

        FloatingActionButton addButton = view.findViewById(R.id.fab_add_word);
        addButton.setOnClickListener(v -> {
            SaveWordFragment saveWordFragment = new SaveWordFragment(this::insertWord, new WordEntity(UUID.randomUUID(), null, null, Locale.UK.getLanguage(), Locale.forLanguageTag("hu").getLanguage()));
            saveWordFragment.show(getParentFragmentManager(), "addWordDialog");
        });
    }

    private void createListItemContextMenu(ContextMenu menu, WordListAdapter.ViewHolder v) {
        menu.add(0, 0, 0, "Delete").setOnMenuItemClickListener(item -> {
            deleteWord(v);
            return true;
        });
    }

    private void insertWord(WordEntity wordEntity) {
        wordDao.insert(wordEntity);
        wordListAdapter.submitList(wordDao.findAll().stream().map(Word::fromEntity).collect(Collectors.toList()));
    }

    private void saveWord(WordEntity wordEntity) {
        wordDao.update(wordEntity);
        wordListAdapter.submitList(wordDao.findAll().stream().map(Word::fromEntity).collect(Collectors.toList()));
    }

    private void deleteWord(WordListAdapter.ViewHolder viewHolder) {
        wordDao.delete(viewHolder.getEntity().toEntity());
        wordListAdapter.submitList(wordDao.findAll().stream().map(Word::fromEntity).collect(Collectors.toList()));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.wordDao = AppDatabase.getInstance(context).wordDao();
    }
}
