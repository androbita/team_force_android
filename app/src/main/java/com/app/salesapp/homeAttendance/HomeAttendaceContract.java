package com.app.salesapp.homeAttendance;

import com.app.salesapp.attendance.ListAttendanceResponseModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.attendance.createattendance.PostAttendanceRequestModel;
import com.app.salesapp.channel.model.PostChannelRequestModel;
import com.app.salesapp.network.Response;

import java.util.List;

/**
 * Created by zcky on 3/4/18.
 */

public class HomeAttendaceContract {
    public interface View{
        void onSuccessPostAttendance(Response<String> response);

        void onErrorPostAttendance(String message, PostAttendanceRequestModel postAttendanceRequestModel);

        void showLoading(boolean show);

        void loadSpinnerData(List<ListChannelResponseModel.ChannelList> listChannel, List<ListChannelResponseModel.SubjectList> listSubject);

        void onSuccessCreateChannel(Response<String> response);

        void onErrorCreateChannel(String message, PostChannelRequestModel postChannelRequestModel);

        void onListChannelReceived(ListChannelResponseModel data);
    }

    public interface Presenter{
        void onDestroy();
    }
}
