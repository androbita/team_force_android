package com.app.salesapp.schedule.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.databinding.FragmentScheduleListBinding;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.schedule.list.ScheduleListPresenter;
import com.app.salesapp.schedule.list.ScheduleListView;
import com.app.salesapp.schedule.list.ScheduleListViewModel;
import com.app.salesapp.schedule.list.adapter.ScheduleListAdapter;
import com.app.salesapp.schedule.model.ScheduleResponse;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class ScheduleActivity extends AppCompatActivity implements ScheduleListView {
    @Inject
    SalesAppService salesAppService;
    @Inject
    UserService userService;
    private FragmentScheduleListBinding binding;
    private ScheduleListPresenter presenter;
    private ScheduleListAdapter scheduleListAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);


        ScheduleListViewModel viewModel = new ScheduleListViewModel();

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_schedule_list);
        binding.setViewModel(viewModel);

        presenter = new ScheduleListPresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        scheduleListAdapter = new ScheduleListAdapter(this, userService, presenter,
                new ArrayList<ScheduleResponse.ScheduleList>());

        layoutManager = new LinearLayoutManager(this);
        binding.listSchedule.setLayoutManager(layoutManager);
        binding.listSchedule.setAdapter(scheduleListAdapter);

        final EndlessScrollListener listener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                presenter.getSchedule(page);
            }
        };
        binding.listSchedule.addOnScrollListener(listener);

        binding.schedulePullRefresh.setEnabled(true);
        binding.schedulePullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.reset();
                presenter.getSchedule(FIRST_PAGE);
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null));
        presenter.getSchedule(FIRST_PAGE);
    }

    @Override
    public void setSwipeRefresh(boolean refresh) {
        binding.schedulePullRefresh.setRefreshing(refresh);
    }

    @Override
    public void onScheduleListSuccess(List<ScheduleResponse.ScheduleList> data) {
        scheduleListAdapter.replaceData(data);
    }

    @Override
    public void onScheduleListLoadMoreSuccess(List<ScheduleResponse.ScheduleList> data) {
        scheduleListAdapter.addData(data);
    }

    @Override
    public void isAuthFailure() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showSnakeBar(String errorMessage) {

    }

    @Override
    public void showToastMessage(String appErrorMessage) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
