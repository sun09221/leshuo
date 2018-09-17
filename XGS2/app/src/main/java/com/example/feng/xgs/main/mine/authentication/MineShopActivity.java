package com.example.feng.xgs.main.mine.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PermissionActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/30 0030.
 * 开通店铺
 */

public class MineShopActivity extends PermissionActivity {

    @BindView(R.id.et_mine_shop_name)
    EditText mEtName;
    @BindView(R.id.iv_mine_shop_logo)
    ImageView mIvLogo;
    @BindView(R.id.et_mine_shop_address)
    EditText mEtAddress;
    @BindView(R.id.et_mine_shop_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_mine_shop_info)
    EditText mEtInfo;

    private String mImagePath;
    private String mEnterpriseId;
    private String mStoreId;
    private String mImgUrl;

    //LOGO
    @OnClick(R.id.iv_mine_shop_logo)void onClickLogo(){
        requestCameraPermission();
    }

    //提交
    @OnClick(R.id.tv_mine_shop_sure)void onClickSubmit(){
        check();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_shop;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_shop));

        Intent intent = getIntent();
        mEnterpriseId = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        loadData();
    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ENTERPRISE_ID, mEnterpriseId);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.STORE_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {


                    @Override
                    public void onSuccess(String response) {

                        JSONObject object = JSON.parseObject(response);
                        //店铺id
                        mStoreId = object.getString("id");
                        String mobile = object.getString("phone");
                        String address = object.getString("address");
                        String longitude = object.getString("longitude");//维度
                        String latitude = object.getString("latitude");//经度
                        //logo
                        mImgUrl = object.getString("logopath");
                        String name = object.getString("shopname");//名称
                        String info = object.getString("introduction");//简介

                        mEtName.setText(name);
                        mEtMobile.setText(mobile);
                        mEtAddress.setText(address);
                        mEtInfo.setText(info);

                        loadImg(mImgUrl, mIvLogo, R.mipmap.icon_add_big_gray);

                    }
                })
                .build().post();

    }


    private void check(){
        final String name = mEtName.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showText(this, getString(R.string.edit_mine_shop_name));
            return;
        }



        final String address = mEtAddress.getText().toString();
        if(TextUtils.isEmpty(address)){
            ToastUtils.showText(this, getString(R.string.edit_mine_shop_address));
            return;
        }

        final String mobile = mEtMobile.getText().toString();
        if(TextUtils.isEmpty(mobile)){
            ToastUtils.showText(this, getString(R.string.edit_mobile));
            return;
        }


        final String info = mEtInfo.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, getString(R.string.edit_mine_shop_info));
            return;
        }

        if(TextUtils.isEmpty(mImagePath)){
            if(TextUtils.isEmpty(mImgUrl)){
                ToastUtils.showText(this, "请上传店铺LOGO");
                return;
            }

            submit(name, mImgUrl, address, mobile, info);

        }else {
            UploadImgUtil.create().loader(this).uploadImg(mImagePath, new UploadImgUtil.IUploadImgListener() {
                @Override
                public void onSuccess(String path) {
                    submit(name, path, address, mobile, info);
                }
            });
        }



    }

    private void submit(String name, String imgUrl, String address, String mobile, String info){
        Map<String, String> map = new HashMap<>();
        String url;
        if(TextUtils.isEmpty(mStoreId)){
            map.put(ParameterKeys.ENTERPRISE_ID, mEnterpriseId);
            url = UrlKeys.STORE_ADD;
        }else {
            map.put(ParameterKeys.ID_ONLY, mStoreId);
            url = UrlKeys.STORE_EDITOR;
        }

        map.put(ParameterKeys.SHOP_NAME, name);
        map.put(ParameterKeys.SHOP_LOGO, imgUrl);
        map.put(ParameterKeys.SHOP_ADDRESS, address);
        map.put(ParameterKeys.MOBILE, mobile);
        map.put(ParameterKeys.SHOP_INFO, info);
        map.put(ParameterKeys.SHOP_LATITUDEE, "34");
        map.put(ParameterKeys.SHOP_LONGITUDE, "119");

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(url)
                .raw(raw)
                .loader(MineShopActivity.this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(MineShopActivity.this, "修改成功");
                        finish();
                    }
                })
                .build().post();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    mImagePath = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        if(photoUri.getScheme().equals("file")){
                            mImagePath =photoUri.getPath();
                        }else {
                            mImagePath = UriUtils.getPhotoPath(this, photoUri);
                        }
                    }
                    break;
                default:
                    break;
            }
            loadImg(mImagePath, mIvLogo);
            LogUtils.d("onActivityResult: " + mImagePath);
        }
    }

}
