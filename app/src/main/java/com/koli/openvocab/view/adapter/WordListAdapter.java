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
import com.koli.openvocab.model.Word;

public class WordListAdapter extends ListAdapter<Word, WordListAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private final OnItemContextMenuClickListener contextMenuItemListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder viewHolder);
    }

    public interface OnItemContextMenuClickListener {
        boolean onClick(MenuItem menuItem, ViewHolder viewHolder);
    }

    public WordListAdapter(OnItemClickListener onItemClickListener, OnItemContextMenuClickListener contextMenuItemListener) {
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

    public static class WordDiff extends DiffUtil.ItemCallback<Word> {
        @Override
        public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem){
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem){
            return newItem.equals(oldItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private Word entity;
        private final TextView query;
        private final TextView result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.query = itemView.findViewById(R.id.dictionary_item_query);
            this.result = itemView.findViewById(R.id.dictionary_item_result);
        }

        public void bindTo(Word entity) {
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

        public Word getEntity() {
            return entity;
        }

    }

}
