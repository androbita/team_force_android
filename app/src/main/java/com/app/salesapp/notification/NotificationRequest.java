package com.app.salesapp.notification;

public class NotificationRequest {
    String token;
    String program_id;
    String page;

    public NotificationRequest(String token, String program_id, String page) {
        this.token = token;
        this.program_id = program_id;
        this.page = page;
    }
}
