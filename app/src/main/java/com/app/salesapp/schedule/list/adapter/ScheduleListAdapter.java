package com.app.salesapp.schedule.list.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.R;
import com.app.salesapp.databinding.ScheduleListItemBinding;
import com.app.salesapp.schedule.list.ScheduleListPresenter;
import com.app.salesapp.schedule.model.ScheduleResponse;
import com.app.salesapp.user.UserService;

import java.util.List;

public class ScheduleListAdapter extends RecyclerView.Adapter<ScheduleListAdapter.ViewHolder> {

    private Context context;
    private UserService userService;
    private ScheduleListPresenter presenter;
    private List<ScheduleResponse.ScheduleList> ScheduleLists;

    public ScheduleListAdapter(Context context, UserService userService,
                               ScheduleListPresenter presenter,
                               List<ScheduleResponse.ScheduleList> ScheduleLists) {
        this.userService = userService;
        this.context = context;
        this.presenter = presenter;
        this.ScheduleLists = ScheduleLists;
    }

    public void addData(List<ScheduleResponse.ScheduleList> data) {
        this.ScheduleLists.addAll(data);
        notifyDataSetChanged();
    }

    public void replaceData(List<ScheduleResponse.ScheduleList> ScheduleLists) {
        if (ScheduleLists == null || ScheduleLists.isEmpty())
            return;

        this.ScheduleLists = ScheduleLists;
        notifyDataSetChanged();
    }

    @Override
    public ScheduleListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ScheduleListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.schedule_list_item, parent, false);
        return new ScheduleListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ScheduleListAdapter.ViewHolder holder, final int position) {
        final ScheduleResponse.ScheduleList ScheduleList = ScheduleLists.get(holder.getAdapterPosition());
        final ScheduleListItemBinding dataBinding = holder.getBinding();
        final ScheduleListItemViewModel viewModel = new ScheduleListItemViewModel(context, ScheduleList);

        dataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        dataBinding.setViewModel(viewModel);
        dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return ScheduleLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ScheduleListItemBinding binding;

        public ViewHolder(final ScheduleListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ScheduleListItemBinding getBinding() {
            return binding;
        }
    }
}
