package com.app.salesapp.chart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChartMainFragment extends Fragment
        implements ChartMainContract.View, ChartMainFragmentListener, View.OnClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LinearLayout rootContainer;
    private ViewPager pager;
    private TextView tvMessage;
    private ProgressBar progressBar;
   // private TextView tvActivityStatus;
   //private TextView tvActivityTime;
   // private TextView tvChannelDistribution;
   // private TextView tvChannelTraining;
   // private TextView tvChannelSelling;
   // private TextView tvLastChannel;
   // private Button btnSpinnerUser;

    private String mParam1;
    private String mParam2;
    private String selectedUserId;
    private Context context;

    private ChartPagerAdapter chartPagerAdapter;
    private String[] fragmentTitle = {"Distribution", "Selling", "Attendance", "Schedule"};
    private List<UserResponseModel> users = new ArrayList<>();

    @Inject
    protected SalesAppService salesAppService;

    @Inject
    protected UserService userService;

    private ChartMainPresenter presenter;

    @SuppressLint("ValidFragment")
    public ChartMainFragment(Context context) {
        this.context = context;
    }

    public ChartMainFragment() {
    }

    public static ChartMainFragment newInstance(Context context, String param1, String param2) {
        ChartMainFragment fragment = new ChartMainFragment(context);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
        presenter = new ChartMainPresenter(this, salesAppService, userService);
        selectedUserId = "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabs_chart_menu);
        pager = (ViewPager) rootView.findViewById(R.id.pager_chart);
        rootContainer = (LinearLayout) rootView.findViewById(R.id.root_container);
       /* tvActivityStatus = (TextView) rootView.findViewById(R.id.tv_activity_status);
        tvActivityTime = (TextView) rootView.findViewById(R.id.tv_activity_time);
        tvChannelDistribution = (TextView) rootView.findViewById(R.id.tv_channel_distribution);
        tvChannelTraining = (TextView) rootView.findViewById(R.id.tv_channel_training);
        tvChannelSelling = (TextView) rootView.findViewById(R.id.tv_channel_selling);
        tvLastChannel = (TextView) rootView.findViewById(R.id.tv_last_channel);*/
        tvMessage = (TextView) rootView.findViewById(R.id.tv_message);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
      //  btnSpinnerUser = (Button) rootView.findViewById(R.id.btn_spinner_user);

        tvMessage.setOnClickListener(this);
       // btnSpinnerUser.setOnClickListener(this);

        chartPagerAdapter = new ChartPagerAdapter(getContext(), this, getFragmentManager());

        for (String title : fragmentTitle) {
            chartPagerAdapter.addFrag(ChartDetailFragment.newInstance(context, title), title);
        }

        pager.setAdapter(chartPagerAdapter);
        tabLayout.setupWithViewPager(pager);

        presenter.getUsers();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_spinner_user && !users.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setCancelable(false);

            ArrayAdapter<UserResponseModel> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    users);

            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                    UserResponseModel selectedUser = users.get(which);
                    selectedUserId = selectedUser.userId;
                //    btnSpinnerUser.setText(selectedUser.name);

                    pager.getAdapter().notifyDataSetChanged();
                }
            });

            builder.setNegativeButton(android.R.string.cancel, null);
            builder.show();
            return;
        }

        if (v.getId() == R.id.tv_message) {
            presenter.getCurrentStatus();
        }
    }

    @Override
    public void showLoading() {
        rootContainer.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showUsers(List<UserResponseModel> users) {
        this.users = users;
        if (!users.isEmpty()) {
            selectedUserId = users.get(0).userId;
        }
        userService.saveUserList(users);
//        btnSpinnerUser.setText(!users.isEmpty() ? users.get(0).name : "-");
        presenter.getCurrentStatus();
    }

    @Override
    public void showCurrentStatus(List<StatusResponseModel> statuses) {
        rootContainer.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);

        if (statuses == null || statuses.isEmpty()) {
            return;
        }

        StatusResponseModel status = statuses.get(0);

        if (TextUtils.isEmpty(status.activityStatus.activity)) {
            status.activityStatus.activity = "-";
        }

        if (TextUtils.isEmpty(status.activityStatus.time)) {
            status.activityStatus.time = "-";
        }

       /* tvActivityStatus.setText(String.format(": %s", status.activityStatus.activity));
        tvActivityTime.setText(String.format(": %s", status.activityStatus.time));
        tvChannelDistribution.setText(String.format(": %s", status.channelStatus.distribution));
        tvChannelTraining.setText(String.format(": %s", status.channelStatus.training));
        tvChannelSelling.setText(String.format(": %s", status.channelStatus.selling));
        tvLastChannel.setText(status.activityStatus.lastChannel);
        */
        chartPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmptyStatus() {
        rootContainer.setVisibility(View.VISIBLE);
        tvMessage.setVisibility(View.GONE);

       /* tvActivityStatus.setText(": - ");
        tvActivityTime.setText(": - ");
        tvChannelDistribution.setText(": - ");
        tvChannelTraining.setText(": - ");
        tvChannelSelling.setText(": - ");*/
    }

    @Override
    public void refresh() {
        presenter.getUsers();
    }

    @Override
    public String getUserId() {
        return selectedUserId;
    }

    @Override
    public void setUserId(String selectedUsersId) {
        selectedUserId = selectedUsersId;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
