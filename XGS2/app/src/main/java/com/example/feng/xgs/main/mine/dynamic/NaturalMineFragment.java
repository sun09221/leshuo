package com.example.feng.xgs.main.mine.dynamic;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.feng.xgs.R;
import com.example.feng.xgs.base.fragments.EmptyFragment;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.base.recycler.entity.EntityKeys;
import com.example.feng.xgs.config.key.ContentKeys;
import com.example.feng.xgs.config.key.ParameterKeys;
import com.example.feng.xgs.config.key.UrlKeys;
import com.example.feng.xgs.core.net.RestClient;
import com.example.feng.xgs.core.net.RestClientBuilder;
import com.example.feng.xgs.core.net.callback.IError;
import com.example.feng.xgs.core.net.callback.ISuccess;
import com.example.feng.xgs.main.move.MineNaturalChildAdapter;
import com.example.feng.xgs.main.move.NaturalChildDataConverter;
import com.example.feng.xgs.main.move.detail.DynamicDetailActivity;
import com.example.feng.xgs.main.move.detail.DynamicVideoDetailActivity;
import com.example.feng.xgs.main.move.handler.LikeHandler;
import com.example.feng.xgs.main.move.reward.GiveARewardDialog;
import com.example.feng.xgs.main.nearby.detail.NearbyDetailActivity;
import com.example.feng.xgs.utils.file.SharedPreferenceUtils;
import com.example.feng.xgs.utils.manager.LogUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;

/**
 * Created by feng on 2018/4/27 0027.
 * 本色子fragment
 */
public class NaturalMineFragment extends EmptyFragment
        implements BaseQuickAdapter.OnItemChildClickListener,
        MineNaturalChildAdapter.IOnItemImagePreviewClickListener {

    public static final int RC_PHOTO_PREVIEW = 2;
    private MineNaturalChildAdapter mAdapter;
    private NaturalChildDataConverter mDataConverter;
    private int mPageNumber = 1;
    private boolean isPause;
    private LinearLayoutManager mManager;

    public static NaturalMineFragment create(int type, String circleId) {
        Bundle args = new Bundle();
        args.putInt(ContentKeys.ACTIVITY_TYPE, type);
        args.putString(ContentKeys.ACTIVITY_ID_ONLY, circleId);
        NaturalMineFragment fragment = new NaturalMineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.rv_base)
    RecyclerView mRecyclerView;
    @BindView(R.id.refresh_base)
    SwipeRefreshLayout mRefresh;

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
        setRecyclerVideo();
        loadData();
    }

    private void initRecyclerView() {
        mManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mManager);
        addDivider(10);
        mDataConverter = new NaturalChildDataConverter();
        mAdapter = new MineNaturalChildAdapter(mDataConverter.ENTITIES);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemImagePreviewListener(this);
    }

    private void loadData() {
        Bundle args = getArguments();
        if (args == null) {
            return;
        }
        int type = args.getInt(ContentKeys.ACTIVITY_TYPE);

        String mUrl = null;
        Map<String, String> map = new HashMap<>();
        String circleid = args.getString(ContentKeys.ACTIVITY_ID_ONLY);
        LogUtils.i("circleid", circleid);
        map.put(ParameterKeys.PEOPLE_ID,circleid);
        mUrl = UrlKeys.PERSON_DYNAMIC;
        map.put(ParameterKeys.TYPE, type + "");
        String raw = JSON.toJSONString(map);
        RestClientBuilder builder = RestClient.builder();

        builder.url(mUrl)
                .raw(raw)
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
                        LogUtils.d("error");
                        mRefresh.setRefreshing(false);
                        mAdapter.setEmptyView(emptyView);

                    }
                })
                .build().post();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position) {
        final BaseEntity entity = mAdapter.getData().get(position);
        String peopleId = entity.getField(EntityKeys.PEOPLE_ID);
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.tv_natural_item_give_a_reward:
                GiveARewardDialog.create(getActivity()).beginShow(peopleId);
                break;
            case R.id.iv_natural_item_head:
                Intent intent1 = new Intent(getActivity(), NearbyDetailActivity.class);
                intent1.putExtra(ContentKeys.ACTIVITY_ID_ONLY, SharedPreferenceUtils.getPeopleId());
                getActivity().startActivity(intent1);
                break;
//            case R.id.tv_natural_item_attention:
//                AttentionHandler.create(getActivity()).attention(peopleId, new AttentionHandler.IAttentionListener() {
//                    @Override
//                    public void onAttention() {
//                        TextView textView = (TextView) view;
//                        textView.setText("已关注");
//                        textView.setCompoundDrawables(null, null, null, null);
//                    }
//                });
//                break;
            case R.id.tv_natural_item_leave_message:
                if (entity.getItemType()==21){
                    //               SendCommentDialog.create(getActivity()).beginShow();
                    intent = new Intent(getActivity(), DynamicDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                    LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
                    String itemData = JSON.toJSONString(linkedHashMap);
                    LogUtils.d("linkedHashMap: " + itemData);
                    intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                    startActivity(intent);
                }else {
                    //               SendCommentDialog.create(getActivity()).beginShow();
                    intent = new Intent(getActivity(), DynamicVideoDetailActivity.class);
                    intent.putExtra(ContentKeys.ACTIVITY_ID_ONLY, entity.getField(EntityKeys.DYNAMIC_ID));
//                        intent.putExtra(ContentKeys.ACTIVITY_PEOPLE_ID, entity.getField(EntityKeys.PEOPLE_ID));

                    LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) entity.getFields();
                    String itemData = JSON.toJSONString(linkedHashMap);
                    LogUtils.d("linkedHashMap: " + itemData);
                    intent.putExtra(ContentKeys.ACTIVITY_INFO, itemData);
                    startActivity(intent);
                }

                break;
            case R.id.tv_natural_item_like_image:
                String dynamicId = entity.getField(EntityKeys.DYNAMIC_ID);
                LikeHandler.create(getActivity()).like(peopleId, dynamicId, new LikeHandler.ILikeListener() {
                    @Override
                    public void onLike() {
                        ImageView textView = (ImageView) view;
                        textView.setSelected(true);
                        entity.setField(EntityKeys.TYPE_LIKE,"1");
                        Animation animation;
                        animation= AnimationUtils.loadAnimation(getContext(),R.anim.myscale);
                        textView.startAnimation(animation);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                try {
                                    mAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                }
                                try {
                                    mAdapter.notifyDataSetChanged();
                                }catch (Exception e){
                                }
                            }
                        }, 1000);


                    }
                });

                break;
            default:
                break;
        }

