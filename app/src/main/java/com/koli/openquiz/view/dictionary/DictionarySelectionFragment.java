package com.koli.openquiz.view.dictionary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.koli.openquiz.R;
import com.koli.openquiz.persistence.DictionaryStore;
import com.koli.openquiz.view.adapter.DictionaryListAdapter;

public class DictionarySelectionFragment extends Fragment {

    private Context context;
    private DictionaryStore dictionaryStore;
    private ListView dictionaryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dictionary_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        dictionaryList = view.findViewById(R.id.dictionary_list);

        DictionaryListAdapter listAdapter = new DictionaryListAdapter(context, dictionaryStore.list());
        dictionaryList.setAdapter(listAdapter);

        dictionaryList.setOnItemClickListener((parent, _view, position, id) -> {
            Intent intent = new Intent(context, ListActivity.class);
            intent.putExtra("DICTIONARY", (String) parent.getItemAtPosition(position));
            startActivity(intent);
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.dictionaryStore = new DictionaryStore(context);
    }
}
