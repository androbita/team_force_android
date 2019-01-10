package com.app.salesapp.attendance.createattendance;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.draft.DraftModel;
import com.app.salesapp.network.Response;
import com.app.salesapp.service.Constants;
import com.app.salesapp.service.FetchAddressIntentService;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CreateAttendanceActivity extends BaseActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        CreateAttendanceView, LocationListener {

    public final String MY_LOCATION = "My Location";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private TextView myLocation;
    private CreateAttendancePresenter createAttendancePresenter;

    @Inject
    public UserService userService;

    @Inject
    public SalesAppService salesAppService;

    private AutoCompleteTextView autoCompleteChannel;
    private AutoCompleteTextView autoCompleteSubject;
    private ChannelSpinnerAdapter channelAdapter;
    private SubjectSpinnerAdapter subjectAdapter;
    private ArrayList<ListChannelResponseModel.ChannelList> channelLists;
    private ArrayList<ListChannelResponseModel.SubjectList> subjectLists;

    private ImageView myLocationBtn;
    private ImageView btnSubmit;
    private EditText remarkET;
    private RelativeLayout progressLayout;
    ListChannelResponseModel.ChannelList selectedChannel = null;
    ListChannelResponseModel.SubjectList selectedSubject = null;
    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_attendance);

        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_keyboard_arrow_left_36dp, null);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        ImageView logo = (ImageView) findViewById(R.id.app_logo);
        Glide.with(getApplicationContext()).load(userService.getUserLogo())
                .error(R.drawable.sales_club_logo)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(logo);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        myLocation = (TextView) findViewById(R.id.myLocation);

        mResultReceiver = new AddressResultReceiver(new Handler());

        createAttendancePresenter = new CreateAttendancePresenter(this, salesAppService, userService);

        channelLists = new ArrayList<>();
        channelAdapter = new ChannelSpinnerAdapter(this, channelLists);
        autoCompleteChannel = (AutoCompleteTextView) findViewById(R.id.autocomplete_channel);
        channelAdapter.setDropDownViewResource(R.layout.spinner_view_resource);
        autoCompleteChannel.setAdapter(channelAdapter);
        autoCompleteChannel.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedChannel = (ListChannelResponseModel.ChannelList) parent.getItemAtPosition(position);
            }
        });

        autoCompleteChannel.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!autoCompleteChannel.isPopupShowing())
                    autoCompleteChannel.showDropDown();
                return false;
            }
        });

        subjectLists = new ArrayList<>();
        subjectAdapter = new SubjectSpinnerAdapter(this, subjectLists);
        autoCompleteSubject = (AutoCompleteTextView) findViewById(R.id.autocomplete_activity);
        subjectAdapter.setDropDownViewResource(R.layout.spinner_view_resource);
        autoCompleteSubject.setAdapter(subjectAdapter);
        autoCompleteSubject.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedSubject = (ListChannelResponseModel.SubjectList) parent.getItemAtPosition(position);
            }
        });

        autoCompleteSubject.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!autoCompleteSubject.isPopupShowing())
                    autoCompleteSubject.showDropDown();
                return false;
            }
        });


        myLocationBtn = (ImageView) findViewById(R.id.imgMyLocation);
        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestingLocationUpdates = true;
                getMyLocation();
            }
        });
        remarkET = (EditText) findViewById(R.id.remarkET);
        btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSoftKeyboard();
                if (selectedChannel == null)
                    autoCompleteChannel.setError(getString(R.string.msg_error_channel));
                else if (selectedSubject == null)
                    autoCompleteSubject.setError(getString(R.string.msg_error_subject));
                else if (mLastLocation == null) {
                    showSnackBar(CreateAttendanceActivity.this, "Gagal Mendapatkan Lokasi Anda");
                } else
                    doSubmitAttendance();
            }
        });
        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        showLoading(false);

        createAttendancePresenter.getListChannel(new ListChannelRequestModel(userService.getUserPreference().token,
                userService.getCurrentProgram()));

        createLocationRequest();
    }

    private void doSubmitAttendance() {
        createAttendancePresenter.postAttendance(new PostAttendanceRequestModel(userService.getAccessToken(),
                userService.getCurrentProgram(),
                selectedChannel.users_organization_id,
                selectedSubject.value,
                remarkET.getText().toString(),
                myLocation.getText().toString(),
                String.valueOf(mLastLocation.getLatitude()),
                String.valueOf(mLastLocation.getLongitude()),
                Util.getCurrentDateTime(), ""
        ));
    }

    private void getMyLocation() {
        if (mMap != null && mLastLocation != null) {
            mMap.clear();
            LatLng myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLoc).title(MY_LOCATION));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
            startIntentService();
        }
    }

    @Override
    public void onSuccessPostAttendance(Response<String> response) {
        AlertDialog dialog = new AlertDialog.Builder(CreateAttendanceActivity.this)
                .setTitle("Post Attendance")
                .setMessage("Success")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();

    }


    @Override
    public void onErrorPostAttendance(String message, final PostAttendanceRequestModel postAttendanceRequestModel) {
        AlertDialog dialog = new AlertDialog.Builder(CreateAttendanceActivity.this)
                .setTitle("Post Attendance")
                .setMessage("Something wrong, " + message)
                .setCancelable(false)
                .setPositiveButton("RETRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        doSubmitAttendance();
                        dialog.dismiss();
                    }
                }).setNegativeButton("SAVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        userService.addDraft(new DraftModel(0, postAttendanceRequestModel));
                    }
                }).create();

        dialog.show();
    }


    @Override
    public void showLoading(boolean show) {
        if (show)
            progressLayout.setVisibility(View.VISIBLE);
        else
            progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void loadSpinnerData(List<ListChannelResponseModel.ChannelList> listChannel, List<ListChannelResponseModel.SubjectList> listSubject) {
        channelLists.clear();
        channelLists.addAll(listChannel);
        channelAdapter.setTempItems(listChannel);
        channelAdapter.notifyDataSetChanged();

        subjectLists.clear();
        subjectLists.addAll(listSubject);
        subjectAdapter.setTempsItems(listSubject);
        subjectAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private AddressResultReceiver mResultReceiver;

    protected void startIntentService() {
        Intent intent = new Intent(this, FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
        startService(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            showToast("Permission Location Required");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    String mAddressOutput = "";

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if(mRequestingLocationUpdates) {
            getMyLocation();
            mRequestingLocationUpdates = false;
        }
    }


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);
            displayAddressOutput();

            if (resultCode == Constants.SUCCESS_RESULT) {
                Toast.makeText(CreateAttendanceActivity.this, "Address Found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void displayAddressOutput() {
        myLocation.setText(mAddressOutput);
    }
}