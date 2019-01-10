package com.app.salesapp.timeline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.OptimizedFragment;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.CreateAttendanceActivity;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.databinding.FragmentTimelineBinding;
import com.app.salesapp.distribution.createdistribution.CreateDistributionActivity;
import com.app.salesapp.feedback.SendFeedbackActivity;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.salesreport.CreateSalesReportActivity;
import com.app.salesapp.timeline.adapter.SearchSpinnerAdapter;
import com.app.salesapp.timeline.adapter.TimelineAdapter;
import com.app.salesapp.timeline.model.TimelineResponse;
import com.app.salesapp.training.CreateTrainingActivity;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.app.salesapp.common.SalesAppConstant.FIRST_PAGE;

public class TimelineFragment extends OptimizedFragment implements TimelineView {

    private Context context;
    private FragmentTimelineBinding binding;

    private TimelinePresenter presenter;
    private TimelineAdapter timelineAdapter;
    private LinearLayoutManager timelineManager;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private SearchSpinnerAdapter searchSpinnerAdapter;
    private ArrayList<TimelineResponse.SearchByList> searchByLists;
    private String query = "";


    @SuppressLint("ValidFragment")
    public TimelineFragment(Context context) {
        this.context = context;
    }

    public TimelineFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public static TimelineFragment newInstance(Context context) {
        TimelineFragment fragment = new TimelineFragment(context);
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
        TimelineViewModel viewModel = new TimelineViewModel();

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        binding.setViewModel(viewModel);

        presenter = new TimelinePresenter(this, salesAppService, userService, viewModel);
        binding.setPresenter(presenter);

        timelineAdapter = new TimelineAdapter(getActivity(), presenter,
                new ArrayList<TimelineResponse.TimelineList>());

        timelineManager = new LinearLayoutManager(context);
        binding.listTimeline.setLayoutManager(timelineManager);
        binding.listTimeline.setAdapter(timelineAdapter);

        final EndlessScrollListener listener = new EndlessScrollListener(timelineManager) {
            @Override
            public void onLoadMore(int page) {
                String key = "";
                if (searchByLists.size() != 0) {
                    key = ((TimelineResponse.SearchByList) binding.searchBySpinner.getSelectedItem()).key;
                }
                presenter.getTimeline(page, query, key);
            }
        };
        binding.listTimeline.addOnScrollListener(listener);

        binding.timelinePullRefresh.setEnabled(true);
        binding.timelinePullRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.reset();
                String key = "";
                if (searchByLists.size() != 0) {
                    key = ((TimelineResponse.SearchByList) binding.searchBySpinner.getSelectedItem()).key;
                }
                presenter.getTimeline(FIRST_PAGE, query, key);
            }
        });

        binding.searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();
                listener.reset();
                String key = "";
                if (searchByLists.size() != 0) {
                    key = ((TimelineResponse.SearchByList) binding.searchBySpinner.getSelectedItem()).key;
                }
                presenter.getTimeline(FIRST_PAGE, query, key);

            }
        });

        initSearchByAdapter();

        return binding.getRoot();
    }

    private void initSearchByAdapter() {
        searchByLists = new ArrayList<>();
        searchSpinnerAdapter = new SearchSpinnerAdapter(getActivity(), searchByLists);
        binding.searchBySpinner.setAdapter(searchSpinnerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        String key = "";
        if (searchByLists != null && searchByLists.size() != 0) {
            TimelineResponse.SearchByList searchList = ((TimelineResponse.SearchByList) binding.searchBySpinner.getSelectedItem());
            if(searchList != null) {
                key = searchList.key != null ? searchList.key : "";
            }
        }
        presenter.getTimeline(FIRST_PAGE, query, key);
    }

    @Override
    public void setSwipeRefresh(boolean refresh) {
        binding.timelinePullRefresh.setRefreshing(refresh);
    }

    @Override
    public void onTimelineSuccess(List<TimelineResponse.TimelineList> data, List<TimelineResponse.SearchByList> search_by_list) {
        timelineAdapter.replaceData(data);
        searchByLists.clear();
        searchByLists.addAll(search_by_list);
        searchSpinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTimelineLoadMoreSuccess(List<TimelineResponse.TimelineList> data) {
        timelineAdapter.addData(data);
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
    public void postCommentSuccess() {
        String key = "";
        if (searchByLists.size() != 0) {
            key = ((TimelineResponse.SearchByList) binding.searchBySpinner.getSelectedItem()).key;
        }
        presenter.getTimeline(FIRST_PAGE, query, key);
    }

    @Override
    public void openWriteActivity() {
        startActivity(new Intent(getActivity(), SendFeedbackActivity.class));
    }

    @Override
    public void openCheckInPage() {
        startActivity(new Intent(getActivity(), CreateAttendanceActivity.class));
    }

    @Override
    public void openSellingPage() {
        startActivity(new Intent(getActivity(), CreateSalesReportActivity.class));
    }

    @Override
    public void openDistributionPage() {
        startActivity(new Intent(getActivity(), CreateDistributionActivity.class));
    }

    @Override
    public void openTrainingPage() {
        startActivity(new Intent(getActivity(), CreateTrainingActivity.class));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
