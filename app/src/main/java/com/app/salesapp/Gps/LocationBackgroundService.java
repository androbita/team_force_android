package com.app.salesapp.Gps;

/**
 * Created by zcky on 2/20/18.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import com.app.salesapp.Gps.serviceLocation.ServiceREST;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.network.NetworkError;
import com.app.salesapp.network.Response;
import com.app.salesapp.posttracking.PostTrackingRequestModel;
import com.app.salesapp.posttracking.PostTrackingResponseModel;
import com.app.salesapp.service.Constants;
import com.app.salesapp.service.FetchAddressIntentService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class LocationBackgroundService extends Service implements LocationListener
{
    public static final String USER_PREFERENCE = "user_preference";
    public static final String CURRENT_PROGRAM = "currentProgram";
    private static final Gson gson = new Gson();
    private static final String TRACKING_LIST = "tracking_config";
    private static final String TAG = "GPSService";
    private LocationManager mLocationManager = null;
    private int LOCATION_INTERVAL = 1000*60*30;
    private static final float LOCATION_DISTANCE = 10000;
    private String token;
    private String programId;
    private ServiceREST serviceApi = new ServiceREST();
    CompositeSubscription subscriptions = new CompositeSubscription();
    private String includeSaturday;
    private String includeSunday;
    private String startTime;
    private String stopTime;
    private Location currentLocation;
    private SharedPreferences pref;
    String mAddressOutput;
    Location location;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getIncludeSaturday() {
        return includeSaturday;
    }

    public void setIncludeSaturday(String includeSaturday) {
        this.includeSaturday = includeSaturday;
    }

    public String getIncludeSunday() {
        return includeSunday;
    }

    public void setIncludeSunday(String includeSunday) {
        this.includeSunday = includeSunday;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public void setLocationInterval(String freq){
        int frequency = Integer.parseInt(freq);
        LOCATION_INTERVAL = 1000 * 60 * frequency;
        Log.d("INTERVALUPDATE",String.valueOf(LOCATION_INTERVAL) );
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        pref = getPreferance();
        initIntervalAndOther();
        initializeLocationManager();
        requestLocation();
        sendLocationToService();
        return START_STICKY;
    }

    @Override
    public void onCreate() {

    }

    public void requestLocation(){
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    this);
            if (mLocationManager != null) {
                location = mLocationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    this);
            if (mLocationManager != null) {
                location = mLocationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        } catch (java.lang.SecurityException ex) {
        } catch (IllegalArgumentException ex) {
        }
    }

    public void sendLocationToService(){
        location = getLocation();
        setCurrentLocation(location);
        boolean isTrackingDays = isTodayHasPermissionToRunLiveTracking();
        boolean isInRangeTime = isInRangeTrackingTime();
        boolean isFrequencyAvailable = !getFrequecyPref().equals("0");
        Log.d("isTrackingDays", String.valueOf(isTrackingDays));
        Log.d("isInRangeTime", String.valueOf(isInRangeTime));
        if(isTrackingDays && isInRangeTime && isFrequencyAvailable){
            getMyAddresLocation(location);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public String getCurrentDate(){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    public void postTracking(PostTrackingRequestModel postTrackingRequestModel) {
        Subscription subscribe = serviceApi.postTracking(postTrackingRequestModel, new ServiceREST.ServiceCallback<Response<PostTrackingResponseModel>>() {
            @Override
            public void onSuccess(Response<PostTrackingResponseModel> response) {
                Log.e("postTracking", response.data.toString());
            }
            @Override
            public void onError(NetworkError error) {
                Log.e("postTracking", error.toString());
            }
        });
        subscriptions.add(subscribe);
    }

    public boolean isTodayHasPermissionToRunLiveTracking(){
        String [] date = new String[2];
        if(!getIncludeSaturday().equals("1")){
            date[0] = "Saturday";
        }
        if(!getIncludeSunday().equals("1")){
            date[1] = "Sunday";
        }
        String dateName = new SimpleDateFormat("EEE", Locale.getDefault()).format(new Date());
        boolean isTodayIncluded = true;
        if(Arrays.asList(date).indexOf(dateName) != -1){
            isTodayIncluded = false;
        }
        return isTodayIncluded;
    }

    public long getStartTimeInMillis(){
        long timeStart = 0;
        String startTime = getDate () +" "+getStartTime();
        try {
            Date time = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss",Locale.getDefault()).parse(startTime);
            timeStart = time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStart;
    }

    public long getStopTimeInMillis(){
        long timeStop = 0;
        String stopTime = getDate () +" " +getStopTime();
        try {
            Date time = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss",Locale.getDefault()).parse(stopTime);
            timeStop = time.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStop;
    }

    public String getDate(){
        return new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault()).format(new Date());
    }


    public long getCurrentTimeInMillis(){
        long timeNow = System.currentTimeMillis();
        return timeNow;
    }

    public boolean isInRangeTrackingTime(){
        boolean isInRange = false;
        Log.d("getCurrentTimeInMillis", String.valueOf(getCurrentTimeInMillis()));
        Log.d("getStartTimeInMillis", String.valueOf(getStartTimeInMillis()));
        Log.d("getStopTimeInMillis", String.valueOf(getStopTimeInMillis()));
        if(getCurrentTimeInMillis() >= getStartTimeInMillis() && getCurrentTimeInMillis() <= getStopTimeInMillis()){
            isInRange = true;
        }
        return isInRange;
    }

    public void getMyAddresLocation(Location mLastLocation) {
        if (mLastLocation != null) {
            startIntentService(mLastLocation);
        }
    }

    private AddressResultReceiver mResultReceiver;

    protected void startIntentService(Location mLastLocation) {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        mResultReceiver = new AddressResultReceiver(new Handler());
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        Log.d("StartGettingAddess", "TRUE");
        startService(intent);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public double getLatitude(){
        double latitude= 0;
        if(location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        double longitude= 0;
        if(location != null){
            longitude = location.getLongitude();
        }
        return longitude;
    }

    public Location getLocation(){
        return location;
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
           mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            Location loc = getCurrentLocation();
            postTracking(new PostTrackingRequestModel(
                    getToken(),
                    getProgramId(),
                    loc.getLatitude(),
                    loc.getLongitude(),
                    getCurrentDate(),
                    mAddressOutput
            ));
            if (resultCode == Constants.SUCCESS_RESULT) {
                Log.d("Address Found",mAddressOutput);
            }

        }
    }

    public void initIntervalAndOther(){
        setStopTime(getTimeStopPref());
        setStartTime(getTimeStartPref());
        setIncludeSunday(getIncludeSundayPref());
        setIncludeSaturday(getIncludeSaturdayPref());
        setLocationInterval(getFrequecyPref());
        setToken(getAccessToken());
        setProgramId(getCurrentProgram());
    }

    public SharedPreferences getPreferance(){
        SharedPreferences preferences = getSharedPreferences("SalesAppPref", MODE_PRIVATE);
        return preferences;
    }


    public List<ConfigResponseModel.TrackingConfig> getListTracking() {
        List<ConfigResponseModel.TrackingConfig> tracking = gson.fromJson(pref.getString(TRACKING_LIST, null), new TypeToken<ArrayList<ConfigResponseModel.TrackingConfig>>(){}.getType());
        if(tracking==null)
            tracking = new ArrayList<>();
        return tracking;
    }

    public String getFrequecyPref(){
        if (getListTracking() != null && getListTracking().size() > 0) {
            ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
            return tracking.frequency;
        } else {
            return "0";
        }
    }

    public String getIncludeSaturdayPref(){
        String trackingString = "";
        if (getListTracking() != null && !getListTracking().isEmpty()) {
            ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
            trackingString = tracking.includeSaturday;
        }
        return trackingString;
    }

    public String getIncludeSundayPref(){
        String trackingString = "";
        if (getListTracking() != null && !getListTracking().isEmpty()) {
            ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
            trackingString = tracking.includeSunday;
        }
        return trackingString;
    }

    public String getTimeStartPref(){
        String trackingString = "";
        if (getListTracking() != null && !getListTracking().isEmpty()) {
            ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
            trackingString = tracking.timeStart;
        }
        return trackingString;
    }

    public String getTimeStopPref(){
        String trackingString = "";
        if (getListTracking() != null && !getListTracking().isEmpty()) {
            ConfigResponseModel.TrackingConfig tracking = getListTracking().get(0);
            trackingString = tracking.timeStop;
        }
        return trackingString;
    }

    public String getCurrentProgram() {
        return pref.getString(CURRENT_PROGRAM, "");
    }

    public String getAccessToken() {
        if (getUserPreference() != null)
            return getUserPreference().token;
        else return "";
    }

    public LoginResponseModel getUserPreference() {
        if (!TextUtils.isEmpty(pref.getString(USER_PREFERENCE, null))) {
            return gson.fromJson(pref.getString(USER_PREFERENCE, null), LoginResponseModel.class);
        }
        return null;
    }
}
