package com.koli.openvocab.view.adapter;

import android.view.ContextMenu;
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
import com.koli.openvocab.persistence.sql.entity.DictionaryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DictionaryListAdapter extends ListAdapter<DictionaryEntity, DictionaryListAdapter.ViewHolder> {

    private final OnItemClickListener onItemClickListener;
    private final OnItemContextMenuClickListener contextMenuItemListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }

    public interface OnItemContextMenuClickListener {
        boolean onClick(MenuItem menuItem, ViewHolder viewHolder);
    }

    public DictionaryListAdapter(OnItemClickListener onItemClickListener, OnItemContextMenuClickListener contextMenuItemListener) {
        super(new DictionaryDiff());
        this.onItemClickListener = onItemClickListener;
        this.contextMenuItemListener = contextMenuItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_dictionaries, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        view.setOnCreateContextMenuListener((menu, v, menuInfo) -> createMenu(viewHolder, menu));
        return viewHolder;
    }

    private void createMenu(ViewHolder viewHolder, ContextMenu menu) {
        menu.add(0, 0, 0, "Edit").setOnMenuItemClickListener(item -> contextMenuItemListener.onClick(item, viewHolder));
        menu.add(0, 1, 0, "Delete").setOnMenuItemClickListener(item -> contextMenuItemListener.onClick(item, viewHolder));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getCurrentList().get(position));
        //DictionaryEntity dictionaryEntity = dictionaries.get(position);
        //holder.setEntity(dictionaryEntity);
        //holder.getName().setText(dictionaryEntity.getName());
        //holder.getName().setTag(Optional.ofNullable(dictionaryEntity.getId()).map(UUID::toString).orElse(null));
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
