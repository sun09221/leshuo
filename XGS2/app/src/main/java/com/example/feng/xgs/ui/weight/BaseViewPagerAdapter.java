package com.example.feng.xgs.ui.weight;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by feng on 2018/4/26 0026.
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> mFragments;
    private List<String> mTitles;

    public BaseViewPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(mTitles != null){
            return mTitles.get(position);
        }
        return super.getPageTitle(position);
    }

    public void setTitle(List<String> titles){
        this.mTitles = titles;
    }
}
