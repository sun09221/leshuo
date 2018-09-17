package com.example.feng.xgs.main.move.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.EmptyActivity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.ShareUtils;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.handler.AttentionHandler;
import com.example.feng.xgs.main.move.handler.LikeHandler;
import com.example.feng.xgs.main.move.handler.SendCommentDialog;
import com.example.feng.xgs.main.move.reward.GiveARewardDialog;
import com.example.feng.xgs.main.nearby.complaint.ComplaintDialog;
import com.example.feng.xgs.main.nearby.detail.NearbyDetailActivity;
import com.example.feng.xgs.ui.dialog.IDialogListener;
import com.example.feng.xgs.utils.ArrayToListUtil;
import com.example.feng.xgs.utils.manager.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by feng on 2018/4/28 0028.
 * 动态详情、本色详情(图片)
 */

public class DynamicDetailActivity extends EmptyActivity implements   BaseQuickAdapter.OnItemChildClickListener,View.OnClickListener, BGANinePhotoLayout.Delegate {

    @BindView(R.id.rv_dynamic_detail)
    RecyclerView mRecyclerView;
    private DynamicDetailAdapter mAdapter;
    private DynamicDetailDataConverter mDataConverter;
    //评论点赞
    private TextView mTvAttention, mTvLike;
    private ImageView mgeLike;
    //头像
    private ImageView mIvHead;
    //个人信息
    private TextView mTvName, mTvContent, mTvCircle;
    private BGANinePhotoLayout mPhotoLayout;
    private String mPeopleId, mDynamicId;

    private TextView mTvCommentNormal, mTvCommentTitle ,tv_dynamic_detail_head_number_title;

    private ArrayList<String> mImgList;
    private String mInfo;
    private String mImageUrls;

    @OnClick(R.id.iv_toolbar_share)
    void onClickShare() {
        if (mImgList != null && mImgList.size() > 0) {
            ShareUtils.create(this)
                    .setText(mInfo)
                    .setImgUrl(mImgList.get(0))
                    .setTitleUrl(UrlKeys.BASE_NAME + UrlKeys.DYNAMIC_SHARE + "dynamicid=" + mDynamicId + "&peopleid=" + mPeopleId)
                    .setUrl(UrlKeys.BASE_NAME + UrlKeys.DYNAMIC_SHARE + "dynamicid=" + mDynamicId + "&peopleid=" + mPeopleId)
                    .showDialog();
        }


    }


    @OnClick(R.id.iv_toolbar_more)
    void onClickMore() {
        ComplaintDialog.create(this).beginShow(mPeopleId);
    }


    @Override
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void emptyLoadData() {
        loadDataHead();
    }

    @Override
    public void onRefresh() {
        loadDataHead();
    }

