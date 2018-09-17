package com.example.feng.xgs.main.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.PermissionFragment;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.CodeKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.ShareKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.area.model.detail.ModelAuthenticationActivity;
import com.example.feng.xgs.main.area.model.detail.ModelDetailActivity;
import com.example.feng.xgs.main.area.record.RecordActivity;
import com.example.feng.xgs.main.message.PersonListActivity;
import com.example.feng.xgs.main.mine.account.AccountActivity;
import com.example.feng.xgs.main.mine.activity.ActivityActivity;
import com.example.feng.xgs.main.mine.dating.DatingActivity;
import com.example.feng.xgs.main.mine.dynamic.MineDynamicActivity;
import com.example.feng.xgs.main.mine.info.MineInformationActivity;
import com.example.feng.xgs.main.mine.order.OrderActivity;
import com.example.feng.xgs.main.mine.product.ProductActivity;
import com.example.feng.xgs.main.mine.qrcode.QrCodeActivity;
import com.example.feng.xgs.main.mine.setting.SettingActivity;
import com.example.feng.xgs.main.mine.setting.help.HelpActivity;
import com.example.feng.xgs.main.mine.team.TeamActivity;
import com.example.feng.xgs.main.mine.vip.VIPActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by feng on 2018/3/15 0015.
 * 个人中心
 */

public class MineFragment extends PermissionFragment implements BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemClickListener ,SwipeRefreshLayout.OnRefreshListener{


    @BindView(R.id.rv_mine)
    RecyclerView mRecyclerView;
    @BindView(R.id.srl_all)
    SwipeRefreshLayout srl_all;
    private MineAdapter mAdapter;
    private int mRequestCount = 0;


    public static MineFragment create() {
        return new MineFragment();
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onBindView(@Nullable Bundle saveInstanceState, View rootView) {

        initRecyclerView();
        loadData();
    }

    private void initRecyclerView() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 4);
        mRecyclerView.setLayoutManager(manager);
        MineDataConverter dataConverter = new MineDataConverter();
        mAdapter = new MineAdapter(dataConverter.setContext(getActivity()).convert());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setClipToPadding(false);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        srl_all.setOnRefreshListener(this);
    }



    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_mine_head_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.tv_mine_info:
                startActivityForResult(new Intent(getActivity(), MineInformationActivity.class),1);
                break;
            case R.id.tv_mine_name:
                startActivityForResult(new Intent(getActivity(), MineInformationActivity.class),1);
                break;
            case R.id.iv_mine_head:
                startActivityForResult(new Intent(getActivity(), MineInformationActivity.class),1);
                break;
            case R.id.tv_mine_attention:
                startPersonListActivity(getString(R.string.message_attention_me), CodeKeys.MESSAGE_ATTENTION_MY);
                break;
            case R.id.tv_mine_comment:
                startPersonListActivity(getString(R.string.message_comment), CodeKeys.MESSAGE_COMMENT_MY);
                break;
        }

    }

    private void startPersonListActivity(String title, int type) {
        Intent intent = new Intent(getContext(), PersonListActivity.class);
        intent.putExtra(ContentKeys.ACTIVITY_TITLE, title);
        intent.putExtra(ContentKeys.ACTIVITY_TYPE, type);
        startActivity(intent);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        switch (position) {
            case 1:
                boolean isModel = SharedPreferenceUtils.getAppFlag(ShareKeys.IS_MODEL);
                if (isModel) {
                    Intent intent = new Intent(getActivity(), ModelDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_TYPE, CodeKeys.MODEL_PERSON);
                    intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.mine_model));
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, SharedPreferenceUtils.getCustomAppProfile(ShareKeys.MODEL_ID));
                    startActivity(intent);//我是模特
                } else {
                    startActivity(new Intent(getActivity(), ModelAuthenticationActivity.class));//模特认证


                }

                break;
            case 2:
                showDialog();

                break;

