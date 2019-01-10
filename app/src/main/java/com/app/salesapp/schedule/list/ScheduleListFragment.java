package com.app.salesapp.schedule.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.OptimizedFragment;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.databinding.FragmentScheduleListBinding;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.schedule.list.adapter.ScheduleListAdapter;
import com.app.salesapp.schedule.model.ScheduleResponse;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class ScheduleListFragment extends OptimizedFragment implements ScheduleListView {

    @Inject
    SalesAppService salesAppService;
    @Inject
    UserService userService;
    private Context context;
    private FragmentScheduleListBinding binding;
    private ScheduleListPresenter presenter;
    private ScheduleListAdapter scheduleListAdapter;
    private LinearLayoutManager layoutManager;

    @SuppressLint("ValidFragment")
    public ScheduleListFragment(Context context) {
        this.context = context;
    }

    public ScheduleListFragment() {
    }

    public static ScheduleListFragment newInstance(Context context) {
        ScheduleListFragment fragment = new ScheduleListFragment(context);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ScheduleListViewModel viewModel = new ScheduleListViewModel();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_schedule_list, container, false);
        binding.setViewModel(viewModel);

        presenter = new ScheduleListPresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        scheduleListAdapter = new ScheduleListAdapter(getActivity(), userService, presenter,
                new ArrayList<ScheduleResponse.ScheduleList>());

        layoutManager = new LinearLayoutManager(context);
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

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showSnakeBar(String errorMessage) {
        showSnakeBar(getActivity(), errorMessage);
    }

    @Override
    public void showToastMessage(String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
