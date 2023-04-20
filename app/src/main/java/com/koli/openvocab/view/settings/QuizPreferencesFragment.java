package com.koli.openvocab.view.settings;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.PreferenceFragmentCompat;

import com.koli.openvocab.R;

public class QuizPreferencesFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        addPreferencesFromResource(R.xml.pref_quiz);
        addPreferencesFromResource(R.xml.pref_quiz_types);
    }
}
