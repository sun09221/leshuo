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
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.AuthenticationActivity;
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
 * 企业认证
 */

public class AuthenticationEnterpriseActivity extends PermissionActivity {

    private static final int TYPE_ID_CARD = 11;
    private static final int TYPE_BUSINESS_LICENSE = 12;
    @BindView(R.id.et_authentication_name)
    EditText mEtName;
    @BindView(R.id.et_authentication_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_authentication_number)
    EditText mEtNumber;
    @BindView(R.id.iv_authentication_card)
    ImageView mIvCard;
    @BindView(R.id.et_authentication_job)
    EditText mEtJob;
    @BindView(R.id.et_authentication_company)
    EditText mEtCompany;
    @BindView(R.id.iv_authentication_business_license)
    ImageView mIvBusinessLicense;

    private String mImagePath, mImagePathCard;
    private int mType = TYPE_ID_CARD;
    private String mIdOnly;
    private String mImgIdCardUrl;
    private String mImgUrl;
    private boolean isImgBusinessFinish = false;
    private boolean isImgIdCardFinish = false;


    //营业执照
    @OnClick(R.id.iv_authentication_business_license)
    void onClickBusiness() {
        mType = TYPE_BUSINESS_LICENSE;
        requestCameraPermission();
    }

    //选择身份证图片
    @OnClick(R.id.iv_authentication_card)
    void onClickCardCheck() {
        mType = TYPE_ID_CARD;
        requestCameraPermission();
    }

    //保存
    @OnClick(R.id.tv_authentication_save)
    void onClickSave() {
        upload();
    }

    //开通店铺
    @OnClick(R.id.tv_authentication_shop)
    void onClickShop() {
        if(TextUtils.isEmpty(mIdOnly)){
            ToastUtils.showText(this, "请先完善企业信息");
            return;
        }
        Intent intent = new Intent(this, MineShopActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mIdOnly);
        startActivity(intent);
    }


    @Override
    public Object setLayout() {
        return R.layout.activity_mine_authentication_enterprise;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_authentication_enterprise));

        loadData();
    }

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_ENTERPRISE_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        //企业ID
                        mIdOnly = object.getString("id");
                        String position = object.getString("position");//职位
                        //营业执照
                        mImgUrl = object.getString("businesslicense");
                        //身份证照片
                        mImgIdCardUrl = object.getString("idcarimgpath");
                        String mobile = object.getString("phone");//电话
                        String peopleid = object.getString("peopleid");
                        String company = object.getString("companyname");//公司
                        String idCard = object.getString("idcar");//身份证号
                        String chargeName = object.getString("chargename");//姓名

                        mEtName.setText(chargeName);
                        mEtMobile.setText(mobile);
                        mEtNumber.setText(idCard);
                        mEtJob.setText(position);
                        mEtCompany.setText(company);

                        loadImg(mImgUrl, mIvBusinessLicense, R.mipmap.icon_add_big_gray);
                        loadImg(mImgIdCardUrl, mIvCard, R.mipmap.icon_add_big_gray);

                    }
                })
                .build().post();
    }

    private void upload() {
        final String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showText(this, getString(R.string.edit_real_name));
            return;
        }

        final String mobile = mEtMobile.getText().toString();
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showText(this, getString(R.string.edit_mobile));
            return;
        }

        final String idCardNumber = mEtNumber.getText().toString();
        if (TextUtils.isEmpty(idCardNumber)) {
            ToastUtils.showText(this, getString(R.string.edit_id_card_number));
            return;
        }

        final String job = mEtJob.getText().toString();
        if (TextUtils.isEmpty(job)) {
            ToastUtils.showText(this, getString(R.string.edit_job));
            return;
        }

        final String company = mEtCompany.getText().toString();
        if (TextUtils.isEmpty(company)) {
            ToastUtils.showText(this, getString(R.string.edit_company_name));
            return;
        }

        if (TextUtils.isEmpty(mImagePath) && TextUtils.isEmpty(mImgUrl)) {
            ToastUtils.showText(this, "请上传营业执照");
            return;
        }

        if (TextUtils.isEmpty(mImagePathCard) && TextUtils.isEmpty(mImgIdCardUrl)) {
            ToastUtils.showText(this, "请上传身份证照片");
            return;
        }

        NaturalLoader.showLoading(this);
        if(!TextUtils.isEmpty(mImagePath)){
            UploadImgUtil.create()
                    .uploadImg(mImagePath, new UploadImgUtil.IUploadImgListener() {
                        @Override
                        public void onSuccess(final String path) {
                            mImgUrl = path;
                            isImgBusinessFinish = true;
                            submit(name, mobile, idCardNumber, job, company);
                        }
                    });
        }else {
            isImgBusinessFinish = true;
            submit(name, mobile, idCardNumber, job, company);
        }

        if(!TextUtils.isEmpty(mImagePathCard)){
            UploadImgUtil.create()
                    .uploadImg(mImagePathCard, new UploadImgUtil.IUploadImgListener() {
                        @Override
                        public void onSuccess(final String path) {
                            isImgIdCardFinish = true;
                            mImgIdCardUrl = path;
                            submit(name, mobile, idCardNumber, job, company);
                        }
                    });
        }else {
            isImgIdCardFinish = true;
            submit(name, mobile, idCardNumber, job, company);
        }

    }

    private void submit(String name, String mobile, String idCardNumber,
                        String job, String company){
        if(isImgBusinessFinish && isImgIdCardFinish){
            Map<String, String> map = new HashMap<>();
            String url;
            if(TextUtils.isEmpty(mIdOnly)){
                map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
                url = UrlKeys.AUTHENTICATION_ENTERPRISE_ADD;
            }else {
                map.put(ParameterKeys.ID_ONLY, mIdOnly);
                url = UrlKeys.AUTHENTICATION_ENTERPRISE_EDITOR;
            }

            map.put(ParameterKeys.ENTERPRISE_NAME, name);
            map.put(ParameterKeys.ENTERPRISE_MOBILE, mobile);
            map.put(ParameterKeys.ENTERPRISE_IDCARD, idCardNumber);
            map.put(ParameterKeys.ENTERPRISE_JOB, job);
            map.put(ParameterKeys.ENTERPRISE_COMPANY, company);
            map.put(ParameterKeys.ENTERPRISE_ID_CARD_PATH, mImgIdCardUrl);
            map.put(ParameterKeys.ENTERPRISE_BUSINESS, mImgUrl);

            String raw = JSON.toJSONString(map);
            RestClient.builder()
                    .url(url)
                    .raw(raw)
                    .toast()
                    .success(new ISuccess() {
                        @Override
                        public void onSuccess(String response) {
                            ToastUtils.showText(AuthenticationEnterpriseActivity.this, "修改成功");
                            Intent intent = new Intent(AuthenticationEnterpriseActivity.this, AuthenticationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .build().post();
            NaturalLoader.stopLoading();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String imagePath = null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    imagePath = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();

                        if(photoUri.getScheme().equals("file")){
                            imagePath =photoUri.getPath();
                        }else {
                            imagePath = UriUtils.getPhotoPath(this, photoUri);
                        }
                    }
                    break;
                default:
                    break;
            }


            if (mType == TYPE_ID_CARD) {
                mImagePathCard = imagePath;
                loadImg(mImagePathCard, mIvCard);
            } else if (mType == TYPE_BUSINESS_LICENSE) {
                mImagePath = imagePath;
                loadImg(mImagePath, mIvBusinessLicense);
            }

            LogUtils.d("onActivityResult: " + imagePath);
        }
    }


}
