package com.app.salesapp.location;

import com.app.salesapp.common.storage.AppSessionStoreService;
import com.app.salesapp.util.Location;

public class LocationService {
    public static final String KEY_LAST_LOCATION = "LAST_LOCATION";

    private final AppSessionStoreService appSessionStoreService;

    public LocationService(AppSessionStoreService appSessionStoreService) {
        this.appSessionStoreService = appSessionStoreService;
    }

    public Location getLastKnownLocation() {
        return appSessionStoreService.getObject(KEY_LAST_LOCATION, Location.class);
    }
}
