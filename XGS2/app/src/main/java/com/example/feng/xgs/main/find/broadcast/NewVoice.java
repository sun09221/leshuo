package com.example.feng.xgs.main.find.broadcast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.circle.NewVoiceAdapter;
import com.example.feng.xgs.main.circle.TitleAdapter;
import com.example.feng.xgs.main.circle.VoiceDataFriendConverter;
import com.example.feng.xgs.main.circle.VoiceDataNewConverter;
import com.example.feng.xgs.main.circle.VoiceDataZanConverter;
import com.example.feng.xgs.main.circle.VoiceTitleDataConverter;
import com.example.feng.xgs.main.move.handler.LikeHandler;
import com.example.feng.xgs.main.nearby.detail.NearbyDetailActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewVoice extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener , BaseQuickAdapter.OnItemChildClickListener {
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private ImageView voicesend;
    private RecyclerView recTitle;
    private RecyclerView recData;
    private SwipeRefreshLayout refreshBase;
    private NewVoiceAdapter mCircleAdapter;
    private TitleAdapter mtitleAdapter;
    private LinearLayoutManager mManager;
    private LinearLayoutManager mTitleManager;
    private String parentLevel = "";
    private String praiseNum;
    private String zan;
    private TextView tv_empty_no_data;
    private ImageView toolbar_back;
    private int praiseNums;
    private int page = 2;
    VoiceTitleDataConverter mDataConverter;
    VoiceDataZanConverter mDataConverterZan;
    VoiceDataNewConverter mDataConverterNew;
    VoiceDataFriendConverter mDataConverterFriend;
    private List<BaseEntity> ENTITIE;
    private TextView textView;
    private boolean isLoading = false;    //设置是否处于上啦加载状态
    private Handler handler = new Handler();
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 0x10:
                    //   loadData();
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_voice);
        init();
        tvToolbarTitle.setText("新作榜");
        initRefreshLayout(refreshBase);
        initTitleRecViews();
        setSelect(0);
        refreshBase.setVisibility(View.GONE);
        recData.setVisibility(View.GONE);
        tv_empty_no_data.setVisibility(View.VISIBLE);
        initRecViews();
        loadData();
        setEvent1();
        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        voicesend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NewVoice.this,MusicPublish.class);
                startActivity(intent);
            }
        });
    }

    private void initTitleRecViews() {
        mTitleManager = new LinearLayoutManager(this);
        mTitleManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recTitle.setLayoutManager(mTitleManager);
        mtitleAdapter = new TitleAdapter(R.layout.item_title_voice, ENTITIE);
        recTitle.setAdapter(mtitleAdapter);

        mtitleAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                setSelect(i);
                baseQuickAdapter.notifyDataSetChanged();
                parentLevel = ENTITIE.get(i).getField("id");
                if (parentLevel.equals("222")) {
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    isLoading = false;
                    page=2;
                    setEvent2();
                    initRecViewsZan();
                    loadDataZan();
                } else if (parentLevel.equals("333")) {
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    isLoading = false;
                    page=2;
                    setEvent3();
                    initRecViewsNew();
                    loadDataNew();
                } else if (parentLevel.equals("444")) {
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    isLoading = false;
                    page=2;
                    setEvent4();
                    initRecViewsFriend();
                    loadDataFriend();
                } else {
                    refreshBase.setVisibility(View.GONE);
                    recData.setVisibility(View.GONE);
                    tv_empty_no_data.setVisibility(View.VISIBLE);
                    isLoading = false;
                    page=2;
                    setEvent1();
                    initRecViews();
                    loadData();
                }
            }


        });
    }

    private void init() {
        ivToolbarBack = findViewById(R.id.iv_toolbar_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        voicesend = findViewById(R.id.iv_toolbar_right);
        recTitle = findViewById(R.id.rec_title);
        recData = findViewById(R.id.rec_data);
        refreshBase = findViewById(R.id.refresh_base);
        toolbar_back = findViewById(R.id.iv_toolbar_back);
        tv_empty_no_data = findViewById(R.id.tv_empty_no_data);
        ENTITIE = new ArrayList<>();
        BaseEntity entity1 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "全部")
                .setField("id", "111")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "全部")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIE.add(entity1);
        BaseEntity entity2 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "点赞最多")
                .setField("id", "222")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "点赞最多")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIE.add(entity2);
        BaseEntity entity3 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "最新发布")
                .setField("id", "333")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "最新发布")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIE.add(entity3);
        BaseEntity entity4 = BaseEntity.builder()
                .setField(EntityKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId())
