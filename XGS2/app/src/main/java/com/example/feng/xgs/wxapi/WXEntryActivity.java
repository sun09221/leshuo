package com.example.feng.xgs.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.IFailure;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.pay.wechat.LatteWeChat;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/**
 * Created by feng on 2018/2/9.
 * 微信登录结果回调页面
 * 注: 该页面必须在包名的的wxapi包下，类名也必须一致
 */

public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LatteWeChat.getInstance().init(this).getWXAPI().handleIntent(getIntent(), this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        LatteWeChat.getInstance().init(this).getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0, 0);
    }

    //微信发送请求到第三方应用后的回调
    @Override
    public void onReq(BaseReq baseReq) {

    }

    //第三方应用发送请求到微信后的回调
    @Override
    public void onResp(BaseResp baseResp) {

        final String code = ((SendAuth.Resp) baseResp).code;
        LogUtils.d("code: " + code);
        final StringBuilder authUrl = new StringBuilder();
        authUrl
                .append("https://api.weixin.qq.com/sns/oauth2/access_token?appid=")
                .append(LatteWeChat.APP_ID)
                .append("&secret=")
                .append(LatteWeChat.APP_SECRET)
                .append("&code=")
                .append(code)
                .append("&grant_type=authorization_code");

        LogUtils.d("authUrl", authUrl.toString());
        getAuth(authUrl.toString());
    }

    private void getAuth(String authUrl) {
        RestClient
                .builder()
                .url(authUrl)
                .requestAll()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        final JSONObject authObj = JSON.parseObject(response);
                        final String accessToken = authObj.getString("access_token");
                        final String openId = authObj.getString("openid");
                        LatteWeChat.getInstance().getSignInCallback().onSignInSuccess(openId);

//                        finish();
//                        overridePendingTransition(0, 0);

//                        final StringBuilder userInfoUrl = new StringBuilder();
//                        userInfoUrl
//                                .append("https://api.weixin.qq.com/sns/userinfo?access_token=")
//                                .append(accessToken)
//                                .append("&openid=")
//                                .append(openId)
//                                .append("&lang=")
//                                .append("zh_CN");
//
//                        LogUtils.d("userInfoUrl", userInfoUrl.toString());
//                        getUserInfo(userInfoUrl.toString());

                    }
                })
                .build()
                .get();
    }

    private void getUserInfo(String userInfoUrl) {
        RestClient
                .builder()
                .url(userInfoUrl)
                .requestAll()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
//                        LatteWeChat.getInstance().getSignInCallback().onSignInSuccess(response);
                        LogUtils.d("WeChat userInfo" + response);

                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {

                    }
                })
                .build()
                .get();
    }


}
