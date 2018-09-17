package com.example.feng.xgs.main.find.shop;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.cart.ShopCartActivity;
import com.example.feng.xgs.main.find.shop.detail.ShopDetailActivity;
import com.example.feng.xgs.main.find.shop.search.ShopSearchActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/15 0015.
 * 商城
 */

public class ShopActivity extends EmptyActivity implements OnItemClickListener, BaseQuickAdapter.OnItemClickListener {

//    @BindView(R.id.banner_shop)ConvenientBanner<String> mBanner;
    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    private ShopAdapter mAdapter;
    private ShopDataConverter mDataConverter;
    private static final String TYPE_RECOMMEND = "1";
    private static final String TYPE_NORMAL = "2";

    //跳转到搜索页面
    @OnClick(R.id.tv_shop_search)void onClickSearch(){
        startActivity(new Intent(this, ShopSearchActivity.class));
    }

    @OnClick(R.id.btn_shop_cart)void onClickShopCart(){
        Log.d(TAG, "onClickShopCart: 购物车");
        startActivity(new Intent(this, ShopCartActivity.class));
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
        return R.layout.activity_find_shop;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_shop));

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(manager);
        mDataConverter = new ShopDataConverter();
        mAdapter = new ShopAdapter(mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }



    private void loadData(){
        NaturalLoader.showLoading(this);
        loadBannerData();
    }

    private void loadBannerData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, ContentKeys.BANNER_TYPE_SHOP);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.BANNER)
                .raw(raw)
                .toast(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.setBannerJson(response);
                        loadHotTabData();
                    }
                })
                .build().post();
    }

    private void loadHotTabData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE,"1");//type：类型：1为父级 0为子级
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_HOT_TAB)
                .raw(raw)
                .toast(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.setHotTabJson(response);
                        loadItemData(TYPE_RECOMMEND);
                    }
                })
                .build().post();
    }

    private void loadItemData(final String type){
        //type：（1.每日推荐 2.精品特价）
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, type);
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

//        NaturalLoader.showLoading(this);
        RestClient.builder()
                .url(UrlKeys.SHOP_LIST)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        if(type.equals(TYPE_RECOMMEND)){
                            loadItemData(TYPE_NORMAL);
//                            mRecommendJson = response;
                            mDataConverter.setRecommendJson(response);
                        }else if(type.equals(TYPE_NORMAL)){
                            mRefresh.setRefreshing(false);
                            mAdapter.setEmptyView(emptyView);
                            mDataConverter.clearData();
                            mAdapter.setNewData(mDataConverter.setNormalJson(response).convert());
                            NaturalLoader.stopLoading();
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        if(type.equals(TYPE_NORMAL)){
                            mRefresh.setRefreshing(false);
                            mAdapter.setEmptyView(emptyView);
                            NaturalLoader.stopLoading();
                        }
                    }
                })
                .build().post();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        int itemType = adapter.getItemViewType(position);
        if( itemType == ItemTypeKeys.FIND_SHOP_IMAGE_SMALL || itemType == ItemTypeKeys.FIND_SHOP_IMAGE_BIG){
            BaseEntity entity = mAdapter.getData().get(position);
            Intent intent = new Intent(this, ShopDetailActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_TITLE, "商品详情");
            intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
            startActivity(intent);
        }
    }




}
