package com.app.salesapp.training.list.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.salesapp.R;
import com.app.salesapp.databinding.TrainingListItemBinding;
import com.app.salesapp.training.list.TrainingListPresenter;
import com.app.salesapp.training.model.TrainingResponse;
import com.app.salesapp.user.UserService;

import java.util.List;

public class TrainingListAdapter extends RecyclerView.Adapter<TrainingListAdapter.ViewHolder> {

    private Context context;
    private UserService userService;
    private TrainingListPresenter presenter;
    private List<TrainingResponse.TrainingList> trainingLists;

    public TrainingListAdapter(Context context, UserService userService,
                               TrainingListPresenter presenter,
                               List<TrainingResponse.TrainingList> trainingLists) {
        this.userService = userService;
        this.context = context;
        this.presenter = presenter;
        this.trainingLists = trainingLists;
    }

    public void addData(List<TrainingResponse.TrainingList> data) {
        this.trainingLists.addAll(data);
        notifyDataSetChanged();
    }

    public void replaceData(List<TrainingResponse.TrainingList> trainingLists) {
        if (trainingLists == null || trainingLists.isEmpty())
            return;

        this.trainingLists = trainingLists;
        notifyDataSetChanged();
    }

    @Override
    public TrainingListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TrainingListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.training_list_item, parent, false);
        return new TrainingListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final TrainingListAdapter.ViewHolder holder, final int position) {
        final TrainingResponse.TrainingList trainingList = trainingLists.get(holder.getAdapterPosition());
        final TrainingListItemBinding dataBinding = holder.getBinding();
        final TrainingListItemViewModel viewModel = new TrainingListItemViewModel(context, trainingList);

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
        return trainingLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TrainingListItemBinding binding;

        public ViewHolder(final TrainingListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public TrainingListItemBinding getBinding() {
            return binding;
        }
    }
}
