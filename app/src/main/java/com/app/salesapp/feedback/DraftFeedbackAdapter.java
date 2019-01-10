package com.app.salesapp.feedback;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.salesapp.R;

import java.util.ArrayList;

public class DraftFeedbackAdapter extends RecyclerView.Adapter<DraftFeedbackAdapter.ViewHolder> {
    public interface OnDraftListener{
        void onDraftClicked(PostFeedbackRequest postFeedbackRequest, int pos);
    }

    private Context mContext;
    private ArrayList<PostFeedbackRequest> mValues;
    private OnDraftListener listener;

    public DraftFeedbackAdapter(Context context, ArrayList<PostFeedbackRequest> items,OnDraftListener listener) {
        mValues = items;
        mContext = context;
        this.listener = listener;
    }

    public void replaceData(ArrayList<PostFeedbackRequest> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<PostFeedbackRequest> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }


    private void setList(ArrayList<PostFeedbackRequest> data) {
        mValues.addAll(data);
    }

    @Override
    public DraftFeedbackAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_draft_feedback, parent, false);
        final DraftFeedbackAdapter.ViewHolder holder = new DraftFeedbackAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DraftFeedbackAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);

        holder.titleKegiatan.setText(holder.mItem.getDescription());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDraftClicked(holder.mItem,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleKegiatan;
        public PostFeedbackRequest mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleKegiatan = (TextView) view.findViewById(R.id.titleTV);
        }

        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }

}