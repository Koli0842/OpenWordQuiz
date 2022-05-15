package com.koli.openquiz.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class DictionaryListAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> dictionaries;

    public DictionaryListAdapter(Context context, List<String> dictionaries) {
        this.context = context;
        this.dictionaries = dictionaries;
    }

    @Override
    public int getCount() {
        return dictionaries.size();
    }

    @Override
    public Object getItem(int position) {
        return dictionaries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView item;
        if(convertView == null) {
            item = new TextView(context);
            item.setText(dictionaries.get(position));
            item.setTextSize(20);
            item.setPadding(32, 32, 32, 32);
        } else {
            item = (TextView) convertView;
        }

        return item;
    }
}
