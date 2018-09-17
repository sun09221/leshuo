package com.example.feng.xgs.main.find.shop.detail.image;

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
 */

public class ShopImageContentFragment extends EmptyFragment {


    private ShopImageAdapter mAdapter;
    private ShopImageDataConverter mDataConverter;

    public static ShopImageContentFragment create(String result) {
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_INFO, result);
        ShopImageContentFragment fragment = new ShopImageContentFragment();
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
        String result = args.getString(ContentKeys.ACTIVITY_INFO);
        initRecyclerView();
        loadData(result);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider(6);
        mDataConverter = new ShopImageDataConverter();
        mAdapter = new ShopImageAdapter(R.layout.item_find_shop_detail_image, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(String result) {
        mAdapter.setNewData(mDataConverter.setImageResult(result).convert());
    }


}
