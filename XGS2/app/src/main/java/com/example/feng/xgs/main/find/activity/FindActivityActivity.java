package com.example.feng.xgs.main.find.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.mine.activity.ActivityFragment;
import com.example.feng.xgs.main.nearby.LocationHandler;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by feng on 2018/6/4 0004.
 * icon_find_normal：活动
 */

public class FindActivityActivity extends ToolbarActivity implements ScrollTab.OnTabListener {

    @BindView(R.id.tv_find_activity_address)TextView mTvAddress;
    @BindView(R.id.tab_find_activity)ScrollTab mTab;
    @BindView(R.id.vp_find_activity)ViewPager mViewPager;
    private LocationHandler mLocationHandler;

    @Override
    public Object setLayout() {
        return R.layout.activity_find_activity;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_activity));
        startLocation();
        initTab();
    }

    private void initTab(){
        List<String> titles = new ArrayList<>();
        titles.add("系统活动");
        titles.add("会员活动");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ActivityFragment.create(ContentKeys.FIND_ACTIVITY, ContentKeys.FIND_ACTIVITY_SERVICE));
        fragments.add(ActivityFragment.create(ContentKeys.FIND_ACTIVITY, ContentKeys.FIND_ACTIVITY_VIP));

        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mTab.setTitles(titles);
        mTab.setOnTabListener(this);
        mTab.setViewPager(mViewPager);
    }

    @Override
    public void onChange(int position, View v) {
        mViewPager.setCurrentItem(position);
    }


    /**
     * 定位成功后开始请求数据
     */
    private void startLocation() {
        mLocationHandler = LocationHandler.create(this);
        mLocationHandler.initLocation(new LocationHandler.ILocationResultListener() {
            @Override
            public void onLocationResult(AMapLocation location) {
                String city = location.getCity();
                mTvAddress.setText(city);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationHandler != null){
            mLocationHandler.onDestroy();
        }
    }
}
