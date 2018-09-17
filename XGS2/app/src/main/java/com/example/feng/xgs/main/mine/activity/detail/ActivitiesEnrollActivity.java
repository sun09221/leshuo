package com.example.feng.xgs.main.mine.activity.detail;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/7 0007.
 * 活动报名
 */

public class ActivitiesEnrollActivity extends ToolbarActivity {


    @BindView(R.id.et_activities_enroll_name)
    EditText mEtName;
    @BindView(R.id.et_activities_enroll_sex)
    TextView mEtSex;
    @BindView(R.id.et_activities_enroll_mobile)
    EditText mEtMobile;
    @BindView(R.id.tv_activities_enroll_price)
    TextView mEtPrice;
    private String mActivityId;

    //报名
    @OnClick(R.id.tv_activities_enroll_sure)void onClickSure(){
        pay();
    }

    @OnClick(R.id.tv_activities_enroll_pay)
    void onClickPay() {
        pay();
    }

    @Override
    public Object setLayout() {
        return R.layout.activty_find_activities_enroll;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_activity_enroll));

        Intent intent = getIntent();
        mActivityId = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        LogUtils.d(mActivityId);
        mEtSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChoice();
            }
        });

    }
    private void dialogChoice() {
        final String items[] = {"男", "女"};
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("性别")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEtSex.setText(items[which]);
                    }
                }).create();
        dialog.show();
    }


    public boolean vd(String str){

        char[] chars=str.toCharArray();
        boolean isGB2312=false;
        for(int i=0;i<chars.length;i++){
            byte[] bytes=(""+chars[i]).getBytes();
            if(bytes.length==2){
                int[] ints=new int[2];
                ints[0]=bytes[0]& 0xff;
                ints[1]=bytes[1]& 0xff;
                if(ints[0]>=0x81 && ints[0]<=0xFE && ints[1]>=0x40 && ints[1]<=0xFE){
                    isGB2312=true;
                    break;
                }
            }
        }
        return isGB2312;

    }
    public static boolean isNumeric(String str){

        for (int i = str.length();--i>=0;){

            if (!Character.isDigit(str.charAt(i))){

                return false;

            }

        }

        return true;

    }
    private void pay(){
        String name = mEtName.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_enroll_name));
            return;
        } if (!vd(name)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_error_name));
            return;
        }

        String sex = mEtSex.getText().toString();
        if(TextUtils.isEmpty(sex)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_enroll_sex));
            return;
        }

        String sexType = null;
        if(sex.equals("男")){
            sexType = "1";
        }else if(sex.equals("女")){
            sexType = "0";
        }else {
            ToastUtils.showText(this, "性别输入有误");
            return;
        }

        String mobile = mEtMobile.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_enroll_mobile));
            return;
        }
        if(!isNumeric(mobile)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_error_mobile));
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ACTIVITY_ID, mActivityId);
        map.put(ParameterKeys.PEOPLE_NAME, name);
        map.put(ParameterKeys.USER_SEX, sexType);
        map.put(ParameterKeys.PEOPLE_PHONE, mobile);

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.FIND_ACTIVITY_ENROLL)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(ActivitiesEnrollActivity.this, "报名成功");
                        finish();
                    }
                })
                .build().post();
    }
}
