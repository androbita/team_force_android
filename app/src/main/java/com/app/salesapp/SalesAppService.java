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
import com.app.salesapp.network.NetworkError;
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

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SalesAppService {

    private final SalesAppNetworkService salesAppNetworkService;

    public SalesAppService(SalesAppNetworkService salesAppNetworkService) {
        this.salesAppNetworkService = salesAppNetworkService;
    }

    public Subscription doLogin(LoginRequestModel loginModel, final ServiceCallback callback) {
        return salesAppNetworkService.postLogin(loginModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<LoginResponseModel>>>() {
                    @Override
                    public Observable<? extends Response<LoginResponseModel>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<LoginResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<LoginResponseModel> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getTimeline(TimelineRequest timelineRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getTimeline(timelineRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<TimelineResponse>>>() {
                    @Override
                    public Observable<? extends Response<TimelineResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<TimelineResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<TimelineResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription postDistribution(DistributionPostRequest timelineRequest, final ServiceCallback callback) {
        return salesAppNetworkService.postDistribution(timelineRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<DistributionPostResponse>>>() {
                    @Override
                    public Observable<? extends Response<DistributionPostResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<DistributionPostResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<DistributionPostResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getSelling(SellingRequest sellingRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getSelling(sellingRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<SellingResponse>>>() {
                    @Override
                    public Observable<? extends Response<SellingResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<SellingResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<SellingResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getReceived(ReceivedRequest receivedRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getReceived(receivedRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ReceivedResponse>>>() {
                    @Override
                    public Observable<? extends Response<ReceivedResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<ReceivedResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ReceivedResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getDistribution(DistributionRequest distributionRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getDistribution(distributionRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<DistributionResponse>>>() {
                    @Override
                    public Observable<? extends Response<DistributionResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<DistributionResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<DistributionResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getTraining(TrainingRequest trainingRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getTraining(trainingRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<TrainingResponse>>>() {
                    @Override
                    public Observable<? extends Response<TrainingResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<TrainingResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<TrainingResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getSchedule(ScheduleRequest scheduleRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getSchedule(scheduleRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ScheduleResponse>>>() {
                    @Override
                    public Observable<? extends Response<ScheduleResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<ScheduleResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ScheduleResponse> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getListAttendance(ListAttendanceRequestModel listAttendanceRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getListAttendance(listAttendanceRequestModel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ListAttendanceResponseModel>>>() {
                    @Override
                    public Observable<? extends Response<ListAttendanceResponseModel>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                })
                .subscribe(new Subscriber<Response<ListAttendanceResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ListAttendanceResponseModel> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription getListChannel(ListChannelRequestModel channelRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getChannel(channelRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ListChannelResponseModel>>>() {
                    @Override
                    public Observable<? extends Response<ListChannelResponseModel>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<ListChannelResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ListChannelResponseModel> listChannelResponseModelResponse) {
                        callback.onSuccess(listChannelResponseModelResponse);
                    }
                });
    }

    public Subscription getSellingType(ListChannelRequestModel channelRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getSellingType(channelRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<SellingTypeModel>>>>() {
                                       @Override
                                       public Observable<? extends Response<List<SellingTypeModel>>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<List<SellingTypeModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<SellingTypeModel>> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }

    public Subscription getProduct(ListChannelRequestModel channelRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getProduct(channelRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<ProductModel>>>>() {
                                       @Override
                                       public Observable<? extends Response<List<ProductModel>>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<List<ProductModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<ProductModel>> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }

    public Subscription postAttendance(PostAttendanceRequestModel attendanceRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.postAttendance(attendanceRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                    @Override
                    public Observable<? extends Response<String>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postSelling(SellingReportRequestModel sellingReportRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.postSelling(sellingReportRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                    @Override
                    public Observable<? extends Response<String>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postComment(PostCommentRequest postCommentRequest, final ServiceCallback callback) {
        return salesAppNetworkService.postComment(postCommentRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                    @Override
                    public Observable<? extends Response<String>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postFeedback(final PostFeedbackRequest postFeedbackRequest, final SendFeedbackCallback callback) {
        return salesAppNetworkService.postFeedback(postFeedbackRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                    @Override
                    public Observable<? extends Response<String>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e), postFeedbackRequest);
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postDisplayed(final DisplayPostRequest displayPostRequest, final ServiceCallback callback) {
        return salesAppNetworkService.postDisplayed(displayPostRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<DisplayPostResponse>>>() {
                    @Override
                    public Observable<? extends Response<DisplayPostResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<DisplayPostResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<DisplayPostResponse> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getDetailTimeline(final DetailTimelineRequest detailTimelineRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getDetailTimeline(detailTimelineRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<DetailTimelineModel>>>() {
                    @Override
                    public Observable<? extends Response<DetailTimelineModel>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<DetailTimelineModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<DetailTimelineModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getOutboundList(final ListOutboundRequest listOutboundRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getOutboundList(listOutboundRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<InboundModel>>>() {
                    @Override
                    public Observable<? extends Response<InboundModel>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<InboundModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<InboundModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postInboundUpdate(InboundUpdateRequest inboundUpdateRequest, final ServiceCallback callback) {
        return salesAppNetworkService.postReceived(inboundUpdateRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                    @Override
                    public Observable<? extends Response<String>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postAnnouncement(PostAnnouncementRequest postAnnouncementRequest, final ServiceCallback callback) {
        return salesAppNetworkService.postAnnouncement(postAnnouncementRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                    @Override
                    public Observable<? extends Response<String>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getNotificationList(final NotificationRequest notificationRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getNotificationList(notificationRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<NotificationResponse>>>() {
                    @Override
                    public Observable<? extends Response<NotificationResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<NotificationResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<NotificationResponse> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getModule(ListChannelRequestModel channelRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getModule(channelRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<ModuleModel>>>>() {
                                       @Override
                                       public Observable<? extends Response<List<ModuleModel>>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<List<ModuleModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<ModuleModel>> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }


    public Subscription getSales(ListSalesRequest listSalesRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getListSales(listSalesRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<SalesModel>>>>() {
                                       @Override
                                       public Observable<? extends Response<List<SalesModel>>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<List<SalesModel>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<SalesModel>> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }


    public Subscription getLogo(ListAttendanceRequestModel listAttendanceRequestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getLogo(listAttendanceRequestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<String>>>>() {
                                       @Override
                                       public Observable<? extends Response<List<String>>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<List<String>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<String>> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }

    public Subscription postTraining(PostTrainingRequest postTrainingRequest, final ServiceCallback<Response<String>> callback) {
        return salesAppNetworkService.postTraining(postTrainingRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                                       @Override
                                       public Observable<? extends Response<String>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }

    public Subscription postSales(AudienceModel model, final ServiceCallback<Response<String>> callback) {
        return salesAppNetworkService.postSales(model)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<String>>>() {
                                       @Override
                                       public Observable<? extends Response<String>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<String> listResponse) {
                        callback.onSuccess(listResponse);
                    }
                });
    }

    public Subscription postListUsers(UserRequestModel request, final ServiceCallback<ResponseArray<UserResponseModel>> callback) {
        return salesAppNetworkService.postListUsers(request)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ResponseArray<UserResponseModel>>>() {
                                       @Override
                                       public Observable<? extends ResponseArray<UserResponseModel>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<ResponseArray<UserResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(ResponseArray<UserResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postCurrentActivity(StatusRequestModel request, final ServiceCallback<ResponseArray<StatusResponseModel>> callback) {
        return salesAppNetworkService.postCurrentActivity(request)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends ResponseArray<StatusResponseModel>>>() {
                                       @Override
                                       public Observable<? extends ResponseArray<StatusResponseModel>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<ResponseArray<StatusResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(ResponseArray<StatusResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postAttendanceChart(ChartRequestModel request, final ServiceCallback<Response<ChartResponseModel>> callback) {
        return salesAppNetworkService.postAttendanceChart(request)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ChartResponseModel>>>() {
                                       @Override
                                       public Observable<? extends Response<ChartResponseModel>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<ChartResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ChartResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postChannelVisitChart(ChartRequestModel request, final ServiceCallback<Response<ChartResponseModel>> callback) {
        return salesAppNetworkService.postChannelVisitChart(request)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ChartResponseModel>>>() {
                                       @Override
                                       public Observable<? extends Response<ChartResponseModel>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<ChartResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ChartResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postDistributionChart(ChartRequestModel request, final ServiceCallback<Response<ChartResponseModel>> callback) {
        return salesAppNetworkService.postDistributionChart(request)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ChartResponseModel>>>() {
                                       @Override
                                       public Observable<? extends Response<ChartResponseModel>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<ChartResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ChartResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postSellingChart(ChartRequestModel request, final ServiceCallback<Response<ChartResponseModel>> callback) {
        return salesAppNetworkService.postSellingChart(request)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<ChartResponseModel>>>() {
                                       @Override
                                       public Observable<? extends Response<ChartResponseModel>> call(Throwable throwable) {
                                           return Observable.error(throwable);
                                       }
                                   }
                ).subscribe(new Subscriber<Response<ChartResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<ChartResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getConfig(ConfigRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getConfig(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<ConfigResponseModel>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(Response<ConfigResponseModel> result) {
                                   callback.onSuccess(result);
                               }
                           }
                );
    }

    public Subscription postTracking(PostTrackingRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.postTracking(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<PostTrackingResponseModel>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(Response<PostTrackingResponseModel> result) {
                                   callback.onSuccess(result);
                               }
                           }
                );
    }

    public Subscription getSearching(SearchingRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getSearch(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ResponseArray<SearchingRequestResponseModel>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   callback.onError(new NetworkError(e));
                               }

                               @Override
                               public void onNext(ResponseArray<SearchingRequestResponseModel> result) {
                                   callback.onSuccess(result);
                               }
                           }
                );
    }

    public Subscription postChannel(PostChannelRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.postChannel(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<PostChannelResponseModel>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {
                                   callback.onError(new NetworkError(e));
                               }

                               @Override
                               public void onNext(Response<PostChannelResponseModel> result) {
                                   callback.onSuccess(result);
                               }
                           }
                );
    }

    public Subscription getListCity(CityRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getListCity(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<CityResponseModel>>() {
                               @Override
                               public void onCompleted() {

                               }

                               @Override
                               public void onError(Throwable e) {

                               }

                               @Override
                               public void onNext(Response<CityResponseModel> result) {
                                   callback.onSuccess(result);
                               }
                           }
                );
    }

    public Subscription getDistributionList(DistributionListRequest distributionListRequest, final ServiceCallback callback) {
        return salesAppNetworkService.getListDistribution(distributionListRequest)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response<DistributionPostResponseModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<DistributionPostResponseModel> result) {
                        callback.onSuccess(result);
                    }
                });
    }

    public Subscription postMaintenance(final DisplayPostRequest displayPostRequest, final ServiceCallback callback) {
        return salesAppNetworkService.postDisplayed(displayPostRequest)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<DisplayPostResponse>>>() {
                    @Override
                    public Observable<? extends Response<DisplayPostResponse>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<DisplayPostResponse>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<DisplayPostResponse> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getTypeList(final ConfigRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getTypeList(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<TypeListResponseModel>>>() {
                    @Override
                    public Observable<? extends Response<TypeListResponseModel>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<TypeListResponseModel>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<TypeListResponseModel> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription getFormData(final SurveyRequestModel requestModel, final ServiceCallback callback) {
        return salesAppNetworkService.getFormData(requestModel.getUserId(), requestModel.getProgramId())
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<DataSurveyModel>>>>() {
                    @Override
                    public Observable<? extends Response<List<DataSurveyModel>>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<List<DataSurveyModel>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<DataSurveyModel>> response) {
                        callback.onSuccess(response);
                    }
                });
    }

    public Subscription postFormData(final CustomFieldModel requestModel, final ServiceCallback callback) {

        return salesAppNetworkService.postFormData(requestModel)
                .subscribeOn((Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends Response<List<Object>>>>() {
                    @Override
                    public Observable<? extends Response<List<Object>>> call(Throwable throwable) {
                        return Observable.error(throwable);
                    }
                }).subscribe(new Subscriber<Response<List<Object>>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(new NetworkError(e));
                    }

                    @Override
                    public void onNext(Response<List<Object>> response) {
                        callback.onSuccess(response);
                    }
                });
    }


    public interface ServiceCallback<T> {
        void onSuccess(T response);

        void onError(NetworkError error);
    }

    public interface SendFeedbackCallback<T> {
        void onSuccess(T response);

        void onError(NetworkError error, PostFeedbackRequest postFeedbackRequest);
    }
}
