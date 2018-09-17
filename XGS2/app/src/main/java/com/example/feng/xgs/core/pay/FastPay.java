package com.example.feng.xgs.core.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import com.example.feng.xgs.MainActivity;
import com.example.feng.xgs.config.ConfigKeys;
import com.example.feng.xgs.config.IPerfectConfig;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.core.pay.alipay.IAlPayResultListener;
import com.example.feng.xgs.core.pay.alipay.PayAsyncTask;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by feng on 2018/3/10 0010.
 * 微信支付宝支付,选择
 */

public class FastPay {

    //设置支付回调监听

    private Activity mActivity = null;

    private String mOrderID = null;
    private int mTag = 0;

    private FastPay(Activity activity) {
        this.mActivity = activity;
    }


    public static FastPay create(Activity activity) {
        return new FastPay(activity);
    }


    public FastPay setPayResultListener(IAlPayResultListener listener) {
        this.mIAlPayResultListener = listener;
        return this;
    }

    public FastPay setOrderId(String orderId) {
        this.mOrderID = orderId;
        return this;
    }

    public FastPay setOrderTAG(int tag) {
        this.mTag = tag;
        return this;
    }

    /**
     * 开启异步任务，调起支付宝
     * */
    public void aliPay(String paySign) {
//        paySign = "app_id=2015052600090779&biz_content=%7B%22timeout_express%22%3A%2230m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%220.02%22%2C%22subject%22%3A%221%22%2C%22body%22%3A%22%E6%88%91%E6%98%AF%E6%B5%8B%E8%AF%95%E6%95%B0%E6%8D%AE%22%2C%22out_trade_no%22%3A%22314VYGIAGG7ZOYY%22%7D&charset=utf-8&method=alipay.trade.app.pay&sign_type=RSA2&timestamp=2016-08-15%2012%3A12%3A15&version=1.0&sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW%2B1wbKOdYtDIb4%2B7PL3Pc94RZL0zKaWcaY3tSL89%2FuAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z%2BZzQ%3D";
        LogUtils.d("AliPaySign: " + paySign);
        final PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mIAlPayResultListener);
        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign);
    }

    //支付宝支付结果回调
    private IAlPayResultListener mIAlPayResultListener = new IAlPayResultListener() {
        @Override
        public void onPaySuccess() {
            SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_TYPE, "1");
            ToastUtils.showText(mActivity,"VIP升级成功");
            Intent intent =new Intent(mActivity, MainActivity.class);
            mActivity.startActivity(intent);

        }

        @Override
        public void onPaying() {

        }

        @Override
        public void onPayFail() {

        }

        @Override
        public void onPayCancel() {

        }

        @Override
        public void onPayConnectError() {

        }
    };
    /**
     * 发送订单信息到微信，调起微信支付
     * */
    public void weChatPay(){
//        LatteLoader.stopLoading();

        final String appId = IPerfectConfig.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
        IWXAPI iwxapi = WXAPIFactory.createWXAPI(mActivity, appId, true);
        iwxapi.registerApp(appId);
//                        final JSONObject result =
//                                JSON.parseObject(response).getJSONObject("result");
//        try {
//            final String prepayId = result.getString("prepayid");
//            final String partnerId = result.getString("partnerid");
//            final String packageValue = result.getString("package");
//            final String timestamp = result.getString("timestamp");
//            final String nonceStr = result.getString("noncestr");
//            final String paySign = result.getString("sign");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }




        final PayReq payReq = new PayReq();

        payReq.appId = appId;
        payReq.partnerId = "1900000109";
        payReq.prepayId= "1101000000140415649af9fc314aa427";
        payReq.packageValue = "Sign=WXPay";
        payReq.nonceStr= "1101000000140429eb40476f8896f4c9";
        payReq.timeStamp= "1398746574";
        payReq.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";


        iwxapi.sendReq(payReq);

    }


}
