package com.koli.openvocab.view.settings;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.convert.DictionaryImportConverter;
import com.koli.openvocab.model.Dictionary;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.DictionaryDao;
import com.koli.openvocab.persistence.sql.entity.DictionaryWithWords;
import com.koli.openvocab.persistence.sql.repository.DictionaryWithWordsRepository;
import com.koli.openvocab.util.StorageUtil;
import com.koli.openvocab.util.moshi.UUIDAdapter;
import com.koli.openvocab.view.adapter.DictionaryListAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;

public class DictionaryManagerActivity extends AppCompatActivity {

    private final ActivityResultLauncher<String> importDictionaryFlowLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), this::importDictionaryAndRequireConfirm);

    private final ActivityResultLauncher<String> checkPermissionAndLaunchImportDictionaryFlowLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) launchImportDictionaryFlow();
        });

    private final DictionaryImportConverter dictionaryImportConverter = new DictionaryImportConverter();
    private StorageUtil storageUtil;
    private JsonAdapter<Dictionary> dictionaryJsonAdapter;
    private DictionaryDao dictionaryDao;
    private DictionaryWithWordsRepository dictionaryWithWordsRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.storageUtil = new StorageUtil(this);
        this.dictionaryWithWordsRepository = AppDatabase.getInstance(this).dictionaryWithWordsRepository();
        this.dictionaryJsonAdapter = new Moshi.Builder().add(new UUIDAdapter()).build().adapter(Dictionary.class).lenient();
        this.dictionaryDao = AppDatabase.getInstance(this).dictionaryDao();

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

    private void importDictionaryAndRequireConfirm(Uri fileUri) {
        Dictionary dictionary = importDictionary(fileUri);
        if (dictionary != null) {
            AddDictionaryDialogFragment dialogFragment = new AddDictionaryDialogFragment(this::storeDictionary, dictionary);
            dialogFragment.show(getSupportFragmentManager(), "addDictionaryDialog");
        }

    }

    private Dictionary importDictionary(Uri fileUri) {
        if (fileUri != null) {
            String fileContent = storageUtil.read(fileUri);
            try {
                return dictionaryJsonAdapter.fromJson(fileContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void storeDictionary(Dictionary dictionary) {
        DictionaryWithWords dictionaryWithWords = dictionaryImportConverter.toDatabaseEntity(dictionary);
        dictionaryWithWordsRepository.insert(dictionaryWithWords);
        updateDictionaryList();
    }

    private void updateDictionaryList() {
        DictionaryListAdapter listAdapter = new DictionaryListAdapter(dictionaryDao.findAll(), v -> {
        });
        RecyclerView dictionaryList = findViewById(R.id.dictionary_list);
        dictionaryList.setAdapter(listAdapter);
    }
}
