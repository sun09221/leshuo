package com.example.feng.xgs.main.sign;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.nearby.LocationHandler;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by feng on 2018/3/19 0019.
 * 注册
 */

public class RegisterActivity extends SendCodeActivity {


    private String SEX_STATUS ;

    @BindView(R.id.et_register_name)
    EditText mEtName;
    @BindView(R.id.tv_register_code2)
    TextView pwdSwitch;
    @BindView(R.id.et_register_pwd)
    EditText mEtPwd;
    @BindView(R.id.et_register_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_register_code)
    EditText mEtCode;
    @BindView(R.id.tv_register_code)
    TextView mTvCode;
    @BindView(R.id.tv_register_men)
    TextView mTVMen;
    @BindView(R.id.tv_register_women)
    TextView mTVWomen;
    private LocationHandler mLocationHandler;
    private double mLatitude;
    private double mLongitude;
    boolean flag = false;
    @OnClick(R.id.tv_register_sure)
    void onClickRegister() {

        register();
    }

    @OnClick(R.id.tv_register_code)
    void onClickCode() {
        String mobile = mEtMobile.getText().toString();
        // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        sendSms(mobile);

    }

    @OnClick(R.id.tv_register_men)
    void onClickMen() {
        mTVMen.setSelected(true);
        mTVWomen.setSelected(false);
        SEX_STATUS = "1";
    }

    @OnClick(R.id.tv_register_women)
    void onClickWomen() {
        mTVMen.setSelected(false);
        mTVWomen.setSelected(true);
        SEX_STATUS = "0";
    }


    @Override
    public Object setLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void changeText(int mIndex) {
        if (mIndex > 0) {
            if (!mTvCode.isSelected()) {
                mTvCode.setSelected(true);
            }
            mTvCode.setText(String.format("%ss", mIndex));
        } else {
            mTvCode.setSelected(false);
            mTvCode.setText(getString(R.string.sign_get_code));
        }
    }

    //注册回调监听，放到发送和验证前注册，注意这里是子线程需要传到主线程中去操作后续提示
    EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            mHandler.sendMessage(msg);
        }

    };


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if (msg.arg2 == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (msg.arg1 == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    isCodeSuccess = true;
                    Log.e("tag",msg.obj.toString());
                    register();
                    //提交验证码成功
                } else if (msg.arg1 == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    isCodeSuccess = false;
                    ToastUtils.showText(RegisterActivity.this, "验证码获取成功");
                    //获取验证码成功
                } else if (msg.arg1 == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ToastUtils.showText(RegisterActivity.this, msg.obj.toString());
                ((Throwable) msg.obj).printStackTrace();
            }

            return false;
        }
    });


    protected void onStop() {
        super.onStop();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterEventHandler(eh);
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        super.onBindView(savedInstanceState);
        SMSSDK.registerEventHandler(eh);
        setTitle(getString(R.string.sign_register));

//        mTvCode.setSelected(true);
        mTVWomen.setSelected(true);
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
        pwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag) {
                    mEtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwdSwitch.setText("显示密码");
                } else {
                    mEtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwdSwitch.setText("隐藏密码");
                }
                flag = !flag;
                mEtPwd.postInvalidate();
                CharSequence text = mEtPwd.getText();
                if (text instanceof Spannable) {
                    Spannable spanText = (Spannable) text;
                    Selection.setSelection(spanText, text.length());
                }
            }
        });
    }

    public void register() {
        final String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showText(this, getString(R.string.edit_sign_name));
            return;
        }

        final String mobile = mEtMobile.getText().toString().trim();
        if (mobile.length() != 11) {
            ToastUtils.showText(this, getString(R.string.edit_mobile));
            return;
        }

        String pwd = mEtPwd.getText().toString();
        if (pwd.length() < 6 || pwd.length() > 20) {
            ToastUtils.showText(this, getString(R.string.edit_sign_pwd));
            return;
        }


        String code = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showText(this, getString(R.string.edit_sign_code));
            return;
        } else {
            if (!isCodeSuccess) {
                SMSSDK.submitVerificationCode("86", mobile, code);
            }
            // 提交验证码，其中的code表示验证码，如“1357”

        }

        if (mLongitude == -1 || mLatitude == -1) {
            ToastUtils.showText(this, "正在获取经纬度，请稍候...");
            return;
        }

        if (isCodeSuccess) {
            reg(name, mobile, pwd);
        }

    }

    private boolean isCodeSuccess = false;

    private void reg(final String name, String mobile, String pwd) {
        Map<String, Object> map = new HashMap<>();
        map.put(ParameterKeys.USER_NAME, name);
        map.put(ParameterKeys.USER_MOBILE, mobile);
        map.put(ParameterKeys.USER_PASSWORD, pwd);
        map.put(ParameterKeys.SHOP_LONGITUDE, String.valueOf(mLongitude));
        map.put(ParameterKeys.SHOP_LATITUDEE, String.valueOf(mLatitude));
        map.put(ParameterKeys.USER_SEX, SEX_STATUS);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SIGN_REGISTER)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        JSONObject object = JSON.parseObject(response);
                        String jPushUserName = object.getString("jgusername");
                        String jPushPassword = object.getString("jgpassword");
                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_NAME, name);
                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.J_PUSH_USER_NAME, jPushUserName);
                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.J_PUSH_PASSWORD, jPushPassword);
                        ToastUtils.showText(RegisterActivity.this, "注册成功");
                        finish();
                    }
                }).build().post();
    }


    public void register(String mobile, String pwd) {
        //极光im注册
        JMessageClient.register(mobile, pwd, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                Log.d(TAG, "gotResult: 注册状态" + i + "注册信息: " + s);
                if (i == 0) {
                    finish();
                }
            }
        });
    }

}
