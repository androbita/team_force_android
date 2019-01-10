package com.app.salesapp.salesreport;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;

import java.util.ArrayList;
import java.util.List;

public class ProductSpinnerAdapter extends ArrayAdapter<ProductModel> {
    
        private ArrayList<ProductModel> items;
        private Activity activity;

        public ProductSpinnerAdapter(Activity activity, ArrayList<ProductModel> items) {
            super(activity, R.layout.spinner_view_resource, items);
            this.items = items;
            this.activity = activity;
            this.suggestions = new ArrayList<>();
            this.temps = new ArrayList<>(items);
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
        public ProductModel getItem(int position) {
            return items.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater inflater = activity.getLayoutInflater();
                v = inflater.inflate(R.layout.spinner_view_resource, null);
            }
            TextView lbl = (TextView) v.findViewById(R.id.text1);
            lbl.setTextColor(Color.BLACK);
            lbl.setText(items.get(position).name);
            return v;
        }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }


    private ArrayList<ProductModel> suggestions;

    private ArrayList<ProductModel> temps;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((ProductModel)(resultValue)).name;
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                suggestions.clear();
                for (ProductModel channelList : temps) {
                    if(channelList.name.toLowerCase().contains(constraint.toString().toLowerCase())){
                        suggestions.add(channelList);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<ProductModel> filterList = (ArrayList<ProductModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (ProductModel obj : filterList) {
                    add(obj);
                    notifyDataSetChanged();
                }
            }else if(constraint == null) addAll(temps);
        }

    };

    public void setTempItems(List<ProductModel> tempItems) {
        this.temps = new ArrayList<>(tempItems);
    }
    
}
