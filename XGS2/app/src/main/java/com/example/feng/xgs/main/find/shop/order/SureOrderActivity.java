package com.example.feng.xgs.main.find.shop.order;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.base.recycler.BaseDecoration;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.bean.PayBean;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.info.address.AddressActivity;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/21 0021.
 * 确认订单
 */

public class SureOrderActivity extends ToolbarActivity {


    @BindView(R.id.tv_order_sure_name)
    TextView mTvName;
    @BindView(R.id.tv_order_sure_mobile)
    TextView mTvMobile;
    @BindView(R.id.tv_order_sure_address)
    TextView mTvAddress;
    @BindView(R.id.rv_order_sure)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_order_sure_price)
    TextView mTvPrice;
    private SureOrderDataConverter mDataConverter;
    private SureOrderAdapter mAdapter;
    private String mOrderNo;
    private String mAddressId;
    private String mPriceAll;

    //改变收货地址
    @OnClick(R.id.tv_order_sure_address)void onClickChangeAddress(){
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.address_select));
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.ADDRESS_SELECT);
        startActivityForResult(intent, CodeKeys.EDITOR_REQUEST);
    }

    //提交订单
    @OnClick(R.id.tv_order_sure_pay)void onClickPay(){
        createOrder();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_find_order_sure;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle("确认订单");

        initRecyclerView();
        loadData();
        getData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.background_color)));
        mRecyclerView.setNestedScrollingEnabled(false);

        mDataConverter = new SureOrderDataConverter();
        mAdapter = new SureOrderAdapter(R.layout.item_find_sure_order, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.ORDER_SHOP_ADDRESS)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String name = object.getString("receivername");
                        String address = object.getString("address");
                        String mobile = object.getString("receiverphone");
                        mAddressId = object.getString("id");
                        String postcode = object.getString("postcode");

                        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(mobile) && TextUtils.isEmpty(address)){
                            mTvName.setText("");
                            mTvMobile.setText("");
                            mTvAddress.setText("请选择收货地址");
                        }else {
                            mTvName.setText(name);
                            mTvAddress.setText(address);
                            mTvMobile.setText(mobile);
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        mTvName.setText("");
                        mTvMobile.setText("");
                        mTvAddress.setText("请选择收货地址");
                    }
                })
                .build().post();

    }

    //获取从上个页面传递的数据
    private void getData(){
        Intent intent = getIntent();
        mPriceAll = intent.getStringExtra(ContentKeys.ACTIVITY_PRICE);
        mTvPrice.setText("实付款: " + mPriceAll);
        String info = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        mAdapter.setNewData(mDataConverter.setJsonData(info).convert());
        LogUtils.d("info: " + info);
    }



    private void createOrder() {
        List<PayBean.ProductBean> beanList = new ArrayList<>();
        List<BaseEntity> entities = mAdapter.getData();
        for (int i = 0; i < entities.size(); i++) {
            BaseEntity entity = entities.get(i);
//            String type = entity.getField(EntityKeys.TYPE);
//            if (ContentKeys.SELECT.equals(type)) {
                PayBean.ProductBean product = new PayBean.ProductBean();
                product.setProductid(entity.getField(EntityKeys.PRODUCT_ID));
                product.setNum(entity.getField(EntityKeys.COUNT));
                beanList.add(product);
//            }
        }

        OrderCreateHandler
                .create(this)
                .setAddressId(mAddressId)
                .createOrder(ContentKeys.ORDER_SHOPPING, beanList,
                        new OrderCreateHandler.IOrderCreateListener() {
                            @Override
                            public void onOrderCreate(String orderNo, String price) {
                                Intent intent = new Intent(SureOrderActivity.this, PayActivity.class);
                                intent.putExtra(ContentKeys.ACTIVITY_ORDER_NO, orderNo);
                                intent.putExtra(ContentKeys.ACTIVITY_PRICE, mPriceAll);
                                intent.putExtra("ali", UrlKeys.ORDER_SHOP_PAY_ALI);
                                intent.putExtra("weixin", UrlKeys.ORDER_SHOP_PAY_WEIXIN);
//                                intent.putExtra(ContentKeys.ACTIVITY_LINK, UrlKeys.ORDER_SHOP_PAY_ALI);
                                startActivity(intent);
                            }
                        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CodeKeys.FOR_RESULT_CODE){
            if(requestCode == CodeKeys.EDITOR_REQUEST && data != null){
                String name = data.getStringExtra(ContentKeys.ACTIVITY_TITLE);
                String info = data.getStringExtra(ContentKeys.ACTIVITY_INFO);
                mAddressId = data.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
                String mobile = data.getStringExtra(ContentKeys.ACTIVITY_MOBILE);

                mTvAddress.setText(info);
                mTvName.setText(name);
                mTvMobile.setText(mobile);
            }
        }
    }
}
