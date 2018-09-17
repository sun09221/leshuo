package com.example.feng.xgs.main.area.model.detail.gift;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.bean.PayBean;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.order.OrderCreateHandler;
import com.example.feng.xgs.main.find.shop.order.PaynumActivity;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/6/1 0001.
 * 礼物列表
 */

public class GiftListActivity extends EmptyActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.tv_area_gift_price)
    TextView mTvPrice;
    private GiftListDataConverter mDataConverter;
    private GiftListAdapter mAdapter;
    private double mPriceAll = 0;
    private String mModelId;

    @OnClick(R.id.tv_area_gift_sure)void onClickSure(){
        giftPay();
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
        return R.layout.activity_find_area_gift;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_gift_pay));

        Intent intent = getIntent();
        mModelId = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);

        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new GiftListDataConverter();
        mAdapter = new GiftListAdapter(R.layout.item_find_area_gift_list, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);

    }

    private void loadData() {

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_GIFT_LIST)
                .raw(raw)
                .loader(this)
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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        BaseEntity entity = mAdapter.getData().get(position);
        int count = entity.getFieldObject(EntityKeys.COUNT);
        String priceStr = entity.getField(EntityKeys.PRICE);
        double price = Double.valueOf(priceStr);
        TextView tvCount = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.tv_gift_list_item_count);
        LogUtils.d("礼物数量: " + count);

        switch (view.getId()) {
            case R.id.iv_gift_list_item_add:
                if (count == 0) {
                    ImageView ivCut = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.iv_gift_list_item_cut);
                    if (tvCount != null) tvCount.setVisibility(View.VISIBLE);
                    if (ivCut != null) ivCut.setVisibility(View.VISIBLE);
                }
                count++;
                entity.setField(EntityKeys.COUNT, count);
                mPriceAll += price;
                break;
            case R.id.iv_gift_list_item_cut:
                if (count >= 0) {
                    if (count == 1) {
                        ImageView ivCut = (ImageView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.iv_gift_list_item_cut);
                        if (tvCount != null) tvCount.setVisibility(View.GONE);
                        if (ivCut != null) ivCut.setVisibility(View.GONE);
                    }
                    count--;
                    entity.setField(EntityKeys.COUNT, count);
                    mPriceAll -= price;
                }
                break;
            default:
                break;
        }

        if (tvCount != null) tvCount.setText(String.valueOf(count));
        mTvPrice.setText(String.format("￥%s", mPriceAll));
    }


    private void giftPay() {

        List<PayBean.ProductBean> beanList = new ArrayList<>();

        List<BaseEntity> entities = mAdapter.getData();
        for (int i = 0; i < entities.size(); i++) {
            BaseEntity entity = entities.get(i);
            int count = entity.getFieldObject(EntityKeys.COUNT);
            if(count > 0){
                String rewardId = entity.getField(EntityKeys.ID_ONLY);
                PayBean.ProductBean productBean = new PayBean.ProductBean();
                productBean.setNum(String.valueOf(count));
                productBean.setProductid(rewardId);
                beanList.add(productBean);
            }
        }

        if(beanList.size() <= 0){
            ToastUtils.showText(this, "请选择礼物");
            return;
        }

        OrderCreateHandler.create(this)
                .setModelId(mModelId)
                .createOrder(ContentKeys.ORDER_GIFT, beanList,
                        new OrderCreateHandler.IOrderCreateListener() {
                            @Override
                            public void onOrderCreate(String orderNo, String price) {
                                Intent intent = new Intent(GiftListActivity.this, PaynumActivity.class);
                                intent.putExtra(ContentKeys.ACTIVITY_PRICE, "￥" + mPriceAll);
                                intent.putExtra(ContentKeys.ACTIVITY_ORDER_NO, orderNo);
                                intent.putExtra("ali", UrlKeys.ORDER_GIFT_PAY_ALI);
                                intent.putExtra("weixin", UrlKeys.ORDER_GIFT_PAY_WEIXIN);
                                startActivity(intent);
                            }
                        });

    }

}
