package com.app.salesapp.location;

import android.location.Location;

public class LocationReceivedEvent {
    public Location location;

    public LocationReceivedEvent(Location location) {
        this.location = location;
    }
}