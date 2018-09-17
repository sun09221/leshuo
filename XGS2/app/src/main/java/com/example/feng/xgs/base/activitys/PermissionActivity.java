package com.example.feng.xgs.base.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.feng.xgs.base.PermissionDialog;
import com.example.feng.xgs.ui.dialog.CameraHandlerActivity;
import com.example.feng.xgs.config.key.CodeKeys;


/**
 * Created by feng on 2017/12/30.
 */

public abstract class PermissionActivity extends ToolbarActivity{

    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

            CameraHandlerActivity.create(this).beginCameraDialog();
            Log.d("TYPE", "requestLocationPermission: 打开照相机");

        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CodeKeys.CAMERA_PERMISSION);

        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CodeKeys.CAMERA_PERMISSION:
                boolean isSuccess = true;
                for (int i = 0; i < grantResults.length; i++) {
                    Log.d("permission", "onRequestPermissionsResult: 权限请求成功，打开照相机"
                            + grantResults[i]);
                    if(grantResults[i] != 0){
                        isSuccess = false;
                        break;
                    }
                }
                if (isSuccess) {
                    //打开照相机
                    CameraHandlerActivity.create(this).beginCameraDialog();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
