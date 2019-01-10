package com.app.salesapp.inbound;

import com.app.salesapp.attendance.ListAttendanceRequestModel;

public class ListOutboundRequest extends ListAttendanceRequestModel {

    public ListOutboundRequest(String token, String programId, String page) {
        super(token, programId, page);
    }
}
