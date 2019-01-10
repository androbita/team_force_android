package com.app.salesapp.attendance.attendanceListActivity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.AttendanceContract;
import com.app.salesapp.attendance.AttendancePresenter;
import com.app.salesapp.attendance.ListAttendanceResponseModel;
import com.app.salesapp.attendance.MyAttendanceRecyclerAdapter;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class AttendanceListActivity extends AppCompatActivity implements AttendanceContract.View{
    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private AttendancePresenter attendancePresenter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessScrollListener scrollListener;

    RecyclerView listAttendance;
    MyAttendanceRecyclerAdapter adapter;

    ArrayList<ListAttendanceResponseModel.AttendanceList> data;
    int currentPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null));

        attendancePresenter = new AttendancePresenter(this, salesAppService);
        listAttendance = (RecyclerView) findViewById(R.id.listAttendance);
        linearLayoutManager = new LinearLayoutManager(this);
        listAttendance.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();
        adapter = new MyAttendanceRecyclerAdapter(this, data);
        listAttendance.setAdapter(adapter);

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                currentPage = current_page;
                attendancePresenter.getListAttendance(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
            }
        };
        listAttendance.addOnScrollListener(scrollListener);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void showListAttendance(ListAttendanceResponseModel data) {
        adapter.replaceData((ArrayList) data.attendance_list);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        attendancePresenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        attendancePresenter.getListAttendance(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
    }
}
