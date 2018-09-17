package com.example.feng.xgs.main.mine.earnings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
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
 * Created by feng on 2018/5/8 0008.
 * 提现
 */

public class WithdrawActivity extends ToolbarActivity {

    @BindView(R.id.tv_ali)
    TextView mTvAli;
    @BindView(R.id.tv_we_chat)
    TextView mTvWeChat;
    @BindView(R.id.et_withdraw_price)
    EditText mEtPrice;

    private String mPayType = ContentKeys.WE_CHAT;
    private String mItemType;

    //支付宝
    @OnClick(R.id.tv_ali)void onClickAli(){
        mTvAli.setSelected(true);
        mTvWeChat.setSelected(false);
        mPayType = ContentKeys.ALI_PAY;
    }

    //微信
    @OnClick(R.id.tv_we_chat)void onClickWeChat(){
        mTvAli.setSelected(false);
        mTvWeChat.setSelected(true);
        mPayType = ContentKeys.WE_CHAT;
    }

    //提现
    @OnClick(R.id.tv_withdraw_sure)void onClickWithdraw(){
        withdraw();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_earnings_withdraw;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mItemType = intent.getStringExtra(ContentKeys.ACTIVITY_TYPE);

        setTitle(getString(R.string.mine_withdraw));
        mTvWeChat.setSelected(true);
    }


    private void withdraw(){
        String price = mEtPrice.getText().toString();
        if(TextUtils.isEmpty(price)){
            ToastUtils.showText(this, "请输入提现金额");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ACCOUNT_TYPE, mPayType);
        map.put(ParameterKeys.ITEM, mItemType);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.MONEY, price);
        map.put(ParameterKeys.TYPE, "2");//类型： 1充值 2提现
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.EARNINGS)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(WithdrawActivity.this, "提现成功");
                        finish();
                    }
                })
                .build().post();

    }

}
