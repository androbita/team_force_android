package com.app.salesapp.distribution.distributionListActivity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.databinding.FragmentDistributionListBinding;
import com.app.salesapp.distribution.distributionlist.DistributionListPresenter;
import com.app.salesapp.distribution.distributionlist.DistributionListView;
import com.app.salesapp.distribution.distributionlist.DistributionListViewModel;
import com.app.salesapp.distribution.distributionlist.adapter.DistributionListAdapter;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class DistributionListActivity extends AppCompatActivity implements DistributionListView {
    private FragmentDistributionListBinding binding;

    private DistributionListPresenter presenter;
    private DistributionListAdapter distributionListAdapter;
    private LinearLayoutManager layoutManager;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_list);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        DistributionListViewModel viewModel = new DistributionListViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_distribution_list);
        binding.setViewModel(viewModel);

        presenter = new DistributionListPresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        distributionListAdapter = new DistributionListAdapter(this, userService, presenter,
                new ArrayList<DistributionResponse.DistributionList>());

        layoutManager = new LinearLayoutManager(this);
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null));
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
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showSnakeBar(String errorMessage) {

    }

    @Override
    public void showToastMessage(String errorMessage) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
