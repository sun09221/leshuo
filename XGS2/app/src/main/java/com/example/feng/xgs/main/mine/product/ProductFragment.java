package com.example.feng.xgs.main.mine.product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/30 0030.
 * 我的产品
 */

public class ProductFragment extends EmptyFragment {

    private String mType;

    public static ProductFragment create(String type){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_TYPE, type);
        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    private ProductDataConverter mDataConverter;
    private ProductAdapter mAdapter;
    private String mUrl = "";

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
        return R.layout.include_recycler_view;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        Bundle bundle = getArguments();
        if(bundle == null) return;
        mType = bundle.getString(ContentKeys.ACTIVITY_TYPE);


        initRefreshLayout(mRefresh);
        initRecyclerView(mType);
        loadData();
    }

    private void initRecyclerView(String type){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new ProductDataConverter();
        mAdapter = new ProductAdapter(R.layout.item_mine_product, mDataConverter.ENTITIES, type);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.STATE, mType);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.MINE_PRODUCT_LIST)
                .raw(raw)
                .loader(getContext())
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
