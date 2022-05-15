package com.koli.openquiz.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.koli.openquiz.model.Dictionary;
import com.koli.openquiz.model.Word;

public class WordListAdapter extends BaseAdapter {

    private final Context context;
    private final Dictionary dictionary;

    public WordListAdapter(Context context, Dictionary dictionary) {
        this.context = context;
        this.dictionary = dictionary;
    }

    @Override
    public int getCount() {
        return dictionary.size();
    }

    @Override
    public Object getItem(int position) {
        return dictionary.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.HORIZONTAL);

        if(convertView == null) {
            final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            final Word word = dictionary.get(position);

            final TextView query = new TextView(context);
            query.setText(word.getQuery());
            query.setTextSize(20);
            query.setPadding(32, 32, 32, 32);
            query.setLayoutParams(param);
            final TextView result = new TextView(context);
            result.setText(word.getResult());
            result.setTextSize(20);
            result.setPadding(32, 32, 32, 32);
            query.setLayoutParams(param);

            layout.addView(query);
            layout.addView(result);
        } else {
            layout = (LinearLayout) convertView;
        }

        return layout;
    }
}
