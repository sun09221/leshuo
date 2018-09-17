package com.example.feng.xgs.main.sign;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.amap.api.location.AMapLocation;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.pay.wechat.IWeChatSignInCallback;
import com.example.feng.xgs.core.pay.wechat.LatteWeChat;
import com.example.feng.xgs.main.nearby.LocationHandler;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/3/19 0019.
 * 登录
 */

public class LoginActivity extends ToolbarActivity {

    @BindView(R.id.et_login_name)
    EditText mEtName;
    @BindView(R.id.et_login_pwd)
    EditText mEtPwd;
    @BindView(R.id.tv_login_remember)
    TextView mTvRemember;
    private boolean isRemember = false;
    public String sex_selection;
    //登录
    @OnClick(R.id.tv_login_sure)
    void onClickLogin() {
        login();
    }

    //注册
    @OnClick(R.id.tv_login_register)
    void onClickRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    //记住密码
    @OnClick(R.id.tv_login_remember)
    void onClickRemember() {
        isRemember = !isRemember;
        mTvRemember.setSelected(isRemember);
        Log.d(TAG, "onClickRemember: " + isRemember);
        SharedPreferenceUtils.setAppFlag(ShareKeys.SIGN_IS_REMEMBER, isRemember);
    }

    //忘记密码
    @OnClick(R.id.tv_login_forget)
    void onClickForget() {
        startActivity(new Intent(this, ForgetPwdActivity.class));
    }

    //微信登录
    @OnClick(R.id.iv_login_we_chat)
    void onClickWeChat() {
        LatteWeChat.getInstance().init(this)
                .onSignSuccess(new IWeChatSignInCallback() {
                    @Override
                    public void onSignInSuccess(String userInfo) {

                        Map<String, String> map = new HashMap<>();
                        map.put(ParameterKeys.OPEN_ID, userInfo);
                        map.put(ParameterKeys.SHOP_LONGITUDE, String.valueOf(mLongitude));
                        map.put(ParameterKeys.SHOP_LATITUDEE, String.valueOf(mLatitude));
                        String raw = JSON.toJSONString(map);

                        RestClient.builder()
                                .url(UrlKeys.WE_CHAT_LOGIN)
                                .raw(raw)
                                .loader(LoginActivity.this)
                               // .toast()
                                .success(new ISuccess() {
                                    @Override
                                    public void onSuccess(String response) {
                                        ToastUtils.showText(LoginActivity.this, "微信授权登录成功");
                                        SignHandler.create().login(response, LoginActivity.this, "123456");
//                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                                        finish();
                                    }
                                })
                                .build().post();

                    }
                })
                .signIn();
    }

    //qq登录
    @OnClick(R.id.iv_login_qq)
    void onClickQQ() {

    }

    @Override
    public Object setLayout() {
        return R.layout.activity_login;
    }

    private LocationHandler mLocationHandler;
    private double mLatitude;
    private double mLongitude;

    @Override
    public void onBindView(Bundle savedInstanceState) {
        goneBack();
        setTitle(getString(R.string.sign_login));

        initUserName();
        startLocation();
    }

    /**
     * 定位成功后开始请求数据
     */
    private void startLocation() {
        mLocationHandler = LocationHandler.create(this);
        mLocationHandler.initLocation(new LocationHandler.ILocationResultListener() {
            @Override
            public void onLocationResult(AMapLocation location) {
//                mCity = location.getCity();
                mLatitude = location.getLatitude();
                mLongitude = location.getLongitude();


            }
        });
    }

    /**
     * 执行登录操作
     */
    private void login() {
        final String name = mEtName.getText().toString();
//        if(name.length() != 11){
//            ToastUtils.showText(this, getString(R.string.edit_sign_mobile));
//            return;
//        }

        final String pwd = mEtPwd.getText().toString();
        if (pwd.length() < 6 || pwd.length() > 20) {
            ToastUtils.showText(this, getString(R.string.edit_sign_pwd));
            return;
        }


        Map<String, Object> map = new HashMap<>();
        map.put(ParameterKeys.USER_NAME, name);
        map.put(ParameterKeys.USER_PASSWORD, pwd);
        map.put(ParameterKeys.SHOP_LONGITUDE, String.valueOf(mLongitude));
        map.put(ParameterKeys.SHOP_LATITUDEE, String.valueOf(mLatitude));
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SIGN_LOGIN)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        if (isRemember) {
                            SharedPreferenceUtils.setCustomAppProfile(ShareKeys.SIGN_PWD, pwd);
                        } else {
                            SharedPreferenceUtils.setCustomAppProfile(ShareKeys.SIGN_PWD, "");
                        }

                        SignHandler.create().login(response, LoginActivity.this, pwd);
                    }
                })
                .build().post();

    }


    /**
     * 当记住密码时，获取密码，并设置到EditText上
     */
    private void initUserName() {
        String mobile = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_NAME);
        if (!TextUtils.isEmpty(mobile)) {
            mEtName.setText(mobile);
        }

        isRemember = SharedPreferenceUtils.getAppFlag(ShareKeys.SIGN_IS_REMEMBER);
        Log.d(TAG, "onBindView: " + isRemember);
        if (isRemember) {
            String pwd = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.SIGN_PWD);
            mTvRemember.setSelected(true);
            Log.d(TAG, "onBindView: " + pwd);
            if (!TextUtils.isEmpty(pwd)) {
                mEtPwd.setText(pwd);
            }
        }
    }

}
