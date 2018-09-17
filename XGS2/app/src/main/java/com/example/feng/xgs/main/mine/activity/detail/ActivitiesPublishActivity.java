package com.example.feng.xgs.main.mine.activity.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PermissionActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.ui.dialog.IDialogStringListener;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.UriUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/7 0007.
 * 发布活动
 */

public class ActivitiesPublishActivity extends PermissionActivity {

    @BindView(R.id.et_activities_publish_price)
    EditText mEtPrice;
    @BindView(R.id.et_activities_publish_name)
    EditText mEtName;
    @BindView(R.id.et_activities_publish_addres)
    EditText mEtAddress;
    @BindView(R.id.et_activities_publish_time)
    EditText mEtTime;
    @BindView(R.id.et_activities_publish_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_activities_publish_info)
    EditText mEtInfo;
    @BindView(R.id.et_activities_publish_detail)
    EditText mEtDetail;
    @BindView(R.id.iv_activities_publish_detail)
    ImageView mIvDetail;
    @BindView(R.id.tv_activities_publish_model)
    TextView mTvModel;
    private String mImgUrl = null;
    //是否显示报名费
    private boolean isShowPrice;
    private String mType;
    private String mImagePath;

    //保存
    @OnClick(R.id.tv_toolbar_save)void onClickSave(){
        String type= SharedPreferenceUtils.getCustomAppProfile(ShareKeys.USER_TYPE);
        if (type.equals("1")){
            save();
        }else{
            ToastUtils.showText(this, getString(R.string.edit_find_activities_vip));
        }

    }

    //上传活动图片
    @OnClick(R.id.iv_activities_publish_detail)void onClickUploadImage(){
        requestCameraPermission();
    }

    //选择活动类型
    @OnClick(R.id.ly_activities_publish_model)void onClickModel(){
        ActivitiesModelDialog.create(this).beginShow(new IDialogStringListener() {
            @Override
            public void onSure(String type) {
                mType = type;
                if(ContentKeys.FIND_ACTIVITY_ENROLL.equals(type)){
                    mTvModel.setText("免费");
                    mEtPrice.setVisibility(View.GONE);
                    isShowPrice = false;
                }else {
                    mTvModel.setText("付费");
                    mEtPrice.setVisibility(View.VISIBLE);
                    isShowPrice = true;
                }

            }
        });
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_activities_publish;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.find_activity_publish));
    }

    //保存
    private void save(){

        if(TextUtils.isEmpty(mType)){
            ToastUtils.showText(this, "请选择活动类型");
            return;
        }

        final Map<String, String> map = new HashMap<>();
        if(isShowPrice){
            String price = mEtPrice.getText().toString();
            if(TextUtils.isEmpty(price)){
                ToastUtils.showText(this, getString(R.string.edit_find_activities_price));
                return;
            }
            map.put(ParameterKeys.MONEY, price);
        }

        final String name = mEtName.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_name));
            return;
        }

        final String address = mEtAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_address));
            return;
        }

        final String time = mEtTime.getText().toString();
        if(TextUtils.isEmpty(time)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_time));
            return;
        }

        final String mobile = mEtMobile.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_mobile));
            return;
        }

        final String info = mEtInfo.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_info));
            return;
        }

        final String detail = mEtDetail.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, getString(R.string.edit_find_activities_detail));
            return;
        }

        if(TextUtils.isEmpty(mImagePath)){
            ToastUtils.showText(this, getString(R.string.upload_find_activities_image));
            return;
        }

        UploadImgUtil.create().loader(this).uploadImg(mImagePath, new UploadImgUtil.IUploadImgListener() {
            @Override
            public void onSuccess(String path) {

                map.put(ParameterKeys.ACTIVITY_NAME, name);
                map.put(ParameterKeys.TYPE, mType);
                map.put(ParameterKeys.ADDRESS, address);
                map.put(ParameterKeys.ACTIVITY_TIME, time);
                map.put(ParameterKeys.INFORMATION, info);
                map.put(ParameterKeys.MOBILE, mobile);
                map.put(ParameterKeys.IMAGES, path);
                map.put(ParameterKeys.ACTIVITY_DETAIL, detail);

                String raw = JSON.toJSONString(map);
                RestClient.builder()
                        .url(UrlKeys.FIND_ACTIVITY_PUBLISH)
                        .raw(raw)
                        .loader(ActivitiesPublishActivity.this)
                        .toast()
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                ToastUtils.showText(ActivitiesPublishActivity.this, "发布成功");
                                setResult(CodeKeys.FOR_RESULT_CODE);
                                finish();
                            }
                        })
                        .build().post();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    mImagePath = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE);
                    loadImg(mImagePath, mIvDetail);
//                    uploadImg(cameraPath);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if(data != null){
                        Uri photoUri = data.getData();
                        String mImagePath ="";
                        if(photoUri.getScheme().equals("file")){
                            mImagePath =photoUri.getPath();
                        }else {
                            mImagePath = UriUtils.getPhotoPath(this, photoUri);
                        }
                        loadImg(mImagePath, mIvDetail);
//                        uploadImg(photoPath);
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void uploadImg(String path) {

    }
}
