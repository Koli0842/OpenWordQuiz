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
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;

public class SaveDictionaryDialogFragment extends DialogFragment {

    private EditText dictionaryNameText;

    public interface Listener {
        void onConfirmSave(DictionaryEntity dictionary);
    }

    private final Listener listener;
    private final DictionaryEntity dictionaryEntity;

    public SaveDictionaryDialogFragment(Listener listener, DictionaryEntity dictionaryEntity) {
        this.listener = listener;
        this.dictionaryEntity = dictionaryEntity;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);
        View view = getLayoutInflater().inflate(R.layout.fragment_dialog_save_dictionary, null);
        this.dictionaryNameText = view.findViewById(R.id.dictionary_add_name);

        dictionaryNameText.setText(dictionaryEntity.getName());

        return new AlertDialog.Builder(requireActivity())
            .setView(view)
            .setTitle(dictionaryEntity.getId().toString())
            .setPositiveButton("Save", (dialog, which) -> {
                String dictionaryName = dictionaryNameText.getText().toString();
                listener.onConfirmSave(new DictionaryEntity(dictionaryEntity.getId(), dictionaryName, dictionaryEntity.getOrdinal()));
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
