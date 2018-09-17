package com.example.feng.xgs.main.area.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.BaseFragment;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.area.record.gift.RecordGiftFragment;
import com.example.feng.xgs.main.area.record.like.RecordLikeFragment;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;
import com.example.feng.xgs.ui.weight.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by feng on 2018/6/1 0001.
 * 模特--查看记录
 */

public class ModelRecordFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.vp_find_student_record)NoScrollViewPager mViewPager;
    @BindView(R.id.rg_find_student_record)RadioGroup mRgTabs;
    private List<RadioButton> mTabs;
    private int mTarIndex;

    public static ModelRecordFragment create(String idOnly){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
        ModelRecordFragment fragment = new ModelRecordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_find_student_record;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        initRadioGroup();
        initViewPager();
    }

    private void initRadioGroup(){
        mTabs = new ArrayList<>();
        int count = mRgTabs.getChildCount();
        for (int i = 0; i < count; i++) {
            mTabs.add((RadioButton) mRgTabs.getChildAt(i));
        }

        mRgTabs.setOnCheckedChangeListener(this);
    }

    private void initViewPager(){
        Bundle args = getArguments();
        if(args == null) return;
        String modelId = args.getString(ContentKeys.ACTIVITY_ID_ONLY);

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(RecordLikeFragment.create(CodeKeys.RECORD_MODEL, modelId));
        fragments.add(RecordGiftFragment.create(CodeKeys.RECORD_MODEL, modelId));
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getChildFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
        mTabs.get(0).setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < mTabs.size(); i++) {
            if(checkedId == mTabs.get(i).getId()){
                mTarIndex = i;
//                Log.d(TAG, "onCheckedChanged: "+ mTarIndex);
                break;
            }
        }
        mViewPager.setCurrentItem(mTarIndex, false);
    }
}
