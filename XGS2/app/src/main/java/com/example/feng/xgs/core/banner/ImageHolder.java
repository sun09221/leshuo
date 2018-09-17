package com.example.feng.xgs.core.banner;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.feng.xgs.core.GlideApp;

/**
 * Created by feng on 2017/12/20.
 */

public class ImageHolder implements Holder<String>{

    private AppCompatImageView mImageView = null;

    @Override
    public View createView(Context context) {
        mImageView = new AppCompatImageView(context);
        return mImageView;
    }

    @Override
    public void UpdateUI(Context context, int position, String data) {
        GlideApp.with(context)
                .load(data)
//                .error(R.mipmap.icon_goods_defalut)
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .dontAnimate()
                .centerCrop()
                .into(mImageView);
    }


}
