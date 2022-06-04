package com.koli.openvocab.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.koli.openvocab.R;
import com.koli.openvocab.model.Word;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private final List<Word> choices;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }


    public QuizAdapter(List<Word> choices, OnItemClickListener onItemClickListener) {
        this.choices = choices;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.grid_quiz_buttons, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(v -> onItemClickListener.onClick(viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getChoiceButton().setText(choices.get(position).getResult());
        holder.setWord(choices.get(position));
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialButton choiceButton;
        private Word word;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.choiceButton = itemView.findViewById(R.id.quiz_choice);
        }

        public MaterialButton getChoiceButton() {
            return choiceButton;
        }

        public void setWord(Word word) {
            this.word = word;
        }

        public Word getWord() {
            return word;
        }
    }

}
