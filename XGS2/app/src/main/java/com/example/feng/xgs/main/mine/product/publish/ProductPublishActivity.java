package com.example.feng.xgs.main.mine.product.publish;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PermissionActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.ui.dialog.IDialogStringListener;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/6/19 0019.
 */

public class ProductPublishActivity extends PermissionActivity {
    @BindView(R.id.tv_mine_product_publish_type)
    TextView mTvType;
    @BindView(R.id.et_mine_product_publish_name)
    EditText mEtName;
    @BindView(R.id.et_mine_product_publish_price)
    EditText mEtPrice;
    @BindView(R.id.iv_product_publish)
    ImageView mIvDetail;
    @BindView(R.id.et_mine_product_publish_detail)
    EditText mEtDetail;
    private String mImagePath;

    @OnClick(R.id.rl_mine_product_publish_type)void onClickType(){
        ProductPublishDialog.create(this).beginShow(mResponse, new IDialogStringListener() {
            @Override
            public void onSure(String type) {
                mTvType.setText(type);
            }
        });
    }

    @OnClick(R.id.iv_product_publish)void onClickImage(){
        requestCameraPermission();
    }

    @OnClick(R.id.tv_mine_product_publish_save)void onClickSave(){
        save();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_product_publish;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_product_publish));

        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        mUrl = UrlKeys.SHOP_HOT_TAB;
        map.put(ParameterKeys.TYPE,  "1");
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();

        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mResponse=response;

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");


                    }
                })
                .build().post();

    }
    private String mResponse="";
    public final static boolean isInteger(Object object) {
        try {
            Pattern p = Pattern.compile("-?[0-9]+");
            Matcher m = p.matcher(object.toString());
            return m.matches();
        } catch (Exception e) {
            return false;
        }
    }
    private void save(){
        String type = mTvType.getText().toString();
        if(TextUtils.isEmpty(type) || type.equals("类别")){
            ToastUtils.showText(this, "请选择产品类型");
            return;
        }

        final String name = mEtName.getText().toString();
        if(TextUtils.isEmpty(name)){
            ToastUtils.showText(this, getString(R.string.edit_product_name));
            return;
        }



        final String price = mEtPrice.getText().toString();
        Object o = price;
        if(TextUtils.isEmpty(price)){
            ToastUtils.showText(this, getString(R.string.edit_product_price));
            return;
        }
        if(!isInteger(o)){
            ToastUtils.showText(this, getString(R.string.edit_product_error_price));
            return;
        }

        if(TextUtils.isEmpty(mImagePath)){
            ToastUtils.showText(this, getString(R.string.upload_product_image));
            return;
        }

        final String info = mEtDetail.getText().toString();
        if(TextUtils.isEmpty(info)){
            ToastUtils.showText(this, getString(R.string.edit_product_info));
            return;
        }

        UploadImgUtil.create()
                .loader(this)
                .uploadImg(mImagePath, new UploadImgUtil.IUploadImgListener() {
            @Override
            public void onSuccess(String path) {
                Map<String, String> map = new HashMap<>();
                map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
                map.put(ParameterKeys.PRODUCT_NAME, name);
                map.put(ParameterKeys.PRODUCT_PRICE, price);
                map.put(ParameterKeys.PRODUCT_PATH, path);
                map.put(ParameterKeys.PRODUCT_INFO, info);
                map.put(ParameterKeys.PRODUCT_TYPE, mTvType.getText().toString());
                String raw = JSON.toJSONString(map);

                RestClient.builder()
                        .url(UrlKeys.MINE_PRODUCT_PUBLISH)
                        .raw(raw)
                        .toast(ProductPublishActivity.this)
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                ToastUtils.showText(ProductPublishActivity.this, "发布成功");
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
                    LogUtils.d("onActivityResult: " + mImagePath);
                    loadImg(mImagePath, mIvDetail);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        if(photoUri.getScheme().equals("file")){
                            mImagePath =photoUri.getPath();
                        }else {
                            mImagePath = UriUtils.getPhotoPath(this, photoUri);
                        }
                        LogUtils.d("onActivityResult: " + mImagePath);
                        loadImg(mImagePath, mIvDetail);
                    }

                    break;
                default:
                    break;
            }
        }
    }
}
