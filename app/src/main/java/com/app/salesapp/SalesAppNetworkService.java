package com.app.salesapp;

import com.app.salesapp.Gps.ConfigRequestModel;
import com.app.salesapp.Gps.ConfigResponseModel;
import com.app.salesapp.attendance.ListAttendanceRequestModel;
import com.app.salesapp.attendance.ListAttendanceResponseModel;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.attendance.createattendance.PostAttendanceRequestModel;
import com.app.salesapp.channel.model.PostChannelRequestModel;
import com.app.salesapp.channel.model.PostChannelResponseModel;
import com.app.salesapp.chart.ChartRequestModel;
import com.app.salesapp.chart.ChartResponseModel;
import com.app.salesapp.chart.StatusRequestModel;
import com.app.salesapp.chart.StatusResponseModel;
import com.app.salesapp.chart.UserRequestModel;
import com.app.salesapp.chart.UserResponseModel;
import com.app.salesapp.city.CityRequestModel;
import com.app.salesapp.city.CityResponseModel;
import com.app.salesapp.distribution.model.DisplayPostRequest;
import com.app.salesapp.distribution.model.DisplayPostResponse;
import com.app.salesapp.distribution.model.DistributionListRequest;
import com.app.salesapp.distribution.model.DistributionPostRequest;
import com.app.salesapp.distribution.model.DistributionPostResponse;
import com.app.salesapp.distribution.model.DistributionPostResponseModel;
import com.app.salesapp.distribution.model.DistributionRequest;
import com.app.salesapp.distribution.model.DistributionResponse;
import com.app.salesapp.distribution.model.ReceivedRequest;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.feedback.PostFeedbackRequest;
import com.app.salesapp.feedback.model.TypeListResponseModel;
import com.app.salesapp.inbound.InboundModel;
import com.app.salesapp.inbound.InboundUpdateRequest;
import com.app.salesapp.inbound.ListOutboundRequest;
import com.app.salesapp.login.LoginRequestModel;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.network.Response;
import com.app.salesapp.network.ResponseArray;
import com.app.salesapp.notification.DetailTimelineModel;
import com.app.salesapp.notification.DetailTimelineRequest;
import com.app.salesapp.notification.NotificationRequest;
import com.app.salesapp.notification.NotificationResponse;
import com.app.salesapp.notification.PostAnnouncementRequest;
import com.app.salesapp.posttracking.PostTrackingRequestModel;
import com.app.salesapp.posttracking.PostTrackingResponseModel;
import com.app.salesapp.salesreport.ProductModel;
import com.app.salesapp.salesreport.SellingReportRequestModel;
import com.app.salesapp.salesreport.SellingTypeModel;
import com.app.salesapp.salesreport.sellinglist.model.SellingRequest;
import com.app.salesapp.salesreport.sellinglist.model.SellingResponse;
import com.app.salesapp.schedule.model.ScheduleRequest;
import com.app.salesapp.schedule.model.ScheduleResponse;
import com.app.salesapp.search.model.SearchingRequestModel;
import com.app.salesapp.search.model.SearchingRequestResponseModel;
import com.app.salesapp.survey.DataSurveyModel;
import com.app.salesapp.survey.SurveyRequestModel;
import com.app.salesapp.survey.SurveyResponseModel;
import com.app.salesapp.survey.submit.CustomFieldModel;
import com.app.salesapp.survey.submit.SubmitSurveyRequestModel;
import com.app.salesapp.timeline.comment.PostCommentRequest;
import com.app.salesapp.timeline.model.TimelineRequest;
import com.app.salesapp.timeline.model.TimelineResponse;
import com.app.salesapp.training.AudienceModel;
import com.app.salesapp.training.ListSalesRequest;
import com.app.salesapp.training.ModuleModel;
import com.app.salesapp.training.PostTrainingRequest;
import com.app.salesapp.training.SalesModel;
import com.app.salesapp.training.model.TrainingRequest;
import com.app.salesapp.training.model.TrainingResponse;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_CHANNEL;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_DETAIL_TIMELINE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_DISTRIBUTION;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_FORM_DATA;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_LIST_ATTENDANCE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_LIST_CITY;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_LIST_NOTIFICATION;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_LIST_OUTBOUND;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_LOGO;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_MODULE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_PRODUCT;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_RECEIVED;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_SCHEDULE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_SEARCH;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_SELLING;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_TRAINING;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_TRANS_TYPE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.LIST_DISTRIBUTION;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.LOGIN;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_ANNOUNCEMENT;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_ATTENDANCE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_ATTENDANCE_CHART;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_CHANNEL;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_CHANNEL_VISIT_CHART;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_COMMENT;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_CURRENT_ACTIVITY;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_DISPLAYED;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_DISTRIBUTION;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_DISTRIBUTION_CHART;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_FEEDBACK;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_FORM_DATA;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_LIST_SALES;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_MAINTENANCE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_RECEIVED;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_SALES;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_SELLING;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_SELLING_CHART;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_TRACKING;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_TRAINING;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_LIST_USERS;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.TIMELINE;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.GET_CONFIG;
import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.TYPE_LIST;

