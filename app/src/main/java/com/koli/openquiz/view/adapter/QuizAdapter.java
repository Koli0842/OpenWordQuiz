package com.koli.openquiz.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.koli.openquiz.R;
import com.koli.openquiz.model.Word;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {

    private final List<Word> choices;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(ViewHolder v);
    }


    public QuizAdapter(List<Word> buttons, OnItemClickListener onItemClickListener) {
        this.choices = buttons;
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
        holder.getChoice().setText(choices.get(position).getResult());
        holder.getChoice().setTag(choices.get(position));
    }

    @Override
    public int getItemCount() {
        return choices.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final MaterialButton choice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.choice = itemView.findViewById(R.id.quiz_choice);
        }

        public MaterialButton getChoice() {
            return choice;
        }
    }

}
