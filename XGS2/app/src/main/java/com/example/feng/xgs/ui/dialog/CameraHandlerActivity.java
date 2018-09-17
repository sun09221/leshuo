package com.example.feng.xgs.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
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
 */

public class CameraHandlerActivity implements View.OnClickListener {

    private AlertDialog mDialog;
    private Activity mActivity;

    public CameraHandlerActivity(Activity activity) {
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();
    }


    public static CameraHandlerActivity create(Activity activity) {
        return new CameraHandlerActivity(activity);
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
        Uri uriCamera = UriUtils.getUriForFile(mActivity, tempFile);
//        CameraImageBean.getInstance().setPath(uriCamera);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriCamera);
        mActivity.startActivityForResult(intent, CodeKeys.CAMERA_OPEN);
    }

    /**
     * 打开相册
     */
    private void pickPhoto() {
//        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        mFragment.startActivityForResult(intent, CodeKeys.PHOTO_OPEN);
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
        mActivity.startActivityForResult(intent, CodeKeys.PHOTO_OPEN);
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");//相片类型
//        mActivity.startActivityForResult(intent, CodeKeys.PHOTO_OPEN);

//        final Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        mActivity.startActivityForResult
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
