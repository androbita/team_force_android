package com.app.salesapp.attendance;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class AttendanceFragment extends Fragment implements AttendanceContract.View {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context context;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private AttendancePresenter attendancePresenter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessScrollListener scrollListener;

    @SuppressLint("ValidFragment")
    public AttendanceFragment(Context context) {
        this.context = context;
    }

    public AttendanceFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public static AttendanceFragment newInstance(Context context, String param1, String param2) {
        AttendanceFragment fragment = new AttendanceFragment(context);
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
    }

    RecyclerView listAttendance;
    MyAttendanceRecyclerAdapter adapter;


    @Override
    public void onResume() {
        super.onResume();
        attendancePresenter.getListAttendance(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
    }

    ArrayList<ListAttendanceResponseModel.AttendanceList> data;
    int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_attendance, container, false);

        attendancePresenter = new AttendancePresenter(this, salesAppService);
        listAttendance = (RecyclerView) rootView.findViewById(R.id.listAttendance);
        linearLayoutManager = new LinearLayoutManager(getContext());
        listAttendance.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();
        adapter = new MyAttendanceRecyclerAdapter(getActivity(), data);
        listAttendance.setAdapter(adapter);

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                currentPage = current_page;
                attendancePresenter.getListAttendance(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
            }
        };
        listAttendance.addOnScrollListener(scrollListener);
        return rootView;
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
}
