package com.koli.openvocab.view.settings.dictionaries;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.koli.openvocab.R;
import com.koli.openvocab.model.json.DictionaryJson;

public class AddDictionaryDialogFragment extends DialogFragment {

    private EditText dictionaryNameText;

    public interface Listener {
        void onConfirmSave(DictionaryJson dictionary);
    }

    private final Listener listener;
    private final DictionaryJson importedDictionary;

    public AddDictionaryDialogFragment(Listener listener, DictionaryJson importedDictionary) {
        this.listener = listener;
        this.importedDictionary = importedDictionary;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);
        View view = getLayoutInflater().inflate(R.layout.fragment_dialog_save_dictionary, null);
        this.dictionaryNameText = view.findViewById(R.id.dictionary_add_name);

        dictionaryNameText.setText(importedDictionary.getName());

        return new AlertDialog.Builder(requireActivity())
            .setView(view)
            .setPositiveButton("Save", (dialog, which) -> {
                importedDictionary.setName(dictionaryNameText.getText().toString());
                listener.onConfirmSave(importedDictionary);
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
