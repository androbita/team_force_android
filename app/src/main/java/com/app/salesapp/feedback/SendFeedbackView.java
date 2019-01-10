package com.app.salesapp.feedback;

import com.app.salesapp.feedback.model.TypeListResponseModel;
import com.app.salesapp.network.Response;

public interface SendFeedbackView {
    void showLoading(boolean show);

    void onErrorPostFeedback(String message);

    void onSuccesPostFeedback(Response<String> response);

    void showDraftFeedback(boolean show);

    void onSuccesGetTypeList(Response<TypeListResponseModel> response);
}
