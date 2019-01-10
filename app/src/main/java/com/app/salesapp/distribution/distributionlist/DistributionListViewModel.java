package com.app.salesapp.distribution.distributionlist;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.app.salesapp.BR;

public class DistributionListViewModel extends BaseObservable {

    private boolean isLoading;

    public void setLoading(boolean loading) {
        isLoading = loading;
        notifyPropertyChanged(BR.loadingVisibility);
    }

    @Bindable
    public int getLoadingVisibility() {
        return isLoading ? View.VISIBLE : View.GONE;
    }
}
