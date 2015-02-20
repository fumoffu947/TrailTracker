package com.example.phijo967.listexemple;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by phijo967 on 2015-02-16.
 */
public class MyAdapter implements ListAdapter {

    ArrayList<String> data = new ArrayList<String>();
    ArrayList<DataSetObserver> listeners = new ArrayList<DataSetObserver>();
    Context context;


    public MyAdapter(ArrayList<String> data, Context c) {
        this.context = c;
        this.data = data;
    }

    public void addName(String name) {
        data.add(name);
        for (DataSetObserver listener : listeners) {
            listener.onChanged();
        }
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        listeners.add(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        listeners.remove(dataSetObserver);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        //if (convertView == null) {
        TextView text = new TextView(context);
        String value = (String) getItem(position);
        text.setText(value);
        if (position % 2 == 0) {
            text.setBackgroundColor(Color.DKGRAY);
        }
        return text;
        /*}else
        {return convertView;
        }*/
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
