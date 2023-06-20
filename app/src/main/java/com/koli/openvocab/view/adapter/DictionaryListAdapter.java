package com.koli.openvocab.view.adapter;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;

public class DictionaryListAdapter extends ListAdapter<DictionaryEntity, DictionaryListAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private final OnCreateContextMenuListener onCreateContextMenuListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }

    public interface OnCreateContextMenuListener {
        void onCreateContextMenu(ContextMenu contextMenu, ViewHolder viewHolder);
    }

    public DictionaryListAdapter(OnItemClickListener onItemClickListener, OnCreateContextMenuListener onCreateContextMenuListener) {
        super(new DictionaryDiff());
        this.onItemClickListener = onItemClickListener;
        this.onCreateContextMenuListener = onCreateContextMenuListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_dictionaries, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        view.setOnCreateContextMenuListener((menu, v, menuInfo) -> onCreateContextMenuListener.onCreateContextMenu(menu, viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getCurrentList().get(position));
    }

    public static class DictionaryDiff extends DiffUtil.ItemCallback<DictionaryEntity> {
        @Override
        public boolean areItemsTheSame(@NonNull DictionaryEntity oldItem, @NonNull DictionaryEntity newItem){
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull DictionaryEntity oldItem, @NonNull DictionaryEntity newItem){
            return newItem.equals(oldItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private DictionaryEntity entity;
        private final TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.name = itemView.findViewById(R.id.dictionary_item);
        }

        void bindTo(DictionaryEntity dictionary) {
            this.entity = dictionary;
            name.setText(dictionary.getName());
            name.setTag(dictionary.getId().toString());
        }

        public DictionaryEntity getEntity() {
            return entity;
        }

        public void setEntity(DictionaryEntity entity) {
            this.entity = entity;
        }

        public TextView getName() {
            return name;
        }
    }

}
