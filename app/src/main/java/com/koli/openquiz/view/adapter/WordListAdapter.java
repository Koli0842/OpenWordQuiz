package com.koli.openquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openquiz.R;
import com.koli.openquiz.model.Dictionary;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.ViewHolder> {

    private final Dictionary dictionary;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }

    public WordListAdapter(Dictionary dictionary, OnItemClickListener onItemClickListener) {
        this.dictionary = dictionary;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_dictionary_words, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getQuery().setText(dictionary.get(position).getQuery());
        holder.getResult().setText(dictionary.get(position).getResult());
    }

    @Override
    public int getItemCount() {
        return dictionary.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView query;
        private final TextView result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.query = itemView.findViewById(R.id.dictionary_item_query);
            this.result = itemView.findViewById(R.id.dictionary_item_result);
        }

        public TextView getQuery() {
            return query;
        }

        public TextView getResult() {
            return result;
        }
    }

}
