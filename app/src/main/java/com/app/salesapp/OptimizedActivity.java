package com.app.salesapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

public abstract class OptimizedActivity extends AppCompatActivity {

    public Handler handler;
    public View mRootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mRootView = LayoutInflater.from(this).inflate(layoutResID, null);
        setContentView(mRootView);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mRootView = view;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        mRootView = view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacksAndMessages(null);
        unbindDrawables(mRootView);
    }

    public Handler getHandler() {
        return handler;
    }

    public static void unbindDrawables(View view) {
        if (view != null) {
            if (view.getBackground() != null) {
                view.getBackground().setCallback(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    View parent = (View) view.getParent();
                    if (parent == null || !(parent instanceof SwipeRefreshLayout)) {
                        view.setBackground(null);
                    }
                }
            }
            if (view instanceof ImageView) {
                if (((ImageView) view).getDrawable() != null) {
                    ((ImageView) view).setImageDrawable(null);
                    ((ImageView) view).setImageBitmap(null);
                }
            }

            if (view instanceof RecyclerView) {
                if (((RecyclerView) view).getAdapter() != null) {
                    int itemCount = ((RecyclerView) view).getAdapter().getItemCount();
                    for (int i = 0; i < itemCount; i++) {
                        RecyclerView.ViewHolder holder = ((RecyclerView) view).findViewHolderForAdapterPosition(i);
                        if (holder != null)
                            unbindDrawables(holder.itemView);
                    }
                }
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    unbindDrawables(((ViewGroup) view).getChildAt(i));
                }
                if (!(view instanceof AdapterView))
                    ((ViewGroup) view).removeAllViews();
            }
        }
    }

    public boolean postDelayed(Runnable r, long delayMillis) {
        return handler.postDelayed(r, delayMillis);
    }
}
