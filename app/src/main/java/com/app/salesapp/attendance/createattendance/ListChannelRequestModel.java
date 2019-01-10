package com.app.salesapp.attendance.createattendance;

/**
 * Created by hiantohendry on 10/27/16.
 */
public class ListChannelRequestModel {
    public String token;
    public String program_id;

    public ListChannelRequestModel(String token, String program_id) {
        this.token = token;
        this.program_id = program_id;
    }
}
