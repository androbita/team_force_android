package com.app.salesapp.attendance.createattendance;

import java.util.List;

public class ListChannelResponseModel {
    public class ChannelList {
        public String users_organization_id;
        public String name;
    }

    public class SubjectList {
        public String value;
        public String name;
    }

    public List<ChannelList> channel_list;
    public List<SubjectList> subject_list;

}
