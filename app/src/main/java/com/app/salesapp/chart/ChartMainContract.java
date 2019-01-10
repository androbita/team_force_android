package com.app.salesapp.chart;

import java.util.List;

public interface ChartMainContract {

    interface View {

        void showLoading();

        void hideLoading();

        void showUsers(List<UserResponseModel> users);

        void showCurrentStatus(List<StatusResponseModel> statuses);

        void showEmptyStatus();
    }

    interface Presenter {

        void getUsers();

        void getCurrentStatus();

        void onDestroy();
    }
}
