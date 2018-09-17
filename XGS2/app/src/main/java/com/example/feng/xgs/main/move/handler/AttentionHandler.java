package com.example.feng.xgs.main.move.handler;

import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2018/5/7 0007.
 * 请求关注接口
 */

public class AttentionHandler {
    private Activity mActivity;

    private AttentionHandler(Activity activity){
        this.mActivity = activity;
    }

    public static AttentionHandler create(Activity activity){
        return new AttentionHandler(activity);
    }

    //关注
    public void attention(String attentionPeopleId, IAttentionListener listener){
        this.mListener = listener;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.ATTENTION_PEOPLE_ID, attentionPeopleId);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ATTENTION)
                .loader(mActivity)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(mActivity, "关注成功");
                        if(mListener != null){
                            mListener.onAttention();
                        }
                    }
                })
                .build().post();

    }

    private IAttentionListener mListener;
    public interface IAttentionListener{
        void onAttention();
    }
}
