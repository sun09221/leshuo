package com.example.feng.xgs.main.move.detail;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.activitys.ToolbarActivity;
import com.example.feng.xgs.base.recycler.BaseDecoration;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.core.video.LandLayoutVideo;
import com.example.feng.xgs.main.move.handler.AttentionHandler;
import com.example.feng.xgs.main.move.handler.LikeHandler;
import com.example.feng.xgs.main.move.handler.SendCommentDialog2;
import com.example.feng.xgs.main.move.reward.GiveARewardDialog;
import com.example.feng.xgs.main.nearby.complaint.ComplaintDialog;
import com.example.feng.xgs.ui.dialog.IDialogListener;
import com.example.feng.xgs.utils.DensityUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.example.feng.xgs.utils.manager.ToastUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * Created by feng on 2018/5/22 0022.
 * 动态详情、本色详情(视频)
 */

public class DynamicVideoDetailActivity extends ToolbarActivity {

    @BindView(R.id.video_natural_item)
    LandLayoutVideo detailPlayer;
    private OrientationUtils orientationUtils;
    private boolean isPause;
    private boolean isPlay;

    @BindView(R.id.rv_dynamic_detail)
    RecyclerView mRecyclerView;
    private DynamicDetailAdapter mAdapter;
    private DynamicDetailDataConverter mDataConverter;
    private String mInfo;
    private String mVideoUrl;
    private String videoImg;
    private TextView tv_dynamic_detail_head_number_title;

    @OnClick(R.id.iv_toolbar_share)
    void onClickShare() {
//        ShareUtils.create(this)
//                .setText(mInfo)
//                .setVideoUrl(UrlKeys.VIDEO_URL_TEST)
//                .showDialog();
        ToastUtils.showText(DynamicVideoDetailActivity.this,"该视频暂不可以分享");
    }

    private void share(){
//        WXVideoObject videoObject = new WXVideoObject();
//        videoObject.videoUrl = mVideoUrl;
//
//        WXMediaMessage message = new WXMediaMessage(videoObject);
//        message.title = "测试";
//        message.description = "视频描述";
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        message.thumbData = ;


        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction =
//        req.message = message;


    }

    @OnClick(R.id.iv_toolbar_more)
    void onClickMore() {
        ComplaintDialog.create(this).beginShow(mPeopleId);
    }

    @BindView(R.id.tv_natural_item_give_a_reward)TextView mTvReward;
    @BindView(R.id.iv_natural_item_head)ImageView mIvHead;
    @BindView(R.id.tv_natural_item_name)TextView mTvName;
    @BindView(R.id.tv_natural_item_attention)TextView mTvAttention;
    @BindView(R.id.tv_natural_item_content)TextView mTvContent;
//    @BindView(R.id.tv_natural_item_circle)TextView mTvCircle;
    @BindView(R.id.tv_natural_item_leave_message)TextView mTvLeaveMessage;
    @BindView(R.id.tv_natural_item_like_image)ImageView mgLike;
    @BindView(R.id.tv_natural_item_like)TextView mTvLike;
    @BindView(R.id.tv_dynamic_detail_head_no_comment)TextView mTvCommentNormal;
    @BindView(R.id.tv_dynamic_detail_head_comment_title)TextView mTvCommentTitle;
    private String mPeopleId, mDynamicId;

    //头像，跳转到个人主页
    @OnClick(R.id.iv_natural_item_head)void onClickHead(){

    }

    //关注
    @OnClick(R.id.tv_natural_item_attention)void onClickAttention(){
        AttentionHandler.create(this).attention(mPeopleId, new AttentionHandler.IAttentionListener() {
            @Override
            public void onAttention() {
                mTvAttention.setText(getString(R.string.attention_already));
                mTvAttention.setCompoundDrawables(null, null, null, null);
            }
        });
    }

    //留言
    @OnClick(R.id.tv_natural_item_leave_message)void onClickLeaveMessage(){
        SendCommentDialog2.create(this).beginShow(mDynamicId, new IDialogListener() {
            @Override
            public void onSure() {
                loadData();
            }
        });
    }

    //点赞
    @Optional
    @OnClick(R.id.tv_natural_item_like_image)void onClickLike(){
        LikeHandler.create(this).like(mPeopleId, mDynamicId,new LikeHandler.ILikeListener() {
            @Override
            public void onLike() {
                mgLike.setSelected(true);
                mTvLike.setText("已点赞");
                Animation animation;
                animation= AnimationUtils.loadAnimation(getApplication(),R.anim.myscale);
                mgLike.startAnimation(animation);
            }
        });
    }

    //打赏
    @OnClick(R.id.tv_natural_item_give_a_reward)void onClickReward(){
        GiveARewardDialog.create(this).beginShow(mPeopleId);
    }


    @Override
    public Object setLayout() {
        return R.layout.activity_dynamic_video_detail;
    }

