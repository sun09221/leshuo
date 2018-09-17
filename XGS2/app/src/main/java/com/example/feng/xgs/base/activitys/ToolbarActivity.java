package com.example.feng.xgs.base.activitys;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.feng.xgs.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2017/12/18.
 * 初始化toolbar
 */

public abstract class ToolbarActivity extends BaseActivity{

    @BindView(R.id.tv_toolbar_title)
    TextView mTitle;
    @BindView(R.id.iv_toolbar_back)
    ImageView mIvBack;

    public void setTitle(String title){
        mTitle.setText(title);
    }

    public void goneBack(){
        mIvBack.setVisibility(View.GONE);
    }

    @OnClick(R.id.iv_toolbar_back)
    void onClickBack(){
        finish();
    }
}
