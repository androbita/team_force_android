package com.app.salesapp.deps;

import com.app.salesapp.SalesAppModule;
import com.app.salesapp.attendance.AttendanceFragment;
import com.app.salesapp.attendance.attendanceListActivity.AttendanceListActivity;
import com.app.salesapp.attendance.createattendance.CreateAttendanceActivity;
import com.app.salesapp.chart.ChartMainFragment;
import com.app.salesapp.chart.attendancechart.AttendanceChartFragment;
import com.app.salesapp.chart.channelvisitchart.ChannelVisitChartFragment;
import com.app.salesapp.chart.distributionchart.DistributionChartFragment;
import com.app.salesapp.chart.sellingchart.SellingChartFragment;
import com.app.salesapp.common.AppModule;
import com.app.salesapp.common.storage.LocalStorageModule;
import com.app.salesapp.distribution.activity.DistributionMaterialActivity;
import com.app.salesapp.distribution.createdistribution.CreateDistributionActivity;
import com.app.salesapp.distribution.distributionListActivity.DistributionListActivity;
import com.app.salesapp.distribution.distributionlist.DistributionListFragment;
import com.app.salesapp.distribution.distributionlist.display.DisplayUpdateActivity;
import com.app.salesapp.draft.DraftActivity;
import com.app.salesapp.draft.MyDraftRecyclerAdapter;
import com.app.salesapp.fcm.CommentCommand;
import com.app.salesapp.feedback.FeedBackActivity;
import com.app.salesapp.feedback.SendFeedbackActivity;
import com.app.salesapp.homeAttendance.HomeAttendanceFragment;
import com.app.salesapp.inbound.InboundFragment;
import com.app.salesapp.inbound.InboundListActivity;
import com.app.salesapp.inbound.InboundUpdateActivity;
import com.app.salesapp.inbound.MyInboundRecyclerAdapter;
import com.app.salesapp.location.LocationBaseActivity;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.main.MainActivity;
import com.app.salesapp.network.CommonNetworkModule;
import com.app.salesapp.network.ConfigModule;
import com.app.salesapp.notification.MyNotificationRecyclerAdapter;
import com.app.salesapp.notification.DetailCommentActivity;
import com.app.salesapp.notification.NotificationFragment;
import com.app.salesapp.salesreport.CreateSalesReportActivity;
import com.app.salesapp.salesreport.sellinglist.SellingListActivity;
import com.app.salesapp.schedule.activity.ScheduleActivity;
import com.app.salesapp.schedule.list.ScheduleListFragment;
import com.app.salesapp.search.SearchActivity;
import com.app.salesapp.survey.SurveyActivity;
import com.app.salesapp.timeline.TimelineFragment;
import com.app.salesapp.training.CreateTrainingActivity;
import com.app.salesapp.training.activity.AudianceActivity;
import com.app.salesapp.training.activity.MaterialActivity;
import com.app.salesapp.training.list.TrainingListActivity;
import com.app.salesapp.user.UserModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        SalesAppModule.class,
        CommonNetworkModule.class,
        AppModule.class,
        ConfigModule.class,
        LocalStorageModule.class,
        UserModule.class
})

public interface SalesAppDeps {

    void inject(MainActivity mainActivity);

    void inject(LocationBaseActivity locationBaseActivity);

    void inject(LoginActivity loginActivity);

    void inject(TimelineFragment timelineFragment);

    void inject(SellingListActivity sellingListActivity);

    void inject(AttendanceFragment attendanceFragment);

    void inject(CreateAttendanceActivity createAttendanceActivity);

    void inject(CreateSalesReportActivity createSalesReportActivity);

    void inject(FeedBackActivity feedBackActivity);

    void inject(SendFeedbackActivity sendFeedbackActivity);

    void inject(CreateDistributionActivity createDistributionActivity);

    void inject(NotificationFragment notificationFragment);

    void inject(CommentCommand commentCommand);

    void inject(DetailCommentActivity detailCommentActivity);

    void inject(MyNotificationRecyclerAdapter myNotificationRecyclerAdapter);

    void inject(DistributionListFragment distributionListFragment);

    void inject(InboundFragment inboundFragment);

    void inject(InboundUpdateActivity inboundUpdateActivity);

    void inject(MyInboundRecyclerAdapter myInboundRecyclerAdapter);

    void inject(DisplayUpdateActivity displayUpdateActivity);

    void inject(CreateTrainingActivity createTrainingActivity);

    void inject(TrainingListActivity trainingListActivity);

    void inject(ScheduleListFragment scheduleListFragment);

    void inject(ChartMainFragment chartMainFragment);

    void inject(MyDraftRecyclerAdapter myDraftRecyclerAdapter);

    void inject(DraftActivity draftActivity);

    void inject(AttendanceChartFragment fragment);

    void inject(ChannelVisitChartFragment fragment);

    void inject(DistributionChartFragment fragment);

    void inject(SellingChartFragment fragment);

    void inject(HomeAttendanceFragment fragment);

    void inject(AttendanceListActivity attendanceListActivity);

    void inject(DistributionListActivity distributionListActivity);

    void inject(SearchActivity searchActivity);

    void inject(ScheduleActivity scheduleActivity);

    void inject(InboundListActivity inboundListActivity);

    void inject(AudianceActivity audianceActivity);

    void inject(MaterialActivity materialActivity);

    void inject(DistributionMaterialActivity distributionMaterialActivity);

    void inject(SurveyActivity surveyActivity);
}
