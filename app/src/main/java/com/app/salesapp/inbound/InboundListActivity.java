package com.app.salesapp.inbound;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.common.EndlessScrollListener;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class InboundListActivity extends AppCompatActivity implements receiveView {
    RecyclerView listInbound;
    MyInboundRecyclerAdapter adapter;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int REQUEST_CODE = 200;

    private String mParam1;
    private String mParam2;
    private Context context;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    private InboundPresenter inboundPresenter;
    private LinearLayoutManager linearLayoutManager;
    private EndlessScrollListener scrollListener;

    ArrayList<InboundModel.OutboundList> data;
    int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound_list);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Receive");
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        inboundPresenter = new InboundPresenter(this, salesAppService);
        listInbound = (RecyclerView) findViewById(R.id.listInbound);
        linearLayoutManager = new LinearLayoutManager(this);
        listInbound.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();
        adapter = new MyInboundRecyclerAdapter(this, data, this);
        listInbound.setAdapter(adapter);

        scrollListener = new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                currentPage = current_page;
                inboundPresenter.getListInbound(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
            }
        };
        listInbound.addOnScrollListener(scrollListener);
        inboundPresenter.getListInbound(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onResume(){
        super.onResume();
        inboundPresenter.getListInbound(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
    }

    @Override
    public void showListInbound(InboundModel data) {
        adapter.replaceData((ArrayList) data.outbound_list);
    }

    @Override
    public void showLoading(boolean b) {

    }

    @Override
    public void showUpdateReceive() {
        startActivityForResult(new Intent(this,InboundUpdateActivity.class),REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                inboundPresenter.getListInbound(userService.getUserPreference().token, userService.getCurrentProgram(), currentPage);
            }
        }
    }
}
