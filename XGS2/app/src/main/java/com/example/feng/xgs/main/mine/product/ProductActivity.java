package com.example.feng.xgs.main.mine.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.mine.product.publish.ProductPublishActivity;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/30 0030.
 * 我的产品
 */

public class ProductActivity extends ToolbarActivity implements ScrollTab.OnTabListener {

    @BindView(R.id.iv_toolbar_right)ImageView mIvPublish;
    @BindView(R.id.tab_mine_product)ScrollTab mTab;
    @BindView(R.id.vp_mine_product)ViewPager mViewPager;

    //添加产品
    @OnClick(R.id.iv_toolbar_right)void onClickPublish(){
        startActivity(new Intent(this, ProductPublishActivity.class));
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_product;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_product));

        initViewPager();
    }

    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("审核中");
        titles.add("已审核");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ProductFragment.create(ContentKeys.MINE_PRODUCT_CHECKING));
        fragments.add(ProductFragment.create(ContentKeys.MINE_PRODUCT_CHECK_FINISH));

        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mTab.setTitles(titles);
        mTab.setViewPager(mViewPager);
        mTab.setOnTabListener(this);
    }

    @Override
    public void onChange(int position, View v) {
        mViewPager.setCurrentItem(position);
    }
}
