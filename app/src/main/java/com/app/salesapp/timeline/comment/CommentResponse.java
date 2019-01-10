package com.app.salesapp.timeline.comment;

import com.google.gson.annotations.SerializedName;

public class CommentResponse {

    @SerializedName("commentId")
    public String commentId;
    @SerializedName("username")
    public String username;
    @SerializedName("text")
    public String text;
    @SerializedName("datetime")
    public String datetime;
    @SerializedName("photo_profile")
    public String photoProfile;
}
