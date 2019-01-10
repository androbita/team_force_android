package com.app.salesapp.notification;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;

import com.app.salesapp.attendance.ListAttendanceResponseModel;
import com.app.salesapp.attendance.MyAttendanceRecyclerAdapter;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.inbound.InboundUpdateActivity;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

public class NotificationFragment extends Fragment implements NotificationView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private Context context;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private NotificationPresenter notificationPresenter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessScrollListener scrollListener;
    private boolean loadMore;

    @SuppressLint("ValidFragment")
    public NotificationFragment(Context context) {
        this.context = context;
    }

    public NotificationFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public static NotificationFragment newInstance(Context context, String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment(context);
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

    RecyclerView listNotification;
    MyNotificationRecyclerAdapter adapter;

    ArrayList<NotificationModel> data;
    int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);

        notificationPresenter = new NotificationPresenter(this, salesAppService, userService);
        listNotification = (RecyclerView) rootView.findViewById(R.id.listNotification);
        linearLayoutManager = new LinearLayoutManager(getContext());
        listNotification.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();
        adapter = new MyNotificationRecyclerAdapter(getActivity(), data);
        listNotification.setAdapter(adapter);

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                if (loadMore) {
                    currentPage = current_page;
                    notificationPresenter.getListNotification(currentPage);
                }
            }
        };

        listNotification.addOnScrollListener(scrollListener);
        notificationPresenter.getListNotification(currentPage);
        return rootView;
    }


    @Override
    public void showListNotification(ArrayList<NotificationModel> data, boolean loadMore) {
        this.loadMore = loadMore;
        if (!data.isEmpty()) {
            Collections.sort(data, Collections.reverseOrder());
            adapter.replaceData(data);
        }
    }

    @Override
    public void showToastError(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean b) {

    }

    @Override
    public void showDialogSuccess(String message) {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("Post Announcment")
                .setMessage("Success")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationPresenter.onDestroy();
    }
}
