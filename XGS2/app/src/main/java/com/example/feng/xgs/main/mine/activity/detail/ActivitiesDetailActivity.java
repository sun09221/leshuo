package com.example.feng.xgs.main.mine.activity.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/7 0007.
 * 活动详情页面
 */

public class ActivitiesDetailActivity extends ToolbarActivity {

    @BindView(R.id.iv_activities_detail)
    ImageView mIvDetail;
    @BindView(R.id.tv_activities_detail_name)
    TextView mTvName;
    @BindView(R.id.tv_activities_detail_time)
    TextView mTvTime;
    @BindView(R.id.tv_activities_detail_info)
    TextView mTvInfo;
    @BindView(R.id.tv_activities_detail_price)
    TextView mTvPrice;
    @BindView(R.id.tv_activities_detail_address)
    TextView mTvAddress;
    @BindView(R.id.tv_activities_detail_sure)
    TextView mTvSure;
    private String mType;
    private String mIdOnly;

    @OnClick(R.id.tv_activities_detail_sure)void onClick(){
//        if(ContentKeys.FIND_ACTIVITY_ENROLL.equals(mType)){
            Intent intent = new Intent(this, ActivitiesEnrollActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mIdOnly);
            startActivity(intent);
//        }
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_activities_detail;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mIdOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        setTitle("活动详情");

        mType = intent.getStringExtra(ContentKeys.ACTIVITY_TYPE);
        if(ContentKeys.FIND_ACTIVITY_ENROLL.equals(mType)){
            mTvSure.setText(getString(R.string.enroll));
        }else if(ContentKeys.FIND_ACTIVITY_NOT_ENROLL.equals(mType)){
            mTvSure.setText(getString(R.string.join_activity));
        }else {
            return;
        }

        loadData();
    }

    private void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, mIdOnly);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_ACTIVITY_DETAIL)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String idOnly = object.getString("id");
                        String name = object.getString("activityname");
                        String address = object.getString("address");
                        String time = object.getString("activitytime");
                        String price = object.getString("money");
                        String information = object.getString("information");
                        String detail = object.getString("detailedcontent");
                        String imgUrl = object.getString("images");
                        String type = object.getString("type");

                        loadImg(imgUrl, mIvDetail);
                        mTvName.setText(name);
                        mTvAddress.setText("地址: " + address);
                        mTvTime.setText(time);
                        mTvInfo.setText(detail);

                        if(ContentKeys.FIND_ACTIVITY_ENROLL.equals(type)){
                            mTvPrice.setText(price);
                            mTvPrice.setVisibility(View.VISIBLE);
                        }else {
                            mTvPrice.setVisibility(View.GONE);
                        }
                    }
                })
                .build().post();
    }

}
