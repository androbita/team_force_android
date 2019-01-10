package com.app.salesapp.timeline.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.timeline.model.TimelineResponse;

import java.util.ArrayList;

public class SearchSpinnerAdapter extends ArrayAdapter<TimelineResponse.SearchByList> {

    private ArrayList<TimelineResponse.SearchByList> items;
    private Activity activity;

    public SearchSpinnerAdapter(Activity activity, ArrayList<TimelineResponse.SearchByList> items) {
        super(activity, R.layout.spinner_search_item, items);
        this.items = items;
        this.activity = activity;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        if (v == null) {
            v = new TextView(activity);
        }

        v.setTextColor(Color.BLACK);
        v.setText(items.get(position).name);
        return v;
    }

    @Override
    public TimelineResponse.SearchByList getItem(int position) {
        return items.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.spinner_search_item, null);
        }
        TextView lbl = (TextView) v.findViewById(R.id.text1);
        lbl.setTextColor(Color.BLACK);
        lbl.setText(items.get(position).name);
        return v;
    }
}
