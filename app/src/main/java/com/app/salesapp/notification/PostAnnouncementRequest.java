package com.app.salesapp.notification;

public class PostAnnouncementRequest {
    String token;
    String program_id;
    String description;

    public PostAnnouncementRequest(String token, String program_id, String description) {
        this.token = token;
        this.program_id = program_id;
        this.description = description;
    }
}
