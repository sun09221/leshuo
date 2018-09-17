package com.example.feng.xgs.main.find.broadcast;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/7 0007.
 * 发布广播页面
 */

public class BroadcastSendActivity extends ToolbarActivity {


    @BindView(R.id.tv_toolbar_save)
    TextView mTvRight;
    @BindView(R.id.et_broadcast_send_time)
    TextView mEtTime;
    @BindView(R.id.et_broadcast_send_address)
    EditText mEtAddress;
    @BindView(R.id.et_broadcast_send_info)
    EditText mEtInfo;
    private DateFormat format;
    private Calendar calendar;

    @OnClick(R.id.tv_toolbar_save)void onClickPublish(){
        publish();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_find_broadcast_send;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_broadcast_send));
        mTvRight.setText(getString(R.string.publish));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        format = formatter;//获取日期格式器对象
        calendar = Calendar.getInstance(Locale.CHINA);//获取日期格式器对象
        updateTimeShow(); //将页面TextView的显示更新为最新时间
        mEtTime.setOnClickListener(new View.OnClickListener() {//设置按钮的点击事件监听器
            @Override
            public void onClick(View v) {
                //生成一个DatePickerDialog对象，并显示。显示的DatePickerDialog控件可以选择年月日，并设置
                DatePickerDialog datePickerDialog = new DatePickerDialog(BroadcastSendActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        //修改日历控件的年，月，日
                        //这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateTimeShow();
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                updateTimeShow(); //将页面TextView的显示更新为最新时间
            }
        });
    }
    private void updateTimeShow(){ //将页面TextView的显示更新为最新时间
        mEtTime.setText(format.format(calendar.getTime()));
    }

    public static boolean containsEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i+1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c || hs == 0x2b1b || hs == 0x2b50|| hs == 0x231a ) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() -1) {
                    char ls = source.charAt(i+1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return  isEmoji;
    }
    private void publish(){
        String time = mEtTime.getText().toString();
        if(TextUtils.isEmpty(time)){
            ToastUtils.showText(this, getString(R.string.edit_find_broadcast_time));
            return;
        }

        String address = mEtAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            ToastUtils.showText(this, getString(R.string.edit_find_broadcast_address));
            return;
        }

        String info = mEtInfo.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, getString(R.string.edit_find_broadcast_info));
            return;
        }
        if(containsEmoji(info)){
            ToastUtils.showText(this, getString(R.string.edit_find_broadcast_error));
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.BROADCAST_TIME, time);
        map.put(ParameterKeys.BROADCAST_ADDRESS, address);
        map.put(ParameterKeys.CONTENT, info);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_BROADCAST_PUBLISH)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(BroadcastSendActivity.this, "发布成功");
                        setResult(CodeKeys.FOR_RESULT_CODE);
                        finish();
                    }
                })
                .build().post();
    }
}
