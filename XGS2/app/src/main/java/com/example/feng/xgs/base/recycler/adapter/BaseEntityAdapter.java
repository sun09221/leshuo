package com.example.feng.xgs.base.recycler.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.feng.xgs.base.recycler.entity.BaseEntity;
import com.example.feng.xgs.core.GlideApp;

import java.util.List;

/**
 * Created by feng on 2017/12/4.
 */

public abstract class BaseEntityAdapter extends BaseQuickAdapter<BaseEntity, BaseViewHolder>{


    public BaseEntityAdapter(@LayoutRes int layoutResId, @Nullable List<BaseEntity> data) {
        super(layoutResId, data);
        openLoadAnimation();
        isFirstOnly(true);
//        setLoadMoreView(new CustomLoadMoreView());
    }



    public void loadImg(String imgUrl, ImageView view){
        GlideApp.with(mContext)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .dontAnimate()
                .into(view);
    }
    public void loadImg1(String imgUrl, ImageView view){
        GlideApp.with(mContext)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .dontAnimate()
                .into(view);
    }
    public void loadImg(String imgUrl, ImageView view, int drawable){
        GlideApp.with(mContext)
                .load(imgUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .centerCrop()
                .error(drawable)
                .dontAnimate()
                .into(view);
    }
}
