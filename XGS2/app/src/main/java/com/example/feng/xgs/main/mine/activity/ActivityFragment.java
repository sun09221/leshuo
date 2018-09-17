package com.example.feng.xgs.main.mine.activity;

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
 * Created by feng on 2018/6/4 0004.
 * icon_find_normal: 活动
 */

public class ActivityFragment extends EmptyFragment{

    private static final String ACTIVITY_TYPE_CHILD = "activityTypeChild";
    private String mType;
    private String mTypeChild;

    public static ActivityFragment create(String type, String typeChild){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_TYPE, type);
        args.putString(ACTIVITY_TYPE_CHILD, typeChild);
        ActivityFragment fragment = new ActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    private ActivityAdapter mAdapter;
    private ActivityDataConverter mDataConverter;

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
        Bundle args = getArguments();
        if(args == null) return;
        mType = args.getString(ContentKeys.ACTIVITY_TYPE);
        mTypeChild = args.getString(ACTIVITY_TYPE_CHILD);

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider(10);
        mDataConverter = new ActivityDataConverter();
        mAdapter = new ActivityAdapter(R.layout.item_find_activity, mDataConverter.ENTITIES, mType);
        mRecyclerView.setAdapter(mAdapter);

    }

    public void loadData(){

        String url;
        Map<String, String> map = new HashMap<>();

        if(ContentKeys.MINE_ACTIVITY.equals(mType)){
            url = UrlKeys.MINE_ACTIVITY_LIST;
            map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
            map.put(ParameterKeys.STATE, mTypeChild);
        }else {
            url = UrlKeys.FIND_ACTIVITY_LIST;
            map.put(ParameterKeys.TYPE_SYS, mTypeChild);
        }

        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(url)
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
