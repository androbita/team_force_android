package com.app.salesapp.attendance.createattendance;

public class PostAttendanceRequestModel {
    public String token;
    public String program_id;
    public String users_organization_id;
    public String subject;
    public String subjectName;
    public String notes;
    public String location;
    public String latitude;
    public String longitude;
    public String datetime;
    public String picture;

    public PostAttendanceRequestModel(String token, String program_id, String users_organization_id, String subject, String notes, String location, String latitude, String longitude, String datetime, String subjectName) {
        this.token = token;
        this.program_id = program_id;
        this.users_organization_id = users_organization_id;
        this.subject = subject;
        this.subjectName = subjectName;
        this.notes = notes;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datetime = datetime;
    }

    public PostAttendanceRequestModel(String token, String program_id, String users_organization_id, String subject, String notes, String location, String latitude, String longitude, String datetime, String picture, String subjectName) {
        this.token = token;
        this.program_id = program_id;
        this.users_organization_id = users_organization_id;
        this.subject = subject;
        this.subjectName = subjectName;
        this.notes = notes;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.datetime = datetime;
        this.picture = picture;
    }
}
