package com.app.salesapp.fcm;

import java.util.Date;

public class NotificationModel implements Comparable<NotificationModel> {

    private Date dateTime;

    public Date getDateTime() {
        return dateTime;
    }

    public String message;
    public boolean status;
    public String type;
    public String requestId;

    public NotificationModel(Date dateTime, String message, boolean status, String type, String requestId) {
        this.dateTime = dateTime;
        this.message = message;
        this.status = status;
        this.type = type;
        this.requestId = requestId;
    }


    @Override
    public int compareTo(NotificationModel o) {
        if (this.getDateTime() == null)
            if (o.getDateTime() == null)
                return 0;
            else
                return -1;
        else
            if (this.getDateTime() == null)
                return 1;
            else
                return getDateTime().compareTo(o.getDateTime());
    }
}
