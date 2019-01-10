package com.app.salesapp.homeAttendance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.attendance.createattendance.PostAttendanceRequestModel;
import com.app.salesapp.channel.model.PostChannelRequestModel;
import com.app.salesapp.city.CityRequestModel;
import com.app.salesapp.draft.DraftModel;
import com.app.salesapp.network.Response;
import com.app.salesapp.service.Constants;
import com.app.salesapp.service.FetchAddressIntentService;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.Util;
import com.github.ybq.android.spinkit.style.DoubleBounce;
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
import com.mindorks.paracamera.Camera;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zcky on 3/4/18.
 */

public class HomeAttendanceFragment  extends Fragment implements  HomeAttendaceContract.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private String mParam1;
    private String mParam2;
    private String mParam3;

    private String picture = "";

    private HomeAttendance homeAttendance;

    private Context context;

    public final String MY_LOCATION = "My Location";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    String [] channel;
    String [] subject;

    private LinearLayout postLayout;

    //submit attendance
    private AutoCompleteTextView autoCompleteChannelSubmit;
    private AutoCompleteTextView autoCompleteSubjectSubmit;
    private EditText remarkETSubmit;
    private Button btnSubmit;
    private ImageButton addAttendanceBtn;
    private ImageButton addNewAttPic;
    private ProgressBar progressBar;

    ListChannelResponseModel.ChannelList selectedChannel = null;
    ListChannelResponseModel.SubjectList selectedSubject = null;

    String myLocation;

    private String mLastUpdateTime;
    private boolean mRequestingLocationUpdates = true;
    private LocationRequest mLocationRequest;

    private static final int CAMERA_PIC_REQUEST = 1337;


    private HomeAttendancePresenter homeAttendancePresenter;
    private Camera camera;
    private String photosAfter = "";

    private List<ListChannelResponseModel.ChannelList> listCh;
    private List<ListChannelResponseModel.SubjectList> listSj;

    public HomeAttendanceFragment() {
    }

    @SuppressLint("ValidFragment")
    public HomeAttendanceFragment(Context context) {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((SalesApp) context.getApplicationContext()).getSalesAppDeps().inject(this);
        setChannelAndSubject();
        this.context = context;
    }

    public static HomeAttendanceFragment newInstance(Context context, String param1, String param2, String param3) {
        HomeAttendanceFragment fragment = new HomeAttendanceFragment(context);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
        }
        homeAttendancePresenter = new HomeAttendancePresenter(this, salesAppService, userService);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initCamera();
        postLayout = (LinearLayout)getView().findViewById(R.id.postChannel);
        /*submit attendance view */
        autoCompleteChannelSubmit = (AutoCompleteTextView) getView().findViewById(R.id.autocomplete_channel_sub);
        autoCompleteSubjectSubmit = (AutoCompleteTextView) getView().findViewById(R.id.autocomplete_subbject_sub);
        remarkETSubmit =  (EditText) getView().findViewById(R.id.remarkET_sub);
        btnSubmit = (Button) getView().findViewById(R.id.btnSubmitAttendance);
        progressBar = (ProgressBar) getView().findViewById(R.id.progress_bar);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeSoftKeyboard();
                if (selectedChannel == null)
                    autoCompleteChannelSubmit.setError(getString(R.string.msg_error_channel));
                else if (selectedSubject == null)
                    autoCompleteSubjectSubmit.setError(getString(R.string.msg_error_subject));
                else if (mLastLocation == null) {
                    Toast.makeText(getActivity(), "Gagal Mendapatkan Lokasi Anda",Toast.LENGTH_SHORT).show();
                } else{
                    doSubmitAttendance();
                }
            }
        });
        listCh = new ArrayList<>();
        listSj = new ArrayList<>();

        setAutoComplete();
        getLocation();
        addAttendanceBtn = (ImageButton) getView().findViewById(R.id.addNewAttendance);
        addAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateAttendanceDialog();

            }
        });
        addNewAttPic = (ImageButton) getView().findViewById(R.id.addNewAttPic);
        addNewAttPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    camera.takePicture();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        //            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);

            }
        });
    }

    public void setAutoComplete(){
        setChannelAndSubject();
        ArrayAdapter<String> adapterChannel = new ArrayAdapter<String>(getActivity(), R.layout.list_item_auto_complete, R.id.text1, channel);
        autoCompleteChannelSubmit.setAdapter(adapterChannel);

        autoCompleteChannelSubmit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedChannel = getSelectedChannel(position);
            }
        });

        autoCompleteChannelSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!autoCompleteChannelSubmit.isPopupShowing())
                    autoCompleteChannelSubmit.showDropDown();
                return false;
            }
        });

        ArrayAdapter<String> adapterSubject = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, subject);
        autoCompleteSubjectSubmit.setAdapter(adapterSubject);

        autoCompleteSubjectSubmit.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                selectedSubject = getSelectedSubject(position);
            }
        });

        autoCompleteSubjectSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!autoCompleteSubjectSubmit.isPopupShowing())
                    autoCompleteSubjectSubmit.showDropDown();
                return false;
            }
        });
    }

    private void initCamera() {
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("Teamforce")
                .setName("Teamforce_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000).build(this);
    }

    //camera blom tau mau diapain
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            Bitmap bitmap = camera.getCameraBitmap();
            if (bitmap != null) {
                Util.movePhotoToGallery(getActivity(), camera.getCameraBitmapPath(), "Teamforce_After_"+Util.getCurrentDateTime()+"_" + System.currentTimeMillis() + ".jpg");
                photosAfter = Util.encodeToBase64(BitmapFactory.decodeFile(camera.getCameraBitmapPath()), Bitmap.CompressFormat.JPEG, 50);
            }
        }
    }

    public void showCreateAttendanceDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.new_attendance_layout, (ViewGroup)getActivity().findViewById(R.id.root));
        ImageButton cancleBtn = (ImageButton) layout.findViewById(R.id.cancleBtnAttt);
        Button btnCreateAttendance = (Button) layout.findViewById(R.id.btnCreateAttendance);
        final EditText channelName = (EditText) layout.findViewById(R.id.chName);
        final EditText channelCity = (EditText) layout.findViewById(R.id.chCity);
        final EditText channelAddress = (EditText) layout.findViewById(R.id.chAddress);
        builder.setView(layout);
        final AlertDialog dialog = builder.create();
        channelAddress.setText(myLocation);

        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postLayout.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });

        btnCreateAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (channelName.getText().toString().equals(""))
                    channelName.setError(getString(R.string.msg_error_channel));
                else if (channelCity.getText().toString().equals(""))
                    channelCity.setError(getString(R.string.msg_error_subject));
                else if (channelAddress.getText().toString().equals("")) {
                    channelAddress.setError(getString(R.string.msg_error_subject));
                } else{
                    addChannel(channelName.getText().toString(),channelCity.getText().toString(), channelAddress.getText().toString());
                    postLayout.setVisibility(View.VISIBLE);
                    dialog.dismiss();

                }
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        postLayout.setVisibility(View.GONE);

    }

    public void addChannel(String name, String city, String address){
        homeAttendancePresenter.postChannel(new PostChannelRequestModel(userService.getUserPreference().token,
                userService.getCurrentProgram(),
                name,
                address,
                city,
                mLastLocation.getLatitude(),
                mLastLocation.getLongitude()
                ));
    }

    public void getLocation(){
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        createLocationRequest();
    }

    public ListChannelResponseModel.ChannelList getSelectedChannel(int position){
        ListChannelResponseModel.ChannelList channelSelected = null;
        String channelName = autoCompleteChannelSubmit.getText().toString();
        for(int i = 0 ; i < listCh.size(); i++){
            ListChannelResponseModel.ChannelList channel = listCh.get(i);
            String nameChannel = channel.name;
            if(channelName.equals(nameChannel)){
                channelSelected = channel;
                break;
            }
        }
        return channelSelected;
    }

    public ListChannelResponseModel.SubjectList getSelectedSubject(int position){
        ListChannelResponseModel.SubjectList subject = null;
        String subjectName = autoCompleteSubjectSubmit.getText().toString();
        for(int i = 0 ; i < listSj.size(); i++){
            ListChannelResponseModel.SubjectList sbj = listSj.get(i);
            String nameSubject = sbj.name;
            if(subjectName.equals(nameSubject)){
                subject = sbj;
                break;
            }
        }
        return subject;
    }

    public void setChannelAndSubject(){
        listCh = userService.getListChannel();
        channel = new String [listCh.size()];
        for(int i = 0 ; i < listCh.size(); i++){
            ListChannelResponseModel.ChannelList cList= listCh.get(i);
            channel[i] = cList.name;
        }
        listSj = userService.getListSubject();
        subject = new String [listSj.size()];
        for(int i = 0 ; i < listSj.size(); i++){
            ListChannelResponseModel.SubjectList sList= listSj.get(i);
            subject[i] = sList.name;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setAutoComplete();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_attendace_fagment, container, false);
        return rootView;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * TODO : Send picture here. Get channel from auto complete please refer to {@link com.app.salesapp.attendance.createattendance.CreateAttendanceActivity}
     */
    private void doSubmitAttendance() {
        PostAttendanceRequestModel request;
        if (photosAfter.equals("")) {
                request = new PostAttendanceRequestModel(userService.getAccessToken(),
                        userService.getCurrentProgram(),
                        selectedChannel.users_organization_id,
                        selectedSubject.value,
                        remarkETSubmit.getText().toString(),
                        myLocation,
                        String.valueOf(mLastLocation.getLatitude()),
                        String.valueOf(mLastLocation.getLongitude()),
                        Util.getCurrentDateTime(),
                        selectedSubject.name
                );
            } else {
                request = new PostAttendanceRequestModel(userService.getAccessToken(),
                        userService.getCurrentProgram(),
                        selectedChannel.users_organization_id,
                        selectedSubject.value,
                        remarkETSubmit.getText().toString(),
                        myLocation,
                        String.valueOf(mLastLocation.getLatitude()),
                        String.valueOf(mLastLocation.getLongitude()),
                        Util.getCurrentDateTime(),
                        photosAfter,
                        selectedSubject.name
                );
            }
            homeAttendancePresenter.postAttendance(request);
    }


    @Override
    public void onSuccessPostAttendance(Response<String> response) {
        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        userService.setPostAttendanceChannel(selectedChannel);
        reInitPostAttendance();
    }

    public void reInitPostAttendance(){
        selectedChannel = null;
        selectedSubject = null;
        autoCompleteSubjectSubmit.setText("");
        autoCompleteChannelSubmit.setText("");
        photosAfter = "";
        remarkETSubmit.setText("");
    }


    @Override
    public void onErrorPostAttendance(String message, final PostAttendanceRequestModel postAttendanceRequestModel) {
        if (isAdded()) {
            AlertDialog.Builder dialog =  new  AlertDialog.Builder(getActivity())
                    .setTitle("Post Attendance")
                    .setMessage("Something wrong," + message)
                    .setPositiveButton("RETRY",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    homeAttendancePresenter.postAttendance(postAttendanceRequestModel);
                                }
                            }
                    )
                    .setNegativeButton("SAVE",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    userService.addDraft(new DraftModel(0, postAttendanceRequestModel));
                                    dialog.dismiss();
                                }
                            }
                    );
            dialog.create();
            dialog.show();
            autoCompleteSubjectSubmit.setText("");
            autoCompleteChannelSubmit.setText("");
            photosAfter = "";
            remarkETSubmit.setText("");
        }
    }

    @Override
    public void showLoading(boolean show) {
        if (isAdded() && getActivity() != null) {
            if (show) {
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            } else {
                progressBar.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        }
    }

    @Override
    public void loadSpinnerData(List<ListChannelResponseModel.ChannelList> listChannel, List<ListChannelResponseModel.SubjectList> listSubject) {

    }

    @Override
    public void onSuccessCreateChannel(Response<String> response) {
        DFragment dialog = new DFragment();
        dialog.setTitle("Post Channel");
        dialog.setMessage("Success");
        FragmentManager fm = getFragmentManager();
        dialog.show(fm,"Post Channel Success");
        homeAttendancePresenter.getNewListChannel(new ListChannelRequestModel(userService.getUserPreference().token,
                userService.getCurrentProgram()));
    }

    @Override
    public void onErrorCreateChannel(String message, PostChannelRequestModel postChannelRequestModel) {
        DFragment dialog = new DFragment();
        dialog.setTitle("Post Channel");
        dialog.setMessage("Something Went Wrong, "+message);
        FragmentManager fm = getFragmentManager();
        dialog.show(fm,"Post Channel Success");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        if(mRequestingLocationUpdates && isAdded()) {
            getMyLocation();
            mRequestingLocationUpdates = false;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(false);

    }

    public void closeSoftKeyboard(){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    public void getMyAddresLocation(Location mLastLocation) {
        if (mLastLocation != null) {
            startIntentService(mLastLocation);
        }
    }

    private AddressResultReceiver mResultReceiver;

    protected void startIntentService(Location mLastLocation) {
        if (context != null) {
            Intent intent = new Intent(context, FetchAddressIntentService.class);
            mResultReceiver = new AddressResultReceiver(new Handler());
            intent.putExtra(Constants.RECEIVER, mResultReceiver);
            intent.putExtra(Constants.LOCATION_DATA_EXTRA, mLastLocation);
            context.startService(intent);
        }
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            myLocation = resultData.getString(Constants.RESULT_DATA_KEY);
            if(myLocation.equalsIgnoreCase("serviceRest not available")) {
                getMyLocation();
            }else{
                if (isAdded()) {
                    Toast.makeText(getActivity(), myLocation, Toast.LENGTH_LONG).show();
                }

                if (resultCode == Constants.SUCCESS_RESULT) {
                    Log.d("Address Found", myLocation);
                }
            }
        }
    }

    @Override
    public void onListChannelReceived(ListChannelResponseModel data) {
        userService.saveChannelList(data.channel_list);
        userService.saveSubjectList(data.subject_list);
        setAutoComplete();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void getMyLocation() {
        if (mMap != null && mLastLocation != null) {
            mMap.clear();
            LatLng myLoc = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLoc).title(MY_LOCATION));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLoc));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);
            getMyAddresLocation(mLastLocation);
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public interface FragmentCallBack {
        void setAutoComplete();
    }

    private FragmentCallBack fragmentCallBack;

    public void setFragmentCallBack(FragmentCallBack fragmentCallBack) {
        this.fragmentCallBack = fragmentCallBack;
    }

    private void callThisWhenYouWantToCallTheOtherFragment(){
        fragmentCallBack.setAutoComplete();
    }
}
