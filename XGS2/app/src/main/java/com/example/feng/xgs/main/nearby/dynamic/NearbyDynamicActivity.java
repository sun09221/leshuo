package com.example.feng.xgs.main.nearby.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.detail.DynamicDetailActivity;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/4/28 0028.
 * 个人动态
 */

public class NearbyDynamicActivity extends EmptyActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    private NearbyDynamicAdapter mAdapter;
    private NearbyDynamicDataConverter mDataConverter;

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void emptyLoadData() {
        loadData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_list;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.dynamic));
        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider(10);
        mDataConverter = new NearbyDynamicDataConverter();
        mAdapter = new NearbyDynamicAdapter(R.layout.item_nearby_dynamic, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void loadData(){
        Intent intent = getIntent();
        if(intent == null) return;
        String idOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        Log.d(TAG, "loadData: " + idOnly);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, idOnly);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.NEARBY_DYNAMIC_LIST)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);

                        mDataConverter.clearData();
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);
                    }
                })
                .build().post();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BaseEntity entity = mAdapter.getData().get(position);
        Intent intent = new Intent(this, DynamicDetailActivity.class);
        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
        String itemData = JSON.toJSONString(linkedHashMap);
        LogUtils.d("linkedHashMap: " + itemData);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
        startActivity(intent);
    }
}
