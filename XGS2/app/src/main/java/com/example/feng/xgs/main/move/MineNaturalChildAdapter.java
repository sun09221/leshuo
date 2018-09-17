package com.example.feng.xgs.main.move;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.recycler.ItemTypeKeys;
import com.example.feng.xgs.base.recycler.adapter.BaseMultipleEntityAdapter;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.core.video.SampleCoverVideo;
import com.example.feng.xgs.main.move.detail.DynamicDetailActivity;
import com.example.feng.xgs.main.move.detail.DynamicVideoDetailActivity;
import com.example.feng.xgs.utils.ArrayToListUtil;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.shuyu.gsyvideoplayer.listener.LockClickListener;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by feng on 2018/4/27 0027.
 */

public class MineNaturalChildAdapter extends BaseMultipleEntityAdapter implements BGANinePhotoLayout.Delegate {

    public static final String TAG_VIDEO = "ListNormalAdapter";
    private OrientationUtils orientationUtils;
    public boolean isPlay;
    public boolean isFull;
    private String videoImage;
    private StandardGSYVideoPlayer curPlayer;
    private ImageView videoImageview;


    public MineNaturalChildAdapter(List<BaseEntity> data) {
        super(data);
        addItemType(ItemTypeKeys.NATURAL_ATTENTION, R.layout.item_mine_natural);
        addItemType(ItemTypeKeys.NATURAL_VIDEO, R.layout.item_mine_natural_video);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BaseEntity entity) {
        int itemType = holder.getItemViewType();

        if(itemType == ItemTypeKeys.NATURAL_ATTENTION || itemType == ItemTypeKeys.NATURAL_VIDEO){
            holder.addOnClickListener(R.id.tv_natural_item_give_a_reward);
            holder.addOnClickListener(R.id.iv_natural_item_head);
         //   holder.addOnClickListener(R.id.tv_natural_item_attention);
            holder.addOnClickListener(R.id.tv_natural_item_leave_message);
            holder.addOnClickListener(R.id.tv_natural_item_like_image);

            String name = entity.getField(EntityKeys.NAME) + "\n" + entity.getField(EntityKeys.SEX);
            videoImage = entity.getField(ParameterKeys.videoImg);
             String type = entity.getField(EntityKeys.TYPE);
             if (type.equals("1")||type.equals("2")){
                 holder.setGone(R.id.tv_nearby_item_vip,true);
             }else{
                 holder.setGone(R.id.tv_nearby_item_vip,false);
             }
            holder.setText(R.id.tv_natural_item_name, name)
                    .setText(R.id.tv_natural_item_content, entity.getField(EntityKeys.INFO));
//                    .setText(R.id.tv_natural_item_circle, entity.getField(EntityKeys.CIRCLE));

            ImageView imageViewHead = holder.getView(R.id.iv_natural_item_head);
            loadImg(entity.getField(EntityKeys.IMG_URL), imageViewHead);

            // ====================点赞关注状态设置 =============
            String likeType = entity.getField(EntityKeys.TYPE_LIKE);
            String zan = entity.getField(EntityKeys.ZAN);
            String attentionType = entity.getField(EntityKeys.TYPE_ATTENTION);
            final TextView tvLike = holder.getView(R.id.tv_natural_item_like);
            final ImageView mgeLike = holder.getView(R.id.tv_natural_item_like_image);
            TextView tvAttention = holder.getView(R.id.tv_natural_item_attention);
            if(ContentKeys.LIKE_SELECT.equals(likeType)){
                mgeLike.setSelected(true);
                tvLike.setText("已点赞");
//                Animation mAnimation = AnimationUtils.loadAnimation(mContext,R.anim.myscale);
//                tvLike.setAnimation(mAnimation);
//                mAnimation.start();
            }else {
                mgeLike.setSelected(false);
                tvLike.setText("点赞");
            }
//            if(ContentKeys.ATTENTION_SELECT.equals(attentionType)){
//                tvAttention.setText("已关注");
//                tvAttention.setCompoundDrawables(null, null, null, null);
//            }else {
//                tvAttention.setText("关注");
//            }
//            mgeLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    tvLike.setText("已点赞");
//                }
//            });

            if(itemType == ItemTypeKeys.NATURAL_VIDEO){
                SampleCoverVideo gsyVideoPlayer = holder.getView(R.id.video_natural_item);
                String videoUrl = entity.getField(EntityKeys.IMG_URL_S);
                initVideo(videoUrl, gsyVideoPlayer, holder.getLayoutPosition());

                //跳转到详情页
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, DynamicVideoDetailActivity.class);
                        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
                        String itemData = JSON.toJSONString(linkedHashMap);
                        LogUtils.d("linkedHashMap: " + itemData);
                        intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                        mContext.startActivity(intent);

                    }
                });

            }else {
                //跳转到详情页
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        int position = holder.getLayoutPosition();
//                        if(position %2 == 0){
                            intent.setClass(mContext, DynamicDetailActivity.class);
//                        }else {
//                            intent.setClass(mContext, DynamicVideoDetailActivity.class);
//                        }
                        intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
                        String itemData = JSON.toJSONString(linkedHashMap);
                        LogUtils.d("linkedHashMap: " + itemData);
                        intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                        mContext.startActivity(intent);

                    }
                });
                String imgUrls = entity.getFieldObject(EntityKeys.IMG_URL_S);
                ArrayList<String> imgList = ArrayToListUtil.array2List(imgUrls);

                if(imgList != null){
                    BGANinePhotoLayout ninePhotoLayout = holder.getView(R.id.photo_natural_item_look);
                    ninePhotoLayout.setDelegate(this);
                    ninePhotoLayout.setData(imgList);
                }
            }

        }


    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        if(mListener != null){
            mListener.onPreview(ninePhotoLayout, view, position, model, models);
        }

    }

    private IOnItemImagePreviewClickListener mListener;

    public interface IOnItemImagePreviewClickListener{
        void onPreview(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models);
    }

    public void setOnItemImagePreviewListener(IOnItemImagePreviewClickListener listener){
        this.mListener = listener;
    }

    private void initVideo(String videoUrl, final SampleCoverVideo gsyVideoPlayer, int position){
        videoImageview=new ImageView(mContext);
        loadImg(videoImage,videoImageview);
        GSYVideoOptionBuilder gsyVideoOption = new GSYVideoOptionBuilder();
        gsyVideoOption.setIsTouchWiget(true)
                .setThumbImageView(videoImageview)
                .setRotateViewAuto(false)
                .setLockLand(false)
                .setShowFullAnimation(false)
                .setNeedLockFull(true)
                .setSeekRatio(1)
                .setUrl(videoUrl)
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
                .build(gsyVideoPlayer);


////        if (icon_find_activity_address % 2 == 0) {
////            holder.gsyVideoPlayer.loadCoverImage(url, R.mipmap.xxx1);
////        } else {
////            holder.gsyVideoPlayer.loadCoverImage(url, R.mipmap.xxx2);
////        }
//
//        //防止错位，离开释放
//        //holder.gsyVideoPlayer.initUIState();
//
//        //默认缓存路径
//        //使用lazy的set可以避免滑动卡的情况存在
//        gsyVideoPlayer.setUpLazy(videoUrl, false, null, null, "这是title");
//
//
//        //增加title
//        gsyVideoPlayer.getTitleTextView().setVisibility(View.GONE);
//
//        //设置返回键
        gsyVideoPlayer.getBackButton().setVisibility(View.GONE);
//
//        //设置全屏按键功能
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resolveFullBtn(gsyVideoPlayer);
            }
        });
