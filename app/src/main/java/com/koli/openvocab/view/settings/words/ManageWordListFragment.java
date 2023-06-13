package com.koli.openvocab.view.settings.words;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.koli.openvocab.R;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.persistence.sql.entity.WordEntity;
import com.koli.openvocab.view.adapter.WordListAdapterWithDiff;

import java.util.List;
import java.util.UUID;

public class ManageWordListFragment extends Fragment {

    private Context context;
    private WordDao wordDao;

    private RecyclerView wordList;
    private WordListAdapterWithDiff wordListAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_words, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        wordList = view.findViewById(R.id.dictionary_list);
        wordList.setLayoutManager(new LinearLayoutManager(context));
        wordListAdapter = new WordListAdapterWithDiff(v -> {
            SaveWordFragment saveWordFragment = new SaveWordFragment(this::saveWord, v.getEntity());
            saveWordFragment.show(getParentFragmentManager(), "saveWordDialog");
        }, (item, v) -> {
            if (item.getItemId() == 0) {
                deleteWord(v);
                return true;
            } else {
                return false;
            }
        });
        wordList.setAdapter(wordListAdapter);
        wordListAdapter.submitList(wordDao.findAll());

        FloatingActionButton addButton = view.findViewById(R.id.fab_add_word);
        addButton.setOnClickListener(v -> {
            SaveWordFragment saveWordFragment = new SaveWordFragment(this::insertWord, new WordEntity(UUID.randomUUID(), null, null));
            saveWordFragment.show(getParentFragmentManager(), "addWordDialog");
        });
    }

    private void insertWord(WordEntity wordEntity) {
        wordDao.insert(wordEntity);
        wordListAdapter.submitList(wordDao.findAll());
    }

    private void saveWord(WordEntity wordEntity) {
        wordDao.update(wordEntity);
        wordListAdapter.submitList(wordDao.findAll());
    }

    private void deleteWord(WordListAdapterWithDiff.ViewHolder viewHolder) {
        wordDao.delete(viewHolder.getEntity());
        wordListAdapter.submitList(wordDao.findAll());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.wordDao = AppDatabase.getInstance(context).wordDao();
    }
}
