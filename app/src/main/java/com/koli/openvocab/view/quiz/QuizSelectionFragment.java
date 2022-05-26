package com.koli.openvocab.view.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.DictionaryDao;
import com.koli.openvocab.view.adapter.DictionaryListAdapter;

public class QuizSelectionFragment extends Fragment {

    private Context context;
    private DictionaryDao dictionaryDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView dictionaryList = view.findViewById(R.id.dictionary_list);

        DictionaryListAdapter listAdapter = new DictionaryListAdapter(dictionaryDao.findAll(), v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("DICTIONARY", (String) v.getDictionary().getTag());
            startActivity(intent);
        });
        dictionaryList.setAdapter(listAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.dictionaryDao = AppDatabase.getInstance(context).dictionaryDao();
    }
}
