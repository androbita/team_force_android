package com.app.salesapp.location;

public interface LocationBaseView {
    void onLocationReceived(android.location.Location location);

    void onLocationPermissionGranted();

    void onLocationPermissionDenied();
}
