package com.app.salesapp.main;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

public class MainViewModel {


    @BindingAdapter({"android:src"})
    public static void setImageViewResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }
}
