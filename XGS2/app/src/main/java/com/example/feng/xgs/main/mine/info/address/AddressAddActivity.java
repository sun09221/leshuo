package com.example.feng.xgs.main.mine.info.address;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.info.CityJsonBean;
import com.example.feng.xgs.main.mine.info.CityJsonHandler;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2017/12/22.
 * 新增收货地址
 */

public class AddressAddActivity extends ToolbarActivity {

    private ArrayList<CityJsonBean> options1Items;
    private ArrayList<ArrayList<String>> options2Items;
    private ArrayList<ArrayList<ArrayList<String>>> options3Items;

    @BindView(R.id.et_address_add_name)
    EditText mEtName;
    @BindView(R.id.et_address_add_mobile)
    EditText mEtMobile;
    @BindView(R.id.tv_address_add_province)
    TextView mTvProvince;
    @BindView(R.id.tv_address_add_street)
    TextView mTvStreet;
    @BindView(R.id.et_address_add_post_code)
    EditText mEtPostCode;
    @BindView(R.id.et_address_add_detail)
    EditText mEtDetail;
    @BindView(R.id.tv_address_add_default)
    TextView mTvDefault;

    private String mIsDefault = ContentKeys.ADDRESS_DEFAULT;
    private String mAddressId = null;
    private int mType;
    private String mUrl;
    private String toastHint;
    private String mProvince;
    private String mCity;
    private String mArea;

    //保存
    @OnClick(R.id.tv_toolbar_save)
    void onClickSure() {
        addData();
    }

    @OnClick(R.id.ly_address_add_select)void onClickAddress(){
        HideKeyboard(mEtMobile);
        showPickerView();
    }
    //隐藏虚拟键盘
    public static void HideKeyboard(View v)
    {
        InputMethodManager imm = ( InputMethodManager ) v.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
        if ( imm.isActive( ) ) {
            imm.hideSoftInputFromWindow( v.getApplicationWindowToken( ) , 0 );

        }
    }
    //设置默认地址
    @OnClick(R.id.tv_address_add_default)
    void onClickDefault() {
        if (ContentKeys.ADDRESS_DEFAULT.equals(mIsDefault)) {

            mIsDefault = ContentKeys.ADDRESS_DEFAULT_NO;
            mTvDefault.setSelected(false);

        } else {
            mIsDefault = ContentKeys.ADDRESS_DEFAULT;
            mTvDefault.setSelected(true);
        }

    }

    @Override
    public Object setLayout() {
        return R.layout.activity_address_add;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.address_add));
        Intent intent = getIntent();
        if (intent == null) return;

        mType = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, -1);
        if (mType == CodeKeys.ADDRESS_EDITOR_TAG) {
            mUrl = UrlKeys.ADDRESS_EDITOR;
            mAddressId = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
            mTvDefault.setVisibility(View.GONE);
            loadEditorData();
        } else if (mType == CodeKeys.ADDRESS_ADD_TAG) {
            mUrl = UrlKeys.ADDRESS_ADD;
        }

        mTvDefault.setSelected(true);

        CityJsonHandler.create().initJsonData(new CityJsonHandler.ICityJsonListener() {
            @Override
            public void onCityList(ArrayList<CityJsonBean> options1Items, ArrayList<ArrayList<String>> options2Items, ArrayList<ArrayList<ArrayList<String>>> options3Items) {
                AddressAddActivity.this.options1Items = options1Items;
                AddressAddActivity.this.options2Items = options2Items;
                AddressAddActivity.this.options3Items = options3Items;
            }
        });

    }

    private void addData() {
        if (TextUtils.isEmpty(mUrl)) {
            ToastUtils.showText(this, "请返回重试");
            return;
        }

        String name = mEtName.getText().toString();
        if (name.length() <= 0) {
            ToastUtils.showText(this, "姓名不能为空");
            return;
        }

        String mobile = mEtMobile.getText().toString().trim();
        if (mobile.length() != 11) {
            ToastUtils.showText(this, "请输入11位有效手机号");
            return;
        }

//        String street = mTvStreet.getText().toString();
//        street = "测试街道";
//        if (TextUtils.isEmpty(street)) {
//            ToastUtils.showText(this, getString(R.string.address_select_street));
//            return;
//        }



        String postCode = mEtPostCode.getText().toString();
        if (postCode.length() <= 0) {
            ToastUtils.showText(this, getString(R.string.edit_address_post_code));
            return;
        }

        String detail = mEtDetail.getText().toString();
        if (detail.length() <= 0) {
            ToastUtils.showText(this, getString(R.string.edit_address_detail));
            return;
        }


        Map<String, String> map = new HashMap<>();
        //如果是新增地址，传peopleId，如果是编辑传地址id
        if (mType == CodeKeys.ADDRESS_ADD_TAG) {
            map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
            toastHint = "添加成功";
        } else if (mType == CodeKeys.ADDRESS_EDITOR_TAG) {
            map.put(ParameterKeys.ID_ONLY, mAddressId);
            toastHint = "修改成功";
        } else {
            return;
        }

        //如果省市区都为空，则返回
        if(TextUtils.isEmpty(mProvince) && TextUtils.isEmpty(mCity) && TextUtils.isEmpty(mArea)){
            ToastUtils.showText(this, "请选择省市区");
            return;
        }


        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.TYPE, mIsDefault);
        map.put(ParameterKeys.ADDRESS_PROVINCE, mProvince);
        map.put(ParameterKeys.ADDRESS_CITY, mCity);
        map.put(ParameterKeys.ADDRESS_AREA, mArea);
