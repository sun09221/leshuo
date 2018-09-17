package com.example.feng.xgs.main.area.info;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.BaseFragment;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/31 0031.
 * 参赛须知
 */

public class EntryInfoFragment extends BaseFragment {


    public static EntryInfoFragment create(String idOnly){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
        EntryInfoFragment fragment = new EntryInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.tv_entry_info)TextView mTvInfo;
    @Override
    public Object setLayout() {
        return R.layout.fragment_find_area_entry_info;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        loadData();

    }

    private void loadData(){
        Bundle args = getArguments();
        if(args == null) return;
        String mIdOnly = args.getString(ContentKeys.ACTIVITY_ID_ONLY);
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.AREA_ID, mIdOnly);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_AREA_INFO)
                .raw(raw)
                .loader(getContext())
              //  .toast()
               // .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        String content = JSON.parseObject(response).getString("content");
                        mTvInfo.setText(content);
                    }
                })
                .build().post();
    }
}
