package com.koli.openvocab.view.settings.dictionaries;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.convert.DictionaryImportConverter;
import com.koli.openvocab.model.DictionaryImport;
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

    private final ActivityResultLauncher<String> importDictionaryFlowLauncher =
        registerForActivityResult(new ActivityResultContracts.GetContent(), this::importDictionaryAndRequireConfirm);

    private final ActivityResultLauncher<String> checkPermissionAndLaunchImportDictionaryFlowLauncher =
        registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) launchImportDictionaryFlow();
        });

    private final DictionaryImportConverter dictionaryImportConverter = new DictionaryImportConverter();
    private StorageUtil storageUtil;
    private JsonAdapter<DictionaryImport> dictionaryJsonAdapter;
    private DictionaryDao dictionaryDao;
    private DictionaryWithWordsRepository dictionaryWithWordsRepository;
    private RecyclerView dictionaryList;
    private DictionaryListAdapter dictionaryListAdapter;

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
        }, this::handleMenuItemSelected);
        dictionaryList.setAdapter(dictionaryListAdapter);
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }

    private void loadDictionaryFileIfPermitted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            launchImportDictionaryFlow();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2) {
            launchImportDictionaryFlow();
        } else {
            checkPermissionAndLaunchImportDictionaryFlowLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void launchImportDictionaryFlow() {
        importDictionaryFlowLauncher.launch("application/json");
    }

    private void importDictionaryAndRequireConfirm(Uri fileUri) {
        DictionaryImport dictionary = importDictionary(fileUri);
        if (dictionary != null) {
            AddDictionaryDialogFragment dialogFragment = new AddDictionaryDialogFragment(this::storeDictionary, dictionary);
            dialogFragment.show(getParentFragmentManager(), "addDictionaryDialog");
        }

    }

    private DictionaryImport importDictionary(Uri fileUri) {
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

    public void storeDictionary(DictionaryImport dictionary) {
        DictionaryWithWords dictionaryWithWords = dictionaryImportConverter.toDatabaseEntity(dictionary);
        dictionaryWithWordsRepository.insert(dictionaryWithWords);
        dictionaryListAdapter.submitList(dictionaryDao.findAll());
    }

    private boolean handleMenuItemSelected(MenuItem item, DictionaryListAdapter.ViewHolder viewHolder) {
        if (item.getItemId() == 0) {
            SaveDictionaryDialogFragment saveDictionaryDialogFragment = new SaveDictionaryDialogFragment(this::saveDictionary, viewHolder.getEntity());
            saveDictionaryDialogFragment.show(getParentFragmentManager(), "SaveDictionaryDialog");
            return true;
        } else if (item.getItemId() == 1) {
            deleteDictionary(viewHolder);
            return true;
        } else {
            return false;
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
        this.dictionaryJsonAdapter = new Moshi.Builder().add(new UUIDAdapter()).build().adapter(DictionaryImport.class).lenient();
        this.dictionaryDao = AppDatabase.getInstance(getContext()).dictionaryDao();
    }

}
