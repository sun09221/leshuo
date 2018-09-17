package com.example.feng.xgs.main.find.shop.cart;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.order.SureOrderActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2017/12/18.
 * 购物车
 */

public class ShopCartActivity extends EmptyActivity implements BaseQuickAdapter.OnItemChildClickListener {

    private static final int ITEM_SELECT = 0;
    private static final int ALL_SELECT = 1;
    private static final int COUNT_ADD = 11;
    private static final int COUNT_CUT = 12;
    private boolean mIsAllSelect = false;
    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.rl_cart_bottom)
    RelativeLayout mLayoutBottom;
    @BindView(R.id.tv_toolbar_save)
    TextView mTvRight;
    @BindView(R.id.tv_cart_price)
    TextView mTvPrice;
    @BindView(R.id.tv_cart_select)
    TextView mTvSelect;
    @BindView(R.id.tv_cart_close)
    TextView mTvClose;
    private double mPriceAll;

    private boolean isDeleteAll = false;//是否删除所选商品

    @OnClick(R.id.tv_cart_select)
    void onClickSelect() {
        allSelect();
    }

    //确认订单
    @OnClick(R.id.tv_cart_close)
    void onClickClose() {
        if (isDeleteAll) {
            deleteAll();
        } else {
            startOrderSure();
        }

    }

    @OnClick(R.id.tv_toolbar_save)
    void onClickDelete() {
        if (isDeleteAll) {
            mTvSelect.setVisibility(View.VISIBLE);
            mTvPrice.setVisibility(View.VISIBLE);
            mTvClose.setText(getString(R.string.close));
            mTvRight.setText(getString(R.string.editor));
        } else {
            mTvSelect.setVisibility(View.GONE);
            mTvPrice.setVisibility(View.GONE);
            mTvClose.setText(getString(R.string.delete));
            mTvRight.setText(getString(R.string.finish));
        }

        isDeleteAll = !isDeleteAll;
    }

    private ShopCartAdapter mAdapter;
    private ShopCartDataConverter mDataConverter;


    @Override
    public Object setLayout() {
        return R.layout.activity_shop_cart;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.shop_cart));
        mTvRight.setText(getString(R.string.editor));
        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mDataConverter = new ShopCartDataConverter();
        mAdapter = new ShopCartAdapter(R.layout.item_find_shop_cart, mDataConverter.ENTITIES);
        addDivider();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
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

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_CART_LIST)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);

                        mDataConverter.clearData();
                        List<BaseEntity> entities = mDataConverter.setJsonData(response).convert();
                        mAdapter.setNewData(entities);

                        setNormalBottom();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);
                        setNormalBottom();
                    }
                })
                .build().post();

    }

    private void setNormalBottom() {
        if (mAdapter.getData().size() == 0) {
            mLayoutBottom.setVisibility(View.GONE);
        }

        mTvPrice.setText("￥0.0");
        mTvSelect.setSelected(false);

    }

    /**
     * item的选中事件
     */
    private void itemSelect(View view, int position) {
        boolean isSelectAll = true;
        List<BaseEntity> entities = mAdapter.getData();

        //设置状态改变
        BaseEntity entityItem = entities.get(position);
        String type = entityItem.getField(EntityKeys.TYPE);
        if (ContentKeys.SELECT.equals(type)) {
            entityItem.setField(EntityKeys.TYPE, ContentKeys.NORMAL);
            ImageView imageView = (ImageView) view;
            imageView.setSelected(false);
        } else if (ContentKeys.NORMAL.equals(type)) {
            entityItem.setField(EntityKeys.TYPE, ContentKeys.SELECT);
            ImageView imageView = (ImageView) view;
            imageView.setSelected(true);
        } else {
            return;
        }

        //循环判断是否全选
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            BaseEntity entity = entities.get(i);
            if (entity.getField(EntityKeys.TYPE).equals(ContentKeys.NORMAL)) {
                isSelectAll = false;
                break;
            }
        }
        mIsAllSelect = isSelectAll;
        mTvSelect.setSelected(mIsAllSelect);

        //设置总价格
        setAllPrice(entities);
    }

    /**
     * +-数量
     */
    private void itemChangeCount(int position, int type) {

        List<BaseEntity> entities = mAdapter.getData();
        BaseEntity entity = entities.get(position);
        String count = entity.getField(EntityKeys.COUNT);
        int countInteger = Integer.parseInt(count);

        if (type == COUNT_ADD) {
            countInteger++;
        } else if (type == COUNT_CUT) {
            if (countInteger > 1) {
                countInteger--;
            }
        } else {
            return;
        }

        String countResult = String.valueOf(countInteger);
        TextView textView = (TextView) mAdapter.getViewByPosition(mRecyclerView, position, R.id.tv_cart_item_count);
        if (textView != null) textView.setText(countResult);
        entity.setField(EntityKeys.COUNT, countResult);

        setAllPrice(entities);
    }

    /**
     * 全选改变item状态
     */
    private void allSelect() {

        List<BaseEntity> entities = mAdapter.getData();
        int size = entities.size();
        mIsAllSelect = !mIsAllSelect;
        for (int i = 0; i < size; i++) {
            BaseEntity entity = entities.get(i);

            if (mIsAllSelect) {
                entity.setField(EntityKeys.TYPE, ContentKeys.SELECT);
            } else {
                entity.setField(EntityKeys.TYPE, ContentKeys.NORMAL);
            }
        }
        mTvSelect.setSelected(mIsAllSelect);

        mAdapter.notifyDataSetChanged();
        setAllPrice(entities);

    }

    private void deleteItem(final int position) {

        final List<BaseEntity> entities = mAdapter.getData();

        String idOnly = entities.get(position).getField(EntityKeys.ID_ONLY);
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_S, idOnly);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_CART_DELETE)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.remove(position);
                        setAllPrice(entities);
                        if (mAdapter.getItemCount() == 0) {
                            mLayoutBottom.setVisibility(View.GONE);
                        }
                    }
                })
                .build().post();
    }

    /**
     * 删除全部选中商品
     */
    private void deleteAll() {
        final List<BaseEntity> entities = mAdapter.getData();

        StringBuilder builder = new StringBuilder();
        int count = 0;
        for (int i = 0; i < entities.size(); i++) {
            BaseEntity entity = entities.get(i);
            String type = entity.getField(EntityKeys.TYPE);
            if (ContentKeys.SELECT.equals(type)) {
                String idOnly = entity.getField(EntityKeys.ID_ONLY);
                if (count == 0) {
                    builder.append(idOnly);
                } else {
                    builder.append(ContentKeys.DELIMIT)
                            .append(idOnly);
                }
                count++;
            }
        }

        if (count == 0) {
            ToastUtils.showText(this, "请选择要删除的商品");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_S, builder.toString());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_CART_DELETE)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {


                        for (int i = entities.size() - 1; i >= 0; i--) {
                            BaseEntity entity = entities.get(i);
                            String type = entity.getField(EntityKeys.TYPE);
                            if (ContentKeys.SELECT.equals(type)) {
                                mAdapter.remove(i);
                            }
                        }

                        setAllPrice(entities);
                        if (mAdapter.getItemCount() == 0) {
                            mLayoutBottom.setVisibility(View.GONE);
                        }
                    }
                })
                .build().post();
    }

    /**
     * 计算总价格
     */
    private void setAllPrice(List<BaseEntity> entities) {
        mPriceAll = 0;
        int size = entities.size();
        for (int i = 0; i < size; i++) {
            BaseEntity entity = entities.get(i);
            String type = entity.getField(EntityKeys.TYPE);
            //如果是选中状态，计算总价格
            if (ContentKeys.SELECT.equals(type)) {
                int count = Integer.parseInt(entity.getField(EntityKeys.COUNT));
                double price = Double.parseDouble(entity.getField(EntityKeys.PRICE));
                Log.d("TAG", "getAllPrice: " + price);
                double itemPrice = price * count;
                mPriceAll += itemPrice;
            }
        }

        mTvPrice.setText(String.format("￥%s", mPriceAll));
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_cart_item_radio:
                itemSelect(view, position);
                break;
            case R.id.iv_cart_item_add:
                itemChangeCount(position, COUNT_ADD);
                break;
            case R.id.iv_cart_item_cut:
                itemChangeCount(position, COUNT_CUT);

                break;
            case R.id.tv_cart_item_delete:
                deleteItem(position);
                break;
            default:
                break;
        }
    }


    private void startOrderSure() {
        List<BaseEntity> entities = mAdapter.getData();
        List<LinkedHashMap<Object, Object>> linkedHashMapList = new ArrayList<>();
        for (int i = 0; i < entities.size(); i++) {
            BaseEntity entity = entities.get(i);
            String type = entity.getField(EntityKeys.TYPE);
            if (ContentKeys.SELECT.equals(type)) {
                LinkedHashMap<Object, Object> linkedHashMap =
                        (LinkedHashMap<Object, Object>) entity.getFields();

                linkedHashMapList.add(linkedHashMap);
            }
        }

        if (linkedHashMapList.size() <= 0) {
            ToastUtils.showText(this, "请选择商品");
            return;
        }

        String info = JSON.toJSONString(linkedHashMapList);

        Intent intent = new Intent(this, SureOrderActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, info);
        intent.putExtra(ContentKeys.ACTIVITY_PRICE, "￥" + String.valueOf(mPriceAll));
        startActivity(intent);

    }
}
