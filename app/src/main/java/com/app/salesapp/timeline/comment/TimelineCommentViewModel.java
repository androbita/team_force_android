package com.app.salesapp.timeline.comment;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.TextUtils;

import com.app.salesapp.R;
import com.app.salesapp.util.Util;

public class TimelineCommentViewModel extends BaseObservable {

    private static final String TIME_PATTERN_COMMENT = "MMM d, HH:mm";

    private final Context context;
    private final CommentResponse comment;

    public TimelineCommentViewModel(Context context, CommentResponse comment) {
        this.context = context;
        this.comment = comment;
    }

    @Bindable
    public String getUsername() {
        return TextUtils.isEmpty(comment.username) ? "" : comment.username;
    }

    @Bindable
    public String getText() {
        return TextUtils.isEmpty(comment.text) ? "" : comment.text;
    }

    @Bindable
    public String getDatetime() {
        String datetimeResult = Util.formatDateFromAPI(comment.datetime, TIME_PATTERN_COMMENT);
        return String.format(context.getResources().getString(R.string.text_timeline_date), datetimeResult);
    }
}
