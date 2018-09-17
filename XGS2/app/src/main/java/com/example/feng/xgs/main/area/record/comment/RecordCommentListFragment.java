package com.example.feng.xgs.main.area.record.comment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/6/7 0007.
 * 留言记录
 */

public class RecordCommentListFragment extends EmptyFragment{


    private RecordCommentDataConverter mDataConverter;
    private RecordCommentAdapter mAdapter;
    private String mModelId,mDynamicId;
    private int mType;
    private String sendString;

    public static RecordCommentListFragment create(int type, String modelId){
        Bundle args = new Bundle();
        args.putInt(ContentKeys.ACTIVITY_TYPE, type);
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, modelId);
        RecordCommentListFragment fragment = new RecordCommentListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_base)RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)SwipeRefreshLayout mRefresh;
//    @BindView(R.id.et_dialog_send_content)EditText send_content;
//    @BindView(R.id.tv_dialog_send_sure)TextView send_sure;


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
        return R.layout.include_recycle_view;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {
        Bundle args = getArguments();
        if(args == null) return;
        mType = args.getInt(ContentKeys.ACTIVITY_TYPE);
        if(mType == CodeKeys.RECORD_MODEL){
            mModelId = args.getString(ContentKeys.ACTIVITY_ID_ONLY);
        }
        mDynamicId=SharedPreferenceUtils.getCustomAppProfile2(ShareKeys.PEOPLE);
        initRefreshLayout(mRefresh);
        initRecyclerView();
        loadData();
//        SendCommentDialog2.create(getActivity()).beginShow(mDynamicId,mModelId, new IDialogListener() {
//            @Override
//            public void onSure() {
//                loadData();
//            }
//        });
//        send_sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                sendComment();
//            }
//        });
    }
//    private void sendComment(){
//        String content = send_content.getText().toString();
//        try {
//            sendString = URLEncoder.encode(content,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        if (TextUtils.isEmpty(content)) {
//            ToastUtils.showText(getContext(), "请输入评论内容");
//            return;
//        }
//
//        Map<String, String> map = new HashMap<>();
//        map.put(ParameterKeys.MODEL_ID, mModelId);
//        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
//        map.put(ParameterKeys.CONTENT, sendString);
//
//        String raw = JSON.toJSONString(map);
//        RestClient.builder()
//                .url(UrlKeys.FIND_MODEL_COMMENT)
//                .raw(raw)
//                .loader(getContext())
//                .toast()
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//                        ToastUtils.showText(getContext(), "评论成功");
//                        ((TextView) send_content).setText("");
//                        loadData();
//                    }
//                })
//                .build().post();
//    }
    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        addDivider();

        mDataConverter = new RecordCommentDataConverter();
        mAdapter = new RecordCommentAdapter(R.layout.item_mine_record_comment, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        String url = UrlKeys.AREA_RECORD_COMMENT;
        if(mType == CodeKeys.RECORD_MODEL){
            map.put(ParameterKeys.MODEL_ID, mModelId);
        }else if(mType == CodeKeys.RECORD_MINE){
            url = UrlKeys.AREA_RECORD_COMMENT_MINE;
            map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        }else {
            return;
        }

        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(url)
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
}
