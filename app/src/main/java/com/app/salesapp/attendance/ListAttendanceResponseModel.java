package com.app.salesapp.attendance;

import java.util.List;

public class ListAttendanceResponseModel {
    public class AttendanceList {
        public String attendance_id;
        public String channel_name;
        public String subject;
        public String datetime;
        public String location;
        public String fullname;
        public String notes;
    }

    public int total_page;
    public List<AttendanceList> attendance_list;
}
