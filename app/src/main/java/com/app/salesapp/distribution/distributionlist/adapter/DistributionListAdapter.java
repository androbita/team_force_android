package com.app.salesapp.distribution.distributionlist.adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.R;
import com.app.salesapp.databinding.DistributionListItemBinding;
import com.app.salesapp.distribution.distributionlist.DistributionListPresenter;
import com.app.salesapp.distribution.distributionlist.display.DisplayUpdateActivity;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.user.UserService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class DistributionListAdapter extends RecyclerView.Adapter<DistributionListAdapter.ViewHolder> {

    private Context context;
    private UserService userService;
    private DistributionListPresenter presenter;
    private List<DistributionResponse.DistributionList> distributionLists;

    public DistributionListAdapter(Context context, UserService userService,
                                   DistributionListPresenter presenter,
                                   List<DistributionResponse.DistributionList> distributionLists) {
        this.userService = userService;
        this.context = context;
        this.presenter = presenter;
        this.distributionLists = distributionLists;
    }

    public void addData(List<DistributionResponse.DistributionList> data) {
        this.distributionLists.addAll(data);
        notifyDataSetChanged();
    }

    public void replaceData(List<DistributionResponse.DistributionList> distributionLists) {
        if (distributionLists == null || distributionLists.isEmpty())
            return;

        this.distributionLists = distributionLists;
        notifyDataSetChanged();
    }

    @Override
    public DistributionListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DistributionListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.distribution_list_item, parent, false);
        return new DistributionListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final DistributionListAdapter.ViewHolder holder, final int position) {
        final DistributionResponse.DistributionList distributionList = distributionLists.get(holder.getAdapterPosition());
        final DistributionListItemBinding dataBinding = holder.getBinding();
        final DistributionListItemViewModel viewModel = new DistributionListItemViewModel(context, distributionList);

        dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userService.setMaterialName(distributionList.materialName);
                userService.setUsersOrganizationId(distributionList.usersOrganizationId);
                userService.setMerchandiseDistributionId(distributionList.merchantdiseDistributionId);
                userService.setSelectedChannel(distributionList.channelName);
                context.startActivity(new Intent(dataBinding.getRoot().getContext(), DisplayUpdateActivity.class));
            }
        });

        Glide.with(context).load(distributionList.pictureAfter)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dataBinding.imagePhoto);

        dataBinding.setViewModel(viewModel);
        dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return distributionLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private DistributionListItemBinding binding;

        public ViewHolder(final DistributionListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public DistributionListItemBinding getBinding() {
            return binding;
        }
    }
}
