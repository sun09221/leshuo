package com.example.feng.xgs.main.mine.account;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
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
 * Created by feng on 2018/5/9 0009.
 * 绑定账户
 */

public class AccountAddActivity extends ToolbarActivity {

    @BindView(R.id.tv_ali)
    TextView mTvAli;
    @BindView(R.id.tv_we_chat)
    TextView mTvWeChat;
    @BindView(R.id.et_account_add_name)
    EditText mEtName;
    @BindView(R.id.et_account_add_number)
    EditText mEtNumber;

    private String mPayType = ContentKeys.ACCOUNT_WE_CHAT;

    //支付宝
    @OnClick(R.id.tv_ali)void onClickAli(){
        mTvAli.setSelected(true);
        mTvWeChat.setSelected(false);
        mPayType = ContentKeys.ACCOUNT_ALI;
    }

    //微信
    @OnClick(R.id.tv_we_chat)void onClickWeChat(){
        mTvAli.setSelected(false);
        mTvWeChat.setSelected(true);
        mPayType = ContentKeys.ACCOUNT_WE_CHAT;
    }

    //立即绑定
    @OnClick(R.id.tv_account_add_sure)void onClickSure(){
        bind();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_account_add;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_account));
        mTvWeChat.setSelected(true);
    }

    private void bind(){
        String name = mEtName.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showText(this, getString(R.string.edit_account_name));
            return;
        }

        String number = mEtNumber.getText().toString();
        if(TextUtils.isEmpty(number)){
            ToastUtils.showText(this, getString(R.string.edit_account_number));
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.NAME, name);
        map.put(ParameterKeys.NUMBER, number);
        map.put(ParameterKeys.TYPE, mPayType);

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.ACCOUNT_APPEND)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(AccountAddActivity.this, "绑定成功");
                        setResult(CodeKeys.FOR_RESULT_CODE);
                        finish();
                    }
                })
                .build().post();



    }

}
