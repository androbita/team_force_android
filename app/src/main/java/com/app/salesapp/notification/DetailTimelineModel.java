package com.app.salesapp.notification;

import com.app.salesapp.timeline.comment.CommentResponse;

import java.util.List;

public class DetailTimelineModel {

        public String timeline_id;
        public String username;
        public String photo_profile;
        public String type;
        public String location;
        public String datetime;
        public String remark;
        public String photo;
        public String total_comment;
        public List<CommentResponse> comment;

}
