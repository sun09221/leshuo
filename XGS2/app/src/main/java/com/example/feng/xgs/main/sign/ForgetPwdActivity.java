package com.example.feng.xgs.main.sign;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/3/19 0019.
 * 找回密码
 */

public class ForgetPwdActivity extends SendCodeActivity {


    @BindView(R.id.et_forget_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_forget_code)
    EditText mEtCode;
    @BindView(R.id.tv_forget_code)
    TextView mTvCode;
    @BindView(R.id.et_forget_pwd_new)
    EditText mEtPwd;
    @BindView(R.id.et_forget_pwd_new_sure)
    EditText mEtPwdSure;

    @OnClick(R.id.tv_forget_sure)void onClickForget(){
        findPwd();
    }
    @OnClick(R.id.tv_forget_code)void onClickCode(){
        String mobile = mEtMobile.getText().toString();
        sendSms(mobile);
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    public void changeText(int mIndex) {
        if(mIndex > 0){
            if(mTvCode.isSelected()){
                mTvCode.setSelected(false);
            }
            mTvCode.setText(String.format("%ss", mIndex));
        }else {
            mTvCode.setSelected(true);
            mTvCode.setText(getString(R.string.sign_get_code));
        }
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        super.onBindView(savedInstanceState);
        setTitle(getString(R.string.sign_find_pwd));
        mTvCode.setSelected(true);
    }

    private void findPwd(){
        String mobile = mEtMobile.getText().toString().trim();
        if(mobile.length() != 11){
            ToastUtils.showText(this, getString(R.string.edit_mobile));
            return;
        }

        String code = mEtCode.getText().toString().trim();
        if(TextUtils.isEmpty(code)){
            ToastUtils.showText(this, getString(R.string.edit_sign_code));
            return;
        }

        String pwd = mEtPwd.getText().toString();
        if(pwd.length() <6 || pwd.length() > 20){
            ToastUtils.showText(this, getString(R.string.edit_sign_pwd));
            return;
        }

        String pwdSure = mEtPwdSure.getText().toString();
        if(!pwd.equals(pwdSure)){
            ToastUtils.showText(this, "两次输入的密码不一致");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put(ParameterKeys.USER_MOBILE, mobile);
        map.put(ParameterKeys.USER_PASSWORD, pwd);
//        map.put(ParameterKeys.CODE, code);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SIGN_FORGET)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(ForgetPwdActivity.this, "密码修改成功");
                        finish();
                    }
                }).build().post();


    }


}
