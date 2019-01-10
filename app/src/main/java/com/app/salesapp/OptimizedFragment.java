package com.app.salesapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.app.salesapp.log.SalesAppLog;

import static com.app.salesapp.common.SalesAppConstant.LOG_TAG;

public class OptimizedFragment extends Fragment {

    public Handler handler;
    public View mRootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (handler != null)
            handler.removeCallbacksAndMessages(null);

        if (BuildConfig.DEBUG) {
            // need to add library leak canary, if you needed
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        OptimizedActivity.unbindDrawables(mRootView);
    }

    protected void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    protected void showSnakeBar(Activity activity, String message) {
        try {
            Snackbar.make(activity.getWindow().findViewById(android.R.id.content), message,
                    Snackbar.LENGTH_SHORT)
                    .show();
        } catch (NullPointerException e) {
            SalesAppLog.e(LOG_TAG, e);
        }
    }

    public Handler getHandler() {
        return handler;
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }
}
