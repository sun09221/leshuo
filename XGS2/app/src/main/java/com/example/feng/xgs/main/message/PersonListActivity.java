package com.example.feng.xgs.main.message;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.chatui.ui.activity.ChatUIActivity;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/5/4 0004.
 * 评论我的、关注我的、我关注的页面
 */

public class PersonListActivity extends EmptyActivity implements BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    private PersonListDataConverter mDataConverter;
    private PersonListAdapter mAdapter;
    private int mType;

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
        return R.layout.activity_list;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mType = intent.getIntExtra(ContentKeys.ACTIVITY_TYPE, -1);
        setTitle(title);
        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new PersonListDataConverter();
        mAdapter = new PersonListAdapter(R.layout.item_message_person_list, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
    }

    private void loadData() {
        String mUrl = null;

        if (mType == CodeKeys.MESSAGE_COMMENT_MY) {
            mUrl = UrlKeys.COMMENT_MY;
        } else if (mType == CodeKeys.MESSAGE_ATTENTION_MY) {
            mUrl = UrlKeys.ATTENTION_MY;
        } else if (mType == CodeKeys.MESSAGE_MY_ATTENTION) {
            mUrl = UrlKeys.MY_ATTENTION;
        } else {
            return;
        }


        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(mUrl)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);
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


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.tv_person_list_item_private_letter:
                Intent intent = new Intent(this, ChatUIActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_TITLE,  mDataConverter.ENTITIES.get(position).getField(EntityKeys.NAME));
                intent.putExtra(ContentKeys.ACTIVITY_IMG_URL, mDataConverter.ENTITIES.get(position).getField(EntityKeys.IMG_URL));
                intent.putExtra(ContentKeys.ACTIVITY_MOBILE, mDataConverter.ENTITIES.get(position).getField("jgusername"));
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
