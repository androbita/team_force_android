package com.app.salesapp.distribution.distributionlist;

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
import com.app.salesapp.databinding.FragmentDistributionListBinding;
import com.app.salesapp.distribution.distributionlist.adapter.DistributionListAdapter;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class DistributionListFragment extends OptimizedFragment implements DistributionListView {

    private Context context;
    private FragmentDistributionListBinding binding;

    private DistributionListPresenter presenter;
    private DistributionListAdapter distributionListAdapter;
    private LinearLayoutManager layoutManager;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    @SuppressLint("ValidFragment")
    public DistributionListFragment(Context context) {
        this.context = context;
    }

    public DistributionListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public static DistributionListFragment newInstance(Context context) {
        DistributionListFragment fragment = new DistributionListFragment(context);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        DistributionListViewModel viewModel = new DistributionListViewModel();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_distribution_list, container, false);
        binding.setViewModel(viewModel);

        presenter = new DistributionListPresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        distributionListAdapter = new DistributionListAdapter(getActivity(), userService, presenter,
                new ArrayList<DistributionResponse.DistributionList>());

        layoutManager = new LinearLayoutManager(context);
        binding.listDistribution.setLayoutManager(layoutManager);
        binding.listDistribution.setAdapter(distributionListAdapter);

        final EndlessScrollListener listener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                presenter.getDistribution(page);
            }
        };
        binding.listDistribution.addOnScrollListener(listener);

        binding.distributionPullRefresh.setEnabled(true);
        binding.distributionPullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.reset();
                presenter.getDistribution(FIRST_PAGE);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getDistribution(FIRST_PAGE);
    }

    @Override
    public void setSwipeRefresh(boolean refresh) {
        binding.distributionPullRefresh.setRefreshing(refresh);
    }

    @Override
    public void onDistributionListSuccess(List<DistributionResponse.DistributionList> data) {
        distributionListAdapter.replaceData(data);
    }

    @Override
    public void onDistributionListLoadMoreSuccess(List<DistributionResponse.DistributionList> data) {
        distributionListAdapter.addData(data);
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