public interface SalesAppNetworkService {

    @POST(LOGIN)
    Observable<Response<LoginResponseModel>> postLogin(@Body LoginRequestModel loginModel);

    @POST(TIMELINE)
    Observable<Response<TimelineResponse>> getTimeline(@Body TimelineRequest timelineRequest);

    @POST(POST_DISTRIBUTION)
    Observable<Response<DistributionPostResponse>> postDistribution(@Body DistributionPostRequest distributionPostRequest);

    @POST(GET_SELLING)
    Observable<Response<SellingResponse>> getSelling(@Body SellingRequest sellingRequest);

    @POST(GET_DISTRIBUTION)
    Observable<Response<DistributionResponse>> getDistribution(@Body DistributionRequest distributionRequest);

    @POST(GET_TRAINING)
    Observable<Response<TrainingResponse>> getTraining(@Body TrainingRequest trainingRequest);

    @POST(GET_SCHEDULE)
    Observable<Response<ScheduleResponse>> getSchedule(@Body ScheduleRequest scheduleRequest);

    @POST(GET_RECEIVED)
    Observable<Response<ReceivedResponse>> getReceived(@Body ReceivedRequest receivedRequest);

    @POST(GET_LIST_ATTENDANCE)
    Observable<Response<ListAttendanceResponseModel>> getListAttendance(@Body ListAttendanceRequestModel listAttendanceRequestModel);

    @POST(GET_CHANNEL)
    Observable<Response<ListChannelResponseModel>> getChannel(@Body ListChannelRequestModel listChannelRequestModel);

    @POST(POST_ATTENDANCE)
    Observable<Response<String>> postAttendance(@Body PostAttendanceRequestModel postAttendanceRequestModel);

    @POST(GET_TRANS_TYPE)
    Observable<Response<List<SellingTypeModel>>> getSellingType(@Body ListChannelRequestModel postAttendanceRequestModel);

    @POST(GET_PRODUCT)
    Observable<Response<List<ProductModel>>> getProduct(@Body ListChannelRequestModel postAttendanceRequestModel);

    @POST(POST_SELLING)
    Observable<Response<String>> postSelling(@Body SellingReportRequestModel sellingReportRequestModel);

    @POST(POST_COMMENT)
    Observable<Response<String>> postComment(@Body PostCommentRequest postCommentRequest);

    @POST(POST_FEEDBACK)
    Observable<Response<String>> postFeedback(@Body PostFeedbackRequest postFeedbackRequest);

    @POST(POST_DISPLAYED)
    Observable<Response<DisplayPostResponse>> postDisplayed(@Body DisplayPostRequest displayPostRequest);

    @POST(GET_DETAIL_TIMELINE)
    Observable<Response<DetailTimelineModel>> getDetailTimeline(@Body DetailTimelineRequest detailTimelineRequest);

