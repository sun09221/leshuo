package com.example.feng.xgs.main.mine.earnings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/8 0008.
 * 我的收益 根据type决定是打赏还是佣金
 */

public class EarningsFragment extends EmptyFragment {

    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.tv_earnings_count_all)TextView mTvPrice;
    @BindView(R.id.tv_earnings_count_all_describe)TextView mTvDescribe;
    private String mType;

    @OnClick(R.id.tv_earnings_withdraw)void onClickWithdraw(){
        Intent intent = new Intent(getContext(), WithdrawActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, mType);
        startActivity(intent);
    }

    private EarningsDataConverter mDataConverter;
    private EarningsAdapter mAdapter;


    public static EarningsFragment create(String type) {
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_TYPE, type);
        EarningsFragment fragment = new EarningsFragment();
        fragment.setArguments(args);
        return fragment;
    }

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
        return R.layout.fragment_mine_earnings;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        Bundle args = getArguments();
        if(args == null) return;
        mType = args.getString(ContentKeys.ACTIVITY_TYPE);

        initRefreshLayout(mRefresh);
        initRecyclerView();
//        addHead();
        loadData();

        if(ContentKeys.MINE_EARNINGS_SHOP.equals(mType)){
            mTvDescribe.setText("购物收益");
        }else {
            mTvDescribe.setText("VIP收益");
        }
    }


    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new EarningsDataConverter();
        mAdapter = new EarningsAdapter(R.layout.item_mine_earnings, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

    }


    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ITEM, mType);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.EARNINGS_LIST)
                .raw(raw)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.setEmptyView(emptyView);
                        mRefresh.setRefreshing(false);

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