//            case 3:
//                startActivity(new Intent(getActivity(), EarningsActivity.class));//收益
//                break;

            case 3:
                startActivity(new Intent(getActivity(), AccountActivity.class));//账户
                break;

            case 4:
                Intent intent = new Intent(getActivity(), MineDynamicActivity.class);
                intent.putExtra(ContentKeys.ACTIVITY_TITLE, getString(R.string.mine_dynamic));
                intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
                startActivity(intent);//我的动态
                break;

            case 5:
                startActivity(new Intent(getActivity(), OrderActivity.class));//订单
                break;

            case 6:
                startActivity(new Intent(getActivity(), TeamActivity.class));//团队
                break;

            case 7:
                startActivity(new Intent(getActivity(), QrCodeActivity.class));//二维码
                break;

            case 8:
                startActivity(new Intent(getActivity(), VIPActivity.class));//升级VIP
                break;

//            case 8:
//                startActivity(new Intent(getActivity(), RechargeActivity.class));//充值打赏
//                break;

            case 10:
                startActivity(new Intent(getActivity(), ProductActivity.class));//我的产品
                break;

            case 11:
                startActivity(new Intent(getActivity(), ActivityActivity.class));//我的活动
                break;

//            case 11:
//                startActivity(new Intent(getActivity(), AuthenticationEnterpriseActivity.class));//企业认证
//
//                break;
            case 12:
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));//企业认证
                break;

            case 13:
                startActivity(new Intent(getActivity(), RecordActivity.class));//我的记录
                break;
            case 15:
                startActivity(new Intent(getActivity(), HelpActivity.class));//新手指引
                break;
            default:
                break;
        }
    }
    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;
    private void showDialog() {
        alert = null;
        builder = new AlertDialog.Builder(getActivity());
        alert = builder
                .setTitle("系统提示：")
                .setMessage("请上传您的真实信息，虚假、违规、不雅信息一经查证将受到平台处罚。")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alert.dismiss();
                        startActivity(new Intent(getActivity(), DatingActivity.class));//交友资料
                    }
                })
                .create();             //创建AlertDialog对象
        alert.show();

    }


    private void loadData() {
        mRequestCount = 0;
        final BaseEntity entity = mAdapter.getData().get(0);

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.ID_ONLY, SharedPreferenceUtils.getPeopleId());
        String raw = JSON.toJSONString(map);
        RestClient.builder()
                .url(UrlKeys.USER_INFO)
                .raw(raw)
//                .loader(getContext())
                .toast()
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        srl_all.setRefreshing(false);
                        JSONObject object = JSON.parseObject(response);
                        String nickName = object.getString("nickname");
                        String imgUrl = object.getString("header");
                        String type = object.getString("type");

                        entity.setField(EntityKeys.NAME, nickName)
                                .setField(EntityKeys.IMG_URL, imgUrl)
                                .setField(EntityKeys.TYPE, type);
                        SharedPreferenceUtils.setCustomAppProfile(ShareKeys.USER_TYPE, type);
                        mAdapter.notifyItemChanged(0);

                    }
                }).build().post();

        Map<String, String> mapComment = new HashMap<>();
        mapComment.put(ParameterKeys.PEOPLE_ID, SharedPreferenceUtils.getPeopleId());
        String rawComment = JSON.toJSONString(mapComment);

        RestClient.builder()
                .url(UrlKeys.COUNT_COMMENT)
                .raw(rawComment)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String count = object.getString("count");
                        entity.setField(EntityKeys.COUNT_COMMENT, count);
                        mAdapter.notifyItemChanged(0);
                    }
                })
                .build().post();

        RestClient.builder()
                .url(UrlKeys.COUNT_LIKE)
                .raw(rawComment)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String count = object.getString("count");
                        entity.setField(EntityKeys.COUNT_LIKE, count);
                        mAdapter.notifyItemChanged(0);
                    }
                })
                .build().post();

        RestClient.builder()
                .url(UrlKeys.COUNT_ATTENTION)
                .raw(rawComment)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        JSONObject object = JSON.parseObject(response);
                        String count = object.getString("count");
                        entity.setField(EntityKeys.COUNT_ATTENTION, count);
                        mAdapter.notifyItemChanged(0);
                    }
                })
                .build().post();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1&&resultCode==1){
            loadData();
        }
    }

    @Override
    public void onRefresh() {

        loadData();
    }
}