//        map.put(ParameterKeys.ADDRESS_STREET, street);
        map.put(ParameterKeys.ADDRESS_NAME, name);
        map.put(ParameterKeys.ADDRESS_MOBILE, mobile);
        map.put(ParameterKeys.ADDRESS_POST_CODE, postCode);
        map.put(ParameterKeys.ADDRESS_DETAIL, detail);

        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(mUrl)
                .loader(this)
                .toast()
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(AddressAddActivity.this, toastHint);
                        setResult(CodeKeys.FOR_RESULT_CODE);
                        finish();
                    }
                })
                .build().post();
    }


    /**
     * 如果是编辑收货地址，需要加载地址数据
     */
    private void loadEditorData() {
        if (TextUtils.isEmpty(mAddressId)) {
            ToastUtils.showText(this, "加载失败...");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, mAddressId);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ADDRESS_DETAIL)
                .loader(this)
                .toast()
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        setData(response);
                    }
                })
                .build().post();
    }


    /**
     * 为控件绑定数据
     */
    private void setData(String response) {
        JSONObject object = JSON.parseObject(response);

        String peopleId = object.getString("peopleid");
        String idOnly = object.getString("id");

        String name = object.getString("receivername");
        String mobile = object.getString("receiverphone");

        mProvince = object.getString("province");
        mCity = object.getString("city");
        mArea = object.getString("county"); //区
        String street = object.getString("street");
        String address = object.getString("address");

        String pageSize = object.getString("pageSize");
        String pageNum = object.getString("pageNum");

        String postCode = object.getString("postcode");//邮编
        mIsDefault = object.getString("type");//是否是默认地址 1是0否

        String editorTime = object.getString("modifytime");
        String createTime = object.getString("createtime");

        String addressAll = mProvince + mCity + mArea;
        mEtName.setText(name);
        mEtMobile.setText(mobile);
        mTvProvince.setText(addressAll);
        mTvStreet.setText(street);
        mEtDetail.setText(address);
        mEtPostCode.setText(postCode);

        if (mIsDefault.equals(ContentKeys.ADDRESS_DEFAULT)) {
            mTvDefault.setSelected(true);
            mTvDefault.setVisibility(View.GONE);
        } else {
            mTvDefault.setSelected(false);
            mTvDefault.setVisibility(View.VISIBLE);
        }
    }



    /**
     * 弹出选择器
     * */
    private void showPickerView() {

        if(options1Items == null || options2Items == null || options3Items == null){
            ToastUtils.showText(this, "城市数据尚未初始化完毕");
            return;
        }


        OptionsPickerView pvOptions =
                new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        mProvince = options1Items.get(options1).getPickerViewText();
                        mCity = options2Items.get(options1).get(options2);
                        mArea = options3Items.get(options1).get(options2).get(options3);
                        String tx = options1Items.get(options1).getPickerViewText() +
                                options2Items.get(options1).get(options2) +
                                options3Items.get(options1).get(options2).get(options3);
                        mTvProvince.setText(tx);
//                        ToastUtils.showText(AddressAddActivity.this, tx);
                    }
                })

                        .setTitleText("城市选择")
                        .setDividerColor(Color.BLACK)
                        .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                        .setContentTextSize(20)
                        .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


}
