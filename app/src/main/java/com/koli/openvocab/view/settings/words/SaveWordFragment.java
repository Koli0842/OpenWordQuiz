package com.koli.openvocab.view.settings.words;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.koli.openvocab.R;
import com.koli.openvocab.model.DictionaryImport;
import com.koli.openvocab.model.Word;
import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SaveWordFragment extends DialogFragment {

    private EditText wordQuery;
    private EditText wordResult;
    private Spinner queryLocaleSpinner;
    private Spinner resultLocaleSpinner;

    public interface Listener {
        void onConfirmSave(WordEntity wordEntity);
    }

    private final Listener listener;
    private final WordEntity wordEntity;

    public SaveWordFragment(Listener listener, WordEntity wordEntity) {
        this.listener = listener;
        this.wordEntity = wordEntity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);
        View view = getLayoutInflater().inflate(R.layout.fragment_dialog_save_word, null);

        this.wordQuery = view.findViewById(R.id.word_query);
        this.wordResult = view.findViewById(R.id.word_result);
        this.queryLocaleSpinner = view.findViewById(R.id.query_locale_spinner);
        this.resultLocaleSpinner = view.findViewById(R.id.result_locale_spinner);

        ArrayAdapter<String> localeArrayAdapter = new ArrayAdapter<>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Stream.of(Locale.getAvailableLocales()).map(Locale::toLanguageTag).collect(Collectors.toList()));

        wordQuery.setText(wordEntity.getQuery());
        wordResult.setText(wordEntity.getResult());
        queryLocaleSpinner.setAdapter(localeArrayAdapter);
        queryLocaleSpinner.setSelection(localeArrayAdapter.getPosition(wordEntity.getQueryLocale()));
        resultLocaleSpinner.setAdapter(localeArrayAdapter);
        resultLocaleSpinner.setSelection(localeArrayAdapter.getPosition(wordEntity.getResultLocale()));

        return new AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle(wordEntity.getId().toString())
            .setPositiveButton("Save", (dialog, which) -> {
                listener.onConfirmSave(new WordEntity(
                    wordEntity.getId(), wordQuery.getText().toString(), wordResult.getText().toString(),
                    (String) queryLocaleSpinner.getSelectedItem(), (String) resultLocaleSpinner.getSelectedItem()));
            })
            .setNegativeButton("Cancel", (dialog, which) -> {
            })
            .create();
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        manager.beginTransaction()
            .add(this, tag)
            .commitAllowingStateLoss();
    }
}