    @POST(GET_LIST_OUTBOUND)
    Observable<Response<InboundModel>> getOutboundList(@Body ListOutboundRequest listOutboundRequest);

    @POST(POST_RECEIVED)
    Observable<Response<String>> postReceived(@Body InboundUpdateRequest inboundUpdateRequest);

    @POST(POST_ANNOUNCEMENT)
    Observable<Response<String>> postAnnouncement(@Body PostAnnouncementRequest postAnnouncementRequest);

    @POST(GET_LIST_NOTIFICATION)
    Observable<Response<NotificationResponse>> getNotificationList(@Body NotificationRequest listOutboundRequest);

    @POST(GET_MODULE)
    Observable<Response<List<ModuleModel>>> getModule(@Body ListChannelRequestModel postAttendanceRequestModel);

    @POST(GET_LOGO)
    Observable<Response<List<String>>> getLogo(@Body ListAttendanceRequestModel listAttendanceRequestModel);

    @POST(POST_TRAINING)
    Observable<Response<String>> postTraining(@Body PostTrainingRequest postTraining);

    @POST(POST_SALES)
    Observable<Response<String>> postSales(@Body AudienceModel audienceModel);

    @POST(POST_LIST_USERS)
    Observable<ResponseArray<UserResponseModel>> postListUsers(@Body UserRequestModel requestModel);

    @POST(POST_CURRENT_ACTIVITY)
    Observable<ResponseArray<StatusResponseModel>> postCurrentActivity(@Body StatusRequestModel requestModel);

    @POST(POST_ATTENDANCE_CHART)
    Observable<Response<ChartResponseModel>> postAttendanceChart(@Body ChartRequestModel requestModel);

    @POST(POST_CHANNEL_VISIT_CHART)
    Observable<Response<ChartResponseModel>> postChannelVisitChart(@Body ChartRequestModel requestModel);

    @POST(POST_DISTRIBUTION_CHART)
    Observable<Response<ChartResponseModel>> postDistributionChart(@Body ChartRequestModel requestModel);

    @POST(POST_SELLING_CHART)
    Observable<Response<ChartResponseModel>> postSellingChart(@Body ChartRequestModel requestModel);

    @POST(POST_LIST_SALES)
    Observable<Response<List<SalesModel>>> getListSales(@Body ListSalesRequest requestModel);

    @POST(GET_CONFIG)
    Observable<Response<ConfigResponseModel>> getConfig(@Body ConfigRequestModel requestModel);

    @POST(POST_TRACKING)
    Observable<Response<PostTrackingResponseModel>> postTracking(@Body PostTrackingRequestModel postTrackingRequestModel);

    @POST(GET_SEARCH)
    Observable<ResponseArray<SearchingRequestResponseModel>> getSearch(@Body SearchingRequestModel SearchingRequestModel);

    @POST(POST_CHANNEL)
    Observable<Response<PostChannelResponseModel>> postChannel(@Body PostChannelRequestModel postChannelRequestModel);

    @POST(GET_LIST_CITY)
    Observable<Response<CityResponseModel>> getListCity(@Body CityRequestModel cityRequestModel);

    @POST(LIST_DISTRIBUTION)
    Observable<Response<DistributionPostResponseModel>> getListDistribution(@Body DistributionListRequest distributionListRequest);

    @POST(POST_MAINTENANCE)
    Observable<Response<DisplayPostResponse>> postMaintenance(@Body DisplayPostRequest displayPostRequest);

    @POST(TYPE_LIST)
    Observable<Response<TypeListResponseModel>> getTypeList(@Body ConfigRequestModel requestModel);

    @POST(GET_FORM_DATA)
    Observable<Response<List<DataSurveyModel>>> getFormData(@Query("token") String userId,
                                                            @Query("programid") String programId);

    @POST(POST_FORM_DATA)
    Observable<Response<List<Object>>> postFormData(@Body CustomFieldModel submitSurveyRequestModel);

