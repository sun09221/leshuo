package com.example.feng.xgs.base.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.ui.dialog.CameraHandler;
import com.example.feng.xgs.utils.DensityUtils;


/**
 * Created by feng on 2017/12/18.
 */

public abstract class PermissionFragment extends BaseFragment implements View.OnClickListener {

    private AlertDialog mOpenPermissionDialog;


    public void requestCameraPermission() {
        if (isRequestPermission()) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CodeKeys.CAMERA_PERMISSION);
        } else {
            CameraHandler.create(this).beginCameraDialog();
            Log.d("TYPE", "requestLocationPermission: 打开照相机");
        }
    }

    private boolean isRequestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return false;
        } else {
            return true;
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
                    CameraHandler.create(this).beginCameraDialog();
                } else {
                    //打开权限管理
                    showDialog();
                }
                break;
            default:
                break;
        }
    }

    //打开权限管理
    private void openAPpSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
        startActivity(intent);
    }



    private void showDialog(){
        mOpenPermissionDialog = new AlertDialog.Builder(getContext()).create();
        mOpenPermissionDialog.show();
        final Window window = mOpenPermissionDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_hint);
            window.setGravity(Gravity.CENTER);


//            window.setBackgroundDrawable(new ShapeDrawable(R.drawable.white_solid_radius));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            int screenWidth = DensityUtils.getScreenWidth(getContext());
            params.width = screenWidth/7*5;
            params.gravity = Gravity.CENTER;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
            window.setBackgroundDrawableResource(R.drawable.white_solid_radius_shape);
            window.findViewById(R.id.tv_dialog_hint_cancel).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_hint_sure).setOnClickListener(this);
//            window.findViewById(R.id.camera_dialog_photo).setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_hint_cancel:
                mOpenPermissionDialog.cancel();
                break;
            case R.id.tv_dialog_hint_sure:
                openAPpSetting();
                mOpenPermissionDialog.cancel();
                break;
            default:
                break;
        }
    }
}
