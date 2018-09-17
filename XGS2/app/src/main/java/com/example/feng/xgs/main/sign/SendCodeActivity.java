package com.example.feng.xgs.main.sign;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.handler.HandlerUtils;
import com.example.feng.xgs.utils.handler.IHandlerMessageListener;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.SMSSDK;

/**
 * Created by feng on 2018/3/19 0019.
 */

public abstract class SendCodeActivity extends ToolbarActivity {

    private int mIndex = 60;
    //是否可以发送验证码
    private boolean isCanSendCode = true;
    private HandlerUtils mHandler;
    private static final int MESSAGE_WHAT_CODE = 1001;


    /**
     * 在handler中调用，不断改变倒计时
     */
    public abstract void changeText(int mIndex);
//    public abstract TextView getCodeTextView();

    @Override
    public void onBindView(Bundle savedInstanceState) {
        initHandler();
    }

    private void initHandler() {
        mHandler = new HandlerUtils(this, new IHandlerMessageListener() {
            @Override
            public void onHandlerMessage(Message msg) {

                if (msg.what == MESSAGE_WHAT_CODE) {
                    mIndex--;
                    changeText(mIndex);
                    if (mIndex > 0) {
                        mHandler.sendEmptyMessageDelayed(MESSAGE_WHAT_CODE, 1000);
                    } else {
                        isCanSendCode = true;
                        mIndex=60;
                    }

                }
            }
        });
    }

    public void sendSms(String mobile) {
        if (mobile.length() != 11) {
            ToastUtils.showText(this, getString(R.string.edit_mobile));
            return;
        }

        if (!isCanSendCode) {
            ToastUtils.showText(this, "请稍后再试");
            return;
        }

        Log.d(TAG, "onClickCode: 手机号: " + mobile);
//        JsonBean bean = new JsonBean();
////        bean.setMobile(mobile);
//        String raw = JSON.toJSONString(bean);
//
//        RestClient.builder()
//                .url(UrlKeys.SIGN_CODE)
//                .loader(this)
//                .toast(this)
//                .raw(raw)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        Log.d(TAG, "onSuccess: " + response);
        SMSSDK.getVerificationCode("86", mobile);
        //请求成功后，不允许再发送sms
        isCanSendCode = false;
        mHandler.sendEmptyMessage(MESSAGE_WHAT_CODE);

//                    }
//                })
//                .build().post();
    }


    @Override
    protected void onStart() {
        super.onStart();
        long nowTime = System.currentTimeMillis();
        long oldTime = SharedPreferenceUtils.getCustomAppLong(ShareKeys.CODE_TIMESTAMP);
        Log.e("TYPE", "onStart: 退出时时间" + oldTime + "新时间: " + nowTime);

        //如果退出时的时间距离重新进入页面的时间小于60s
        if (nowTime - oldTime <= 60 * 1000) {
            isCanSendCode = false;
            //计算倒计时index， 并开始计时
            mIndex = (int) (oldTime - nowTime) / 1000 + 60;
            Log.e("TYPE", "onStart: 倒计时" + mIndex);
            if (mHandler == null) {
                initHandler();
            }
            mHandler.sendEmptyMessage(MESSAGE_WHAT_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isCanSendCode) {
            Log.e("TYPE", "onPause: " + mIndex);
            long oldTime = System.currentTimeMillis() - (60 - mIndex) * 1000;
            Log.e("TYPE", "onPause: 退出时时间" + oldTime);
            SharedPreferenceUtils.setCustomAppLong(ShareKeys.CODE_TIMESTAMP, oldTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
    private void loadData() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        //店铺id
                      String  result = object.getString("id");

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                       ToastUtils.showText(SendCodeActivity.this,"验证码获取失败");

                    }
                })
                .build().post();
    }
}
