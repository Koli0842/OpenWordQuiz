package com.koli.openvocab.view.adapter;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.persistence.sql.entity.WordEntity;

import java.util.List;
import java.util.function.BiFunction;

public class WordListAdapterWithDiff extends ListAdapter<WordEntity, WordListAdapterWithDiff.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private final OnItemContextMenuClickListener contextMenuItemListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder viewHolder);
    }

    public interface OnItemContextMenuClickListener {
        boolean onClick(MenuItem menuItem, ViewHolder viewHolder);
    }

    public WordListAdapterWithDiff(OnItemClickListener onItemClickListener, OnItemContextMenuClickListener contextMenuItemListener) {
        super(new WordDiff());
        this.onItemClickListener = onItemClickListener;
        this.contextMenuItemListener = contextMenuItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_dictionary_words, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        view.setOnCreateContextMenuListener((menu, v, menuInfo) -> menu.add(0, 0, 0, "Delete").setOnMenuItemClickListener(item -> contextMenuItemListener.onClick(item, viewHolder)));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    public static class WordDiff extends DiffUtil.ItemCallback<WordEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem){
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull WordEntity oldItem, @NonNull WordEntity newItem){
            return newItem.equals(oldItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private WordEntity entity;
        private final TextView query;
        private final TextView result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.query = itemView.findViewById(R.id.dictionary_item_query);
            this.result = itemView.findViewById(R.id.dictionary_item_result);
        }

        public void bindTo(WordEntity entity) {
            this.entity = entity;
            this.query.setText(entity.getQuery());
            this.result.setText(entity.getResult());
        }

        public TextView getQuery() {
            return query;
        }

        public TextView getResult() {
            return result;
        }

        public WordEntity getEntity() {
            return entity;
        }

    }

}
