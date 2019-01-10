package com.app.salesapp.training;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class AudienceRecyclerAdapter extends RecyclerView.Adapter<AudienceRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<AudienceModel> mValues;

    @Inject
    public UserService userService;

    public AudienceRecyclerAdapter(Context context, ArrayList<AudienceModel> items) {
        mValues = items;
        mContext = context;
    }

    public void replaceData(ArrayList<AudienceModel> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<AudienceModel> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }

    public void search(ArrayList<AudienceModel> data){
        mValues = data;
        notifyDataSetChanged();
    }

    private void setList(ArrayList<AudienceModel> data) {
        mValues.addAll(data);
    }

    @Override
    public AudienceRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_audience, parent, false);
        final AudienceRecyclerAdapter.ViewHolder holder = new AudienceRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AudienceRecyclerAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);


        holder.nameTV.setText(holder.mItem.full_name + "  ");
        holder.emailTV.setText(holder.mItem.email);
        holder.isSelected.setChecked(holder.mItem.isSelected);
        holder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mValues.get(holder.getAdapterPosition()).isSelected = isChecked;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTV;
        private final TextView emailTV;
        private final Switch isSelected;
        private final RelativeLayout bgColor;
        public AudienceModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTV = (TextView) view.findViewById(R.id.nameTV);
            emailTV = (TextView) view.findViewById(R.id.emailTV);
            isSelected = (Switch) view.findViewById(R.id.is_audience_selected);
            bgColor = (RelativeLayout) view.findViewById(R.id.bgColor);
        }


        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
