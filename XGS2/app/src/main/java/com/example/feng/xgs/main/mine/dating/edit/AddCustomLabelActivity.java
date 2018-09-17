package com.example.feng.xgs.main.mine.dating.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

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
 * Created by feng on 2018/5/16 0016.
 * 添加自定义标签
 */

public class AddCustomLabelActivity extends ToolbarActivity{


    @BindView(R.id.et_edit_info)EditText mEtInfo;
    private String mIdOnly;

    @OnClick(R.id.tv_edit_info_sure)void onClickSure(){
        addLabel();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_dating_edit_info;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mIdOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        setTitle("添加自定义标签");

    }

    private void addLabel(){

        final String label = mEtInfo.getText().toString();
        if(TextUtils.isEmpty(label)){
            ToastUtils.showText(this, "自定义标签不能为空");
            return;
        }


        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.LABEL_ID, mIdOnly);
        map.put(ParameterKeys.NAME, label);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.LABEL_ADD)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Intent intent = new Intent(AddCustomLabelActivity.this, LabelSelectActivity.class);
                        intent.putExtra(ContentKeys.ACTIVITY_INFO, label);
                        setResult(CodeKeys.FOR_RESULT_CODE, intent);
                        finish();
                    }
                })
                .build().post();
    }


}
