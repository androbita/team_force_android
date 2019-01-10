package com.app.salesapp.main;

import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.user.UserService;

import java.util.ArrayList;

public class NavigationBarPresenter {

    private NavigationBarView view;
    private UserService userService;

    public NavigationBarPresenter(NavigationBarView view, UserService userService) {
        this.view = view;
        this.userService = userService;
    }

    public void goToNotificationPage() {
        ArrayList<NotificationModel> list = userService.getNotifications();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).status = false;
        }
        userService.saveNotifications(list);
        view.openNotificationPage();
    }

    public void goToTimelinePage() {
        view.openTimelineNavigationBarPage();
    }

    public void goToChartPage() {
        view.openChartNavigationBarPage();
    }

    public void goToSharePage() {
        view.openStatusPage();
    }

    public void goToMaintenancePage() {
        view.openMaintenancePage();
    }

    public void goToHomePage() {
        view.openHomeNavigationPage();
    }

    public void goToTrainingListPage(){
        view.openTrainingListPage();
    }

    public void goToSalesReportPage(){
        view.openSalesReportPage();
    }

    public void goToDistributionPage(){
        view.openDistributionPage();
    }

    public void goToTrainingPage(){
        view.openTrainingPage();
    }

    public void goToSellingListPage(){
        view.openSellingListPage();
    }

    public void goToDistributionListPage(){
        view.openDistributionListPage();
    }

    public void goToReceivedListPage(){view.openReceivedPage();}

    public void goToSurveyPage(){view.openSurveyPage();}
}
