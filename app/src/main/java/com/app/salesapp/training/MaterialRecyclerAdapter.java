package com.app.salesapp.training;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.training.model.ModuleSelectedModel;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

public class MaterialRecyclerAdapter extends RecyclerView.Adapter<MaterialRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ModuleSelectedModel> mValues;

    @Inject
    public UserService userService;

    public MaterialRecyclerAdapter(Context context, ArrayList<ModuleSelectedModel> items) {
        mValues = items;
        mContext = context;
    }

    public void replaceData(ArrayList<ModuleSelectedModel> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<ModuleSelectedModel> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }

    public void search(ArrayList<ModuleSelectedModel> data){
        mValues = data;
        notifyDataSetChanged();
    }

    private void setList(ArrayList<ModuleSelectedModel> data) {
        mValues.addAll(data);
    }

    @Override
    public MaterialRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_audience, parent, false);
        final MaterialRecyclerAdapter.ViewHolder holder = new MaterialRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MaterialRecyclerAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameTV.setText(holder.mItem.name);
        holder.emailTV.setText("");
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
        public ModuleSelectedModel mItem;

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
