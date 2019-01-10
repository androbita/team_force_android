package com.app.salesapp.attendance;

/**
 * Created by hiantohendry on 10/27/16.
 */
public class ListAttendanceRequestModel {
    protected String token;
    protected String program_id;
    protected String page;
    public ListAttendanceRequestModel(String token, String programId, String page) {
        this.token = token;
        this.program_id = programId;
        this.page = page;
    }
}
