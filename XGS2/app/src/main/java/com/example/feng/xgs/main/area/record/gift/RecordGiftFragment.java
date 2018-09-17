package com.example.feng.xgs.main.area.record.gift;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.config.key.CodeKeys;
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
 * Created by feng on 2018/6/6 0006.
 * 礼物记录
 */

public class RecordGiftFragment extends EmptyFragment {

    private RecordGiftDataConverter mDataConverter;
    private RecordGiftAdapter mAdapter;
    private String mModelId;
    private int mType;


    public static RecordGiftFragment create(int type, String modelId){
        Bundle args = new Bundle();
        args.putInt(ContentKeys.ACTIVITY_TYPE, type);
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, modelId);
        RecordGiftFragment fragment = new RecordGiftFragment();
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
        Bundle args = getArguments();
        if(args == null) return;
        mType = args.getInt(ContentKeys.ACTIVITY_TYPE);
        if(mType == CodeKeys.RECORD_MODEL){
            mModelId = args.getString(ContentKeys.ACTIVITY_ID_ONLY);
        }

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider();

        mDataConverter = new RecordGiftDataConverter();
        mAdapter = new RecordGiftAdapter(R.layout.item_mine_record_gift, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        String url = UrlKeys.AREA_RECORD_GIFT;
        if(mType == CodeKeys.RECORD_MODEL){
            map.put(ParameterKeys.MODEL_ID, mModelId);
        }else if(mType == CodeKeys.RECORD_MINE){
            url = UrlKeys.AREA_RECORD_GIFT_MINE;
            map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        }else {
            return;
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
