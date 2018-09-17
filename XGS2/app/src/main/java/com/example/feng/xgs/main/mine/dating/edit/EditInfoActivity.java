package com.example.feng.xgs.main.mine.dating.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.main.mine.dating.DatingActivity;
import com.example.feng.xgs.utils.manager.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/10 0010.
 */

public class EditInfoActivity extends ToolbarActivity{

    @BindView(R.id.et_edit_info)EditText mEtInfo;
    private String mInfo;

    @OnClick(R.id.tv_edit_info_sure)void onClickSure(){
        sure();
    }
    @Override
    public Object setLayout() {
        return R.layout.activity_mine_dating_edit_info;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mInfo = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        setTitle(title);

        if(!TextUtils.isEmpty(mInfo)){
            mEtInfo.setText(mInfo);
        }
    }

    private void sure(){
        String info = mEtInfo.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, "输入内容不能为空");
        }


        Intent intent = new Intent(this, DatingActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, info);
        setResult(CodeKeys.FOR_RESULT_CODE, intent);
        finish();
    }
}
