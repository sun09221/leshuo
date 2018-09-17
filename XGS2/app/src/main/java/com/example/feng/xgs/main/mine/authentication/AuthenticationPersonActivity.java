package com.example.feng.xgs.main.mine.authentication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.PermissionActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.AuthenticationActivity;
import com.example.feng.xgs.utils.DialogUtil;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/30 0030.
 * 个人认证
 */

public class AuthenticationPersonActivity extends PermissionActivity {


    @BindView(R.id.et_authentication_name)
    EditText mEtName;
    @BindView(R.id.et_authentication_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_authentication_number)
    EditText mEtNumber;
    @BindView(R.id.iv_authentication_card)
    ImageView mIvCard;
    @BindView(R.id.tv_toolbar_save)
    TextView tvToolbarSave;
    @BindView(R.id.include_toolbar)
    RelativeLayout includeToolbar;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_2)
    ImageView iv2;
    @BindView(R.id.iv_3)
    ImageView iv3;
    @BindView(R.id.iv_4)
    ImageView iv4;
    @BindView(R.id.tv_toolbar_title)
    AppCompatTextView tvToolbarTitle;
    @BindView(R.id.tv_identity)
    TextView tvIdentity;
    @BindView(R.id.rl_identity)
    RelativeLayout rlIdentity;
    @BindView(R.id.rl_1)
    LinearLayout rl1;
    @BindView(R.id.rl_2)
    LinearLayout rl2;
    @BindView(R.id.rl_3)
    LinearLayout rl3;
    @BindView(R.id.rl_4)
    LinearLayout rl4;
    private String mImagePath;
    private String mImageUrl;
    private int choice_type = 0;

    @OnClick(R.id.tv_toolbar_save)
    void onClickSave() {
        upload();
    }

    //选择图片
    @OnClick(R.id.iv_authentication_card)
    void onClickCardCheck() {
        choice_type = 0;
        requestCameraPermission();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_authentication_person;
    }

    private int type;

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_authentication_person));
        Intent intent = getIntent();
        tvToolbarTitle.setText(intent.getStringExtra("title"));
        type = intent.getIntExtra("type", 0);
        if (type == 1) {
            rlIdentity.setVisibility(View.VISIBLE);
            rl1.setVisibility(View.VISIBLE);
            rl2.setVisibility(View.VISIBLE);
            rl3.setVisibility(View.VISIBLE);
            rl4.setVisibility(View.VISIBLE);
        }
        identity_list.add("音乐老师");
        identity_list.add("唱作人");
        identity_list.add("主持人");
        loadData();
    }

    private void loadData() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_PERSON_INFO)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {


                        JSONObject object = JSON.parseObject(response);

                        mImagePath = object.getString("idcarpath");


                        String mobile = object.getString("phone");
                        String name = object.getString("name");
                        String idCard = object.getString("idcar");
                        mEtName.setText(name);
                        mEtMobile.setText(mobile);
                        mEtNumber.setText(idCard);
                        if (object.getString("diploma") != null) {
                            diploma_path = object.getString("diploma");
                            loadImg(diploma_path, iv1, R.mipmap.icon_add_big_gray);
                        }
                        if (object.getString("awardcard") != null) {
                            award_path = object.getString("awardcard");
                            loadImg(award_path, iv3, R.mipmap.icon_add_big_gray);
                        }
                        if (object.getString("workcard") != null) {
                            work_path = object.getString("workcard");
                            loadImg(work_path, iv4, R.mipmap.icon_add_big_gray);
                        }
                        if (object.getString("unitcard") != null) {
                            unit_path = object.getString("unitcard");
                            loadImg(unit_path, iv2, R.mipmap.icon_add_big_gray);
                        }
                        if(object.getString("type") != null){
                            if(object.getString("type").equals("1")){
                                tvIdentity.setText("唱作人");
                                rlIdentity.setEnabled(false);
                            }else if(object.getString("type").equals("2")){
                                tvIdentity.setText("音乐老师");
                                rlIdentity.setEnabled(false);
                            }else if(object.getString("type").equals("3")){
                                tvIdentity.setText("主持人");
                                rlIdentity.setEnabled(false);
                            }
                        }
                        loadImg(mImagePath, mIvCard, R.mipmap.icon_add_big_gray);

                    }
                })
                .build().post();
    }
    public boolean checkname(String name)
    {
        int n = 0;
        for(int i = 0; i < name.length(); i++) {
            n = (int)name.charAt(i);
            if(!(19968 <= n && n <40869)) {
                return false;
            }
        }
        return true;
    }
    public static boolean checkIdcard(String value) {
        int length = 0;
        if (value == null) {
            return false;
        } else {
            length = value.length();


            if (length != 15) {} else if (length != 18) {} else {
                return false;
            }
        }

        //不判断城市
// String[] areasArray = { "11", "12", "13", "14", "15", "21", "22", "23", "31",
//               "32", "33", "34", "35", "36", "37", "41", "42", "43", "44",
//               "45", "46", "50", "51", "52", "53", "54", "61", "62", "63",
//               "64", "65", "71", "81", "82", "91" };
//
//
// HashSet < String > areasSet = new HashSet < String > (Arrays.asList(areasArray));
// String valueStart2 = value.substring(0, 2);
//
//
// if (areasSet.contains(valueStart2)) {} else {
//   return false;
// }

        Pattern pattern = null;
        Matcher matcher = null;


        int year = 0;
        switch (length) {
            case 15:
                year = Integer.parseInt(value.substring(6, 8)) + 1900;


                if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
                    pattern = Pattern.compile("^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$"); // 测试出生日期的合法性
                } else {
                    pattern = Pattern.compile("^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$"); // 测试出生日期的合法性
                }
                matcher = pattern.matcher(value);
                if (matcher.find()) {
                    return true;
                } else {
                    return false;
                }
            case 18:
                year = Integer.parseInt(value.substring(6, 10));


                if (year % 4 == 0 || (year % 100 == 0 && year % 4 == 0)) {
                    pattern = Pattern.compile("^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$"); // 测试出生日期的合法性
                } else {
                    pattern = Pattern.compile("^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$"); // 测试出生日期的合法性
                }


                matcher = pattern.matcher(value);
                if (matcher.find()) {
                    int S = (Integer.parseInt(value.substring(0, 1)) + Integer.parseInt(value.substring(10, 11))) * 7 + (Integer.parseInt(value.substring(1, 2)) + Integer.parseInt(value.substring(11, 12))) * 9 + (Integer.parseInt(value.substring(2, 3)) + Integer.parseInt(value.substring(12, 13))) * 10 + (Integer.parseInt(value.substring(3, 4)) + Integer.parseInt(value.substring(13, 14))) * 5 + (Integer.parseInt(value.substring(4, 5)) + Integer.parseInt(value.substring(14, 15))) * 8 + (Integer.parseInt(value.substring(5, 6)) + Integer.parseInt(value.substring(15, 16))) * 4 + (Integer.parseInt(value.substring(6, 7)) + Integer.parseInt(value.substring(16, 17))) * 2 + Integer.parseInt(value.substring(7, 8)) * 1 + Integer.parseInt(value.substring(8, 9)) * 6 + Integer.parseInt(value.substring(9, 10)) * 3;
                    int Y = S % 11;
                    String M = "F";
                    String JYM = "10X98765432";
                    M = JYM.substring(Y, Y + 1); // 判断校验位
                    if (M.equals(value.substring(17, 18))) {
                        return true; // 检测ID的校验位
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            default:
                return false;
        }
    }
    private void upload() {
        final String name = mEtName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showText(this, getString(R.string.edit_real_name));
            return;
        }
        if (!checkname(name)) {
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
        if (!checkIdcard(idCardNumber)) {
            ToastUtils.showText(this, getString(R.string.edit_id_card_error_number));
            return;
        }
        if (TextUtils.isEmpty(mImagePath)) {
            ToastUtils.showText(this, "请上传身份证照片");
            return;
        }
        if (type == 1 && "点击选择".equals(tvIdentity.getText().toString())) {
            ToastUtils.showText(this, "请选择身份");
            return;
        }
        submit(name, mobile, idCardNumber);
//        if (TextUtils.isEmpty(mImagePath)) {
//            if (TextUtils.isEmpty(mImageUrl)) {
//                ToastUtils.showText(this, "请上传身份证照片");
//                return;
//            }
//
//            submit(name, mobile, idCardNumber, mImageUrl);
//        } else {
//            UploadImgUtil.create().loader(this).uploadImg(mImagePath, new UploadImgUtil.IUploadImgListener() {
//                @Override
//                public void onSuccess(String path) {
//                    submit(name, mobile, idCardNumber, path);
//                }
//            });
//        }

    }


    private void submit(String name, String mobile, String idCard) {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.NAME, name);
        map.put(ParameterKeys.MOBILE, mobile);
        map.put("idcar", idCard);
        map.put(ParameterKeys.ID_CARD_PATH, mImagePath);
        if (type == 1) {
            if (tvIdentity.getText().toString().equals("唱作人")) {
                map.put(ParameterKeys.TYPE, "1");
            } else if (tvIdentity.getText().toString().equals("音乐老师")) {
                map.put(ParameterKeys.TYPE, "2");
            } else if (tvIdentity.getText().toString().equals("主持人")) {
                map.put(ParameterKeys.TYPE, "3");
            }
        } else {
            map.put(ParameterKeys.TYPE, "0");
        }


        if (diploma_path != null) {
            map.put("diploma", diploma_path);
        }
        if (award_path != null) {
            map.put("awardcard", award_path);
        }
        if (work_path != null) {
            map.put("workcard", work_path);
        }
        if (unit_path != null) {
            map.put("unitcard", unit_path);
        }
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.AUTHENTICATION_PERSON_SAVE)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        NaturalLoader.stopLoading();

                        ToastUtils.showText(AuthenticationPersonActivity.this, "保存成功");
                          Intent intent = new Intent(AuthenticationPersonActivity.this, AuthenticationActivity.class);
                          startActivity(intent);

                        finish();
                    }
                })
                .build().post();
    }

    private void initPath(String local_path) {
        UploadImgUtil.create().loader(this).uploadImg(local_path, new UploadImgUtil.IUploadImgListener() {
            @Override
            public void onSuccess(String path) {

                switch (choice_type) {
                    case 0:
                        mImagePath = path;
                        loadImg(path, mIvCard);
                        break;
                    case 1:
                        diploma_path = path;
                        loadImg(path, iv1);
                        break;
                    case 2:
                        unit_path = path;
                        loadImg(path, iv2);
                        break;
                    case 3:
                        award_path = path;
                        loadImg(path, iv3);
                        break;
                    case 4:
                        work_path = path;
                        loadImg(path, iv4);
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    initPath(SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE));
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        if (photoUri.getScheme().equals("file")) {
                            initPath(photoUri.getPath());

                        } else {
                            initPath(UriUtils.getPhotoPath(this, photoUri));
                        }
                    }
                    break;
                default:
                    break;
            }
            loadImg(mImagePath, mIvCard);
            LogUtils.d("onActivityResult: " + mImagePath);
        }
    }

    private String diploma_path, unit_path, award_path, work_path;
    private List<String> identity_list = new ArrayList<>();
    private int identity_type;

    @OnClick({R.id.iv_1, R.id.iv_2, R.id.iv_3, R.id.iv_4, R.id.rl_identity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_1:
                choice_type = 1;
                requestCameraPermission();
                break;
            case R.id.iv_2:
                choice_type = 2;
                requestCameraPermission();
                break;
            case R.id.iv_3:
                choice_type = 3;
                requestCameraPermission();
                break;
            case R.id.iv_4:
                choice_type = 4;
                requestCameraPermission();
                break;
            case R.id.rl_identity:
                DialogUtil.getInstance().showDialog(this, identity_list, tvIdentity);
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
