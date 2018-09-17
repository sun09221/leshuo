package com.example.feng.xgs.main.nearby.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.BaseActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.nearby.NearbyFragment;

/**
 * Created by feng on 2018/6/21 0021.
 * 首页搜索
 */

public class SearchPersonActivity extends BaseActivity{
    @Override
    public Object setLayout() {
        return R.layout.activity_nearby_search_person;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String searchInfo = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.ly_search_person, NearbyFragment.create(CodeKeys.NEARBY_SEARCH, searchInfo,""));
        transaction.commit();
    }
}
