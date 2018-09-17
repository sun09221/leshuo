package com.example.feng.xgs.main.circle;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.NaturalChildAdapter;
import com.example.feng.xgs.main.move.NaturalChildAdapterFriend;
import com.example.feng.xgs.main.move.NaturalChildDataConverter;
import com.example.feng.xgs.main.move.detail.DynamicDetailActivity;
import com.example.feng.xgs.main.move.detail.DynamicVideoDetailActivity;
import com.example.feng.xgs.main.move.handler.AttentionHandler;
import com.example.feng.xgs.main.move.handler.LikeHandler;
import com.example.feng.xgs.main.move.reward.GiveARewardDialog;
import com.example.feng.xgs.main.nearby.detail.NearbyDetailActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * 描述：添加类的描述
 *
 * @author 金源
 * @time 2018/7/23
 */
public class CircleFragment extends EmptyFragment  implements BaseQuickAdapter.OnItemChildClickListener,
        NaturalChildAdapter.IOnItemImagePreviewClickListener,NaturalChildAdapterFriend.IOnItemImagePreviewClickListener {
    @BindView(R.id.iv_toolbar_back)
    ImageView ivToolbarBack;
    @BindView(R.id.tv_toolbar_title)
    TextView tvToolbarTitle;
    @BindView(R.id.include_toolbar)
    RelativeLayout includeToolbar;
    @BindView(R.id.rec_title)
    RecyclerView recTitle;
    @BindView(R.id.rec_data)
    RecyclerView recData;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout refreshBase;
    @BindView(R.id.tv_empty_no_data)
    TextView tv_empty_no_data;
    Unbinder unbinder;
    private  int page = 2;
    private CircleAdapter mCircleAdapter;
    private TitleAdapter mtitleAdapter;
    private MoodAdapter moodAdapter;
    private LinearLayoutManager mManager;
    private LinearLayoutManager mTitleManager;
    private NaturalChildAdapter mAdapter;
    private NaturalChildDataConverter mDataConverters;
    private NaturalChildAdapterFriend Adapter;
    private NaturalChildDataConverter DataConverters;
    private String parentLevel = "";
    private String peopleId;
    private String peopleId2;
    private BaseEntity entity;
    private BaseEntity entity2;
    private int time;
    private int  Mrefresh;
    CircleDataConverter mDataConverter;
    CircleAttentionConverter mDataConverteratention;
    TitleDataConverter mTitleDataConverter;
    MoodViewConverter moodViewConverter;
    private String mImgUrl;
    private Handler handler = new Handler();

    private boolean isLoading = false;    //设置是否处于上啦加载状态
    private boolean Loading = false;    //设置是否处于上啦加载状态



    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x10:
                    loadData();
                    break;
            }
            return false;
        }
    });

    @Override
    public Object setLayout() {
        return R.layout.fragment_circle_main;
    }

    public static CircleFragment create() {
        return new CircleFragment();
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        unbinder = ButterKnife.bind(this, rootView);
        tvToolbarTitle.setText("圈子");
        ivToolbarBack.setVisibility(View.GONE);
     //   initRefreshLayout(refreshBase);
        setEvent();
        initTitleRecViews();
        getTitle();
        refreshBase.setColorSchemeResources(
                R.color.pink,
                R.color.main,
                R.color.yellow_light
        );
        refreshBase.setVisibility(View.GONE);
        recData.setVisibility(View.GONE);
        tv_empty_no_data.setVisibility(View.VISIBLE);
        initRecViewsall();
        loadData2();
    }

    private void initRecViews() {
        mManager = new LinearLayoutManager(getContext());
        recData.setLayoutManager(mManager);
      //  addDivider(10);
        mDataConverter = new CircleDataConverter();
        mCircleAdapter = new CircleAdapter(R.layout.item_circle, mDataConverter.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);

    }
    private void initRecViewsattention() {
        mManager = new LinearLayoutManager(getContext());
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverteratention = new CircleAttentionConverter();
        mCircleAdapter = new CircleAdapter(R.layout.item_circle, mDataConverteratention.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);

    }
    private void initRecViewsall() {
        mManager = new LinearLayoutManager(getContext());
        recData.setLayoutManager(mManager);
       // addDivider(10);
        mDataConverter = new CircleDataConverter();
        mDataConverters = new NaturalChildDataConverter();
        mAdapter = new NaturalChildAdapter(mDataConverters.ENTITIES);
        recData.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemImagePreviewListener(this);

    }
    private void initRecViewsfriend() {
        mManager = new LinearLayoutManager(getContext());
        recData.setLayoutManager(mManager);
        // addDivider(10);
        DataConverters = new NaturalChildDataConverter();
        Adapter = new NaturalChildAdapterFriend(DataConverters.ENTITIES);
        recData.setAdapter(Adapter);
        Adapter.setOnItemChildClickListener(this);
        Adapter.setOnItemImagePreviewListener(this);

    }
    private void initRecViewsmood() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recData.setLayoutManager(manager);
        try {
            PagerSnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recData);
        }catch (Exception e){

        }finally {
            moodViewConverter = new MoodViewConverter();
            moodAdapter = new MoodAdapter(R.layout.item_mood,moodViewConverter.ENTITIES);
            recData.setAdapter(moodAdapter);
            moodAdapter.setOnItemChildClickListener(this);
        }
        // addDivider(10);
    }
    private void initTitleRecViews() {
        mTitleManager = new LinearLayoutManager(getContext());
        mTitleManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recTitle.setLayoutManager(mTitleManager);
       // addDivider(10);
        mTitleDataConverter = new TitleDataConverter();
        mtitleAdapter = new TitleAdapter(R.layout.item_title, mTitleDataConverter.ENTITIES);
        recTitle.setAdapter(mtitleAdapter);

        mtitleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                setSelect(i);
                baseQuickAdapter.notifyDataSetChanged();
                parentLevel = mTitleDataConverter.ENTITIES.get(i).getField("id");

                if (parentLevel.equals("111")){
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    isLoading = false;
                    time=1;
                    page=2;
                    setEvent();
                    initRecViewsall();
                    loadData2();
                 }else if (parentLevel.equals("222")){
                    time=2;
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    Loading = false;
                    page=2;
                    setEvent2();
                    initRecViewsfriend();
                    loadData3();
                 }
                 else if (parentLevel.equals("333")){
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    try {
                        loadDataMyAttention();
                        initRecViewsattention();
                        setEventAttention();
                    }catch (Exception e){

                    }

                 }
                 else{
                    loadData();
                    initRecViews();
                    setEvent3();
                 }


            }
        });
    }
    private void loadData() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put("parentlevel", parentLevel);
        LogUtils.i("parentlevel", parentLevel);
        mUrl = UrlKeys.CIRCLE_LIST;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        refreshBase.setRefreshing(false);
                        mDataConverter.clearData();
                        mCircleAdapter.setNewData(mDataConverter.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }
    private void loadDataMyAttention() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        mUrl = UrlKeys.CIRCLE_LIST_MY_ATTENTION;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        refreshBase.setVisibility(View.VISIBLE);
                        recData.setVisibility(View.VISIBLE);
                        tv_empty_no_data.setVisibility(View.GONE);
                        refreshBase.setRefreshing(false);
                        mDataConverteratention.clearData();
                        mCircleAdapter.setNewData(mDataConverteratention.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }
    private void loadData2() {
        page=2;
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        String raw = JSON.toJSONString(map);
        mUrl = UrlKeys.NATURAL_NEW;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        refreshBase.setVisibility(View.VISIBLE);
                        recData.setVisibility(View.VISIBLE);
                        tv_empty_no_data.setVisibility(View.GONE);
                        refreshBase.setRefreshing(false);
                        mDataConverters.clearData();
                        mAdapter.setNewData(mDataConverters.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }
    private void loadData4(int pages) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(pages));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        String raw = JSON.toJSONString(map);
        mUrl = UrlKeys.NATURAL_NEW;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        JSONArray array = objectData.getJSONArray("datalist");
                        if (array != null ) {
                            isLoading = false;
                            mAdapter.setNewData(mDataConverters.setJsonData(response).convert());
                            page++;

                        } else {
                            isLoading = false;
                         //   Toast.makeText(getActivity(), "没有更多内容了", Toast.LENGTH_LONG).show();
                            // adapter.notifyItemRemoved(adapter.getItemCount());
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        Toast.makeText(getActivity(), "没有更多内容了", Toast.LENGTH_LONG).show();
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }
    private void loadData3() {
        page=2;
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        String raw = JSON.toJSONString(map);
        mUrl = UrlKeys.NATURAL_ATTENTION;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        refreshBase.setVisibility(View.VISIBLE);
                        recData.setVisibility(View.VISIBLE);
                        tv_empty_no_data.setVisibility(View.GONE);
                        refreshBase.setRefreshing(false);
                        DataConverters.clearData();
                        Adapter.setNewData(DataConverters.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }
    private void loadData5(int pages) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(pages));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        String raw = JSON.toJSONString(map);
        mUrl = UrlKeys.NATURAL_ATTENTION;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        JSONArray array = objectData.getJSONArray("datalist");
                        if (array != null ) {
                            Loading = false;
                            Adapter.setNewData(DataConverters.setJsonData(response).convert());
                            page++;

                        } else {
                            Loading = false;

                            // adapter.notifyItemRemoved(adapter.getItemCount());
                        }
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        Toast.makeText(getActivity(), "没有更多内容了", Toast.LENGTH_LONG).show();
                        refreshBase.setRefreshing(false);
                    }
                })
                .build().post();
    }
    private void loadDatamood() {
        String mUrl = null;
        mUrl = UrlKeys.MOODVIEW;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        refreshBase.setVisibility(View.VISIBLE);
                        recData.setVisibility(View.VISIBLE);
                        tv_empty_no_data.setVisibility(View.GONE);
                        refreshBase.setRefreshing(false);
                        moodViewConverter.clearData();
                        moodAdapter.setNewData(moodViewConverter.setJsonData(response).convert());

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }
    private void setSelect(int pos) {
        mTitleDataConverter.ENTITIES.get(pos).setField("type", "1");
        for (int i = 0; i < mTitleDataConverter.ENTITIES.size(); i++) {
            if (i != pos) {
                mTitleDataConverter.ENTITIES.get(i).setField("type", "0");
            }

        }

    }

    @Override
    public RecyclerView getRecyclerView() {
        return recData;
    }

    private boolean isFirst = true;

    @Override
    public void emptyLoadData() {
        loadData();
    }

    private void getTitle() {

        String mUrl = null;
        Map<String, String> map = new HashMap<>();

        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.TYPE, "0");
        mUrl = UrlKeys.TITLE_CIRCLE;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        refreshBase.setRefreshing(false);
                        mTitleDataConverter.clearData();
                        mtitleAdapter.setNewData(mTitleDataConverter.setJsonData(response).convert());
                        if (isFirst) {
                            mTitleDataConverter.ENTITIES.get(0).setField("type", "1");
                            parentLevel = mTitleDataConverter.ENTITIES.get(0).getField("id");
                            isFirst=false;
                        } else {
                            initSelect();
                        }

                        Message message = Message.obtain();
                        message.what = 0x10;
                        mHandler.sendMessage(message);
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        refreshBase.setRefreshing(false);

                    }
                })
                .build().post();
    }


    @Override
    public void onRefresh() {
        getTitle();
        loadData();
    }

    private void initSelect() {
        for (int i = 0; i < mTitleDataConverter.ENTITIES.size(); i++) {
            if (parentLevel.equals(mTitleDataConverter.ENTITIES.get(i).getField("id"))) {
                mTitleDataConverter.ENTITIES.get(i).setField("type", "1");
                return;
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, final View view, final int position) {
        try {
            entity2 = mAdapter.getData().get(position);
            peopleId2 = entity2.getField(EntityKeys.PEOPLE_ID);
            String isLike = entity2.getField(EntityKeys.TYPE_LIKE);
            entity = Adapter.getData().get(position);
            peopleId = entity.getField(EntityKeys.PEOPLE_ID);
        }catch (Exception e){

        }


        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_add_circle:
                if (mDataConverter.ENTITIES.size()==0){
                    if ("1".equals(mDataConverteratention.ENTITIES.get(position).getField("state"))) {
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverteratention.ENTITIES.get(position).getField("id"));
                        intent.putExtra("imagepath", mDataConverteratention.ENTITIES.get(position).getField("images"));
                        intent.putExtra("name", mDataConverteratention.ENTITIES.get(position).getField("name"));
                        startActivity(intent);
                    } else {
                        join_Circle(position);

                    }
                }else{
                    if ("1".equals(mDataConverter.ENTITIES.get(position).getField("state"))) {
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverter.ENTITIES.get(position).getField("id"));
                        intent.putExtra("imagepath", mDataConverter.ENTITIES.get(position).getField("images"));
                        intent.putExtra("name", mDataConverter.ENTITIES.get(position).getField("name"));
                        startActivity(intent);
                    } else {
                        join_Circle(position);

                    }
            }


                break;
            case R.id.iv_circle_logo:
                if (mDataConverter.ENTITIES.size()==0){
                    if ("1".equals(mDataConverteratention.ENTITIES.get(position).getField("state"))) {
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverteratention.ENTITIES.get(position).getField("id"));
                        intent.putExtra("imagepath", mDataConverteratention.ENTITIES.get(position).getField("images"));
                        intent.putExtra("name", mDataConverteratention.ENTITIES.get(position).getField("name"));
                        startActivity(intent);
                    } else {
                        join_Circle(position);

                    }
                }else{
                    if ("1".equals(mDataConverter.ENTITIES.get(position).getField("state"))) {
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverter.ENTITIES.get(position).getField("id"));
                        intent.putExtra("imagepath", mDataConverter.ENTITIES.get(position).getField("images"));
                        intent.putExtra("name", mDataConverter.ENTITIES.get(position).getField("name"));
                        startActivity(intent);
                    } else {
                        join_Circle(position);

                    }
                }


                break;
            case R.id.item_circle_layout:
                if (mDataConverter.ENTITIES.size()==0){
                    if ("1".equals(mDataConverteratention.ENTITIES.get(position).getField("state"))) {
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverteratention.ENTITIES.get(position).getField("id"));
                        intent.putExtra("imagepath", mDataConverteratention.ENTITIES.get(position).getField("images"));
                        intent.putExtra("name", mDataConverteratention.ENTITIES.get(position).getField("name"));
                        startActivity(intent);
                    } else {
                        join_Circle(position);

                    }
                }else{
                    if ("1".equals(mDataConverter.ENTITIES.get(position).getField("state"))) {
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverter.ENTITIES.get(position).getField("id"));
                        intent.putExtra("imagepath", mDataConverter.ENTITIES.get(position).getField("images"));
                        intent.putExtra("name", mDataConverter.ENTITIES.get(position).getField("name"));
                        startActivity(intent);
                    } else {
                        join_Circle(position);

                    }
                }


                break;
            case R.id.tv_natural_item_give_a_reward:
                GiveARewardDialog.create(getActivity()).beginShow(peopleId2);
                break;