    @Override
    public void onBindView(Bundle savedInstanceState) {
        loadDataHead();
        initRecyclerView();
//        String url =  "http://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
//        String url =  "";
        tv_dynamic_detail_head_number_title=findViewById(R.id.tv_dynamic_detail_head_number_title);
    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        addDivider();
        mDataConverter = new DynamicDetailDataConverter();
        mAdapter = new DynamicDetailAdapter(R.layout.item_dynamic_detail_item, mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void addDivider(){
        mRecyclerView.addItemDecoration(BaseDecoration.create(DensityUtils.dp2px(this, 1),
                ContextCompat.getColor(this, R.color.background_color)));
    }


    private void loadData(){

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
     * */
    private void setCommentHead(){
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
     * 为头布局绑定数据
     * */
    private void bindHeadData(String response){
        JSONObject object = JSONObject.parseObject(response);
        mInfo = object.getString("info");
        String circle = object.getString("circle");
        String sex = object.getString("sex");
        String name = object.getString("name");
        mVideoUrl = object.getString("imgUrlS");
        String attentionType = object.getString("typeAttention");//"state":"状态：0：为未关注 1：已关
        mPeopleId = object.getString("pId");
        String imageUrl = object.getString("imgUrl");
        mDynamicId = object.getString("dynamicId");
        String likeType = object.getString("typeLike");
        String itemType = object.getString("itemType");
         videoImg = object.getString("videoImg");

        String nameResult = name + "\n" + sex;
        mTvName.setText(nameResult);
        mTvContent.setText(mInfo);
//        mTvCircle.setText(circle);
        loadImg(imageUrl, mIvHead);

        // ====================点赞关注状态设置 =============
        if(ContentKeys.LIKE_SELECT.equals(likeType)){
            mgLike.setSelected(true);
            mTvLike.setText("已点赞");
        }else {
            mgLike.setSelected(false);
            mTvLike.setText("点赞");
        }
        if(ContentKeys.ATTENTION_SELECT.equals(attentionType)){
            mTvAttention.setText("已关注");
            mTvAttention.setCompoundDrawables(null, null, null, null);
        }else {
            mTvAttention.setText("关注");
        }


        loadData();
        initVideo(mVideoUrl);
    }


    /**
     * 加载头布局数据
     * */
    private void loadDataHead() {
        Intent intent = getIntent();
        if(intent == null) return;
        String idOnly = intent.getStringExtra(ContentKeys.ACTIVITY_ID_ONLY);
        String info = intent.getStringExtra(ContentKeys.ACTIVITY_INFO);
        LogUtils.d("linkedHashMap: 传递后" + info);
        LogUtils.d("loadDataHead: " + idOnly);
        bindHeadData(info);
    }

    // ================================== 头布局部分结束 =============================================




    /************************************** 以下为视频播放 **********************************/
    private void initVideo(String url) {
      ImageView  videoImageview=new ImageView(getApplication());
        loadImg(videoImg,videoImageview);
//        //增加封面
//        ImageView imageView = new ImageView(this);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setImageResource(R.mipmap.xxx1);

        resolveNormalVideoUI();

        //外部辅助的旋转，帮助全屏
        orientationUtils = new OrientationUtils(this, detailPlayer);
        //初始化不打开外部的旋转
        orientationUtils.setEnable(false);

        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setThumbImageView(videoImageview)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(url)
                .setCacheWithPlay(false)
                .setVideoTitle(" ")
                .setVideoAllCallBack(mGSYSampleCallBack)
                .setLockClickListener(new LockClickListener() {
                    @Override
                    public void onClick(View view, boolean lock) {
                        if (orientationUtils != null) {
                            //配合下方的onConfigurationChanged
                            orientationUtils.setEnable(!lock);
                        }
                    }
                })
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {
                        Debuger.printfLog(" progress " + progress + " secProgress " + secProgress + " currentPosition " + currentPosition + " duration " + duration);
                    }
                })
                .build(detailPlayer);

        detailPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //直接横屏
                orientationUtils.resolveByClick();

                //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
                detailPlayer.startWindowFullscreen(DynamicVideoDetailActivity.this, true, true);
            }
        });

    }


    private void resolveNormalVideoUI() {
        //增加title
        detailPlayer.getTitleTextView().setVisibility(View.GONE);
        detailPlayer.getBackButton().setVisibility(View.GONE);
    }


    //========================== 视频播放回调 =================================
    private GSYSampleCallBack mGSYSampleCallBack = new GSYSampleCallBack() {
        @Override
        public void onPrepared(String url, Object... objects) {
            Debuger.printfError("***** onPrepared **** " + objects[0]);
            Debuger.printfError("***** onPrepared **** " + objects[1]);
            super.onPrepared(url, objects);
            //开始播放了才能旋转和全屏
            orientationUtils.setEnable(true);
            isPlay = true;
        }

        @Override
        public void onEnterFullscreen(String url, Object... objects) {
            super.onEnterFullscreen(url, objects);
            Debuger.printfError("***** onEnterFullscreen **** " + objects[0]);//title
            Debuger.printfError("***** onEnterFullscreen **** " + objects[1]);//当前全屏player
        }

        @Override
        public void onAutoComplete(String url, Object... objects) {
            super.onAutoComplete(url, objects);
        }

        @Override
        public void onClickStartError(String url, Object... objects) {
            super.onClickStartError(url, objects);
        }

        @Override
        public void onQuitFullscreen(String url, Object... objects) {
            super.onQuitFullscreen(url, objects);
            Debuger.printfError("***** onQuitFullscreen **** " + objects[0]);//title
            Debuger.printfError("***** onQuitFullscreen **** " + objects[1]);//当前非全屏player
            if (orientationUtils != null) {
                orientationUtils.backToProtVideo();
            }
        }
    };

    //========================== 视频播放回调结束 =================================




    //===================== 以下为视频资源释放 ========================
    @Override
    public void onBackPressed() {

        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }

        if (GSYVideoManager.backFromWindowFull(this)) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        getCurPlay().onVideoPause();
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        getCurPlay().onVideoResume(false);
        super.onResume();
        isPause = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isPlay) {
            getCurPlay().release();
        }
        //GSYPreViewManager.instance().releaseMediaPlayer();
        if (orientationUtils != null)
            orientationUtils.releaseListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            detailPlayer.onConfigurationChanged(this, newConfig, orientationUtils, true, true);
        }
    }

    private GSYVideoPlayer getCurPlay() {
        if (detailPlayer.getFullWindowPlayer() != null) {
            return  detailPlayer.getFullWindowPlayer();
        }
        return detailPlayer;
    }
}
