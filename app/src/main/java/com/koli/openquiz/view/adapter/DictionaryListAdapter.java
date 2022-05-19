package com.koli.openquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openquiz.R;

import java.util.List;

public class DictionaryListAdapter extends RecyclerView.Adapter<DictionaryListAdapter.ViewHolder> {

    private final List<String> dictionaries;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }

    public DictionaryListAdapter(List<String> dictionaries, OnItemClickListener onItemClickListener) {
        this.dictionaries = dictionaries;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_dictionaries, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getDictionary().setText(dictionaries.get(position));
    }

    @Override
    public int getItemCount() {
        return dictionaries.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView dictionary;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.dictionary = itemView.findViewById(R.id.dictionary_item);
        }

        public TextView getDictionary() {
            return dictionary;
        }
    }

}
