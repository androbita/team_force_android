package com.app.salesapp.inbound;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.attendance.ListAttendanceResponseModel;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;

import java.util.ArrayList;

import javax.inject.Inject;

public class MyInboundRecyclerAdapter extends RecyclerView.Adapter<MyInboundRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<InboundModel.OutboundList> mValues;
    private receiveView view;

    @Inject
    public UserService userService;

    public MyInboundRecyclerAdapter(Context context, ArrayList<InboundModel.OutboundList> items, receiveView inboundView) {
        mValues = items;
        mContext = context;
        view = inboundView;
        ((SalesApp) mContext.getApplicationContext()).getSalesAppDeps().inject(this);
    }

    public void replaceData(ArrayList<InboundModel.OutboundList> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<InboundModel.OutboundList> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }


    private void setList(ArrayList<InboundModel.OutboundList>data) {
        mValues.addAll(data);
    }

    @Override
    public MyInboundRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_inbound_list, parent, false);
        final MyInboundRecyclerAdapter.ViewHolder holder = new MyInboundRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyInboundRecyclerAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.title.setText(holder.mItem.material_name +" - " + holder.mItem.quantity +" pcs");
        holder.desc.setText(holder.mItem.description);
        holder.dateTV.setText(Util.formatDateFromAPI(holder.mItem.date,null) );
        holder.quantity.setText(holder.mItem.quantity +" - " + holder.mItem.status);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService.setCurrentInbound(holder.mItem);
                view.showUpdateReceive();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public  final TextView title;
        public  final TextView desc;
        private final TextView dateTV;
        public final TextView quantity;
        public InboundModel.OutboundList mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            title = (TextView)view.findViewById(R.id.titleTV);
            desc = (TextView)view.findViewById(R.id.descriptionTV);
            dateTV = (TextView)view.findViewById(R.id.dateTV);
            quantity = (TextView)view.findViewById(R.id.locationTV);

        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
