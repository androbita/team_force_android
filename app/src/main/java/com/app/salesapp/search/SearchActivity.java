package com.app.salesapp.search;

import android.app.SearchManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.app.salesapp.BaseActivity;
import com.app.salesapp.R;
import com.app.salesapp.SalesApp;
import com.app.salesapp.SalesAppService;
import com.app.salesapp.search.model.SearchingRequestModel;
import com.app.salesapp.search.model.SearchingRequestResponseModel;
import com.app.salesapp.user.UserService;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class SearchActivity extends BaseActivity implements SearchMapView {
    public static String FIELD_TEAM = "field_team";
    public static String MARKETING_MATERIAL = "marketing_material";
    public static String PRODUCT_SELLING = "product_selling";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    SearchPresenter presenter;

    @Inject
    SalesAppService salesAppService;

    @Inject
    UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SalesApp) getApplication()).getSalesAppDeps().inject(this);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        presenter = new SearchPresenter( this, salesAppService);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("search");
        menu.findItem(R.id.action_search).setActionView(searchView);
        return true;
    }

    public void search(String query){
        //test add marker//
//        List<LatLng> sendData = new ArrayList<>();
//        sendData.add(new LatLng(-31.952854, 115.857342));
//        sendData.add( new LatLng(-33.87365, 151.20689));
//        sendData.add(new LatLng(-27.47093, 153.0235));
//        resposeLocationCommunicator = (ResposeLocationCommunicator)getCurrentFragment();
//        resposeLocationCommunicator.onNewLocationReceived(sendData);

        presenter.getSearching(new SearchingRequestModel(userService.getUserPreference().token,userService.getCurrentProgram(),getSearchType(),query));
    }

    public String getSearchType(){
        SearchMapFragment page = getCurrentFragment();
        String type;
        int currPage = 0;
        if (page != null) {
            currPage = viewPager.getCurrentItem();
        }
        switch (currPage) {
            case 1:
                type = MARKETING_MATERIAL;
                break;
            case 2:
                type = PRODUCT_SELLING;
                break;
            default:
                type = FIELD_TEAM;
                break;
        }
        return type;
    }

    public SearchMapFragment getCurrentFragment(){
        return (SearchMapFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(SearchMapFragment.newInstance(0), "Team Visit");
        adapter.addFragment(SearchMapFragment.newInstance(1), "Marchandise");
        adapter.addFragment(SearchMapFragment.newInstance(2), "Product");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void OnSearchReceived(List<SearchingRequestResponseModel> data){
        //test on received called
        List<LatLng> sendData = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            sendData.add(new LatLng(data.get(i).getLatitude(), data.get(i).getLongitude()));
        }

        resposeLocationCommunicator = (ResposeLocationCommunicator) getCurrentFragment();
        resposeLocationCommunicator.onNewLocationReceived(sendData);
    }

    @Override
    public void OnShowErrorToast() {
        Toast.makeText(this, R.string.data_not_found, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    ResposeLocationCommunicator resposeLocationCommunicator;

    public interface ResposeLocationCommunicator{
        void onNewLocationReceived( List<LatLng> sendData);
    }
}