//                    .setField(EntityKeys.DYNAMIC_ID, dynamicId)
                .setField("createtime", "")
                .setField("describe", "好友发布")
                .setField("id", "444")
                .setField("peoplenum", "1")
                .setField("images", "")
                .setField("name", "好友发布")
                .setField(EntityKeys.TYPE, "0")
                .build();
        ENTITIE.add(entity4);
    }

    public void initRefreshLayout(SwipeRefreshLayout refreshLayout) {
        refreshLayout.setColorSchemeResources(
                R.color.pink,
                R.color.main,
                R.color.yellow_light
        );
        refreshLayout.setProgressViewOffset(true, 0, 200);
        refreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {

    }

    private void loadData() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
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

    private void loadDataZan() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, "1");
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
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
                        mDataConverterZan.clearData();
                        mCircleAdapter.setNewData(mDataConverterZan.setJsonData(response).convert());

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

    private void loadDataNew() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, "2");
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
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
                        mDataConverterNew.clearData();
                        mCircleAdapter.setNewData(mDataConverterNew.setJsonData(response).convert());

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

    private void loadDataFriend() {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(1));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE_FRIEND;
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
                        mDataConverterFriend.clearData();
                        mCircleAdapter.setNewData(mDataConverterFriend.setJsonData(response).convert());

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

    private void initRecViews() {
        mManager = new LinearLayoutManager(this);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverter = new VoiceTitleDataConverter();
        mCircleAdapter = new NewVoiceAdapter(R.layout.item_all_natural_voice, mDataConverter.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);
    }

    private void initRecViewsZan() {
        mManager = new LinearLayoutManager(this);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverterZan = new VoiceDataZanConverter();
        mCircleAdapter = new NewVoiceAdapter(R.layout.item_all_natural_voice, mDataConverterZan.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);
    }

    private void initRecViewsNew() {
        mManager = new LinearLayoutManager(this);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverterNew = new VoiceDataNewConverter();
        mCircleAdapter = new NewVoiceAdapter(R.layout.item_all_natural_voice, mDataConverterNew.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);
    }

    private void initRecViewsFriend() {
        mManager = new LinearLayoutManager(this);
        recData.setLayoutManager(mManager);
        //  addDivider(10);
        mDataConverterFriend = new VoiceDataFriendConverter();
        mCircleAdapter = new NewVoiceAdapter(R.layout.item_all_natural_voice, mDataConverterFriend.ENTITIES);
        recData.setAdapter(mCircleAdapter);
        mCircleAdapter.setOnItemChildClickListener(this);
    }

    private void setSelect(int pos) {
        ENTITIE.get(pos).setField("type", "1");
        for (int i = 0; i < ENTITIE.size(); i++) {
            if (i != pos) {
                ENTITIE.get(i).setField("type", "0");
            }

        }

    }

    private void setEvent1(){
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

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastPos = mManager.findLastVisibleItemPosition();
                if (lastPos+1==mCircleAdapter.getItemCount()){
                    if (refreshBase.isRefreshing()) {
                        return;
                    }
                    if (!isLoading){
                        isLoading = true;
                        loadDataall(page);

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
                        loadDataZan();
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
                if (lastPos+1==mCircleAdapter.getItemCount()){
                    if (refreshBase.isRefreshing()) {
                        return;
                    }
                    if (!isLoading){
                        isLoading = true;
                        loadDataZan2(page);

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
                        loadDataNew();
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
                if (lastPos+1==mCircleAdapter.getItemCount()){
                    if (refreshBase.isRefreshing()) {
                        return;
                    }
                    if (!isLoading){
                        isLoading = true;
                        loadDataNew2(page);

                    }
                }
            }
        });
    }
    private void setEvent4(){
        refreshBase.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDataFriend();
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
                if (lastPos+1==mCircleAdapter.getItemCount()){
                    if (refreshBase.isRefreshing()) {
                        return;
                    }
                    if (!isLoading){
                        isLoading = true;
                        loadDataFriend2(page);

                    }
                }
            }
        });
    }
    private void loadDataall(int pages) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(pages));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        String raw = JSON.toJSONString(map);
        mUrl = UrlKeys.ALL_VOICE;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        JSONArray array = objectData.getJSONArray("datalist");
                        if (array != null ) {
                            page++;
                            isLoading = false;
                            mCircleAdapter.setNewData(mDataConverter.setJsonData(response).convert());

                        } else {
                            Toast.makeText(NewVoice.this, "没有更多内容了", Toast.LENGTH_LONG).show();
                            // adapter.notifyItemRemoved(adapter.getItemCount());
                        }
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
    private void loadDataZan2(int pages) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, "1");
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(pages));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        String raw = JSON.toJSONString(map);
        mUrl = UrlKeys.ALL_VOICE;
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        JSONArray array = objectData.getJSONArray("datalist");
                        if (array != null ) {
                            page++;
                            isLoading = false;
                            mCircleAdapter.setNewData(mDataConverterZan.setJsonData(response).convert());

                        } else {
                            Toast.makeText(NewVoice.this, "没有更多内容了", Toast.LENGTH_LONG).show();
                            // adapter.notifyItemRemoved(adapter.getItemCount());
                        }
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
    private void loadDataNew2(int pages) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.TYPE, "2");
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(pages));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        JSONArray array = objectData.getJSONArray("datalist");
                        if (array != null ) {
                            page++;
                            isLoading = false;
                            mCircleAdapter.setNewData(mDataConverterNew.setJsonData(response).convert());

                        } else {
                            Toast.makeText(NewVoice.this, "没有更多内容了", Toast.LENGTH_LONG).show();
                            // adapter.notifyItemRemoved(adapter.getItemCount());
                        }

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

    private void loadDataFriend2(int pages) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, String.valueOf(pages));
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZEAALL);
        mUrl = UrlKeys.ALL_VOICE_FRIEND;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject objectData = JSON.parseObject(response);
                        JSONArray array = objectData.getJSONArray("datalist");
                        if (array != null ) {
                            page++;
                            isLoading = false;
                            mCircleAdapter.setNewData(mDataConverterFriend.setJsonData(response).convert());

                        } else {
                            Toast.makeText(NewVoice.this, "没有更多内容了", Toast.LENGTH_LONG).show();
                            // adapter.notifyItemRemoved(adapter.getItemCount());
                        }

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
    public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {
        final BaseEntity entity2 = mCircleAdapter.getData().get(position);
        String peopleId2 = entity2.getField(EntityKeys.PEOPLE_ID);
        switch (view.getId()) {
            case R.id.iv_natural_item_head_layout:
                Intent intent = new Intent(NewVoice.this, NearbyDetailActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, peopleId2);
                startActivity(intent);
                break;
            case R.id.tv_natural_item_likes_image:
                 zan = entity2.getField("zan");
                 if (Integer.parseInt(zan)==1){
                     ToastUtils.showText(NewVoice.this,"已赞过");
                 }else{
                     entity2.setField("zan","1");
                     praiseNum = entity2.getField("praiseNum");
                     praiseNums=Integer.parseInt(praiseNum);
                     LikeHandler.create(this).musiclike(entity2.getField("id"),new LikeHandler.ILikeListener() {
                         @Override
                         public void onLike() {
                             ImageView textView = (ImageView) view;
                             textView.setSelected(true);
                             entity2.setField("zan","2");
                             Animation animation;
                             animation= AnimationUtils.loadAnimation(getApplication(),R.anim.myscale);
                             textView.startAnimation(animation);
                             new Handler().postDelayed(new Runnable(){
                                 public void run() {
                                     try {
                                         mCircleAdapter.notifyDataSetChanged();
                                     }catch (Exception e){
                                     }
                                 }
                             }, 1000);

                         }
                     });
                 }

                break;
            case R.id.tv_natural_item_leave_messages:
                loadDataMusivTime(entity2.getField("id"));
                Intent intent_message = new Intent(NewVoice.this, MusicPlay.class);
                intent_message.putExtra(ContentKeys.ACTIVITY_ID_ONLY, peopleId2);
                intent_message.putExtra(ContentKeys.ACTIVITY_LAMEIMG, entity2.getField("labelimg"));
                intent_message.putExtra(ContentKeys.ACTIVITY_ID, entity2.getField("id"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_HEAD, entity2.getField("header"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_TITLE,entity2.getField("title"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_CREATTIME, entity2.getField("createtime"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_SEX, entity2.getField("sex"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_CONSTELLATION, entity2.getField("starsign"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_MUSICPATH, entity2.getField("musicpath"));
                intent_message.putExtra(ContentKeys.ACTIVITY_MUSIC_LENGTH, entity2.getField("lengthtime"));
                intent_message.putExtra(ContentKeys.ACTIVITY_PEOPLEID, entity2.getField("peopleid"));
                intent_message.putExtra(ContentKeys.ACTIVITY_STATE, entity2.getField("state"));
                startActivityForResult(intent_message, CodeKeys.FOLLOW);
                break;
            case R.id.tv_natural_item_share:
                loadDataMusivTime(entity2.getField("id"));
                Intent intent_message2 = new Intent(NewVoice.this, MusicPlay.class);
                intent_message2.putExtra(ContentKeys.ACTIVITY_ID_ONLY, peopleId2);
                intent_message2.putExtra(ContentKeys.ACTIVITY_LAMEIMG, entity2.getField("labelimg"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_ID, entity2.getField("id"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_HEAD, entity2.getField("header"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_TITLE,entity2.getField("title"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_CREATTIME, entity2.getField("createtime"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_SEX, entity2.getField("sex"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_CONSTELLATION, entity2.getField("starsign"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_MUSICPATH, entity2.getField("musicpath"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_MUSIC_LENGTH, entity2.getField("lengthtime"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_PEOPLEID, entity2.getField("peopleid"));
                intent_message2.putExtra(ContentKeys.ACTIVITY_STATE, entity2.getField("state"));
                startActivityForResult(intent_message2, CodeKeys.FOLLOW);
                break;
            case R.id.playmusic:
                loadDataMusivTime(entity2.getField("id"));
                Intent intent_message3 = new Intent(NewVoice.this, MusicPlay.class);
                intent_message3.putExtra(ContentKeys.ACTIVITY_ID_ONLY, peopleId2);
                intent_message3.putExtra(ContentKeys.ACTIVITY_LAMEIMG, entity2.getField("labelimg"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_ID, entity2.getField("id"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_HEAD, entity2.getField("header"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_TITLE,entity2.getField("title"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_CREATTIME, entity2.getField("createtime"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_SEX, entity2.getField("sex"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_CONSTELLATION, entity2.getField("starsign"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_MUSICPATH, entity2.getField("musicpath"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_MUSIC_LENGTH, entity2.getField("lengthtime"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_PEOPLEID, entity2.getField("peopleid"));
                intent_message3.putExtra(ContentKeys.ACTIVITY_STATE, entity2.getField("state"));
                startActivityForResult(intent_message3, CodeKeys.FOLLOW);
                break;
            default:
                break;
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == CodeKeys.FOLLOW){
                setSelect(0);
                loadData();
                initRecViews();
        }
    }
    private void loadDataMusivTime(String id) {
        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.MUSIC_ID ,id);
        mUrl = UrlKeys.COMMENT_MUSICTIME;
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();
        builder.url(mUrl)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

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

}