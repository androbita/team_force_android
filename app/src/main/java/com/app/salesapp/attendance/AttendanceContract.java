package com.app.salesapp.attendance;

import java.util.ArrayList;

/**
 * Created by hiantohendry on 10/14/16.
 */

public interface AttendanceContract {
    public interface View{
        void showListAttendance(ListAttendanceResponseModel data);
    }

    public interface Presenter{
        void getListAttendance(String token, String programId, int page);
        void onDestroy();
    }
}
