package com.example.feng.xgs.main.nearby.complaint;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.mine.dating.DatingLabelKeys;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by feng on 2018/4/28 0028.
 * 投诉
 */

public class ComplaintDialog implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {

    private Activity mActivity;
    private AlertDialog mDialog;
    private ComplaintAdapter mAdapter;
    private ComplaintDataConverter mDataConverter;
    private String mPeopleId;
    private String mComplaintInfo;//投诉原因

    private ComplaintDialog(Activity activity){
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();
    }

    public static ComplaintDialog create(Activity activity){
        return new ComplaintDialog(activity);
    }

    public void beginShow(String peopleId){
        this.mPeopleId = peopleId;
        mDialog.show();

        final Window window = mDialog.getWindow();
        if(window != null){
            View view = LayoutInflater.from(mActivity).inflate(R.layout.dialog_nearby_complaint, null);
            window.setContentView(view);
            window.setGravity(Gravity.CENTER);
            window.setWindowAnimations(R.style.dialog_scale_animation);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



            final WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);


            final int screenHeight = DensityUtils.getScreenHeight(mActivity);
            Log.d("TYPE", "beginShow: 屏幕高度: " + screenHeight);

            view.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    int height = v.getHeight();
                    Log.d("TYPE", "beginShow: 设置前window高度" + height);
                    if(screenHeight * 2 < height *3){
                        height = screenHeight *2 / 3;
                        params.height = height;
                        window.setAttributes(params);
                    }
                    Log.d("TYPE", "beginShow: 设置后window高度" + height);
                }
            });


            window.findViewById(R.id.tv_dialog_nearby_complaint_cancel).setOnClickListener(this);
            window.findViewById(R.id.tv_dialog_nearby_complaint_sure).setOnClickListener(this);


            initRecyclerView(window);
            loadData();

        }
    }

    private void initRecyclerView(Window window){
        RecyclerView recyclerView = window.findViewById(R.id.rv_dialog_nearby_complaint);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mDataConverter = new ComplaintDataConverter();
        mAdapter = new ComplaintAdapter(R.layout.item_nearby_complaint, mDataConverter.ENTITIES);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.LABEL_ID, DatingLabelKeys.COMPLAINT);
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.LABEL_SECOND)
                .raw(raw)
                .loader(mActivity)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearData();
//                        mDataConverter.setJsonData(response).convert();

                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
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

    private void complaint(){

        if(TextUtils.isEmpty(mComplaintInfo)){
            ToastUtils.showText(mActivity, "请选择投诉原因");
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.USER_iD, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.COMPLAINT_ID, mPeopleId);
        map.put(ParameterKeys.RESON, mComplaintInfo);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.COMPLAINT)
                .raw(raw)
                .loader(mActivity)
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(mActivity, "投诉成功");
                        if(mDialog != null){
                            mDialog.cancel();
                        }
                    }
                })
                .build().post();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dialog_nearby_complaint_cancel:
                mDialog.cancel();
                break;
            case R.id.tv_dialog_nearby_complaint_sure:
                complaint();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        List<BaseEntity> entities = mAdapter.getData();
        for (int i = 0; i < entities.size(); i++) {
            BaseEntity entityItem = entities.get(i);
            if(i == position){
                entityItem.setField(EntityKeys.TYPE, ContentKeys.SELECT);
                mComplaintInfo = entityItem.getField(EntityKeys.NAME);
            }else {
                entityItem.setField(EntityKeys.TYPE, ContentKeys.NORMAL);
            }
        }

        mAdapter.notifyDataSetChanged();
    }
}
