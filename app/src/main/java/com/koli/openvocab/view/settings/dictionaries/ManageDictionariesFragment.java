package com.koli.openvocab.view.settings.dictionaries;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.convert.DictionaryJsonConverter;
import com.koli.openvocab.model.json.DictionaryJson;
import com.koli.openvocab.persistence.sql.AppDatabase;
import com.koli.openvocab.persistence.sql.dao.DictionaryDao;
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;
import com.koli.openvocab.persistence.sql.entity.DictionaryWithWords;
import com.koli.openvocab.persistence.sql.repository.DictionaryWithWordsRepository;
import com.koli.openvocab.util.StorageUtil;
import com.koli.openvocab.util.moshi.UUIDAdapter;
import com.koli.openvocab.view.adapter.DictionaryListAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.util.UUID;

public class ManageDictionariesFragment extends Fragment {

    private final ActivityResultLauncher<String> importWhenPermissionCheckPassed = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            launchImportDictionaryFlow();
        }
    });
    private final ActivityResultLauncher<String> importDictionary = registerForActivityResult(new ActivityResultContracts.GetContent(), this::importDictionaryAndRequireConfirm);
    private final ActivityResultLauncher<String> exportDictionary = registerForActivityResult(new ActivityResultContracts.CreateDocument("application/json"),
        this::saveExportDictionaryFile);

    private final DictionaryJsonConverter dictionaryJsonConverter = new DictionaryJsonConverter();
    private StorageUtil storageUtil;
    private JsonAdapter<DictionaryJson> dictionaryJsonAdapter;
    private DictionaryDao dictionaryDao;
    private DictionaryWithWordsRepository dictionaryWithWordsRepository;
    private RecyclerView dictionaryList;
    private DictionaryListAdapter dictionaryListAdapter;
    private String contentToExport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Dictionaries");
        return inflater.inflate(R.layout.fragment_dictionary_manager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        dictionaryWithWordsRepository.insertMainIfNotPresent();

        this.dictionaryList = view.findViewById(R.id.dictionary_list);
        view.findViewById(R.id.fab_import).setOnClickListener(v ->
            loadDictionaryFileIfPermitted());
        view.findViewById(R.id.fab_add).setOnClickListener(v -> {
            SaveDictionaryDialogFragment saveDictionaryDialogFragment = new SaveDictionaryDialogFragment(this::insertDictionary, new DictionaryEntity(UUID.randomUUID(), null, null));
            saveDictionaryDialogFragment.show(getParentFragmentManager(), "SaveDictionaryDialog");
        });

        dictionaryListAdapter = new DictionaryListAdapter(v -> {
            ManageWordsInDictionaryListFragment fragment = new ManageWordsInDictionaryListFragment(v.getEntity());
            getParentFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }, this::createListItemContextMenu);
        dictionaryList.setAdapter(dictionaryListAdapter);
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }

    private void createListItemContextMenu(ContextMenu menu, DictionaryListAdapter.ViewHolder viewHolder) {
        menu.add(0, 0, 0, "Edit").setOnMenuItemClickListener(item -> {
            SaveDictionaryDialogFragment saveDictionaryDialogFragment = new SaveDictionaryDialogFragment(this::saveDictionary, viewHolder.getEntity());
            saveDictionaryDialogFragment.show(getParentFragmentManager(), "SaveDictionaryDialog");
            return true;
        });
        menu.add(0, 1, 0, "Delete").setOnMenuItemClickListener(item -> {
            deleteDictionary(viewHolder);
            return true;
        });
        menu.add(0, 2, 0, "Export").setOnMenuItemClickListener(item -> {
            exportDictionary(viewHolder);
            return true;
        });
    }

    private void loadDictionaryFileIfPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            launchImportDictionaryFlow();
        } else if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchImportDictionaryFlow();
        } else {
            importWhenPermissionCheckPassed.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void launchImportDictionaryFlow() {
        importDictionary.launch("application/json");
    }

    private void importDictionaryAndRequireConfirm(Uri fileUri) {
        DictionaryJson dictionary = importDictionary(fileUri);
        if (dictionary != null) {
            AddDictionaryDialogFragment dialogFragment = new AddDictionaryDialogFragment(this::storeDictionary, dictionary);
            dialogFragment.show(getParentFragmentManager(), "addDictionaryDialog");
        }

    }

    private DictionaryJson importDictionary(Uri fileUri) {
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

    public void storeDictionary(DictionaryJson dictionary) {
        DictionaryWithWords dictionaryWithWords = dictionaryJsonConverter.toDatabaseEntity(dictionary);
        dictionaryWithWordsRepository.insert(dictionaryWithWords);
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }


    private void exportDictionary(DictionaryListAdapter.ViewHolder viewHolder) {
        DictionaryWithWords dictionaryWithWords = dictionaryWithWordsRepository.findById(viewHolder.getEntity().getId());
        DictionaryJson dictionaryJson = dictionaryJsonConverter.fromDatabaseEntity(dictionaryWithWords);
        contentToExport = dictionaryJsonAdapter.toJson(dictionaryJson);
        exportDictionary.launch(viewHolder.getEntity().getName());
    }

    private void saveExportDictionaryFile(Uri uri) {
        if (uri != null) {
            storageUtil.write(uri, contentToExport);
        }
    }

    private void insertDictionary(DictionaryEntity entity) {
        dictionaryDao.insert(entity);
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }

    private void saveDictionary(DictionaryEntity entity) {
        dictionaryDao.update(entity);
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }

    private void deleteDictionary(DictionaryListAdapter.ViewHolder viewHolder) {
        dictionaryDao.delete(viewHolder.getEntity());
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.storageUtil = new StorageUtil(getContext());
        this.dictionaryWithWordsRepository = AppDatabase.getInstance(getContext()).dictionaryWithWordsRepository();
        this.dictionaryJsonAdapter = new Moshi.Builder().add(new UUIDAdapter()).build().adapter(DictionaryJson.class).lenient();
        this.dictionaryDao = AppDatabase.getInstance(getContext()).dictionaryDao();
    }

}
