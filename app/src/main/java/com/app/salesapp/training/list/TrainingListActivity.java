package com.app.salesapp.training.list;

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
import com.app.salesapp.databinding.FragmentTrainingListBinding;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.training.list.adapter.TrainingListAdapter;
import com.app.salesapp.training.model.TrainingResponse;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class TrainingListActivity extends BaseActivity implements TrainingListView {

    @Inject
    SalesAppService salesAppService;
    @Inject
    UserService userService;
    private FragmentTrainingListBinding binding;
    private TrainingListPresenter presenter;
    private TrainingListAdapter trainingListAdapter;
    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_training_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Training List");
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        TrainingListViewModel viewModel = new TrainingListViewModel();

        binding.setViewModel(viewModel);

        presenter = new TrainingListPresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        trainingListAdapter = new TrainingListAdapter(this, userService, presenter,
                new ArrayList<TrainingResponse.TrainingList>());

        layoutManager = new LinearLayoutManager(this);
        binding.listTraining.setLayoutManager(layoutManager);
        binding.listTraining.setAdapter(trainingListAdapter);

        final EndlessScrollListener listener = new EndlessScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page) {
                presenter.getTraining(page);
            }
        };
        binding.listTraining.addOnScrollListener(listener);

        binding.trainingPullRefresh.setEnabled(true);
        binding.trainingPullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.reset();
                presenter.getTraining(FIRST_PAGE);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getTraining(FIRST_PAGE);
    }

    @Override
    public void setSwipeRefresh(boolean refresh) {
        binding.trainingPullRefresh.setRefreshing(refresh);
    }

    @Override
    public void onTrainingListSuccess(List<TrainingResponse.TrainingList> data) {
        trainingListAdapter.replaceData(data);
    }

    @Override
    public void onTrainingListLoadMoreSuccess(List<TrainingResponse.TrainingList> data) {
        trainingListAdapter.addData(data);
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
