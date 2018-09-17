package com.example.feng.xgs.main.mine.qrcode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.ShareUtils;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.QrCodeUtil;
import com.example.feng.xgs.utils.file.FileUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/9 0009.
 * 专属二维码
 */

public class QrCodeActivity extends ToolbarActivity{

    @BindView(R.id.iv_qr_code)ImageView mIvQrCode;
    @BindView(R.id.iv_natural_item_head_detail)ImageView head_detail;
    @BindView(R.id.tv_natural_item_name)TextView name;
    @BindView(R.id.tv_natural_item_type)TextView address;
    @BindView(R.id.tv_natural_item_sex)ImageView head_sex;

//  private String mImagePath;
    private String mImageUrl;
    private String nickname;
    private String sex;
    private String header;
    private String hometown;

    @OnClick(R.id.iv_toolbar_share)void onClickShare(){
        if(TextUtils.isEmpty(mImageUrl)){
            ToastUtils.showText(this, "请等待二维码生成完毕");
            return;
        }
        ShareUtils.create(this)
                .setText("")
                .setImgUrl(mImageUrl)
                .showDialog();
    }

    @Override
    public Object setLayout() {
        return R.layout.activty_mine_qr_code;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_qr_code));
        loadDatainformal();
        loadData();

    }

    private void initQrCode(String content){
//        String content = "https://www.baidu.com/";

        Bitmap bitmap = QrCodeUtil.createBitmap(this, content, 220);

        if(bitmap != null){
            File file = FileUtil.saveBitmap(bitmap, "qrCode", 100);
            if(file != null){
                String mImagePath = file.getPath();
                LogUtils.d("二维码地址: " + mImagePath);
            }

            mIvQrCode.setImageBitmap(bitmap);
        }
    }

    private void loadData(){
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.MINE_QRCODE)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        mImageUrl = object.getString("url");
                        loadImg(mImageUrl, mIvQrCode);
//                        initQrCode(url);
                    }
                })
                .build().post();
    }
    private void loadDatainformal() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.USER_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSONObject.parseObject(response);
                        nickname = object.getString("nickname");
                        sex = object.getString("sex");
                         header = object.getString("header");
                         hometown = object.getString("hometown");
                        name.setText(nickname);
                        address.setText(hometown);
                        if (sex.equals("男")){
                            head_sex.setVisibility(View.VISIBLE);
                            head_sex.setBackgroundResource(R.mipmap.ercode_man);
                        }else if (sex.equals("女")){
                            head_sex.setBackgroundResource(R.mipmap.ercode_women);
                            head_sex.setVisibility(View.VISIBLE);
                        }else{
                            head_sex.setVisibility(View.GONE);
                        }
                        loadImg(header, head_detail);
                    }
                })
                .build().post();
    }
}
