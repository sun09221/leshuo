package com.example.feng.xgs.main.sign;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.MainActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.main.mine.info.MineInformationActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by feng on 2018/5/2 0002.
 */

public class SignHandler {

    private SignHandler(){

    }

    public static SignHandler create(){
        return new SignHandler();
    }

    public void login(String result, final Activity activity, String pwd){
        JSONObject object = JSONObject.parseObject(result);
        final String detailstate;
        if(result.indexOf("detailstate")!=-1){
             detailstate = object.getString("detailstate");  //0 未完善 1 已完善
        }else {
             detailstate = "1";  //0 未完善 1 已完善
        }
        final String userId = object.getString("id");
        final String peopleId = object.getString("peopleid");
        final String mobile = object.getString("phone");
        final String userName = object.getString("userName");
        final String isModel = object.getString("isModel");//0:不是，1：是
        final String modelId = object.getString("modelid");
        final String type = object.getString("type");//"type":"0:：普通用户 1：vip用户"
        final String jPushUserName = object.getString("jgusername");
        final String jPushPassword = object.getString("jgpassword");
        final String header = object.getString("header");
        final String sex = object.getString("sex");
        SharedPreferenceUtils.setCustomAppSex(sex,sex);

//        final String jPushUserName = "15238691746";
//        final String jPushPassword = "123456";
        //极光im登录
//        isImLogin = false;
        JMessageClient.login(jPushUserName, jPushPassword, new BasicCallback() {
            @Override
            public void gotResult(int i, String s) {
                LogUtils.d("gotResult: 登录状态"+ i + "登录信息: " + s);

                if(i == 0){
                    SharedPreferenceUtils.setAppSign(true);
                    SharedPreferenceUtils.setUId(userId);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_MOBILE, mobile);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_HEAD, header);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_NAME, userName);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.PEOPLE_ID, peopleId);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_TYPE, type);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.DETAILSTATE, detailstate);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.J_PUSH_USER_NAME, jPushUserName);
                    SharedPreferenceUtils.setCustomAppProfile(ShareKeys.J_PUSH_PASSWORD, jPushPassword);

                    if(ContentKeys.USER_MODEL.equals(isModel)){
                        SharedPreferenceUtils.setAppFlag(ShareKeys.IS_MODEL, true);
                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.MODEL_ID, modelId);
                    }

                    //登录成功
//                    isImLogin = true;
                    if (detailstate.equals("1")){

                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.putExtra("sex",sex);
                        activity.startActivity(intent);
                        activity.finish();
                    }else{
                        activity.startActivity(new Intent(activity, MineInformationActivity.class));
                        activity.finish();
                    }

                }else {
                    ToastUtils.showText(activity, "登录失败，请重试");
                }

            }
        });





    }
}