//        BGARecyclerViewAdapter

    }


    @Override
    public void onPreview(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");
        BGAPhotoPreviewActivity.IntentBuilder photoPreviewIntentBuilder = new BGAPhotoPreviewActivity.IntentBuilder(getActivity())
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


    private void setRecyclerVideo() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int firstVisibleItem, lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = mManager.findFirstVisibleItemPosition();
                lastVisibleItem = mManager.findLastVisibleItemPosition();
                //大于0说明有播放
                if (GSYVideoManager.instance().getPlayPosition() >= 0) {
                    //当前播放的位置
                    int position = GSYVideoManager.instance().getPlayPosition();
                    //对应的播放列表TAG
                    if (GSYVideoManager.instance().getPlayTag().equals(mAdapter.TAG_VIDEO)
                            && (position < firstVisibleItem || position > lastVisibleItem)) {

                        //如果滑出去了上面和下面就是否，和今日头条一样
                        //是否全屏
                        if (!GSYVideoManager.isFullState(getActivity())) {
                            GSYVideoManager.releaseAllVideos();
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }


//    @Override
//    public void onBackPressed() {
//        if (GSYVideoManager.backFromWindowFull(this)) {
//            return;
//        }
//        super.onBackPressed();
//    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
        isPause = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        GSYVideoManager.onResume();
        isPause = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        GSYVideoManager.releaseAllVideos();
        if (mAdapter != null) {
            mAdapter.onDestroy();
        }
    }


    /********************************为了支持重力旋转********************************/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mAdapter != null && mAdapter.getListNeedAutoLand() && !isPause) {
            mAdapter.onConfigurationChanged(getActivity(), newConfig);
        }
    }

    public void onBackPress() {
        //为了支持重力旋转
        onBackPressAdapter();
        if (GSYVideoManager.backFromWindowFull(getActivity())) {
            return;
        }
    }

    private void onBackPressAdapter() {
        //为了支持重力旋转
        if (mAdapter != null && mAdapter.getListNeedAutoLand()) {
            mAdapter.onBackPressed();
        }

        if (GSYVideoManager.backFromWindowFull(getActivity())) {
            return;
        }
    }
}


