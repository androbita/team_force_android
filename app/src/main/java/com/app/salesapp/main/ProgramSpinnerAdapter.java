package com.app.salesapp.main;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.login.LoginResponseModel;

import java.util.ArrayList;

public class ProgramSpinnerAdapter extends ArrayAdapter<LoginResponseModel.ListProgram> {

    private ArrayList<LoginResponseModel.ListProgram> items;
    private Activity activity;

    public ProgramSpinnerAdapter(Activity activity, ArrayList<LoginResponseModel.ListProgram> items) {
        super(activity, R.layout.spinner_nav_resource, items);
        this.items = items;
        this.activity = activity;
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView v = (TextView) super.getView(position, convertView, parent);
        if (v == null) {
            v = new TextView(activity);
        }
        v.setText(items.get(position).name);
        return v;
    }

    @Override
    public LoginResponseModel.ListProgram getItem(int position) {
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
        lbl.setTextColor(Color.WHITE);
        lbl.setText(items.get(position).name);
        return v;
    }

}
        