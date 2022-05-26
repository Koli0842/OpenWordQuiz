package com.koli.openvocab.view.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.koli.openvocab.R;
import com.koli.openvocab.model.Dictionary;

public class AddDictionaryDialogFragment extends DialogFragment {

    private EditText dictionaryNameText;

    public interface Listener {
        void onConfirmSave(Dictionary dictionary);
    }

    private final Listener listener;
    private final Dictionary importedDictionary;

    public AddDictionaryDialogFragment(Listener listener, Dictionary importedDictionary) {
        this.listener = listener;
        this.importedDictionary = importedDictionary;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);
        View view = getLayoutInflater().inflate(R.layout.fragment_dialog_add_dictionary, null);
        this.dictionaryNameText = view.findViewById(R.id.dictionary_add_name);

        dictionaryNameText.setText(importedDictionary.getName());

        return new AlertDialog.Builder(requireActivity())
            .setView(view)
            .setPositiveButton("Save", (dialog, which) -> {
                importedDictionary.setName(dictionaryNameText.getText().toString());
                listener.onConfirmSave(importedDictionary);
                getParentFragmentManager().popBackStack();
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
