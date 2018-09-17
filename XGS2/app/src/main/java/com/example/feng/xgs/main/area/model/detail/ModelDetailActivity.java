package com.example.feng.xgs.main.area.model.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.area.ranking.RankingFragment;
import com.example.feng.xgs.main.area.record.ModelRecordFragment;
import com.example.feng.xgs.main.area.record.comment.RecordCommentFragment;
import com.example.feng.xgs.main.area.record.gift.RecordGiftFragment;
import com.example.feng.xgs.main.area.record.like.RecordLikeFragment;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;
import com.example.feng.xgs.ui.weight.NoScrollViewPager;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/6/1 0001.
 * 学员详情
 */

public class ModelDetailActivity extends ToolbarActivity implements RadioGroup.OnCheckedChangeListener {


    @BindView(R.id.iv_mine_model)
    ImageView mIvDetail;
    @BindView(R.id.rg_mine_model)
    RadioGroup mRgTab;
    @BindView(R.id.vp_shop_detail)
    NoScrollViewPager mViewPager;
    private List<RadioButton> mTabs = new ArrayList<>();
    private int mTarIndex = 0;
    private String mModelId ,mDynamicId ;;
    private String mAreaId;
    private int mType;

    @Override
    public Object setLayout() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.activity_mine_model;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mModelId = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        mDynamicId = intent.getStringExtra(ContentKeys.ACTIVITY_ONLY);
        mAreaId = intent.getStringExtra(ContentKeys.ACTIVITY_AREA_ID);
        mType = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, -1);
        setTitle(title);
        loadData();
//        initViewPager();
    }

    private void initViewPager(String infoResult) {
        int buttonCount = mRgTab.getChildCount();
        for (int i = 0; i < buttonCount; i++) {
            RadioButton radioButton = (RadioButton) mRgTab.getChildAt(i);
            mTabs.add(radioButton);
            //如果是查看其它模特信息，修改tab文字及切换不同的fragment
            if(mType == CodeKeys.MODEL_OTHER){
                switch (i) {
                    case 1:
                        radioButton.setText(getString(R.string.find_fans_comment));
                        break;
                    case 2:
                        radioButton.setText(getString(R.string.find_look_record));
                        break;
                    case 3:
                        radioButton.setText(getString(R.string.find_new_ranking));
                        break;
                    default:
                        break;
                }
            }
        }

        mRgTab.setOnCheckedChangeListener(this);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ModelInfoFragment.create(infoResult, mType));
        if(CodeKeys.MODEL_PERSON == mType){//模特查看自己的信息

            fragments.add(RecordLikeFragment.create(CodeKeys.RECORD_MODEL, mModelId));
            fragments.add(RecordCommentFragment.create(CodeKeys.RECORD_MODEL, mModelId));
            fragments.add(RecordGiftFragment.create(CodeKeys.RECORD_MODEL, mModelId));

        }else if(CodeKeys.MODEL_OTHER == mType){//查看其它模特的信息
            fragments.add(RecordCommentFragment.create(CodeKeys.RECORD_MODEL, mModelId));
            fragments.add(ModelRecordFragment.create(mModelId));
            fragments.add(RankingFragment.create(mAreaId, RankingFragment.TYPE_HIDE_HEAD));
        }else {
            return;
        }

        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(fragments.size() - 1);
        mTabs.get(0).setChecked(true);


    }


    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.MODEL_ID, mModelId);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_MODEL_DETAIL)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String imgUrl = object.getString("imgpath");
                        loadImg(imgUrl, mIvDetail);
                        initViewPager(response);
                    }
                })
                .build().post();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < mTabs.size(); i++) {
            if (checkedId == mTabs.get(i).getId()) {
                mTarIndex = i;
                LogUtils.d(TAG, "onCheckedChanged: " + mTarIndex);
                break;
            }
        }
        mViewPager.setCurrentItem(mTarIndex, false);
    }


}
