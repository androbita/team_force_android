package com.app.salesapp.feedback;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.salesapp.Gps.ConfigRequestModel;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.feedback.model.TypeListResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import java.util.ArrayList;

public class SendFeedbackPresenter {

    SendFeedbackView view;
    SalesAppService salesAppService;
    CompositeSubscription subscriptions;
    UserService userService;

    public SendFeedbackPresenter(SendFeedbackView view, SalesAppService salesAppService, UserService userService) {
        this.view = view;
        this.salesAppService = salesAppService;
        this.userService = userService;

        subscriptions = new CompositeSubscription();
    }

    public void postFeedback(final PostFeedbackRequest postFeedbackRequest) {
        view.showLoading(true);
        if(!postFeedbackRequest.getImagePath().equals("")) {
            Bitmap myBitmap = BitmapFactory.decodeFile(postFeedbackRequest.getImagePath());
            postFeedbackRequest.setPicture( Util.encodeToBase64(myBitmap, Bitmap.CompressFormat.JPEG, 50));
        }

        Subscription subscription = salesAppService.postFeedback(postFeedbackRequest, new SalesAppService.SendFeedbackCallback<Response<String>>() {
            @Override
            public void onSuccess(Response<String> response) {
                view.showLoading(false);

                if (response.isSuccess())
                    view.onSuccesPostFeedback(response);
                else
                    view.onErrorPostFeedback(response.message);
            }

            @Override
            public void onError(NetworkError error,PostFeedbackRequest postFeedbackRequest1) {
                view.showLoading(false);
                ArrayList<PostFeedbackRequest> postFeedbackRequestsList = userService.getDraftFeedback();
                postFeedbackRequestsList.add(postFeedbackRequest);
                userService.saveDraftFeedback(postFeedbackRequestsList);
                view.onErrorPostFeedback(error.getAppErrorMessage());
            }

        });
        subscriptions.add(subscription);
    }

    public void getTypeList(final ConfigRequestModel requestModel) {
        Subscription subscribe = salesAppService.getTypeList(requestModel, new SalesAppService.ServiceCallback<Response<TypeListResponseModel>>() {
            @Override
            public void onSuccess(Response<TypeListResponseModel> response) {
                view.onSuccesGetTypeList(response);
            }
            @Override
            public void onError(NetworkError error) {

            }
        });
        subscriptions.add(subscribe);
    }


    public void onDestroy() {
        subscriptions.clear();
    }
}
