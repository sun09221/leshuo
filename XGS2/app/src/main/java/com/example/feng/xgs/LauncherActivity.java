package com.example.feng.xgs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.example.feng.xgs.base.PermissionDialog;
import com.example.feng.xgs.base.activitys.BaseActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.main.sign.LoginActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.handler.HandlerUtils;
import com.example.feng.xgs.utils.handler.IHandlerMessageListener;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/3/15 0015.
 * 启动页
 */

public class LauncherActivity extends BaseActivity {


    private HandlerUtils mHandler;
    private int mIndex = 3;
    private static final int WHAT_SMS = 101;
    /**
     * 是否点击了跳过按钮
     * 如果点击了，禁止自动跳转
     */
    private boolean isActionDownNext = false;
    @BindView(R.id.tv_launcher_timer)
    TextView mTvLauncher;

    @OnClick(R.id.tv_launcher_timer)
    void onClickNext() {
        isActionDownNext = true;
        requestLocationPermission();
//        requestLocationPermission();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        mTvLauncher.setText("3s\n跳过");
        countDown();

        Random random = new Random();
        random.nextInt();
        random.nextInt(12);
    }


    /**
     * 倒计时
     */
    private void countDown() {
        mHandler = new HandlerUtils(this, new IHandlerMessageListener() {
            @Override
            public void onHandlerMessage(Message msg) {
                //如果手动点击跳转，这里不再处理跳转
                if (isActionDownNext) {
                    return;
                }
                if (msg.what == WHAT_SMS) {
                    mIndex--;
                    if (mIndex > 0) {
                        mHandler.sendEmptyMessageDelayed(WHAT_SMS, 1000);
                        mTvLauncher.setText(String.format("%ss\n跳过", mIndex));
//                        mTvCode.setBackgroundResource(R.drawable.gray_radius);
                    } else {
                        requestLocationPermission();
//                        requestLocationPermission();
                    }
                }

            }
        });
        mHandler.sendEmptyMessage(WHAT_SMS);
    }


    private void startNext() {

        boolean isSignTag = SharedPreferenceUtils.getAppSign();
        if (isSignTag) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startNext();
            LogUtils.d("已有定位权限");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    CodeKeys.LOCATION_PERMISSION);
            LogUtils.d("开始请求定位权限");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CodeKeys.LOCATION_PERMISSION:
                boolean isSuccess = true;
                for (int i = 0; i < grantResults.length; i++) {
                    LogUtils.d("permission", "onRequestPermissionsResult: 权限请求成功"
                            + grantResults[i]);
                    if(grantResults[i] != 0){
                        isSuccess = false;
                        break;
                    }
                }
                if (isSuccess) {
                    //打开照相机
                    startNext();
                } else {
                    //打开权限管理
                    PermissionDialog.create(this).beginPermissionDialog();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mHandler != null){
            mHandler.removeCallbacksAndMessages(null);
        }
    }
}
