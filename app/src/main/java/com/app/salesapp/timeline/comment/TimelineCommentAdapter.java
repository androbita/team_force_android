package com.app.salesapp.timeline.comment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.app.salesapp.R;
import com.app.salesapp.databinding.TimelineCommentItemBinding;
import com.app.salesapp.timeline.custom.RoundedCornersTransformation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class TimelineCommentAdapter extends RecyclerView.Adapter<TimelineCommentAdapter.ViewHolder> {

    private Context context;
    private List<CommentResponse> comments;

    public TimelineCommentAdapter(Context context, List<CommentResponse> comments) {
        this.comments = comments;
        this.context = context;
    }

    public void replaceData(List<CommentResponse> comments) {
        if (comments == null || comments.isEmpty())
            return;

        this.comments = comments;
        notifyDataSetChanged();
    }

    @Override
    public TimelineCommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TimelineCommentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.timeline_comment_item, parent, false);
        return new TimelineCommentAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final TimelineCommentAdapter.ViewHolder holder, final int position) {
        final CommentResponse response = comments.get(holder.getAdapterPosition());
        final TimelineCommentItemBinding dataBinding = holder.getBinding();
        final TimelineCommentViewModel viewModel = new TimelineCommentViewModel(context, response);

        Glide.with(context).load(response.photoProfile)
                .transform(new RoundedCornersTransformation(context, 6, 0))
                .placeholder(R.drawable.ic_profile_blue)
                .error(R.drawable.ic_profile_blue)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(dataBinding.imgUserComment);

        dataBinding.setViewModel(viewModel);
        dataBinding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TimelineCommentItemBinding binding;

        public ViewHolder(TimelineCommentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public TimelineCommentItemBinding getBinding() {
            return binding;
        }
    }
}
