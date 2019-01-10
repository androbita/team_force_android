package com.app.salesapp.salesreport.sellinglist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.databinding.FragmentSellingListBinding;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.salesreport.sellinglist.adapter.SellingListAdapter;
import com.app.salesapp.salesreport.sellinglist.model.SellingResponse;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class SellingListActivity extends BaseActivity implements SellingListView {

    private Context context;
    private FragmentSellingListBinding binding;

    private SellingListPresenter presenter;
    private SellingListAdapter sellingListAdapter;
    private LinearLayoutManager layoutManager;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_selling_list);
        SellingListViewModel viewModel = new SellingListViewModel();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Selling List");
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        binding.setViewModel(viewModel);

        presenter = new SellingListPresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        sellingListAdapter = new SellingListAdapter(this, presenter,
                new ArrayList<SellingResponse.SellingList>());

        layoutManager = new LinearLayoutManager(context);
        binding.listSelling.setLayoutManager(layoutManager);
        binding.listSelling.setAdapter(sellingListAdapter);

        final EndlessScrollListener listener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                presenter.getSelling(page);
            }
        };
        binding.listSelling.addOnScrollListener(listener);

        binding.sellingPullRefresh.setEnabled(true);
        binding.sellingPullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.reset();
                presenter.getSelling(FIRST_PAGE);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getSelling(FIRST_PAGE);
    }

    @Override
    public void setSwipeRefresh(boolean refresh) {
        binding.sellingPullRefresh.setRefreshing(refresh);
    }

    @Override
    public void onSellingListSuccess(List<SellingResponse.SellingList> data) {
        sellingListAdapter.replaceData(data);
    }

    @Override
    public void onSellingListLoadMoreSuccess(List<SellingResponse.SellingList> data) {
        sellingListAdapter.addData(data);
    }

    @Override
    public void isAuthFailure() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showSnakeBar(String errorMessage) {
        showSnakeBar(errorMessage);
    }

    @Override
    public void showToastMessage(String errorMessage) {
        showToast(errorMessage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
