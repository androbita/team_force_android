package com.app.salesapp.chart.distributionchart;

import com.app.salesapp.chart.ChartResponseModel;

public interface DistributionChartContract {

    interface View {

        void showLoading();

        void hideLoading();

        String getActiveUserId();

        void showDistributionChart(ChartResponseModel distributionChart);

        void showError(String errorMessage);
    }

    interface Presenter {

        void getDistributionChart(String viewBy);

        void onDestroy();
    }
}
