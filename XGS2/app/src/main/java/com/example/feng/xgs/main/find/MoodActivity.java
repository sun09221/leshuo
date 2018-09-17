package com.example.feng.xgs.main.find;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.ShareUtils;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.circle.MoodAdapter;
import com.example.feng.xgs.main.circle.MoodViewConverter;
import com.example.feng.xgs.utils.manager.LogUtils;

public class MoodActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemChildClickListener {
    private ImageView ivToolbarBack;
    private TextView tvToolbarTitle;
    private RecyclerView recData;
    private SwipeRefreshLayout refreshBase;
    private TextView tv_empty_no_data;
    private Handler handler = new Handler();
    MoodViewConverter moodViewConverter;
    private MoodAdapter moodAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        init();
        tvToolbarTitle.setText("心情视图");
        setEvent();
        refreshBase.setVisibility(View.GONE);
        recData.setVisibility(View.GONE);
        tv_empty_no_data.setVisibility(View.VISIBLE);
        initRecViewsmood();
        loadDatamood();
        ivToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void init() {
        ivToolbarBack = findViewById(R.id.iv_toolbar_back);
        tvToolbarTitle = findViewById(R.id.tv_toolbar_title);
        recData = findViewById(R.id.rec_data);
        refreshBase = findViewById(R.id.refresh_base);
        tv_empty_no_data = findViewById(R.id.tv_empty_no_data);
    }
    private void setEvent(){
        refreshBase.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadDatamood();
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
    private void initRecViewsmood() {
        LinearLayoutManager manager = new LinearLayoutManager(getApplication());
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

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_natural_item_share:
                BaseEntity entity = moodAdapter.getData().get(position);
                //分享
                ShareUtils.create(this)
                        .setText("心情分享")
                        .setImgUrl( entity.getField(EntityKeys.IMG_PATH))
                        .setTitleUrl(entity.getField(EntityKeys.IMG_PATH))
                        .setUrl(entity.getField(EntityKeys.IMG_PATH))
                        .showDialog();
                         break;
            default:
                break;
        }
    }
}
