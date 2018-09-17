package com.example.feng.xgs.main.area.model.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/6/5 0005.
 * 模特认证
 */

public class ModelAuthenticationActivity extends ToolbarActivity {

    @BindView(R.id.et_model_authentication_card_number)
    EditText mEtCardNumber;
    @BindView(R.id.et_model_authentication_mobile)
    EditText mEtMobile;

    //提交
    @OnClick(R.id.tv_model_authentication_sure)
    void onClick() {
        submit();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_model_authentication;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.bind));
    }

    private void submit() {

        String carNumber = mEtCardNumber.getText().toString();
        if (TextUtils.isEmpty(carNumber)) {
            ToastUtils.showText(this, getString(R.string.edit_id_card_number));
            return;
        }

        String mobile = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showText(this, getString(R.string.edit_mobile));
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.MOBILE, mobile);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.ID_CARD, carNumber);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.MODEL_AUTHENTICATION)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        SharedPreferenceUtils.setAppFlag(ShareKeys.IS_MODEL, true);
                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.MODEL_ID, object.getString("modelid"));
                        finish();
                    }
                })
                .build().post();
    }
}
