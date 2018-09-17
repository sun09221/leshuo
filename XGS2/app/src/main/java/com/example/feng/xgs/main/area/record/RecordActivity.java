package com.example.feng.xgs.main.area.record;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.main.area.record.comment.RecordCommentListFragment;
import com.example.feng.xgs.main.area.record.gift.RecordGiftFragment;
import com.example.feng.xgs.main.area.record.like.RecordLikeFragment;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/30 0030.
 * 个人中心-我的记录
 */

public class RecordActivity extends ToolbarActivity implements ScrollTab.OnTabListener {


    @BindView(R.id.tab_record)ScrollTab mTab;
    @BindView(R.id.vp_record)ViewPager mViewPager;


    @Override
    public Object setLayout() {
        return R.layout.activity_mine_record;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_record));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initViewPager();
    }

    private void initViewPager(){
        List<String> titles = new ArrayList<>();
        titles.add("礼物记录");
        titles.add("点赞记录");
        titles.add("留言记录");

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(RecordGiftFragment.create(CodeKeys.RECORD_MINE, ""));
        fragments.add(RecordLikeFragment.create(CodeKeys.RECORD_MINE, ""));
        fragments.add(RecordCommentListFragment.create(CodeKeys.RECORD_MINE, ""));

        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mTab.setTitles(titles);
        mTab.setViewPager(mViewPager);
        mTab.setOnTabListener(this);
    }

    @Override
    public void onChange(int position, View v) {
        mViewPager.setCurrentItem(position);
    }
}
