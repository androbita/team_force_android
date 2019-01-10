package com.app.salesapp.timeline.comment;

import com.google.gson.annotations.SerializedName;

public class PostCommentRequest {

    @SerializedName("token")
    public String token;
    @SerializedName("program_id")
    public String programId;
    @SerializedName("timeline_id")
    public String timelineId;
    @SerializedName("comment")
    public String comment;

    public PostCommentRequest(String token, String programId, String timelineId, String comment) {
        this.token = token;
        this.programId = programId;
        this.timelineId = timelineId;
        this.comment = comment;
    }
}
