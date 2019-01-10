package com.app.salesapp.chart.sellingchart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.chart.ChartMainFragmentListener;
import com.app.salesapp.chart.ChartResponseModel;
import com.app.salesapp.chart.UserResponseModel;
import com.app.salesapp.user.UserService;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SellingChartFragment extends Fragment implements View.OnClickListener, SellingChartContract.View,AdapterView.OnItemSelectedListener {

    private static final int CHART_TAB = 4;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    protected BarChart mChart;
    private TextView tvMessage;
    private ProgressBar progressBar;
   // private Button btnSpinnerSort;

    private String viewBy = "weekly";
    private String mParam1;
    private String mParam2;
    private Context context;
    private ChartMainFragmentListener fragmentListener;

    @Inject
    protected SalesAppService salesAppService;

    @Inject
    protected UserService userService;

    protected SellingChartPresenter presenter;

    private Spinner userSpinner;
    private Spinner spinnerSort;

    List<UserResponseModel> users;
    String [] listUser;
    String[] views = new String[]{"View By","Weekly", "Monthly"};
    String selectedUsersId = "Select Users";

    @SuppressLint("ValidFragment")
    public SellingChartFragment(Context context, ChartMainFragmentListener fragmentListener) {
        this.context = context;
        this.fragmentListener = fragmentListener;
    }

    public SellingChartFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((SalesApp) getActivity().getApplication()).getSalesAppDeps().inject(this);
        presenter = new SellingChartPresenter(this, salesAppService, userService);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_selling_barchart, container, false);

        mChart = (BarChart) rootView.findViewById(R.id.chart1);
        tvMessage = (TextView) rootView.findViewById(R.id.tv_message);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
        tvMessage.setOnClickListener(this);

        mChart.invalidate();

        presenter.getSellingChart(viewBy);

        setUserList();
        userSpinner = (Spinner) rootView.findViewById(R.id.btn_spinner_user);
        ArrayAdapter<String>userAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, listUser);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(userAdapter);
        userSpinner.setOnItemSelectedListener(this);

        spinnerSort = (Spinner) rootView.findViewById(R.id.btn_spinner_sort);
        ArrayAdapter<String>sortAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item,views);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);
        spinnerSort.setOnItemSelectedListener(this);

        return rootView;
    }

    public void setUserList(){
        users = userService.getUserList();
        listUser = new String[users.size()+1];
        listUser[0] = "Select Users";
        for(int i = 0; i < users.size(); i++){
            listUser[i+1] = users.get(i).name;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_message) {
            fragmentListener.refresh();
            return;
        }

        if (v.getId() == R.id.btn_spinner_sort) {
            final String[] views = new String[]{"Weekly", "Monthly"};

            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    views);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);
            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    viewBy = views[which];
              //      btnSpinnerSort.setText(viewBy);
                    presenter.getSellingChart(viewBy.toLowerCase());
                }
            });

            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
        }
    }

    @Override
    public void showLoading() {
        mChart.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public String getActiveUserId() {
        return fragmentListener.getUserId();
    }

    @Override
    public void showSellingChart(ChartResponseModel sellingChart) {
        mChart.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);

        setData(sellingChart.labels, sellingChart.values);
    }

    private void setData(List<String> labels, List<Integer> values) {
        List<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < labels.size(); i++) {
            entries.add(new BarEntry((float) values.get(i), i));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Selling");
        BarData data = new BarData(labels, dataSet);

        mChart.setData(data);
        mChart.setDescription("");
        mChart.setDescriptionTextSize(5);
    }

    @Override
    public void showError(String errorMessage) {
        mChart.setVisibility(View.GONE);
        tvMessage.setVisibility(View.VISIBLE);
        tvMessage.setText(errorMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.btn_spinner_sort) {
            viewBy =  parent.getItemAtPosition(position).toString();
        }
        if (spinner.getId() == R.id.btn_spinner_user) {
            selectedUsersId = getSelectedUser( parent.getItemAtPosition(position).toString());
            fragmentListener.setUserId(selectedUsersId);
        }

        if(!viewBy.equals("View By") && !selectedUsersId.equals("Select Users")){
            presenter.getSellingChart(viewBy.toLowerCase());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getSelectedUser(String selectedItem){
        String response = "";
        for(int i = 0 ; i < users.size(); i++){
            UserResponseModel us = users.get(i);
            if(us.name.equals(selectedItem)){
                response = us.userId;
                break;
            }
        }
        return response;
    }
}
