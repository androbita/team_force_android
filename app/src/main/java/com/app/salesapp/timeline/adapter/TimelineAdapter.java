package com.app.salesapp.timeline.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.app.salesapp.R;
import com.app.salesapp.databinding.TimelineItemBinding;
import com.app.salesapp.timeline.TimelinePresenter;
import com.app.salesapp.timeline.comment.TimelineCommentAdapter;
import com.app.salesapp.timeline.custom.RoundedCornersTransformation;
import com.app.salesapp.timeline.model.TimelineResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.ViewHolder> {

    private int lastPosition;
    private Context context;
    private TimelinePresenter presenter;
    private TimelineCommentAdapter timelineCommentAdapter;
    private List<TimelineResponse.TimelineList> timelineResponses;

    public TimelineAdapter(Context context, TimelinePresenter presenter,
                           List<TimelineResponse.TimelineList> timelineResponses) {
        this.context = context;
        this.presenter = presenter;
        this.timelineResponses = timelineResponses;
    }

    public void addData(List<TimelineResponse.TimelineList> data) {
        this.timelineResponses.addAll(data);
        notifyDataSetChanged();
    }

    public void replaceData(List<TimelineResponse.TimelineList> timelineResponses) {
        if (timelineResponses == null || timelineResponses.isEmpty())
            return;

        this.timelineResponses = timelineResponses;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TimelineItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.timeline_item, parent, false);
        return new TimelineAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final TimelineResponse.TimelineList timelineResponse = timelineResponses.get(holder.getAdapterPosition());
        final TimelineItemBinding dataBinding = holder.getBinding();
        final TimelineItemViewModel viewModel = new TimelineItemViewModel(context, timelineResponse);

        Glide.with(context).load(timelineResponse.photoProfile)
                .transform(new RoundedCornersTransformation(context, 6, 0))
                .placeholder(R.drawable.ic_profile_blue)
                .error(R.drawable.ic_profile_blue)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dataBinding.imgUserTimeline);

        Glide.with(context).load(timelineResponse.photoProfile)
                .transform(new RoundedCornersTransformation(context, 6, 0))
                .placeholder(R.drawable.ic_profile_blue)
                .error(R.drawable.ic_profile_blue)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dataBinding.layoutTimelineItemDetail.imgUserDetailTimeline);

        Glide.with(context).load(timelineResponse.photo)
                .placeholder(R.drawable.place_holder)
                .error(R.drawable.place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dataBinding.imgDetailTimeline);

        dataBinding.textCommentCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setCommentVisibility(viewModel.isCommentVisibility() ? false : true);
            }
        });

        dataBinding.imgUserTimeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyItemChanged(lastPosition);
                lastPosition = holder.getAdapterPosition();
                viewModel.setUserDetailVisibility(viewModel.isUserDetailVisibility() ? false : true);
            }
        });

        dataBinding.textLoadMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setAllCommentHasShow(true);
                timelineCommentAdapter.replaceData(timelineResponse.comments);
                dataBinding.listComments.setAdapter(timelineCommentAdapter);
            }
        });

        dataBinding.inputComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textName, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    presenter.postComment(timelineResponse.timelineId, textName.getText().toString());
                }
                return true;
            }
        });

        dataBinding.imgSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = dataBinding.inputComment.getText().toString();
                dataBinding.inputComment.setText("");
                presenter.postComment(timelineResponse.timelineId, comment);
            }
        });

        timelineCommentAdapter = new TimelineCommentAdapter(context, viewModel.populateTopThreeComment());
        dataBinding.listComments.setLayoutManager(new LinearLayoutManager(context));
        dataBinding.listComments.setAdapter(timelineCommentAdapter);
        dataBinding.setViewModel(viewModel);
        dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return timelineResponses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TimelineItemBinding binding;

        public ViewHolder(TimelineItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public TimelineItemBinding getBinding() {
            return binding;
        }
    }
}
