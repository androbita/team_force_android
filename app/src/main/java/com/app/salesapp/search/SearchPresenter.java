package com.app.salesapp.search;

import android.util.Log;
import android.widget.Toast;

import com.app.salesapp.SalesAppService;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.network.ResponseArray;
import com.app.salesapp.search.model.SearchingRequestModel;
import com.app.salesapp.search.model.SearchingRequestResponseModel;
import com.app.salesapp.user.UserService;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by zcky on 3/11/18.
 */

public class SearchPresenter {
    private final SalesAppService salesAppService;
    private final SearchMapView view;

    private CompositeSubscription subscriptions;

    public SearchPresenter(SearchMapView view, SalesAppService salesAppService) {
        this.view = view;
        this.salesAppService = salesAppService;
        subscriptions = new CompositeSubscription();
    }

    public void getSearching(SearchingRequestModel searchingRequestModel) {
        Subscription subscribe = salesAppService.getSearching(searchingRequestModel, new SalesAppService.ServiceCallback<ResponseArray<SearchingRequestResponseModel>>() {
            @Override
            public void onSuccess(ResponseArray<SearchingRequestResponseModel> response) {
                Log.d("getSearching","success");
                view.OnSearchReceived(response.data);
            }

            @Override
            public void onError(NetworkError error) {
                Log.e("getSearching", error.toString());
                view.OnShowErrorToast();
            }
        });
        subscriptions.add(subscribe);
    }
}
