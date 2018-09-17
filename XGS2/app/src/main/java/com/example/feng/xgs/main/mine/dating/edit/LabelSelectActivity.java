package com.example.feng.xgs.main.mine.dating.edit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
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
import com.example.feng.xgs.main.mine.dating.DatingActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by feng on 2018/5/11 0011.
 * 选择标签
 */

public class LabelSelectActivity extends EmptyActivity implements BaseQuickAdapter.OnItemChildClickListener {


    //选中的position
    private int mPositionCurrent = -1;


    private LabelSelectDataConverter mDataConverter;
    private LabelSelectAdapter mAdapter;
    private StringBuilder mBuilderResult = new StringBuilder();

    //是否单选type, 是否添加 "自定义标签" 布局
    private boolean mIsSelectOnly, mIsCustomShow;

    @BindView(R.id.rv_label_select)RecyclerView mRecyclerView;
    private String mInfoLast;
    private String mIdOnly;

    @OnClick(R.id.tv_toolbar_save)void onClickSave(){
        save();
    }



    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void emptyLoadData() {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public Object setLayout() {
        return R.layout.activity_mine_dating_label_select;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String title = intent.getStringExtra(ContentKeys.ACTIVITY_TITLE);
        mIdOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        setTitle(title);

        mInfoLast = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        mIsCustomShow = intent.getBooleanExtra(ContentKeys.IS_DATING_LABEL_CUSTOM_SHOW, false);
        mIsSelectOnly = intent.getBooleanExtra(ContentKeys.IS_DATING_LABEL_SELECT_ONLY, false);

        initRecyclerView();
        loadData();
    }

    private void initRecyclerView(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);

        mDataConverter = new LabelSelectDataConverter();
        mAdapter = new LabelSelectAdapter(mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(this);
    }


    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.LABEL_ID, mIdOnly);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.LABEL_SECOND)
                .raw(raw)
                .loader(this)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearData();
                        List<BaseEntity> entities = mDataConverter
                                .setShowHead(mIsCustomShow)
                                .setEditorInfo(mInfoLast)
                                .setJsonData(response)
                                .convert();

                        mPositionCurrent = mDataConverter.getSelectPosition();
                        mAdapter.setNewData(entities);

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {

                    }
                })
                .build().post();
//        mAdapter.setNewData(mDataConverter.setShowHead(mIsCustomShow).setEditorInfo(mInfoLast).convert());
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if(view.getId() == R.id.tv_label_select_head){
            Intent intent = new Intent(this, AddCustomLabelActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mIdOnly);
            startActivityForResult(intent, CodeKeys.ADD_REQUEST);
        }else if(view.getId() == R.id.tv_label_select_item){
            BaseEntity entity = mAdapter.getData().get(position);
            //判断是单选还是多选
            if(mIsSelectOnly){

                for (int i = 0; i < mAdapter.getData().size(); i++) {
                    BaseEntity entityItem = mAdapter.getData().get(i);
                    entityItem.setField(EntityKeys.TYPE, ContentKeys.NORMAL);
                }
                entity.setField(EntityKeys.TYPE, ContentKeys.SELECT);
                mAdapter.notifyDataSetChanged();
//                if(mPositionCurrent != -1){
//                    mAdapter.getData().get(mPositionCurrent).setField(EntityKeys.TYPE, ContentKeys.NORMAL);
//                    mAdapter.notifyItemChanged(mPositionCurrent);
//                }
                mPositionCurrent = position;

            }else {
                String type = entity.getField(EntityKeys.TYPE);
                if(ContentKeys.SELECT.equals(type)){
                    entity.setField(EntityKeys.TYPE, ContentKeys.NORMAL);
                }else {
                    entity.setField(EntityKeys.TYPE, ContentKeys.SELECT);
                }
                mAdapter.notifyItemChanged(position);
            }


        }

    }

    private void save(){
        List<BaseEntity> entities = mAdapter.getData();
        mBuilderResult.delete(0, mBuilderResult.length());
        for (int i = 0; i < entities.size(); i++) {
            String type = entities.get(i).getField(EntityKeys.TYPE);
            if(ContentKeys.SELECT.equals(type)){
                mBuilderResult.append("," + entities.get(i).getField(EntityKeys.NAME));
            }
        }

        String labelResult = mBuilderResult.toString();
        if(TextUtils.isEmpty(labelResult) || labelResult.length() < 1){
            ToastUtils.showText(this, "请选择标签");
            return;
        }

        String result = labelResult.substring(1);

        if(mIdOnly.equals("84")){
            Intent intent = new Intent(this, EditQuestionActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_INFO, result);
            startActivityForResult(intent, CodeKeys.DATING_QUESTION_REQUEST);
        }else {
            Intent intent = new Intent(this, DatingActivity.class);
            intent.putExtra(ContentKeys.ACTIVITY_INFO, result);
            setResult(CodeKeys.FOR_RESULT_CODE, intent);

            finish();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == CodeKeys.FOR_RESULT_CODE){
            if(requestCode == CodeKeys.ADD_REQUEST && data != null){
                String info = data.getStringExtra(ContentKeys.ACTIVITY_INFO);
                BaseEntity entity = BaseEntity.builder()
                        .setField(EntityKeys.NAME, info)
                        .setField(EntityKeys.TYPE, ContentKeys.NORMAL)
                        .setItemType(ItemTypeKeys.DATING_LABEL_SELECT_ITEM)
                        .build();
                mAdapter.addData(1, entity);

            }

            if(requestCode == CodeKeys.DATING_QUESTION_REQUEST && data != null){
                String question = data.getStringExtra(ContentKeys.ACTIVITY_INFO);
                String answer = data.getStringExtra(ContentKeys.ACTIVITY_ANSWER);
                Intent intent = new Intent(this, DatingActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_INFO, question);
                intent.putExtra(ContentKeys.ACTIVITY_ANSWER, answer);
                setResult(CodeKeys.FOR_RESULT_CODE, intent);
                finish();
                overridePendingTransition(0,0);
            }
        }
    }
}
