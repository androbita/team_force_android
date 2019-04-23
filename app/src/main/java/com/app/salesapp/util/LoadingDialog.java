package com.app.salesapp.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.app.salesapp.R;
import com.github.ybq.android.spinkit.style.DoubleBounce;

public class LoadingDialog extends Dialog {
    Activity activity;
    private ProgressBar progressBar;

    public LoadingDialog(@NonNull Context context) {
        super(context);
        activity = (Activity) context;
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        if (this.getWindow() != null) {
            this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        this.setCancelable(false);
    }
}
