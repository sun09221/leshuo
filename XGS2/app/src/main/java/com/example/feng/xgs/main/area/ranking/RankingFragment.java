package com.example.feng.xgs.main.area.ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
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
 * Created by feng on 2018/5/31 0031.
 * 最新排行
 */

public class RankingFragment extends EmptyFragment {

    public static final int TYPE_SHOW_HEAD = 11;
    public static final int TYPE_HIDE_HEAD = 12;

    public static RankingFragment create(String idOnly, int type){
        Bundle args = new Bundle();
        args.putInt(ContentKeys.ACTIVITY_TYPE, type);
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
        RankingFragment fragment = new RankingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.iv_ranking_default)
    ImageView mIvDefault;
    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    private RankingAdapter mAdapter;
    private RankingDataConverter mDataConverter;

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
        return R.layout.fragment_find_ranking;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        Bundle args = getArguments();
        if(args == null) return;
        String idOnly = args.getString(ContentKeys.ACTIVITY_ID_ONLY);
        int type = args.getInt(ContentKeys.ACTIVITY_TYPE);
        if(type == TYPE_HIDE_HEAD){
            mIvDefault.setVisibility(View.GONE);
        }

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new RankingDataConverter();
        mAdapter = new RankingAdapter(R.layout.item_find_raking, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadData(){

        Bundle args = getArguments();
        if(args == null) return;
        String mIdOnly = args.getString(ContentKeys.ACTIVITY_ID_ONLY);
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.AREA_ID, mIdOnly);
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_RANKING_LIST)
                .raw(raw)
                .loader(getContext())
              //  .toast()
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
