package com.example.feng.xgs.main.find.shop.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.ShareUtils;
import com.example.feng.xgs.core.banner.BannerCreator;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.shop.detail.comment.ShopCommentFragment;
import com.example.feng.xgs.main.find.shop.detail.image.ShopImageContentFragment;
import com.example.feng.xgs.main.find.shop.detail.parameter.ShopParameterFragment;
import com.example.feng.xgs.main.find.shop.order.SureOrderActivity;
import com.example.feng.xgs.ui.tab.ScrollTab;
import com.example.feng.xgs.ui.weight.BaseViewPagerAdapter;
import com.example.feng.xgs.utils.ArrayToListUtil;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/15 0015.
 * 商品详情
 */

public class ShopDetailActivity extends ToolbarActivity implements ScrollTab.OnTabListener, OnItemClickListener {

    @BindView(R.id.iv_toolbar_right)
    ImageView mIvRight;
    @BindView(R.id.web)
    WebView web;
    @BindView(R.id.tab_shop_detail)
    ScrollTab mTab;
    @BindView(R.id.vp_shop_detail)
    ViewPager mViewPager;
    @BindView(R.id.banner_shop_detail)
    ConvenientBanner<String> mBanner;
    @BindView(R.id.tv_shop_detail_name)
    TextView mTvName;
    @BindView(R.id.tv_shop_detail_info)
    TextView mTvInfo;
    @BindView(R.id.tv_shop_detail_price)
    TextView mTvPrice;
    @BindView(R.id.tv_shop_detail_count)
    TextView mTvCount;
    private String mIdOnly;
    private String mImageResult;
    private String mParameterResult;
    private String mPrice;
    private String mName;
    private List<String> imageList;
    private String mInfo;

    //分享
    @OnClick(R.id.iv_toolbar_right)
    void onClickShare() {
        if (imageList != null && imageList.size() > 0) {
            ShareUtils.create(this)
                    .setText(mInfo)
                    .setImgUrl(imageList.get(0))
                    .setTitleUrl(UrlKeys.BASE_NAME + UrlKeys.PRODUCT_SHARE + "productid=" + mIdOnly)
                    .setUrl(UrlKeys.BASE_NAME + UrlKeys.PRODUCT_SHARE + "productid=" + mIdOnly)
                    .showDialog();
        }
    }

    //客服
    @OnClick(R.id.tv_shop_detail_service)
    void onClickSerVice() {

    }

    //推广
    @OnClick(R.id.tv_shop_detail_popularize)
    void onClickPopularize() {

    }

    //购物车
    @OnClick(R.id.tv_shop_detail_cart)
    void onClickCart() {
        addShopCart();
    }

    //购买
    @OnClick(R.id.tv_shop_detail_pay)
    void onClickPay() {
        startOrderSure();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_find_shop_detail;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        setTitle("商品详情");
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        mIdOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        mIvRight.setImageResource(R.mipmap.icon_share);
        initTab();
        loadData();
//        web.loadUrl(UrlKeys.BASE_URL + "/sc_xgs/share/toProductSharePage.action?productid="+mIdOnly);

    }

    private void initBanner(String bannerResult) {

        imageList = ArrayToListUtil.array2List(bannerResult);
        if (imageList != null) {
            BannerCreator.setDefault(mBanner, imageList, this);
        }

    }

    private void initTab() {
        List<String> titles = new ArrayList<>();
        titles.add("图文详情");
        titles.add("产品参数");
        titles.add("评论详情");
        mTab.setTitles(titles);
    }

    private void initViewPager() {

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ShopImageContentFragment.create(mImageResult));
        fragments.add(ShopParameterFragment.create(mParameterResult));
        fragments.add(ShopCommentFragment.create(mIdOnly));
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        mTab.setViewPager(mViewPager);
        mTab.setOnTabListener(this);
    }

    private void loadData() {

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, mIdOnly);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_DETAIL)
                .raw(raw)
                .toast()
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);

                        //产品名称
                        mName = object.getString("productname");
                        mInfo = object.getString("descript");
                        //价格
                        mPrice = object.getString("price");
                        String bannerResult = object.getString("sowingmap");//轮播图
                        String count = "月销" + object.getString("salesvolume") + "笔";//销量
                        mTvName.setText(mName);
                        mTvInfo.setText(mInfo);
                        mTvCount.setText(count);
                        mTvPrice.setText("￥" + mPrice);

                        mParameterResult = response;
                        mImageResult = object.getString("imagespath");//图片地址

                        initBanner(bannerResult);
                        initViewPager();

                    }
                })
                .build().post();
    }

    private void addShopCart() {
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PRODUCT_ID, mIdOnly);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.SHOP_CART_ADD)
                .raw(raw)
                .loader(this)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(ShopDetailActivity.this, "加入购物车成功");
                    }
                })
                .build().post();
    }


    private void startOrderSure() {

        List<LinkedHashMap<Object, Object>> linkedHashMapList = new ArrayList<>();

        String imgUrl = "";
        if (imageList != null && imageList.size() > 0) {
            imgUrl = imageList.get(0);
        }
        LinkedHashMap<Object, Object> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(EntityKeys.NAME, mName);
        linkedHashMap.put(EntityKeys.IMG_URL, imgUrl);
        linkedHashMap.put(EntityKeys.PRICE, mPrice);
        linkedHashMap.put(EntityKeys.COUNT, "1");
//        linkedHashMap.put(EntityKeys.ID_ONLY, name);
        linkedHashMap.put(EntityKeys.PRODUCT_ID, mIdOnly);

        linkedHashMapList.add(linkedHashMap);


        if (linkedHashMapList.size() <= 0) {
            ToastUtils.showText(this, "请选择商品");
            return;
        }

        String info = JSON.toJSONString(linkedHashMapList);

        Intent intent = new Intent(this, SureOrderActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, info);
        intent.putExtra(ContentKeys.ACTIVITY_PRICE, "￥" + mPrice);
        startActivity(intent);

    }

    @Override
    public void onChange(int position, View v) {
        mViewPager.setCurrentItem(position);
    }


    @Override
    public void onItemClick(int position) {

    }
}
