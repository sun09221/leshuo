package com.example.feng.xgs.base.recycler.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by feng on 2017/12/21.
 */

public abstract class BaseBeanAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {


    public BaseBeanAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
        openLoadAnimation();
        isFirstOnly(true);
    }

//    public void loadImg(String imgUrl, ImageView view){
//        Glide.with(mContext)
//                .load(imgUrl)
//                .error(R.mipmap.icon_img_default)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
//                .centerCrop()
//                .dontAnimate()
//                .into(view);
//    }

//    public void loadImg(String imgUrl, ImageView view, int drawable){
//        Glide.with(mContext)
//                .load(imgUrl)
//                .error(drawable)
//                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
//                .centerCrop()
//                .dontAnimate()
//                .into(view);
//    }

}
