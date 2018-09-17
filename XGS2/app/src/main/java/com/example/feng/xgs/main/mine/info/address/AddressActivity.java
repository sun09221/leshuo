package com.example.feng.xgs.main.mine.info.address;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.order.SureOrderActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2017/12/22.
 * 收货地址列表activity
 */

public class AddressActivity extends EmptyActivity implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.tv_toolbar_save)TextView mTvManage;
    @BindView(R.id.refresh_address)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.rv_address)
    RecyclerView mRecyclerView;
    private AddressDataConverter mDataConverter;
    private AddressAdapter mAdapter;
    private int mType;

    @OnClick(R.id.tv_toolbar_save)void onClickManager(){
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.address_manager));
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.ADDRESS_MANAGER);
        startActivity(intent);
    }

    @OnClick(R.id.tv_address_sure)
    void onClickSure() {
        Intent intent = new Intent(this, AddressAddActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.ADDRESS_ADD_TAG);
        startActivityForResult(intent, CodeKeys.ADD_REQUEST);
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
        return R.layout.activity_address;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState) {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        mType = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, -1);
        if(mType == CodeKeys.ADDRESS_MANAGER){
            mTvManage.setVisibility(View.GONE);
        }
        mTvManage.setText("管理");
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        setTitle(title);

        initRefreshLayout(mRefresh);
        initRecyclerView();

        loadData();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider(10);
        mDataConverter = new AddressDataConverter();
        mAdapter = new AddressAdapter(R.layout.item_mine_address, mDataConverter.ENTITIES, mType);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemClickListener(this);
    }

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ADDRESS_LIST)
                .loader(this)
                .raw(raw)
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CodeKeys.FOR_RESULT_CODE) {
            Log.d(TAG, "onActivityResult: " + requestCode);
            switch (requestCode) {
                case CodeKeys.ADD_REQUEST:
                    loadData();
                    break;
                case CodeKeys.EDITOR_REQUEST:
                    loadData();
                    break;
            }

        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_address_item_editor:
                Intent intent = new Intent(this, AddressAddActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.ADDRESS_EDITOR_TAG);
                intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mAdapter.getData().get(position).getField(EntityKeys.ID_ONLY));
                startActivityForResult(intent, CodeKeys.ADD_REQUEST);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        if (mType == CodeKeys.ADDRESS_SELECT) {
            BaseEntity entity = mAdapter.getData().get(position);
            String name = entity.getField(EntityKeys.NAME);
            String mobile = entity.getField(EntityKeys.MOBILE);
            String address = entity.getField(EntityKeys.ADDRESS_ALL);
            String addressId = entity.getField(EntityKeys.ID_ONLY);
            Intent intent = new Intent(this, SureOrderActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, addressId);
            intent.putExtra(ContentKeys.ACTIVITY_TITLE, name);
            intent.putExtra(ContentKeys.ACTIVITY_INFO, address);
            intent.putExtra(ContentKeys.ACTIVITY_MOBILE, mobile);
            setResult(CodeKeys.FOR_RESULT_CODE, intent);
            finish();
        } else {

        }
    }
}
