package com.koli.openvocab.view.settings.dictionaries;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.model.CheckedWord;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.DictionaryWordMappingDao;
import com.koli.openvocab.persistence.sql.dao.WordDao;
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;
import com.koli.openvocab.persistence.sql.entity.DictionaryWordMapping;
import com.koli.openvocab.persistence.sql.entity.WordEntity;
import com.koli.openvocab.view.adapter.CheckingWordListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ManageWordsInDictionaryListFragment extends Fragment {

    private final DictionaryEntity dictionary;
    private final List<WordEntity> wordsToAdd = new ArrayList<>();
    private final List<WordEntity> wordsToRemove = new ArrayList<>();

    private Context context;
    private WordDao wordDao;
    private DictionaryWordMappingDao dictionaryWordMappingDao;

    private RecyclerView wordList;
    private CheckingWordListAdapter wordListAdapter;

    public ManageWordsInDictionaryListFragment(DictionaryEntity dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle(dictionary.getName());
        return inflater.inflate(R.layout.fragment_manage_words, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        wordList = view.findViewById(R.id.dictionary_list);
        wordList.setLayoutManager(new LinearLayoutManager(context));
        wordListAdapter = new CheckingWordListAdapter((viewHolder, isChecked) -> onCheckedChanged(viewHolder.getEntity(), isChecked));
        wordList.setAdapter(wordListAdapter);
        updateList();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_words_in_dictionary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_words_in_dictionary) {
            dictionaryWordMappingDao.insert(wordsToAdd.stream().map(this::createDictionaryWordMapping).collect(Collectors.toList()));
            dictionaryWordMappingDao.delete(wordsToRemove.stream().map(this::createDictionaryWordMapping).collect(Collectors.toList()));
            getParentFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateList() {
        List<Word> wordsInDictionary = wordDao.findAllInDictionary(dictionary.getId()).stream()
            .map(Word::fromEntity)
            .collect(Collectors.toList());
        List<CheckedWord> checkedWords = wordDao.findAll().stream()
            .map(Word::fromEntity)
            .map(word -> new CheckedWord(word, wordsInDictionary.contains(word)))
            .collect(Collectors.toList());
        wordListAdapter.submitList(checkedWords);
    }

    private void onCheckedChanged(CheckedWord checkedWord, boolean isChecked) {
        if (isChecked) {
            wordsToRemove.remove(checkedWord.toWordEntity());
            wordsToAdd.add(checkedWord.toWordEntity());
        } else {
            wordsToAdd.remove(checkedWord.toWordEntity());
            wordsToRemove.add(checkedWord.toWordEntity());
        }
    }

    private DictionaryWordMapping createDictionaryWordMapping(WordEntity word) {
        return new DictionaryWordMapping(dictionary.getId(), word.getId());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.wordDao = AppDatabase.getInstance(context).wordDao();
        this.dictionaryWordMappingDao = AppDatabase.getInstance(context).dictionaryWithWordsDao();
    }
}
