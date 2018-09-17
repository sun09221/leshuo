package com.example.feng.xgs.main.area.model.list;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.area.model.detail.ModelDetailActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by feng on 2018/5/31 0031.
 * 学员列表
 */

public class StudentListFragment extends EmptyFragment implements BaseQuickAdapter.OnItemClickListener {

    private String mAreaId;

    public static StudentListFragment create(String idOnly){
        Bundle args = new Bundle();
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, idOnly);
        StudentListFragment fragment = new StudentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;
    private StudentAdapter mAdapter;
    private StudentDataConverter mDataConverter;

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
        return R.layout.include_recycler_view;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(manager);
//        addDivider(10);
        mDataConverter = new StudentDataConverter();
        mAdapter = new StudentAdapter( mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void loadData(){
        Bundle bundle = getArguments();
        if(bundle == null) return;
        mAreaId = bundle.getString(ContentKeys.ACTIVITY_ID_ONLY);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.AREA_ID, mAreaId);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.FIND_STUDENT_LIST)
                .raw(raw)
                .loader(getContext())
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
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);
                    }
                })
                .build().post();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(adapter.getItemViewType(position) == ItemTypeKeys.LIST_ITEM){
            Intent intent = new Intent(getActivity(), ModelDetailActivity.class);
            BaseEntity entity = mAdapter.getData().get(position);
            SharedPreferences share=null;
            share=getContext().getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor edit=share.edit();
            edit.putString(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
            edit.putString(ContentKeys.ACTIVITY_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
            edit.putString(ContentKeys.ACTIVITY_AREA_ID, mAreaId);
            edit.putInt(ContentKeys.ACTIVITY_TYPE, CodeKeys.MODEL_OTHER);
            edit.putString(ContentKeys.ACTIVITY_TITLE, entity.getField(EntityKeys.NAME));
            edit.commit();
            intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.ID_ONLY));
            intent.putExtra(ContentKeys.ACTIVITY_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
            intent.putExtra(ContentKeys.ACTIVITY_AREA_ID, mAreaId);
            intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.MODEL_OTHER);
            intent.putExtra(ContentKeys.ACTIVITY_TITLE, entity.getField(EntityKeys.NAME));
            startActivity(intent);
        }

    }
}
