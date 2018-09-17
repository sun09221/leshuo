package com.example.feng.xgs.main.mine.team;

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
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/9 0009.
 * 我的团队
 */

public class TeamActivity extends EmptyActivity{

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;

    private TeamDataConverter mDataConverter;
    private TeamAdapter mAdapter;

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
        setTitle(getString(R.string.mine_team));

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();

    }


    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider(10);
        mDataConverter = new TeamDataConverter();
        mAdapter = new TeamAdapter(R.layout.item_mine_team, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");

//        NaturalLoader.showLoading(this);
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.TEAM)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.setEmptyView(emptyView);
                        mRefresh.setRefreshing(false);

                        mDataConverter.clearData();
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
//                        NaturalLoader.stopLoading();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        mAdapter.setEmptyView(emptyView);
                        mRefresh.setRefreshing(false);
//                        NaturalLoader.stopLoading();
                    }
                })
                .build().post();

//        mAdapter.setNewData(mDataConverter.convert());
    }

}
