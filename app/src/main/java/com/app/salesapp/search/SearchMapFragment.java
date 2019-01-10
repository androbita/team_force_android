package com.app.salesapp.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.salesapp.R;
import com.app.salesapp.util.CustomClusterRenderer;
import com.app.salesapp.util.PinMapItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by soeltanzaki_r on 3/8/18.
 */

public class SearchMapFragment extends Fragment implements OnMapReadyCallback, SearchActivity.ResposeLocationCommunicator {
    GoogleMap gMap = null;
    private static String ARG_PARAM1 = "page";
    private int page;
    private static final LatLng DEFAULT = new LatLng(-6.1751, 106.8650);

    public int icon;

    public SearchMapFragment() {
    }

    public CameraPosition getDefaultCameraPosition(){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(DEFAULT)
                .zoom(11)
                .build();
        return cameraPosition;
    }


    public static SearchMapFragment newInstance(int param1) {
        SearchMapFragment fragment = new SearchMapFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            page = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_fragment_search, container, false);
        initMap();
        return view;
    }

    public void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(getDefaultCameraPosition()));

        mClusterManager = new ClusterManager<PinMapItem>(getActivity(), gMap);

        gMap.setOnCameraIdleListener(mClusterManager);

        final CustomClusterRenderer renderer = new CustomClusterRenderer(getActivity(), gMap, mClusterManager, page);

        mClusterManager.setRenderer(renderer);

        mClusterManager
                .setOnClusterClickListener(new ClusterManager.OnClusterClickListener<PinMapItem>() {
                    @Override
                    public boolean onClusterClick(final Cluster<PinMapItem> cluster) {
                        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                cluster.getPosition(), (float) Math.floor(gMap
                                        .getCameraPosition().zoom + 1)), 300,
                                null);
                        return true;
                    }
                });

        mClusterManager.setOnClusterItemClickListener(
                new ClusterManager.OnClusterItemClickListener<PinMapItem>() {
                    @Override public boolean onClusterItemClick(PinMapItem clusterItem) {


                        return true;
                    }
                });
        gMap.setOnMarkerClickListener(mClusterManager);

    }

    public void setResponseMarker( List<LatLng> sendData){
        mClusterManager.clearItems();
        for(int i = 0 ; i < sendData.size(); i++){
            PinMapItem offsetItem = new PinMapItem(sendData.get(i).latitude, sendData.get(i).longitude, "title", "snippet");
            mClusterManager.addItem(offsetItem);
        }

        mClusterManager.cluster();

    }

    @Override
    public void onNewLocationReceived(List<LatLng> sendData) {
        setResponseMarker(sendData);
    }


    private ClusterManager<PinMapItem> mClusterManager;

}
