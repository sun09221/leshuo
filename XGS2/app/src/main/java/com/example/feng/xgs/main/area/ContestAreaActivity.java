package com.example.feng.xgs.main.area;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.banner.BannerCreator;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.area.company.CompanyFragment;
import com.example.feng.xgs.main.area.info.EntryInfoFragment;
import com.example.feng.xgs.main.area.model.list.StudentListFragment;
import com.example.feng.xgs.main.area.ranking.RankingFragment;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;
import com.example.feng.xgs.ui.weight.NoScrollViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/31 0031.
 * 赛区详情
 */

public class ContestAreaActivity extends ToolbarActivity implements RadioGroup.OnCheckedChangeListener, OnItemClickListener {

    @BindView(R.id.banner_find_contest_area)ConvenientBanner<String> mBanner;
    @BindView(R.id.rg_find_contest_area)
    RadioGroup mRgTab;
    @BindView(R.id.vp_find_contest_area)
    NoScrollViewPager mViewPager;
    private List<RadioButton> mTabs = new ArrayList<>();
    private int mTarIndex = 0;
    private String mIdOnly;

    @Override
    public Object setLayout() {
        return R.layout.activity_find_contest_area;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        setTitle(title);
        mIdOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        if(TextUtils.isEmpty(mIdOnly)) return;

        initBanner();
        initViewPager();
    }
     List<String> imgList;
    private void initBanner(){
        imgList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, ContentKeys.BANNER_TYPE_AREA);
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.BANNER)
                .raw(raw)
                //.toast(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray array = JSON.parseObject(response).getJSONArray("imagepath");

                        if (array != null) {


                            for (int i = 0; i < array.size(); i++) {
                                String imgUrl = array.getString(i);
                                imgList.add(imgUrl);
                            }

                        }
                        BannerCreator.setDefault(mBanner, imgList, ContestAreaActivity.this);


                    }
                })
                .build().post();
    }

    private void initViewPager(){
        int buttonCount = mRgTab.getChildCount();
        for (int i = 0; i < buttonCount; i++) {
            mTabs.add((RadioButton) mRgTab.getChildAt(i));
        }
        mRgTab.setOnCheckedChangeListener(this);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(StudentListFragment.create(mIdOnly));
        fragments.add(CompanyFragment.create(mIdOnly));
        fragments.add(EntryInfoFragment.create(mIdOnly));
        fragments.add(RankingFragment.create(mIdOnly, RankingFragment.TYPE_SHOW_HEAD));
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size() - 1);
        mTabs.get(0).setChecked(true);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < mTabs.size(); i++) {
            if(checkedId == mTabs.get(i).getId()){
                mTarIndex = i;
                Log.d(TAG, "onCheckedChanged: "+ mTarIndex);
                break;
            }
        }
        mViewPager.setCurrentItem(mTarIndex, false);
    }


    //轮播图的点击事件
    @Override
    public void onItemClick(int position) {

    }
}
