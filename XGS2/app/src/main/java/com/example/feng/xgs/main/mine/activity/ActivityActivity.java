package com.example.feng.xgs.main.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.main.mine.activity.detail.ActivitiesPublishActivity;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/7 0007.
 * 活动
 */

public class ActivityActivity extends ToolbarActivity implements ScrollTab.OnTabListener {


    private ActivityFragment mFragmentNo;

    //发布活动
    @OnClick(R.id.iv_toolbar_right)void onClickPublish(){
        String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
        if (type.equals("0")){
            ToastUtils.showText(ActivityActivity.this,"请升级VIP");
        }else{
            startActivityForResult(new Intent(this, ActivitiesPublishActivity.class), CodeKeys.ADD_REQUEST);
        }

    }

    @BindView(R.id.tab_find_activity)ScrollTab mTab;
    @BindView(R.id.vp_find_activity)ViewPager mViewPager;


    @Override
    public Object setLayout() {
        return R.layout.activity_mine_activity;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_activity));

        initTab();
    }

    private void initTab(){
        List<String> titles = new ArrayList<>();
        titles.add("通过");
        titles.add("未通过");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ActivityFragment.create(ContentKeys.MINE_ACTIVITY, ContentKeys.FIND_ACTIVITY_ADOPT));
        mFragmentNo = ActivityFragment.create(ContentKeys.MINE_ACTIVITY, ContentKeys.FIND_ACTIVITY_ADOPT_NO);
        fragments.add(mFragmentNo);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CodeKeys.FOR_RESULT_CODE){
            if(requestCode == CodeKeys.ADD_REQUEST){
                mFragmentNo.loadData();
            }
        }
    }

}
