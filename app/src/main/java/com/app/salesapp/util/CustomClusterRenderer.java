package com.app.salesapp.util;

import android.content.Context;

import com.app.salesapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by DELL on 3/20/2018.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<PinMapItem> {

    private final Context mContext;
    private int mPage;

    public CustomClusterRenderer(Context context, GoogleMap map,
                                 ClusterManager<PinMapItem> clusterManager, int page) {
        super(context, map, clusterManager);

        mContext = context;
        mPage = page;
    }

    @Override
    protected void onBeforeClusterItemRendered(PinMapItem item,
                                                         MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.fromResource(getIcon());

        markerOptions.icon(markerDescriptor);
    }

    public int getIcon(){
        int iconNumber;
        switch (mPage) {
            case 1:
                iconNumber = R.drawable.icon_product_sales;
                break;
            case 2:
                iconNumber = R.drawable.icon_merchandise;
                break;
            default:
                iconNumber = R.drawable.icon_sales;
                break;
        }
        return iconNumber;
    }

}
