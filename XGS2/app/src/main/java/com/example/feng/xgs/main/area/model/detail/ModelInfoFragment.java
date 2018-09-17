package com.example.feng.xgs.main.area.model.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.BaseFragment;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.area.model.detail.gift.GiftListActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/31 0031.
 * 我是模特--基本信息
 */

public class ModelInfoFragment extends BaseFragment {


    private String mModelId;

    public static ModelInfoFragment create(String infoResult, int type){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_INFO, infoResult);
        args.putInt(ContentKeys.ACTIVITY_TYPE, type);
        ModelInfoFragment fragment = new ModelInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.tv_model_info_name)
    TextView mTvName;
    @BindView(R.id.tv_model_info_info)
    TextView mTvInfo;
    @BindView(R.id.tv_model_info_gift)
    TextView mTvGift;
    @BindView(R.id.tv_model_info_like)
    TextView mTvLike;

    //送礼物
    @OnClick(R.id.tv_model_info_gift)void onClickGift(){
        Intent intent = new Intent(getContext(), GiftListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mModelId);
        startActivity(intent);
    }

    //点赞
    @OnClick(R.id.tv_model_info_like)void onClickLike(){
        like();
    }



    @Override
    public Object setLayout() {
        return R.layout.fragment_mine_model_info;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        initData();
    }

    private void initData(){
        Bundle args = getArguments();
        if(args == null) return;
        String info = args.getString(ContentKeys.ACTIVITY_INFO);
        int type = args.getInt(ContentKeys.ACTIVITY_TYPE);
        if(type == CodeKeys.MODEL_PERSON){
            mTvGift.setVisibility(View.GONE);
            mTvLike.setVisibility(View.GONE);
        }

        JSONObject object = JSON.parseObject(info);
        String isLike = object.getString("state");//是否点赞 0否1是
        String name = object.getString("name");
        String phone = object.getString("phone");
        String idcard = object.getString("idcard");
        String ballotnumber = object.getString("ballotnumber");//投票数
        mModelId = object.getString("id");
        String time = object.getString("createtime");
        String imgUrl = object.getString("imgpath");
        String raceareaId = object.getString("raceareaid");//区域id
        String likeCount = object.getString("likesnum");//点赞数
        String rewardCount = object.getString("rewardcount");//礼物数
        String peopleid = object.getString("peopleid");
        String rank = object.getString("rankcount");//排名
        String popularity = object.getString("popularity");//人气数
        SharedPreferenceUtils.setCustomAppProfile2(ShareKeys.PEOPLE, peopleid);
        mTvName.setText(name);
        String infoModel = "排名: " + rank + "\n总票数: " + ballotnumber + "\n人气值: " + popularity;
        mTvInfo.setText(infoModel);

        mTvGift.setText(rewardCount);
        mTvLike.setText(likeCount);
    }


    private void like(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.MODEL_ID, mModelId);

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.FIND_MODEL_LIKE)
                .raw(raw)
                .loader(getContext())
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(getContext(), "点赞成功");
                        mTvLike.setText((Integer.parseInt(mTvLike.getText().toString())+1)+"");
                    }
                })
                .build().post();
    }

}
