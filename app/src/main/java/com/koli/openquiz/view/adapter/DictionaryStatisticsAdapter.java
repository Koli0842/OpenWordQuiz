package com.koli.openquiz.view.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openquiz.R;
import com.koli.openquiz.persistence.sql.entity.DictionaryStatEntity;
import com.koli.openquiz.persistence.sql.view.DictionaryWordStatView;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DictionaryStatisticsAdapter extends RecyclerView.Adapter<DictionaryStatisticsAdapter.ViewHolder> {

    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");
    private final List<DictionaryWordStatView> wordStats;
    private final Map<UUID, DictionaryStatEntity> dictionaryStats;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }

    public DictionaryStatisticsAdapter(List<DictionaryWordStatView> wordStats, Map<UUID, DictionaryStatEntity> dictionaryStats, OnItemClickListener onItemClickListener) {
        this.wordStats = wordStats;
        this.dictionaryStats = dictionaryStats;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_stats, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        return viewHolder;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DictionaryWordStatView dictionaryEntity = wordStats.get(position);
        holder.getDictionary().setText(dictionaryEntity.getDictionaryName());
        long correctCount = dictionaryEntity.getCorrectCount();
        long answeredCount = dictionaryEntity.getAnsweredCount();
        holder.getAccuracy().setText(String.format("%s%%", decimalFormat.format(100.0 * correctCount / answeredCount)));
        holder.getCorrectCount().setText(String.valueOf(correctCount));
        holder.getAnsweredCount().setText(String.valueOf(answeredCount));
        holder.getHighestStreakCount().setText(String.valueOf(getHighestStreak(dictionaryEntity)));
    }

    private long getHighestStreak(DictionaryWordStatView dictionaryEntity) {
        DictionaryStatEntity dictionaryStatEntity = dictionaryStats.get(dictionaryEntity.getDictionaryId());
        return dictionaryStatEntity == null ? 0 : dictionaryStatEntity.getHighestStreak();
    }

    @Override
    public int getItemCount() {
        return wordStats.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dictionary;
        private final TextView accuracy;
        private final TextView correctCount;
        private final TextView answeredCount;
        private final TextView highestStreakCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.dictionary = itemView.findViewById(R.id.statistics_dictionary_title);
            this.accuracy = itemView.findViewById(R.id.statistics_accuracy);
            this.correctCount = itemView.findViewById(R.id.statistics_correct_count);
            this.answeredCount = itemView.findViewById(R.id.statistics_answer_count);
            this.highestStreakCount = itemView.findViewById(R.id.statistics_highest_streak);
        }

        public TextView getDictionary() {
            return dictionary;
        }

        public TextView getAccuracy() {
            return accuracy;
        }

        public TextView getCorrectCount() {
            return correctCount;
        }

        public TextView getAnsweredCount() {
            return answeredCount;
        }

        public TextView getHighestStreakCount() {
            return highestStreakCount;
        }
    }

}
