package com.example.feng.xgs.main.move.publish;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PhotoLayoutActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.DateUtils;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.example.feng.xgs.utils.UploadImgUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * Created by feng on 2018/5/11 0011.
 * 本色动态发布
 * 根据type不同，发布图片或者视频
 */

public class NaturalPublishActivity extends PhotoLayoutActivity implements CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.et_natural_publish)EditText mEtInfo;
    @BindView(R.id.photo_natural_publish)BGASortableNinePhotoLayout mPhotoLayout;
    @BindView(R.id.switch_natural_publish)Switch mSwitch;
    @BindView(R.id.tv_toolbar_save)TextView mTvPublish;
    private List<String> mImageList;
    private String mImgUrls;
    private String mContent;

    private int mUploadImageCount = 0;//上传图片数
    private boolean isUploading = false;//是否正在上传图片
    private String mType;
    private String circleid="";

    //发布
    @OnClick(R.id.tv_toolbar_save)void onClickPublish(){
        if(!isUploading){
            checkParameter();
        }else {
            ToastUtils.showText(this, "正在发布，请稍候...");
        }

    }


    @Override
    public Object setLayout() {
        return R.layout.activity_natural_publish;
    }



    @Override
    public void onBindView(Bundle savedInstanceState) {
        super.onBindView(savedInstanceState);
        mSwitch.setOnCheckedChangeListener(this);
        mTvPublish.setText(getString(R.string.publish));

        Intent intent = getIntent();
        mType = intent.getStringExtra(ContentKeys.ACTIVITY_TYPE);
        circleid= intent.getStringExtra("circleid");
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        setTitle(title);

    }

    /**
     * 1.先检验输入内容和圈子id不为空
     * 2.然后上传图片
     * 3.图片上传完毕，再发布动态
     * */
    private void checkParameter(){

        if(!ContentKeys.NATURAL_PUBLISH_IMAGE.equals(mType) && !ContentKeys.NATURAL_PUBLISH_VIDEO.equals(mType)){
            return;
        }

        mContent = mEtInfo.getText().toString();
        if(TextUtils.isEmpty(mContent)){
            ToastUtils.showText(this, "请输入这一刻的想法...");
            return;
        }
        if(mImageList==null){
            ToastUtils.showText(this, "请上传图片...");
            return;
        }
        upLoadImages();

    }

    /**
     * 上传图片
     * */
    private void upLoadImages(){
        if(mImageList != null && mImageList.size() > 0){
            isUploading = true;
            mUploadImageCount = 0;
            NaturalLoader.showLoading(this);
            final StringBuilder builder = new StringBuilder();
            for (int i = 0; i < mImageList.size(); i++) {
                LogUtils.d("onActivityResult: " + mImageList.get(i));

                UploadImgUtil.create().uploadImg(mImageList.get(i), new UploadImgUtil.IUploadImgListener() {
                    @Override
                    public void onSuccess(String path) {
                        if(mUploadImageCount == 0){
                            builder.append(path);
                        }else {
                            builder.append(",").append(path);
                        }

                        mUploadImageCount++;
                        //当所有图片上传完毕
                        if(mUploadImageCount == mImageList.size()){
                            mImgUrls = builder.toString();
                            publishImage();
                            isUploading = false;
                        }

                    }
                });
            }
        }
    }

    //type:动态类型：1：图文 2：视频
    private void publishImage(){
        String time = DateUtils.getNowDate();

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.CONTENT, mContent);
        map.put(ParameterKeys.IMAGE_PATH, mImgUrls);
        map.put(ParameterKeys.PUSH_TIME, time);
        map.put("circleid", circleid);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.TYPE, mType);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.DYNAMIC_PUBLISH_NATURAL)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        NaturalLoader.stopLoading();
                        ToastUtils.showText(NaturalPublishActivity.this, "发布成功");
                        finish();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        NaturalLoader.stopLoading();
                    }
                })
                .build().post();
    }



    /**
     * 跳转回传
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CodeKeys.FOR_RESULT_CODE){

        }else if (resultCode == RESULT_OK && requestCode == RC_CHOOSE_PHOTO) {
            mImageList = BGAPhotoPickerActivity.getSelectedPhotos(data);
            mPhotoLayout.setData(BGAPhotoPickerActivity.getSelectedPhotos(data));
            LogUtils.d("RC_CHOOSE_PHOTO 图片size: " + mImageList.size());
        } else if (requestCode == RC_PHOTO_PREVIEW) {
            mImageList = BGAPhotoPickerPreviewActivity.getSelectedPhotos(data);
            mPhotoLayout.setData(BGAPhotoPickerPreviewActivity.getSelectedPhotos(data));
            LogUtils.d("RC_PHOTO_PREVIEW 图片size: " + mImageList.size());
//             /storage/emulated/0/BGAPhotoPickerTakePhoto/Capture_2018-05-17_14-55_38-1896957965.jpg
        }

    }


    /**
     * switch的选中事件
     * */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Log.d(TAG, "onCheckedChanged: " + isChecked);
    }


    @Override
    public BGASortableNinePhotoLayout getPhotoLayout() {
        return mPhotoLayout;
    }



}
