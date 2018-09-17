package com.example.feng.xgs.main.mine.vip;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.loader.NaturalLoader;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.pay.FastPay;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/10 0010.
 * 升级VIP
 */

public class VIPActivity extends ToolbarActivity {


    @BindView(R.id.tv_ali)
    TextView mTvAli;
    @BindView(R.id.tv_we_chat)
    TextView mTvWeChat;

    @BindView(R.id.tv_vip_time_left)
    TextView mTvVipTimeLeft;
    @BindView(R.id.tv_vip_price_left)
    TextView mTvPriceLeft;
    @BindView(R.id.tv_vip_time_center)
    TextView mTvTimeCenter;
    @BindView(R.id.tv_vip_price_center)
    TextView mTvPriceCenter;
    @BindView(R.id.tv_vip_time_right)
    TextView mTvTimeRight;
    @BindView(R.id.tv_vip_price_right)
    TextView mTvPriceRight;
    @BindView(R.id.ly_vip_item_parent)
    LinearLayout mLyLayout;
    @BindView(R.id.ly_vip_left)
    LinearLayout mLyLeft;
    @BindView(R.id.ly_vip_center)
    LinearLayout mLyCenter;
    @BindView(R.id.ly_vip_right)
    LinearLayout mLyRight;


    private static final String TYPE_ITEM_LEFT = "1";
    private static final String TYPE_ITEM_CENTER = "2";
    private static final String TYPE_ITEM_RIGHT = "4";
    private static final String TYPE_ITEM_CENTER_2 = "3";
    @BindView(R.id.include_toolbar)
    RelativeLayout includeToolbar;
    @BindView(R.id.tv_vip_info)
    TextView tvVipInfo;
    @BindView(R.id.tv_vip_time_center_2)
    TextView tvVipTimeCenter2;
    @BindView(R.id.tv_vip_price_center_2)
    TextView tvVipPriceCenter2;
    @BindView(R.id.ly_vip_center_2)
    LinearLayout lyVipCenter2;
    @BindView(R.id.tv_vip_sure)
    TextView tvVipSure;
    private String mTypeVIP = TYPE_ITEM_LEFT;
    private String mTypePay = ContentKeys.WE_CHAT;

    private String mInfo;

    //左边的item
    @OnClick(R.id.ly_vip_left)
    void onClickLeft() {
        mLyLeft.setSelected(true);
        mLyCenter.setSelected(false);
        mLyRight.setSelected(false);
        lyVipCenter2.setSelected(false);
        mTypeVIP = TYPE_ITEM_LEFT;
    }

    //中间的item
    @OnClick(R.id.ly_vip_center)
    void onClickCenter() {
        mLyLeft.setSelected(false);
        mLyCenter.setSelected(true);
        mLyRight.setSelected(false);
        lyVipCenter2.setSelected(false);
        mTypeVIP = TYPE_ITEM_CENTER;
    }

    //右边的item
    @OnClick(R.id.ly_vip_right)
    void onClickRight() {
        mLyLeft.setSelected(false);
        mLyCenter.setSelected(false);
        mLyRight.setSelected(true);
        lyVipCenter2.setSelected(false);
        mTypeVIP = TYPE_ITEM_RIGHT;
    }

    //右边的item
    @OnClick(R.id.ly_vip_center_2)
    void onClickCenter_2() {
        mLyLeft.setSelected(false);
        mLyCenter.setSelected(false);
        mLyRight.setSelected(false);
        lyVipCenter2.setSelected(true);
        mTypeVIP = TYPE_ITEM_CENTER_2;
    }

    //支付宝
    @OnClick(R.id.tv_ali)
    void onClickAli() {
        mTvAli.setSelected(true);
        mTvWeChat.setSelected(false);
        mTypePay = ContentKeys.ALI_PAY;
    }

    //微信
    @OnClick(R.id.tv_we_chat)
    void onClickWeChat() {
        mTvAli.setSelected(false);
        mTvWeChat.setSelected(true);
        mTypePay = ContentKeys.WE_CHAT;
    }

    //升级VIP
    @OnClick(R.id.tv_vip_sure)
    void onClickSure() {
        pay();
    }

    //VIP优势介绍
    @OnClick(R.id.tv_vip_info)
    void onClickInfo() {
        if (TextUtils.isEmpty(mInfo)) {
            return;
        }
        Intent intent = new Intent(this, VIPInfoActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, mInfo);
        startActivity(intent);
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_vip;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle(getString(R.string.mine_vip));


        initView();
    }

    private void initView() {
        mTvWeChat.setSelected(true);

        int screenWidth = DensityUtils.getScreenWidth(this);
        int itemWidth = (screenWidth - DensityUtils.dp2px(this, 60)) / 3;
        int viewPagerHeight = itemWidth + DensityUtils.dp2px(this, 36);

        mLyLayout.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, viewPagerHeight));
        mLyLeft.setSelected(true);

        loadData();

    }

    private void loadData() {

        RestClient.builder()
                .url(UrlKeys.VIP)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray array = JSON.parseObject(response).getJSONArray("datalist");
                        if (array != null) {
                            int size = array.size();
                            for (int i = 0; i < size; i++) {
                                JSONObject object = JSON.parseObject(array.getString(i));
                                String priceYear = object.getString("yearprice");
                                String priceQuarter = object.getString("quarterprice");
                                String priceMouth = object.getString("monthprice");
                                String halfyearprice = object.getString("halfyearprice");
                                mInfo = object.getString("advantage");
                                tvVipPriceCenter2.setText(halfyearprice);
                                mTvPriceLeft.setText(priceMouth);
                                mTvPriceCenter.setText(priceQuarter);
                                mTvPriceRight.setText(priceYear);
                            }
                        }

                    }
                })
                .build().post();
    }

    private void pay() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.TYPE_VIP, mTypeVIP);
        map.put(ParameterKeys.TYPE, ContentKeys.ORDER_VIP);
        String raw = JSON.toJSONString(map);

        NaturalLoader.showLoading(this);
        RestClient.builder()
                .url(UrlKeys.ORDER_VIP_CREATE)
                .raw(raw)
                .toast(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String orderNo = object.getString("orderno");
                        if (ContentKeys.ALI_PAY.equals(mTypePay)) {
                            aliPay(orderNo);
                        } else if (ContentKeys.WE_CHAT.equals(mTypePay)) {
                            weChatPay(orderNo);
                        } else {
                            ToastUtils.showText(VIPActivity.this, "请选择支付方式");
                        }
                    }
                })
                .build().post();
    }

    private void aliPay(String orderNo) {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ORDER_NO, orderNo);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ORDER_VIP_PAY_ALI)
                .raw(raw)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String paySign = object.getString("order");
                        NaturalLoader.stopLoading();
                        FastPay.create(VIPActivity.this).aliPay(paySign);

                    }
                })
                .build().post();
    }

    private void weChatPay(String orderNo) {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ORDER_NO, orderNo);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.ORDER_VIP_PAY_WEIXIN)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        IWXAPI api = WXAPIFactory.createWXAPI(VIPActivity.this, object.getString("appid"));
                        api.registerApp(object.getString("appid"));
                        PayReq payReq = new PayReq();
                        payReq.appId = object.getString("appid");
                        payReq.partnerId = object.getString("partnerid");
                        payReq.packageValue = "Sign=WXPay";
                        payReq.prepayId = object.getString("prepayid");
                        payReq.nonceStr = object.getString("noncestr");
                        payReq.timeStamp = object.getString("timestamp");
                        payReq.sign = object.getString("sign");
                        api.sendReq(payReq);
                    }
                })
                .build().post();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
