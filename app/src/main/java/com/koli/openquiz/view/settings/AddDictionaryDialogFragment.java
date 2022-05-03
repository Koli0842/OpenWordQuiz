package com.koli.openquiz.view.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.koli.openquiz.R;

public class AddDictionaryDialogFragment extends DialogFragment {

    public interface Listener {
        void onSaveFile(String name);
    }

    private Activity activity;
    private View view;
    private Listener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance) {

        activity = getActivity();
        LayoutInflater inflater = activity.getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_dialog_add_dictionary, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(view)
                .setPositiveButton("Save", (dialog, which) -> {
                    EditText editText = (EditText) view.findViewById(R.id.dictionary_add_name);
                    listener.onSaveFile(editText.getText().toString());
                    activity.getFragmentManager().popBackStack();
                });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Listener) context;
    }

    @Override
    public void show (FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }
}
