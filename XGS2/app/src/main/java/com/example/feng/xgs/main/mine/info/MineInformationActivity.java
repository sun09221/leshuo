package com.example.feng.xgs.main.mine.info;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.feng.xgs.MainActivity;
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
import com.example.feng.xgs.main.mine.info.address.AddressActivity;
import com.example.feng.xgs.ui.dialog.IDialogStringListener;
import com.example.feng.xgs.utils.UploadImgUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.file.UriUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.UserInfo;
import cn.jpush.im.api.BasicCallback;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by feng on 2018/5/2 0002.
 * 个人信息
 */
public class MineInformationActivity extends PermissionActivity {


    @BindView(R.id.iv_info_head)
    CircleImageView mIvHead;
    @BindView(R.id.et_info_nickname)
    EditText mEtName;
    @BindView(R.id.iv_toolbar_back)
    ImageView iv_toolbar_back;
    @BindView(R.id.tv_info_sex)
    TextView mTvSex;
    @BindView(R.id.tv_info_birthday)
    TextView mTvBirthday;
    @BindView(R.id.et_info_mobile)
    EditText mEtMobile;
    @BindView(R.id.et_info_email)
    EditText mEtEmail;
//    @BindView(R.id.tv_info_address)
//    TextView mTvAddress;

    //    private String mAddress;
    private String mNickName;
    private String mBirthday;
    private String mMobile;
    private String mEmail;
    private String mImgUrl;
    private TimePickerView pvTime;
    private String mSexType;


    //保存编辑信息
    @OnClick(R.id.tv_toolbar_save)
    void onClickSave() {
        submitInfo();
    }

    //性别
    @OnClick(R.id.rl_info_sex)
    void onClickSex() {
        if (mTvSex.getText().equals("男")){
            return;
        }else  if (mTvSex.getText().equals("女")){
            return;
        }else{
        SexDialog.create(this)
                .beginShow(new IDialogStringListener() {
                    @Override
                    public void onSure(String type) {
                            mTvSex.setText(type);
                            mSexType = getSexType(type);
                            LogUtils.d("type: " + mSexType + ", " + type);
                    }
                });
        }
    }

    //出生年月
    @OnClick(R.id.rl_info_birthday)
    void onClickBirthday() {
        showTimePicker();
    }

    //收货地址
    @OnClick(R.id.rl_info_address)
    void onClickAddress() {
        Intent intent = new Intent(this, AddressActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.address_manager));
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.ADDRESS_MANAGER);
        startActivity(intent);
    }

    //修改头像
    @OnClick(R.id.iv_info_head)
    void onClickHead() {
        requestCameraPermission();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_information;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.information));
        loadData();
        iv_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MineInformationActivity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    private void loadData() {
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
                        JSONObject object = JSON.parseObject(response);
//                        mAddress = object.getString("constantaddress");
                        mNickName = object.getString("nickname");

                        mBirthday = object.getString("birthday");
                        if (!mBirthday.isEmpty()) {
                            mTvBirthday.setEnabled(false);
                        }
                        mMobile = object.getString("phone");
                        String hometown = object.getString("hometown");
                        mEmail = object.getString("email");
                        String industry = object.getString("industry");
                        String jobArea = object.getString("jobarea");
                        String message = object.getString("message");
                        String starsign = object.getString("starsign");
                        mImgUrl = object.getString("header");
                        String sex = object.getString("sex");
                        mSexType = getSexType(sex);

                        loadImg(mImgUrl, mIvHead, R.mipmap.icon_head_default);
                        mEtName.setText(mNickName);
                        mTvSex.setText(sex);
                        mTvBirthday.setText(mBirthday);
                        mEtMobile.setText(mMobile);
                        mEtEmail.setText(mEmail);


                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_HEAD, mImgUrl);
