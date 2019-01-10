package com.app.salesapp.chart.attendancechart;

import com.app.salesapp.chart.ChartResponseModel;

public interface AttendanceChartContract {

    interface View {

        void showLoading();

        void hideLoading();

        String getActiveUserId();

        void showAttendanceChart(ChartResponseModel attendanceChart);

        void showError(String errorMessage);
    }

    interface Presenter {

        void getAttendanceChart(String viewBy);

        void onDestroy();
    }
}
