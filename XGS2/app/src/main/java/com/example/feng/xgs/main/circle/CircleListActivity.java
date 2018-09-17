package com.example.feng.xgs.main.circle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.NaturalChildFragment;
import com.example.feng.xgs.main.move.publish.NaturalPublishDialog;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class CircleListActivity extends EmptyActivity {
    @BindView(R.id.include_toolbar)
    RelativeLayout includeToolbar;
    @BindView(R.id.tv_toolbar_title)
    AppCompatTextView tv_toolbar_title;
    @BindView(R.id.iv_circle_logo)
    ImageView ivCircleLogo;
    @BindView(R.id.tv_circle_title)
    TextView tvCircleTitle;
    @BindView(R.id.tv_watch_num)
    TextView tvWatchNum;
    @BindView(R.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R.id.tv_add_circle)
    TextView tvAddCircle;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.iv_release)
    ImageView ivRelease;
    private MyAdapter adapter;
    private String[] titles = {"全部"};

    @Override
    public RecyclerView getRecyclerView() {
        return null;
    }

    @Override
    public void emptyLoadData() {

    }

    private List<Fragment> fragmentList;

    private void initTab() {
        fragmentList = new ArrayList<>();
       // fragmentList.add(NaturalChildFragment.create(1, id));
        fragmentList.add(NaturalChildFragment.create(0, id));
        adapter = new MyAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onRefresh() {

    }


    @OnClick({R.id.iv_toolbar_back, R.id.iv_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_toolbar_back:
                finish();
                break;
            case R.id.iv_release:
                NaturalPublishDialog.create(this).beginShow(id);
                break;
        }
    }


    class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        //重写这个方法，将设置每个Tab的标题
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }


    }

    @Override
    public Object setLayout() {
        return R.layout.activity_circle_layout;
    }

    private String id = "";
    private String imagePath = "";
    private String names="";

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        imagePath = intent.getStringExtra("imagepath");
        names= intent.getStringExtra("name");
        tv_toolbar_title.setText(names);
        initTab();
        initMsg();
    }

    private void initMsg() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put("id", id);
        LogUtils.i("id", id);
        mUrl = UrlKeys.CIRCLE_MSG;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        tvCircleTitle.setText(objectData.getString("name"));
                        tvWatchNum.setText(objectData.getString("peoplenum"));
                        loadImg(imagePath, ivCircleLogo);
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        ToastUtils.showText(CircleListActivity.this, "获取详情失败");

                    }
                })
                .build().post();
    }
}
