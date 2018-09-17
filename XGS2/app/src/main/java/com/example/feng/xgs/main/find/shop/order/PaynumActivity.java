package com.example.feng.xgs.main.find.shop.order;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.pay.alipay.IAlPayResultListener;
import com.example.feng.xgs.core.pay.alipay.PayAsyncTask;
import com.example.feng.xgs.main.area.model.detail.ModelDetailActivity;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/6/12 0012.
 * 选择支付方式
 */

public class PaynumActivity extends ToolbarActivity {
    @BindView(R.id.tv_pay_price)
    TextView mTvPrice;
    @BindView(R.id.tv_ali)
    TextView mTvAli;
    @BindView(R.id.tv_we_chat)
    TextView mTvWeChat;
    private String mOrderNo;
    private String mPayModel = ContentKeys.WE_CHAT;
    //    private String mType;
    private String mAliUrl;
    private String mWeiXinUrl;
    private String only_id;
    private String activity_only;
    private String activity_area_only;
    private int activity_type;
    private String activity_title;


    //选择支付宝
    @OnClick(R.id.tv_ali)
    void onClickAliPay() {
        mTvAli.setSelected(true);
        mTvWeChat.setSelected(false);
        mPayModel = ContentKeys.ALI_PAY;
    }

    //选择支付
    @OnClick(R.id.tv_we_chat)
    void onClickWeChatPay() {
        mTvAli.setSelected(false);
        mTvWeChat.setSelected(true);
        mPayModel = ContentKeys.WE_CHAT;
    }

    //调起支付
    @OnClick(R.id.tv_pay_sure)
    void onClickSure() {
        checkPayModel();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_pay_model;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.pay));

        Intent intent = getIntent();
        String price = intent.getStringExtra(ContentKeys.ACTIVITY_PRICE);
//        mType = intent.getStringExtra(ContentKeys.ACTIVITY_TYPE);
        mOrderNo = intent.getStringExtra(ContentKeys.ACTIVITY_ORDER_NO);
        mAliUrl = intent.getStringExtra("ali");
        mWeiXinUrl = intent.getStringExtra("weixin");
        mTvPrice.setText(price);
        mTvWeChat.setSelected(true);
        SharedPreferences share_get=null;
        share_get=getSharedPreferences("data", MODE_PRIVATE);
        only_id = share_get.getString(ContentKeys.ACTIVITY_ID_ONLY, null);
        activity_only = share_get.getString(ContentKeys.ACTIVITY_ONLY, null);
        activity_area_only = share_get.getString(ContentKeys.ACTIVITY_AREA_ID, null);
        activity_type = share_get.getInt(ContentKeys.ACTIVITY_TYPE, 0);;
        activity_title = share_get.getString(ContentKeys.ACTIVITY_TITLE, null);
    }


    private void checkPayModel() {
        if (ContentKeys.ALI_PAY.equals(mPayModel)) {
            aliPay();
        } else {
            weChatPay();
        }
    }

