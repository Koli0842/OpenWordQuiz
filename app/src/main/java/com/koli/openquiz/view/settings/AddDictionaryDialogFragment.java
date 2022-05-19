package com.koli.openquiz.view.settings;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.koli.openquiz.R;

public class AddDictionaryDialogFragment extends DialogFragment {

    public interface Listener {
        void onNameEntered(String name);
    }

    private final Listener listener;
    private View view;

    public AddDictionaryDialogFragment(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {
        super.onCreateDialog(savedInstance);

        this.view = getLayoutInflater().inflate(R.layout.fragment_dialog_add_dictionary, null);

        return new AlertDialog.Builder(requireActivity())
            .setView(view)
            .setPositiveButton("Save", (dialog, which) -> {
                EditText editText = view.findViewById(R.id.dictionary_add_name);
                listener.onNameEntered(editText.getText().toString());
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
