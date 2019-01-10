package com.app.salesapp.chart;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.app.salesapp.chart.attendancechart.AttendanceChartFragment;
import com.app.salesapp.chart.channelvisitchart.ChannelVisitChartFragment;
import com.app.salesapp.chart.distributionchart.DistributionChartFragment;
import com.app.salesapp.chart.sellingchart.SellingChartFragment;

import java.util.ArrayList;
import java.util.List;

public class ChartPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> fragTitleList = new ArrayList<>();

    private Context context;
    private ChartMainFragmentListener fragmentListener;

    public ChartPagerAdapter(Context context, ChartMainFragmentListener fragmentListener, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.fragmentListener = fragmentListener;
    }

    public void addFrag(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragTitleList.add(title);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
            default:
                return new DistributionChartFragment(context, fragmentListener);
            case 1:
                return new SellingChartFragment(context, fragmentListener);
            case 2:
                return new AttendanceChartFragment(context, fragmentListener);
            case 3:
                return new ChannelVisitChartFragment(context, fragmentListener);
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragTitleList.get(position);
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