//paySign: alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018053060325348&biz_content=%7B%22body%22%3A%22%E5%8D%87%E7%BA%A7vip%E4%BB%98%E6%AC%BE%22%2C%22out_trade_no%22%3A%222018062716274664156%22%2C%22passback_params%22%3A%22%25C9%25FD%25BC%25B6vip%25B8%25B6%25BF%25EE%22%2C%22product_code%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22vip%E5%8D%87%E7%BA%A7%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.0%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.92.86.192%2Fsc_xdd%2Fpay%2FcheckRechargePayResult.action&sign=aLTn0LUg31p7AU6gEx%2FGxWPZhSYnSLc8Cz1Ja0zbuLfpSULMJaNUFQqXJggjaBhO6ftL0mz3hLSSH8KLBDgetqg2TxCX%2FC1FFkYw07Kott1Lvqys6iLshwHID0YLiDeM%2F%2Fech5U6QNqjSy8eFz9%2BlFvsIyEIKYpKrNubRSrlORsInM%2F5OW3%2BfqAHzcV%2Fncx2s10oznHhGNQVP55Jj%2F6U%2BTLzDutWnov%2FD9Cg%2FPm2I8JR8rT14VFTcAujaJ7jqwDG1o7ygo0SW8%2ByB%2Fn6ucwQ4ClgSCdVIxAwJg7qVsijm3Grs%2B73c%2Fw80uBfK0oLYPCvn6qJczRiJA3KlsPm66B7Gw%3D%3D&sign_type=RSA2&timestamp=2018-06-27+16%3A27%3A48&version=1.0&sign=aLTn0LUg31p7AU6gEx%2FGxWPZhSYnSLc8Cz1Ja0zbuLfpSULMJaNUFQqXJggjaBhO6ftL0mz3hLSSH8KLBDgetqg2TxCX%2FC1FFkYw07Kott1Lvqys6iLshwHID0YLiDeM%2F%2Fech5U6QNqjSy8eFz9%2BlFvsIyEIKYpKrNubRSrlORsInM%2F5OW3%2BfqAHzcV%2Fncx2s10oznHhGNQVP55Jj%2F6U%2BTLzDutWnov%2FD9Cg%2FPm2I8JR8rT14VFTcAujaJ7jqwDG1o7ygo0SW8%2ByB%2Fn6ucwQ4ClgSCdVIxAwJg7qVsijm3Grs%2B73c%2Fw80uBfK0oLYPCvn6qJczRiJA3KlsPm66B7Gw%3D%3D
//paySign: alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018053060325348&biz_content=%7B%22body%22%3A%22%E9%AA%86%E9%A9%BC%E7%94%B7%E5%87%89%E9%9E%8B*1%22%2C%22out_trade_no%22%3A%222018062716300514272%22%2C%22passback_params%22%3A%22%25C2%25E6%25CD%25D5%25C4%25D0%25C1%25B9%25D0%25AC*1%22%2C%22product_code%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%93%81%E4%BB%98%E6%AC%BE%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.92.86.192%2Fsc_xdd%2Fpay%2FcheckProductPayResult.action&sign=hgcQ3YRjLvBxLq6RbNk4eQsmxIAZjBti9BecH2kWWWoST4FXrxYJyrZCZY1K%2BC6y9l%2BHdy6c8t31ip8m3PXS8ING%2BXjTZF8vpofOMMD6HyvnapejEWu1V%2B3MTTM48w2osuWtnmP7UCenTc4q%2B%2BZygTbd53%2BDksPD46SF6oK9W0JE985hEIZduladLQ121Z2RKng%2Fnc%2BUcom6y414JV2617N1dL0VbxIRpUk0YlscLUKFWdcE%2FTBDnoj808PO5TepxRFOdS7ZKD7A9jbbpAs2naYtMnGwA4PAcSFQNZGN7SrCt6cVubykc9QM0X9inwe2kUIHqbx3AfaFs8rKHR9%2BHA%3D%3D&sign_type=RSA2&timestamp=2018-06-27+16%3A30%3A06&version=1.0&sign=hgcQ3YRjLvBxLq6RbNk4eQsmxIAZjBti9BecH2kWWWoST4FXrxYJyrZCZY1K%2BC6y9l%2BHdy6c8t31ip8m3PXS8ING%2BXjTZF8vpofOMMD6HyvnapejEWu1V%2B3MTTM48w2osuWtnmP7UCenTc4q%2B%2BZygTbd53%2BDksPD46SF6oK9W0JE985hEIZduladLQ121Z2RKng%2Fnc%2BUcom6y414JV2617N1dL0VbxIRpUk0YlscLUKFWdcE%2FTBDnoj808PO5TepxRFOdS7ZKD7A9jbbpAs2naYtMnGwA4PAcSFQNZGN7SrCt6cVubykc9QM0X9inwe2kUIHqbx3AfaFs8rKHR9%2BHA%3D%3D

