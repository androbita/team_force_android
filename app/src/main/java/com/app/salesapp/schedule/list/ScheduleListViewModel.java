package com.app.salesapp.schedule.list;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.app.salesapp.BR;

public class ScheduleListViewModel extends BaseObservable {

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
