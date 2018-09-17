package com.example.feng.xgs.main.mine.reward;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.order.PayActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/9 0009.
 * 充值打赏
 */

public class RechargeActivity extends EmptyActivity implements BaseQuickAdapter.OnItemChildClickListener {


    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
    @BindView(R.id.tv_reward_price)TextView mTvPrice;
    private MineRewardAdapter mAdapter;
    private MineRewardDataConverter mDataConverter;

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
        return R.layout.activity_mine_recharge;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_pay));

        initRefreshLayout(mRefresh);
        initRecyclerView();

        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new MineRewardDataConverter();
        mAdapter = new MineRewardAdapter(R.layout.item_mine_recharge, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
    }



    private void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.MINE_RECHARGE_LIST)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        JSONObject object = JSON.parseObject(response);
                        String count = object.getString("count");
                        mTvPrice.setText(count);

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


    private void createOrder(String productId, final String price){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PRODUCT_ID, productId);
        map.put(ParameterKeys.TYPE, ContentKeys.ORDER_PAY);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ORDER_RECHARGE_CREATE)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String orderNo = object.getString("orderno");
                        Intent intent = new Intent(RechargeActivity.this, PayActivity.class);
                        intent.putExtra("ali", UrlKeys.ORDER_RECHARGE_PAY_ALI);
                        intent.putExtra("weixin", UrlKeys.ORDER_RECHARGE_PAY_WEIXIN);
                        intent.putExtra(ContentKeys.ACTIVITY_ORDER_NO, orderNo);
                        intent.putExtra(ContentKeys.ACTIVITY_PRICE, price);
                        startActivity(intent);
                    }
                })
                .build().post();
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if(view.getId() == R.id.tv_mine_reward_item_price){
            BaseEntity entity = mAdapter.getData().get(position);
            String price = "￥" + entity.getField(EntityKeys.PRICE);
            String idOnly = entity.getField(EntityKeys.ID_ONLY);
            createOrder(idOnly, price);
        }
    }
}
