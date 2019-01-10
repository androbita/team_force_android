package com.app.salesapp.distribution.activity.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.distribution.model.MaterialSelected;
import com.app.salesapp.training.MaterialRecyclerAdapter;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by zcky on 4/11/18.
 */

public class DistributionMaterialRecyclerAdapter extends RecyclerView.Adapter<DistributionMaterialRecyclerAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<MaterialSelected> mValues = new ArrayList<>();
    private UserService userService;

    public DistributionMaterialRecyclerAdapter(Context context, ArrayList<MaterialSelected> items, UserService service) {
        if (items != null) {
            mValues = items;
        }
        mContext = context;
        userService = service;
    }

    public void replaceData(ArrayList<MaterialSelected> data) {
        setList(data);
        notifyDataSetChanged();
    }

    public void resetData(ArrayList<MaterialSelected> data) {
        mValues.clear();
        setList(data);
        notifyDataSetChanged();
    }

    public void search(ArrayList<MaterialSelected> data){
        if (data != null) {
            mValues = data;
            notifyDataSetChanged();
        }
    }

    private void setList(ArrayList<MaterialSelected> data) {
        mValues.addAll(data);
    }

    @Override
    public DistributionMaterialRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_distribution_material, parent, false);
        final DistributionMaterialRecyclerAdapter.ViewHolder holder = new DistributionMaterialRecyclerAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final DistributionMaterialRecyclerAdapter.ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nameTV.setText(holder.mItem.materialName);
        holder.quantity.setText(String.valueOf(holder.mItem.input));
        holder.isSelected.setChecked(holder.mItem.isSelected);
        holder.isSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mValues.get(holder.getAdapterPosition()).isSelected = isChecked;
                userService.setMaterialSelected(mValues);
            }
        });
        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isNumeric(s.toString())){
                    holder.mItem.input = Integer.valueOf(s.toString());
                    userService.setMaterialSelected(mValues);
                }
            }
        });
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nameTV;
        private final EditText quantity;
        private final Switch isSelected;
        private final RelativeLayout bgColor;
        public MaterialSelected mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nameTV = (TextView) view.findViewById(R.id.nameTV);
            quantity = (EditText) view.findViewById(R.id.inputQuantity);
            isSelected = (Switch) view.findViewById(R.id.is_audience_selected);
            bgColor = (RelativeLayout) view.findViewById(R.id.bgColor);
        }


        @Override
        public String toString() {
            return super.toString() + " '";
        }
    }
}
