package com.example.feng.xgs.main.mine.dating.edit;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.utils.manager.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/19 0019.
 * 我的问题，回答页面
 */

public class EditQuestionActivity extends ToolbarActivity{


    private String mQuestion;
    @BindView(R.id.tv_question_answer)TextView mTvQuestion;
    @BindView(R.id.et_question_answer)EditText mEtAnswer;

    @OnClick(R.id.tv_toolbar_save)void onClickSave(){
        String answer = mEtAnswer.getText().toString();

        if(TextUtils.isEmpty(mQuestion)){
            ToastUtils.showText(this, "请重新选择问题");
            return;
        }
        if(TextUtils.isEmpty(answer)){
            ToastUtils.showText(this, "请输入回答");
            return;
        }

        Intent intent = new Intent(this, LabelSelectActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, mQuestion);
        intent.putExtra(ContentKeys.ACTIVITY_ANSWER, answer);
        setResult(CodeKeys.FOR_RESULT_CODE, intent);
        finish();

    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_dating_question_answer;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle("我的回答");
        Intent intent = getIntent();
        if(intent == null){
            return;
        }

        mQuestion = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        mTvQuestion.setText(mQuestion);
    }
}
