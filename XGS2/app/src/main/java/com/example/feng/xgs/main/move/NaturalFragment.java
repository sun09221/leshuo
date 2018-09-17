package com.example.feng.xgs.main.move;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.BaseFragment;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/4/24 0024.
 * 本色
 */

public class NaturalFragment extends BaseFragment implements ScrollTab.OnTabListener {

    @BindView(R.id.tab_natural)ScrollTab mTab;
    @BindView(R.id.vp_natural)ViewPager mViewPager;
    @BindView(R.id.iv_natural_right)ImageView mIVRight;
    private int mPosition = 0;
    private List<Fragment> mFragments;

    //发布
    @OnClick(R.id.iv_natural_right)void onClickPublish(){
//        NaturalPublishDialog.create(getActivity()).beginShow();
    }

    public static NaturalFragment create(){
        return new NaturalFragment();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_natural;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        initTab();
    }

    private void initTab(){
        List<String> titles = new ArrayList<>();
        titles.add("关注");
        titles.add("最热");
        titles.add("视频");

        mFragments = new ArrayList<>();
        mFragments.add(NaturalChildFragment.create(CodeKeys.DYNAMIC_NATURAL_ATTENTION, ""));
        mFragments.add(NaturalChildFragment.create(CodeKeys.DYNAMIC_NATURAL_HOT, ""));
        mFragments.add(NaturalChildFragment.create(CodeKeys.DYNAMIC_NATURAL_VIDEO, ""));
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);

        mTab.setTitles(titles);
        mTab.setViewPager(mViewPager);
        mTab.setOnTabListener(this);

//        mTabMy.setTabViewPager(mViewPager, titles);

    }

    @Override
    public void onChange(int position, View v) {
        mPosition = position;
        mViewPager.setCurrentItem(position);
    }

    public void onBackPress(){
        ((NaturalChildFragment)mFragments.get(mPosition)).onBackPress();
    }



}
