package com.app.salesapp.chart.channelvisitchart;

import com.app.salesapp.chart.ChartResponseModel;

public interface ChannelVisitChartContract {

    interface View {

        void showLoading();

        void hideLoading();

        String getActiveUserId();

        void showChannelVisitChart(ChartResponseModel channelVisitChart);

        void showError(String errorMessage);
    }

    interface Presenter {

        void getChannelVisitChart(String viewBy);

        void onDestroy();
    }
}
