package com.example.feng.xgs.main.mine.setting;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
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
 * 意见反馈
 */

public class FeedbackActivity extends ToolbarActivity {

    @BindView(R.id.tv_feedback_qq)TextView mTvQQ;
    @BindView(R.id.et_feedback_info)EditText mETInfo;

    //提交
    @OnClick(R.id.tv_feedback_sure)void onClickSubmit(){
        submit();
    }
    @Override
    public Object setLayout() {
        return R.layout.activity_mine_feedback;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_setting_feedback));

    }

    private void submit(){
        String info = mETInfo.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, getString(R.string.edit_feedback_hint));
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.CONTENT, info);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FEEDBACK)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(FeedbackActivity.this, "反馈成功");
                        finish();
                    }
                })
                .build().post();

    }
}
