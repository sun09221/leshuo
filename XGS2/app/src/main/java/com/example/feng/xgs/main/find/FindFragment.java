package com.example.feng.xgs.main.find;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.amap.api.location.AMapLocation;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.banner.BannerCreator;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.find.activity.FindActivityActivity;
import com.example.feng.xgs.main.find.broadcast.NewRadio;
import com.example.feng.xgs.main.find.broadcast.NewVoice;
import com.example.feng.xgs.main.nearby.LocationHandler;
import com.example.feng.xgs.main.nearby.location.NearbyLocationActivity;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/4/24 0024.
 * icon_find_normal
 */

public class FindFragment extends EmptyFragment implements OnItemClickListener {

    //    @BindView(R.id.iv_toolbar_back)ImageView mIvBack;
    @BindView(R.id.tv_toolbar_title)
    TextView mTvTitle;
    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.banner_find)
    ConvenientBanner<String> mBanner;

    @BindView(R.id.tv_toolbar_save)
    TextView mTvAddress;
    private String mProvince;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x10) {
                BannerCreator.setDefault(mBanner, imgList, FindFragment.this);
            }
        }
    };

    @OnClick(R.id.tv_toolbar_save)
    void onClickAddress() {
        if (TextUtils.isEmpty(mProvince)) {
            ToastUtils.showText(getContext(), "正在定位，请稍候...");
            return;
        }
        Intent intent = new Intent(getActivity(), NearbyLocationActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_INFO, mProvince);
        startActivityForResult(intent, CodeKeys.EDITOR_REQUEST);
    }

    private FindAdapter mAdapter;
    private FindDataConverter mDataConverter;
    private LocationHandler mLocationHandler;


//    //商城
//    @OnClick(R.id.tv_find_shop)
//    void onClickShop() {
//        startActivity(new Intent(getContext(), ShopActivity.class));
//    }
    //心情电台
    @OnClick(R.id.tv_find_mood)
    void onClickMood() {
      //  startActivity(new Intent(getContext(), MoodActivity.class));
        startActivity(new Intent(getContext(), NewRadio.class));
    }
    //活动
    @OnClick(R.id.tv_find_activity)
    void onClickActivity() {
        startActivity(new Intent(getContext(), FindActivityActivity.class));
    }

    //广播
    @OnClick(R.id.tv_find_broadcast)
    void onClickBroadcast() {
        startActivity(new Intent(getContext(), NewVoice.class));
    }

//    //店铺
//    @OnClick(R.id.tv_find_store)
//    void onClickStore() {
//        startActivity(new Intent(getContext(), StoreActivity.class));
//    }

    public static FindFragment create() {
        return new FindFragment();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void emptyLoadData() {
        loadData();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_find;
    }


    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        mTvTitle.setText(R.string.find);

//        mIvBack.setVisibility(View.GONE);
        initBanner();
        initRefreshLayout(mRefresh);
        initRecyclerView();
        startLocation();
    }

    List<String> imgList;

    private void initBanner() {
        imgList = new ArrayList<>();

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, ContentKeys.BANNER_TYPE_FIND);
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.BANNER)
                .raw(raw)
                .toast(getActivity())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.i("asdassdas", response.toString());
                        JSONArray array = JSON.parseObject(response).getJSONArray("imagepath");

                        if (array != null) {


                            for (int i = 0; i < array.size(); i++) {
                                String imgUrl = array.getString(i);
                                imgList.add(imgUrl);
                            }

                        }
                        Message message = Message.obtain();
                        message.what = 0x10;
                        mHandler.sendMessage(message);


                    }
                })
                .build().post();

//        imgList.add(UrlKeys.IMG_URL_TEST);
//        imgList.add(UrlKeys.IMG_URL_TEST);
//        imgList.add(UrlKeys.IMG_URL_TEST);

    }


    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new FindDataConverter();
        mAdapter = new FindAdapter(R.layout.item_find, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {

        if (TextUtils.isEmpty(mProvince)) {
            startLocation();
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PROVINCE, mProvince);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_LIST)
                .raw(raw)
                .loader(getContext())
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.setEmptyView(emptyView);
                        mRefresh.setRefreshing(false);

                        mDataConverter.clearData();
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        mAdapter.setEmptyView(emptyView);
                        mRefresh.setRefreshing(false);
                    }
                })
                .build().post();

    }


    //轮播图的点击事件
    @Override
    public void onItemClick(int position) {

    }


    /**
     * 定位成功后开始请求数据
     */
    private void startLocation() {
        mLocationHandler = LocationHandler.create(getActivity());
        mLocationHandler.initLocation(new LocationHandler.ILocationResultListener() {
            @Override
            public void onLocationResult(AMapLocation location) {

                mProvince = location.getProvince();
                mTvAddress.setText(mProvince);

                loadData();

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == CodeKeys.FOR_RESULT_CODE) {
            if (requestCode == CodeKeys.EDITOR_REQUEST && data != null) {
                mProvince = data.getStringExtra(ContentKeys.ACTIVITY_INFO);
                mTvAddress.setText(mProvince);
                loadData();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mLocationHandler != null) {
            mLocationHandler.onDestroy();
        }

    }
}
