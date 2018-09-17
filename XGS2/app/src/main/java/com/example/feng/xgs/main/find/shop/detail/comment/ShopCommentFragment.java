package com.example.feng.xgs.main.find.shop.detail.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.feng.xgs.main.move.detail.DynamicDetailAdapter;
import com.example.feng.xgs.main.move.detail.DynamicDetailDataConverter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/15 0015.
 * 商品详情 评论
 */

public class ShopCommentFragment extends EmptyFragment {


    private DynamicDetailAdapter mAdapter;
    private DynamicDetailDataConverter mDataConverter;

    public static ShopCommentFragment create(String idOnly) {
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
        ShopCommentFragment fragment = new ShopCommentFragment();
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
        loadData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_find_shop_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider(1);
        mDataConverter = new DynamicDetailDataConverter();
        mAdapter = new DynamicDetailAdapter(R.layout.item_dynamic_detail_item, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        Bundle args = getArguments();
        if(args == null) return;
        String mIdOnly = args.getString(ContentKeys.ACTIVITY_ID_ONLY);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PRODUCT_ID, mIdOnly);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_COMMENT_LIST)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.setEmptyView(emptyView);

                        mDataConverter.clearData();
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        mAdapter.setEmptyView(emptyView);
                    }
                })
                .build().post();
    }


}
