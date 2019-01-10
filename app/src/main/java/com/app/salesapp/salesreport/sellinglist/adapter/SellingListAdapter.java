package com.app.salesapp.salesreport.sellinglist.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.salesapp.R;
import com.app.salesapp.databinding.SellingListItemBinding;
import com.app.salesapp.salesreport.sellinglist.SellingListPresenter;
import com.app.salesapp.salesreport.sellinglist.model.SellingResponse;

import java.util.List;

public class SellingListAdapter extends RecyclerView.Adapter<SellingListAdapter.ViewHolder> {

    private Context context;
    private SellingListPresenter presenter;
    private List<SellingResponse.SellingList> sellingLists;

    public SellingListAdapter(Context context, SellingListPresenter presenter,
                              List<SellingResponse.SellingList> sellingLists) {
        this.context = context;
        this.presenter = presenter;
        this.sellingLists = sellingLists;
    }

    public void addData(List<SellingResponse.SellingList> data) {
        this.sellingLists.addAll(data);
        notifyDataSetChanged();
    }

    public void replaceData(List<SellingResponse.SellingList> sellingLists) {
        if (sellingLists == null || sellingLists.isEmpty())
            return;
        this.sellingLists = sellingLists;
        notifyDataSetChanged();
    }

    @Override
    public SellingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SellingListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.selling_list_item, parent, false);
        return new SellingListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final SellingListAdapter.ViewHolder holder, final int position) {
        final SellingResponse.SellingList sellingList = sellingLists.get(holder.getAdapterPosition());
        final SellingListItemBinding dataBinding = holder.getBinding();
        final SellingListItemViewModel viewModel = new SellingListItemViewModel(context, sellingList, presenter.getUserName());

        dataBinding.setViewModel(viewModel);
        dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return sellingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SellingListItemBinding binding;

        public ViewHolder(SellingListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public SellingListItemBinding getBinding() {
            return binding;
        }
    }
}