//                        mTvAddress.setText(mAddress);


                    }
                }).build().post();
    }

    private void submitInfo() {

        mNickName = mEtName.getText().toString();
        mBirthday = mTvBirthday.getText().toString();
        mMobile = mEtMobile.getText().toString();
        mEmail = mEtEmail.getText().toString();
//        mAddress = mTvAddress.getText().toString();


        if (TextUtils.isEmpty(mNickName)) {
            ToastUtils.showLongText(this, getString(R.string.edit_mine_nickname));
            return;
        }


        if (TextUtils.isEmpty(mSexType)) {
            ToastUtils.showText(this, getString(R.string.select_sex));
            return;
        }

        if (TextUtils.isEmpty(mNickName)) {
            ToastUtils.showLongText(this, getString(R.string.edit_mine_nickname));
            return;
        }

        if (TextUtils.isEmpty(mBirthday)) {
            ToastUtils.showLongText(this, getString(R.string.select_mine_birthday));
            return;
        }

        if (TextUtils.isEmpty(mMobile)) {
            ToastUtils.showLongText(this, getString(R.string.edit_mine_mobile));
            return;
        }

        if (TextUtils.isEmpty(mEmail)) {
            ToastUtils.showLongText(this, getString(R.string.edit_mine_email));
            return;
        }

//        if (TextUtils.isEmpty(mAddress)) {
//            ToastUtils.showLongText(this, getString(R.string.select_mine_address));
//            return;
//        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.USER_ID, SharedPreferenceUtils.getUserId());
//        map.put(ParameterKeys.HEADER, mImgUrl);
        map.put(ParameterKeys.NICKNAME, mNickName);
        map.put(ParameterKeys.USER_SEX, mSexType);
        map.put(ParameterKeys.BIRTHDAY, mBirthday);
        map.put(ParameterKeys.MOBILE, mMobile);
        map.put(ParameterKeys.EMAIL, mEmail);
//        map.put(ParameterKeys.CONSTANT_ADDRESS, mAddress);

        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.USER_INFO_EDIT)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        UserInfo userInfo = JMessageClient.getMyInfo();
                        userInfo.setNickname(mNickName);

                        JMessageClient.updateMyInfo(UserInfo.Field.nickname, userInfo, new BasicCallback() {
                            @Override
                            public void gotResult(int i, String s) {
                                LogUtils.d("昵称更新成功");
                                ToastUtils.showText(MineInformationActivity.this, "修改成功");
                                setResult(1);
                                Intent intent = new Intent(MineInformationActivity.this, MainActivity.class);
                                intent.putExtra("sex",mTvSex.getText());
                                startActivity(intent);
                            }
                        });

                    }
                }).build().post();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CodeKeys.CAMERA_OPEN:
                    String cameraPath = SharedPreferenceUtils.getCustomAppProfile(ShareKeys.CAMERA_FILE);
                    LogUtils.d("onActivityResult: " + cameraPath);

                    changeHead(cameraPath);
                    break;
                case CodeKeys.PHOTO_OPEN:
                    if (data != null) {
                        Uri photoUri = data.getData();
                        String photoPath = "";
                        if (photoUri.getScheme().equals("file")) {
                            photoPath = photoUri.getPath();
                        } else {
                            photoPath = UriUtils.getPhotoPath(this, photoUri);
                        }
                        LogUtils.d("onActivityResult: " + photoPath);
                        changeHead(photoPath);
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void changeHead(final String localPath) {

        loadImg(localPath, mIvHead, R.mipmap.icon_head_default);
        UploadImgUtil.create().loader(this).uploadImg(localPath, new UploadImgUtil.IUploadImgListener() {
            @Override
            public void onSuccess(String path) {
                mImgUrl = path;
                Map<String, String> map = new HashMap<>();
                map.put(ParameterKeys.USER_ID, SharedPreferenceUtils.getUserId());
                map.put(ParameterKeys.HEADER, mImgUrl);

                String raw = JSON.toJSONString(map);

                RestClient.builder()
                        .url(UrlKeys.USER_INFO_EDIT)
                        .raw(raw)
                        .toast()
                        .success(new ISuccess() {
                            @Override
                            public void onSuccess(String response) {
                                ToastUtils.showText(MineInformationActivity.this, "修改成功");

                                NaturalLoader.stopLoading();
                                File file = new File(localPath);
                                JMessageClient.updateUserAvatar(file, new BasicCallback() {
                                    @Override
                                    public void gotResult(int i, String s) {
                                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_HEAD, mImgUrl);
                                        LogUtils.d("头像更新成功");
                                    }
                                });

                            }
                        }).build().post();
//                http://p8swfk8xu.bkt.clouddn.com/img_20180516113018
            }
        });

    }

    private String getSexType(String sex) {
        String sexType = "";
        if (ContentKeys.SEX_WOMEN.equals(sex)) {
            sexType = ContentKeys.SEX_WOMEN_TYPE;
        } else if (ContentKeys.SEX_MAN.equals(sex)) {
            sexType = ContentKeys.SEX_MAN_TYPE;
        }

        return sexType;
    }


    private void showTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.set(1900, 0, 1);
        endDate.set(2020, 11, 31);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                mTvBirthday.setText(getTime(date));
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})// 默认全部显示
                .setCancelText("")//取消按钮文字
                .setSubmitText("")//确认按钮文字
                .setContentTextSize(16)
                .setTitleSize(16)//标题文字大小
                .setTitleText("请选择出生年月")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(ContextCompat.getColor(this, R.color.black))//标题文字颜色
                .setSubmitColor(ContextCompat.getColor(this, R.color.black))//确定按钮文字颜色
                .setCancelColor(ContextCompat.getColor(this, R.color.black))//取消按钮文字颜色
                .setTitleBgColor(0xFFffffff)//标题背景颜色 Night mode
                .setBgColor(0xFFffffff)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .setTextColorCenter(ContextCompat.getColor(this, R.color.main))
                .setTextColorOut(ContextCompat.getColor(this, R.color.gray))
                .build();
        pvTime.show();
    }


    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }


}
