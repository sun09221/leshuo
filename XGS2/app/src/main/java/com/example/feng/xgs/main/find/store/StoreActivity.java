package com.example.feng.xgs.main.find.store;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/7 0007.
 * 我的店铺
 */

public class StoreActivity extends EmptyActivity {

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    private StoreDataConverter mDataConverter;
    private StoreAdapter mAdapter;

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
        setTitle(getString(R.string.find_store));
        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider(10);
        mDataConverter = new StoreDataConverter();
        mAdapter = new StoreAdapter(R.layout.item_find_store, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.LATITUDE, "34.79");
        map.put(ParameterKeys.LONGITUDE, "113.60");
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");

        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_STORE_LIST)
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



}
