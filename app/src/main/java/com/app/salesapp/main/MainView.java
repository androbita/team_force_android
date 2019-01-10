package com.app.salesapp.main;

import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.salesreport.ProductModel;
import com.app.salesapp.salesreport.SellingTypeModel;

import java.util.ArrayList;
import java.util.List;

public interface MainView {

    void openHomePage();

    void openAttendancePage();

    void openTimelinePage();

    void openSellingListPage();

    void openDistributionListPage();

    void openTrainingListPage();

    void showNotificationIcon(boolean isHasNewNotification, boolean isTimelineOpened, boolean isNotificationOpened);

    void openInboundListPage();

    void openScheduleListPage();

    void openFeedBackPage();

    void openLogoutDialog();

    void showLoading(boolean show);

    void changeLogo(String logo);

    void onListChannelReceived(ListChannelResponseModel data);

    void onListProductReceived(List<ProductModel> data);

    void onListSellingTypeReceived(List<SellingTypeModel> data);

    void onReceiveListReceived(List<ReceivedResponse.ReceivedList> receivedLists);

    void startLocationServiceTracking();

    void openDraftPages();

    void updateMenu(ArrayList<String> availableMenus);
}
