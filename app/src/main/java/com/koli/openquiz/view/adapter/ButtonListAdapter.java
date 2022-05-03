package com.koli.openquiz.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.koli.openquiz.model.Word;

import java.util.List;

public class ButtonListAdapter extends BaseAdapter {

    private Context context;
    private List<Word> choices;

    public ButtonListAdapter(Context context, List<Word> buttons) {
        this.context = context;
        this.choices = buttons;
    }

    @Override
    public int getCount() {
        return choices.size();
    }

    @Override
    public Object getItem(int position) {
        return choices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if(convertView == null) {
            button = new Button(context);
            button.setText(choices.get(position).getResult());
            button.setTag(choices.get(position));
            button.setClickable(false);
            button.setFocusable(false);
        } else {
            button = (Button) convertView;
        }

        return button;
    }
}
