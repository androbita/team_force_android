package com.app.salesapp.attendance.createattendance;

import com.app.salesapp.network.Response;

import java.util.List;

public interface CreateAttendanceView {
    void onSuccessPostAttendance(Response<String> response);

    void onErrorPostAttendance(String message, PostAttendanceRequestModel postAttendanceRequestModel);

    void showLoading(boolean show);

    void loadSpinnerData(List<ListChannelResponseModel.ChannelList> listChannel, List<ListChannelResponseModel.SubjectList> listSubject);
}
