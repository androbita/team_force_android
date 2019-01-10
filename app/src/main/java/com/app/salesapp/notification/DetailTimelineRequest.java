package com.app.salesapp.notification;

public class DetailTimelineRequest {
    private String token;
    private int timeline_id;

    public DetailTimelineRequest(String token, int timeline_id) {
        this.token = token;
        this.timeline_id = timeline_id;
    }

}
