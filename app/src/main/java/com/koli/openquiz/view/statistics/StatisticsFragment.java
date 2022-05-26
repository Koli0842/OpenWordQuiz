package com.koli.openquiz.view.statistics;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openquiz.R;
import com.koli.openquiz.persistence.sql.AppDatabase;
import com.koli.openquiz.persistence.sql.dao.DictionaryStatsDao;
import com.koli.openquiz.persistence.sql.dao.DictionaryWordStatViewDao;
import com.koli.openquiz.persistence.sql.entity.DictionaryStatEntity;
import com.koli.openquiz.view.adapter.DictionaryStatisticsAdapter;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatisticsFragment extends Fragment {

    private Context context;
    private DictionaryWordStatViewDao dictionaryWithWordsDao;
    private DictionaryStatsDao dictionaryStatsDao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        RecyclerView statisticsList = view.findViewById(R.id.statistics_list);

        Map<UUID, DictionaryStatEntity> dictionaryScoreMap = dictionaryStatsDao.findAll().stream()
            .collect(Collectors.toMap(DictionaryStatEntity::getDictionaryId, Function.identity()));

        DictionaryStatisticsAdapter adapter = new DictionaryStatisticsAdapter(dictionaryWithWordsDao.findAll(), dictionaryScoreMap, viewHolder -> {});
        statisticsList.setAdapter(adapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        this.context = context;
        this.dictionaryWithWordsDao = AppDatabase.getInstance(context).dictionaryWordStatViewDao();
        this.dictionaryStatsDao = AppDatabase.getInstance(context).dictionaryStatsDao();

    }
}
