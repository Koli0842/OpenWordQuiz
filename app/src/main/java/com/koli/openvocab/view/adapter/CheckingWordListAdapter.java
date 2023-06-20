package com.koli.openvocab.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.koli.openvocab.R;
import com.koli.openvocab.model.CheckedWord;

public class CheckingWordListAdapter extends ListAdapter<CheckedWord, CheckingWordListAdapter.ViewHolder> {

    private final OnItemCheckChangedListener onItemCheckChangedListener;

    public interface OnItemCheckChangedListener {
        void onCheckedChanged(ViewHolder viewHolder, boolean isChecked);
    }

    public CheckingWordListAdapter(OnItemCheckChangedListener onItemCheckChangedListener) {
        super(new CheckedWordDiff());
        this.onItemCheckChangedListener = onItemCheckChangedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.list_row_checkable_words, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.getQuery().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed()) {
                onItemCheckChangedListener.onCheckedChanged(viewHolder, isChecked);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(getItem(position));
    }

    public static class CheckedWordDiff extends DiffUtil.ItemCallback<CheckedWord> {
        @Override
        public boolean areItemsTheSame(@NonNull CheckedWord oldItem, @NonNull CheckedWord newItem){
            return oldItem.getWord().getId().equals(newItem.getWord().getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CheckedWord oldItem, @NonNull CheckedWord newItem){
            return newItem.equals(oldItem);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CheckedWord entity;
        private final CheckBox query;
        private final TextView result;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.query = itemView.findViewById(R.id.word_checkbox);
            this.result = itemView.findViewById(R.id.dictionary_item_result);
        }

        public void bindTo(CheckedWord entity) {
            this.entity = entity;
            this.query.setChecked(entity.isChecked());
            this.query.setText(entity.getWord().getQuery());
            this.result.setText(entity.getWord().getResult());
        }

        public CheckBox getQuery() {
            return query;
        }

        public TextView getResult() {
            return result;
        }

        public CheckedWord getEntity() {
            return entity;
        }

    }

}
