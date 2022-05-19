package com.koli.openquiz.view.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openquiz.R;
import com.koli.openquiz.persistence.DictionaryStore;
import com.koli.openquiz.util.StorageUtil;
import com.koli.openquiz.view.adapter.DictionaryListAdapter;

public class DictionaryManagerActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> importDictionaryFlowLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), this::showSaveDictionaryDialog);

    private final ActivityResultLauncher<String> checkPermissionAndLaunchImportDictionaryFlowLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) launchImportDictionaryFlow();
        });

    private StorageUtil storageUtil;
    private DictionaryStore dictionaryStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.storageUtil = new StorageUtil(this);
        this.dictionaryStore = new DictionaryStore(this);

        findViewById(R.id.fab).setOnClickListener(view ->
            loadDictionaryFileIfPermitted());

        updateDictionaryList();
    }

    private void loadDictionaryFileIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchImportDictionaryFlow();
        } else {
            checkPermissionAndLaunchImportDictionaryFlowLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void launchImportDictionaryFlow() {
        importDictionaryFlowLauncher.launch("*/*");
    }

    private void showSaveDictionaryDialog(Uri fileUri) {
        if (fileUri != null) {
            AddDictionaryDialogFragment dialogFragment = new AddDictionaryDialogFragment(name -> storeDictionary(name, fileUri));
            dialogFragment.show(getSupportFragmentManager(), "addDictionaryDialog");
        }
    }

    public void storeDictionary(String name, Uri fileUri) {
        dictionaryStore.store(name, storageUtil.read(fileUri));
        updateDictionaryList();
    }

    private void updateDictionaryList() {
        DictionaryListAdapter listAdapter = new DictionaryListAdapter(dictionaryStore.list(), v -> {});
        RecyclerView dictionaryList = findViewById(R.id.dictionary_list);
        dictionaryList.setAdapter(listAdapter);
    }
}
