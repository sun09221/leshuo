package com.example.feng.xgs.main.mine.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/9 0009.
 * 我的订单
 */

public class OrderActivity extends ToolbarActivity implements ScrollTab.OnTabListener {

    @BindView(R.id.tab_order)ScrollTab mTab;
    @BindView(R.id.vp_order)ViewPager mViewPager;
    @Override
    public Object setLayout() {
        return R.layout.activity_mine_order;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_order));

        initTab();
    }

    private void initTab(){
        List<String> titles = new ArrayList<>();
      //  titles.add("购物订单");
        titles.add("VIP订单");
        titles.add("礼物订单");

        List<Fragment> fragments = new ArrayList<>();
     //   fragments.add(OrderFragment.create(ContentKeys.ORDER_SHOPPING));
        fragments.add(OrderFragment.create(ContentKeys.ORDER_VIP));
        fragments.add(OrderFragment.create(ContentKeys.ORDER_GIFT));
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);

        mTab.setTitles(titles);
        mTab.setViewPager(mViewPager);
        mTab.setOnTabListener(this);
    }

    @Override
    public void onChange(int position, View v) {
        mViewPager.setCurrentItem(position);
    }
}
