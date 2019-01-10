package com.app.salesapp.distribution.distributionlist.display;

import com.app.salesapp.distribution.model.DistributionPostResponseModel;
import com.app.salesapp.distribution.model.DistributionResponse;

import java.util.List;

public interface DisplayUpdateView {

    void setMaterialData(List<DistributionPostResponseModel.DistributionList> data);

    void showMessage(String message);

    void finishActivity();
}