//        gsyVideoPlayer.setRotateViewAuto(false);
//        gsyVideoPlayer.setLockLand(false);
//        gsyVideoPlayer.setPlayTag(TAG_VIDEO);
//        gsyVideoPlayer.setReleaseWhenLossAudio(false);
//        gsyVideoPlayer.setShowFullAnimation(false);
//        gsyVideoPlayer.setIsTouchWiget(false);
//        //循环
//        //holder.gsyVideoPlayer.setLooping(true);
//        gsyVideoPlayer.setNeedLockFull(true);
//        //holder.gsyVideoPlayer.setSpeed(2);
//
//        gsyVideoPlayer.setPlayPosition(position);
//
//        gsyVideoPlayer.setVideoAllCallBack(new GSYSampleCallBack() {
//            @Override
//            public void onClickStartIcon(String url, Object... objects) {
//                super.onClickStartIcon(url, objects);
//            }
//
//            @Override
//            public void onPrepared(String url, Object... objects) {
//                super.onPrepared(url, objects);
//                Debuger.printfLog("onPrepared");
//                boolean full = gsyVideoPlayer.getCurrentPlayer().isIfCurrentIsFullscreen();
//                if (!gsyVideoPlayer.getCurrentPlayer().isIfCurrentIsFullscreen()) {
//                    GSYVideoManager.instance().setNeedMute(true);
//                }
//                curPlayer = (StandardGSYVideoPlayer) objects[1];
//                isPlay = true;
//                if (getListNeedAutoLand()) {
//                    //重力全屏工具类
//                    initOrientationUtils(gsyVideoPlayer, full);
//                    NaturalChildAdapter.this.onPrepared();
//                }
//            }
//
//            @Override
//            public void onQuitFullscreen(String url, Object... objects) {
//                super.onQuitFullscreen(url, objects);
//                isFull = false;
//                GSYVideoManager.instance().setNeedMute(true);
//                if (getListNeedAutoLand()) {
//                    NaturalChildAdapter.this.onQuitFullscreen();
//                }
//            }
//
//            @Override
//            public void onEnterFullscreen(String url, Object... objects) {
//                super.onEnterFullscreen(url, objects);
//                GSYVideoManager.instance().setNeedMute(false);
//                isFull = true;
//                gsyVideoPlayer.getCurrentPlayer().getTitleTextView().setText((String) objects[0]);
//            }
//
//            @Override
//            public void onAutoComplete(String url, Object... objects) {
//                super.onAutoComplete(url, objects);
//                curPlayer = null;
//                isPlay = false;
//                isFull = false;
//                if (getListNeedAutoLand()) {
//                    NaturalChildAdapter.this.onAutoComplete();
//                }
//            }
//        });
    }

    //========================== 视频播放回调 =================================
    private GSYSampleCallBack mGSYSampleCallBack = new GSYSampleCallBack() {
        @Override
        public void onPrepared(String url, Object... objects) {
            Debuger.printfError("***** onPrepared **** " + objects[0]);
            Debuger.printfError("***** onPrepared **** " + objects[1]);
            super.onPrepared(url, objects);
            //开始播放了才能旋转和全屏
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

        }
    };

    /**
     * 全屏幕按键处理
     */
    private void resolveFullBtn(final StandardGSYVideoPlayer standardGSYVideoPlayer) {
        if (getListNeedAutoLand() && orientationUtils != null) {
            resolveFull();
        }
        standardGSYVideoPlayer.startWindowFullscreen(mContext, false, true);
    }

    /**
     * 列表时是否需要支持重力旋转
     *
     * @return 返回true为支持列表重力全屏
     */
    public boolean getListNeedAutoLand() {
        return true;
    }

    private void initOrientationUtils(StandardGSYVideoPlayer standardGSYVideoPlayer, boolean full) {
        orientationUtils = new OrientationUtils((Activity) mContext, standardGSYVideoPlayer);
        //是否需要跟随系统旋转设置
        //orientationUtils.setRotateWithSystem(false);
        orientationUtils.setEnable(false);
        orientationUtils.setIsLand((full) ? 1 : 0);
    }

    private void resolveFull() {
        if (getListNeedAutoLand() && orientationUtils != null) {
            //直接横屏
            orientationUtils.resolveByClick();
        }
    }

    private void onQuitFullscreen() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    public void onAutoComplete() {
        if (orientationUtils != null) {
            orientationUtils.setEnable(false);
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
        isPlay = false;
    }

    public void onPrepared() {
        if (orientationUtils == null) {
            return;
        }
        //开始播放了才能旋转和全屏
        orientationUtils.setEnable(true);
    }

    public void onConfigurationChanged(Activity activity, Configuration newConfig) {
        //如果旋转了就全屏
        if (isPlay && curPlayer != null && orientationUtils != null) {
            curPlayer.onConfigurationChanged(activity, newConfig, orientationUtils, false, true);
        }
    }

    public OrientationUtils getOrientationUtils() {
        return orientationUtils;
    }


    public void onBackPressed() {
        if (orientationUtils != null) {
            orientationUtils.backToProtVideo();
        }
    }

    public void onDestroy() {
        if (isPlay && curPlayer != null) {
            curPlayer.getCurrentPlayer().release();
        }
        if (orientationUtils != null) {
            orientationUtils.releaseListener();
            orientationUtils = null;
        }
    }

}
