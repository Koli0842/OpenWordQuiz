package com.koli.openquiz.view.settings;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.koli.openquiz.R;
import com.koli.openquiz.view.adapter.DictionaryListAdapter;
import com.koli.openquiz.service.DictionaryStorageService;

import org.jetbrains.annotations.NotNull;

public class DictionaryManagerActivity extends AppCompatActivity implements AddDictionaryDialogFragment.Listener {

    private static final int PICK_FILE_CODE = 0;
    private static final int PERMISSION_READ_STORAGE = 1;
    private DictionaryStorageService service;

    private DialogFragment addDictionaryDialog = new AddDictionaryDialogFragment();

    private Uri fileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view -> openFile());

        service = new DictionaryStorageService(this);
        updateDictionaryList();
    }

    private void updateDictionaryList() {
        DictionaryListAdapter listAdapter = new DictionaryListAdapter(this, service.list());
        ListView dictionaryList = (ListView) findViewById(R.id.dictionary_list);
        dictionaryList.setAdapter(listAdapter);
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_FILE_CODE) {
            if(resultCode == RESULT_OK) {
                fileUri = data.getData();

                if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    showSaveDictionaryDialog();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE }, PERMISSION_READ_STORAGE);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String permissions[], @NotNull int[] grantResults) {
        if (requestCode == PERMISSION_READ_STORAGE) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showSaveDictionaryDialog();
            }
        }
    }

    private void showSaveDictionaryDialog() {
        addDictionaryDialog.show(getSupportFragmentManager(), "addDictionaryDialog");
    }

    @Override
    public void onSaveFile(String name) {
        service.store(name, service.readFromStorage(fileUri));
        updateDictionaryList();
    }
}
