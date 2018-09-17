package com.example.feng.xgs.main.mine.earnings;

import android.content.Intent;
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
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/8 0008.
 * 我的收益
 */

public class EarningsActivity extends ToolbarActivity{

    @BindView(R.id.tab_earnings)ScrollTab mTab;
    @BindView(R.id.vp_earnings)ViewPager mViewPager;

    //提现
    @OnClick(R.id.tv_mine_earning_withdraw)void onClickWithdraw(){
        startActivity(new Intent(this, WithdrawActivity.class));
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_earnings;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_earnings));
        initTab();
    }

    private void initTab(){
        List<String> titles = new ArrayList<>();
        titles.add("购物收益");
        titles.add("VIP收益");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(EarningsFragment.create(ContentKeys.MINE_EARNINGS_SHOP));
        fragments.add(EarningsFragment.create(ContentKeys.MINE_EARNINGS_VIP));
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mTab.setTitles(titles);
        mTab.setViewPager(mViewPager);

        mTab.setOnTabListener(new ScrollTab.OnTabListener() {
            @Override
            public void onChange(int position, View v) {
                mViewPager.setCurrentItem(position);
            }
        });

    }
}
