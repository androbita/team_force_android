package com.app.salesapp.search;


import com.app.salesapp.search.model.SearchingRequestResponseModel;

import java.util.List;

/**
 * Created by zcky on 3/11/18.
 */

public interface SearchMapView {

    void OnSearchReceived(List<SearchingRequestResponseModel> data);

    void OnShowErrorToast();
}
