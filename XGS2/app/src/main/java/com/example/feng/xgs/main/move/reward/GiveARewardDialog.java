package com.example.feng.xgs.main.move.reward;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
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
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by feng on 2018/5/2 0002.
 * 打赏
 */

public class GiveARewardDialog implements BaseQuickAdapter.OnItemClickListener {

    private Activity mActivity;
    private AlertDialog mDialog;
    private RewardAdapter mAdapter;
    private RewardDataConverter mDataConverter;
    private String mPeopleId;

    private GiveARewardDialog(Activity activity){
        this.mActivity = activity;
        mDialog = new AlertDialog.Builder(activity).create();
    }

    public static GiveARewardDialog create(Activity activity){
        return new GiveARewardDialog(activity);
    }

    public void beginShow(String peopleId){
        mDialog.show();
        this.mPeopleId = peopleId;

        Window window = mDialog.getWindow();
        if(window != null){
            window.setContentView(R.layout.dialog_natural_give_a_reward);
            window.setWindowAnimations(R.style.dialog_bottom_animation);
            window.setGravity(Gravity.BOTTOM);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
//            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            params.dimAmount = 0.5f;
            window.setAttributes(params);


            initRecyclerView(window);
            loadData();

        }
    }

    private void initRecyclerView(Window window){
        RecyclerView recyclerView = window.findViewById(R.id.rv_dialog_reward);
        GridLayoutManager manager = new GridLayoutManager(mActivity, 4);
        recyclerView.setLayoutManager(manager);
        mDataConverter = new RewardDataConverter();
        mAdapter = new RewardAdapter(R.layout.item_dialog_reward, mDataConverter.ENTITIES);
        recyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(this);
    }

    private void loadData(){

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.REWARD_LIST)
                .raw(raw)
                .loader(mActivity)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {

                    }
                })
                .build().post();
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        BaseEntity entity = mAdapter.getData().get(position);
        String price = entity.getField(EntityKeys.PRICE);
        String rewardId = entity.getField(EntityKeys.ID_ONLY);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        map.put(ParameterKeys.REWARD_ID, rewardId);
        map.put(ParameterKeys.PRICE, price);
        map.put(ParameterKeys.REWARD_PEOPLE_ID, mPeopleId);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.REWARD)
                .raw(raw)
                .toast(mActivity)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        ToastUtils.showText(mActivity, "打赏成功");
                        mDialog.cancel();
                    }
                })
                .build().post();
    }

}
