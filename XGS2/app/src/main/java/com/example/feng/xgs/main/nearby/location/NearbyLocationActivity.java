package com.example.feng.xgs.main.nearby.location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.MainActivity;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.BaseActivity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/6/9 0009.
 * 城市选择
 */

public class NearbyLocationActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.et_nearby_location_search)
    EditText mEtSearch;
    @BindView(R.id.rv_nearby_location)
    RecyclerView mRecyclerView;
    private NearbyLocationAdapter mAdapter;
    private NearbyLocationDataConverter mDataConverter;


    @OnClick(R.id.iv_toolbar_back)
    void onClickFinish() {
        finish();
    }

    @OnClick(R.id.iv_nearby_location_search)
    void onClickSearch() {

    }

    @Override
    public Object setLayout() {
        return R.layout.activity_nearby_location;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);
        mDataConverter = new NearbyLocationDataConverter();
        mAdapter = new NearbyLocationAdapter(mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);

    }

    private void loadData() {
        Intent intent = getIntent();
        final String city = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);

        RestClient.builder()
                .url(UrlKeys.FIND_PROVINCE)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        mAdapter.setNewData(mDataConverter
                                .setCity(city)
                                .setJsonData(response)
                                .convert());
                    }
                })
                .build().post();

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.tv_nearby_location_item_content) {
            String city = mAdapter.getData().get(position).getField(EntityKeys.CITY);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_INFO, city);
            setResult(CodeKeys.FOR_RESULT_CODE, intent);
            finish();
        }
    }
}