//paySign: alipay_sdk=alipay-sdk-java-dynamicVersionNo&app_id=2018053060325348&biz_content=%7B%22body%22%3A%22%E4%B8%89%E5%8F%AA%E6%9D%BE%E9%BC%A0*1%22%2C%22out_trade_no%22%3A%222018062716471497116%22%2C%22passback_params%22%3A%22%25C8%25FD%25D6%25BB%25CB%25C9%25CA%25F3*1%22%2C%22product_code%22%3A%22QUICK_WAP_PAY%22%2C%22subject%22%3A%22%E5%95%86%E5%93%81%E4%BB%98%E6%AC%BE%22%2C%22timeout_express%22%3A%2230m%22%2C%22total_amount%22%3A%2221.8%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=http%3A%2F%2F47.92.86.192%2Fsc_xdd%2Fpay%2FcheckProductPayResult.action&sign=fiy2%2Bog3IeZfdAdA2iO7oNlGC3IpNWPyVs%2BfkTdFg7MUYHRHYq5ZQGjlzJl9xwznXBTkPZX%2Bwj0rrFzLwzHkSl86ftssf3NN0zHTf%2BGVQvuN2oFCFnHkJv%2FrR%2BPMaa7mpuJSx1gQKqfn%2FkUIgBRPI6MfuMbM7v0OMbBfZ%2Bx1OOL68LeChp%2FwygCZO%2BISPOQijX1NPj87gXCPywdhR5Li7wj8vQbbI%2FTmpRiCEfVKQ37J7CKY6E88OAUBQWwvzmJEEyHOhI1W%2FlIRlW4GvJv5L5oJdaCdMvnHUngjhycUYCyD%2BeedW2oQfnu3uYrAaT3avda0pIpH1XOo4ZERJOOm5w%3D%3D&sign_type=RSA2&timestamp=2018-06-27+16%3A47%3A15&version=1.0&sign=fiy2%2Bog3IeZfdAdA2iO7oNlGC3IpNWPyVs%2BfkTdFg7MUYHRHYq5ZQGjlzJl9xwznXBTkPZX%2Bwj0rrFzLwzHkSl86ftssf3NN0zHTf%2BGVQvuN2oFCFnHkJv%2FrR%2BPMaa7mpuJSx1gQKqfn%2FkUIgBRPI6MfuMbM7v0OMbBfZ%2Bx1OOL68LeChp%2FwygCZO%2BISPOQijX1NPj87gXCPywdhR5Li7wj8vQbbI%2FTmpRiCEfVKQ37J7CKY6E88OAUBQWwvzmJEEyHOhI1W%2FlIRlW4GvJv5L5oJdaCdMvnHUngjhycUYCyD%2BeedW2oQfnu3uYrAaT3avda0pIpH1XOo4ZERJOOm5w%3D%3D

    private void aliPay() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ORDER_NO, mOrderNo);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(mAliUrl)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String paySign = object.getString("order");
                        final PayAsyncTask payAsyncTask = new PayAsyncTask(PaynumActivity.this, mListenerAli);
                        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign);
                    }
                })
                .build().post();
    }

    private void weChatPay() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ORDER_NO, mOrderNo);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(mWeiXinUrl)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        IWXAPI api = WXAPIFactory.createWXAPI(PaynumActivity.this, object.getString("appid"));
                        api.registerApp(object.getString("appid"));
                        PayReq payReq = new PayReq();
                        payReq.appId = object.getString("appid");
                        payReq.partnerId = object.getString("partnerid");
                        payReq.packageValue = "Sign=WXPay";
                        payReq.prepayId = object.getString("prepayid");
                        payReq.nonceStr = object.getString("noncestr");
                        payReq.timeStamp = object.getString("timestamp");
                        payReq.sign = object.getString("sign");
                        api.sendReq(payReq);
                        loadData();
                    }
                })
                .build().post();
    }
    private void loadData(){
        Intent intent = new Intent(PaynumActivity.this, ModelDetailActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY,only_id);
        intent.putExtra(ContentKeys.ACTIVITY_ONLY, activity_only);
        intent.putExtra(ContentKeys.ACTIVITY_AREA_ID, activity_area_only);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, activity_type);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, activity_title);
        startActivity(intent);
    }

    //支付宝支付结果回调
    private IAlPayResultListener mListenerAli = new IAlPayResultListener() {
        @Override
        public void onPaySuccess() {
            loadData();
        }

        @Override
        public void onPaying() {

        }

        @Override
        public void onPayFail() {
            loadData();
        }

        @Override
        public void onPayCancel() {

        }

        @Override
        public void onPayConnectError() {

        }
    };
}
