package com.app.salesapp.util;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("latitude")
    public double latitude;
    @SerializedName("longitude")
    public double longitude;

    @SuppressWarnings({"unused", "used by Gson"})
    public Location() {
    }

    public Location(android.location.Location lastLocation) {
        longitude = lastLocation.getLongitude();
        latitude = lastLocation.getLatitude();
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