//            case R.id.tv_natural_item_share:
//                BaseEntity entity = moodAdapter.getData().get(position);
//                //分享
//                ShareUtils.create(getActivity())
//                        .setText("心情分享")
//                        .setImgUrl( entity.getField(EntityKeys.IMG_PATH))
//                        .setTitleUrl(entity.getField(EntityKeys.IMG_PATH))
//                        .setUrl(entity.getField(EntityKeys.IMG_PATH))
//                        .showDialog();
//
//
//            break;
            case R.id.iv_natural_item_head:
                Intent intent1 = new Intent(getActivity(), NearbyDetailActivity.class);
                intent1.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mDataConverters.ENTITIES.get(position).getField(EntityKeys.PEOPLE_ID));
                getActivity().startActivity(intent1);
                break;
            case R.id.tv_natural_item_attention:
                AttentionHandler.create(getActivity()).attention(peopleId2, new AttentionHandler.IAttentionListener() {
                    @Override
                    public void onAttention() {
                        TextView textView = (TextView) view;
                        textView.setText("已关注");
                        textView.setCompoundDrawables(null, null, null, null);
                    }
                });
                break;
            case R.id.tv_natural_item_leave_message:
                if (entity2.getItemType()==21){
                    //               SendCommentDialog.create(getActivity()).beginShow();
                    intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity2.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                    LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity2.getFields();
                    String itemData = JSON.toJSONString(linkedHashMap);
                    LogUtils.d("linkedHashMap: " + itemData);
                    intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                    startActivity(intent);
                }else {
                    //               SendCommentDialog.create(getActivity()).beginShow();
                    intent = new Intent(getActivity(), DynamicVideoDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity2.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                    LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity2.getFields();
                    String itemData = JSON.toJSONString(linkedHashMap);
                    LogUtils.d("linkedHashMap: " + itemData);
                    intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                    startActivity(intent);
                }

                break;
            case R.id.tv_natural_item_like_image:
                String dynamicId = entity2.getField(EntityKeys.DYNAMIC_ID);
                LikeHandler.create(getActivity()).like(peopleId2, dynamicId, new LikeHandler.ILikeListener() {
                    @Override
                    public void onLike() {
                        ImageView textView = (ImageView) view;
                        textView.setSelected(true);
                        entity2.setField(EntityKeys.TYPE_LIKE,"1");
                        Animation animation;
                        animation= AnimationUtils.loadAnimation(getContext(),R.anim.myscale);
                        textView.startAnimation(animation);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                try {
                                    mAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                }
                                try {
                                    Adapter.notifyDataSetChanged();
                                }catch (Exception e){
                                }
                            }
                        }, 1000);


                    }
                });

                break;
            case R.id.tv_natural_item_give_a_reward_friend:
                GiveARewardDialog.create(getActivity()).beginShow(peopleId);
                break;
