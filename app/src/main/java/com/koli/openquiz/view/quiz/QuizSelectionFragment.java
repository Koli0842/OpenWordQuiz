package com.koli.openquiz.view.quiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openquiz.R;
import com.koli.openquiz.persistence.DictionaryStore;
import com.koli.openquiz.view.adapter.DictionaryListAdapter;

public class QuizSelectionFragment extends Fragment {

    private Context context;
    private DictionaryStore service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView dictionaryList = view.findViewById(R.id.dictionary_list);

        DictionaryListAdapter listAdapter = new DictionaryListAdapter(service.list(), v -> {
            Intent intent = new Intent(context, QuizActivity.class);
            intent.putExtra("DICTIONARY", v.getDictionary().getText());
            startActivity(intent);
        });
        dictionaryList.setAdapter(listAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.service = new DictionaryStore(context);
    }
}
