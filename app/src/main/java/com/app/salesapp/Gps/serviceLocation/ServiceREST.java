package com.app.salesapp.Gps.serviceLocation;

import com.app.salesapp.SalesAppNetworkService;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.feedback.PostFeedbackRequest;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.posttracking.PostTrackingRequestModel;
import com.app.salesapp.posttracking.PostTrackingResponseModel;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zcky on 2/22/18.
 */

public class ServiceREST {
    SalesAppNetworkService salesAppNetworkService;

    public ServiceREST(){
        salesAppNetworkService = ServiceApi.getClient().create(SalesAppNetworkService.class);
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

    public interface ServiceCallback<T> {
        void onSuccess(T response);

        void onError(NetworkError error);
    }

    public interface SendFeedbackCallback<T> {
        void onSuccess(T response);

        void onError(NetworkError error, PostFeedbackRequest postFeedbackRequest);
    }

}