    @Override
    public Object setLayout() {
        return R.layout.activity_dynamic_detail;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {

        initRecyclerView();
        loadDataHead();
    }
    //点赞

    @Optional
    @OnClick(R.id.tv_natural_item_like_image)void onClickLike(){
        LikeHandler.create(this).like(mPeopleId, mDynamicId,new LikeHandler.ILikeListener() {
            @Override
            public void onLike() {
                mgeLike.setSelected(true);
                mTvLike.setText("已点赞");
                Animation animation;
                animation= AnimationUtils.loadAnimation(getApplication(),R.anim.myscale);
                mgeLike.startAnimation(animation);
            }
        });
    }
    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new DynamicDetailDataConverter();
        mAdapter = new DynamicDetailAdapter(R.layout.item_dynamic_detail_item, mDataConverter.ENTITIES);
        addHeadView();
        addHeadViewDivider();
        mAdapter.setOnItemChildClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData() {

        Map<String, String> map = new HashMap<>();
        map.put(ParameterKeys.DYNAMIC_ID, mDynamicId);
        map.put(ParameterKeys.PAGE_NUMBER, "1");
        map.put(ParameterKeys.PAGE_SIZE, ContentKeys.PAGE_SIZE);
        String raw = JSON.toJSONString(map);

        RestClient.builder()
                .url(UrlKeys.DYNAMIC_DETAIL_COMMENT)
                .loader(this)
                .raw(raw)
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        mDataConverter.clearData();
                        mAdapter.setNewData(mDataConverter.setJsonData(response).convert());
                        setCommentHead();
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String message) {
                        setCommentHead();
                    }
                })
                .build().post();
    }

    /**
     * 当没有评论时，展示暂无评论
     */
    private void setCommentHead() {
        if (mAdapter.getData().size() > 0) {
            mTvCommentTitle.setVisibility(View.VISIBLE);
            tv_dynamic_detail_head_number_title.setVisibility(View.VISIBLE);
            tv_dynamic_detail_head_number_title.setText(mAdapter.getData().size()+"条");
            mTvCommentNormal.setVisibility(View.GONE);
        } else {
            mTvCommentTitle.setVisibility(View.GONE);
            tv_dynamic_detail_head_number_title.setVisibility(View.GONE);
            mTvCommentNormal.setVisibility(View.VISIBLE);
        }
    }


    // ================================== 头布局部分 =============================================

    /**
     * 添加分割线及“评论”文字
     */
    private void addHeadViewDivider() {
        View view = LayoutInflater.from(this).inflate(R.layout.head_dynamic_detail, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.addHeaderView(view, 1);
        mTvCommentNormal = view.findViewById(R.id.tv_dynamic_detail_head_no_comment);
        mTvCommentTitle = view.findViewById(R.id.tv_dynamic_detail_head_comment_title);
        tv_dynamic_detail_head_number_title=view.findViewById(R.id.tv_dynamic_detail_head_number_title);
    }

    /**
     * 添加图片head
     */
    private void addHeadView() {
        View view = LayoutInflater.from(this).inflate(R.layout.item_natural, (ViewGroup) mRecyclerView.getParent(), false);
        mAdapter.addHeaderView(view, 0);

        mPhotoLayout = view.findViewById(R.id.photo_natural_item_look);

        mIvHead = view.findViewById(R.id.iv_natural_item_head);
        mTvName = view.findViewById(R.id.tv_natural_item_name);
        mTvContent = view.findViewById(R.id.tv_natural_item_content);
//        mTvCircle = view.findViewById(R.id.tv_natural_item_circle);

        TextView tvGiveAReward = view.findViewById(R.id.tv_natural_item_give_a_reward);
        TextView tvComment = view.findViewById(R.id.tv_natural_item_leave_message);
        mTvAttention = view.findViewById(R.id.tv_natural_item_attention);
        mTvLike = view.findViewById(R.id.tv_natural_item_like);
        mgeLike = view.findViewById(R.id.tv_natural_item_like_image);
        mgeLike.setOnClickListener(this);
        mTvAttention.setOnClickListener(this);
        tvComment.setOnClickListener(this);
        tvGiveAReward.setOnClickListener(this);
    }


    /**
     * 为头布局绑定数据
     */
    private void bindHeadData(String response) {
        JSONObject object = JSONObject.parseObject(response);
        mInfo = object.getString("info");
        String circle = object.getString("circle");
        String sex = object.getString("sex");
        String name = object.getString("name");
        mImageUrls = object.getString("imgUrlS");
        String attentionType = object.getString("typeAttention");//"state":"状态：0：为未关注 1：已关
        mPeopleId = object.getString("pId");
        String imageUrl = object.getString("imgUrl");
        mDynamicId = object.getString("dynamicId");
        String likeType = object.getString("typeLike");
        String itemType = object.getString("itemType");

        String nameResult = name + "\n" + sex;
        mTvName.setText(nameResult);
        mTvContent.setText(mInfo);
//        mTvCircle.setText(circle);
        loadImg(imageUrl, mIvHead);
        mIvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(DynamicDetailActivity.this, NearbyDetailActivity.class);
                intent1.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mPeopleId);
                startActivity(intent1);
            }
        });

        // ====================点赞关注状态设置 =============
        if (ContentKeys.LIKE_SELECT.equals(likeType)) {
            mgeLike.setSelected(true);
            mTvLike.setText("已点赞");
        } else {
            mgeLike.setSelected(false);
            mTvLike.setText("点赞");
        }
        if (ContentKeys.ATTENTION_SELECT.equals(attentionType)) {
            mTvAttention.setText("已关注");
            mTvAttention.setCompoundDrawables(null, null, null, null);
        } else {
            mTvAttention.setText("关注");
        }

        mImgList = ArrayToListUtil.array2List(mImageUrls);

        if (mImgList != null) {
            mPhotoLayout.setDelegate(DynamicDetailActivity.this);
            mPhotoLayout.setData(mImgList);
        }

        loadData();
    }

    /**
     * 头布局点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_natural_item_give_a_reward:
                //打赏
                GiveARewardDialog.create(this).beginShow(mPeopleId);
                break;
            case R.id.tv_natural_item_attention:
                //关注
                AttentionHandler.create(this).attention(mPeopleId, new AttentionHandler.IAttentionListener() {
                    @Override
                    public void onAttention() {
                        mTvAttention.setText(getString(R.string.attention_already));
                        mTvAttention.setCompoundDrawables(null, null, null, null);
                    }
                });

                break;
            case R.id.tv_natural_item_leave_message:
                //留言
                SendCommentDialog.create(this).beginShow(mDynamicId, new IDialogListener() {
                    @Override
                    public void onSure() {
                        loadData();
                    }
                });
                break;
            case R.id.tv_natural_item_like_image:
                LikeHandler.create(this).like(mPeopleId, mDynamicId, new LikeHandler.ILikeListener() {
                    @Override
                    public void onLike() {
                        mgeLike.setSelected(true);
                        mTvLike.setText("已点赞");
                        Animation animation;
                        animation= AnimationUtils.loadAnimation(getApplication(),R.anim.myscale);
                        mgeLike.startAnimation(animation);
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 加载头布局数据
     */
    private void loadDataHead() {
        Intent intent = getIntent();
        if (intent == null) return;
        String idOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        String info = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        LogUtils.d("linkedHashMap: 传递后" + info);
        LogUtils.d("loadDataHead: " + idOnly);
//        Map<String, String> map = new HashMap<>();
//        map.put(ParameterKeys.ID_ONLY, idOnly);
//        String raw = JSON.toJSONString(map);
        bindHeadData(info);
//        RestClient.builder()
//                .url(UrlKeys.DYNAMIC_DETAIL)
//                .raw(raw)
//                .loader(this)
//                .success(new ISuccess() {
//                    @Override
//                    public void onSuccess(String response) {
//
//                        loadData();
//                    }
//                })
//                .build().post();

    }

    // ================================== 头布局部分结束 =============================================


    /**
     * 图片预览
     */
    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(this)
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


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {


        switch (view.getId()) {

            case R.id.iv_dynamic_detail_item:
                Intent intent1 = new Intent(this, NearbyDetailActivity.class);
                intent1.putExtra(ContentKeys.ACTIVITY_ID_ONLY, mDataConverter.ENTITIES.get(position).getField(EntityKeys.PEOPLE_ID));
                this.startActivity(intent1);
                break;


            default:
                break;
        }
    }
}
