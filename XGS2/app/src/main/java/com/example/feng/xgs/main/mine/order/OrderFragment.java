package com.example.feng.xgs.main.mine.order;

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
 * Created by feng on 2018/5/9 0009.
 * 订单 根据type显示购物、VIP、充值订单
 */

public class OrderFragment extends EmptyFragment{

    private OrderDataConverter mDataConverter;
    private OrderAdapter mAdapter;

    public static OrderFragment create(String type){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_TYPE, type);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;

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

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(manager);
        mDataConverter = new OrderDataConverter();
        mAdapter = new OrderAdapter(R.layout.item_mine_order, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadData(){
        Bundle args = getArguments();
        if(args == null) return;
        String type = args.getString(ContentKeys.ACTIVITY_TYPE);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.TYPE, type);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ORDER_LIST)
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
                        if(mRefresh != null){
                            mRefresh.setRefreshing(false);
                            mAdapter.setEmptyView(emptyView);
                        }
                    }
                })
                .build().post();
    }
}
