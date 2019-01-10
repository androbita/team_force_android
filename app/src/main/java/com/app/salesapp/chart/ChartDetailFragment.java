package com.app.salesapp.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.R;

public class ChartDetailFragment extends Fragment {
    private static final int CHART_TAB = 4;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context context;

    @SuppressLint("ValidFragment")
    public ChartDetailFragment(Context context) {
        this.context = context;
    }

    public ChartDetailFragment() {
    }

    public static ChartDetailFragment newInstance(Context context, String title) {
        ChartDetailFragment fragment = new ChartDetailFragment(context);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart_title, container, false);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
