package com.app.salesapp.main;

import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.app.salesapp.Gps.ConfigRequestModel;
import com.app.salesapp.Gps.ConfigResponseModel;
import com.app.salesapp.R;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.ListAttendanceRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.databinding.NavigationBarBinding;
import com.app.salesapp.distribution.model.ReceivedRequest;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.fcm.NotificationModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.posttracking.PostTrackingRequestModel;
import com.app.salesapp.salesreport.ProductModel;
import com.app.salesapp.salesreport.SellingTypeModel;
import com.app.salesapp.user.UserService;
import com.google.android.gms.location.LocationRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainPresenter {

    private final MainView view;
    private final SalesAppService salesAppService;
    private final UserService userService;

    private CompositeSubscription subscriptions;
    private ArrayList<NotificationModel> notificationIcon;

    private LocationRequest mLocationRequest;


    public MainPresenter(MainView view, SalesAppService salesAppService, UserService userService) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;
        subscriptions = new CompositeSubscription();
    }

    public void navigationSelected(int itemId) {
        switch (itemId) {
            case R.id.home_page:
                view.openHomePage();
                break;
            case R.id.nav_timeline:
                view.openTimelinePage();
                break;
            case R.id.nav_attendance:
                view.openAttendancePage();
                break;
            case R.id.nav_report:
                view.openSellingListPage();
                break;
            case R.id.nav_distribution_list:
                view.openDistributionListPage();
                break;
            case R.id.nav_training:
                view.openTrainingListPage();
                break;
            case R.id.nav_visit_schedule:
                view.openScheduleListPage();
                break;
            case R.id.nav_inbound_list:
                view.openInboundListPage();
                break;
            case R.id.nav_outbox:
                view.openDraftPages();
                break;
            case R.id.logout:
                view.openLogoutDialog();
                break;
            default:
                view.openTimelinePage();
                break;
        }
    }

    public void onDestroy() {
        subscriptions.clear();
    }

    public void setActiveIcon(ArrayList<NotificationModel> notificationIcon, boolean isTimelineOpened, boolean isNotificationOpened) {

        boolean isHasNewNotification = false;
        for (int i = 0; i < notificationIcon.size(); i++) {
            if (userService.getNotifications().get(i).status) {
                isHasNewNotification = true;
                break;
            }
        }

        view.showNotificationIcon(isHasNewNotification, isTimelineOpened, isNotificationOpened);
    }

    public void doGetLogo() {
        view.showLoading(true);
        final ListAttendanceRequestModel requestModel = new ListAttendanceRequestModel(userService.getAccessToken(), userService.getCurrentProgram(), "");
        salesAppService.getLogo(requestModel, new SalesAppService.ServiceCallback<Response<List<String>>>() {
            @Override
            public void onSuccess(Response<List<String>> response) {
                view.showLoading(false);
                userService.saveUserLogo(response.data.get(0));
                view.changeLogo(response.data.get(0));
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
            }
        });
    }

    public void getListChannel(ListChannelRequestModel listChannelRequestModel) {
        view.showLoading(true);
        Subscription subscribe = salesAppService.getListChannel(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<ListChannelResponseModel>>() {
            @Override
            public void onSuccess(Response<ListChannelResponseModel> response) {
                view.showLoading(false);

                if (response.isSuccess())
                    view.onListChannelReceived(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
            }
        });

        subscriptions.add(subscribe);
    }

    public void getListProduct(ListChannelRequestModel listChannelRequestModel) {

        view.showLoading(true);
        Subscription subscribe = salesAppService.getProduct(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<List<ProductModel>>>() {
            @Override
            public void onSuccess(Response<List<ProductModel>> response) {
                view.showLoading(false);

                if (response.isSuccess())
                    view.onListProductReceived(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
            }
        });

        subscriptions.add(subscribe);

    }

    public void getListSellingType(ListChannelRequestModel listChannelRequestModel) {
        view.showLoading(true);
        Subscription subscribe = salesAppService.getSellingType(listChannelRequestModel, new SalesAppService.ServiceCallback<Response<List<SellingTypeModel>>>() {
            @Override
            public void onSuccess(Response<List<SellingTypeModel>> response) {
                view.showLoading(false);

                if (response.isSuccess())
                    view.onListSellingTypeReceived(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
            }
        });

        subscriptions.add(subscribe);
    }

    public void getReceived(ReceivedRequest receivedRequest) {
        view.showLoading(true);
        Subscription subscribe = salesAppService.getReceived(receivedRequest, new SalesAppService.ServiceCallback<Response<ReceivedResponse>>() {
            @Override
            public void onSuccess(Response<ReceivedResponse> response) {
                view.showLoading(false);

                if (response.isSuccess())
                    view.onReceiveListReceived(response.data.receivedLists);
            }

            @Override
            public void onError(NetworkError error) {
                view.showLoading(false);
            }
        });

        subscriptions.add(subscribe);
    }


    public void getConfig(ConfigRequestModel configRequestModel) {
        Subscription subscribe = salesAppService.getConfig(configRequestModel, new SalesAppService.ServiceCallback<Response<ConfigResponseModel>>() {
            @Override
            public void onSuccess(Response<ConfigResponseModel> response) {
                userService.saveTackingList(response.data.tracking_config);
                Integer frequency = Integer.valueOf(userService.getFrequecy());
                view.startLocationServiceTracking();
                view.updateMenu(response.data.menus);
            }

            @Override
            public void onError(NetworkError error) {

            }
        });
        subscriptions.add(subscribe);
    }

    public void postTracking(PostTrackingRequestModel postTrackingRequestModel) {
        Subscription subscribe = salesAppService.postTracking(postTrackingRequestModel, new SalesAppService.ServiceCallback<Response<ConfigResponseModel>>() {
            @Override
            public void onSuccess(Response<ConfigResponseModel> response) {
                Log.e("postTracking", response.data.toString());
            }

            @Override
            public void onError(NetworkError error) {
                Log.e("postTracking", error.toString());
            }
        });
        subscriptions.add(subscribe);
    }

    public String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }



    public long getStartTimeInMillis(){
        long timeStart = 0;
        String startTime = getDate()+" "+userService.getTimeStart();
        try {
            Date time = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss",Locale.getDefault()).parse(startTime);
            timeStart = time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStart;
    }

    public long getStopTimeInMillis(){
        long timeStop = 0;
        String stopTime = getDate()+" "+userService.getTimeStop();
        try {
            Date time = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss",Locale.getDefault()).parse(stopTime);
            timeStop = time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStop;
    }

    public String getDate(){
        return new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());
    }


    public void updateSideMenuVisibility(ArrayList<String> availableMenus, Menu menu){
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() == R.id.nav_visit_schedule && availableMenus.contains("Schedule")){
                menu.getItem(i).setVisible(true);
            }else if(menu.getItem(i).getItemId() == R.id.nav_visit_schedule && !availableMenus.contains("Schedule")){
                menu.getItem(i).setVisible(false);
            }

            if (menu.getItem(i).getItemId() == R.id.nav_attendance && availableMenus.contains("Attendance")){
                menu.getItem(i).setVisible(true);
            }else if(menu.getItem(i).getItemId() == R.id.nav_attendance && availableMenus.contains("Attendance")){
                menu.getItem(i).setVisible(false);
            }

            if (menu.getItem(i).getItemId() == R.id.nav_distribution_list && availableMenus.contains("Distribution")){
                menu.getItem(i).setVisible(true);
            }else if(menu.getItem(i).getItemId() == R.id.nav_distribution_list && availableMenus.contains("Distribution")){
                menu.getItem(i).setVisible(false);
            }

            if (menu.getItem(i).getItemId() == R.id.nav_report && availableMenus.contains("Selling")){
                menu.getItem(i).setVisible(true);
            }else if(menu.getItem(i).getItemId() == R.id.nav_report && availableMenus.contains("Selling")){
                menu.getItem(i).setVisible(false);
            }

            if (menu.getItem(i).getItemId() == R.id.nav_training && availableMenus.contains("Training")){
                menu.getItem(i).setVisible(true);
            }else if(menu.getItem(i).getItemId() == R.id.nav_training && availableMenus.contains("Training")){
                menu.getItem(i).setVisible(false);
            }

        }
    }

    public void updateBottomMenuVisibility(ArrayList<String> availableMenus, NavigationBarBinding navigationBarBinding){

        if(availableMenus.contains("Share")){
            navigationBarBinding.share.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.share.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Training")){
            navigationBarBinding.training.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.training.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Survey")){
            navigationBarBinding.survey.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.survey.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Receive")){
            navigationBarBinding.received.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.received.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Distribution")){
            navigationBarBinding.distribution.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.distribution.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Maintenance")){
            navigationBarBinding.maintenance.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.maintenance.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Selling")){
            navigationBarBinding.selling.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.selling.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Order")){
            navigationBarBinding.salesOrder.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.salesOrder.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Supply")){
            navigationBarBinding.supply.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.supply.setVisibility(View.GONE);
        }

        if(availableMenus.contains("Dynamic")){
            navigationBarBinding.survey.setVisibility(View.VISIBLE);
        }else{
            navigationBarBinding.survey.setVisibility(View.GONE);
        }

    }

}

