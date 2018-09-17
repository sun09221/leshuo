package com.example.feng.xgs.main.find.shop.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.BaseActivity;
import com.example.feng.xgs.base.recycler.BaseDecoration;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.list.ShopListActivity;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/28 0028.
 * 商品搜索
 */

public class ShopSearchActivity extends BaseActivity {

    @BindView(R.id.et_toolbar_search_content)
    EditText mEtSearch;
    @BindView(R.id.iv_toolbar_search_error)
    ImageView mIvSearchError;
    @BindView(R.id.tv_toolbar_search)
    TextView mTvSearch;
    @BindView(R.id.rv_shop_search)
    RecyclerView mRecyclerView;
    private ShopSearchDataConverter mDataConverter;
    private ShopSearchAdapter mAdapter;

    @OnClick(R.id.iv_toolbar_back)void onClickBack(){
        finish();
    }

    @OnClick(R.id.iv_toolbar_search_error)void onClickError(){
        mEtSearch.setText("");
    }

    @OnClick(R.id.tv_toolbar_search)void onClickSearch(){
        search();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_find_shop_search;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {

        initRecyclerView();
        loadData();

    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.background_color)));
        mDataConverter = new ShopSearchDataConverter();
        mAdapter = new ShopSearchAdapter(mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PAGE_SIZE, "10");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_SEARCH_HOT)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        mDataConverter.clearData();
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
                    }
                })
                .build().post();
    }

    private void search(){
        final String searchInfo = mEtSearch.getText().toString();
        if(TextUtils.isEmpty(searchInfo)){
            ToastUtils.showText(this, "请输入要搜索的商品");
            return;
        }

        Intent intent = new Intent(this, ShopListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.SHOP_LIST_SEARCH);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, searchInfo);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, searchInfo);
        startActivity(intent);


    }



}
