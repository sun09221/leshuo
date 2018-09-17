package com.example.feng.xgs.main.mine.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;

/**
 * Created by feng on 2018/5/9 0009.
 * 我的动态
 */

public class MineDynamicActivity extends ToolbarActivity{


    @Override
    public Object setLayout() {
        return R.layout.activity_mine_dynamic;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String peopleId = intent.getStringExtra(ContentKeys.ACTIVITY_PEOPLE_ID);
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);

        setTitle(title);

        beginFragment(peopleId);
    }

    private void beginFragment(String peopleId){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.layout_mine_dynamic,
                NaturalMineFragment.create(CodeKeys.DYNAMIC_PERSON, peopleId));
        transaction.commit();
    }

}