//            case R.id.tv_natural_item_share:
//                BaseEntity entity = moodAdapter.getData().get(position);
//                //分享
//                ShareUtils.create(getActivity())
//                        .setText("心情分享")
//                        .setImgUrl( entity.getField(EntityKeys.IMG_PATH))
//                        .setTitleUrl(entity.getField(EntityKeys.IMG_PATH))
//                        .setUrl(entity.getField(EntityKeys.IMG_PATH))
//                        .showDialog();
//
//
//            break;
            case R.id.iv_natural_item_head_friend:
                Intent intentfriend = new Intent(getActivity(), NearbyDetailActivity.class);
                intentfriend.putExtra(ContentKeys.ACTIVITY_ID_ONLY, DataConverters.ENTITIES.get(position).getField(EntityKeys.PEOPLE_ID));
                getActivity().startActivity(intentfriend);
                break;
            case R.id.tv_natural_item_attention_friend:
                AttentionHandler.create(getActivity()).attention(peopleId, new AttentionHandler.IAttentionListener() {
                    @Override
                    public void onAttention() {
                        TextView textView = (TextView) view;
                        textView.setText("已关注");
                        textView.setCompoundDrawables(null, null, null, null);
                    }
                });
                break;
            case R.id.tv_natural_item_leave_message_friend:
                if (entity.getItemType()==21){
                    //               SendCommentDialog.create(getActivity()).beginShow();
                    intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                    LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
                    String itemData = JSON.toJSONString(linkedHashMap);
                    LogUtils.d("linkedHashMap: " + itemData);
                    intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                    startActivity(intent);
                }else {
                    //               SendCommentDialog.create(getActivity()).beginShow();
                    intent = new Intent(getActivity(), DynamicVideoDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                    LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
                    String itemData = JSON.toJSONString(linkedHashMap);
                    LogUtils.d("linkedHashMap: " + itemData);
                    intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                    startActivity(intent);
                }

                break;
            case R.id.tv_natural_item_like_image_friend:
                String dynamicId2 = entity.getField(EntityKeys.DYNAMIC_ID);
                LikeHandler.create(getActivity()).like(peopleId, dynamicId2, new LikeHandler.ILikeListener() {
                    @Override
                    public void onLike() {
                        ImageView textView = (ImageView) view;
                        textView.setSelected(true);
                        entity.setField(EntityKeys.TYPE_LIKE,"1");
                        Animation animation;
                        animation= AnimationUtils.loadAnimation(getContext(),R.anim.myscale);
                        textView.startAnimation(animation);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                try {
                                    mAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                }
                                try {
                                    Adapter.notifyDataSetChanged();
                                }catch (Exception e){
                                }
                            }
                        }, 1000);


                    }
                });

                break;
            default:
                break;
        }
    }

    private void join_Circle(final int pos) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put("circleid", mDataConverter.ENTITIES.get(pos).getField("id"));
        LogUtils.i("circleid", mDataConverter.ENTITIES.get(pos).getField("id"));
        mUrl = UrlKeys.CIRCLE_JOIN;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.ENTITIES.get(pos).setField("state", "1");
                        mCircleAdapter.notifyItemChanged(pos);
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), CircleListActivity.class);
                        intent.putExtra("id", mDataConverter.ENTITIES.get(pos).getField("id"));
                        intent.putExtra("imagepath", mDataConverter.ENTITIES.get(pos).getField("images"));
                        startActivity(intent);

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        LogUtils.d("error");
                        ToastUtils.showText(getActivity(), message);

                    }
                })
                .build().post();
    }


    @Override
    public void onPreview(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getActivity())
                .saveImgDir(downloadDir); // 保存图片的目录，如果传 null，则没有保存图片功能

        if (ninePhotoLayout.getItemCount() == 1) {
            // 预览单张图片
            photoPreviewIntentBuilder.previewPhoto(ninePhotoLayout.getCurrentClickItem());
        } else if (ninePhotoLayout.getItemCount() > 1) {
            // 预览多张图片
            photoPreviewIntentBuilder.previewPhotos(ninePhotoLayout.getData())
                    .currentPosition(ninePhotoLayout.getCurrentClickItemPosition()); // 当前预览图片的索引
        }
        startActivity(photoPreviewIntentBuilder.build());
    }
    private void setEvent(){
        refreshBase.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData2();
                        refreshBase.setRefreshing(false);
                    }
                },2000);
            }
        });

        recData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPos = mManager.findLastVisibleItemPosition();
                if (lastPos+1==mAdapter.getItemCount()){
                    if (refreshBase.isRefreshing()) {
                        return;
                    }
                    if (!isLoading){
                        isLoading = true;
                        loadData4(page);

                    }
                }
            }
        });
    }
    private void setEvent2(){
        refreshBase.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData3();
                        refreshBase.setRefreshing(false);
                    }
                },2000);
            }
        });

        recData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPos = mManager.findLastVisibleItemPosition();
                if (lastPos+1==Adapter.getItemCount()){
                    if (refreshBase.isRefreshing()) {
                        return;
                    }
                    if (!Loading&&time==2&&dy>50){
                        Loading = true;
                        loadData5(page);

                    }
                }
            }
        });
    }
    private void setEvent3(){
        refreshBase.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                        refreshBase.setRefreshing(false);
                    }
                },2000);
            }
        });

        recData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
    private void setEventAttention(){
        refreshBase.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDataMyAttention();
                        refreshBase.setRefreshing(false);
                    }
                },2000);
            }
        });

        recData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }
}
