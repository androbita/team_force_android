package com.app.salesapp.main;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.app.salesapp.BaseActivity;
import com.app.salesapp.Gps.ConfigRequestModel;
import com.app.salesapp.Gps.GpsSetting;
import com.app.salesapp.Gps.LocationBackgroundService;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.attendance.attendanceListActivity.AttendanceListActivity;
import com.app.salesapp.attendance.createattendance.ListChannelRequestModel;
import com.app.salesapp.attendance.createattendance.ListChannelResponseModel;
import com.app.salesapp.chart.ChartMainFragment;
import com.app.salesapp.databinding.ActivityMainBinding;
import com.app.salesapp.distribution.createdistribution.CreateDistributionActivity;
import com.app.salesapp.distribution.distributionListActivity.DistributionListActivity;
import com.app.salesapp.distribution.distributionlist.display.DisplayUpdateActivity;
import com.app.salesapp.distribution.model.ReceivedRequest;
import com.app.salesapp.distribution.model.ReceivedResponse;
import com.app.salesapp.draft.DraftActivity;
import com.app.salesapp.feedback.SendFeedbackActivity;
import com.app.salesapp.homeAttendance.HomeAttendanceFragment;
import com.app.salesapp.inbound.InboundFragment;
import com.app.salesapp.inbound.InboundListActivity;
import com.app.salesapp.login.LoginActivity;
import com.app.salesapp.login.LoginResponseModel;
import com.app.salesapp.notification.NotificationFragment;
import com.app.salesapp.salesreport.CreateSalesReportActivity;
import com.app.salesapp.salesreport.ProductModel;
import com.app.salesapp.salesreport.SellingTypeModel;
import com.app.salesapp.salesreport.sellinglist.SellingListActivity;
import com.app.salesapp.schedule.activity.ScheduleActivity;
import com.app.salesapp.search.SearchActivity;
import com.app.salesapp.service.Constants;
import com.app.salesapp.survey.SurveyActivity;
import com.app.salesapp.timeline.TimelineFragment;
import com.app.salesapp.training.CreateTrainingActivity;
import com.app.salesapp.training.list.TrainingListActivity;
import com.app.salesapp.user.UserService;
import com.app.salesapp.util.RxBus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener,
        MainView, NavigationBarView {

    @Inject
    SalesAppService salesAppService;
    @Inject
    UserService userService;
    @Inject
    RxBus rxBus;
    String currentProgramId = "";
    private boolean isTimelineOpened = true;
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private MainPresenter presenter;
    private TimelineFragment timelineFragment;
    private Spinner programIdSpinner;
    private ProgramSpinnerAdapter programIdAdapter;
    private ArrayList<LoginResponseModel.ListProgram> listPrograms;
    private NavigationBarPresenter navPresenter;
    GpsSetting gpsStatus;
    private BottomSheetBehavior mBottomSheetBehavior;

    TextView outboxBadges;
    NavigationView navigationView;
    NestedScrollView bottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        gpsStatus = new GpsSetting(this);
        gpsStatus.checkGpsState();

        viewModel = new MainViewModel();
        presenter = new MainPresenter(this, salesAppService, userService);
        navPresenter = new NavigationBarPresenter(this, userService);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);
        binding.setNavPresenter(navPresenter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_SETTLING) {
                    closeSoftKeyboard();
                    initializeCountDrawer();
                }
            }
        };
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setItemIconTintList(null);
        binding.navView.setNavigationItemSelectedListener(this);
        View header = binding.navView.getHeaderView(0);

        TextView nameText = (TextView) header.findViewById(R.id.nameTV);
        binding.appBarMain.test.navBar.setNavPresenter(navPresenter);

        if (userService.getUserPreference() != null)
            nameText.setText("Hi "+userService.getUserPreference().fullName+"!");


        if (findViewById(R.id.main_content) != null) {
            if (savedInstanceState != null) return;
            // onNavigationItemSelected(binding.navView.getMenu().getItem(0));
            // binding.navView.getMenu().getItem(0).setChecked(true);
        }

        if(!userService.getUserLogo().equals("")){
            Glide.with(getApplicationContext()).load(userService.getUserLogo())
                    .error(R.drawable.sales_club_logo)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(binding.appBarMain.layoutToolbar.appLogo);
        }
        
        programIdSpinner = (Spinner) header.findViewById(R.id.programIdSpinner);
        listPrograms = (ArrayList<LoginResponseModel.ListProgram>) userService.getUserPreference().listProgram;
        programIdAdapter = new ProgramSpinnerAdapter(this, listPrograms);
        programIdSpinner.setAdapter(programIdAdapter);
        programIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!userService.getCurrentProgram().equals(listPrograms.get(position).programId)) {
                    userService.setCurrentProgram(listPrograms.get(position).programId);
                    presenter.getListChannel(new ListChannelRequestModel(userService.getUserPreference().token,
                            userService.getCurrentProgram()));
                    presenter.doGetLogo();
                    presenter.getConfig(new ConfigRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));
                }
                openHomePage();
                binding.drawerLayout.closeDrawers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        for (int i = 0; i < listPrograms.size(); i++) {
            if (listPrograms.get(i).programId.equals(userService.getCurrentProgram())) {
                programIdSpinner.setSelection(i);
                break;
            }
        }

        Glide.with(getApplicationContext()).load(userService.getUserLogo())
                .error(R.drawable.sales_club_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.appBarMain.layoutToolbar.appLogo);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        outboxBadges = (TextView) MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_outbox));

        binding.appBarMain.layoutToolbar.pendingCountTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDraftPage();
            }
        });

        startServiceAutomaticly();

        bottomSheet = binding.appBarMain.test.navBar.bottomSheet;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        //80 is visible bottom menu height
        mBottomSheetBehavior.setPeekHeight((int) (80 * Resources.getSystem().getDisplayMetrics().density));
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        presenter.getConfig(new ConfigRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    public void collapsingBottomSheet(){
        bottomSheet = binding.appBarMain.test.navBar.bottomSheet;
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void initializeCountDrawer(){
        if (outboxBadges != null) {
            outboxBadges.setGravity(Gravity.CENTER_VERTICAL);
            outboxBadges.setTypeface(null, Typeface.BOLD);
            outboxBadges.setTextColor(getResources().getColor(R.color.white));

            FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
            outboxBadges.setLayoutParams(lParams);
            outboxBadges.setGravity(Gravity.CENTER);

            if (userService.getListDraft().size() > 0) {
                outboxBadges.setVisibility(View.VISIBLE);
                outboxBadges.setText(String.valueOf(userService.getListDraft().size()));
                outboxBadges.setBackground(getResources().getDrawable(R.drawable.badges));
            }else{
                outboxBadges.setVisibility(View.GONE);
                outboxBadges.setText("");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                openSearchPage();
                return false;
            }
        });
        return true;
    }

    public void startServiceAutomaticly(){
        if(userService.hasConfigPreference() ){
            startLocationServiceTracking();
        }
    }

    private void openDraftPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, DraftActivity.class);
        startActivity(i);
    }

    public void openFeedBackPage(){
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, SendFeedbackActivity.class);
        startActivity(i);
    }

    @Override
    public void openLogoutDialog() {
        showSignoutAlertDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeCountDrawer();

        if (userService.getListDraft().size() > 0) {
            binding.appBarMain.layoutToolbar.pendingCountTV.setVisibility(View.GONE);
            binding.appBarMain.layoutToolbar.pendingCountTV.setText(String.valueOf(userService.getListDraft().size()));
        } else {
            binding.appBarMain.layoutToolbar.pendingCountTV.setVisibility(View.GONE);
        }
        presenter.setActiveIcon(userService.getNotifications(), isTimelineOpened, true);
        gpsStatus.checkGpsState();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            boolean fromNotification = extras.getBoolean(Constants.IS_NOTIFICATION, false);
            if (fromNotification) openNotificationPage();
        }
    }


    private void showSignoutAlertDialog() {
        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Logout")
                .setMessage("Apakah anda yakin untuk keluar?")
                .setCancelable(false)
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        userService.clearData();
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).create();

        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        presenter.navigationSelected(item.getItemId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void openAttendancePage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, AttendanceListActivity.class);
        startActivity(i);
    }

    public void openSearchPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(i);
    }

    @Override
    public void openDraftPages() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, DraftActivity.class);
        startActivity(i);
    }

    @Override
    public void openHomePage() {
        showContent(HomeAttendanceFragment.newInstance(getApplicationContext(), "", "", ""), false);
    }

    @Override
    public void openTimelinePage() {
        binding.navView.getMenu().getItem(0).setChecked(true);
        showContent(TimelineFragment.newInstance(getApplicationContext()), false);
    }

    @Override
    public void openSellingListPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, SellingListActivity.class);
        startActivity(i);
    }

    @Override
    public void openDistributionListPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, DistributionListActivity.class);
        startActivity(i);
    }

    @Override
    public void openReceivedPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, InboundListActivity.class);
        startActivity(i);
    }

    @Override
    public void openSurveyPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, SurveyActivity.class);
        startActivity(i);
    }

    @Override
    public void openSalesReportPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, CreateSalesReportActivity.class);
        startActivity(i);
    }

    @Override
    public void openDistributionPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, CreateDistributionActivity.class);
        startActivity(i);
    }

    @Override
    public void openTrainingPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, CreateTrainingActivity.class);
        startActivity(i);
    }

    @Override
    public void openStatusPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, SendFeedbackActivity.class);
        startActivity(i);
    }

    @Override
    public void openMaintenancePage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, DisplayUpdateActivity.class);
        startActivity(i);
    }

    @Override
    public void openTrainingListPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, TrainingListActivity.class);
        startActivity(i);
    }


    @Override
    public void openScheduleListPage() {
        collapsingBottomSheet();
        Intent i = new Intent(MainActivity.this, ScheduleActivity.class);
        startActivity(i);
    }

    @Override
    public void showLoading(boolean show) {

    }

    @Override
    public void changeLogo(String logo) {
        Glide.with(getApplicationContext()).load(logo)
                .error(R.drawable.sales_club_logo)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.appBarMain.layoutToolbar.appLogo);
    }

    @Override
    public void onListChannelReceived(ListChannelResponseModel data) {
        userService.saveChannelList(data.channel_list);
        userService.saveSubjectList(data.subject_list);
        openHomePage();
        presenter.getListProduct(new ListChannelRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));
    }

    @Override
    public void onListProductReceived(List<ProductModel> data) {
        userService.saveProductList(data);
        presenter.getListSellingType(new ListChannelRequestModel(userService.getUserPreference().token, userService.getCurrentProgram()));
    }

    @Override
    public void onListSellingTypeReceived(List<SellingTypeModel> data) {
        userService.saveListSellingType(data);
        presenter.getReceived(new ReceivedRequest(userService.getUserPreference().token, userService.getCurrentProgram()));
    }

    @Override
    public void onReceiveListReceived(List<ReceivedResponse.ReceivedList> receivedLists) {
        userService.saveReceiveList(receivedLists);
    }

    @Override
    public void showNotificationIcon(boolean isHasNewNotification, boolean isTimelineOpened, boolean isNotificationOpened) {
        if (isTimelineOpened) {
            binding.appBarMain.test.navBar.timelineIcon.setImageResource(R.drawable.icon_home);
            if (isHasNewNotification) {
                binding.appBarMain.test.navBar.notificationIcon.setImageResource(R.drawable.icon_notif);
            } else {
                binding.appBarMain.test.navBar.notificationIcon.setImageResource(R.drawable.icon_notif);
            }
        } else {
            binding.appBarMain.test.navBar.timelineIcon.setImageResource(R.drawable.icon_home);
            if (isNotificationOpened) {
                if (isHasNewNotification) {
                    binding.appBarMain.test.navBar.notificationIcon.setImageResource(R.drawable.icon_notif);
                } else {
                    binding.appBarMain.test.navBar.notificationIcon.setImageResource(R.drawable.icon_notif);
                }
            } else {
                binding.appBarMain.test.navBar.notificationIcon.setImageResource(R.drawable.icon_notif);
            }
        }
    }

    @Override
    public void openInboundListPage() {
        showContent(InboundFragment.newInstance(getApplicationContext(), "", ""), false);
    }

    @Override
    public void updateMenu(ArrayList<String> availableMenus) {
        Menu menu = binding.navView.getMenu();
        presenter.updateSideMenuVisibility(availableMenus, menu);
        presenter.updateBottomMenuVisibility(availableMenus, binding.appBarMain.test.navBar);
    }

    public void showContent(Fragment paramFragment, boolean paramBoolean) {
        collapsingBottomSheet();
        if (!isFinishing()) {
            try {
                FragmentTransaction localFragmentTransaction = getSupportFragmentManager().beginTransaction();
                localFragmentTransaction.replace(R.id.main_content, paramFragment);

                if (paramBoolean)
                    localFragmentTransaction.addToBackStack(null);
                localFragmentTransaction.commitAllowingStateLoss();
            } catch (Exception ex) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public void openNotificationPage() {
        isTimelineOpened = false;
        showContent(NotificationFragment.newInstance(getApplicationContext(), "", ""), false);
        presenter.setActiveIcon(userService.getNotifications(), isTimelineOpened, true);
    }

    @Override
    public void openTimelineNavigationBarPage() {
        isTimelineOpened = true;
        openTimelinePage();
        presenter.setActiveIcon(userService.getNotifications(), isTimelineOpened, false);
    }

    @Override
    public void openChartNavigationBarPage() {
        isTimelineOpened = false;
        showContent(ChartMainFragment.newInstance(getApplicationContext(), "", ""), false);
        presenter.setActiveIcon(userService.getNotifications(), isTimelineOpened, false);
    }

    @Override
    public void openHomeNavigationPage() {
        isTimelineOpened = false;
        showContent(HomeAttendanceFragment.newInstance(getApplicationContext(), "", "", ""), false);
        presenter.setActiveIcon(userService.getNotifications(), isTimelineOpened, false);
    }


    @Override
    public void startLocationServiceTracking(){
        int INTERVAL_DAY = Integer.valueOf(userService.getFrequecy()) * 1000 * 60;
        Intent intent = new Intent(MainActivity.this, LocationBackgroundService.class);
        intent.putExtra("startService","true");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pi = PendingIntent.getService(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(this.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), INTERVAL_DAY, pi);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override public boolean dispatchTouchEvent(MotionEvent event){
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (mBottomSheetBehavior != null &&
                    mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY()))
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return super.dispatchTouchEvent(event);
    }
}
