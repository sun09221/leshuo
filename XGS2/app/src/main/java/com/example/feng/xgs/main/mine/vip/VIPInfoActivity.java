package com.example.feng.xgs.main.mine.vip;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/8 0008.
 * VIP优势
 */

public class VIPInfoActivity extends ToolbarActivity{

    @BindView(R.id.vip_mine_name)TextView vip_mine_name;
    @BindView(R.id.vip_mine_head)ImageView vip_mine_head;
    @BindView(R.id.vip_mine_prompt)TextView vip_mine_prompt;
    @BindView(R.id.vip_mine_pass)TextView vip_mine_pass;
    private String nikename;
    private String type;
    private String head;

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_agreementvip;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_vip_info));
        loadData();
        vip_mine_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(VIPInfoActivity.this,VIPActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.USER_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSONObject.parseObject(response);
                        nikename = object.getString("nickname");
                        head = object.getString("header");
                        type = object.getString("header");
                        vip_mine_name.setText(nikename);
                        loadImg(head, vip_mine_head);
                        String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
                        if (type.equals("1")){
                            vip_mine_prompt.setText("尽享会员权限");
                            vip_mine_pass.setVisibility(View.GONE);
                        }else{
                            vip_mine_prompt.setText("暂未激活会员");
                            vip_mine_pass.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .build().post();
    }
}
