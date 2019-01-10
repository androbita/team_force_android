package com.app.salesapp.notification;

import java.util.List;

public class NotificationResponse {
    public class ListNotif {
        public String notification_id;
        public String timeline_id;
        public String description;
        public String read;
        public String active;
        public String created_date;
    }

    public int total_page;
    public List<ListNotif> notification_list;
}
