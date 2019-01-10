package com.app.salesapp.attendance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.salesapp.R;

import java.util.ArrayList;

public class MyAttendanceRecyclerAdapter extends RecyclerView.Adapter<MyAttendanceRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ListAttendanceResponseModel.AttendanceList> mValues;
    public MyAttendanceRecyclerAdapter(Context context, ArrayList<ListAttendanceResponseModel.AttendanceList> items) {
        mValues = items;
        mContext = context;
    }

    public void replaceData(ArrayList<ListAttendanceResponseModel.AttendanceList> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<ListAttendanceResponseModel.AttendanceList> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }


    private void setList(ArrayList<ListAttendanceResponseModel.AttendanceList>data) {
        mValues.addAll(data);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_draft_list, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.titleKegiatan.setText(holder.mItem.fullname + " - " + holder.mItem.subject);
        holder.detailKegiatan.setText(holder.mItem.datetime);
        holder.locationTV.setText(holder.mItem.location);
        holder.channel.setText(holder.mItem.channel_name);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleKegiatan;
        public final TextView detailKegiatan;
        public final TextView channel;
        public final TextView locationTV;
        public ListAttendanceResponseModel.AttendanceList mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleKegiatan = (TextView)view.findViewById(R.id.titleTV);
            detailKegiatan = (TextView)view.findViewById(R.id.dateTV);
            locationTV = (TextView)view.findViewById(R.id.locationTV);
            channel = (TextView)view.findViewById(R.id.channel);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
