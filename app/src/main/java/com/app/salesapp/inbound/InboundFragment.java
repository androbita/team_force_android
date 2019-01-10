package com.app.salesapp.inbound;

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

public class InboundFragment extends Fragment implements InboundView {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    @SuppressLint("ValidFragment")
    public InboundFragment(Context context) {
        this.context = context;
    }

    public InboundFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public static InboundFragment newInstance(Context context, String param1, String param2) {
        InboundFragment fragment = new InboundFragment(context);
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

    RecyclerView listInbound;
    MyInboundRecyclerAdapter adapter;


    @Override
    public void onResume() {
        super.onResume();

    }

    ArrayList<InboundModel.OutboundList> data;
    int currentPage = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbound, container, false);
       /*not used anymore*/
       /* inboundPresenter = new InboundPresenter(this, salesAppService);
        listInbound = (RecyclerView) rootView.findViewById(R.id.listInbound);
        linearLayoutManager = new LinearLayoutManager(getContext());
        listInbound.setLayoutManager(linearLayoutManager);

        data = new ArrayList<>();
        adapter = new MyInboundRecyclerAdapter(getActivity(), data);
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
       */
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        inboundPresenter.onDestroy();
    }

    @Override
    public void showListInbound(InboundModel data) {
        adapter.replaceData((ArrayList) data.outbound_list);
    }

    @Override
    public void showLoading(boolean b) {

    }
}
