package com.example.feng.xgs.main.find.shop.detail.parameter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.config.key.ContentKeys;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/15 0015.
 * 商品参数
 */

public class ShopParameterFragment extends EmptyFragment {


    private ShopParameterAdapter mAdapter;
    private ShopParameterDataConverter mDataConverter;

    public static ShopParameterFragment create(String response) {
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_INFO, response);
        ShopParameterFragment fragment = new ShopParameterFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_shop_detail)
    RecyclerView mRecyclerView;


    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void emptyLoadData() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_find_shop_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        Bundle args = getArguments();
        if(args == null) return;
        String response = args.getString(ContentKeys.ACTIVITY_INFO);
        initRecyclerView();
        loadData(response);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider(1);
        mDataConverter = new ShopParameterDataConverter();
        mAdapter = new ShopParameterAdapter(R.layout.item_find_shop_detail_parameter, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(String response) {
        mAdapter.setNewData(mDataConverter.setContext(getContext()).setJsonData(response).convert());
    }


}
