package com.app.salesapp.location;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.SalesApp;
import com.app.salesapp.common.storage.AppSessionStoreService;
import com.app.salesapp.util.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

public abstract class LocationBaseActivity extends BaseActivity implements LocationBaseView {
    private static final int PERMISSIONS_REQUEST_LOCATION = 1001;
    private static final int GOOGLE_PLAY_SERVICE_ERROR_DIALOG = 10;

    @Inject
    LocationService locationService;

    @Inject
    AppSessionStoreService appSessionStoreService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        checkForLocationPermission();
    }

    private void checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION))
                showLocationRequireDialog();
            else
                requestLocationPemission();
        } else
            requestLocation();
    }

    protected void requestLocationPemission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_LOCATION);
    }

    public Location getLastKnownLocation() {
        return locationService.getLastKnownLocation();
    }

    public void requestLocation() {
        if (isServiceAvailable())
            startService(new Intent(this, LocationService.class));
    }

    @Subscribe
    public void onLocationReceivedEvent(LocationReceivedEvent event) {
        onLocationReceived(event.location);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                    onLocationPermissionGranted();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    onLocationPermissionDenied();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private boolean isServiceAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result, GOOGLE_PLAY_SERVICE_ERROR_DIALOG).show();
            }
            return false;
        }
        return true;
    }

    private void showLocationRequireDialog() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
