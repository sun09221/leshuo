package com.example.feng.xgs.main.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/8 0008.
 * 用户协议
 */

public class AgreementActivity extends ToolbarActivity{

    @BindView(R.id.tv_agreement_info)TextView mTvInfo;
    @Override
    public Object setLayout() {
        return R.layout.activity_mine_agreement;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {

        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        String link = intent.getStringExtra(ContentKeys.ACTIVITY_LINK);

        setTitle(title);


        RestClient.builder()
                .url(link)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String info = object.getString("content");
                        mTvInfo.setText(info);
                    }
                })
                .build().post();
//        String info = "1、用户在使用本服务前需要注册一个“陌陌”帐号。“陌陌”帐号应当使用手机号码绑定注册，请用户使用尚未与“陌陌”帐号绑定的手机号码，以及未被陌陌科技根据本协议封禁的手机号码注册“陌陌”帐号。陌陌科技可以根据用户需求或产品需要对帐号注册和绑定的方式进行变更，而无须事先通知用户。\n" +
//                "2、“陌陌”系基于地理位置的移动社交产品，用户注册时应当授权陌陌科技公开及使用其地理位置信息方可成功注册“陌陌”帐号。故用户完成注册即表明用户同意陌陌科技提取、公开及使用用户的地理位置信息。如用户需要终止向其他用户公开其地理位置信息，可自行设置为隐身状态。\n" +
//                "3、鉴于“陌陌”帐号的绑定注册方式，您同意陌陌科技在注册时将使用您提供的手机号码及/或自动提取您的手机号码及自动提取您的手机设备识别码等信息用于注册。\n" +
//                "4、在用户注";


    }
}
