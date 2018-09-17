package com.example.feng.xgs.ui.dialog;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.example.feng.xgs.R;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.utils.file.FileUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;

import java.io.File;

/**
 * Created by feng on 2017/11/30.
 * 弹出对话框
 */

public class CameraHandler implements View.OnClickListener {

    private AlertDialog mDialog;
    private Fragment mFragment;

    public CameraHandler(Fragment fragment) {
        this.mFragment = fragment;
        mDialog = new AlertDialog.Builder(fragment.getContext()).create();
    }

    public static CameraHandler create(Fragment fragment) {
        return new CameraHandler(fragment);
    }

    public void beginCameraDialog() {
        mDialog.show();
        final Window window = mDialog.getWindow();
        if (window != null) {
            window.setContentView(R.layout.dialog_camera);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.dialog_bottom_animation);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //设置属性
            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);

            window.findViewById(R.id.tv_dialog_camera_take).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_camera_photo).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_camera_cancel).setOnClickListener(this);
        }
    }

    private String getPhotoName() {
        return FileUtil.getFileNameByTime("FRIEND_CONTENT", "jpg");
    }

    /**
     * 打开相机
     */
    private void takePhoto() {
        final String currentPhotoName = getPhotoName();
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        final File tempFile = new File(FileUtil.CAMERA_PHOTO_DIR, currentPhotoName);
        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.CAMERA_FILE, tempFile.getPath());
        //适配7.0(7.0以后使用content Uri)
        Uri uriCamera = UriUtils.getUriForFile(mFragment.getActivity(), tempFile);
//        CameraImageBean.getInstance().setPath(uriCamera);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
        mFragment.startActivityForResult(intent, CodeKeys.CAMERA_OPEN);
    }

    /**
     * 打开相册
     */
    private void pickPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");//相片类型
        mFragment.startActivityForResult(intent, CodeKeys.PHOTO_OPEN);
//        final Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        mFragment.startActivityForResult
//                (Intent.createChooser(intent, "选择获取图片的方式"), CodeKeys.PHOTO_OPEN);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_dialog_camera_cancel) {
            mDialog.cancel();
        } else if (id == R.id.tv_dialog_camera_photo) {
            pickPhoto();
            mDialog.cancel();
        } else if (id == R.id.tv_dialog_camera_take) {
            takePhoto();
            mDialog.cancel();
        }
    }

}
