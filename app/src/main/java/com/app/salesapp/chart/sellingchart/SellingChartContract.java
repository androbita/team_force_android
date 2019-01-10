package com.app.salesapp.chart.sellingchart;

import com.app.salesapp.chart.ChartResponseModel;

public interface SellingChartContract {

    interface View {

        void showLoading();

        void hideLoading();

        String getActiveUserId();

        void showSellingChart(ChartResponseModel sellingChart);

        void showError(String errorMessage);
    }

    interface Presenter {

        void getSellingChart(String viewBy);

        void onDestroy();
    }
}