    class SalesAppApi {
        public static final String LOGIN = "?r=Teamforce/base/postLogin";
        public static final String TIMELINE = "?r=Teamforce/activity/getTimeline";
        public static final String GET_CHANNEL = "?r=Teamforce/base/listChannel";
        public static final String GET_LIST_ATTENDANCE = "?r=Teamforce/attendance/getAttendance";
        public static final String POST_ATTENDANCE = "?r=Teamforce/attendance/postAttendance";
        public static final String POST_DISTRIBUTION = "?r=Teamforce/distribution/postDistributions";

        public static final String GET_TRANS_TYPE = "?r=Teamforce/base/listSellingType";
        public static final String GET_PRODUCT = "?r=Teamforce/base/listProduct";
        public static final String GET_MODULE = "?r=Teamforce/base/listModule";
        public static final String POST_SELLING = "?r=Teamforce/selling/postSelling";
        public static final String GET_SELLING = "?r=Teamforce/selling/getSelling";
        public static final String GET_DISTRIBUTION = "?r=Teamforce/distribution/getDistribution";
        public static final String GET_TRAINING = "?r=Teamforce/training/getTraining";
        public static final String GET_SCHEDULE = "?r=Teamforce/activity/getSchedule";
        public static final String GET_RECEIVED = "?r=Teamforce/distribution/listReceived";
        public static final String POST_COMMENT = "?r=Teamforce/activity/postComment";
        public static final String POST_FEEDBACK = "?r=Teamforce/activity/postStatus";
        public static final String POST_DISPLAYED = "?r=Teamforce/maintenance/postMaintenance";
        public static final String GET_DETAIL_TIMELINE = "?r=Teamforce/activity/timelineDetail";
        public static final String GET_LIST_OUTBOUND = "?r=Teamforce/receive/getOutbound";
        public static final String POST_RECEIVED = "?r=Teamforce/receive/postReceived";
        public static final String POST_ANNOUNCEMENT = "?r=Teamforce/default/postAnnouncement ";

        public static final String GET_LIST_NOTIFICATION = "?r=Teamforce/base/getNotification";
        public static final String GET_LOGO = "?r=Teamforce/base/getLogo";
        public static final String POST_TRAINING = "?r=Teamforce/training/postTraining";
        public static final String POST_SALES = "?r=Teamforce/training/postSales";

        public static final String POST_LIST_USERS = "?r=Teamforce/dashboard/listUsers";
        public static final String POST_CURRENT_ACTIVITY = "?r=Teamforce/dashboard/getLastActivity";
        public static final String POST_ATTENDANCE_CHART = "?r=Teamforce/dashboard/getTabAttendance";
        public static final String POST_CHANNEL_VISIT_CHART = "?r=Teamforce/dashboard/getTabVisit";
        public static final String POST_DISTRIBUTION_CHART = "?r=Teamforce/dashboard/getTabDistribution";
        public static final String POST_SELLING_CHART = "?r=Teamforce/dashboard/getTabSelling";
        public static final String POST_LIST_SALES = "?r=Teamforce/base/listSales";
        public static final String GET_CONFIG = "?r=Teamforce/base/getConfig";
        public static final String POST_TRACKING = "?r=Teamforce/tracking/postTracking";
        public static final String GET_SEARCH = "?r=Teamforce/tracking/getSearching";
        public static final String POST_CHANNEL="?r=Teamforce/channel/postChannel";
        public static final String GET_LIST_CITY="?r=Teamforce/base/listCity ";
        public static final String LIST_DISTRIBUTION ="?r=Teamforce/maintenance/listDistribution";
        public static final String POST_MAINTENANCE = "?r=Teamforce/maintenance/postMaintenance";
        public static final String TYPE_LIST = "?r=Teamforce/base/listTypeShare";
        public static final String GET_FORM_DATA = "?r=Teamforce/customField/getFormData";
        public static final String POST_FORM_DATA = "?r=Teamforce/customField/submitPostData";
    }
}
