package com.example.feng.xgs.main.find.shop.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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
import com.example.feng.xgs.main.find.shop.detail.ShopDetailActivity;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/15 0015.
 * 商品列表
 */

public class ShopListActivity extends EmptyActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    private ShopListAdapter mAdapter;
    private ShopListDataConverter mDataConverter;
    private String mSearchInfo;

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



        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(manager);
        int margin = DensityUtils.dp2px(this, 10);
        mRecyclerView.setPadding(margin, margin, margin, margin);
        mRecyclerView.setClipToPadding(false);
        addDivider(6);
        mDataConverter = new ShopListDataConverter();
        mAdapter = new ShopListAdapter(R.layout.item_find_shop_image_small, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void loadData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        setTitle(title);

        final int type = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.INTENT_DEFAULT);
        String url;
        Map<String, String> map = new HashMap<>();

        //更多商品 type：（1.每日推荐 2.精品特价）
        if(type == CodeKeys.SHOP_LIST_MORE){
            map.put(ParameterKeys.TYPE, "1");
            url = UrlKeys.SHOP_LIST;

            //商品搜索
        }else if(type == CodeKeys.SHOP_LIST_SEARCH){
            mSearchInfo = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
            if(TextUtils.isEmpty(mSearchInfo)){
                return;
            }
            map.put(ParameterKeys.PRODUCT_NAME, mSearchInfo);
            url = UrlKeys.SHOP_SEARCH;

            //类别商品
        }else if(type == CodeKeys.SHOP_LIST_TYPE){
            String info = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
            map.put(ParameterKeys.PRODUCT_TYPE, info);
            url = UrlKeys.SHOP_SEARCH;
        }else {
            return;
        }


        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);


        RestClient.builder()
                .url(url)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        if(type == CodeKeys.SHOP_LIST_SEARCH){
                            saveSearchHistory();
                        }
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



    /**
     * 保存历史搜素信息
     * */
    private void saveSearchHistory(){


        StringBuilder builder = new StringBuilder();
        String jsonOld = SharedPreferenceUtils.getCustomAppProfile(ContentKeys.SEARCH_JSON);
        if(TextUtils.isEmpty(jsonOld)){
            builder.append(mSearchInfo);
        }else {
            boolean isRepeat = false;
            String[] searchArrayOld = jsonOld.split(ContentKeys.DELIMIT);
            for (int i = 0; i < searchArrayOld.length; i++) {
                if(mSearchInfo.equals(searchArrayOld[i])){
                    isRepeat = true;
                    break;
                }
            }

            if(!isRepeat){
                builder.append(ContentKeys.DELIMIT)
                        .append(mSearchInfo)
                        .append(jsonOld);
            }else {
                builder.append(jsonOld);
            }
        }

        SharedPreferenceUtils.setCustomAppProfile(ContentKeys.SEARCH_JSON, builder.toString());
    }



    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        BaseEntity entity = mAdapter.getData().get(position);
        Intent intent = new Intent(this, ShopDetailActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, "商品详情");
        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
        startActivity(intent);

    }
}
