package com.app.salesapp.Gps.serviceLocation;

import com.app.salesapp.network.Response;
import com.app.salesapp.posttracking.PostTrackingRequestModel;
import com.app.salesapp.posttracking.PostTrackingResponseModel;

import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;


import static com.app.salesapp.SalesAppNetworkService.SalesAppApi.POST_TRACKING;

/**
 * Created by zcky on 2/22/18.
 */

public interface locationTrackingInterface {
    @POST(POST_TRACKING)
    Observable<Response<PostTrackingResponseModel>> postTracking(@Body PostTrackingRequestModel postTrackingRequestModel);
}
