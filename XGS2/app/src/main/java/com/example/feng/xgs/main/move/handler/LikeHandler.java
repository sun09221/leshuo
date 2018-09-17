package com.example.feng.xgs.main.move.handler;

import android.app.Activity;
import android.text.TextUtils;

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
 * 请求点赞接口
 */

public class LikeHandler {

    private Activity mActivity;

    private LikeHandler(Activity activity){
        this.mActivity = activity;
    }

    public static LikeHandler create(Activity activity){
        return new LikeHandler(activity);
    }

    //点赞
    public void like(String peopleId, String dynamicId, ILikeListener listener){
        this.mListener = listener;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PRAISE_PEOPLE_ID, peopleId);
        if(!TextUtils.isEmpty(dynamicId)){
            map.put(ParameterKeys.DYNAMIC_ID, dynamicId);
        }

        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.LIKE)
                .loader(mActivity)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(mActivity, "点赞成功");
                        if(mListener != null){
                            mListener.onLike();
                        }
                    }
                })
                .build().post();

    }
    //音频点赞
    public void musiclike(String musicid, ILikeListener listener){
        this.mListener = listener;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.MUSIC_ID, musicid);


        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.MUSICLIKE)
                .loader(mActivity)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(mActivity, "点赞成功");
                        if(mListener != null){
                            mListener.onLike();
                        }
                    }
                })
                .build().post();

    }
    private ILikeListener mListener;
    public interface ILikeListener{
        void onLike();
    }
}
