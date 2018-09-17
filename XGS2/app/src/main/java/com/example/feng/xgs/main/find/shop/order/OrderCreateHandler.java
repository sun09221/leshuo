package com.example.feng.xgs.main.find.shop.order;

import android.app.Activity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.bean.PayBean;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.List;

/**
 * Created by feng on 2018/6/12 0012.
 * 创建订单
 */

public class OrderCreateHandler {

    private Activity mActivity;
    private IOrderCreateListener mListener;
    private String mModelId;
    private String mAddressId;

    private OrderCreateHandler(Activity activity) {
        this.mActivity = activity;
    }

    public static OrderCreateHandler create(Activity activity) {
        return new OrderCreateHandler(activity);
    }

    public void createOrder(String type,
                            List<PayBean.ProductBean> beanList,
                            IOrderCreateListener listener) {
        this.mListener = listener;
        PayBean bean = new PayBean();
        bean.setPeopleid(SharedPreferenceUtils.getPeopleId());
        bean.setType(type);//type：订单类型：1：购物订单 2：VIP订单 3：充值订单
        bean.setProduct(beanList);
        if(!TextUtils.isEmpty(mModelId)){
            bean.setModelid(mModelId);
        }

        if(!TextUtils.isEmpty(mAddressId)){
            bean.setAddressid(mAddressId);
        }

        String raw = JSON.toJSONString(bean);

        RestClient.builder()
                .url(UrlKeys.ORDER_CREATE)
                .raw(raw)
                .loader(mActivity)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
//                        ToastUtils.showText(ShopCartActivity.this, "");
                        JSONObject object = JSON.parseObject(response);
                        String orderNo = object.getString("orderno");
//                        String price = object.getString("total_price");
                        if(mListener != null){
                            mListener.onOrderCreate(orderNo, "");
                        }
//                        Intent intent = new Intent(ShopCartActivity.this, SureOrderActivity.class);
//                        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
//                        startActivity(intent);
                    }
                })
                .build().post();

    }

    public OrderCreateHandler setModelId(String modelId){
        this.mModelId = modelId;
        return this;
    }

    public OrderCreateHandler setAddressId(String addressId){
        this.mAddressId = addressId;
        return this;
    }

    public interface IOrderCreateListener {
        void onOrderCreate(String orderNo, String price);
    }
}
