package com.example.feng.xgs.main.mine.setting;

import android.content.Intent;
import android.os.Bundle;

import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.main.mine.setting.help.HelpActivity;
import com.example.feng.xgs.main.sign.LoginActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;

/**
 * Created by feng on 2018/5/2 0002.
 * icon_mine_setting
 */

public class SettingActivity extends ToolbarActivity{

    //退出登录
    @OnClick(R.id.tv_setting_exit)void onClickExit(){
        JMessageClient.logout();

        Intent intent = new Intent(this, LoginActivity.class);
        SharedPreferenceUtils.setAppSign(false);

        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_TYPE, ContentKeys.USER_TYPE_NORMAL);
        SharedPreferenceUtils.setAppFlag(ShareKeys.IS_MODEL, false);
        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.J_PUSH_USER_NAME, "");
        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.J_PUSH_PASSWORD, "");
        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_HEAD, "");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    //用户协议
    @OnClick(R.id.tv_setting_agreement)void onClickAgreement(){
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.mine_setting_agreement));
        intent.putExtra(ContentKeys.ACTIVITY_LINK, UrlKeys.AGREEMENT);
        startActivity(intent);
    }

    //使用帮助
    @OnClick(R.id.tv_setting_help)void onClickHelp(){
        startActivity(new Intent(this, HelpActivity.class));
    }

    //意见反馈
    @OnClick(R.id.tv_setting_feedback)void onClickFeedback(){
        startActivity(new Intent(this, FeedbackActivity.class));
    }

    //关于我们
    @OnClick(R.id.tv_setting_about)void onClickAbout(){
        Intent intent = new Intent(this, AgreementActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.mine_setting_about));
        intent.putExtra(ContentKeys.ACTIVITY_LINK, UrlKeys.ABOUT_ME);
        startActivity(intent);
    }


    @Override
    public Object setLayout() {
        return R.layout.activity_mine_setting;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_setting));
    }
}
