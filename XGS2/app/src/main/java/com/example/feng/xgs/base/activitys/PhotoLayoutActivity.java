package com.example.feng.xgs.base.activitys;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.example.feng.xgs.base.PermissionDialog;
import com.example.feng.xgs.config.key.CodeKeys;

import java.io.File;
import java.util.ArrayList;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by feng on 2018/2/25.
 * 图片选择
 */

public abstract class PhotoLayoutActivity extends ToolbarActivity
        implements BGASortableNinePhotoLayout.Delegate{

    public static final int RC_CHOOSE_PHOTO = 1;
    public static final int RC_PHOTO_PREVIEW = 2;
    private static final int PRC_PHOTO_PICKER = 11;
    private BGASortableNinePhotoLayout mPhotoLayout;

    public abstract BGASortableNinePhotoLayout getPhotoLayout();

    @Override
    public void onBindView(Bundle savedInstanceState) {
        mPhotoLayout = getPhotoLayout();

//        mPhotoLayout.setMaxItemCount(9);
        mPhotoLayout.setEditable(true);
        mPhotoLayout.setPlusEnable(true);
        mPhotoLayout.setSortable(true);
        mPhotoLayout.setDelegate(this);
    }

    //权限请求
    public void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            choicePhotoWrapper();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, CodeKeys.CAMERA_PERMISSION);

        }
    }



    //打开图片选择页面
    private void choicePhotoWrapper() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

        Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
                .cameraFileDir(takePhotoDir) // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话则不开启图库里的拍照功能
                .maxChooseCount(mPhotoLayout.getMaxItemCount()) // 图片选择张数的最大值
                .selectedPhotos(null) // 当前已选中的图片路径集合
                .pauseOnScroll(false) // 滚动列表时是否暂停加载图片
                .build();
        startActivityForResult(photoPickerIntent, RC_CHOOSE_PHOTO);
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
                    choicePhotoWrapper();
                } else {
                    //打开权限管理
                    PermissionDialog.create(this).beginPermissionDialog();
                }
                break;
            default:
                break;
        }
    }



    // ------------------- 以下为photoLayout的事件回调
    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout bgaSortableNinePhotoLayout, View view, int i, ArrayList<String> arrayList) {
        requestCameraPermission();
    }

    //删除事件
    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout bgaSortableNinePhotoLayout, View view, int position, String s, ArrayList<String> arrayList) {
        mPhotoLayout.removeItem(position);
//        LogUtils.d(TAG, "onClickDeleteNinePhotoItem: " + mImageList.size());
//        mImageList.remove(icon_find_activity_address);

    }

    //图片预览
    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout bgaSortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        Intent photoPickerPreviewIntent = new BGAPhotoPickerPreviewActivity.IntentBuilder(this)
                .previewPhotos(models) // 当前预览的图片路径集合
                .selectedPhotos(models) // 当前已选中的图片路径集合
                .maxChooseCount(mPhotoLayout.getMaxItemCount()) // 图片选择张数的最大值
                .currentPosition(position) // 当前预览图片的索引
                .isFromTakePhoto(false) // 是否是拍完照后跳转过来
                .build();
        startActivityForResult(photoPickerPreviewIntent, RC_PHOTO_PREVIEW);
    }

    @Override
    public void onNinePhotoItemExchanged(BGASortableNinePhotoLayout bgaSortableNinePhotoLayout, int i, int i1, ArrayList<String> arrayList) {

    }

}
