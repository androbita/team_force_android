package com.app.salesapp.timeline.adapter;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;
import android.view.View;

import com.app.salesapp.BR;
import com.app.salesapp.R;
import com.app.salesapp.timeline.comment.CommentResponse;
import com.app.salesapp.timeline.model.TimelineResponse;
import com.app.salesapp.util.Util;

import java.util.ArrayList;
import java.util.List;

public class TimelineItemViewModel extends BaseObservable {

    public static final int FIRST_VISIBILITY_COMMENT_COUNT = 3;

    private static final String TIME_PATTERN_TIMELINE = "MMM d, HH:mm";
    private final TimelineResponse.TimelineList timelineResponse;

    private boolean isCommentVisibility, isUserDetailVisibility, isAllCommentHasShow;

    private Context context;

    public TimelineItemViewModel(Context context, TimelineResponse.TimelineList timelineResponse) {
        this.context = context;
        this.timelineResponse = timelineResponse;
    }

    @Bindable
    public String getUsername() {
        return TextUtils.isEmpty(timelineResponse.username) ? "" : timelineResponse.username;
    }

    @Bindable
    public String getActivities() {
        return String.format(context.getResources().getString(R.string.text_timeline_activities), getType());
    }



    @Bindable
    public String getDatetime() {
        String datetimeResult = Util.formatDateFromAPI(timelineResponse.datetime, TIME_PATTERN_TIMELINE);
        return String.format(context.getResources().getString(R.string.text_timeline_date), datetimeResult);
    }

    @Bindable
    public String getCommentCount() {
        if (Integer.valueOf(timelineResponse.totalComment) > 1)
            return String.format(context.getResources().getString(R.string.text_comments_count), timelineResponse.totalComment);
        else
            return String.format(context.getResources().getString(R.string.text_comment_count), timelineResponse.totalComment);
    }

    public void setCommentVisibility(boolean commentVisibility) {
        isCommentVisibility = commentVisibility;
        notifyPropertyChanged(BR.commentVisibility);
    }

    public boolean isCommentVisibility() {
        return isCommentVisibility;
    }

    @Bindable
    public int getCommentVisibility() {
        return isCommentVisibility ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getPosition() {
        return "";
    }

    @Bindable
    public String getUserEmail() {
        return timelineResponse.username + "@mail.com";
    }

    @Bindable
    public String getPhone() {
        return "";
    }

    public void setUserDetailVisibility(boolean userDetailVisibility) {
        isUserDetailVisibility = userDetailVisibility;
        notifyPropertyChanged(BR.userDetailVisibility);
    }

    public boolean isUserDetailVisibility() {
        return isUserDetailVisibility;
    }

    @Bindable
    public int getUserDetailVisibility() {
        return isUserDetailVisibility ? View.VISIBLE : View.GONE;
    }

    public void setAllCommentHasShow(boolean allCommentHasShow) {
        isAllCommentHasShow = allCommentHasShow;
        notifyPropertyChanged(BR.loadMoreCommentsVisibility);
    }

    @Bindable
    public int getLoadMoreCommentsVisibility() {
        return timelineResponse.comments.size() > FIRST_VISIBILITY_COMMENT_COUNT &&
                !isAllCommentHasShow ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getRemark() {
        return TextUtils.isEmpty(timelineResponse.remark) ? "" : timelineResponse.remark;
    }

    @Bindable
    public int getRemarkVisbility() {
        return TextUtils.isEmpty(timelineResponse.remark) ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public int getPhotoVisibility() {
        return TextUtils.isEmpty(timelineResponse.photo) ? View.GONE : View.VISIBLE;
    }

    public List<CommentResponse> populateTopThreeComment() {
        List<CommentResponse> topThreeComments = new ArrayList<>();
        for (CommentResponse commentResponse : timelineResponse.comments) {
            topThreeComments.add(commentResponse);

            if (topThreeComments.size() == FIRST_VISIBILITY_COMMENT_COUNT) break;
        }

        return topThreeComments;
    }

    @Bindable
    public int getAnnouncementVisibility() {
        return "Announcement".equalsIgnoreCase(timelineResponse.type) ? View.VISIBLE : View.GONE;
    }

    @Bindable
    public String getLocation() {
        return TextUtils.isEmpty(timelineResponse.location) ? "" : timelineResponse.location;
    }

    @Bindable
    public int getLocationVisibility() {
        return (getType().equalsIgnoreCase("Checked in")) ? View.VISIBLE : View.GONE;
    }

    private String getType() {
        return TextUtils.isEmpty(timelineResponse.type) ? "" : timelineResponse.type;
    }
}
